package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.AccountDetailBean;
import com.gather_excellent_help.utils.imageutils.ImageLoader;

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
        if(dataBean.getAdd_time()!=null) {
            holder.tvAccountDetailTime.setText("日期:"+dataBean.getAdd_time());
        }
        if(dataBean.getProject()!=null) {
            holder.tvAccountDetailPro.setText("项目:"+dataBean.getProject());
        }
        holder.tvAccountDetailMoney.setText("发生金额:￥" + dataBean.getValue());
        holder.tvAccountDetailSalery.setText("余额:"+dataBean.getAmount());
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

        public AccountDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
