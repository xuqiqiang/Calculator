package com.snailstudio.library.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();

    public static String filePathToCloudPath(String filePath) {
        if (filePath.startsWith(Cache.rootName)) {
            return filePath.substring(Cache.rootName.length());
        } else {
            Log.e(TAG, "filePathToCloudPath error!");
            return null;
        }
    }

    public static String cloudPathToFilePath(String cloudPath) {
        return Cache.rootName + cloudPath;
    }

    public static int getLevel(File file) {
        if (TextUtils.equals(file.getPath(), Cache.rootName))
            return 0;
        return filePathToCloudPath(file.getParent()).split("/").length;
    }

    public static boolean deleteCache(Context context, String[] cacheName,
            String fileName) {
        File f;
        boolean isHaveSDCard = false;
        if (Environment.getExternalStorageState() != null
                && !Environment.getExternalStorageState().equals("removed")) {
            isHaveSDCard = true;
        }

        if (isHaveSDCard) {
            File root = new File(Cache.rootName);// android.os.Environment.getExternalStorageDirectory();

            if (!root.exists()) {// 目录存在返回true
                return true;
            }

            String pathename = Cache.rootName;

            if (cacheName != null) {
                int i, length = cacheName.length;
                for (i = 0; i < length; i++) {
                    pathename += File.separator + cacheName[i];
                    File path = new File(pathename);// android.os.Environment.getExternalStorageDirectory();

                    if (!path.exists()) {
                        return true;
                    }
                }
            }

            f = new File(pathename + File.separator + fileName);// 创建文件
            if (!f.exists()) {
                return true;
            }

            return f.delete();

        } else {
            return context.deleteFile(fileName);
        }
    }

}