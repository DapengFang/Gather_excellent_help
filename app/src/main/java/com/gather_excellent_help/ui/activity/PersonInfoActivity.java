package com.gather_excellent_help.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.UserAvatarBean;
import com.gather_excellent_help.bean.UserinfoBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.CircularImage;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.PhotoUtils;
import com.gather_excellent_help.utils.ToastUtils;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.imageutils.ImageLoader;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class PersonInfoActivity extends BaseActivity {

    @Bind(R.id.rl_person_exit)
    RelativeLayout rlPersonExit;
    @Bind(R.id.tv_person_infoset_save)
    TextView tvPersonInfosetSave;
    @Bind(R.id.civ_person_head)
    CircularImage civPersonHead;
    @Bind(R.id.tv_person_phone)
    TextView tvPersonPhone;
    @Bind(R.id.tv_person_nick)
    TextView tvPersonNick;
    @Bind(R.id.tv_person_sex)
    TextView tvPersonSex;
    @Bind(R.id.rl_person_sex)
    RelativeLayout rlPersonSex;
    @Bind(R.id.usertype)
    TextView usertype;
    @Bind(R.id.rl_person_usertype)
    RelativeLayout rlPersonUsertype;
    @Bind(R.id.iv_right2)
    ImageView ivRight2;
    @Bind(R.id.rl_person_address)
    RelativeLayout rlPersonAddress;

    private NetUtil netUtils;
    private Map<String,String> map;
    private String url = Url.BASE_URL + "UserInfo.aspx";
    private String head_url = Url.BASE_URL + "ChangeFace.aspx";
    private String userLogin;//用户登录后的标识

    public static final int TAKE_PICTURE = 0;
    public static final int CHOOSE_PICTURE = 1;
    public static final int DO_PICTURE = 2;
    private String whick;
    private File tempfile;

    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        netUtils = new NetUtil();
        //mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        getUserInfo();
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                if(whick.equals("info")) {
                    parseData(response);
                }else if(whick.equals("head")) {
                    parseHead(response);
                }

            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString()+","+e.getMessage());
            }
        });
        rlPersonExit.setOnClickListener(new MyOnclickListener());
        civPersonHead.setOnClickListener(new MyOnclickListener());
    }

    /**
     * 解析
     * @param response
     */
    private void parseHead(String response) {
        LogUtil.e(response);
        UserAvatarBean userAvatarBean = new Gson().fromJson(response, UserAvatarBean.class);
        int statusCode = userAvatarBean.getStatusCode();
        switch (statusCode){
            case 1:
                List<UserAvatarBean.DataBean> data = userAvatarBean.getData();
                if(data!=null) {
                    if(data.size()>0) {
                        UserAvatarBean.DataBean dataBean = data.get(0);
                        if(dataBean!=null) {
                            String avatar = dataBean.getAvatar();
                            if(avatar!=null && !TextUtils.isEmpty(avatar)) {
                                if(!avatar.contains("http:")) {
                                    avatar = Url.IMG_URL + avatar;
                                }
                                Glide.with(this).load(avatar)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                                        .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                                        .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                                        .into(civPersonHead);//请求成功后把图片设置到的控件
                                EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN,"登录成功！"));
                            }
                        }
                    }
                }
                break;
            case 0:
                Toast.makeText(PersonInfoActivity.this, userAvatarBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class MyOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case  R.id.rl_person_exit:
                    finish();
                    break;
                case R.id.civ_person_head:
                     updateUserHead();
                     break;
            }
        }
    }
    private static final int output_X = 480;
    private static final int output_Y = 480;

    /**
     * 修改用户头像
     */
    private void updateUserHead() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择图片");
        builder.setItems(new String[]{"拍照","相册"}, new DialogInterface.OnClickListener() {
            @SuppressLint("WorldWriteableFiles")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                switch (which) {
                   case TAKE_PICTURE:
                       autoObtainCameraPermission();
                       break;
                   case CHOOSE_PICTURE:
                       autoObtainStoragePermission();
                       break;
               }
            }
        });
        builder.create().show();
    }


    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CHOOSE_PICTURE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(PersonInfoActivity.this, "com.gather_excellent_help.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, TAKE_PICTURE);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                } else {

                    ToastUtils.showShort(this, "请允许打开相机！！");
                }
                break;
            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CHOOSE_PICTURE);
                } else {

                    ToastUtils.showShort(this, "请允许打开操作SDCard权限！！");
                }
                break;
        }
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtils.showShort(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(PersonInfoActivity.this, "com.gather_excellent_help.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtils.takePicture(this, imageUri, TAKE_PICTURE);
            } else {
                ToastUtils.showShort(this, "设备没有SD卡！");
            }
        }
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
            civPersonHead.setImageDrawable(drawable);
            String upload = Tools.BitmapToBase64(photo);
            LogUtil.e(upload);
            if(!TextUtils.isEmpty(upload)) {
                map = new HashMap<>();
                map.put("id",userLogin);
                map.put("file",upload);
                whick = "head";
                netUtils.okHttp2Server2(head_url,map);
            }
        }
    }

    private static final int CODE_RESULT_REQUEST = 0xa2;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 如果是直接从相册获取
                case CHOOSE_PICTURE:
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.gather_excellent_help.fileprovider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                    break;
                // 如果是调用相机拍照时
                case TAKE_PICTURE:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        Drawable drawable = new BitmapDrawable(bitmap);
                        Glide.clear(civPersonHead);
                        civPersonHead.setImageDrawable(drawable);
                        String upload = Tools.BitmapToBase64(bitmap);
                        LogUtil.e(upload);
                        if(!TextUtils.isEmpty(upload)) {
                            map = new HashMap<>();
                            map.put("id",userLogin);
                            map.put("file",upload);
                            whick = "head";
                            netUtils.okHttp2Server2(head_url,map);
                        }
                    }
                default:
                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 解析用户信息
     * @param response
     */
    private void parseData(String response) {
        LogUtil.e(response);
        Gson gson = new Gson();
        UserinfoBean userinfoBean = gson.fromJson(response, UserinfoBean.class);
        int statusCode = userinfoBean.getStatusCode();
        List<UserinfoBean.DataBean> data = userinfoBean.getData();
        switch (statusCode) {
            case 1 :
                if(data==null) {
                    return;
                }
                loadUserInfo(data);
                break;
            case 0:
                Toast.makeText(PersonInfoActivity.this, "修改失败！"+userinfoBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 展示用户信息
     * @param data
     */
    private void loadUserInfo(List<UserinfoBean.DataBean> data) {
        UserinfoBean.DataBean dataBean = data.get(0);
        String avatar = dataBean.getAvatar();
        String group = dataBean.getGroup();
        String mobile = dataBean.getMobile();
        String nick_name = dataBean.getNick_name();
        String sex = dataBean.getSex();
        if(nick_name!=null && !TextUtils.isEmpty(nick_name)) {
            tvPersonNick.setText(nick_name);
        }
        if(avatar!=null && !avatar.equals("")) {
            if(!avatar.contains("http")){
                avatar = Url.IMG_URL + avatar;
            }
            Glide.with(this).load(avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                    .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                    .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                    .into(civPersonHead);//请求成功后把图片设置到的控件
        }
        if(mobile!=null && !mobile.equals("")) {
            tvPersonPhone.setText(mobile);
        }
        if(sex!=null && !sex.equals("")) {
           tvPersonSex.setText(sex);
        }
        if(group!=null && !group.equals("")) {
            usertype.setText(group);
        }
    }

    /**
     * 获取用户的登录信息
     */
    private void getUserInfo() {
        if(Tools.isLogin(this)) {
            userLogin = Tools.getUserLogin(this);
            map= new HashMap<>();
            map.put("Id",userLogin);
            whick = "info";
            netUtils.okHttp2Server2(url,map);
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

}
