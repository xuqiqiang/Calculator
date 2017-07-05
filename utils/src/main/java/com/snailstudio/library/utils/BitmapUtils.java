package com.snailstudio.library.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import java.io.FileNotFoundException;

public class BitmapUtils {

    private static final String TAG = "BitmapUtils";

    public static boolean isEmpty(Bitmap bitmap) {
        return bitmap == null || bitmap.isRecycled();
    }

    public static void recycle(Bitmap bitmap) {
        // 先判断是否已经回收
        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
        }
        System.gc();
    }

    public static Bitmap getBitmapFromUri(Activity context, Uri uri) {
        return getBitmapFromUri(context, uri, 300);
    }

    /**
     * 通过路径获取系统图片
     * 
     * @param context
     * @param uri
     * @return
     */
    public static Bitmap getBitmapFromUri(Activity context, Uri uri,
            int maxWidth) {
        Bitmap bitmap = null;
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        int dw = 600;
        int dh = 600;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(uri), null, op);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        float wRatio = (float) Math.ceil(op.outWidth / (float) dw);
        float hRatio = (float) Math.ceil(op.outHeight / (float) dh);
        if (wRatio > 1 || hRatio > 1) {
            op.inSampleSize = (int) Math.max(wRatio, hRatio);
        }
        op.inJustDecodeBounds = false;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(uri), null, op);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        if (bitmap.getWidth() > maxWidth)
            bitmap = getNewBitmap(bitmap, maxWidth);

        return bitmap;
    }

    public static Bitmap getNewBitmap(Bitmap bitmap, float newWidth) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例

        float scaleWidth = newWidth / width;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        // 得到新的图片
        try {
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                    true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}