package com.gather_excellent_help;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gather_excellent_help.ui.base.BaseFullScreenActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideActivity extends BaseFullScreenActivity {

    @Bind(R.id.vp_welcome_page_top)
    ViewPager vpWelcomePageTop;
    @Bind(R.id.ll_welcome_point_layout)
    LinearLayout llWelcomePointLayout;
    @Bind(R.id.tv_guidance_page)
    TextView tvGuidancePage;
    @Bind(R.id.ll_splash_ship)
    LinearLayout ll_splash_ship;

    /**
     * 上一个页面的位置
     */
    protected int lastPosition;


    //图片地址
    private List<Integer> arrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        arrs = new ArrayList<Integer>();
        arrs.add(R.drawable.welcome1);
        arrs.add(R.drawable.welcome2);
        arrs.add(R.drawable.welcome3);
        arrs.add(R.drawable.welcome4);

        initViewpage(arrs);
        tvGuidancePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
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
            imageView.setBackgroundResource(images.get(position));

            view.addView(viewLayout, 0); // 将图片增加到ViewPager
            return viewLayout;
        }

        @Override
        public void finishUpdate(View container) {
        }

    }

    public void showImageButton() {
        tvGuidancePage.setVisibility(View.VISIBLE);
    }


    public void goneImageButton() {
        tvGuidancePage.setVisibility(View.GONE);
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
}
