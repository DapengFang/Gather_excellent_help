package com.gather_excellent_help.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by wuxin on 2017/7/10.
 */

public class NetUtil {

    private static final String[] USERIDS = {"userId", "id", "user_id", "Id", "seller_id"};

    /**
     * 请求结果返回接口
     */
    private OnServerResponseListener onServerResponseListener;


    /**
     * 联网请求服务器（post）
     *
     * @return 参数对象
     */
    public void okHttp2Server2(final Context context, String url, Map<String, String> map) {
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        if (map != null) {
            try {
                String user_id = null;
                String data = EncryptNetUtil.getBase64String(map);
                for (int i = 0; i < USERIDS.length; i++) {
                    user_id = map.get(USERIDS[i]);
                    if (user_id != null) {
                        break;
                    }
                }
                if (user_id != null) {
                    Integer randomNumber = EncryptNetUtil.getRandomNumber();
                    long timesTamp = EncryptNetUtil.getTimesTamp();
                    String token = CacheUtils.getString(context, CacheUtils.LOGIN_TOKEN, "");
                    String userLogin = Tools.getUserLogin(context);
                    String signature = EncryptNetUtil.getMD5EncryptHeader(timesTamp, randomNumber, userLogin, token, data);
                    builder.addHeader("timestamp", String.valueOf(timesTamp));
                    builder.addHeader("nonce", String.valueOf(randomNumber));
                    if (signature != null) {
                        builder.addHeader("signature", signature);
                    }
                }
                if (data != null) {
                    builder.addParams("data", data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        onServerResponseListener.getFailResponse(call, e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        synchronized (NetUtil.this) {
                            try {
                                LogUtil.e("加密返回结果= " + response);
                                response = EncryptNetUtil.getDecryptResponse(response);
                                LogUtil.e("数据解密后= " + response);
                                if (response.contains("\"data\":[")) {
                                    CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                                    int statusCode = codeStatueBean.getStatusCode();
                                    if (statusCode == 2) {
                                        toLogin(context);
                                        return;
                                    }
                                }
                                onServerResponseListener.getSuccessResponse(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 联网请求服务器（post）
     *
     * @return 参数对象
     */
    public void okHttp2Server2Current(final Context context, String url, Map<String, Object> map) {
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        if (map != null) {
            try {
                String data = EncryptNetUtil.getBase64StringCurrent(map);
                Integer randomNumber = EncryptNetUtil.getRandomNumber();
                long timesTamp = EncryptNetUtil.getTimesTamp();
                String token = CacheUtils.getString(context, CacheUtils.LOGIN_TOKEN, "");
                String userLogin = Tools.getUserLogin(context);
                String signature = EncryptNetUtil.getMD5EncryptHeader(timesTamp, randomNumber, userLogin, token, data);
                builder.addHeader("timestamp", String.valueOf(timesTamp));
                builder.addHeader("nonce", String.valueOf(randomNumber));
                if (signature != null) {
                    builder.addHeader("signature", signature);
                }
                if (data != null) {
                    builder.addParams("data", data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        onServerResponseListener.getFailResponse(call, e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        synchronized (NetUtil.this) {
                            try {
                                LogUtil.e("加密返回结果= " + response);
                                response = EncryptNetUtil.getDecryptResponse(response);
                                LogUtil.e("数据解密后= " + response);
                                if (response.contains("\"data\":[")) {
                                    CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                                    int statusCode = codeStatueBean.getStatusCode();
                                    if (statusCode == 2) {
                                        toLogin(context);
                                        return;
                                    }
                                }
                                onServerResponseListener.getSuccessResponse(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 跳转到登录界面
     *
     * @param context
     */
    private void toLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 联网请求服务器（post）
     * 上传文件
     *
     * @return 参数对象
     */
    public void okHttp2Server4(String url, Map<String, String> map, File[] files) {
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        if (map != null) {
            builder = addParams2Builder(builder, map);
            builder = addFiles2Builder(builder, files);
        }
        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        onServerResponseListener.getFailResponse(call, e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        onServerResponseListener.getSuccessResponse(response);
                    }
                });
    }

    private PostFormBuilder addFiles2Builder(PostFormBuilder builder, File[] files) {
        for (int i = 0; i < files.length; i++) {
            builder.addFile("picture", (1 + i) + ".png", files[i]);
        }
        return builder;
    }


    /**
     * okHttp进行联网请求（get）
     *
     * @param url
     * @param map
     */
    public void okHttp2Server(String url, Map<String, String> map) {
        GetBuilder builder = OkHttpUtils.get().url(url);
        if (map != null) {
            builder = addParams2Builder2(builder, map);
        }
        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        onServerResponseListener.getFailResponse(call, e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        onServerResponseListener.getSuccessResponse(response);
                    }
                });
    }

    /**
     * 添加post请求的参数
     *
     * @param builder
     * @param map
     * @return
     */
    private PostFormBuilder addParams2Builder(PostFormBuilder builder, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            LogUtil.e("key= " + entry.getKey() + " and value= " + entry.getValue());
            builder.addParams(entry.getKey(), entry.getValue());
        }
        return builder;
    }

    /**
     * 添加get请求参数
     *
     * @param builder
     * @param map
     * @return
     */
    private GetBuilder addParams2Builder2(GetBuilder builder, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            LogUtil.e("key= " + entry.getKey() + " and value= " + entry.getValue());
            builder.addParams(entry.getKey(), entry.getValue());
        }
        return builder;
    }

    /**
     * 用來返回结果的接口
     */
    public interface OnServerResponseListener {

        void getSuccessResponse(String response);

        void getFailResponse(Call call, Exception e);
    }

    public void setOnServerResponseListener(OnServerResponseListener onServerResponseListener) {
        this.onServerResponseListener = onServerResponseListener;
    }

    /**
     * 联网请求服务器（post）
     *
     * @return 参数对象
     */
    public void okHttp2ServerEnctry(String url, String data) {
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        if (data != null) {
            builder.addParams("data", data);
        }
        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        onServerResponseListener.getFailResponse(call, e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        onServerResponseListener.getSuccessResponse(response);
                    }
                });
    }

    /**
     * 联网请求服务器（post）
     *
     * @return 参数对象
     */
    public void okHttp2ServerEnctryHeader(String url, String data, String tamstap, String nonce, String signature) {

        PostFormBuilder builder = OkHttpUtils.post().url(url);
        if (tamstap != null) {
            builder.addHeader("timestamp", tamstap);
        }
        if (nonce != null) {
            builder.addHeader("nonce", nonce);
        }
        if (signature != null) {
            builder.addHeader("signature", signature);
        }
        if (data != null) {
            builder.addParams("data", data);
        }
        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        onServerResponseListener.getFailResponse(call, e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        onServerResponseListener.getSuccessResponse(response);
                    }
                });
    }

}
