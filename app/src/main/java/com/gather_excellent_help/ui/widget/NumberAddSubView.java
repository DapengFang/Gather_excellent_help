package com.gather_excellent_help.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;


/**
 * 作用：数字加减器
 */
public class NumberAddSubView extends LinearLayout implements View.OnClickListener {

    private Button btn_sub;
    private TextView tv_number;
    private Button btn_add;
    /**
     * 当前购买的个数
     */
    private int value = 1;

    /**
     * 最少库存
     */
    private int minValue = 1;

    /**
     * 库存里面的最大值
     */
    private int maxValue = 30;

    public int getValue() {
        String numebr = tv_number.getText().toString();
        if(!TextUtils.isEmpty(numebr)){
            value = Integer.parseInt(numebr);
        }
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        tv_number.setText(value+"");
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public NumberAddSubView(Context context) {
        this(context, null);
    }

    /**
     * android系统规定，在布局文件中使用该类的时候，用该构造方法实例化该类
     * @param context
     * @param attrs
     */
    public NumberAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        /**
         * 最后一个参数：把当前类当作是父View,本质是合为一体
         */
        View.inflate(context, R.layout.number_add_sub_view, this);

        btn_sub = (Button) findViewById(R.id.btn_sub);
        tv_number = (TextView) findViewById(R.id.tv_number);
        btn_add = (Button) findViewById(R.id.btn_add);

        //设置监听事件
        btn_sub.setOnClickListener(this);
        btn_add.setOnClickListener(this);

        tv_number.setText(getValue()+"");

        //获取属性
        TintTypedArray tt = TintTypedArray.obtainStyledAttributes(context,attrs,R.styleable.NumberAddSubView);
        int value = tt.getInt(R.styleable.NumberAddSubView_value, 0);
        if(value >0){
            setValue(value);
        }

        int minValue = tt.getInt(R.styleable.NumberAddSubView_minValue,0);
        if(minValue >0){
            setMinValue(minValue);
        }

        int maxValue = tt.getInt(R.styleable.NumberAddSubView_maxValue,0);
        if(maxValue >0){
            setMaxValue(maxValue);
        }


        Drawable btnSubBackground = tt.getDrawable(R.styleable.NumberAddSubView_btnSubBackground);
        if(btnSubBackground != null){
            btn_sub.setBackground(btnSubBackground);
        }

        Drawable btnAddBackground = tt.getDrawable(R.styleable.NumberAddSubView_btnAddBackground);
        if(btnAddBackground != null){
            btn_add.setBackground(btnAddBackground);
        }

        Drawable textviewBackground = tt.getDrawable(R.styleable.NumberAddSubView_textviewBackground);
        if(textviewBackground != null){
            tv_number.setBackground(textviewBackground);
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sub://减的按钮被点击了
                subNumber();
                if(listener != null){
                    listener.onSubButton(v,value);
                }
                break;

            case R.id.btn_add://加的按钮被点击了
                addNumber();
                if(listener != null){
                    listener.onAddButton(v, value);
                }
                break;
        }
    }

    /**
     * 响应加的按钮
     */
    private void addNumber() {
        if(value <maxValue){
            value = value+1;
        }
        tv_number.setText(value+"");

    }

    /**
     * 响应减的按钮
     */
    private void subNumber() {

        if(value > minValue){
            value = value-1;
        }
        tv_number.setText(value+"");
    }

    /**
     * 当按钮加或者减的被点击的时候，回调
     */
    public interface OnButtonClickListener{
        /**
         * 当减按钮被点击的时候回调
         * @param view
         * @param value
         */
        public void onSubButton(View view, int value);

        /**
         * 当加按钮被点击的时候回调
         * @param view
         * @param value
         */
        public void onAddButton(View view, int value);
    }

    private  OnButtonClickListener listener;

    /**
     * 设置监听减和加按钮的监听
     * @param listener
     */
    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }
}
