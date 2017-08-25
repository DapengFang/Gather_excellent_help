package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.QiangTimeBean;

import java.util.ArrayList;

/**
 * Created by Dapeng Fang on 2017/8/4.
 */

public class TaoQiangTimeAdapter extends RecyclerView.Adapter<TaoQiangTimeAdapter.TaoQiangViewHolder> {

    private Context context;
    private ArrayList<QiangTimeBean> data;

    public TaoQiangTimeAdapter(Context context, ArrayList<QiangTimeBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public TaoQiangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.item_qiang_time, null);
        return new TaoQiangViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(TaoQiangViewHolder holder, final int position) {
        QiangTimeBean qiangTimeBean = data.get(position);
        boolean check = qiangTimeBean.isCheck();
        if(check) {
            holder.ll_qiang_tao_show.setBackgroundColor(Color.parseColor("#fa2d3b"));
            holder.tv_qiang_tao_qiang.setTextColor(Color.WHITE);
            holder.tv_qiang_tao_nav.setTextColor(Color.WHITE);
        }else{
            holder.ll_qiang_tao_show.setBackgroundColor(Color.parseColor("#31353b"));
            holder.tv_qiang_tao_qiang.setTextColor(Color.parseColor("#9d9d9d"));
            holder.tv_qiang_tao_nav.setTextColor(Color.parseColor("#9d9d9d"));
        }
        int time = qiangTimeBean.getTime();
        if(time<10) {
            holder.tv_qiang_tao_nav.setText("0"+time+":00");
        }else{
            holder.tv_qiang_tao_nav.setText(time+":00");
        }
        if(position == 0) {
            holder.tv_qiang_tao_qiang.setText("抢购进行中");
        }else{
            holder.tv_qiang_tao_qiang.setText("即将开抢");
        }
        holder.ll_qiang_tao_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemclickListener.onItemClick(view,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return null == data? 0:data.size();
    }

    public class TaoQiangViewHolder extends RecyclerView.ViewHolder{

        TextView tv_qiang_tao_nav;
        TextView tv_qiang_tao_qiang;
        LinearLayout ll_qiang_tao_show;

        public TaoQiangViewHolder(View itemView) {
            super(itemView);
            tv_qiang_tao_nav = (TextView) itemView.findViewById(R.id.tv_qiang_tao_nav);
            tv_qiang_tao_qiang = (TextView) itemView.findViewById(R.id.tv_qiang_tao_qiang);
            ll_qiang_tao_show = (LinearLayout) itemView.findViewById(R.id.ll_qiang_tao_show);
        }
    }
    private OnItemClickListener onItemclickListener;

    public interface OnItemClickListener{
        void onItemClick(View v,int position);
    }

    public void setOnItemclickListener(OnItemClickListener onItemclickListener) {
        this.onItemclickListener = onItemclickListener;
    }
}
