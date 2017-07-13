package com.gather_excellent_help.ui.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * 作者：Dapeng Fang on 2016/9/7 16:20
 * <p>
 * 邮箱：fdp111888@163.com
 */

public class MyNestedScrollView extends NestedScrollView {

    private int downX;
    private int downY;
    private int mTouchSlop;
    private OnScrollToBottomListener onScrollToBottom;

    public void setScrollViewListener(OnScrollToBottomListener onScrollToBottom) {
        this.onScrollToBottom = onScrollToBottom;
    }

    public MyNestedScrollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if(scrollY>0 && null != onScrollToBottom){
            onScrollToBottom.onScrollBottomListener(clampedY);
        }
    }
    public interface OnScrollToBottomListener{
       void onScrollBottomListener(boolean isBottom);
    }

}
