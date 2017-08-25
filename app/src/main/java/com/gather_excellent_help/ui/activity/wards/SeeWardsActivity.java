package com.gather_excellent_help.ui.activity.wards;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.BackRebateBean;
import com.gather_excellent_help.bean.CodeStatueBean;
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
    @Bind(R.id.ll_taobao_loadmore)
    LinearLayout llTaobaoLoadmore;

    private NetUtil netUtil;
    private Map<String, String> map;
    private String url = Url.BASE_URL + "RewardLog.aspx";
    private String userLogin;
    private String pageSize = "10";
    private String pageNo = "1";
    private String type = "1";
    private int page = 1;
    private boolean isLoadMore = false;
    private FullyLinearLayoutManager fullyLinearLayoutManager;
    private int lastVisibleItem;

    private List<BackRebateBean.DataBean> rewardData;
    private List<BackRebateBean.DataBean> currData;
    private BackRebateAdapter backRebateAdapter;


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
        fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        rcvSeeWard.setLayoutManager(fullyLinearLayoutManager);
        showLoading();
        loadRewardData();
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e("查看奖励" + response);
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1:
                        BackRebateBean backRebateBean = new Gson().fromJson(response, BackRebateBean.class);
                        currData = backRebateBean.getData();
                        int size = currData.size();
                        if(isLoadMore) {
                            page++;
                            if(size<10) {
                                showLoadNoMore();
                                return;
                            }else{
                                rewardData.addAll(currData);
                            }
                            backRebateAdapter.notifyDataSetChanged();
                        }else{
                            rewardData = currData;
                            backRebateAdapter = new BackRebateAdapter(SeeWardsActivity.this, rewardData);
                            rcvSeeWard.setAdapter(backRebateAdapter);
                            backRebateAdapter.notifyDataSetChanged();
                            page = 2;
                        }
                        rcvSeeWard.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                    lastVisibleItem = fullyLinearLayoutManager
                                            .findLastVisibleItemPosition();
                                    if (lastVisibleItem + 1 == fullyLinearLayoutManager
                                            .getItemCount()) {
                                        isLoadMore = true;
                                        showLoadMore();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                LogUtil.e("page ======"+page);
                                                pageNo = String.valueOf(page);
                                                loadRewardData();
                                            }
                                        }, 1000);
                                    }
                                }
                                super.onScrollStateChanged(recyclerView, newState);
                            }

                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                lastVisibleItem = fullyLinearLayoutManager.findLastVisibleItemPosition();
                            }
                        });
                        hindLoadMore();
                        break;
                    case 0:
                        hindLoadMore();
                        break;
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                       hindLoadMore();
            }
        });
        rlExit.setOnClickListener(new MyOnclickListener());
    }

    private void loadRewardData() {
        map = new HashMap<>();
        map.put("Id", userLogin);
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
                    int lei = finalI + 1;
                    type = String.valueOf(lei);
                    pageNo = "1";
                    isLoadMore = false;
                    showLoading();
                    loadRewardData();
                }
            });
        }
    }

    /**
     * 显示加载更多
     */
    private void showLoadMore() {
        if(llTaobaoLoadmore!=null) {
            llTaobaoLoadmore.setVisibility(View.VISIBLE);
            llTaobaoLoadmore.getChildAt(0).setVisibility(View.VISIBLE);
            TextView tvTitle = (TextView) llTaobaoLoadmore.getChildAt(1);
            tvTitle.setText("加载更多...");
        }
    }
    /**
     * 显示正在加载中
     */
    private void showLoading() {
        if(llTaobaoLoadmore!=null) {
            llTaobaoLoadmore.setVisibility(View.VISIBLE);
            llTaobaoLoadmore.getChildAt(0).setVisibility(View.VISIBLE);
            TextView tvTitle = (TextView) llTaobaoLoadmore.getChildAt(1);
            tvTitle.setText("正在加载中...");
        }
    }

    /**
     * 显示没有更多的数据了
     */
    private void showLoadNoMore() {
        if(llTaobaoLoadmore!=null) {
            TextView tvTitle = (TextView) llTaobaoLoadmore.getChildAt(1);
            llTaobaoLoadmore.getChildAt(0).setVisibility(View.GONE);
            tvTitle.setText("没有更多的数据了...");
            llTaobaoLoadmore.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 显示隐藏
     */
    private void hindLoadMore() {
        if(llTaobaoLoadmore!=null) {
            llTaobaoLoadmore.setVisibility(View.GONE);
        }
    }
}
