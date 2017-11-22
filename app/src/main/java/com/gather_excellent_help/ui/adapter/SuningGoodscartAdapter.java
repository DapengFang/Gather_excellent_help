package com.gather_excellent_help.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.suning.SuningGoodscartBean;
import com.gather_excellent_help.db.suning.SqliteServiceManager;
import com.gather_excellent_help.ui.activity.AlipayInfoActivity;
import com.gather_excellent_help.ui.activity.suning.SuningGoodscartActivity;
import com.gather_excellent_help.ui.widget.NumberAddSubView;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.Tools;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/16.
 */

public class SuningGoodscartAdapter extends RecyclerView.Adapter<SuningGoodscartAdapter.SuningGoodscartViewHolder> {

    private Context context;
    private List<SuningGoodscartBean.DataBean> data;
    private SqliteServiceManager manager;
    private AlertDialog alertDialog;

    public SuningGoodscartAdapter(Context context, List<SuningGoodscartBean.DataBean> data) {
        this.context = context;
        this.data = data;
        manager = new SqliteServiceManager(context);
    }

    @Override
    public SuningGoodscartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SuningGoodscartViewHolder(View.inflate(context, R.layout.item_suning_goodscart, null));
    }

    @Override
    public void onBindViewHolder(final SuningGoodscartViewHolder holder, final int position) {
        final SuningGoodscartBean.DataBean dataBean = data.get(position);
        String product_check = dataBean.getProduct_check();
        final String id = dataBean.getId();
        String product_id = dataBean.getProduct_id();
        String product_mprice = dataBean.getProduct_mprice();
        String product_sprice = dataBean.getProduct_sprice();
        String product_spec = dataBean.getProduct_spec();
        String product_pic = dataBean.getProduct_pic();
        String product_num = dataBean.getProduct_num();
        String product_title = dataBean.getProduct_title();
        if(product_check!=null) {
            boolean bool = Tools.getInt2Boolean(product_check);
            if(bool) {
                holder.cb_item_shopcart_check.setChecked(true);
            }else{
                holder.cb_item_shopcart_check.setChecked(false);
            }
        }
        if(product_pic!=null) {
            String replace_img = product_pic.replace("800x800", "400x400");
            Glide.with(context).load(replace_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                    .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                    .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                    .into(holder.iv_suning_order_ware);//请求成功后把图片设置到的控件
        }
        if(product_title!=null) {
            holder.tv_suning_goodscart_title.setText(product_title);
        }
        if(product_spec!=null) {
            holder.tv_suning_goodscart_spec.setText(product_spec);
        }
        if(product_sprice!=null) {
            holder.tv_suning_order_realprice.setText("￥" + product_sprice);
        }
        if(product_mprice!=null) {
            holder.tv_suning_order_oldprice.setText("￥" + product_mprice);
            holder.tv_suning_order_oldprice.getPaint().setAntiAlias(true);//抗锯齿
            holder.tv_suning_order_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
        }
        if(product_num!=null) {
            holder.tv_suning_order_number.setText("x" + product_num);
            holder.nas_goodscart_num.setValue(Integer.parseInt(product_num));
        }
        holder.cb_item_shopcart_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e("--------------------");
                if(dataBean.getProduct_check().equals("1")) {
                    dataBean.setProduct_check("0");
                }else if(dataBean.getProduct_check().equals("0")) {
                    dataBean.setProduct_check("1");
                }
                notifyItemChanged(position);
                LogUtil.e(dataBean.getProduct_check()+"------------");
                manager.updateGoodsCheck(new String[]{dataBean.getProduct_check(),id});
                onUpdatePriceListener.onUpdateData();

            }
        });
        holder.iv_item_shopcart_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(dataBean,position,id);
            }
        });
        holder.nas_goodscart_num.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onSubButton(View view, int value) {
                holder.tv_suning_order_number.setText("x" + value);
                manager.updateGoods(new String[]{value+"",id});
                onUpdatePriceListener.onUpdateData();
            }

            @Override
            public void onAddButton(View view, int value) {
                holder.tv_suning_order_number.setText("x" + value);
                manager.updateGoods(new String[]{value+"",id});
                onUpdatePriceListener.onUpdateData();
            }
        });

    }

    private void deleteWare(SuningGoodscartBean.DataBean dataBean, int position, String id) {
        int index = data.indexOf(dataBean);
        data.remove(index);
        notifyItemRemoved(position);//当移除的时候用这个刷新
        notifyDataSetChanged();
        manager.deleteGoods(new String[]{id});
        onUpdatePriceListener.onUpdateData();
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    public class SuningGoodscartViewHolder extends RecyclerView.ViewHolder {

        CheckBox cb_item_shopcart_check;
        ImageView iv_suning_order_ware;
        TextView tv_suning_goodscart_title;
        TextView tv_suning_goodscart_spec;
        NumberAddSubView nas_goodscart_num;
        TextView tv_suning_order_realprice;
        TextView tv_suning_order_oldprice;
        TextView tv_suning_order_number;
        ImageView iv_item_shopcart_delete;

        public SuningGoodscartViewHolder(View itemView) {
            super(itemView);
            cb_item_shopcart_check = (CheckBox) itemView.findViewById(R.id.cb_item_shopcart_check);
            iv_suning_order_ware = (ImageView) itemView.findViewById(R.id.iv_suning_order_ware);
            tv_suning_goodscart_title = (TextView) itemView.findViewById(R.id.tv_suning_goodscart_title);
            tv_suning_goodscart_spec = (TextView) itemView.findViewById(R.id.tv_suning_goodscart_spec);
            nas_goodscart_num = (NumberAddSubView) itemView.findViewById(R.id.nas_goodscart_num);
            tv_suning_order_realprice = (TextView) itemView.findViewById(R.id.tv_suning_order_realprice);
            tv_suning_order_oldprice = (TextView) itemView.findViewById(R.id.tv_suning_order_oldprice);
            tv_suning_order_number = (TextView) itemView.findViewById(R.id.tv_suning_order_number);
            iv_item_shopcart_delete = (ImageView) itemView.findViewById(R.id.iv_item_shopcart_delete);
        }
    }

    private OnUpdatePriceListener onUpdatePriceListener;

    public interface OnUpdatePriceListener {
        void onUpdateData();
    }

    public void setOnUpdatePriceListener(OnUpdatePriceListener onUpdatePriceListener) {
        this.onUpdatePriceListener = onUpdatePriceListener;
    }

    public List<SuningGoodscartBean.DataBean> getData() {
        return data;
    }

    /**
     * 解除支付宝绑定的dialog
     */
    private void showDeleteDialog(final SuningGoodscartBean.DataBean dataBean,final int position,final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("温馨提示")
                .setMessage("您确定要删除该商品信息吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteWare(dataBean, position, id);
                    }
                })
                .setNegativeButton("取消", null);
        alertDialog = builder.create();
        SuningGoodscartActivity activity = (SuningGoodscartActivity) context;
        if (activity != null && !activity.isFinishing()) {
            alertDialog.show();
        }
    }
}
