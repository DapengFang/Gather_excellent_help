package com.gather_excellent_help.ui.fragment.dragfragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.suning.SuningLimitBean;
import com.gather_excellent_help.bean.suning.SuningSpecBean;
import com.gather_excellent_help.bean.suning.SuningSpecidBackBean;
import com.gather_excellent_help.bean.suning.SuningStockBean;
import com.gather_excellent_help.bean.suning.SuningWareBean;
import com.gather_excellent_help.bean.suning.netcart.NetGoodscartAddBean;
import com.gather_excellent_help.db.suning.SqliteServiceManager;
import com.gather_excellent_help.ui.activity.suning.HackySeeBigimgActivity;
import com.gather_excellent_help.ui.activity.suning.OrderConfirmActivity;
import com.gather_excellent_help.ui.activity.suning.SuningDetailActivity;
import com.gather_excellent_help.ui.activity.suning.SuningGoodscartActivity;
import com.gather_excellent_help.ui.widget.PcsDetailChoicePopupwindow;
import com.gather_excellent_help.ui.widget.SharePopupwindow;
import com.gather_excellent_help.ui.widget.SuningStandardPopupwindow;
import com.gather_excellent_help.ui.widget.SuningWarenumPopupwindow;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.cartutils.NetCartUtil;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**
 * VerticalFragment1 商品详情页顶部上半部分
 */
public class VerticalFragment1 extends Fragment {

    private Context context;
    private ViewPager vp_top_home;
    private LinearLayout ll_point_group;
    private TextView tv_vertical_standard;
    private LinearLayout llRoot;

    private TextView tv_good_detail_name;
    private TextView tv_activity_sun_tao_icon;
    private TextView tv_good_detail_goodprice;
    private TextView tv_good_detail_cprice;
    private TextView tv_vertical_ishave_quarity;
    private TextView tv_vertical_standard_title;
    private RelativeLayout rl_vertical_see_spec;

    private RelativeLayout rl_vertical_see_ware_num;
    private TextView tv_vertical_standard_ware_num;

    private ImageView iv_suning_detail_back;
    private ImageView iv_suning_detail_cart;

    private String pcs_url = Url.BASE_URL + "suning/GoodsInfo.ashx?action=GetStoreQuantity";//是否有货
    private String ishave_url = Url.BASE_URL + "suning/GoodsInfo.ashx?action=ChangeProductNumsStore";//判断库存
    private String specsid_url = Url.BASE_URL + "suning/GoodsInfo.ashx?action=Get_Specs_Id";//商品规格返回id接口
    private String limit_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=GetLimitDetail";//商品返回的限购
    private RelativeLayout rl_vertical_address_choice;
    private TextView tv_vertical_address;

    private TextView tv_good_detail_limit;

    private String isSpecFirst = "";

    private PcsDetailChoicePopupwindow pcsDetailChoicePopupwindow;

    private View vShadow;

    private Myhandler myhandler;

    private int prePosition = 0;

    private String pcs = "";//省市区

    private String spec_url = Url.BASE_URL + "suning/GoodsInfo.ashx?action=GetSpecs";//规格接口

    private NetUtil netUtil;
    private NetUtil netUtil2;
    private Map<String, String> map;

    private String specs_id = "";//规格id

    private String local_position = "39.934,116.329";//当前位置经纬度

    private String spec_ids = "";//记录一组规格id

    private SuningStandardPopupwindow suningStandardPopupwindow;
    private SuningWarenumPopupwindow suningWarennumPopupwindow;


    private String whick;
    private String response = "";
    private String address = "";
    private String lalotitude = "";
    private List<SuningWareBean.DataBean.UrlsBean> img_urls;
    private int index_l1;
    private int ware_num = 1;//商品数量
    private String spec_titel = "";
    private String ware_json = "";

    private String isHave = "0";
    private long start_time;

    private String goods_id = "";//商品id
    private String article_id = "";//数据库自增id
    private String goods_img = "";//商品图片链接
    private String goods_title = "";//商品标题
    private String goods_price = "";//商品价格
    private String c_price = "";//超市价格

    private int what_buy = 1;
    private List<SuningSpecBean.DataBean> data;//商品规格数据
    private String attr_id = "";//省市区id
    private int limit_num;
    private String addrWay = "1";
    private String userLogin;
    private int purchased_num;
    private int sale_num;

    private ImageView iv_suning_detail_share;

    private NetCartUtil netCartUtil;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private SharePopupwindow sharePopupwindow;
    private AlertDialog dialog;
    private String store_text;
    private int store_status;
    private int cancel_order_num;


