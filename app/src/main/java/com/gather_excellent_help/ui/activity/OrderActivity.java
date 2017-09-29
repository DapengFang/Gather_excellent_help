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
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.PhotoUtils;
import com.google.gson.Gson;

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
    @Bind(R.id.rcv_order_manager)
    RecyclerView rcvOrderManager;

    @Bind(R.id.vid_order_manager)
    ViewpagerIndicator vid_order_manager;
    @Bind(R.id.iv_order_no_zhanwei)
    ImageView ivOrderNoZhanwei;


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
    private int page;
    private boolean isLoaderMore = false;
    private FullyLinearLayoutManager fullyLinearLayoutManager;
    private int lastVisibleItem;
    private List<OrderAllBean.DataBean> allData;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        netUtil = new NetUtil();
        fullyLinearLayoutManager = new FullyLinearLayoutManager(OrderActivity.this);
        rcvOrderManager.setLayoutManager(fullyLinearLayoutManager);
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
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1:
                        LogUtil.e(response);
                        pareAllData(response);
                        break;
                    case 0:
                        LogUtil.e(codeStatueBean.getStatusMessage());
                        Toast.makeText(OrderActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
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
                    lastVisibleItem = fullyLinearLayoutManager
                            .findLastVisibleItemPosition();

                    if (lastVisibleItem + 1 == fullyLinearLayoutManager
                            .getItemCount()) {
                        isLoaderMore = true;
                        page++;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pageIndex = String.valueOf(page);
                                net2ServerOrder(curr_statue);
                            }
                        }, 1000);
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
        if (isLoaderMore) {
            if (data.size() < 10) {
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
                    isLoaderMore = false;
                    switch (tab_p) {
                        case 0:
                            curr_statue = 5;
                            net2ServerOrder(curr_statue);
                            break;
                        case 1:
                            curr_statue = 1;
                            net2ServerOrder(curr_statue);
                            break;
                        case 2:
                            curr_statue = 2;
                            net2ServerOrder(curr_statue);
                            break;
                        case 3:
                            curr_statue = 3;
                            net2ServerOrder(curr_statue);
                            break;
                        case 4:
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
        if(handler!=null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
