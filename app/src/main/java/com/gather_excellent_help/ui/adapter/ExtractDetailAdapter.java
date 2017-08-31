package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.AccountDetailBean;
import com.gather_excellent_help.bean.ExractDetailBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dapeng Fang on 2017/7/27.
 */

public class ExtractDetailAdapter extends RecyclerView.Adapter<ExtractDetailAdapter.AccountDetailViewHolder> {

    private Context context;
    private List<ExractDetailBean.DataBean> data;

    public ExtractDetailAdapter(Context context, List<ExractDetailBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public AccountDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.item_account_detail_person, null);
        return new AccountDetailViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(AccountDetailViewHolder holder, int position) {
        ExractDetailBean.DataBean dataBean = data.get(position);
        holder.tv_account_first.setText("订单号:");
        holder.tv_account_second.setText("审核日期:");
        holder.tv_account_third.setText("提取金额:");
        if(dataBean.getWithdrawal_no()!=null) {
            holder.tvAccountDetailTime.setText(dataBean.getWithdrawal_no());
        }
        if(dataBean.getAudit_time()!=null) {
            holder.tvAccountDetailPro.setText(dataBean.getAudit_time());
        }
        holder.tvAccountDetailMoney.setText("￥" + dataBean.getValue());
        int status = dataBean.getStatus();
        if(status == 1) {
            holder.tvAccountDetailSalery.setText("状态:已完成");
        }else if(status == 2) {
            holder.tvAccountDetailSalery.setText("状态:驳回");
        }else if(status == 3) {
            holder.tvAccountDetailSalery.setText("状态:待处理");
        }
        if(dataBean.getRemark()!=null) {
            holder.tvAccountDetailPhone.setText(dataBean.getRemark());
        }
    }

    @Override
    public int getItemCount() {
        return null == data? 0:data.size();
    }

    public class AccountDetailViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_account_detail_time)
        TextView tvAccountDetailTime;
        @Bind(R.id.tv_account_detail_pro)
        TextView tvAccountDetailPro;
        @Bind(R.id.tv_account_detail_money)
        TextView tvAccountDetailMoney;
        @Bind(R.id.tv_account_detail_salery)
        TextView tvAccountDetailSalery;
        @Bind(R.id.tv_account_detail_phone)
        TextView tvAccountDetailPhone;
        @Bind(R.id.tv_account_first)
        TextView tv_account_first;
        @Bind(R.id.tv_account_second)
        TextView tv_account_second;
        @Bind(R.id.tv_account_third)
        TextView tv_account_third;

        public AccountDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
