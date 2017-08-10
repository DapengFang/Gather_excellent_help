package com.gather_excellent_help.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.HomeData;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.HomeBannerBean;
import com.gather_excellent_help.bean.HomeGroupBean;
import com.gather_excellent_help.bean.HomeTypeBean;
import com.gather_excellent_help.bean.HomeVipBean;
import com.gather_excellent_help.bean.HomeWareBean;
import com.gather_excellent_help.bean.QiangTaoBean;
import com.gather_excellent_help.bean.TyepIndexBean;
import com.gather_excellent_help.ui.activity.QiangTaoActivity;
import com.gather_excellent_help.ui.activity.WareListActivity;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.ui.activity.WebRecordActivity;
import com.gather_excellent_help.ui.widget.CarouselImageView;
import com.gather_excellent_help.ui.widget.MyGridView;
import com.gather_excellent_help.ui.widget.RushDownTimer;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.ScreenUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.imageutils.ImageLoader;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by wuxin on 2017/7/12.
 */

public class HomeRushAllAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Activity activity;
    private int status = 1;
    public static final int LOAD_MORE = 0;
    public static final int LOAD_PULL_TO = 1;
    public static final int LOAD_NONE = 2;
    public static final int LOAD_END = 3;
    private static final int TYPE_TOP = -1;
    private static final int TYPE_FOOTER = -2;
    private static final int TYPE_GRID = -3;
    private static final int TYPE_GROUP = -4;
    private static final int TYPE_FIRSTBUY = -5;
    private static final int TYPE_VIP = -6;

    private ImageLoader mImageLoader;
    private NetUtil netUtils;
    private Map<String, String> map;
    private String url = Url.BASE_URL + "IndexBanner.aspx";
    private List<HomeWareBean.DataBean> data;
    private List<HomeGroupBean.DataBean> groupData;
    private List<TyepIndexBean.DataBean> typeData;
    private List<QiangTaoBean.DataBean> qiangData;
    private List<HomeVipBean.DataBean> vipData;
    private int extraCount = 5;

    private RushDownTimer rushDownTimer;
    private String hour;
    private String minute;
    private String second;
    private TextView tvRushHour;
    private TextView tvRushMinute;
    private TextView tvRushSecond;
    private boolean isFirst;
    private double user_rate;


    public HomeRushAllAdapter(Context context, List<HomeWareBean.DataBean> data, Activity activity, List<HomeGroupBean.DataBean> groupData, List<TyepIndexBean.DataBean> typeData,
                              RushDownTimer rushDownTimer, List<QiangTaoBean.DataBean> qiangData, List<HomeVipBean.DataBean> vipData, boolean isFirst) {
        this.context = context;
        this.activity = activity;
        this.data = data;
        this.groupData = groupData;
        this.typeData = typeData;
        this.qiangData = qiangData;
        this.vipData = vipData;
        this.rushDownTimer = rushDownTimer;
        this.isFirst = isFirst;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        String userRate = Tools.getUserRate(context);
        if(!TextUtils.isEmpty(userRate)) {
            double v = Double.parseDouble(userRate);
            user_rate = v/100;
        }
        if (isFirst) {
            extraCount = 5;
        } else {
            extraCount = 4;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isFirst) {
            if (position == 0) {
                return TYPE_TOP;
            } else if (position == 1) {
                return TYPE_GRID;
            } else if (position == 2) {
                return TYPE_VIP;
            } else if (position == 3) {
                return TYPE_FIRSTBUY;
            } else if (position == 4) {
                return TYPE_GROUP;
            } else {
                return position;
            }
        } else {
            if (position == 0) {
                return TYPE_TOP;
            } else if (position == 1) {
                return TYPE_GRID;
            } else if (position == 2) {
                return TYPE_GROUP;
            } else if (position == 3) {
                return TYPE_VIP;
            } else {
                return position;
            }
        }


//        else if (position + 1 == getItemCount()) {
//            return TYPE_FOOTER;
//        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOP) {
            View rootView = View.inflate(parent.getContext(), R.layout.component_home_banner, null);
            return new BannerViewHolder(rootView);
        } else if (viewType == TYPE_GRID) {
            View rootView = View.inflate(parent.getContext(), R.layout.component_home_type, null);
            return new TypeViewHolder(rootView);
        } else if (viewType == TYPE_FIRSTBUY) {
            View rootView = View.inflate(parent.getContext(), R.layout.component_firstbuy_zera, null);
            return new FirstBuyViewHolder(rootView);
        } else if (viewType == TYPE_GROUP) {
            View rootView = View.inflate(parent.getContext(), R.layout.component_group_zera, null);
            return new GroupViewHolder(rootView);
        } else if (viewType == TYPE_FOOTER) {
            View view = View.inflate(parent.getContext(), R.layout.load_more, null);
            return new FooterViewHolder(view);
        } else if (viewType == TYPE_VIP) {
            View inflate = View.inflate(parent.getContext(), R.layout.component_vip_zera, null);
            return new HomeVipViewHolder(inflate);
        } else {
            View rootView = View.inflate(parent.getContext(), R.layout.component_rush_zrea, null);
            return new RushWareViewHolder(rootView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.bindItem();
        } else if (holder instanceof TypeViewHolder) {
            TypeViewHolder typeViewHolder = (TypeViewHolder) holder;
            GridView gvHomeType = typeViewHolder.gvHomeType;
            loadTypeData(gvHomeType);
            gvHomeType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int id = typeData.get(i).getId();
                    Intent intent = new Intent(context, WareListActivity.class);
                    intent.putExtra("type_id", String.valueOf(id));
                    context.startActivity(intent);
                }
            });
        } else if (holder instanceof BannerViewHolder) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            CarouselImageView civHomeGanner = bannerViewHolder.civHomeGanner;
            getBannerData(civHomeGanner);
        } else if (holder instanceof RushWareViewHolder) {
            RushWareViewHolder rushWareViewHolder = (RushWareViewHolder) holder;
            final HomeWareBean.DataBean dataBean = data.get(position - extraCount);
            rushWareViewHolder.tvRushMoreTitle.setText(dataBean.getTitle());
            String url = dataBean.getImg_url();
            LogUtil.e(url);
            if(url!=null) {
                mImageLoader.loadImage(url, rushWareViewHolder.ivRushMoreBig, true);
            }
            rushWareViewHolder.ivRushMoreBig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = dataBean.getId();
                    Intent intent = new Intent(context, WareListActivity.class);
                    intent.putExtra("activity_id", String.valueOf(id));
                    intent.putExtra("content", "activity");
                    context.startActivity(intent);
                }
            });
            final List<HomeWareBean.DataBean.ItemBean> itemData = dataBean.getItem();
            if (itemData != null && itemData.size() > 2) {
                HomeRushAdapter homeRushAdapter = new HomeRushAdapter(context, itemData);
                rushWareViewHolder.gvRushZera.setAdapter(homeRushAdapter);
                rushWareViewHolder.gvRushZera.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String couponsUrl = itemData.get(i).getCouponsUrl();
                        if(couponsUrl!=null && !TextUtils.isEmpty(couponsUrl)) {
                            if(couponsUrl!=null && !TextUtils.isEmpty(couponsUrl)) {
                                Intent intent = new Intent(context, WebActivity.class);
                                intent.putExtra("web_url",couponsUrl);
                                context.startActivity(intent);
                            }
                        }else{
                            String link_url = itemData.get(i).getLink_url();
                            String goods_id = itemData.get(i).getProductId();
                            String goods_img = itemData.get(i).getImg_url();
                            String goods_title = itemData.get(i).getTitle();
                            Intent intent = new Intent(context, WebRecordActivity.class);
                            intent.putExtra("url", link_url);
                            intent.putExtra("goods_id", goods_id);
                            intent.putExtra("goods_img", goods_img);
                            intent.putExtra("goods_title", goods_title);
                            context.startActivity(intent);
                        }
                    }
                });
            }
        } else if (holder instanceof GroupViewHolder) {
            GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
            TextView tvTitle = groupViewHolder.tvTitle;
            tvTitle.setText("团购区");
            DecimalFormat df = new DecimalFormat("#0.00");
            final HomeGroupBean.DataBean dataBean = groupData.get(0);
            String sTitle0 = dataBean.getTitle().substring(0, 12) + "...";
            groupViewHolder.tv_group_big_title.setText(sTitle0);
            groupViewHolder.rl_item_laod_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WareListActivity.class);
                    intent.putExtra("content", "isQiang");
                    context.startActivity(intent);
                }
            });
            final int couponsPrice = dataBean.getCouponsPrice();
            double sell_price = dataBean.getSell_price();
            double tkRate = dataBean.getTkRate() / 100;
            double zhuan = (sell_price - couponsPrice) * tkRate * 0.9f * user_rate;
            double coast = sell_price - zhuan -couponsPrice;
            final String couponsUrl = dataBean.getCouponsUrl();
            final String secondCouponsUrl = dataBean.getSecondCouponsUrl();
            groupViewHolder.tvHomeGroupZhuan.setText("赚:￥" + df.format(zhuan) + " 成本:￥" + df.format(coast));
            if (couponsPrice > 0) {
                groupViewHolder.tvHomeGroupCoupons.setVisibility(View.VISIBLE);
                groupViewHolder.tvHomeGroupCoupons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (couponsUrl != null && !TextUtils.isEmpty(couponsUrl)) {
                            Intent intent = new Intent(context, WebActivity.class);
                            intent.putExtra("web_url", couponsUrl);
                            context.startActivity(intent);
                        }
                    }
                });
            } else {
                groupViewHolder.tvHomeGroupCoupons.setVisibility(View.GONE);
            }
            if (secondCouponsUrl != null && !TextUtils.isEmpty(secondCouponsUrl)) {
                groupViewHolder.tvGroupBigSecondSoupons.setVisibility(View.VISIBLE);
                groupViewHolder.tvGroupBigSecondSoupons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("web_url", secondCouponsUrl);
                        context.startActivity(intent);
                    }
                });
            } else {
                groupViewHolder.tvGroupBigSecondSoupons.setVisibility(View.GONE);
            }
            int group_id = CacheUtils.getInteger(context, CacheUtils.GROUP_TYPE, -1);
            if (group_id == 4) {
                boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
                if (toggleShow) {
                    groupViewHolder.tvHomeGroupZhuan.setVisibility(View.GONE);
                } else {
                    groupViewHolder.tvHomeGroupZhuan.setVisibility(View.VISIBLE);
                }
            } else {
                groupViewHolder.tvHomeGroupZhuan.setVisibility(View.GONE);
            }
            groupViewHolder.tv_group_big_price.setText("今日特价：￥" + df.format(sell_price));
            mImageLoader.loadImage(dataBean.getImg_url(), groupViewHolder.iv_group_big_pic, true);
            groupViewHolder.ll_group_big.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (couponsPrice > 0) {
                        if (couponsUrl != null && !TextUtils.isEmpty(couponsUrl)) {
                            Intent intent = new Intent(context, WebActivity.class);
                            intent.putExtra("web_url", couponsUrl);
                            context.startActivity(intent);
                        }
                    } else {
                        String link_url = dataBean.getLink_url();
                        String goods_id = dataBean.getProductId();
                        String goods_img = dataBean.getImg_url();
                        String goods_title = dataBean.getTitle();
                        Intent intent = new Intent(context, WebRecordActivity.class);
                        intent.putExtra("url", link_url);
                        intent.putExtra("goods_id", goods_id);
                        intent.putExtra("goods_img", goods_img);
                        intent.putExtra("goods_title", goods_title);
                        context.startActivity(intent);
                    }
                }
            });
            LinearLayout childLeft = (LinearLayout) groupViewHolder.rl_group_left.getChildAt(0);
            TextView tvMallTitle = (TextView) childLeft.findViewById(R.id.tv_group_mall_title);
            TextView tvMallPrice = (TextView) childLeft.findViewById(R.id.tv_group_mall_price);
            ImageView ivMallPic = (ImageView) childLeft.findViewById(R.id.iv_group_mall_pic);
            TextView tvSmallZhuan = (TextView) childLeft.findViewById(R.id.tv_group_small_zhuan);
            TextView tvSmallCoast = (TextView) childLeft.findViewById(R.id.tv_group_small_coast);
            TextView tvSmallCoupons = (TextView) childLeft.findViewById(R.id.tv_group_small_coupons);
            TextView tvSmallSecondCoupons = (TextView) childLeft.findViewById(R.id.tv_group_small_second_coupons);
            final HomeGroupBean.DataBean groupBean1 = groupData.get(1);
            if (groupBean1.getTitle().length() > 12) {
                String sTitle = groupBean1.getTitle().substring(0, 12) + "...";
                tvMallTitle.setText(sTitle);
            } else {
                tvMallTitle.setText(groupBean1.getTitle());
            }
            final int couponsPrice1 = groupBean1.getCouponsPrice();
            double sell_price1 = groupBean1.getSell_price();
            double tkRate1 = groupBean1.getTkRate() / 100;
            double zhuan1 = (sell_price1 - couponsPrice1) * tkRate1 * 0.9f * user_rate;
            double coast1 = sell_price1 - zhuan1 - couponsPrice1;
            final String couponsUrl1 = groupBean1.getCouponsUrl();
            final String secondCouponsUrl1 = groupBean1.getSecondCouponsUrl();
            if (couponsPrice1 > 0) {
                tvSmallCoupons.setVisibility(View.VISIBLE);
                tvSmallCoupons.setText("领券立减" + couponsPrice1);
                tvSmallCoupons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (couponsUrl1 != null && !TextUtils.isEmpty(couponsUrl1)) {
                            Intent intent = new Intent(context, WebActivity.class);
                            intent.putExtra("web_url", couponsUrl1);
                            context.startActivity(intent);
                        }
                    }
                });
            } else {
                tvSmallCoupons.setVisibility(View.GONE);
            }

            if (secondCouponsUrl1 != null && !TextUtils.isEmpty(secondCouponsUrl1)) {
                tvSmallSecondCoupons.setVisibility(View.VISIBLE);
                tvSmallSecondCoupons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("web_url", secondCouponsUrl1);
                        context.startActivity(intent);
                    }
                });
            } else {
                tvSmallSecondCoupons.setVisibility(View.GONE);
            }
            tvMallPrice.setText("今日特价：￥" + df.format(sell_price1));
            tvSmallZhuan.setText("赚:￥" + df.format(zhuan1));
            tvSmallCoast.setText("成本:￥" + df.format(coast1));
            if (group_id == 4) {
                boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
                if (toggleShow) {
                    tvSmallZhuan.setVisibility(View.GONE);
                    tvSmallCoast.setVisibility(View.GONE);
                } else {
                    tvSmallZhuan.setVisibility(View.VISIBLE);
                    tvSmallCoast.setVisibility(View.VISIBLE);
                }
            } else {
                tvSmallZhuan.setVisibility(View.GONE);
                tvSmallCoast.setVisibility(View.GONE);
            }
            mImageLoader.loadImage(groupBean1.getImg_url(), ivMallPic, true);
            childLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (couponsPrice1 > 0) {
                        if (couponsUrl1 != null && !TextUtils.isEmpty(couponsUrl1)) {
                            Intent intent = new Intent(context, WebActivity.class);
                            intent.putExtra("web_url", couponsUrl1);
                            context.startActivity(intent);
                        }
                    } else {
                        String link_url = groupBean1.getLink_url();
                        String goods_id = groupBean1.getProductId();
                        String goods_img = groupBean1.getImg_url();
                        String goods_title = groupBean1.getTitle();
                        Intent intent = new Intent(context, WebRecordActivity.class);
                        intent.putExtra("url", link_url);
                        intent.putExtra("goods_id", goods_id);
                        intent.putExtra("goods_img", goods_img);
                        intent.putExtra("goods_title", goods_title);
                        context.startActivity(intent);
                    }
                }
            });
            int j = -1;
            for (int i = 2; i < groupData.size(); i++) {
                LinearLayout childRight = (LinearLayout) groupViewHolder.ll_group_right.getChildAt(i + j - 1);
                TextView tvMallTitle2 = (TextView) childRight.findViewById(R.id.tv_group_mall_title);
                TextView tvMallPrice2 = (TextView) childRight.findViewById(R.id.tv_group_mall_price);
                ImageView ivMallPic2 = (ImageView) childRight.findViewById(R.id.iv_group_mall_pic);
                TextView tvSmallZhuan2 = (TextView) childRight.findViewById(R.id.tv_group_small_zhuan);
                TextView tvSmallCoast2 = (TextView) childRight.findViewById(R.id.tv_group_small_coast);
                TextView tvSmallCoupons2 = (TextView) childRight.findViewById(R.id.tv_group_small_coupons);
                TextView tvSmallSecondCoupons2 = (TextView) childRight.findViewById(R.id.tv_group_small_second_coupons);
                final HomeGroupBean.DataBean groupBean2 = groupData.get(i);
                if (groupBean2.getTitle().length() > 12) {
                    String sTitle2 = groupBean2.getTitle().substring(0, 12) + "...";
                    tvMallTitle2.setText(sTitle2);
                } else {
                    tvMallPrice2.setText(groupBean2.getTitle());
                }
                final int couponsPrice2 = groupBean2.getCouponsPrice();
                double sell_price2 = groupBean2.getSell_price();
                double tkRate2 = groupBean2.getTkRate() / 100;
                double zhuan2 = (sell_price2 - couponsPrice2) * tkRate2 * 0.9f * user_rate;
                double coast2 = sell_price2 - zhuan2 - couponsPrice2;
                final String couponsUrl2 = groupBean2.getCouponsUrl();
                final String secondCouponsUrl2 = groupBean2.getSecondCouponsUrl();
                if (couponsPrice2 > 0) {
                    tvSmallCoupons2.setVisibility(View.VISIBLE);
                    tvSmallCoupons2.setText("领券立减" + couponsPrice2);
                    tvSmallCoupons2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (couponsUrl2 != null && !TextUtils.isEmpty(couponsUrl2)) {
                                Intent intent = new Intent(context, WebActivity.class);
                                intent.putExtra("web_url", couponsUrl2);
                                context.startActivity(intent);
                            }
                        }
                    });
                } else {
                    tvSmallCoupons2.setVisibility(View.GONE);
                }

                if (secondCouponsUrl2 != null && !TextUtils.isEmpty(secondCouponsUrl2)) {
                    tvSmallSecondCoupons2.setVisibility(View.VISIBLE);
                    tvSmallSecondCoupons2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, WebActivity.class);
                            intent.putExtra("web_url", secondCouponsUrl2);
                            context.startActivity(intent);
                        }
                    });
                } else {
                    tvSmallSecondCoupons2.setVisibility(View.GONE);
                }
                tvMallPrice2.setText("今日特价：￥" + df.format(sell_price2));
                tvSmallZhuan2.setText("赚:￥" + df.format(zhuan2));
                tvSmallCoast2.setText("成本:￥" + df.format(coast2));
                if (group_id == 4) {
                    boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
                    if (toggleShow) {
                        tvSmallZhuan2.setVisibility(View.GONE);
                        tvSmallCoast2.setVisibility(View.GONE);
                    } else {
                        tvSmallZhuan2.setVisibility(View.VISIBLE);
                        tvSmallCoast2.setVisibility(View.VISIBLE);
                    }
                } else {
                    tvSmallZhuan2.setVisibility(View.GONE);
                    tvSmallCoast2.setVisibility(View.GONE);
                }
                mImageLoader.loadImage(groupBean2.getImg_url(), ivMallPic2, true);
                childRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (couponsPrice2 > 0) {
                            if (couponsUrl2 != null && !TextUtils.isEmpty(couponsUrl2)) {
                                Intent intent = new Intent(context, WebActivity.class);
                                intent.putExtra("web_url", couponsUrl2);
                                context.startActivity(intent);
                            }
                        } else {
                            String link_url = groupBean2.getLink_url();
                            String goods_id = groupBean2.getProductId();
                            String goods_img = groupBean2.getImg_url();
                            String goods_title = groupBean2.getTitle();
                            Intent intent = new Intent(context, WebRecordActivity.class);
                            intent.putExtra("url", link_url);
                            intent.putExtra("goods_id", goods_id);
                            intent.putExtra("goods_img", goods_img);
                            intent.putExtra("goods_title", goods_title);
                            context.startActivity(intent);
                        }
                    }
                });
                j++;
            }
        } else if (holder instanceof FirstBuyViewHolder) {
            final QiangTaoBean.DataBean dataBean0 = qiangData.get(0);
            FirstBuyViewHolder firstBuyViewHoldre = (FirstBuyViewHolder) holder;
            TextView tvTitle = firstBuyViewHoldre.tvTitle;
            tvTitle.setText("抢购区");
            firstBuyViewHoldre.rl_item_laod_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, QiangTaoActivity.class);
                    context.startActivity(intent);
                }
            });

            String hour = getCurrentHour();
            int curr_index = Integer.parseInt(hour);
            firstBuyViewHoldre.tv_first_big_time.setText("距离第" + (curr_index + 1) + "场倒计时");
            firstBuyViewHoldre.llFirstLeftZero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    String link_url = dataBean0.getLink_url();
