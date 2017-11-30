package com.gather_excellent_help.ui.activity.suning.saleafter;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.activity.suning.SuningOrderDetailActivity;
import com.gather_excellent_help.ui.base.BaseActivity;

public class SaleAfterActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;

    private LinearLayout ll_sale_after_money_tuikuan;
    private LinearLayout ll_sale_after_choice_tuikuan;
    private LinearLayout ll_sale_after_choice_huanhuo;
    private ImageView iv_suning_order_ware;
    private TextView tv_suning_order_title;
    private TextView tv_suning_order_type;
    private TextView tv_suning_order_realprice;
    private TextView tv_suning_order_oldprice;
    private TextView tv_suning_order_number;
    private String ware_img;
    private String ware_title;
    private String spec_text;
    private String real_price;
    private String goods_price;
    private String quantity;
    private String article_id;
    private String order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_after);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
        ll_sale_after_money_tuikuan = (LinearLayout)findViewById(R.id.ll_sale_after_money_tuikuan);
        ll_sale_after_choice_tuikuan = (LinearLayout) findViewById(R.id.ll_sale_after_choice_tuikuan);
        ll_sale_after_choice_huanhuo = (LinearLayout) findViewById(R.id.ll_sale_after_choice_huanhuo);
        iv_suning_order_ware = (ImageView) findViewById(R.id.iv_suning_order_ware);
        tv_suning_order_title = (TextView) findViewById(R.id.tv_suning_order_title);
        tv_suning_order_type = (TextView) findViewById(R.id.tv_suning_order_type);
        tv_suning_order_realprice = (TextView) findViewById(R.id.tv_suning_order_realprice);
        tv_suning_order_oldprice = (TextView) findViewById(R.id.tv_suning_order_oldprice);
        tv_suning_order_number = (TextView) findViewById(R.id.tv_suning_order_number);
    }


    /**
     * 初始化数据
     */
    private void initData() {
        tv_top_title_name.setText("选择服务类型");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ware_img = bundle.getString("ware_img");
        ware_title = bundle.getString("ware_title");
        spec_text = bundle.getString("spec_text");
        real_price = bundle.getString("real_price");
        goods_price = bundle.getString("goods_price");
        quantity = bundle.getString("quantity");
        article_id = bundle.getString("article_id");
        order_id = bundle.getString("order_id");

        wareDataShow();

        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        ll_sale_after_money_tuikuan.setOnClickListener(myonclickListener);
        ll_sale_after_choice_tuikuan.setOnClickListener(myonclickListener);
        ll_sale_after_choice_huanhuo.setOnClickListener(myonclickListener);
    }

    /**
     * 显示商品信息
     */
    private void wareDataShow() {
        String img_url = ware_img.replace("800x800", "400x400");
        Glide.with(SaleAfterActivity.this).load(img_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                .into(iv_suning_order_ware);//请求成功后把图片设置到的控件

        if (spec_text != null) {
            tv_suning_order_type.setText(spec_text);
        }

        if (ware_title != null) {
            tv_suning_order_title.setText(ware_title);
        }
        tv_suning_order_realprice.setText("￥" + real_price);
        tv_suning_order_oldprice.getPaint().setAntiAlias(true);
        tv_suning_order_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tv_suning_order_oldprice.setText("￥" + goods_price);
        tv_suning_order_number.setText("x" + quantity);
    }

    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.ll_sale_after_money_tuikuan:
                    saleBackMoney();
                    break;
                case R.id.ll_sale_after_choice_tuikuan:
                    saleBackMoneyAndGoods();
                    break;
                case R.id.ll_sale_after_choice_huanhuo:
                    saleExahangeGoods();
                    break;
            }
        }
    }

    /**
     * 退款
     */
    private void saleBackMoney() {
        Intent intent = new Intent(this, BackMoneyActivity.class);
        Bundle bundle = new Bundle();
        if (ware_img != null) {
            bundle.putString("ware_img", ware_img);
        }
        if (ware_title != null) {
            bundle.putString("ware_title", ware_title);
        }
        if (spec_text != null) {
            bundle.putString("spec_text", spec_text);
        }
        bundle.putString("real_price", real_price);
        bundle.putString("goods_price", goods_price);
        bundle.putString("quantity", String.valueOf(quantity));
        bundle.putString("article_id", String.valueOf(article_id));
        bundle.putString("order_id", String.valueOf(order_id));
        bundle.putInt("apply_type",1);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 换货
     */
    private void saleExahangeGoods() {
       Toast.makeText(SaleAfterActivity.this, "暂不支持换货功能，敬请期待。", Toast.LENGTH_SHORT).show();
    }

    /**
     * 退款退货
     */
    private void saleBackMoneyAndGoods() {
        Intent intent = new Intent(this, BackMoneyActivity.class);
        Bundle bundle = new Bundle();
        if (ware_img != null) {
            bundle.putString("ware_img", ware_img);
        }
        if (ware_title != null) {
            bundle.putString("ware_title", ware_title);
        }
        if (spec_text != null) {
            bundle.putString("spec_text", spec_text);
        }
        bundle.putString("real_price", real_price);
        bundle.putString("goods_price", goods_price);
        bundle.putString("quantity", String.valueOf(quantity));
        bundle.putString("article_id", String.valueOf(article_id));
        bundle.putString("order_id", String.valueOf(order_id));
        bundle.putInt("apply_type",2);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
