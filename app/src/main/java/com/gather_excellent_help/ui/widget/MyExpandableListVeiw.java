package com.gather_excellent_help.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.gather_excellent_help.utils.LogUtil;

/**
 * Created by Dapeng Fang on 2017/7/24.
 */

public class MyExpandableListVeiw extends ExpandableListView implements AbsListView.OnScrollListener{


    private int lastItem;
    private int totalItem;
    private boolean isLoading = false;

    public MyExpandableListVeiw(Context context) {
        super(context);
    }

    public MyExpandableListVeiw(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyExpandableListVeiw(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        //判断ListView是否滑动到第一个Item的顶部
        if (isValid!=null&&this.getChildCount() > 0 && this.getFirstVisiblePosition() == 0
                && this.getChildAt(0).getTop() >= this.getPaddingTop()) {
            //解决滑动冲突，当滑动到第一个item，下拉刷新才起作用
            LogUtil.e("可以刷新了");
            isValid.setSwipeEnabledTrue();
        } else {
            isValid.setSwipeEnabledFalse();
            LogUtil.e("不可以刷新");
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastItem = firstVisibleItem + visibleItemCount;
        this.totalItem = totalItemCount;
    }

    private OnSwipeIsValid isValid;

    public interface OnSwipeIsValid{
        public void setSwipeEnabledTrue();
        public void setSwipeEnabledFalse();
    }

    public void setIsValid(OnSwipeIsValid isValid) {
        this.isValid = isValid;
    }
}
