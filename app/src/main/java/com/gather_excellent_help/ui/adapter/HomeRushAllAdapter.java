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
import com.gather_excellent_help.bean.HomeRushChangeBean;
import com.gather_excellent_help.bean.HomeTypeBean;
import com.gather_excellent_help.bean.HomeWareBean;
import com.gather_excellent_help.bean.QiangTaoBean;
import com.gather_excellent_help.bean.TyepIndexBean;
import com.gather_excellent_help.ui.activity.WareListActivity;
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

import java.util.ArrayList;
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
    private ImageLoader mImageLoader;
    private NetUtil netUtils;
    private Map<String, String> map;
    private String url = Url.BASE_URL + "IndexBanner.aspx";
    private List<HomeWareBean.DataBean> data;
    private List<HomeGroupBean.DataBean> groupData;
    private List<TyepIndexBean.DataBean> typeData;
    private List<QiangTaoBean.DataBean> qiangData;
    private int extraCount = 4;

    private RushDownTimer rushDownTimer;
    private String hour;
    private String minute;
    private String second;
    private TextView tvRushHour;
    private TextView tvRushMinute;
    private TextView tvRushSecond;
    private boolean isFirst = true;



    public HomeRushAllAdapter(Context context, List<HomeWareBean.DataBean> data, Activity activity, List<HomeGroupBean.DataBean> groupData, List<TyepIndexBean.DataBean> typeData,
                              RushDownTimer rushDownTimer,List<QiangTaoBean.DataBean> qiangData) {
        this.context = context;
        this.activity = activity;
        this.data = data;
        this.groupData = groupData;
        this.typeData = typeData;
        this.qiangData = qiangData;
        this.rushDownTimer = rushDownTimer;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    @Override
    public int getItemViewType(int position) {
        if(isFirst) {
            extraCount =4;
            if(position == 0) {
                return TYPE_TOP;
            }else if(position == 1) {
                return TYPE_GRID;
            }else if(position == 2) {
                return TYPE_FIRSTBUY;
            }else if(position == 3) {
                return TYPE_GROUP;
            }  else {
                return position;
            }
        }else{
            extraCount = 3;
            if(position == 0) {
                return TYPE_TOP;
            }else if(position == 1) {
                return TYPE_GRID;
            }else if(position == 2) {
                return TYPE_GROUP;
            }  else {
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
        } else if(viewType == TYPE_GRID) {
            View rootView = View.inflate(parent.getContext(),R.layout.component_home_type,null);
            return new TypeViewHolder(rootView);
        }else if(viewType == TYPE_FIRSTBUY) {
            View rootView = View.inflate(parent.getContext(),R.layout.component_firstbuy_zera,null);
            return new FirstBuyViewHolder(rootView);
        }else if(viewType == TYPE_GROUP) {
            View rootView = View.inflate(parent.getContext(),R.layout.component_group_zera,null);
            return new GroupViewHolder(rootView);
        }else if(viewType == TYPE_FOOTER){
            View view = View.inflate(parent.getContext(), R.layout.load_more, null);
            return new FooterViewHolder(view);
        }else {
            View rootView = View.inflate(parent.getContext(), R.layout.component_rush_zrea, null);
            return new RushWareViewHolder(rootView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.bindItem();
        }else if(holder instanceof TypeViewHolder) {

            TypeViewHolder typeViewHolder = (TypeViewHolder) holder;
            GridView gvHomeType = typeViewHolder.gvHomeType;
            loadTypeData(gvHomeType);
            gvHomeType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int id = typeData.get(i).getId();
                    Intent intent = new Intent(context, WareListActivity.class);
                    intent.putExtra("type_id",String.valueOf(id));
                    context.startActivity(intent);
                }
            });
        }else if (holder instanceof BannerViewHolder) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            CarouselImageView civHomeGanner = bannerViewHolder.civHomeGanner;
            getBannerData(civHomeGanner);
        } else if (holder instanceof RushWareViewHolder) {
            RushWareViewHolder rushWareViewHolder = (RushWareViewHolder) holder;
            final HomeWareBean.DataBean dataBean = data.get(position - extraCount);
            rushWareViewHolder.tvRushMoreTitle.setText(dataBean.getTitle());
            String url = dataBean.getImg_url();
            LogUtil.e(url);
            mImageLoader.loadImage(url,rushWareViewHolder.ivRushMoreBig,true);
            rushWareViewHolder.ivRushMoreBig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = dataBean.getId();
                    Intent intent = new Intent(context, WareListActivity.class);
                    intent.putExtra("type_id",String.valueOf(id));
                    context.startActivity(intent);
                }
            });
            final List<HomeWareBean.DataBean.ItemBean> itemData = dataBean.getItem();
           if( itemData!= null && itemData.size()> 2) {
               HomeRushAdapter homeRushAdapter = new HomeRushAdapter(context,itemData);
               rushWareViewHolder.gvRushZera.setAdapter(homeRushAdapter);
               rushWareViewHolder.gvRushZera.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                       String link_url = itemData.get(i).getLink_url();
                       String goods_id = itemData.get(i).getProductId();
                       String goods_img = Url.IMG_URL + itemData.get(i).getImg_url();
                       String goods_title = itemData.get(i).getTitle();
                       Intent intent = new Intent(context, WebRecordActivity.class);
                       intent.putExtra("url",link_url);
                       intent.putExtra("goods_id",goods_id);
                       intent.putExtra("goods_img",goods_img);
                       intent.putExtra("goods_title",goods_title);
                       context.startActivity(intent);
                   }
               });
           }
        } else if(holder instanceof GroupViewHolder) {
            GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
            TextView tvTitle = groupViewHolder.tvTitle;
            tvTitle.setText("团购区");
            final HomeGroupBean.DataBean dataBean = groupData.get(0);
            String sTitle0 = dataBean.getTitle().substring(0, 12) + "...";
            groupViewHolder.tv_group_big_title.setText(sTitle0);
            groupViewHolder.tv_group_big_price.setText("今日特价：￥"+dataBean.getMarket_price());
            mImageLoader.loadImage(dataBean.getImg_url(),groupViewHolder.iv_group_big_pic,true);
            groupViewHolder.ll_group_big.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String link_url = dataBean.getLink_url();
                    String goods_id = dataBean.getProductId();
                    String goods_img = Url.IMG_URL + dataBean.getImg_url();
                    String goods_title = dataBean.getTitle();
                    Intent intent = new Intent(context, WebRecordActivity.class);
                    intent.putExtra("url",link_url);
                    intent.putExtra("goods_id",goods_id);
                    intent.putExtra("goods_img",goods_img);
                    intent.putExtra("goods_title",goods_title);
                    context.startActivity(intent);
                }
            });
            LinearLayout childLeft = (LinearLayout) groupViewHolder.rl_group_left.getChildAt(0);
            TextView tvMallTitle = (TextView) childLeft.findViewById(R.id.tv_group_mall_title);
            TextView tvMallPrice = (TextView) childLeft.findViewById(R.id.tv_group_mall_price);
            ImageView ivMallPic = (ImageView) childLeft.findViewById(R.id.iv_group_mall_pic);
            final HomeGroupBean.DataBean groupBean1 = groupData.get(1);
            String sTitle = groupBean1.getTitle().substring(0, 12) + "...";
            tvMallPrice.setText("今日特价：￥"+groupBean1.getMarket_price());
            tvMallTitle.setText(sTitle);
            mImageLoader.loadImage(groupBean1.getImg_url(),ivMallPic,true);
            childLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String link_url = groupBean1.getLink_url();
                    String goods_id = groupBean1.getProductId();
                    String goods_img = Url.IMG_URL + groupBean1.getImg_url();
                    String goods_title = groupBean1.getTitle();
                    Intent intent = new Intent(context, WebRecordActivity.class);
                    intent.putExtra("url",link_url);
                    intent.putExtra("goods_id",goods_id);
                    intent.putExtra("goods_img",goods_img);
                    intent.putExtra("goods_title",goods_title);
                    context.startActivity(intent);
                }
            });
            int j=-1;
            for (int i=2;i<groupData.size();i++){
                LinearLayout childRight = (LinearLayout) groupViewHolder.ll_group_right.getChildAt(i + j - 1);
                TextView tvMallTitle2 = (TextView) childRight.findViewById(R.id.tv_group_mall_title);
                TextView tvMallPrice2 = (TextView) childRight.findViewById(R.id.tv_group_mall_price);
                ImageView ivMallPic2 = (ImageView) childRight.findViewById(R.id.iv_group_mall_pic);
                final HomeGroupBean.DataBean groupBean2 = groupData.get(i);
                String sTitle2 = groupBean2.getTitle().substring(0, 12) + "...";
                tvMallPrice2.setText("今日特价：￥"+groupBean2.getMarket_price());
                tvMallTitle2.setText(sTitle2);
                mImageLoader.loadImage(groupBean2.getImg_url(),ivMallPic2,true);
                childRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String link_url = groupBean2.getLink_url();
                        String goods_id = groupBean2.getProductId();
                        String goods_img = Url.IMG_URL + groupBean2.getImg_url();
                        String goods_title = groupBean2.getTitle();
                        Intent intent = new Intent(context, WebRecordActivity.class);
                        intent.putExtra("url",link_url);
                        intent.putExtra("goods_id",goods_id);
                        intent.putExtra("goods_img",goods_img);
                        intent.putExtra("goods_title",goods_title);
                        context.startActivity(intent);
                    }
                });
                j++;
            }
        }else if(holder instanceof FirstBuyViewHolder) {
            QiangTaoBean.DataBean dataBean0 = qiangData.get(0);
            FirstBuyViewHolder firstBuyViewHoldre = (FirstBuyViewHolder) holder;
            TextView tvTitle = firstBuyViewHoldre.tvTitle;
            tvTitle.setText("抢购区");
            firstBuyViewHoldre.tvItemHomeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,WareListActivity.class);
                    intent.putExtra("content","isQiang");
                    context.startActivity(intent);
                }
            });
            if(dataBean0.getTitle().length()>12) {
                firstBuyViewHoldre.tvFirstBigTitle.setText(dataBean0.getTitle().substring(0,12)+"...");
            }else{
                firstBuyViewHoldre.tvFirstBigTitle.setText(dataBean0.getTitle());
            }
            firstBuyViewHoldre.tvFirstBigPrice.setText("￥"+dataBean0.getSell_price());
            int group_id = CacheUtils.getInteger(context, CacheUtils.GROUP_TYPE, -1);
            if(group_id==4){
                boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
                if(toggleShow) {
                    firstBuyViewHoldre.tvFirstBigZhuan.setVisibility(View.GONE);
                    firstBuyViewHoldre.tvFirstBigChengben.setVisibility(View.GONE);
                }else{
                    firstBuyViewHoldre.tvFirstBigZhuan.setVisibility(View.VISIBLE);
                    firstBuyViewHoldre.tvFirstBigChengben.setVisibility(View.VISIBLE);
                }
            }else{
                firstBuyViewHoldre.tvFirstBigZhuan.setVisibility(View.GONE);
                firstBuyViewHoldre.tvFirstBigChengben.setVisibility(View.GONE);
            }
            mImageLoader.loadImage(dataBean0.getImg_url(),((FirstBuyViewHolder) holder).ivFirstbuyBig,true);
            tvTitle.setTextColor(Color.parseColor("#32C300"));
            firstBuyViewHoldre.tvRushHour.setText(rushDownTimer.getHour());
            firstBuyViewHoldre.tvRushMinute.setText(rushDownTimer.getMinute());
            firstBuyViewHoldre.tvRushSecond.setText(rushDownTimer.getSecond());
            LogUtil.e(hour +"--"+ minute + "--" + second);
            QiangTaoBean.DataBean dataBean1 = qiangData.get(1);
            QiangTaoBean.DataBean dataBean2 = qiangData.get(2);
            LinearLayout llFirstWareZera = firstBuyViewHoldre.llFirstWareZera;
            for (int i=0;i<llFirstWareZera.getChildCount();i++){
                if(i!=1) {
                    View child = llFirstWareZera.getChildAt(i);
                    ImageView ivFirstMallBg = (ImageView) child.findViewById(R.id.iv_first_mall_bg);
                    ImageView ivFirstMallProgress = (ImageView) child.findViewById(R.id.iv_first_mall_progress);
                    ImageView ivSmall= (ImageView) child.findViewById(R.id.iv_first_buy_small);
                    if(i==0) {
                        mImageLoader.loadImage(dataBean1.getImg_url(),ivSmall,true);
                    }else if(i==2){
                        mImageLoader.loadImage(dataBean2.getImg_url(),ivSmall,true);
                    }
                    int width = ScreenUtil.getScreenWidth(context)*17/60;
                    ViewGroup.LayoutParams lp2;
                    lp2 = ivFirstMallBg.getLayoutParams();
                    lp2.width =width;
                    ivFirstMallBg.setLayoutParams(lp2);
                    ViewGroup.LayoutParams lp;
                    lp =  ivFirstMallProgress.getLayoutParams();
                    lp.width = width/6;
                    ivFirstMallProgress.setLayoutParams(lp);
                }
            }

            tvRushHour = firstBuyViewHoldre.tvRushHour;
            tvRushMinute = firstBuyViewHoldre.tvRushMinute;
            tvRushSecond = firstBuyViewHoldre.tvRushSecond;
        }
    }

    @Override
    public int getItemCount() {
        return data.size()+extraCount;
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
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context,40));
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

    // change recycler state
    public void updateLoadStatus(int status) {
        this.status = status;
        notifyDataSetChanged();
    }

    /**
     * 联网获取banner图的数据
     * @param civHomeGanner
     */
    private void getBannerData(final CarouselImageView civHomeGanner) {
        netUtils = new NetUtil();
        netUtils.okHttp2Server2(url,null);
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                parseData(response,civHomeGanner);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });
    }

    /**
     * 解析数据
     * @param response
     * @param civHomeGanner
     */
    private void parseData(String response, CarouselImageView civHomeGanner) {
        Gson gson = new Gson();
        HomeBannerBean homeBannerBean = gson.fromJson(response, HomeBannerBean.class);
        int statusCode = homeBannerBean.getStatusCode();
        List<HomeBannerBean.DataBean> data = homeBannerBean.getData();
        switch (statusCode) {
            case 1 :
                showBanner(data,civHomeGanner);
                break;
            case 0:
                LogUtil.e(homeBannerBean.getStatusMessage());
                break;
        }
    }

    /**
     * 首页banner图的显示
     * @param data
     * @param civHomeGanner
     */
    private void showBanner(final List<HomeBannerBean.DataBean> data, CarouselImageView civHomeGanner) {
        RelativeLayout.LayoutParams cParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        civHomeGanner.setLayoutParams(cParams);
        CarouselImageView.ImageCycleViewListener mAdCycleViewListener = new CarouselImageView.ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {
                String link_url = data.get(position%data.size()).getLink_url();
                String goods_id = data.get(position%data.size()).getProductId();
                String goods_img = Url.IMG_URL + data.get(position%data.size()).getImg_url();
                String goods_title = data.get(position%data.size()).getTitle();
                Intent intent = new Intent(context, WebRecordActivity.class);
                intent.putExtra("url",link_url);
                intent.putExtra("goods_id",goods_id);
                intent.putExtra("goods_img",goods_img);
                intent.putExtra("goods_title",goods_title);
                context.startActivity(intent);
            }

            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                mImageLoader.loadImage(imageURL,imageView,true);
            }
        };
        civHomeGanner.setImageResources(data, mAdCycleViewListener);
        civHomeGanner.startImageCycle();
    }

    /**
     * 首页分类的数据展示
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
        tvRushHour.setText(rushDownTimer.getHour());
        tvRushMinute.setText(rushDownTimer.getMinute());
        tvRushSecond.setText(rushDownTimer.getSecond());
    }


}
