package com.gather_excellent_help.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.gather_excellent_help.R;
import com.gather_excellent_help.aliapi.DemoTradeCallback;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebRecordActivity extends BaseActivity {

    @Bind(R.id.wv_banner)
    WebView wvBanner;
    @Bind(R.id.rl_share)
    RelativeLayout rlShare;
    private String url;

    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private AlibcTaokeParams alibcTaokeParams = null;//淘客参数，包括pid，unionid，subPid
    private Boolean isTaoke = false;//是否是淘客商品类型
    private String itemId = "522166121586";//默认商品id
    private String shopId = "60552065";//默认店铺id
    private Map<String, String> exParams;//yhhpass参数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_record);
        ButterKnife.bind(this);
        rlShare.setVisibility(View.VISIBLE);
        initData();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        WebSettings webSettings = wvBanner.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        //使页面支持缩放：
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        //支持js
        webSettings.setJavaScriptEnabled(true);  //支持js
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(false);  //将图片调整到适合webview的大小
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        //webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        LogUtil.e(url);
        //设置Web视图
        //wvBanner.setWebViewClient(new MyWebViewClient());
        //加载需要显示的网页
        //wvBanner.loadUrl(url);

        alibcShowParams = new AlibcShowParams(OpenType.H5, false);
        exParams = new HashMap<>();
        exParams.put("isv_code", "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
        AlibcTrade.show(this, wvBanner, new MyWebViewClient(), null, new AlibcPage(url), alibcShowParams, alibcTaokeParams, null, new DemoTradeCallback());
    }




    //Web视图
    private class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return false;
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

    public class MyOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit :
                    finish();
                    break;
                case R.id.rl_share:
                    shareWareUrl();
                    break;
            }
        }
    }

    /**
     * 商品转链
     */
    private void shareWareUrl() {

    }

}



