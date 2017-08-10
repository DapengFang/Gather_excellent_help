package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dapeng Fang on 2017/8/10.
 */

public class MyNewsAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<View> lists;

    public MyNewsAdapter(Context context, ArrayList<View> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)   {
        container.removeView(lists.get(position));//删除页卡
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
        container.addView(lists.get(position), 0);//添加页卡
        return lists.get(position);
    }

    @Override
    public int getCount() {
        return  lists.size();//返回页卡的数量
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;//官方提示这样写
    }
}
