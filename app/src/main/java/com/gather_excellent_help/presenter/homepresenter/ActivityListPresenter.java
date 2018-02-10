package com.gather_excellent_help.presenter.homepresenter;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.gather_excellent_help.MainActivity;
import com.gather_excellent_help.R;
import com.gather_excellent_help.aliapi.DemoTradeCallback;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.ActivityListBean;
import com.gather_excellent_help.bean.ChangeUrlBean;
import com.gather_excellent_help.bean.TaoWordBean;
import com.gather_excellent_help.presenter.BasePresenter;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.activity.OrderActivity;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.ui.activity.WebRecordActivity;
import com.gather_excellent_help.ui.activity.suning.SuningDetailActivity;
import com.gather_excellent_help.ui.widget.DividerItemDecoration;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.MyNestedScrollView;
import com.gather_excellent_help.update.HomeActivityListAdapter;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.shareutil.ShareUtil;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2017/8/22.
 */

public class ActivityListPresenter extends BasePresenter {

    private Context context;
    private RecyclerView rcvHomeActivityList;
    private MyNestedScrollView myNestedScrollView;
    private String activity_url = Url.BASE_URL + "IndexAllGoods.aspx";
    private String pageSize = "10";
    private String pageIndex = "1";
    private NetUtil netUtil;
    private Map<String, String> map;
    private List<ActivityListBean.DataBean> activityData;
    private boolean isLoadMore = false;
    private int page = 1;
    private List<ActivityListBean.DataBean> currData;
    private HomeActivityListAdapter homeActivityListAdapter;
    private FullyLinearLayoutManager layoutManager;
    private int lastVisibleItem;
    private LinearLayout ll_home_loadmore;
    private String adverId;
    private String whick = "";
    private String click_url;
    private String userLogin;
    private String goods_img;
    private String goods_title;
    private String taoWord;
    private AlertDialog dialog;
    private String goods_price = "";
    private String goods_coupon = "";
    private String share_content;
    private int whick_share;


    public ActivityListPresenter(Context context, RecyclerView rcvHomeActivityList, MyNestedScrollView myNestedScrollView, LinearLayout ll_home_loadmore) {
        this.context = context;
        this.rcvHomeActivityList = rcvHomeActivityList;
        this.myNestedScrollView = myNestedScrollView;
        this.ll_home_loadmore = ll_home_loadmore;
        netUtil = new NetUtil();
    }

    @Override
    public View initView() {
        return rcvHomeActivityList;
    }

    @Override
    public void initData() {
        layoutManager = new FullyLinearLayoutManager(context);
        rcvHomeActivityList.setLayoutManager(layoutManager);
        isLoadMore = false;
        net2Server();
        netUtil.setOnServerResponseListener(new MyOnServerResponseListener());
    }

    private void net2Server() {
        whick = "warelist";
        map = new HashMap<>();
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
        netUtil.okHttp2Server2(context,activity_url, map);
    }

    public class MyOnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            try {
                if (whick.equals("warelist")) {
                    parseData(response);
                } else if (whick.equals("changeurl")) {
                    parseChangeData(response);
                } else if (whick.equals("getwords")) {
                    parseChangeWordsData(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
            if (context != null) {
                EncryptNetUtil.startNeterrorPage(context);
            }
        }
    }

    /**
     * 剪切板剪切淘口令
     */
    private void showCopyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View inflate = View.inflate(context, R.layout.item_copy_taoword_dialog, null);
        TextView tvCopyContent = (TextView) inflate.findViewById(R.id.tv_copy_taoword_content);
        TextView tvCopyDismiss = (TextView) inflate.findViewById(R.id.tv_copy_taoword_dismiss);
        TextView tvCopyShare = (TextView) inflate.findViewById(R.id.tv_copy_taoword_share);
        LinearLayout ll_share_qq = (LinearLayout) inflate.findViewById(R.id.ll_share_qq);
        LinearLayout ll_share_weixin = (LinearLayout) inflate.findViewById(R.id.ll_share_weixin);
        LinearLayout ll_share_sina = (LinearLayout) inflate.findViewById(R.id.ll_share_sina);
        LinearLayout ll_share_weixin_friend = (LinearLayout) inflate.findViewById(R.id.ll_share_weixin_friend);
        if (whick_share == 1) {
            share_content = "商品名称:" + goods_title + "\n商品价格 ¥" + goods_price + "\n优惠券" + goods_coupon + "元" + "\n复制这条消息:" + taoWord + "\n去打开手机淘宝";
        } else if (whick_share == 2) {
            share_content = "商品名称:" + goods_title + "\n商品价格 ¥" + goods_price + "\n复制这条消息:" + taoWord + "\n去打开手机淘宝";
        }
        int index = share_content.indexOf(goods_price);
        SpannableString spannableString = new SpannableString(share_content);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
        spannableString.setSpan(colorSpan, index - 1, index + goods_price.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(1.2f);
        spannableString.setSpan(sizeSpan01, index, index + goods_price.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvCopyContent.setText(spannableString);
        dialog = builder.setView(inflate)
                .show();

        ll_share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDiffSolfplam(SHARE_MEDIA.QQ);
            }
        });

