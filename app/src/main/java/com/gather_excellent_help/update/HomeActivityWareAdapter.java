package com.gather_excellent_help.update;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.HomeWareBean;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dapeng Fang on 2017/8/18.
 */

public class HomeActivityWareAdapter extends RecyclerView.Adapter<HomeActivityWareAdapter.HomeActivityWareViewHolder> {
    
    private Context context;
    private List<HomeWareBean.DataBean.ItemBean> itemData;
    //private ImageLoader mImageLoader;
    private double user_rate;
    private int shopType;
    private boolean isToggle;

    public HomeActivityWareAdapter(Context context, List<HomeWareBean.DataBean.ItemBean> itemData) {
        this.context = context;
        this.itemData = itemData;
        //mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        shopType = Tools.getShopType(context);
        String userRate = Tools.getUserRate(context);
        if (!TextUtils.isEmpty(userRate)) {
            double v = Double.parseDouble(userRate);
            user_rate = v / 100;
        }
        isToggle = Tools.isToggleShow(context);
    }

    @Override
    public HomeActivityWareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.home_activity_ware_item, null);
        return new HomeActivityWareViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(HomeActivityWareViewHolder holder, final int position) {
        DecimalFormat df = new DecimalFormat("#0.00");
        HomeWareBean.DataBean.ItemBean itemBean = itemData.get(position);
        String img_url = itemBean.getImg_url();
        String title = itemBean.getTitle();
        double sell_price = itemBean.getSell_price();
        int couponsPrice = itemBean.getCouponsPrice();
        double tkRate = itemBean.getTkRate()/100;
        double zhuan = (sell_price -couponsPrice)*tkRate*0.9f*user_rate;
        double coast = sell_price -couponsPrice-zhuan;
        if(holder.tvActivityWarePrice !=null && holder.tvActivityWarePrice!=null) {
            if (shopType == 1) {
                if (isToggle) {
                    holder.tvActivityWarePrice.setText("￥"+df.format(sell_price));
                    holder.tvActivityWareType.setText("页面价");
                } else {
                    holder.tvActivityWarePrice.setText("￥"+df.format(coast));
                    holder.tvActivityWareType.setText("成本");
                }
            } else {
                holder.tvActivityWarePrice.setText("￥"+df.format(sell_price));
                holder.tvActivityWareType.setText("页面价");
            }
        }
        if(holder.ivActivityWareImg!=null && img_url!=null) {
            //mImageLoader.loadImage(img_url,holder.ivActivityWareImg,true);
            Glide.with(context).load(img_url+"_320x320q90.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                    .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                    .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                    .into(holder.ivActivityWareImg);//请求成功后把图片设置到的控件
        }
        if(holder.tvActivityWareTitle!=null && title!=null) {
           holder.tvActivityWareTitle.setText(title);
        }
        holder.llActivityWare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onItemClickListener.onItemClick(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == itemData ? 0:itemData.size();
    }

    public class HomeActivityWareViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_activity_ware_img)
        ImageView ivActivityWareImg;
        @Bind(R.id.tv_activity_ware_title)
        TextView tvActivityWareTitle;
        @Bind(R.id.tv_activity_ware_price)
        TextView tvActivityWarePrice;
        @Bind(R.id.tv_activity_ware_type)
        TextView tvActivityWareType;
        @Bind(R.id.ll_activity_ware)
        LinearLayout llActivityWare;

        public HomeActivityWareViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View v,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
