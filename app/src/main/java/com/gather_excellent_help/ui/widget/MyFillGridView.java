package com.gather_excellent_help.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.LogUtil;

/**
 * Created by Dapeng Fang on 2017/8/14.
 */

public class MyFillGridView extends GridView {
    private int mNumColumns = 3;//每一行的item的个数
    private int mNumRows;//多少行
    private int mVerticalSpacing;//每行的item之前的间距
    private int childHeight;
    private int childWidth;

    public MyFillGridView(Context context) {
        super(context);
    }

    public MyFillGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(getSuggestedMinimumHeight(), MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            LogUtil.e("h============" + h);
            if (h > height) //采用最大的view的高度。
                height = h;
        }
        height = DensityUtil.dip2px(getContext(),200);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//        int validWidth = widthSize - getPaddingLeft() - getPaddingRight();
//        int validHeight = heightSize - getPaddingTop() - getPaddingBottom();
//
//        final int count = getChildCount();
//        if (mNumColumns > count) {
//            mNumColumns = 1;
//        }
//        // 根据第一个孩子的大小决定其他孩子的大小
//        if (count > 0) {
//            //计算一共有多少行
//            mNumRows = count / mNumColumns;
//            if (count % mNumColumns != 0) {
//                mNumRows++;
//            }
//
//            final View child = getChildAt(0);
//            // 一行items之间的间隔数量
//            int horizontalSpacingNumber = mNumColumns - 1;
////            int totalHorizontalSpacing = horizontalSpacingNumber
////                    * mHorizontalSpacing;
////            //获取每一列的宽度
////            mColumnWidth = (validWidth - totalHorizontalSpacing) / mNumColumns;
//            //计算child的heightSpec
//            int verticalSpacingNumber = mNumRows - 1;
//            int totalVerticalSpacing = verticalSpacingNumber * mVerticalSpacing;
//            int itemHeighgSize = (validHeight - totalVerticalSpacing)
//                    / mNumRows;
//
//            //计算child的widthSpec
//            int childWidthSpec = MeasureSpec.makeMeasureSpec(widthMeasureSpec,
//                    MeasureSpec.EXACTLY);
//            int childHeightSpec = MeasureSpec.makeMeasureSpec(itemHeighgSize,
//                    MeasureSpec.EXACTLY);
//            //对child进行实际的测量
//            child.measure(childWidthSpec, childHeightSpec);
//
//            childHeight = child.getMeasuredHeight();
//            childWidth = child.getMeasuredWidth();
//
//        }
//        //测量gridView的大小
//        setMeasuredDimension(widthSize, childHeight);
    }
}
