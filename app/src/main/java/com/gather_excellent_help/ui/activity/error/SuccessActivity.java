package com.gather_excellent_help.ui.activity.error;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.TestActivity;
import com.gather_excellent_help.ui.base.BaseActivity;

public class SuccessActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;
    private TextView tv_success_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
        tv_success_status = (TextView) findViewById(R.id.tv_success_status);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        String extract_type = intent.getStringExtra("extract_type");
        String extract_message = intent.getStringExtra("extract_message");
        if (extract_type != null) {
            if (extract_type.equals("1")) {
                tv_top_title_name.setText("提现成功");
                tv_success_status.setText("提现成功");
            } else if (extract_type.equals("0")) {
                tv_top_title_name.setText("提现失败");
                if (extract_message != null) {
                    tv_success_status.setText(extract_message);
                }
            }
        }
        rl_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
