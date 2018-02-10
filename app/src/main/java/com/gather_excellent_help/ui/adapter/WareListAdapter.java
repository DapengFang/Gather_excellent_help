package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.gather_excellent_help.bean.SearchWareBean;
import com.gather_excellent_help.ui.activity.WebActivity;
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

public class WareListAdapter extends BaseAdapter {

    private Context context;
    private List<SearchWareBean.DataBean> data;
    private LayoutInflater inflater;    //布局填充器
    private double user_rate;
    private String load_type;
    private int shopType;

    public WareListAdapter(Context context, List<SearchWareBean.DataBean> data, String load_type) {
        this.context = context;
        this.data = data;
        if (load_type != null) {
            this.load_type = load_type;
        } else {
            this.load_type = "";
        }
        shopType = Tools.getShopType(context);
        inflater = LayoutInflater.from(context);
        String userRate = Tools.getUserRate(context);
        if (!TextUtils.isEmpty(userRate)) {
            double v = Double.parseDouble(userRate);
            user_rate = v / 100;
        }
    }

    @Override
    public int getCount() {
        return null == data ? 0 : data.size();
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
        SearchWareBean.DataBean dataBean = data.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_type_text_pic_grid, null);
            holder = new ViewHolder();
            holder.home_type_photo = (ImageView) convertView.findViewById(R.id.iv_home_type_photo);
            holder.home_type_name = (TextView) convertView.findViewById(R.id.tv_home_type_title);
            holder.tv_home_type_sale = (TextView) convertView.findViewById(R.id.tv_rush_ware_sale);
            holder.tv_home_type_coast = (TextView) convertView.findViewById(R.id.tv_rush_ware_coast);
            holder.tv_home_type_aprice = (TextView) convertView.findViewById(R.id.tv_rush_ware_aprice);
            holder.tv_rush_ware_coupons = (TextView) convertView.findViewById(R.id.tv_rush_ware_coupons);
            holder.tv_rush_ware_second_coupons = (TextView) convertView.findViewById(R.id.tv_type_second_coupons);
            holder.ll_activity_list_ware_zhuan = (LinearLayout) convertView.findViewById(R.id.ll_activity_list_ware_zhuan);
            holder.iv_activity_list_vip = (ImageView) convertView.findViewById(R.id.iv_activity_list_vip);
            holder.rl_activity_rexiao_share = (RelativeLayout) convertView.findViewById(R.id.rl_activity_rexiao_share);
            holder.ll_activity_lsit_coupon = (LinearLayout) convertView.findViewById(R.id.ll_activity_lsit_coupon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DecimalFormat df = new DecimalFormat("#0.00");
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
            if (load_type.equals("isVip")) {
                holder.ll_activity_lsit_coupon.setVisibility(View.INVISIBLE);
                holder.tv_rush_ware_second_coupons.setVisibility(View.GONE);
            } else {
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

            if (holder.ll_activity_lsit_coupon != null) {
                holder.ll_activity_lsit_coupon.setVisibility(View.GONE);
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
        return convertView;
    }

    class ViewHolder {
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
        LinearLayout ll_activity_lsit_coupon;
    }

    private OnShareClickListener onShareClickListener;

    public interface OnShareClickListener {
        void onShareClick(View v, int position);
    }

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }
}
