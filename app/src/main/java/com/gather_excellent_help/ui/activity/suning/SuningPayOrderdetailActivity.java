package com.gather_excellent_help.ui.activity.suning;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;

public class SuningPayOrderdetailActivity extends BaseActivity {

    private TextView tv_checkstand_ordercode;
    private TextView tv_checkstand_money;
    private TextView tv_checkstand_money_time;
    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suning_pay_orderdetail);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_checkstand_ordercode = (TextView) findViewById(R.id.tv_checkstand_ordercode);
        tv_checkstand_money = (TextView) findViewById(R.id.tv_checkstand_money);
        tv_checkstand_money_time = (TextView) findViewById(R.id.tv_checkstand_money_time);
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tv_top_title_name.setText("订单详情");
        Intent intent = getIntent();
        String pay_price = intent.getStringExtra("pay_price");
        String order_num = intent.getStringExtra("order_num");
        String order_time = intent.getStringExtra("order_time");
        tv_checkstand_money.setText(pay_price);
        tv_checkstand_money_time.setText(order_time);
        tv_checkstand_ordercode.setText(order_num);
        rl_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
