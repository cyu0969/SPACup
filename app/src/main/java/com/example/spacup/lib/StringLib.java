package com.example.spacup.lib;

import android.content.Context;

import com.example.spacup.R;

public class StringLib {

    public final String TAG = StringLib.class.getSimpleName();

    private volatile static StringLib instance;

    protected StringLib() {}

    public static StringLib getInstance() {
        if (instance == null) {
            synchronized (StringLib.class) {
                if (instance == null) {
                    instance = new StringLib();
                }
            }
        }
        return instance;
    }

    // 문자열이 null이거나 "" 인지를 파악하는 기능
    public boolean isBlank(String str) {
        if (str == null || str.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    // 문자열를 지정된 길이로 잘라서 반환
    public String getSubString(Context context, String str, int max) {
        if (str != null && str.length() > max) {
            return str.substring(0, max) + context.getResources().getString(R.string.skip_string);
        } else {
            return str;
        }
    }
}