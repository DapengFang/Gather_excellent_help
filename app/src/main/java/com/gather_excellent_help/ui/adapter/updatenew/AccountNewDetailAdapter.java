package com.gather_excellent_help.ui.adapter.updatenew;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.AccountDetailBean;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Dapeng Fang on 2018/1/22.
 */

public class AccountNewDetailAdapter extends RecyclerView.Adapter<AccountNewDetailAdapter.AcccountNewDetailViewHolder> {

    private Context context;
    private List<AccountDetailBean.DataBean> data;

    public AccountNewDetailAdapter(Context context, List<AccountDetailBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public AcccountNewDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.account_new_detail_page, null);
        return new AcccountNewDetailViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(AcccountNewDetailViewHolder holder, final int position) {
        DecimalFormat df = new DecimalFormat("#0.00");
        AccountDetailBean.DataBean dataBean = data.get(position);
        String add_time = dataBean.getAdd_time();
        double value = dataBean.getValue();
        String project = dataBean.getProject();
        holder.tv_new_detail_time.setText(add_time);
        holder.tv_new_detail_project.setText(project);
        holder.tv_new_detail_money.setText(df.format(value));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    public class AcccountNewDetailViewHolder extends RecyclerView.ViewHolder {

        TextView tv_new_detail_time;
        TextView tv_new_detail_project;
        TextView tv_new_detail_money;

        public AcccountNewDetailViewHolder(View itemView) {
            super(itemView);
            tv_new_detail_time = (TextView) itemView.findViewById(R.id.tv_new_detail_time);
            tv_new_detail_project = (TextView) itemView.findViewById(R.id.tv_new_detail_project);
            tv_new_detail_money = (TextView) itemView.findViewById(R.id.tv_new_detail_money);
        }
    }

    /**
     * 每一条item的点击事件
     */
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
