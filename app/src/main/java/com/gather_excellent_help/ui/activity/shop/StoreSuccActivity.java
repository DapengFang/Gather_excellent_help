package com.gather_excellent_help.ui.activity.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;

public class StoreSuccActivity extends BaseActivity {

    private ImageView iv_zhuangtai_exit;
    private TextView tv_top_title_name;
    private TextView tv_store_succ_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_succ);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_zhuangtai_exit = (ImageView)findViewById(R.id.iv_zhuangtai_exit);
        tv_top_title_name = (TextView)findViewById(R.id.tv_top_title_name);
        tv_store_succ_submit = (TextView)findViewById(R.id.tv_store_succ_submit);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        iv_zhuangtai_exit.setVisibility(View.GONE);
        tv_top_title_name.setText("线下付费给服务商");
        tv_store_succ_submit.setOnClickListener(new MyOnclickListener());
    }

    public class MyOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            finish();
        }
    }
}
