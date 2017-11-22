package com.gather_excellent_help.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dapeng Fang on 2017/8/9.
 */

public class Check {
    public static final String c_phone = "[1][358]\\d{9}";
    public static final String c_password = "^[a-zA-z][a-zA-z0-9]{5,11}$";
    public static final String c_mail = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
    public static final String p_sample = "([\\d])\\1{2,}";
    public static final String p_simple = "(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){3,}|(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){3,})\\d";

    /**
     * 手机号号段校验，
     * 第1位：1；
     * 第2位：{3、4、5、6、7、8}任意数字；
     * 第3—11位：0—9任意数字
     *
     * @param value
     * @return
     */
    public static boolean isTelPhoneNumber(String value) {
        if (value != null && value.length() == 11) {
            Pattern pattern = Pattern.compile("^1[2|3|4|5|6|7|8|9][0-9]\\d{8}$");
            Matcher matcher = pattern.matcher(value);
            return matcher.matches();
        }
        return false;
    }

    /**
     * 验证输入的名字是否为“中文”或者是否包含“·”
     */
    public static boolean isLegalName(String name) {
        if (name.contains("·") || name.contains("•")) {
            if (name.matches("^[\\u4e00-\\u9fa5]+[·•][\\u4e00-\\u9fa5]+$")) {
                return true;
            } else {
                return false;
            }
        } else {
            if (name.matches("^[\\u4e00-\\u9fa5]+$")) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 验证输入的是否为邮箱
     */
    public static boolean isEmail(String name) {
        if (name != null && name.length() > 0) {
            if (name.matches("\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
