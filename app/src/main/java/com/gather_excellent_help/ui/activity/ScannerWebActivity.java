package com.gather_excellent_help.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.gather_excellent_help.R;
import com.gather_excellent_help.aliapi.DemoTradeCallback;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class ScannerWebActivity extends BaseActivity {

    @Bind(R.id.wv_scanner_web)
    WebView wvScannerWeb;

    private String scaner_url;
    private NetUtil netUtil;
    private Intent intent;

    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private AlibcTaokeParams alibcTaokeParams = null;//淘客参数，包括pid，unionid，subPid
    private DemoTradeCallback demoTradeCallback;
    private Map<String, String> exParams;//yhhpass参数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_web);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 加载数据
     */
    private void initData(){
        WebSettings webSettings = wvScannerWeb.getSettings();
        //设置此属性，可任意比例缩放
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //使页面支持缩放：
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        //支持js
        webSettings.setJavaScriptEnabled(true);  //支持js
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片

        //设置Web视图
        wvScannerWeb.setWebViewClient(new MyWebViewClient());
        wvScannerWeb.setWebChromeClient(new WebChromeClient());
        alibcShowParams = new AlibcShowParams(OpenType.H5, false);
        exParams = new HashMap<>();
        exParams.put("isv_code", "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
        demoTradeCallback = new DemoTradeCallback(ScannerWebActivity.this);
        intent = getIntent();
        scaner_url = intent.getStringExtra("scaner_url");
        int url_type = intent.getIntExtra("url_type", -1);
        if(url_type == 1) {
            AlibcTrade.show(this, wvScannerWeb, new WebViewClient(), null, new AlibcPage(scaner_url), alibcShowParams, alibcTaokeParams, null,demoTradeCallback);
        }else if(url_type == 2) {
            wvScannerWeb.loadUrl(scaner_url);
        }
        netUtil = new NetUtil();
        netUtil.setOnServerResponseListener(new OnServerResponseListener());
    }

    public class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener{

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "---" + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(intent!=null) {
            intent = null;
        }
    }
}
