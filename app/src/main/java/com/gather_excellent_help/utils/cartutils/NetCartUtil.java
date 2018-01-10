package com.gather_excellent_help.utils.cartutils;

import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2017/12/5.
 */
public class NetCartUtil {
    public static final String add_url = Url.BASE_URL + "suning/MyShoppingCart.ashx?action=cart_goods_add";
    public static final String del_url = Url.BASE_URL + "suning/MyShoppingCart.ashx?action=cart_goods_delete";
    public static final String upd_url = Url.BASE_URL + "suning/MyShoppingCart.ashx?action=cart_goods_update";
    public static final String get_url = Url.BASE_URL + "suning/MyShoppingCart.ashx?action=cart_goods_list";
    public static final String total_url = Url.BASE_URL + "suning/MyShoppingCart.ashx?action=cart_goods_buy";
    public static final String WHICH_ADD = "cart_add";
    public static final String WHICH_DEL = "cart_del";
    public static final String WHICH_UPD = "cart_upd";
    public static final String WHICH_GET = "cart_get";
    public static final String WHICH_TOTAL = "cart_TOTAL";
    private NetUtil netUtil;
    private Map<String, String> map;
    private String whick;
    private final OnServerResponseListener onServerResponseListener;


    public NetCartUtil() {
        netUtil = new NetUtil();
        whick = "";
        onServerResponseListener = new OnServerResponseListener();
        netUtil.setOnServerResponseListener(onServerResponseListener);
    }

    public void addCart(String user_id, String channel_id, String article_id, String goods_id, String quantity) {
        whick = WHICH_ADD;
        map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("channel_id", channel_id);
        map.put("article_id", article_id);
        map.put("goods_id", goods_id);
        map.put("quantity", quantity);
        netUtil.okHttp2Server2(add_url, map);
    }

    public void deleteCart(String clear, String user_id, String cart_id) {
        whick = WHICH_DEL;
        map = new HashMap<>();
        map.put("clear", clear);
        map.put("user_id", user_id);
        map.put("cart_id", cart_id);
        netUtil.okHttp2Server2(del_url, map);
    }

    public void updateCart(String cart_id, String user_id, String channel_id, String article_id, String goods_id, String quantity) {
        whick = WHICH_UPD;
        map = new HashMap<>();
        map.put("cart_id", cart_id);
        map.put("user_id", user_id);
        map.put("channel_id", channel_id);
        map.put("article_id", article_id);
        map.put("goods_id", goods_id);
        map.put("quantity", quantity);
        netUtil.okHttp2Server2(upd_url, map);
    }

    public void getCartList(String user_id) {
        whick = WHICH_GET;
        map = new HashMap<>();
        map.put("user_id", user_id);
        netUtil.okHttp2Server2(get_url, map);
    }

    public void getTotalPrice(String jsonData) {
        whick = WHICH_TOTAL;
        map = new HashMap<>();
        map.put("jsonData", jsonData);
        netUtil.okHttp2Server2(total_url, map);
    }

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
            onCartResponseListener.onCartResponse(response, whick);
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
            onCartResponseListener.onCartFail();
        }
    }

    private OnCartResponseListener onCartResponseListener;

    public interface OnCartResponseListener {
        void onCartResponse(String response, String whick);

        void onCartFail();
    }

    public void setOnCartResponseListener(OnCartResponseListener onCartResponseListener) {
        this.onCartResponseListener = onCartResponseListener;
    }
}
