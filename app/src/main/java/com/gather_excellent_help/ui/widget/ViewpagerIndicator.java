package com.gather_excellent_help.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.gather_excellent_help.utils.DensityUtil;

/**
 * Created by wuxin on 2017/7/6.
 */

public class ViewpagerIndicator  extends LinearLayout{

    private Paint mPaint;//三角形的画笔

    private Path mPath;//代表三角形

    private int mTrigleWidth;//三角形的宽度

    private int mTrigleHeight;//三角形的高度

    private int mMoveTriglePosition;//三角形的初始位置

    private int mTranslationX;//三角形改变的位置

    private static final float RADIO_TRIANGLE_WIDTH = 3/4f;
    private int mMoveTrantCount;//每次点击移动的距离

    private int count = 5;


    public ViewpagerIndicator(Context context) {
        this(context,null);
    }

    public ViewpagerIndicator(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ViewpagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(DensityUtil.dip2px(getContext(),1));
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTrigleWidth = (int) (w/count * RADIO_TRIANGLE_WIDTH);

        mMoveTriglePosition = w/(2*count) - mTrigleWidth/2;

        mMoveTrantCount = w/count;

        initTrigle();
    }

    /**
     * 画小三角
     */
    private void initTrigle() {
        mTrigleHeight = mTrigleWidth/2;
        mPath = new Path();
        mPath.moveTo(0,0);
        mPath.lineTo(mTrigleWidth,0);
        mPath.lineTo(mTrigleWidth/2,-mTrigleHeight);
        mPath.close();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mMoveTriglePosition +mTranslationX,getHeight() -4);
        //canvas.drawPath(mPath,mPaint);
        canvas.drawLine(0,0,mTrigleWidth,0,mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    public void checkMove(int position){
        mTranslationX = mMoveTrantCount*position;
        invalidate();
    }

    /**
     * 指示器根据手指滚动而滚动
     * @param position    tabWidth * offset 正在滑动的偏移量
     * @param offset 0.00000000~1.00000000   position * tabWidth 已经滑动过的偏移量
     */
    public void scroll(int position, float offset) {
        int tabWidth = getWidth() / 6;
        mTranslationX = (int)(tabWidth * offset + position * tabWidth);//三角形的偏移量
        invalidate();
    }

    public void setCount(int count) {
        this.count = count;
    }
}
