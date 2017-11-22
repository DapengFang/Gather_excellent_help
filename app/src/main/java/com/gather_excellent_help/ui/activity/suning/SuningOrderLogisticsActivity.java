package com.gather_excellent_help.ui.activity.suning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;

public class SuningOrderLogisticsActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suning_order_logistics);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout)findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView)findViewById(R.id.tv_top_title_name);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        tv_top_title_name.setText("订单信息");
        SuningOrderLogisticsActivity.MyonclickListener myonclickListener = new SuningOrderLogisticsActivity.MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
    }

    /**
     * 页面上事件监听
     */
    public class MyonclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_exit :
                    finish();
                    break;
            }
        }
    }
}
