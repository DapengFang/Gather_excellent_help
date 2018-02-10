package com.gather_excellent_help.ui.activity.taosearch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.ChangeUrlBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.SearchTaobaoBean;
import com.gather_excellent_help.bean.TaoWordBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.ui.activity.WebRecordActivity;
import com.gather_excellent_help.ui.adapter.TaobaoWareListAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.fragment.TaobaoFragment;
import com.gather_excellent_help.ui.widget.TaobaoShaixuanPopupwindow;
import com.gather_excellent_help.ui.widget.TaobaoZonghePopupwindow;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.shareutil.ShareUtil;
import com.google.gson.Gson;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class TaoSearchActivity extends BaseActivity {

    @Bind(R.id.et_taobao_search_content)
    EditText etTaobaoSearchContent;
    @Bind(R.id.rl_taobao_sousuo)
    RelativeLayout rlTaobaoSousuo;
    @Bind(R.id.tv_taobao_zonghe_sort)
    TextView tvTaobaoZongheSort;
    @Bind(R.id.iv_taobao_zonghe_sort)
    ImageView ivTaobaoZongheSort;
    @Bind(R.id.ll_taobao_zonghe)
    LinearLayout llTaobaoZonghe;
    @Bind(R.id.tv_taobao_xiaoliang_sort)
    TextView tvTaobaoXiaoliangSort;
    @Bind(R.id.ll_taobao_xiaoliang)
    LinearLayout llTaobaoXiaoliang;
    @Bind(R.id.tv_taobao_price_sort)
    TextView tvTaobaoPriceSort;
    @Bind(R.id.iv_up_price)
    ImageView ivUpPrice;
    @Bind(R.id.iv_down_price)
    ImageView ivDownPrice;
    @Bind(R.id.ll_taobao_price)
    LinearLayout llTaobaoPrice;
    @Bind(R.id.tv_taobao_shaixuan_sort)
    TextView tvTaobaoShaixuanSort;
    @Bind(R.id.iv_taobao_shaixuan_sort)
    ImageView ivTaobaoShaixuanSort;
    @Bind(R.id.ll_taobao_shaixuan)
    LinearLayout llTaobaoShaixuan;
    @Bind(R.id.gv_taobao_list)
    GridView gvTaobaoList;
    @Bind(R.id.ll_taobao_loadmore)
    LinearLayout llTaobaoLoadmore;
    @Bind(R.id.activity_ware_list)
    LinearLayout activityWareList;
    @Bind(R.id.tv_news_edit_search)
    TextView tvNewsEditSearch;
    @Bind(R.id.ll_news_search_after)
    LinearLayout llNewsSearchAfter;
    @Bind(R.id.rl_edit_text_exit)
    RelativeLayout rlEditTextExit;
    @Bind(R.id.progress)
    ProgressBar progress;

    private SwipeRefreshLayout swip_taobao_refresh;
    private RelativeLayout rl_exit;

    private boolean price_sort;

    private NetUtil netUtil;
    private NetUtil netUtil2;
    private Map<String, String> map;
    private String search_url = Url.BASE_URL + "SearchMore.aspx";
    private String keyword = "";//关键字
    private String city = "";//城市
    private String type = "";//排序
    private String is_tmall = "";//是否是天猫
    private String start_price = "";//范围下限
    private String end_price = "";//范围上限
    private String page_no = "1";//第几页
    private String page_size = "10";//每页多少
    private TaobaoShaixuanPopupwindow taobaoShaixuanPopupwindow;
    private TaobaoZonghePopupwindow taobaoZonghePopupwindow;
    private int isLoadmore = -1;//是否加载更多
    private int page = 1;//加载更多

    private List<SearchTaobaoBean.DataBean> taobaodata;//要加载的数据
    private List<SearchTaobaoBean.DataBean> newData;//每次获取的数据
    private TaobaoWareListAdapter taobaoWareListAdapter;
    public static final int CHECK_NULL = 4; //加载数据的标识

    private Handler handler;
    private boolean mIsRequestDataRefresh;
    private String whick = "";
    private String click_url;
    private String taoWord;
    private int whick_share;
    private String share_content;
    private AlertDialog dialog;
    private String adverId;
    private String userLogin;
    private int couponsPrice;
    private String goods_id;
    private String goods_img;
    private String goods_title;
    private String goods_price;
    private String goods_coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taobao_fragment);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    private void initView() {
        swip_taobao_refresh = (SwipeRefreshLayout) findViewById(R.id.swip_taobao_refresh);
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case CHECK_NULL:
                        if (llNewsSearchAfter != null
                                && rlEditTextExit != null && llTaobaoLoadmore != null
                                && gvTaobaoList != null) {
                            loadTaobaoData();
                            handler.removeMessages(CHECK_NULL);
                        } else {
                            handler.sendEmptyMessageDelayed(CHECK_NULL, 500);
                        }
                        break;
                }
            }
        };
        if (handler != null) {
            handler.sendEmptyMessageDelayed(CHECK_NULL, 200);
        }
        setupSwipeRefresh(swip_taobao_refresh);
    }

    private void setupSwipeRefresh(View view) {
        if (swip_taobao_refresh != null) {
            swip_taobao_refresh.setColorSchemeResources(R.color.colorFirst,
                    R.color.colorSecond, R.color.colorThird);
            swip_taobao_refresh.setProgressViewOffset(true, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
            swip_taobao_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mIsRequestDataRefresh = true;
                    setRefresh(mIsRequestDataRefresh);
                    page = 1;
                    page_no = "1";
                    loadTaobaoData();
                }
            });
        }
    }

    /**
     * 设置刷新的方法
     *
     * @param requestDataRefresh 是否需要刷新
     */
    public void setRefresh(boolean requestDataRefresh) {
        if (!requestDataRefresh) {
            mIsRequestDataRefresh = false;
            if (swip_taobao_refresh != null) {
                swip_taobao_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swip_taobao_refresh != null) {
                            swip_taobao_refresh.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        } else {
            if (swip_taobao_refresh != null) {
                swip_taobao_refresh.setRefreshing(true);
            }
        }
    }

    /**
     * 导入淘宝数据
     */
    private void loadTaobaoData() {
        netUtil = new NetUtil();
        netUtil2 = new NetUtil();
        Intent intent = getIntent();
        String search_words = intent.getStringExtra("search_words");
        if (search_words != null) {
            keyword = search_words;
        }
        if (TextUtils.isEmpty(keyword)) {
            etTaobaoSearchContent.setText("");
        } else {
            etTaobaoSearchContent.setText(keyword);
        }
        showLoading();
        searchTaobaoWare(keyword, city, type, is_tmall, start_price, end_price, page_no);
        netUtil.setOnServerResponseListener(
                new NetUtil.OnServerResponseListener() {
                    @Override
                    public void getSuccessResponse(String response) {
                        LogUtil.e(response);
                        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                        int statusCode = codeStatueBean.getStatusCode();
                        switch (statusCode) {
                            case 1:
                                SearchTaobaoBean searchTaobaoBean = new Gson().fromJson(response, SearchTaobaoBean.class);
                                if (gvTaobaoList == null) {
                                    return;
                                }
                                List<SearchTaobaoBean.DataBean> data = searchTaobaoBean.getData();
                                if (data != null) {
                                    int size = data.size();
                                    if (size == 0) {
                                        Toast.makeText(TaoSearchActivity.this, "没有找到你所查询的商品信息！", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                if (isLoadmore != -1) {
                                    page++;
                                    newData = searchTaobaoBean.getData();
                                    taobaodata.addAll(newData);
                                    taobaoWareListAdapter.notifyDataSetChanged();
                                } else {
                                    taobaodata = searchTaobaoBean.getData();
                                    newData = taobaodata;
                                    taobaoWareListAdapter = new TaobaoWareListAdapter(TaoSearchActivity.this, taobaodata);
                                    gvTaobaoList.setAdapter(taobaoWareListAdapter);
                                    page = 2;
                                }

                                taobaoWareListAdapter.setOnShareClickListener(
                                        new TaobaoWareListAdapter.OnShareClickListener() {
                                            @Override
                                            public void onShareClick(View v, int position) {
                                                SearchTaobaoBean.DataBean.CouponInfoBean coupon_info = taobaodata.get(position).getCoupon_info();
                                                if (coupon_info != null) {
                                                    String coupon_info_s = coupon_info.getCoupon_info();
                                                    goods_id = String.valueOf(taobaodata.get(position).getProductId());
                                                    goods_img = taobaodata.get(position).getImg_url();
                                                    goods_title = taobaodata.get(position).getTitle();
                                                    String sell_price = taobaodata.get(position).getSell_price();
                                                    goods_price = sell_price;
                                                    if (coupon_info_s != null && !TextUtils.isEmpty(coupon_info_s)) {
                                                        int index = coupon_info_s.indexOf("减") + 1;
                                                        String coupon = coupon_info_s.substring(index, coupon_info_s.length() - 1);
                                                        couponsPrice = Integer.parseInt(coupon);
                                                        goods_coupon = coupon;
                                                    }
                                                }
                                                boolean login = Tools.isLogin(TaoSearchActivity.this);
                                                adverId = Tools.getAdverId(TaoSearchActivity.this);
                                                userLogin = Tools.getUserLogin(TaoSearchActivity.this);
                                                if (!login) {
                                                    toLogin();
                                                    return;
                                                }
                                                whick = "changeurl";
                                                if (couponsPrice > 0) {
                                                    whick_share = 1;
                                                    map = new HashMap<>();
                                                    map = ShareUtil.getChangeWareParam(map, goods_id, adverId,userLogin);
                                                    ShareUtil.getCouponChangeUrl(TaoSearchActivity.this,netUtil2, map);
                                                } else {
                                                    whick_share = 2;
                                                    map = new HashMap<>();
                                                    map = ShareUtil.getChangeWareParam(map, goods_id, adverId,userLogin);
                                                    ShareUtil.getWareChangeUrl(TaoSearchActivity.this,netUtil2, map);
                                                }
                                            }
                                        }

                                );
                                gvTaobaoList.setOnItemClickListener(
                                        new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view,
                                                                    int i, long l) {
                                                SearchTaobaoBean.DataBean.CouponInfoBean coupon_info = taobaodata.get(i).getCoupon_info();
                                                if (coupon_info != null) {
                                                    String coupon_info_s = coupon_info.getCoupon_info();
                                                    String coupon_click_url = coupon_info.getCoupon_click_url();
                                                    String link_url = taobaodata.get(i).getLink_url();
                                                    String goods_id = String.valueOf(taobaodata.get(i).getProductId());
                                                    String goods_img = taobaodata.get(i).getImg_url();
                                                    String goods_title = taobaodata.get(i).getTitle();
                                                    String sell_price = taobaodata.get(i).getSell_price();
                                                    if (coupon_info_s != null && !TextUtils.isEmpty(coupon_info_s)) {
                                                        int index = coupon_info_s.indexOf("减") + 1;
                                                        String coupon = coupon_info_s.substring(index, coupon_info_s.length() - 1);
                                                        Intent intent = new Intent(TaoSearchActivity.this, WebActivity.class);
                                                        intent.putExtra("web_url", coupon_click_url);
                                                        intent.putExtra("url", link_url);
                                                        intent.putExtra("goods_id", goods_id);
                                                        intent.putExtra("goods_img", goods_img);
                                                        intent.putExtra("goods_title", goods_title);
                                                        intent.putExtra("goods_price", sell_price);
                                                        intent.putExtra("goods_coupon", coupon);
                                                        intent.putExtra("goods_coupon_url", coupon_click_url);
                                                        startActivity(intent);
                                                    } else {
                                                        Intent intent = new Intent(TaoSearchActivity.this, WebRecordActivity.class);
                                                        intent.putExtra("url", link_url);
                                                        intent.putExtra("goods_id", goods_id);
                                                        intent.putExtra("goods_img", goods_img);
                                                        intent.putExtra("goods_title", goods_title);
                                                        intent.putExtra("goods_price", sell_price);
                                                        startActivity(intent);
                                                    }
                                                } else {
                                                    String link_url = taobaodata.get(i).getLink_url();
                                                    String goods_id = String.valueOf(taobaodata.get(i).getProductId());
                                                    String goods_img = taobaodata.get(i).getImg_url();
                                                    String goods_title = taobaodata.get(i).getTitle();
                                                    String sell_price = taobaodata.get(i).getSell_price();
                                                    Intent intent = new Intent(TaoSearchActivity.this, WebRecordActivity.class);
                                                    intent.putExtra("url", link_url);
                                                    intent.putExtra("goods_id", goods_id);
                                                    intent.putExtra("goods_img", goods_img);
                                                    intent.putExtra("goods_title", goods_title);
                                                    intent.putExtra("goods_price", sell_price);
                                                    startActivity(intent);
                                                }
                                            }
                                        }

                                );
                                gvTaobaoList.setOnScrollListener(
                                        new AbsListView.OnScrollListener() {
                                            @Override
                                            public void onScrollStateChanged(AbsListView absListView,
                                                                             int scrollState) {
                                                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                                                    if (absListView.getLastVisiblePosition() == (absListView.getCount() - 1)) {
                                                        isLoadmore = 0;
                                                        LogUtil.e("page == " + page);
                                                        page_no = String.valueOf(page);
                                                        if (newData.size() < Integer.valueOf(page_size)) {
                                                            showLoadNoMore();
                                                        } else {
                                                            showLoadMore();
                                                            searchTaobaoWare(keyword, city, type, is_tmall, start_price, end_price, page_no);
                                                        }
                                                    } else {
                                                        llTaobaoLoadmore.setVisibility(View.GONE);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                                            }
                                        }

                                );
                                llTaobaoLoadmore.setVisibility(View.GONE);
                                mIsRequestDataRefresh = false;

                                setRefresh(mIsRequestDataRefresh);

                                break;
                            case 0:
                                Toast.makeText(TaoSearchActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void getFailResponse(Call call, Exception e) {
                        LogUtil.e(call.toString() + "--" + e.getMessage());
                        if (llTaobaoLoadmore != null) {
                            llTaobaoLoadmore.setVisibility(View.GONE);
                        }
                        if (swip_taobao_refresh != null && swip_taobao_refresh.isRefreshing()) {
                            swip_taobao_refresh.setRefreshing(false);
                        }
                        EncryptNetUtil.startNeterrorPage(TaoSearchActivity.this);
                    }

                }

        );
        netUtil2.setOnServerResponseListener(
                new NetUtil.OnServerResponseListener() {
                    @Override
                    public void getSuccessResponse(String response) {
                        try {
                            if (whick.equals("changeurl")) {
                                parseChangeData(response);
                            } else if (whick.equals("getwords")) {
                                parseChangeWordsData(response);
                            }
                            hindBoardInput();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void getFailResponse(Call call, Exception e) {
                        LogUtil.e(call.toString() + "-" + e.getMessage());
                        hindBoardInput();
                        EncryptNetUtil.startNeterrorPage(TaoSearchActivity.this);
                    }
                }
        );
        rl_exit.setOnClickListener(new MyOnclickListener());
        rlTaobaoSousuo.setOnClickListener(new MyOnclickListener());
        llTaobaoZonghe.setOnClickListener(new MyOnclickListener());
        llTaobaoXiaoliang.setOnClickListener(new MyOnclickListener());
        llTaobaoPrice.setOnClickListener(new MyOnclickListener());
        llTaobaoShaixuan.setOnClickListener(new MyOnclickListener());
        rlEditTextExit.setOnClickListener(new MyOnclickListener());
        etTaobaoSearchContent.addTextChangedListener(watcher);
        etTaobaoSearchContent.setOnEditorActionListener
                (new TextView.OnEditorActionListener() {
                     @Override
                     public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                         if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                             String sousuoStr = etTaobaoSearchContent.getText().toString().trim();
                             if (TextUtils.isEmpty(sousuoStr)) {
                                 llNewsSearchAfter.setVisibility(View.GONE);
                                 return true;
                             }
                             initChoice();
                             Toast.makeText(TaoSearchActivity.this, "正在搜索中，请稍后！", Toast.LENGTH_SHORT).show();
                             hindBoardInput();
                             keyword = sousuoStr;
                             isLoadmore = -1;
                             page_no = "1";
                             page = 1;
                             city = "";//城市
                             String type = "";//排序
                             is_tmall = "";//是否是天猫
                             start_price = "";//范围下限
                             end_price = "";//范围上限
                             if (taobaoShaixuanPopupwindow != null) {
                                 taobaoShaixuanPopupwindow.setNoChecked();
                             }
                             searchTaobaoWare(keyword, city, type, is_tmall, start_price, end_price, page_no);
                             return true;
                         }
                         return false;
                     }
                 }
                );
    }

    /**
     * 隐藏软键盘
     */
    private void hindBoardInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etTaobaoSearchContent.getWindowToken(), 0);
    }

    private void toLogin() {
        Intent intent = new Intent(TaoSearchActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 解析优惠券转链的数据
     *
     * @param response
     */
    private void parseChangeData(String response) throws Exception {

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
                    ShareUtil.getChangeWordUrl(TaoSearchActivity.this,netUtil2, map);
                }
                break;
            case 0:
                Toast.makeText(TaoSearchActivity.this, changeUrlBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析获取淘口令数据
     *
     * @param response
     */
    private void parseChangeWordsData(String response) throws Exception {

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
                ShareUtil.showCopyDialog(TaoSearchActivity.this, whick_share, share_content, goods_price, shareListener, goods_img,
                        goods_title, dialog);
                break;
            case 0:
                Toast.makeText(TaoSearchActivity.this, taoWordBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }

    }

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
            Toast.makeText(TaoSearchActivity.this, "分享成功", Toast.LENGTH_LONG).show();
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
            Toast.makeText(TaoSearchActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(TaoSearchActivity.this, "分享取消", Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    };


    /**
     * 显示加载更多
     */
    private void showLoadMore() {
        if (llTaobaoLoadmore != null) {
            llTaobaoLoadmore.setVisibility(View.VISIBLE);
            TextView tvTitle = (TextView) llTaobaoLoadmore.getChildAt(1);
            llTaobaoLoadmore.getChildAt(0).setVisibility(View.VISIBLE);
            tvTitle.setText("加载更多...");
        }
    }

    /**
     * 显示正在加载中
     */
    private void showLoading() {
        if (llTaobaoLoadmore != null) {
            llTaobaoLoadmore.setVisibility(View.VISIBLE);
            TextView tvTitle = (TextView) llTaobaoLoadmore.getChildAt(1);
            llTaobaoLoadmore.getChildAt(0).setVisibility(View.VISIBLE);
            tvTitle.setText("正在加载中...");
        }
    }

    /**
     * 显示没有更多的数据了
     */
    private void showLoadNoMore() {
        if (llTaobaoLoadmore != null) {
            TextView tvTitle = (TextView) llTaobaoLoadmore.getChildAt(1);
            llTaobaoLoadmore.getChildAt(0).setVisibility(View.GONE);
            tvTitle.setText("没有更多的数据了...");
            llTaobaoLoadmore.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (taobaoShaixuanPopupwindow != null && taobaoShaixuanPopupwindow.isShowing()) {
            taobaoShaixuanPopupwindow.dismiss();
        }
        if (taobaoZonghePopupwindow != null && taobaoZonghePopupwindow.isShowing()) {
            taobaoZonghePopupwindow.dismiss();
        }

    }

    private void initChoice() {
        ivUpPrice.setImageResource(R.drawable.up_gray_arraw);
        ivDownPrice.setImageResource(R.drawable.down_gray_arraw);
        ivTaobaoShaixuanSort.setSelected(false);
        tvTaobaoShaixuanSort.setSelected(false);
        tvTaobaoPriceSort.setSelected(false);
        tvTaobaoZongheSort.setSelected(false);
        ivTaobaoZongheSort.setSelected(false);
        tvTaobaoXiaoliangSort.setSelected(false);
    }

    /**
     * 监听点击事件的类
     */
    public class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_taobao_zonghe:
                    tvTaobaoZongheSort.setSelected(true);
                    ivTaobaoZongheSort.setSelected(true);
                    textSelectChange(0);
                    page_no = "1";
                    isLoadmore = -1;
                    ivUpPrice.setImageResource(R.drawable.up_gray_arraw);
                    ivDownPrice.setImageResource(R.drawable.down_gray_arraw);
                    ivTaobaoShaixuanSort.setSelected(false);
                    showPopMenu2();
                    break;
                case R.id.ll_taobao_xiaoliang:
                    tvTaobaoXiaoliangSort.setSelected(true);
                    textSelectChange(1);
                    ivUpPrice.setImageResource(R.drawable.up_gray_arraw);
                    ivDownPrice.setImageResource(R.drawable.down_gray_arraw);
                    imgSelectChanger();
                    type = "4";
                    page_no = "1";
                    isLoadmore = -1;
                    showLoading();
                    searchTaobaoWare(keyword, city, type, is_tmall, start_price, end_price, page_no);
                    break;
                case R.id.ll_taobao_price:
                    page_no = "1";
                    isLoadmore = -1;
                    tvTaobaoPriceSort.setSelected(true);
                    textSelectChange(2);
                    price_sort = !price_sort;
                    setPriceSortImg(price_sort);
                    imgSelectChanger();
                    if (price_sort) {
                        type = "6";
                    } else {
                        type = "5";
                    }
                    showLoading();
                    searchTaobaoWare(keyword, city, type, is_tmall, start_price, end_price, page_no);
                    break;
                case R.id.ll_taobao_shaixuan:
                    page_no = "1";
                    isLoadmore = -1;
                    tvTaobaoShaixuanSort.setSelected(true);
                    ivTaobaoShaixuanSort.setSelected(true);
                    textSelectChange(3);
                    ivUpPrice.setImageResource(R.drawable.up_gray_arraw);
                    ivDownPrice.setImageResource(R.drawable.down_gray_arraw);
                    ivTaobaoZongheSort.setSelected(false);
                    showPopMenu();
                    break;
                case R.id.rl_taobao_sousuo:
                    String sousuoStr = etTaobaoSearchContent.getText().toString().trim();
                    hindBoardInput();
                    if (TextUtils.isEmpty(sousuoStr)) {
                        return;
                    }
                    initChoice();
                    Toast.makeText(TaoSearchActivity.this, "正在搜索中，请稍后！", Toast.LENGTH_SHORT).show();

                    keyword = sousuoStr;
                    page_no = "1";
                    page = 1;
                    isLoadmore = -1;
                    city = "";//城市
                    String type = "";//排序
                    is_tmall = "";//是否是天猫
                    start_price = "";//范围下限
                    end_price = "";//范围上限
                    if (taobaoShaixuanPopupwindow != null) {
                        taobaoShaixuanPopupwindow.setNoChecked();
                    }
                    searchTaobaoWare(keyword, city, type, is_tmall, start_price, end_price, page_no);
                    break;
                case R.id.rl_news_search_before:
                    llNewsSearchAfter.setVisibility(View.VISIBLE);
                    // 获取编辑框焦点
                    etTaobaoSearchContent.setFocusable(true);
                    etTaobaoSearchContent.requestFocus();
                    //打开软键盘
                    InputMethodManager im = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    break;
                case R.id.rl_edit_text_exit:
                    etTaobaoSearchContent.setText("");
                    break;
                case R.id.rl_exit:
                    hindBoardInput();
                    finish();
                    break;
            }
        }
    }

    /**
     * 搜索淘宝商品
     */
    private void searchTaobaoWare(String keyword, String city, String type, String is_tmall
            , String start_price, String end_price, String page_no) {
        map = new HashMap<>();
        map.put("keyword", keyword);
        map.put("city", city);
        map.put("type", type);
        map.put("is_tmall", is_tmall);
        map.put("start_price", start_price);
        map.put("end_price", end_price);
        map.put("page_no", page_no);
        map.put("page_size", page_size);
        netUtil.okHttp2Server2(TaoSearchActivity.this,search_url, map);
    }

    /**
     * 设置升降序价格显示图标
     *
     * @param price_sort
     */
    private void setPriceSortImg(boolean price_sort) {
        if (price_sort) {
            ivUpPrice.setImageResource(R.drawable.up_red_arraw);
            ivDownPrice.setImageResource(R.drawable.down_gray_arraw);
        } else {
            ivUpPrice.setImageResource(R.drawable.up_gray_arraw);
            ivDownPrice.setImageResource(R.drawable.down_red_arraw);
        }
    }

    public void textSelectChange(int pos) {
        TextView[] compont_texts = {tvTaobaoZongheSort, tvTaobaoXiaoliangSort, tvTaobaoPriceSort, tvTaobaoShaixuanSort};
        for (int i = 0; i < compont_texts.length; i++) {
            if (i != pos) {
                compont_texts[i].setSelected(false);
            }
        }
    }

    public void imgSelectChanger() {
        ivTaobaoShaixuanSort.setSelected(false);
        ivTaobaoZongheSort.setSelected(false);
    }

    private void showPopMenu() {
        if (taobaoShaixuanPopupwindow == null) {
            taobaoShaixuanPopupwindow = new TaobaoShaixuanPopupwindow(TaoSearchActivity.this);
            taobaoShaixuanPopupwindow.showAsDropDown(llTaobaoShaixuan, 5, 5);
        } else if (taobaoShaixuanPopupwindow != null
                && taobaoShaixuanPopupwindow.isShowing()) {
            taobaoShaixuanPopupwindow.dismiss();
        } else {
            taobaoShaixuanPopupwindow.showAsDropDown(llTaobaoShaixuan, 5, 5);
        }

        taobaoShaixuanPopupwindow.setOnPopupClickListener(new TaobaoShaixuanPopupwindow.OnPopupClickListener() {
            @Override
            public void onPopupClick(String start, String end, boolean check, String c) {
                start_price = start;
                end_price = end;
                if (check) {
                    is_tmall = "1";
                } else {
                    is_tmall = "0";
                }
                city = c;
                showLoading();
                searchTaobaoWare(keyword, city, type, is_tmall, start_price, end_price, page_no);
                if (taobaoShaixuanPopupwindow.isShowing()) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            taobaoShaixuanPopupwindow.dismiss();
                        }
                    }, 200);

                }
            }

            @Override
            public void onListClick(String c) {
                city = c;
                showLoading();
                searchTaobaoWare(keyword, city, type, is_tmall, start_price, end_price, page_no);
                if (taobaoShaixuanPopupwindow.isShowing()) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            taobaoShaixuanPopupwindow.dismiss();
                        }
                    }, 200);

                }
            }
        });

    }

    private void showPopMenu2() {
        if (taobaoZonghePopupwindow == null) {
            taobaoZonghePopupwindow = new TaobaoZonghePopupwindow(TaoSearchActivity.this);
            taobaoZonghePopupwindow.showAsDropDown(llTaobaoZonghe, 5, 5);
        } else if (taobaoZonghePopupwindow != null
                && taobaoZonghePopupwindow.isShowing()) {
            taobaoZonghePopupwindow.dismiss();
        } else {
            taobaoZonghePopupwindow.showAsDropDown(llTaobaoZonghe, 5, 5);
        }

        taobaoZonghePopupwindow.setOnTypeSelectedListener(new TaobaoZonghePopupwindow.OnTypeSelectedListener() {
            @Override
            public void onSelectedPos(int pos) {
                int types = pos + 1;
                type = String.valueOf(types);
                showLoading();
                searchTaobaoWare(keyword, city, type, is_tmall, start_price, end_price, page_no);
                if (taobaoZonghePopupwindow.isShowing()) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            taobaoZonghePopupwindow.dismiss();
                        }
                    }, 200);

                }
            }
        });
    }

    public void onEvent(AnyEvent event) {
        if (event.getType() == EventType.EVENT_LOGIN) {
            String msg = "onEventMainThread收到了消息：" + event.getMessage();
            LogUtil.e(msg);
            initData();
        }
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

            int length = s.length();
            if (length == 0) {
                tvNewsEditSearch.setText("取消");
                rlEditTextExit.setVisibility(View.GONE);
            } else {
                tvNewsEditSearch.setText("搜索");
                rlEditTextExit.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };

}
