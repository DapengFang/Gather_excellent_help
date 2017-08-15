package com.gather_excellent_help.ui.adapter;
/**
 * Created by ${} on 2017/7/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.OrderAllBean;
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class OrderAllAdapter extends RecyclerView.Adapter<OrderAllAdapter.OrderManagerViewHolder> {


    private Context context;
    private LayoutInflater inflater;    //布局填充器
    private ImageLoader mImageLoader;
    private List<OrderAllBean.DataBean> allData;
    private int curr_statue;
    private int order_type;

    public OrderAllAdapter(Context context, List<OrderAllBean.DataBean> allData, int curr_statue,int order_type) {
        this.context = context;
        this.allData = allData;
        this.curr_statue = curr_statue;
        this.order_type = order_type;
        inflater = LayoutInflater.from(context);
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    @Override
    public OrderManagerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = inflater.inflate(R.layout.item_all_order, null);
        return new OrderManagerViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(OrderManagerViewHolder holder, int position) {
        OrderAllBean.DataBean dataBean = allData.get(position);
        DecimalFormat df = new DecimalFormat("#0.00");
        holder.tvOrderAllTime.setText("下单时间:" + dataBean.getCreate_time());
        holder.tvOrderAllPrice.setText("订单金额:￥" + dataBean.getPrice());
        holder.tvOrderAllNumber.setText("订单号:" + dataBean.getTrade_id());
        if(order_type == -1) {
            holder.tv_order_zhuan.setVisibility(View.GONE);
        }else if(order_type == -2) {
            holder.tv_order_zhuan.setVisibility(View.VISIBLE);
            holder.tv_order_zhuan.setText("赚:￥" + df.format(dataBean.getAmount()));
        }else{
            holder.tv_order_zhuan.setVisibility(View.GONE);
        }
        String goodsImg = dataBean.getGoodsImg();
        String tk_status = dataBean.getTk_status();
        if(goodsImg!=null && !TextUtils.isEmpty(goodsImg)) {
            mImageLoader.loadImage(goodsImg,holder.ivOrderAllImg,true);
        }
        if(curr_statue == 1) {
            holder.tvOrderAllStatue.setText("待付款");
        }else if(curr_statue == 2){
            holder.tvOrderAllStatue.setText("已付款");
        }else if(curr_statue == 3) {
            holder.tvOrderAllStatue.setText("已完成");
        }else if(curr_statue == 4) {
            holder.tvOrderAllStatue.setText("退款/售后");
        }else {
            if(tk_status.equals("3")) {
                holder.tvOrderAllStatue.setText("待付款");
            }
            if(tk_status.equals("12")) {
                holder.tvOrderAllStatue.setText("已付款");
            }
            if(tk_status.equals("14")) {
                holder.tvOrderAllStatue.setText("已完成");
            }
            if(tk_status.equals("13")) {
                holder.tvOrderAllStatue.setText("退款/售后");
            }
        }

    }

    @Override
    public int getItemCount() {
        return null==allData? 0 : allData.size();
    }

    public class OrderManagerViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_order_all_img)
        ImageView ivOrderAllImg;
        @Bind(R.id.tv_order_all_number)
        TextView tvOrderAllNumber;
        @Bind(R.id.tv_order_all_price)
        TextView tvOrderAllPrice;
        @Bind(R.id.tv_order_all_time)
        TextView tvOrderAllTime;
        @Bind(R.id.tv_order_all_statue)
        TextView tvOrderAllStatue;
        @Bind(R.id.tv_order_all_seedetail)
        TextView tvOrderAllSeedetail;
        @Bind(R.id.tv_order_zhuan)
        TextView tv_order_zhuan;
        public OrderManagerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
