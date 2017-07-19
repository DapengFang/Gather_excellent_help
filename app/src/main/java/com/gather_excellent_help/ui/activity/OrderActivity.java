package com.gather_excellent_help.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.adapter.OrderAllAdapter;
import com.gather_excellent_help.ui.adapter.OrderManagerAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.ViewpagerIndicator;
import com.gather_excellent_help.utils.LogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.rcv_order_manager)
    RecyclerView rcvOrderManager;

    @Bind(R.id.vid_order_manager)
    ViewpagerIndicator vid_order_manager;

    private String[] tabs = {"全部","待付款", "已付款", "已完成", "退款/售后"};
    private OrderManagerAdapter orderManagerAdapter;
    private OrderAllAdapter orderAllAdapter;
    private int tab_p = 0;//当前的订单的位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        vidacatorControll();
        tvTopTitleName.setText("商品订单");
        Intent intent = getIntent();
        tab_p = intent.getIntExtra("tab_p", -1);
        if(tab_p!=-1) {
            vid_order_manager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    vid_order_manager.checkMove(tab_p);
                    TextView tv = (TextView) vid_order_manager.getChildAt(tab_p);
                    tv.setTextColor(Color.RED);
                }
            },50);
            if(tab_p==0) {
                initOrderData();
            }else{
                loadOrderData();
            }
        }
        rlExit.setOnClickListener(new MyOnclickListener());
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

    class MyOnTabSelectedListener implements TabLayout.OnTabSelectedListener {


        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            switch (tab.getPosition()) {
                case 0:
                    tab_p = 0;
                    initOrderData();
                    break;
                case 1:
                    tab_p = 1;
                    loadOrderData();
                    break;
                case 2:
                    tab_p = 2;
                    loadOrderData();
                    break;
                case 3:
                    tab_p = 3;
                    loadOrderData();
                    break;
                case 4:
                    tab_p = 4;
                    loadOrderData();
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    private void loadOrderData() {
        orderManagerAdapter = new OrderManagerAdapter(OrderActivity.this);
        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(OrderActivity.this);
        rcvOrderManager.setLayoutManager(fullyLinearLayoutManager);
        rcvOrderManager.setAdapter(orderManagerAdapter);
    }
    private void initOrderData() {
        orderAllAdapter = new OrderAllAdapter(OrderActivity.this);
        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(OrderActivity.this);
        rcvOrderManager.setLayoutManager(fullyLinearLayoutManager);
        rcvOrderManager.setAdapter(orderAllAdapter);
    }

    /**
     * 顶部指示条控制ViewPager
     */
    private void vidacatorControll() {
        final int childCount = vid_order_manager.getChildCount();
        for (int i=0;i<childCount;i++){
            View child = vid_order_manager.getChildAt(i);
            final int finalI = i;
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vid_order_manager.checkMove(finalI);
                    for (int j=0;j<childCount;j++){
                        if(j!=finalI) {
                            TextView tv = (TextView) vid_order_manager.getChildAt(j);
                            tv.setTextColor(Color.parseColor("#55000000"));
                        }else{
                            TextView tv = (TextView) v;
                            tv.setTextColor(Color.RED);
                        }
                    }
                    tab_p = finalI;
                    switch (tab_p) {
                        case 0 :
                         initOrderData();
                            break;
                        case 1:
                            loadOrderData();
                            break;
                        case 2:
                            loadOrderData();
                            break;
                        case 3:
                            loadOrderData();
                            break;
                        case 4:
                            loadOrderData();
                            break;
                    }
                }
            });
        }
    }
}
