package com.gather_excellent_help.ui.adapter;
/**
 * Created by ${} on 2017/7/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.OrderAllBean;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class OrderAllAdapter extends RecyclerView.Adapter<OrderAllAdapter.OrderManagerViewHolder> {


    private Context context;
    private LayoutInflater inflater;    //布局填充器
    private List<OrderAllBean.DataBean> allData;
    private int curr_statue;
    private int order_type;

    public OrderAllAdapter(Context context, List<OrderAllBean.DataBean> allData, int curr_statue, int order_type) {
        this.context = context;
        this.allData = allData;
        this.curr_statue = curr_statue;
        this.order_type = order_type;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public OrderManagerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = inflater.inflate(R.layout.item_all_order_update, null);
        return new OrderManagerViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(OrderManagerViewHolder holder, int position) {
        OrderAllBean.DataBean dataBean = allData.get(position);
        final String trade_id = dataBean.getTrade_id();
        String create_time = dataBean.getCreate_time();
        String start = trade_id.substring(0, 4);
        String end = trade_id.substring(trade_id.length() - 4);
        String show_trade = start + "*********" + end;
        holder.tvOrderAllNumber.setText("订单号:" + show_trade);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "此订单号已经复制到剪切板~", Toast.LENGTH_SHORT).show();
                Tools.copyToClipboard(context, trade_id);
                return false;
            }
        });
        DecimalFormat df = new DecimalFormat("#0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        final String show_time = create_time.replace("T", "");
        holder.tvOrderAllTime.setText("下单时间:" + show_time);
        holder.tvOrderAllPrice.setText("￥" + dataBean.getPrice());
        if (order_type == -1) {
            holder.tv_order_zhuan.setVisibility(View.GONE);
        } else if (order_type == -2) {
            holder.tv_order_zhuan.setVisibility(View.VISIBLE);
            holder.tv_order_zhuan.setText("￥" + df.format(dataBean.getAmount()));
        } else {
            holder.tv_order_zhuan.setVisibility(View.GONE);
        }
        String goodsImg = dataBean.getGoodsImg();
        String tk_status = dataBean.getTk_status();
        if (goodsImg != null && !TextUtils.isEmpty(goodsImg)) {
            Glide.with(context).load(goodsImg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                    .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                    .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                    .into(holder.ivOrderAllImg);//请求成功后把图片设置到的控件
        } else {
            holder.ivOrderAllImg.setImageResource(R.mipmap.zhanwei_icon);
        }

        holder.tvOrderAllStatue.setBackgroundResource(R.drawable.suning_order_part_state_orange);
        if (curr_statue == 1) {
            holder.tvOrderAllStatue.setText("结算");
        } else if (curr_statue == 2) {
            holder.tvOrderAllStatue.setText("付款");
        } else if (curr_statue == 3) {
            holder.tvOrderAllStatue.setText("完成");
        } else if (curr_statue == 4) {
            holder.tvOrderAllStatue.setText("失效");
            holder.tvOrderAllStatue.setBackgroundResource(R.drawable.suning_order_part_state_gray);
        } else {
            if (tk_status.equals("3")) {
                holder.tvOrderAllStatue.setText("结算");
            }
            if (tk_status.equals("12")) {
                holder.tvOrderAllStatue.setText("付款");
            }
            if (tk_status.equals("14")) {
                holder.tvOrderAllStatue.setText("完成");
            }
            if (tk_status.equals("13")) {
                holder.tvOrderAllStatue.setText("失效");
                holder.tvOrderAllStatue.setBackgroundResource(R.drawable.suning_order_part_state_gray);
            }
        }


    }

    @Override
    public int getItemCount() {
        return null == allData ? 0 : allData.size();
    }

    public class OrderManagerViewHolder extends RecyclerView.ViewHolder {

        ImageView ivOrderAllImg;
        TextView tvOrderAllNumber;
        TextView tvOrderAllPrice;
        TextView tvOrderAllTime;
        TextView tvOrderAllStatue;
        TextView tvOrderAllSeedetail;
        TextView tv_order_zhuan;
        LinearLayout ll_order_trade_no;

        public OrderManagerViewHolder(View itemView) {
            super(itemView);
            ivOrderAllImg = (ImageView) itemView.findViewById(R.id.iv_order_all_img);
            tvOrderAllNumber = (TextView) itemView.findViewById(R.id.tv_order_all_number);
            tvOrderAllPrice = (TextView) itemView.findViewById(R.id.tv_order_all_price);
            tvOrderAllTime = (TextView) itemView.findViewById(R.id.tv_order_all_time);
            tvOrderAllStatue = (TextView) itemView.findViewById(R.id.tv_order_all_statue);
            tvOrderAllSeedetail = (TextView) itemView.findViewById(R.id.tv_order_all_seedetail);
            tv_order_zhuan = (TextView) itemView.findViewById(R.id.tv_order_zhuan);
            ll_order_trade_no = (LinearLayout) itemView.findViewById(R.id.ll_order_trade_no);
        }
    }
}
