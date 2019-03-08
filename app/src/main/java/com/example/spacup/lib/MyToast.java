package com.example.spacup.lib;

import android.content.Context;
import android.widget.Toast;

// .show() 안쓰기 위해서 정리해 놓음.
public class MyToast {

    public static void s(Context context, int id) {
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
    }

    public static void s(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void l(Context context, int id) {
        Toast.makeText(context, id, Toast.LENGTH_LONG).show();
    }

    public static void l(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
