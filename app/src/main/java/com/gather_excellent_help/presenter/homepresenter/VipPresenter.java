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
import com.gather_excellent_help.bean.HomeVipBean;
import com.gather_excellent_help.presenter.BasePresenter;
import com.gather_excellent_help.ui.activity.WareListActivity;
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

public class VipPresenter extends BasePresenter {
    private Activity context;
    private LinearLayout llHomeVipZera;
    private LinearLayout ll_vip_right_zera;
    private String vip_url = Url.BASE_URL + "ChannelPrice.aspx";
    private NetUtil netUtil;
    private List<HomeVipBean.DataBean> vipData;
    private double user_rate;
    private int shopType;
    private boolean isToggle;

    private RelativeLayout rl_left_top_listzera;
    private LinearLayout ll_left_top_ware;
    private ImageView iv_left_top_zera;
    private TextView tv_left_top_waretitle;
    private TextView tv_left_top_wareprice;
    private ImageView iv_left_top_warepic;

    public VipPresenter(Activity context, LinearLayout llHomeVipZera) {
        this.context = context;
        this.llHomeVipZera = llHomeVipZera;
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
        ll_vip_right_zera = (LinearLayout) llHomeVipZera.findViewById(R.id.ll_vip_right_zera);

        rl_left_top_listzera = (RelativeLayout) llHomeVipZera.findViewById(R.id.rl_left_top_listzera);
        ll_left_top_ware = (LinearLayout) llHomeVipZera.findViewById(R.id.ll_left_top_ware);
        iv_left_top_zera = (ImageView) llHomeVipZera.findViewById(R.id.iv_left_top_zera);
        tv_left_top_waretitle = (TextView) llHomeVipZera.findViewById(R.id.tv_left_top_waretitle);
        tv_left_top_wareprice = (TextView) llHomeVipZera.findViewById(R.id.tv_left_top_wareprice);
        iv_left_top_warepic = (ImageView) llHomeVipZera.findViewById(R.id.iv_left_top_warepic);
        return llHomeVipZera;
    }

    @Override
    public void initData() {
        netUtil.okHttp2Server2(context,vip_url, null);
        netUtil.setOnServerResponseListener(new MyOnServerResponserListener());
    }

