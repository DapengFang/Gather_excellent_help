package com.gather_excellent_help.ui.activity.credits;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.BackRebateBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.adapter.AcccountDetailAdapter;
import com.gather_excellent_help.ui.adapter.BackRebateAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class BackRebateActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.tab_back_rebate)
    TabLayout tabBackRebate;
    @Bind(R.id.rcv_back_rebate)
    RecyclerView rcvBackRebate;
    @Bind(R.id.iv_order_no_zhanwei)
    ImageView ivOrderNoZhanwei;
    private List<BackRebateBean.DataBean> data;
    private List<BackRebateBean.DataBean> currData;
    private String back_url = Url.BASE_URL + "RebateLog.aspx";
    private NetUtil netUtil;
    private Map<String, String> map;
    private String Id;
    private String pageIndex = "1";
    private String pageSize = "10";
    private String type = "";
    private boolean isLoadMore = false;
    private int page = 1;
    private int lastVisibleItem;
    private boolean isCanLoad = true;
    private FullyLinearLayoutManager fullyLinearLayoutManager;
    private BackRebateAdapter backRebateAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_rebate);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        type = "1";
        tvTopTitleName.setText("返佣明细");
        ArrayList<String> mTitles = new ArrayList<>();
        mTitles.add("全部");
        mTitles.add("通过");
        mTitles.add("驳回");
        mTitles.add("待处理");
        tabBackRebate.setTabMode(TabLayout.MODE_FIXED);
        tabBackRebate.addTab(tabBackRebate.newTab().setText(mTitles.get(0)));
        tabBackRebate.addTab(tabBackRebate.newTab().setText(mTitles.get(1)));
        tabBackRebate.addTab(tabBackRebate.newTab().setText(mTitles.get(2)));
        tabBackRebate.addTab(tabBackRebate.newTab().setText(mTitles.get(3)));
        fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        rcvBackRebate.setLayoutManager(fullyLinearLayoutManager);
        rlExit.setOnClickListener(new MyOnclickListener());
        netUtil = new NetUtil();
        loadBackData(type, pageIndex);
        tabBackRebate.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        type = "1";
                        page = 1;
                        isLoadMore = false;
                        isCanLoad =true;
                        break;
                    case 1:
                        type = "2";
                        page = 1;
                        isLoadMore = false;
                        isCanLoad = true;
                        break;
                    case 2:
                        type = "3";
                        page = 1;
                        isLoadMore = false;
                        isCanLoad =true;
                        break;
                    case 3:
                        type = "4";
                        page = 1;
                        isLoadMore = false;
                        isCanLoad = true;
                        break;
                }
                loadBackData(type, pageIndex);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1:
                        BackRebateBean backRebateBean = new Gson().fromJson(response, BackRebateBean.class);
                        currData = backRebateBean.getData();
                        if (isLoadMore) {
                            page++;
                            data.addAll(currData);
                            backRebateAdapter.notifyDataSetChanged();
                        } else {
                            if(currData!=null) {
                                if(currData.size() > 0) {
                                    ivOrderNoZhanwei.setVisibility(View.GONE);
                                }else{
                                    ivOrderNoZhanwei.setVisibility(View.VISIBLE);
                                }
                            }
                            page = 2;
                            data = currData;
                            backRebateAdapter = new BackRebateAdapter(BackRebateActivity.this, data);
                            rcvBackRebate.setAdapter(backRebateAdapter);
                        }
                        isCanLoad = true;
                        break;
                    case 0:
                        Toast.makeText(BackRebateActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "-" + e.getMessage());
            }
        });

        rcvBackRebate.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if(!isCanLoad) {
                        return;
                    }

                    lastVisibleItem = fullyLinearLayoutManager
                            .findLastVisibleItemPosition();
                    if (lastVisibleItem + 1 == fullyLinearLayoutManager
                            .getItemCount()) {
                        isLoadMore = true;
                        if(currData.size()<10) {
                            Toast.makeText(BackRebateActivity.this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        pageIndex = String.valueOf(page);
                        isCanLoad = false;
                        loadBackData(type,pageIndex);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = fullyLinearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void loadBackData(String type, String pageIndex) {
        Id = Tools.getUserLogin(this);
        if (TextUtils.isEmpty(Id)) {
            toLogin();
            return;
        }
        map = new HashMap<>();
        map.put("Id", Id);
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
        map.put("type", type);
        netUtil.okHttp2Server2(back_url, map);
    }

    /**
     * 登录
     */
    private void toLogin() {
        Intent intent = new Intent(BackRebateActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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
}
