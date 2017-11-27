package com.gather_excellent_help.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.api.pay.PayResult;
import com.gather_excellent_help.api.pay.SignUtils;
import com.gather_excellent_help.bean.BackRebateBean;
import com.gather_excellent_help.bean.suning.SuningOrderBean;
import com.gather_excellent_help.ui.activity.suning.CheckStandActivity;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2017/7/27.
 */

public class SuningOrderAdapter extends RecyclerView.Adapter<SuningOrderAdapter.SuningOrderViewHolder> {

    private Context context;
    private List<SuningOrderBean.DataBean> data;
    private String order_status;


    public SuningOrderAdapter(Context context, List<SuningOrderBean.DataBean> data, String order_status) {
        this.context = context;
        this.data = data;
        this.order_status = order_status;
        LogUtil.e(order_status);
    }

    @Override
    public SuningOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.part_suning_order, null);
        return new SuningOrderViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SuningOrderViewHolder holder, final int position) {
        DecimalFormat df = new DecimalFormat("#0.00");
        SuningOrderBean.DataBean dataBean = data.get(position);
        if (dataBean != null) {
            final int status = dataBean.getOrder_status();
            String add_time = dataBean.getAdd_time();
            double real_amount = dataBean.getReal_amount();
            String order_no = dataBean.getOrder_no();
            List<SuningOrderBean.DataBean.GoodListBean> goodList = dataBean.getGoodList();
            if (add_time != null) {
                holder.tv_item_order_time.setText(add_time);
            }
            holder.tv_item_order_shifukuan.setText("￥" + String.valueOf(df.format(real_amount)));
            if (status == 1) {
                holder.tv_item_order_type.setText("买家待付款");
                holder.tv_item_order_topay.setVisibility(View.VISIBLE);
                holder.tv_item_order_cancel.setVisibility(View.VISIBLE);
                holder.tv_item_order_extra.setVisibility(View.GONE);
                holder.tv_item_order_topay.setText("立即付款");
                holder.tv_item_order_cancel.setText("取消订单");
            } else if (status == 2) {
                holder.tv_item_order_type.setText("等待卖家发货");
                holder.tv_item_order_topay.setVisibility(View.VISIBLE);
                holder.tv_item_order_cancel.setVisibility(View.VISIBLE);
                holder.tv_item_order_extra.setVisibility(View.VISIBLE);
                holder.tv_item_order_topay.setText("提醒发货");
                holder.tv_item_order_cancel.setText("查看物流");
                holder.tv_item_order_extra.setText("取消订单");
            } else if (status == 3) {
                holder.tv_item_order_type.setText("卖家已发货");
                holder.tv_item_order_topay.setVisibility(View.VISIBLE);
                holder.tv_item_order_cancel.setVisibility(View.GONE);
                holder.tv_item_order_extra.setVisibility(View.GONE);
                holder.tv_item_order_topay.setText("查看物流");
            } else if (status == 4) {
                holder.tv_item_order_type.setText("交易完成");
                holder.tv_item_order_topay.setVisibility(View.VISIBLE);
                holder.tv_item_order_cancel.setVisibility(View.VISIBLE);
                holder.tv_item_order_extra.setVisibility(View.GONE);
                holder.tv_item_order_topay.setText("确认订单");
                holder.tv_item_order_cancel.setText("申请售后");
            } else if (status == 5) {
                holder.tv_item_order_type.setText("订单作废");
                holder.tv_item_order_topay.setVisibility(View.GONE);
                holder.tv_item_order_cancel.setVisibility(View.GONE);
                holder.tv_item_order_extra.setVisibility(View.GONE);
            }
            holder.tv_item_order_topay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClickListener.onRightButtonClick(v, position, status);
                }
            });
            holder.tv_item_order_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClickListener.onLeftButtonClick(v, position, status);
                }
            });
            holder.tv_item_order_extra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClickListener.onExtraButtonClick(v,position,status);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClickListener.onItemClickListener(v, position, status);
                }
            });
            if (goodList != null && goodList.size() > 0) {
                holder.tv_suning_order_warenum.setText(goodList.size() + "件商品");
                holder.ll_order_manager_container.removeAllViews();
                for (int i = 0; i < goodList.size(); i++) {
                    View inflate = View.inflate(context, R.layout.item_suning_order_ware, null);
                    ImageView iv_suning_order_ware = (ImageView) inflate.findViewById(R.id.iv_suning_order_ware);
                    TextView tv_suning_order_title = (TextView) inflate.findViewById(R.id.tv_suning_order_title);
                    TextView tv_suning_order_type = (TextView) inflate.findViewById(R.id.tv_suning_order_type);
                    TextView tv_suning_order_realprice = (TextView) inflate.findViewById(R.id.tv_suning_order_realprice);
                    TextView tv_suning_order_oldprice = (TextView) inflate.findViewById(R.id.tv_suning_order_oldprice);
                    TextView tv_suning_order_number = (TextView) inflate.findViewById(R.id.tv_suning_order_number);
                    SuningOrderBean.DataBean.GoodListBean goodListBean = goodList.get(i);
                    if (goodListBean != null) {
                        if (goodListBean.getImg_url() != null) {
                            String img_url = goodListBean.getImg_url().replace("800x800", "400x400");
                            Glide.with(context).load(img_url)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                                    .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                                    .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                                    .into(iv_suning_order_ware);//请求成功后把图片设置到的控件
                        }else{
                            iv_suning_order_ware.setImageResource(R.mipmap.zhanwei_icon);
                        }
                        if (goodListBean.getSpec_text() != null) {
                            tv_suning_order_type.setText(goodListBean.getSpec_text());
                        }
                        if (goodListBean.getGoods_title() != null) {
                            tv_suning_order_title.setText(goodListBean.getGoods_title());
                        }
                        tv_suning_order_realprice.setText("￥" + String.valueOf(df.format(goodListBean.getReal_price())));
                        tv_suning_order_oldprice.getPaint().setAntiAlias(true);
                        tv_suning_order_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                        tv_suning_order_oldprice.setText("￥" + String.valueOf(df.format(goodListBean.getGoods_price())));
                        tv_suning_order_number.setText("x" + goodListBean.getQuantity());
                    }
                    holder.ll_order_manager_container.addView(inflate);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    public class SuningOrderViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_order_manager_container;
        TextView tv_item_order_time;
        TextView tv_item_order_type;
        TextView tv_suning_order_warenum;
        TextView tv_item_order_shifukuan;
        TextView tv_item_order_topay;
        TextView tv_item_order_cancel;
        TextView tv_item_order_extra;
        View itemView;

        public SuningOrderViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ll_order_manager_container = (LinearLayout) itemView.findViewById(R.id.ll_order_manager_container);
            tv_item_order_time = (TextView) itemView.findViewById(R.id.tv_item_order_time);
            tv_item_order_type = (TextView) itemView.findViewById(R.id.tv_item_order_type);
            tv_suning_order_warenum = (TextView) itemView.findViewById(R.id.tv_suning_order_warenum);
            tv_item_order_shifukuan = (TextView) itemView.findViewById(R.id.tv_item_order_shifukuan);
            tv_item_order_topay = (TextView) itemView.findViewById(R.id.tv_item_order_topay);
            tv_item_order_cancel = (TextView) itemView.findViewById(R.id.tv_item_order_cancel);
            tv_item_order_extra = (TextView) itemView.findViewById(R.id.tv_item_order_extra);
        }
    }

    private OnButtonClickListener onButtonClickListener;

    public interface OnButtonClickListener {

        void onItemClickListener(View view, int position, int status);

        void onLeftButtonClick(View view, int position, int status);

        void onRightButtonClick(View view, int position, int status);

        void onExtraButtonClick(View view, int position, int status);
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }
}
