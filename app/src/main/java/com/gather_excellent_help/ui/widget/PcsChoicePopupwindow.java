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
import com.gather_excellent_help.bean.PcsBean;
import com.gather_excellent_help.bean.address.Area;
import com.gather_excellent_help.db.DBhelper;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.ScreenUtil;
import com.gather_excellent_help.utils.pcsutils.PcsQueryUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者：Dapeng Fang on 2016/12/29 10:22
 * <p/>
 * 邮箱：fdp111888@163.com
 */

public class PcsChoicePopupwindow extends PopupWindow {

    private Context context;
    private LayoutInflater inflater;
    private View vShadow;



    private String p = "";
    private String c = "";
    private String q = "";
    private String z = "";

    private List<PcsBean.DataBean> province;
    private List<PcsBean.DataBean> city;
    private List<PcsBean.DataBean> district;
    private List<PcsBean.DataBean> town;
    private String address = "";
    private String pcs = "";

    private String pid = "";
    private String cid = "";
    private String did = "";



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
        View view= inflater.inflate(R.layout.activity_address_pcs_selector,null);
        final AddressSelector addressSelector = (AddressSelector) view.findViewById(R.id.add_test_selector);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_pcs_choice_cancel);
        addressSelector.setTabAmount(4);
        PcsQueryUtil.getProvince(context, new PcsQueryUtil.OnPcsResultListener() {
            @Override
            public void onPcsResult(String result) {
                LogUtil.e("province = " + result);
                PcsBean pcsBean = new Gson().fromJson(result, PcsBean.class);
                int statusCode = pcsBean.getStatusCode();
                switch (statusCode) {
                    case 1 :
                        province = pcsBean.getData();
                        if(province!=null && province.size()>0) {
                            addressSelector.setCities(province,"p");
                        }
                        break;
                   case 0:
                       Toast.makeText(context, pcsBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                       break;

                }
            }
        });

        addressSelector.setOnItemClickListener(new AddressSelector.OnItemClickListener() {
            @Override
            public void itemClick(final AddressSelector addressSelector, PcsBean.DataBean area, int tabPosition, int position) {
                switch (tabPosition){
                    case 0:
                        if(province!=null) {
                            PcsBean.DataBean dataBean = province.get(position);
                            String pcode = dataBean.getArea_id();
                            pid = pcode;
                           p = dataBean.getName();
                            PcsQueryUtil.getCity(context, pcode, new PcsQueryUtil.OnPcsResultListener() {
                                @Override
                                public void onPcsResult(String result) {
                                    LogUtil.e("city = " + result);
                                    PcsBean pcsBean = new Gson().fromJson(result, PcsBean.class);
                                    int statusCode = pcsBean.getStatusCode();
                                    switch (statusCode) {
                                        case 1 :
                                            city = pcsBean.getData();
                                            if(city!=null && city.size()>0) {
                                                addressSelector.setCities(city,"c");
                                            }
                                            break;
                                        case 0:
                                            Toast.makeText(context, pcsBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                                            break;

                                    }
                                }
                            });
                        }
                        break;
                    case 1:
                        if(city!=null) {
                            PcsBean.DataBean dataBean = city.get(position);
                            String pcode = dataBean.getArea_id();
                            cid = pcode;
                            c = dataBean.getName();
                            PcsQueryUtil.getDistract(context, pcode, new PcsQueryUtil.OnPcsResultListener() {
                                @Override
                                public void onPcsResult(String result) {
                                    LogUtil.e("district = " + result);
                                    PcsBean pcsBean = new Gson().fromJson(result, PcsBean.class);
                                    int statusCode = pcsBean.getStatusCode();
                                    switch (statusCode) {
                                        case 1 :
                                            district = pcsBean.getData();
                                            if(district!=null && district.size()>0) {
                                                addressSelector.setCities(district,"d");
                                            }
                                            break;
                                        case 0:
                                            Toast.makeText(context, pcsBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                                            break;

                                    }
                                }
                            });
                        }
                        break;
                    case 2:
                        if(district !=null && district.size()>0) {
                           PcsBean.DataBean dataBean = district.get(position);
                            String pcode = dataBean.getArea_id();
                            did = pcode;
                            q = dataBean.getName();
                            PcsQueryUtil.getTown(context,cid, pcode, new PcsQueryUtil.OnPcsResultListener() {
                                @Override
                                public void onPcsResult(String result) {
                                    LogUtil.e("town= " + result);
                                    PcsBean pcsBean = new Gson().fromJson(result, PcsBean.class);
                                    int statusCode = pcsBean.getStatusCode();
                                    switch (statusCode) {
                                        case 1 :
                                            town = pcsBean.getData();
                                            if(town!=null && town.size()>0) {
                                                addressSelector.setCities(town,"t");
                                            }
                                            break;
                                        case 0:
                                            Toast.makeText(context, pcsBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                                            break;

                                    }
                                }
                            });
                        }
                        break;
                    case 3:
                        if(town !=null && town.size()>0) {
                            PcsBean.DataBean dataBean = town.get(position);
                            z =  dataBean.getName();
                            if(p.equals(c)) {
                                address = p + q + z;
                                pcs = p + "," + q + ","+ z;
                            }else{
                                address = p + c + q + z;
                                pcs = p + "," + c + "," + q + ","+ z;
                            }
                        }else {
                            if(p.equals(c)) {
                                address = p + q ;
                                pcs = p + "," + q;
                            }else{
                                address = p + c + q;
                                pcs = p + "," + c + "," + q;
                            }
                        }
                        String area_id = pid + "," + cid + "," + did;
                        onItemClickListenr.getFinalAddress(address,pcs,area_id);
                        setDefaultAddress();
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
                        if(district !=null) {
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

    /**
     * 初始化省市区镇
     */
    private void setDefaultAddress() {
        z = "";
    }

    private OnItemClickListenr onItemClickListenr;

    public interface OnItemClickListenr {
        void getFinalAddress(String address,String pcs,String area_id);

    }

    public void setOnItemClickListenr(OnItemClickListenr onItemClickListenr) {
        this.onItemClickListenr = onItemClickListenr;
    }
}
