package com.gather_excellent_help.ui.adapter.sale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.sale.TuihuoBean;

import java.util.ArrayList;

/**
 * 作者：Dapeng Fang on 2016/9/26 10:31
 * <p/>
 * 邮箱：fdp111888@163.com
 */

public class TuihuanAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TuihuoBean> datas;
    private LayoutInflater layoutInflater;

    public TuihuanAdapter(Context context, ArrayList<TuihuoBean> datas) {
        this.context = context;
        this.datas = datas;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(convertView==null) {
            viewHolder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.item_tuihuan_text,viewGroup,false);
            viewHolder.tvItem= (TextView) convertView.findViewById(R.id.tv_item_tuihuan_text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvItem.setText(datas.get(position).getTextItem());
        return convertView;
    }

    class ViewHolder{
        TextView tvItem;
    }
}
