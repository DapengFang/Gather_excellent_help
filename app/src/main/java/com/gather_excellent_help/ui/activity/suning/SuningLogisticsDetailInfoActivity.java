package com.gather_excellent_help.ui.activity.suning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.suning.SuningLogisticsBean;
import com.gather_excellent_help.ui.adapter.LogisticsInfoAdapter;
import com.gather_excellent_help.ui.adapter.SuningLogisticsInfoAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.WanRecycleView;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class SuningLogisticsDetailInfoActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;

    private WanRecycleView wan_suning_logistics;
    private RecyclerView recyclerView;
    private RelativeLayout rl_order_no_zhanwei;

    private NetUtil netUtil;
    private Map<String, String> map;
    private String logistics_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=Getlogistics";//物流信息接口
    private int order_id;//订单自增id
    private SuningLogisticsInfoAdapter suningLogisticsInfoAdapter;
    private FullyLinearLayoutManager fullyLinearLayoutManager;
    private String userLogin;
    private int article_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_info);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
        wan_suning_logistics = (WanRecycleView) findViewById(R.id.wan_suning_logistics);
        rl_order_no_zhanwei = (RelativeLayout) findViewById(R.id.rl_order_no_zhanwei);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        netUtil = new NetUtil();
        tv_top_title_name.setText("物流信息");
        userLogin = Tools.getUserLogin(this);
        Intent intent = getIntent();
        order_id = intent.getIntExtra("order_id", 0);
        article_id = intent.getIntExtra("article_id", 0);
        recyclerView = wan_suning_logistics.getRefreshableView();

        fullyLinearLayoutManager = new FullyLinearLayoutManager(SuningLogisticsDetailInfoActivity.this);
        recyclerView.setLayoutManager(fullyLinearLayoutManager);

        //设置刷新相关
        wan_suning_logistics.setScrollingWhileRefreshingEnabled(true);
        wan_suning_logistics.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        wan_suning_logistics.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                wan_suning_logistics.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }

        });
        getLogisticsInfoData();
        OnServerResponseListener onServerResponseListener = new OnServerResponseListener();
        netUtil.setOnServerResponseListener(onServerResponseListener);
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
    }

    /**
     * 获取物流信息数据
     */
    private void getLogisticsInfoData() {
        map = new HashMap<>();
        map.put("Id", userLogin);
        map.put("user_id", userLogin);
        map.put("order_id", String.valueOf(order_id));
        map.put("article_id", String.valueOf(article_id));
        netUtil.okHttp2Server2(SuningLogisticsDetailInfoActivity.this, logistics_url, map);
    }

    /**
     * 页面上的点击事件
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
     * 监听联网请求的回调
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            try {
                LogUtil.e("物流信息 = " + response);
                parseLogisticsData(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
            rl_order_no_zhanwei.setVisibility(View.VISIBLE);
            EncryptNetUtil.startNeterrorPage(SuningLogisticsDetailInfoActivity.this);
        }
    }

    /**
     * 解析物流信息
     *
     * @param response
     */
    private void parseLogisticsData(String response) throws Exception {
        SuningLogisticsBean suningLogisticsBean = new Gson().fromJson(response, SuningLogisticsBean.class);
        int statusCode = suningLogisticsBean.getStatusCode();
        switch (statusCode) {
            case 1:
                List<SuningLogisticsBean.DataBean> data = suningLogisticsBean.getData();
                if (data != null && data.size() > 0) {
                    rl_order_no_zhanwei.setVisibility(View.GONE);
                    SuningLogisticsBean.DataBean dataBean = data.get(0);
                    List<SuningLogisticsBean.DataBean.OrderItemIdsBean> orderItemIds = dataBean.getOrderItemIds();
                    suningLogisticsInfoAdapter = new SuningLogisticsInfoAdapter(SuningLogisticsDetailInfoActivity.this, data, orderItemIds);
                    recyclerView.setAdapter(suningLogisticsInfoAdapter);
                } else {
                    rl_order_no_zhanwei.setVisibility(View.VISIBLE);
                }
                break;
            case 0:
                Toast.makeText(SuningLogisticsDetailInfoActivity.this, suningLogisticsBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                rl_order_no_zhanwei.setVisibility(View.VISIBLE);
                break;
        }
    }
}
