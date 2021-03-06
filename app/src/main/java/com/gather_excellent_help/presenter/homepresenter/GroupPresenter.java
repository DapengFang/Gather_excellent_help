package com.gather_excellent_help.presenter.homepresenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.HomeGroupBean;
import com.gather_excellent_help.presenter.BasePresenter;
import com.gather_excellent_help.ui.activity.WareListActivity;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.ui.activity.WebRecordActivity;
import com.gather_excellent_help.ui.activity.suning.SuningDetailActivity;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.span.ImageSpanUtil;
import com.gather_excellent_help.utils.span.MyImageSpan;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2017/8/21.
 */

public class GroupPresenter extends BasePresenter {

    private Activity context;
    private LinearLayout llHomeGroupZera;
    private String group_url = Url.BASE_URL + "GroupBuy.aspx";
    private NetUtil netUtil;
    private double user_rate;
    private int shopType;
    private boolean isToggle;
    private LinearLayout ll_group_right_zera;
    private List<HomeGroupBean.DataBean> groupData;


    private RelativeLayout rl_left_top_listzera;
    private LinearLayout ll_left_top_ware;
    private ImageView iv_left_top_zera;
    private TextView tv_left_top_waretitle;
    private TextView tv_left_top_wareprice;
    private ImageView iv_left_top_warepic;


    public GroupPresenter(Activity context, LinearLayout llHomeGroupZera) {
        this.context = context;
        this.llHomeGroupZera = llHomeGroupZera;
        initView();
        netUtil = new NetUtil();
        shopType = Tools.getShopType(context);
        String userRate = Tools.getUserRate(context);
        if (!TextUtils.isEmpty(userRate)) {
            double v = Double.parseDouble(userRate);
            user_rate = v / 100;
        }
        isToggle = Tools.isToggleShow(context);
    }

    @Override
    public View initView() {
        ll_group_right_zera = (LinearLayout) llHomeGroupZera.findViewById(R.id.ll_group_right_zera);

        rl_left_top_listzera = (RelativeLayout) llHomeGroupZera.findViewById(R.id.rl_left_top_listzera);
        ll_left_top_ware = (LinearLayout) llHomeGroupZera.findViewById(R.id.ll_left_top_ware);
        iv_left_top_zera = (ImageView) llHomeGroupZera.findViewById(R.id.iv_left_top_zera);
        tv_left_top_waretitle = (TextView) llHomeGroupZera.findViewById(R.id.tv_left_top_waretitle);
        tv_left_top_wareprice = (TextView) llHomeGroupZera.findViewById(R.id.tv_left_top_wareprice);
        iv_left_top_warepic = (ImageView) llHomeGroupZera.findViewById(R.id.iv_left_top_warepic);
        return llHomeGroupZera;
    }

    @Override
    public void initData() {
        netUtil.okHttp2Server2(context,group_url, null);
        netUtil.setOnServerResponseListener(new MyOnServerResponseListener());
    }

