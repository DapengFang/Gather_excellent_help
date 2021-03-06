package com.gather_excellent_help.ui.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.utils.AndroidWorkaround;
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

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
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
    }

}
