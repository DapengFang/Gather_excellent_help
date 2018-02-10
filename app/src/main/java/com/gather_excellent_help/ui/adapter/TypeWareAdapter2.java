package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.ActivityListBean;
import com.gather_excellent_help.bean.SearchWareBean;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.update.TypeActivityListAdapter;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.span.ImageSpanUtil;
import com.gather_excellent_help.utils.span.MyImageSpan;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wuxin on 2017/7/12.
 */

public class TypeWareAdapter2 extends RecyclerView.Adapter<TypeWareAdapter2.TypeWareViewHolder> {

    private Context context;
    private List<ActivityListBean.DataBean> data;
    private double user_rate;
    private int shopType;

    public TypeWareAdapter2(Context context, List<ActivityListBean.DataBean> data) {
        this.context = context;
        this.data = data;
        shopType = Tools.getShopType(context);
        String userRate = Tools.getUserRate(context);
        if (!TextUtils.isEmpty(userRate)) {
            double v = Double.parseDouble(userRate);
            user_rate = v / 100;
        }
    }

    @Override
    public TypeWareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.item_type_text_pic_grid, null);
        return new TypeWareViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(TypeWareViewHolder holder, final int position) {

        DecimalFormat df = new DecimalFormat("#0.00");
        ActivityListBean.DataBean dataBean = data.get(position);
        final int site_id = dataBean.getSite_id();
        final int article_id = dataBean.getArticle_id();
        final int exclusive = dataBean.getExclusive();
        int couponsPrice = dataBean.getCouponsPrice();
        double tkRate = dataBean.getTkRate() / 100;
        double zhuan = (dataBean.getSell_price() - couponsPrice) * tkRate * 0.9f * user_rate * dataBean.getCommission_rate();
        double coast = dataBean.getSell_price() - zhuan - couponsPrice;
        final String couponsUrl = dataBean.getCouponsUrl();
        final String secondCouponsUrl = dataBean.getSecondCouponsUrl();

        double suning_rate = dataBean.getSuning_rate();
        double s_zhuan = dataBean.getSell_price() * tkRate * user_rate;
        double s_coast = dataBean.getSell_price() - s_zhuan;

        if (holder.rl_activity_rexiao_share != null) {
            holder.rl_activity_rexiao_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareClickListener.onShareClick(v, position);
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemclickListener.onItemClick(v, position);
            }
        });

        if (exclusive == 1) {
            holder.iv_activity_list_vip.setVisibility(View.VISIBLE);
            holder.ll_activity_lsit_coupon.setVisibility(View.GONE);
        } else {
            holder.iv_activity_list_vip.setVisibility(View.GONE);
            holder.ll_activity_lsit_coupon.setVisibility(View.VISIBLE);
        }

        if (holder.tv_home_type_aprice != null) {
            String ware_price = " ¥" + df.format(dataBean.getSell_price());
            SpannableString spannableString = new SpannableString(ware_price);
            RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(0.8f);
            spannableString.setSpan(sizeSpan01, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.tv_home_type_aprice.setText(spannableString);
        }

        if (site_id == 1) {

            if (dataBean.getTitle() != null) {
                SpannableString span = new SpannableString("\t\t" + dataBean.getTitle());
                Drawable drawable = context.getResources().getDrawable(R.drawable.t_taobao_ware_icon);
                Bitmap bitmap = ImageSpanUtil.zoomDrawable(drawable, DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16));
                MyImageSpan image = new MyImageSpan(context, bitmap, -1);
                span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.home_type_name.setText(span);
            }

            if (dataBean.getImg_url() != null && holder.home_type_photo != null) {
                Glide.with(context).load(dataBean.getImg_url() + "_430x430q90.jpg")
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                        .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                        .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                        .into(holder.home_type_photo);//请求成功后把图片设置到的控件
            }

            if (couponsPrice > 0) {
                holder.ll_activity_lsit_coupon.setVisibility(View.VISIBLE);
                holder.tv_rush_ware_coupons.setText("" + couponsPrice);
            } else {
                holder.ll_activity_lsit_coupon.setVisibility(View.INVISIBLE);
            }
            if (secondCouponsUrl != null && !TextUtils.isEmpty(secondCouponsUrl)) {
                holder.tv_rush_ware_second_coupons.setVisibility(View.VISIBLE);
                holder.tv_rush_ware_second_coupons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("web_url", secondCouponsUrl);
                        intent.putExtra("type", "second");
                        context.startActivity(intent);
                    }
                });
            } else {
                holder.tv_rush_ware_second_coupons.setVisibility(View.GONE);
            }

            holder.tv_home_type_sale.setText("赚 " + df.format(zhuan));
            holder.tv_home_type_coast.setText("到手价 " + df.format(coast));

            if (shopType == 1) {
                boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
                if (toggleShow) {
                    holder.ll_activity_list_ware_zhuan.setVisibility(View.INVISIBLE);
                } else {
                    holder.ll_activity_list_ware_zhuan.setVisibility(View.VISIBLE);
                }
            } else {
                holder.ll_activity_list_ware_zhuan.setVisibility(View.INVISIBLE);
            }
            if (zhuan == 0) {
                holder.ll_activity_list_ware_zhuan.setVisibility(View.INVISIBLE);
            }
        } else if (site_id == 2) {
            LogUtil.e("suning_rate = " + suning_rate);

            if (dataBean.getTitle() != null) {
                SpannableString span = new SpannableString("\t\t" + dataBean.getTitle());
                Drawable drawable = context.getResources().getDrawable(R.drawable.s_suning_ware_icon);
                Bitmap bitmap = ImageSpanUtil.zoomDrawable(drawable, DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16));
                MyImageSpan image = new MyImageSpan(context, bitmap, -1);
                span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.home_type_name.setText(span);
            }

            if (holder.rl_activity_rexiao_share != null) {
                holder.rl_activity_rexiao_share.setVisibility(View.GONE);
            }

            if (holder.tv_rush_ware_coupons != null) {
                holder.tv_rush_ware_coupons.setVisibility(View.GONE);
            }
            if (holder.tv_rush_ware_second_coupons != null) {
                holder.tv_rush_ware_second_coupons.setVisibility(View.GONE);
            }
            holder.tv_home_type_sale.setText("赚 " + df.format(s_zhuan));
            holder.tv_home_type_coast.setText("到手价 " + df.format(s_coast));
            if (shopType == 1) {
                boolean toggleShow = CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
                if (toggleShow) {
                    holder.ll_activity_list_ware_zhuan.setVisibility(View.INVISIBLE);
                } else {
                    holder.ll_activity_list_ware_zhuan.setVisibility(View.VISIBLE);
                }
            } else {
                holder.ll_activity_list_ware_zhuan.setVisibility(View.INVISIBLE);
            }

            if (s_zhuan == 0) {
                holder.ll_activity_list_ware_zhuan.setVisibility(View.INVISIBLE);
            }
            if (dataBean.getImg_url() != null && holder.home_type_photo != null) {
                Glide.with(context).load(dataBean.getImg_url().replace("800x800", "400x400"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                        .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                        .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                        .into(holder.home_type_photo);//请求成功后把图片设置到的控件
            }

        }

    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    public class TypeWareViewHolder extends RecyclerView.ViewHolder {

        ImageView home_type_photo;        //商品图片
        TextView home_type_name;            //商品名称
        TextView tv_home_type_sale;         //赚
        TextView tv_home_type_coast;       //成本
        TextView tv_home_type_aprice;       //活动价
        TextView tv_rush_ware_coupons;       //优惠券
        TextView tv_rush_ware_second_coupons;       //优惠券2
        LinearLayout ll_activity_list_ware_zhuan;     //赚和成本
        ImageView iv_activity_list_vip;    //专享价
        RelativeLayout rl_activity_rexiao_share; //分享
        LinearLayout ll_activity_lsit_coupon; //优惠券隐藏

        public TypeWareViewHolder(View itemView) {
            super(itemView);
            home_type_photo = (ImageView) itemView.findViewById(R.id.iv_home_type_photo);
            home_type_name = (TextView) itemView.findViewById(R.id.tv_home_type_title);
            tv_home_type_sale = (TextView) itemView.findViewById(R.id.tv_rush_ware_sale);
            tv_home_type_coast = (TextView) itemView.findViewById(R.id.tv_rush_ware_coast);
            tv_home_type_aprice = (TextView) itemView.findViewById(R.id.tv_rush_ware_aprice);
            tv_rush_ware_coupons = (TextView) itemView.findViewById(R.id.tv_rush_ware_coupons);
            tv_rush_ware_second_coupons = (TextView) itemView.findViewById(R.id.tv_type_second_coupons);
            ll_activity_list_ware_zhuan = (LinearLayout) itemView.findViewById(R.id.ll_activity_list_ware_zhuan);
            iv_activity_list_vip = (ImageView) itemView.findViewById(R.id.iv_activity_list_vip);
            rl_activity_rexiao_share = (RelativeLayout) itemView.findViewById(R.id.rl_activity_rexiao_share);
            ll_activity_lsit_coupon = (LinearLayout) itemView.findViewById(R.id.ll_activity_lsit_coupon);
        }
    }

    private OnShareClickListener onShareClickListener;

    public interface OnShareClickListener {
        void onShareClick(View v, int position);
    }

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }

    private OnItemclickListener onItemclickListener;

    public interface OnItemclickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemclickListener(OnItemclickListener onItemclickListener) {
        this.onItemclickListener = onItemclickListener;
    }
}