    public class MyOnServerResponserListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e("专享" + response);
            try {
                parseData(response);
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

    private void parseData(String response) throws Exception {
        HomeVipBean homeVipBean = new Gson().fromJson(response, HomeVipBean.class);
        int statusCode = homeVipBean.getStatusCode();
        switch (statusCode) {
            case 1:
                vipData = homeVipBean.getData();
                if (vipData != null) {
                    if (vipData.size() < 2) {
                        return;
                    }
                    loadData(vipData);
                }
                break;
            case 0:
                Toast.makeText(context, homeVipBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 加载专享区数据
     *
     * @param vipData
     */
    private void loadData(List<HomeVipBean.DataBean> vipData) throws Exception{
        llHomeVipZera.setVisibility(View.VISIBLE);
        final DecimalFormat df = new DecimalFormat("#0.00");
        if (vipData != null) {
            final HomeVipBean.DataBean dataBean0 = vipData.get(0);
            final int site_id0 = dataBean0.getSite_id();
            String img_url0 = dataBean0.getImg_url();
            int couponsPrice0 = dataBean0.getCouponsPrice();
            String title0 = dataBean0.getTitle();
            double sell_price0 = dataBean0.getSell_price();
            final String link_url0 = dataBean0.getLink_url();

            if (img_url0 != null && iv_left_top_warepic != null) {
                if (context != null && !context.isFinishing()) {
                    Glide.with(context).load(img_url0 + "_320x320q90.jpg")
                            .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                            .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                            .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                            .into(iv_left_top_warepic);//请求成功后把图片设置到的控件
                }
            }
            String price_content = "付款价 ¥" + df.format(sell_price0);
            SpannableString spannableString_l01 = new SpannableString(price_content);
            RelativeSizeSpan sizeSpan_01 = new RelativeSizeSpan(1.3f);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#fa2a3b"));
            spannableString_l01.setSpan(colorSpan, 4, spannableString_l01.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            spannableString_l01.setSpan(sizeSpan_01, 5, spannableString_l01.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv_left_top_wareprice.setText(spannableString_l01);
            iv_left_top_zera.setImageResource(R.drawable.home_vip_top_icon);
            if (site_id0 == 1) {
                SpannableString span = new SpannableString("\t\t" + title0);
                Drawable drawable = context.getResources().getDrawable(R.drawable.t_taobao_ware_icon);
                Bitmap bitmap = ImageSpanUtil.zoomDrawable(drawable, DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16));
                MyImageSpan image = new MyImageSpan(context, bitmap, -1);
                span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_left_top_waretitle.setText(span);
            } else if (site_id0 == 2) {
                SpannableString span = new SpannableString("\t\t" + title0);
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
                    intent.putExtra("content", "isVip");
                    context.startActivity(intent);
                }
            });

            ll_left_top_ware.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String link_url = dataBean0.getLink_url();
                    String goods_id = String.valueOf(dataBean0.getProductId());
                    String goods_img = dataBean0.getImg_url();
                    String goods_title = dataBean0.getTitle();
                    double goods_price = dataBean0.getSell_price();
                    double market_price = dataBean0.getMarket_price();
                    final int article_id = dataBean0.getArticle_id();
                    if (site_id0 == 1) {
                        Intent intent = new Intent(context, WebRecordActivity.class);
                        intent.putExtra("url", link_url);
                        intent.putExtra("goods_id", goods_id);
                        intent.putExtra("goods_img", goods_img);
                        intent.putExtra("goods_title", goods_title);
                        intent.putExtra("goods_price", df.format(goods_price) + "");
                        context.startActivity(intent);
                    } else if (site_id0 == 2) {
                        Intent intent = new Intent(context, SuningDetailActivity.class);
                        intent.putExtra("article_id", article_id);
                        intent.putExtra("goods_id", goods_id);
                        intent.putExtra("goods_img", goods_img);
                        intent.putExtra("goods_title", goods_title);
                        intent.putExtra("goods_price", df.format(goods_price) + "");
                        intent.putExtra("c_price", df.format(market_price) + "");
                        context.startActivity(intent);
                    }
                }
            });

            int childCount = ll_vip_right_zera.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (i != 1) {
                    View childAt = ll_vip_right_zera.getChildAt(i);
                    LinearLayout ll_vip_zera_zhuan = (LinearLayout) childAt.findViewById(R.id.ll_vip_zera_zhuan);
                    LinearLayout ll_vip_zera_ware = (LinearLayout) childAt.findViewById(R.id.ll_vip_zera_ware);
                    ImageView iv_vip_ware_img = (ImageView) childAt.findViewById(R.id.iv_vip_ware_img);
                    TextView tv_vip_ware_coupon = (TextView) childAt.findViewById(R.id.tv_vip_ware_coupon);
                    final TextView tv_vip_ware_title = (TextView) childAt.findViewById(R.id.tv_vip_ware_title);
                    final TextView tv_vip_ware_price = (TextView) childAt.findViewById(R.id.tv_vip_ware_price);
                    TextView tv_vip_ware_zhuan = (TextView) childAt.findViewById(R.id.tv_vip_ware_zhuan);
                    TextView tv_vip_ware_coast = (TextView) childAt.findViewById(R.id.tv_vip_ware_coast);
                    final HomeVipBean.DataBean dataBean = vipData.get(i / 2 + 1);
                    final int site_id = dataBean.getSite_id();
                    final int article_id = dataBean.getArticle_id();
                    String img_url = dataBean.getImg_url();
                    int couponsPrice = dataBean.getCouponsPrice();
                    String couponsUrl = dataBean.getCouponsUrl();
                    final String title = dataBean.getTitle();
                    double sell_price = dataBean.getSell_price();
                    final String link_url = dataBean.getLink_url();
                    double tkRate = dataBean.getTkRate() / 100;
                    double zhuan = (sell_price - couponsPrice) * tkRate * 0.9f * user_rate * dataBean.getCommission_rate();
                    double coast = sell_price - couponsPrice - zhuan;

                    double suning_rate = dataBean.getSuning_rate();
                    double s_zhuan = sell_price * tkRate * user_rate;
                    double s_coast = sell_price - s_zhuan;


                    if (tv_vip_ware_price != null) {
                        SpannableString spannableString = new SpannableString("¥" + df.format(sell_price));
                        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(0.8f);
                        spannableString.setSpan(sizeSpan01, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        tv_vip_ware_price.setText(spannableString);
                    }
                    if (site_id == 1) {

                        SpannableString span = new SpannableString("\t\t" + title);
                        Drawable drawable = context.getResources().getDrawable(R.drawable.t_taobao_ware_icon);
                        Bitmap bitmap = ImageSpanUtil.zoomDrawable(drawable, DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16));
                        MyImageSpan image = new MyImageSpan(context, bitmap, -1);
                        span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tv_vip_ware_title.setText(span);
//
                        if (img_url != null && iv_vip_ware_img != null) {
                            if (context != null && !context.isFinishing()) {
                                Glide.with(context).load(img_url + "_320x320q90.jpg")
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                                        .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                                        .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                                        .into(iv_vip_ware_img);//请求成功后把图片设置到的控件
                            }
                        }
                        if (tv_vip_ware_coupon != null) {
                            tv_vip_ware_coupon.setText("券" + couponsPrice);
                        }

                        if (tv_vip_ware_zhuan != null) {
                            tv_vip_ware_zhuan.setText("赚 " + df.format(zhuan));
                        }
                        if (tv_vip_ware_coast != null) {
                            tv_vip_ware_coast.setText("到手价 " + df.format(coast));
                        }
                        tv_vip_ware_coupon.setVisibility(View.GONE);
                        if (shopType == 1) {
                            if (isToggle) {
                                ll_vip_zera_zhuan.setVisibility(View.GONE);
                            } else {
                                ll_vip_zera_zhuan.setVisibility(View.VISIBLE);
                            }
                        } else {
                            ll_vip_zera_zhuan.setVisibility(View.GONE);
                        }

                        if (zhuan == 0) {
                            ll_vip_zera_zhuan.setVisibility(View.GONE);
                        }
                    } else if (site_id == 2) {
                        if (tv_vip_ware_zhuan != null) {
                            tv_vip_ware_zhuan.setText("赚 " + df.format(s_zhuan));
                        }
                        if (tv_vip_ware_coast != null) {
                            tv_vip_ware_coast.setText("到手价 " + df.format(s_coast));
                        }

                        if (shopType == 1) {
                            if (isToggle) {
                                ll_vip_zera_zhuan.setVisibility(View.GONE);
                            } else {
                                ll_vip_zera_zhuan.setVisibility(View.VISIBLE);
                            }
                        } else {
                            ll_vip_zera_zhuan.setVisibility(View.GONE);
                        }

                        if (s_zhuan == 0) {
                            ll_vip_zera_zhuan.setVisibility(View.GONE);
                        }

                        SpannableString span = new SpannableString("\t\t" + title);
                        Drawable drawable = context.getResources().getDrawable(R.drawable.s_suning_ware_icon);
                        Bitmap bitmap = ImageSpanUtil.zoomDrawable(drawable, DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16));
                        MyImageSpan image = new MyImageSpan(context, bitmap, -1);
                        span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tv_vip_ware_title.setText(span);

                        if (img_url != null && iv_vip_ware_img != null) {
                            if (context != null && !context.isFinishing()) {
                                Glide.with(context).load(img_url.replace("800x800", "400x400"))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                                        .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                                        .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                                        .into(iv_vip_ware_img);//请求成功后把图片设置到的控件
                            }
                        }
                    }

                    ll_vip_zera_ware.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String link_url = dataBean.getLink_url();
                            String goods_id = String.valueOf(dataBean.getProductId());
                            String goods_img = dataBean.getImg_url();
                            String goods_title = dataBean.getTitle();
                            double goods_price = dataBean.getSell_price();
                            double market_price = dataBean.getMarket_price();
                            if (site_id == 1) {
                                Intent intent = new Intent(context, WebRecordActivity.class);
                                intent.putExtra("url", link_url);
                                intent.putExtra("goods_id", goods_id);
                                intent.putExtra("goods_img", goods_img);
                                intent.putExtra("goods_title", goods_title);
                                intent.putExtra("goods_price", df.format(goods_price) + "");
                                context.startActivity(intent);
                            } else if (site_id == 2) {
                                Intent intent = new Intent(context, SuningDetailActivity.class);
                                intent.putExtra("article_id", article_id);
                                intent.putExtra("goods_id", goods_id);
                                intent.putExtra("goods_img", goods_img);
                                intent.putExtra("goods_title", goods_title);
                                intent.putExtra("goods_price", df.format(goods_price) + "");
                                intent.putExtra("c_price", df.format(market_price) + "");
                                context.startActivity(intent);
                            }
                        }
                    });
                }
            }
        }
    }
}
