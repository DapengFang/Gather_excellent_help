package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyachi.stepview.VerticalStepView;
import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.BackRebateBean;
import com.gather_excellent_help.bean.suning.SuningLogisticsBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dapeng Fang on 2017/7/27.
 */

public class LogisticsInfoAdapter extends RecyclerView.Adapter<LogisticsInfoAdapter.LogisticsInfoViewHolder> {

    private Context context;
    private List<SuningLogisticsBean.DataBean> data;
    private List<SuningLogisticsBean.DataBean.OrderItemIdsBean> orderItemIds;

    public LogisticsInfoAdapter(Context context, List<SuningLogisticsBean.DataBean> data
            , List<SuningLogisticsBean.DataBean.OrderItemIdsBean> orderItemIds) {
        this.context = context;
        this.data = data;
        this.orderItemIds = orderItemIds;
    }

    @Override
    public LogisticsInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.item_suning_logistics_info, null);
        return new LogisticsInfoViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(LogisticsInfoViewHolder holder, int position) {
        SuningLogisticsBean.DataBean dataBean = data.get(position);
        SuningLogisticsBean.DataBean.OrderItemIdsBean orderItemIdsBean = orderItemIds.get(position);
        String orderItemId = orderItemIdsBean.getOrderItemId();
        holder.tv_suning_logistics_ordernum.setText("子订单号：" + orderItemId);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<SuningLogisticsBean.DataBean.LogisticsDetailBean> logisticsDetail = dataBean.getLogisticsDetail();
        if (logisticsDetail != null && logisticsDetail.size() > 0) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < logisticsDetail.size(); i++) {
                SuningLogisticsBean.DataBean.LogisticsDetailBean logisticsDetailBean = logisticsDetail.get(i);
                String operateState = logisticsDetailBean.getOperateState();
                String operateTime = logisticsDetailBean.getOperateTime();
                try {
                    Date date = df.parse(operateTime);
                    String curr_time = df2.format(date);
                    list.add(operateState+"\n"+curr_time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            holder.vstep_logistics.setStepsViewIndicatorComplectingPosition(list.size())//设置完成的步数
                    .reverseDraw(true)
                    .setStepViewTexts(list)//总步骤
                    .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#ffffff"))//设置StepsViewIndicator完成线的颜色
                    .setStepsViewIndicatorUnCompletedLineColor(Color.parseColor("#f8f8f8"))//设置StepsViewIndicator未完成线的颜色
                    .setStepViewComplectedTextColor(Color.parseColor("#ffffff"))//设置StepsView text完成线的颜色
                    .setStepViewUnComplectedTextColor(Color.parseColor("#f8f8f8"))//设置StepsView text未完成线的颜色
                    .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(context, R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                    .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(context, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                    .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(context, R.drawable.attention));//设置StepsViewIndicator AttentionIcon

        }


    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    public class LogisticsInfoViewHolder extends RecyclerView.ViewHolder {

        TextView tv_suning_logistics_ordernum;
        VerticalStepView vstep_logistics;

        public LogisticsInfoViewHolder(View itemView) {
            super(itemView);
            tv_suning_logistics_ordernum = (TextView) itemView.findViewById(R.id.tv_suning_logistics_ordernum);
            vstep_logistics = (VerticalStepView) itemView.findViewById(R.id.vstep_logistics);
        }
    }
}
