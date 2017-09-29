package com.gather_excellent_help;

import android.app.Application;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.baichuan.trade.common.adapter.ut.AlibcUserTracker;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.ui.service.MyService;

import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.ut.mini.internal.UTTeamWork;

import java.util.HashMap;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

/**
 * Created by wuxin on 2017/7/10.
 */

public class MyApplication extends Application {
    public static MyApplication application = null;
    private String url = Url.BASE_URL + "UsersLoginRecord.aspx";
    private String userLogin;
    private NetUtil netUtil;
    private Map<String, String> map;
    private String appVersion;

//    public static RefWatcher getRefWatcher(Context context) {
//        MyApplication application = (MyApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }
//
//    private RefWatcher refWatcher;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        appVersion = Tools.getVersion(application);
        // initialize最好放在attachBaseContext最前面，初始化直接在Application类里面，切勿封装到其他类
        SophixManager.getInstance().setContext(application)
                .setAppVersion(appVersion)
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            LogUtil.e("PatchStatus.CODE_LOAD_SUCCESS = 补丁加载成功" );
                            //Toast.makeText(application, "更新补丁加载成功", Toast.LENGTH_SHORT).show();
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                            Tools.saveHotfixStute(application,true);
                            LogUtil.e("PatchStatus.CODE_LOAD_RELAUNCH = 补丁加载需要冷启动" );
                        } else if(code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                            LogUtil.e("PatchStatus.CODE_LOAD_FAIL = 内存引擎异常" );
                            SophixManager.getInstance().cleanPatches();
                        } else{
                            // 其它错误信息, 查看PatchStatus类说明
                            LogUtil.e("其它错误信息, 查看PatchStatus类说明");
                        }
                    }
                }).initialize();
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch();
//        new Thread(){
//                    public void run(){
//                        appVersion = "1.0.0";
//                        if (Build.VERSION.SDK_INT >= 23) {
//                            int checkCallPhonePermission = ContextCompat.checkSelfPermission(application, android.Manifest.permission.READ_PHONE_STATE);
//                            if(checkCallPhonePermission == PackageManager.PERMISSION_GRANTED){
//                                appVersion = Tools.getVersion(application);
//                            }
//                        }
//                // initialize最好放在attachBaseContext最前面，初始化直接在Application类里面，切勿封装到其他类
//                SophixManager.getInstance().setContext(application)
//                        .setAppVersion(appVersion)
//                        .setAesKey(null)
//                        .setEnableDebug(true)
//                        .setPatchLoadStatusStub(new PatchLoadStatusListener() {
//                            @Override
//                            public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
//                                // 补丁加载回调通知
//                                if (code == PatchStatus.CODE_LOAD_SUCCESS) {
//                                    // 表明补丁加载成功
//                                    LogUtil.e("PatchStatus.CODE_LOAD_SUCCESS = 补丁加载成功" );
//                                    Toast.makeText(application, "更新补丁加载成功", Toast.LENGTH_SHORT).show();
//                                } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
//                                    // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
//                                    // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
//                                    Tools.saveHotfixStute(application,true);
//                                    LogUtil.e("PatchStatus.CODE_LOAD_RELAUNCH = 补丁加载需要冷启动" );
//                                } else if(code == PatchStatus.CODE_LOAD_FAIL) {
//                                    // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
//                                    LogUtil.e("PatchStatus.CODE_LOAD_FAIL = 内存引擎异常" );
//                                    SophixManager.getInstance().cleanPatches();
//                                } else{
//                                    // 其它错误信息, 查看PatchStatus类说明
//                                    LogUtil.e("其它错误信息, 查看PatchStatus类说明");
//                                }
//                            }
//                        }).initialize();
//                // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
//                SophixManager.getInstance().queryAndLoadNewPatch();
//            }
//        }.start();
        netUtil = new NetUtil();
        boolean login = Tools.isLogin(application);
        if (login) {
            userLogin = Tools.getUserLogin(application);
            map = new HashMap<>();
            map.put("id", userLogin);
            netUtil.okHttp2Server2(url, map);
        }
        netUtil.setOnServerResponseListener(new MyOnsetServerLisetener());
        new Thread() {
            public void run() {
                //电商SDK初始化
                AlibcTradeSDK.asyncInit(application, new AlibcTradeInitCallback() {
                    @Override
                    public void onSuccess() {
                        Map utMap = new HashMap<>();
                        utMap.put("debug_api_url", "http://muvp.alibaba-inc.com/online/UploadRecords.do");
                        utMap.put("debug_key", "baichuan_sdk_utDetection");
                        UTTeamWork.getInstance().turnOnRealTimeDebug(utMap);
                        AlibcUserTracker.getInstance().sendInitHit4DAU("19", "3.1.1.100");

                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Toast.makeText(MyApplication.this, "初始化失败,错误码=" + code + " / 错误消息=" + msg, Toast.LENGTH_SHORT).show();
                    }
                });

                Intent localIntent = new Intent();
                localIntent.setClass(application, MyService.class); // 销毁时重新启动Service
                application.startService(localIntent);

                PlatformConfig.setWeixin("wxc883e0b88fddcc71", "73657caeee905f216e6228fe3f3ed5e0");
                PlatformConfig.setQQZone("1106275340", "vYLYLlyRQ7ZDZMlf");
                PlatformConfig.setSinaWeibo("869688985", "942c4c9d93c5e0e6b97f8c8eed00d105", "http://sns.whalecloud.com");
                UMShareAPI.get(application);
                JPushInterface.setDebugMode(true);
                JPushInterface.init(application);
                //refWatcher = LeakCanary.install(application);
            }
        }.start();


    }

    public class MyOnsetServerLisetener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "--" + e.getMessage());
        }
    }

}

