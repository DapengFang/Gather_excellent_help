package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.BrandBean;
import com.gather_excellent_help.bean.SearchWareBean;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wuxin on 2017/7/12.
 */

public class BrandListAdapter extends BaseAdapter {

    private Context context;
    private List<BrandBean.DataBean> data;
    private LayoutInflater inflater;    //布局填充器
    private ImageLoader mImageLoader;

    public BrandListAdapter(Context context, List<BrandBean.DataBean> data) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final BrandBean.DataBean dataBean = data.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_brand_list, null);
            holder = new ViewHolder();
            holder.home_brand_name = (TextView) convertView.findViewById(R.id.tv_brand_list_title);
            holder.cv_brand_list = (CardView) convertView.findViewById(R.id.cv_brand_list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.home_brand_name.setText(dataBean.getTitle());
        boolean select = dataBean.isSelect();
        if(select) {
            holder.home_brand_name.setTextColor(Color.WHITE);
            holder.cv_brand_list.setCardBackgroundColor(Color.RED);
        }else{
            holder.home_brand_name.setTextColor(Color.parseColor("#55000000"));
            holder.cv_brand_list.setCardBackgroundColor(Color.parseColor("#11000000"));
        }
        return convertView;
    }

    class ViewHolder {
        TextView home_brand_name;            //商品名称
        CardView cv_brand_list;    //商品北京
    }
}
