package com.gather_excellent_help.ui.activity.error;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;

public class NetErrorActivity extends BaseActivity {

    private RelativeLayout rl_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_error);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        rl_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
