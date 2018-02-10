package com.gather_excellent_help.ui.activity.shop;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.PhotoUtils;
import com.gather_excellent_help.utils.ToastUtils;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
    @Bind(R.id.ll_shop_remind)
    LinearLayout llShopRemind;
    private String name;
    private String telephone;
    private String address;
    private String info;
    private String business_time;
    private String brand;
    private String which;//哪一个选择图片
    private boolean left_pic;//左边的图片是否设置
    private String upload1 = "";
    private String upload2 = "";
    private String upload3 = "";
    private String upload4 = "";
    private String merchant_url = Url.BASE_URL + "StoreApply.aspx";
    private NetUtil netUtil;
    private Map<String, String> map;

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");

    private Uri cropImageUri;

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
        LogUtil.e(name + "--" + telephone + "--" + address + "--" + info + "--" + business_time + "--" + brand);
    }

    public class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_merchant_choice1:
                    which = "l1";
                    autoObtainStoragePermission();
                    break;
                case R.id.tv_merchant_choice2:
                    if (!left_pic) {
                        which = "l2";
                    } else {
                        which = "l3";
                    }
                    autoObtainStoragePermission();
                    break;
                case R.id.tv_merchant_choice3:
                    which = "l4";
                    autoObtainStoragePermission();
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
        final String userLogin = Tools.getUserLogin(this);
        if (TextUtils.isEmpty(userLogin)) {
            toLogin();
            return;
        }
        if (TextUtils.isEmpty(upload1) || TextUtils.isEmpty(upload2) || TextUtils.isEmpty(upload3) || TextUtils.isEmpty(upload4)) {
            Toast.makeText(ShopPhotoUpdateActivity.this, "请上传图片！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name == null) {
            name = "";
        }
        if (telephone == null) {
            telephone = "";
        }
        if (address == null) {
            address = "";
        }
        if (info == null) {
            info = "";
        }
        if (business_time == null) {
            business_time = "";
        }
        if (brand == null) {
            brand = "";
        }
        llShopRemind.setVisibility(View.VISIBLE);
        new Thread() {
            public void run() {
                map = new HashMap<>();
                map.put("user_id", userLogin);
                map.put("name", name);
                map.put("telephone", telephone);
                map.put("address", address);
                map.put("info", info);
                map.put("business_time", business_time);
                map.put("bussiness", brand);
                map.put("file", upload1.trim());
                map.put("file2", upload2.trim());
                map.put("file3", upload3.trim());
                map.put("file4", upload4.trim());
                netUtil.okHttp2Server2(ShopPhotoUpdateActivity.this,merchant_url, map);
            }
        }.start();
    }

    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    ToastUtils.showShort(this, "请允许打开操作SDCard的权限！！");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.gather_excellent_help.fileprovider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri2(this, newUri, cropImageUri, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    try {
                        Bitmap bitmap =null;
                        if(cropImageUri!=null) {
                            bitmap = PhotoUtils.getBitmapFormUri(this, cropImageUri);
                        }
                        if (bitmap != null) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            String sphoto = Tools.BitmapToBase64(bitmap);
                            if (which.equals("l1")) {
                                if(ivMerchantPictureL1!=null) {
                                    ivMerchantPictureL1.setImageBitmap(bitmap);
                                }
                                upload1 = sphoto;
                            } else if (which.equals("l2")) {
                                if(ivMerchantPictureL2!=null) {
                                    ivMerchantPictureL2.setImageBitmap(bitmap);
                                }
                                left_pic = true;
                                upload2 = sphoto;
                            } else if (which.equals("l3")) {
                                if(ivMerchantPictureL3!=null) {
                                    ivMerchantPictureL3.setImageBitmap(bitmap);
                                }
                                upload3 = sphoto;
                            } else if (which.equals("l4")) {
                                if(ivMerchantPictureL4!=null) {
                                    ivMerchantPictureL4.setImageBitmap(bitmap);
                                }
                                upload4 = sphoto;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 保存裁剪之后的图片数据
     *
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
                upload4 = sphoto;
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

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            llShopRemind.setVisibility(View.GONE);
            pareMerchantData(response);
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "--" + e.getMessage());
            llShopRemind.setVisibility(View.GONE);
            EncryptNetUtil.startNeterrorPage(ShopPhotoUpdateActivity.this);
        }
    }

    /**
     * 解析商家入驻信息
     *
     * @param response
     */
    private void pareMerchantData(String response) {
        LogUtil.e(response);
        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
        int statusCode = codeStatueBean.getStatusCode();
        switch (statusCode) {
            case 1:
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
        EventBus.getDefault().post(new AnyEvent(EventType.EVENT_EXIT, "退出之前的界面"));
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
}
