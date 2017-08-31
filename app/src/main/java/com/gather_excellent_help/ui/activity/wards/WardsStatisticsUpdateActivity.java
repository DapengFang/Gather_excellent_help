package com.gather_excellent_help.ui.activity.wards;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.WardStaticsBean;
import com.gather_excellent_help.ui.activity.credits.AccountDetailAvtivity;
import com.gather_excellent_help.ui.adapter.AcccountDetailAdapter;
import com.gather_excellent_help.ui.adapter.WardStaticsAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
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

public class WardsStatisticsUpdateActivity extends BaseActivity {

    @Bind(R.id.rcv_wards_statistics_s)
    RecyclerView rcvWardsStatisticsS;
    @Bind(R.id.iv_order_no_zhanwei)
    ImageView ivOrderNoZhanwei;
    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.tv_ward_statics_start)
    TextView tvWardStaticsStart;
    @Bind(R.id.rl_ward_time_start)
    RelativeLayout rlWardTimeStart;
    @Bind(R.id.tv_ward_statics_end)
    TextView tvWardStaticsEnd;
    @Bind(R.id.rl_ward_time_end)
    RelativeLayout rlWardTimeEnd;
    @Bind(R.id.et_ward_statics_username)
    EditText etWardStaticsUsername;
    @Bind(R.id.tv_ward_statics_confirm)
    TextView tvWardStaticsConfirm;

    private String startTime = "";
    private String endTime = "";

    private int whick = 0;

    private String url = Url.BASE_URL + "RewardSearch.aspx";
    private NetUtil netUtil;
    private Map<String, String> map;
    private String Id;//用户ID
    private String pageSize = "10";//------------每页多少
    private String pageNo = "1";//--------------第几页
    private String start_time = "";//----------开始时间
    private String end_time = "";//------------结束时间
    private String user_name = "";//-----------用户名

    private boolean isLoadMore = false;
    private int page = 1;
    private List<WardStaticsBean.DataBean> wardsData;
    private List<WardStaticsBean.DataBean> currData;
    private WardStaticsAdapter wardStaticsAdapter;
    private FullyLinearLayoutManager layoutManager;
    private double lastVisibleItem;
    private String whicks = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wards_statistics_update);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 解析数据
     */
    private void initData() {
        tvTopTitleName.setText("奖励统计");
        netUtil = new NetUtil();
        Id = Tools.getUserLogin(this);
        ivOrderNoZhanwei.setVisibility(View.VISIBLE);
        layoutManager = new FullyLinearLayoutManager(this);
        rcvWardsStatisticsS.setLayoutManager(layoutManager);
        netUtil.setOnServerResponseListener(new OnServerResponseListener());
        rlExit.setOnClickListener(new MyOnclikListener());
        rlWardTimeStart.setOnClickListener(new MyOnclikListener());
        rlWardTimeEnd.setOnClickListener(new MyOnclikListener());
        tvWardStaticsConfirm.setOnClickListener(new MyOnclikListener());
        rcvWardsStatisticsS.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisibleItem = layoutManager
                            .findLastVisibleItemPosition();

                    if (lastVisibleItem + 1 == layoutManager
                            .getItemCount()) {
                        if (TextUtils.isEmpty(whicks)) {
                            return;
                        }
                        isLoadMore = true;
                        if (currData != null) {
                            if (currData.size() < 10) {
                                Toast.makeText(WardsStatisticsUpdateActivity.this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pageNo = String.valueOf(page);
                                net2Server();
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


    private void showDateDialog(final int which) {
        final DatePicker datePicker = new DatePicker(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择日期")
                .setView(datePicker)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth() + 1;
                        int day = datePicker.getDayOfMonth();
                        String time = year + "-" + month + "-" + day;
                        if (which == -1) {
                            startTime = time;
                            tvWardStaticsStart.setText(startTime);
                        } else {
                            endTime = time;
                            tvWardStaticsEnd.setText(endTime);
                        }

                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public class MyOnclikListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.rl_ward_time_start:
                    whick = -1;
                    showDateDialog(whick);
                    break;
                case R.id.rl_ward_time_end:
                    whick = -2;
                    showDateDialog(whick);
                    break;
                case R.id.tv_ward_statics_confirm:
                    toQuery();
                    break;
            }
        }
    }

    /**
     * 查询奖励统计信息
     */
    private void toQuery() {
        if (TextUtils.isEmpty(startTime)) {
            Toast.makeText(WardsStatisticsUpdateActivity.this, "请选择开始年月", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(endTime)) {
            Toast.makeText(WardsStatisticsUpdateActivity.this, "请选择结束年月", Toast.LENGTH_SHORT).show();
            return;
        }
        String username = etWardStaticsUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(WardsStatisticsUpdateActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        page = 1;
        pageNo = "1";
        start_time = startTime;
        end_time = endTime;
        user_name = username;
        isLoadMore = false;
        whicks = "query";
        net2Server();

    }

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            WardStaticsBean wardStaticsBean = new Gson().fromJson(response, WardStaticsBean.class);
            int statusCode = wardStaticsBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    currData = wardStaticsBean.getData();
                    if (isLoadMore) {
                        page++;
                        wardsData.addAll(currData);
                        wardStaticsAdapter.notifyDataSetChanged();
                    } else {
                        if (currData != null) {
                            if (currData.size() > 0) {
                                ivOrderNoZhanwei.setVisibility(View.GONE);
                            } else {
                                ivOrderNoZhanwei.setVisibility(View.VISIBLE);
                            }
                        }
                        page = 2;
                        wardsData = currData;
                        wardStaticsAdapter = new WardStaticsAdapter(WardsStatisticsUpdateActivity.this, wardsData);
                        rcvWardsStatisticsS.setAdapter(wardStaticsAdapter);
                    }
                    break;
                case 0:
                    Toast.makeText(WardsStatisticsUpdateActivity.this, wardStaticsBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "--" + e.getMessage());
        }
    }

    private void net2Server() {
        map = new HashMap<>();
        map.put("Id", Id);
        map.put("pageSize", pageSize);
        map.put("pageNo", pageNo);
        map.put("start_time", start_time);
        map.put("end_time", end_time);
        map.put("user_name", user_name);
        netUtil.okHttp2Server2(url, map);
    }
}
