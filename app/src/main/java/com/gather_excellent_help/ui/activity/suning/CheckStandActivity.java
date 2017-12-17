package com.gather_excellent_help.ui.activity.suning;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.api.pay.PayResult;
import com.gather_excellent_help.api.pay.SignUtils;
import com.gather_excellent_help.bean.suning.SuningPaystateBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.AlipayManagerActivity;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class CheckStandActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;

    private CheckBox cb_zhifubao_select;//选择支付宝
    private TextView tv_checkstand_topay;//去支付

    private TextView tv_checkstand_ordercode;//订单号码
    private TextView tv_checkstand_money;//支付金额

    private String pstatus_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=is_payment";
    //private String pstatus_url = "http://192.168.200.125:8022/api/juyoubang/suning/SNbusinessHandler.ashx?action=is_payment";
    private NetUtil netUtil;
    private Map<String, String> map;

    private String pay_status = "";

    private String user_login;//用户id
    private double pay_price;//应付金额
    private String order_num = "";//订单号
    private DecimalFormat df;
    private int orderId;//订单id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_stand);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);

        cb_zhifubao_select = (CheckBox) findViewById(R.id.cb_zhifubao_select);
        tv_checkstand_topay = (TextView) findViewById(R.id.tv_checkstand_topay);

        tv_checkstand_ordercode = (TextView) findViewById(R.id.tv_checkstand_ordercode);
        tv_checkstand_money = (TextView) findViewById(R.id.tv_checkstand_money);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        df = new DecimalFormat("#0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        pay_price = intent.getDoubleExtra("pay_price", 0);
        order_num = intent.getStringExtra("order_num");
        orderId = intent.getIntExtra("orderId", 0);
        cb_zhifubao_select.setChecked(true);
        user_login = Tools.getUserLogin(this);
        netUtil = new NetUtil();
        OnServerResponseListenr onServerResponseListenr = new OnServerResponseListenr();
        netUtil.setOnServerResponseListener(onServerResponseListenr);
        tv_top_title_name.setText("聚优帮收银台");
        tv_checkstand_ordercode.setText(order_num);
        tv_checkstand_money.setText(String.valueOf(df.format(pay_price)));
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        tv_checkstand_topay.setOnClickListener(myonclickListener);
    }

    /**
     * 监听全局的点击事件
     */
    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_checkstand_topay:
                    boolean checked = cb_zhifubao_select.isChecked();
                    if (!checked) {
                        Toast.makeText(CheckStandActivity.this, "请您选择支付方式！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    toPayOrder();
                    break;
            }
        }
    }

    /**
     * 订单支付
     */
    private void toPayOrder() {
        String orderInfo = getOrderInfo("聚优帮购物", "聚优帮-商家-购物", "0.01");
        //String orderInfo = getOrderInfo("聚优帮购物", "聚优帮-商家-购物", String.valueOf(df.format(pay_price)));
        LogUtil.e(orderInfo);
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        LogUtil.e("支付信息 = " + payInfo);

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(CheckStandActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    // 商户PID
    //public static final String PARTNER = "2088621736065035";
    public static final String PARTNER = "2088621987751299";//最新
    // 商户收款账号
    //public static final String SELLER = "jurenyunshang@163.com";
    public static final String SELLER = "juyobcom@163.com";//最新
    // 商户私钥，pkcs8格式
//    public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKAXUb5ILO3ZGkzD\n" +
//            "LYldxiOOCVEKkWImcZusaFYOHAGqwAbwmtReguAqe48XwuwQ9HxLD4ob+fQilQoa\n" +
//            "vgNrrre+VL8w6JyCUTYsf93sySa0ZhlOSTaC9WzkZmy2RGTIm+m4XvsBR+7YxFz1\n" +
//            "oVxTqLBbpGPl/Yv52JZCSzfai6MlAgMBAAECgYBYRsOSc2Yi+XYhIfb4KjdZ37aP\n" +
//            "c45bWuuGdflKipky6h7dNMuLk1oDlilv7CBV37uQ1isJcwfPcUpRuhFQ/Y3ncMwR\n" +
//            "jtXcA+rjijwNpDbcSCCxbeiUJ86AuCZlPcjrCut4HT4bJ37n7OsYxlJDTd002ftm\n" +
//            "jkSYDyp/aXEl6LMbwQJBAMyBeVlW3PT6mln6Li+6Hz2uzP5QtbsptLrcyN2JZNLZ\n" +
//            "ETl0CxTr/druLlLi7lHuFnVwtaIpVvL0R5Fa7QAan6kCQQDIZtuBZ3q2OHL0QkbE\n" +
//            "PQAhxcCkDqWkg1TwkyMH2hBwPTTk03GpYxJqNsRaJ4Bo8+Qhc0bTzyzajnW9BRhx\n" +
//            "50UdAkAlQu3+VjJaPJMFE+14arwofAID0GbcXEP+zB2ZJ+CD/mMCCd+/BySw0DcG\n" +
//            "iSyNJwlWipu9mTSrfdDQbjXCkBdJAkAPJT7nq8NpbCC79xpUEGwnIUPq4jSAl3Be\n" +
//            "8i/Okxt08BtEQ/quG4+zuUnjqmKZC/2szYKH1XuFVQju7Sioyxq9AkA1ldoHdbbN\n" +
//            "cHP8Zpc4Hpz+YIIGJhVifl3rNLAi2CqngrQFjmJxNa0GLkTSc4/zE4L4Q1UKEM2c\n" +
//            "qs8s8NwJW5RL";
    //最新
    public static final String RSA_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCvhxE+9VmU82wY2FB7Cw4tcuuNnM7fhzYi/KfO3dFr4oR5RhIs1noYcWmC3/YlR+EM6GE5VIhG0dz2gR9YiF8HNBcGGFnWO++Hf0QhW82gf8hGg1t/KA+FfYSK8++g/vQ2kh2W8LLnMa1TX5jFkYpgJAS194zH2deCPqlQXyY8OcxjzcYDwN+M4gYfMVwwxeoEwq66S+FlNeyu/3TfyrU2H73bK4hKbHhhNRTIxHwZbZpwaGme04NZWCJ5H5fvNiQYnxhCNOM0QQWS4vKQZ4rDCi9/Y3VwD+azXpcrePECCxbOJHrjP15XEh0qtSzeYy5bvDg59tb/HtSaGxIlmytxAgMBAAECggEAGO9ZFdaI2U4TRxxDopcZdn9i0wU5pitxF1tJA2wjee3937SClHgLdDl4Qa1LLFSftdO1LA3EJjtd5m7FYzBMP5u4wZbn/DUbe+YqaYq/FVPRwaKbdt7cBZylHZA1tGtDihULuwuKfqlh8/wamcZpH+s9+BOoniNmKAx6xP4fUmKu0QicoAG2w7P0WOSfBO09EQ3EadBPOHXmayqI0yRTO/FrRH4TMCNpKdYhn4hvMckDBStj5fBMC9cfGZhpX+dEgqq2Oo7a+ASv8gqNFyaoZJM1ZKyzyyRku8nF4Ay1u4W9kfW+yx7VNFARJu1nQCOSbLvTxCCRvnirAYfBgA6FmQKBgQDyNXjMLw0BVizhrqc+O6S0QOYuEaL8m8DEGSoNHS6VltO2AG+I2Mz9CbrdWVqWZ2N/8XTfA4mP/EIl7kEWOz9986GiISJiMq5HGnVwmp4P25oBZgZfJa4w3obeDWH+B1TxUW7P7rPsH4fG8HmI/pNnx8F1Nd208pcd5u4BTl4OuwKBgQC5hZ/HKe9Z24/MsyqYAMgQXFapCVIHW34ZNmRIF5mVW77hlKJhDa5nkHii9X0uLB8zBL3ygokx44QBQJW42ptmyuAR1sy6j75pJ1oeUWo976SzaMXy7uuj5e6cMftK9uBaLiQhnB+l004h5nX8CxHdHhBqedUbsimh2fs2Wi4pwwKBgQCSke8uyI47L9XrIFDOpTMvbVKdsEiMSwik4oGC/BTTqzkkmzDjhCbQPVIaXtclyxz4MLrDthVJz6KL55j1hhcO9h7qyNhQylCScJ1+7eIWwJJ6pC3Y6Cl4I0FEIJ8bJX6updPD+rRggFge+Cjj0QKMu+IDNvyaYm82RifjHHRovwKBgHCDS618Dz8N254notNg1fSmK6CEmI0Bve4IgLUAKxLb+13PyNLXe0gv5BngjDSuaZWFcLVc2Vu6QdqIksTzP4AN1eq7vJoAh1g6l3r/oVFCvz5XcoWsxJg34Ig1/9Ms+k924E7J4p+tERuaKz/abtENZKNOlCIh4QHZs3L5KjoFAoGBAKIZwyMCH2W5P7Rw24ZyJcd+zFlpX8r7SzIxLhjkgMFmtmJCmrKJ4MNl/ZfDV1zZg/CQOJD3/tjsEbUPfWH7o/s5a7jDGXJFjOWnN/jp94zupj04Bi72cCTBpX3LwNu/iimK0Lt1X5zIlSuz/Fxl9bcsLCfZjwuCCgwYnBl/Clkn";
    //public static final String RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr4cRPvVZlPNsGNhQewsOLXLrjZzO34c2Ivynzt3Ra+KEeUYSLNZ6GHFpgt/2JUfhDOhhOVSIRtHc9oEfWIhfBzQXBhhZ1jvvh39EIVvNoH/IRoNbfygPhX2EivPvoP70NpIdlvCy5zGtU1+YxZGKYCQEtfeMx9nXgj6pUF8mPDnMY83GA8DfjOIGHzFcMMXqBMKuukvhZTXsrv9038q1Nh+92yuISmx4YTUUyMR8GW2acGhpntODWVgieR+X7zYkGJ8YQjTjNEEFkuLykGeKwwovf2N1cA/ms16XK3jxAgsWziR64z9eVxIdKrUs3mMuW7w4OfbW/x7UmhsSJZsrcQIDAQAB";// 支付宝公钥
    private static final int SDK_PAY_FLAG = 1;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String memo = payResult.getMemo();
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(CheckStandActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        pay_status = "2";
                        if (pay_status != null && !TextUtils.isEmpty(pay_status)) {
                            map = new HashMap<>();
                            map.put("user_id", user_login);
                            map.put("is_payment", pay_status);
                            map.put("order_id", String.valueOf(orderId));
                            netUtil.okHttp2Server2(pstatus_url, map);
                        }
                    } else {
                        pay_status = "1";
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(CheckStandActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(CheckStandActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                        if (pay_status != null && !TextUtils.isEmpty(pay_status)) {
                            map = new HashMap<>();
                            map.put("user_id", user_login);
                            map.put("is_payment", pay_status);
                            map.put("order_id", String.valueOf(orderId));
                            netUtil.okHttp2Server2(pstatus_url, map);
                        }
                    }
                    break;
                }

                default:
                    break;
            }
        }

        ;
    };


    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + order_num + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://api.myausplus.com/alipay/andriod_back" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * 联网请求的回调
     */
    public class OnServerResponseListenr implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
            SuningPaystateBean suningPaystateBean = new Gson().fromJson(response, SuningPaystateBean.class);
            int statusCode = suningPaystateBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    List<SuningPaystateBean.DataBean> data = suningPaystateBean.getData();
                    if (data != null && data.size() > 0) {
                        SuningPaystateBean.DataBean dataBean = data.get(0);
                        if (dataBean != null) {
                            String pay_text = dataBean.getPay_text();
                            Toast.makeText(CheckStandActivity.this, pay_text, Toast.LENGTH_SHORT).show();
                            toOrderPage();
                        }
                    }
                    break;
                case 0:
                    Toast.makeText(CheckStandActivity.this, suningPaystateBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            //Toast.makeText(CheckStandActivity.this, "支付状态保存出现问题，请及时联系客服处理！！！！", Toast.LENGTH_SHORT).show();
            LogUtil.e(call.toString() + "-" + e.getMessage());
        }
    }

    /**
     * 跳转到订单列表页面
     */
    private void toOrderPage() {
        Intent intent = new Intent(this, SuningOrderActivity.class);
        intent.putExtra("pay_status", pay_status);
        startActivity(intent);
        finish();
    }
}
