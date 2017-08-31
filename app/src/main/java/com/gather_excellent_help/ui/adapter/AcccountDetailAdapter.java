package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.AccountDetailBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dapeng Fang on 2017/7/27.
 */

public class AcccountDetailAdapter extends RecyclerView.Adapter<AcccountDetailAdapter.AccountDetailViewHolder> {

    private Context context;
    private List<AccountDetailBean.DataBean> data;

    public AcccountDetailAdapter(Context context, List<AccountDetailBean.DataBean> data) {
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
        AccountDetailBean.DataBean dataBean = data.get(position);
        holder.tv_account_first.setText("日期:");
        holder.tv_account_second.setText("项目:");
        holder.tv_account_third.setText("发生金额:");
        if(dataBean.getAdd_time()!=null) {
            holder.tvAccountDetailTime.setText(dataBean.getAdd_time());
        }
        if(dataBean.getProject()!=null) {
            holder.tvAccountDetailPro.setText(dataBean.getProject());
        }
        holder.tvAccountDetailMoney.setText("￥" + dataBean.getValue());
        holder.tvAccountDetailSalery.setText("余额:￥"+dataBean.getAmount());
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
