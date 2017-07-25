package com.gather_excellent_help.ui.activity.wards;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WardsStatisticsActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.tv_ward_statistics_search)
    TextView tvWardStatisticsSearch;
    @Bind(R.id.rcv_wards_statistics)
    RecyclerView rcvWardsStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wards_statistics);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 数据初始化
     */
    private void initData() {
        tvTopTitleName.setText("奖励统计");
        rlExit.setOnClickListener(new MyOnclickListener());
    }

    public class MyOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit :
                    finish();
                    break;
                case R.id.tv_ward_statistics_search:
                    srarhWardsStatistics();
                    break;
            }
        }
    }

    /**
     * 搜索奖励统计的信息
     */
    private void srarhWardsStatistics() {

    }
}
