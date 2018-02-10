package com.gather_excellent_help.ui.activity.credits;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;

import com.gather_excellent_help.bean.LowerMermberBean;
import com.gather_excellent_help.ui.activity.WareListActivity;
import com.gather_excellent_help.ui.adapter.LowerMemberAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.MyNestedScrollView;
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

public class LowerMemberStaticsActivity extends BaseActivity {

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

    private RelativeLayout rl_order_no_zhanwei;
    private TextView tv_order_no_zhanwei;
    @Bind(R.id.ll_lower_member_show)
    LinearLayout ll_lower_member_show;

//    private SwipeRefreshLayout swip_refresh;
//
//    private MyNestedScrollView mynest_scrollview;

    private RelativeLayout rl_lower_member_coallaspe;
    private TextView tv_lower_member_collaspe;
    private ImageView iv_lower_member_arraw;

    private WanRecycleView wan_wards_statistics_s;
    private RecyclerView rcvWardsStatisticsS;

    private String startTime = "";
    private String endTime = "";

    private int whick = 0;

    private String url = Url.BASE_URL + "LowerMemberQuery.aspx";
    private NetUtil netUtil;
    private Map<String, String> map;
    private String Id;//用户ID
    private String pageSize = "10";//------------每页多少
    private String pageNo = "1";//--------------第几页
    private String start_time = "";//----------开始时间
    private String end_time = "";//------------结束时间
    private String user_name = "";//-----------用户名

    private List<LowerMermberBean.DataBean> lowerData;
    private List<LowerMermberBean.DataBean> currData;
    private LowerMemberAdapter lowerMemberAdapter;

    private boolean isLoadMore = false;
    private boolean isCanLoad = true;
    private int page = 1;
    private FullyLinearLayoutManager layoutManager;
    private int lastVisibleItem;
    private String whicks = "";
    private int firstVisbleItem;

    private boolean isAnimationFirst = true;
    private AlertDialog alertDialog;

