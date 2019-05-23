package com.example.spacup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.spacup.item.MemberInfoItem;
import com.example.spacup.lib.FileLib;
import com.example.spacup.lib.MyLog;
import com.example.spacup.lib.RemoteLib;
import com.example.spacup.lib.StringLib;
import com.example.spacup.remote.RemoteService;
import com.squareup.picasso.Picasso;


import java.io.File;

// 프로필 아이콘을 등록하는 액티비티
public class MypageIconActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int CROP_FROM_ALBUM = 3;

    Context context;

    ImageView mypageIconImage;

    MemberInfoItem memberInfoItem;

    File mypageIconFile;
    String mypageIconFilename;

    // 화면을 구성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_icon);

        context = this;

        memberInfoItem = ((MyApp) getApplication()).getMemberInfoItem();

        setToolbar();
        setView();
        setMypageIcon();
    }

    // 툴바를 설정
    private void setToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.profile_setting);
        }
    }

    // 화면을 설정
    public void setView() {
        mypageIconImage = (ImageView) findViewById(R.id.profile_icon);

        Button albumButton = (Button) findViewById(R.id.album);
        albumButton.setOnClickListener(this);

        Button cameraButton = (Button) findViewById(R.id.camera);
        cameraButton.setOnClickListener(this);
    }

    // 프로필 아이콘을 설정
    private void setMypageIcon() {
        MyLog.d(TAG, "onResume " +
                RemoteService.MEMBER_ICON_URL + memberInfoItem.memberIconFilename);

        if (StringLib.getInstance().isBlank(memberInfoItem.memberIconFilename)) {
            Picasso.with(this).load(R.drawable.ic_person).into(mypageIconImage);
        } else {
            Picasso.with(this)
                    .load(RemoteService.MEMBER_ICON_URL + memberInfoItem.memberIconFilename)
                    .into(mypageIconImage);
        }
    }

    // 사용자가 선택한 프로필 아이콘을 저장할 파일 이름을 설정
    private void setMypageIconFile() {
        mypageIconFilename = memberInfoItem.seq + "_" + String.valueOf(System.currentTimeMillis());

        mypageIconFile = FileLib.getInstance().getProfileIconFile(context, mypageIconFilename);
    }

    // 프로필 이미지 설정시 앨범 or 카메라 선택을 가능하게 하는 메소드
    @Override
    public void onClick(View v) {
        setMypageIconFile();

        if (v.getId() == R.id.album) {
            getImageFromAlbum();

        } else if (v.getId() == R.id.camera) {
            getImageFromCamera();
        }
    }

    // 오른쪽 상단 메뉴를 구성
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_close:
                finish();
                break;
        }

        return true;
    }

    // 카메라 실행해서 이미지 촬용
    private void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    // 기존 앨범에서 이미지 선택
    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mypageIconFile));
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    // 오른쪽 상단 메뉴를 구성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);
        return true;
    }

    // 이미지를 자르기 위한 메소드
    private Intent getCropIntent(Uri inputUri, Uri outputUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());

        return intent;
    }

    // 메라에서 촬영한 이미지를 프로필 아이콘에 사용할 크기로 자른다.
    private void cropImageFromCamera() {
        Uri uri = Uri.fromFile(mypageIconFile);
        Intent intent = getCropIntent(uri, uri);
        startActivityForResult(intent, CROP_FROM_CAMERA);
    }

    // 카메라 앨범에서 선택한 이미지를 프로필 아이콘에 사용할 크기로 자른다.
    private void cropImageFromAlbum(Uri inputUri) {
        Uri outputUri = Uri.fromFile(mypageIconFile);

        MyLog.d(TAG, "startPickFromAlbum uri " + inputUri.toString());
        Intent intent = getCropIntent(inputUri, outputUri);
        startActivityForResult(intent, CROP_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        MyLog.d(TAG, "onActivityResult " + intent);

        if (resultCode != RESULT_OK) return;

        if (requestCode == PICK_FROM_CAMERA) {
            cropImageFromCamera();

        } else if (requestCode == CROP_FROM_CAMERA) {
            Picasso.with(this).load(mypageIconFile).into(mypageIconImage);
            uploadMypageIcon();

        } else if (requestCode == PICK_FROM_ALBUM && intent != null) {
            Uri dataUri = intent.getData();
            if (dataUri != null) {
                cropImageFromAlbum(dataUri);
            }
        } else if (requestCode == CROP_FROM_ALBUM && intent != null) {
            Picasso.with(this).load(mypageIconFile).into(mypageIconImage);
            uploadMypageIcon();
        }
    }

    // 프로필 아이콘을 서버에 업로드
    private void uploadMypageIcon() {
        RemoteLib.getInstance().uploadMemberIcon(memberInfoItem.seq, mypageIconFile);

        memberInfoItem.memberIconFilename = mypageIconFilename + ".png";
    }
}
