package com.gather_excellent_help.ui.activity.wards;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.ViewpagerIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SeeWardsActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.vid_see_ward)
    ViewpagerIndicator vidSeeWard;
    @Bind(R.id.rcv_see_ward)
    RecyclerView rcvSeeWard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_wards);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        vidacatorControll();
        tvTopTitleName.setText("奖励明细");
        rlExit.setOnClickListener(new MyOnclickListener());
    }

    public class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
            }
        }
    }

    /**
     * 顶部指示条控制ViewPager
     */
    private void vidacatorControll() {
        vidSeeWard.setCount(4);
        final int childCount = vidSeeWard.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = vidSeeWard.getChildAt(i);
            final int finalI = i;
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vidSeeWard.checkMove(finalI);
                    for (int j = 0; j < childCount; j++) {
                        if (j != finalI) {
                            TextView tv = (TextView) vidSeeWard.getChildAt(j);
                            tv.setTextColor(Color.parseColor("#55000000"));
                        } else {
                            TextView tv = (TextView) v;
                            tv.setTextColor(Color.RED);
                        }
                    }
                }
            });
        }
    }
}
