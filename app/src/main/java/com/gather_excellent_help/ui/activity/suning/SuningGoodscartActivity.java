package com.gather_excellent_help.ui.activity.suning;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.suning.SuningGoodscartBean;
import com.gather_excellent_help.bean.suning.SuningStockBean;
import com.gather_excellent_help.bean.suning.SuningWjsonBean;
import com.gather_excellent_help.bean.suning.netcart.NetGoodsDeleteBean;
import com.gather_excellent_help.bean.suning.netcart.NetGoodsUpdateBean;
import com.gather_excellent_help.bean.suning.netcart.NetGoodscartBean;
import com.gather_excellent_help.bean.suning.netcart.NetGoodscartCheckBean;
import com.gather_excellent_help.bean.suning.netcart.NetGoodscartTotalBean;
import com.gather_excellent_help.db.suning.SqliteServiceManager;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.SetActivity;
import com.gather_excellent_help.ui.adapter.SuningGoodscartAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.WanRecycleView;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.ScreenUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.cartutils.NetCartUtil;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class SuningGoodscartActivity extends BaseActivity {

    private TextView tv_top_title_name;
    private RelativeLayout rl_exit;

    private WanRecycleView wan_suning_goodscart;
    private RecyclerView recyclerView;

    private CheckBox cb_shopcart_checkall;
    private TextView tv_shop_total_price;
    private TextView tv_topay;
    private TextView tv_goodscart_price_icon;

    private boolean isCheckAll = false;
    private boolean isTopay = false;
    private boolean edit_status = true;
    private double total_price;

    private String pcs_url = Url.BASE_URL + "suning/GoodsInfo.ashx?action=GetStoreQuantity";//是否有货
    private RelativeLayout rl_zhuangtai_right;
    private TextView tv_zhuangtai_right;
    private LinearLayout ll_goodscast_price_show;
    private SuningGoodscartAdapter suningGoodscartAdapter;
    private LinearLayout ll_goodscast_clear_show;
    private TextView tv_goodscart_clear_toSee;
    private boolean isHavaGoods = true;
    private NetUtil netUtil;
    private Map<String, String> map;
    private String whick = "";
    private String area_id = "";
    private String num_click = "";
    private String addWay = "";
    private String lalotitude = "";
    private int num_value;

    private NetCartUtil netCartUtil;
    private String userLogin;

    private List<NetGoodscartBean.DataBean> netData;
    private NetGoodscartBean.DataBean dataBean;
    private List<NetGoodscartCheckBean.DataBean> checkData;
    private NetGoodscartCheckBean netGoodscartCheckBean;
    private List<NetGoodscartBean.DataBean.GoodsListBean> goods_list;
    private List<NetGoodscartBean.DataBean.GgListBean> gg_list;
    private NetGoodscartBean.DataBean.GoodsListBean goodsListBean;
    private NetGoodscartBean.DataBean.GgListBean ggListBean;
    private String t_spec_text = "";
    private String t_goods_title = "";
    private String t_img_url = "";
    private double t_market_price;
    private double t_sell_price;
    private String t_productId = "";


    private FullyLinearLayoutManager fullyLinearLayoutManager;
    private AlertDialog alertDialog;

    private boolean isEvent;
    private boolean isCheckState = false;
    private boolean isInitState = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    break;
            }
        }
    };


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
        tv_goodscart_price_icon = (TextView) findViewById(R.id.tv_goodscart_price_icon);
        tv_topay = (TextView) findViewById(R.id.tv_topay);

        rl_zhuangtai_right = (RelativeLayout) findViewById(R.id.rl_zhuangtai_right);
        tv_zhuangtai_right = (TextView) findViewById(R.id.tv_zhuangtai_right);

        ll_goodscast_price_show = (LinearLayout) findViewById(R.id.ll_goodscast_price_show);

        ll_goodscast_clear_show = (LinearLayout) findViewById(R.id.ll_goodscast_clear_show);
        tv_goodscart_clear_toSee = (TextView) findViewById(R.id.tv_goodscart_clear_toSee);
    }

    /**
     * 初始哈数据
     */
    private void initData() {
        userLogin = Tools.getUserLogin(this);
        netUtil = new NetUtil();
        netCartUtil = new NetCartUtil();
        OnCartResponseListener onCartResponseListener = new OnCartResponseListener();
        netCartUtil.setOnCartResponseListener(onCartResponseListener);
        tv_top_title_name.setText("购物车");
        Intent intent = getIntent();
        area_id = intent.getStringExtra("area_id");
        addWay = intent.getStringExtra("addWay");
        lalotitude = intent.getStringExtra("lalotitude");
        if (!TextUtils.isEmpty(lalotitude)) {
            addWay = "1";
        } else {
            addWay = "2";
        }
        setTopEditShow();
        recyclerView = wan_suning_goodscart.getRefreshableView();
        fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        if (wan_suning_goodscart != null && wan_suning_goodscart.isRefreshing()) {
            wan_suning_goodscart.onRefreshComplete();
        }

        //设置刷新相关
        wan_suning_goodscart.setScrollingWhileRefreshingEnabled(true);
        wan_suning_goodscart.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        wan_suning_goodscart.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                Tools.saveCartCheck(SuningGoodscartActivity.this, "");
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }

        });

        //获取线上购物车的数据
        getNetGoodscartData();
        OnServerResponseListener onServerResponseListener = new OnServerResponseListener();
        netUtil.setOnServerResponseListener(onServerResponseListener);
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        cb_shopcart_checkall.setOnClickListener(myonclickListener);
        rl_zhuangtai_right.setOnClickListener(myonclickListener);
        tv_topay.setOnClickListener(myonclickListener);
        tv_goodscart_clear_toSee.setOnClickListener(myonclickListener);
    }

    /**
     * 显示catView
     */
    private void showCatView() {
        View inflate = View.inflate(this, R.layout.loading_dialog_view, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflate);
        alertDialog = builder.create();
        if (SuningGoodscartActivity.this != null && !SuningGoodscartActivity.this.isFinishing()) {
            alertDialog.show();
        }
        alertDialog.getWindow().setLayout(ScreenUtil.getScreenWidth(this) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 隐藏catView
     */
    private void hindCatView() {
        if (SuningGoodscartActivity.this != null && !SuningGoodscartActivity.this.isFinishing()) {
            if (alertDialog != null && alertDialog.isShowing()) {
                View view = new View(SuningGoodscartActivity.this);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                    }
                }, 1000);
            }
        }
    }

    /**
     * 获取线上服务器上的购物车数据
     */
    private void getNetGoodscartData() {
        netCartUtil.getCartList(userLogin);
    }

    /**
     * 判断库存
     */
    private void checkIsHave(String addrWay, String addstr, String productId, String num) {
        showCatView();
        whick = "checkIsHave";
        map = new HashMap<>();
        map.put("addrstr", addstr);
        map.put("addrWay", addrWay);
        map.put("ProductId", productId);
        map.put("lnglat", lalotitude);
        map.put("num", num);
        netUtil.okHttp2Server2(pcs_url, map);
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
     * 全选和非全选
     */
    public void checkAll_none() {
        if (checkData != null && checkData.size() > 0) {
            int number = 0;
            for (int i = 0; i < checkData.size(); i++) {
                NetGoodscartCheckBean.DataBean checkBean = checkData.get(i);
                boolean bool = Tools.getInt2Boolean(String.valueOf(checkBean.getIs_check()));
                if (!bool) {//只要有一个不被选中，就设置全选为非勾选状态
                    //没选择的
                    isCheckAll = false;
                    total_price = 0;
                } else {
                    //选中的
                    number += 1;
                }
            }
            if (number == netData.size()) {
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
        if (checkData != null && checkData.size() > 0) {
            int num = 0;
            for (int i = 0; i < checkData.size(); i++) {
                NetGoodscartCheckBean.DataBean checkBean = checkData.get(i);
                boolean bool = Tools.getInt2Boolean(String.valueOf(checkBean.getIs_check()));
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

    public void getLocalTotalPrice() {
        total_price = 0;
        ArrayList<Integer> check_carts = new ArrayList<>();
        if (checkData != null && checkData.size() > 0) {
            for (int i = 0; i < checkData.size(); i++) {
                NetGoodscartCheckBean.DataBean checkBean = checkData.get(i);
                boolean bool = Tools.getInt2Boolean(String.valueOf(checkBean.getIs_check()));
                if (bool) {
                    int cart_id = checkBean.getCart_id();
                    check_carts.add(cart_id);
                }
            }
            if (check_carts != null && check_carts.size() > 0) {
                if (netData != null && netData.size() > 0) {
                    for (int i = 0; i < netData.size(); i++) {
                        NetGoodscartBean.DataBean dataBean = netData.get(i);
                        int cart_id = dataBean.getCart_id();
                        if (check_carts.contains(cart_id)) {
                            int quantity = dataBean.getQuantity();
                            List<NetGoodscartBean.DataBean.GoodsListBean> goods_list = dataBean.getGoods_list();
                            if (goods_list != null && goods_list.size() > 0) {
                                NetGoodscartBean.DataBean.GoodsListBean goodsListBean = goods_list.get(0);
                                if (goodsListBean != null) {
                                    double sell_price = goodsListBean.getSell_price();
                                    total_price += sell_price * quantity;
                                }
                            }
                        }
                    }
                }
            }
        }
        setCheckStatus();
    }

    /**
     * 获取总计价格
     */
    public void getTotalPrice() {
        total_price = 0;
        ArrayList<Integer> check_carts = new ArrayList<>();
        if (checkData != null && checkData.size() > 0) {
            for (int i = 0; i < checkData.size(); i++) {
                NetGoodscartCheckBean.DataBean checkBean = checkData.get(i);
                boolean bool = Tools.getInt2Boolean(String.valueOf(checkBean.getIs_check()));
                if (bool) {
                    int cart_id = checkBean.getCart_id();
                    check_carts.add(cart_id);
                }
            }
            if (check_carts != null && check_carts.size() > 0) {
                if (netData != null && netData.size() > 0) {
                    ArrayList<SuningWjsonBean> suningWjsonLists = new ArrayList<>();
                    for (int i = 0; i < netData.size(); i++) {
                        NetGoodscartBean.DataBean dataBean = netData.get(i);
                        int cart_id = dataBean.getCart_id();
                        if (check_carts.contains(cart_id)) {
                            int channel_id = dataBean.getChannel_id();
                            int goods_id = dataBean.getGoods_id();
                            int article_id = dataBean.getArticle_id();
                            int quantity = dataBean.getQuantity();
                            SuningWjsonBean suningWjsonBean = new SuningWjsonBean();
                            suningWjsonBean.setArticle_id(String.valueOf(article_id));
                            suningWjsonBean.setChannel_id(String.valueOf(channel_id));
                            suningWjsonBean.setGoods_id(String.valueOf(goods_id));
                            suningWjsonBean.setQuantity(String.valueOf(quantity));
                            suningWjsonLists.add(suningWjsonBean);
                        }
                    }
                    String json = new Gson().toJson(suningWjsonLists);
                    LogUtil.e("---total price = " + json);
                    netCartUtil.getTotalPrice(json);
                } else {
                    total_price = 0;
                }
            } else {
                total_price = 0;
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
                    int check = 0;
                    if (isCheckAll) {
                        check = 1;
                    } else {
                        check = 0;
                    }
                    if (checkData != null && checkData.size() > 0) {
                        for (int i = 0; i < checkData.size(); i++) {
                            NetGoodscartCheckBean.DataBean checkBean = checkData.get(i);
                            checkBean.setIs_check(check);
                        }
                    }
                    String result = new Gson().toJson(netGoodscartCheckBean);
                    Tools.saveCartCheck(SuningGoodscartActivity.this, result);
                    showCatView();
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
                        ArrayList<Integer> check_carts = new ArrayList<>();
                        if (checkData != null && checkData.size() > 0) {
                            for (int i = 0; i < checkData.size(); i++) {
                                NetGoodscartCheckBean.DataBean checkBean = checkData.get(i);
                                boolean bool = Tools.getInt2Boolean(String.valueOf(checkBean.getIs_check()));
                                if (bool) {
                                    int cart_id = checkBean.getCart_id();
                                    check_carts.add(cart_id);
                                }
                            }
                            if (check_carts != null && check_carts.size() > 0) {
                                if (netData != null && netData.size() > 0) {
                                    SuningGoodscartBean suningGoodscartBean = new SuningGoodscartBean();
                                    List<SuningGoodscartBean.DataBean> suningCartdata = new ArrayList<>();
                                    for (int i = 0; i < netData.size(); i++) {
                                        NetGoodscartBean.DataBean dataBean = netData.get(i);
                                        int cart_id = dataBean.getCart_id();
                                        if (check_carts.contains(cart_id)) {
                                            DecimalFormat df = new DecimalFormat("#0.00");
                                            SuningGoodscartBean.DataBean ndatabean = new SuningGoodscartBean.DataBean();
                                            int channel_id = dataBean.getChannel_id();
                                            int goods_id = dataBean.getGoods_id();
                                            int article_id = dataBean.getArticle_id();
                                            int quantity = dataBean.getQuantity();
                                            int purchase_num = dataBean.getPurchase_num();
                                            goods_list = dataBean.getGoods_list();
                                            gg_list = dataBean.getGg_list();
                                            if (goods_list != null && goods_list.size() > 0) {
                                                goodsListBean = goods_list.get(0);
                                                t_goods_title = goodsListBean.getGoods_title();
                                                t_img_url = goodsListBean.getImg_url();
                                                t_market_price = goodsListBean.getMarket_price();
                                                t_sell_price = goodsListBean.getSell_price();
                                                t_productId = goodsListBean.getProductId();
                                            }
                                            if (gg_list != null && gg_list.size() > 0) {
                                                ggListBean = gg_list.get(0);
                                                t_spec_text = ggListBean.getSpec_text();
                                            }
                                            ndatabean.setProduct_spec_limit(String.valueOf(purchase_num));
                                            ndatabean.setProduct_spec(t_spec_text);
                                            ndatabean.setProduct_sprice(df.format(t_sell_price));
                                            ndatabean.setProduct_mprice(df.format(t_market_price));
                                            ndatabean.setProduct_title(t_goods_title);
                                            ndatabean.setProduct_spec_id(String.valueOf(goods_id));
                                            ndatabean.setProduct_goodsid(t_productId);
                                            ndatabean.setProduct_id(String.valueOf(article_id));
                                            ndatabean.setProduct_num(String.valueOf(quantity));
                                            ndatabean.setProduct_pic(t_img_url);
                                            suningCartdata.add(ndatabean);
                                        }
                                    }
                                    suningGoodscartBean.setData(suningCartdata);
                                    String cart_json = new Gson().toJson(suningGoodscartBean);
                                    if (isHavaGoods) {
                                        Intent intent = new Intent(SuningGoodscartActivity.this, OrderCartConfirmActivity.class);
                                        intent.putExtra("cart_json", cart_json);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SuningGoodscartActivity.this, "网络连接出现问题，请您重新加入购物车。", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                    } else {
                        //删除购物车数据
                        deletNetGoodscartData();
                    }
                    break;
                case R.id.tv_goodscart_clear_toSee:
                    finish();
                    EventBus.getDefault().post(new AnyEvent(EventType.GOODSCART_CLEAR, "购物车空空如也"));
                    break;
            }
        }
    }

    /**
     * 删除线上购物车数据
     */
    private void deletNetGoodscartData() {
        String cartCheck = Tools.getCartCheck(SuningGoodscartActivity.this);
        if (cartCheck != null && !TextUtils.isEmpty(cartCheck)) {
            netGoodscartCheckBean = new Gson().fromJson(cartCheck, NetGoodscartCheckBean.class);
            checkData = netGoodscartCheckBean.getData();
        }
        if (checkData != null && checkData.size() > 0) {
            String cart_ids = "";
            for (int i = 0; i < checkData.size(); i++) {
                NetGoodscartCheckBean.DataBean checkBean = checkData.get(i);
                int cart_id = checkBean.getCart_id();
                int is_check = checkBean.getIs_check();
                if (is_check == 1) {
//                    int index = checkData.indexOf(checkBean);
//                    checkData.remove(index);
                    cart_ids += cart_id + ",";
                }
            }
            if (!TextUtils.isEmpty(cart_ids)) {
                String cart_id = cart_ids.substring(0, cart_ids.length() - 1);
                LogUtil.e("cart_id = " + cart_id);
                netCartUtil.deleteCart("0", userLogin, cart_id);
            }

        }

    }

    /**
     * 解析判断库存数据
     *
     * @param response
     */
    private void parseCheckIshavaData(String response) {
        LogUtil.e(response);
        isHavaGoods = true;
        SuningStockBean suningStockBean = new Gson().fromJson(response, SuningStockBean.class);
        int statusCode = suningStockBean.getStatusCode();
        switch (statusCode) {
            case 1:
                int cart_id = dataBean.getCart_id();
                int article_id = dataBean.getArticle_id();
                int goods_id = dataBean.getGoods_id();
                netCartUtil.updateCart(String.valueOf(cart_id), userLogin, "7", String.valueOf(article_id), String.valueOf(goods_id),
                        String.valueOf(num_value));
                break;
            case 0:
                Toast.makeText(SuningGoodscartActivity.this, "当前购买数量库存不足！！！", Toast.LENGTH_SHORT).show();
                break;
        }
        initData();
    }

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            if (whick.equals("checkIsHave")) {
                parseCheckIshavaData(response);
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e("网络连接出现问题~" + call.toString() + "-" + e.getMessage());
            isHavaGoods = false;
            initData();
        }
    }

    public class OnCartResponseListener implements NetCartUtil.OnCartResponseListener {

        @Override
        public void onCartResponse(String response, String whick) {
            hindCatView();
            if (whick.equals(NetCartUtil.WHICH_DEL)) {
                parseNetGoodsDelData(response);
            } else if (whick.equals(NetCartUtil.WHICH_UPD)) {
                parseUpdateCartData(response);
            } else if (whick.equals(NetCartUtil.WHICH_GET)) {
                if (!isInitState) {
                    isInitState = true;
                    showCatView();
                    getNetGoodscartData();
                } else {
                    parseNetGoodscartData(response);
                }
            } else if (whick.equals(NetCartUtil.WHICH_TOTAL)) {
                parseTotalPriceData(response);
            }
        }

        @Override
        public void onCartFail() {

        }
    }

    /**
     * 解析删除的数据
     *
     * @param response
     */
    private void parseNetGoodsDelData(String response) {
        NetGoodsDeleteBean netGoodsDeleteBean = new Gson().fromJson(response, NetGoodsDeleteBean.class);
        int statusCode = netGoodsDeleteBean.getStatusCode();
        switch (statusCode) {
            case 1:
                if (isEvent) {
                    Tools.saveCartCheck(this, "");
                    finish();
                } else {
                    String result = new Gson().toJson(netGoodscartCheckBean);
                    Tools.saveCartCheck(SuningGoodscartActivity.this, result);
                }
                break;
            case 0:
                Toast.makeText(SuningGoodscartActivity.this, netGoodsDeleteBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
        isCheckState = false;
        initData();
    }

    /**
     * 解析更新的数据
     *
     * @param response
     */
    private void parseUpdateCartData(String response) {
        try {
            LogUtil.e("SuningGoodscartActivity = " + response);
            NetGoodsUpdateBean netGoodsUpdateBean = new Gson().fromJson(response, NetGoodsUpdateBean.class);
            int statusCode = netGoodsUpdateBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    break;
                case 0:
                    Toast.makeText(SuningGoodscartActivity.this, netGoodsUpdateBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
            initData();
        } catch (Exception e) {
            LogUtil.e("SuningGoodscartActivity parseUpdateCartData error" + e.getMessage());
            Toast.makeText(SuningGoodscartActivity.this, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析总计价格
     *
     * @param response
     */
    private void parseTotalPriceData(String response) {
        NetGoodscartTotalBean netGoodscartTotalBean = new Gson().fromJson(response, NetGoodscartTotalBean.class);
        int statusCode = netGoodscartTotalBean.getStatusCode();
        switch (statusCode) {
            case 1:
                List<NetGoodscartTotalBean.DataBean> data = netGoodscartTotalBean.getData();
                if (data != null && data.size() > 0) {
                    NetGoodscartTotalBean.DataBean dataBean = data.get(0);
                    double total = dataBean.getTotal();
                    total_price = total;
                }
                setCheckStatus();
                break;
            case 0:
                Toast.makeText(SuningGoodscartActivity.this, netGoodscartTotalBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                total_price = 0;
                setCheckStatus();
                break;
        }
    }

    /**
     * 解析线上购物车的数据
     *
     * @param response
     */
    private void parseNetGoodscartData(String response) {
        try {
            NetGoodscartBean netGoodscartBean = new Gson().fromJson(response, NetGoodscartBean.class);
            int statusCode = netGoodscartBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    netData = netGoodscartBean.getData();
                    if (!isCheckState) {
                        Tools.saveCartCheck(SuningGoodscartActivity.this, "");
                    }
                    String cartCheck = Tools.getCartCheck(SuningGoodscartActivity.this);
                    if (cartCheck != null && !TextUtils.isEmpty(cartCheck)) {
                        netGoodscartCheckBean = new Gson().fromJson(cartCheck, NetGoodscartCheckBean.class);
                        checkData = netGoodscartCheckBean.getData();
                    } else {
                        getCheckData(netData);
                    }
                    if (netData != null && netData.size() > 0) {
                        suningGoodscartAdapter = new SuningGoodscartAdapter(this, netData, checkData);
                        recyclerView.setAdapter(suningGoodscartAdapter);
                        suningGoodscartAdapter.setOnUpdatePriceListener(new SuningGoodscartAdapter.OnUpdatePriceListener() {
                            @Override
                            public void onUpdateData(int whick) {
                                if (whick == 1) {
                                    isCheckState = false;
                                }
                                showCatView();
                                initData();
                            }
                        });
                        ll_goodscast_clear_show.setVisibility(View.GONE);
                    } else {
                        if (recyclerView != null) {
                            netData = new ArrayList<>();
                            checkData = new ArrayList<>();
                            suningGoodscartAdapter = new SuningGoodscartAdapter(this, netData, checkData);
                            recyclerView.setAdapter(suningGoodscartAdapter);
                        }
                        isCheckAll = false;
                        isTopay = false;
                        total_price = 0;
                        setCheckStatus();
                        showTopay();
                        ll_goodscast_clear_show.setVisibility(View.VISIBLE);
                    }
                    checkAll_none();
                    checkOnly_check();
                    getLocalTotalPrice();
                    setCheckStatus();
                    numberAddSubComplete();
                    buyNumChanage();
                    clickItemComplete();
                    hindCatView();
                    break;
                case 0:
                    Toast.makeText(SuningGoodscartActivity.this, netGoodscartBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception e) {
            LogUtil.e("SuningGoodscartActivity parseNetGoodscartData error");
            Toast.makeText(SuningGoodscartActivity.this, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 立即购买后购物车的变化
     */
    private void buyNumChanage() {
        suningGoodscartAdapter.setOnBuyLimitNumListener(new SuningGoodscartAdapter.OnBuyLimitNumListener() {
            @Override
            public void onChangeNum(int position, int value) {
                dataBean = netData.get(position);
                if (dataBean != null) {
                    List<NetGoodscartBean.DataBean.GoodsListBean> goods_list = dataBean.getGoods_list();
                    if (goods_list != null && goods_list.size() > 0) {
                        NetGoodscartBean.DataBean.GoodsListBean goodsListBean = goods_list.get(0);
                        String product_id = goodsListBean.getProductId();
                        num_click = "add";
                        num_value = value;
                        checkIsHave(addWay, area_id, product_id, String.valueOf(value));
                    }
                }
            }
        });
    }

    /**
     * 点击item的实现
     */
    private void clickItemComplete() {
        suningGoodscartAdapter.setOnItemClickListener(new SuningGoodscartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                try {
                    NetGoodscartBean.DataBean dataBean = netData.get(position);
                    DecimalFormat df = new DecimalFormat("#0.00");
                    int article_id = dataBean.getArticle_id();
                    List<NetGoodscartBean.DataBean.GoodsListBean> goods_list = dataBean.getGoods_list();
                    NetGoodscartBean.DataBean.GoodsListBean goodsListBean = goods_list.get(0);
                    String goods_title = goodsListBean.getGoods_title();
                    String img_url = goodsListBean.getImg_url();
                    String productId = goodsListBean.getProductId();
                    double market_price = goodsListBean.getMarket_price();
                    double sell_price = goodsListBean.getSell_price();
                    Intent intent = new Intent(SuningGoodscartActivity.this, SuningDetailActivity.class);
                    intent.putExtra("article_id", article_id);
                    intent.putExtra("goods_id", productId);
                    intent.putExtra("goods_img", img_url);
                    intent.putExtra("goods_title", goods_title);
                    intent.putExtra("goods_price", df.format(sell_price) + "");
                    intent.putExtra("c_price", df.format(market_price) + "");
                    startActivity(intent);
                } catch (Exception e) {
                    LogUtil.e("SuningGoodscartActivity error");
                    Toast.makeText(SuningGoodscartActivity.this, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 数字加减器的实现
     */
    private void numberAddSubComplete() {
        suningGoodscartAdapter.setOnNumberAddSubListener(new SuningGoodscartAdapter.OnNumberAddSubListener() {
            @Override
            public void onAddClick(View v, int position, int value) {
//                dataBean = netData.get(position);
//                if (dataBean != null) {
//                    List<NetGoodscartBean.DataBean.GoodsListBean> goods_list = dataBean.getGoods_list();
//                    if (goods_list != null && goods_list.size() > 0) {
//                        NetGoodscartBean.DataBean.GoodsListBean goodsListBean = goods_list.get(0);
//                        String product_id = goodsListBean.getProductId();
//                        //num_click = "add";
//                        num_value = value;
//                        checkIsHave(addWay, area_id, product_id, String.valueOf(value));
//                    }
//                }
                isTopay = false;
                showTopay();
            }

            @Override
            public void onSubClick(View v, int position, int value) {
//                LogUtil.e("value --------" + value);
//                dataBean = netData.get(position);
//                if (dataBean != null) {
//                    List<NetGoodscartBean.DataBean.GoodsListBean> goods_list = dataBean.getGoods_list();
//                    if (goods_list != null && goods_list.size() > 0) {
//                        NetGoodscartBean.DataBean.GoodsListBean goodsListBean = goods_list.get(0);
//                        String product_id = goodsListBean.getProductId();
//                        //num_click = "sub";
//                        num_value = value;
//                        checkIsHave(addWay, area_id, product_id, String.valueOf(value));
//                    }
//                }
                isTopay = false;
                showTopay();
            }

            @Override
            public void onChangeNavnumClick(View v, int position, int value) {
                isTopay = true;
                tv_goodscart_price_icon.setVisibility(View.VISIBLE);
                showTopay();
                dataBean = netData.get(position);
                if (dataBean != null) {
                    List<NetGoodscartBean.DataBean.GoodsListBean> goods_list = dataBean.getGoods_list();
                    if (goods_list != null && goods_list.size() > 0) {
                        NetGoodscartBean.DataBean.GoodsListBean goodsListBean = goods_list.get(0);
                        String product_id = goodsListBean.getProductId();
                        //num_click = "sub";
                        num_value = value;
                        checkIsHave(addWay, area_id, product_id, String.valueOf(value));
                    }
                }
            }
        });
    }

    private void getCheckData(List<NetGoodscartBean.DataBean> netData) {
        isCheckState = true;
        if (netData != null && netData.size() > 0) {
            netGoodscartCheckBean = new NetGoodscartCheckBean();
            checkData = new ArrayList<>();
            for (int i = 0; i < netData.size(); i++) {
                NetGoodscartCheckBean.DataBean checkBean = new NetGoodscartCheckBean.DataBean();
                NetGoodscartBean.DataBean dataBean = netData.get(i);
                int cart_id = dataBean.getCart_id();
                checkBean.setCart_id(cart_id);
                checkData.add(checkBean);
            }
            netGoodscartCheckBean.setData(checkData);
            String checkresult = new Gson().toJson(netGoodscartCheckBean);
            Tools.saveCartCheck(SuningGoodscartActivity.this, checkresult);
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
            isEvent = true;
            LogUtil.e(msg);
            deletNetGoodscartData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Tools.saveCartCheck(this, "");
        EventBus.getDefault().unregister(this);
    }

}
