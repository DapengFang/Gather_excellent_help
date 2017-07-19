package com.gather_excellent_help.ui.adapter;
/**
 * Created by ${} on 2017/7/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gather_excellent_help.R;


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

    public class OrderManagerViewHolder extends RecyclerView.ViewHolder{

        public OrderManagerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
