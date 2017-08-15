package com.gather_excellent_help.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Dapeng Fang on 2017/8/14.
 */

public class MyImageView extends ImageView {
    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 父容器传过来的高度的值
        int height = getMeasuredHeight();
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
               MeasureSpec.UNSPECIFIED);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
