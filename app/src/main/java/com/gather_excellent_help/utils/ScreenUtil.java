package com.gather_excellent_help.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by wuxin on 2017/7/4.
 * 获取屏幕宽高的工具类
 */

public class ScreenUtil {

    /**
     * @param context
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context){
        if(null == context) {
            throw  new RuntimeException("context不能为空！");
        }
        DisplayMetrics dm;
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * @param context
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context context){
        if(null == context) {
            throw  new RuntimeException("context不能为空！");
        }
        DisplayMetrics dm;
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
