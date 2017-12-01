package com.gather_excellent_help.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.suning.SuningSpecBean;
import com.gather_excellent_help.ui.adapter.SuningSpecAdapter;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.ScreenUtil;

import java.util.List;


/**
 * 作者：Dapeng Fang on 2016/12/29 10:22
 * <p/>
 * 邮箱：fdp111888@163.com
 */

public class SuningWarenumPopupwindow extends PopupWindow {

    private Context context;
    private LayoutInflater inflater;
    private View vShadow;
    private int number = 1;
    private NumberAddSubView nav_bottom_pop_num;

    private String goods_id = "";//商品id
    private String article_id = "";//数据库自增id
    private String goods_img = "";//商品图片链接
    private String goods_title = "";//商品标题
    private String goods_price = "";//商品价格
    private String c_price = "";//商品超市价格
    private ImageView iv_bottom_pop_img;
    private ImageView iv_bottom_pop_exit;
    private TextView tv_bottom_pop_name;
    private TextView tv_bottom_pop_goodprice;
    private TextView tv_bottom_pop_ogoodprice;
    private TextView tv_activity_sun_tao_icon;
    private int limitNumber;//限购数量

    public SuningWarenumPopupwindow(Context context, View vShadow) {
        super(context);
        setFocusable(true);
        this.context = context;
        this.vShadow = vShadow;
        inflater = LayoutInflater.from(context);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        View view = inflater.inflate(R.layout.bottom_tobuy_num_popup, null);
        TextView tv_bottom_pop_buy = (TextView) view.findViewById(R.id.tv_bottom_pop_buy);
        iv_bottom_pop_img = (ImageView) view.findViewById(R.id.iv_bottom_pop_img);
        iv_bottom_pop_exit = (ImageView) view.findViewById(R.id.iv_bottom_pop_exit);
        tv_bottom_pop_name = (TextView) view.findViewById(R.id.tv_bottom_pop_name);
        tv_bottom_pop_goodprice = (TextView) view.findViewById(R.id.tv_bottom_pop_goodprice);
        tv_bottom_pop_ogoodprice = (TextView) view.findViewById(R.id.tv_bottom_pop_ogoodprice);
        tv_activity_sun_tao_icon = (TextView) view.findViewById(R.id.tv_activity_sun_tao_icon);
        nav_bottom_pop_num = (NumberAddSubView) view.findViewById(R.id.nav_bottom_pop_num);
        nav_bottom_pop_num.setMinValue(1);
        nav_bottom_pop_num.setValue(number);
        tv_activity_sun_tao_icon.setSelected(true);
        iv_bottom_pop_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                vShadow.setVisibility(View.GONE);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };

        number = nav_bottom_pop_num.getValue();
        tv_bottom_pop_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListenr.onPopupBuy(nav_bottom_pop_num.getValue());
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                vShadow.setVisibility(View.GONE);
            }
        });
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ScreenUtil.getScreenHeight(context) - DensityUtil.dip2px(context, 20));
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#00000000"));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }


    private OnItemClickListenr onItemClickListenr;


    public interface OnItemClickListenr {
        void onPopupBuy(int num);

    }

    public void setOnItemClickListenr(OnItemClickListenr onItemClickListenr) {
        this.onItemClickListenr = onItemClickListenr;
    }

    /**
     * 设置商品限购
     * @param limitNumber
     */
    public void setLimitNumber(int limitNumber) {
        this.limitNumber = limitNumber;
    }

    /**
     * 设置商品数量
     *
     * @param number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * 设置加减器的值
     */
    public void setNavNumber() {
        if (nav_bottom_pop_num != null) {
            nav_bottom_pop_num.setValue(number);
        }
    }

    /**
     * 设置加减器的最大值
     */
    public void setNavLimitNumber(){
        if(nav_bottom_pop_num!=null) {
            if(limitNumber>0) {
                nav_bottom_pop_num.setMaxValue(limitNumber);
            }
        }
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public void setC_price(String c_price) {
        this.c_price = c_price;
    }

    /**
     * 设置苏宁商品信息
     */
    public void setSunigWareShow() {
        if (iv_bottom_pop_img != null && goods_img != null) {
            String replace_img = goods_img.replace("800x800", "400x400");
            Glide.with(context).load(replace_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                    .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                    .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                    .into(iv_bottom_pop_img);//请求成功后把图片设置到的控件
        }

        if (tv_bottom_pop_name != null && goods_title != null) {
            tv_bottom_pop_name.setText("\t\t\t\t\t\t" + goods_title);
        }

        if (tv_bottom_pop_goodprice != null && goods_price != null) {
            tv_bottom_pop_goodprice.setText("￥" + goods_price);
        }

        if (tv_bottom_pop_ogoodprice != null && c_price != null) {
            tv_bottom_pop_ogoodprice.getPaint().setAntiAlias(true);//抗锯齿
            tv_bottom_pop_ogoodprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            tv_bottom_pop_ogoodprice.setText(c_price);
        }
    }
}
