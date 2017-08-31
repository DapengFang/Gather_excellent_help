package com.gather_excellent_help.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.ScreenUtil;


/**
 * 作者：Dapeng Fang on 2016/12/29 10:22
 * <p/>
 * 邮箱：fdp111888@163.com
 */

public class SharePopupwindow extends PopupWindow {

    private Context context;
    private LayoutInflater inflater;


    public SharePopupwindow(Context context){
        super(context);
        setFocusable(true);
        this.context = context;
        inflater =LayoutInflater.from(context);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        View view= inflater.inflate(R.layout.item_share_friend,null);
        LinearLayout llShareQQ = (LinearLayout) view.findViewById(R.id.ll_share_qq);
        LinearLayout llShareWeixin = (LinearLayout) view.findViewById(R.id.ll_share_weixin);
        LinearLayout llShareSina = (LinearLayout) view.findViewById(R.id.ll_share_sina);
        TextView tv_share_cancel = (TextView) view.findViewById(R.id.tv_share_cancel);
        llShareQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListenr.onQQClick();
            }
        });
        llShareWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListenr.onWeixinClick();
            }
        });
        llShareSina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListenr.onSinaClick();
            }
        });
        tv_share_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShowing()) {
                    dismiss();
                }
            }
        });
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ScreenUtil.getScreenHeight(context) - DensityUtil.dip2px(context,20));
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#55000000"));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private OnItemClickListenr onItemClickListenr;

    public interface OnItemClickListenr {
        void onQQClick();
        void onWeixinClick();
        void onSinaClick();
    }

    public void setOnItemClickListenr(OnItemClickListenr onItemClickListenr) {
        this.onItemClickListenr = onItemClickListenr;
    }
}
