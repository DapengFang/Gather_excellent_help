package com.gather_excellent_help.utils.changeutils;

import android.content.Context;

import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2017/10/31.
 */

public class ChangeUrlUtil {

    private OnChangeUrlListener onChangeUrlListener;

    public static boolean checkContainWareId(String url){
        if(url.contains("id=") && url.contains("#detail")) {
           return true;
        }
        return false;
    }

    public static String getWareId(String url){
        int i = url.indexOf("id=");
        String substring = url.substring(i);
        String[] split = substring.split("&");
        String result = split[0].replace("id=", "");
        return result;
    };

    public static void getChangeUrl(Context context, String url, String wareId, String adId, final OnChangeUrlListener onChangeUrlListener){
        String changeUrl = Url.BASE_URL + "GoodsConvert.aspx";
        NetUtil netUtil = new NetUtil();
        Map<String,String> map = new HashMap<>();
        map.put("goodsId", wareId);
        map.put("adzoneId", adId);
        netUtil.okHttp2Server2(changeUrl,map);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                onChangeUrlListener.onResultUrl(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e("网络连接出现问题~");
            }
        });

    }

    public interface OnChangeUrlListener{
        void onResultUrl(String result);
    }
}
