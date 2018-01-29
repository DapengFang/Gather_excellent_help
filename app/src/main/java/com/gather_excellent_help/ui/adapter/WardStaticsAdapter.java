package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.WardStaticsBean;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dapeng Fang on 2017/8/4.
 */

public class WardStaticsAdapter extends RecyclerView.Adapter<WardStaticsAdapter.WardStaticsViewHolder> {

    private Context context;
    private List<WardStaticsBean.DataBean> wardsData;

    public WardStaticsAdapter(Context context, List<WardStaticsBean.DataBean> wardsData) {
        this.context = context;
        this.wardsData = wardsData;
    }

    @Override
    public WardStaticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.ward_statics_item, null);
        return new WardStaticsViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(WardStaticsViewHolder holder, int position) {
        WardStaticsBean.DataBean dataBean = wardsData.get(position);
        DecimalFormat df = new DecimalFormat("#0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        if(holder.tvTongjiName!=null && dataBean.getUsers_name()!=null) {
            holder.tvTongjiName.setText(dataBean.getUsers_name());
        }
       if(holder.tvTongjiNumber!=null) {
           holder.tvTongjiNumber.setText(dataBean.getCount()+"");
       }
        if(holder.tvWardCredits!=null) {
            holder.tvWardCredits.setText(" ¥"+df.format(dataBean.getTotal()));
        }
    }

    @Override
    public int getItemCount() {
        return null == wardsData? 0:wardsData.size();
    }

    public class WardStaticsViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_tongji_name)
        TextView tvTongjiName;
        @Bind(R.id.tv_tongji_number)
        TextView tvTongjiNumber;
        @Bind(R.id.tv_ward_credits)
        TextView tvWardCredits;
        public WardStaticsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
