package com.gather_excellent_help.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.gather_excellent_help.R;
import com.gather_excellent_help.aliapi.DemoTradeCallback;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.ChangeUrlBean;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;

public class WebRecordActivity extends BaseActivity {

    @Bind(R.id.wv_banner)
    WebView wvBanner;
    @Bind(R.id.rl_share)
    RelativeLayout rlShare;
    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.activity_web)
    LinearLayout activityWeb;
    private String url;
    private String chang_url = Url.BASE_URL + "GoodsConvert.aspx";
    private String goods_id = "";

    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private AlibcTaokeParams alibcTaokeParams = null;//淘客参数，包括pid，unionid，subPid
    private Boolean isTaoke = false;//是否是淘客商品类型
    private String itemId = "522166121586";//默认商品id
    private String shopId = "60552065";//默认店铺id
    private Map<String, String> exParams;//yhhpass参数
    private Map<String, String> map;
    private NetUtil netUtil;
    private String click_url = "";//转链的url
    private String goods_img = "";
    private String goods_title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_record);
        ButterKnife.bind(this);
        rlShare.setVisibility(View.VISIBLE);
        tvTopTitleName.setText("商品详情");
        initData();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        goods_id = intent.getStringExtra("goods_id");
        goods_img = intent.getStringExtra("goods_img");
        goods_title = intent.getStringExtra("goods_title");
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
               rlExit.setOnClickListener(new MyOnclickListener());
        rlShare.setOnClickListener(new MyOnclickListener());
        netUtil = new NetUtil();
        map = new HashMap<>();
        map.put("goodsId",goods_id);
        map.put("adzoneId","");
        netUtil.okHttp2Server2(chang_url,map);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });

    }

    /**
     * @param response
     * 解析数据
     */
    private void parseData(String response) {
        LogUtil.e(response);
        ChangeUrlBean changeUrlBean = new Gson().fromJson(response, ChangeUrlBean.class);
        int statusCode = changeUrlBean.getStatusCode();
        switch (statusCode) {
            case 1 :
                if(changeUrlBean.getData()!=null && changeUrlBean.getData().size()>0) {
                    click_url = changeUrlBean.getData().get(0).getClick_url();
                    if(click_url!=null && !TextUtils.isEmpty(click_url)) {
                        AlibcTrade.show(this, wvBanner, new MyWebViewClient(), null, new AlibcPage(click_url), alibcShowParams, alibcTaokeParams, null, new DemoTradeCallback());
                    }else{
                        AlibcTrade.show(this, wvBanner, new MyWebViewClient(), null, new AlibcPage(url), alibcShowParams, alibcTaokeParams, null, new DemoTradeCallback());
                    }
                }
                break;
            case 0:
                Toast.makeText(WebRecordActivity.this, changeUrlBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
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

    public class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.rl_share:
                    if(!TextUtils.isEmpty(click_url)) {
                        Toast.makeText(WebRecordActivity.this, "正在加载，请稍后！", Toast.LENGTH_SHORT).show();
                        shareWareUrl();
                    }else{
                        Toast.makeText(WebRecordActivity.this, "商品已下架，无法分享！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    /**
     * 商品转链
     */
    private void shareWareUrl() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(goods_title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(click_url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我在聚优帮上发现了一个不错的商品，快来看看吧。");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(goods_img);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(click_url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(goods_title);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(goods_title);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(click_url);

        // 启动分享GUI
        oks.show(this);

    }

}



