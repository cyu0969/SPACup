package com.example.spacup.lib;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.example.spacup.R;

// 다이얼로그와 관련된 메소드로 구성된 라이브러리
public class DialogLib {

    public final String TAG = DialogLib.class.getSimpleName();
    private volatile static DialogLib instance;

    public static DialogLib getInstance() {
        if (instance == null) {
            synchronized (DialogLib.class) {
                if (instance == null) {
                    instance = new DialogLib();
                }
            }
        }
        return instance;
    }

    // 즐겨찾기 추가 다이얼로그 화면출력
    public void showKeepInsertDialog(Context context, final Handler handler,
                                     final int memberSeq, final int infoSeq) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.keep_insert)
                .setMessage(R.string.keep_insert_message)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FavoriteLib.getInstance().insertKeep(handler, memberSeq, infoSeq);
                    }
                })
                .show();
    }

    // 즐겨찾기 삭제 다이얼로그 화면출력
    public void showKeepDeleteDialog(Context context, final Handler handler,
                                     final int memberSeq, final int infoSeq) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.keep_delete)
                .setMessage(R.string.keep_delete_message)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FavoriteLib.getInstance().deleteKeep(handler, memberSeq, infoSeq);
                    }
                })
                .show();
    }
}
