package com.gather_excellent_help.ui.activity.credits;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountDetailAvtivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.rcv_account_detail)
    RecyclerView rcvAccountDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail_avtivity);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        tvTopTitleName.setText("账户明细");
        rlExit.setOnClickListener(new MyOnclickListener());
    }

    public class MyOnclickListener implements View.OnClickListener{

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
