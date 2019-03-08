package com.example.spacup.lib;

import android.util.Log;

import com.example.spacup.BuildConfig;

// 기존 로그를 좀 더 편하기 사용하기 위한 메소드로 구성된 객체 파일.
public class MyLog {

    private static boolean enabled = BuildConfig.DEBUG;

    public static void d(String tag, String text) {
        if (!enabled) return;

        Log.d(tag, text);
    }

    public static void d(String text) {
        if (!enabled) return;

        Log.d("tag", text);
    }

    public static void d(String tag, Class<?> cls, String text) {
        if (!enabled) return;

        Log.d(tag, cls.getName() + "." + text);
    }

    public static void e(String tag, String text) {
        if (!enabled) return;

        Log.e(tag, text);
    }
}
