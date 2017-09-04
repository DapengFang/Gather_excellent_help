package com.gather_excellent_help.ui.activity.shop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.AlipayManagerActivity;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.activity.credits.MerchantEnterActivity;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class ShopPhotoUpdateActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.rl_share)
    RelativeLayout rlShare;
    @Bind(R.id.tv_merchant_choice1)
    TextView tvMerchantChoice1;
    @Bind(R.id.iv_merchant_picture_l1)
    ImageView ivMerchantPictureL1;
    @Bind(R.id.tv_merchant_choice2)
    TextView tvMerchantChoice2;
    @Bind(R.id.iv_merchant_picture_l2)
    ImageView ivMerchantPictureL2;
    @Bind(R.id.iv_merchant_picture_l3)
    ImageView ivMerchantPictureL3;
    @Bind(R.id.tv_merchant_choice3)
    TextView tvMerchantChoice3;
    @Bind(R.id.iv_merchant_picture_l4)
    ImageView ivMerchantPictureL4;
    @Bind(R.id.tv_merchant_last_next)
    TextView tvMerchantLastNext;
    private String name;
    private String telephone;
    private String address;
    private String info;
    private String business_time;
    private String brand;
    public static final int SELECT_PIC_BY_PICK_PHOTO = 1;
    public static final int CROP_PIC_BY_PICK_PHOTO = 2;
    private String which;//哪一个选择图片
    private boolean left_pic;//左边的图片是否设置
    private String upload1 = "";
    private String upload2 = "";
    private String upload3 = "";
    private String upload4 = "";
    private String merchant_url = Url.BASE_URL + "StoreApply.aspx";
    private NetUtil netUtil;
    private Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_photo_update);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 解析数据
     */
    private void initData() {
        rlShare.setVisibility(View.GONE);
        tvTopTitleName.setText("店铺信息");
        getLastPageInfo();
        netUtil = new NetUtil();
        netUtil.setOnServerResponseListener(new OnServerResponseListener());
        rlExit.setOnClickListener(new MyOnclickListener());
        tvMerchantChoice1.setOnClickListener(new MyOnclickListener());
        tvMerchantChoice2.setOnClickListener(new MyOnclickListener());
        tvMerchantChoice3.setOnClickListener(new MyOnclickListener());
        tvMerchantLastNext.setOnClickListener(new MyOnclickListener());
    }

    /**
     * 获取上一级页面的数据
     */
    private void getLastPageInfo() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        telephone = intent.getStringExtra("telephone");
        address = intent.getStringExtra("address");
        info = intent.getStringExtra("info");
        business_time = intent.getStringExtra("business_time");
        brand = intent.getStringExtra("brand");
        LogUtil.e(name + "--" + telephone + "--" + address + "--" + info + "--" + business_time + "--" +brand);
    }

    public class MyOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit :
                    finish();
                    break;
                case R.id.tv_merchant_choice1:
                    which = "l1";
                    pickPhoto();
                    break;
                case R.id.tv_merchant_choice2:
                    if (!left_pic) {
                        which = "l2";
                    } else {
                        which = "l3";
                    }
                    pickPhoto();
                    break;
                case R.id.tv_merchant_choice3:
                    which = "l4";
                    pickPhoto();
                    break;
                case R.id.tv_merchant_last_next:
                    upLoadMerchantInfo();
                    break;
            }
        }
    }

    /**
     * 上传商户信息
     */
    private void upLoadMerchantInfo() {
        String userLogin = Tools.getUserLogin(this);
        if(TextUtils.isEmpty(userLogin)) {
            toLogin();
            return;
        }
        LogUtil.e(upload1);
        LogUtil.e(upload2);
        LogUtil.e(upload3);
        LogUtil.e(upload4);
        if(TextUtils.isEmpty(upload1) || TextUtils.isEmpty(upload2) || TextUtils.isEmpty(upload3) || TextUtils.isEmpty(upload4)) {
            Toast.makeText(ShopPhotoUpdateActivity.this, "请上传图片！", Toast.LENGTH_SHORT).show();
            return;
        }

        map = new HashMap<>();
        map.put("user_id", userLogin);
        map.put("name", name);
        map.put("telephone", telephone);
        map.put("address", address);
        map.put("info", info);
        map.put("business_time", business_time);
        map.put("business", brand);
        map.put("file",upload1.trim());
        map.put("file2",upload2.trim());
        map.put("file3",upload3.trim());
        map.put("file4",upload4.trim());
        netUtil.okHttp2Server2(merchant_url,map);
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, CROP_PIC_BY_PICK_PHOTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            //doPhoto(requestCode, data);
            switch (requestCode) {
                // 如果是直接从相册获取
                case CROP_PIC_BY_PICK_PHOTO:
                    if (which.equals("l2") || which.equals("l3")) {
                        startPhotoZoom(data.getData());
                    } else {
                        startPhotoZoom2(data.getData());
                    }
                    break;
                // 如果是调用相机拍照时
                // 取得裁剪后的图片
                case SELECT_PIC_BY_PICK_PHOTO:
                    /**
                     * 非空判断大家一定要验证，如果不验证的话，
                     * 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                     * 当前功能时，会报NullException，小马只
                     * 在这个地方加下，大家可以根据不同情况在合适的
                     * 地方做判断处理类似情况
                     *
                     */
                    if(data != null){
                        setPicToView(data);
                    }
                    break;
                default:
                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,
		 * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么
		 * 制做的了...吼吼
		 */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }
    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom2(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,
		 * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么
		 * 制做的了...吼吼
		 */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            /**
             * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上
             * 传到服务器，QQ头像上传采用的方法跟这个类似
             */
            String sphoto = Tools.BitmapToBase64(photo);
            if (which.equals("l1")) {
                ivMerchantPictureL1.setImageBitmap(photo);
                upload1 = sphoto;
            } else if (which.equals("l2")) {
                ivMerchantPictureL2.setImageBitmap(photo);
                left_pic = true;
                upload2 = sphoto;
            } else if (which.equals("l3")) {
                ivMerchantPictureL3.setImageBitmap(photo);
                upload3 = sphoto;
            } else if (which.equals("l4")) {
                ivMerchantPictureL4.setImageBitmap(photo);
                upload4 =sphoto;
            }
        }
    }

    /**
     * 登录
     */
    private void toLogin() {
        Intent intent = new Intent(ShopPhotoUpdateActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener{

        @Override
        public void getSuccessResponse(String response) {
          pareMerchantData(response);
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
           LogUtil.e(call.toString() + "--" +e.getMessage());
        }
    }

    /**
     * 解析商家入驻信息
     * @param response
     */
    private void pareMerchantData(String response) {
        LogUtil.e(response);
        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
        int statusCode = codeStatueBean.getStatusCode();
        switch (statusCode) {
            case 1 :
                toAlipay();
                break;
            case 0:
                Toast.makeText(ShopPhotoUpdateActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 区支付宝支付页面
     */
    private void toAlipay() {
        Intent intent = new Intent(ShopPhotoUpdateActivity.this, AlipayManagerActivity.class);
        startActivity(intent);
        finish();
        EventBus.getDefault().post(new AnyEvent(EventType.EVENT_EXIT,"退出之前的界面"));
    }
}
