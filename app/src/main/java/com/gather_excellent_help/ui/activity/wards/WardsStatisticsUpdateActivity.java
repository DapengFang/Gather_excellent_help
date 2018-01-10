package com.gather_excellent_help.ui.activity.wards;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
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
import com.gather_excellent_help.ui.activity.credits.LowerMemberStaticsActivity;
import com.gather_excellent_help.ui.adapter.AcccountDetailAdapter;
import com.gather_excellent_help.ui.adapter.WardStaticsAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.MyNestedScrollView;
import com.gather_excellent_help.ui.widget.WanRecycleView;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class WardsStatisticsUpdateActivity extends BaseActivity {

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
    @Bind(R.id.ll_wards_statics_show)
    LinearLayout llWardsStaticsShow;

    private RecyclerView rcvWardsStatisticsS;
    private WanRecycleView wav_lower_wards_statistics;


    private RelativeLayout rl_wards_statistics_coallaspe;
    private TextView tv_wards_statistics_collaspe;
    private ImageView iv_wards_statistics_arraw;

    private RelativeLayout rl_order_no_zhanwei;
    private TextView tv_order_no_zhanwei;

    private String startTime = "";
    private String endTime = "";

    private boolean isCanLoad = true;

    private int whick = 0;
    private String url = Url.BASE_URL + "RewardSearch.aspx";
    private NetUtil netUtil;
    private Map<String, String> map;
    private String Id;//用户ID
    private String pageSize = "10";//------------每页多少
    private String start_time = "";//----------开始时间
    private String end_time = "";//------------结束时间
    private String user_name = "";//-----------用户名
    private String pageNos = "1";//--------------第几页

    private boolean isLoadMore = false;
    private int page = 1;
    private List<WardStaticsBean.DataBean> wardsData;
    private List<WardStaticsBean.DataBean> currData;
    private WardStaticsAdapter wardStaticsAdapter;
    private FullyLinearLayoutManager layoutManager;
    private double lastVisibleItem;
    private String whicks = "";

    private boolean isCoallaspe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wards_statistics_update);
        initView();
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        wav_lower_wards_statistics = (WanRecycleView) findViewById(R.id.wav_lower_wards_statistics);

        rl_wards_statistics_coallaspe = (RelativeLayout) findViewById(R.id.rl_wards_statistics_coallaspe);
        tv_wards_statistics_collaspe = (TextView) findViewById(R.id.tv_wards_statistics_collaspe);
        iv_wards_statistics_arraw = (ImageView) findViewById(R.id.iv_wards_statistics_arraw);
        rl_order_no_zhanwei = (RelativeLayout)findViewById(R.id.rl_order_no_zhanwei);
        tv_order_no_zhanwei = (TextView)findViewById(R.id.tv_order_no_zhanwei);
    }

    /**
     * 解析数据
     */
    private void initData() {
        tvTopTitleName.setText("奖励统计");
        netUtil = new NetUtil();
        Id = Tools.getUserLogin(this);
        pageNos = "1";
        loadNetData();
        rcvWardsStatisticsS = wav_lower_wards_statistics.getRefreshableView();
        layoutManager = new FullyLinearLayoutManager(this);
        rcvWardsStatisticsS.setLayoutManager(layoutManager);

        //设置刷新相关
        wav_lower_wards_statistics.setScrollingWhileRefreshingEnabled(true);
        wav_lower_wards_statistics.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        wav_lower_wards_statistics.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                isLoadMore = false;
                isCanLoad = true;
                page = 1;
                pageNos = "1";
                loadNetData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }

        });
        rlExit.setOnClickListener(new MyOnclikListener());
        rlWardTimeStart.setOnClickListener(new MyOnclikListener());
        rlWardTimeEnd.setOnClickListener(new MyOnclikListener());
        tvWardStaticsConfirm.setOnClickListener(new MyOnclikListener());
        rl_wards_statistics_coallaspe.setOnClickListener(new MyOnclikListener());
        rcvWardsStatisticsS.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isCanLoad) {
                        lastVisibleItem = layoutManager
                                .findLastVisibleItemPosition();
                        if (lastVisibleItem + 1 == layoutManager
                                .getItemCount()) {
                            isLoadMore = true;
                            if (currData != null) {
                                if (currData.size() < 10) {
                                    Toast.makeText(WardsStatisticsUpdateActivity.this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
                                }
                            }
                            isCanLoad = false;
                            pageNos = String.valueOf(page);
                            loadNetData();
                        }
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
     * 控制搜索展开和收起
     */
    private void coalapseSearch() {
        if (isCoallaspe) {
            tv_wards_statistics_collaspe.setText("展开搜索");
            iv_wards_statistics_arraw.setImageResource(R.drawable.down_lower_expand_icon);
            llWardsStaticsShow.setVisibility(View.GONE);
        } else {
            tv_wards_statistics_collaspe.setText("收起搜索");
            iv_wards_statistics_arraw.setImageResource(R.drawable.up_lower_collaspe_icon);
            llWardsStaticsShow.setVisibility(View.VISIBLE);
        }
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
                case R.id.rl_wards_statistics_coallaspe:
                    isCoallaspe = !isCoallaspe;
                    coalapseSearch();
                    break;
            }
        }
    }

    /**
     * 查询奖励统计信息
     */
    private void toQuery() {
        tvWardStaticsConfirm.setClickable(false);
        if (TextUtils.isEmpty(startTime)) {
            tvWardStaticsConfirm.setClickable(true);
            Toast.makeText(WardsStatisticsUpdateActivity.this, "请选择开始年月", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(endTime)) {
            tvWardStaticsConfirm.setClickable(true);
            Toast.makeText(WardsStatisticsUpdateActivity.this, "请选择结束年月", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(WardsStatisticsUpdateActivity.this, "正在查询！", Toast.LENGTH_SHORT).show();
        String username = etWardStaticsUsername.getText().toString().trim();
        isLoadMore = false;
        isCanLoad = true;
        page = 1;
        pageNos = "1";
        start_time = startTime;
        end_time = endTime;
        user_name = username;
        isLoadMore = false;
        whicks = "query";
        loadNetData();
    }

//    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {
//
//        @Override
//        public void getSuccessResponse(String response) {
//            LogUtil.e(response);
//            tvWardStaticsConfirm.setClickable(true);
//            WardStaticsBean wardStaticsBean = new Gson().fromJson(response, WardStaticsBean.class);
//            int statusCode = wardStaticsBean.getStatusCode();
//            switch (statusCode) {
//                case 1:
//                    if (whicks.equals("query") && page == 1) {
//                        Toast.makeText(WardsStatisticsUpdateActivity.this, "查询完成！", Toast.LENGTH_SHORT).show();
//                    }
//                    currData = wardStaticsBean.getData();
//                    if (isLoadMore) {
//                        page++;
//                        if (currData.size() == 0) {
//                            Toast.makeText(WardsStatisticsUpdateActivity.this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
//                        } else {
//                            wardsData.addAll(currData);
//                            wardStaticsAdapter.notifyDataSetChanged();
//                            isCanLoad = true;
//                        }
//                    } else {
//                        if (currData != null) {
//                            if (currData.size() > 0) {
//                                rl_order_no_zhanwei.setVisibility(View.GONE);
//                            } else {
//                                rl_order_no_zhanwei.setVisibility(View.VISIBLE);
//                            }
//                        }
//                        tv_order_no_zhanwei.setVisibility(View.GONE);
//                        isCanLoad = true;
//                        page = 2;
//                        wardsData = currData;
//                        wardStaticsAdapter = new WardStaticsAdapter(WardsStatisticsUpdateActivity.this, wardsData);
//                        rcvWardsStatisticsS.setAdapter(wardStaticsAdapter);
//                    }
//                    break;
//                case 0:
//                    Toast.makeText(WardsStatisticsUpdateActivity.this, wardStaticsBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//
//        @Override
//        public void getFailResponse(Call call, Exception e) {
//            tvWardStaticsConfirm.setClickable(true);
//            LogUtil.e(call.toString() + "--" + e.getMessage());
//        }
//    }

    private void loadNetData() {
        OkHttpUtils
                .post()
                .url(url)
                .addParams("Id", Id)
                .addParams("pageSize", pageSize)
                .addParams("pageIndex", pageNos)
                .addParams("start_time", start_time)
                .addParams("end_time", end_time)
                .addParams("user_name", user_name)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        tvWardStaticsConfirm.setClickable(true);
                        LogUtil.e(call.toString() + "--" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.e(response);
                        if(wav_lower_wards_statistics!=null && wav_lower_wards_statistics.isRefreshing()) {
                            wav_lower_wards_statistics.onRefreshComplete();
                        }
                        tvWardStaticsConfirm.setClickable(true);
                        WardStaticsBean wardStaticsBean = new Gson().fromJson(response, WardStaticsBean.class);
                        int statusCode = wardStaticsBean.getStatusCode();
                        switch (statusCode) {
                            case 1:
                                if (whicks.equals("query") && page == 1) {
                                    Toast.makeText(WardsStatisticsUpdateActivity.this, "查询完成！", Toast.LENGTH_SHORT).show();
                                }
                                currData = wardStaticsBean.getData();
                                if (isLoadMore) {
                                    page++;
                                    if (currData.size() == 0) {
                                        Toast.makeText(WardsStatisticsUpdateActivity.this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        wardsData.addAll(currData);
                                        wardStaticsAdapter.notifyDataSetChanged();
                                        isCanLoad = true;
                                    }
                                } else {
                                    if (currData != null) {
                                        if (currData.size() > 0) {
                                            rl_order_no_zhanwei.setVisibility(View.GONE);
                                        } else {
                                            rl_order_no_zhanwei.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    tv_order_no_zhanwei.setVisibility(View.GONE);
                                    isCanLoad = true;
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
                });
    }

}
