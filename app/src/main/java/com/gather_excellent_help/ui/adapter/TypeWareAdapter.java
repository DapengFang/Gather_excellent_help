package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.TypeWareBean;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import java.net.URL;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wuxin on 2017/7/13.
 */

public class TypeWareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<TypeWareBean.DataBean> data;
    private ImageLoader mImageLoader;

    public TypeWareAdapter(Context context,List<TypeWareBean.DataBean> data) {
        this.context = context;
        this.data = data;
        mImageLoader =ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(parent.getContext(), R.layout.item_type_ware,null);
        return new TypeWareViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TypeWareViewHolder typeWareViewHolder = (TypeWareViewHolder) holder;
        TypeWareBean.DataBean dataBean = data.get(position);
        String url = dataBean.getImg_url();
        mImageLoader.loadImage(url,typeWareViewHolder.ivTypeImg,true);
        String title = dataBean.getTitle().substring(0,10)+"...";
        typeWareViewHolder.tvTypeName.setText(title);
        typeWareViewHolder.tvTypeWareSale.setText("赚:￥"+(dataBean.getMarket_price() - dataBean.getSell_price()));
        typeWareViewHolder.tvTypeWareCoast.setText("成本:￥"+dataBean.getMarket_price());
        Tools.setPartTextColor(typeWareViewHolder.tvTypeWareAprice,"活动价:￥"+dataBean.getSell_price(),":");
        typeWareViewHolder.tvTypeWareCoupons.setText("领券立减"+dataBean.getCouponsPrice());
        typeWareViewHolder.ll_type_ware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TypeWareViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.iv_type_img)
        ImageView ivTypeImg;
        @Bind(R.id.tv_type_name)
        TextView tvTypeName;
        @Bind(R.id.tv_type_ware_sale)
        TextView tvTypeWareSale;
        @Bind(R.id.tv_type_ware_coast)
        TextView tvTypeWareCoast;
        @Bind(R.id.tv_type_ware_aprice)
        TextView tvTypeWareAprice;
        @Bind(R.id.tv_type_ware_coupons)
        TextView  tvTypeWareCoupons;
        @Bind(R.id.ll_type_ware)
        LinearLayout ll_type_ware;

        public TypeWareViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
