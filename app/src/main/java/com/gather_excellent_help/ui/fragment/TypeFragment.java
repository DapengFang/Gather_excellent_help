package com.gather_excellent_help.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.gather_excellent_help.R;
import com.gather_excellent_help.aliapi.DemoTradeCallback;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.ActivityListBean;
import com.gather_excellent_help.bean.ChangeUrlBean;
import com.gather_excellent_help.bean.TypeNavigatorBean;
import com.gather_excellent_help.bean.TypeWareBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.activity.ScannerWebActivity;
import com.gather_excellent_help.ui.activity.WareListActivity;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.ui.activity.WebRecordActivity;
import com.gather_excellent_help.ui.activity.suning.SuningDetailActivity;
import com.gather_excellent_help.ui.adapter.TypeWareAdapter;
import com.gather_excellent_help.ui.base.BaseFragment;
import com.gather_excellent_help.ui.base.LazyLoadFragment;
import com.gather_excellent_help.ui.widget.Classes;
import com.gather_excellent_help.ui.widget.College;
import com.gather_excellent_help.ui.widget.CustomExpandableListView;
import com.gather_excellent_help.ui.widget.DividerItemDecoration;
import com.gather_excellent_help.ui.widget.MyExpandableListVeiw;
import com.gather_excellent_help.ui.widget.SimpleExpandableListViewAdapter;
import com.gather_excellent_help.update.HomeActivityListAdapter;
import com.gather_excellent_help.update.TypeActivityListAdapter;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.changeutils.ChangeUrlUtil;
import com.gather_excellent_help.utils.imageutils.ImageLoader;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Call;
import push.jerry.cn.scan.CaptureActivity;


/**
 * created by Dapeng Fang
 */
