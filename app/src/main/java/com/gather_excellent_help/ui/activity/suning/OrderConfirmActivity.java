package com.gather_excellent_help.ui.activity.suning;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.gather_excellent_help.bean.suning.SuningSpecidBackBean;
import com.gather_excellent_help.bean.suning.SuningStockBean;
import com.gather_excellent_help.bean.suning.SuningWjsonBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.address.PersonAddressActivity;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.NumberAddSubView;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.ScreenUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.span.ImageSpanUtil;
import com.gather_excellent_help.utils.span.MyImageSpan;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class OrderConfirmActivity extends BaseActivity {

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
    private NumberAddSubView nas_order_confirm_num;

    //商品信息相关
    private ImageView iv_bottom_pop_img;
    private TextView tv_bottom_pop_name;
    private TextView tv_bottom_pop_goodprice;
    private TextView tv_bottom_pop_cprice;
    private TextView tv_bottom_pop_goods_num;
    private TextView tv_order_confirm_warespec;

    //private RelativeLayout rl_net_show;

    private RelativeLayout rl_order_confirm_ware;

    private SwipeRefreshLayout swip_order_confirm_refresh;

    private String pushorder_url = Url.BASE_URL + "suning/AddSNOrder.aspx";//提交订单接口
    private String address_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=GetUserAddress";//地址列表接口
    private String free_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=GetSerfree";//运费接口
    private String specsid_url = Url.BASE_URL + "suning/GoodsInfo.ashx?action=Get_Specs_Id";//商品规格返回id接口
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
    private String ware_json;
    private String userLogin;
    private String ware_num = "1";//商品数量服务器端
    private String goods_id = "";//商品id
    private String goods_title = "";//商品标题
    private String goods_img = "";//商品图片链接
    private double goods_price;//商品价格
    private String m_price = "";//商品超市价格
    private int goods_num = 1;//商品数量
    private SuningWjsonBean suningWjsonBean;
    private String spec_id = "";//一组产品规格（33,32,39）
    private String channel_id = "";//产品参数
    private String spec_back_id = "";//一组产品
    private String product_id = "";

    private boolean isHavaGoods = true;
    private String spec_content = "";//规格内容
    private boolean mIsRequestDataRefresh;
    private AlertDialog alertDialog;
    private double freightFare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
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
        nas_order_confirm_num = (NumberAddSubView) findViewById(R.id.nas_order_confirm_num);

        //商品信息
        iv_bottom_pop_img = (ImageView) findViewById(R.id.iv_bottom_pop_img);
        tv_bottom_pop_name = (TextView) findViewById(R.id.tv_bottom_pop_name);
        tv_bottom_pop_goodprice = (TextView) findViewById(R.id.tv_bottom_pop_goodprice);
        tv_bottom_pop_cprice = (TextView) findViewById(R.id.tv_bottom_pop_cprice);
        tv_bottom_pop_goods_num = (TextView) findViewById(R.id.tv_bottom_pop_goods_num);
        rl_order_confirm_ware = (RelativeLayout) findViewById(R.id.rl_order_confirm_ware);
        tv_order_confirm_warespec = (TextView) findViewById(R.id.tv_order_confirm_warespec);

        //rl_net_show = (RelativeLayout) findViewById(R.id.rl_net_show);

        swip_order_confirm_refresh = (SwipeRefreshLayout) findViewById(R.id.swip_order_confirm_refresh);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        userLogin = Tools.getUserLogin(this);
        Intent intent = getIntent();
        product_id = intent.getStringExtra("product_id");
        ware_json = intent.getStringExtra("ware_json");
        goods_img = intent.getStringExtra("goods_img");
        goods_title = intent.getStringExtra("goods_title");
        spec_content = intent.getStringExtra("spec_content");
        String ware_price = intent.getStringExtra("goods_price");
        String c_price = intent.getStringExtra("c_price");
        if (c_price != null) {
            tv_bottom_pop_cprice.getPaint().setAntiAlias(true);
            tv_bottom_pop_cprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰 
            tv_bottom_pop_cprice.setText(" ¥" + c_price);
            m_price = c_price;
        }
        goods_price = Double.parseDouble(ware_price);
        if (goods_img != null) {
            String replace_img = goods_img.replace("800x800", "400x400");
            Glide.with(this).load(replace_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                    .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                    .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                    .into(iv_bottom_pop_img);//请求成功后把图片设置到的控件
        }

        if (spec_content != null) {
            if (spec_content.length() > 10) {
                String s = spec_content.substring(0, 10) + "...";
                tv_order_confirm_warespec.setText(s);
            } else {
                tv_order_confirm_warespec.setText(spec_content);
            }
        }

        if (goods_title != null) {
            SpannableString span = new SpannableString("\t\t" + goods_title);
            Drawable drawable = getResources().getDrawable(R.drawable.s_suning_ware_icon);
            Bitmap bitmap = ImageSpanUtil.zoomDrawable(drawable, DensityUtil.dip2px(OrderConfirmActivity.this, 16), DensityUtil.dip2px(OrderConfirmActivity.this, 16));
            MyImageSpan image = new MyImageSpan(OrderConfirmActivity.this, bitmap, -1);
            span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_bottom_pop_name.setText(span);
        }
        if (ware_price != null) {
            tv_bottom_pop_goodprice.setText(" ¥" + goods_price);
        }
        if (ware_json != null) {
            LogUtil.e(ware_json);
            suningWjsonBean = new Gson().fromJson(ware_json, SuningWjsonBean.class);
            if (suningWjsonBean != null) {
                goods_id = suningWjsonBean.getArticle_id();
                String quantity = suningWjsonBean.getQuantity();
                spec_id = suningWjsonBean.getGoods_id();
                channel_id = suningWjsonBean.getChannel_id();
                LogUtil.e("quantity = " + quantity);
                if (quantity != null && !TextUtils.isEmpty(quantity)) {
                    goods_num = Integer.parseInt(quantity);
                    ware_num = quantity;
                    LogUtil.e("goods_num = " + goods_num);
                }
            }
        }
        tv_bottom_pop_goods_num.setText("x" + goods_num);
        nas_order_confirm_num.setValue(goods_num);
        //showTotalprice();
        tv_top_title_name.setText("确认订单");
        tv_suning_order_invoice_title.setText("不开发票");
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
        rl_order_confirm_ware.setOnClickListener(myonclickListener);
        et_suning_order_mark.setOnClickListener(myonclickListener);
        nas_order_confirm_num.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onSubButton(View view, int value) {
                LogUtil.e("value = " + value);
                checkIsHave("2", area_id, product_id, String.valueOf(value));
            }

            @Override
            public void onAddButton(View view, int value) {
                LogUtil.e("value = " + value);
                checkIsHave("2", area_id, product_id, String.valueOf(value));
            }
        });
        setupSwipeRefresh(swip_order_confirm_refresh);
    }

    /**
     * 显示CatView
     */
    private void showCatView() {
        View inflate = View.inflate(this, R.layout.loading_dialog_view, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflate);
        alertDialog = builder.create();
        if (OrderConfirmActivity.this != null && !OrderConfirmActivity.this.isFinishing()) {
            alertDialog.show();
            alertDialog.getWindow().setLayout(ScreenUtil.getScreenWidth(this) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * 隐藏CatView
     */
    private void hindCatView() {
        if (OrderConfirmActivity.this != null && !OrderConfirmActivity.this.isFinishing()) {
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
        if (swip_order_confirm_refresh != null) {
            swip_order_confirm_refresh.setColorSchemeResources(R.color.colorFirst,
                    R.color.colorSecond, R.color.colorThird);
            swip_order_confirm_refresh.setProgressViewOffset(true, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
            swip_order_confirm_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
            if (swip_order_confirm_refresh != null) {
                swip_order_confirm_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swip_order_confirm_refresh != null) {
                            swip_order_confirm_refresh.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        } else {
            if (swip_order_confirm_refresh != null) {
                swip_order_confirm_refresh.setRefreshing(true);
            }
        }
    }

    /**
     * 获取商品产品规格后的id
     */
    private void getSpecId() {
        whick = "specids";
        map = new HashMap<>();
        map.put("article_id", goods_id);
        map.put("specs_id", "," + spec_id + ",");
        netUtil.okHttp2Server2(OrderConfirmActivity.this, specsid_url, map);
    }

    /**
     * 获取运费
     */
    private void getFree() {
        whick = "free";
        map = new HashMap<>();
        map.put("Id", userLogin);
        map.put("user_id", userLogin);
        map.put("area_id", area_id);
        map.put("address", address);
        map.put("ProductId", product_id);
        map.put("num", ware_num);
        netUtil.okHttp2Server2(OrderConfirmActivity.this, free_url, map);
    }


    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_order_create_confirm:
                    showCatView();
                    tv_order_create_confirm.setClickable(false);
                    tv_order_create_confirm.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isHavaGoods) {
                                pushBuyOrder();
                            } else {
                                Toast.makeText(OrderConfirmActivity.this, "当前网络连接出现问题，请您重新下单再试。", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 1000);
                    break;
                case R.id.et_suning_order_mark:
                    et_suning_order_mark.setCursorVisible(true);
                    break;
                case R.id.tv_sunng_add_newaddress:
                    toPersonAdderssManager();
                    break;
                case R.id.rl_order_confirm_ware:
                    toSuningDetailPage();
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
     * 跳转到苏宁详情页面
     */
    private void toSuningDetailPage() {
        finish();
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
        Intent intent = new Intent(OrderConfirmActivity.this, PersonAddressActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到收银台
     */
    private void toCheckStand(double pay_price, String order_num, int orderId) {
        LogUtil.e(pay_price + "--" + order_num);
        EventBus.getDefault().post(new AnyEvent(EventType.GOODS_PAY_LIMIT, "更新限购数量！"));
        Intent intent = new Intent(OrderConfirmActivity.this, CheckStandActivity.class);
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
        map.put("Id", userLogin);
        map.put("user_id", userLogin);
        netUtil.okHttp2Server2(OrderConfirmActivity.this, address_url, map);
    }

    /**
     * 提交购买订单
     */
    private void pushBuyOrder() {
        getSpecId();
    }

    /**
     * 联网请求回调的监听
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            try {
                hindCatView();
                if (whick.equals("pushorder")) {
                    tv_order_create_confirm.setClickable(true);
                    parseOrderData(response);
                } else if (whick.equals("getaddress")) {
                    tv_order_create_confirm.setClickable(true);
                    parseAddressData(response);
                } else if (whick.equals("specids")) {
                    parseSpecidData(response);
                } else if (whick.equals("checkIsHave")) {
                    parseCheckIshavaData(response);
                } else if (whick.equals("free")) {
                    parseFreeData(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e("网络连接出现问题~" + call.toString() + "-" + e.getMessage());
            hindCatView();
            tv_order_create_confirm.setClickable(true);
            isHavaGoods = false;
            EncryptNetUtil.startNeterrorPage(OrderConfirmActivity.this);
        }
    }

    /**
     * 解析运费数据
     */
    private void parseFreeData(String response) throws Exception {
        LogUtil.e("free = " + response);
        SuningFreeBean suningFreeBean = new Gson().fromJson(response, SuningFreeBean.class);
        int statusCode = suningFreeBean.getStatusCode();
        switch (statusCode) {
            case 1:
                List<SuningFreeBean.DataBean> data = suningFreeBean.getData();
                if (data != null && data.size() > 0) {
                    SuningFreeBean.DataBean dataBean = data.get(0);
                    if (dataBean != null) {
                        freightFare = dataBean.getFreightFare();
                        showTotalprice(freightFare);
                        mIsRequestDataRefresh = false;
                        setRefresh(mIsRequestDataRefresh);
                    }
                }
                break;
            case 0:
                Toast.makeText(OrderConfirmActivity.this, "请选择收货地址", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析判断库存数据
     *
     * @param response
     */
    private void parseCheckIshavaData(String response) throws Exception {
        LogUtil.e(response);
        isHavaGoods = true;
        SuningStockBean suningStockBean = new Gson().fromJson(response, SuningStockBean.class);
        int statusCode = suningStockBean.getStatusCode();
        switch (statusCode) {
            case 1:
                goods_num = goods_num + 1;
                ware_num = String.valueOf(goods_num);
                nas_order_confirm_num.setValue(goods_num);
                getFree();
                break;
            case 0:
                Toast.makeText(OrderConfirmActivity.this, "当前购买数量库存不足！！！", Toast.LENGTH_SHORT).show();
                nas_order_confirm_num.setValue(goods_num);
                ware_num = String.valueOf(goods_num);
                getFree();
                break;
        }
    }

    /**
     * 解析一组产品规格请求返回的id
     *
     * @param response
     */
    private void parseSpecidData(String response) throws Exception {
        LogUtil.e("产品规格返回的id = " + response);
        SuningSpecidBackBean suningSpecidBackBean = new Gson().fromJson(response, SuningSpecidBackBean.class);
        int statusCode = suningSpecidBackBean.getStatusCode();
        switch (statusCode) {
            case 1:
                try {
                    List<SuningSpecidBackBean.DataBean> data = suningSpecidBackBean.getData();
                    if (data != null && data.size() > 0) {
                        SuningSpecidBackBean.DataBean dataBean = data.get(0);
                        if (dataBean != null) {
                            spec_back_id = dataBean.getGoods_specid();
                            if (spec_back_id == null || TextUtils.isEmpty(spec_back_id)) {
                                spec_back_id = "0";
                            }
                            remark = et_suning_order_mark.getText().toString().trim();
                            if (TextUtils.isEmpty(addr_id)) {
                                Toast.makeText(OrderConfirmActivity.this, "请选择收货地址！！！", Toast.LENGTH_SHORT).show();
                                tv_order_create_confirm.setClickable(true);
                                return;
                            }
                            ArrayList<SuningWjsonBean> suningWjsonLists = new ArrayList<>();
                            SuningWjsonBean suningWjsonBean = new SuningWjsonBean();
                            suningWjsonBean.setArticle_id(goods_id);
                            suningWjsonBean.setChannel_id(channel_id);
                            suningWjsonBean.setGoods_id(spec_back_id);
                            suningWjsonBean.setQuantity(ware_num);
                            suningWjsonLists.add(suningWjsonBean);
                            json = new Gson().toJson(suningWjsonLists);
                            LogUtil.e("提交订单 = " + json);
                            pushOrder2Server();
                        }
                    } else {
                        Toast.makeText(OrderConfirmActivity.this, "无法获取商品信息！", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    LogUtil.e("OrderConfirmActivity error");
                    Toast.makeText(OrderConfirmActivity.this, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
                }
                break;
            case 0:
                Toast.makeText(OrderConfirmActivity.this, suningSpecidBackBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 提交订单
     */
    private void pushOrder2Server() {
        whick = "pushorder";
        map = new HashMap<>();
        map.put("Id", userLogin);
        map.put("user_id", userLogin);
        map.put("addr_id", addr_id);
        map.put("remark", remark);
        map.put("orderType", "0");
        map.put("goodsJSON", json);
        map.put("invoiceState", invoiceState);
        map.put("invoiceTitle", invoiceTitle);
        map.put("taxNo", taxNo);
        map.put("sn_freight", String.valueOf(freightFare));
        netUtil.okHttp2Server2(OrderConfirmActivity.this, pushorder_url, map);
    }


    /**
     * 显示总价格以及邮费是多少
     *
     * @param freightFare
     */
    private void showTotalprice(double freightFare) {
        DecimalFormat df = new DecimalFormat("#0.00");
        String free = df.format(freightFare);
        double total = goods_price * goods_num;
        if (freightFare == 0) {
            tv_confirm_postage.setText("免运费");
        } else {
            tv_confirm_postage.setText(" ¥" + free);
        }
        tv_confirm_goods_price.setText(" ¥" + df.format(total));
        double totalprice = total + freightFare;
        tv_confirm_order_totalprice.setText(" ¥" + df.format(totalprice));
        tv_bottom_pop_goods_num.setText("x" + goods_num);
    }

    /**
     * 解析提交订单后的数据
     *
     * @param response
     */
    private void parseOrderData(String response) throws Exception {
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
                Toast.makeText(OrderConfirmActivity.this, suningCreateBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析地址信息的数据
     *
     * @param response
     */
    private void parseAddressData(String response) throws Exception {
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
                        this.area_id = dataBean.getArea_id();
                    }
                } else {
                    tv_sunng_add_newaddress.setVisibility(View.VISIBLE);
                    rl_suning_default_address.setVisibility(View.GONE);
                    mIsRequestDataRefresh = false;
                    setRefresh(mIsRequestDataRefresh);
                }
                getFree();
                break;
            case 0:
                Toast.makeText(OrderConfirmActivity.this, addressDetailBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
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
    protected void onStop() {
        super.onStop();
        hindCatView();
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
        if (addstr != null && !TextUtils.isEmpty(addstr)) {
            String[] split = addstr.split(",");
            addstr = split[0] + "," + split[1] + "," + split[2];
            whick = "checkIsHave";
            map = new HashMap<>();
            map.put("addrstr", addstr);
            map.put("addrWay", addrWay);
            map.put("ProductId", productId);
            map.put("lnglat", "");
            map.put("num", num);
            netUtil.okHttp2Server2(OrderConfirmActivity.this, pcs_url, map);
        } else {
            Toast.makeText(OrderConfirmActivity.this, "请选择收货地址", Toast.LENGTH_SHORT).show();
        }
    }

}
