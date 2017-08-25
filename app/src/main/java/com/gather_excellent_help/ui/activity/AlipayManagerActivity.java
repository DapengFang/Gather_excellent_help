package com.gather_excellent_help.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.api.pay.PayResult;
import com.gather_excellent_help.api.pay.SignUtils;
import com.gather_excellent_help.bean.ApplyMoneyBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class AlipayManagerActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.wv_banner)
    WebView wvBanner;
    private String url = "http://juyoubang.com.h001.webe7.com/api/share/paycost.html";
    private String key;
    private String succ_url = "http://juyoubang.com.h001.webe7.com/api/share/success.html";
    private String fail_url = "http://juyoubang.com.h001.webe7.com/api/share/failure.html";
    private String money_url = Url.BASE_URL + "AppSystem.aspx";
    private NetUtil netUtil;
    private double enterAmount;
    private String extract_url = Url.BASE_URL + "PayStatus.aspx";
    private Map<String,String> map;
    private String pay_status = "";
    private String which = "";
    private String user_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay_manager);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    @SuppressLint("JavascriptInterface")
    private void initData() {
        tvTopTitleName.setText("加盟支付");
        netUtil = new NetUtil();
        user_login = Tools.getUserLogin(this);
        rlExit.setOnClickListener(new MyOnClickListener());
        WebSettings webSettings = wvBanner.getSettings();
        //设置此属性，可任意比例缩放
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //使页面支持缩放：
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        //支持js
        webSettings.setJavaScriptEnabled(true);  //支持js
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(false);  //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        //设置Web视图
        wvBanner.setWebViewClient(new MyWebViewClient());
        //加载需要显示的网页
        wvBanner.loadUrl(url);

        wvBanner.addJavascriptInterface(AlipayManagerActivity.this, "AlipayInterface");
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                if(which.equals("money")) {
                    parseMoneyData(response);
                }else if(which.equals("extract")) {
                   parseExtractData(response);
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
            Toast.makeText(AlipayManagerActivity.this, "网络请求出错！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 解析支付入驻数据
     * @param response
     */
    private void parseExtractData(String response) {
        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
        //Toast.makeText(AlipayManagerActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
        LogUtil.e(codeStatueBean.getStatusMessage());
    }

    /**
     * 解析支付金额的数据
     * @param response
     */
    private void parseMoneyData(String response) {
        ApplyMoneyBean applyMoneyBean = new Gson().fromJson(response, ApplyMoneyBean.class);
        int statusCode = applyMoneyBean.getStatusCode();
        switch (statusCode) {
            case 1:
                List<ApplyMoneyBean.DataBean> data = applyMoneyBean.getData();
                if (data != null && data.size() > 0) {
                    ApplyMoneyBean.DataBean dataBean = data.get(0);
                    enterAmount = dataBean.getEnterAmount();
                    if (enterAmount > 0) {
                        DecimalFormat df = new DecimalFormat("#0.00");
                        String price = df.format(enterAmount);
                        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
                            new AlertDialog.Builder(AlipayManagerActivity.this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialoginterface, int i) {
                                            finish();
                                        }
                                    }).show();
                            return;
                        }
                        key = String.valueOf(System.currentTimeMillis());
                        String orderInfo = getOrderInfo("聚优帮加盟", "聚优帮-商家-加盟", price);
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

                        LogUtil.e(payInfo);

                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                // 构造PayTask 对象
                                PayTask alipay = new PayTask(AlipayManagerActivity.this);
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
                }
                break;
            case 0:
                Toast.makeText(AlipayManagerActivity.this, applyMoneyBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //Web视图
    private class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, final String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
            }
        }
    }

    // 商户PID
    public static final String PARTNER = "2088621736065035";
    // 商户收款账号
    public static final String SELLER = "jurenyunshang@163.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKAXUb5ILO3ZGkzD\n" +
            "LYldxiOOCVEKkWImcZusaFYOHAGqwAbwmtReguAqe48XwuwQ9HxLD4ob+fQilQoa\n" +
            "vgNrrre+VL8w6JyCUTYsf93sySa0ZhlOSTaC9WzkZmy2RGTIm+m4XvsBR+7YxFz1\n" +
            "oVxTqLBbpGPl/Yv52JZCSzfai6MlAgMBAAECgYBYRsOSc2Yi+XYhIfb4KjdZ37aP\n" +
            "c45bWuuGdflKipky6h7dNMuLk1oDlilv7CBV37uQ1isJcwfPcUpRuhFQ/Y3ncMwR\n" +
            "jtXcA+rjijwNpDbcSCCxbeiUJ86AuCZlPcjrCut4HT4bJ37n7OsYxlJDTd002ftm\n" +
            "jkSYDyp/aXEl6LMbwQJBAMyBeVlW3PT6mln6Li+6Hz2uzP5QtbsptLrcyN2JZNLZ\n" +
            "ETl0CxTr/druLlLi7lHuFnVwtaIpVvL0R5Fa7QAan6kCQQDIZtuBZ3q2OHL0QkbE\n" +
            "PQAhxcCkDqWkg1TwkyMH2hBwPTTk03GpYxJqNsRaJ4Bo8+Qhc0bTzyzajnW9BRhx\n" +
            "50UdAkAlQu3+VjJaPJMFE+14arwofAID0GbcXEP+zB2ZJ+CD/mMCCd+/BySw0DcG\n" +
            "iSyNJwlWipu9mTSrfdDQbjXCkBdJAkAPJT7nq8NpbCC79xpUEGwnIUPq4jSAl3Be\n" +
            "8i/Okxt08BtEQ/quG4+zuUnjqmKZC/2szYKH1XuFVQju7Sioyxq9AkA1ldoHdbbN\n" +
            "cHP8Zpc4Hpz+YIIGJhVifl3rNLAi2CqngrQFjmJxNa0GLkTSc4/zE4L4Q1UKEM2c\n" +
            "qs8s8NwJW5RL";   // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgF1G+SCzt2RpMwy2JXcYjjglRCpFiJnGbrGhWDhwBqsAG8JrUXoLgKnuPF8LsEPR8Sw+KG/n0IpUKGr4Da663vlS/MOicglE2LH/d7MkmtGYZTkk2gvVs5GZstkRkyJvpuF77AUfu2MRc9aFcU6iwW6Rj5f2L+diWQks32oujJQIDAQAB";
    private static final int SDK_PAY_FLAG = 1;

    @JavascriptInterface
    public void toPay() {
        which = "money";
        netUtil.okHttp2Server2(money_url, null);
    }

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
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(AlipayManagerActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        wvBanner.loadUrl(succ_url);
                        pay_status = "1";
                        MobclickAgent.onEvent(AlipayManagerActivity.this, "a_pay");
                    } else {
                        pay_status = "0";
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(AlipayManagerActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                            wvBanner.loadUrl(fail_url);
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(AlipayManagerActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                            wvBanner.loadUrl(fail_url);
                        }
                    }
                    if(pay_status!=null && !TextUtils.isEmpty(pay_status)) {
                        map = new HashMap<>();
                        map.put("user_id",user_login);
                        map.put("pay_status",pay_status);
                        which = "extract";
                        netUtil.okHttp2Server2(extract_url,map);
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
        orderInfo += "&out_trade_no=" + "\"" + key + "\"";

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


}
