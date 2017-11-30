package com.gather_excellent_help.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.PopupWindow;

import com.gather_excellent_help.utils.ScreenUtil;

/**
 * 作者：Dapeng Fang on 2016/9/26 10:18
 * <p/>
 * 邮箱：fdp111888@163.com
 */

public class TuihuoMenuPopWindow extends PopupWindow {
    private Context context;
    private View view;


    public TuihuoMenuPopWindow(Context context, View view) {
        this.context = context;
        this.view = view;
        init();
    }

    private void init() {
        //实例化级联菜单
        setFocusable(true);
        setContentView(view);
        int screenHeight = ScreenUtil.getScreenHeight(context);
        int screenWidth = ScreenUtil.getScreenWidth(context);
        setWidth(screenWidth);
        setHeight(screenHeight*2/3);
        ColorDrawable dw = new ColorDrawable(Color.WHITE);
        //设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw);
        //设置回调接口
    }

}
