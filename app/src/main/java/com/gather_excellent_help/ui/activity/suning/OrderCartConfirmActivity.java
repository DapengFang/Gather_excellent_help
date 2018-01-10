package com.gather_excellent_help.ui.activity.suning;

import android.app.AlertDialog;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.AddressDetailBean;
import com.gather_excellent_help.bean.suning.SuningCreateBean;
import com.gather_excellent_help.bean.suning.SuningFreeBean;
import com.gather_excellent_help.bean.suning.SuningGoodscartBean;
import com.gather_excellent_help.bean.suning.SuningStockBean;
import com.gather_excellent_help.bean.suning.SuningWjsonBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.WebRecordActivity;
import com.gather_excellent_help.ui.activity.address.PersonAddressActivity;
import com.gather_excellent_help.ui.adapter.SuningOrdercartAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;

import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.ScreenUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;


public class OrderCartConfirmActivity extends BaseActivity {

    private static final int CHOICE_INVOICE_INFO = 0x123;
    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;
    private TextView tv_order_create_confirm;
    //地址相关控件
    private TextView tv_sunng_add_newaddress;
    private RelativeLayout rl_suning_default_address;
    private TextView tv_suning_address_name;
    private TextView tv_suning_address_phone;
    private TextView tv_suning_address_details;

    private TextView tv_confirm_postage;

    //备注
    private EditText et_suning_order_mark;
    //发票
    private RelativeLayout rl_suning_order_invoice_show;
    private TextView tv_suning_order_invoice_title;

    //商品付款金额相关
    private TextView tv_confirm_goods_price;
    private TextView tv_confirm_order_totalprice;

    private RecyclerView rcv_cart_order_confirm;

    //private RelativeLayout rl_net_show;

    private SwipeRefreshLayout swip_order_cart_refresh;

    private String free_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=GetCartSerfree";//运费接口
    private String pushorder_url = Url.BASE_URL + "suning/AddSNOrder.aspx";//提交订单接口
    private String address_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=GetUserAddress";//地址列表接口
    private String pcs_url = Url.BASE_URL + "suning/GoodsInfo.ashx?action=GetStoreQuantity";//是否有货
    private String whick = "";
    private Map<String, String> map;
    private NetUtil netUtil;

    private String addr_id = "";//地址id
    private String area_id = "";//省市区id
    private String address = "";//地址
    private String remark = "";//备注
    private String invoiceState = "0";//是否开发票
    private String invoiceTitle = "";//发票抬头
    private String taxNo = "";//纳税人识别号
    private String json = "";//商品信息json串
    private List<AddressDetailBean.DataBean> data;
    private String userLogin;
    private String ware_num = "1";//商品数量服务器端
    private String goods_id = "";//商品id
    private String channel_id = "7";//产品参数
    private String spec_back_id = "";//一组产品
    private List<SuningGoodscartBean.DataBean> cartData;
    private SuningOrdercartAdapter suningOrdercartAdapter;

    private String totalprice = "";//总价
    private SuningGoodscartBean.DataBean dataBean;
    private boolean isHavaGoods = true;
    private String cart_ids;//所有的cart_id的拼接

