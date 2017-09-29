package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.SSLCertificateSocketFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.HomeTypeBean;
import com.gather_excellent_help.bean.SearchWareBean;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wuxin on 2017/7/12.
 */

public class WareListAdapter extends BaseAdapter {

    private Context context;
    private List<SearchWareBean.DataBean> data;
    private LayoutInflater inflater;    //布局填充器
    private double user_rate;
    private String load_type;
    private int shopType;

    public WareListAdapter(Context context,List<SearchWareBean.DataBean> data,String load_type) {
        this.context = context;
        this.data = data;
        if(load_type!=null) {
            this.load_type = load_type;
        }else{
            this.load_type = "";
        }
        shopType = Tools.getShopType(context);
        inflater = LayoutInflater.from(context);
        //mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        String userRate = Tools.getUserRate(context);
        if(!TextUtils.isEmpty(userRate)) {
            double v = Double.parseDouble(userRate);
            user_rate = v/100;
        }
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
        SearchWareBean.DataBean dataBean = data.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_type_text_pic_grid, null);
            holder = new ViewHolder();
            holder.home_type_photo = (ImageView) convertView.findViewById(R.id.iv_home_type_photo);
            holder.home_type_name = (TextView) convertView.findViewById(R.id.tv_home_type_title);
            holder.tv_home_type_sale = (TextView) convertView.findViewById(R.id.tv_rush_ware_sale);
            holder.tv_home_type_coast = (TextView) convertView.findViewById(R.id.tv_rush_ware_coast);
            holder.tv_home_type_aprice = (TextView) convertView.findViewById(R.id.tv_rush_ware_aprice);
            holder.tv_rush_ware_coupons = (TextView) convertView.findViewById(R.id.tv_rush_ware_coupons);
            holder.tv_rush_ware_second_coupons = (TextView) convertView.findViewById(R.id.tv_type_second_coupons);
            holder.ll_activity_list_ware_zhuan = (LinearLayout) convertView.findViewById(R.id.ll_activity_list_ware_zhuan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(dataBean.getTitle()!=null) {
            holder.home_type_name.setText(dataBean.getTitle());
        }
        //mImageLoader.loadImage(dataBean.getImg_url()+"_430x430q90.jpg",holder.home_type_photo,true);
        if(dataBean.getImg_url()!=null && holder.home_type_photo!=null) {
            Glide.with(context).load(dataBean.getImg_url()+"_430x430q90.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                    .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                    .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                    .into(holder.home_type_photo);//请求成功后把图片设置到的控件
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        int couponsPrice = dataBean.getCouponsPrice();
        double tkRate = dataBean.getTkRate() / 100;
        double zhuan = (dataBean.getSell_price() - couponsPrice) * tkRate * 0.9f * user_rate* dataBean.getCommission_rate();
        double coast = dataBean.getSell_price() - zhuan -couponsPrice;
        final String couponsUrl = dataBean.getCouponsUrl();
        final String secondCouponsUrl = dataBean.getSecondCouponsUrl();
        if(load_type.equals("isVip")) {
            holder.tv_rush_ware_coupons.setVisibility(View.INVISIBLE);
            holder.tv_rush_ware_second_coupons.setVisibility(View.GONE);
        }else{
            if(couponsPrice>0) {
                holder.tv_rush_ware_coupons.setVisibility(View.VISIBLE);
                holder.tv_rush_ware_coupons.setText("领券减"+couponsPrice);
                holder.tv_rush_ware_coupons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(couponsUrl!=null && !TextUtils.isEmpty(couponsUrl)) {
//                            Intent intent = new Intent(context, WebActivity.class);
//                            intent.putExtra("web_url",couponsUrl);
//                            context.startActivity(intent);
                        }
                    }
                });
            }else {
                holder.tv_rush_ware_coupons.setVisibility(View.INVISIBLE);
            }
            if(secondCouponsUrl!=null && !TextUtils.isEmpty(secondCouponsUrl)) {
                holder.tv_rush_ware_second_coupons.setVisibility(View.VISIBLE);
                holder.tv_rush_ware_second_coupons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("web_url",secondCouponsUrl);
                        intent.putExtra("type","second");
                        context.startActivity(intent);
                    }
                });
            }else{
                holder.tv_rush_ware_second_coupons.setVisibility(View.GONE);
            }
        }
        holder.tv_home_type_sale.setText("￥"+df.format(zhuan));
        holder.tv_home_type_coast.setText("￥"+df.format(coast));
        holder.tv_home_type_aprice.setText("￥"+df.format(dataBean.getSell_price()));
        //int group_id = CacheUtils.getInteger(context, CacheUtils.GROUP_TYPE, -1);
        if(shopType==1){
            boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
            if(toggleShow) {
               holder.ll_activity_list_ware_zhuan.setVisibility(View.INVISIBLE);
            }else{
                holder.ll_activity_list_ware_zhuan.setVisibility(View.VISIBLE);
            }
        }else{
            holder.ll_activity_list_ware_zhuan.setVisibility(View.INVISIBLE);
        }

        if(zhuan == 0) {
            holder.ll_activity_list_ware_zhuan.setVisibility(View.INVISIBLE);
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
        TextView tv_rush_ware_second_coupons ;       //优惠券2
        LinearLayout ll_activity_list_ware_zhuan;     //赚和成本
    }

}
