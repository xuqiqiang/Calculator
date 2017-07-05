package com.snailstudio.library.utils;

public class DataUtils {

    public static boolean equals(float a, float b) {
        if (a <= b + 0.000001f && a >= b - 0.000001f)
            return true;
        return false;
    }
}