public class TypeFragment extends LazyLoadFragment {
    @Bind(R.id.iv_zhuangtai_exit)
    ImageView ivZhuangtaiExit;
    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.et_sousuo)
    EditText etSousuo;
    @Bind(R.id.cus_list_navigator)
    ExpandableListView cusListNavigator;
    @Bind(R.id.rcv_type_show)
    RecyclerView rcvTypeShow;
    @Bind(R.id.tv_type_search)
    TextView tvTypeSearch;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @Bind(R.id.ll_type_loadmore)
    LinearLayout ll_type_loadmore;

    private LinearLayout ll_type_sousuo;
    private LinearLayout ll_type_scanner;
    private LinearLayout ll_type_msg;
    private String type_id = "";

    private boolean isAll = false;

    private boolean mIsRequestDataRefresh = false;
    private NetUtil netUtil;
    private NetUtil netUtil2;
    private String url = Url.BASE_URL + "CategoryList.aspx";
    private String ware_url = Url.BASE_URL + "CategoryGoodList.aspx";
    private TypeNavigatorBean.DataBean.SubListBean subListBean;
    private TypeNavigatorBean.DataBean.SubListBean.ThirdListBean thirdListBean;
    private String pageSize = "10";
    private String pageIndex = "1";

    private boolean isLoadMore = false;
    private int page = 1;
    private Map<String, String> map;
    private LinearLayoutManager layoutManager;
    private int lastVisibleItem;
    private List<ActivityListBean.DataBean> activityData;
    private List<ActivityListBean.DataBean> currData;
    private TypeActivityListAdapter typeActivityListAdapter;
    public static final int CHECK_NULL = 4; //加载数据的标识

    private Handler handler;

    /**
     * @return 布局对象
     */
    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.type_fragment, null);
        ll_type_sousuo = (LinearLayout) inflate.findViewById(R.id.ll_type_sousuo);
        ll_type_scanner = (LinearLayout) inflate.findViewById(R.id.ll_type_scanner);
        ll_type_msg = (LinearLayout) inflate.findViewById(R.id.ll_type_msg);
        return inflate;
    }

    /**
     * 数据初始化
     */
    @Override
    public void initData() {
        netUtil = new NetUtil();
        netUtil2 = new NetUtil();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case CHECK_NULL:
                        if (rcvTypeShow != null && swipeRefresh != null
                                && tvTopTitleName != null &&
                                rlExit != null) {
                            loadTypeData();
                            if (handler != null) {
                                handler.removeMessages(CHECK_NULL);
                            }
                        } else {
                            if (handler != null) {
                                handler.sendEmptyMessageDelayed(CHECK_NULL, 500);
                            }
                        }
                        break;
                }
            }
        };
        if (handler != null) {
            handler.sendEmptyMessageDelayed(CHECK_NULL, 600);
        }
    }

    /**
     * 导入分类数据
     */
    private void loadTypeData() {
        layoutManager = new LinearLayoutManager(getContext());
        rcvTypeShow.setLayoutManager(layoutManager);
        requestDataRefresh();
        swipeRefresh.setRefreshing(mIsRequestDataRefresh);
        tvTopTitleName.setText("商品分类");
        rlExit.setVisibility(View.GONE);
        getNavigatorData();
        map = new HashMap<>();
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
        netUtil2.okHttp2Server2(ware_url, map);
        netUtil2.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e("ware list" + response);
                if (getContext() == null) {
                    return;
                }
                if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
                    if (mIsRequestDataRefresh == true) {
                        stopDataRefresh();
                        setRefresh(mIsRequestDataRefresh);
                    }
                }
                parseData2(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
                    stopDataRefresh();
                    swipeRefresh.setRefreshing(mIsRequestDataRefresh);
                    Toast.makeText(getContext(), "网络连接出现问题~", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvTypeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() == null) {
                    return;
                }
                String content = etSousuo.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getActivity(), "请输入你要搜索商品的名称！", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), WareListActivity.class);
                    intent.putExtra("content", "isTypeSou");
                    intent.putExtra("sousuo", content);
                    startActivity(intent);
                    etSousuo.setText("");
                }
            }
        });
        ll_type_sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() == null) {
                    return;
                }
                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra("content", "isTypeSou");
                intent.putExtra("sousuo", "");
                startActivity(intent);
            }
        });
        ll_type_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getContext() == null) {
                    return;
                }
                boolean isLogin = Tools.isLogin(getContext());
                if(isLogin) {
                    CaptureActivity.open(getContext(), new CaptureActivity.OnScanResultListener() {
                        @Override
                        public void onResult(String result) {
                            LogUtil.e(result);
                            if(result.startsWith("http")) {
                                boolean b = ChangeUrlUtil.checkContainWareId(result);
                                if(b) {
                                    String wareId = ChangeUrlUtil.getWareId(result);
                                    String adverId = Tools.getAdverId(getContext());
                                    LogUtil.e(wareId);
                                    ChangeUrlUtil.getChangeUrl(getContext(), result, wareId, adverId, new ChangeUrlUtil.OnChangeUrlListener() {
                                        @Override
                                        public void onResultUrl(String result) {
                                            LogUtil.e("click_url = " + result);
                                            ChangeUrlBean changeUrlBean = new Gson().fromJson(result, ChangeUrlBean.class);
                                            int statusCode = changeUrlBean.getStatusCode();
                                            switch (statusCode) {
                                                case 1:
                                                    List<ChangeUrlBean.DataBean> data = changeUrlBean.getData();
                                                    if (data != null && data.size() > 0) {
                                                        String click_url = changeUrlBean.getData().get(0).getClick_url();
                                                        LogUtil.e("click_url = " + click_url);
                                                        Intent intent = new Intent(getContext(), ScannerWebActivity.class);
                                                        intent.putExtra("scaner_url", click_url);
                                                        intent.putExtra("url_type",1);
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(getContext(), "转链出现问题，没有拿到转链的链接~", Toast.LENGTH_SHORT).show();
                                                    }
                                                    break;
                                                case 0:
                                                    Toast.makeText(getContext(), changeUrlBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                                                    break;
                                            }
                                        }
                                    });
                                }else{
                                    Intent intent = new Intent(getContext(), ScannerWebActivity.class);
                                    intent.putExtra("scaner_url", result);
                                    intent.putExtra("url_type",2);
                                    startActivity(intent);
                                }
                            }else{
                                Toast.makeText(getContext(), result , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        ll_type_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "暂无消息通知", Toast.LENGTH_SHORT).show();
            }
        });

        rcvTypeShow.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisibleItem = layoutManager
                            .findLastVisibleItemPosition();

                    if (lastVisibleItem + 1 == layoutManager
                            .getItemCount()) {
                        isLoadMore = true;
                        ll_type_loadmore.setVisibility(View.VISIBLE);
                        ll_type_loadmore.getChildAt(0).setVisibility(View.VISIBLE);
                        TextView tv = (TextView) ll_type_loadmore.getChildAt(1);
                        tv.setText("正在加载中");
                        if (handler == null) {
                            return;
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (currData == null) {
                                    return;
                                }
                                if (currData.size() < Integer.parseInt(pageSize)) {
                                    ll_type_loadmore.setVisibility(View.VISIBLE);
                                    ll_type_loadmore.getChildAt(0).setVisibility(View.GONE);
                                    TextView tv = (TextView) ll_type_loadmore.getChildAt(1);
                                    tv.setText("没有更多的数据了");
                                    if (ll_type_loadmore != null) {
                                        ll_type_loadmore.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (ll_type_loadmore != null) {
                                                    ll_type_loadmore.setVisibility(View.GONE);
                                                }
                                            }
                                        }, 1000);
                                    }
                                } else {
                                    pageIndex = String.valueOf(page);
                                    //联网请求数据
                                    if (isAll) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("id", type_id);
                                        map.put("pageSize", pageSize);
                                        map.put("pageIndex", pageIndex);
                                        netUtil2.okHttp2Server2(ware_url, map);
                                    } else {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("pageSize", pageSize);
                                        map.put("pageIndex", pageIndex);
                                        netUtil2.okHttp2Server2(ware_url, map);
                                    }
                                }
                            }
                        }, 1000);
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

    private void parseData2(String response) {
        ActivityListBean activityListBean = new Gson().fromJson(response, ActivityListBean.class);
        int statusCode = activityListBean.getStatusCode();
        switch (statusCode) {
            case 1:
                if (rcvTypeShow == null) {
                    return;
                }
                if (isLoadMore) {
                    page++;
                    LogUtil.e("page == " + page);
                    currData = activityListBean.getData();
                    if (activityData != null && currData != null) {
                        activityData.addAll(currData);
                    }
                    typeActivityListAdapter.notifyDataSetChanged();
                } else {
                    currData = activityListBean.getData();
                    if (currData != null) {
                        activityData = currData;
                        typeActivityListAdapter = new TypeActivityListAdapter(getContext(), activityData);
                        rcvTypeShow.setAdapter(typeActivityListAdapter);
                        page = 2;
                        if (currData.size() == 0) {
                            Toast.makeText(getContext(), "该条目下没有商品信息。", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                ll_type_loadmore.setVisibility(View.GONE);
                typeActivityListAdapter.setOnItemclickListener(new TypeActivityListAdapter.OnItemclickListener() {
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
                            if (couponsPrice > 0) {
                                if (couponsUrl != null && !TextUtils.isEmpty(couponsUrl)) {
                                    Intent intent = new Intent(getContext(), WebActivity.class);
                                    intent.putExtra("web_url", couponsUrl);
                                    intent.putExtra("url", link_url);
                                    intent.putExtra("goods_id", goods_id);
                                    intent.putExtra("goods_img", goods_img);
                                    intent.putExtra("goods_title", goods_title);
                                    intent.putExtra("goods_price", df.format(sell_price) + "");
                                    intent.putExtra("goods_coupon", String.valueOf(couponsPrice));
                                    intent.putExtra("goods_coupon_url", couponsUrl);
                                    getContext().startActivity(intent);
                                }
                            } else {
                                Intent intent = new Intent(getContext(), WebRecordActivity.class);
                                intent.putExtra("url", link_url);
                                intent.putExtra("goods_id", goods_id);
                                intent.putExtra("goods_img", goods_img);
                                intent.putExtra("goods_title", goods_title);
                                intent.putExtra("goods_price", df.format(sell_price) + "");
                                getContext().startActivity(intent);
                            }
                        } else if (site_id == 2) {
                            //苏宁
                            Intent intent = new Intent(getContext(), SuningDetailActivity.class);
                            intent.putExtra("article_id", article_id);
                            intent.putExtra("goods_id", goods_id);
                            intent.putExtra("goods_img", goods_img);
                            intent.putExtra("goods_title", goods_title);
                            intent.putExtra("goods_price", df.format(sell_price) + "");
                            intent.putExtra("c_price", df.format(market_price) + "");
                            getContext().startActivity(intent);
                        }
                    }
                });
                break;
            case 0:
                Toast.makeText(getContext(), activityListBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 获取左边导航数据
     */
    private void getNavigatorData() {

        netUtil.okHttp2Server2(url, null);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });

    }

    /**
     * 解析一级索引的数据
     *
     * @param response
     */
    private void parseData(String response) {
        TypeNavigatorBean navigatorData = new Gson().fromJson(response, TypeNavigatorBean.class);
        int statusCode = navigatorData.getStatusCode();
        switch (statusCode) {
            case 1:
                final List<TypeNavigatorBean.DataBean> data = navigatorData.getData();
                final SimpleExpandableListViewAdapter adapter = new SimpleExpandableListViewAdapter(data, getActivity());
                if (cusListNavigator != null) {
                    // 设置适配器
                    cusListNavigator.setAdapter(adapter);
                    cusListNavigator.setGroupIndicator(null);
                    cusListNavigator.setOnScrollListener(new MyOnScrollListener());
                    cusListNavigator.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int i) {
                            for (int c = 0; c < adapter.getGroupCount(); c++) {
                                if (c != i) {
                                    cusListNavigator.collapseGroup(c);
                                }
                            }
                            for (int l = 0; l < data.size(); l++) {
                                List<TypeNavigatorBean.DataBean.SubListBean> subList = data.get(l).getSubList();
                                for (int m = 0; m < subList.size(); m++) {
                                    TypeNavigatorBean.DataBean.SubListBean subListBean = subList.get(m);
                                    subListBean.setCheck(false);
                                    List<TypeNavigatorBean.DataBean.SubListBean.ThirdListBean> threeList = subListBean.getThreeList();
                                    for (int n = 0; n < threeList.size(); n++) {
                                        TypeNavigatorBean.DataBean.SubListBean.ThirdListBean thirdListBean = threeList.get(n);
                                        thirdListBean.setCheck(false);
                                    }
                                }
                            }
                            TypeNavigatorBean.DataBean dataBean = data.get(i);
                            int id = dataBean.getId();
                            type_id = String.valueOf(id);
                            requestDataRefresh();
                            swipeRefresh.setRefreshing(mIsRequestDataRefresh);
                            isLoadMore = false;
                            isAll = true;
                            page = 1;
                            pageIndex = String.valueOf(page);
                            Map<String, String> map = new HashMap<>();
                            map.put("id", type_id);
                            map.put("pageSize", pageSize);
                            map.put("pageIndex", pageIndex);
                            netUtil2.okHttp2Server2(ware_url, map);
                        }
                    });

                    adapter.setOnExpandableClickListener(new SimpleExpandableListViewAdapter.OnExpandableClickListener() {
                        @Override
                        public void onSecondItemClick(int position, int groupPisition) {
                            for (int l = 0; l < data.size(); l++) {
                                List<TypeNavigatorBean.DataBean.SubListBean> subList = data.get(l).getSubList();
                                for (int m = 0; m < subList.size(); m++) {
                                    TypeNavigatorBean.DataBean.SubListBean subListBean = subList.get(m);
                                    subListBean.setCheck(false);
                                    List<TypeNavigatorBean.DataBean.SubListBean.ThirdListBean> threeList = subListBean.getThreeList();
                                    for (int n = 0; n < threeList.size(); n++) {
                                        TypeNavigatorBean.DataBean.SubListBean.ThirdListBean thirdListBean = threeList.get(n);
                                        thirdListBean.setCheck(false);
                                    }
                                }
                            }
                            subListBean = data.get(position).getSubList().get(groupPisition);
                            int id = subListBean.getId();
                            type_id = String.valueOf(id);
                            subListBean.setCheck(true);
                            isLoadMore = false;
                            isAll = true;
                            requestDataRefresh();
                            swipeRefresh.setRefreshing(mIsRequestDataRefresh);
                            page = 1;
                            pageIndex = String.valueOf(page);
                            Map<String, String> map = new HashMap<>();
                            map.put("id", type_id);
                            map.put("pageSize", pageSize);
                            map.put("pageIndex", pageIndex);
                            netUtil2.okHttp2Server2(ware_url, map);
                        }

                        @Override
                        public void onThirdItemClick(int position, int groupPisition, int childPosition, TextView tv) {
                            for (int l = 0; l < data.size(); l++) {
                                List<TypeNavigatorBean.DataBean.SubListBean> subList = data.get(l).getSubList();
                                for (int m = 0; m < subList.size(); m++) {
                                    TypeNavigatorBean.DataBean.SubListBean subListBean = subList.get(m);
                                    subListBean.setCheck(false);
                                    List<TypeNavigatorBean.DataBean.SubListBean.ThirdListBean> threeList = subListBean.getThreeList();
                                    for (int n = 0; n < threeList.size(); n++) {
                                        TypeNavigatorBean.DataBean.SubListBean.ThirdListBean thirdListBean = threeList.get(n);
                                        thirdListBean.setCheck(false);
                                    }
                                }
                            }
                            thirdListBean = data.get(position).getSubList().get(groupPisition).getThreeList().get(childPosition);
                            int id = thirdListBean.getId();
                            type_id = String.valueOf(id);
                            thirdListBean.setCheck(true);
                            isLoadMore = false;
                            isAll = true;
                            requestDataRefresh();
                            swipeRefresh.setRefreshing(mIsRequestDataRefresh);
                            page = 1;
                            pageIndex = String.valueOf(page);
                            Map<String, String> map = new HashMap<>();
                            map.put("id", type_id);
                            map.put("pageSize", pageSize);
                            map.put("pageIndex", pageIndex);
                            netUtil2.okHttp2Server2(ware_url, map);
                        }
                    });

                    if (mIsRequestDataRefresh == true) {
                        map = new HashMap<>();
                        map.put("pageSize", pageSize);
                        map.put("pageIndex", pageIndex);
                        netUtil2.okHttp2Server2(ware_url, map);
                    }
                }
                break;
            case 0:
                if (mIsRequestDataRefresh == true) {
                    stopDataRefresh();
                    setRefresh(mIsRequestDataRefresh);
                }
                break;
        }

    }

    private void setupSwipeRefresh(View view) {
        if (swipeRefresh != null) {
            swipeRefresh.setColorSchemeResources(R.color.colorFirst,
                    R.color.colorSecond, R.color.colorThird);
            swipeRefresh.setProgressViewOffset(true, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestDataRefresh();
                    setRefresh(mIsRequestDataRefresh);
                    isLoadMore = false;
                    isAll = false;
                    page = 1;
                    pageIndex = String.valueOf(page);
                    netUtil.okHttp2Server2(url, null);
                }
            });
        }
    }

    public void requestDataRefresh() {
        mIsRequestDataRefresh = true;
    }

    public void stopDataRefresh() {
        mIsRequestDataRefresh = false;
    }


    /**
     * 设置刷新的方法
     *
     * @param requestDataRefresh 是否需要刷新
     */
    public void setRefresh(boolean requestDataRefresh) {
        if (!requestDataRefresh) {
            mIsRequestDataRefresh = false;
            if (swipeRefresh != null) {
                swipeRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefresh != null) {
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        } else {
            if (swipeRefresh != null) {
                swipeRefresh.setRefreshing(true);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        setupSwipeRefresh(rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    public class MyOnScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
            //判断ListView是否滑动到第一个Item的顶部
            if (swipeRefresh != null && cusListNavigator.getChildCount() > 0 && cusListNavigator.getFirstVisiblePosition() == 0
                    && cusListNavigator.getChildAt(0).getTop() >= cusListNavigator.getPaddingTop()) {
                //解决滑动冲突，当滑动到第一个item，下拉刷新才起作用
                swipeRefresh.setEnabled(true);
            } else {
                swipeRefresh.setEnabled(false);
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }


    public void onEvent(AnyEvent event) {
        if (event.getType() == EventType.EVENT_LOGIN) {
            String msg = "onEventMainThread收到了消息：" + event.getMessage();
            LogUtil.e(msg);
            initData();
        }
    }

    @Override
    protected void stopLoad() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