    public class MyOnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            try {
                parseData(response);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            EncryptNetUtil.startNeterrorPage(context);
        }
    }

    private void parseData(String response) throws Exception {
        HomeGroupBean homeGroupBean = new Gson().fromJson(response, HomeGroupBean.class);
        int statusCode = homeGroupBean.getStatusCode();
        switch (statusCode) {
            case 1:
                groupData = homeGroupBean.getData();
                if (groupData.size() < 4) {
                    return;
                }
                HomeGroupBean.DataBean dataBean = groupData.get(0);
                loadWareData(dataBean);
                loadRightWareData(groupData);
                break;
            case 0:
                Toast.makeText(context, homeGroupBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void loadRightWareData(List<HomeGroupBean.DataBean> groupData) throws Exception {
        int childCount = ll_group_right_zera.getChildCount();
        LogUtil.e(childCount + "----------------");
        for (int i = 0; i < childCount; i++) {
            if (i != 1) {
                View childAt = ll_group_right_zera.getChildAt(i);
                LinearLayout ll_group_right_ware = (LinearLayout) childAt.findViewById(R.id.ll_group_right_ware);
                TextView tv_group_ware_title = (TextView) childAt.findViewById(R.id.tv_group_ware_title);
                TextView tv_group_ware_price = (TextView) childAt.findViewById(R.id.tv_group_ware_price);
                LinearLayout ll_group_ware_zhuan = (LinearLayout) childAt.findViewById(R.id.ll_group_ware_zhuan);
                TextView tv_group_ware_zhuan = (TextView) childAt.findViewById(R.id.tv_group_ware_zhuan);
                TextView tv_group_ware_coast = (TextView) childAt.findViewById(R.id.tv_group_ware_coast);
                ImageView iv_group_ware_img = (ImageView) childAt.findViewById(R.id.iv_group_ware_img);
                TextView tv_group_ware_coupon = (TextView) childAt.findViewById(R.id.tv_group_ware_coupon);
                LinearLayout ll_activity_group_coupon = (LinearLayout) childAt.findViewById(R.id.ll_activity_group_coupon);
                HomeGroupBean.DataBean dataBean = groupData.get((i + 2) / 2);
                final int site_id = dataBean.getSite_id();
                final int article_id = dataBean.getArticle_id();
                final DecimalFormat df = new DecimalFormat("#0.00");
                final String img_url = dataBean.getImg_url();
                final int couponsPrice = dataBean.getCouponsPrice();
                final String couponsUrl = dataBean.getCouponsUrl();
                final String title = dataBean.getTitle();
                final double sell_price = dataBean.getSell_price();
                final double market_price = dataBean.getMarket_price();
                final String link_url = dataBean.getLink_url();
                double tkRate = dataBean.getTkRate() / 100;
                double zhuan = (sell_price - couponsPrice) * tkRate * 0.9f * user_rate * dataBean.getCommission_rate();
                double coast = sell_price - couponsPrice - zhuan;
                final String goods_id = String.valueOf(dataBean.getProductId());

                double suning_rate = dataBean.getSuning_rate();
                double s_zhuan = sell_price * tkRate * user_rate;
                double s_coast = sell_price - s_zhuan;

                if (tv_group_ware_price != null) {

                    String price_content = "¥" + df.format(sell_price);
                    SpannableString spannableString_l01 = new SpannableString(price_content);
                    RelativeSizeSpan sizeSpan_01 = new RelativeSizeSpan(0.8f);
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#fa2a3b"));
                    spannableString_l01.setSpan(colorSpan, 0, spannableString_l01.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                    spannableString_l01.setSpan(sizeSpan_01, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    tv_group_ware_price.setText(spannableString_l01);

                }
                if (site_id == 1) {

                    if (title != null && tv_group_ware_title != null) {

                        SpannableString span = new SpannableString("\t\t" + title);
                        Drawable drawable = context.getResources().getDrawable(R.drawable.t_taobao_ware_icon);
                        Bitmap bitmap = ImageSpanUtil.zoomDrawable(drawable, DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16));
                        MyImageSpan image = new MyImageSpan(context, bitmap, -1);
                        span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tv_group_ware_title.setText(span);
                    }
                    if (img_url != null && iv_group_ware_img != null) {
                        if (context != null && !context.isFinishing()) {
                            Glide.with(context).load(img_url + "_320x320q90.jpg")
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                                    .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                                    .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                                    .into(iv_group_ware_img);//请求成功后把图片设置到的控件
                        }
                    }
                    if (tv_group_ware_coupon != null) {
                        tv_group_ware_coupon.setText("" + couponsPrice);
                    }
                    if (tv_group_ware_zhuan != null) {
                        tv_group_ware_zhuan.setText("赚 " + df.format(zhuan));
                    }
                    if (tv_group_ware_coast != null) {
                        tv_group_ware_coast.setText("到手价 " + df.format(coast));
                    }
                    if (ll_activity_group_coupon != null) {
                        if (couponsPrice > 0) {
                            ll_activity_group_coupon.setVisibility(View.VISIBLE);
                        } else {
                            ll_activity_group_coupon.setVisibility(View.GONE);
                        }
                    }
                    if (ll_group_ware_zhuan != null) {
                        if (shopType == 1) {
                            if (isToggle) {
                                ll_group_ware_zhuan.setVisibility(View.GONE);
                            } else {
                                ll_group_ware_zhuan.setVisibility(View.VISIBLE);
                            }
                        } else {
                            ll_group_ware_zhuan.setVisibility(View.GONE);
                        }
                        if (zhuan == 0) {
                            ll_group_ware_zhuan.setVisibility(View.GONE);
                        }
                    }
                } else if (site_id == 2) {

                    if (tv_group_ware_zhuan != null) {
                        tv_group_ware_zhuan.setText("赚 " + df.format(s_zhuan));
                    }
                    if (tv_group_ware_coast != null) {
                        tv_group_ware_coast.setText("到手价 " + df.format(s_coast));
                    }

                    if (ll_group_ware_zhuan != null) {
                        if (shopType == 1) {
                            if (isToggle) {
                                ll_group_ware_zhuan.setVisibility(View.GONE);
                            } else {
                                ll_group_ware_zhuan.setVisibility(View.VISIBLE);
                            }
                        } else {
                            ll_group_ware_zhuan.setVisibility(View.GONE);
                        }
                        if (s_zhuan == 0) {
                            ll_group_ware_zhuan.setVisibility(View.GONE);
                        }
                    }

                    if (ll_activity_group_coupon != null) {
                        ll_activity_group_coupon.setVisibility(View.GONE);
                    }

                    if (title != null && tv_group_ware_title != null) {
                        SpannableString span = new SpannableString("\t\t" + title);
                        Drawable drawable = context.getResources().getDrawable(R.drawable.s_suning_ware_icon);
                        Bitmap bitmap = ImageSpanUtil.zoomDrawable(drawable, DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16));
                        MyImageSpan image = new MyImageSpan(context, bitmap, -1);
                        span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tv_group_ware_title.setText(span);
                    }

                    if (img_url != null && iv_group_ware_img != null) {
                        if (context != null && !context.isFinishing()) {
                            Glide.with(context).load(img_url.replace("800x800", "400x400"))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                                    .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                                    .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                                    .into(iv_group_ware_img);//请求成功后把图片设置到的控件
                        }
                    }
                }

                if (ll_group_right_ware != null) {
                    ll_group_right_ware.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (site_id == 1) {
                                if (couponsPrice > 0) {
                                    if (couponsUrl != null && !TextUtils.isEmpty(couponsUrl)) {
                                        Intent intent = new Intent(context, WebActivity.class);
                                        intent.putExtra("web_url", couponsUrl);
                                        intent.putExtra("url", link_url);
                                        intent.putExtra("goods_id", goods_id);
                                        intent.putExtra("goods_img", img_url);
                                        intent.putExtra("goods_title", title);
                                        intent.putExtra("goods_price", df.format(sell_price) + "");
                                        intent.putExtra("goods_coupon", String.valueOf(couponsPrice));
                                        intent.putExtra("goods_coupon_url", couponsUrl);
                                        context.startActivity(intent);
                                    }
                                } else {
                                    Intent intent = new Intent(context, WebRecordActivity.class);
                                    intent.putExtra("url", link_url);
                                    intent.putExtra("goods_id", goods_id);
                                    intent.putExtra("goods_img", img_url);
                                    intent.putExtra("goods_title", title);
                                    intent.putExtra("goods_price", df.format(sell_price) + "");
                                    context.startActivity(intent);
                                }
                            } else if (site_id == 2) {
                                Intent intent = new Intent(context, SuningDetailActivity.class);
                                intent.putExtra("article_id", article_id);
                                intent.putExtra("goods_id", goods_id);
                                intent.putExtra("goods_img", img_url);
                                intent.putExtra("goods_title", title);
                                intent.putExtra("goods_price", df.format(sell_price) + "");
                                intent.putExtra("c_price", df.format(market_price) + "");
                                context.startActivity(intent);
                            }
                        }
                    });
                }
            }
        }
    }

    private void loadWareData(HomeGroupBean.DataBean dataBean) throws Exception {
        llHomeGroupZera.setVisibility(View.VISIBLE);
        final DecimalFormat df = new DecimalFormat("#0.00");
        final int site_id = dataBean.getSite_id();
        final int article_id = dataBean.getArticle_id();
        final String img_url = dataBean.getImg_url();
        final int couponsPrice = dataBean.getCouponsPrice();
        final String couponsUrl = dataBean.getCouponsUrl();
        final String title = dataBean.getTitle();
        final double sell_price = dataBean.getSell_price();
        final double market_price = dataBean.getMarket_price();
        final String link_url = dataBean.getLink_url();
        double tkRate = dataBean.getTkRate() / 100;
        double zhuan = (sell_price - couponsPrice) * tkRate * 0.9f * user_rate * dataBean.getCommission_rate();
        double coast = sell_price - couponsPrice - zhuan;
        final String goods_id = String.valueOf(dataBean.getProductId());

        double suning_rate = dataBean.getSuning_rate();
        double s_zhuan = sell_price * tkRate * user_rate;
        double s_coast = sell_price - s_zhuan;


        if (img_url != null && iv_left_top_warepic != null) {
            if (context != null && !context.isFinishing()) {
                Glide.with(context).load(img_url + "_320x320q90.jpg")
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                        .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                        .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                        .into(iv_left_top_warepic);//请求成功后把图片设置到的控件
            }
        }
        String price_content = "付款价 ¥" + df.format(sell_price);

        SpannableString spannableString_l01 = new SpannableString(price_content);
        RelativeSizeSpan sizeSpan_01 = new RelativeSizeSpan(1.3f);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#fa2a3b"));
        spannableString_l01.setSpan(colorSpan, 4, spannableString_l01.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        spannableString_l01.setSpan(sizeSpan_01, 5, spannableString_l01.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_left_top_wareprice.setText(spannableString_l01);
        iv_left_top_zera.setImageResource(R.drawable.home_group_top_icon);
        if (site_id == 1) {
            SpannableString span = new SpannableString("\t\t" + title);
            Drawable drawable = context.getResources().getDrawable(R.drawable.t_taobao_ware_icon);
            Bitmap bitmap = ImageSpanUtil.zoomDrawable(drawable, DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16));
            MyImageSpan image = new MyImageSpan(context, bitmap, -1);
            span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_left_top_waretitle.setText(span);
        } else if (site_id == 2) {
            SpannableString span = new SpannableString("\t\t" + title);
            Drawable drawable = context.getResources().getDrawable(R.drawable.s_suning_ware_icon);
            Bitmap bitmap = ImageSpanUtil.zoomDrawable(drawable, DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16));
            MyImageSpan image = new MyImageSpan(context, bitmap, -1);
            span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_left_top_waretitle.setText(span);
        }

        rl_left_top_listzera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WareListActivity.class);
                intent.putExtra("content", "isQiang");
                context.startActivity(intent);
            }
        });

        ll_left_top_ware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (site_id == 1) {
                    if (couponsPrice > 0) {
                        if (couponsUrl != null && !TextUtils.isEmpty(couponsUrl)) {
                            Intent intent = new Intent(context, WebActivity.class);
                            intent.putExtra("web_url", couponsUrl);
                            intent.putExtra("url", link_url);
                            intent.putExtra("goods_id", goods_id);
                            intent.putExtra("goods_img", img_url);
                            intent.putExtra("goods_title", title);
                            intent.putExtra("goods_price", df.format(sell_price) + "");
                            intent.putExtra("goods_coupon", String.valueOf(couponsUrl));
                            intent.putExtra("goods_coupon_url", couponsUrl);
                            context.startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(context, WebRecordActivity.class);
                        intent.putExtra("url", link_url);
                        intent.putExtra("goods_id", goods_id);
                        intent.putExtra("goods_img", img_url);
                        intent.putExtra("goods_title", title);
                        intent.putExtra("goods_price", df.format(sell_price) + "");
                        context.startActivity(intent);
                    }
                } else if (site_id == 2) {
                    Intent intent = new Intent(context, SuningDetailActivity.class);
                    intent.putExtra("article_id", article_id);
                    intent.putExtra("goods_id", goods_id);
                    intent.putExtra("goods_img", img_url);
                    intent.putExtra("goods_title", title);
                    intent.putExtra("goods_price", df.format(sell_price) + "");
                    intent.putExtra("c_price", df.format(market_price) + "");
                    context.startActivity(intent);
                }

            }
        });
    }
}
