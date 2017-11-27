package com.gather_excellent_help.ui.activity.suning;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.suning.SuningStockBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.fragment.dragfragment.VerticalFragment1;
import com.gather_excellent_help.ui.fragment.dragfragment.VerticalFragment3;
import com.gather_excellent_help.ui.widget.DragLayout;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.PhotoUtils;
import com.gather_excellent_help.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class SuningDetailActivity extends FragmentActivity {

    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 0x123;
    private DragLayout draglayout;
    private FrameLayout fl_suning_top;
    private FrameLayout fl_suning_bottom;
    private TextView tv_suning_detail_buy;
    private TextView tv_suning_detail_cart;
    private VerticalFragment1 fragment1;
    private VerticalFragment3 fragment3;

    private View vShadow;

    private String ware_url = Url.BASE_URL + "suning/GoodsInfo.ashx?action=GetInfo";//商品信息
    private String whick;
    private NetUtil netUtil;
    private Map<String, String> map;


    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private String load_over = "";

    private String accept_address = "";//送至xx地
    private String lalotitude = "";//经纬度
    private String goods_img = "";//商品图片链接
    private String goods_title = "";//商品标题
    private String goods_price = "";//商品价格
    private String c_price = "";//超市价格
    private int article_id;//数据库自增id
    private String goods_id = "";//产品id
    private String local_position = "39.934,116.329";//当前位置经纬度
    private long start_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suning_detail);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {

        draglayout = (DragLayout) findViewById(R.id.draglayout);
        fl_suning_top = (FrameLayout) findViewById(R.id.fl_suning_top);
        fl_suning_bottom = (FrameLayout) findViewById(R.id.fl_suning_bottom);
        tv_suning_detail_buy = (TextView) findViewById(R.id.tv_suning_detail_buy);
        tv_suning_detail_cart = (TextView) findViewById(R.id.tv_suning_detail_cart);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        article_id = intent.getIntExtra("article_id", 0);
        goods_id = intent.getStringExtra("goods_id");
        goods_img = intent.getStringExtra("goods_img");
        goods_title = intent.getStringExtra("goods_title");
        goods_price = intent.getStringExtra("goods_price");
        c_price = intent.getStringExtra("c_price");
        fragment1 = new VerticalFragment1();
        fragment3 = new VerticalFragment3();
        Bundle bundle = new Bundle();
        bundle.putString("article_id",String.valueOf(article_id));
        bundle.putString("goods_id",goods_id);
        bundle.putString("goods_img",goods_img);
        bundle.putString("goods_title",goods_title);
        bundle.putString("goods_price",goods_price);
        bundle.putString("c_price",c_price);
        fragment1.setArguments(bundle);
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        netUtil = new NetUtil();
        getWareData();
        OnServerResponseListener onServerResponseListener = new OnServerResponseListener();
        netUtil.setOnServerResponseListener(onServerResponseListener);
        //获取地理位置信息
        obtainRequirePermission();
        MyonclickListener myonclickListener = new MyonclickListener();
        tv_suning_detail_buy.setOnClickListener(myonclickListener);
        tv_suning_detail_cart.setOnClickListener(myonclickListener);
        tv_suning_detail_buy.setClickable(false);
        tv_suning_detail_cart.setClickable(false);

    }

    /**
     * 获取商品信息数据
     */
    private void getWareData() {
        whick = "ware";
        map = new HashMap<>();
        map.put("goods_id", goods_id);
        map.put("Local", local_position);
        netUtil.okHttp2Server2(ware_url, map);
    }


    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();   //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            LogUtil.e(addr + "-" + province + "-" + city + "-" + district);
            accept_address = province + " " + city + " " + district;
            lalotitude = latitude + "," + longitude;
            fragment1.setCurrentPostion(accept_address, lalotitude);
        }
    }


    private void obtainRequirePermission() {
        //获取Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSIONS_REQUEST_CODE);
        } else {
            mLocationClient.start();
            //mLocationClient为第二步初始化过的LocationClient对象
            //调用LocationClient的start()方法，便可发起定位请求
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LOCATION_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationClient.start();
                    //mLocationClient为第二步初始化过的LocationClient对象
                    //调用LocationClient的start()方法，便可发起定位请求
                } else {
                    ToastUtils.showShort(this, "请允许打开读取位置信息的权限！！");
                }
                break;
        }
    }


    /**
     * 加载上下两个fragment
     */
    private void showDrglayout() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_suning_top, fragment1).add(R.id.fl_suning_bottom, fragment3)
                .commit();

        DragLayout.ShowNextPageNotifier nextIntf = new DragLayout.ShowNextPageNotifier() {
            @Override
            public void onDragNext() {
                fragment3.initView();
            }
        };
        draglayout.setNextPageListener(nextIntf);
    }

    /**
     * 监听页面上的点击事件
     */
    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_suning_detail_buy:
                    toBuySuningWare();
                    break;
                case R.id.tv_suning_detail_cart:
                    toAddGoodsCart();
                    break;
                case R.id.rl_exit:
                    finish();
                    break;
            }
        }
    }

    /**
     * 加入购物车
     */
    private void toAddGoodsCart() {
        if(fragment1!=null) {
            fragment1.setWhat_buy(2);
            fragment1.setIsSpecFirst("click");
            fragment1.getSpecData();
        }
    }

    /**
     * 购买苏宁商品的方法
     */
    private void toBuySuningWare() {
       if(fragment1!=null) {
           fragment1.setWhat_buy(1);
           fragment1.setIsSpecFirst("click");
           fragment1.getSpecData();
       }
    }

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            if (whick.equals("ware")) {
                showDrglayout();
                fragment1.setResponse(response);
                fragment3.setResponse(response);
                load_over = "over";
                fragment1.setOnLoadCompleteListenr(new VerticalFragment1.OnLoadCompleteListenr() {
                    @Override
                    public void onLoadComplete() {
                        tv_suning_detail_buy.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tv_suning_detail_buy.setClickable(true);
                            }
                        },500);
                        tv_suning_detail_cart.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tv_suning_detail_cart.setClickable(true);
                            }
                        },500);
                    }
                });
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e("网络连接出现问题~");
        }
    }

    /**
     * 刷新数据
     *
     * @param event
     */
    public void onEvent(AnyEvent event) {
        if (event.getType() == EventType.GOODSCART_CLEAR) {
            String msg = "onEventMainThread收到了消息：" + event.getMessage();
            LogUtil.e(msg);
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null && myListener != null) {
            mLocationClient.unRegisterLocationListener(myListener);
            myListener = null;
            mLocationClient = null;
        }
        EventBus.getDefault().unregister(this);
    }
}
