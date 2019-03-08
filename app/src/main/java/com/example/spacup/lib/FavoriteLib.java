package com.example.spacup.lib;

import android.os.Handler;

import com.example.spacup.remote.RemoteService;
import com.example.spacup.remote.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteLib {

    public final String TAG = FavoriteLib.class.getSimpleName();
    private volatile static FavoriteLib instance;

    public static FavoriteLib getInstance() {
        if (instance == null) {
            synchronized (FavoriteLib.class) {
                if (instance == null) {
                    instance = new FavoriteLib();
                }
            }
        }
        return instance;
    }

    // 즐겨찾기 추가를 서버에 요청하는 기능.
    public void insertKeep(final Handler handler, int memberSeq, final int infoSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        // handler 결과를 응답하는 핸들러
        // memberseq 사용자 일련번호
        // infoseq 자격증 정보 일련번호

        Call<String> call = remoteService.insertKeep(memberSeq, infoSeq);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    MyLog.d(TAG, "insertFavorite " + response);
                    handler.sendEmptyMessage(infoSeq);
                } else { // 등록 실패
                    MyLog.d(TAG, "response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
            }
        });
    }

    // 즐겨찾기 삭제를 서버에 요청하는 기능
    public void deleteKeep(final Handler handler, int memberSeq, final int infoSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.deleteKeep(memberSeq, infoSeq);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    MyLog.d(TAG, "deleteFavorite " + response);
                    handler.sendEmptyMessage(infoSeq);
                } else { // 등록 실패
                    MyLog.d(TAG, "response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
            }
        });
    }
}
