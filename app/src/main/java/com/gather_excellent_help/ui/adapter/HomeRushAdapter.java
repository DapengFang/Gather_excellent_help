package com.gather_excellent_help.ui.adapter;

import android.content.Context;
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
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxin on 2017/7/12.
 */

public class HomeRushAdapter extends BaseAdapter {

    private Context context; private LayoutInflater inflater;    //布局填充器

    private ImageLoader mImageLoader;
    private List<HomeWareBean.DataBean.ItemBean> datas;

    public HomeRushAdapter(Context context,List<HomeWareBean.DataBean.ItemBean> datas) {
        this.context = context;
        this.datas = datas;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
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
            holder.home_rush_price = (TextView) convertView.findViewById(R.id.tv_rush_price);
            holder.home_fanli_price = (TextView) convertView.findViewById(R.id.tv_fanli_price);
            holder.home_sell_price = (TextView) convertView.findViewById(R.id.tv_sell_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeWareBean.DataBean.ItemBean itemBean = datas.get(position);
        String newTitle = itemBean.getTitle().substring(0, 16) + "...";
        holder.home_rush_name.setText(newTitle);
        holder.home_rush_price.setText("页面价:￥"+itemBean.getMarket_price());
        holder.home_fanli_price.setText("聚优帮返:￥"+(itemBean.getMarket_price() - itemBean.getSell_price()));
        holder.home_sell_price.setText("到手价:￥"+itemBean.getSell_price());
        if(itemBean.getImg_url()!=null) {
            mImageLoader.loadImage(itemBean.getImg_url(),holder.home_rush_photo,true);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView home_rush_photo;        //商品图片
        TextView home_rush_name;            //商品名称
        TextView home_rush_price;            //商品价格
        TextView home_sell_price;            //优惠价格
        TextView home_fanli_price;            //聚优帮返利
    }
}
