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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.CityUrl;
import com.gather_excellent_help.bean.CityBean;
import com.gather_excellent_help.ui.adapter.TaobaoShaixuanCityAdapter;
import com.gather_excellent_help.ui.adapter.WareSelectorAdapter;
import com.gather_excellent_help.utils.ScreenUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class TaobaoShaixuanPopupwindow extends PopupWindow {



    private Context context;
    private LayoutInflater layoutInflater;
    private final TabLayout tabShuaixuanPopup;
    private final FrameLayout flShaixuanPopup;
    private List<String> titles;
    private RecyclerView rcvWarePopup;
    private View right_view;
    private EditText et_shaixuan_start_price;
    private EditText et_shaixuan_end_price;
    private CheckBox cb_shaixuan_choice;
    private TextView tv_shaicuan_left_confirm;
    private String city = "";
    private String start_price= "";
    private String end_price = "";
    private boolean checked;

    public TaobaoShaixuanPopupwindow(final Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        View inflate = layoutInflater.inflate(R.layout.item_taobao_shaixuan_popup, null);
        tabShuaixuanPopup = (TabLayout) inflate.findViewById(R.id.tab_shuaixuan_popup);
        flShaixuanPopup = (FrameLayout) inflate.findViewById(R.id.fl_shaixuan_popup);
        final View left_view = View.inflate(context, R.layout.item_shaixuan_left, null);
        et_shaixuan_start_price = (EditText) left_view.findViewById(R.id.et_shaixuan_start_price);
        et_shaixuan_end_price = (EditText) left_view.findViewById(R.id.et_shaixuan_end_price);
        cb_shaixuan_choice = (CheckBox) left_view.findViewById(R.id.cb_shaixuan_choice);
        tv_shaicuan_left_confirm = (TextView) left_view.findViewById(R.id.tv_shaicuan_left_confirm);
        tv_shaicuan_left_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_price = et_shaixuan_start_price.getText().toString().trim();
                end_price = et_shaixuan_end_price.getText().toString().trim();
                checked = cb_shaixuan_choice.isChecked();
                onPopupClickListener.onPopupClick(start_price,end_price,checked,city);
            }
        });
        flShaixuanPopup.removeAllViews();
        flShaixuanPopup.addView(left_view);
        CityBean cityBean = new Gson().fromJson(CityUrl.CITY_LIST, CityBean.class);
        List<CityBean.DataBean> data = cityBean.getData();
        right_view = View.inflate(context, R.layout.item_shaixuan_right, null);
        rcvWarePopup = (RecyclerView) right_view.findViewById(R.id.rcv_shaixuan_city);
        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(context);
        rcvWarePopup.setLayoutManager(fullyLinearLayoutManager);
        TaobaoShaixuanCityAdapter taobaoShaixuanCityAdapter = new TaobaoShaixuanCityAdapter(context, data);
        if(rcvWarePopup!=null) {
            rcvWarePopup.setAdapter(taobaoShaixuanCityAdapter);
            taobaoShaixuanCityAdapter.setOnItemClickListener(new TaobaoShaixuanCityAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String text) {
                    city = text;
                    onPopupClickListener.onListClick(city);
                }
            });

        }
        titles = new ArrayList<>();
        titles.add("筛选");
        titles.add("地区");
        tabShuaixuanPopup.setTabMode(TabLayout.MODE_FIXED);
        tabShuaixuanPopup.addTab(tabShuaixuanPopup.newTab().setText(titles.get(0)));
        tabShuaixuanPopup.addTab(tabShuaixuanPopup.newTab().setText(titles.get(1)));
        tabShuaixuanPopup.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0 :
                        flShaixuanPopup.removeAllViews();
                        flShaixuanPopup.addView(left_view);
                        break;
                    case 1:
                        flShaixuanPopup.removeAllViews();
                        flShaixuanPopup.addView(right_view);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(inflate);
        //设置SelectPicPopupWindow弹出窗体的宽
        int screenWidth = ScreenUtil.getScreenWidth(context);
        this.setWidth(screenWidth*3/4);
        int screenHeight = ScreenUtil.getScreenHeight(context);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(screenHeight*8/13);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#ffffff"));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private OnPopupClickListener onPopupClickListener;
    public interface OnPopupClickListener{
        void onPopupClick(String start,String end,boolean check,String city);
        void onListClick(String city);
    }

    public void setOnPopupClickListener(OnPopupClickListener onPopupClickListener) {
        this.onPopupClickListener = onPopupClickListener;
    }

    public void setNoChecked(){
        if(cb_shaixuan_choice!=null) {
            cb_shaixuan_choice.setChecked(false);
        }
    }
}
