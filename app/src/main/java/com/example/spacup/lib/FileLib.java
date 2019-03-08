package com.example.spacup.lib;

import android.content.Context;
import android.os.Environment;

import java.io.File;

// 앱과 관련된 파일을 좀 더 편하게 다루기 위한 객체
public class FileLib {

    public static final String TAG = FileLib.class.getSimpleName();
    private volatile static FileLib instance;

    public static FileLib getInstance() {
        if (instance == null) {
            synchronized (FileLib.class) {
                if (instance == null) {
                    instance = new FileLib();
                }
            }
        }
        return instance;
    }

    // 파일을 저장할 수 있는 디렉토리를 반환
    private File getFileDir(Context context) {
        String state = Environment.getExternalStorageState();
        File filesDir;

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            filesDir = context.getExternalFilesDir(null);
        } else {
            filesDir = context.getFilesDir();
        }

        return filesDir;
    }

    // 프로필 아이콘 파일을 저장할 수 있는 디렉토리를 반환
    public File getProfileIconFile(Context context, String name) {
        return new File(FileLib.getInstance().getFileDir(context), name + ".png");
    }

    // 이미지 파일을 저장할 수 있는 디렉토리를 반환
    public File getImageFile(Context context, String name) {
        return new File(FileLib.getInstance().getFileDir(context), name + ".png");
    }
}
