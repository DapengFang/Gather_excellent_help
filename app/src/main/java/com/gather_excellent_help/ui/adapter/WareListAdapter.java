package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.HomeTypeBean;
import com.gather_excellent_help.bean.SearchWareBean;
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
    private ImageLoader mImageLoader;

    public WareListAdapter(Context context,List<SearchWareBean.DataBean> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    @Override
    public int getCount() {
        return data.size();
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
            convertView = inflater.inflate(R.layout.item_text_pic_grid, null);
            holder = new ViewHolder();
            holder.home_type_photo = (ImageView) convertView.findViewById(R.id.iv_home_type_photo);
            holder.home_type_name = (TextView) convertView.findViewById(R.id.tv_home_type_title);
            holder.tv_home_type_sale = (TextView) convertView.findViewById(R.id.tv_rush_ware_sale);
            holder.tv_home_type_coast = (TextView) convertView.findViewById(R.id.tv_rush_ware_coast);
            holder.tv_home_type_aprice = (TextView) convertView.findViewById(R.id.tv_rush_ware_aprice);
            holder.tv_rush_ware_coupons = (TextView) convertView.findViewById(R.id.tv_rush_ware_coupons);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(dataBean.getTitle().length()>12) {
            String sTitle = dataBean.getTitle().substring(0, 12) + "...";
            holder.home_type_name.setText(sTitle);
        }else{
            holder.home_type_name.setText(dataBean.getTitle());
        }
        mImageLoader.loadImage(dataBean.getImg_url(),holder.home_type_photo,true);
        DecimalFormat df = new DecimalFormat("#0.00");
        double zhuan = dataBean.getSell_price() * 0.1F * 0.9f * 0.83f;
        double coast = dataBean.getSell_price() - zhuan;
        holder.tv_home_type_sale.setText("赚:￥"+df.format(zhuan));
        holder.tv_home_type_coast.setText("成本:"+df.format(coast));
        holder.tv_home_type_aprice.setText("页面价："+df.format(dataBean.getSell_price()));
        return convertView;
    }

    class ViewHolder {
        ImageView home_type_photo;        //商品图片
        TextView home_type_name;            //商品名称
        TextView tv_home_type_sale ;         //赚
        TextView tv_home_type_coast ;       //成本
        TextView tv_home_type_aprice ;       //活动价
        TextView tv_rush_ware_coupons ;       //优惠券
    }
}
