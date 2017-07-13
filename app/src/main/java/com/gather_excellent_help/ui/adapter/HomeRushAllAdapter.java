package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.HomeData;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.HomeBannerBean;
import com.gather_excellent_help.bean.HomeTypeBean;
import com.gather_excellent_help.ui.activity.TestActivity2;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.ui.widget.CarouselImageView;
import com.gather_excellent_help.ui.widget.MyGridView;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
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

    public HomeRushAllAdapter(Context context) {
        this.context = context;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    @Override
    public int getItemViewType(int position) {
            if(position == 0) {
                return TYPE_TOP;
            }else if(position == 1) {
                return TYPE_GRID;
            }else if(position == 2) {
                return TYPE_FIRSTBUY;
            }else if(position == 3) {
                return TYPE_GROUP;
            } else if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return position;
            }
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
        }else if (holder instanceof BannerViewHolder) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            CarouselImageView civHomeGanner = bannerViewHolder.civHomeGanner;
            getBannerData(civHomeGanner);
        } else if (holder instanceof RushWareViewHolder) {
            RushWareViewHolder rushWareViewHolder = (RushWareViewHolder) holder;
            GridView gvRushZera = rushWareViewHolder.gvRushZera;
            HomeRushAdapter homeRushAdapter = new HomeRushAdapter(context);
            gvRushZera.setAdapter(homeRushAdapter);
        } else if(holder instanceof GroupViewHolder) {
            GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
            TextView tvTitle = groupViewHolder.tvTitle;
            tvTitle.setText("团购区");
        }else if(holder instanceof FirstBuyViewHolder) {
            FirstBuyViewHolder firstBuyViewHoldre = (FirstBuyViewHolder) holder;
            TextView tvTitle = firstBuyViewHoldre.tvTitle;
            tvTitle.setText("抢购区");
            tvTitle.setTextColor(Color.parseColor("#32C300"));
        }
    }

    @Override
    public int getItemCount() {
        return 10;//
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
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("url",data.get(position).getLink_url());
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
}