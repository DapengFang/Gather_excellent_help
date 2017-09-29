package com.gather_excellent_help.ui.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.StatusBarUtil;
import com.gather_excellent_help.utils.Tools;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taobao.sophix.SophixManager;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by wuxin on 2017/7/11.
 */

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        boolean hotfixStute = Tools.getHotfixStute(this);
        LogUtil.e("hotfixStute == "+ hotfixStute);
        if (!isAppOnForeground()) {
            //app 进入后台
            if (hotfixStute) {
                SophixManager.getInstance().killProcessSafely();
                Tools.saveHotfixStute(this, false);
                Toast.makeText(BaseActivity.this, "杀死app进程成功", Toast.LENGTH_SHORT).show();
                LogUtil.e("杀死app进程成功");
            }else{
                LogUtil.e("杀死app进程失败");
            }
            LogUtil.e("现在处于后台进程");
        }else{
            LogUtil.e("现在处于前台进程");
        }
    }

    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the device
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                return true;
        return false;
    }

}
