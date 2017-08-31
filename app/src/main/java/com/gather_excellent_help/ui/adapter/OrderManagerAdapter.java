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
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;


public class OrderManagerAdapter extends RecyclerView.Adapter<OrderManagerAdapter.OrderManagerViewHolder> {


    private Context context;
    private LayoutInflater inflater;    //布局填充器

    public OrderManagerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public OrderManagerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = inflater.inflate(R.layout.item_order_content, null);
        return new OrderManagerViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(OrderManagerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class OrderManagerViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_order_top_shop)
        TextView tvOrderTopShop;
        @Bind(R.id.tv_order_top_statue)
        TextView tvOrderTopStatue;
        @Bind(R.id.iv_order_ware_img)
        ImageView ivOrderWareImg;
        @Bind(R.id.tv_order_ware_title)
        TextView tvOrderWareTitle;
        @Bind(R.id.tv_order_ware_type)
        TextView tvOrderWareType;
        @Bind(R.id.tv_order_ware_oldprice)
        TextView tvOrderWareOldprice;
        @Bind(R.id.tv_order_ware_newprice)
        TextView tvOrderWareNewprice;
        @Bind(R.id.tv_order_ware_number)
        TextView tvOrderWareNumber;
        @Bind(R.id.tv_order_center_total)
        TextView tvOrderCenterTotal;
        @Bind(R.id.tv_order_right_first)
        TextView tvOrderRightFirst;
        @Bind(R.id.tv_order_right_second)
        TextView tvOrderRightSecond;
        @Bind(R.id.tv_order_right_third)
        TextView tvOrderRightThird;
        public OrderManagerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
