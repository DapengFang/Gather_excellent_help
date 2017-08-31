package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.HomeTypeBean;
import com.gather_excellent_help.bean.TyepIndexBean;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import java.util.List;

/**
 * Created by wuxin on 2017/7/12.
 */

public class HomeTypeAdapter extends BaseAdapter {

    private Context context;
    private List<HomeTypeBean> datas;
    private LayoutInflater inflater;    //布局填充器
    private List<TyepIndexBean.DataBean> typeData;
    //private ImageLoader mImageLoader;

    public HomeTypeAdapter(Context context, List<HomeTypeBean> datas, List<TyepIndexBean.DataBean> typeData) {
        this.context = context;
        this.datas = datas;
        this.typeData = typeData;
        //mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
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
        TyepIndexBean.DataBean dataBean = typeData.get(position);
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
        if(holder.home_type_name!=null) {
            if(dataBean.getTitle()!=null) {
                holder.home_type_name.setText(dataBean.getTitle());
            }
        }
        if(holder.home_type_photo!=null) {
            if(dataBean.getImg_url()!=null) {
                //mImageLoader.loadImage(Url.IMG_URL+dataBean.getImg_url(),holder.home_type_photo,true);
                Glide.with(context).load(Url.IMG_URL+dataBean.getImg_url())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                        .into(holder.home_type_photo);//请求成功后把图片设置到的控件
            }else{
                LogUtil.e("img==null");
            }
        }
        return convertView;
    }

    class ViewHolder {
        ImageView home_type_photo;        //商品图片
        TextView home_type_name;            //商品名称
    }
}
