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
import android.widget.TextView;

import com.gather_excellent_help.bean.ListBean;
import com.gather_excellent_help.utils.DensityUtil;

import java.util.List;

public class WareSelectorAdapter extends RecyclerView.Adapter<WareSelectorAdapter.WareSelectorViewHolder> {

    private Context context;
    private List<ListBean.DataBean> data;

    public WareSelectorAdapter(Context context, List<ListBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public WareSelectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView view = getGenericChildView();
        return new WareSelectorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WareSelectorViewHolder holder, final int position) {
        ListBean.DataBean dataBean = data.get(position);
        String title = dataBean.getTitle();
        holder.tv_title.setText(title);
        holder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class WareSelectorViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title;

        public WareSelectorViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView;
        }
    }

    private TextView getGenericChildView() {

        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setPadding(20, 30, 0, 30);
        textView.setTextSize(DensityUtil.dip2px(context,3));
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
