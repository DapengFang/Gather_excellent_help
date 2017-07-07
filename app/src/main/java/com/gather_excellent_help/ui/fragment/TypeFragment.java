package com.gather_excellent_help.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.gather_excellent_help.ui.base.BaseFragment;
import com.gather_excellent_help.utils.DensityUtil;

/**
 * Created by wuxin on 2017/7/7.
 */

public class TypeFragment extends BaseFragment {
    @Override
    public View initView() {
        Context context = getContext();
        TextView textView = new TextView(context);
        textView.setText("分类");
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(DensityUtil.dip2px(context,14));
        return textView;
    }

    @Override
    public void initData() {

    }
}
