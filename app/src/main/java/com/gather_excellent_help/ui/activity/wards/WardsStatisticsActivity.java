package com.gather_excellent_help.ui.activity.wards;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.WardStaticsBean;
import com.gather_excellent_help.ui.activity.WareListActivity;
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

public class WardsStatisticsActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.tv_ward_statistics_search)
    TextView tvWardStatisticsSearch;
    @Bind(R.id.rcv_wards_statistics)
    RecyclerView rcvWardsStatistics;
    @Bind(R.id.et_ward_statics_starttime)
    EditText etWardStaticsStarttime;
    @Bind(R.id.et_ward_statics_endtime)
    EditText etWardStaticsEndtime;
    @Bind(R.id.et_ward_statics_user)
    EditText etWardStaticsUser;
    private String url = Url.BASE_URL + "RewardSearch.aspx";
    private NetUtil netUtil;
    private Map<String, String> map;
    private String Id;//用户ID
    private String pageSize = "10";//------------每页多少
    private String pageNo = "1";//--------------第几页
    private String start_time = "";//----------开始时间
    private String end_time = "";//------------结束时间
    private String user_name = "";//-----------用户名


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wards_statistics);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 数据初始化
     */
    private void initData() {
        Id = Tools.getUserLogin(this);
        tvTopTitleName.setText("奖励统计");
        rlExit.setOnClickListener(new MyOnclickListener());
        tvWardStatisticsSearch.setOnClickListener(new MyOnclickListener());
        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        rcvWardsStatistics.setLayoutManager(fullyLinearLayoutManager);
        netUtil = new NetUtil();
        map = new HashMap<>();
        net2Server();
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                WardStaticsBean wardStaticsBean = new Gson().fromJson(response, WardStaticsBean.class);
                int statusCode = wardStaticsBean.getStatusCode();
                switch (statusCode) {
                    case 1:
                        List<WardStaticsBean.DataBean> wardsData = wardStaticsBean.getData();
                        WardStaticsAdapter wardStaticsAdapter = new WardStaticsAdapter(WardsStatisticsActivity.this, wardsData);
                        rcvWardsStatistics.setAdapter(wardStaticsAdapter);
                        break;
                    case 0:
                        Toast.makeText(WardsStatisticsActivity.this, wardStaticsBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "--" + e.getMessage());
            }
        });
    }

    private void net2Server() {
        map.put("Id", Id);
        map.put("pageSize", pageSize);
        map.put("pageNo", pageNo);
        map.put("start_time", start_time);
        map.put("end_time", end_time);
        map.put("user_name", user_name);
        netUtil.okHttp2Server2(url, map);
    }

    public class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_ward_statistics_search:
                    srarhWardsStatistics();
                    break;
            }
        }
    }

    /**
     * 搜索奖励统计的信息
     */
    private void srarhWardsStatistics() {
        start_time = etWardStaticsStarttime.getText().toString().trim();
        end_time = etWardStaticsEndtime.getText().toString().trim();
        user_name  = etWardStaticsUser.getText().toString().trim();
        if(TextUtils.isEmpty(start_time)) {
            Toast.makeText(WardsStatisticsActivity.this, "开始时间为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(end_time)) {
            Toast.makeText(WardsStatisticsActivity.this, "结束时间为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(user_name)) {
            Toast.makeText(WardsStatisticsActivity.this, "用户名为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        net2Server();
    }


    private void showDateDialog() {
        final DatePicker datePicker = new DatePicker(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择时间")
                .setView(datePicker)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth() + 1;
                        int day = datePicker.getDayOfMonth();
                        Toast.makeText(WardsStatisticsActivity.this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
