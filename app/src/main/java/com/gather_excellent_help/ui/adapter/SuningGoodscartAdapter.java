package com.gather_excellent_help.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.suning.netcart.NetGoodsDeleteBean;
import com.gather_excellent_help.bean.suning.netcart.NetGoodscartBean;
import com.gather_excellent_help.bean.suning.netcart.NetGoodscartCheckBean;
import com.gather_excellent_help.ui.activity.suning.SuningGoodscartActivity;
import com.gather_excellent_help.ui.widget.NumberAddSubView;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.cartutils.NetCartUtil;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/16.
 */

public class SuningGoodscartAdapter extends RecyclerView.Adapter<SuningGoodscartAdapter.SuningGoodscartViewHolder> {

    private Context context;
    private List<NetGoodscartBean.DataBean> netData;
    private List<NetGoodscartCheckBean.DataBean> checkData;
    private AlertDialog alertDialog;
    private NetCartUtil netCartUtil;
    private final String userLogin;
    private int cart_value;


    public SuningGoodscartAdapter(Context context, List<NetGoodscartBean.DataBean> netData, List<NetGoodscartCheckBean.DataBean> checkData) {
        this.context = context;
        this.netData = netData;
        this.checkData = checkData;
        userLogin = Tools.getUserLogin(context);
        netCartUtil = new NetCartUtil();
        OnCartResponseListener onCartResponseListener = new OnCartResponseListener();
        netCartUtil.setOnCartResponseListener(onCartResponseListener);
    }

