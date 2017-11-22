package com.gather_excellent_help.utils.pcsutils;

import android.content.Context;

import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2017/11/1.
 */

public class PcsQueryUtil {

    private OnPcsResultListener onPcsResultListener;

    /**
     * 获取省数据
     * @param context
     * @param onPcsResultListener
     */
    public static void getProvince(Context context, final OnPcsResultListener onPcsResultListener) {
        String province_url = Url.BASE_URL +"suning/GetAddress.ashx?action=GetProvince";
        NetUtil netUtil = new NetUtil();
        LogUtil.e("province_url = " + province_url);
        netUtil.okHttp2Server2(province_url, null);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                onPcsResultListener.onPcsResult(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e("网络连接出现问题~");
            }
        });
    }

    /**
     * 获取市数据
     * @param context
     * @param pid
     * @param onPcsResultListener
     */
    public static void getCity(Context context, String pid, final OnPcsResultListener onPcsResultListener) {
        String city_url = Url.BASE_URL + "suning/GetAddress.ashx?action=GetCity";
        NetUtil netUtil = new NetUtil();
        Map<String, String> map = new HashMap<>();
        map.put("area_id", pid);
        netUtil.okHttp2Server2(city_url, map);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                onPcsResultListener.onPcsResult(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e("网络连接出现问题~");
            }
        });
    }

    /**
     * 获取区数据
     * @param context
     * @param cid
     * @param onPcsResultListener
     */
    public static void getDistract(Context context, String cid, final OnPcsResultListener onPcsResultListener) {
        String distract_url = Url.BASE_URL + "suning/GetAddress.ashx?action=GetCounty";
        NetUtil netUtil = new NetUtil();
        Map<String, String> map = new HashMap<>();
        map.put("area_id", cid);
        netUtil.okHttp2Server2(distract_url, map);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                onPcsResultListener.onPcsResult(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e("网络连接出现问题~");
            }
        });

    }

    /**
     * 获取镇数据
     * @param context
     * @param did
     * @param onPcsResultListener
     */
    public static void getTown(Context context, String cid,String did, final OnPcsResultListener onPcsResultListener) {
        String town_url = Url.BASE_URL + "suning/GetAddress.ashx?action=GetTown";
        NetUtil netUtil = new NetUtil();
        Map<String, String> map = new HashMap<>();
        map.put("city_id",cid);
        map.put("area_id", did);
        netUtil.okHttp2Server2(town_url, map);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                onPcsResultListener.onPcsResult(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e("网络连接出现问题~");
            }
        });
    }

    public interface OnPcsResultListener {
        void onPcsResult(String result);
    }
}

