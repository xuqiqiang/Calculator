package com.snailstudio.library.utils;

import android.util.Base64;

public class Base64Utils {

    public static String encode(String str) {
        String str_64 = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        str_64 = str_64.replace("/", "_a");
        return str_64.trim();
    }

    public static String decode(String str_64) {
        str_64 = str_64.replace("_a", "/");
        String str = new String(
                Base64.decode(str_64.getBytes(), Base64.DEFAULT));
        return str.trim();
    }
}