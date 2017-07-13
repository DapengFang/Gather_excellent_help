package com.gather_excellent_help.utils;

import android.content.Context;

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
     * 用户登录后的标识
     * @param context
     * @return
     */
    public static String getUserLogin(Context context){
        return CacheUtils.getString(context,CacheUtils.LOGIN_VALUE,"");
    }
}
