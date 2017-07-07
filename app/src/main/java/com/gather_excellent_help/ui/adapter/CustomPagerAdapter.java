package com.gather_excellent_help.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxin on 2017/7/6.
 */

public class CustomPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments == null ? null : mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    /**
     * 添加一个页面
     * @param fragment
     */
    public void addPager(Fragment fragment) {
        if (mFragments == null) {
            ArrayList<Fragment> fragments = new ArrayList<>();
            fragments.add(fragment);
            setPagers(fragments);
        } else {
            mFragments.add(fragment);
        }
    }

    /**
     * 设置当前adapter的页面集合
     * @param fragments
     */
    public void setPagers(List<Fragment> fragments) {
        mFragments = fragments;
    }
}
