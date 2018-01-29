package com.gather_excellent_help.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.TextView;
import android.widget.Toast;


import com.gather_excellent_help.MainActivity;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.ActivityListBean;
import com.gather_excellent_help.bean.ChangeUrlBean;
import com.gather_excellent_help.bean.ListBean;
import com.gather_excellent_help.bean.TaoWordBean;
import com.gather_excellent_help.bean.TypeNavigatorBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.activity.ScannerWebActivity;
import com.gather_excellent_help.ui.activity.WareListActivity;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.ui.activity.WebRecordActivity;
import com.gather_excellent_help.ui.activity.suning.SuningDetailActivity;

import com.gather_excellent_help.ui.adapter.TypeWareAdapter2;

import com.gather_excellent_help.ui.adapter.WareSelectorAdapter;
import com.gather_excellent_help.ui.base.LazyLoadFragment;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;

import com.gather_excellent_help.ui.widget.SimpleExpandableListViewAdapter;
import com.gather_excellent_help.ui.widget.TypeSelectorPopupwindow;

import com.gather_excellent_help.update.TypeActivityListAdapter;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.DividerGridItemDecoration;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.ScreenUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.changeutils.ChangeUrlUtil;

import com.gather_excellent_help.utils.shareutil.ShareUtil;
import com.google.gson.Gson;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.text.DecimalFormat;
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

    private AlertDialog dialog;

    private LinearLayout ll_type_sousuo;
    private LinearLayout ll_type_scanner;
    private LinearLayout ll_type_switch_list;
    private View v_line_protocal;
    private ImageView iv_type_switch_icon;
    private String type_id = "";

    private boolean isAll = false;

    private boolean mIsRequestDataRefresh = false;
    private NetUtil netUtil;
    private NetUtil netUtil2;
    private NetUtil netUtil3;
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
    private TypeWareAdapter2 typeWareAdapter2;
    public static final int CHECK_NULL = 4; //加载数据的标识

    private Handler handler;
    private String whick;
    private String click_url;
    private String taoWord;
    private String userLogin;
    private String goods_img;
    private String goods_title;
    private String adverId;
    private int whick_share;
    private String goods_price;
    private String goods_coupon;
    private String share_content;
    private int switch_page = 1;
    private GridLayoutManager gridLayoutManager;

    private AlertDialog alertDialog;
    private int itemCount;

    private LinearLayout ll_type_top_shaixuan;
    private LinearLayout ll_ware_list_price;
    private LinearLayout ll_ware_list_leibie;
    private LinearLayout ll_ware_list_pingpai;
    private LinearLayout ll_ware_list_rongliang;

    private TextView tv_ware_list_leibie;
    private TextView tv_ware_list_pingpai;
    private TextView tv_ware_list_rongliang;
    private ImageView iv_up_price;
    private ImageView iv_down_price;
    private TypeSelectorPopupwindow typeSelectorPopupwindow;
    private int crr_click = -1; //记录几点那个条目（类别，品牌，容量）
    private List<ListBean.DataBean> conditionData;

    private String leibie_url = Url.BASE_URL + "ClassList.aspx";
    private String rongliang_url = Url.BASE_URL + "SubCategoryList.aspx";
    private String pingpai_url = Url.BASE_URL + "goodsBrand.aspx";

    private String home_search_url = Url.BASE_URL + "IndexSearch.aspx";
    private String keyword = "";//关键字
    private String PriceOder = "2";//1----降序，2-----升序
    private String Type = "";//商品类型
    private String brandId = "";//品牌
    private String capacity = "";//容量
    private int sort_price = SortPrice.ASC.ordinal(); //价格排序
    private DividerGridItemDecoration dividerGridItemDecoration;


    /**
     * @return 布局对象
     */
    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.type_fragment, null);
        ll_type_sousuo = (LinearLayout) inflate.findViewById(R.id.ll_type_sousuo);
        ll_type_scanner = (LinearLayout) inflate.findViewById(R.id.ll_type_scanner);
        ll_type_switch_list = (LinearLayout) inflate.findViewById(R.id.ll_type_switch_list);
        v_line_protocal = inflate.findViewById(R.id.v_line_protocal);
        iv_type_switch_icon = (ImageView) inflate.findViewById(R.id.iv_type_switch_icon);

        ll_type_top_shaixuan = (LinearLayout) inflate.findViewById(R.id.ll_type_top_shaixuan);
        ll_ware_list_price = (LinearLayout) inflate.findViewById(R.id.ll_ware_list_price);
        ll_ware_list_leibie = (LinearLayout) inflate.findViewById(R.id.ll_ware_list_leibie);
        ll_ware_list_pingpai = (LinearLayout) inflate.findViewById(R.id.ll_ware_list_pingpai);
        ll_ware_list_rongliang = (LinearLayout) inflate.findViewById(R.id.ll_ware_list_rongliang);

        iv_up_price = (ImageView) inflate.findViewById(R.id.iv_up_price);
        iv_down_price = (ImageView) inflate.findViewById(R.id.iv_down_price);

        tv_ware_list_leibie = (TextView) inflate.findViewById(R.id.tv_ware_list_leibie);
        tv_ware_list_pingpai = (TextView) inflate.findViewById(R.id.tv_ware_list_pingpai);
        tv_ware_list_rongliang = (TextView) inflate.findViewById(R.id.tv_ware_list_rongliang);

        return inflate;
    }

    /**
     * 数据初始化
     */
    @Override
    public void initData() {
        netUtil = new NetUtil();
        netUtil2 = new NetUtil();
        netUtil3 = new NetUtil();

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
     * 解析优惠券转链的数据
     *
     * @param response
     */
    private void parseChangeData(String response) {
        try {
            LogUtil.e("click_url = " + response);
            ChangeUrlBean changeUrlBean = new Gson().fromJson(response, ChangeUrlBean.class);
            int statusCode = changeUrlBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    List<ChangeUrlBean.DataBean> data = changeUrlBean.getData();
                    if (data != null && data.size() > 0) {
                        click_url = changeUrlBean.getData().get(0).getClick_url();
                        LogUtil.e("click_url = " + click_url);
                        whick = "getwords";
                        map = new HashMap<>();
                        map = ShareUtil.getChangeWordsParam(map, userLogin, click_url, goods_img, goods_title);
                        ShareUtil.getChangeWordUrl(netUtil2, map);
                    }
                    break;
                case 0:
                    Toast.makeText(getContext(), changeUrlBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "系统服务出现异常，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析获取淘口令数据
     *
     * @param response
     */
    private void parseChangeWordsData(String response) {
        try {
            TaoWordBean taoWordBean = new Gson().fromJson(response, TaoWordBean.class);
            int statusCode = taoWordBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    taoWord = taoWordBean.getData();
                    LogUtil.e(taoWord);
                    if (whick_share == 1) {
                        share_content = "商品名称:" + goods_title + "\n商品价格 ¥" + goods_price + "\n优惠券" + goods_coupon + "元" + "\n复制这条消息:" + taoWord + "\n去打开手机淘宝";
                    } else if (whick_share == 2) {
                        share_content = "商品名称:" + goods_title + "\n商品价格 ¥" + goods_price + "\n复制这条消息:" + taoWord + "\n去打开手机淘宝";
                    }
                    ShareUtil.showCopyDialog(getContext(), whick_share, share_content, goods_price, shareListener, goods_img,
                            goods_title, dialog);
                    break;
                case 0:
                    Toast.makeText(getContext(), taoWordBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "系统服务出现异常，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 展示加载中
     */
    private void showCatView() {
        View inflate = View.inflate(getContext(), R.layout.loading_dialog_view, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(inflate);
        MainActivity activity = (MainActivity) getActivity();
        alertDialog = builder.create();
        if (activity != null && !activity.isFinishing()) {
            alertDialog.show();
        }
        alertDialog.getWindow().setLayout(ScreenUtil.getScreenWidth(activity) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 隐藏加载中
     */
    private void hindCatView() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null && !activity.isFinishing()) {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
    }

    /**
     * 友盟分享的监听
     */
    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(getContext(), "分享成功", Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getContext(), "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getContext(), "分享取消", Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    };

    /**
     * 点击切换不同的点击图标
     */
    private void switchDiffIcon() {
        if (switch_page == 2) {
            iv_type_switch_icon.setImageResource(R.drawable.icon_type_switch_list);
            ll_type_top_shaixuan.setVisibility(View.VISIBLE);
        } else if (switch_page == 1) {
            iv_type_switch_icon.setImageResource(R.drawable.icon_type_switch_list2);
            ll_type_top_shaixuan.setVisibility(View.GONE);
        }
    }

    /**
     * 点击切换不同的页面布局
     */
    private void switchDiffPage() {
        if (switch_page == 1) {
            cusListNavigator.setVisibility(View.VISIBLE);
            v_line_protocal.setVisibility(View.VISIBLE);
            layoutManager = new FullyLinearLayoutManager(getContext());
            if (rcvTypeShow != null && dividerGridItemDecoration != null) {
                rcvTypeShow.setLayoutManager(layoutManager);
                rcvTypeShow.removeItemDecoration(dividerGridItemDecoration);
            }
        } else if (switch_page == 2) {
            cusListNavigator.setVisibility(View.GONE);
            v_line_protocal.setVisibility(View.GONE);
            gridLayoutManager = new GridLayoutManager(getContext(), 2);
            dividerGridItemDecoration = new DividerGridItemDecoration(getContext(), DensityUtil.px2dip(getContext(),2), "#eeeeee");
            if (rcvTypeShow != null) {
                rcvTypeShow.setLayoutManager(gridLayoutManager);
                rcvTypeShow.addItemDecoration(dividerGridItemDecoration);
            }
        }
        isLoadMore = false;
        isAll = false;
        page = 1;
        pageIndex = String.valueOf(page);
        whick = "ware";

        if (switch_page == 1) {
            map = new HashMap<>();
            map.put("pageSize", pageSize);
            map.put("pageIndex", pageIndex);
            netUtil2.okHttp2Server2(ware_url, map);
        } else if (switch_page == 2) {
            searchWareList();
        }

    }

    /**
     * 导入分类数据
     */
    private void loadTypeData() {
        switch_page = 1;
        page = 1;
        pageIndex = "1";
        isLoadMore = false;
        isAll = false;
        crr_click = -1;

        PriceOder = "2";
        Type = "";
        brandId = "";
        capacity = "";

        rcvTypeShow.setVisibility(View.GONE);
        ll_type_top_shaixuan.setVisibility(View.GONE);
        tv_ware_list_leibie.setText("类别");
        tv_ware_list_rongliang.setText("规格");
        tv_ware_list_pingpai.setText("品牌");
        layoutManager = new LinearLayoutManager(getContext());
        rcvTypeShow.setLayoutManager(layoutManager);

        if (dividerGridItemDecoration != null) {
            rcvTypeShow.removeItemDecoration(dividerGridItemDecoration);
        }
        cusListNavigator.setVisibility(View.VISIBLE);
        v_line_protocal.setVisibility(View.VISIBLE);
        requestDataRefresh();
        swipeRefresh.setRefreshing(mIsRequestDataRefresh);
        tvTopTitleName.setText("商品分类");
        rlExit.setVisibility(View.GONE);
        getNavigatorData();
        whick = "ware";
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
                if (whick.equals("ware")) {
                    if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
                        if (mIsRequestDataRefresh == true) {
                            stopDataRefresh();
                            setRefresh(mIsRequestDataRefresh);
                        }
                    }
                    parseData2(response);
                } else if (whick.equals("changeurl")) {
                    parseChangeData(response);
                } else if (whick.equals("getwords")) {
                    parseChangeWordsData(response);
                }
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
                if (getContext() == null) {
                    return;
                }
                boolean isLogin = Tools.isLogin(getContext());
                if (isLogin) {
                    CaptureActivity.open(getContext(), new CaptureActivity.OnScanResultListener() {
                        @Override
                        public void onResult(String result) {
                            LogUtil.e(result);
                            if (result.startsWith("http")) {
                                boolean b = ChangeUrlUtil.checkContainWareId(result);
                                if (b) {
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
                                                        intent.putExtra("url_type", 1);
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
                                } else {
                                    Intent intent = new Intent(getContext(), ScannerWebActivity.class);
                                    intent.putExtra("scaner_url", result);
                                    intent.putExtra("url_type", 2);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        ll_type_switch_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_type_switch_list.setClickable(false);
                rcvTypeShow.setVisibility(View.GONE);
                showCatView();
                if (switch_page == 1) {
                    switch_page = 2;
                } else if (switch_page == 2) {
                    switch_page = 1;
                }
                switchDiffIcon();
                switchDiffPage();
            }
        });

        rcvTypeShow.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (switch_page == 1) {
                        lastVisibleItem = layoutManager
                                .findLastVisibleItemPosition();
                        itemCount = layoutManager.getItemCount();
                    } else if (switch_page == 2) {
                        lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                        itemCount = gridLayoutManager.getItemCount();
                    }

                    if (lastVisibleItem + 1 == itemCount) {
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
                                    whick = "ware";
                                    pageIndex = String.valueOf(page);
                                    //联网请求数据
                                    if (switch_page == 1) {
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
                                    } else {
                                        searchWareList();
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
                if (switch_page == 1) {
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                } else if (switch_page == 2) {
                    lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                }
            }
        });
        MyonclickListener myonclickListener = new MyonclickListener();
        ll_ware_list_price.setOnClickListener(myonclickListener);
        ll_ware_list_leibie.setOnClickListener(myonclickListener);
        ll_ware_list_pingpai.setOnClickListener(myonclickListener);
        ll_ware_list_rongliang.setOnClickListener(myonclickListener);

        OnServerResponseListener onServerResponseListener = new OnServerResponseListener();
        netUtil3.setOnServerResponseListener(onServerResponseListener);
    }

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            parseConditionData(response);
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
        }
    }

    private void parseData2(String response) {
        try {
            ActivityListBean activityListBean = new Gson().fromJson(response, ActivityListBean.class);
            int statusCode = activityListBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    ll_type_switch_list.setClickable(true);
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
                        if (switch_page == 1) {
                            if (typeActivityListAdapter != null) {
                                typeActivityListAdapter.notifyDataSetChanged();
                            }
                        } else if (switch_page == 2) {
                            if (typeWareAdapter2 != null) {
                                typeWareAdapter2.notifyDataSetChanged();
                            }
                        }
                    } else {
                        currData = activityListBean.getData();
                        if (currData != null) {
                            activityData = currData;
                            if (switch_page == 1) {
                                typeActivityListAdapter = new TypeActivityListAdapter(getContext(), activityData);
                                rcvTypeShow.setAdapter(typeActivityListAdapter);
                            } else if (switch_page == 2) {
                                typeWareAdapter2 = new TypeWareAdapter2(getContext(), activityData);
                                rcvTypeShow.setAdapter(typeWareAdapter2);
                            }
                            page = 2;
                            if (currData.size() == 0) {
                                Toast.makeText(getContext(), "该条目下没有商品信息。", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    rcvTypeShow.setVisibility(View.VISIBLE);
                    hindCatView();
                    ll_type_loadmore.setVisibility(View.GONE);
                    if (switch_page == 1) {
                        itemShareAndClick();
                    } else if (switch_page == 2) {
                        itemShareAndClick2();
                    }
                    break;
                case 0:
                    Toast.makeText(getContext(), activityListBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception e) {
            LogUtil.e("系统出现故障，请退出后重新尝试！");
        }
    }

    /**
     * 两列列表的点击以及分享
     */
    private void itemShareAndClick2() {
        typeWareAdapter2.setOnShareClickListener(new TypeWareAdapter2.OnShareClickListener() {
            @Override
            public void onShareClick(View v, int position) {
                DecimalFormat df = new DecimalFormat("#0.00");
                ActivityListBean.DataBean dataBean = activityData.get(position);
                int site_id = dataBean.getSite_id();
                int article_id = dataBean.getArticle_id();
                String couponsUrl = dataBean.getCouponsUrl();
                String link_url = dataBean.getLink_url();
                String goods_id = dataBean.getProductId();
                goods_img = dataBean.getImg_url();
                goods_title = dataBean.getTitle();
                double sell_price = dataBean.getSell_price();
                double market_price = dataBean.getMarket_price();
                int couponsPrice = dataBean.getCouponsPrice();
                goods_price = df.format(sell_price);
                goods_coupon = df.format(couponsPrice);
                boolean login = Tools.isLogin(getContext());
                adverId = Tools.getAdverId(getContext());
                userLogin = Tools.getUserLogin(getContext());
                if (!login) {
                    toLogin();
                    return;
                }
                if (site_id == 1) {
                    whick = "changeurl";
                    if (couponsPrice > 0) {
                        whick_share = 1;
                        map = new HashMap<>();
                        map = ShareUtil.getChangeWareParam(map, goods_id, adverId);
                        ShareUtil.getCouponChangeUrl(netUtil2, map);
                    } else {
                        whick_share = 2;
                        map = new HashMap<>();
                        map = ShareUtil.getChangeWareParam(map, goods_id, adverId);
                        ShareUtil.getWareChangeUrl(netUtil2, map);
                    }
                }
            }
        });

        typeWareAdapter2.setOnItemclickListener(new TypeWareAdapter2.OnItemclickListener() {
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
    }

    /**
     * 单列列表的点击以及分享
     */
    private void itemShareAndClick() {
        typeActivityListAdapter.setOnShareClickListener(new TypeActivityListAdapter.OnShareClickListener() {
            @Override
            public void onShareClick(View v, int position) {
                DecimalFormat df = new DecimalFormat("#0.00");
                ActivityListBean.DataBean dataBean = activityData.get(position);
                int site_id = dataBean.getSite_id();
                int article_id = dataBean.getArticle_id();
                String couponsUrl = dataBean.getCouponsUrl();
                String link_url = dataBean.getLink_url();
                String goods_id = dataBean.getProductId();
                goods_img = dataBean.getImg_url();
                goods_title = dataBean.getTitle();
                double sell_price = dataBean.getSell_price();
                double market_price = dataBean.getMarket_price();
                int couponsPrice = dataBean.getCouponsPrice();
                goods_price = df.format(sell_price);
                goods_coupon = df.format(couponsPrice);
                boolean login = Tools.isLogin(getContext());
                adverId = Tools.getAdverId(getContext());
                userLogin = Tools.getUserLogin(getContext());
                if (!login) {
                    toLogin();
                    return;
                }
                if (site_id == 1) {
                    whick = "changeurl";
                    if (couponsPrice > 0) {
                        whick_share = 1;
                        map = new HashMap<>();
                        map = ShareUtil.getChangeWareParam(map, goods_id, adverId);
                        ShareUtil.getCouponChangeUrl(netUtil2, map);
                    } else {
                        whick_share = 2;
                        map = new HashMap<>();
                        map = ShareUtil.getChangeWareParam(map, goods_id, adverId);
                        ShareUtil.getWareChangeUrl(netUtil2, map);
                    }
                }

            }
        });
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
    }

    private void toLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
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
                            whick = "ware";
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
                            whick = "ware";
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
                            whick = "ware";
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
                        whick = "ware";
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
                    if (switch_page == 1) {
                        netUtil.okHttp2Server2(url, null);
                    } else if (switch_page == 2) {
                        crr_click = -1;
                        PriceOder = "2";
                        Type = "";
                        brandId = "";
                        capacity = "";
                        rcvTypeShow.setVisibility(View.GONE);
                        tv_ware_list_leibie.setText("类别");
                        tv_ware_list_rongliang.setText("规格");
                        tv_ware_list_pingpai.setText("品牌");
                        searchWareList();
                    }
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


    /**
     * 顶部筛选的数据联网请求
     */
    private void searchWareList() {
        whick = "ware";
        switch_page = 2;
        map = new HashMap<>();
        map.put("key_words", keyword);
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
        map.put("PriceOder", PriceOder);
        map.put("Type", Type);
        map.put("brandId", brandId);
        map.put("capacity", capacity);
        netUtil2.okHttp2Server2(home_search_url, map);
        LogUtil.e(map.toString());
    }

    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_ware_list_price:
                    if (sort_price == WareListActivity.SortPrice.ASC.ordinal()) {
                        sort_price = WareListActivity.SortPrice.DESC.ordinal();
                    } else {
                        sort_price = WareListActivity.SortPrice.ASC.ordinal();
                    }
                    setSortPriceIcon(sort_price);
                    setSortPriceWareList(sort_price);
                    break;
                case R.id.ll_ware_list_leibie:
                    isLoadMore = false;
                    crr_click = 0;
                    netUtil3.okHttp2Server2(leibie_url, null);
                    break;
                case R.id.ll_ware_list_pingpai:
                    isLoadMore = false;
                    crr_click = 1;
                    map = new HashMap<>();
                    if (Type != null && !TextUtils.isEmpty(Type)) {
                        map.put("id", Type);
                        netUtil3.okHttp2Server2(pingpai_url, map);
                    } else {
                        Toast.makeText(getContext(), "请选择类别！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.ll_ware_list_rongliang:
                    isLoadMore = false;
                    crr_click = 2;
                    if (Type != null && !TextUtils.isEmpty(Type)) {
                        map = new HashMap<>();
                        map.put("sub_id", Type);
                        netUtil3.okHttp2Server2(rongliang_url, map);
                    } else {
                        Toast.makeText(getContext(), "请选择类别！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    /**
     * @param sort_price 设置查询升降序的列表
     */
    private void setSortPriceWareList(int sort_price) {
        if (sort_price == WareListActivity.SortPrice.ASC.ordinal()) {
            PriceOder = String.valueOf(2);
        } else {
            PriceOder = String.valueOf(1);
        }
        page = 1;
        isLoadMore = false;
        pageIndex = String.valueOf(page);
        searchWareList();
    }

    /**
     * @param sort_price 设置升降序的图标
     */
    private void setSortPriceIcon(int sort_price) {
        if (sort_price == WareListActivity.SortPrice.ASC.ordinal()) {
            iv_up_price.setImageResource(R.drawable.up_black_arraw);
            iv_down_price.setImageResource(R.drawable.down_gray_arraw);
        } else {
            iv_up_price.setImageResource(R.drawable.up_gray_arraw);
            iv_down_price.setImageResource(R.drawable.down_black_arraw);
        }
    }

    public enum SortPrice {
        ASC,
        DESC;
    }

    /**
     * @param response 解析数据
     */
    private void parseConditionData(String response) {
        ListBean listBean = new Gson().fromJson(response, ListBean.class);
        int statusCode = listBean.getStatusCode();
        switch (statusCode) {
            case 1:
                LogUtil.e(response);
                conditionData = listBean.getData();
                ListBean.DataBean dataBean = new ListBean.DataBean();
                if (crr_click != 0) {
                    dataBean.setId(-1);
                    dataBean.setTitle("全部");
                    conditionData.add(dataBean);
                }
                if (conditionData == null) {
                    return;
                }
                setPopMenuNull();
                showPopMenu(conditionData);
                break;
            case 0:
                Toast.makeText(getContext(), listBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showPopMenu(List<ListBean.DataBean> data) {
        if (typeSelectorPopupwindow == null) {
            typeSelectorPopupwindow = new TypeSelectorPopupwindow(getContext(), data);
            if (handler != null) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showPopupwindow();
                    }
                }, 300);
            }
        } else if (typeSelectorPopupwindow != null
                && typeSelectorPopupwindow.isShowing()) {
            typeSelectorPopupwindow.dismiss();
        } else {
            if (handler != null) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        typeSelectorPopupwindow.showAsDropDown(ll_ware_list_pingpai, 5, 5);
                    }
                }, 300);
            }
        }

        typeSelectorPopupwindow.setOnItemClickListener(new WareSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                isLoadMore = false;
                page = 1;
                pageIndex = String.valueOf(page);
                ListBean.DataBean dataBean = conditionData.get(position);
                int id = dataBean.getId();
                String title = dataBean.getTitle();
                if (title.length() > 2) {
                    title = title.substring(0, 2);
                }
                if (crr_click == 0) {
                    if (id == -1) {
                        Type = "";
                    } else {
                        Type = String.valueOf(id);
                    }
                    brandId = "";
                    capacity = "";
                    tv_ware_list_pingpai.setText("品牌");
                    tv_ware_list_rongliang.setText("规格");
                    tv_ware_list_leibie.setText(title);
                } else if (crr_click == 1) {
                    if (id == -1) {
                        brandId = "";
                    } else {
                        brandId = String.valueOf(id);
                    }
                    capacity = "";
                    tv_ware_list_rongliang.setText("规格");
                    tv_ware_list_pingpai.setText(title);
                } else if (crr_click == 2) {
                    if (id == -1) {
                        capacity = "";
                    } else {
                        capacity = String.valueOf(id);
                    }
                    tv_ware_list_rongliang.setText(title);
                }
                searchWareList();
                if (typeSelectorPopupwindow.isShowing()) {
                    typeSelectorPopupwindow.dismiss();
                }
            }
        });
    }

    /**
     * 显示popupwindow
     */
    private void showPopupwindow() {
        switch (crr_click) {
            case 0:
                typeSelectorPopupwindow.showAsDropDown(ll_ware_list_leibie, 5, 5);
                break;
            case 1:
                typeSelectorPopupwindow.showAsDropDown(ll_ware_list_pingpai, 5, 5);
                break;
            case 2:
                typeSelectorPopupwindow.showAsDropDown(ll_ware_list_rongliang, 5, 5);
                break;
        }
    }

    /**
     * 将popupwindow置空，方便后面调用
     */
    private void setPopMenuNull() {
        if (typeSelectorPopupwindow != null) {
            typeSelectorPopupwindow = null;
        }
    }


}
