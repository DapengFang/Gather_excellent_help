package com.gather_excellent_help.aliapi;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.trade.biz.context.AlibcResultType;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.gather_excellent_help.MyApplication;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Dapeng Fang on 16/8/23.
 */
public class DemoTradeCallback implements AlibcTradeCallback {

    private NetUtil netUtils;
    private Map<String, String> map;
    private Context context;
    private String bind_order = Url.BASE_URL + "MatchOrder.aspx";


    public DemoTradeCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onTradeSuccess(AlibcTradeResult tradeResult) {
        //当addCartPage加购成功和其他page支付成功的时候会回调
        if (tradeResult.resultType.equals(AlibcResultType.TYPEPAY)){
            //支付成功
            //Toast.makeText(MyApplication.application, "支付成功,成功订单号为"+tradeResult.payResult.paySuccessOrders, Toast.LENGTH_SHORT).show();
            List<String> paySuccessOrders = tradeResult.payResult.paySuccessOrders;
            String order = "";
            for (int i=0;i<paySuccessOrders.size();i++){
                String ord = paySuccessOrders.get(i);
                order += ord +"a";
            }
            order = order.substring(0,order.length()-1);
            netUtils = new NetUtil();
            map = new HashMap<>();
            String loginId = CacheUtils.getString(context, CacheUtils.LOGIN_VALUE, "");
            map.put("userId",loginId);
            map.put("OrderId",order);
            netUtils.okHttp2Server2(bind_order,map);
            netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
                @Override
                public void getSuccessResponse(String response) {
                    LogUtil.e(response);
                }

                @Override
                public void getFailResponse(Call call, Exception e) {
                    LogUtil.e(call.toString() + "--" +e.getMessage());
                }
            });
        }
    }

    @Override
    public void onFailure(int errCode, String errMsg) {
        //Toast.makeText(MyApplication.application, "电商SDK出错,错误码="+errCode+" / 错误消息="+errMsg, Toast.LENGTH_SHORT).show();
    }
}
