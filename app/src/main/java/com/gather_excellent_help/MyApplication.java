package com.gather_excellent_help;

import android.app.Application;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.baichuan.trade.common.adapter.ut.AlibcUserTracker;
import com.ut.mini.internal.UTTeamWork;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuxin on 2017/7/10.
 */

public class MyApplication extends Application {
    public static MyApplication application = null;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        //电商SDK初始化
        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
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

    }
}

