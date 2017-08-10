package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.NewsTitleBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dapeng Fang on 2017/8/10.
 */

public class NewsHorizaionalTitleAdapter extends RecyclerView.Adapter<NewsHorizaionalTitleAdapter.NewsHorizationalViewHolder> {

    private Context context;
    private List<NewsTitleBean.DataBean> data;

    public NewsHorizaionalTitleAdapter(Context context, List<NewsTitleBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public NewsHorizationalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.item_horizational_text_nav, null);
        return new NewsHorizationalViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(NewsHorizationalViewHolder holder, final int position) {
        final NewsTitleBean.DataBean dataBean = data.get(position);
        boolean check = dataBean.isCheck();
        if(check) {
            holder.tvHorizationalTitle.setTextColor(Color.RED);
            holder.viewLine.setVisibility(View.VISIBLE);
        }else{
            holder.tvHorizationalTitle.setTextColor(Color.GRAY);
            holder.viewLine.setVisibility(View.INVISIBLE);
        }
        if(dataBean.getTitle()!=null) {
            holder.tvHorizationalTitle.setText(dataBean.getTitle());
        }
        if(holder.llHorizationTitle!=null) {
            holder.llHorizationTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i=0;i<data.size();i++){
                        NewsTitleBean.DataBean dataBean1 = data.get(i);
                        if(i==position) {
                            dataBean1.setCheck(true);
                        }else{
                            dataBean1.setCheck(false);
                        }
                    }
                    notifyDataSetChanged();
                    onTitleClickListener.onTitleClick(view,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class NewsHorizationalViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_horizational_title)
        TextView tvHorizationalTitle;
        @Bind(R.id.view_line)
        View viewLine;
        @Bind(R.id.ll_horization_title)
        LinearLayout llHorizationTitle;
        public NewsHorizationalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private OnTitleClickListener onTitleClickListener;

    public interface  OnTitleClickListener {
        void onTitleClick(View v,int position);
    }

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        this.onTitleClickListener = onTitleClickListener;
    }
}
