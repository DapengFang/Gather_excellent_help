package com.gather_excellent_help.presenter.homepresenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.imageutils.ImageLoader;
import com.google.gson.Gson;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2017/8/21.
 */

public class VipPresenter extends BasePresenter {
    private Activity context;
    private LinearLayout llHomeVipZera;
    private RelativeLayout rl_item_laod_more;
    private TextView tv_item_home_title;
    private LinearLayout ll_vip_left_zero;
    private LinearLayout ll_vip_right_zera;
    private String vip_url = Url.BASE_URL + "ChannelPrice.aspx";
    private NetUtil netUtil;
    private List<HomeVipBean.DataBean> vipData;
    private double user_rate;
    private int shopType;
    private boolean isToggle;

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
        rl_item_laod_more = (RelativeLayout) llHomeVipZera.findViewById(R.id.rl_item_laod_more);
        tv_item_home_title = (TextView) llHomeVipZera.findViewById(R.id.tv_item_home_title);
        ll_vip_left_zero = (LinearLayout) llHomeVipZera.findViewById(R.id.ll_vip_left_zero);
        ll_vip_right_zera = (LinearLayout) llHomeVipZera.findViewById(R.id.ll_vip_right_zera);
        return llHomeVipZera;
    }

    @Override
    public void initData() {
        tv_item_home_title.setText("专享区");
        netUtil.okHttp2Server2(vip_url, null);
        netUtil.setOnServerResponseListener(new MyOnServerResponserListener());
        ll_vip_left_zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WareListActivity.class);
                intent.putExtra("content", "isVip");
                context.startActivity(intent);
            }
        });
        rl_item_laod_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WareListActivity.class);
                intent.putExtra("content", "isVip");
                context.startActivity(intent);
            }
        });
    }

    public class MyOnServerResponserListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e("专享" + response);
            if (context != null) {
                parseData(response);
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            if (context != null) {
                Toast.makeText(context, "请检查你的网络连接是否正常！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseData(String response) {
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
    private void loadData(List<HomeVipBean.DataBean> vipData) {
        final DecimalFormat df = new DecimalFormat("#0.00");
        if (vipData != null) {
            int childCount = ll_vip_right_zera.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (i != 1) {
                    View childAt = ll_vip_right_zera.getChildAt(i);
                    LinearLayout ll_vip_zera_zhuan = (LinearLayout) childAt.findViewById(R.id.ll_vip_zera_zhuan);
                    LinearLayout ll_vip_zera_ware = (LinearLayout) childAt.findViewById(R.id.ll_vip_zera_ware);
                    ImageView iv_vip_ware_img = (ImageView) childAt.findViewById(R.id.iv_vip_ware_img);
                    TextView tv_vip_ware_coupon = (TextView) childAt.findViewById(R.id.tv_vip_ware_coupon);
                    TextView tv_vip_ware_title = (TextView) childAt.findViewById(R.id.tv_vip_ware_title);
                    TextView tv_vip_ware_person = (TextView) childAt.findViewById(R.id.tv_vip_ware_person);
                    TextView tv_vip_ware_price = (TextView) childAt.findViewById(R.id.tv_vip_ware_price);
                    TextView tv_vip_ware_zhuan = (TextView) childAt.findViewById(R.id.tv_vip_ware_zhuan);
                    TextView tv_vip_ware_coast = (TextView) childAt.findViewById(R.id.tv_vip_ware_coast);
                    TextView tv_activity_sun_tao_icon = (TextView) childAt.findViewById(R.id.tv_activity_sun_tao_icon);
                    final HomeVipBean.DataBean dataBean = vipData.get(i / 2);
                    final int site_id = dataBean.getSite_id();
                    final int article_id = dataBean.getArticle_id();
                    String img_url = dataBean.getImg_url();
                    int couponsPrice = dataBean.getCouponsPrice();
                    String couponsUrl = dataBean.getCouponsUrl();
                    String title = dataBean.getTitle();
                    double sell_price = dataBean.getSell_price();
                    final String link_url = dataBean.getLink_url();
                    double tkRate = dataBean.getTkRate() / 100;
                    double zhuan = (sell_price - couponsPrice) * tkRate * 0.9f * user_rate * dataBean.getCommission_rate();
                    double coast = sell_price - couponsPrice - zhuan;
                    tv_vip_ware_person.setVisibility(View.GONE);
                    if (title != null && tv_vip_ware_title != null) {
                        tv_vip_ware_title.setText("\t\t\t\t\t\t" + title);
                    }
                    if (tv_vip_ware_price != null) {
                        tv_vip_ware_price.setText("￥" + df.format(sell_price));
                    }
                    if (site_id == 1) {
                        if (tv_activity_sun_tao_icon != null) {
                            tv_activity_sun_tao_icon.setSelected(false);
                            tv_activity_sun_tao_icon.setText("淘宝");
                        }
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
                            tv_vip_ware_coupon.setText("领券减" + couponsPrice);
                        }

                        if (tv_vip_ware_zhuan != null) {
                            tv_vip_ware_zhuan.setText("￥" + df.format(zhuan));
                        }
                        if (tv_vip_ware_coast != null) {
                            tv_vip_ware_coast.setText("￥" + df.format(coast));
                        }
                        tv_vip_ware_coupon.setVisibility(View.GONE);
//                  if(couponsPrice>0) {
//                      tv_vip_ware_coupon.setVisibility(View.VISIBLE);
//                  }else{
//                      tv_vip_ware_coupon.setVisibility(View.GONE);
//                  }
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
                        if (ll_vip_zera_zhuan != null) {
                            ll_vip_zera_zhuan.setVisibility(View.GONE);
                        }
                        if (tv_activity_sun_tao_icon != null) {
                            tv_activity_sun_tao_icon.setSelected(true);
                            tv_activity_sun_tao_icon.setText("苏宁");
                        }
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