    private boolean mIsRequestDataRefresh;
    private AlertDialog alertDialog;
    private double freightFare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_cart_confirm);
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
        tv_order_create_confirm = (TextView) findViewById(R.id.tv_order_create_confirm);

        //地址
        tv_sunng_add_newaddress = (TextView) findViewById(R.id.tv_sunng_add_newaddress);
        rl_suning_default_address = (RelativeLayout) findViewById(R.id.rl_suning_default_address);
        tv_suning_address_name = (TextView) findViewById(R.id.tv_suning_address_name);
        tv_suning_address_phone = (TextView) findViewById(R.id.tv_suning_address_phone);
        tv_suning_address_details = (TextView) findViewById(R.id.tv_suning_address_details);

        //备注和发票
        et_suning_order_mark = (EditText) findViewById(R.id.et_suning_order_mark);
        rl_suning_order_invoice_show = (RelativeLayout) findViewById(R.id.rl_suning_order_invoice_show);
        tv_suning_order_invoice_title = (TextView) findViewById(R.id.tv_suning_order_invoice_title);

        //运费
        tv_confirm_postage = (TextView) findViewById(R.id.tv_confirm_postage);

        //商品付款金额信息
        tv_confirm_goods_price = (TextView) findViewById(R.id.tv_confirm_goods_price);
        tv_confirm_order_totalprice = (TextView) findViewById(R.id.tv_confirm_order_totalprice);


        //商品信息
        rcv_cart_order_confirm = (RecyclerView) findViewById(R.id.rcv_cart_order_confirm);


        swip_order_cart_refresh = (SwipeRefreshLayout) findViewById(R.id.swip_order_cart_refresh);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        userLogin = Tools.getUserLogin(this);
        tv_top_title_name.setText("确认订单");
        tv_suning_order_invoice_title.setText("不开发票");

        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        rcv_cart_order_confirm.setLayoutManager(layoutManager);
        getCartData();
        if (cartData == null) {
            cartData = new ArrayList<>();
        }
        suningOrdercartAdapter = new SuningOrdercartAdapter(this, cartData);
        rcv_cart_order_confirm.setAdapter(suningOrdercartAdapter);

        //网络连接初始化
        netUtil = new NetUtil();
        OnServerResponseListener onServerResponseListener = new OnServerResponseListener();
        netUtil.setOnServerResponseListener(onServerResponseListener);
        getAddressDefault();
        //页面监听初始化
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        rl_suning_order_invoice_show.setOnClickListener(myonclickListener);
        tv_order_create_confirm.setOnClickListener(myonclickListener);
        tv_sunng_add_newaddress.setOnClickListener(myonclickListener);
        rl_suning_default_address.setOnClickListener(myonclickListener);
        rl_suning_order_invoice_show.setOnClickListener(myonclickListener);
        et_suning_order_mark.setOnClickListener(myonclickListener);
