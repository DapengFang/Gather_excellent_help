package com.gather_excellent_help.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
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
import com.gather_excellent_help.bean.ListBean;
import com.gather_excellent_help.bean.SearchWareBean;
import com.gather_excellent_help.bean.TaoWordBean;
import com.gather_excellent_help.ui.activity.suning.SuningDetailActivity;
import com.gather_excellent_help.ui.adapter.WareListAdapter;
import com.gather_excellent_help.ui.adapter.WareSelectorAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.TypeSelectorPopupwindow;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
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
import okhttp3.Call;


public class WareListActivity extends BaseActivity implements Animation.AnimationListener {

    @Bind(R.id.gv_wart_list)
    GridView gvWartList;
    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.et_ware_list_content)
    EditText etWareListContent;
    @Bind(R.id.rl_sousuo)
    RelativeLayout rlSousuo;
    @Bind(R.id.ll_ware_list_pingpai)
    LinearLayout llWareListPingpai;
    @Bind(R.id.ll_ware_list_price)
    LinearLayout llWareListPrice;
    @Bind(R.id.ll_ware_list_leibie)
    LinearLayout llWareListLeibie;
    @Bind(R.id.ll_ware_list_rongliang)
    LinearLayout llWareListRongliang;
    @Bind(R.id.iv_up_price)
    ImageView ivUpPrice;
    @Bind(R.id.iv_down_price)
    ImageView ivDownPrice;
    @Bind(R.id.ll_ware_list_loadmore)
    LinearLayout ll_ware_list_loadmore;
    @Bind(R.id.ll_ware_list_sousuo)
    LinearLayout ll_ware_list_sousuo;
    @Bind(R.id.tv_ware_list_leibie)
    TextView tvWareListLeibie;
    @Bind(R.id.tv_ware_list_pingpai)
    TextView tvWareListPingpai;
    @Bind(R.id.tv_ware_list_rongliang)
    TextView tvWareListRongliang;
    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.activity_ware_list)
    LinearLayout activityWareList;
    @Bind(R.id.rl_edit_text_exit)
    RelativeLayout rlEditTextExit;

    private NetUtil netUtil;//搜索条件列表的网络接口
    private NetUtil netUtil2;//搜索商品列表的网络接口
    private NetUtil netUtil3;//分享的网络接口
    private String pingpai_url = Url.BASE_URL + "goodsBrand.aspx";
    private TypeSelectorPopupwindow typeSelectorPopupwindow;
    private int crr_click = -1; //记录几点那个条目（类别，品牌，容量）
    private int sort_price = SortPrice.ASC.ordinal();
    private String sousuoStr;
    private String sousuo_url = Url.BASE_URL + "SearchGoods.aspx";
    private Map<String, String> map;
    private String keyword = "";//关键字
    private String PriceOder = "2";//1----降序，2-----升序
    private String Type = "";//商品类型
    private String brandId = "";//品牌
    private String capacity = "";//容量
    private String pageIndex = "1";//页数
    private String pageSize = "10";//每一页的数量
    private WareListAdapter wareListAdapter;
    private List<ListBean.DataBean> conditionData; //查询条件数据
    private List<SearchWareBean.DataBean> wareData;
    private int page = 1;//显示第几页
    private boolean isCanLoad = true;
    private Handler handler = new Handler();

    private String type_id;
    private String ware_url = Url.BASE_URL + "CategoryGoodList.aspx";
    private int isLoadmore = -1;
    private List<SearchWareBean.DataBean> newData;
    private String content_et = "";
    private String activity_id = "";
    private String activity_url = Url.BASE_URL + "ActivityMoreList.aspx";
    private String home_search_url = Url.BASE_URL + "IndexSearch.aspx";
    private Intent intent;

    private boolean mIsTitleHide = false;
    private boolean mIsAnim = false;
    private float lastX = 0;
    private float lastY = 0;
    private String leibie_url = Url.BASE_URL + "ClassList.aspx";
    private String rongliang_url = Url.BASE_URL + "SubCategoryList.aspx";

    private String whick = "";
    private String taoWord;
    private String click_url;
    private int whick_share;
    private String share_content;
    private String userLogin;
    private String goods_price;
    private AlertDialog dialog;
    private String goods_img;
    private String goods_title;
    private String goods_coupon;
    private String adverId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_list);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        rlEditTextExit.setVisibility(View.GONE);
        showLoading();
        userLogin = Tools.getUserLogin(this);
        netUtil = new NetUtil();
        netUtil2 = new NetUtil();
        netUtil3 = new NetUtil();
        map = new HashMap<>();
        intent = getIntent();
        sousuoStr = intent.getStringExtra("content");
        if (sousuoStr == null) {
            sousuoStr = "";
        }
        type_id = intent.getStringExtra("type_id");
        String type_title = intent.getStringExtra("type_title");
        activity_id = intent.getStringExtra("activity_id");
        if (type_id != null && !TextUtils.isEmpty(type_id)) {
            if (type_title != null && !TextUtils.isEmpty(type_title)) {
                if (type_title.length() > 2) {
                    type_title = type_title.substring(0, 2);
                }
                tvWareListLeibie.setText(type_title);
            }
            Type = type_id;
            etWareListContent.setHint("请输入商品名称");
            map = new HashMap<>();
            map.put("id", type_id);
            map.put("key_words", keyword);
            map.put("PriceOder", PriceOder);
            map.put("Type", Type);
            map.put("brandId", brandId);
            map.put("capacity", capacity);
            map.put("pageSize", pageSize);
            map.put("pageIndex", "1");
            netUtil2.okHttp2Server2(WareListActivity.this,ware_url, map);
        } else {
            etWareListContent.setHint("请输入商品名称");
            if (sousuoStr.equals("isQiang")) {
                map = new HashMap<>();
                map.put("key_words", keyword);
                map.put("pageSize", pageSize);
                map.put("pageIndex", "1");
                map.put("PriceOder", PriceOder);
                map.put("Type", Type);
                map.put("brandId", brandId);
                map.put("capacity", capacity);
                String qiang_url = Url.BASE_URL + "GroupBuyList.aspx";
                netUtil2.okHttp2Server2(WareListActivity.this,qiang_url, map);
            } else if (sousuoStr.equals("isVip")) {
                map = new HashMap<>();
                map.put("key_words", keyword);
                map.put("pageSize", pageSize);
                map.put("pageIndex", "1");
                map.put("PriceOder", PriceOder);
                map.put("Type", Type);
                map.put("brandId", brandId);
                map.put("capacity", capacity);
                String vip_url = Url.BASE_URL + "ChannelPriceList.aspx";
                netUtil2.okHttp2Server2(WareListActivity.this,vip_url, map);
            } else if (sousuoStr.equals("activity")) {
                map = new HashMap<>();
                map.put("key_words", keyword);
                map.put("pageSize", pageSize);
                map.put("pageIndex", "1");
                map.put("activity_id", activity_id);
                map.put("PriceOder", PriceOder);
                map.put("Type", Type);
                map.put("brandId", brandId);
                map.put("capacity", capacity);
                netUtil2.okHttp2Server2(WareListActivity.this,activity_url, map);
            } else if (sousuoStr.equals("isTypeSou")) {
                map = new HashMap<>();
                map.put("key_words", keyword);
                map.put("pageSize", pageSize);
                map.put("pageIndex", "1");
                map.put("PriceOder", PriceOder);
                map.put("Type", Type);
                map.put("brandId", brandId);
                map.put("capacity", capacity);
                netUtil2.okHttp2Server2(WareListActivity.this,sousuo_url, map);
            } else if (sousuoStr.equals("isHomeSou")) {
                map = new HashMap<>();
                map.put("key_words", keyword);
                map.put("pageSize", pageSize);
                map.put("pageIndex", "1");
                map.put("PriceOder", PriceOder);
                map.put("Type", Type);
                map.put("brandId", brandId);
                map.put("capacity", capacity);
                netUtil2.okHttp2Server2(WareListActivity.this,home_search_url, map);
            }
        }
        rlExit.setOnClickListener(new MyOnClickListener());
        llWareListPrice.setOnClickListener(new MyOnClickListener());
        llWareListLeibie.setOnClickListener(new MyOnClickListener());
        llWareListPingpai.setOnClickListener(new MyOnClickListener());
        llWareListRongliang.setOnClickListener(new MyOnClickListener());
        rlSousuo.setOnClickListener(new MyOnClickListener());
        rlEditTextExit.setOnClickListener(new MyOnClickListener());
        etWareListContent.addTextChangedListener(watcher);
        etWareListContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Toast.makeText(WareListActivity.this, "正在搜索中，请稍后！", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etWareListContent.getWindowToken(), 0);
                    isLoadmore = -1;
                    isCanLoad = true;
                    ll_ware_list_loadmore.setVisibility(View.GONE);
                    content_et = etWareListContent.getText().toString().trim();
                    if (content_et != null && !TextUtils.isEmpty(content_et)) {
                        keyword = content_et;
                        page = 1;
                        PriceOder = "2";
                        setSortPriceIcon(SortPrice.ASC.ordinal());
                        Type = "";
                        brandId = "";
                        capacity = "";
                        type_id = "";
                        if (tvWareListRongliang != null) {
                            tvWareListRongliang.setText("规格");
                        }
                        if (tvWareListPingpai != null) {
                            tvWareListPingpai.setText("品牌");
                        }
                        sousuoStr = "isHomeSou";
                        map = new HashMap<>();
                        map.put("key_words", keyword);
                        map.put("pageSize", "10");
                        map.put("pageIndex", "1");
                        map.put("PriceOder", PriceOder);
                        map.put("Type", Type);
                        map.put("brandId", brandId);
                        map.put("capacity", capacity);
                        netUtil2.okHttp2Server2(WareListActivity.this,home_search_url, map);
                    } else {
                        Toast.makeText(WareListActivity.this, "请输入关键字！", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "--" + e.getMessage());
                EncryptNetUtil.startNeterrorPage(WareListActivity.this);
            }
        });
        netUtil2.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                parseData2(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "--" + e.getMessage());
                EncryptNetUtil.startNeterrorPage(WareListActivity.this);
            }
        });
        netUtil3.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                if (whick.equals("changeurl")) {
                    parseChangeData(response);
                } else if (whick.equals("getwords")) {
                    parseChangeWordsData(response);
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "-" + e.getMessage());
                EncryptNetUtil.startNeterrorPage(WareListActivity.this);
            }
        });
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
                        ShareUtil.getChangeWordUrl(WareListActivity.this,netUtil3, map);
                    }
                    break;
                case 0:
                    Toast.makeText(WareListActivity.this, changeUrlBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(WareListActivity.this, "系统服务出现异常，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
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
                    ShareUtil.showCopyDialog(WareListActivity.this, whick_share, share_content, goods_price, shareListener, goods_img,
                            goods_title, dialog);
                    break;
                case 0:
                    Toast.makeText(WareListActivity.this, taoWordBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(WareListActivity.this, "系统服务出现异常，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(WareListActivity.this, "分享成功", Toast.LENGTH_LONG).show();
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
            Toast.makeText(WareListActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(WareListActivity.this, "分享取消", Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    };


    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            int length = s.length();
            if (length == 0) {
                rlEditTextExit.setVisibility(View.GONE);
            } else {
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

    /**
     * @param response 解析搜索后的商品数据
     */
    private void parseData2(String response) {
        SearchWareBean searchWareBean = new Gson().fromJson(response, SearchWareBean.class);
        int statusCode = searchWareBean.getStatusCode();
        switch (statusCode) {
            case 1:
                if (gvWartList == null) {
                    return;
                }
                List<SearchWareBean.DataBean> data = searchWareBean.getData();
                if (isLoadmore != -1) {
                    page++;
                    isCanLoad = true;
                    LogUtil.e("page == " + page);
                    newData = searchWareBean.getData();
                    wareData.addAll(newData);
                    wareListAdapter.notifyDataSetChanged();
                } else {
                    isCanLoad = true;
                    wareData = searchWareBean.getData();
                    newData = wareData;
                    wareListAdapter = new WareListAdapter(WareListActivity.this, wareData, sousuoStr);
                    gvWartList.setAdapter(wareListAdapter);
                    page = 2;
                }
                if (data != null) {
                    int size = data.size();
                    if (size == 0) {
                        Toast.makeText(WareListActivity.this, "没有找到你查询的商品信息！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                wareListAdapter.setOnShareClickListener(new WareListAdapter.OnShareClickListener() {
                    @Override
                    public void onShareClick(View v, int position) {
                        DecimalFormat df = new DecimalFormat("#0.00");
                        int site_id = wareData.get(position).getSite_id();
                        int article_id = wareData.get(position).getArticle_id();
                        String goods_id = wareData.get(position).getProductId();
                        goods_img = wareData.get(position).getImg_url();
                        goods_title = wareData.get(position).getTitle();
                        double sell_price = wareData.get(position).getSell_price();
                        double market_price = wareData.get(position).getMarket_price();
                        String couponsUrl = wareData.get(position).getCouponsUrl();
                        String link_url = wareData.get(position).getLink_url();
                        int couponsPrice = wareData.get(position).getCouponsPrice();
                        goods_price = df.format(sell_price);
                        goods_coupon = df.format(couponsPrice);
                        boolean login = Tools.isLogin(WareListActivity.this);
                        adverId = Tools.getAdverId(WareListActivity.this);
                        userLogin = Tools.getUserLogin(WareListActivity.this);
                        if (!login) {
                            toLogin();
                            return;
                        }
                        if (site_id == 1) {
                            whick = "changeurl";
                            if (couponsPrice > 0) {
                                whick_share = 1;
                                map = new HashMap<>();
                                map = ShareUtil.getChangeWareParam(map, goods_id, adverId,userLogin);
                                ShareUtil.getCouponChangeUrl(WareListActivity.this,netUtil3, map);
                            } else {
                                whick_share = 2;
                                map = new HashMap<>();
                                map = ShareUtil.getChangeWareParam(map, goods_id, adverId,userLogin);
                                ShareUtil.getWareChangeUrl(WareListActivity.this,netUtil3, map);
                            }
                        }
                    }
                });
                gvWartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        DecimalFormat df = new DecimalFormat("#0.00");
                        int site_id = wareData.get(i).getSite_id();
                        int article_id = wareData.get(i).getArticle_id();
                        String goods_id = wareData.get(i).getProductId();
                        String goods_img = wareData.get(i).getImg_url();
                        String goods_title = wareData.get(i).getTitle();
                        double sell_price = wareData.get(i).getSell_price();
                        double market_price = wareData.get(i).getMarket_price();
                        if (site_id == 1) {
                            if (sousuoStr.equals("isVip")) {
                                String link_url = wareData.get(i).getLink_url();
                                Intent intent = new Intent(WareListActivity.this, WebRecordActivity.class);
                                intent.putExtra("url", link_url);
                                intent.putExtra("goods_id", goods_id);
                                intent.putExtra("goods_img", goods_img);
                                intent.putExtra("goods_title", goods_title);
                                intent.putExtra("goods_price", df.format(sell_price) + "");
                                startActivity(intent);
                            } else {
                                String couponsUrl = wareData.get(i).getCouponsUrl();
                                String link_url = wareData.get(i).getLink_url();
                                int couponsPrice = wareData.get(i).getCouponsPrice();
                                if (couponsPrice > 0) {
                                    if (couponsUrl != null && !TextUtils.isEmpty(couponsUrl)) {
                                        Intent intent = new Intent(WareListActivity.this, WebActivity.class);
                                        intent.putExtra("web_url", couponsUrl);
                                        intent.putExtra("url", link_url);
                                        intent.putExtra("goods_id", goods_id);
                                        intent.putExtra("goods_img", goods_img);
                                        intent.putExtra("goods_title", goods_title);
                                        intent.putExtra("goods_price", df.format(sell_price) + "");
                                        intent.putExtra("goods_coupon", String.valueOf(couponsPrice));
                                        intent.putExtra("goods_coupon_url", couponsUrl);
                                        startActivity(intent);
                                    }
                                } else {
                                    Intent intent = new Intent(WareListActivity.this, WebRecordActivity.class);
                                    intent.putExtra("url", link_url);
                                    intent.putExtra("goods_id", goods_id);
                                    intent.putExtra("goods_img", goods_img);
                                    intent.putExtra("goods_title", goods_title);
                                    intent.putExtra("goods_price", df.format(sell_price) + "");
                                    startActivity(intent);
                                }
                            }
                        } else if (site_id == 2) {
                            //苏宁
                            Intent intent = new Intent(WareListActivity.this, SuningDetailActivity.class);
                            intent.putExtra("article_id", article_id);
                            intent.putExtra("goods_id", goods_id);
                            intent.putExtra("goods_img", goods_img);
                            intent.putExtra("goods_title", goods_title);
                            intent.putExtra("goods_price", df.format(sell_price) + "");
                            intent.putExtra("c_price", df.format(market_price) + "");
                            startActivity(intent);
                        }
                    }
                });

                ll_ware_list_loadmore.setVisibility(View.GONE);

                gvWartList.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                            if (!isCanLoad) {
                                return;
                            }
                            if (absListView.getLastVisiblePosition() == (absListView.getCount() - 1)) {
                                isLoadmore = 0;
                                if (type_id != null && !TextUtils.isEmpty(type_id)) {
                                    pageIndex = String.valueOf(page);
                                    LogUtil.e("newData.size() ==" + newData.size());
                                    if (newData.size() < Integer.valueOf(pageSize)) {
                                        showLoadNoMore();
                                    } else {
                                        showLoadMore();
                                        searchWareList();
                                        isCanLoad = false;
                                    }
                                } else {
                                    if (sousuoStr != null && !TextUtils.isEmpty(sousuoStr)) {
                                        LogUtil.e("page == " + page);
                                        pageIndex = String.valueOf(page);
                                        LogUtil.e("newData.size() ==" + newData.size());
                                        if (newData.size() < Integer.valueOf(pageSize)) {
                                            showLoadNoMore();
                                        } else {
                                            showLoadMore();
                                            searchWareList();
                                            isCanLoad = false;
                                        }
                                    }
                                }
                            } else {
                                ll_ware_list_loadmore.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                    }
                });
                ll_ware_list_loadmore.setVisibility(View.GONE);
                break;
            case 0:
                Toast.makeText(WareListActivity.this, searchWareBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void toLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void showLoadMore() {
        ll_ware_list_loadmore.setVisibility(View.VISIBLE);
        ll_ware_list_loadmore.getChildAt(0).setVisibility(View.VISIBLE);
        TextView tvTitle = (TextView) ll_ware_list_loadmore.getChildAt(1);
        tvTitle.setText("加载更多...");
    }

    private void showLoadNoMore() {
        TextView tvTitle = (TextView) ll_ware_list_loadmore.getChildAt(1);
        tvTitle.setText("没有更多的数据了...");
        ll_ware_list_loadmore.getChildAt(0).setVisibility(View.GONE);
        ll_ware_list_loadmore.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        TextView tvTitle = (TextView) ll_ware_list_loadmore.getChildAt(1);
        tvTitle.setText("正在加载中...");
        ll_ware_list_loadmore.getChildAt(0).setVisibility(View.VISIBLE);
        ll_ware_list_loadmore.setVisibility(View.VISIBLE);
    }

    /**
     * @param response 解析数据
     */
    private void parseData(String response) {
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
                Toast.makeText(WareListActivity.this, listBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showPopMenu(List<ListBean.DataBean> data) {
        if (typeSelectorPopupwindow == null) {
            typeSelectorPopupwindow = new TypeSelectorPopupwindow(WareListActivity.this, data);
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
                        typeSelectorPopupwindow.showAsDropDown(llWareListPingpai, 5, 5);
                    }
                }, 300);
            }
        }

    }

    /**
     * 显示popupwindow
     */
    private void showPopupwindow() {
        switch (crr_click) {
            case 0:
                typeSelectorPopupwindow.showAsDropDown(llWareListLeibie, 5, 5);
                break;
            case 1:
                typeSelectorPopupwindow.showAsDropDown(llWareListPingpai, 5, 5);
                break;
            case 2:
                typeSelectorPopupwindow.showAsDropDown(llWareListRongliang, 5, 5);
                break;
        }
        typeSelectorPopupwindow.setOnItemClickListener(new WareSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                isLoadmore = -1;
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
                    tvWareListPingpai.setText("品牌");
                    tvWareListRongliang.setText("规格");
                    tvWareListLeibie.setText(title);
                } else if (crr_click == 1) {
                    if (id == -1) {
                        brandId = "";
                    } else {
                        brandId = String.valueOf(id);
                    }
                    capacity = "";
                    tvWareListRongliang.setText("规格");
                    tvWareListPingpai.setText(title);
                } else if (crr_click == 2) {
                    if (id == -1) {
                        capacity = "";
                    } else {
                        capacity = String.valueOf(id);
                    }
                    tvWareListRongliang.setText(title);
                }
                ll_ware_list_loadmore.setVisibility(View.VISIBLE);
                TextView tv = (TextView) ll_ware_list_loadmore.getChildAt(1);
                tv.setText("正在加载中");
                showLoadNoMore();
                searchWareList();
                if (typeSelectorPopupwindow.isShowing()) {
                    typeSelectorPopupwindow.dismiss();
                }
            }
        });
    }

    /**
     * 将popupwindow置空，方便后面调用
     */
    private void setPopMenuNull() {
        if (typeSelectorPopupwindow != null) {
            typeSelectorPopupwindow = null;
        }
    }


    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_ware_list_price:
                    isLoadmore = -1;
                    isCanLoad = true;
                    if (sort_price == SortPrice.ASC.ordinal()) {
                        sort_price = SortPrice.DESC.ordinal();
                    } else {
                        sort_price = SortPrice.ASC.ordinal();
                    }
                    setSortPriceIcon(sort_price);
                    setSortPriceWareList(sort_price);
                    break;
                case R.id.ll_ware_list_leibie:
                    isLoadmore = -1;
                    isCanLoad = true;
                    crr_click = 0;
                    if (activity_id != null) {
                        activity_id = "0";
                    }
                    map = new HashMap<>();
                    netUtil.okHttp2Server2(WareListActivity.this,leibie_url, null);
                    break;
                case R.id.ll_ware_list_pingpai:
                    isLoadmore = -1;
                    isCanLoad = true;
                    crr_click = 1;
                    map = new HashMap<>();
                    if (Type != null && !TextUtils.isEmpty(Type)) {
                        map.put("id", Type);
                        netUtil.okHttp2Server2(WareListActivity.this,pingpai_url, map);
                    } else {
                        Toast.makeText(WareListActivity.this, "请选择类别！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.ll_ware_list_rongliang:
                    isLoadmore = -1;
                    isCanLoad = true;
                    crr_click = 2;
                    if (Type != null && !TextUtils.isEmpty(Type)) {
                        map = new HashMap<>();
                        map.put("sub_id", Type);
                        netUtil.okHttp2Server2(WareListActivity.this,rongliang_url, map);
                    } else {
                        Toast.makeText(WareListActivity.this, "请选择类别！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.rl_sousuo:
                    Toast.makeText(WareListActivity.this, "正在搜索中，请稍后！", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etWareListContent.getWindowToken(), 0);
                    isLoadmore = -1;
                    isCanLoad = true;
                    ll_ware_list_loadmore.setVisibility(View.GONE);
                    content_et = etWareListContent.getText().toString().trim();
                    if (content_et != null && !TextUtils.isEmpty(content_et)) {
                        keyword = content_et;
                        page = 1;
                        PriceOder = "2";
                        setSortPriceIcon(SortPrice.ASC.ordinal());
                        Type = "";
                        brandId = "";
                        capacity = "";
                        type_id = "";
                        sousuoStr = "isHomeSou";
                        if (tvWareListRongliang != null) {
                            tvWareListRongliang.setText("规格");
                        }
                        if (tvWareListPingpai != null) {
                            tvWareListPingpai.setText("品牌");
                        }
                        map = new HashMap<>();
                        map.put("key_words", keyword);
                        map.put("pageSize", "10");
                        map.put("pageIndex", "1");
                        map.put("PriceOder", PriceOder);
                        map.put("Type", Type);
                        map.put("brandId", brandId);
                        map.put("capacity", capacity);
                        netUtil2.okHttp2Server2(WareListActivity.this,home_search_url, map);
                    } else {
                        Toast.makeText(WareListActivity.this, "请输入关键字！", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.rl_exit:
                    InputMethodManager imm2 = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm2.hideSoftInputFromWindow(etWareListContent.getWindowToken(), 0);
                    finish();
                    break;
                case R.id.rl_edit_text_exit:
                    etWareListContent.setText("");
                    break;
            }
        }
    }

    /**
     * @param sort_price 设置查询升降序的列表
     */
    private void setSortPriceWareList(int sort_price) {
        if (sort_price == SortPrice.ASC.ordinal()) {
            PriceOder = String.valueOf(2);
        } else {
            PriceOder = String.valueOf(1);
        }
        page = 1;
        pageIndex = String.valueOf(page);
        searchWareList();

    }

    /**
     * @param
     */
    private void searchWareList() {
        if (type_id != null && !TextUtils.isEmpty(type_id)) {
            etWareListContent.setHint("请输入商品名称");
            map = new HashMap<>();
            map.put("id", type_id);
            map.put("key_words", keyword);
            map.put("pageSize", "10");
            map.put("pageIndex", pageIndex);
            map.put("PriceOder", PriceOder);
            map.put("Type", Type);
            map.put("brandId", brandId);
            map.put("capacity", capacity);
            netUtil2.okHttp2Server2(WareListActivity.this,ware_url, map);
        } else {
            etWareListContent.setHint("请输入商品名称");
            if (sousuoStr.equals("isQiang")) {
                map = new HashMap<>();
                map.put("key_words", keyword);
                map.put("pageSize", "10");
                map.put("pageIndex", pageIndex);
                map.put("PriceOder", PriceOder);
                map.put("Type", Type);
                map.put("id", Type);
                map.put("brandId", brandId);
                map.put("capacity", capacity);
                String qiang_url = Url.BASE_URL + "GroupBuyList.aspx";
                netUtil2.okHttp2Server2(WareListActivity.this,qiang_url, map);
            } else if (sousuoStr.equals("isVip")) {
                map = new HashMap<>();
                map.put("key_words", keyword);
                map.put("pageSize", "10");
                map.put("pageIndex", pageIndex);
                map.put("PriceOder", PriceOder);
                map.put("Type", Type);
                map.put("id", Type);
                map.put("brandId", brandId);
                map.put("capacity", capacity);
                String vip_url = Url.BASE_URL + "ChannelPriceList.aspx";
                netUtil2.okHttp2Server2(WareListActivity.this,vip_url, map);
            } else if (sousuoStr.equals("activity")) {
                map = new HashMap<>();
                map.put("key_words", keyword);
                map.put("pageSize", "10");
                map.put("pageIndex", pageIndex);
                map.put("activity_id", activity_id);
                map.put("PriceOder", PriceOder);
                map.put("Type", Type);
                map.put("brandId", brandId);
                map.put("capacity", capacity);
                netUtil2.okHttp2Server2(WareListActivity.this,activity_url, map);
            } else if (sousuoStr.equals("isTypeSou")) {
                map = new HashMap<>();
                map.put("key_words", keyword);
                map.put("pageSize", "10");
                map.put("pageIndex", pageIndex);
                map.put("PriceOder", PriceOder);
                map.put("Type", Type);
                map.put("brandId", brandId);
                map.put("capacity", capacity);
                netUtil2.okHttp2Server2(WareListActivity.this,sousuo_url, map);
            } else if (sousuoStr.equals("isHomeSou")) {
                map = new HashMap<>();
                map.put("key_words", keyword);
                map.put("pageSize", "10");
                map.put("pageIndex", pageIndex);
                map.put("PriceOder", PriceOder);
                map.put("Type", Type);
                map.put("brandId", brandId);
                map.put("capacity", capacity);
                netUtil2.okHttp2Server2(WareListActivity.this,home_search_url, map);
            }
        }
    }

    /**
     * @param sort_price 设置升降序的图标
     */
    private void setSortPriceIcon(int sort_price) {
        if (sort_price == SortPrice.ASC.ordinal()) {
            ivUpPrice.setImageResource(R.drawable.up_black_arraw);
            ivDownPrice.setImageResource(R.drawable.down_gray_arraw);
        } else {
            ivUpPrice.setImageResource(R.drawable.up_gray_arraw);
            ivDownPrice.setImageResource(R.drawable.down_black_arraw);
        }
    }

    public enum SortPrice {
        ASC,
        DESC;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (typeSelectorPopupwindow != null && typeSelectorPopupwindow.isShowing()) {
            typeSelectorPopupwindow.dismiss();
            typeSelectorPopupwindow = null;
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        if (mIsAnim) {
            return false;
        }
        final int action = ev.getAction();

        float x = ev.getX();
        float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                lastX = x;
                return false;
            case MotionEvent.ACTION_MOVE:
                float dY = Math.abs(y - lastY);
                float dX = Math.abs(x - lastX);
                boolean down = y > lastY ? true : false;
                lastY = y;
                lastX = x;
                if (dX < 8 && dY > 8 && !mIsTitleHide && !down) {
                    Animation anim = AnimationUtils.loadAnimation(
                            WareListActivity.this, R.anim.push_top_in);
                    anim.setFillAfter(true);
                    anim.setInterpolator(new OvershootInterpolator());
                    anim.setAnimationListener(WareListActivity.this);
                    ll_ware_list_sousuo.startAnimation(anim);
                } else if (dX < 8 && dY > 8 && mIsTitleHide && down) {
                    Animation anim = AnimationUtils.loadAnimation(
                            WareListActivity.this, R.anim.push_top_out);
                    //anim.setFillAfter(true);
                    anim.setInterpolator(new OvershootInterpolator());
                    anim.setAnimationListener(WareListActivity.this);
                    ll_ware_list_sousuo.startAnimation(anim);
                } else {
                    return false;
                }
                mIsTitleHide = !mIsTitleHide;
                mIsAnim = true;
                break;
            default:
                return false;
        }
        return false;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        ll_ware_list_sousuo.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (mIsTitleHide) {
            ll_ware_list_sousuo.setVisibility(View.GONE);
        } else {
            ll_ware_list_sousuo.setVisibility(View.VISIBLE);
        }
        mIsAnim = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
