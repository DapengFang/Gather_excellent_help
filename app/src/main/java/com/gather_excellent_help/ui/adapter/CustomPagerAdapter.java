package com.gather_excellent_help.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.gather_excellent_help.ui.base.BaseFragment;
import com.gather_excellent_help.ui.base.LazyLoadFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxin on 2017/7/6.
 */

public class CustomPagerAdapter extends FragmentPagerAdapter {

    private List<LazyLoadFragment> mFragments;

    public CustomPagerAdapter(FragmentManager fm,List<LazyLoadFragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return null == mFragments ? null : mFragments.get(position);
    }

    @Override
    public int getCount() {
        return null == mFragments ? 0 : mFragments.size();
    }

}
