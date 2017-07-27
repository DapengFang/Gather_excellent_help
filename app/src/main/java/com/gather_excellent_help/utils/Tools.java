package com.gather_excellent_help.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by wuxin on 2017/7/11.
 */

public class Tools {

    /**
     * 用户是否登录
     * @param context
     * @return
     */
    public static boolean isLogin(Context context){
        return CacheUtils.getBoolean(context,CacheUtils.LOGIN_STATE,false);
    }


    /**
     * 用户是否登录
     * @param context
     * @return
     */
    public static boolean isBindTao(Context context){
        return CacheUtils.getBoolean(context,CacheUtils.BIND_STATE,false);
    }

    /**
     * 用户登录后的标识
     * @param context
     * @return
     */
    public static String getUserLogin(Context context){
        return CacheUtils.getString(context,CacheUtils.LOGIN_VALUE,"");
    }
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //讲Bitmap转为Base64的方法。
    @SuppressLint("NewApi")
    public static String BitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null && !bitmap.isRecycled()) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 对图片大小进行压缩
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    /**
     * 设置部分字体颜色为红色
     */
    public static void setPartTextColor(TextView tv,String str,String s) {
        int end = str.indexOf(s);
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(Color.RED),end, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(style);
    }
}
