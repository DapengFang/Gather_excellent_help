package com.gather_excellent_help.ui.activity.credits;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.ShopDetailBean;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.EditTextPopupwindow;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.DataCleanManager;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.imageutils.ImageLoader;
import com.google.gson.Gson;

import java.io.File;
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
    private ImageLoader mImageLoader;

    private int which = 0;//区分展示还是修改


    /***
     * 使用照相机拍照获取图片
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /***
     * 使用相册中的图片
     */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
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

    private Uri photoUri;
    /**
     * 获取到的图片路径
     */
    private String picPath = "";


    /***
     * 从Intent获取图片路径的KEY
     */
    public static final String KEY_PHOTO_PATH = "photo_path";

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
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        which = 0;
        String loginId = Tools.getUserLogin(this);
        map = new HashMap<>();
        map.put("seller_id", loginId);
        netUtil.okHttp2Server2(see_shop_url, map);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1:
                        if (which == 0) {
                            ShopDetailBean shopDetailBean = new Gson().fromJson(response, ShopDetailBean.class);
                            List<ShopDetailBean.DataBean> data = shopDetailBean.getData();
                            if(data.size()>0) {
                                ShopDetailBean.DataBean dataBean = shopDetailBean.getData().get(0);
                                etShopDetailTel.setText(dataBean.getTelephone());
                                etShopDetailYewu.setText(dataBean.getName());
                                tvShopDetailIntroduction.setText(dataBean.getInfo());
                                tvShopDetailAddress.setText(dataBean.getAddress());
                                mImageLoader.loadImage(Url.IMG_URL + dataBean.getStore_url(),ivShopPicture,true);
                            }
                        }else{
                             Toast.makeText(ShopDetailActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                             which = 0;
                             initData();
                        }
                        break;
                    case 0:
                        Toast.makeText(ShopDetailActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
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
                    takePhoto();
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
        String loginId = Tools.getUserLogin(this);
        telephone = etShopDetailTel.getText().toString().trim();
        info = tvShopDetailIntroduction.getText().toString().trim();
        address = tvShopDetailAddress.getText().toString().trim();
        which = 1;
        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", loginId);
        map.put("name", "");
        map.put("telephone", telephone);
        map.put("address", address);
        map.put("info", info);
        File file = new File(picPath);
        netUtil.okHttp2Server3(update_shop_url, map,file);
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
                    } else {
                        tvShopTimeAm.setText("早上" + hour + ":" + minute);
                    }
                } else {
                    if (minute < 10) {
                        tvShopTimePm.setText("晚上" + hour + ":0" + minute);
                    } else {
                        tvShopTimePm.setText("晚上" + hour + ":" + minute);
                    }
                }
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            /**-----------------*/
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            doPhoto(requestCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        }
        String[] pojo = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
            cursor.close();
        }
        LogUtil.e("imagePath = " + picPath);
        if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
            Bitmap bm = BitmapFactory.decodeFile(picPath);
            ivShopPicture.setImageBitmap(bm);

        } else {
            Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
