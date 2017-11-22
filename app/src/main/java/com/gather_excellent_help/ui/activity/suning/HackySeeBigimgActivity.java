package com.gather_excellent_help.ui.activity.suning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.suning.SuningWareBean;
import com.gather_excellent_help.ui.fragment.dragfragment.ImageDetailFragment;
import com.gather_excellent_help.ui.widget.HackyViewPager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HackySeeBigimgActivity extends FragmentActivity {

    private HackyViewPager hvp_see_pager;
    private TextView indicator;
    private int currPosition = -1;
    private List<SuningWareBean.DataBean.UrlsBean> urls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hacky_see_bigimg);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        hvp_see_pager = (HackyViewPager)findViewById(R.id.hvp_see_pager);
        indicator = (TextView)findViewById(R.id.indicator);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        Intent intent = getIntent();
        currPosition = intent.getIntExtra("curr_position",-1);
        String imgs = intent.getStringExtra("imgs");
        SuningWareBean suningWareBean = new Gson().fromJson(imgs, SuningWareBean.class);
        if(suningWareBean!=null) {
            List<SuningWareBean.DataBean> data = suningWareBean.getData();
            if(data!=null && data.size()>0) {
                SuningWareBean.DataBean dataBean = data.get(0);
                urls = dataBean.getUrls();
                ImagePagerAdapter mAdapter = new ImagePagerAdapter(
                        getSupportFragmentManager(), urls);
                hvp_see_pager.setAdapter(mAdapter);
                hvp_see_pager.setCurrentItem(currPosition);
                indicator.setText(currPosition+1+"/"+ urls.size());
                // 更新下标
                hvp_see_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                    }

                    @Override
                    public void onPageSelected(int arg0) {
                        indicator.setText(arg0+1+"/"+ urls.size());
                    }

                });
            }
        }

    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public List<SuningWareBean.DataBean.UrlsBean> imgs;

        public ImagePagerAdapter(FragmentManager fm, List<SuningWareBean.DataBean.UrlsBean> imgs) {
            super(fm);
            this.imgs = imgs;
        }

        @Override
        public int getCount() {
            return imgs == null ? 0 : imgs.size();
        }

        @Override
        public Fragment getItem(int position) {
            SuningWareBean.DataBean.UrlsBean urlsBean = imgs.get(position);
            String original_path = urlsBean.getOriginal_path();
            return ImageDetailFragment.newInstance(original_path);
        }

    }
}
