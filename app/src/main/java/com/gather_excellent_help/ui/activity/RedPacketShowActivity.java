package com.gather_excellent_help.ui.activity;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.TaoWordBean;
import com.gather_excellent_help.ui.activity.shop.WhichJoinActivity;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.SharePopupwindow;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class RedpacketShowActivity extends BaseActivity {


    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;
    private RelativeLayout rl_share;
    private WebView wv_banner;
    private View v_shadow;

    private SharePopupwindow sharePopupwindow;
    private String redpacket_url;

    private String get_url = Url.BASE_URL + "GetTpwdNew.aspx";
    private NetUtil netUtil;
    private Map<String, String> map;
    private String taoWord;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_packet_show);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
        rl_share = (RelativeLayout) findViewById(R.id.rl_share);
        wv_banner = (WebView) findViewById(R.id.wv_banner);
        v_shadow = findViewById(R.id.v_shadow);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //初始化
        netUtil = new NetUtil();
        tv_top_title_name.setText("福利专区");
        rl_share.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        redpacket_url = intent.getStringExtra("redpacket_url");
        if (redpacket_url == null) {
            redpacket_url = "";
        }
        //设置点击事件
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        rl_share.setOnClickListener(myonclickListener);
        //WebView配置
        WebSettings webSettings = wv_banner.getSettings();
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
        wv_banner.setWebViewClient(new MyWebviewClient());
        wv_banner.loadUrl(redpacket_url);
        String userLogin = Tools.getUserLogin(this);
        map = new HashMap<>();
        map.put("user_id", userLogin);
        map.put("convert_url", redpacket_url);
        map.put("img_url", "http://app.juyob.com/hbback.jpg");
        map.put("title", "每天都有红包，最高1111元！快分享给小伙伴一起来抢吧！");
        netUtil.okHttp2Server2(get_url, map);
        netUtil.setOnServerResponseListener(new OnServerResponseListener());
    }

    /**
     * 处理页面上的点击事件
     */
    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.rl_share:
                    shareWareUrl();
                    break;
            }
        }
    }

    /**
     * 商品转链分享
     */
    private void shareWareUrl() {
        v_shadow.setVisibility(View.VISIBLE);
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

            @Override
            public void onWeixinFriendClick() {
                showCopyDialog(SHARE_MEDIA.WEIXIN_CIRCLE);
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
        final String share_content = "每天都有红包，最高1111元！\n快分享给小伙伴一起来抢吧！\n复制这条消息" + taoWord + "\n去淘宝打开就有哦！\n每天很多次机会！";
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

    private void showPopMenu() {
        if (sharePopupwindow == null) {
            sharePopupwindow = new SharePopupwindow(RedpacketShowActivity.this, v_shadow);
            sharePopupwindow.showAtLocation(wv_banner, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else if (sharePopupwindow != null
                && sharePopupwindow.isShowing()) {
            sharePopupwindow.dismiss();
        } else {
            sharePopupwindow.showAtLocation(wv_banner, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }

    }

    /**
     * 分享淘口令链接到不同给的平台
     *
     * @param platform
     */
    private void shareDiffSolfplam(SHARE_MEDIA platform) {
        UMImage thumb = new UMImage(this, R.mipmap.juyoubangs);
        //image.setThumb(thumb);
        UMWeb web = new UMWeb(redpacket_url);
        web.setTitle("聚优帮福利专区");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("聚优帮发福利了，快来领取红包啦！");//描述
        new ShareAction(this)
                .setPlatform(platform)//传入平台
                .withMedia(web)//分享内容
                .setCallback(shareListener)//回调监听器
                .share();
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
            Toast.makeText(RedpacketShowActivity.this, "分享成功", Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (sharePopupwindow != null && sharePopupwindow.isShowing()) {
                sharePopupwindow.dismiss();
            }
            if (v_shadow != null) {
                v_shadow.setVisibility(View.GONE);
            }
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(RedpacketShowActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (sharePopupwindow != null && sharePopupwindow.isShowing()) {
                sharePopupwindow.dismiss();
            }
            if (v_shadow != null) {
                v_shadow.setVisibility(View.GONE);
            }
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(RedpacketShowActivity.this, "分享取消", Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (sharePopupwindow != null && sharePopupwindow.isShowing()) {
                sharePopupwindow.dismiss();
            }
            if (v_shadow != null) {
                v_shadow.setVisibility(View.GONE);
            }
        }
    };

    public class MyWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }
    }

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
            TaoWordBean taoWordBean = new Gson().fromJson(response, TaoWordBean.class);
            int statusCode = taoWordBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    taoWord = taoWordBean.getData();
                    LogUtil.e(taoWord);
                    break;
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (sharePopupwindow != null && sharePopupwindow.isShowing()) {
            sharePopupwindow.dismiss();
        }
    }
}
