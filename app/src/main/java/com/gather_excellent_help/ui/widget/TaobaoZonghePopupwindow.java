package com.gather_excellent_help.ui.widget;
/**
 * Created by ${} on 2017/7/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.CityUrl;
import com.gather_excellent_help.bean.CityBean;
import com.gather_excellent_help.ui.adapter.TaobaoShaixuanCityAdapter;
import com.gather_excellent_help.ui.adapter.TaobaoZongheAdapter;
import com.gather_excellent_help.ui.adapter.WareSelectorAdapter;
import com.gather_excellent_help.utils.ScreenUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class TaobaoZonghePopupwindow extends PopupWindow {



    private Context context;
    private LayoutInflater layoutInflater;
    private RecyclerView rcvWarePopup;


    public TaobaoZonghePopupwindow(final Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        View inflate = layoutInflater.inflate(R.layout.item_shaixuan_right, null);
        rcvWarePopup = (RecyclerView) inflate.findViewById(R.id.rcv_shaixuan_city);
        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(context);
        rcvWarePopup.setLayoutManager(fullyLinearLayoutManager);
        ArrayList<String> data = new ArrayList<>();
        data.add("赚的多");
        data.add("累计推广量");
        data.add("总支出佣金");
        TaobaoZongheAdapter taobaoShaixuanCityAdapter = new TaobaoZongheAdapter(context, data);
        rcvWarePopup.setAdapter(taobaoShaixuanCityAdapter);
        taobaoShaixuanCityAdapter.setOnItemClickListener(new TaobaoZongheAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                onTypeSelectedListener.onSelectedPos(position);
            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(inflate);
        //设置SelectPicPopupWindow弹出窗体的宽
        int screenWidth = ScreenUtil.getScreenWidth(context);
        this.setWidth(screenWidth);
        int screenHeight = ScreenUtil.getScreenHeight(context);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(screenHeight/4);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#eeffffff"));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }
    private OnTypeSelectedListener onTypeSelectedListener;

   public  interface OnTypeSelectedListener{
       void onSelectedPos(int pos);
   }

    public void setOnTypeSelectedListener(OnTypeSelectedListener onTypeSelectedListener) {
        this.onTypeSelectedListener = onTypeSelectedListener;
    }
}
