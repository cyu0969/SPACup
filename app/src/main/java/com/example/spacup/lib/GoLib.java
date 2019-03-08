package com.example.spacup.lib;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

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
}
