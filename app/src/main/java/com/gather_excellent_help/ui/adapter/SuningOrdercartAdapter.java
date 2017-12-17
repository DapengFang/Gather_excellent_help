package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.suning.SuningGoodscartBean;
import com.gather_excellent_help.ui.activity.suning.SuningDetailActivity;
import com.gather_excellent_help.ui.widget.NumberAddSubView;
import com.gather_excellent_help.utils.LogUtil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Dapeng Fang on 2017/7/27.
 */

public class SuningOrdercartAdapter extends RecyclerView.Adapter<SuningOrdercartAdapter.SuningOrdecartViewHolder> {

    private Context context;
    private List<SuningGoodscartBean.DataBean> data;

    public SuningOrdercartAdapter(Context context, List<SuningGoodscartBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public SuningOrdecartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suning_cart_order_confirm, null, false);
        return new SuningOrdecartViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final SuningOrdecartViewHolder holder, final int position) {
        try {
            final DecimalFormat df = new DecimalFormat("#0.00");
            final SuningGoodscartBean.DataBean dataBean = data.get(position);
            final String product_mprice = dataBean.getProduct_mprice();
            String product_num = dataBean.getProduct_num();
            final String product_sprice = dataBean.getProduct_sprice();
            String product_spec = dataBean.getProduct_spec();
            String product_spec_id = dataBean.getProduct_spec_id();
            final String product_pic = dataBean.getProduct_pic();
            final String product_title = dataBean.getProduct_title();
            final String product_id = dataBean.getProduct_id();
            final String product_goodsid = dataBean.getProduct_goodsid();

            if (product_mprice != null) {
                double mprice = Double.parseDouble(product_mprice);
                holder.tv_bottom_pop_cprice.getPaint().setAntiAlias(true);
                holder.tv_bottom_pop_cprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰 
                holder.tv_bottom_pop_cprice.setText("￥" + df.format(mprice));
            }

            if(product_spec !=null) {
                if(product_spec.length()>10) {
                    String s = product_spec.substring(0, 10) + "...";
                    holder.tv_activity_suning_warespec.setText(s);
                }else{
                    holder.tv_activity_suning_warespec.setText(product_spec);
                }
            }

            if (product_pic != null) {
                String replace_img = product_pic.replace("800x800", "400x400");
                Glide.with(context).load(replace_img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                        .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                        .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                        .into(holder.iv_bottom_pop_img);//请求成功后把图片设置到的控件
            }
            if (product_title != null) {
                SpannableString span = new SpannableString("\t\t" + product_title);
                ImageSpan image = new ImageSpan(context, R.drawable.suning_ziying_icon, DynamicDrawableSpan.ALIGN_BASELINE);
                span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tv_bottom_pop_name.setText(span);
            }
            if (product_sprice != null) {
                double sprice = Double.parseDouble(product_sprice);
                holder.tv_bottom_pop_goodprice.setText("￥" + df.format(sprice));
            }

            if (product_num != null) {
                int num = Integer.parseInt(product_num);
                holder.tv_bottom_pop_goods_num.setText("x" + num);
                holder.nas_order_confirm_num.setValue(num);
            }

            holder.nas_order_confirm_num.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
                @Override
                public void onSubButton(View view, int value) {
                    onNumAddSubListener.onSubClick(view, position, value);
                }

                @Override
                public void onAddButton(View view, int value) {
                    onNumAddSubListener.onAddClick(view, position, value);
                }
            });
            holder.rl_order_cart_ware.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SuningDetailActivity.class);
                    intent.putExtra("article_id",Integer.parseInt(product_id));
                    intent.putExtra("goods_id", product_goodsid);
                    intent.putExtra("goods_img", product_pic);
                    intent.putExtra("goods_title", product_title);
                    intent.putExtra("goods_price", product_sprice);
                    intent.putExtra("c_price", product_mprice);
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            LogUtil.e("SuningOrdercartAdapter error");
            Toast.makeText(context, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    public class SuningOrdecartViewHolder extends RecyclerView.ViewHolder {

        //商品信息相关
        ImageView iv_bottom_pop_img;
        TextView tv_bottom_pop_name;
        TextView tv_bottom_pop_goodprice;
        TextView tv_bottom_pop_cprice;
        TextView tv_bottom_pop_goods_num;
        TextView tv_activity_sun_tao_icon;
        NumberAddSubView nas_order_confirm_num;
        RelativeLayout rl_order_cart_ware;
        TextView tv_activity_suning_warespec;

        public SuningOrdecartViewHolder(View itemView) {
            super(itemView);
            iv_bottom_pop_img = (ImageView) itemView.findViewById(R.id.iv_bottom_pop_img);
            tv_bottom_pop_name = (TextView) itemView.findViewById(R.id.tv_bottom_pop_name);
            tv_bottom_pop_goodprice = (TextView) itemView.findViewById(R.id.tv_bottom_pop_goodprice);
            tv_bottom_pop_cprice = (TextView) itemView.findViewById(R.id.tv_bottom_pop_cprice);
            tv_bottom_pop_goods_num = (TextView) itemView.findViewById(R.id.tv_bottom_pop_goods_num);
            tv_activity_sun_tao_icon = (TextView) itemView.findViewById(R.id.tv_activity_sun_tao_icon);
            nas_order_confirm_num = (NumberAddSubView) itemView.findViewById(R.id.nas_order_confirm_num);
            rl_order_cart_ware = (RelativeLayout) itemView.findViewById(R.id.rl_order_cart_ware);
            tv_activity_suning_warespec = (TextView) itemView.findViewById(R.id.tv_activity_suning_warespec);
        }
    }

    /**
     * 获取商品总价格
     */
    public double getTotalPrice() {
        double total_price = 0;
        for (int i = 0; i < data.size(); i++) {
            SuningGoodscartBean.DataBean dataBean = data.get(i);
            String product_sprice = dataBean.getProduct_sprice();
            String product_num = dataBean.getProduct_num();
            double s_price = Double.parseDouble(product_sprice);
            int num = Integer.parseInt(product_num);
            total_price += s_price * num;
        }
        return total_price;
    }



    private OnNumAddSubListener onNumAddSubListener;

    public interface OnNumAddSubListener {
        void onAddClick(View v, int position, int value);

        void onSubClick(View v, int position, int value);
    }

    public void setOnNumAddSubListener(OnNumAddSubListener onNumAddSubListener) {
        this.onNumAddSubListener = onNumAddSubListener;
    }
}
