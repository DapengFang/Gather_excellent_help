package com.gather_excellent_help.ui.adapter.soutao;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.soutao.SouTaobaoHelpBean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2018/1/18.
 */

public class SouTaobaoHelpAdapter extends RecyclerView.Adapter<SouTaobaoHelpAdapter.SouTaobaoHelpViewHolder> {

    private Context context;
    private List<SouTaobaoHelpBean> data;

    public SouTaobaoHelpAdapter(Context context, List<SouTaobaoHelpBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public SouTaobaoHelpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SouTaobaoHelpViewHolder(View.inflate(context, R.layout.sou_taobao_help_title, null));
    }

    @Override
    public void onBindViewHolder(final SouTaobaoHelpViewHolder holder, int position) {
        final SouTaobaoHelpBean souTaobaoHelpBean = data.get(position);
        String f_title = souTaobaoHelpBean.getF_title();
        String s_title = souTaobaoHelpBean.getS_title();
        final int hind = souTaobaoHelpBean.getHind();
        if (hind == 0) {
            holder.iv_taobao_help_arraw.setImageResource(R.drawable.sou_taobao_down_arraw);
            holder.tv_taobao_help_answer.setVisibility(View.GONE);
        } else if (hind == 1) {
            holder.iv_taobao_help_arraw.setImageResource(R.drawable.sou_taobao_up_arraw);
            holder.tv_taobao_help_answer.setVisibility(View.VISIBLE);
        }
        holder.tv_taobao_help_ask.setText(f_title);
        holder.tv_taobao_help_answer.setText(s_title);
        holder.rl_sou_taobao_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int chind = souTaobaoHelpBean.getHind();
                if (chind == 0) {
                    souTaobaoHelpBean.setHind(1);
                    holder.tv_taobao_help_answer.setVisibility(View.VISIBLE);
                    holder.iv_taobao_help_arraw.setImageResource(R.drawable.sou_taobao_up_arraw);
                } else if (chind == 1) {
                    souTaobaoHelpBean.setHind(0);
                    holder.iv_taobao_help_arraw.setImageResource(R.drawable.sou_taobao_down_arraw);
                    holder.tv_taobao_help_answer.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    public class SouTaobaoHelpViewHolder extends RecyclerView.ViewHolder {

        TextView tv_taobao_help_ask;
        ImageView iv_taobao_help_arraw;
        TextView tv_taobao_help_answer;
        RelativeLayout rl_sou_taobao_ask;

        public SouTaobaoHelpViewHolder(View itemView) {
            super(itemView);
            tv_taobao_help_ask = (TextView) itemView.findViewById(R.id.tv_taobao_help_ask);
            iv_taobao_help_arraw = (ImageView) itemView.findViewById(R.id.iv_taobao_help_arraw);
            tv_taobao_help_answer = (TextView) itemView.findViewById(R.id.tv_taobao_help_answer);
            rl_sou_taobao_ask = (RelativeLayout) itemView.findViewById(R.id.rl_sou_taobao_ask);
        }
    }
}
