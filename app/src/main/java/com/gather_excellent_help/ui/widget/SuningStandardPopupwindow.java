package com.gather_excellent_help.ui.widget;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.PcsBean;
import com.gather_excellent_help.bean.suning.SuningSpecBean;
import com.gather_excellent_help.ui.activity.suning.OrderConfirmActivity;
import com.gather_excellent_help.ui.adapter.SuningSpecAdapter;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.ScreenUtil;
import com.gather_excellent_help.utils.pcsutils.PcsQueryUtil;
import com.google.gson.Gson;

import java.util.List;


/**
 * 作者：Dapeng Fang on 2016/12/29 10:22
 * <p/>
 * 邮箱：fdp111888@163.com
 */

public class SuningStandardPopupwindow extends PopupWindow {

    private Context context;
    private LayoutInflater inflater;
    private View vShadow;
    private List<SuningSpecBean.DataBean> data;
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
    private int limitNumber;

    public SuningStandardPopupwindow(Context context, View vShadow, List<SuningSpecBean.DataBean> data) {
        super(context);
        setFocusable(true);
        this.context = context;
        this.vShadow = vShadow;
        this.data = data;
        inflater = LayoutInflater.from(context);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        View view = inflater.inflate(R.layout.bottom_tobuy_popup, null);
        TextView tv_bottom_pop_buy = (TextView) view.findViewById(R.id.tv_bottom_pop_buy);
        RecyclerView rcv_bottom_spec = (RecyclerView) view.findViewById(R.id.rcv_bottom_spec);
        iv_bottom_pop_img = (ImageView) view.findViewById(R.id.iv_bottom_pop_img);
        iv_bottom_pop_exit = (ImageView) view.findViewById(R.id.iv_bottom_pop_exit);
        tv_bottom_pop_name = (TextView) view.findViewById(R.id.tv_bottom_pop_name);
        tv_activity_sun_tao_icon = (TextView) view.findViewById(R.id.tv_activity_sun_tao_icon);
        tv_bottom_pop_goodprice = (TextView) view.findViewById(R.id.tv_bottom_pop_goodprice);
        tv_bottom_pop_ogoodprice = (TextView) view.findViewById(R.id.tv_bottom_pop_ogoodprice);
        nav_bottom_pop_num = (NumberAddSubView) view.findViewById(R.id.nav_bottom_pop_num);
        nav_bottom_pop_num.setMinValue(1);
        nav_bottom_pop_num.setMaxValue(10);
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
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_bottom_spec.setLayoutManager(layoutManager);
        for (int i = 0; i < data.size(); i++) {
            SuningSpecBean.DataBean dataBean = data.get(i);
            List<SuningSpecBean.DataBean.ContentBean> content = dataBean.getContent();
            if (content != null && content.size() > 0) {
                SuningSpecBean.DataBean.ContentBean contentBean = content.get(0);
                contentBean.setCheck(true);
            }
        }
        SuningSpecAdapter suningSpecAdapter = new SuningSpecAdapter(context, data);
        rcv_bottom_spec.setAdapter(suningSpecAdapter);
        number = nav_bottom_pop_num.getValue();
        tv_bottom_pop_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListenr.onPopupBuy(data, nav_bottom_pop_num.getValue());
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

    public void setLimitNumber(int limitNumber) {
        this.limitNumber = limitNumber;
    }

    public interface OnItemClickListenr {
        void onPopupBuy(List<SuningSpecBean.DataBean> data, int num);

    }

    public void setOnItemClickListenr(OnItemClickListenr onItemClickListenr) {
        this.onItemClickListenr = onItemClickListenr;
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
     * 设置加减器的最大值
     */
    public void setNavLimitNumber() {
        if (nav_bottom_pop_num != null) {
            if (limitNumber > 0) {
                nav_bottom_pop_num.setMaxValue(limitNumber);
            }
        }
    }

    /**
     * 设置加减器的数字
     */
    public void setNavNumber() {
        if (nav_bottom_pop_num != null) {
            nav_bottom_pop_num.setValue(number);
        }
    }

    /**
     * 设置产品id
     *
     * @param goods_id
     */
    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    /**
     * 设置商品数据库自增id
     *
     * @param article_id
     */
    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    /**
     * 设置商品图片
     *
     * @param goods_img
     */
    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    /**
     * 设置商品标题
     *
     * @param goods_title
     */
    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    /**
     * 设置商品价格
     *
     * @param goods_price
     */
    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    /**
     * 设置商品超市价格
     *
     * @param c_price
     */
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
