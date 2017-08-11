package com.gather_excellent_help.utils;

/**
 * Created by Dapeng Fang on 2017/8/9.
 */

public class Check {
    public static final String c_phone = "[1][358]\\d{9}";
    public static final String c_password = "^[a-zA-z][a-zA-z0-9]{5,11}$";
    public static final String c_mail = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
    public static final String p_sample = "([\\d])\\1{2,}";
    public static final String p_simple = "(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){3,}|(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){3,})\\d";
}
