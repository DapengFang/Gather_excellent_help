package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.TypeWareBean;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import java.net.URL;
import java.text.DecimalFormat;
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
    private int shopType;
    private double user_rate;

    public TypeWareAdapter(Context context,List<TypeWareBean.DataBean> data) {
        this.context = context;
        this.data = data;
        shopType = Tools.getShopType(context);
        String userRate = Tools.getUserRate(context);
        if(!TextUtils.isEmpty(userRate)) {
            double v = Double.parseDouble(userRate);
            user_rate = v/100;
        }
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
        if(dataBean.getTitle().length()<=12) {
            typeWareViewHolder.tvTypeName.setText(dataBean.getTitle());
        }else{
            String title = dataBean.getTitle().substring(0,12)+"...";
            typeWareViewHolder.tvTypeName.setText(title);
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        int couponsPrice = dataBean.getCouponsPrice();
        double sell_price = dataBean.getSell_price();
        double tkRate = dataBean.getTkRate()/100;
        double zhuan = (sell_price - couponsPrice) * tkRate * 0.9f *user_rate;
        double coast = sell_price - zhuan -couponsPrice;
        final String couponsUrl = dataBean.getCouponsUrl();
        final String secondCouponsUrl = dataBean.getSecondCouponsUrl();
        typeWareViewHolder.tvTypeWareSale.setText("赚: ¥"+df.format(zhuan));
        typeWareViewHolder.tvTypeWareCoast.setText("成本: ¥"+df.format(coast));
        Tools.setPartTextColor(typeWareViewHolder.tvTypeWareAprice,"活动价: ¥"+df.format(sell_price),":");
        if(couponsPrice>0) {
            typeWareViewHolder.tvTypeWareCoupons.setVisibility(View.VISIBLE);
            typeWareViewHolder.tvTypeWareCoupons.setText("领券立减"+couponsPrice);
            typeWareViewHolder.tvTypeWareCoupons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(couponsUrl!=null && !TextUtils.isEmpty(couponsUrl)) {
//                        Intent intent = new Intent(context, WebActivity.class);
//                        intent.putExtra("web_url",couponsUrl);
//                        context.startActivity(intent);
                    }
                }
            });
        }else{
            typeWareViewHolder.tvTypeWareCoupons.setVisibility(View.GONE);
        }
        if(secondCouponsUrl!=null && !TextUtils.isEmpty(secondCouponsUrl)) {
            typeWareViewHolder.tvTypeSecondCoupons.setVisibility(View.VISIBLE);
            typeWareViewHolder.tvTypeSecondCoupons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context, WebActivity.class);
//                    intent.putExtra("web_url",secondCouponsUrl);
//                    context.startActivity(intent);
                }
            });
        }else{
            typeWareViewHolder.tvTypeSecondCoupons.setVisibility(View.GONE);
        }
        //int group_id = CacheUtils.getInteger(context, CacheUtils.GROUP_TYPE, -1);
        if(shopType==1){
            boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
            if(toggleShow) {
                typeWareViewHolder.tvTypeWareSale.setVisibility(View.GONE);
                typeWareViewHolder.tvTypeWareCoast.setVisibility(View.GONE);
            }else{
                typeWareViewHolder.tvTypeWareSale.setVisibility(View.VISIBLE);
                typeWareViewHolder.tvTypeWareCoast.setVisibility(View.VISIBLE);
            }
        }else{
            typeWareViewHolder.tvTypeWareSale.setVisibility(View.GONE);
            typeWareViewHolder.tvTypeWareCoast.setVisibility(View.GONE);
        }
        typeWareViewHolder.ll_type_ware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == data ? 0:data.size();
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
        @Bind(R.id.tv_type_second_coupons)
        TextView tvTypeSecondCoupons;
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
