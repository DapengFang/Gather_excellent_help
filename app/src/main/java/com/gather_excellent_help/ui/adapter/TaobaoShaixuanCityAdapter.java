package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.CityBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dapeng Fang on 2017/7/28.
 */

public class TaobaoShaixuanCityAdapter extends RecyclerView.Adapter<TaobaoShaixuanCityAdapter.TaobaoShaixuanViewHolder> {
    private Context context;
    private List<CityBean.DataBean> data;

    public TaobaoShaixuanCityAdapter(Context context, List<CityBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public TaobaoShaixuanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.item_tabao_shaixuan_city, null);
        return new TaobaoShaixuanViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(TaobaoShaixuanViewHolder holder, int position) {
        final CityBean.DataBean dataBean = data.get(position);
        holder.tvTaobaoShaixuanText.setText(dataBean.getProven());
        holder.tvTaobaoShaixuanText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(dataBean.getProven());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == data? 0:data.size();
    }

    public class TaobaoShaixuanViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.tv_taobao_shaixuan_text)
        TextView tvTaobaoShaixuanText;

        public TaobaoShaixuanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(String text);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
