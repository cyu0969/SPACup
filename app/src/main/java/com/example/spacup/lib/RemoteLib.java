package com.example.spacup.lib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.example.spacup.remote.RemoteService;
import com.example.spacup.remote.ServiceGenerator;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 네트워크가 연결되어 있는지를 확인하고 원격 서버에 파일을 업로드하는 기능을 담고 있는 객체 파일.
public class RemoteLib {

    public static final String TAG = RemoteLib.class.getSimpleName();

    private volatile static RemoteLib instance;

    public static RemoteLib getInstance() {
        if (instance == null) {
            synchronized (RemoteLib.class) {
                if (instance == null) {
                    instance = new RemoteLib();
                }
            }
        }
        return instance;
    }

    // 네트워크와 연결 여부를 확인하는 기능.
    public boolean isConnected(Context context) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();

            if (info != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    // 사용자 프로필 아이콘을 서버에 업로드하는 가능.
    public void uploadMemberIcon(int memberSeq, File file) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody memberSeqBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), "" + memberSeq);

        Call<ResponseBody> call =
                remoteService.uploadMemberIcon(memberSeqBody, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                MyLog.d(TAG, "uploadMemberIcon success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MyLog.e(TAG, "uploadMemberIcon fail");
            }
        });
    }

    // 자격증과 관련된 이미지를 서버에 업로드 하는 기능
    public void uploadFoodImage(int infoSeq, String imageMemo, File file, final Handler handler) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody infoSeqBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), "" + infoSeq);
        RequestBody imageMemoBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), imageMemo);

        Call<ResponseBody> call =
                remoteService.uploadCertificateImage(infoSeqBody, imageMemoBody, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                MyLog.d(TAG, "uploadCertificateImage success");
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MyLog.e(TAG, "uploadCertificateImage fail");
            }
        });
    }
}
