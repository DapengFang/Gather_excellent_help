package com.gather_excellent_help.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.OrderAllBean;
import com.gather_excellent_help.ui.adapter.OrderAllAdapter;
import com.gather_excellent_help.ui.adapter.OrderManagerAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.MyNestedScrollView;
import com.gather_excellent_help.ui.widget.ViewpagerIndicator;
import com.gather_excellent_help.ui.widget.WanRecycleView;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.PhotoUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.roger.catloadinglibrary.CatLoadingView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class OrderActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;

    private WanRecycleView wan_order_manager;
    RecyclerView rcvOrderManager;

    @Bind(R.id.vid_order_manager)
    ViewpagerIndicator vid_order_manager;
    @Bind(R.id.iv_order_no_zhanwei)
    ImageView ivOrderNoZhanwei;
    private boolean isCanLoad = true;

    private OrderAllAdapter orderAllAdapter;
    private int tab_p = 0;//当前的订单的位置

    private String order_url = Url.BASE_URL + "OrderShow.aspx";
    private String tui_url = Url.BASE_URL + "EarnOrder.aspx";
    private NetUtil netUtil;
    private Map<String, String> map;
    private int curr_statue;//订单类型
    private String pageSize = "10";
    private String loginId;
    private int order_type;
    private String pageIndex = "1";
    private int page = 1;
    private boolean isLoaderMore = false;
    private FullyLinearLayoutManager fullyLinearLayoutManager;
    private int lastVisibleItem;
    private List<OrderAllBean.DataBean> allData;
    private Handler handler = new Handler();
    private CatLoadingView catLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        wan_order_manager = (WanRecycleView) findViewById(R.id.wan_order_manager);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        catLoadingView = new CatLoadingView();
        rcvOrderManager = wan_order_manager.getRefreshableView();
        fullyLinearLayoutManager = new FullyLinearLayoutManager(OrderActivity.this);
        rcvOrderManager.setLayoutManager(fullyLinearLayoutManager);

        //设置刷新相关
        wan_order_manager.setScrollingWhileRefreshingEnabled(true);
        wan_order_manager.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        wan_order_manager.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                pageIndex = "1";
                page = 1;
                isLoaderMore = false;
                switch (tab_p) {
                    case 0:
                        isCanLoad = true;
                        curr_statue = 5;
                        net2ServerOrder(curr_statue);
                        break;
                    case 1:
                        isCanLoad = true;
                        curr_statue = 1;
                        net2ServerOrder(curr_statue);
                        break;
                    case 2:
                        isCanLoad = true;
                        curr_statue = 2;
                        net2ServerOrder(curr_statue);
                        break;
                    case 3:
                        isCanLoad = true;
                        curr_statue = 3;
                        net2ServerOrder(curr_statue);
                        break;
                    case 4:
                        isCanLoad = true;
                        curr_statue = 4;
                        net2ServerOrder(curr_statue);
                        break;
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }

        });

        netUtil = new NetUtil();
        vidacatorControll();
        tvTopTitleName.setText("推广赚订单");
        Intent intent = getIntent();
        order_type = intent.getIntExtra("order_type", 7);
        tab_p = intent.getIntExtra("tab_p", -1);
        if (tab_p != -1) {
            vid_order_manager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    vid_order_manager.checkMove(tab_p);
                    TextView tv = (TextView) vid_order_manager.getChildAt(tab_p);
                    tv.setTextColor(Color.RED);
                }
            }, 20);
            if (tab_p == 0) {
                curr_statue = 5;
            } else {
                curr_statue = tab_p;
            }
            isLoaderMore = false;
            net2ServerOrder(curr_statue);
        }
        rlExit.setOnClickListener(new MyOnclickListener());
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                if (wan_order_manager != null) {
                    wan_order_manager.onRefreshComplete();
                }
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1:
                        LogUtil.e(response);
                        pareAllData(response);
                        hindCatView();
                        break;
                    case 0:
                        LogUtil.e(codeStatueBean.getStatusMessage());
                        Toast.makeText(OrderActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        hindCatView();
                        break;
                }

            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "==" + e.getMessage());
            }
        });
        rcvOrderManager.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isCanLoad) {
                        return;
                    }
                    lastVisibleItem = fullyLinearLayoutManager
                            .findLastVisibleItemPosition();
                    if (lastVisibleItem + 1 == fullyLinearLayoutManager
                            .getItemCount()) {
                        isLoaderMore = true;
                        page++;
                        isCanLoad = false;
                        pageIndex = String.valueOf(page);
                        net2ServerOrder(curr_statue);
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

    /**
     * 解析所有订单信息
     *
     * @param response
     */
    private void pareAllData(String response) {
        OrderAllBean orderAllBean = new Gson().fromJson(response, OrderAllBean.class);
        List<OrderAllBean.DataBean> data = orderAllBean.getData();
        isCanLoad = true;
        if (isLoaderMore) {
            if (data.size() == 0) {
                Toast.makeText(OrderActivity.this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
                return;
            } else {
                allData.addAll(data);
            }
            orderAllAdapter.notifyDataSetChanged();
        } else {
            if (data != null) {
                if (data.size() > 0) {
                    ivOrderNoZhanwei.setVisibility(View.GONE);
                } else {
                    ivOrderNoZhanwei.setVisibility(View.VISIBLE);
                }
            }
            allData = data;
            if (order_type == -1) {
                orderAllAdapter = new OrderAllAdapter(OrderActivity.this, allData, curr_statue, -1);
            } else if (order_type == -2) {
                orderAllAdapter = new OrderAllAdapter(OrderActivity.this, allData, curr_statue, -2);
            }
            rcvOrderManager.setAdapter(orderAllAdapter);
            orderAllAdapter.notifyDataSetChanged();
        }

    }

    /**
     * @param curr_statue 联网请求订单信息
     */
    private void net2ServerOrder(int curr_statue) {
        showCatView();
        String curr_url = tui_url;
        if (order_type == -1) {
            curr_url = order_url;
        } else if (order_type == -2) {
            curr_url = tui_url;
        }
        loginId = CacheUtils.getString(this, CacheUtils.LOGIN_VALUE, "");
        map = new HashMap<>();
        map.put("Id", loginId);
        map.put("Type", String.valueOf(curr_statue));
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
        netUtil.okHttp2Server2(curr_url, map);
    }

    /**
     * 展示CatView
     */
    private void showCatView() {
        if (catLoadingView != null) {
            catLoadingView.show(getSupportFragmentManager(), "");
        }
    }

    /**
     * 隐藏CatView
     */
    private void hindCatView() {
        if (catLoadingView != null) {
            catLoadingView.dismiss();
        }
    }


    /**
     * 监听全局点击事件的类
     */
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
        vid_order_manager.setCount(5);
        final int childCount = vid_order_manager.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = vid_order_manager.getChildAt(i);
            final int finalI = i;
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vid_order_manager.checkMove(finalI);
                    for (int j = 0; j < childCount; j++) {
                        if (j != finalI) {
                            TextView tv = (TextView) vid_order_manager.getChildAt(j);
                            tv.setTextColor(Color.parseColor("#333333"));
                        } else {
                            TextView tv = (TextView) v;
                            tv.setTextColor(Color.RED);
                        }
                    }
                    tab_p = finalI;
                    pageIndex = "1";
                    page = 1;
                    isLoaderMore = false;
                    switch (tab_p) {
                        case 0:
                            isCanLoad = true;
                            curr_statue = 5;
                            net2ServerOrder(curr_statue);
                            break;
                        case 1:
                            isCanLoad = true;
                            curr_statue = 1;
                            net2ServerOrder(curr_statue);
                            break;
                        case 2:
                            isCanLoad = true;
                            curr_statue = 2;
                            net2ServerOrder(curr_statue);
                            break;
                        case 3:
                            isCanLoad = true;
                            curr_statue = 3;
                            net2ServerOrder(curr_statue);
                            break;
                        case 4:
                            isCanLoad = true;
                            curr_statue = 4;
                            net2ServerOrder(curr_statue);
                            break;
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
