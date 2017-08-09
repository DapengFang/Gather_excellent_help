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
import com.gather_excellent_help.bean.SearchTaobaoBean;
import com.gather_excellent_help.bean.SearchWareBean;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wuxin on 2017/7/12.
 */

public class TaobaoWareListAdapter extends BaseAdapter {

    private Context context;
    private List<SearchTaobaoBean.DataBean> data;
    private LayoutInflater inflater;    //布局填充器
    private ImageLoader mImageLoader;

    public TaobaoWareListAdapter(Context context, List<SearchTaobaoBean.DataBean> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    @Override
    public int getCount() {
        return null == data? 0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchTaobaoBean.DataBean dataBean = data.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_text_pic_grid, null);
            holder = new ViewHolder();
            holder.home_type_photo = (ImageView) convertView.findViewById(R.id.iv_home_type_photo);
            holder.home_type_name = (TextView) convertView.findViewById(R.id.tv_home_type_title);
            holder.tv_home_type_sale = (TextView) convertView.findViewById(R.id.tv_rush_ware_sale);
            holder.tv_home_type_coast = (TextView) convertView.findViewById(R.id.tv_rush_ware_coast);
            holder.tv_home_type_aprice = (TextView) convertView.findViewById(R.id.tv_rush_ware_aprice);
            holder.tv_rush_ware_coupons = (TextView) convertView.findViewById(R.id.tv_rush_ware_coupons);
            holder.tv_rush_ware_second_coupons = (TextView) convertView.findViewById(R.id.tv_type_second_coupons);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_rush_ware_second_coupons.setVisibility(View.GONE);
        if(dataBean.getTitle().length()>10) {
            String sTitle = dataBean.getTitle().substring(0, 10) + "...";
            holder.home_type_name.setText(sTitle);
        }else{
            holder.home_type_name.setText(dataBean.getTitle());
        }
        if(holder.home_type_photo!=null && dataBean.getImg_url()!=null) {
            mImageLoader.loadImage(dataBean.getImg_url(),holder.home_type_photo,true);
        }
        holder.tv_home_type_aprice.setText("页面价："+dataBean.getSell_price());
        final SearchTaobaoBean.DataBean.CouponInfoBean coupon_info = dataBean.getCoupon_info();
        if(coupon_info!=null) {
            String coupon_info1 = coupon_info.getCoupon_info();
            int coupons = 0;
            if(coupon_info1!=null && !TextUtils.isEmpty(coupon_info1)) {
                int index = coupon_info1.indexOf("减")+1;
                String coupon = coupon_info1.substring(index, coupon_info1.length() - 1);
                coupons = Integer.parseInt(coupon);
                holder.tv_rush_ware_coupons.setText("领券立减"+coupon);
                holder.tv_rush_ware_coupons.setVisibility(View.VISIBLE);
            }else{
                holder.tv_rush_ware_coupons.setVisibility(View.INVISIBLE);
            }
            double maxCommissionRate = Double.parseDouble(coupon_info.getMax_commission_rate());
            double sellPrice = Double.parseDouble(dataBean.getSell_price());
            double zhuan = (sellPrice - coupons) * (maxCommissionRate / 100) * 0.9f * 0.83f;
            DecimalFormat df = new DecimalFormat("#0.00");
            double coast = sellPrice - zhuan -coupons;
            holder.tv_home_type_sale.setText("赚:￥"+df.format(zhuan));
            holder.tv_home_type_coast.setText("成本:"+df.format(coast));
            holder.tv_rush_ware_coupons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("web_url",coupon_info.getCoupon_click_url());
                    context.startActivity(intent);
                }
            });
            int group_id = CacheUtils.getInteger(context, CacheUtils.GROUP_TYPE, -1);
            if(group_id==4){
                boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
                if(toggleShow) {
                    holder.tv_home_type_sale.setVisibility(View.GONE);
                    holder.tv_home_type_coast.setVisibility(View.GONE);
                }else{
                    holder.tv_home_type_sale.setVisibility(View.VISIBLE);
                    holder.tv_home_type_coast.setVisibility(View.VISIBLE);
                }
            }else{
                holder.tv_home_type_sale.setVisibility(View.GONE);
                holder.tv_home_type_coast.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    class ViewHolder {
        ImageView home_type_photo;        //商品图片
        TextView home_type_name;            //商品名称
        TextView tv_home_type_sale ;         //赚
        TextView tv_home_type_coast ;       //成本
        TextView tv_home_type_aprice ;       //活动价
        TextView tv_rush_ware_coupons ;       //优惠券
        TextView tv_rush_ware_second_coupons ;       //优惠券
    }
}
