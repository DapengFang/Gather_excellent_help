package com.gather_excellent_help.ui.adapter;
/**
 * Created by ${} on 2017/7/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.ListBean;
import com.gather_excellent_help.utils.DensityUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WareSelectorAdapter extends RecyclerView.Adapter<WareSelectorAdapter.WareSelectorViewHolder> {

    private Context context;
    private List<ListBean.DataBean> data;

    public WareSelectorAdapter(Context context, List<ListBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public WareSelectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  inflate = View.inflate(context, R.layout.item_tabao_shaixuan_city, null);
        return new WareSelectorViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(WareSelectorViewHolder holder, final int position) {
        ListBean.DataBean dataBean = data.get(position);
        String title = dataBean.getTitle();
        holder.tvTaobaoShaixuanText.setText(title);
        holder.tvTaobaoShaixuanText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == data ? 0:data.size();
    }

    public class WareSelectorViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_taobao_shaixuan_text)
        TextView tvTaobaoShaixuanText;

        public WareSelectorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