//        suningOrdercartAdapter.setOnNumButtonListener(new SuningOrdercartAdapter.OnNumButtonListener() {
//            @Override
//            public void onAddClick(View v, int position, String product_id, String num,String price) {
//                totalprice =price;
//                showTotalPrice();
//            }
//
//            @Override
//            public void onSubClick(View v, int position, String product_id, String num,String price) {
//                totalprice = price;
//                showTotalPrice();
//            }
//        });
        suningOrdercartAdapter.setOnNumAddSubListener(new SuningOrdercartAdapter.OnNumAddSubListener() {
            @Override
            public void onAddClick(View v, int position, int value) {
                dataBean = cartData.get(position);
                String product_id = dataBean.getProduct_id();
                checkIsHave("2", area_id, product_id, String.valueOf(value));
            }

            @Override
            public void onSubClick(View v, int position, int value) {
                dataBean = cartData.get(position);
                String product_id = dataBean.getProduct_id();
                checkIsHave("2", area_id, product_id, String.valueOf(value));
            }
        });
        setupSwipeRefresh(swip_order_cart_refresh);
    }


    /**
     * 显示CatView
     */
    private void showCatView() {
        View inflate = View.inflate(this, R.layout.loading_dialog_view, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflate);
        alertDialog = builder.create();
        if (OrderCartConfirmActivity.this != null && !OrderCartConfirmActivity.this.isFinishing()) {
            alertDialog.show();
        }
        alertDialog.getWindow().setLayout(ScreenUtil.getScreenWidth(this) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 隐藏CatView
     */
    private void hindCatView() {
        if (OrderCartConfirmActivity.this != null && !OrderCartConfirmActivity.this.isFinishing()) {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
    }

    /**
     * 设置刷新
     *
     * @param view
     */
    private void setupSwipeRefresh(View view) {
        if (swip_order_cart_refresh != null) {
            swip_order_cart_refresh.setColorSchemeResources(R.color.colorFirst,
                    R.color.colorSecond, R.color.colorThird);
            swip_order_cart_refresh.setProgressViewOffset(true, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
            swip_order_cart_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    mIsRequestDataRefresh = true;
                    setRefresh(mIsRequestDataRefresh);
                    getAddressDefault();
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
            if (swip_order_cart_refresh != null) {
                swip_order_cart_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swip_order_cart_refresh != null) {
                            swip_order_cart_refresh.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        } else {
            if (swip_order_cart_refresh != null) {
                swip_order_cart_refresh.setRefreshing(true);
            }
        }
    }

    /**
     * 获取运费
     */
    public void getFreeData() {
        whick = "free";
        map = new HashMap<>();
        map.put("user_id", userLogin);
        map.put("area_id", area_id);
        map.put("address", address);
        map.put("cart_ids", cart_ids);
        netUtil.okHttp2Server2(free_url, map);
    }

    /**
     * 获取购物车数据
     */
    private void getCartData() {
        Intent intent = getIntent();
        String cart_json = intent.getStringExtra("cart_json");
        cart_ids = intent.getStringExtra("cart_ids");
        if (cart_json != null && !TextUtils.isEmpty(cart_json)) {
            SuningGoodscartBean suningGoodscartBean = new Gson().fromJson(cart_json, SuningGoodscartBean.class);
            cartData = suningGoodscartBean.getData();
        }
    }

    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_order_create_confirm:
                    tv_order_create_confirm.setClickable(false);
                    tv_order_create_confirm.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pushBuyOrder();
                        }
                    }, 1000);
                    break;
                case R.id.et_suning_order_mark:
                    et_suning_order_mark.setCursorVisible(true);
                    break;
                case R.id.tv_sunng_add_newaddress:
                    toPersonAdderssManager();
                    break;
                case R.id.rl_suning_default_address:
                    toPersonAdderssManager();
                    break;
                case R.id.rl_suning_order_invoice_show:
                    toInvoiceManager();
                    break;
            }
        }
    }

    /**
     * 跳转到发票信息管理
     */
    private void toInvoiceManager() {
        Intent intent = new Intent(this, InvoiceChoiceActivity.class);
        intent.putExtra("invoice_type", invoiceState);
        intent.putExtra("invoice_title", invoiceTitle);
        intent.putExtra("invoice_taxno", taxNo);
        startActivityForResult(intent, CHOICE_INVOICE_INFO);
    }

    /**
     * 跳转到收货地址管理
     */
    private void toPersonAdderssManager() {
        Intent intent = new Intent(OrderCartConfirmActivity.this, PersonAddressActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到收银台
     */
    private void toCheckStand(double pay_price, String order_num, int orderId) {
        EventBus.getDefault().post(new AnyEvent(EventType.GOODS_PAY_LIMIT, "更新限购数量！"));
        LogUtil.e(pay_price + "--" + order_num);
        EventBus.getDefault().post(new AnyEvent(EventType.CLEAR_ALL_GOODSCART, "清空购物车"));
        Intent intent = new Intent(OrderCartConfirmActivity.this, CheckStandActivity.class);
        intent.putExtra("pay_price", pay_price);
        intent.putExtra("order_num", order_num);
        intent.putExtra("orderId", orderId);
        startActivity(intent);
        finish();
    }

    /**
     * 获取默认地址信息
     */
    private void getAddressDefault() {
        whick = "getaddress";
        map = new HashMap<>();
        map.put("user_id", userLogin);
        netUtil.okHttp2Server2(address_url, map);
    }

    /**
     * 提交购买订单
     */
    private void pushBuyOrder() {
        remark = et_suning_order_mark.getText().toString().trim();
        if (TextUtils.isEmpty(addr_id)) {
            tv_order_create_confirm.setClickable(true);
            Toast.makeText(OrderCartConfirmActivity.this, "请选择收货地址！！！", Toast.LENGTH_SHORT).show();
            return;
        }
        showCatView();
        ArrayList<SuningWjsonBean> suningWjsonLists = new ArrayList<>();
        if (cartData != null && cartData.size() > 0) {
            for (int i = 0; i < cartData.size(); i++) {
                SuningGoodscartBean.DataBean dataBean = cartData.get(i);
                goods_id = dataBean.getProduct_id();
                spec_back_id = dataBean.getProduct_spec_id();
                ware_num = dataBean.getProduct_num();
                SuningWjsonBean suningWjsonBean = new SuningWjsonBean();
                suningWjsonBean.setArticle_id(goods_id);
                suningWjsonBean.setChannel_id(channel_id);
                suningWjsonBean.setGoods_id(spec_back_id);
                suningWjsonBean.setQuantity(ware_num);
                suningWjsonLists.add(suningWjsonBean);
            }
        }
        json = new Gson().toJson(suningWjsonLists);
        LogUtil.e("提交订单 = " + json);
        whick = "pushorder";
        map = new HashMap<>();
        map.put("user_id", userLogin);
        map.put("addr_id", addr_id);
        map.put("remark", remark);
        map.put("orderType", "1");
        map.put("goodsJSON", json);
        map.put("invoiceState", invoiceState);
        map.put("invoiceTitle", invoiceTitle);
        map.put("taxNo", taxNo);
        map.put("sn_freight", String.valueOf(freightFare));
        netUtil.okHttp2Server2(pushorder_url, map);
    }

    /**
     * 联网请求返回数据监听
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            hindCatView();
            if (whick.equals("pushorder")) {
                tv_order_create_confirm.setClickable(true);
                parseOrderData(response);
            } else if (whick.equals("getaddress")) {
                tv_order_create_confirm.setClickable(true);
                parseAddressData(response);
            } else if (whick.equals("checkIsHave")) {
                parseCheckIshavaData(response);
            } else if (whick.equals("free")) {
                parseFreeData(response);
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e("网络连接出现问题~" + call.toString() + "-" + e.getMessage());
            hindCatView();
            tv_order_create_confirm.setClickable(true);
            isHavaGoods = false;
        }
    }

    /**
     * 解析运费的数据
     *
     * @param response
     */
    private void parseFreeData(String response) {
        LogUtil.e("运费信息 = " + response);
        try {
            SuningFreeBean suningFreeBean = new Gson().fromJson(response, SuningFreeBean.class);
            int statusCode = suningFreeBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    List<SuningFreeBean.DataBean> data = suningFreeBean.getData();
                    if (data != null && data.size() > 0) {
                        SuningFreeBean.DataBean dataBean = data.get(0);
                        if (dataBean != null) {
                            freightFare = dataBean.getFreightFare();
                            showTotalPrice(freightFare);
                            mIsRequestDataRefresh = false;
                            setRefresh(mIsRequestDataRefresh);
                        }
                    }
                    break;
                case 0:
                    Toast.makeText(OrderCartConfirmActivity.this, suningFreeBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception e) {
            LogUtil.e("OrderCartConfirmActivity parseFreeData error");
            Toast.makeText(OrderCartConfirmActivity.this, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
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
                String product_num = dataBean.getProduct_num();
                int num = Integer.parseInt(product_num);
                dataBean.setProduct_num(String.valueOf(num + 1));
                break;
            case 0:
                Toast.makeText(OrderCartConfirmActivity.this, "当前购买数量库存不足！！！", Toast.LENGTH_SHORT).show();
                break;
        }
        initData();
    }

    /**
     * 展示付款金额
     *
     * @param freightFare
     */
    private void showTotalPrice(double freightFare) {
        DecimalFormat df = new DecimalFormat("#0.00");
        double price = Double.parseDouble(totalprice);
        double total_price = freightFare + price;
        String free = df.format(freightFare);
        if (freightFare == 0) {
            tv_confirm_postage.setText("免运费");
        } else {
            tv_confirm_postage.setText("￥" + free);
        }
        tv_confirm_goods_price.setText("￥" + df.format(price));
        tv_confirm_order_totalprice.setText("￥" + df.format(total_price));
    }

    /**
     * 解析提交订单后的数据
     *
     * @param response
     */
    private void parseOrderData(String response) {
        LogUtil.e(response);
        SuningCreateBean suningCreateBean = new Gson().fromJson(response, SuningCreateBean.class);
        int statusCode = suningCreateBean.getStatusCode();
        switch (statusCode) {
            case 1:
                List<SuningCreateBean.DataBean> data = suningCreateBean.getData();
                if (data != null && data.size() > 0) {
                    SuningCreateBean.DataBean dataBean = data.get(0);
                    if (dataBean != null) {
                        double pay_price = dataBean.getPay_price();
                        String order_num = dataBean.getOrder_num();
                        int orderId = dataBean.getOrderId();
                        toCheckStand(pay_price, order_num, orderId);
                    }
                }
                break;
            case 0:
                Toast.makeText(OrderCartConfirmActivity.this, suningCreateBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析地址信息的数据
     *
     * @param response
     */
    private void parseAddressData(String response) {
        LogUtil.e(response);
        AddressDetailBean addressDetailBean = new Gson().fromJson(response, AddressDetailBean.class);
        int statusCode = addressDetailBean.getStatusCode();
        switch (statusCode) {
            case 1:
                int default_index = 0;
                data = addressDetailBean.getData();
                if (data != null && data.size() > 0) {
                    tv_sunng_add_newaddress.setVisibility(View.GONE);
                    rl_suning_default_address.setVisibility(View.VISIBLE);
                    for (int i = 0; i < data.size(); i++) {
                        AddressDetailBean.DataBean dataBean = data.get(i);
                        int is_default = dataBean.getIs_default();
                        if (is_default == 1) {
                            default_index = i;
                        }
                    }
                    AddressDetailBean.DataBean dataBean = data.get(default_index);
                    if (dataBean != null) {
                        address = dataBean.getAddress();
                        if (dataBean.getAccept_name() != null) {
                            tv_suning_address_name.setText(dataBean.getAccept_name());
                        }
                        if (dataBean.getMobile() != null) {
                            tv_suning_address_phone.setText(dataBean.getMobile());
                        }
                        if (dataBean.getArea() != null && address != null) {
                            tv_suning_address_details.setText(dataBean.getArea() + address);
                        }
                        addr_id = String.valueOf(dataBean.getId());
                        area_id = dataBean.getArea_id();
                        if (area_id != null) {
                            if (suningOrdercartAdapter != null) {
                                double price = suningOrdercartAdapter.getTotalPrice();
                                DecimalFormat df = new DecimalFormat("#0.00");
                                this.totalprice = df.format(price);
                                //showTotalPrice(freightFare);
                                getFreeData();
                            }
                        }
                    }
                } else {
                    tv_sunng_add_newaddress.setVisibility(View.VISIBLE);
                    rl_suning_default_address.setVisibility(View.GONE);
                    mIsRequestDataRefresh = false;
                    setRefresh(mIsRequestDataRefresh);
                }
                break;
            case 0:
                Toast.makeText(OrderCartConfirmActivity.this, addressDetailBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2 && requestCode == CHOICE_INVOICE_INFO) {
            //获取拿到的发票信息
            int invoice_type = data.getIntExtra("invoice_type", -1);
            String invoice_title = data.getStringExtra("invoice_title");
            String invoice_tax_no = data.getStringExtra("invoice_tax_no");
            if (invoice_type == 1) {
                invoiceState = "1";
                tv_suning_order_invoice_title.setText("纸制-" + invoice_title);
            } else if (invoice_type == 2) {
                invoiceState = "0";
                tv_suning_order_invoice_title.setText("不开发票");
            }
            invoiceTitle = invoice_title;
            taxNo = invoice_tax_no;
            LogUtil.e("invoice_type = " + invoice_type);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 刷新数据
     *
     * @param event
     */
    public void onEvent(AnyEvent event) {
        if (event.getType() == EventType.UPDATA_ADDRESS_ORDER) {
            String msg = "onEventMainThread收到了消息：" + event.getMessage();
            LogUtil.e(msg);
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 判断库存
     */
    private void checkIsHave(String addrWay, String addstr, String productId, String num) {
        String[] split = addstr.split(",");
        addstr = split[0] + "," + split[1] + "," + split[2];
        whick = "checkIsHave";
        map = new HashMap<>();
        map.put("addrstr", addstr);
        map.put("addrWay", addrWay);
        map.put("ProductId", productId);
        map.put("lnglat", "");
        map.put("num", num);
        netUtil.okHttp2Server2(pcs_url, map);
    }


}
