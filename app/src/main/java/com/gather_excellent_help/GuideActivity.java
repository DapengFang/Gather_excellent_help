package com.gather_excellent_help;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.ui.activity.pswset.UserPswsetActivity;
import com.gather_excellent_help.ui.base.BaseFullScreenActivity;
import com.gather_excellent_help.utils.BitmapUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class GuideActivity extends BaseFullScreenActivity {

    @Bind(R.id.vp_welcome_page_top)
    ViewPager vpWelcomePageTop;
    @Bind(R.id.ll_welcome_point_layout)
    LinearLayout llWelcomePointLayout;
    @Bind(R.id.ll_splash_ship)
    LinearLayout ll_splash_ship;

    private ImageView iv_guidance_page;

    private int isUUid = -1;

    /**
     * 上一个页面的位置
     */
    protected int lastPosition;


    //图片地址
    private List<Integer> arrs;
    private Bitmap bitmap;

    private String advice_url = Url.BASE_URL + "GetDevice.aspx";
    private NetUtil netUtil;
    private Map<String, String> map;
    private String deviceId;//设备号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        iv_guidance_page = (ImageView) findViewById(R.id.iv_guidance_page);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        netUtil = new NetUtil();
        deviceId = Tools.getDeviceId(this);
        map = new HashMap<>();
        map.put("device_number", deviceId);
        netUtil.okHttp2Server2(advice_url, map);
        netUtil.setOnServerResponseListener(new OnServerResponseListener());
        arrs = new ArrayList<>();
        arrs.add(R.drawable.welcome1);
        arrs.add(R.drawable.welcome2);
        arrs.add(R.drawable.welcome3);
        arrs.add(R.drawable.welcome4);
        initViewpage(arrs);
        iv_guidance_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUUid == 0) {
                    Intent intent = new Intent(GuideActivity.this, UserPswsetActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    /**
     * 联网请求的回调
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
            int statusCode = codeStatueBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    isUUid = 1;
                    break;
                case 0:
                    isUUid = 0;
                    break;
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "--" + e.getMessage());
        }
    }

    private void initViewpage(final List<Integer> arrs) {
        initPoint();
        int pagerPosition = 0;
        vpWelcomePageTop.setAdapter(new ImagePagerAdapter(arrs));
        vpWelcomePageTop.setCurrentItem(pagerPosition);
        vpWelcomePageTop.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            /*
             * 页面切换后调用
             * position  新的页面位置
             */
            public void onPageSelected(int position) {

                //改变指示点的状态
                //把当前点enbale 为true
                llWelcomePointLayout.getChildAt(position).setEnabled(true);
                //把上一个点设为false
                llWelcomePointLayout.getChildAt(lastPosition).setEnabled(false);
                lastPosition = position;

                if (3 == position) {
                    showImageButton();
                } else {
                    goneImageButton();
                }
            }

            @Override
            /**
             * 页面正在滑动的时候，回调
             */
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            /**
             * 当页面状态发生变化的时候，回调
             */
            public void onPageScrollStateChanged(int state) {

            }
        });

        ll_splash_ship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUUid == 0) {
                    Intent intent = new Intent(GuideActivity.this, UserPswsetActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });

    }

    private class ImagePagerAdapter extends PagerAdapter {
        private List<Integer> images;
        private LayoutInflater inflater;

        ImagePagerAdapter(List<Integer> arrs) {
            this.images = arrs;
            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View viewLayout = inflater.inflate(R.layout.home_page_top_item, view,
                    false);
            ImageView imageView = (ImageView) viewLayout
                    .findViewById(R.id.image);
            bitmap = BitmapUtil.readBitMap(GuideActivity.this, images.get(position));
            //imageView.setBackgroundResource(images.get(position));
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            view.addView(viewLayout, 0); // 将图片增加到ViewPager
            return viewLayout;
        }

        @Override
        public void finishUpdate(View container) {
        }

    }

    public void showImageButton() {
        iv_guidance_page.setVisibility(View.VISIBLE);
    }


    public void goneImageButton() {
        iv_guidance_page.setVisibility(View.GONE);
    }

    private void initPoint() {
        for (int i = 0; i < arrs.size(); i++) {

            //添加指示点
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.rightMargin = 20;
            point.setLayoutParams(params);

            point.setBackgroundResource(R.drawable.guide_vp_point_selector);
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }
            llWelcomePointLayout.addView(point);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }
}
