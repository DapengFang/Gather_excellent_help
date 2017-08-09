package com.gather_excellent_help.ui.activity.credits;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.api.pay.PayResult;
import com.gather_excellent_help.bean.BrandBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.ui.activity.AlipayManagerActivity;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.adapter.BrandListAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.MyGridView;
import com.gather_excellent_help.utils.Base64Coder;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class MerchantEnterActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.gv_merchant_brand)
    MyGridView gvMerchantBrand;
    @Bind(R.id.tv_merchant_last_next)
    TextView tvMerchantLastNext;
    @Bind(R.id.iv_merchant_brand_arraw)
    ImageView ivMerchantBrandArraw;
    @Bind(R.id.tv_merchant_choice1)
    TextView tvMerchantChoice1;
    @Bind(R.id.tv_merchant_choice2)
    TextView tvMerchantChoice2;
    @Bind(R.id.tv_merchant_choice3)
    TextView tvMerchantChoice3;
    @Bind(R.id.iv_merchant_picture_l1)
    ImageView ivMerchantPictureL1;
    @Bind(R.id.iv_merchant_picture_l2)
    ImageView ivMerchantPictureL2;
    @Bind(R.id.iv_merchant_picture_l3)
    ImageView ivMerchantPictureL3;
    @Bind(R.id.iv_merchant_picture_l4)
    ImageView ivMerchantPictureL4;
    @Bind(R.id.tv_merchant_brand_cannel)
    TextView tvMerchantBrandCannel;
    @Bind(R.id.tv_merchant_brand_commit)
    TextView tvMerchantBrandCommit;
    @Bind(R.id.ll_merchant_brand_expand)
    LinearLayout llMerchantBrandExpand;
    @Bind(R.id.et_merchant_address)
    EditText etMerchantAddress;
    @Bind(R.id.et_merchant_shopinfo)
    EditText etMerchantShopinfo;
    @Bind(R.id.et_merchant_phone)
    EditText etMerchantPhone;
    @Bind(R.id.et_merchant_shopname)
    EditText etMerchantShopname;
    @Bind(R.id.tv_shop_time_am)
    TextView tvShopTimeAm;
    @Bind(R.id.tv_shop_time_pm)
    TextView tvShopTimePm;

    private NetUtil netUtil;
    private Map<String, String> map;
    private String brand_url = Url.BASE_URL + "goodsBrand.aspx";
    private String merchant_url = Url.BASE_URL + "StoreApply.aspx";

    /***
     * 使用相册中的图片
     */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 1;
    public static final int CROP_PIC_BY_PICK_PHOTO = 2;
    private Uri photoUri;
    private String picPath;
    private String which;//哪一个选择图片
    private boolean left_pic;//左边的图片是否设置
    private String picpath1 = "";
    private String picpath2 = "";
    private String picpath3 = "";
    private String picpath4 = "";
    private String where = "";
    private ArrayList<BrandBean.DataBean> selectlists = new ArrayList<>();
    private BrandListAdapter brandListAdapter;
    private List<BrandBean.DataBean> brandData;//加载的数据
    private List<BrandBean.DataBean> newDatas;//获取的数据

    private boolean isExpand;
    private String brand = "";
    private String name = "";
    private String telephone = "";
    private String address = "";
    private String info = "";
    private String business_time = "9:00a18:00";
    private String business = "";
    private String startTime = "";
    private String endTime = "";
    private String upload1 = "";
    private String upload2 = "";
    private String upload3 = "";
    private String upload4 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_enter);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        ivMerchantBrandArraw.setImageResource(R.drawable.left_black_arraw);
        netUtil = new NetUtil();
        where = "";
        netUtil.okHttp2Server2(brand_url, null);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                if (TextUtils.isEmpty(where)) {
                    parseData(response);
                }else{
                    pareMerchantData(response);
                }
            }
            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "--" + e.getMessage());
            }
        });
        gvMerchantBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view.findViewById(R.id.tv_brand_list_title);
                CardView cv = (CardView) view.findViewById(R.id.cv_brand_list);
                BrandBean.DataBean dataBean = brandData.get(i);
                dataBean.setSelect(!dataBean.isSelect());
                boolean select = dataBean.isSelect();
                if (select) {
                    tv.setTextColor(Color.WHITE);
                    cv.setCardBackgroundColor(Color.RED);
                    selectlists.add(dataBean);
                } else {
                    tv.setTextColor(Color.parseColor("#55000000"));
                    cv.setCardBackgroundColor(Color.parseColor("#11000000"));
                    if (selectlists.contains(dataBean)) {
                        selectlists.remove(dataBean);
                    }
                }
            }
        });
        tvTopTitleName.setText("申请");
        rlExit.setOnClickListener(new MyOnClickListener());
        tvMerchantLastNext.setOnClickListener(new MyOnClickListener());
        tvMerchantChoice1.setOnClickListener(new MyOnClickListener());
        tvMerchantChoice2.setOnClickListener(new MyOnClickListener());
        tvMerchantChoice3.setOnClickListener(new MyOnClickListener());
        tvMerchantBrandCannel.setOnClickListener(new MyOnClickListener());
        tvMerchantBrandCommit.setOnClickListener(new MyOnClickListener());
        llMerchantBrandExpand.setOnClickListener(new MyOnClickListener());
        tvShopTimeAm.setOnClickListener(new MyOnClickListener());
        tvShopTimePm.setOnClickListener(new MyOnClickListener());
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
                Toast.makeText(MerchantEnterActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 区支付宝支付页面
     */
    private void toAlipay() {
        Intent intent = new Intent(MerchantEnterActivity.this, AlipayManagerActivity.class);
        startActivity(intent);
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

    /**
     * 解析品牌数据
     *
     * @param response
     */
    private void parseData(String response) {
        BrandBean brandBean = new Gson().fromJson(response, BrandBean.class);
        newDatas = brandBean.getData();
        if (newDatas != null && newDatas.size() > 4) {
            brandData = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                brandData.add(newDatas.get(i));
            }
            brandListAdapter = new BrandListAdapter(this, brandData);
            gvMerchantBrand.setAdapter(brandListAdapter);
        }
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
                case R.id.tv_merchant_last_next:
                    upLoadMerchantInfo();
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
                case R.id.tv_merchant_brand_cannel:
                    setInitBrand();
                    break;
                case R.id.tv_merchant_brand_commit:
                    brand = "";
                    if (selectlists.size() > 0) {
                        for (int i = 0; i < selectlists.size(); i++) {
                            brand += selectlists.get(i).getTitle() + "/";
                        }
                        brand = brand.substring(0, brand.length() - 1);
                    }
                    for (int i = 0; i < brandData.size(); i++) {
                        BrandBean.DataBean dataBean = brandData.get(i);
                        dataBean.setSelect(false);
                    }
                    setInitBrand();
                    Toast.makeText(MerchantEnterActivity.this, "主营品牌选择成功！", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.ll_merchant_brand_expand:
                    isExpand = !isExpand;
                    if (!isExpand) {
                        brandData = new ArrayList<>();
                        for (int i = 0; i < 8; i++) {
                            brandData.add(newDatas.get(i));
                        }
                        ivMerchantBrandArraw.setImageResource(R.drawable.left_black_arraw);
                    } else {
                        brandData = newDatas;
                        ivMerchantBrandArraw.setImageResource(R.drawable.down_black_arraw);
                    }
                    if (gvMerchantBrand != null) {
                        brandListAdapter = new BrandListAdapter(MerchantEnterActivity.this, brandData);
                        gvMerchantBrand.setAdapter(brandListAdapter);
                    }
                    break;
                case R.id.tv_shop_time_am:
                    showTimePicker(R.id.tv_shop_time_am);
                    break;
                case R.id.tv_shop_time_pm:
                    showTimePicker(R.id.tv_shop_time_pm);
                    break;
            }
        }
    }

    private void setInitBrand() {
        isExpand = false;
        ivMerchantBrandArraw.setImageResource(R.drawable.left_black_arraw);
        for (int i = 0; i < brandData.size(); i++) {
            BrandBean.DataBean dataBean = brandData.get(i);
            dataBean.setSelect(false);
        }
        ArrayList<BrandBean.DataBean> commitData = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            commitData.add(newDatas.get(i));
        }
        brandData = commitData;
        if (gvMerchantBrand != null) {
            brandListAdapter = new BrandListAdapter(MerchantEnterActivity.this, brandData);
            gvMerchantBrand.setAdapter(brandListAdapter);
        }
        selectlists.clear();
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
        name = etMerchantShopname.getText().toString().trim();
        telephone = etMerchantPhone.getText().toString().trim();
        address = etMerchantAddress.getText().toString().trim();
        info = etMerchantShopinfo.getText().toString().trim();
        if(TextUtils.isEmpty(name)) {
            Toast.makeText(MerchantEnterActivity.this, "请输入店名！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(telephone)) {
            Toast.makeText(MerchantEnterActivity.this, "请输入电话！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(address)) {
            Toast.makeText(MerchantEnterActivity.this, "请输入地址！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(info)) {
            Toast.makeText(MerchantEnterActivity.this, "请输入店铺简介！", Toast.LENGTH_SHORT).show();
            return;
        }
        LogUtil.e(upload1);
        LogUtil.e(upload2);
        LogUtil.e(upload3);
        LogUtil.e(upload4);
        if(TextUtils.isEmpty(upload1) || TextUtils.isEmpty(upload2) || TextUtils.isEmpty(upload3) || TextUtils.isEmpty(upload4)) {
            Toast.makeText(MerchantEnterActivity.this, "请上传图片！", Toast.LENGTH_SHORT).show();
            return;
        }
        String pics = upload1.trim()+","+upload2.trim()+","+upload3.trim()+","+upload4.trim();

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
//        if (!TextUtils.isEmpty(picpath1) && !TextUtils.isEmpty(picpath2)
//                && !TextUtils.isEmpty(picpath3) && !TextUtils.isEmpty(picpath4)) {
//            File file1 = new File(picpath1);
//            File file2 = new File(picpath2);
//            File file3 = new File(picpath3);
//            File file4 = new File(picpath4);
//            File[] files = {file1, file2, file3, file4};
//            where = "upload_file";
//            netUtil.okHttp2Server4(merchant_url, map, files);
//        } else {
//            Toast.makeText(MerchantEnterActivity.this, "请选择图片！", Toast.LENGTH_SHORT).show();
//        }
         where = "upload_file";
         netUtil.okHttp2Server2(merchant_url,map);
    }

    /**
     * 登录
     */
    private void toLogin() {
        Intent intent = new Intent(MerchantEnterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
//        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(openAlbumIntent, CROP_PIC_BY_PICK_PHOTO);

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
                    startPhotoZoom(data.getData());
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
     * 选择图片后，获取图片的路径
     *
     * @param requestCode
     * @param data
     */
    private void doPhoto(int requestCode, Intent data) {
        if (requestCode == SELECT_PIC_BY_PICK_PHOTO)  //从相册取图片，有些手机有异常情况，请注意
        {
            if (data == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
            photoUri = data.getData();
            if (photoUri == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }

            String[] pojo = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(photoUri, pojo, null, null,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    picPath = cursor.getString(columnIndex);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            LogUtil.e("imagePath = " + picPath);
            if (picPath != null) {
                BitmapFactory.Options opts1 = new BitmapFactory.Options();
                opts1.inTempStorage = new byte[100 * 1024];
                opts1.inPreferredConfig = Bitmap.Config.ARGB_8888;
                opts1.inPurgeable = true;
                opts1.inInputShareable = true;
                Bitmap bm = BitmapFactory.decodeFile(picPath, opts1);
                if (which.equals("l1")) {
                    ivMerchantPictureL1.setImageBitmap(bm);
                    picpath1 = picPath;
                } else if (which.equals("l2")) {
                    ivMerchantPictureL2.setImageBitmap(bm);
                    left_pic = true;
                    picpath2 = picPath;
                } else if (which.equals("l3")) {
                    ivMerchantPictureL3.setImageBitmap(bm);
                    picpath3 = picPath;
                } else if (which.equals("l4")) {
                    ivMerchantPictureL4.setImageBitmap(bm);
                    picpath4 = picPath;
                }

            } else {
                Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == CROP_PIC_BY_PICK_PHOTO) {
            if (data == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
            photoUri = data.getData();
            if (photoUri == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
            LogUtil.e(which);
            if (which.equals("l2") || which.equals("l3")) {
                cropImage2(photoUri, 280, 280, SELECT_PIC_BY_PICK_PHOTO);
            } else {
                cropImage(photoUri, 280, 140, SELECT_PIC_BY_PICK_PHOTO);
            }
        }

    }

    //截取图片
    public void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
        //裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String url = getPath(this, uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        intent.putExtra("crop", "true");
        //裁剪框的比例，1：1
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
        //裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        //图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, requestCode);
    }

    //截取图片
    public void cropImage2(Uri uri, int outputX, int outputY, int requestCode) {
        //裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String url = getPath(this, uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        intent.putExtra("crop", "true");
        //裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        //图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, requestCode);
    }

    //以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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
//			ByteArrayOutputStream stream = new ByteArrayOutputStream();
//			photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
//			byte[] b = stream.toByteArray();
//			// 将图片流以字符串形式存储下来
//
//            String sphoto = new String(Base64Coder.encodeLines(b));
//			//这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了，
//			//服务器处理的方法是服务器那边的事了，吼吼
//
//			//如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换
//			//为我们可以用的图片类型就OK啦...吼吼
//			Bitmap dBitmap = BitmapFactory.decodeFile(tp);
//			Drawable drawable = new BitmapDrawable(dBitmap);
//            ib.setBackgroundDrawable(drawable);
//            iv.setBackgroundDrawable(drawable);
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

}


