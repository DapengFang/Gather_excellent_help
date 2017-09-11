package com.gather_excellent_help.presenter.homepresenter;

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
import com.gather_excellent_help.ui.activity.WebRecordActivity;
import com.gather_excellent_help.ui.widget.CarouselImageView;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.imageutils.ImageLoader;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2017/8/21.
 */

public class BannerPresenter  extends BasePresenter{

    private Context context;
    private CarouselImageView civHomeGanner;
    private NetUtil netUtils;
    private String banner_url = Url.BASE_URL + "IndexBanner.aspx";

    public BannerPresenter(Context context, CarouselImageView civHomeGanner) {
        this.context = context;
        this.civHomeGanner = civHomeGanner;
        netUtils = new NetUtil();
    }

    @Override
    public View initView() {

        return civHomeGanner;
    }

    @Override
    public void initData() {
        netUtils.okHttp2Server2(banner_url, null);
        netUtils.setOnServerResponseListener(new MyOnServerResponseListener());
    }


    public class MyOnServerResponseListener implements NetUtil.OnServerResponseListener{

        @Override
        public void getSuccessResponse(String response) {
            parseData(response, civHomeGanner);
            onStopRefreshListener.stopSuccessRefresh();
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
           LogUtil.e(call.toString()+"----"+e.getMessage());
            onStopRefreshListener.stopFailRefresh();
        }
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
                Glide.with(context).load(imageURL)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                        .placeholder(R.drawable.white_banner)//加载过程中的图片
                        .error(R.drawable.white_banner)//加载失败的时候显示的图片
                        .into(imageView);//请求成功后把图片设置到的控件
            }
        };
        civHomeGanner.setImageResources(data, mAdCycleViewListener);
        civHomeGanner.startImageCycle();
    }

    private OnStopRefreshListener onStopRefreshListener;

    public interface  OnStopRefreshListener{
        void stopSuccessRefresh();
        void stopFailRefresh();
    }

    public void setOnStopRefreshListener(OnStopRefreshListener onStopRefreshListener) {
        this.onStopRefreshListener = onStopRefreshListener;
    }
}
