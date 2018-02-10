package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import com.gather_excellent_help.bean.SearchTaobaoBean;
import com.gather_excellent_help.bean.SearchWareBean;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wuxin on 2017/7/12.
 */

public class TaobaoWareListAdapter extends BaseAdapter {

    private Context context;
    private List<SearchTaobaoBean.DataBean> data;
    private LayoutInflater inflater;    //布局填充器
    private double user_rate;
    private int shopType;

    public TaobaoWareListAdapter(Context context, List<SearchTaobaoBean.DataBean> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        shopType = Tools.getShopType(context);
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
        SearchTaobaoBean.DataBean dataBean = data.get(position);
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
        if (holder.iv_activity_list_vip != null) {
            holder.iv_activity_list_vip.setVisibility(View.GONE);
        }
        if (holder.rl_activity_rexiao_share != null) {
            holder.rl_activity_rexiao_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareClickListener.onShareClick(v, position);
                }
            });
        }
        if (holder.tv_rush_ware_second_coupons != null) {
            holder.tv_rush_ware_second_coupons.setVisibility(View.GONE);
        }
        if (dataBean.getTitle() != null) {
            holder.home_type_name.setText(dataBean.getTitle());
        }
        if (holder.home_type_photo != null && dataBean.getImg_url() != null) {
            Glide.with(context).load(dataBean.getImg_url() + "_430x430q90.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                    .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                    .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                    .into(holder.home_type_photo);//请求成功后把图片设置到的控件
        }
        //holder.tv_home_type_aprice.setText("¥" + dataBean.getSell_price());

        String price_content = " ¥" + dataBean.getSell_price();

        SpannableString spannableString_l01 = new SpannableString(price_content);
        RelativeSizeSpan sizeSpan_01 = new RelativeSizeSpan(0.8f);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#fa2a3b"));
        spannableString_l01.setSpan(colorSpan, 0, spannableString_l01.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        spannableString_l01.setSpan(sizeSpan_01, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.tv_home_type_aprice.setText(spannableString_l01);

        final SearchTaobaoBean.DataBean.CouponInfoBean coupon_info = dataBean.getCoupon_info();
        if (coupon_info != null) {
            String coupon_info1 = coupon_info.getCoupon_info();
            int coupons = 0;
            if (coupon_info1 != null && !TextUtils.isEmpty(coupon_info1)) {
                int index = coupon_info1.indexOf("减") + 1;
                String coupon = coupon_info1.substring(index, coupon_info1.length() - 1);
                coupons = Integer.parseInt(coupon);
                holder.tv_rush_ware_coupons.setText("" + coupon);
                holder.ll_activity_lsit_coupon.setVisibility(View.VISIBLE);
            } else {
                holder.ll_activity_lsit_coupon.setVisibility(View.INVISIBLE);
            }
            double maxCommissionRate = Double.parseDouble(coupon_info.getMax_commission_rate());
            double sellPrice = Double.parseDouble(dataBean.getSell_price());
            double zhuan = (sellPrice - coupons) * (maxCommissionRate / 100) * 0.9f * user_rate;
            DecimalFormat df = new DecimalFormat("#0.00");
            double coast = sellPrice - zhuan - coupons;
            holder.tv_home_type_sale.setText("赚 " + df.format(zhuan));
            holder.tv_home_type_coast.setText("到手价 " + df.format(coast));
            holder.ll_activity_lsit_coupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
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
        } else {
            holder.ll_activity_lsit_coupon.setVisibility(View.INVISIBLE);
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
        TextView tv_rush_ware_second_coupons;       //优惠券
        LinearLayout ll_activity_list_ware_zhuan;   //显示隐藏赚
        ImageView iv_activity_list_vip;   //专享价
        RelativeLayout rl_activity_rexiao_share;   //分享
        LinearLayout ll_activity_lsit_coupon;      //优惠券的container
    }

    private OnShareClickListener onShareClickListener;

    public interface OnShareClickListener {
        void onShareClick(View v, int position);
    }

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }
}
