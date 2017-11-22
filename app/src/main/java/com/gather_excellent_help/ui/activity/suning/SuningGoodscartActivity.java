package com.gather_excellent_help.ui.activity.suning;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.suning.SuningGoodscartBean;
import com.gather_excellent_help.db.suning.SqliteServiceManager;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.adapter.SuningGoodscartAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.WanRecycleView;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class SuningGoodscartActivity extends BaseActivity {

    private TextView tv_top_title_name;
    private RelativeLayout rl_exit;

    private SqliteServiceManager manager;
    private WanRecycleView wan_suning_goodscart;
    private RecyclerView recyclerView;

    private CheckBox cb_shopcart_checkall;
    private TextView tv_shop_total_price;
    private TextView tv_topay;
    private List<SuningGoodscartBean.DataBean> data;

    private boolean isCheckAll = false;
    private boolean isTopay = false;
    private boolean edit_status = true;
    private double total_price;

    private RelativeLayout rl_zhuangtai_right;
    private TextView tv_zhuangtai_right;
    private LinearLayout ll_goodscast_price_show;
    private SuningGoodscartAdapter suningGoodscartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suning_goodscart);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        wan_suning_goodscart = (WanRecycleView) findViewById(R.id.wan_suning_goodscart);
        cb_shopcart_checkall = (CheckBox) findViewById(R.id.cb_shopcart_checkall);
        tv_shop_total_price = (TextView) findViewById(R.id.tv_shop_total_price);
        tv_topay = (TextView) findViewById(R.id.tv_topay);

        rl_zhuangtai_right = (RelativeLayout) findViewById(R.id.rl_zhuangtai_right);
        tv_zhuangtai_right = (TextView) findViewById(R.id.tv_zhuangtai_right);

        ll_goodscast_price_show = (LinearLayout) findViewById(R.id.ll_goodscast_price_show);
    }

    /**
     * 初始哈数据
     */
    private void initData() {
        tv_top_title_name.setText("购物车");
        setTopEditShow();
        recyclerView = wan_suning_goodscart.getRefreshableView();
        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        if (wan_suning_goodscart.isRefreshing()) {
            wan_suning_goodscart.onRefreshComplete();
        }

        //设置刷新相关
        wan_suning_goodscart.setScrollingWhileRefreshingEnabled(true);
        wan_suning_goodscart.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        wan_suning_goodscart.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }

        });
        getGoodscartData();
        checkAll_none();
        checkOnly_check();
        getTotalPrice();
        setCheckStatus();
        if (data != null && data.size() > 0) {
            suningGoodscartAdapter = new SuningGoodscartAdapter(this, data);
            recyclerView.setAdapter(suningGoodscartAdapter);
            suningGoodscartAdapter.setOnUpdatePriceListener(new SuningGoodscartAdapter.OnUpdatePriceListener() {
                @Override
                public void onUpdateData() {
                    toUpdateGoodscart();
                }
            });
        } else {
            if (recyclerView != null) {
                data = new ArrayList<>();
                suningGoodscartAdapter = new SuningGoodscartAdapter(this, data);
                recyclerView.setAdapter(suningGoodscartAdapter);
            }
            isCheckAll = false;
            isTopay = false;
            total_price = 0;
            setCheckStatus();
            showTopay();
        }

        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        cb_shopcart_checkall.setOnClickListener(myonclickListener);
        rl_zhuangtai_right.setOnClickListener(myonclickListener);
        tv_topay.setOnClickListener(myonclickListener);

    }

    /**
     * 获取购物车数据
     */
    private void getGoodscartData() {
        if (manager == null) {
            manager = new SqliteServiceManager(this);
        }
        List<Map<String, String>> cartlist = manager.selectAllGoods(null);
        LogUtil.e("cartList = " + cartlist.size());
        if (cartlist != null && cartlist.size() > 0) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("data", cartlist);
            String s = new Gson().toJson(hashMap);
            LogUtil.e(s);
            SuningGoodscartBean suningGoodscartBean = new Gson().fromJson(s, SuningGoodscartBean.class);
            data = suningGoodscartBean.getData();
        } else {
            data = new ArrayList<>();
        }
    }


    /**
     * 设置顶部的编辑状态
     */
    private void setTopEditShow() {
        if (edit_status) {
            tv_zhuangtai_right.setText("编辑");
            ll_goodscast_price_show.setVisibility(View.VISIBLE);
            tv_topay.setText("去结算");
        } else {
            tv_zhuangtai_right.setText("完成");
            ll_goodscast_price_show.setVisibility(View.GONE);
            tv_topay.setText("删除");
        }
    }

    /**
     * 更新购物车相关数据展示
     */
    private void toUpdateGoodscart() {
        getGoodscartData();
        checkAll_none();
        checkOnly_check();
        getTotalPrice();
        if (data != null && data.size() > 0) {
            setCheckStatus();
        } else {
            isCheckAll = false;
            total_price = 0;
            setCheckStatus();
        }
    }

    /**
     * 全选和非全选
     */
    public void checkAll_none() {
        if (data != null && data.size() > 0) {
            int number = 0;
            for (int i = 0; i < data.size(); i++) {
                SuningGoodscartBean.DataBean dataBean = data.get(i);
                boolean bool = Tools.getInt2Boolean(dataBean.getProduct_check());
                if (!bool) {//只要有一个不被选中，就设置全选为非勾选状态
                    //没选择的
                    isCheckAll = false;
                    total_price = 0;
                } else {
                    //选中的
                    number += 1;
                }
            }
            if (number == data.size()) {
                isCheckAll = true;
            }
        } else {
            isCheckAll = false;
            total_price = 0;
        }
    }

    /*
  * 有一个选中，去结算背景改变
  * */
    public void checkOnly_check() {
        if (data != null && data.size() > 0) {
            int num = 0;
            for (int i = 0; i < data.size(); i++) {
                SuningGoodscartBean.DataBean dataBean = data.get(i);
                String product_check = dataBean.getProduct_check();
                boolean bool = Tools.getInt2Boolean(product_check);
                if (bool) {
                    num++;
                }
            }
            if (num > 0) {
                isTopay = true;
            } else {
                isTopay = false;
            }
        } else {
            isTopay = false;
        }
        showTopay();
    }

    /**
     * 是否能够支付
     */
    private void showTopay() {
        if (isTopay) {
            tv_topay.setClickable(true);
            tv_topay.setBackgroundColor(Color.parseColor("#ff1100"));
        } else {
            tv_topay.setClickable(false);
            tv_topay.setBackgroundColor(Color.parseColor("#999999"));
        }
    }

    /**
     * 获取总计价格
     */
    public void getTotalPrice() {
        total_price = 0;
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                SuningGoodscartBean.DataBean dataBean = data.get(i);
                String product_check = dataBean.getProduct_check();
                boolean bool = Tools.getInt2Boolean(product_check);
                if (bool) {
                    String product_sprice = dataBean.getProduct_sprice();
                    String product_num = dataBean.getProduct_num();
                    double price = Double.valueOf(product_sprice);
                    int num = Integer.parseInt(product_num);
                    total_price += price * num;
                }
            }
            LogUtil.e("total_price = " + total_price);
        } else {
            total_price = 0;
        }
    }

    /**
     * 设置下部状态栏
     */
    private void setCheckStatus() {
        DecimalFormat df = new DecimalFormat("#0.00");
        if (isCheckAll) {
            cb_shopcart_checkall.setChecked(true);
        } else {
            cb_shopcart_checkall.setChecked(false);
        }
        tv_shop_total_price.setText(String.valueOf(df.format(total_price)));
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
                case R.id.cb_shopcart_checkall:
                    if (isCheckAll) {
                        isCheckAll = false;
                    } else {
                        isCheckAll = true;
                    }
                    if (manager == null) {
                        manager = new SqliteServiceManager(SuningGoodscartActivity.this);
                    }
                    String check = "";
                    if (isCheckAll) {
                        check = "1";
                    } else {
                        check = "0";
                    }
                    if (data != null && data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                            SuningGoodscartBean.DataBean dataBean = data.get(i);
                            String id = dataBean.getId();
                            manager.updateGoodsCheck(new String[]{check, id});
                        }
                    }
                    initData();
                    break;
                case R.id.rl_zhuangtai_right:
                    if (edit_status) {
                        edit_status = false;
                        tv_zhuangtai_right.setText("完成");
                        ll_goodscast_price_show.setVisibility(View.GONE);
                        tv_topay.setText("删除");
                    } else {
                        edit_status = true;
                        tv_zhuangtai_right.setText("编辑");
                        ll_goodscast_price_show.setVisibility(View.VISIBLE);
                        tv_topay.setText("去结算");
                    }
                    break;
                case R.id.tv_topay:
                    if (edit_status) {
                        //提交订单
                        getGoodscartData();
                        if (data != null && data.size() > 0) {
                            SuningGoodscartBean suningGoodscartBean = new SuningGoodscartBean();
                            List<SuningGoodscartBean.DataBean> ndata = new ArrayList<>();
                            String cart_json = "";
                            for (int i = 0; i < data.size(); i++) {
                                SuningGoodscartBean.DataBean dataBean = data.get(i);
                                String product_check = dataBean.getProduct_check();
                                boolean b = Tools.getInt2Boolean(product_check);
                                if (b) {
                                    ndata.add(dataBean);
                                    suningGoodscartBean.setData(ndata);
                                }
                            }
                            if (ndata.size() > 0) {
                                cart_json = new Gson().toJson(suningGoodscartBean);
                                LogUtil.e("cart_json = " + cart_json);
                            }
                            Intent intent = new Intent(SuningGoodscartActivity.this, OrderCartConfirmActivity.class);
                            intent.putExtra("cart_json", cart_json);
                            startActivity(intent);


                        }
                    } else {
                        //删除购物车数据
                        if (suningGoodscartAdapter != null) {
                            List<SuningGoodscartBean.DataBean> data = suningGoodscartAdapter.getData();
                            if (data != null && data.size() > 0) {
                                deletCheckGoodsCart(data);
                            }
                            edit_status = true;
                            initData();
                        }

                    }
                    break;
            }
        }
    }

    /**
     * 删除选中的商品
     *
     * @param data
     */
    private void deletCheckGoodsCart(List<SuningGoodscartBean.DataBean> data) {
        for (int i = 0; i < data.size(); i++) {
            SuningGoodscartBean.DataBean dataBean = data.get(i);
            String product_check = dataBean.getProduct_check();
            boolean b = Tools.getInt2Boolean(product_check);
            if (b) {
                String id = dataBean.getId();
                if (manager == null) {
                    manager = new SqliteServiceManager(SuningGoodscartActivity.this);
                }
                manager.deleteGoods(new String[]{id});
            }
        }
    }

    /**
     * 刷新数据
     *
     * @param event
     */
    public void onEvent(AnyEvent event) {
        if (event.getType() == EventType.CLEAR_ALL_GOODSCART) {
            String msg = "onEventMainThread收到了消息：" + event.getMessage();
            LogUtil.e(msg);
            getGoodscartData();
            if (data != null && data.size() > 0) {
                deletCheckGoodsCart(data);
            } else {
                manager.deleteAllGoods();
            }
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
