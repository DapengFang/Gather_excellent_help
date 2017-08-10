package com.gather_excellent_help;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.baichuan.trade.common.adapter.ut.AlibcUserTracker;
import com.gather_excellent_help.ui.service.MyService;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.ut.mini.internal.UTTeamWork;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wuxin on 2017/7/10.
 */

public class MyApplication extends Application {
    public static MyApplication application = null;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        new Thread(){
            public void run(){
                //电商SDK初始化
                AlibcTradeSDK.asyncInit(application, new AlibcTradeInitCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MyApplication.this, "初始化成功", Toast.LENGTH_SHORT).show();
                        Map utMap = new HashMap<>();
                        utMap.put("debug_api_url","http://muvp.alibaba-inc.com/online/UploadRecords.do");
                        utMap.put("debug_key","baichuan_sdk_utDetection");
                        UTTeamWork.getInstance().turnOnRealTimeDebug(utMap);
                        AlibcUserTracker.getInstance().sendInitHit4DAU("19","3.1.1.100");

                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Toast.makeText(MyApplication.this, "初始化失败,错误码="+code+" / 错误消息="+msg, Toast.LENGTH_SHORT).show();
                    }
                });

                Intent localIntent = new Intent();
                localIntent.setClass(application, MyService.class); // 销毁时重新启动Service
                application.startService(localIntent);

                PlatformConfig.setWeixin("wxc883e0b88fddcc71", "73657caeee905f216e6228fe3f3ed5e0");
                PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
                PlatformConfig.setSinaWeibo("869688985", "942c4c9d93c5e0e6b97f8c8eed00d105", "http://sns.whalecloud.com");
                UMShareAPI.get(application);
                JPushInterface.setDebugMode(true);
                JPushInterface.init(application);
            }
        }.start();



    }
}

