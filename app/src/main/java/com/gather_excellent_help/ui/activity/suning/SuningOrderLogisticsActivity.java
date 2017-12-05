package com.gather_excellent_help.ui.activity.suning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.cartutils.NetCartUtil;

public class SuningOrderLogisticsActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;
    private NetCartUtil netCartUtil;
    private String userlogin;

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
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tv_top_title_name.setText("订单信息");
        SuningOrderLogisticsActivity.MyonclickListener myonclickListener = new SuningOrderLogisticsActivity.MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        netCartUtil = new NetCartUtil();
        userlogin = Tools.getUserLogin(this);
        OnCartResponseListener onCartResponseListener = new OnCartResponseListener();
        netCartUtil.setOnCartResponseListener(onCartResponseListener);
    }

    /**
     * 页面上事件监听
     */
    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
            }
        }
    }

    public void add(View v){
        Toast.makeText(SuningOrderLogisticsActivity.this, "add", Toast.LENGTH_SHORT).show();
        netCartUtil.addCart("9368","7","2937","21787","1");
    }
    public void del(View v){
        Toast.makeText(SuningOrderLogisticsActivity.this, "del", Toast.LENGTH_SHORT).show();
        netCartUtil.deleteCart("0","9368","6");
    }
    public void upd(View v){
        Toast.makeText(SuningOrderLogisticsActivity.this, "upd", Toast.LENGTH_SHORT).show();
        netCartUtil.updateCart("6","9368","7","2937","21787","2");
    }
    public void get(View v){
        Toast.makeText(SuningOrderLogisticsActivity.this, "get", Toast.LENGTH_SHORT).show();
        netCartUtil.getCartList("9368","10","1");
    }

    public class OnCartResponseListener implements NetCartUtil.OnCartResponseListener {

        @Override
        public void onCartResponse(String response, String whick) {
            LogUtil.e(whick + "=" +response);
            if (whick == NetCartUtil.WHICH_ADD) {

            } else if (whick == NetCartUtil.WHICH_DEL) {

            } else if (whick == NetCartUtil.WHICH_UPD) {

            } else if (whick == NetCartUtil.WHICH_GET) {

            }
        }

        @Override
        public void onCartFail() {

        }
    }
}
