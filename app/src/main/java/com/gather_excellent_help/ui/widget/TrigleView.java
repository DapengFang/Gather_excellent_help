package com.gather_excellent_help.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.LogUtil;

/**
 * Created by Dapeng Fang on 2017/8/18.
 */

public class TrigleView extends View {

    private Paint mPaint;//三角形的画笔

    private Path mPath;//代表三角形

    private int mTrigleWidth;//三角形的宽度

    private int mTrigleHeight;//三角形的高度
    private int count = 5;
    private static final float RADIO_TRIANGLE_WIDTH = 3/4f;

    public TrigleView(Context context) {
        this(context,null);
    }

    public TrigleView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public TrigleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        //mPaint.setAntiAlias(true);
        //mPaint.setStrokeWidth(DensityUtil.dip2px(getContext(),1));
        //mPaint.setPathEffect(new CornerPathEffect(3));
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
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
        p.setColor(Color.WHITE);
        //实例化路径
        Path path = new Path();
        path.moveTo(0,width/2);
        path.lineTo(width,width/2);
        path.lineTo(width/2,0);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, p);
    }
}
