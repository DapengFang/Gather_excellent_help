package com.gather_excellent_help.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebActivity extends BaseActivity {

    @Bind(R.id.wv_banner)
    WebView wvBanner;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
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
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        //加载需要显示的网页
        wvBanner.loadUrl(url);
        //设置Web视图
        wvBanner.setWebViewClient(new MyWebViewClient ());
    }

    //Web视图
    private class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
