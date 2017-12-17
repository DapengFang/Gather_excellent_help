package com.gather_excellent_help.presenter.homepresenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.ActivityListBean;
import com.gather_excellent_help.presenter.BasePresenter;
import com.gather_excellent_help.ui.activity.OrderActivity;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.ui.activity.WebRecordActivity;
import com.gather_excellent_help.ui.activity.suning.SuningDetailActivity;
import com.gather_excellent_help.ui.widget.DividerItemDecoration;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.MyNestedScrollView;
import com.gather_excellent_help.update.HomeActivityListAdapter;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2017/8/22.
 */

public class ActivityListPresenter extends BasePresenter {

    private Context context;
    private RecyclerView rcvHomeActivityList;
    private MyNestedScrollView myNestedScrollView;
    private String activity_url = Url.BASE_URL + "IndexAllGoods.aspx";
    private String pageSize = "10";
    private String pageIndex = "1";
    private NetUtil netUtil;
    private Map<String, String> map;
    private List<ActivityListBean.DataBean> activityData;
    private boolean isLoadMore = false;
    private int page = 1;
    private List<ActivityListBean.DataBean> currData;
    private HomeActivityListAdapter homeActivityListAdapter;
    private FullyLinearLayoutManager layoutManager;
    private int lastVisibleItem;
    private LinearLayout ll_home_loadmore;

    public ActivityListPresenter(Context context, RecyclerView rcvHomeActivityList, MyNestedScrollView myNestedScrollView, LinearLayout ll_home_loadmore) {
        this.context = context;
        this.rcvHomeActivityList = rcvHomeActivityList;
        this.myNestedScrollView = myNestedScrollView;
        this.ll_home_loadmore = ll_home_loadmore;
        netUtil = new NetUtil();
    }

    @Override
    public View initView() {
        return rcvHomeActivityList;
    }

    @Override
    public void initData() {
        layoutManager = new FullyLinearLayoutManager(context);
        rcvHomeActivityList.setLayoutManager(layoutManager);
        isLoadMore = false;
        net2Server();
        netUtil.setOnServerResponseListener(new MyOnServerResponseListener());
    }

    private void net2Server() {
        map = new HashMap<>();
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
        netUtil.okHttp2Server2(activity_url, map);
    }

    public class MyOnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            if (context != null) {
                parseData(response);
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            if (context != null) {
                Toast.makeText(context, "请检查你的网络连接是否正常！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseData(String response) {
        LogUtil.e("活动列表 === " + response);
        ActivityListBean activityListBean = new Gson().fromJson(response, ActivityListBean.class);
        int statusCode = activityListBean.getStatusCode();
        switch (statusCode) {
            case 1:
                if (isLoadMore) {
                    page++;
                    LogUtil.e("page == " + page);
                    currData = activityListBean.getData();
                    activityData.addAll(currData);
                    homeActivityListAdapter.notifyDataSetChanged();
                } else {
                    currData = activityListBean.getData();
                    activityData = currData;
                    homeActivityListAdapter = new HomeActivityListAdapter(context, activityData);
                    rcvHomeActivityList.setAdapter(homeActivityListAdapter);
                    page = 2;
                }
                ll_home_loadmore.setVisibility(View.GONE);
                myNestedScrollView.setOnTouchListener(new MyOnTouchClickListener());

                homeActivityListAdapter.setOnItemclickListener(new HomeActivityListAdapter.OnItemclickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        DecimalFormat df = new DecimalFormat("#0.00");
                        ActivityListBean.DataBean dataBean = activityData.get(position);
                        int site_id = dataBean.getSite_id();
                        int article_id = dataBean.getArticle_id();
                        String couponsUrl = dataBean.getCouponsUrl();
                        String link_url = dataBean.getLink_url();
                        String goods_id = dataBean.getProductId();
                        String goods_img = dataBean.getImg_url();
                        String goods_title = dataBean.getTitle();
                        double sell_price = dataBean.getSell_price();
                        double market_price = dataBean.getMarket_price();
                        int couponsPrice = dataBean.getCouponsPrice();
                        if (site_id == 1) {
                            //淘宝
                            if (couponsPrice > 0) {
                                if (couponsUrl != null && !TextUtils.isEmpty(couponsUrl)) {
                                    Intent intent = new Intent(context, WebActivity.class);
                                    intent.putExtra("web_url", couponsUrl);
                                    intent.putExtra("url", link_url);
                                    intent.putExtra("goods_id", goods_id);
                                    intent.putExtra("goods_img", goods_img);
                                    intent.putExtra("goods_title", goods_title);
                                    intent.putExtra("goods_price", df.format(sell_price) + "");
                                    intent.putExtra("goods_coupon", String.valueOf(couponsPrice));
                                    intent.putExtra("goods_coupon_url", couponsUrl);
                                    context.startActivity(intent);
                                }
                            } else {
                                Intent intent = new Intent(context, WebRecordActivity.class);
                                intent.putExtra("url", link_url);
                                intent.putExtra("goods_id", goods_id);
                                intent.putExtra("goods_img", goods_img);
                                intent.putExtra("goods_title", goods_title);
                                intent.putExtra("goods_price", df.format(sell_price) + "");
                                context.startActivity(intent);
                            }
                        } else if (site_id == 2) {
                            //苏宁
                            Intent intent = new Intent(context, SuningDetailActivity.class);
                            intent.putExtra("article_id", article_id);
                            intent.putExtra("goods_id", goods_id);
                            intent.putExtra("goods_img", goods_img);
                            intent.putExtra("goods_title", goods_title);
                            intent.putExtra("goods_price", df.format(sell_price) + "");
                            intent.putExtra("c_price", df.format(market_price) + "");
                            context.startActivity(intent);
                            LogUtil.e(article_id + "--" + goods_id + "--" + goods_img + "--" + goods_title + "--" + sell_price + "--" + market_price);
                        }
                    }
                });
                break;
            case 0:
                Toast.makeText(context, activityListBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class MyOnTouchClickListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int scrollY = view.getScrollY();
            int height = view.getHeight();
            int scrollViewMeasuredHeight = myNestedScrollView.getChildAt(0).getMeasuredHeight();
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    if ((scrollY + height) == scrollViewMeasuredHeight) {
                        isLoadMore = true;
                        ll_home_loadmore.setVisibility(View.VISIBLE);
                        ll_home_loadmore.getChildAt(0).setVisibility(View.VISIBLE);
                        TextView tv = (TextView) ll_home_loadmore.getChildAt(1);
                        tv.setText("正在加载中");
                        ll_home_loadmore.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (currData.size() < Integer.parseInt(pageSize)) {
                                    ll_home_loadmore.setVisibility(View.VISIBLE);
                                    ll_home_loadmore.getChildAt(0).setVisibility(View.GONE);
                                    TextView tv = (TextView) ll_home_loadmore.getChildAt(1);
                                    tv.setText("没有更多的数据了");
                                    ll_home_loadmore.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ll_home_loadmore.setVisibility(View.GONE);
                                        }
                                    }, 1000);
                                } else {
                                    pageIndex = String.valueOf(page);
                                    net2Server();
                                }
                            }
                        }, 1000);
                    }
                    break;
            }
            return false;

        }
    }

    private OnActivityListCompleteListener onActivityListCompleteListener;

    public interface OnActivityListCompleteListener {
        void onComplete();
    }

    public void setOnActivityListCompleteListener(OnActivityListCompleteListener onActivityListCompleteListener) {
        this.onActivityListCompleteListener = onActivityListCompleteListener;
    }
}
