package com.gather_excellent_help;

import android.*;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.VersionBean;
import com.gather_excellent_help.ui.base.BaseFullScreenActivity;
import com.gather_excellent_help.ui.widget.SplashView;
import com.gather_excellent_help.utils.BitmapUtil;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.PhotoUtils;
import com.gather_excellent_help.utils.ToastUtils;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import android.Manifest;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class SplashActivity extends BaseFullScreenActivity {


    @Bind(R.id.iv_splash)
    ImageView ivSplash;
    @Bind(R.id.rl_splash)
    RelativeLayout rlSplash;

    private File apkFile;
    private ProgressDialog pd;

    private static final int REQUEST_TIME = 1;
    public static final int REQUEST_DOWNLOAD_SUCCESS = 3;
    public static final int REQUEST_DOWNLOAD_ERROR = 4;
    private String check_url = Url.BASE_URL + "GetAppVersion.aspx";
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private static final int REQUEST_CODE_ASK_CALL_PHONE = 0x05;

    private int time = 2;

    private NetUtil netUtil;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_TIME:
                    goToMain();
                    break;
                case REQUEST_DOWNLOAD_ERROR:
                    Toast.makeText(getApplicationContext(), "下载失败，请检查网络状态！", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case REQUEST_DOWNLOAD_SUCCESS:
                    autoObtainStoragePermission();
                    break;
            }
        }
    };
    private Bitmap bitmap;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_ASK_CALL_PHONE);
            } else {
                startLoadingData();
            }
        } else {
            startLoadingData();
        }
    }

    /**
     * start splash animation
     */
    private void startLoadingData() {
        bitmap = BitmapUtil.readBitMap(this, R.drawable.splash_img);
        if (bitmap != null) {
            ivSplash.setImageBitmap(bitmap);
            ivSplash.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        netUtil = new NetUtil();
        checkUpdate();
    }

    private void checkUpdate() {
        netUtil.okHttp2Server2(SplashActivity.this,check_url, null);
        netUtil.setOnServerResponseListener(new OnServerResponseListener());
    }

    public void goToMain() {
        finish();
        boolean isFirst = CacheUtils.getBoolean(this, CacheUtils.FIRST_STATE, false);
        if (!isFirst) {
            Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
            startActivity(intent);
            CacheUtils.putBoolean(this, CacheUtils.FIRST_STATE, true);
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        if (pd != null) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 自动获取sdk权限
     */
    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            installApk();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    installApk();
                } else {
                    ToastUtils.showShort(this, "请允许打开操作SDCard的权限！！");
                }
                break;
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLoadingData();
                } else {
                    Toast.makeText(SplashActivity.this, "请允许打开读取手机的权限！！", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    /**
     * 向用户展示更新下载的对话框
     */
    private void showUpdateDialog() {
        alertDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("有新版本，请立即更新！")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showloadApkDialog();
                    }
                }).create();
        if (SplashActivity.this != null && !isFinishing()) {
            alertDialog.show();
        }
    }

    /**
     * 提示下载Apk的dialog
     */
    private void showloadApkDialog() {
        createApkFile();
        showDownloadDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Tools.downloadApk(Url.DOWNLOAD_URL, apkFile, pd, mHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(REQUEST_DOWNLOAD_ERROR);
                }
            }
        }).start();
    }

    /*
    * 创建apk文件的路径
    */
    private void createApkFile() {
        File dirFile = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dirFile = getExternalFilesDir(null);
        } else {
            dirFile = getFilesDir();
        }
        apkFile = new File(dirFile, "juyoubang.apk");
        if (apkFile.exists()) {
            apkFile.delete();
        } else {
            try {
                apkFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.e(e.getMessage());
            }
        }
    }

    /**
     * 对话框进度条的初始化展示
     */
    private void showDownloadDialog() {
        pd = new ProgressDialog(this);
        pd.setTitle("提示");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);
        pd.show();
    }


    /**
     * 安装下载的APK文件
     */
    private void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(this, "com.gather_excellent_help.fileprovider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 联网回调的接口
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            try {
                LogUtil.e(response);
                parseData(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            EncryptNetUtil.startNeterrorPage(SplashActivity.this);
            LogUtil.e(call.toString() + "--" + e.getMessage());
        }
    }

    /**
     * 解析数据
     *
     * @param response
     */
    private void parseData(String response) throws Exception {

        VersionBean versionBean = new Gson().fromJson(response, VersionBean.class);
        int statusCode = versionBean.getStatusCode();
        switch (statusCode) {
            case 1:
                List<VersionBean.DataBean> data = versionBean.getData();
                if (data != null && data.size() > 0) {
                    VersionBean.DataBean dataBean = data.get(0);
                    int isHb = dataBean.getIsHb();
                    LogUtil.e("是否开启 = " + isHb);
                    CacheUtils.putInteger(SplashActivity.this, CacheUtils.IS_OPEN_RED, isHb);
                    if (dataBean != null) {
                        String appVersion = dataBean.getAppVersion();
                        String version = Tools.getVersion(this);
                        if (appVersion != null) {
                            if (appVersion.equals(Tools.getVersion(this))) {
                                mHandler.sendEmptyMessageDelayed(REQUEST_TIME, 1000);
                            } else {
                                showUpdateDialog();
                            }
                        }
                    }
                }
                break;
            case 0:
                Toast.makeText(SplashActivity.this, versionBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
