package com.gather_excellent_help.ui.activity.suning;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.suning.SuningOrderBean;
import com.gather_excellent_help.bean.suning.SuningOrderConfirmBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;


public class SuningOrderDetailActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;

    private TextView tv_suning_detail_status;
    private TextView tv_suning_detail_accept;
    private TextView tv_suning_detail_phone;
    private TextView tv_suning_detail_address;
    private LinearLayout ll_suning_detail_container;

    private TextView tv_suning_detail_free;
    private TextView tv_suning_detail_money;
    private TextView tv_suning_detail_orderno;
    private TextView tv_suning_detail_createtime;
    private TextView tv_suning_detail_paytime;
    private TextView tv_suning_detail_sendtime;
    private TextView tv_suning_detail_accepttime;
    private TextView tv_item_detail_right;
    private TextView tv_item_detail_left;
    private TextView tv_item_detail_extra;

    private String orderInfo;
    private int status;//当前订单状态
    private AlertDialog alertDialog;

    private NetUtil netUtil;
    private Map<String, String> map;
    private String whick = "";//哪一个
    private String userLogin;

    private String cancel_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=DeletSingerOrder";//取消订单
    private String confrim_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=recive_myProduct";//确认订单
    private int order_id;//订单id
    private String order_no;
    private double real_amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suning_order_detail);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);

        ll_suning_detail_container = (LinearLayout) findViewById(R.id.ll_suning_detail_container);
        tv_suning_detail_status = (TextView) findViewById(R.id.tv_suning_detail_status);
        tv_suning_detail_accept = (TextView) findViewById(R.id.tv_suning_detail_accept);
        tv_suning_detail_phone = (TextView) findViewById(R.id.tv_suning_detail_phone);
        tv_suning_detail_address = (TextView) findViewById(R.id.tv_suning_detail_address);

        tv_suning_detail_free = (TextView) findViewById(R.id.tv_suning_detail_free);
        tv_suning_detail_money = (TextView) findViewById(R.id.tv_suning_detail_money);
        tv_suning_detail_orderno = (TextView) findViewById(R.id.tv_suning_detail_orderno);
        tv_suning_detail_createtime = (TextView) findViewById(R.id.tv_suning_detail_createtime);
        tv_suning_detail_paytime = (TextView) findViewById(R.id.tv_suning_detail_paytime);
        tv_suning_detail_sendtime = (TextView) findViewById(R.id.tv_suning_detail_sendtime);
        tv_suning_detail_accepttime = (TextView) findViewById(R.id.tv_suning_detail_accepttime);
        tv_item_detail_right = (TextView) findViewById(R.id.tv_item_detail_right);
        tv_item_detail_left = (TextView) findViewById(R.id.tv_item_detail_left);
        tv_item_detail_extra = (TextView)findViewById(R.id.tv_item_detail_extra);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tv_top_title_name.setText("订单详情");
        userLogin = Tools.getUserLogin(this);
        netUtil = new NetUtil();
        OnServerResponseListener onServerResponseListener = new OnServerResponseListener();
        netUtil.setOnServerResponseListener(onServerResponseListener);
        Intent intent = getIntent();
        orderInfo = intent.getStringExtra("orderInfo");
        getOrderInfo(orderInfo);
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        tv_item_detail_left.setOnClickListener(myonclickListener);
        tv_item_detail_right.setOnClickListener(myonclickListener);
    }

    /**
     * 获取订单信息
     *
     * @param orderInfo
     */
    private void getOrderInfo(String orderInfo) {
        if (orderInfo != null) {
            SuningOrderBean.DataBean dataBean = new Gson().fromJson(orderInfo, SuningOrderBean.DataBean.class);
            if (dataBean != null) {
                status = dataBean.getOrder_status();
                order_no = dataBean.getOrder_no();
                order_id = dataBean.getId();
                real_amount = dataBean.getReal_amount();
                DecimalFormat df = new DecimalFormat("#0.00");
                showOrderStatus(status);
                showOrderAddress(dataBean);
                showWareInfo(dataBean, df);
                showFreeAndMoney(dataBean, df);
                showOrderTime(dataBean, status);
            }
        }

    }

    /**
     * 显示订单时间
     *
     * @param dataBean
     * @param status
     */
    private void showOrderTime(SuningOrderBean.DataBean dataBean, int status) {

        String add_time = dataBean.getAdd_time();
        String payment_time = dataBean.getPayment_time();
        String express_time = dataBean.getExpress_time();
        String complete_time = dataBean.getComplete_time();
        tv_suning_detail_orderno.setText("订单号：" + order_no);
        if (status == 1) {
            tv_suning_detail_createtime.setVisibility(View.VISIBLE);
            tv_suning_detail_paytime.setVisibility(View.GONE);
            tv_suning_detail_sendtime.setVisibility(View.GONE);
            tv_suning_detail_accepttime.setVisibility(View.GONE);
            if (add_time != null) {
                tv_suning_detail_createtime.setText("创建时间：" + add_time);
            }
            tv_item_detail_extra.setVisibility(View.GONE);
            tv_item_detail_left.setVisibility(View.VISIBLE);
            tv_item_detail_right.setVisibility(View.VISIBLE);
            tv_item_detail_left.setText("取消订单");
            tv_item_detail_right.setText("立即付款");
        } else if (status == 2) {
            tv_suning_detail_createtime.setVisibility(View.VISIBLE);
            tv_suning_detail_paytime.setVisibility(View.VISIBLE);
            tv_suning_detail_sendtime.setVisibility(View.GONE);
            tv_suning_detail_accepttime.setVisibility(View.GONE);
            if (add_time != null) {
                tv_suning_detail_createtime.setText("创建时间：" + add_time);
            }
            if (payment_time != null) {
                tv_suning_detail_paytime.setText("付款时间：" + payment_time);
            }
            tv_item_detail_extra.setVisibility(View.VISIBLE);
            tv_item_detail_left.setVisibility(View.VISIBLE);
            tv_item_detail_right.setVisibility(View.VISIBLE);
            tv_item_detail_extra.setText("取消订单");
            tv_item_detail_left.setText("查看物流");
            tv_item_detail_right.setText("提醒发货");
        } else if (status == 3) {
            tv_suning_detail_createtime.setVisibility(View.VISIBLE);
            tv_suning_detail_paytime.setVisibility(View.VISIBLE);
            tv_suning_detail_sendtime.setVisibility(View.VISIBLE);
            tv_suning_detail_accepttime.setVisibility(View.GONE);
            if (add_time != null) {
                tv_suning_detail_createtime.setText("创建时间：" + add_time);
            }
            if (payment_time != null) {
                tv_suning_detail_paytime.setText("付款时间：" + payment_time);
            }
            if (express_time != null) {
                tv_suning_detail_sendtime.setText("发货时间：" + express_time);
            }
            tv_item_detail_extra.setVisibility(View.GONE);
            tv_item_detail_left.setVisibility(View.GONE);
            tv_item_detail_right.setVisibility(View.VISIBLE);
            tv_item_detail_right.setText("查看物流");
        } else if (status == 4) {
            tv_suning_detail_createtime.setVisibility(View.VISIBLE);
            tv_suning_detail_paytime.setVisibility(View.VISIBLE);
            tv_suning_detail_sendtime.setVisibility(View.VISIBLE);
            tv_suning_detail_accepttime.setVisibility(View.VISIBLE);
            if (add_time != null) {
                tv_suning_detail_createtime.setText("创建时间：" + add_time);
            }
            if (payment_time != null) {
                tv_suning_detail_paytime.setText("付款时间：" + payment_time);
            }
            if (express_time != null) {
                tv_suning_detail_sendtime.setText("发货时间：" + express_time);
            }
            if (complete_time != null) {
                tv_suning_detail_accepttime.setText("完成时间：" + complete_time);
            }
            tv_item_detail_extra.setVisibility(View.GONE);
            tv_item_detail_left.setVisibility(View.VISIBLE);
            tv_item_detail_right.setVisibility(View.VISIBLE);
            tv_item_detail_left.setText("申请售后");
            tv_item_detail_right.setText("确认订单");
        }
    }

    /**
     * 显示邮费和付款金额
     *
     * @param dataBean
     * @param df
     */
    private void showFreeAndMoney(SuningOrderBean.DataBean dataBean, DecimalFormat df) {
        double real_amount = dataBean.getReal_amount();
        tv_suning_detail_free.setText("免邮费");
        tv_suning_detail_money.setText("￥" + df.format(real_amount));
    }

    /**
     * 展示商品信息
     *
     * @param dataBean
     * @param df
     */
    private void showWareInfo(SuningOrderBean.DataBean dataBean, DecimalFormat df) {
        List<SuningOrderBean.DataBean.GoodListBean> goodList = dataBean.getGoodList();
        if (goodList != null && goodList.size() > 0) {
            ll_suning_detail_container.removeAllViews();
            for (int i = 0; i < goodList.size(); i++) {
                View inflate = View.inflate(SuningOrderDetailActivity.this, R.layout.suning_order_detail_ware, null);
                ImageView iv_suning_order_ware = (ImageView) inflate.findViewById(R.id.iv_suning_order_ware);
                TextView tv_suning_order_title = (TextView) inflate.findViewById(R.id.tv_suning_order_title);
                TextView tv_suning_order_type = (TextView) inflate.findViewById(R.id.tv_suning_order_type);
                TextView tv_suning_order_realprice = (TextView) inflate.findViewById(R.id.tv_suning_order_realprice);
                TextView tv_suning_order_oldprice = (TextView) inflate.findViewById(R.id.tv_suning_order_oldprice);
                TextView tv_suning_order_number = (TextView) inflate.findViewById(R.id.tv_suning_order_number);
                SuningOrderBean.DataBean.GoodListBean goodListBean = goodList.get(i);
                if (goodListBean != null) {
                    String goods_title = goodListBean.getGoods_title();
                    String spec_text = goodListBean.getSpec_text();
                    double real_price = goodListBean.getReal_price();
                    double goods_price = goodListBean.getGoods_price();
                    int quantity = goodListBean.getQuantity();
                    if (goodListBean.getImg_url() != null) {
                        String img_url = goodListBean.getImg_url().replace("800x800", "400x400");
                        Glide.with(SuningOrderDetailActivity.this).load(img_url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                                .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                                .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                                .into(iv_suning_order_ware);//请求成功后把图片设置到的控件
                    }
                    if (spec_text != null) {
                        tv_suning_order_type.setText(spec_text);
                    }

                    if (goods_title != null) {
                        tv_suning_order_title.setText(goods_title);
                    }
                    tv_suning_order_realprice.setText("￥" + String.valueOf(df.format(real_price)));
                    tv_suning_order_oldprice.getPaint().setAntiAlias(true);
                    tv_suning_order_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    tv_suning_order_oldprice.setText("￥" + String.valueOf(df.format(goods_price)));
                    tv_suning_order_number.setText("x" + quantity);
                    ll_suning_detail_container.addView(inflate);
                }
            }

        }
    }

    /**
     * 展示收货地址
     * @param dataBean
     */
    private void showOrderAddress(SuningOrderBean.DataBean dataBean) {
        String accept_name = dataBean.getAccept_name();
        String mobile = dataBean.getMobile();
        String area = dataBean.getArea();
        String address = dataBean.getAddress();
        tv_suning_detail_accept.setText(accept_name);
        tv_suning_detail_phone.setText(mobile);
        tv_suning_detail_address.setText(area + " " + address);
    }

    /**
     * 展示订单状态
     *
     * @param status
     */
    private void showOrderStatus(int status) {
        if (status == 4) {
            tv_suning_detail_status.setText("买家待付款，支付后将及时为您安排发货。");
        } else if (status == 1) {
            tv_suning_detail_status.setText("买家已付款，等待卖家发货。");
        } else if (status == 2) {
            tv_suning_detail_status.setText("卖家已发货，请您查看物流信息。");
        } else if (status == 3) {
            tv_suning_detail_status.setText("买家已签收，交易完成。");
        }
    }

    /**
     * 页面上事件监听
     */
    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_item_detail_left:
                    leftButtonHandler();
                    break;
                case R.id.tv_item_detail_right:
                    rightButtonHandler();
                    break;
            }
        }
    }

    /**
     * 处理右边button的点击事件
     */
    private void rightButtonHandler() {
        if (status == 4) {
            toPayOrder(real_amount, order_no, order_id);
        } else if (status == 1) {
            remindSend();
        } else if (status == 2) {
            seeLogisticsInfo();
        } else if (status == 3) {
            confirmOrder(String.valueOf(order_id));
        }
    }

    /**
     * 处理左边button的点击事件
     */
    private void leftButtonHandler() {
        if (status == 4) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示")
                    .setMessage("你确定要取消的订单吗?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cancelOrder(String.valueOf(order_id));
                        }
                    })
                    .setNegativeButton("取消", null);
            alertDialog = builder.create();
            if (SuningOrderDetailActivity.this != null && !SuningOrderDetailActivity.this.isFinishing()) {
                alertDialog.show();
            }
        }
    }

    /**
     * 退款或售后
     */
    private void toRebackOrder() {
        Toast.makeText(SuningOrderDetailActivity.this, "退款售后！", Toast.LENGTH_SHORT).show();
    }

    /**
     * 订单支付
     */
    private void toPayOrder(double pay_price, String order_num, int orderId) {
        Intent intent = new Intent(this, CheckStandActivity.class);
        intent.putExtra("pay_price", pay_price);
        intent.putExtra("order_num", order_num);
        intent.putExtra("orderId", orderId);
        startActivity(intent);
        finish();
    }

    /**
     * 取消订单
     */
    private void cancelOrder(String order_id) {
        whick = "cancel_order";
        map = new HashMap<>();
        map.put("user_id", userLogin);
        map.put("order_id", order_id);
        netUtil.okHttp2Server2(cancel_url, map);
    }


    /**
     * 提醒发货
     */
    private void remindSend() {
        Toast.makeText(this, "已经提醒卖家发货了，请您耐心等待。。。", Toast.LENGTH_SHORT).show();
    }

    /**
     * 查看物流信息
     */
    private void seeLogisticsInfo() {
        Intent intent = new Intent(SuningOrderDetailActivity.this, LogisticsInfoActivity.class);
        intent.putExtra("order_id", order_id);
        startActivity(intent);
    }

    /**
     * 确认订单
     */
    private void confirmOrder(String order_id) {
        whick = "confirm_order";
        map = new HashMap<>();
        map.put("user_id", userLogin);
        map.put("order_id", order_id);
        map.put("order_status", "3");
        netUtil.okHttp2Server2(confrim_url, map);
    }


    /**
     * 监听联网请求的回调
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            if (whick.equals("cancel_order")) {
                parseCancelOrderData(response);
            } else if (whick.equals("confirm_order")) {
                parderConfirmOrderData(response);
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
        }
    }

    /**
     * 解析取消订单数据
     *
     * @param response
     */
    private void parseCancelOrderData(String response) {
        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
        int statusCode = codeStatueBean.getStatusCode();
        switch (statusCode) {
            case 1:
                Toast.makeText(SuningOrderDetailActivity.this, "订单取消成功。", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new AnyEvent(EventType.UPDATA_ORDER_LIST, "刷新订单列表"));
                finish();
                break;
            case 0:
                Toast.makeText(SuningOrderDetailActivity.this, "订单取消失败！！！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析确认订单数据
     *
     * @param response
     */
    private void parderConfirmOrderData(String response) {
        SuningOrderConfirmBean suningOrderConfirmBean = new Gson().fromJson(response, SuningOrderConfirmBean.class);
        int statusCode = suningOrderConfirmBean.getStatusCode();
        switch (statusCode) {
            case 1:
                Toast.makeText(SuningOrderDetailActivity.this, "确认订单成功。", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new AnyEvent(EventType.UPDATA_ORDER_LIST, "刷新订单列表"));
                finish();
                break;
            case 0:
                Toast.makeText(SuningOrderDetailActivity.this, "确认订单失败！！！", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
