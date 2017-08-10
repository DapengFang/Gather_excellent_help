package com.gather_excellent_help.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


import com.gather_excellent_help.utils.LogUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者：Dapeng Fang on 2017/1/5 10:41
 * <p/>
 * 邮箱：fdp111888@163.com
 */

public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent localIntent = new Intent();
        localIntent.setClass(this, MyService.class); // 销毁时重新启动Service
        this.startService(localIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        return START_STICKY_COMPATIBILITY;
    }
}
