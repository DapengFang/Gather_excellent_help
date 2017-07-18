package com.gather_excellent_help.ui.widget;
/**
 * Created by ${} on 2017/7/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.ListBean;
import com.gather_excellent_help.ui.adapter.WareSelectorAdapter;
import com.gather_excellent_help.utils.ScreenUtil;

import java.util.List;

import butterknife.Bind;


public class TypeSelectorPopupwindow extends PopupWindow {


    private RecyclerView rcvWarePopup;
    private Context context;
    private List<ListBean.DataBean> data;
    private LayoutInflater layoutInflater;

    public TypeSelectorPopupwindow(Context context, List<ListBean.DataBean> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
        View inflate = layoutInflater.inflate(R.layout.ware_popwindow_list, null);
        rcvWarePopup = (RecyclerView) inflate.findViewById(R.id.rcv_ware_popup);
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(context);
        rcvWarePopup.setLayoutManager(layoutManager);
        WareSelectorAdapter wareSelectorAdapter = new WareSelectorAdapter(context, data);
        rcvWarePopup.setAdapter(wareSelectorAdapter);
        wareSelectorAdapter.setOnItemClickListener(new WareSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                onItemClickListener.onItemClick(position);
            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(inflate);
        //设置SelectPicPopupWindow弹出窗体的宽
        int screenWidth = ScreenUtil.getScreenWidth(context);
        this.setWidth(screenWidth/4);
        int screenHeight = ScreenUtil.getScreenHeight(context);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(screenHeight/5);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.GRAY);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private WareSelectorAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(WareSelectorAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
