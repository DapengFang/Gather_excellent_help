package com.gather_excellent_help.ui.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.gather_excellent_help.R;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.Tools;

/*
 * 自定义view的几个步骤：
 * 1、首先需要写一个类来继承自View
 * 2、需要得到view的对象，那么需要重写构造方法，其中一参的构造方法用于new，二参的构造方法用于xml布局文件使用，三参的构造方法可以传入一个样式
 * 3、需要设置view的大小，那么需要重写onMeasure方法
 * 4、需要设置view的位置，那么需要重写onLayout方法，但是这个方法在自定义view的时候用的不多，原因主要在于view的位置主要是由父控件来决定
 * 5、需要绘制出所需要显示的view，那么需要重写onDraw方法
 * 6、当控件状态改变的时候，需要重绘view，那么调用invalidate();方法，这个方法实际上会重新调用onDraw方法
 * 7、在这其中，如果需要对view设置点击事件，可以直接调用setOnClickListener方法
 */

public class MyToggleButton extends View {

    /**
     * 开关按钮的背景
     */
    private Bitmap backgroundBitmap;
    /**
     * 开关按钮的滑动部分
     */
    private Bitmap slideButton;
    /**
     * 滑动按钮的左边界
     */
    private float slideBtn_left;
    /**
     * 当前开关的状态
     */
    private boolean currentState = false;

    /**
     * 在代码里面创建对象的时候，使用此构造方法
     *
     * @param context
     */
    public MyToggleButton(Context context) {
        super(context);
    }

    /**
     * 在布局文件中声明的view，创建时由系统自动调用
     *
     * @param context
     * @param attrs
     */
    public MyToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    /**
     * 测量尺寸时的回调方法
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 设置当前view的大小 width:view的宽，单位都是像素值 heigth:view的高，单位都是像素值
        setMeasuredDimension(backgroundBitmap.getWidth(),
                backgroundBitmap.getHeight());
    }

    // 这个方法对于自定义view的时候帮助不大，因为view的位置一般由父组件来决定的
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 画view的方法,绘制当前view的内容
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);

        Paint paint = new Paint();
        // 打开抗锯齿
        paint.setAntiAlias(true);

        // 画背景
        canvas.drawBitmap(backgroundBitmap, 0, 0, paint);
        // 画滑块
        canvas.drawBitmap(slideButton, slideBtn_left, 0, paint);
    }

    /**
     * 初始化view
     */
    private void initView() {

        backgroundBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.switch_background);
        backgroundBitmap = Tools.zoomBitmap(backgroundBitmap, DensityUtil.dip2px(getContext(),45), DensityUtil.dip2px(getContext(),23));
        slideButton = BitmapFactory.decodeResource(getResources(),
                R.drawable.slide_button);
        slideButton = Tools.zoomBitmap(slideButton,DensityUtil.dip2px(getContext(),23),DensityUtil.dip2px(getContext(),23));
        /*
         * 点击事件
         */
        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                currentState = !currentState;
                flushState();
                flushView();
                onStateChangeListener.isCurrentState(currentState);
            }
        });
    }

    /**
     * 刷新视图
     */
    protected void flushView() {
        // 刷新当前view会导致ondraw方法的执行
        invalidate();
    }

    /**
     * 刷新当前的状态
     */
    protected void flushState() {
        if (currentState) {
            slideBtn_left = backgroundBitmap.getWidth()
                    - slideButton.getWidth();
            backgroundBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.switch_background_gray);
            backgroundBitmap = Tools.zoomBitmap(backgroundBitmap, DensityUtil.dip2px(getContext(),45), DensityUtil.dip2px(getContext(),23));
            slideButton = BitmapFactory.decodeResource(getResources(),
                    R.drawable.slide_button);
            slideButton = Tools.zoomBitmap(slideButton,DensityUtil.dip2px(getContext(),23),DensityUtil.dip2px(getContext(),23));
        } else {
            slideBtn_left = 0;
            backgroundBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.switch_background);
            backgroundBitmap = Tools.zoomBitmap(backgroundBitmap, DensityUtil.dip2px(getContext(),45), DensityUtil.dip2px(getContext(),23));
            slideButton = BitmapFactory.decodeResource(getResources(),
                    R.drawable.slide_button);
            slideButton = Tools.zoomBitmap(slideButton,DensityUtil.dip2px(getContext(),23),DensityUtil.dip2px(getContext(),23));
        }
    }

    public boolean isCurrentState() {
        return currentState;
    }

    public void setCurrentState(boolean currentState) {
        this.currentState = currentState;
        flushState();
        flushView();
    }

    private OnStateChangeListener onStateChangeListener;

    public interface OnStateChangeListener {
        void isCurrentState(boolean currentState);
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }




}