package com.gather_excellent_help.ui.activity.wards;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.BackRebateBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.ui.activity.credits.BackRebateActivity;
import com.gather_excellent_help.ui.adapter.BackRebateAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.ViewpagerIndicator;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class SeeWardsActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.vid_see_ward)
    ViewpagerIndicator vidSeeWard;
    @Bind(R.id.rcv_see_ward)
    RecyclerView rcvSeeWard;

    private NetUtil netUtil;
    private Map<String, String> map;
    private String url = Url.BASE_URL + "RewardLog.aspx";
    private String userLogin;
    private String pageSize = "6";
    private String pageNo = "1";
    private String type = "1";

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
        userLogin = Tools.getUserLogin(this);
        netUtil = new NetUtil();
        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        rcvSeeWard.setLayoutManager(fullyLinearLayoutManager);
        loadRewardData();
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e("查看奖励" + response);
                CodeStatueBean codeStatueBean = new Gson().fromJson(response,CodeStatueBean.class);
                int statusCode =  codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1 :
                        BackRebateBean backRebateBean =new Gson().fromJson(response,BackRebateBean.class);
                        List<BackRebateBean.DataBean> rewardData = backRebateBean.getData();
                        BackRebateAdapter backRebateAdapter = new BackRebateAdapter(SeeWardsActivity.this, rewardData);
                        rcvSeeWard.setAdapter(backRebateAdapter);
                        break;
                    case 0:

                        break;
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });
        rlExit.setOnClickListener(new MyOnclickListener());
    }

    private void loadRewardData() {
        map = new HashMap<>();
        map.put("Id", "5");
        map.put("pageSize", pageSize);
        map.put("pageNo", pageNo);
        map.put("Type", type);
        netUtil.okHttp2Server2(url, map);
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
        TextView tv = (TextView) vidSeeWard.getChildAt(0);
        tv.setTextColor(Color.RED);
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
                    int lei = finalI+1;
                    type =String.valueOf(lei);
                    loadRewardData();
                }
            });
        }
    }
}
