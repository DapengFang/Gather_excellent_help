package com.gather_excellent_help.presenter.homepresenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.HomeBannerBean;
import com.gather_excellent_help.presenter.BasePresenter;
import com.gather_excellent_help.ui.activity.WareListActivity;
import com.gather_excellent_help.ui.activity.WebRecordActivity;
import com.gather_excellent_help.ui.activity.suning.SuningDetailActivity;
import com.gather_excellent_help.ui.widget.CarouselImageView;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.imageutils.ImageLoader;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2017/8/21.
 */

public class BannerPresenter extends BasePresenter {

    private Activity context;
    private CarouselImageView civHomeGanner;
    private RelativeLayout rl_home_banner;
    private NetUtil netUtils;
    private String banner_url = Url.BASE_URL + "IndexBanner.aspx";

    public BannerPresenter(Activity context, CarouselImageView civHomeGanner, RelativeLayout rl_home_banner) {
        this.context = context;
        this.civHomeGanner = civHomeGanner;
        this.rl_home_banner = rl_home_banner;
        netUtils = new NetUtil();
    }

    @Override
    public View initView() {
        return civHomeGanner;
    }

    @Override
    public void initData() {
        netUtils.okHttp2Server2(context,banner_url, null);
        netUtils.setOnServerResponseListener(new MyOnServerResponseListener());
    }


    public class MyOnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            try {
                parseData(response, civHomeGanner);
                onStopRefreshListener.stopSuccessRefresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "----" + e.getMessage());
            onStopRefreshListener.stopFailRefresh();
            EncryptNetUtil.startNeterrorPage(context);
        }
    }

    /**
     * 解析数据
     *
     * @param response
     * @param civHomeGanner
     */
    private void parseData(String response, CarouselImageView civHomeGanner) throws Exception{
        LogUtil.e("banner-------" + response);
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
                DecimalFormat df = new DecimalFormat("#0.00");
                int banner_activity_id = data.get(position % data.size()).getBanner_activity_id();
                String link_url = data.get(position % data.size()).getLink_url();
                String goods_id = data.get(position % data.size()).getProductId();
                String goods_img = Url.IMG_URL + data.get(position % data.size()).getImg_url();
                String goods_title = data.get(position % data.size()).getTitle();
                int site_id = data.get(position % data.size()).getSite_id();
                int article_id = data.get(position % data.size()).getArticle_id();
                double sell_price = data.get(position % data.size()).getSell_price();
                double market_price = data.get(position % data.size()).getMarket_price();
                Intent intent = null;

                if (site_id == 1) {
                    if (banner_activity_id > 0) {
                        intent = new Intent(context, WareListActivity.class);
                        intent.putExtra("activity_id", String.valueOf(banner_activity_id));
                        intent.putExtra("content", "activity");
                        context.startActivity(intent);
                    } else {
                        intent = new Intent(context, WebRecordActivity.class);
                        intent.putExtra("url", link_url);
                        intent.putExtra("goods_id", goods_id);
                        intent.putExtra("goods_img", goods_img);
                        intent.putExtra("goods_title", goods_title);
                        context.startActivity(intent);
                    }
                } else if (site_id == 2) {
                    //苏宁
                    intent = new Intent(context, SuningDetailActivity.class);
                    intent.putExtra("article_id", article_id);
                    intent.putExtra("goods_id", goods_id);
                    intent.putExtra("goods_img", goods_img);
                    intent.putExtra("goods_title", goods_title);
                    intent.putExtra("goods_price", df.format(sell_price) + "");
                    intent.putExtra("c_price", df.format(market_price) + "");
                    context.startActivity(intent);
                }
            }

            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                if (context != null && !context.isFinishing() && imageURL != null) {
                    Glide.with(context).load(imageURL)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                            .into(imageView);//请求成功后把图片设置到的控件
                }
            }
        };
        civHomeGanner.setImageResources(data, mAdCycleViewListener);
        civHomeGanner.startImageCycle();
        rl_home_banner.setVisibility(View.VISIBLE);
    }

    /**
     * 停止刷新的监听
     */
    private OnStopRefreshListener onStopRefreshListener;

    public interface OnStopRefreshListener {

        void stopSuccessRefresh();

        void stopFailRefresh();
    }

    public void setOnStopRefreshListener(OnStopRefreshListener onStopRefreshListener) {
        this.onStopRefreshListener = onStopRefreshListener;
    }
}
