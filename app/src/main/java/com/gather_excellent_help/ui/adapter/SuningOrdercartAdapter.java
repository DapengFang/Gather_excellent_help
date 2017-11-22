package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.suning.SuningGoodscartBean;
import com.gather_excellent_help.ui.widget.NumberAddSubView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Dapeng Fang on 2017/7/27.
 */

public class SuningOrdercartAdapter extends RecyclerView.Adapter<SuningOrdercartAdapter.SuningOrdecartViewHolder> {

    private Context context;
    private List<SuningGoodscartBean.DataBean>  data;

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
        final DecimalFormat df = new DecimalFormat("#0.00");
        final SuningGoodscartBean.DataBean dataBean = data.get(position);
        String product_mprice = dataBean.getProduct_mprice();
        String product_num = dataBean.getProduct_num();
        String product_sprice = dataBean.getProduct_sprice();
        String product_spec = dataBean.getProduct_spec();
        String product_spec_id = dataBean.getProduct_spec_id();
        String product_pic = dataBean.getProduct_pic();
        String product_title = dataBean.getProduct_title();
        final String product_id = dataBean.getProduct_id();


        if (product_mprice != null) {
            double mprice = Double.parseDouble(product_mprice);
            holder.tv_bottom_pop_cprice.getPaint().setAntiAlias(true);
            holder.tv_bottom_pop_cprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰 
            holder.tv_bottom_pop_cprice.setText("￥"+df.format(mprice));
        }

        holder.tv_activity_sun_tao_icon.setSelected(true);
        if (product_pic != null) {
            String replace_img = product_pic.replace("800x800", "400x400");
            Glide.with(context).load(replace_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                    .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                    .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                    .into(holder.iv_bottom_pop_img);//请求成功后把图片设置到的控件
        }
        if (product_title != null) {
            holder.tv_bottom_pop_name.setText("\t\t\t\t\t\t"+product_title);
        }
        if (product_sprice != null) {
            double sprice = Double.parseDouble(product_sprice);
            holder.tv_bottom_pop_goodprice.setText("￥" + df.format(sprice));
        }


        if(product_num!=null) {
            int num = Integer.parseInt(product_num);
            holder.tv_bottom_pop_goods_num.setText("x" + num);
            holder.nas_order_confirm_num.setValue(num);
        }

        holder.nas_order_confirm_num.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onSubButton(View view, int value) {
                String goods_num = String.valueOf(value);
                holder.tv_bottom_pop_goods_num.setText("x" + value);
                dataBean.setProduct_num(goods_num);
                double totalPrice = getTotalPrice();
                onNumButtonListener.onSubClick(view,position,product_id,goods_num,df.format(totalPrice));
            }

            @Override
            public void onAddButton(View view, int value) {
                String goods_num = String.valueOf(value);
                holder.tv_bottom_pop_goods_num.setText("x" + value);
                dataBean.setProduct_num(goods_num);
                double totalPrice = getTotalPrice();
                onNumButtonListener.onSubClick(view,position,product_id,goods_num,df.format(totalPrice));
            }
        });
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

        public SuningOrdecartViewHolder(View itemView) {
            super(itemView);
            iv_bottom_pop_img = (ImageView) itemView.findViewById(R.id.iv_bottom_pop_img);
            tv_bottom_pop_name = (TextView) itemView.findViewById(R.id.tv_bottom_pop_name);
            tv_bottom_pop_goodprice = (TextView) itemView.findViewById(R.id.tv_bottom_pop_goodprice);
            tv_bottom_pop_cprice = (TextView) itemView.findViewById(R.id.tv_bottom_pop_cprice);
            tv_bottom_pop_goods_num = (TextView) itemView.findViewById(R.id.tv_bottom_pop_goods_num);
            tv_activity_sun_tao_icon = (TextView) itemView.findViewById(R.id.tv_activity_sun_tao_icon);
            nas_order_confirm_num = (NumberAddSubView) itemView.findViewById(R.id.nas_order_confirm_num);
        }
    }

    /**
     * 获取商品总价格
     */
    public double getTotalPrice(){
        double total_price = 0;
        for (int i=0;i<data.size();i++){
            SuningGoodscartBean.DataBean dataBean = data.get(i);
            String product_sprice = dataBean.getProduct_sprice();
            String product_num = dataBean.getProduct_num();
            double s_price = Double.parseDouble(product_sprice);
            int num = Integer.parseInt(product_num);
            total_price += s_price * num;
        }
        return total_price;
    }


    private OnNumButtonListener onNumButtonListener;

    public interface OnNumButtonListener{
        void onAddClick(View v,int position,String product_id,String num,String totalprice);
        void onSubClick(View v,int position,String product_id,String num,String totalprice);
    }

    public void setOnNumButtonListener(OnNumButtonListener onNumButtonListener) {
        this.onNumButtonListener = onNumButtonListener;
    }
}