//                    String goods_id = String.valueOf(dataBean0.getProductId());
//                    String goods_img = dataBean0.getImg_url();
//                    String goods_title = dataBean0.getTitle();
//                    Intent intent = new Intent(context, WebRecordActivity.class);
//                    intent.putExtra("url",link_url);
//                    intent.putExtra("goods_id",goods_id);
//                    intent.putExtra("goods_img",goods_img);
//                    intent.putExtra("goods_title",goods_title);
//                    context.startActivity(intent);
                    Intent intent = new Intent(context, QiangTaoActivity.class);
                    context.startActivity(intent);
                }
            });
            if (dataBean0.getTitle().length() > 12) {
                firstBuyViewHoldre.tvFirstBigTitle.setText(dataBean0.getTitle().substring(0, 12) + "...");
            } else {
                firstBuyViewHoldre.tvFirstBigTitle.setText(dataBean0.getTitle());
            }
            DecimalFormat df = new DecimalFormat("#0.00");
            double sellPrice = Double.parseDouble(dataBean0.getSell_price());

            firstBuyViewHoldre.tvFirstBigPrice.setText("￥" + df.format(sellPrice));
            int group_id = CacheUtils.getInteger(context, CacheUtils.GROUP_TYPE, -1);
            if (group_id == 4) {
                boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
                if (toggleShow) {
                    firstBuyViewHoldre.tvFirstBigZhuan.setVisibility(View.GONE);
                    firstBuyViewHoldre.tvFirstBigChengben.setVisibility(View.GONE);
                } else {
                    double zhuan = sellPrice * 0.2f * 0.9f * user_rate;
                    double coast = sellPrice - zhuan;
                    firstBuyViewHoldre.tvFirstBigZhuan.setVisibility(View.VISIBLE);
                    firstBuyViewHoldre.tvFirstBigChengben.setVisibility(View.VISIBLE);
                    firstBuyViewHoldre.tvFirstBigZhuan.setText("赚:￥" + df.format(zhuan));
                    firstBuyViewHoldre.tvFirstBigChengben.setText("成本:￥" + df.format(coast));
                }
            } else {
                firstBuyViewHoldre.tvFirstBigZhuan.setVisibility(View.GONE);
                firstBuyViewHoldre.tvFirstBigChengben.setVisibility(View.GONE);
            }
            mImageLoader.loadImage(dataBean0.getImg_url(), ((FirstBuyViewHolder) holder).ivFirstbuyBig, true);
            tvTitle.setTextColor(Color.parseColor("#32C300"));
            firstBuyViewHoldre.tvRushHour.setText(rushDownTimer.getHour());
            firstBuyViewHoldre.tvRushMinute.setText(rushDownTimer.getMinute());
            firstBuyViewHoldre.tvRushSecond.setText(rushDownTimer.getSecond());
            LogUtil.e(hour + "--" + minute + "--" + second);
            final QiangTaoBean.DataBean dataBean1 = qiangData.get(1);
            final QiangTaoBean.DataBean dataBean2 = qiangData.get(2);
            QiangTaoBean.DataBean.CouponInfoBean coupon_info_l1 = dataBean1.getCoupon_info();
            QiangTaoBean.DataBean.CouponInfoBean coupon_info_l2 = dataBean2.getCoupon_info();
            LinearLayout llFirstWareZera = firstBuyViewHoldre.llFirstWareZera;
            for (int i = 0; i < llFirstWareZera.getChildCount(); i++) {
                if (i != 1) {
                    View child = llFirstWareZera.getChildAt(i);
                    LinearLayout llFirstRightZero = (LinearLayout) child.findViewById(R.id.ll_first_right_zero);
                    ImageView ivFirstMallBg = (ImageView) child.findViewById(R.id.iv_first_mall_bg);
                    ImageView ivFirstMallProgress = (ImageView) child.findViewById(R.id.iv_first_mall_progress);
                    ImageView ivSmall = (ImageView) child.findViewById(R.id.iv_first_buy_small);
                    TextView tvSmallTitle = (TextView) child.findViewById(R.id.tv_first_small_title);
                    TextView tvSmallCoupons = (TextView) child.findViewById(R.id.tv_first_small_coupons);
                    TextView tvSmallPrice = (TextView) child.findViewById(R.id.tv_first_small_price);
                    TextView tvSmallZhuan = (TextView) child.findViewById(R.id.tv_first_small_zhuan);
                    TextView tvPercent = (TextView) child.findViewById(R.id.tv_first_already_percent);
                    TextView tvAlreadyAcccount = (TextView) child.findViewById(R.id.tv_first_already_account);
                    double sell_price_s1 = Double.parseDouble(dataBean1.getSell_price());
                    double sell_price_s2 = Double.parseDouble(dataBean2.getSell_price());
                    int sold_num = dataBean2.getSold_num();
                    int total_amount = dataBean2.getTotal_amount();
                    int percent = 0;
                    if (total_amount == 0) {
                        percent = 0;
                    } else {
                        percent = (sold_num / total_amount) * 100;
                    }
                    tvPercent.setText(percent + "%");
                    tvAlreadyAcccount.setText("已抢" + sold_num + "件");
                    String coupon_info_s1 = null;
                    String coupon_info_s2 = null;
                    String coupon_click_url_s1 = null;
                    String coupon_click_url_s2 = null;
                    if(coupon_info_l1!=null) {
                        coupon_info_s1 = coupon_info_l1.getCoupon_info();
                        coupon_click_url_s1 = coupon_info_l1.getCoupon_click_url();
                    }

                    if(coupon_info_l2!=null) {
                        coupon_info_s2 = coupon_info_l2.getCoupon_info();
                       coupon_click_url_s2 = coupon_info_l2.getCoupon_click_url();
                    }
                    int coupon_p1 = 0;
                    int coupon_p2 = 0;
                    if (coupon_info_s1 != null && !TextUtils.isEmpty(coupon_info_s1)) {
                        int index_s1 = coupon_info_s1.indexOf("减") + 1;
                        String substring_s1 = coupon_info_s1.substring(index_s1, coupon_info_s1.length() - 1);
                        coupon_p1 = Integer.parseInt(substring_s1);
                    }

                    if (coupon_info_s2 != null && !TextUtils.isEmpty(coupon_info_s2)) {
                        int index_s2 = coupon_info_s2.indexOf("减") + 1;
                        String substring_s2 = coupon_info_s2.substring(index_s2, coupon_info_s2.length() - 1);
                        coupon_p2 = Integer.parseInt(substring_s2);
                    }

                    double zhuan_s1 = (sell_price_s1 - coupon_p1) * 0.2f * 0.9f * user_rate;
                    double zhuan_s2 = (sell_price_s2 - coupon_p2) * 0.2f * 0.9f * user_rate;
                    double coast_s1 = sell_price_s1 - zhuan_s1 - coupon_p1;
                    double coast_s2 = sell_price_s2 - zhuan_s2 - coupon_p2;
                    tvSmallCoupons.setVisibility(View.GONE);
                    final int pos = i;
                    final String finalCoupon_click_url_s = coupon_click_url_s1;
                    final String finalCoupon_click_url_s1 = coupon_click_url_s2;
                    final String finalCoupon_info_s = coupon_info_s1;
                    final String finalCoupon_info_s1 = coupon_info_s2;
                    llFirstRightZero.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (pos == 0) {
                                if (finalCoupon_info_s != null && !TextUtils.isEmpty(finalCoupon_info_s)) {
                                    Intent intent = new Intent(context, WebActivity.class);
                                    intent.putExtra("web_url", finalCoupon_click_url_s);
                                    context.startActivity(intent);
                                } else {
                                    String link_url = dataBean1.getLink_url();
                                    String goods_id = String.valueOf(dataBean1.getProductId());
                                    String goods_img = dataBean1.getImg_url();
                                    String goods_title = dataBean1.getTitle();
                                    Intent intent = new Intent(context, WebRecordActivity.class);
                                    intent.putExtra("url", link_url);
                                    intent.putExtra("goods_id", goods_id);
                                    intent.putExtra("goods_img", goods_img);
                                    intent.putExtra("goods_title", goods_title);
                                    context.startActivity(intent);
                                }
                            } else if (pos == 2) {
                                if (finalCoupon_info_s1 != null && !TextUtils.isEmpty(finalCoupon_info_s1)) {
                                    Intent intent = new Intent(context, WebActivity.class);
                                    intent.putExtra("web_url", finalCoupon_click_url_s1);
                                    context.startActivity(intent);
                                } else {
                                    String link_url = dataBean2.getLink_url();
                                    String goods_id = String.valueOf(dataBean2.getProductId());
                                    String goods_img = dataBean2.getImg_url();
                                    String goods_title = dataBean2.getTitle();
                                    Intent intent = new Intent(context, WebRecordActivity.class);
                                    intent.putExtra("url", link_url);
                                    intent.putExtra("goods_id", goods_id);
                                    intent.putExtra("goods_img", goods_img);
                                    intent.putExtra("goods_title", goods_title);
                                    context.startActivity(intent);
                                }
                            }
                        }
                    });
                    if (i == 0) {
                        if (dataBean1.getTitle().length() > 12) {
                            tvSmallTitle.setText(dataBean1.getTitle().substring(0, 12) + "...");
                        } else {
                            tvSmallTitle.setText(dataBean1.getTitle());
                        }
                        tvSmallPrice.setText("￥" + df.format(sell_price_s1));
                        tvSmallZhuan.setText("赚:￥" + df.format(zhuan_s1) + " 成本:￥" + df.format(coast_s1));
                        mImageLoader.loadImage(dataBean1.getImg_url(), ivSmall, true);
                        if (coupon_p1 == 0) {
                            tvSmallCoupons.setVisibility(View.GONE);
                        } else {
                            tvSmallCoupons.setVisibility(View.VISIBLE);
                            tvSmallCoupons.setText("领券立减" + coupon_p1 + "元");
                        }
                    } else if (i == 2) {
                        if (dataBean2.getTitle().length() > 12) {
                            tvSmallTitle.setText(dataBean2.getTitle().substring(0, 12) + "...");
                        } else {
                            tvSmallTitle.setText(dataBean2.getTitle());
                        }
                        tvSmallPrice.setText("￥" + df.format(sell_price_s2));
                        tvSmallZhuan.setText("赚:￥" + df.format(zhuan_s2) + " 成本:￥" + df.format(coast_s2));
                        mImageLoader.loadImage(dataBean2.getImg_url(), ivSmall, true);
                        if (coupon_p2 == 0) {
                            tvSmallCoupons.setVisibility(View.GONE);
                        } else {
                            tvSmallCoupons.setVisibility(View.VISIBLE);
                            tvSmallCoupons.setText("领券立减" + coupon_p2 + "元");
                        }
                    }
                    int width = ScreenUtil.getScreenWidth(context) * 15 / 60;
                    ViewGroup.LayoutParams lp2;
                    lp2 = ivFirstMallBg.getLayoutParams();
                    lp2.width = width;
                    ivFirstMallBg.setLayoutParams(lp2);
                    ViewGroup.LayoutParams lp;
                    lp = ivFirstMallProgress.getLayoutParams();
                    lp.width = width * (percent / 100);
                    ivFirstMallProgress.setLayoutParams(lp);

                    if (group_id == 4) {
                        boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
                        if (toggleShow) {
                            tvSmallZhuan.setVisibility(View.GONE);
                        } else {
                            tvSmallZhuan.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tvSmallZhuan.setVisibility(View.GONE);
                    }
                }
            }
            tvRushHour = firstBuyViewHoldre.tvRushHour;
            tvRushMinute = firstBuyViewHoldre.tvRushMinute;
            tvRushSecond = firstBuyViewHoldre.tvRushSecond;
        }else if(holder instanceof HomeVipViewHolder) {
            DecimalFormat df = new DecimalFormat("#0.00");
            final HomeVipBean.DataBean dataBean = vipData.get(0);
            HomeVipViewHolder homeVipViewHolder = (HomeVipViewHolder) holder;
            homeVipViewHolder.tvItemHomeTitle.setText("专享价区");
            homeVipViewHolder.tvItemHomeTitle.setTextColor(Color.parseColor("#CD6839"));
            homeVipViewHolder.rlItemLaodMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WareListActivity.class);
                    intent.putExtra("content", "isVip");
                    context.startActivity(intent);
                }
            });
            String img_url = dataBean.getImg_url();
            if(img_url!=null && homeVipViewHolder.ivVipBigPic!=null) {
                mImageLoader.loadImage(img_url,homeVipViewHolder.ivVipBigPic,true);
            }
            String title = dataBean.getTitle();
            if(title!=null) {
                if(title.length()>12) {
                    String s = title.substring(0, 12) + "...";
                    homeVipViewHolder.tvVipBigTitle.setText(s);
                }else{
                    homeVipViewHolder.tvVipBigTitle.setText(title);
                }
            }
            double tkRate = dataBean.getTkRate()/100;
            final int couponsPrice = dataBean.getCouponsPrice();
            final String couponsUrl = dataBean.getCouponsUrl();
            double sell_price = dataBean.getSell_price();
            final String secondCouponsUrl = dataBean.getSecondCouponsUrl();
            double zhuan = (sell_price -couponsPrice)*tkRate*0.9f*user_rate;
            double coast = sell_price -couponsPrice-zhuan;
            homeVipViewHolder.tvHomeVipZhuan.setText("赚:￥"+df.format(zhuan)+" 成本:￥"+df.format(coast));
            homeVipViewHolder.tvVipBigPrice.setText("专享价"+df.format(sell_price));

            int group_id = CacheUtils.getInteger(context, CacheUtils.GROUP_TYPE, -1);
            if (group_id == 4) {
                boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
                if (toggleShow) {
                    homeVipViewHolder.tvHomeVipZhuan.setVisibility(View.GONE);
                } else {
                   homeVipViewHolder.tvHomeVipZhuan.setVisibility(View.VISIBLE);
                }
            } else {
                homeVipViewHolder.tvHomeVipZhuan.setVisibility(View.GONE);
            }

            if(secondCouponsUrl!=null && !TextUtils.isEmpty(secondCouponsUrl)) {
                homeVipViewHolder.tvVipBigSecondCoupons.setVisibility(View.VISIBLE);
                homeVipViewHolder.tvVipBigSecondCoupons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("web_url", secondCouponsUrl);
                        context.startActivity(intent);
                    }
                });
            }else{
                homeVipViewHolder.tvVipBigSecondCoupons.setVisibility(View.GONE);
            }

            if(couponsPrice>0) {
                homeVipViewHolder.tvHomeVipCoupons.setVisibility(View.VISIBLE);
                homeVipViewHolder.tvHomeVipCoupons.setText("领券立减"+couponsPrice);
            }else{
                homeVipViewHolder.tvHomeVipCoupons.setVisibility(View.GONE);
            }

            homeVipViewHolder.llFirstLeftZero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(couponsUrl!=null && !TextUtils.isEmpty(couponsUrl)) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("web_url", couponsUrl);
                        context.startActivity(intent);
                    }else{
                        String link_url = dataBean.getLink_url();
                        String goods_id = String.valueOf(dataBean.getProductId());
                        String goods_img = dataBean.getImg_url();
                        String goods_title = dataBean.getTitle();
                        Intent intent = new Intent(context, WebRecordActivity.class);
                        intent.putExtra("url", link_url);
                        intent.putExtra("goods_id", goods_id);
                        intent.putExtra("goods_img", goods_img);
                        intent.putExtra("goods_title", goods_title);
                        context.startActivity(intent);
                    }
                }
            });

            final HomeVipBean.DataBean dataBean1 = vipData.get(1);
            String img_url1 = dataBean1.getImg_url();
            double sell_price1 = dataBean1.getSell_price();
            double tkRate1 = dataBean1.getTkRate();
            final int couponsPrice1 = dataBean1.getCouponsPrice();
            final String couponsUrl1 = dataBean1.getCouponsUrl();
            final String secondCouponsUrl1 = dataBean1.getSecondCouponsUrl();
            String title1 = dataBean1.getTitle();
            double zhuan1 = (sell_price1- couponsPrice1)*tkRate1*0.9f*0.83;
            double coast1 = sell_price1 - zhuan1 -couponsPrice1;

            final HomeVipBean.DataBean dataBean2 = vipData.get(2);
            String img_url2 = dataBean2.getImg_url();
            double sell_price2 = dataBean2.getSell_price();
            double tkRate2 = dataBean2.getTkRate();
            int couponsPrice2 = dataBean2.getCouponsPrice();
            final String couponsUrl2 = dataBean2.getCouponsUrl();
            final String secondCouponsUrl2 = dataBean2.getSecondCouponsUrl();
            String title2 = dataBean2.getTitle();
            double zhuan2 = (sell_price2- couponsPrice2)*tkRate1*0.9f*0.83;
            double coast2 = sell_price2 - zhuan2 -couponsPrice2;

            TextView tv_vip_mall_title =null;
            TextView tv_vip_mall_price =null;
            TextView tv_vip_small_zhuan =null;
            TextView tv_vip_small_coast =null;
            TextView tv_vip_small_coupons =null;
            ImageView iv_vip_mall_pic =null;
            TextView tv_vip_small_second_coupons =null;
            View childAt = null;
            LinearLayout llFirstVipZera = homeVipViewHolder.llFirstVipZera;
            for (int i=0;i<llFirstVipZera.getChildCount();i++){
                if(i!=1) {
                    childAt = llFirstVipZera.getChildAt(i);
                    tv_vip_mall_title = (TextView) childAt.findViewById(R.id.tv_vip_mall_title);
                    tv_vip_mall_price = (TextView) childAt.findViewById(R.id.tv_vip_mall_price);
                    tv_vip_small_zhuan = (TextView) childAt.findViewById(R.id.tv_vip_small_zhuan);
                    tv_vip_small_coast = (TextView) childAt.findViewById(R.id.tv_vip_small_coast);
                    tv_vip_small_coupons = (TextView) childAt.findViewById(R.id.tv_vip_small_coupons);
                    iv_vip_mall_pic = (ImageView) childAt.findViewById(R.id.iv_vip_mall_pic);
                    tv_vip_small_second_coupons = (TextView) childAt.findViewById(R.id.tv_vip_small_second_coupons);
                }

                if(i==0) {
                    if(img_url1!=null && iv_vip_mall_pic!=null) {
                        mImageLoader.loadImage(img_url1,iv_vip_mall_pic,true);
                    }
                    if(tv_vip_mall_title!=null && title1!=null) {
                        if(title1.length()>12) {
                            String s = title1.substring(0, 12) + "...";
                            tv_vip_mall_title.setText(s);
                        }else{
                            tv_vip_mall_title.setText(title1);
                        }
                    }
                    if(tv_vip_mall_price!=null) {
                        tv_vip_mall_price.setText("专项价"+df.format(sell_price1));
                    }

                    if(tv_vip_small_zhuan!=null) {
                       tv_vip_small_zhuan.setText("赚:￥"+df.format(zhuan1));
                        if (group_id == 4) {
                            boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
                            if (toggleShow) {
                                tv_vip_small_zhuan.setVisibility(View.GONE);
                            } else {
                                tv_vip_small_zhuan.setVisibility(View.VISIBLE);
                            }
                        } else {
                            tv_vip_small_zhuan.setVisibility(View.GONE);
                        }
                    }

                    if(tv_vip_small_coast!=null) {
                        tv_vip_small_coast.setText("成本:￥"+df.format(coast1));
                        if (group_id == 4) {
                            boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
                            if (toggleShow) {
                                tv_vip_small_coast.setVisibility(View.GONE);
                            } else {
                                tv_vip_small_coast.setVisibility(View.VISIBLE);
                            }
                        } else {
                            tv_vip_small_coast.setVisibility(View.GONE);
                        }
                    }

                    if(tv_vip_small_second_coupons!=null) {
                        if(secondCouponsUrl1!=null && !TextUtils.isEmpty(secondCouponsUrl1)) {
                            tv_vip_small_second_coupons.setVisibility(View.VISIBLE);
                            tv_vip_small_second_coupons.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, WebActivity.class);
                                    intent.putExtra("web_url", secondCouponsUrl1);
                                    context.startActivity(intent);
                                }
                            });
                        }else{
                            tv_vip_small_second_coupons.setVisibility(View.GONE);
                        }
                    }

                    if(tv_vip_small_coupons!=null) {
                        if(couponsPrice1>0) {
                            tv_vip_small_coupons.setVisibility(View.VISIBLE);
                            tv_vip_small_coupons.setText("领券立减"+couponsPrice1);
                        }else{
                            tv_vip_small_coupons.setVisibility(View.GONE);
                        }
                    }

                    if(childAt!=null) {
                        childAt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(couponsUrl1!=null && !TextUtils.isEmpty(couponsUrl1)) {
                                    Intent intent = new Intent(context, WebActivity.class);
                                    intent.putExtra("web_url", couponsUrl1);
                                    context.startActivity(intent);
                                }else{
                                    String link_url = dataBean1.getLink_url();
                                    String goods_id = String.valueOf(dataBean1.getProductId());
                                    String goods_img = dataBean1.getImg_url();
                                    String goods_title = dataBean1.getTitle();
                                    Intent intent = new Intent(context, WebRecordActivity.class);
                                    intent.putExtra("url", link_url);
                                    intent.putExtra("goods_id", goods_id);
                                    intent.putExtra("goods_img", goods_img);
                                    intent.putExtra("goods_title", goods_title);
                                    context.startActivity(intent);
                                }
                            }
                        });
                    }
                }

                if(i==2) {
                    if(img_url2!=null && iv_vip_mall_pic!=null) {
                        mImageLoader.loadImage(img_url2,iv_vip_mall_pic,true);
                    }
                    if(tv_vip_mall_title!=null && title2!=null) {
                        if(title2.length()>12) {
                            String s = title2.substring(0, 12) + "...";
                            tv_vip_mall_title.setText(s);
                        }else{
                            tv_vip_mall_title.setText(title2);
                        }
                    }
                    if(tv_vip_mall_price!=null) {
                        tv_vip_mall_price.setText("专项价"+df.format(sell_price2));
                    }

                    if(tv_vip_small_zhuan!=null) {
                        tv_vip_small_zhuan.setText("赚:￥"+df.format(zhuan2));
                        if (group_id == 4) {
                            boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
                            if (toggleShow) {
                                tv_vip_small_zhuan.setVisibility(View.GONE);
                            } else {
                                tv_vip_small_zhuan.setVisibility(View.VISIBLE);
                            }
                        } else {
                            tv_vip_small_zhuan.setVisibility(View.GONE);
                        }
                    }

                    if(tv_vip_small_coast!=null) {
                        tv_vip_small_coast.setText("成本:￥"+df.format(coast2));
                        if (group_id == 4) {
                            boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
                            if (toggleShow) {
                                tv_vip_small_coast.setVisibility(View.GONE);
                            } else {
                                tv_vip_small_coast.setVisibility(View.VISIBLE);
                            }
                        } else {
                            tv_vip_small_coast.setVisibility(View.GONE);
                        }
                    }

                    if(tv_vip_small_second_coupons!=null) {
                        if(secondCouponsUrl2!=null && !TextUtils.isEmpty(secondCouponsUrl2)) {
                            tv_vip_small_second_coupons.setVisibility(View.VISIBLE);
                            tv_vip_small_second_coupons.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, WebActivity.class);
                                    intent.putExtra("web_url", secondCouponsUrl2);
                                    context.startActivity(intent);
                                }
                            });
                        }else{
                            tv_vip_small_second_coupons.setVisibility(View.GONE);
                        }
                    }

                    if(tv_vip_small_coupons!=null) {
                        if(couponsPrice2>0) {
                            tv_vip_small_coupons.setVisibility(View.VISIBLE);
                            tv_vip_small_coupons.setText("领券立减"+couponsPrice2);
                        }else{
                            tv_vip_small_coupons.setVisibility(View.GONE);
                        }
                    }

                    if(childAt!=null) {
                        childAt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(couponsUrl2!=null && !TextUtils.isEmpty(couponsUrl2)) {
                                    Intent intent = new Intent(context, WebActivity.class);
                                    intent.putExtra("web_url", couponsUrl2);
                                    context.startActivity(intent);
                                }else{
                                    String link_url = dataBean2.getLink_url();
                                    String goods_id = String.valueOf(dataBean2.getProductId());
                                    String goods_img = dataBean2.getImg_url();
                                    String goods_title = dataBean2.getTitle();
                                    Intent intent = new Intent(context, WebRecordActivity.class);
                                    intent.putExtra("url", link_url);
                                    intent.putExtra("goods_id", goods_id);
                                    intent.putExtra("goods_img", goods_img);
                                    intent.putExtra("goods_title", goods_title);
                                    context.startActivity(intent);
                                }
                            }
                        });
                    }


                }


            }
        }
    }

    private String getCurrentHour() {
        long curr_time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        Date date = new Date(curr_time);
        String currtimes = sdf.format(date);
        String[] timesArray = currtimes.split(":");
        return timesArray[3];
    }

    @Override
    public int getItemCount() {
        return data.size() + extraCount;
    }

    /**
     * footer view
     */
    class FooterViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_load_prompt)
        TextView tv_load_prompt;
        @Bind(R.id.progress)
        ProgressBar progress;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 40));
            itemView.setLayoutParams(params);
        }

        private void bindItem() {
            switch (status) {
                case LOAD_MORE:
                    progress.setVisibility(View.VISIBLE);
                    tv_load_prompt.setText("正在加载...");
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_PULL_TO:
                    progress.setVisibility(View.GONE);
                    tv_load_prompt.setText("上拉加载更多");
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_NONE:
                    System.out.println("LOAD_NONE----");
                    progress.setVisibility(View.GONE);
                    tv_load_prompt.setText("已无更多加载");
                    break;
                case LOAD_END:
                    itemView.setVisibility(View.GONE);
                default:
                    break;
            }
        }
    }

    /**
     * 轮播图的ViewHoldre
     */
    class BannerViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.civ_home_ganner)
        CarouselImageView civHomeGanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 抢购的ViewHolder
     */
    class FirstBuyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.rl_item_laod_more)
        RelativeLayout rl_item_laod_more;
        @Bind(R.id.tv_item_home_title)
        TextView tvTitle;
        @Bind(R.id.tv_item_home_more)
        TextView tvItemHomeMore;
        @Bind(R.id.tv_rush_hour)
        TextView tvRushHour;
        @Bind(R.id.tv_rush_minute)
        TextView tvRushMinute;
        @Bind(R.id.tv_rush_second)
        TextView tvRushSecond;
        @Bind(R.id.ll_first_ware_zera)
        LinearLayout llFirstWareZera;
        @Bind(R.id.iv_firstbuy_big)
        ImageView ivFirstbuyBig;
        @Bind(R.id.tv_first_big_title)
        TextView tvFirstBigTitle;
        @Bind(R.id.tv_first_big_price)
        TextView tvFirstBigPrice;
        @Bind(R.id.tv_first_big_zhuan)
        TextView tvFirstBigZhuan;
        @Bind(R.id.tv_first_big_chengben)
        TextView tvFirstBigChengben;
        @Bind(R.id.ll_first_left_zero)
        LinearLayout llFirstLeftZero;
        @Bind(R.id.tv_first_big_time)
        TextView tv_first_big_time;

        public FirstBuyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 团购的ViewHolder
     */
    class GroupViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_item_home_title)
        TextView tvTitle;
        @Bind(R.id.rl_group_left)
        RelativeLayout rl_group_left;
        @Bind(R.id.ll_group_right)
        LinearLayout ll_group_right;
        @Bind(R.id.iv_group_big_pic)
        ImageView iv_group_big_pic;
        @Bind(R.id.tv_group_big_title)
        TextView tv_group_big_title;
        @Bind(R.id.tv_group_big_price)
        TextView tv_group_big_price;
        @Bind(R.id.ll_group_big)
        LinearLayout ll_group_big;
        @Bind(R.id.tv_home_group_zhuan)
        TextView tvHomeGroupZhuan;
        @Bind(R.id.tv_home_group_coupons)
        TextView tvHomeGroupCoupons;
        @Bind(R.id.tv_group_big_second_coupons)
        TextView tvGroupBigSecondSoupons;
        @Bind(R.id.rl_item_laod_more)
        RelativeLayout rl_item_laod_more;

        public GroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 分类的ViewHolder
     */
    class TypeViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.gv_home_type)
        MyGridView gvHomeType;

        public TypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 新日新品等的ViewHolder
     */
    class RushWareViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.gv_rush_zera)
        GridView gvRushZera;
        @Bind(R.id.tv_rush_more_title)
        TextView tvRushMoreTitle;
        @Bind(R.id.iv_rush_more_big)
        ImageView ivRushMoreBig;

        public RushWareViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 专享
     */
    class HomeVipViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.v_type_line)
        View vTypeLine;
        @Bind(R.id.tv_item_home_title)
        TextView tvItemHomeTitle;
        @Bind(R.id.iv_home_rcv_right)
        ImageView ivHomeRcvRight;
        @Bind(R.id.tv_item_home_more)
        TextView tvItemHomeMore;
        @Bind(R.id.rl_item_laod_more)
        RelativeLayout rlItemLaodMore;
        @Bind(R.id.iv_vip_big_pic)
        ImageView ivVipBigPic;
        @Bind(R.id.tv_vip_big_second_coupons)
        TextView tvVipBigSecondCoupons;
        @Bind(R.id.tv_vip_big_title)
        TextView tvVipBigTitle;
        @Bind(R.id.tv_vip_big_price)
        TextView tvVipBigPrice;
        @Bind(R.id.tv_home_vip_zhuan)
        TextView tvHomeVipZhuan;
        @Bind(R.id.tv_home_vip_coupons)
        TextView tvHomeVipCoupons;
        @Bind(R.id.ll_first_left_zero)
        LinearLayout llFirstLeftZero;
        @Bind(R.id.tv_vip_mall_title)
        TextView tvVipMallTitles;
        @Bind(R.id.tv_vip_mall_price)
        TextView tvVipMallPrices;
        @Bind(R.id.tv_vip_small_zhuan)
        TextView tvVipSmallZhuans;
        @Bind(R.id.tv_vip_small_coast)
        TextView tvVipSmallCoasts;
        @Bind(R.id.tv_vip_small_coupons)
        TextView tvVipSmallCouponss;
        @Bind(R.id.iv_vip_mall_pic)
        ImageView ivVipMallPics;
        @Bind(R.id.tv_vip_small_second_coupons)
        TextView tvVipSmallSecondCouponss;
        @Bind(R.id.ll_first_vip_zera)
        LinearLayout llFirstVipZera;

        public HomeVipViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // change recycler state
    public void updateLoadStatus(int status) {
        this.status = status;
        notifyDataSetChanged();
    }

    /**
     * 联网获取banner图的数据
     *
     * @param civHomeGanner
     */
    private void getBannerData(final CarouselImageView civHomeGanner) {
        netUtils = new NetUtil();
        netUtils.okHttp2Server2(url, null);
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                parseData(response, civHomeGanner);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });
    }

    /**
     * 解析数据
     *
     * @param response
     * @param civHomeGanner
     */
    private void parseData(String response, CarouselImageView civHomeGanner) {
        Gson gson = new Gson();
        HomeBannerBean homeBannerBean = gson.fromJson(response, HomeBannerBean.class);
        int statusCode = homeBannerBean.getStatusCode();
        List<HomeBannerBean.DataBean> data = homeBannerBean.getData();
        switch (statusCode) {
            case 1:
                showBanner(data, civHomeGanner);
                break;
            case 0:
                LogUtil.e(homeBannerBean.getStatusMessage());
                break;
        }
    }

    /**
     * 首页banner图的显示
     *
     * @param data
     * @param civHomeGanner
     */
    private void showBanner(final List<HomeBannerBean.DataBean> data, CarouselImageView civHomeGanner) {
        RelativeLayout.LayoutParams cParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        civHomeGanner.setLayoutParams(cParams);
        CarouselImageView.ImageCycleViewListener mAdCycleViewListener = new CarouselImageView.ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {
                String link_url = data.get(position % data.size()).getLink_url();
                String goods_id = data.get(position % data.size()).getProductId();
                String goods_img = Url.IMG_URL + data.get(position % data.size()).getImg_url();
                String goods_title = data.get(position % data.size()).getTitle();
                Intent intent = new Intent(context, WebRecordActivity.class);
                intent.putExtra("url", link_url);
                intent.putExtra("goods_id", goods_id);
                intent.putExtra("goods_img", goods_img);
                intent.putExtra("goods_title", goods_title);
                context.startActivity(intent);
            }

            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                mImageLoader.loadImage(imageURL, imageView, true);
            }
        };
        civHomeGanner.setImageResources(data, mAdCycleViewListener);
        civHomeGanner.startImageCycle();
    }

    /**
     * 首页分类的数据展示
     *
     * @param gvHomeType
     */
    private void loadTypeData(GridView gvHomeType) {
        ArrayList<HomeTypeBean> lists = new ArrayList<>();
        for (int i = 0; i < HomeData.titles.length; i++) {
            HomeTypeBean homeTypeBean = new HomeTypeBean(HomeData.imgs[i], HomeData.titles[i]);
            lists.add(homeTypeBean);
        }
        HomeTypeAdapter homeTypeAdapter = new HomeTypeAdapter(context, lists);
        gvHomeType.setAdapter(homeTypeAdapter);
    }

    public void setRushDownTimer(RushDownTimer rushDownTimer) {
        this.rushDownTimer = rushDownTimer;
        if (tvRushHour != null) {
            tvRushHour.setText(rushDownTimer.getHour());
        }
        if (tvRushMinute != null) {
            tvRushMinute.setText(rushDownTimer.getMinute());
        }
        if (tvRushSecond != null) {
            tvRushSecond.setText(rushDownTimer.getSecond());
        }

    }


}
