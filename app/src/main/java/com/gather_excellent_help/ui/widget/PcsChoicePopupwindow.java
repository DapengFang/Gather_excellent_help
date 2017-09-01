package com.gather_excellent_help.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.TestActivity2;
import com.gather_excellent_help.bean.address.Area;
import com.gather_excellent_help.db.DBhelper;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.ScreenUtil;

import java.util.ArrayList;


/**
 * 作者：Dapeng Fang on 2016/12/29 10:22
 * <p/>
 * 邮箱：fdp111888@163.com
 */

public class PcsChoicePopupwindow extends PopupWindow {

    private Context context;
    private LayoutInflater inflater;
    private View vShadow;
    private DBhelper db;
    private ArrayList<Area> city;
    private ArrayList<Area> district;


    private String p = "";
    private String c = "";


    public PcsChoicePopupwindow(Context context, View vShadow){
        super(context);
        setFocusable(true);
        this.context = context;
        this.vShadow = vShadow;
        inflater =LayoutInflater.from(context);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        View view= inflater.inflate(R.layout.activity_test2,null);
        AddressSelector addressSelector = (AddressSelector) view.findViewById(R.id.add_test_selector);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_pcs_choice_cancel);
        db = new DBhelper(context);
        final ArrayList<Area> province = db.getProvince();
        addressSelector.setTabAmount(3);
        addressSelector.setCities(province,"p");
        addressSelector.setOnItemClickListener(new AddressSelector.OnItemClickListener() {
            @Override
            public void itemClick(AddressSelector addressSelector, Area area, int tabPosition, int position) {
                switch (tabPosition){
                    case 0:
                        if(province!=null) {
                            Area curr = province.get(position);
                            String pcode = curr.getCode();
                            city = db.getCity(pcode);
                            addressSelector.setCities(city,"c");
                            p = curr.getName().substring(0,curr.getName().length()-1);
                        }
                        break;
                    case 1:
                        if(city!=null) {
                            Area curr1 = city.get(position);
                            String pcode1 = curr1.getCode();
                            district = db.getDistrict(pcode1);
                            addressSelector.setCities(district,"s");
                            c = curr1.getName().substring(0,curr1.getName().length()-1);
                        }
                        break;
                    case 2:
                        if(district!=null) {
                            Area curr2 = district.get(position);
                            String s = curr2.getName().substring(0,curr2.getName().length()-1);
                            String address = "";
                            if(p.equals(c)) {
                                address = p + s;
                            }else{
                                address = p + c + s;
                            }
                            onItemClickListenr.getFinalAddress(address);
                        }
                        break;
                }
            }
        });

        addressSelector.setOnTabSelectedListener(new AddressSelector.OnTabSelectedListener() {
            @Override
            public void onTabSelected(AddressSelector addressSelector, AddressSelector.Tab tab) {
                switch (tab.getIndex()){
                    case 0:
                        addressSelector.setCities(province,"p");
                        break;
                    case 1:
                        if(city!=null) {
                            addressSelector.setCities(city,"c");
                        }
                        break;
                    case 2:
                        if(district!=null) {
                            addressSelector.setCities(district,"s");
                        }
                        break;
                }
            }

            @Override
            public void onTabReselected(AddressSelector addressSelector, AddressSelector.Tab tab) {

            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShowing()) {
                    dismiss();
                }
                vShadow.setVisibility(View.GONE);
            }
        });
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ScreenUtil.getScreenHeight(context) - DensityUtil.dip2px(context,20));
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#00000000"));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private OnItemClickListenr onItemClickListenr;

    public interface OnItemClickListenr {
        void getFinalAddress(String address);

    }

    public void setOnItemClickListenr(OnItemClickListenr onItemClickListenr) {
        this.onItemClickListenr = onItemClickListenr;
    }
}
