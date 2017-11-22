package com.gather_excellent_help.ui.activity.suning;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.AddressDetailBean;
import com.gather_excellent_help.bean.suning.SuningCreateBean;
import com.gather_excellent_help.bean.suning.SuningFreeBean;
import com.gather_excellent_help.bean.suning.SuningGoodscartBean;
import com.gather_excellent_help.bean.suning.SuningSpecidBackBean;
import com.gather_excellent_help.bean.suning.SuningWjsonBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.address.PersonAddressActivity;
import com.gather_excellent_help.ui.adapter.SuningOrdercartAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.NumberAddSubView;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
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



    private RelativeLayout rl_net_show;

    private String pushorder_url = Url.BASE_URL + "suning/AddSNOrder.aspx";//提交订单接口
    private String address_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=GetUserAddress";//地址列表接口
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
        rcv_cart_order_confirm = (RecyclerView)findViewById(R.id.rcv_cart_order_confirm);

        rl_net_show = (RelativeLayout) findViewById(R.id.rl_net_show);
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
        if(cartData == null) {
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
        suningOrdercartAdapter.setOnNumButtonListener(new SuningOrdercartAdapter.OnNumButtonListener() {
            @Override
            public void onAddClick(View v, int position, String product_id, String num,String price) {
                totalprice =price;
                showTotalPrice();
            }

            @Override
            public void onSubClick(View v, int position, String product_id, String num,String price) {
                totalprice = price;
                showTotalPrice();
            }
        });
    }

    /**
     * 获取购物车数据
     */
    private void getCartData() {
        Intent intent = getIntent();
        String cart_json = intent.getStringExtra("cart_json");
        if(cart_json!=null && !TextUtils.isEmpty(cart_json)) {
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
                    rl_net_show.setVisibility(View.VISIBLE);
                    tv_order_create_confirm.setClickable(false);
                    tv_order_create_confirm.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pushBuyOrder();
                        }
                    }, 1000);
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
        LogUtil.e(pay_price + "--" + order_num);
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
            Toast.makeText(OrderCartConfirmActivity.this, "请选择收货地址！！！", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<SuningWjsonBean> suningWjsonLists = new ArrayList<>();
        if(cartData!=null && cartData.size()>0) {
            for (int i=0;i<cartData.size();i++){
                SuningGoodscartBean.DataBean dataBean = cartData.get(i);
                goods_id = dataBean.getProduct_id();
                spec_back_id =dataBean.getProduct_spec_id();
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
        netUtil.okHttp2Server2(pushorder_url, map);
    }

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            rl_net_show.setVisibility(View.GONE);
            if (whick.equals("pushorder")) {
                tv_order_create_confirm.setClickable(true);
                parseOrderData(response);
            } else if (whick.equals("getaddress")) {
                tv_order_create_confirm.setClickable(true);
                parseAddressData(response);
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e("网络连接出现问题~" + call.toString() + "-" + e.getMessage());
            rl_net_show.setVisibility(View.GONE);
            tv_order_create_confirm.setClickable(true);
        }
    }

    /**
     * 展示付款金额
     *
     */
    private void showTotalPrice() {
        tv_confirm_postage.setText("免邮费");
        tv_confirm_goods_price.setText("￥" + totalprice);
        tv_confirm_order_totalprice.setText("￥" + totalprice);
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
                EventBus.getDefault().post(new AnyEvent(EventType.CLEAR_ALL_GOODSCART,"清空购物车"));
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
                    if(dataBean!=null) {
                        address = dataBean.getAddress();
                        if(dataBean.getAccept_name()!=null) {
                            tv_suning_address_name.setText(dataBean.getAccept_name());
                        }
                        if(dataBean.getMobile()!=null) {
                            tv_suning_address_phone.setText(dataBean.getMobile());
                        }
                        if(dataBean.getArea()!=null && address!=null) {
                            tv_suning_address_details.setText(dataBean.getArea() + address);
                        }
                        addr_id = String.valueOf(dataBean.getId());
                        area_id = dataBean.getArea_id();
                        if(area_id!=null) {
                            if(suningOrdercartAdapter!=null) {
                                double price = suningOrdercartAdapter.getTotalPrice();
                                this.totalprice = String.valueOf(price);
                                showTotalPrice();
                            }
                        }
                    }
                } else {
                    tv_sunng_add_newaddress.setVisibility(View.VISIBLE);
                    rl_suning_default_address.setVisibility(View.GONE);
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

}