    @Override
    public SuningGoodscartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SuningGoodscartViewHolder(View.inflate(context, R.layout.item_suning_goodscart, null));
    }

    @Override
    public void onBindViewHolder(final SuningGoodscartViewHolder holder, final int position) {
        try {
            DecimalFormat df = new DecimalFormat("#0.00");
            final NetGoodscartBean.DataBean dataBean = netData.get(position);
            final int quantity = dataBean.getQuantity();
            final int purchase_num = dataBean.getPurchase_num();
            List<NetGoodscartBean.DataBean.GoodsListBean> goods_list = dataBean.getGoods_list();
            if (goods_list != null && goods_list.size() > 0) {
                NetGoodscartBean.DataBean.GoodsListBean goodsListBean = goods_list.get(0);
                String goods_title = goodsListBean.getGoods_title();
                String img_url = goodsListBean.getImg_url();
                double market_price = goodsListBean.getMarket_price();
                double sell_price = goodsListBean.getSell_price();
                int limit_num = goodsListBean.getLimit_num();
                if (limit_num > 0) {
                    holder.nas_goodscart_num.setMaxValue(purchase_num + quantity);
                } else {
                    holder.nas_goodscart_num.setMaxValue(100000);
                }
                if (img_url != null) {
                    String replace_img = img_url.replace("800x800", "400x400");
                    Glide.with(context).load(replace_img)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                            .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                            .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                            .into(holder.iv_suning_order_ware);//请求成功后把图片设置到的控件
                }
                if (goods_title != null) {
                    SpannableString span = new SpannableString("\t\t" + goods_title);
                    ImageSpan image = new ImageSpan(context, R.drawable.suning_ware_icon, DynamicDrawableSpan.ALIGN_BASELINE);
                    span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.tv_suning_goodscart_title.setText(span);
                }
                holder.tv_suning_order_realprice.setText("￥" + df.format(sell_price));

                holder.tv_suning_order_oldprice.setText("￥" + df.format(market_price));
            }

            List<NetGoodscartBean.DataBean.GgListBean> gg_list = dataBean.getGg_list();
            if (gg_list != null && gg_list.size() > 0) {
                NetGoodscartBean.DataBean.GgListBean ggListBean = gg_list.get(0);
                String spec_text = ggListBean.getSpec_text();
                if (spec_text != null) {
                    holder.tv_suning_goodscart_spec.setText(spec_text);
                }
            }


            final NetGoodscartCheckBean.DataBean checkBean = checkData.get(position);
            final int is_check = checkBean.getIs_check();

            final boolean bool = Tools.getInt2Boolean(String.valueOf(is_check));
            if (bool) {
                holder.cb_item_shopcart_check.setChecked(true);
                holder.iv_item_shopcart_delete.setVisibility(View.VISIBLE);
                holder.tv_goodscart_complete.setVisibility(View.GONE);
            } else {
                holder.cb_item_shopcart_check.setChecked(false);
                holder.iv_item_shopcart_delete.setVisibility(View.GONE);
                holder.tv_goodscart_complete.setVisibility(View.GONE);
            }

            holder.tv_suning_order_oldprice.getPaint().setAntiAlias(true);//抗锯齿
            holder.tv_suning_order_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰

            holder.tv_suning_order_number.setText("x" + quantity);
            holder.nas_goodscart_num.setValue(quantity);

            holder.cb_item_shopcart_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (is_check == 1) {
                        checkBean.setIs_check(0);
                    } else if (is_check == 0) {
                        checkBean.setIs_check(1);
                    }
                    notifyItemChanged(position);
                    NetGoodscartCheckBean netGoodscartCheckBean = new NetGoodscartCheckBean();
                    netGoodscartCheckBean.setData(checkData);
                    String result = new Gson().toJson(netGoodscartCheckBean);
                    Tools.saveCartCheck(context, result);
                    onUpdatePriceListener.onUpdateData(0);
                }
            });
            holder.iv_item_shopcart_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog(dataBean, checkBean, position);
                }
            });


            holder.nas_goodscart_num.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
                @Override
                public void onSubButton(View view, int value) {
                    cart_value = value;
                    holder.tv_goodscart_complete.setVisibility(View.VISIBLE);
                    onNumberAddSubListener.onSubClick(view, position, value);

                }

                @Override
                public void onAddButton(View view, int value) {
                    if (value == (purchase_num + quantity)) {
                        Toast.makeText(context, "当前限购数量" + (purchase_num + quantity) + "件", Toast.LENGTH_SHORT).show();
                    }
                    cart_value = value;
                    holder.tv_goodscart_complete.setVisibility(View.VISIBLE);
                    onNumberAddSubListener.onAddClick(view, position, value);

                }
            });
            holder.tv_goodscart_complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onNumberAddSubListener.onChangeNavnumClick(view, position, cart_value);
                    holder.tv_goodscart_complete.setVisibility(View.GONE);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, position);
                }
            });
        } catch (Exception e) {
            LogUtil.e("SuningGoodscartAdapter error" + e.getMessage());
            Toast.makeText(context, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }


    }

    private void deleteWare(NetGoodscartBean.DataBean dataBean, NetGoodscartCheckBean.DataBean checkBean, int position) {
        int index = netData.indexOf(dataBean);
        netData.remove(index);
        notifyItemRemoved(position);//当移除的时候用这个刷新
        notifyDataSetChanged();
        int cart_id = dataBean.getCart_id();
        checkBean.setIs_check(0);
        netCartUtil.deleteCart("0", userLogin, String.valueOf(cart_id));
    }

    @Override
    public int getItemCount() {
        return null == netData ? 0 : netData.size();
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
        RelativeLayout rl_item_shopcart_check;
        TextView tv_goodscart_complete;

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
            rl_item_shopcart_check = (RelativeLayout) itemView.findViewById(R.id.rl_item_shopcart_check);
            tv_goodscart_complete = (TextView) itemView.findViewById(R.id.tv_goodscart_complete);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnNumberAddSubListener onNumberAddSubListener;

    public interface OnNumberAddSubListener {
        void onAddClick(View v, int position, int value);

        void onSubClick(View v, int position, int value);

        void onChangeNavnumClick(View v, int position, int value);
    }

    public void setOnNumberAddSubListener(OnNumberAddSubListener onNumberAddSubListener) {
        this.onNumberAddSubListener = onNumberAddSubListener;
    }

    private OnBuyLimitNumListener onBuyLimitNumListener;

    public interface OnBuyLimitNumListener {
        void onChangeNum(int position, int value);
    }

    public void setOnBuyLimitNumListener(OnBuyLimitNumListener onBuyLimitNumListener) {
        this.onBuyLimitNumListener = onBuyLimitNumListener;
    }

    private OnUpdatePriceListener onUpdatePriceListener;

    public interface OnUpdatePriceListener {
        void onUpdateData(int which);
    }

    public void setOnUpdatePriceListener(OnUpdatePriceListener onUpdatePriceListener) {
        this.onUpdatePriceListener = onUpdatePriceListener;
    }

    public List<NetGoodscartBean.DataBean> getData() {
        return netData;
    }

    /**
     * 删除当前该商品
     */
    private void showDeleteDialog(final NetGoodscartBean.DataBean dataBean, final NetGoodscartCheckBean.DataBean checkBean, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("温馨提示")
                .setMessage("您确定要删除该商品信息吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteWare(dataBean, checkBean, position);
                    }
                })
                .setNegativeButton("取消", null);
        alertDialog = builder.create();
        SuningGoodscartActivity activity = (SuningGoodscartActivity) context;
        if (activity != null && !activity.isFinishing()) {
            alertDialog.show();
        }
    }

    public class OnCartResponseListener implements NetCartUtil.OnCartResponseListener {

        @Override
        public void onCartResponse(String response, String whick) {
            if (whick.equals(NetCartUtil.WHICH_DEL)) {
                parseDeletData(response);
            }
        }

        @Override
        public void onCartFail() {

        }
    }

    /**
     * @param response 解析当前删除数据
     */
    private void parseDeletData(String response) {
        try {
            NetGoodsDeleteBean netGoodsDeleteBean = new Gson().fromJson(response, NetGoodsDeleteBean.class);
            int statusCode = netGoodsDeleteBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    NetGoodscartCheckBean netGoodscartCheckBean = new NetGoodscartCheckBean();
                    netGoodscartCheckBean.setData(checkData);
                    String result = new Gson().toJson(netGoodscartCheckBean);
                    Tools.saveCartCheck(context, result);
                    break;
                case 0:
                    Toast.makeText(context, netGoodsDeleteBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
            onUpdatePriceListener.onUpdateData(1);
        } catch (Exception e) {
            LogUtil.e("SuningGoodscartAdapter error" + e.getMessage());
            Toast.makeText(context, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }
    }
}
