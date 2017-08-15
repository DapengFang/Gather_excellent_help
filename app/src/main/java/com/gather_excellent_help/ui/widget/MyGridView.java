package com.gather_excellent_help.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.gather_excellent_help.utils.DensityUtil;

/**
 * Created by wuxin on 2017/7/12.
 */

public class MyGridView extends GridView {
    public MyGridView(Context context) {
        super(context);
    }
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        setMeasuredDimension(widthMeasureSpec,expandSpec);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
