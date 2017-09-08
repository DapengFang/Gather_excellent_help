package com.gather_excellent_help.update;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.HomeWareBean;
import com.gather_excellent_help.ui.activity.WareListActivity;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.ui.activity.WebRecordActivity;
import com.gather_excellent_help.ui.adapter.HomeRushAdapter;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dapeng Fang on 2017/8/18.
 */

public class HomeActivityAdapter extends RecyclerView.Adapter<HomeActivityAdapter.HomeActivityViewHolder> {

    private Context context;
    private List<HomeWareBean.DataBean> rushData;

    public HomeActivityAdapter(Context context, List<HomeWareBean.DataBean> rushData) {
        this.context = context;
        this.rushData = rushData;
    }

    @Override
    public HomeActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.home_activity_item, null);
        return new HomeActivityViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final HomeActivityViewHolder holder, int position) {
        final HomeWareBean.DataBean dataBean = rushData.get(position);
        String title = dataBean.getTitle();
        final String img_url = dataBean.getImg_url();
        final List<HomeWareBean.DataBean.ItemBean> itemData = dataBean.getItem();
        if(title!=null && holder.tv_rush_more_title!=null) {
            SpannableStringBuilder style = new SpannableStringBuilder(title);
            style.setSpan(new ForegroundColorSpan(Color.RED),2, title.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tv_rush_more_title.setText(style);
        }
        if(img_url!=null && holder.iv_rush_more_big!=null) {
            Glide.with(context).load(Url.IMG_URL + img_url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                    .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                    .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                    .into(holder.iv_rush_more_big);//请求成功后把图片设置到的控件
            holder.iv_rush_more_big.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = dataBean.getId();
                    Intent intent = new Intent(context, WareListActivity.class);
                    intent.putExtra("activity_id", String.valueOf(id));
                    intent.putExtra("content", "activity");
                    context.startActivity(intent);
                }
            });
        }

        if (holder.rcv_activity_ware_list!=null && itemData != null && itemData.size() > 2) {
            GridLayoutManager gridLayoutManager=new GridLayoutManager(context,3);
            holder.rcv_activity_ware_list.setLayoutManager(gridLayoutManager);
            HomeActivityWareAdapter homeActivityWareAdapter = new HomeActivityWareAdapter(context,itemData);
            holder.rcv_activity_ware_list.setAdapter(homeActivityWareAdapter);
            homeActivityWareAdapter.setOnItemClickListener(new HomeActivityWareAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int i) {
                    DecimalFormat df = new DecimalFormat("#0.00");
                    String couponsUrl = itemData.get(i).getCouponsUrl();
                    String link_url = itemData.get(i).getLink_url();
                    String goods_id = itemData.get(i).getProductId();
                    String goods_img = itemData.get(i).getImg_url();
                    String goods_title = itemData.get(i).getTitle();
                    double sell_price = itemData.get(i).getSell_price();
                    int couponsPrice = itemData.get(i).getCouponsPrice();
                    if(couponsPrice>0) {
                        if(couponsUrl!=null && !TextUtils.isEmpty(couponsUrl)) {
                            Intent intent = new Intent(context, WebActivity.class);
                            intent.putExtra("web_url",couponsUrl);
                            intent.putExtra("url", link_url);
                            intent.putExtra("goods_id", goods_id);
                            intent.putExtra("goods_img", goods_img);
                            intent.putExtra("goods_title", goods_title);
                            intent.putExtra("goods_price", df.format(sell_price)+"");
                            intent.putExtra("goods_coupon", String.valueOf(couponsPrice));
                            intent.putExtra("goods_coupon_url", couponsUrl);
                            context.startActivity(intent);
                        }
                    } else{
                        Intent intent = new Intent(context, WebRecordActivity.class);
                        intent.putExtra("url", link_url);
                        intent.putExtra("goods_id", goods_id);
                        intent.putExtra("goods_img", goods_img);
                        intent.putExtra("goods_title", goods_title);
                        intent.putExtra("goods_price", df.format(sell_price)+"");
                        context.startActivity(intent);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return null == rushData ? 0:rushData.size();
    }


    public class HomeActivityViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.rcv_activity_ware_list)
        RecyclerView rcv_activity_ware_list;
        @Bind(R.id.tv_rush_more_title)
        TextView tv_rush_more_title;
        @Bind(R.id.iv_rush_more_big)
        ImageView iv_rush_more_big;

        public HomeActivityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