        ll_share_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDiffSolfplam(SHARE_MEDIA.WEIXIN);
            }
        });

        ll_share_sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDiffSolfplam(SHARE_MEDIA.SINA);
            }
        });

        ll_share_weixin_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDiffSolfplam(SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        });

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
                ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(share_content);
                Toast.makeText(context, "淘口令已复制", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 分享淘口令链接到不同给的平台
     *
     * @param platform
     */
    private void shareDiffSolfplam(SHARE_MEDIA platform) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(share_content);
        MainActivity activity = (MainActivity) context;
        UMImage image = new UMImage(context, goods_img);//网络图片
        UMImage thumb = new UMImage(context, R.mipmap.juyoubang_logo);
        image.setThumb(thumb);
        new ShareAction(activity)
                .setPlatform(platform)
                .withText(goods_title)
                .withMedia(image)
                .setCallback(shareListener)
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
            Toast.makeText(context, "分享成功", Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(context, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(context, "分享取消", Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    };

    /**
     * 解析获取淘口令数据
     *
     * @param response
     */
    private void parseChangeWordsData(String response) throws Exception {
        TaoWordBean taoWordBean = new Gson().fromJson(response, TaoWordBean.class);
        int statusCode = taoWordBean.getStatusCode();
        switch (statusCode) {
            case 1:
                taoWord = taoWordBean.getData();
                LogUtil.e(taoWord);
                showCopyDialog();
                break;
            case 0:
                Toast.makeText(context, taoWordBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析优惠券转链的数据
     *
     * @param response
     */
    private void parseChangeData(String response) throws Exception {
        LogUtil.e("click_url = " + response);
        ChangeUrlBean changeUrlBean = new Gson().fromJson(response, ChangeUrlBean.class);
        int statusCode = changeUrlBean.getStatusCode();
        switch (statusCode) {
            case 1:
                List<ChangeUrlBean.DataBean> data = changeUrlBean.getData();
                if (data != null && data.size() > 0) {
                    click_url = changeUrlBean.getData().get(0).getClick_url();
                    LogUtil.e("click_url = " + click_url);
                    whick = "getwords";
                    map = new HashMap<>();
                    map = ShareUtil.getChangeWordsParam(map, userLogin, click_url, goods_img, goods_title);
                    ShareUtil.getChangeWordUrl(context,netUtil, map);
                }
                break;
            case 0:
                Toast.makeText(context, changeUrlBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void parseData(String response) throws Exception {
        LogUtil.e("活动列表 === " + response);
        ActivityListBean activityListBean = new Gson().fromJson(response, ActivityListBean.class);
        int statusCode = activityListBean.getStatusCode();
        switch (statusCode) {
            case 1:
                if (isLoadMore) {
                    page++;
                    LogUtil.e("page == " + page);
                    currData = activityListBean.getData();
                    activityData.addAll(currData);
                    homeActivityListAdapter.notifyDataSetChanged();
                } else {
                    currData = activityListBean.getData();
                    activityData = currData;
                    homeActivityListAdapter = new HomeActivityListAdapter(context, activityData);
                    rcvHomeActivityList.setAdapter(homeActivityListAdapter);
                    page = 2;
                }
                ll_home_loadmore.setVisibility(View.GONE);
                myNestedScrollView.setOnTouchListener(new MyOnTouchClickListener());

                homeActivityListAdapter.setOnShareClickListener(new HomeActivityListAdapter.OnShareClickListener() {
                    @Override
                    public void onShareClick(View v, int position) {
                        DecimalFormat df = new DecimalFormat("#0.00");
                        ActivityListBean.DataBean dataBean = activityData.get(position);
                        int site_id = dataBean.getSite_id();
                        int article_id = dataBean.getArticle_id();
                        String couponsUrl = dataBean.getCouponsUrl();
                        String link_url = dataBean.getLink_url();
                        String goods_id = dataBean.getProductId();
                        goods_img = dataBean.getImg_url();
                        goods_title = dataBean.getTitle();
                        double sell_price = dataBean.getSell_price();
                        double market_price = dataBean.getMarket_price();
                        int couponsPrice = dataBean.getCouponsPrice();
                        goods_price = df.format(sell_price);
                        goods_coupon = df.format(couponsPrice);
                        boolean login = Tools.isLogin(context);
                        adverId = Tools.getAdverId(context);
                        userLogin = Tools.getUserLogin(context);
                        if (login) {
                            if (site_id == 1) {
                                whick = "changeurl";
                                if (couponsPrice > 0) {
                                    whick_share = 1;
                                    map = new HashMap<>();
                                    map = ShareUtil.getChangeWareParam(map, goods_id, adverId,userLogin);
                                    ShareUtil.getCouponChangeUrl(context,netUtil, map);
                                } else {
                                    whick_share = 2;
                                    map = new HashMap<>();
                                    map = ShareUtil.getChangeWareParam(map, goods_id, adverId,userLogin);
                                    ShareUtil.getWareChangeUrl(context,netUtil, map);
                                }
                            }
                        } else {
                            toLogin();
                        }
                    }
                });

                homeActivityListAdapter.setOnItemclickListener(new HomeActivityListAdapter.OnItemclickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        DecimalFormat df = new DecimalFormat("#0.00");
                        ActivityListBean.DataBean dataBean = activityData.get(position);
                        int site_id = dataBean.getSite_id();
                        int article_id = dataBean.getArticle_id();
                        String couponsUrl = dataBean.getCouponsUrl();
                        String link_url = dataBean.getLink_url();
                        String goods_id = dataBean.getProductId();
                        String goods_img = dataBean.getImg_url();
                        String goods_title = dataBean.getTitle();
                        double sell_price = dataBean.getSell_price();
                        double market_price = dataBean.getMarket_price();
                        int couponsPrice = dataBean.getCouponsPrice();

                        if (site_id == 1) {
                            //淘宝
                            if (couponsPrice > 0) {
                                if (couponsUrl != null && !TextUtils.isEmpty(couponsUrl)) {
                                    Intent intent = new Intent(context, WebActivity.class);
                                    intent.putExtra("web_url", couponsUrl);
                                    intent.putExtra("url", link_url);
                                    intent.putExtra("goods_id", goods_id);
                                    intent.putExtra("goods_img", goods_img);
                                    intent.putExtra("goods_title", goods_title);
                                    intent.putExtra("goods_price", df.format(sell_price) + "");
                                    intent.putExtra("goods_coupon", String.valueOf(couponsPrice));
                                    intent.putExtra("goods_coupon_url", couponsUrl);
                                    context.startActivity(intent);
                                }
                            } else {
                                Intent intent = new Intent(context, WebRecordActivity.class);
                                intent.putExtra("url", link_url);
                                intent.putExtra("goods_id", goods_id);
                                intent.putExtra("goods_img", goods_img);
                                intent.putExtra("goods_title", goods_title);
                                intent.putExtra("goods_price", df.format(sell_price) + "");
                                context.startActivity(intent);
                            }
                        } else if (site_id == 2) {
                            //苏宁
                            Intent intent = new Intent(context, SuningDetailActivity.class);
                            intent.putExtra("article_id", article_id);
                            intent.putExtra("goods_id", goods_id);
                            intent.putExtra("goods_img", goods_img);
                            intent.putExtra("goods_title", goods_title);
                            intent.putExtra("goods_price", df.format(sell_price) + "");
                            intent.putExtra("c_price", df.format(market_price) + "");
                            context.startActivity(intent);
                            LogUtil.e(article_id + "--" + goods_id + "--" + goods_img + "--" + goods_title + "--" + sell_price + "--" + market_price);
                        }
                    }
                });
                break;
            case 0:
                Toast.makeText(context, activityListBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void toLogin() {
        Toast.makeText(context, "请先登录！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public class MyOnTouchClickListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int scrollY = view.getScrollY();
            int height = view.getHeight();
            int scrollViewMeasuredHeight = myNestedScrollView.getChildAt(0).getMeasuredHeight();
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    if ((scrollY + height) == scrollViewMeasuredHeight) {
                        isLoadMore = true;
                        ll_home_loadmore.setVisibility(View.VISIBLE);
                        ll_home_loadmore.getChildAt(0).setVisibility(View.VISIBLE);
                        TextView tv = (TextView) ll_home_loadmore.getChildAt(1);
                        tv.setText("正在加载中");
                        ll_home_loadmore.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (currData.size() < Integer.parseInt(pageSize)) {
                                    ll_home_loadmore.setVisibility(View.VISIBLE);
                                    ll_home_loadmore.getChildAt(0).setVisibility(View.GONE);
                                    TextView tv = (TextView) ll_home_loadmore.getChildAt(1);
                                    tv.setText("没有更多的数据了");
                                    ll_home_loadmore.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ll_home_loadmore.setVisibility(View.GONE);
                                        }
                                    }, 1000);
                                } else {
                                    pageIndex = String.valueOf(page);
                                    net2Server();
                                }
                            }
                        }, 1000);
                    }
                    break;
            }
            return false;

        }
    }

    private OnActivityListCompleteListener onActivityListCompleteListener;

    public interface OnActivityListCompleteListener {
        void onComplete();
    }

    public void setOnActivityListCompleteListener(OnActivityListCompleteListener onActivityListCompleteListener) {
        this.onActivityListCompleteListener = onActivityListCompleteListener;
    }
}
