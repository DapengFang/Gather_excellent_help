package com.gather_excellent_help.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.UserinfoBean;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.CircularImage;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
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
    private ImageLoader mImageLoader;//图片加载类

    public static final int TAKE_PICTURE = 0;
    public static final int CHOOSE_PICTURE = 1;
    public static final int DO_PICTURE = 2;
    private String whick;

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
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
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
                       intent = new Intent(
                               MediaStore.ACTION_IMAGE_CAPTURE);
                       //下面这句指定调用相机拍照后的照片存储的路径
                       intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                               .fromFile(new File(Environment
                                       .getExternalStorageDirectory(),
                                       "temp.jpg")));
                       startActivityForResult(intent, TAKE_PICTURE);
                       break;
                   case CHOOSE_PICTURE:
                       intent = new Intent(Intent.ACTION_PICK, null);
                       intent.setDataAndType(
                               MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                               "image/*");
                       startActivityForResult(intent, CHOOSE_PICTURE);
                       break;
               }
            }
        });
        builder.create().show();
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
        startActivityForResult(intent, DO_PICTURE);
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
            if(!TextUtils.isEmpty(upload)) {
                map = new HashMap<>();
                map.put("id",userLogin);
                map.put("file",upload);
                whick = "head";
                netUtils.okHttp2Server2(head_url,map);
            }
            /**
             * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上
             * 传到服务器，QQ头像上传采用的方法跟这个类似
             */

			/*ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			byte[] b = stream.toByteArray();
			// 将图片流以字符串形式存储下来

			tp = new String(Base64Coder.encodeLines(b));
			这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了，
			服务器处理的方法是服务器那边的事了，吼吼

			如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换
			为我们可以用的图片类型就OK啦...吼吼
			Bitmap dBitmap = BitmapFactory.decodeFile(tp);
			Drawable drawable = new BitmapDrawable(dBitmap);
			*/
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 如果是直接从相册获取
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData());
                    break;
                // 如果是调用相机拍照时
                case TAKE_PICTURE:
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/temp.jpg");
                    startPhotoZoom(Uri.fromFile(temp));
                    break;
                // 取得裁剪后的图片
                case DO_PICTURE:
                    /**
                     * 非空判断大家一定要验证，如果不验证的话，
                     * 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                     * 当前功能时，会报NullException，小马只
                     * 在这个地方加下，大家可以根据不同情况在合适的
                     * 地方做判断处理类似情况
                     *
                     */
                    if (data != null) {
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
     * 解析用户信息
     * @param response
     */
    private void parseData(String response) {
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
            mImageLoader.loadImage(avatar,civPersonHead,true);
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


}
