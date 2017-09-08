package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.LowerMermberBean;
import com.gather_excellent_help.bean.WardStaticsBean;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dapeng Fang on 2017/8/4.
 */

public class LowerMemberAdapter extends RecyclerView.Adapter<LowerMemberAdapter.LowerMemberViewHolder> {

    private Context context;
    private List<LowerMermberBean.DataBean> lowerData;

    public LowerMemberAdapter(Context context, List<LowerMermberBean.DataBean> lowerData) {
        this.context = context;
        this.lowerData = lowerData;
    }

    @Override
    public LowerMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.lower_member_item, null);
        return new LowerMemberViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(LowerMemberViewHolder holder, int position) {
        LowerMermberBean.DataBean dataBean = lowerData.get(position);
        DecimalFormat df = new DecimalFormat("#0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        if(holder.tvTongjiName!=null && dataBean.getUsers_name()!=null) {
            holder.tvTongjiName.setText(dataBean.getUsers_name());
        }
       if(holder.tvTongjiNumber!=null) {
           holder.tvTongjiNumber.setText(dataBean.getCount()+"");
       }
        if(holder.tvWardCredits!=null) {
            holder.tvWardCredits.setText("￥"+df.format(dataBean.getDeal_total()));
        }
        if(holder.tvLowerMoney!=null) {
            holder.tvLowerMoney.setText("￥"+df.format(dataBean.getTotal()));
        }
    }

    @Override
    public int getItemCount() {
        return null == lowerData? 0:lowerData.size();
    }

    public class LowerMemberViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_tongji_name)
        TextView tvTongjiName;
        @Bind(R.id.tv_tongji_number)
        TextView tvTongjiNumber;
        @Bind(R.id.tv_ward_credits)
        TextView tvWardCredits;
        @Bind(R.id.tv_lower_money)
        TextView tvLowerMoney;
        public LowerMemberViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
