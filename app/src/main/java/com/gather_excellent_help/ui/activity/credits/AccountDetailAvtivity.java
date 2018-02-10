package com.gather_excellent_help.ui.activity.credits;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.AccountDetailBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.adapter.AcccountDetailAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.WanRecycleView;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.ScreenUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

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

    @Bind(R.id.tv_account_query_time)
    TextView tvAccountQueryTime;
    @Bind(R.id.tv_account_query_project)
    TextView tvAccountQueryProject;

    private RelativeLayout rl_order_no_zhanwei;

    private WanRecycleView wan_account_detail;
    private RecyclerView rcvAccountDetail;


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
    private boolean isCanLoad = true;
    private int lastVisibleItem;
    private FullyLinearLayoutManager layoutManager;
    private AcccountDetailAdapter acccountDetailAdapter;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail_avtivity);
        initView();
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        wan_account_detail = (WanRecycleView) findViewById(R.id.wan_account_detail);
        rl_order_no_zhanwei = (RelativeLayout)findViewById(R.id.rl_order_no_zhanwei);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        netUtil = new NetUtil();
        tvTopTitleName.setText("账户明细");
        tvAccountQueryTime.setSelected(true);
        tvAccountQueryProject.setSelected(false);

        rcvAccountDetail = wan_account_detail.getRefreshableView();
        layoutManager = new FullyLinearLayoutManager(AccountDetailAvtivity.this);
        rcvAccountDetail.setLayoutManager(layoutManager);

        //设置刷新相关
        wan_account_detail.setScrollingWhileRefreshingEnabled(true);
        wan_account_detail.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        wan_account_detail.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                isLoadmore = false;
                page = 1;
                pageIndex = String.valueOf(page);
                tvAccountQueryTime.setSelected(true);
                tvAccountQueryProject.setSelected(false);
                isCanLoad = true;
                net2ServerByType(type);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }

        });

        type = "1";
        net2ServerByType(type);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                hindCatView();
                if (wan_account_detail != null) {
                    wan_account_detail.onRefreshComplete();
                }
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
                LogUtil.e(call.toString() + "-" + e.getMessage());
                EncryptNetUtil.startNeterrorPage(AccountDetailAvtivity.this);
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
                    if (!isCanLoad) {
                        return;
                    }
                    lastVisibleItem = layoutManager
                            .findLastVisibleItemPosition();

                    if (lastVisibleItem + 1 == layoutManager
                            .getItemCount()) {
                        isLoadmore = true;
                        if (currData.size() < 10) {
                            Toast.makeText(AccountDetailAvtivity.this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        isCanLoad = false;
                        pageIndex = String.valueOf(page);
                        net2ServerByType(type);
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
        showCatView();
        String userLogin = Tools.getUserLogin(this);
        if (TextUtils.isEmpty(userLogin)) {
            toLogin();
            return;
        }
        map = new HashMap<>();
        map.put("Id", userLogin);
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
        map.put("type", type);
        netUtil.okHttp2Server2(AccountDetailAvtivity.this,account_url, map);
    }

    /**
     * 显示CatView
     */
    private void showCatView() {
        View inflate = View.inflate(this, R.layout.loading_dialog_view, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflate);
        alertDialog = builder.create();
        if (AccountDetailAvtivity.this != null && !AccountDetailAvtivity.this.isFinishing()) {
            alertDialog.show();
        }
        alertDialog.getWindow().setLayout(ScreenUtil.getScreenWidth(this) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 隐藏CatView
     */
    private void hindCatView() {
        if (AccountDetailAvtivity.this != null && !AccountDetailAvtivity.this.isFinishing()) {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
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
        if (isLoadmore) {
            page++;
            accountData.addAll(currData);
            acccountDetailAdapter.notifyDataSetChanged();
        } else {
            if (currData != null) {
                if (currData.size() > 0) {
                    rl_order_no_zhanwei.setVisibility(View.GONE);
                } else {
                    rl_order_no_zhanwei.setVisibility(View.VISIBLE);
                }
            }
            page = 2;
            accountData = currData;
            acccountDetailAdapter = new AcccountDetailAdapter(this, accountData);
            rcvAccountDetail.setAdapter(acccountDetailAdapter);
        }
        isCanLoad = true;
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
                    isCanLoad = true;
                    net2ServerByType(type);
                    break;
                case R.id.tv_account_query_project:
                    isLoadmore = false;
                    page = 1;
                    pageIndex = String.valueOf(page);
                    tvAccountQueryTime.setSelected(false);
                    tvAccountQueryProject.setSelected(true);
                    type = "2";
                    isCanLoad = true;
                    net2ServerByType(type);
                    break;
            }
        }
    }

}
