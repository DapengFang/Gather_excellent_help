package com.gather_excellent_help.ui.activity.credits;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.AccountDetailBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.adapter.AcccountDetailAdapter;
import com.gather_excellent_help.ui.adapter.ExtractDetailAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class AccountDetailAvtivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.rcv_account_detail)
    RecyclerView rcvAccountDetail;
    @Bind(R.id.tv_account_query_time)
    TextView tvAccountQueryTime;
    @Bind(R.id.tv_account_query_project)
    TextView tvAccountQueryProject;

    private String account_url = Url.BASE_URL + "AmountLog.aspx";
    private Map<String, String> map;
    private NetUtil netUtil;
    private List<AccountDetailBean.DataBean> accountData;
    private List<AccountDetailBean.DataBean> currData;
    private String type;
    private String pageSize = "10";
    private String pageIndex = "1";
    private int page = 1;
    private boolean isLoadmore = false;
    private int lastVisibleItem;
    private FullyLinearLayoutManager layoutManager;
    private AcccountDetailAdapter acccountDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail_avtivity);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        tvTopTitleName.setText("账户明细");
        tvAccountQueryTime.setSelected(true);
        tvAccountQueryProject.setSelected(false);
        layoutManager = new FullyLinearLayoutManager(AccountDetailAvtivity.this);
        rcvAccountDetail.setLayoutManager(layoutManager);
        netUtil = new NetUtil();
        type = "1";
        net2ServerByType(type);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1:
                        AccountDetailBean accountDetailBean = new Gson().fromJson(response, AccountDetailBean.class);
                        currData = accountDetailBean.getData();
                        loadAccountData();
                        break;
                    case 0:
                        Toast.makeText(AccountDetailAvtivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "---" + e.getMessage());
            }
        });
        rlExit.setOnClickListener(new MyOnclickListener());
        tvAccountQueryTime.setOnClickListener(new MyOnclickListener());
        tvAccountQueryProject.setOnClickListener(new MyOnclickListener());
        rcvAccountDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisibleItem = layoutManager
                            .findLastVisibleItemPosition();

                    if (lastVisibleItem + 1 == layoutManager
                            .getItemCount()) {
                        isLoadmore = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pageIndex = String.valueOf(page);
                               net2ServerByType(type);
                            }
                        }, 1000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void net2ServerByType(String type) {
        String userLogin = Tools.getUserLogin(this);
        if(TextUtils.isEmpty(userLogin)) {
            toLogin();
            return;
        }
        map = new HashMap<>();
        map.put("Id", userLogin);
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
        map.put("type", type);
        netUtil.okHttp2Server2(account_url, map);
    }

    /**
     * 登录
     */
    private void toLogin() {
        Intent intent = new Intent(AccountDetailAvtivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 加载获取的数据
     */
    private void loadAccountData() {
        if(isLoadmore) {
            page++;
            if(currData.size()<10) {
                Toast.makeText(AccountDetailAvtivity.this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
                return;
            }else{
                accountData.addAll(currData);
            }
            acccountDetailAdapter.notifyDataSetChanged();
        }else{
            page = 2;
            accountData = currData;
            acccountDetailAdapter = new AcccountDetailAdapter(this, accountData);
            rcvAccountDetail.setAdapter(acccountDetailAdapter);
        }
    }

    public class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_account_query_time:
                    isLoadmore = false;
                    page = 1;
                    pageIndex = String.valueOf(page);
                    tvAccountQueryTime.setSelected(true);
                    tvAccountQueryProject.setSelected(false);
                    type = "1";
                    net2ServerByType(type);
                    break;
                case R.id.tv_account_query_project:
                    isLoadmore = false;
                    page = 1;
                    pageIndex = String.valueOf(page);
                    tvAccountQueryTime.setSelected(false);
                    tvAccountQueryProject.setSelected(true);
                    type = "2";
                    net2ServerByType(type);
                    break;
            }
        }
    }

}
