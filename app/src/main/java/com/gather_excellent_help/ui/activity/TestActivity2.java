package com.gather_excellent_help.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.HomeBannerBean;
import com.gather_excellent_help.bean.HomeTypeBean;
import com.gather_excellent_help.ui.adapter.HomeTypeAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.CarouselImageView;
import com.gather_excellent_help.ui.widget.MyGridView;
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

public class TestActivity2 extends BaseActivity {

    @Bind(R.id.gv_home_type)
    MyGridView gvHomeType;
    @Bind(R.id.civ_home_ganner)
    CarouselImageView civHomeGanner;
    private ImageLoader mImageLoader;

    private String[] titles = {"电视", "空调", "小家电", "烟灶消", "洗衣机", "冰箱", "冷柜", "热水器"};
    private int[] imgs = {R.drawable.dianshi, R.drawable.kongtiao, R.drawable.xiaojiadian,
            R.drawable.yanzaoxiao, R.drawable.xiyiji,
            R.drawable.bingxiang, R.drawable.lenggui, R.drawable.reshuiqi};

    private NetUtil netUtils;
    private Map<String, String> map;
    private String url = Url.BASE_URL + "IndexBanner.aspx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        loadTypeData();
        getBannerData();
    }

    /**
     * 首页banner图的显示
     * @param data
     */
    private void showBanner(final List<HomeBannerBean.DataBean> data) {
        RelativeLayout.LayoutParams cParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        civHomeGanner.setLayoutParams(cParams);
        CarouselImageView.ImageCycleViewListener mAdCycleViewListener = new CarouselImageView.ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {

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
     * 联网获取banner图的数据
     */
    private void getBannerData() {
        netUtils = new NetUtil();
       netUtils.okHttp2Server2(url,null);
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });
    }

    /**
     * 解析数据
     * @param response
     */
    private void parseData(String response) {
        Gson gson = new Gson();
        HomeBannerBean homeBannerBean = gson.fromJson(response, HomeBannerBean.class);
        int statusCode = homeBannerBean.getStatusCode();
        List<HomeBannerBean.DataBean> data = homeBannerBean.getData();
        switch (statusCode) {
            case 1 :
               showBanner(data);
                break;
            case 0:
                LogUtil.e(homeBannerBean.getStatusMessage());
                break;
        }
    }


    private void loadTypeData() {
        ArrayList<HomeTypeBean> lists = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            HomeTypeBean homeTypeBean = new HomeTypeBean(imgs[i], titles[i]);
            lists.add(homeTypeBean);
        }
        HomeTypeAdapter homeTypeAdapter = new HomeTypeAdapter(this, lists);
        gvHomeType.setAdapter(homeTypeAdapter);
    }
}
