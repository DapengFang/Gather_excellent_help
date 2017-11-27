package com.gather_excellent_help.event;

/**
 * Created by wuxin on 2017/7/11.
 */

public class EventType {
    public static final int EVENT_LOGIN = 0;//登录
    public static final int BIND_TAOBAO = 1;
    public static final int EVENT_EXIT = 2;
    public static final int STORE_EXIT = 3;
    public static final int WEIXIN_EXIT = 4;//退出微信登录界面
    public static final int UPDATA_ADDRESS = 5;//刷新收货地址界面
    public static final int UPDATA_ADDRESS_ORDER = 6;//刷新订单页面收货地址界面
    public static final int UPDATA_ORDER_LIST = 7;//刷新订单列表页面
    public static final int CLEAR_ALL_GOODSCART= 8;//清空购物车
    public static final int UNBIND_ALIPAY= 9;//解除绑定支付宝
    public static final int GOODSCART_CLEAR = 10;//购物车空空如也
}
