package com.gather_excellent_help.utils.imageutils;

import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;

import java.lang.reflect.Field;

/**
 * Created by wuxin on 2017/7/4.
 * 图片尺寸工具类
 */

public class ImageSizeUtil {

    /**
     * 根据图片需求的宽和高，以及图片实际的宽和高，计算出图片的采样比
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int caculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                         int reqHeight){
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;

        if(width>reqWidth || height>reqHeight) {
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(height * 1.0f / reqHeight);
            inSampleSize = Math.max(widthRadio,heightRadio);
        }

        return inSampleSize;
    }

    /**
     * 根据ImageView获取适当的压缩的宽和高
     * @param imageView
     * @return
     */
    public static ImageSize getImageViewSize(ImageView imageView){
        ImageSize imageSize = new ImageSize();
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();

        LayoutParams lp = imageView.getLayoutParams();
        int width = imageView.getWidth();//获取ImageView的实际宽度
        if(width<=0) {
            width = lp.width;
        }
        if(width<=0) {
            width = getImageViewFieldValue(imageView,"mMaxWidth");
        }
        if(width<=0) {
            width = displayMetrics.widthPixels;
        }

        int height = imageView.getHeight();
        if(height<=0) {
            height = lp.height;
        }
        if(height<=0) {
            height = getImageViewFieldValue(imageView,"mMaxHeight");
        }
        if(height<=0) {
            height =displayMetrics.heightPixels;
        }
        imageSize.width = width;
        imageSize.height = height;

        return imageSize;
    }

    //ImageView的宽高的类
    public static class ImageSize{

        int width;//图片的宽
        int height;//图片的高

    }

    /**
     * 通过反射获取ImageVeiw的某个属性值
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(Object object,String fieldName){
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if(fieldValue>0 && fieldValue <Integer.MAX_VALUE) {
                value = fieldValue;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }



}
