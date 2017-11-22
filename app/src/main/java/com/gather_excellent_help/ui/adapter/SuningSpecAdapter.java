package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.BackRebateBean;
import com.gather_excellent_help.bean.suning.SuningSpecBean;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dapeng Fang on 2017/7/27.
 */

public class SuningSpecAdapter extends RecyclerView.Adapter<SuningSpecAdapter.SuningSpecViewHolder> {

    private Context context;
    private List<SuningSpecBean.DataBean> data;

    public SuningSpecAdapter(Context context, List<SuningSpecBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public SuningSpecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_spec_first, null, false);
        return new SuningSpecViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SuningSpecViewHolder holder, int position) {
        SuningSpecBean.DataBean dataBean = data.get(position);
        String title = dataBean.getTitle();
        final List<SuningSpecBean.DataBean.ContentBean> content = dataBean.getContent();
        holder.tv_spec_title.setText(title);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        holder.rcv_first_item_spce.setLayoutManager(gridLayoutManager);
        final SuningSpecDetailAdapter suningSpecDetailAdapter = new SuningSpecDetailAdapter(context, content);
        holder.rcv_first_item_spce.setAdapter(suningSpecDetailAdapter);
        suningSpecDetailAdapter.setOnSecondItemClickListenre(new SuningSpecDetailAdapter.OnSecondItemClickListenre() {
            @Override
            public void onSecondItemClick(View v, int pos) {
                for (int i = 0; i < content.size(); i++) {
                    if (pos != i) {
                        SuningSpecBean.DataBean.ContentBean contentBean = content.get(i);
                        contentBean.setCheck(false);
                    }
                }
                SuningSpecBean.DataBean.ContentBean contentBean = content.get(pos);
                contentBean.setCheck(true);
                suningSpecDetailAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    public class SuningSpecViewHolder extends RecyclerView.ViewHolder {

        TextView tv_spec_title;
        RecyclerView rcv_first_item_spce;

        public SuningSpecViewHolder(View itemView) {
            super(itemView);
            tv_spec_title = (TextView) itemView.findViewById(R.id.tv_spec_title);
            rcv_first_item_spce = (RecyclerView) itemView.findViewById(R.id.rcv_first_item_spce);
        }
    }
}
