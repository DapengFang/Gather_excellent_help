package com.gather_excellent_help.ui.activity.credits;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.BackRebateBean;
import com.gather_excellent_help.bean.CodeStatueBean;
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
    private String back_url = Url.BASE_URL + "RebateLog.aspx";
    private NetUtil netUtil;
    private Map<String, String> map;
    private String Id;
    private String pageIndex = "1";
    private String pageSize = "6";
    private String type = "";


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
        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        rcvBackRebate.setLayoutManager(fullyLinearLayoutManager);
        rlExit.setOnClickListener(new MyOnclickListener());
        netUtil = new NetUtil();
        loadBackData(type, pageIndex);
        tabBackRebate.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0 :
                         type = "1";
                        break;
                    case 1:
                        type = "2";
                        break;
                    case 2:
                        type = "3";
                        break;
                    case 3:
                        type = "4";
                        break;
                }
                loadBackData(type,pageIndex);
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
                    case 1 :
                        BackRebateBean backRebateBean = new Gson().fromJson(response, BackRebateBean.class);
                        List<BackRebateBean.DataBean> data = backRebateBean.getData();
                        BackRebateAdapter backRebateAdapter = new BackRebateAdapter(BackRebateActivity.this, data);
                        rcvBackRebate.setAdapter(backRebateAdapter);
                        break;
                    case 0:
                        Toast.makeText(BackRebateActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
               LogUtil.e(call.toString() + "-" +e.getMessage());
            }
        });
    }

    private void loadBackData(String type, String pageIndex) {
        Id = Tools.getUserLogin(this);
        map = new HashMap<>();
        map.put("Id", Id);
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
        map.put("type", type);
        netUtil.okHttp2Server2(back_url, map);
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
