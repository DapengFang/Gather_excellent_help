package com.gather_excellent_help.update;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.ActivityListBean;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.span.ImageSpanUtil;
import com.gather_excellent_help.utils.span.MyImageSpan;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dapeng Fang on 2017/8/18.
 */

public class TypeActivityListAdapter extends RecyclerView.Adapter<TypeActivityListAdapter.HomeActivityListViewHolder> {

    private Context context;
    private List<ActivityListBean.DataBean> activityData;
    private double user_rate;
    private int shopType;
    private boolean isToggle;

    public TypeActivityListAdapter(Context context, List<ActivityListBean.DataBean> activityData) {
        this.context = context;
        this.activityData = activityData;
        shopType = Tools.getShopType(context);
        LogUtil.e("type shopType = " + shopType);
        String userRate = Tools.getUserRate(context);
        if (!TextUtils.isEmpty(userRate)) {
            double v = Double.parseDouble(userRate);
            user_rate = v / 100;
        }
        isToggle = Tools.isToggleShow(context);
    }

    @Override
    public HomeActivityListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.home_type_list_item, null);
        return new HomeActivityListViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final HomeActivityListViewHolder holder, final int position) {
        ActivityListBean.DataBean dataBean = activityData.get(position);
        final DecimalFormat df = new DecimalFormat("#0.00");
        final int site_id = dataBean.getSite_id();
        final int article_id = dataBean.getArticle_id();
        final String img_url = dataBean.getImg_url();
        final int couponsPrice = dataBean.getCouponsPrice();
        final String couponsUrl = dataBean.getCouponsUrl();
        final String title = dataBean.getTitle();
        final double sell_price = dataBean.getSell_price();
        final String link_url = dataBean.getLink_url();
        double tkRate = dataBean.getTkRate() / 100;
        double zhuan = (sell_price - couponsPrice) * tkRate * 0.9f * user_rate * dataBean.getCommission_rate();
        double coast = sell_price - couponsPrice - zhuan;
        final String goods_id = String.valueOf(dataBean.getProductId());
        final String secondCouponsUrl = dataBean.getSecondCouponsUrl();

        double suning_rate = dataBean.getSuning_rate();
        double s_zhuan = sell_price * tkRate * user_rate;
        double s_coast = sell_price - s_zhuan;

        if (holder.tvActivityWarePrice != null) {
            holder.tvActivityWarePrice.setText("￥" + df.format(sell_price));
        }

        if (site_id == 1) {

            if (holder.tvActivityWareTitle != null && title != null) {
                SpannableString span = new SpannableString("\t\t" + title);
                Drawable drawable = context.getResources().getDrawable(R.drawable.taobao_order_icon);
                Bitmap bitmap = ImageSpanUtil.zoomDrawable(drawable, DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16));
                MyImageSpan image = new MyImageSpan(context, bitmap, -1);
                span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvActivityWareTitle.setText(span);
            }

            if (holder.ivActivityListWareImg != null && img_url != null) {
                Glide.with(context).load(img_url + "_320x320q90.jpg")
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                        .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                        .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                        .into(holder.ivActivityListWareImg);//请求成功后把图片设置到的控件
            }
            if (holder.tvActivityWareCoupon != null) {
                holder.tvActivityWareCoupon.setText("领券减" + couponsPrice);
            }

            if (holder.tvActivityWareZhuan != null) {
                holder.tvActivityWareZhuan.setText("￥" + df.format(zhuan));
            }
            if (holder.tvActivityWareCoast != null) {
                holder.tvActivityWareCoast.setText("￥" + df.format(coast));
            }
            if (holder.tvActivityWareCoupon != null) {
                if (couponsPrice > 0) {
                    holder.tvActivityWareCoupon.setVisibility(View.VISIBLE);
                } else {
                    holder.tvActivityWareCoupon.setVisibility(View.GONE);
                }
            }
            if (holder.ll_activity_list_ware_zhuan != null) {
                if (shopType == 1) {
                    if (isToggle) {
                        holder.ll_activity_list_ware_zhuan.setVisibility(View.GONE);
                    } else {
                        holder.ll_activity_list_ware_zhuan.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.ll_activity_list_ware_zhuan.setVisibility(View.GONE);
                }

                if (zhuan == 0) {
                    holder.ll_activity_list_ware_zhuan.setVisibility(View.GONE);
                }
            }
            if (holder.tvActivityWareCouponSecond != null) {
                if (secondCouponsUrl != null && !TextUtils.isEmpty(secondCouponsUrl)) {
                    holder.tvActivityWareCouponSecond.setVisibility(View.VISIBLE);
                } else {
                    holder.tvActivityWareCouponSecond.setVisibility(View.GONE);
                }

                holder.tvActivityWareCouponSecond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("web_url", secondCouponsUrl);
                        intent.putExtra("type", "second");
                        context.startActivity(intent);
                    }
                });
            }
        } else if (site_id == 2) {
            if (holder.tvActivityWareCoupon != null) {
                holder.tvActivityWareCoupon.setVisibility(View.GONE);
            }
            if (holder.tvActivityWareCouponSecond != null) {
                holder.tvActivityWareCouponSecond.setVisibility(View.GONE);
            }

            if (holder.tvActivityWareZhuan != null) {
                holder.tvActivityWareZhuan.setText("￥" + df.format(s_zhuan));
                holder.tvActivityWareZhuan.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int width = holder.tvActivityWareZhuan.getWidth();
                        int height = holder.tvActivityWareZhuan.getHeight();
                        LogUtil.e(" zhuan height = " + height );
                    }
                });
            }
            if (holder.tvActivityWareCoast != null) {
                holder.tvActivityWareCoast.setText("￥" + df.format(s_coast));
            }
            if (holder.ll_activity_list_ware_zhuan != null) {
                if (shopType == 1) {
                    if (isToggle) {
                        holder.ll_activity_list_ware_zhuan.setVisibility(View.GONE);
                    } else {
                        holder.ll_activity_list_ware_zhuan.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.ll_activity_list_ware_zhuan.setVisibility(View.GONE);
                }

                if (s_zhuan == 0) {
                    holder.ll_activity_list_ware_zhuan.setVisibility(View.GONE);
                }
            }

            if (holder.tvActivityWareTitle != null && title != null) {
                SpannableString span = new SpannableString("\t\t" + title);
                Drawable drawable = context.getResources().getDrawable(R.drawable.suning_ware_icon);
                Bitmap bitmap = ImageSpanUtil.zoomDrawable(drawable, DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16));
                MyImageSpan image = new MyImageSpan(context, bitmap, -1);
                span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvActivityWareTitle.setText(span);
            }

            if (holder.ivActivityListWareImg != null && img_url != null) {
                Glide.with(context).load(img_url.replace("800x800", "400x400"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                        .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                        .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                        .into(holder.ivActivityListWareImg);//请求成功后把图片设置到的控件
            }
        }


        if (holder.llActivityListWare != null) {
            holder.llActivityListWare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemclickListener.onItemClick(view, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return null == activityData ? 0 : activityData.size();
    }

    public class HomeActivityListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_activity_list_ware_img)
        ImageView ivActivityListWareImg;
        @Bind(R.id.tv_activity_ware_coupon)
        TextView tvActivityWareCoupon;
        @Bind(R.id.tv_activity_ware_title)
        TextView tvActivityWareTitle;
        @Bind(R.id.tv_activity_ware_person)
        TextView tvActivityWarePerson;
        @Bind(R.id.tv_activity_ware_price)
        TextView tvActivityWarePrice;
        @Bind(R.id.tv_activity_ware_zhuan)
        TextView tvActivityWareZhuan;
        @Bind(R.id.tv_activity_ware_coast)
        TextView tvActivityWareCoast;
        @Bind(R.id.ll_activity_list_ware)
        LinearLayout llActivityListWare;
        @Bind(R.id.tv_activity_ware_coupon_second)
        TextView tvActivityWareCouponSecond;
        @Bind(R.id.ll_activity_list_ware_zhuan)
        LinearLayout ll_activity_list_ware_zhuan;

        TextView tv_activity_sun_tao_icon;

        public HomeActivityListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tv_activity_sun_tao_icon = (TextView) itemView.findViewById(R.id.tv_activity_sun_tao_icon);
        }
    }

    private OnItemclickListener onItemclickListener;

    public interface OnItemclickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemclickListener(OnItemclickListener onItemclickListener) {
        this.onItemclickListener = onItemclickListener;
    }
}
