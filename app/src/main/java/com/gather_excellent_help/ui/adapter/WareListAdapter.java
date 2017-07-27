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
            holder.tv_home_type_price = (TextView) convertView.findViewById(R.id.tv_home_type_price);
            holder.tv_home_type_allprice = (TextView) convertView.findViewById(R.id.tv_home_type_allprice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String sTitle = dataBean.getTitle().substring(0, 12) + "...";
        holder.home_type_name.setText(sTitle);
        mImageLoader.loadImage(dataBean.getImg_url(),holder.home_type_photo,true);
        holder.tv_home_type_price.setText("页面价："+dataBean.getMarket_price());
        holder.tv_home_type_allprice.setText("聚优帮返："+(dataBean.getMarket_price() - dataBean.getSell_price())+" "+
        "到手价："+dataBean.getSell_price());
        return convertView;
    }

    class ViewHolder {
        ImageView home_type_photo;        //商品图片
        TextView home_type_name;            //商品名称
        TextView tv_home_type_price ;         //页面价
        TextView tv_home_type_allprice ;       //据友邦返
    }
}
