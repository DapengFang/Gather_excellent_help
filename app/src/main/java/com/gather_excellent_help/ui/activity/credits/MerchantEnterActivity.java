package com.gather_excellent_help.ui.activity.credits;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MerchantEnterActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_enter);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData(){
        tvTopTitleName.setText("申请");
        rlExit.setOnClickListener(new MyOnClickListener());
    }

    /**
     * 监听点击事件的类
     */
    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit :
                    finish();
                    break;
            }
        }
    }
}