    private boolean isCoallaspe = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lower_member_statics);
        initView();
        ButterKnife.bind(this);
        initData();
    }

    private void initView() {
//        mynest_scrollview = (MyNestedScrollView) findViewById(R.id.mynest_scrollview);
//        swip_refresh = (SwipeRefreshLayout) findViewById(R.id.swip_refresh);
        rl_order_no_zhanwei = (RelativeLayout) findViewById(R.id.rl_order_no_zhanwei);
        tv_order_no_zhanwei = (TextView) findViewById(R.id.tv_order_no_zhanwei);

        rl_lower_member_coallaspe = (RelativeLayout) findViewById(R.id.rl_lower_member_coallaspe);
        tv_lower_member_collaspe = (TextView) findViewById(R.id.tv_lower_member_collaspe);
        iv_lower_member_arraw = (ImageView) findViewById(R.id.iv_lower_member_arraw);

        wan_wards_statistics_s = (WanRecycleView) findViewById(R.id.wav_lower_wards_statistics);

    }


    private void initData() {
        tvTopTitleName.setText("下级会员统计");
        netUtil = new NetUtil();
        Id = Tools.getUserLogin(this);
        net2Server();
        rcvWardsStatisticsS = wan_wards_statistics_s.getRefreshableView();
        layoutManager = new FullyLinearLayoutManager(this);
        rcvWardsStatisticsS.setLayoutManager(layoutManager);

        //设置刷新相关
        wan_wards_statistics_s.setScrollingWhileRefreshingEnabled(true);
        wan_wards_statistics_s.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        wan_wards_statistics_s.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                isLoadMore = false;
                isCanLoad = true;
                page = 1;
                pageNo = "1";
                net2Server();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }

        });
        netUtil.setOnServerResponseListener(new LowerMemberStaticsActivity.OnServerResponseListener());
        rlExit.setOnClickListener(new MyOnclikListener());
        rlWardTimeStart.setOnClickListener(new MyOnclikListener());
        rlWardTimeEnd.setOnClickListener(new MyOnclikListener());
        tvWardStaticsConfirm.setOnClickListener(new MyOnclikListener());
        rl_lower_member_coallaspe.setOnClickListener(new MyOnclikListener());
        //mynest_scrollview.setOnTouchListener(new MyOnTouchClickListener());
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
                                    Toast.makeText(LowerMemberStaticsActivity.this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
                                }
                            }
                            isCanLoad = false;
                            pageNo = String.valueOf(page);
                            net2Server();
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
     * 展示CatView
     */
    private void showCatView() {
        View inflate = View.inflate(this, R.layout.loading_dialog_view, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflate);
        alertDialog = builder.create();
        if (LowerMemberStaticsActivity.this != null && !LowerMemberStaticsActivity.this.isFinishing()) {
            alertDialog.show();
        }
        alertDialog.getWindow().setLayout(ScreenUtil.getScreenWidth(this) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 隐藏CatView
     */
    private void hindCatView() {
        if (LowerMemberStaticsActivity.this != null && !LowerMemberStaticsActivity.this.isFinishing()) {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
    }

    /**
     * 联网请求的回调
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
            hindCatView();
            if (wan_wards_statistics_s != null && wan_wards_statistics_s.isRefreshing()) {
                wan_wards_statistics_s.onRefreshComplete();
            }
            LowerMermberBean lowerMermberBean = new Gson().fromJson(response, LowerMermberBean.class);
            int statusCode = lowerMermberBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    currData = lowerMermberBean.getData();
                    if (isLoadMore) {
                        page++;
                        lowerData.addAll(currData);
                        lowerMemberAdapter.notifyDataSetChanged();
                    } else {
                        if (currData != null) {
                            if (currData.size() > 0) {
                                rl_order_no_zhanwei.setVisibility(View.GONE);
                            } else {
                                rl_order_no_zhanwei.setVisibility(View.VISIBLE);
                            }
                        }
                        tv_order_no_zhanwei.setVisibility(View.GONE);
                        page = 2;
                        lowerData = currData;
                        lowerMemberAdapter = new LowerMemberAdapter(LowerMemberStaticsActivity.this, lowerData);
                        rcvWardsStatisticsS.setAdapter(lowerMemberAdapter);
                    }
                    isCanLoad = true;
                    break;
                case 0:
                    Toast.makeText(LowerMemberStaticsActivity.this, lowerMermberBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "--" + e.getMessage());
            EncryptNetUtil.startNeterrorPage(LowerMemberStaticsActivity.this);
        }
    }

    /**
     * 联网请求
     */
    private void net2Server() {
        showCatView();
        map = new HashMap<>();
        map.put("Id", Id);
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageNo);
        map.put("start_time", start_time);
        map.put("end_time", end_time);
        map.put("user_name", user_name);
        netUtil.okHttp2Server2(LowerMemberStaticsActivity.this,url, map);
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
                case R.id.rl_lower_member_coallaspe:
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
        Toast.makeText(LowerMemberStaticsActivity.this, "正在查询！", Toast.LENGTH_SHORT).show();
        if (TextUtils.isEmpty(startTime)) {
            Toast.makeText(LowerMemberStaticsActivity.this, "请选择开始年月", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(endTime)) {
            Toast.makeText(LowerMemberStaticsActivity.this, "请选择结束年月", Toast.LENGTH_SHORT).show();
            return;
        }
        String username = etWardStaticsUsername.getText().toString().trim();
        isLoadMore = false;
        isCanLoad = true;
        page = 1;
        pageNo = "1";
        start_time = startTime;
        end_time = endTime;
        user_name = username;
        isLoadMore = false;
        whicks = "query";
        net2Server();
    }

    /**
     * 弹出日期选择的dialog
     *
     * @param which
     */
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

    /**
     * 控制搜索展开和收起
     */
    private void coalapseSearch() {
        if (isCoallaspe) {
            tv_lower_member_collaspe.setText("展开搜索");
            iv_lower_member_arraw.setImageResource(R.drawable.down_lower_expand_icon);
            ll_lower_member_show.setVisibility(View.GONE);
        } else {
            tv_lower_member_collaspe.setText("收起搜索");
            iv_lower_member_arraw.setImageResource(R.drawable.up_lower_collaspe_icon);
            ll_lower_member_show.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 监听页面上的触摸事件
     */
//    public class MyOnTouchClickListener implements View.OnTouchListener {
//
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            int scrollY = view.getScrollY();
//            int height = view.getHeight();
//            int scrollViewMeasuredHeight = mynest_scrollview.getChildAt(0).getMeasuredHeight();
//            switch (motionEvent.getAction()) {
//                case MotionEvent.ACTION_UP:
//                    if ((scrollY + height) == scrollViewMeasuredHeight) {
//                        if (isCanLoad) {
//                            lastVisibleItem = layoutManager
//                                    .findLastVisibleItemPosition();
//                            if (lastVisibleItem + 1 == layoutManager
//                                    .getItemCount()) {
//                                isLoadMore = true;
//                                if (currData != null) {
//                                    if (currData.size() < 10) {
//                                        Toast.makeText(LowerMemberStaticsActivity.this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                                isCanLoad = false;
//                                pageNo = String.valueOf(page);
//                                net2Server();
//                            }
//                        }
//                        break;
//                    }
//            }
//            return false;
//        }
//
//    }
}
