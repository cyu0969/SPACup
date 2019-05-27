package com.example.spacup;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.spacup.item.CertificateInfoItem;
import com.example.spacup.lib.DialogLib;
import com.example.spacup.lib.MyLog;
import com.example.spacup.lib.StringLib;
import com.example.spacup.remote.RemoteService;
import com.example.spacup.remote.ServiceGenerator;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 즐겨찾기 리스트의 아이템을 처리하는 어댑터

public class SpecupInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    public static final String INFO_SEQ = "INFO_SEQ";

    Context context;

    int memberSeq;
    int certificateinfoSeq;

    CertificateInfoItem item;

    View loadingText;
    ScrollView scrollView;
    ImageView keepImage;

    // 자격증 정보를 보여주기 위해 사용자 정보를 얻고 이를 기반으로 서버에서 자격증 저보를 조회하는 메소드를 호출.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate_info);

        context = this;

        loadingText = findViewById(R.id.loading_layout);

        memberSeq = ((MyApp)getApplication()).getMemberSeq();
        certificateinfoSeq = getIntent().getIntExtra(INFO_SEQ, 0);
        selectCertificateInfo(certificateinfoSeq, memberSeq);

        setToolbar();
    }

    // 툴바를 설정
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
    }

    // 오른쪽 상단 메뉴를 구성
    // 현재는 닫기만 있지만 설정 기능도 추가할 예정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);
        return true;
    }

    // 위에서 설정한 메뉴 기능중 닫기를 클릭했을 때 동작할 메소드 설정.
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
            case R.id.action_close :
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // 서버에서 자격증 정보를 조회
    private void selectCertificateInfo(int certificateinfoSeq, int memberSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<CertificateInfoItem> call = remoteService.selectCertificateInfo(certificateinfoSeq, memberSeq);

        call.enqueue(new Callback<CertificateInfoItem>() {
            @Override
            public void onResponse(Call<CertificateInfoItem> call, Response<CertificateInfoItem> response) {
                CertificateInfoItem infoItem = response.body();

                if (response.isSuccessful() && infoItem != null && infoItem.seq > 0) {
                    item = infoItem;
                    setView();
                    loadingText.setVisibility(View.GONE);
                } else {
                    loadingText.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.loading_text)).setText(R.string.loading_not);
                }
            }

            @Override
            public void onFailure(Call<CertificateInfoItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    // 자격증 정보를 화면에 설정
    private void setView() {
        getSupportActionBar().setTitle(item.name);

        ImageView infoImage = (ImageView) findViewById(R.id.info_image);
        setImage(infoImage, item.imageFilename);

        scrollView = (ScrollView) findViewById(R.id.scroll_view);

        FragmentManager fm = getSupportFragmentManager();

        TextView nameText = (TextView) findViewById(R.id.name);
        if (!StringLib.getInstance().isBlank(item.name)) {
            nameText.setText(item.name);
        }

        keepImage = (ImageView) findViewById(R.id.keep);
        keepImage.setOnClickListener(this);
        if (item.isFavorite) {
            keepImage.setImageResource(R.drawable.ic_keep_on);
        } else {
            keepImage.setImageResource(R.drawable.ic_keep_off);
        }

        TextView description = (TextView) findViewById(R.id.description);
        if (!StringLib.getInstance().isBlank(item.description)) {
            description.setText(item.description);
        } else {
            description.setText(R.string.no_text);
        }
    }

    // 즐겨찾기 버튼을 클릭했을 때의 동작을 정의
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.keep) {
            if (item.isFavorite) {
                DialogLib.getInstance()
                        .showKeepDeleteDialog(context, keepHandler, memberSeq, item.seq);
                keepImage.setImageResource(R.drawable.ic_keep_off);
            } else {
                DialogLib.getInstance()
                        .showKeepInsertDialog(context, keepHandler, memberSeq, item.seq);
                keepImage.setImageResource(R.drawable.ic_keep_on);
            }
        }
    }

    Handler keepHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            item.isFavorite = !item.isFavorite;

            if (item.isFavorite) {
                keepImage.setImageResource(R.drawable.ic_keep_on);
            } else {
                keepImage.setImageResource(R.drawable.ic_keep_off);
            }
        }
    };

    private void setImage(ImageView imageView, String fileName) {
        if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.with(context).load(R.drawable.bg_specup_drawer).into(imageView);
        } else {
            Picasso.with(context).load(RemoteService.IMAGE_URL + fileName).into(imageView);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((MyApp) getApplication()).setCertificateInfoItem(item);
    }

}
