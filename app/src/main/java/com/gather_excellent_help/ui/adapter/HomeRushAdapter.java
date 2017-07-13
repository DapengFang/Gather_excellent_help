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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxin on 2017/7/12.
 */

public class HomeRushAdapter extends BaseAdapter {

    private Context context;
    private List<HomeTypeBean> datas;
    private LayoutInflater inflater;    //布局填充器

    public HomeRushAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>(3);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(0);
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder {
        ImageView home_rush_photo;        //商品图片
        TextView home_rush_name;            //商品名称
        TextView home_rush_price;            //商品价格
    }
}
