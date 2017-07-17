package com.gather_excellent_help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.RadioGroup;

import android.support.v4.app.FragmentManager;

import com.gather_excellent_help.ui.adapter.CustomPagerAdapter;
import com.gather_excellent_help.ui.base.BaseFragmentActivity;
import com.gather_excellent_help.ui.fragment.GoodscartFragment;
import com.gather_excellent_help.ui.fragment.HomeFragment;
import com.gather_excellent_help.ui.fragment.MineFragment;
import com.gather_excellent_help.ui.fragment.TaobaoFragment;
import com.gather_excellent_help.ui.fragment.TypeFragment;
import com.gather_excellent_help.ui.widget.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {

    @Bind(R.id.vp_main)
    NoScrollViewPager vp_main;
    @Bind(R.id.rg_main)
    RadioGroup rg_main;
    private int currItem;//当前显示的界面
    private ArrayList<Fragment> fragments;//存放fragment页面的工作

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 加载数据
     */
    private void initData() {
        loadViewPager();
        rg_main.check(R.id.rb_home);
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
    }

    /**
     * 加载ViewPager页面
     */
    private void loadViewPager() {
        FragmentManager fm = getSupportFragmentManager();
        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(fm);
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new TypeFragment());
        fragments.add(new TaobaoFragment());
        fragments.add(new GoodscartFragment());
        fragments.add(new MineFragment());
        customPagerAdapter.setPagers(fragments);
        vp_main.setAdapter(customPagerAdapter);
    }

    /**
     *
     */
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            //根据选中的RadiaoButton切换到ViewPager不同页面
            //        vp_content.setCurrentItem(0);
            switch (checkedId) {
                case R.id.rb_home://首页
                    currItem = 0;
                    break;
                case R.id.rb_shaidan://分类
                    currItem = 1;
                    break;
                case R.id.rb_type://澳+专场
                    currItem = 2;
                    break;
                case R.id.rb_shopping://购物车
                    currItem = 3;
                    break;
                case R.id.rb_me://我的
                    currItem = 4;
                    break;
            }
            vp_main.setCurrentItem(currItem, false);
        }
    }

}
