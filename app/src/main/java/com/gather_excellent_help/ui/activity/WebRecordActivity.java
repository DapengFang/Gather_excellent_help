package com.gather_excellent_help.ui.activity;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
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
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.gather_excellent_help.R;
import com.gather_excellent_help.aliapi.DemoTradeCallback;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.ChangeUrlBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.TaoWordBean;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.SharePopupwindow;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
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
    @Bind(R.id.v_shadow)
    View vShadow;
    private String url;
    private String chang_url = Url.BASE_URL + "GoodsConvert.aspx";
    private String get_url = Url.BASE_URL + "GetTpwd.aspx";
    private String bind_url = Url.BASE_URL + "bindTaobao.aspx";//绑定淘宝
    private String goods_id = "";
    public static final int GET_URL = 1;
    private String which = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_URL:
                    which = "get_url";
                    String user_id = Tools.getUserLogin(WebRecordActivity.this);
                    map = new HashMap<>();
                    map.put("user_id", user_id);
                    map.put("convert_url", click_url);
                    map.put("img_url", goods_img);
                    map.put("title", goods_title);
                    netUtil.okHttp2Server2(get_url, map);
                    break;
            }
        }
    };


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
    private String taoWord;

    private SharePopupwindow sharePopupwindow;
    private String openId = "";
    private String avatarUrl = "";
    private String nick = "";
    private String adverId = "";
    private String goods_price = "";

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
        which = "";
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        goods_id = intent.getStringExtra("goods_id");
        goods_img = intent.getStringExtra("goods_img");
        goods_title = intent.getStringExtra("goods_title");
        goods_price = intent.getStringExtra("goods_price");

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
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        //webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        //设置Web视图
        //wvBanner.setWebViewClient(new MyWebViewClient());
        //加载需要显示的网页
        //wvBanner.loadUrl(url);
        netUtil = new NetUtil();
        alibcShowParams = new AlibcShowParams(OpenType.H5, false);
        exParams = new HashMap<>();
        exParams.put("isv_code", "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
        rlExit.setOnClickListener(new MyOnclickListener());
        rlShare.setOnClickListener(new MyOnclickListener());
        boolean login = Tools.isLogin(this);
        adverId = Tools.getAdverId(this);
        LogUtil.e("adverId================" + adverId);
        if (login) {
            boolean bindTao = Tools.isBindTao(this);
            if (bindTao) {
                LogUtil.e("adverId = " + adverId);
                which = "change_url";
                map = new HashMap<>();
                map.put("goodsId", goods_id);
                map.put("adzoneId", adverId);
                netUtil.okHttp2Server2(chang_url, map);
            } else {
                Toast.makeText(WebRecordActivity.this, "请先绑定淘宝账号！", Toast.LENGTH_SHORT).show();
                String userLogin = Tools.getUserLogin(this);
                bindTaobao(userLogin);
            }
        } else {
            toLogin();
            finish();
        }

        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                if (which.equals("change_url")) {
                    parseData(response);
                } else if (which.equals("get_url")) {
                    getTaoWord(response);
                } else if (which.equals("bind")) {
                    parseBindData(response);
                }

            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "--" + e.getMessage());
                // AlibcTrade.show(WebRecordActivity.this, wvBanner, new MyWebViewClient(), null, new AlibcPage(url), alibcShowParams, alibcTaokeParams, null, new DemoTradeCallback(WebRecordActivity.this));

            }
        });

    }

    /**
     * 获取淘口令
     *
     * @param response
     */
    private void getTaoWord(String response) {
        TaoWordBean taoWordBean = new Gson().fromJson(response, TaoWordBean.class);
        int statusCode = taoWordBean.getStatusCode();
        switch (statusCode) {
            case 1:
                taoWord = taoWordBean.getData();
                LogUtil.e(taoWord);
                break;
        }

    }

    /**
     * @param response 解析数据
     */
    private void parseData(String response) {
        LogUtil.e("click_url = " + response);
        ChangeUrlBean changeUrlBean = new Gson().fromJson(response, ChangeUrlBean.class);
        int statusCode = changeUrlBean.getStatusCode();
        switch (statusCode) {
            case 1:
                List<ChangeUrlBean.DataBean> data = changeUrlBean.getData();
                if (data != null && data.size() > 0) {
                    click_url = changeUrlBean.getData().get(0).getClick_url();
                    handler.sendEmptyMessage(GET_URL);
                    AlibcTrade.show(this, wvBanner, new MyWebViewClient(), null, new AlibcPage(click_url), alibcShowParams, alibcTaokeParams, null, new DemoTradeCallback(WebRecordActivity.this));
                } else {
                    AlibcTrade.show(this, wvBanner, new MyWebViewClient(), null, new AlibcPage(url), alibcShowParams, alibcTaokeParams, null, new DemoTradeCallback(WebRecordActivity.this));
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

    /**
     * 监听点击事件的类
     */
    public class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.rl_share:
                    boolean login = Tools.isLogin(WebRecordActivity.this);
                    if (!login) {
                        toLogin();
                        return;
                    }
                    if (!TextUtils.isEmpty(click_url)) {
                        shareWareUrl();
                    } else {
                        Toast.makeText(WebRecordActivity.this, "商品已下架，无法分享！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private void toLogin() {
        Toast.makeText(WebRecordActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 商品转链分享
     */
    private void shareWareUrl() {
        vShadow.setVisibility(View.VISIBLE);
        showPopMenu();
        sharePopupwindow.setOnItemClickListenr(new SharePopupwindow.OnItemClickListenr() {
            @Override
            public void onQQClick() {
                showCopyDialog(SHARE_MEDIA.QQ);
            }

            @Override
            public void onWeixinClick() {
                showCopyDialog(SHARE_MEDIA.WEIXIN);
            }

            @Override
            public void onSinaClick() {
                showCopyDialog(SHARE_MEDIA.SINA);
            }

        });
    }

    /**
     * 剪切板剪切淘口令
     *
     * @param paltform
     */
    private void showCopyDialog(final SHARE_MEDIA paltform) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = View.inflate(this, R.layout.item_copy_taoword_dialog, null);
        TextView tvCopyContent = (TextView) inflate.findViewById(R.id.tv_copy_taoword_content);
        TextView tvCopyDismiss = (TextView) inflate.findViewById(R.id.tv_copy_taoword_dismiss);
        TextView tvCopyShare = (TextView) inflate.findViewById(R.id.tv_copy_taoword_share);
        final String share_content = "商品名称:" + goods_title + "\n商品价格￥" + goods_price + "\n复制这条消息:" + taoWord + "\n去打开手机淘宝";
        tvCopyContent.setText(share_content);
        final AlertDialog dialog = builder.setView(inflate)
                .show();

        tvCopyDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        tvCopyShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(share_content);
                shareDiffSolfplam(paltform);
            }
        });
    }

    /**
     * 分享淘口令链接到不同给的平台
     *
     * @param platform
     */
    private void shareDiffSolfplam(SHARE_MEDIA platform) {
        LogUtil.e(goods_img);
        UMImage image = new UMImage(WebRecordActivity.this, goods_img);//网络图片
        UMImage thumb = new UMImage(this, R.mipmap.juyoubang_logo);
        image.setThumb(thumb);
        UMWeb web = new UMWeb(click_url);
        web.setTitle(goods_title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription("我在聚优帮看到了一件不错的商品,你也看看吧");//描述
        new ShareAction(this)
                .setPlatform(platform)//传入平台
                .withMedia(web)//分享内容
                .setCallback(shareListener)//回调监听器
                .share();
    }


    private void showPopMenu() {
        if (sharePopupwindow == null) {
            sharePopupwindow = new SharePopupwindow(WebRecordActivity.this,vShadow);
            sharePopupwindow.showAtLocation(wvBanner, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else if (sharePopupwindow != null
                && sharePopupwindow.isShowing()) {
            sharePopupwindow.dismiss();
        } else {
            sharePopupwindow.showAtLocation(wvBanner, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(WebRecordActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(WebRecordActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(WebRecordActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };

    /**
     * 绑定淘宝
     *
     * @param s
     */
    public void bindTaobao(final String s) {

        AlibcLogin alibcLogin = AlibcLogin.getInstance();

        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                //获取淘宝用户信息
                LogUtil.e("获取淘宝用户信息: " + AlibcLogin.getInstance().getSession());
                LogUtil.e("代码:" + i);
                openId = AlibcLogin.getInstance().getSession().openId;
                avatarUrl = AlibcLogin.getInstance().getSession().avatarUrl;
                nick = AlibcLogin.getInstance().getSession().nick;
                uploadUserInfo(s);
                which = "bind";
                netUtil.okHttp2Server2(bind_url, map);
            }

            @Override
            public void onFailure(int code, String msg) {
//                Toast.makeText(LoginActivity.this, "绑定失败 ！",
//                        Toast.LENGTH_LONG).show();
//                Log.i("GGG", "错误码" + code + "原因" + msg);
            }
        });
    }

    /**
     * 上传用户信息
     *
     * @param s
     */
    public void uploadUserInfo(String s) {

        if (!TextUtils.isEmpty(s)) {
            map = new HashMap<>();
            map.put("Id", s);
            map.put("openId", openId);
            map.put("portrait", avatarUrl);
            map.put("nickname", nick);
        }

    }

    /**
     * 解析绑定淘宝的数据
     *
     * @param response
     */
    private void parseBindData(String response) {
        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
        int statusCode = codeStatueBean.getStatusCode();
        switch (statusCode) {
            case 1:
                CacheUtils.putBoolean(WebRecordActivity.this, CacheUtils.BIND_STATE, true);
                CacheUtils.putString(WebRecordActivity.this, CacheUtils.TAOBAO_NICK, nick);
                which = "change_url";
                map = new HashMap<>();
                map.put("goodsId", goods_id);
                map.put("adzoneId", adverId);
                netUtil.okHttp2Server2(chang_url, map);
                break;
            case 0:
                Toast.makeText(WebRecordActivity.this, "绑定淘宝失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}