    public class Myhandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            long end_time = System.currentTimeMillis();
            long time = end_time - start_time;
            if (time > 5000) {
                if (msg.what == 1) {
                    myhandler.removeMessages(1);
                    Toast.makeText(context, "获取商品信息失败！！！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (msg.what == 2) {
                    myhandler.removeMessages(2);
                    tv_vertical_address.setText("定位失败");
                    return;
                }
            }
            if (msg.what == 0) {
                int item = (vp_top_home.getCurrentItem() + 1) % img_urls.size();
                vp_top_home.setCurrentItem(item);
                myhandler.postDelayed(new MyRunnable(), 4000);
            } else if (msg.what == 1) {
                if (!TextUtils.isEmpty(response)) {
                    myhandler.removeMessages(1);
                    parseWareData(response);
                } else {
                    myhandler.sendEmptyMessageDelayed(1, 1000);
                }
            } else if (msg.what == 2) {
                if (!TextUtils.isEmpty(address) && !TextUtils.isEmpty(lalotitude)) {
                    tv_vertical_address.setText(address);
                    pcs = address.replace(" ", ",");
                    getCurrentIsHave("1", "", lalotitude, goods_id);
                    myhandler.removeMessages(2);
                } else {
                    tv_vertical_address.setText("正在定位");
                    myhandler.sendEmptyMessageDelayed(2, 1000);
                }
            }
        }
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            myhandler.sendEmptyMessage(0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View rootView = inflater.inflate(R.layout.vertical_fragment1, null);
        initView(rootView);
        initData();
        return rootView;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //联网初始化
        netUtil = new NetUtil();
        netUtil2 = new NetUtil();

        //购物车数据相关
        netCartUtil = new NetCartUtil();
        OnCartResponseListener onCartResponseListener = new OnCartResponseListener();
        netCartUtil.setOnCartResponseListener(onCartResponseListener);
        userLogin = Tools.getUserLogin(context);

        Bundle bundle = getArguments();
        article_id = bundle.getString("article_id");
        goods_id = bundle.getString("goods_id");
        goods_img = bundle.getString("goods_img");
        goods_title = bundle.getString("goods_title");
        goods_price = bundle.getString("goods_price");
        c_price = bundle.getString("c_price");
        if (myhandler == null) {
            myhandler = new Myhandler();
        }
        start_time = System.currentTimeMillis();
        myhandler.sendEmptyMessageDelayed(2, 1000);
        myhandler.sendEmptyMessageDelayed(1, 1000);
        OnServerResponseListener onServerResponseListener = new OnServerResponseListener();
        netUtil.setOnServerResponseListener(onServerResponseListener);
        netUtil2.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                if (whick.equals("limit_num")) {
                    parseLimitData(response);
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "-" + e.getMessage());
            }
        });
        getSpecData();
        getLimitData();
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_vertical_address_choice.setOnClickListener(myonclickListener);
        rl_vertical_see_spec.setOnClickListener(myonclickListener);
        rl_vertical_see_ware_num.setOnClickListener(myonclickListener);
        iv_suning_detail_back.setOnClickListener(myonclickListener);
        iv_suning_detail_cart.setOnClickListener(myonclickListener);
        iv_suning_detail_share.setOnClickListener(myonclickListener);
    }

    /**
     * 获取商品限购的数据
     */
    private void getLimitData() {
        if (netUtil2 == null) {
            netUtil2 = new NetUtil();
        }
        whick = "limit_num";
        map = new HashMap<>();
        map.put("user_id", userLogin);
        map.put("article_id", article_id);
        netUtil2.okHttp2Server2(limit_url, map);
        LogUtil.e("------------------" + article_id);
    }


    /**
     * 初始化控件
     *
     * @param rootView
     */
    private void initView(View rootView) {
        vp_top_home = (ViewPager) rootView.findViewById(R.id.vp_top_home);
        ll_point_group = (LinearLayout) rootView.findViewById(R.id.ll_point_group);
        tv_vertical_standard = (TextView) rootView.findViewById(R.id.tv_vertical_standard);
        vShadow = rootView.findViewById(R.id.v_shadow);
        llRoot = (LinearLayout) rootView.findViewById(R.id.ll_root);
        tv_good_detail_name = (TextView) rootView.findViewById(R.id.tv_good_detail_name);
        tv_activity_sun_tao_icon = (TextView) rootView.findViewById(R.id.tv_activity_sun_tao_icon);
        tv_good_detail_goodprice = (TextView) rootView.findViewById(R.id.tv_good_detail_goodprice);
        tv_good_detail_cprice = (TextView) rootView.findViewById(R.id.tv_good_detail_cprice);
        tv_vertical_address = (TextView) rootView.findViewById(R.id.tv_vertical_address);
        rl_vertical_address_choice = (RelativeLayout) rootView.findViewById(R.id.rl_vertical_address_choice);
        tv_vertical_ishave_quarity = (TextView) rootView.findViewById(R.id.tv_vertical_ishave_quarity);
        tv_vertical_standard_title = (TextView) rootView.findViewById(R.id.tv_vertical_standard_title);
        rl_vertical_see_spec = (RelativeLayout) rootView.findViewById(R.id.rl_vertical_see_spec);

        rl_vertical_see_ware_num = (RelativeLayout) rootView.findViewById(R.id.rl_vertical_see_ware_num);
        tv_vertical_standard_ware_num = (TextView) rootView.findViewById(R.id.tv_vertical_standard_ware_num);

        iv_suning_detail_back = (ImageView) rootView.findViewById(R.id.iv_suning_detail_back);
        iv_suning_detail_cart = (ImageView) rootView.findViewById(R.id.iv_suning_detail_cart);
        iv_suning_detail_share = (ImageView) rootView.findViewById(R.id.iv_suning_detail_share);
        tv_good_detail_limit = (TextView) rootView.findViewById(R.id.tv_good_detail_limit);
    }


    /**
     * 轮播图添加点
     */
    private void addPoint() {

        if (img_urls != null && img_urls.size() > 0) {
            ll_point_group.removeAllViews();
            //添加顶部新闻的红点
            for (int i = 0; i < img_urls.size(); i++) {
                ImageView point = new ImageView(context);
                point.setBackgroundResource(R.drawable.guide_vp_point_selector);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 5));

                if (i == 0) {
                    point.setEnabled(true);//高亮显示
                } else {
                    point.setEnabled(false);
                    params.leftMargin = DensityUtil.dip2px(context, 8);
                }
                point.setLayoutParams(params);

                //添加到线性布局里面
                ll_point_group.addView(point);
            }
        }
    }


    /**
     * 顶部轮播图滚动的PagerAdapter
     */
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return null == img_urls ? 0 : img_urls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = (ImageView) View.inflate(context, R.layout.goods_common_img, null);
            SuningWareBean.DataBean.UrlsBean urlsBean = img_urls.get(position);
            String original_path = urlsBean.getOriginal_path();
            Glide.with(context).load(original_path)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                    .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                    .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                    .into(imageView);//请求成功后把图片设置到的控件
            container.addView(imageView);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            myhandler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP:
                            myhandler.removeCallbacksAndMessages(null);
                            myhandler.sendEmptyMessage(0);
                            seeBigImage();
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            myhandler.postDelayed(new MyRunnable(), 4000);
                            break;
                    }
                    return true;
                }
            });

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    /**
     * 发送Handler消息
     */
    private void sendHandler() {
        if (myhandler == null) {
            myhandler = new Myhandler();
        }
        myhandler.removeCallbacksAndMessages(null);
        myhandler.postDelayed(new MyRunnable(), 4000);
    }

    private boolean isDrager = false;

    /**
     * 顶部轮播图滚动的时候的监听
     */
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            //把上一次的设置为默认
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            //ll_point_group_magazine.getChildAt(nextPrePosition).setEnabled(false);

            //把当前的设置高亮
            ll_point_group.getChildAt(position).setEnabled(true);
            //ll_point_group_magazine.getChildAt(position).setEnabled(true);

            prePosition = position;
            //nextPrePosition=position;
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE:
                    if (isDrager) {
                        isDrager = false;
                        myhandler.removeCallbacksAndMessages(null);
                        myhandler.postDelayed(new MyRunnable(), 4000);
                    }
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:
                    myhandler.removeCallbacksAndMessages(null);
                    isDrager = true;
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    if (isDrager) {
                        isDrager = false;
                        myhandler.removeCallbacksAndMessages(null);
                        myhandler.postDelayed(new MyRunnable(), 4000);
                    }
                    break;
            }
        }

    }

    /**
     * 页面上点击事件的监听
     */
    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_vertical_see_spec:
                    isSpecFirst = "click";
                    getSpecData();
                    break;
                case R.id.rl_vertical_address_choice:
                    showPopMenu();
                    break;
                case R.id.rl_vertical_see_ware_num:
                    showWarenumPop();
                    break;
                case R.id.iv_suning_detail_back:
                    SuningDetailActivity activity = (SuningDetailActivity) context;
                    activity.finish();
                    break;
                case R.id.iv_suning_detail_cart:
                    toGoodsCart();
                    break;
                case R.id.iv_suning_detail_share:
                    shareToPlatform();
                    break;
            }
        }
    }

    /**
     * 分享到不同的平台
     */
    private void shareToPlatform() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions((SuningDetailActivity) context, mPermissionList, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            shareWareUrl();
        }
    }

    /**
     * 请求权限打开的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    shareWareUrl();
                } else {
                    Toast.makeText(context, "请允许打开操作SDCard权限！！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 商品转链分享
     */
    private void shareWareUrl() {
        vShadow.setVisibility(View.VISIBLE);
        showSharePopMenu();
        sharePopupwindow.setOnItemClickListenr(new SharePopupwindow.OnItemClickListenr() {
            @Override
            public void onQQClick() {
                showCopyDialog(SHARE_MEDIA.QQ);
            }

            @Override
            public void onWeixinClick() {
                showCopyDialog(SHARE_MEDIA.WEIXIN);
            }

            @Override
            public void onSinaClick() {
                showCopyDialog(SHARE_MEDIA.SINA);
            }

            @Override
            public void onWeixinFriendClick() {
                showCopyDialog(SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        });
    }

    /**
     * 剪切板剪切淘口令
     *
     * @param paltform
     */
    private void showCopyDialog(final SHARE_MEDIA paltform) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View inflate = View.inflate(context, R.layout.item_copy_taoword_dialog, null);
        TextView tvCopyContent = (TextView) inflate.findViewById(R.id.tv_copy_taoword_content);
        TextView tvCopyDismiss = (TextView) inflate.findViewById(R.id.tv_copy_taoword_dismiss);
        TextView tvCopyShare = (TextView) inflate.findViewById(R.id.tv_copy_taoword_share);
        final String share_content = "仅" + goods_price + "元" + goods_title;
        tvCopyContent.setText(share_content);
        dialog = builder.setView(inflate)
                .show();

        tvCopyDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        tvCopyShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(share_content);
                shareDiffSolfplam(paltform);
            }
        });
    }

    /**
     * 分享淘口令链接到不同给的平台
     *
     * @param platform
     */
    private void shareDiffSolfplam(SHARE_MEDIA platform) {
        LogUtil.e(goods_img);
        UMImage image = new UMImage(context, goods_img);//网络图片
        UMImage thumb = new UMImage(context, R.mipmap.juyoubang_logo);
        image.setThumb(thumb);
        UMWeb web = new UMWeb("http://www.baidu.com");
        web.setTitle(goods_title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription("我在聚优帮看到了一件不错的商品,你也看看吧");//描述
        new ShareAction((SuningDetailActivity) context)
                .setPlatform(platform)//传入平台
                .withMedia(web)//分享内容
                .setCallback(shareListener)//回调监听器
                .share();
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
            Toast.makeText(context, "分享成功", Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (sharePopupwindow != null && sharePopupwindow.isShowing()) {
                sharePopupwindow.dismiss();
            }
            if (vShadow != null) {
                vShadow.setVisibility(View.GONE);
            }
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(context, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (sharePopupwindow != null && sharePopupwindow.isShowing()) {
                sharePopupwindow.dismiss();
            }
            if (vShadow != null) {
                vShadow.setVisibility(View.GONE);
            }
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(context, "分享取消", Toast.LENGTH_LONG).show();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (sharePopupwindow != null && sharePopupwindow.isShowing()) {
                sharePopupwindow.dismiss();
            }
            if (vShadow != null) {
                vShadow.setVisibility(View.GONE);
            }
        }
    };


    /**
     * 创建分享内容的Popupwindow
     */
    private void showSharePopMenu() {
        if (sharePopupwindow == null) {
            sharePopupwindow = new SharePopupwindow(context, vShadow);
            sharePopupwindow.showAtLocation(llRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else if (sharePopupwindow != null
                && sharePopupwindow.isShowing()) {
            sharePopupwindow.dismiss();
        } else {
            sharePopupwindow.showAtLocation(llRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }

    }

    public String getAttr_id() {
        return attr_id;
    }

    public String getAddrWay() {
        return addrWay;
    }

    public String getLalotitude() {
        return lalotitude;
    }

    /**
     * 跳转到购物车界面
     */
    private void toGoodsCart() {
        Intent intent = new Intent(context, SuningGoodscartActivity.class);
        intent.putExtra("area_id", attr_id);
        intent.putExtra("addWay", addrWay);
        intent.putExtra("lalotitude", lalotitude);
        startActivity(intent);
    }

    /**
     * 显示省市区的popupwindow
     */
    private void showPopMenu() {

//        vShadow.setVisibility(View.VISIBLE);
//        if (suningPcsChoicePopupwindow == null) {
//            suningPcsChoicePopupwindow = new SuningPcsChoicePopupwindow(context, vShadow);
//            suningPcsChoicePopupwindow.showAtLocation(llRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        } else if (suningPcsChoicePopupwindow != null
//                && suningPcsChoicePopupwindow.isShowing()) {
//            suningPcsChoicePopupwindow.dismiss();
//        } else {
//            suningPcsChoicePopupwindow.showAtLocation(llRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        }
//        suningPcsChoicePopupwindow.setOnItemClickListenr(new SuningPcsChoicePopupwindow.OnItemClickListenr() {
//
//
//            @Override
//            public void getFinalAddress(String address) {
//                pcs = address;
//                LogUtil.e(address);
//                String replace_address = address.replace(",", " ");
//                tv_vertical_address.setText(replace_address);
//                if (suningPcsChoicePopupwindow.isShowing()) {
//                    suningPcsChoicePopupwindow.dismiss();
//                }
//                getCurrentIsHave("2", pcs, "", goods_id);
//                vShadow.setVisibility(View.GONE);
//            }
//        });

        vShadow.setVisibility(View.VISIBLE);
        if (pcsDetailChoicePopupwindow == null) {
            pcsDetailChoicePopupwindow = new PcsDetailChoicePopupwindow(context, vShadow);
            pcsDetailChoicePopupwindow.showAtLocation(llRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else if (pcsDetailChoicePopupwindow != null
                && pcsDetailChoicePopupwindow.isShowing()) {
            pcsDetailChoicePopupwindow.dismiss();
        } else {
            pcsDetailChoicePopupwindow.showAtLocation(llRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        pcsDetailChoicePopupwindow.setOnItemClickListenr(new PcsDetailChoicePopupwindow.OnItemClickListenr() {
            @Override
            public void getFinalAddress(String address, String a, String a_id) {
                LogUtil.e(address);
                pcs = a;
                attr_id = a_id;
                addrWay = "2";
                tv_vertical_address.setText(address);
                if (pcsDetailChoicePopupwindow.isShowing()) {
                    pcsDetailChoicePopupwindow.dismiss();
                }
                getCurrentIsHave("2", attr_id, "", goods_id);
                vShadow.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 获取当前商品库存
     */
    private void getCurrentIsHave(String addWay, String addrstr, String lnglat, String productId) {
        whick = "isHaveGoods";
        map = new HashMap<>();
        map.put("addrWay", addWay);
        map.put("addrstr", addrstr);
        map.put("lnglat", lnglat);
        map.put("ProductId", productId);
        map.put("num", String.valueOf(ware_num));
        netUtil.okHttp2Server2(pcs_url, map);
    }

    /**
     * 弹出选择规格的popwindow
     */
    private void showStandardPop(List<SuningSpecBean.DataBean> data) {
        if (sale_num <= 0) {
            return;
        }
        vShadow.setVisibility(View.VISIBLE);
        if (suningStandardPopupwindow == null) {
            suningStandardPopupwindow = new SuningStandardPopupwindow(context, vShadow, data);
            suningStandardPopupwindow.setNumber(ware_num);
            suningStandardPopupwindow.setLimitNumber(limit_num);
            suningStandardPopupwindow.setNavLimitNumber();
            suningStandardPopupwindow.setNavNumber();
            suningStandardPopupwindow.setGoods_id(goods_id);
            suningStandardPopupwindow.setArticle_id(article_id);
            suningStandardPopupwindow.setGoods_title(goods_title);
            suningStandardPopupwindow.setGoods_img(goods_img);
            suningStandardPopupwindow.setGoods_price(goods_price);
            suningStandardPopupwindow.setC_price(c_price);
            suningStandardPopupwindow.setSunigWareShow();
            suningStandardPopupwindow.showAtLocation(llRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else if (suningStandardPopupwindow != null
                && suningStandardPopupwindow.isShowing()) {
            suningStandardPopupwindow.dismiss();
        } else {
            suningStandardPopupwindow.setNumber(ware_num);
            suningStandardPopupwindow.setNavNumber();
            suningStandardPopupwindow.setGoods_id(goods_id);
            suningStandardPopupwindow.setArticle_id(article_id);
            suningStandardPopupwindow.setGoods_title(goods_title);
            suningStandardPopupwindow.setGoods_img(goods_img);
            suningStandardPopupwindow.setGoods_price(goods_price);
            suningStandardPopupwindow.setC_price(c_price);
            suningStandardPopupwindow.setSunigWareShow();
            suningStandardPopupwindow.showAtLocation(llRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }

        if (suningStandardPopupwindow != null) {
            suningStandardPopupwindow.setOnItemClickListenr(new SuningStandardPopupwindow.OnItemClickListenr() {
                @Override
                public void onPopupBuy(List<SuningSpecBean.DataBean> data, int number) {
                    setSpecShowAndData(data);
                    ware_num = number;
                    tv_vertical_standard_ware_num.setText("数量：" + ware_num + "件商品");
                    LogUtil.e(spec_ids + "-----" + "数量 = " + ware_num);
                    if (spec_titel != null) {
                        if (spec_titel.length() > 16) {
                            spec_titel = spec_titel.substring(0, 16) + "...";
                        }
                    }
                    tv_vertical_standard_title.setText(spec_titel);
                    map = new HashMap<String, String>();
                    map.put("channel_id", "7");
                    map.put("article_id", article_id);
                    map.put("goods_id", spec_ids);
                    map.put("quantity", String.valueOf(ware_num));
                    ware_json = new Gson().toJson(map);
                    LogUtil.e(ware_json);
                    suningStandardPopupwindow.dismiss();
                    vShadow.setVisibility(View.GONE);
                    LogUtil.e("----------" + spec_ids);
                    boolean b = checkIsToBuy();
                    if (!b) {
                        Toast.makeText(context, "定位失败，请您手动选择送货地区！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int ware_num = getWare_num();
                    checkIsHave(addrWay, attr_id, goods_id, String.valueOf(ware_num));
                }
            });
        }
    }


    /**
     * 获取商品产品规格后的id
     */
    private void getSpecId() {
        if (netUtil == null) {
            netUtil = new NetUtil();
        }
        whick = "specids";
        map = new HashMap<>();
        map.put("article_id", article_id);
        map.put("specs_id", "," + spec_ids + ",");
        netUtil.okHttp2Server2(specsid_url, map);
    }

    /**
     * 判断库存
     */
    private void checkIsHave(String addrWay, String addstr, String productId, String num) {
        whick = "checkIsHave";
        if (addrWay == "2") {
            lalotitude = "";
        }
        map = new HashMap<>();
        map.put("addrstr", addstr);
        map.put("addrWay", addrWay);
        map.put("ProductId", productId);
        map.put("lnglat", lalotitude);
        map.put("num", num);
        netUtil.okHttp2Server2(pcs_url, map);
    }

    /**
     * 设置商品规格数据
     *
     * @param data
     */
    private void setSpecShowAndData(List<SuningSpecBean.DataBean> data) {
        if (data != null) {
            if (data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    SuningSpecBean.DataBean dataBean = data.get(i);
                    String title = dataBean.getTitle();
                    LogUtil.e("title = " + title);
                    List<SuningSpecBean.DataBean.ContentBean> content = dataBean.getContent();
                    for (int j = 0; j < content.size(); j++) {
                        SuningSpecBean.DataBean.ContentBean contentBean = content.get(j);
                        if (contentBean.isCheck()) {
                            int spec_id = contentBean.getSpec_id();
                            spec_ids += spec_id + ",";
                            spec_titel += title + ":" + contentBean.getTitle() + ",";
                        }
                    }
                }
                spec_titel = spec_titel.substring(0, spec_titel.length() - 1);
                spec_ids = spec_ids.substring(0, spec_ids.length() - 1);

            } else {
                spec_titel = "";
                spec_ids = "";
            }
        } else {
            spec_titel = "";
            spec_ids = "";
        }
        LogUtil.e(spec_titel + "------------" + spec_ids);
    }

    /**
     * 设置商品初始化规格数据
     *
     * @param data
     */
    private void setDefaultSpecShowAndData(List<SuningSpecBean.DataBean> data) {
        try {
            if (data != null) {
                if (data.size() > 0) {
                    for (int i = 0; i < data.size(); i++) {
                        SuningSpecBean.DataBean dataBean = data.get(i);
                        String title = dataBean.getTitle();
                        List<SuningSpecBean.DataBean.ContentBean> content = dataBean.getContent();
                        if (content != null && content.size() > 0) {
                            SuningSpecBean.DataBean.ContentBean contentBean = content.get(0);
                            int spec_id = contentBean.getSpec_id();
                            spec_ids += spec_id + ",";
                            spec_titel += title + ":" + contentBean.getTitle() + ",";
                        }
                    }
                    if (spec_titel != null && !TextUtils.isEmpty(spec_titel)) {
                        spec_titel = spec_titel.substring(0, spec_titel.length() - 1);
                    }
                    if (spec_ids != null && !TextUtils.isEmpty(spec_ids)) {
                        spec_ids = spec_ids.substring(0, spec_ids.length() - 1);
                    }
                    if (spec_titel != null) {
                        if (spec_titel.length() > 16) {
                            spec_titel = spec_titel.substring(0, 16) + "...";
                        }
                    }
                } else {
                    spec_ids = "";
                    spec_titel = "";
                }
                tv_vertical_standard_title.setText(spec_titel);
                map = new HashMap<String, String>();
                map.put("channel_id", "7");
                map.put("article_id", article_id);
                map.put("goods_id", spec_ids);
                map.put("quantity", String.valueOf(ware_num));
                ware_json = new Gson().toJson(map);
                LogUtil.e("当前json = " + ware_json);
            }
        } catch (Exception e) {
            LogUtil.e("VerticalFragment setDefaultSpecShowAndData error");
            Toast.makeText(context, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 弹出选择规格的popwindow
     */
    private void showWarenumPop() {
        vShadow.setVisibility(View.VISIBLE);
        if (suningWarennumPopupwindow == null) {
            suningWarennumPopupwindow = new SuningWarenumPopupwindow(context, vShadow);
            suningWarennumPopupwindow.setNumber(ware_num);
            suningWarennumPopupwindow.setLimitNumber(limit_num);
            suningWarennumPopupwindow.setNavLimitNumber();
            suningWarennumPopupwindow.setNavNumber();
            suningWarennumPopupwindow.setGoods_id(goods_id);
            suningWarennumPopupwindow.setArticle_id(article_id);
            suningWarennumPopupwindow.setGoods_title(goods_title);
            suningWarennumPopupwindow.setGoods_img(goods_img);
            suningWarennumPopupwindow.setGoods_price(goods_price);
            suningWarennumPopupwindow.setC_price(c_price);
            suningWarennumPopupwindow.setSunigWareShow();
            suningWarennumPopupwindow.showAtLocation(llRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else if (suningWarennumPopupwindow != null
                && suningWarennumPopupwindow.isShowing()) {
            suningWarennumPopupwindow.dismiss();
        } else {
            suningWarennumPopupwindow.setNumber(ware_num);
            suningWarennumPopupwindow.setNavNumber();
            suningWarennumPopupwindow.setGoods_id(goods_id);
            suningWarennumPopupwindow.setArticle_id(article_id);
            suningWarennumPopupwindow.setGoods_title(goods_title);
            suningWarennumPopupwindow.setGoods_img(goods_img);
            suningWarennumPopupwindow.setGoods_price(goods_price);
            suningWarennumPopupwindow.setC_price(c_price);
            suningWarennumPopupwindow.setSunigWareShow();
            suningWarennumPopupwindow.showAtLocation(llRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }

        if (suningWarennumPopupwindow != null) {
            suningWarennumPopupwindow.setOnItemClickListenr(new SuningWarenumPopupwindow.OnItemClickListenr() {
                @Override
                public void onPopupBuy(int number) {
                    ware_num = number;
                    tv_vertical_standard_ware_num.setText("数量：" + ware_num + "件商品");
                    LogUtil.e(spec_ids + "-----" + "数量 = " + ware_num);
                    map = new HashMap<>();
                    map.put("channel_id", "7");
                    map.put("article_id", article_id);
                    map.put("goods_id", spec_ids);
                    map.put("quantity", String.valueOf(ware_num));
                    ware_json = new Gson().toJson(map);
                    LogUtil.e(ware_json);
                    suningWarennumPopupwindow.dismiss();
                    vShadow.setVisibility(View.GONE);
                }
            });
        }
    }


    /**
     * 获取省市区
     *
     * @return
     */
    public String getPcs() {
        return pcs;
    }

    /**
     * 获取商品数量
     *
     * @return
     */
    public int getWare_num() {
        return ware_num;
    }

    /**
     * 检查是否有货
     *
     * @return
     */
    public boolean checkIsToBuy() {
        if (isHave.equals("1")) {
            return true;
        }
        return false;
    }


    public String getBuyInfo() {
        return ware_json;
    }


    /**
     * 获取商品规格的数据
     */
    public void getSpecData() {
        if (netUtil == null) {
            netUtil = new NetUtil();
        }
        spec_ids = "";
        spec_titel = "";
        whick = "spec";
        map = new HashMap<>();
        map.put("goods_id", goods_id);
        netUtil.okHttp2Server2(spec_url, map);
    }


    /**
     * 查看大图的方法
     */
    private void seeBigImage() {
        Intent intent = new Intent(getContext(), HackySeeBigimgActivity.class);
        intent.putExtra("curr_position", vp_top_home.getCurrentItem());
        intent.putExtra("imgs", response);
        startActivity(intent);
    }

    /**
     * 解析联网请求回来的规格数据
     *
     * @param response
     */
    private void parseSpecData(String response) {
        SuningSpecBean suningSpecBean = new Gson().fromJson(response, SuningSpecBean.class);
        data = suningSpecBean.getData();
        if (data != null) {
            if (!TextUtils.isEmpty(isSpecFirst)) {
                showStandardPop(data);
                isSpecFirst = "";
            } else {
                setDefaultSpecShowAndData(data);
            }
        }
    }


    /**
     * 联网请求的回调
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(whick + "--" + response);
            if (whick.equals("spec")) {
                parseSpecData(response);
            } else if (whick.equals("isHaveGoods")) {
                parseIsHaveGoodsData(response);
            } else if (whick.equals("checkIsHave")) {
                parseCheckIshavaData(response);
            } else if (whick.equals("specids")) {
                parseSpecidData(response);
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
        }
    }

    /**
     * 解析产品限购的数据
     *
     * @param response
     */
    private void parseLimitData(String response) {
        LogUtil.e("限购 = " + response);
        try {
            SuningLimitBean suningLimitBean = new Gson().fromJson(response, SuningLimitBean.class);
            int statusCode = suningLimitBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    List<SuningLimitBean.DataBean> data = suningLimitBean.getData();
                    if (data != null && data.size() > 0) {
                        SuningLimitBean.DataBean dataBean = data.get(0);
                        limit_num = dataBean.getLimit_num();
                        if (limit_num <= 0) {
                            tv_good_detail_limit.setText("");
                            limit_num = 100000;
                            sale_num = 100000;
                        } else {
                            cancel_order_num = dataBean.getCancel_order_num();
                            purchased_num = dataBean.getPurchased_num();
                            sale_num = limit_num - purchased_num + cancel_order_num;
                            if (sale_num <= 0) {
                                rl_vertical_see_spec.setClickable(false);
                                rl_vertical_see_ware_num.setClickable(false);
                                if (onLimitNumListener != null) {
                                    onLimitNumListener.onLimitResult();
                                }
                                tv_good_detail_limit.setText("限购0件");
                            } else {
                                limit_num = sale_num;
                                tv_good_detail_limit.setText("限购" + limit_num + "件");
                            }
                        }
                    }
                    break;
                case 0:
                    Toast.makeText(context, suningLimitBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception e) {
            LogUtil.e("VerticalFragment1 parseLimitData error");
            Toast.makeText(context, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析一组产品规格请求返回的id
     *
     * @param response
     */
    private void parseSpecidData(String response) {
        LogUtil.e("产品规格返回的id = " + response);
        SuningSpecidBackBean suningSpecidBackBean = new Gson().fromJson(response, SuningSpecidBackBean.class);
        int statusCode = suningSpecidBackBean.getStatusCode();
        switch (statusCode) {
            case 1:
                List<SuningSpecidBackBean.DataBean> data = suningSpecidBackBean.getData();
                if (data != null && data.size() > 0) {
                    SuningSpecidBackBean.DataBean dataBean = data.get(0);
                    if (dataBean != null) {
                        String spec_back_id = dataBean.getGoods_specid();
                        if (spec_back_id == null || TextUtils.isEmpty(spec_back_id)) {
                            spec_back_id = "0";
                        }
                        netCartUtil.addCart(userLogin, "7", article_id, spec_back_id, String.valueOf(ware_num));
                    }
                } else {
                    Toast.makeText(context, "无法获取商品信息！", Toast.LENGTH_SHORT).show();
                }
                break;
            case 0:
                Toast.makeText(context, suningSpecidBackBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /**
     * 解析判断库存数据
     *
     * @param response
     */
    private void parseCheckIshavaData(String response) {
        LogUtil.e(response);
        try {
            SuningStockBean suningStockBean = new Gson().fromJson(response, SuningStockBean.class);
            int statusCode = suningStockBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    List<SuningStockBean.DataBean> data = suningStockBean.getData();
                    SuningStockBean.DataBean dataBean = data.get(0);
                    store_status = dataBean.getStore_status();
                    store_text = dataBean.getStore_text();
                    if (store_status == 0) {
                        if (what_buy == 1) {
                            String buyInfo = getBuyInfo();
                            LogUtil.e("buyInfo = " + buyInfo);
                            Intent intent = new Intent(context, OrderConfirmActivity.class);
                            intent.putExtra("ware_json", buyInfo);
                            intent.putExtra("goods_img", goods_img);
                            intent.putExtra("goods_title", goods_title);
                            intent.putExtra("goods_price", goods_price);
                            intent.putExtra("product_id", goods_id);
                            intent.putExtra("c_price", c_price);
                            intent.putExtra("spec_content", spec_titel);
                            startActivity(intent);
                        } else if (what_buy == 2) {
                            getSpecId();
                        }
                    } else {
                        Toast.makeText(context, store_text, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 0:
                    Toast.makeText(context, suningStockBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception e) {
            LogUtil.e("Vertical parseCheckIshavaData error");
            Toast.makeText(context, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析是否有库存
     *
     * @param response
     */
    private void parseIsHaveGoodsData(String response) {
        LogUtil.e(response);
        try {
            SuningStockBean suningStockBean = new Gson().fromJson(response, SuningStockBean.class);
            int statusCode = suningStockBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    List<SuningStockBean.DataBean> data = suningStockBean.getData();
                    if (data != null && data.size() > 0) {
                        SuningStockBean.DataBean dataBean = data.get(0);
                        store_status = dataBean.getStore_status();
                        store_text = dataBean.getStore_text();
                        tv_vertical_ishave_quarity.setText(store_text);
                        isHave = "1";
                    }
                    break;
                case 0:
                    tv_vertical_ishave_quarity.setText("获取库存失败");
                    isHave = "0";
                    break;
            }
        } catch (Exception e) {
            LogUtil.e("VerticalFragment1 parseIsHaveGoodsData error");
            Toast.makeText(context, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析商品信息数据
     *
     * @param response
     */
    private void parseWareData(String response) {
        try {
            SuningWareBean suningWareBean = new Gson().fromJson(response, SuningWareBean.class);
            int statusCode = suningWareBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    List<SuningWareBean.DataBean> data = suningWareBean.getData();
                    if (data != null && data.size() > 0) {
                        DecimalFormat df = new DecimalFormat("#0.00");
                        df.setRoundingMode(RoundingMode.DOWN);
                        SuningWareBean.DataBean dataBean = data.get(0);
                        if (dataBean != null) {
                            img_urls = dataBean.getUrls();
                            if (img_urls != null && img_urls.size() > 0) {
                                MyPagerAdapter myPagerAdapter = new MyPagerAdapter();
                                vp_top_home.setAdapter(myPagerAdapter);
                                addPoint();
                                sendHandler();
                                vp_top_home.addOnPageChangeListener(new MyOnPageChangeListener());
                            }
                            String title = dataBean.getTitle();
                            double sell_price = dataBean.getSell_price();
                            double market_price = dataBean.getMarket_price();
                            if (title != null) {
                                SpannableString span = new SpannableString("\t\t" + title);
                                ImageSpan image = new ImageSpan(context, R.drawable.suning_ziying_icon, DynamicDrawableSpan.ALIGN_BASELINE);
                                span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                tv_good_detail_name.setText(span);
                            }
                            tv_good_detail_goodprice.setText(String.valueOf(df.format(sell_price)));
                            tv_good_detail_cprice.getPaint().setAntiAlias(true);//抗锯齿
                            tv_good_detail_cprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰 
                            tv_good_detail_cprice.setText(String.valueOf(df.format(market_price)));
                            onLoadCompleteListenr.onLoadComplete();
                        }
                    }
                    break;
                case 0:

                    break;
            }
        } catch (Exception e) {
            LogUtil.e("VerticalFragment1 parseWareData error");
            Toast.makeText(context, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (myhandler != null) {
            myhandler.removeCallbacksAndMessages(null);
            myhandler = null;
        }
    }

    /**
     * 设置商品信息数据
     *
     * @param response
     */
    public void setResponse(String response) {
        this.response = response;
        LogUtil.e("商品信息 = " + response);
    }

    /**
     * 设置地址信息的数据
     *
     * @param address
     * @param lalotitude
     */
    public void setCurrentPostion(String address, String lalotitude) {
        this.address = address;
        this.lalotitude = lalotitude;
    }


    /**
     * 设置产品id
     *
     * @param goods_id
     */
    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    /**
     * 设置商品数据库自增id
     *
     * @param article_id
     */
    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    /**
     * 设置商品图片
     *
     * @param goods_img
     */
    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    /**
     * 设置商品标题
     *
     * @param goods_title
     */
    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    /**
     * 设置商品价格
     *
     * @param goods_price
     */
    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    /**
     * @return 获取popupwindow背景
     */
    public View getvShadow() {
        return vShadow;
    }

    /**
     * 返回何种购买方式
     *
     * @return
     */
    public int getWhat_buy() {
        return what_buy;
    }

    public void setWhat_buy(int what_buy) {
        this.what_buy = what_buy;
    }

    public void setIsSpecFirst(String isSpecFirst) {
        this.isSpecFirst = isSpecFirst;
    }

    /**
     * 加载完成的监听
     */
    private OnLoadCompleteListenr onLoadCompleteListenr;

    public interface OnLoadCompleteListenr {
        void onLoadComplete();
    }

    public void setOnLoadCompleteListenr(OnLoadCompleteListenr onLoadCompleteListenr) {
        this.onLoadCompleteListenr = onLoadCompleteListenr;
    }

    /**
     * 监听限购件数的监听
     */
    private OnLimitNumListener onLimitNumListener;

    public interface OnLimitNumListener {
        void onLimitResult();
    }

    public void setOnLimitNumListener(OnLimitNumListener onLimitNumListener) {
        this.onLimitNumListener = onLimitNumListener;
    }

    /**
     * 购物车添加数据相关
     */
    public class OnCartResponseListener implements NetCartUtil.OnCartResponseListener {

        @Override
        public void onCartResponse(String response, String whick) {
            LogUtil.e(whick + "=" + response);
            if (whick.equals(NetCartUtil.WHICH_ADD)) {
                parseAddCartData(response);
            }
        }

        @Override
        public void onCartFail() {
            Toast.makeText(context, "网络连接异常~", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析添加数据库数据
     *
     * @param response
     */
    private void parseAddCartData(String response) {

        NetGoodscartAddBean netGoodscartAddBean = new Gson().fromJson(response, NetGoodscartAddBean.class);
        int statusCode = netGoodscartAddBean.getStatusCode();
        switch (statusCode) {
            case 1:
                Toast.makeText(context, "添加购物车成功", Toast.LENGTH_SHORT).show();
                break;
            case 0:
                Toast.makeText(context, netGoodscartAddBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
