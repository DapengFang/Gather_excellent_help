package com.gather_excellent_help.utils;

import android.content.Context;
import android.content.Intent;

import com.gather_excellent_help.ui.activity.error.NetErrorActivity;
import com.gather_excellent_help.utils.enctry.Des2;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

/**
 * Created by Dapeng Fang on 2018/2/1.
 */

public class EncryptNetUtil {

    public static final String DES_KEY = "12378945";

    /**
     * @return 随机数
     */
    public static Integer getRandomNumber() {
        Random random = new Random(Integer.MAX_VALUE);
        return Math.abs(random.nextInt());
    }

    /**
     * @return 时间戳
     */
    public static long getTimesTamp() {
        return System.currentTimeMillis();
    }

    public static String getMD5EncryptHeader(long timestamp, Integer random, String id, String token,
                                             String desData) throws Exception {
        String signature = String.valueOf(timestamp) + String.valueOf(random) + id + token + "data=" + desData;
        char[] chars = signature.toCharArray();
        signature = sort(chars, chars.length);
        return EncryptUtil.getMd5Value(signature).toUpperCase();
    }

    public static String sort(char str[], int n) {
        int i, j;
        char ch;
        for (i = 0; i < n; i++) {
            for (j = i; j < n; j++) {
                if (str[i] > str[j]) {
                    ch = str[i];
                    str[i] = str[j];
                    str[j] = ch;
                }
            }
        }
        return String.valueOf(str);
    }

    /**
     * 获取请求参数des加密后的字符串
     *
     * @param map
     * @return
     */
    public static String getBase64String(Map<String, String> map) throws Exception {
        String json = new Gson().toJson(map);
        LogUtil.e("请求参数 = " + json);
        String desData = Des2.EncryptAsDoNet(json, DES_KEY);
        LogUtil.e("desData = " + desData);
        return desData;
    }

    /**
     * 联网到Server服务器请求
     *
     * @param map
     * @param userLogin
     * @param netUtils
     * @param url
     */
    public static void netToServerEnctry(Context context, Map<String, String> map, String userLogin, NetUtil netUtils, String url) {
        try {
            String data = EncryptNetUtil.getBase64String(map);
            Integer randomNumber = EncryptNetUtil.getRandomNumber();
            long timesTamp = EncryptNetUtil.getTimesTamp();
            String token = CacheUtils.getString(context, CacheUtils.LOGIN_TOKEN, "");
            String signature = EncryptNetUtil.getMD5EncryptHeader(timesTamp, randomNumber, userLogin, token, data);
            netUtils.okHttp2ServerEnctryHeader(url, data, String.valueOf(timesTamp), String.valueOf(randomNumber), signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取解密后的Response
     *
     * @param response
     * @return
     * @throws Exception
     */
    public static String getDecryptResponse(String response) throws Exception {
        return Des2.DecryptDoNet(response, EncryptNetUtil.DES_KEY);
    }

    /**
     * 跳转到网络连接出错的界面
     *
     * @param context
     */
    public static void startNeterrorPage(Context context) {
        Intent intent = new Intent(context, NetErrorActivity.class);
        context.startActivity(intent);
    }

}
