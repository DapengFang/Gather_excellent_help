package com.gather_excellent_help.ui.adapter;
/**
 * Created by ${} on 2017/7/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.OrderAllBean;
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class OrderAllAdapter extends RecyclerView.Adapter<OrderAllAdapter.OrderManagerViewHolder> {


    private Context context;
    private LayoutInflater inflater;    //布局填充器
    private ImageLoader mImageLoader;
    private List<OrderAllBean.DataBean> allData;

    public OrderAllAdapter(Context context,List<OrderAllBean.DataBean> allData) {
        this.context = context;
        this.allData = allData;
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
        holder.tvOrderAllTime.setText("下单时间:" + dataBean.getCreate_time());
        holder.tvOrderAllPrice.setText("订单金额:" + dataBean.getPrice());
        holder.tvOrderAllNumber.setText("订单号:" + dataBean.getTrade_id());
    }

    @Override
    public int getItemCount() {
        return allData==null? 0 : allData.size();
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
        public OrderManagerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
