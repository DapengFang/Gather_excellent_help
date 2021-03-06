package com.gather_excellent_help.ui.activity.suning;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class CheckStandActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;

    private CheckBox cb_zhifubao_select;//选择支付宝
    private TextView tv_checkstand_topay;//去支付

    private TextView tv_checkstand_ordercode;//订单号码
    private TextView tv_checkstand_money;//支付金额

    private String pstatus_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=is_payment";
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
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(CheckStandActivity.this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            finish();
                        }
                    }).show();
            return;
        }
        String orderInfo = getOrderInfo("聚优帮购物", "聚优帮-商家-购物", "0.01");
        //String orderInfo = getOrderInfo("聚优帮购物", "聚优帮-商家-购物", String.valueOf(df.format(pay_price)));
        LogUtil.e("checkStandActivity = " + orderInfo);
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
            LogUtil.e("sign = " + sign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        LogUtil.e("订单信息 = " + payInfo);

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
    public static final String PARTNER = "2088621987751299";//最新
    // 商户收款账号
    public static final String SELLER = "juyobcom@163.com";//最新
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMHGKFpaDsLffolLuKZZH7RalrJ+cGUHhOUKAjxGl4boCsdJt8wDNbWo2Wl0yD35M/f5h5qxhUf4cIX+W0nUm+1lsOl7PdoHY5enllYToxOVeu57BCECMg1S/p446ciczazRBNKGDaIqO6oWT4wauF6t3aoQXz3zr6DFdqfeDJOtAgMBAAECgYEAsGsMBC6n783w82koRPMxhU9QklPBWon8+VYicORIQr/ySDyahLiLZsfCoVt8j8faA7OAPVvQH5VWRPgt/sr95g882GaTmypd5gnvsuBI9xuMK96JBDqHXYLg+26/sP4nHUYEGljIBDgu+CePmHGKRg6N7z5/Wdthptk9R/MXchUCQQDylh2vDMc/SMReiK5jB1kab5hOoEkg9BtNeVMEQnRKmjJ7/RAOVQ/CP4lxCgcrWWh58jnQJwoN6Hgz2En3Uf/DAkEAzH0WlUlR6XEIPTnB9v+jnme8DVTsCMAdMyxtk+6H7pNhkAo5oKwLL8UsckjaW5710lqu0SqhYy+852152ibXzwJBAJOgrWHZKewmUXKiRVX4o84Fua+ntbk7NN5aBh5ifrjjy/NOlxXRyCxTVEYZbzF/UKhds7cr+t9p6LTMXH8JRNcCQQDDn8dC+emK0b94ExEkIlt85vBYMK29kUxd8FrD8qXH7uaezaljtDLfeFmU221JyUCSBH7JS2cpZDnrjCfOMuqxAkAjjayEI8clw0S5P/IEtls3vOvyCvffqaqLu2mzGwJherFD6Pv1gpMhvgFQDzdY5xEiopwxDof5wZaxwd1jWWt1";
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBxihaWg7C336JS7imWR+0WpayfnBlB4TlCgI8RpeG6ArHSbfMAzW1qNlpdMg9+TP3+YeasYVH+HCF/ltJ1JvtZbDpez3aB2OXp5ZWE6MTlXruewQhAjINUv6eOOnInM2s0QTShg2iKjuqFk+MGrherd2qEF8986+gxXan3gyTrQIDAQAB";// 支付宝公钥
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
                        pay_status = "2";
                        //pushCheckStandStatus();
                        LogUtil.e("支付成功 = " + resultInfo);
                        toOrderDetailPage();
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
                        toOrderPage();
                    }
                    break;
                }
                default:

                    break;
            }
        }
    };

    /**
     * 提交支付状态到服务器
     */
    private void pushCheckStandStatus() {
        if (pay_status != null && !TextUtils.isEmpty(pay_status)) {
            map = new HashMap<>();
            map.put("Id", user_login);
            map.put("user_id", user_login);
            map.put("is_payment", pay_status);
            map.put("order_id", String.valueOf(orderId));
            netUtil.okHttp2Server2(CheckStandActivity.this, pstatus_url, map);
        }
    }


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
        orderInfo += "&notify_url=" + "\"" + "http://47.92.118.0:8080/Notify_url.aspx" + "\"";

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
            try {
                parseCheckData(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
            EncryptNetUtil.startNeterrorPage(CheckStandActivity.this);
        }
    }

    /**
     * 解析收银台的数据
     *
     * @param response
     */
    private void parseCheckData(String response) throws Exception {
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

    /**
     * 跳转到订单列表页面
     */
    private void toOrderPage() {
        Intent intent = new Intent(this, SuningOrderActivity.class);
        intent.putExtra("pay_status", pay_status);
        startActivity(intent);
        finish();
    }

    /**
     * 跳转到订单详情界面
     */
    private void toOrderDetailPage() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String order_time = sf.format(System.currentTimeMillis());
        Intent intent = new Intent(this, SuningPayOrderdetailActivity.class);
        intent.putExtra("pay_price", this.df.format(pay_price));
        intent.putExtra("order_num", order_num);
        intent.putExtra("order_time", order_time);
        startActivity(intent);
        finish();
    }
}
