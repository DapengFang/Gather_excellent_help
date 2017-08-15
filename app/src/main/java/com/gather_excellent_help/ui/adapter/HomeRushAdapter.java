package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.HomeRushChangeBean;
import com.gather_excellent_help.bean.HomeTypeBean;
import com.gather_excellent_help.bean.HomeWareBean;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.ui.widget.MyTextView;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxin on 2017/7/12.
 */

public class HomeRushAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;    //布局填充器
    private ImageLoader mImageLoader;
    private List<HomeWareBean.DataBean.ItemBean> datas;
    private double user_rate;
    private int shopType;

    public HomeRushAdapter(Context context,List<HomeWareBean.DataBean.ItemBean> datas) {
        this.context = context;
        this.datas = datas;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        inflater = LayoutInflater.from(context);
        shopType = Tools.getShopType(context);
        String userRate = Tools.getUserRate(context);
        if(!TextUtils.isEmpty(userRate)) {
            double v = Double.parseDouble(userRate);
            user_rate = v/100;
        }
    }

    @Override
    public int getCount() {
        return null == datas? 0:datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // HomeTypeBean homeTypeBean = datas.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_rush_ware, null);
            holder = new ViewHolder();
            holder.home_rush_photo = (ImageView) convertView.findViewById(R.id.iv_rush_pic);
            holder.home_rush_name = (TextView) convertView.findViewById(R.id.tv_rush_title);
            holder.home_rush_sale = (TextView) convertView.findViewById(R.id.tv_rush_ware_sale);
            holder.home_rush_coast = (TextView) convertView.findViewById(R.id.tv_rush_ware_coast);
            holder.home_rush_aprice = (TextView) convertView.findViewById(R.id.tv_rush_ware_aprice);
            holder.home_rush_coupons = (MyTextView) convertView.findViewById(R.id.tv_rush_ware_coupons);
            holder.home_rush_second_coupons = (TextView) convertView.findViewById(R.id.tv_rush_second_coupons);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeWareBean.DataBean.ItemBean itemBean = datas.get(position);
        if(itemBean.getTitle().length()>6) {
            String newTitle = itemBean.getTitle().substring(0, 6);
            holder.home_rush_name.setText(newTitle);
        }else{
            holder.home_rush_name.setText(itemBean.getTitle());
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        int couponsPrice = itemBean.getCouponsPrice();
        double sell_price = itemBean.getSell_price();
        double tkRate = itemBean.getTkRate()/100;
        LogUtil.e("tkRate = "+tkRate+",user_rate = "+user_rate);
        double zhuan = (sell_price - couponsPrice)*tkRate*0.9f*user_rate;
        double coast = sell_price - zhuan -couponsPrice;
        final String couponsUrl = itemBean.getCouponsUrl();
        final String secondCouponsUrl = itemBean.getSecondCouponsUrl();
        holder.home_rush_sale.setText("赚:￥"+df.format(zhuan));
        holder.home_rush_coast.setText("成本:￥"+df.format(coast));
        if(couponsPrice>0) {
            holder.home_rush_coupons.setVisibility(View.VISIBLE);
            holder.home_rush_coupons.setText("领券立减"+couponsPrice);
            holder.home_rush_coupons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(couponsUrl!=null && !TextUtils.isEmpty(couponsUrl)) {
//                        Intent intent = new Intent(context, WebActivity.class);
//                        intent.putExtra("web_url",couponsUrl);
//                        context.startActivity(intent);
                    }
                }
            });
        }else{
            holder.home_rush_coupons.setVisibility(View.GONE);
        }
        if(secondCouponsUrl!=null && !TextUtils.isEmpty(secondCouponsUrl)) {
            holder.home_rush_second_coupons.setVisibility(View.VISIBLE);
            holder.home_rush_second_coupons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context, WebActivity.class);
//                    intent.putExtra("web_url",secondCouponsUrl);
//                    context.startActivity(intent);
                }
            });
        }else{
            holder.home_rush_second_coupons.setVisibility(View.GONE);
        }
        Tools.setPartTextColor(holder.home_rush_aprice,"活动价:￥"+df.format(sell_price),":");
        //int group_id = CacheUtils.getInteger(context, CacheUtils.GROUP_TYPE, -1);
        if(shopType==1){
            boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
            if(toggleShow) {
                holder.home_rush_sale.setVisibility(View.GONE);
                holder.home_rush_coast.setVisibility(View.GONE);
            }else{
                holder.home_rush_sale.setVisibility(View.VISIBLE);
                holder.home_rush_coast.setVisibility(View.VISIBLE);
            }
        }else{
                holder.home_rush_sale.setVisibility(View.GONE);
                holder.home_rush_coast.setVisibility(View.GONE);
        }
        if(itemBean.getImg_url()!=null) {
            mImageLoader.loadImage(itemBean.getImg_url(),holder.home_rush_photo,true);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView home_rush_photo;        //商品图片
        TextView home_rush_name;            //商品名称
        TextView home_rush_sale;            //聚优帮赚
        TextView home_rush_coast;            //聚优帮成本
        TextView home_rush_aprice;            //聚优帮活动价
        MyTextView home_rush_coupons;           //聚优帮优惠券
        TextView home_rush_second_coupons;           //聚优帮优惠券2
    }
}
