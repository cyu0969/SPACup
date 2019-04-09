package com.example.spacup.lib;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.spacup.MainActivity;
import com.example.spacup.MypageActivity;
import com.example.spacup.SpecupInfoActivity;

// 현재 화면에서 다른 화면으로 이동하기 위한 객체
public class GoLib {

    public final String TAG = GoLib.class.getSimpleName();
    private volatile static GoLib instance;

    public static GoLib getInstance() {
        if (instance == null) {
            synchronized (GoLib.class) {
                if (instance == null) {
                    instance = new GoLib();
                }
            }
        }
        return instance;
    }

    // 프래그먼트를 이용하기 위한 메소드
    public void goFragment(FragmentManager fragmentManager, int containerViewId,
                           Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
    }

    // 프래그먼트에서 뒤로가기를 하기 위한 메소드
    public void goFragmentBack(FragmentManager fragmentManager, int containerViewId,
                               Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .addToBackStack(null)
                .commit();
    }

    // 이전 프래그먼트로 가기위한 메소드
    public void goBackFragment(FragmentManager fragmentManager) {
        fragmentManager.popBackStack();
    }

    public void goMypageActivity(Context context) {
        Intent intent = new Intent(context, MypageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void goSpecupInfoActivitty(Context context, int infoSeq) {
        Intent intent = new Intent(context, SpecupInfoActivity.class);
        intent.putExtra(SpecupInfoActivity.INFO_SEQ, infoSeq);
        context.startActivity(intent);
    }
}
