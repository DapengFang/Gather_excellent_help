package com.gather_excellent_help.ui.activity.credits;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.ShopDetailBean;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.EditTextPopupwindow;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.PhotoUtils;
import com.gather_excellent_help.utils.ToastUtils;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 *
 */
public class ShopDetailActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.iv_shop_carame)
    ImageView ivShopCarame;
    @Bind(R.id.tv_shop_selector)
    TextView tvShopSelector;
    @Bind(R.id.iv_shop_picture)
    ImageView ivShopPicture;
    @Bind(R.id.et_shop_detail_tel)
    EditText etShopDetailTel;
    @Bind(R.id.et_shop_detail_yewu)
    EditText etShopDetailYewu;
    @Bind(R.id.ll_shop_remind)
    LinearLayout llShopRemind;
    //商家信息展示
    private String see_shop_url = Url.BASE_URL + "SellerShow.aspx";
    //商家信息修改
    private String update_shop_url = Url.BASE_URL + "UpdateSeller.aspx";

    private String seller_id;//商家ID
    private String name;//店铺名字
    private String telephone = "";//固定电话
    private String address = "";//详细地址
    private String info = "";//详细介绍

    private NetUtil netUtil;
    private Map<String, String> map;
    private String upload = "";

    private int which = 0;//区分展示还是修改

    /***
     * 使用相册中的图片
     */
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;


    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");

    private Uri cropImageUri;
    @Bind(R.id.tv_shop_time_am)
    TextView tvShopTimeAm;
    @Bind(R.id.tv_shop_time_pm)
    TextView tvShopTimePm;
    @Bind(R.id.rl_shop_detail_text)
    RelativeLayout rlShopDetailText;
    @Bind(R.id.rl_shop_detail_address)
    RelativeLayout rlShopDetailAddress;
    @Bind(R.id.tv_shop_detail_introduction)
    TextView tvShopDetailIntroduction;
    @Bind(R.id.tv_shop_detail_address)
    TextView tvShopDetailAddress;
    @Bind(R.id.tv_shop_detail_confirm)
    TextView tvShopDetailConfirm;
    @Bind(R.id.et_shop_detail_name)
    EditText etShopDetailName;

    /***
     * 从Intent获取图片路径的KEY
     */
    private String startTime = "6:00";
    private String endTime = "18:00";
    private String shopName;
    private String shopYewu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    public void initData() {
        netUtil = new NetUtil();
        which = 0;
        String loginId = Tools.getUserLogin(this);
        if (TextUtils.isEmpty(loginId)) {
            toLogin();
            return;
        }
        map = new HashMap<>();
        map.put("seller_id", loginId);
        netUtil.okHttp2Server2(see_shop_url, map);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                llShopRemind.setVisibility(View.GONE);
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1:
                        if (which == 0) {
                            ShopDetailBean shopDetailBean = new Gson().fromJson(response, ShopDetailBean.class);
                            List<ShopDetailBean.DataBean> data = shopDetailBean.getData();
                            if (data.size() > 0) {
                                ShopDetailBean.DataBean dataBean = shopDetailBean.getData().get(0);
                                if (dataBean.getTelephone() != null) {
                                    etShopDetailTel.setText(dataBean.getTelephone());
                                    telephone = dataBean.getTelephone();
                                }
                                if (dataBean.getBusiness() != null) {
                                    etShopDetailYewu.setText(dataBean.getBusiness());
                                    shopYewu = dataBean.getBusiness();
                                }
                                if (dataBean.getInfo() != null) {
                                    tvShopDetailIntroduction.setText(dataBean.getInfo());
                                    info = dataBean.getInfo();
                                }
                                if (dataBean.getAddress() != null) {
                                    tvShopDetailAddress.setText(dataBean.getAddress());
                                    address = dataBean.getAddress();
                                }
                                if (dataBean.getName() != null) {
                                    etShopDetailName.setText(dataBean.getName());
                                    shopName = dataBean.getName();
                                }
                                if (dataBean.getStarBusinessTime() != null) {
                                    String starBusinessTime = dataBean.getStarBusinessTime();
                                    tvShopTimeAm.setText("早上" + starBusinessTime);
                                    startTime = starBusinessTime;
                                }
                                if (dataBean.getEndBusinessTime() != null) {
                                    String endBusinessTime = dataBean.getEndBusinessTime();
                                    tvShopTimePm.setText("晚上" + endBusinessTime);
                                    endTime = endBusinessTime;
                                }
                                String store_url = dataBean.getStore_url();
                                if (store_url != null) {
                                    if (!store_url.contains("http")) {
                                        store_url = Url.IMG_URL + store_url;
                                    }
                                }
                                Glide.with(ShopDetailActivity.this).load(store_url)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                                        .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                                        .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                                        .into(ivShopPicture);//请求成功后把图片设置到的控件
                            }
                        } else {
                            Toast.makeText(ShopDetailActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                            which = 0;
                            initData();
                        }
                        break;
                    case 0:
                        Toast.makeText(ShopDetailActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        llShopRemind.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "--" + e.getMessage());
            }
        });
        tvTopTitleName.setText("商家信息");
        rlExit.setOnClickListener(new MyOnClickListener());
        tvShopSelector.setOnClickListener(new MyOnClickListener());
        ivShopCarame.setOnClickListener(new MyOnClickListener());
        tvShopTimeAm.setOnClickListener(new MyOnClickListener());
        tvShopTimePm.setOnClickListener(new MyOnClickListener());
        rlShopDetailText.setOnClickListener(new MyOnClickListener());
        rlShopDetailAddress.setOnClickListener(new MyOnClickListener());
        tvShopDetailConfirm.setOnClickListener(new MyOnClickListener());
    }

    /**
     * 登录
     */
    private void toLogin() {
        Intent intent = new Intent(ShopDetailActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 监听点击事件的类
     */
    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_shop_selector:
                    pickPhoto();
                    break;
                case R.id.iv_shop_carame:
                    break;
                case R.id.tv_shop_time_am:
                    showTimePicker(R.id.tv_shop_time_am);
                    break;
                case R.id.tv_shop_time_pm:
                    showTimePicker(R.id.tv_shop_time_pm);
                    break;
                case R.id.rl_shop_detail_text:
                    showDetailIntroduct();
                    break;
                case R.id.rl_shop_detail_address:
                    showDetailAddress();
                    break;
                case R.id.tv_shop_detail_confirm:
                    confirmShopInfo();
                    break;
            }
        }
    }

    /**
     * 保存商家信息
     */
    private void confirmShopInfo() {
        final String loginId = Tools.getUserLogin(this);
        telephone = etShopDetailTel.getText().toString().trim();
        shopName = etShopDetailName.getText().toString().trim();
        shopYewu = etShopDetailYewu.getText().toString().trim();
        info = tvShopDetailIntroduction.getText().toString().trim();
        address = tvShopDetailAddress.getText().toString().trim();
        which = 1;
        if (upload == null) {
            upload = "";
        }
        llShopRemind.setVisibility(View.VISIBLE);
        new Thread(){
            public void run(){
                HashMap<String, String> map = new HashMap<>();
                map.put("seller_id", loginId);
                map.put("name", shopName);
                map.put("telephone", telephone);
                map.put("address", address);
                map.put("info", info);
                map.put("business_time", startTime + "a" + endTime);
                map.put("bussiness", shopYewu);
                map.put("file", upload.trim());
                netUtil.okHttp2Server2(update_shop_url, map);
            }
        }.start();

    }

    /**
     * 编辑地址内容的dialog
     */
    private void showDetailAddress() {
        EditTextPopupwindow popupwindow = new EditTextPopupwindow(this, tvShopDetailAddress);
        popupwindow.showAtLocation(rlShopDetailAddress, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 编辑介绍内容的dialog
     */
    private void showDetailIntroduct() {
        EditTextPopupwindow popupwindow = new EditTextPopupwindow(this, tvShopDetailIntroduction);
        popupwindow.showAtLocation(rlShopDetailAddress, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 选择时间
     *
     * @param id
     */
    private void showTimePicker(final int id) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                if (id == R.id.tv_shop_time_am) {
                    if (minute < 10) {
                        tvShopTimeAm.setText("早上" + hour + ":0" + minute);
                        startTime = hour + ":0" + minute;
                    } else {
                        tvShopTimeAm.setText("早上" + hour + ":" + minute);
                        startTime = hour + ":" + minute;
                    }
                } else {
                    if (minute < 10) {
                        tvShopTimePm.setText("晚上" + hour + ":0" + minute);
                        endTime = hour + ":0" + minute;
                    } else {
                        tvShopTimePm.setText("晚上" + hour + ":" + minute);
                        endTime = hour + ":0" + minute;
                    }
                }
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        autoObtainStoragePermission();
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
                    ToastUtils.showShort(this, "请允许打开操作SDCard权限！！");
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
                    Bitmap bitmap = null;
                    try {
                        bitmap = PhotoUtils.getBitmapFormUri(this, cropImageUri);
                        if (bitmap != null) {
                            Glide.clear(ivShopPicture);
                            ivShopPicture.setImageBitmap(bitmap);
                            String sphoto = Tools.BitmapToBase64(bitmap);
                            upload = sphoto;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
}
