package com.gather_excellent_help.utils;

import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by wuxin on 2017/7/10.
 */

public class NetUtil {

    private OnServerResponseListener onServerResponseListener;


    /**
     * 联网请求服务器
     * @return 参数对象
     */
    public void okHttp2Server(String url, Map<String,String> map){
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        if(map!=null) {
            builder = addParams2Builder(builder, map);
        }
        builder.build()
                .execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                onServerResponseListener.getFailResponse(call,e);
            }

            @Override
            public void onResponse(String response, int id) {
                onServerResponseListener.getSuccessResponse(response);
            }
        });
    }

    public void okHttp2Server2(String url,Map<String,String> map){
        GetBuilder builder = OkHttpUtils.get().url(url);
        if(map!= null) {
            builder = addParams2Builder2(builder,map);
        }
        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        onServerResponseListener.getFailResponse(call,e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        onServerResponseListener.getSuccessResponse(response);
                    }
                });
    }

    private PostFormBuilder addParams2Builder(PostFormBuilder builder, Map<String,String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            LogUtil.e("key= " + entry.getKey() + " and value= " + entry.getValue());
            builder.addParams(entry.getKey(),entry.getValue());
        }
        return builder;
    }
    private GetBuilder addParams2Builder2(GetBuilder builder, Map<String,String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            LogUtil.e("key= " + entry.getKey() + " and value= " + entry.getValue());
            builder.addParams(entry.getKey(),entry.getValue());
        }
        return builder;
    }

    public interface OnServerResponseListener{

        void getSuccessResponse(String response);
        void getFailResponse(Call call, Exception e);
    }

    public void setOnServerResponseListener(OnServerResponseListener onServerResponseListener) {
        this.onServerResponseListener = onServerResponseListener;
    }
}
