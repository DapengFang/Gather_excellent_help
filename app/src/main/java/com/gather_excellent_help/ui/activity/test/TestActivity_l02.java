package com.gather_excellent_help.ui.activity.test;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.TestActivity;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.AccountDetailBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.activity.credits.AccountDetailAvtivity;
import com.gather_excellent_help.ui.adapter.AcccountDetailAdapter;
import com.gather_excellent_help.ui.adapter.updatenew.AccountNewDetailAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.WanRecycleView;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class TestActivity_l02 extends BaseActivity {

    private RelativeLayout rl_exit;
    private RadioGroup rg_top_account;
    private TextView tv_top_title_name;
    private WanRecycleView wan_account_new_detail;
    private RecyclerView rcv_account_new_detail;
    private AlertDialog dialog;

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
    private AccountNewDetailAdapter accountNewDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_l02);
        initView();
        initData();
    }

    /**
     * 初始化试图
     */
    private void initView() {
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        rg_top_account = (RadioGroup) findViewById(R.id.rg_top_account);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
        wan_account_new_detail = (WanRecycleView) findViewById(R.id.wan_account_new_detail);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        netUtil = new NetUtil();
        tv_top_title_name.setText("账户明细");
        rg_top_account.check(R.id.rb_first);
        rl_exit.setOnClickListener(new MyonclickListener());
        rg_top_account.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        rcv_account_new_detail = wan_account_new_detail.getRefreshableView();
        layoutManager = new FullyLinearLayoutManager(this);
        rcv_account_new_detail.setLayoutManager(layoutManager);

        //设置刷新相关
        wan_account_new_detail.setScrollingWhileRefreshingEnabled(true);
        wan_account_new_detail.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        wan_account_new_detail.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                isLoadmore = false;
                page = 1;
                pageIndex = String.valueOf(page);
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
                if (wan_account_new_detail != null) {
                    wan_account_new_detail.onRefreshComplete();
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
                        Toast.makeText(TestActivity_l02.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "---" + e.getMessage());
            }
        });

        rcv_account_new_detail.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            Toast.makeText(TestActivity_l02.this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
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

    /**
     * 加载获取的数据
     */
    private void loadAccountData() {
        if (isLoadmore) {
            page++;
            accountData.addAll(currData);
            accountNewDetailAdapter.notifyDataSetChanged();
        } else {
            page = 2;
            accountData = currData;
            accountNewDetailAdapter = new AccountNewDetailAdapter(this, accountData);
            rcv_account_new_detail.setAdapter(accountNewDetailAdapter);
        }
        isCanLoad = true;
    }

    private void net2ServerByType(String type) {
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
        netUtil.okHttp2Server2(account_url, map);
    }

    /**
     * 跳转到登录界面
     */
    private void toLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 显示账户明细的dialog
     */
    private void showAccountDetailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = View.inflate(this, R.layout.account_detail_list_dialog, null);
        TextView tv_account_list_title = (TextView) inflate.findViewById(R.id.tv_account_list_title);
        TextView tv_account_list_price = (TextView) inflate.findViewById(R.id.tv_account_list_price);
        TextView tv_account_list_time = (TextView) inflate.findViewById(R.id.tv_account_list_time);
        TextView tv_account_list_remark = (TextView) inflate.findViewById(R.id.tv_account_list_remark);
        Button btn_account_lsit_cancel = (Button) inflate.findViewById(R.id.btn_account_lsit_cancel);
        dialog = builder.setView(inflate)
                .show();
        btn_account_lsit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
            }
        }
    }

    /**
     * 底部RadioGroup的点击监听
     */
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            //根据选中的RadiaoButton切换到ViewPager不同页面
            //        vp_content.setCurrentItem(0);
            switch (checkedId) {
                case R.id.rb_first:

                    break;
                case R.id.rb_second:

                    break;
                case R.id.rb_third:

                    break;
                case R.id.rb_four:

                    break;
            }
        }
    }
}
