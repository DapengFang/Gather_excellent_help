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
import com.gather_excellent_help.bean.HomeGroupBean;
import com.gather_excellent_help.presenter.BasePresenter;
import com.gather_excellent_help.ui.activity.WareListActivity;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.ui.activity.WebRecordActivity;
import com.gather_excellent_help.ui.activity.suning.SuningDetailActivity;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.imageutils.ImageLoader;
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
    private LinearLayout ll_group_left_big;
    private String group_url = Url.BASE_URL + "GroupBuy.aspx";
    private NetUtil netUtil;
    private double user_rate;
    private int shopType;
    private boolean isToggle;
    private RelativeLayout rl_item_laod_more;
    private TextView tv_item_home_title;
    private LinearLayout ll_group_right_ware;
    private TextView tv_group_ware_title;
    private TextView tv_group_ware_price;
    private LinearLayout ll_group_ware_zhuan;
    private TextView tv_group_ware_zhuan;
    private TextView tv_group_ware_coast;
    private ImageView iv_group_ware_img;
    private TextView tv_group_ware_coupon;
    private LinearLayout ll_group_right_zera;
    private List<HomeGroupBean.DataBean> groupData;
    private TextView tv_activity_sun_tao_icon;


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
        rl_item_laod_more = (RelativeLayout) llHomeGroupZera.findViewById(R.id.rl_item_laod_more);
        tv_item_home_title = (TextView) llHomeGroupZera.findViewById(R.id.tv_item_home_title);
        ll_group_left_big = (LinearLayout) llHomeGroupZera.findViewById(R.id.ll_group_left_big);
        ll_group_right_ware = (LinearLayout) llHomeGroupZera.findViewById(R.id.ll_group_right_ware);
        tv_group_ware_title = (TextView) llHomeGroupZera.findViewById(R.id.tv_group_ware_title);
        tv_group_ware_price = (TextView) llHomeGroupZera.findViewById(R.id.tv_group_ware_price);
        ll_group_ware_zhuan = (LinearLayout) llHomeGroupZera.findViewById(R.id.ll_group_ware_zhuan);
        tv_group_ware_zhuan = (TextView) llHomeGroupZera.findViewById(R.id.tv_group_ware_zhuan);
        tv_group_ware_coast = (TextView) llHomeGroupZera.findViewById(R.id.tv_group_ware_coast);
        iv_group_ware_img = (ImageView) llHomeGroupZera.findViewById(R.id.iv_group_ware_img);
        tv_group_ware_coupon = (TextView) llHomeGroupZera.findViewById(R.id.tv_group_ware_coupon);
        ll_group_right_zera = (LinearLayout) llHomeGroupZera.findViewById(R.id.ll_group_right_zera);
        tv_activity_sun_tao_icon = (TextView) llHomeGroupZera.findViewById(R.id.tv_activity_sun_tao_icon);
        return llHomeGroupZera;
    }

    @Override
    public void initData() {
        tv_item_home_title.setText("团购区");
        netUtil.okHttp2Server2(group_url, null);
        netUtil.setOnServerResponseListener(new MyOnServerResponseListener());
        rl_item_laod_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WareListActivity.class);
                intent.putExtra("content", "isQiang");
                context.startActivity(intent);
            }
        });
        ll_group_left_big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WareListActivity.class);
                intent.putExtra("content", "isQiang");
                context.startActivity(intent);
            }
        });
    }

    public class MyOnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
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

    private void loadRightWareData(List<HomeGroupBean.DataBean> groupData) {
        int childCount = ll_group_right_zera.getChildCount();
        LogUtil.e(childCount + "----------------");
        for (int i = 0; i < childCount; i++) {
            if (i != 1 || i != 3) {
                View childAt = ll_group_right_zera.getChildAt(i);
                LinearLayout ll_group_right_ware = (LinearLayout) childAt.findViewById(R.id.ll_group_right_ware);
                TextView tv_group_ware_title = (TextView) childAt.findViewById(R.id.tv_group_ware_title);
                TextView tv_group_ware_price = (TextView) childAt.findViewById(R.id.tv_group_ware_price);
                LinearLayout ll_group_ware_zhuan = (LinearLayout) childAt.findViewById(R.id.ll_group_ware_zhuan);
                TextView tv_group_ware_zhuan = (TextView) childAt.findViewById(R.id.tv_group_ware_zhuan);
                TextView tv_group_ware_coast = (TextView) childAt.findViewById(R.id.tv_group_ware_coast);
                ImageView iv_group_ware_img = (ImageView) childAt.findViewById(R.id.iv_group_ware_img);
                TextView tv_group_ware_coupon = (TextView) childAt.findViewById(R.id.tv_group_ware_coupon);
                TextView tv_activity_sun_tao_icon = (TextView) childAt.findViewById(R.id.tv_activity_sun_tao_icon);
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
                if (title != null && tv_group_ware_title != null) {
                    tv_group_ware_title.setText("\t\t\t\t\t\t" + title);
                }
                if (tv_group_ware_price != null) {
                    tv_group_ware_price.setText("￥" + df.format(sell_price));
                }
                if (site_id == 1) {
                    if (tv_activity_sun_tao_icon != null) {
                        tv_activity_sun_tao_icon.setSelected(false);
                        tv_activity_sun_tao_icon.setText("淘宝");
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
                        tv_group_ware_coupon.setText("领券减" + couponsPrice);
                    }
                    if (tv_group_ware_zhuan != null) {
                        tv_group_ware_zhuan.setText("￥" + df.format(zhuan));
                    }
                    if (tv_group_ware_coast != null) {
                        tv_group_ware_coast.setText("￥" + df.format(coast));
                    }
                    if (tv_group_ware_coupon != null) {
                        if (couponsPrice > 0) {
                            tv_group_ware_coupon.setVisibility(View.VISIBLE);
                        } else {
                            tv_group_ware_coupon.setVisibility(View.GONE);
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
                    if (ll_group_ware_zhuan != null) {
                        ll_group_ware_zhuan.setVisibility(View.GONE);
                    }
                    if (tv_group_ware_coupon != null) {
                        tv_group_ware_coupon.setVisibility(View.GONE);
                    }
                    if (tv_activity_sun_tao_icon != null) {
                        tv_activity_sun_tao_icon.setSelected(true);
                        tv_activity_sun_tao_icon.setText("淘宝");
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
                            if(site_id == 1) {
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
                            }else if(site_id == 2) {
                                Intent intent = new Intent(context, SuningDetailActivity.class);
                                intent.putExtra("article_id",article_id);
                                intent.putExtra("goods_id", goods_id);
                                intent.putExtra("goods_img", img_url);
                                intent.putExtra("goods_title", title);
                                intent.putExtra("goods_price", df.format(sell_price)+"");
                                intent.putExtra("c_price", df.format(market_price)+"");
                                context.startActivity(intent);
                            }
                        }
                    });
                }
            }
        }
    }

    private void loadWareData(HomeGroupBean.DataBean dataBean) {
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
        if (title != null && tv_group_ware_title != null) {
            tv_group_ware_title.setText("\t\t\t\t\t\t" + title);
        }
        if (tv_group_ware_price != null) {
            tv_group_ware_price.setText("￥" + df.format(sell_price));
        }
        if (site_id == 1) {
            if (tv_activity_sun_tao_icon != null) {
                tv_activity_sun_tao_icon.setSelected(false);
                tv_activity_sun_tao_icon.setText("淘宝");
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
                tv_group_ware_coupon.setText("领券减" + couponsPrice);
            }
            if (tv_group_ware_zhuan != null) {
                tv_group_ware_zhuan.setText("￥" + df.format(zhuan));
            }
            if (tv_group_ware_coast != null) {
                tv_group_ware_coast.setText("￥" + df.format(coast));
            }
            if (couponsPrice > 0) {
                tv_group_ware_coupon.setVisibility(View.VISIBLE);
            } else {
                tv_group_ware_coupon.setVisibility(View.GONE);
            }
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
        } else if (site_id == 2) {
            if (tv_group_ware_coupon != null) {
                tv_group_ware_coupon.setVisibility(View.GONE);
            }
            if (ll_group_ware_zhuan != null) {
                ll_group_ware_zhuan.setVisibility(View.GONE);
            }
            if (tv_activity_sun_tao_icon != null) {
                tv_activity_sun_tao_icon.setSelected(true);
                tv_activity_sun_tao_icon.setText("苏宁");
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

        ll_group_right_ware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(site_id == 1) {
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
                }else if(site_id == 2) {
                    Intent intent = new Intent(context, SuningDetailActivity.class);
                    intent.putExtra("article_id",article_id);
                    intent.putExtra("goods_id", goods_id);
                    intent.putExtra("goods_img", img_url);
                    intent.putExtra("goods_title", title);
                    intent.putExtra("goods_price", df.format(sell_price)+"");
                    intent.putExtra("c_price", df.format(market_price)+"");
                    context.startActivity(intent);
                }

            }
        });
    }
}
