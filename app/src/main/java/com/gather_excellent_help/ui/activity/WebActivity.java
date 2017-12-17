package com.gather_excellent_help.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.ChangeUrlBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.TaoWordBean;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.SharePopupwindow;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.ScreenUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class WebActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.wv_banner)
    WebView wvBanner;
    @Bind(R.id.rl_share)
    RelativeLayout rlShare;
    @Bind(R.id.v_shadow)
    View vShadow;

    private ImageView iv_order_no_zhanwei;
    private String type = "";
    private SharePopupwindow sharePopupwindow;
    private String url;
    private String goods_id;
    private String goods_img = "";
    private String goods_title = "";
    private String openId = "";
    private String avatarUrl = "";
    private String nick = "";

    public static final int GET_URL = 1;
    private String chang_url = Url.BASE_URL + "GetCouponUrl.aspx";
    private String get_url = Url.BASE_URL + "GetTpwd.aspx";
    private String bind_url = Url.BASE_URL + "bindTaobao.aspx";//绑定淘宝
    private NetUtil netUtil;
    private String click_url = "";//转链的url
    private Map<String, String> map;
    private String which = "";
    private String taoWord = "";
    private String adverId = "";
    private String goods_price = "";
    private String goods_coupon = "";
    private String goods_coupon_url = "";
    private String news_img_url = "";
    private String newsTitle = "";
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_URL:
                    which = "get_url";
                    String user_id = Tools.getUserLogin(WebActivity.this);
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
    private String web_url = "";
    private AlibcLogin alibcLogin;
    private AlertDialog dialog;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_order_no_zhanwei = (ImageView) findViewById(R.id.iv_order_no_zhanwei);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        netUtil = new NetUtil();
        web_url = intent.getStringExtra("web_url");
        if (web_url == null) {
            web_url = "";
        }
        type = intent.getStringExtra("type");
        rlShare.setVisibility(View.VISIBLE);
        if (type != null) {
            if (type.equals("detail")) {
                tvTopTitleName.setText("新闻专区");
                String news_img = intent.getStringExtra("news_img");
                if (news_img != null) {
                    news_img_url = news_img;
                }
                String news_title = intent.getStringExtra("news_title");
                if (news_title != null) {
                    newsTitle = news_title;
                }
                wvBanner.loadUrl(web_url);
            } else if (type.equals("second")) {
                tvTopTitleName.setText("优惠券专区");
                wvBanner.loadUrl(web_url);
                rlShare.setVisibility(View.GONE);
            } else {
                tvTopTitleName.setText("优惠券专区");
                url = intent.getStringExtra("url");
                goods_id = intent.getStringExtra("goods_id");
                goods_img = intent.getStringExtra("goods_img");
                goods_title = intent.getStringExtra("goods_title");
                goods_price = intent.getStringExtra("goods_price");
                goods_coupon = intent.getStringExtra("goods_coupon");
                goods_coupon_url = intent.getStringExtra("goods_coupon_url");
                if (goods_id == null || goods_img == null) {
                    rlShare.setVisibility(View.GONE);
                } else {
                    rlShare.setVisibility(View.VISIBLE);
                    boolean login = Tools.isLogin(this);
                    adverId = Tools.getAdverId(this);
                    if (login) {
                        boolean bindTao = Tools.isBindTao(this);
                        if (bindTao) {
                            showCatView();
                            LogUtil.e("adverId = " + adverId);
                            which = "change_url";
                            map = new HashMap<>();
                            map.put("goodsId", goods_id);
                            map.put("adzoneId", adverId);
                            netUtil.okHttp2Server2(chang_url, map);
                        } else {
                            String userLogin = Tools.getUserLogin(this);
                            showBindTaobaoDialog(userLogin);
                        }
                    } else {
                        toLogin();
                        finish();
                    }

                }
            }
        } else {
            tvTopTitleName.setText("优惠券专区");
            rlShare.setVisibility(View.VISIBLE);
            url = intent.getStringExtra("url");
            goods_id = intent.getStringExtra("goods_id");
            goods_img = intent.getStringExtra("goods_img");
            goods_title = intent.getStringExtra("goods_title");
            goods_price = intent.getStringExtra("goods_price");
            goods_coupon = intent.getStringExtra("goods_coupon");
            goods_coupon_url = intent.getStringExtra("goods_coupon_url");
            if (goods_id == null || goods_img == null) {
                rlShare.setVisibility(View.GONE);
            } else {
                rlShare.setVisibility(View.VISIBLE);
                boolean login = Tools.isLogin(this);
                adverId = Tools.getAdverId(this);
                if (login) {
                    boolean bindTao = Tools.isBindTao(this);
                    if (bindTao) {
                        showCatView();
                        LogUtil.e("adverId = " + adverId);
                        which = "change_url";
                        map = new HashMap<>();
                        map.put("goodsId", goods_id);
                        map.put("adzoneId", adverId);
                        netUtil.okHttp2Server2(chang_url, map);
                    } else {
                        String userLogin = Tools.getUserLogin(this);
                        showBindTaobaoDialog(userLogin);
                    }
                } else {
                    toLogin();
                    finish();
                }
            }
        }
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
        wvBanner.setWebViewClient(new MyWebViewClient());
        rlExit.setOnClickListener(new MyOnclickListener());
        rlShare.setOnClickListener(new MyOnclickListener());
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
                hindCatView(1);
            }
        });
    }

    /**
     * 展示绑定淘宝的dialog
     */
    private void showBindTaobaoDialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("绑定淘宝账号")
                .setMessage("您需要绑定淘宝账号，若取消绑定将会在您查看商品详情时提示您继续绑定操作")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bindTaobao(id);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        alertDialog = builder.create();
        if (WebActivity.this != null && !WebActivity.this.isFinishing()) {
            alertDialog.show();
        }
    }

    /**
     * 显示catView
     */
    private void showCatView() {
        View inflate = View.inflate(this, R.layout.loading_dialog_view, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflate);
        alertDialog = builder.create();
        if (WebActivity.this != null && !WebActivity.this.isFinishing()) {
            alertDialog.show();
        }
        alertDialog.getWindow().setLayout(ScreenUtil.getScreenWidth(this) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 隐藏catView
     */
    private void hindCatView(final int w) {
        if (WebActivity.this != null && !WebActivity.this.isFinishing()) {
            if (alertDialog != null && alertDialog.isShowing()) {
                View view = new View(WebActivity.this);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                    }
                }, 1000);
            }
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
                CacheUtils.putBoolean(WebActivity.this, CacheUtils.BIND_STATE, true);
                CacheUtils.putString(WebActivity.this, CacheUtils.TAOBAO_NICK, nick);
                initData();
                break;
            case 0:
                Toast.makeText(WebActivity.this, "绑定淘宝失败", Toast.LENGTH_SHORT).show();
                break;
        }
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
                    //加载需要显示的网页
                    wvBanner.loadUrl(click_url);
                    if (handler != null) {
                        handler.sendEmptyMessage(GET_URL);
                    }
                    hindCatView(0);
                }
                break;
            case 0:
                Toast.makeText(WebActivity.this, changeUrlBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                hindCatView(1);
                iv_order_no_zhanwei.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void toLogin() {
        Toast.makeText(WebActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    /**
     * 绑定淘宝
     *
     * @param s
     */
    public void bindTaobao(final String s) {

        alibcLogin = AlibcLogin.getInstance();

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


    public class MyWebViewClient extends WebViewClient {
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

    public class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.rl_share:
                    if (Build.VERSION.SDK_INT >= 23) {
                        String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                        ActivityCompat.requestPermissions(WebActivity.this, mPermissionList, STORAGE_PERMISSIONS_REQUEST_CODE);
                    } else {
                        shareWareUrl();
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    shareWareUrl();
                } else {
                    Toast.makeText(WebActivity.this, "请允许打开操作SDCard权限！！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 商品转链分享
     */
    private void shareWareUrl() {
        showPopMenu();
        sharePopupwindow.setOnItemClickListenr(new SharePopupwindow.OnItemClickListenr() {
            @Override
            public void onQQClick() {
                if (type != null) {
                    if (type.equals("detail")) {
                        shareDiffSolfplamNews(SHARE_MEDIA.QQ);
                    } else {
                        showCopyDialog(SHARE_MEDIA.QQ);
                    }
                } else {
                    showCopyDialog(SHARE_MEDIA.QQ);
                }
            }

            @Override
            public void onWeixinClick() {
                if (type != null) {
                    if (type.equals("detail")) {
                        shareDiffSolfplamNews(SHARE_MEDIA.WEIXIN);
                    } else {
                        showCopyDialog(SHARE_MEDIA.WEIXIN);
                    }
                } else {
                    showCopyDialog(SHARE_MEDIA.WEIXIN);
                }
            }

            @Override
            public void onSinaClick() {
                if (type != null) {
                    if (type.equals("detail")) {
                        shareDiffSolfplamNews(SHARE_MEDIA.SINA);
                    } else {
                        showCopyDialog(SHARE_MEDIA.SINA);
                    }
                } else {
                    showCopyDialog(SHARE_MEDIA.SINA);
                }
            }

            @Override
            public void onWeixinFriendClick() {
                if (type != null) {
                    if (type.equals("detail")) {
                        shareDiffSolfplamNews(SHARE_MEDIA.WEIXIN_CIRCLE);
                    } else {
                        showCopyDialog(SHARE_MEDIA.WEIXIN_CIRCLE);
                    }
                } else {
                    showCopyDialog(SHARE_MEDIA.WEIXIN_CIRCLE);
                }
            }
        });
    }

    private void showPopMenu() {
        vShadow.setVisibility(View.VISIBLE);
        if (sharePopupwindow == null) {
            sharePopupwindow = new SharePopupwindow(WebActivity.this, vShadow);
            sharePopupwindow.showAtLocation(wvBanner, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else if (sharePopupwindow != null
                && sharePopupwindow.isShowing()) {
            sharePopupwindow.dismiss();
        } else {
            sharePopupwindow.showAtLocation(wvBanner, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }

    }

    /**
     * 分享淘口令链接到不同给的平台
     *
     * @param platform
     */
    private void shareDiffSolfplam(SHARE_MEDIA platform) {
        UMImage image = new UMImage(WebActivity.this, goods_img);//网络图片
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

    /**
     * 分享淘口令链接到不同给的平台
     *
     * @param platform
     */
    private void shareDiffSolfplamNews(SHARE_MEDIA platform) {
        UMImage image = new UMImage(WebActivity.this, news_img_url);//网络图片
        UMImage thumb = new UMImage(this, R.mipmap.juyoubang_logo);
        image.setThumb(thumb);
        UMWeb web = new UMWeb(web_url);
        web.setTitle(newsTitle);//标题
        web.setThumb(image);  //缩略图
        web.setDescription("快来看看这条新闻吧");//描述
        new ShareAction(this)
                .setPlatform(platform)//传入平台
                .withMedia(web)//分享内容
                .setCallback(shareListener)//回调监听器
                .share();
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
        final String share_content = "商品名称:" + goods_title + "\n商品价格￥" + goods_price + "\n优惠券" + goods_coupon + "元" + "\n复制这条消息:" + taoWord + "\n去打开手机淘宝";
        tvCopyContent.setText(share_content);
        dialog = builder.setView(inflate)
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
            Toast.makeText(WebActivity.this, "分享成功", Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (sharePopupwindow != null && sharePopupwindow.isShowing()) {
                sharePopupwindow.dismiss();
            }
            if (vShadow != null) {
                vShadow.setVisibility(View.GONE);
            }
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(WebActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (sharePopupwindow != null && sharePopupwindow.isShowing()) {
                sharePopupwindow.dismiss();
            }
            if (vShadow != null) {
                vShadow.setVisibility(View.GONE);
            }
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(WebActivity.this, "分享取消", Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (sharePopupwindow != null && sharePopupwindow.isShowing()) {
                sharePopupwindow.dismiss();
            }
            if (vShadow != null) {
                vShadow.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        if (alibcLogin != null) {
            alibcLogin = null;
        }
        AlibcTradeSDK.destory();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        System.gc();
    }
}
