package com.gather_excellent_help.ui.fragment.dragfragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.suning.SuningWareBean;
import com.gather_excellent_help.ui.widget.CustWebView;
import com.google.gson.Gson;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;


public class VerticalFragment3 extends Fragment {

    private CustWebView webview;
    private ProgressBar progressBar;
    private boolean hasInited = false;


    private String response = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (!TextUtils.isEmpty(response)) {
                    SuningWareBean suningWareBean = new Gson().fromJson(response, SuningWareBean.class);
                    int statusCode = suningWareBean.getStatusCode();
                    switch (statusCode) {
                        case 1:
                            List<SuningWareBean.DataBean> data = suningWareBean.getData();
                            if (data != null && data.size() > 0) {
                                SuningWareBean.DataBean dataBean = data.get(0);
                                String info_url = dataBean.getInfo_url();
                                webview.loadUrl(Url.IMG_URL+ info_url);
                            }
                            break;
                        case 0:

                            break;
                    }
                    handler.removeMessages(1);
                } else {
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vertical_fragment3, null);
        webview = (CustWebView) rootView.findViewById(R.id.fragment3_webview);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
        return rootView;
    }

    public void initView() {
        if (null != webview && !hasInited) {
            hasInited = true;
            progressBar.setVisibility(View.GONE);
            WebSettings webSettings = webview.getSettings();
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
            webview.setWebChromeClient(new WebChromeClient());
            webview.setWebViewClient(new WebViewClient());
            handler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    /**
     * 设置数据
     *
     * @param response
     */
    public void setResponse(String response) {
        this.response = response;
    }
}
