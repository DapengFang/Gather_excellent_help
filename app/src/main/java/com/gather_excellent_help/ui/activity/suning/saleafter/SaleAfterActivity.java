package com.gather_excellent_help.ui.activity.suning.saleafter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;

public class SaleAfterActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;

    private LinearLayout ll_sale_after_choice_tuikuan;
    private LinearLayout ll_sale_after_choice_huanhuo;

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
        ll_sale_after_choice_tuikuan = (LinearLayout) findViewById(R.id.ll_sale_after_choice_tuikuan);
        ll_sale_after_choice_huanhuo = (LinearLayout) findViewById(R.id.ll_sale_after_choice_huanhuo);
    }


    /**
     * 初始化数据
     */
    private void initData() {
        tv_top_title_name.setText("申请售后");
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        ll_sale_after_choice_tuikuan.setOnClickListener(myonclickListener);
        ll_sale_after_choice_huanhuo.setOnClickListener(myonclickListener);
    }

    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.ll_sale_after_choice_tuikuan:
                    saleBackMoney();
                    break;
                case R.id.ll_sale_after_choice_huanhuo:
                    saleExahangeGoods();
                    break;
            }
        }
    }

    /**
     * 换货
     */
    private void saleExahangeGoods() {
        Intent intent = new Intent(this, ExchangeGoodsActivity.class);
        startActivity(intent);
    }

    /**
     * 退款
     */
    private void saleBackMoney() {
        Intent intent = new Intent(this, BackMoneyActivity.class);
        startActivity(intent);
    }
}
