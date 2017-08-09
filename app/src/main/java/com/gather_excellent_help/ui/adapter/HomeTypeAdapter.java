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

import java.util.List;

/**
 * Created by wuxin on 2017/7/12.
 */

public class HomeTypeAdapter extends BaseAdapter {

    private Context context;
    private List<HomeTypeBean> datas;
    private LayoutInflater inflater;    //布局填充器

    public HomeTypeAdapter(Context context, List<HomeTypeBean> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
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
        HomeTypeBean homeTypeBean = datas.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_text_pic, null);
            holder = new ViewHolder();
            holder.home_type_photo = (ImageView) convertView.findViewById(R.id.iv_home_type_photo);
            holder.home_type_name = (TextView) convertView.findViewById(R.id.tv_home_type_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.home_type_name.setText(homeTypeBean.getTypeName());
        holder.home_type_photo.setImageResource(homeTypeBean.getId());
        return convertView;
    }

    class ViewHolder {
        ImageView home_type_photo;        //商品图片
        TextView home_type_name;            //商品名称
    }
}
