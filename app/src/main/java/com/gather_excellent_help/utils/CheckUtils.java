package com.gather_excellent_help.utils;

import android.text.TextUtils;

/**
 * Created by wuxin on 2017/7/4.
 * 检查手机号、邮箱、密码是否符合正则表达式规则
 */

public class CheckUtils {
    /**
     * 检查手机号是否合格
     * @param mobiles
     * @return
     */
    public static boolean checkPhone(String mobiles) {
        String telRegex = "[1][358]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 检查密码是否合格
     * @param pwd
     * @return
     */
    public static boolean checkPwd(String pwd) {
        String telRegex = "^[a-zA-z][a-zA-z0-9]{5,11}$";
        if (TextUtils.isEmpty(pwd))
            return false;
        else return pwd.matches(telRegex);
    }

    /**
     * 检查邮箱是否合格
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        String emailRegex="^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        if(TextUtils.isEmpty(email))
            return false;
        else
            return email.matches(emailRegex);
    }
}
