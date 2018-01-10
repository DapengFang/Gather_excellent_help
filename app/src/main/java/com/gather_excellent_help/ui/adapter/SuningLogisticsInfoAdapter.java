package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyachi.stepview.VerticalStepView;
import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.suning.SuningLogisticsBean;
import com.gather_excellent_help.utils.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dapeng Fang on 2017/7/27.
 */

public class SuningLogisticsInfoAdapter extends RecyclerView.Adapter<SuningLogisticsInfoAdapter.SuningLogisticsInfoViewHolder> {

    private Context context;
    private List<SuningLogisticsBean.DataBean> data;
    private List<SuningLogisticsBean.DataBean.OrderItemIdsBean> orderItemIds;
    private int count;

    public SuningLogisticsInfoAdapter(Context context, List<SuningLogisticsBean.DataBean> data
            , List<SuningLogisticsBean.DataBean.OrderItemIdsBean> orderItemIds) {
        this.context = context;
        this.data = data;
        this.orderItemIds = orderItemIds;
    }

    @Override
    public SuningLogisticsInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.item_suning_logistics_detail, null);
        return new SuningLogisticsInfoViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SuningLogisticsInfoViewHolder holder, int position) {
        try {
            SuningLogisticsBean.DataBean dataBean = data.get(position);
            SuningLogisticsBean.DataBean.OrderItemIdsBean orderItemIdsBean = orderItemIds.get(position);
            String orderItemId = orderItemIdsBean.getOrderItemId();
            holder.tv_suning_logistics_orderno.setText("订单编号：" + orderItemId);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<SuningLogisticsBean.DataBean.LogisticsDetailBean> logisticsDetail = dataBean.getLogisticsDetail();
            if (logisticsDetail != null && logisticsDetail.size() > 0) {
                holder.ll_suning_logistics_container.removeAllViews();
                for (int i = 0; i < logisticsDetail.size(); i++) {
                    SuningLogisticsBean.DataBean.LogisticsDetailBean logisticsDetailBean = logisticsDetail.get(i);
                    if (logisticsDetailBean != null) {
                        String operateState = logisticsDetailBean.getOperateState();
                        if (operateState != null && !TextUtils.isEmpty(operateState)) {
                            count++;
                        }
                    }
                }
                for (int i = logisticsDetail.size() - 1; i >= 0; i--) {
                    View inflate = View.inflate(context, R.layout.suning_logitics_part_one, null);
                    LinearLayout ll_logistics_part_one = (LinearLayout) inflate.findViewById(R.id.ll_logistics_part_one);
                    View v_zero = inflate.findViewById(R.id.v_zero);
                    ImageView iv_logistics_part_icon = (ImageView) inflate.findViewById(R.id.iv_logistics_part_icon);
                    View v_one = inflate.findViewById(R.id.v_one);
                    TextView tv_logistics_part_position = (TextView) inflate.findViewById(R.id.tv_logistics_part_position);
                    View v_two = inflate.findViewById(R.id.v_two);
                    TextView tv_logistics_part_time = (TextView) inflate.findViewById(R.id.tv_logistics_part_time);
                    SuningLogisticsBean.DataBean.LogisticsDetailBean logisticsDetailBean = logisticsDetail.get(i);
                    if (logisticsDetailBean != null) {
                        String operateState = logisticsDetailBean.getOperateState();
                        String operateTime = logisticsDetailBean.getOperateTime();

                        String curr_time = "";
                        String curr_state = "";
                        if (operateState != null && !TextUtils.isEmpty(operateState)) {
                            curr_state = operateState;
                        }
                        if (operateTime != null && !TextUtils.isEmpty(operateTime)) {
                            Date date = df.parse(operateTime);
                            curr_time = df2.format(date);
                        }
                        if (i == 0) {
                            holder.tv_suning_logistics_time.setText("创建时间：" + curr_time);
                        }
                        if (TextUtils.isEmpty(curr_state)) {
                            ll_logistics_part_one.setVisibility(View.GONE);
                        } else {
                            ll_logistics_part_one.setVisibility(View.VISIBLE);
                            tv_logistics_part_position.setText(curr_state);
                            tv_logistics_part_time.setText(curr_time);
                        }

                        if (position == 0) {
                            if (i == logisticsDetail.size() - 1) {
                                v_zero.setVisibility(View.INVISIBLE);
                                iv_logistics_part_icon.setVisibility(View.VISIBLE);
                                iv_logistics_part_icon.setImageResource(R.drawable.orange_up_arraw_icon);
                                v_one.setVisibility(View.VISIBLE);
                                v_two.setVisibility(View.VISIBLE);
                            } else if (i == 0) {
                                v_zero.setVisibility(View.VISIBLE);
                                iv_logistics_part_icon.setVisibility(View.VISIBLE);
                                iv_logistics_part_icon.setImageResource(R.drawable.gray_point_circle_icon);
                                v_one.setVisibility(View.GONE);
                                v_two.setVisibility(View.GONE);
                            } else {
                                v_zero.setVisibility(View.VISIBLE);
                                iv_logistics_part_icon.setVisibility(View.VISIBLE);
                                iv_logistics_part_icon.setImageResource(R.drawable.gray_point_circle_icon);
                                v_one.setVisibility(View.VISIBLE);
                                v_two.setVisibility(View.VISIBLE);
                            }
                            if (count == 1) {
                                v_zero.setVisibility(View.INVISIBLE);
                                iv_logistics_part_icon.setVisibility(View.VISIBLE);
                                iv_logistics_part_icon.setImageResource(R.drawable.orange_up_arraw_icon);
                                v_one.setVisibility(View.GONE);
                                v_two.setVisibility(View.GONE);
                            }
                        } else {
                            if (i == logisticsDetail.size() - 1) {
                                v_zero.setVisibility(View.INVISIBLE);
                                iv_logistics_part_icon.setVisibility(View.VISIBLE);
                                iv_logistics_part_icon.setImageResource(R.drawable.gray_up_arraw_icon);
                                v_one.setVisibility(View.VISIBLE);
                                v_two.setVisibility(View.VISIBLE);
                            } else if (i == 0) {
                                v_zero.setVisibility(View.VISIBLE);
                                iv_logistics_part_icon.setVisibility(View.VISIBLE);
                                iv_logistics_part_icon.setImageResource(R.drawable.gray_point_circle_icon);
                                v_one.setVisibility(View.GONE);
                                v_two.setVisibility(View.GONE);
                            } else {
                                v_zero.setVisibility(View.GONE);
                                iv_logistics_part_icon.setVisibility(View.GONE);
                                v_one.setVisibility(View.VISIBLE);
                                v_two.setVisibility(View.VISIBLE);
                            }
                            if (count == 1) {
                                v_zero.setVisibility(View.INVISIBLE);
                                iv_logistics_part_icon.setVisibility(View.VISIBLE);
                                iv_logistics_part_icon.setImageResource(R.drawable.gray_up_arraw_icon);
                                v_one.setVisibility(View.GONE);
                                v_two.setVisibility(View.GONE);
                            }
                        }
                    }
                    holder.ll_suning_logistics_container.addView(inflate);
                }
            }
        } catch (Exception e) {
            LogUtil.e("SuningLogisticsInfoAdapter error");
            Toast.makeText(context, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    public class SuningLogisticsInfoViewHolder extends RecyclerView.ViewHolder {

        TextView tv_suning_logistics_orderno;
        TextView tv_suning_logistics_time;
        LinearLayout ll_suning_logistics_container;

        public SuningLogisticsInfoViewHolder(View itemView) {
            super(itemView);
            tv_suning_logistics_orderno = (TextView) itemView.findViewById(R.id.tv_suning_logistics_orderno);
            tv_suning_logistics_time = (TextView) itemView.findViewById(R.id.tv_suning_logistics_time);
            ll_suning_logistics_container = (LinearLayout) itemView.findViewById(R.id.ll_suning_logistics_container);
        }
    }
}
