package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.suning.SuningSpecBean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/7/27.
 */

public class SuningSpecDetailAdapter extends RecyclerView.Adapter<SuningSpecDetailAdapter.SuningSpecDetailViewHolder> {

    private Context context;
    private List<SuningSpecBean.DataBean.ContentBean> data;

    public SuningSpecDetailAdapter(Context context, List<SuningSpecBean.DataBean.ContentBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public SuningSpecDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_spec_second, parent, false);//解决宽度不能铺满  
        return new SuningSpecDetailViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SuningSpecDetailViewHolder holder, final int position) {
        SuningSpecBean.DataBean.ContentBean contentBean = data.get(position);
        String title = contentBean.getTitle();
        holder.tv_spec_detial.setText(title);
        if (contentBean.isCheck()) {
            holder.tv_spec_detial.setSelected(true);
        } else {
            holder.tv_spec_detial.setSelected(false);
        }
        holder.tv_spec_detial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSecondItemClickListenre.onSecondItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    public class SuningSpecDetailViewHolder extends RecyclerView.ViewHolder {

        TextView tv_spec_detial;


        public SuningSpecDetailViewHolder(View itemView) {
            super(itemView);
            tv_spec_detial = (TextView) itemView.findViewById(R.id.tv_spec_detial);
        }
    }

    /**
     * 点击规格选项的监听
     */
    private OnSecondItemClickListenre onSecondItemClickListenre;

    public interface OnSecondItemClickListenre {
        void onSecondItemClick(View v, int position);
    }

    public void setOnSecondItemClickListenre(OnSecondItemClickListenre onSecondItemClickListenre) {
        this.onSecondItemClickListenre = onSecondItemClickListenre;
    }
}
