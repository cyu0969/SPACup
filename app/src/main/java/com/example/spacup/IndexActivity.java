package com.example.spacup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.spacup.item.MemberInfoItem;
import com.example.spacup.lib.EtcLib;
import com.example.spacup.lib.MyLog;
import com.example.spacup.lib.RemoteLib;
import com.example.spacup.lib.StringLib;
import com.example.spacup.remote.RemoteService;
import com.example.spacup.remote.ServiceGenerator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 시작 액티비티, 사용자 정보를 조회하고 메인으로 넘어갈지 회원가입으로 넘어갈지 결정함.
public class IndexActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    Context context;

    // 인터넷에 연결되어 있는지 확인
    // 인터넷에 연결이 안되어 있으면 showNOService 메소드로 이동
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        context = this;

        if (!RemoteLib.getInstance().isConnected(context)) {
            showNoService();
            return;
        }

        startMain();
    }

    // 인터넷에 연결이 안되어 있을때 동작하는 메소드로 서비스 종료 메시지 창을 보여줌.
    private void showNoService() {
        TextView messageText = (TextView) findViewById(R.id.message);
        messageText.setVisibility(View.VISIBLE);

        Button closeButton = (Button) findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        closeButton.setVisibility(View.VISIBLE);
    }

    // 앱이 실행하고 이상이 없으면 1초후에 startTask메소드 실행
    // 서버에 사용자 정보 조회 요청
    @Override
    protected void onStart() {
        super.onStart();

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startTask();
            }
        }, 1000);
    }

    // 현재 폰의 전화번화 동일한 번호가 있는지 확인하기 위한 메소드로
    // 확인하기 위해 selectMemberInfo 메소드 불러옴
    public void startTask() {
        String phone = EtcLib.getInstance().getPhoneNumber(this);

        selectMemberInfo(phone);
    }

    // 전화번호를 가지고 사용자 정보를 조회
    // 있으면 setMemberInfoItem 메소드 호출
    // 업으면 goProfileActivity 메소드 호출
    public void selectMemberInfo(String phone) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<MemberInfoItem> call = remoteService.selectMemberInfo(phone);
        call.enqueue(new Callback<MemberInfoItem>() {
            @Override
            public void onResponse(Call<MemberInfoItem> call, Response<MemberInfoItem> response) {
                MemberInfoItem item = response.body();

                if (response.isSuccessful() && !StringLib.getInstance().isBlank(item.name)) {
                    MyLog.d(TAG, "success " + response.body().toString());
                    setMemberInfoItem(item);
                } else {
                    MyLog.d(TAG, "not success");
                    goProfileActivity(item);
                }
            }

            @Override
            public void onFailure(Call<MemberInfoItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    // 정보가 조회되면 사용자의 정보를 앱에 보내주는 역활
    private void setMemberInfoItem(MemberInfoItem item) {
        ((MyApp) getApplicationContext()).setMemberInfoItem(item);

        startMain();
    }

    // 사용자 정보가 없으면 실행되는 메소드
    // insertMemberPhone() 메소드를 실행해 전화번호를 서버에 저장하고
    // MainActivity를 실행한 후 ProfileActivity를 실행
    private void goProfileActivity(MemberInfoItem item) {
        if (item == null || item.seq <= 0) {
            insertMemberPhone();
        }

        Intent intent = new Intent(IndexActivity.this, MainActivity.class);
        startActivity(intent);

        Intent intent2 = new Intent(this, MypageActivity.class);
        startActivity(intent2);

        finish();
    }

    // 현재 폰의 전화번호를 서버에 저장시키는 메소드
    private void insertMemberPhone() {
        String phone = EtcLib.getInstance().getPhoneNumber(context);
        RemoteService remoteService =
                ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertMemberPhone(phone);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    MyLog.d(TAG, "success insert id " + response.body().toString());
                } else {
                    int statusCode = response.code();

                    ResponseBody errorBody = response.errorBody();

                    MyLog.d(TAG, "fail " + statusCode + errorBody.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
            }
        });
    }

    // MainActivity를 실행하고 현재 액티비티를 종료
    public void startMain() {
        Intent intent = new Intent(IndexActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }

}
