package com.gather_excellent_help.ui.activity.suning;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.suning.RemindCountBean;
import com.gather_excellent_help.bean.suning.SuningOrderBean;
import com.gather_excellent_help.bean.suning.SuningOrderConfirmBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.adapter.SuningOrderAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.ViewpagerIndicator;
import com.gather_excellent_help.ui.widget.WanRecycleView;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.ScreenUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class SuningOrderActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;
    private ViewpagerIndicator vid_order_manager;
    private WanRecycleView wanRecycleView;
    private RecyclerView rcv_suning_order_list;

    private LinearLayout ll_suning_show;
    private RelativeLayout rl_order_no_zhanwei;

    private LinearLayout ll_pb_show;
    private TextView tv_pb_show_title;
    private ProgressBar pb_show;
    private FullyLinearLayoutManager fullyLinearLayoutManager;

    private String order_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=GetUserOrderList";//订单列表
    private String confrim_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=recive_myProduct";//确认订单
    private String cancel_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=DeletSingerOrder";//取消订单
    private NetUtil netUtil;
    private Map<String, String> map;

    private String[] tabs = {"待付款", "待发货", "待收货", "待评价", "全部"};

    private String userLogin;
    private String pagesize = "10";//每页显示多少条数据
    private String pageindex = "1";//页数
    private String order_status = "1";//订单状态
    private int tab_p = 0;//当前的订单位置
    private int page = 1;//当前页数
    private boolean isLoaderMore;//是否加载更多
    private boolean isCanLoad = true;//是否可以加载

    private List<SuningOrderBean.DataBean> allData;
    private SuningOrderAdapter suningOrderAdapter;
    private int lastVisibleItem;
    private String whick = "";
    private AlertDialog alertDialog;
    private String pay_status;

    private boolean isShowCat;//刷新页面不让其显示
    private ArrayList<RemindCountBean> remind_counts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suning_order);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
        vid_order_manager = (ViewpagerIndicator) findViewById(R.id.vid_order_manager);
        wanRecycleView = (WanRecycleView) findViewById(R.id.rcv_suning_order_list);
        ll_pb_show = (LinearLayout) findViewById(R.id.ll_pb_show);
        tv_pb_show_title = (TextView) findViewById(R.id.tv_pb_show_title);
        pb_show = (ProgressBar) findViewById(R.id.pb_show);
        ll_suning_show = (LinearLayout) findViewById(R.id.ll_suning_show);
        rl_order_no_zhanwei = (RelativeLayout) findViewById(R.id.rl_order_no_zhanwei);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        pay_status = intent.getStringExtra("pay_status");
        tv_top_title_name.setText("苏宁订单");
        userLogin = Tools.getUserLogin(this);
        netUtil = new NetUtil();
        vidacatorControll();
        final int childCount = vid_order_manager.getChildCount();
        defaultViewIndicator(childCount, 0);
        if (pay_status != null) {
            if (pay_status.equals("2")) {
                isCanLoad = true;
                order_status = "2";
                defaultViewIndicator(childCount, 1);
            } else if (pay_status.equals("1")) {
                isCanLoad = true;
                order_status = "1";
                defaultViewIndicator(childCount, 0);
            }
        }
        getSuningOrderData();
        OnServerResponseListener onServerResponseListener = new OnServerResponseListener();
        netUtil.setOnServerResponseListener(onServerResponseListener);
        rcv_suning_order_list = wanRecycleView.getRefreshableView();
        fullyLinearLayoutManager = new FullyLinearLayoutManager(SuningOrderActivity.this);
        rcv_suning_order_list.setLayoutManager(fullyLinearLayoutManager);

        //设置刷新相关
        wanRecycleView.setScrollingWhileRefreshingEnabled(true);
        wanRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        wanRecycleView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                pageindex = "1";
                page = 1;
                isLoaderMore = false;
                isCanLoad = true;
                getSuningOrderData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }
        });
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        rcv_suning_order_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        pageindex = String.valueOf(page);
                        showLoadMore(0);
                        getSuningOrderData();
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
     * 默认设置顶部指示器
     *
     * @param childCount
     * @param index
     */
    private void defaultViewIndicator(final int childCount, final int index) {
        final TextView v = (TextView) vid_order_manager.getChildAt(index);
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                showDiffIndicator(v, index, childCount);
            }
        }, 200);
    }

    /**
     * 展示加载更多
     */
    private void showLoadMore(int status) {
        if (status == 0) {
            tv_pb_show_title.setText("正在加载中");
            ll_pb_show.setVisibility(View.VISIBLE);
            pb_show.setVisibility(View.VISIBLE);
        } else if (status == 1) {
            tv_pb_show_title.setText("加载完成");
            ll_pb_show.setVisibility(View.GONE);
            pb_show.setVisibility(View.GONE);
        } else if (status == 2) {
            tv_pb_show_title.setText("没有更多数据了");
            ll_pb_show.setVisibility(View.VISIBLE);
            pb_show.setVisibility(View.GONE);
        }
    }

    /**
     * 获取苏宁订单数据
     */
    private void getSuningOrderData() {
        if (!isShowCat) {
            showCatView();
        }
        whick = "order_list";
        map = new HashMap<>();
        map.put("user_id", userLogin);
        map.put("pagesize", pagesize);
        map.put("pageindex", pageindex);
        map.put("order_status", order_status);
        netUtil.okHttp2Server2(order_url, map);
    }

    /**
     * 展示加载中
     */
    private void showCatView() {
        View inflate = View.inflate(this, R.layout.loading_dialog_view, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflate);
        alertDialog = builder.create();
        if (SuningOrderActivity.this != null && !SuningOrderActivity.this.isFinishing()) {
            alertDialog.show();
        }
        alertDialog.getWindow().setLayout(ScreenUtil.getScreenWidth(this) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 隐藏加载中
     */
    private void hindCatView() {
        if (SuningOrderActivity.this != null && !SuningOrderActivity.this.isFinishing()) {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
    }

    /**
     * 监听页面上的点击事件
     */
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
     * 顶部指示条控制ViewPager
     */
    private void vidacatorControll() {
        try {
            vid_order_manager.setCount(5);
            final int childCount = vid_order_manager.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = vid_order_manager.getChildAt(i);
                final int finalI = i;
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDiffIndicator((TextView) v, finalI, childCount);
                        tab_p = finalI;
                        pageindex = "1";
                        page = 1;
                        isLoaderMore = false;
                        switch (tab_p) {
                            case 0:
                                isCanLoad = true;
                                order_status = "1";
                                break;
                            case 1:
                                isCanLoad = true;
                                order_status = "2";
                                break;
                            case 2:
                                isCanLoad = true;
                                order_status = "3";
                                break;
                            case 3:
                                isCanLoad = true;
                                order_status = "4";
                                break;
                            case 4:
                                isCanLoad = true;
                                order_status = "0";
                                break;
                        }
                        getSuningOrderData();
                    }
                });
            }
        } catch (Exception e) {
            LogUtil.e("SuningOrderActivity vidacatorControll error");
            Toast.makeText(SuningOrderActivity.this, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param v
     * @param finalI
     * @param childCount 展示不同的下划线
     */
    private void showDiffIndicator(TextView v, int finalI, int childCount) {
        vid_order_manager.checkMove(finalI);
        for (int j = 0; j < childCount; j++) {
            if (j != finalI) {
                TextView tv = (TextView) vid_order_manager.getChildAt(j);
                tv.setTextColor(Color.parseColor("#333333"));
            } else {
                TextView tv = v;
                tv.setTextColor(Color.RED);
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
            if (!isShowCat) {
                hindCatView();
            }
            isShowCat = false;
            if (whick.equals("order_list")) {
                parseOrderListData(response);
            } else if (whick.equals("cancel_order")) {
                parseCancelOrderData(response);
            } else if (whick.equals("confirm_order")) {
                parderConfirmOrderData(response);
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
        }
    }

    /**
     * 解析确认订单数据
     *
     * @param response
     */
    private void parderConfirmOrderData(String response) {
        SuningOrderConfirmBean suningOrderConfirmBean = new Gson().fromJson(response, SuningOrderConfirmBean.class);
        int statusCode = suningOrderConfirmBean.getStatusCode();
        switch (statusCode) {
            case 1:
                Toast.makeText(SuningOrderActivity.this, "确认订单成功。", Toast.LENGTH_SHORT).show();
                pageindex = "1";
                page = 1;
                isLoaderMore = false;
                isCanLoad = true;
                getSuningOrderData();
                break;
            case 0:
                Toast.makeText(SuningOrderActivity.this, "确认订单失败！！！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析取消订单数据
     *
     * @param response
     */
    private void parseCancelOrderData(String response) {
        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
        int statusCode = codeStatueBean.getStatusCode();
        switch (statusCode) {
            case 1:
                Toast.makeText(SuningOrderActivity.this, "订单取消成功。", Toast.LENGTH_SHORT).show();
                pageindex = "1";
                page = 1;
                isLoaderMore = false;
                isCanLoad = true;
                getSuningOrderData();
                break;
            case 0:
                Toast.makeText(SuningOrderActivity.this, "订单取消失败！！！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析订单列表数据
     *
     * @param response
     */
    private void parseOrderListData(String response) {
        try {
            wanRecycleView.onRefreshComplete();
            SuningOrderBean suningOrderBean = new Gson().fromJson(response, SuningOrderBean.class);
            int statusCode = suningOrderBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    List<SuningOrderBean.DataBean> data = suningOrderBean.getData();
                    isCanLoad = true;
                    if (isLoaderMore) {
                        if (data.size() == 0) {
                            showLoadMore(2);
                            return;
                        } else {
                            showLoadMore(1);
                            allData.addAll(data);
                        }
                        suningOrderAdapter.notifyDataSetChanged();
                    } else {
                        showLoadMore(1);
                        allData = data;
                        if (allData != null && allData.size() > 0) {
                            ll_suning_show.setVisibility(View.VISIBLE);
                            rl_order_no_zhanwei.setVisibility(View.GONE);
                        } else {
                            ll_suning_show.setVisibility(View.GONE);
                            rl_order_no_zhanwei.setVisibility(View.VISIBLE);
                        }
                        suningOrderAdapter = new SuningOrderAdapter(SuningOrderActivity.this, data, order_status);
                        rcv_suning_order_list.setAdapter(suningOrderAdapter);
                        suningOrderAdapter.notifyDataSetChanged();
                    }
                    if (allData != null && allData.size() > 0) {
                        remind_counts = new ArrayList<>();
                        for (int i = 0; i < allData.size(); i++) {
                            RemindCountBean remindCountBean = new RemindCountBean();
                            remindCountBean.setCount(0);
                            remindCountBean.setIs_continue(false);
                            remind_counts.add(remindCountBean);
                        }
                    }
                    if (suningOrderAdapter != null) {
                        suningOrderAdapter.setOnButtonClickListener(new SuningOrderAdapter.OnButtonClickListener() {
                            @Override
                            public void onItemClickListener(View view, int position, int status) {
                                onItemclickHandler(view, position, status);
                            }

                            @Override
                            public void onLeftButtonClick(View view, int position, int status) {
                                onLeftButtonHandler(view, position, status);
                            }

                            @Override
                            public void onRightButtonClick(View view, int position, int status) {
                                onRightButtonHandler(view, position, status);
                            }

                            @Override
                            public void onExtraButtonClick(View view, int position, int status) {

                            }
                        });
                    }
                    break;
                case 0:
                    wanRecycleView.onRefreshComplete();
                    Toast.makeText(SuningOrderActivity.this, suningOrderBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception e) {
            LogUtil.e("SuningOrderActivity parseOrderListData error");
            Toast.makeText(SuningOrderActivity.this, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理右边button
     *
     * @param view
     * @param position
     * @param status
     */
    private void onRightButtonHandler(View view, int position, int status) {
        if (allData != null && allData.size() > 0) {
            SuningOrderBean.DataBean dataBean = allData.get(position);
            if (dataBean != null) {
                double real_amount = dataBean.getReal_amount();
                String order_no = dataBean.getOrder_no();
                int id = dataBean.getId();
                if (status == 1) {
                    toPayOrder(real_amount, order_no, id);
                } else if (status == 2) {
                    remindSend(position);
                } else if (status == 3) {
                    confirmOrder(String.valueOf(id));
                } else if (status == 4) {
                    //评价
                    evaluteOrder();
                }
            }
        }
    }

    /**
     * 评价订单
     */
    private void evaluteOrder() {
        Toast.makeText(SuningOrderActivity.this, "该功能正在开发中。", Toast.LENGTH_SHORT).show();
    }

    /**
     * 处理左边的button
     *
     * @param view
     * @param position
     * @param status
     */
    private void onLeftButtonHandler(View view, int position, int status) {
        if (allData != null && allData.size() > 0) {
            SuningOrderBean.DataBean dataBean = allData.get(position);
            if (dataBean != null) {
                final int id = dataBean.getId();
                if (status == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("温馨提示")
                            .setMessage("你确定要取消的订单吗?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    cancelOrder(String.valueOf(id));
                                }
                            })
                            .setNegativeButton("取消", null);
                    alertDialog = builder.create();
                    if (SuningOrderActivity.this != null && !SuningOrderActivity.this.isFinishing()) {
                        alertDialog.show();
                    }
                } else if (status == 3) {
                    seeOrderDetail(view, position, status);
                }
            }
        }
    }

    /**
     * 点击额外的一个button的操作
     *
     * @param view
     * @param position
     * @param status
     */
    private void onExtraButtonHandler(View view, int position, int status) {
        if (allData != null && allData.size() > 0) {
            SuningOrderBean.DataBean dataBean = allData.get(position);
            if (dataBean != null) {

            }
        }
    }

    /**
     * 点击列表item
     *
     * @param view
     * @param position
     * @param status
     */
    private void onItemclickHandler(View view, int position, int status) {
        if (status > 4) {
            Toast.makeText(SuningOrderActivity.this, "此订单交易关闭，无法查看详情哦~", Toast.LENGTH_SHORT).show();
        } else {
            seeOrderDetail(view, position, status);
        }
    }

    /**
     * 订单支付
     */
    private void toPayOrder(double pay_price, String order_num, int orderId) {
        Intent intent = new Intent(this, CheckStandActivity.class);
        intent.putExtra("pay_price", pay_price);
        intent.putExtra("order_num", order_num);
        intent.putExtra("orderId", orderId);
        startActivity(intent);
        finish();
    }

    /**
     * 取消订单
     */
    private void cancelOrder(String order_id) {
        whick = "cancel_order";
        map = new HashMap<>();
        map.put("user_id", userLogin);
        map.put("order_id", order_id);
        netUtil.okHttp2Server2(cancel_url, map);
    }

    /**
     * 提醒发货
     *
     * @param position
     */
    private void remindSend(int position) {
        if (remind_counts != null && remind_counts.size() > 0) {
            RemindCountBean remindCountBean = remind_counts.get(position);
            int count = remindCountBean.getCount();
            boolean aContinue = remindCountBean.is_continue();
            if (count > 2) {
                if (!aContinue) {
                    Toast.makeText(SuningOrderActivity.this, "超过提醒次数。", Toast.LENGTH_SHORT).show();
                    aContinue = true;
                    remindCountBean.setIs_continue(aContinue);
                }
                return;
            }
            Toast.makeText(this, "提醒成功。", Toast.LENGTH_SHORT).show();
            count++;
            remindCountBean.setCount(count);
        }
    }

    /**
     * 确认订单
     */
    private void confirmOrder(String order_id) {
        whick = "confirm_order";
        map = new HashMap<>();
        map.put("user_id", userLogin);
        map.put("order_id", order_id);
        map.put("order_status", "2");
        netUtil.okHttp2Server2(confrim_url, map);
    }

    /**
     * 查看订单详情
     */
    private void seeOrderDetail(View v, int position, int status) {
        if (allData != null && allData.size() > 0) {
            SuningOrderBean.DataBean dataBean = allData.get(position);
            if (dataBean != null) {
                String orderInfo = new Gson().toJson(dataBean);
                Intent intent = new Intent(SuningOrderActivity.this, SuningOrderDetailActivity.class);
                intent.putExtra("orderInfo", orderInfo);
                startActivity(intent);
            }
        }
    }

    /**
     * 刷新数据
     *
     * @param event
     */
    public void onEvent(AnyEvent event) {
        if (event.getType() == EventType.UPDATA_ORDER_LIST) {
            String msg = "onEventMainThread收到了消息：" + event.getMessage();
            LogUtil.e(msg);
            isShowCat = true;
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
