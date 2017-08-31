package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.AccountDetailBean;
import com.gather_excellent_help.bean.BackRebateBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dapeng Fang on 2017/7/27.
 */

public class BackRebateAdapter extends RecyclerView.Adapter<BackRebateAdapter.AccountDetailViewHolder> {

    private Context context;
    private List<BackRebateBean.DataBean> data;

    public BackRebateAdapter(Context context, List<BackRebateBean.DataBean> data) {
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
        BackRebateBean.DataBean dataBean = data.get(position);
        holder.tv_account_first.setText("返佣结算号:");
        holder.tv_account_second.setText("审核日期:");
        holder.tv_account_third.setText("当前状态:");
        holder.tvAccountBackShow.setVisibility(View.GONE);
        if(dataBean.getRebate_no()!=null) {
            holder.tvAccountDetailTime.setText(dataBean.getRebate_no());
        }
        if(dataBean.getAudit_time()!=null) {
            holder.tvAccountDetailPro.setText(dataBean.getAudit_time());
        }
        int status = dataBean.getStatus();
        if(status==1) {
            holder.tvAccountDetailMoney.setText("通过" );
        }else if(status ==2) {
            holder.tvAccountDetailMoney.setText("驳回" );
        }else if(status ==3) {
            holder.tvAccountDetailMoney.setText("待处理" );
        }else{
            holder.tvAccountDetailMoney.setText("已完成" );
        }

        holder.tvAccountDetailSalery.setText("返佣:￥"+dataBean.getValue()+"现金");
        if(dataBean.getRemark()!=null) {
            String remark = dataBean.getRemark();
            if(TextUtils.isEmpty(remark)) {
                holder.tvAccountDetailPhone.setText("无");
            }else{
                holder.tvAccountDetailPhone.setText(dataBean.getRemark());
            }

        }
    }

    @Override
    public int getItemCount() {
        return data == null? 0:data.size();
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
        @Bind(R.id.tv_account_back_show)
        TextView tvAccountBackShow;
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
