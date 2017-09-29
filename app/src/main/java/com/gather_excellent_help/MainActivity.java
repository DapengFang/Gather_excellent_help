package com.gather_excellent_help;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.gather_excellent_help.ui.adapter.CustomPagerAdapter;
import com.gather_excellent_help.ui.base.BaseFragmentActivity;
import com.gather_excellent_help.ui.base.LazyLoadFragment;
import com.gather_excellent_help.ui.fragment.GoodscartFragment;
import com.gather_excellent_help.ui.fragment.HomeFragment;
import com.gather_excellent_help.ui.fragment.HomeUpdateFragment;
import com.gather_excellent_help.ui.fragment.MineFragment;
import com.gather_excellent_help.ui.fragment.TaobaoFragment;
import com.gather_excellent_help.ui.fragment.TypeFragment;
import com.gather_excellent_help.ui.widget.NoScrollViewPager;
import com.gather_excellent_help.utils.EncryptUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.Tools;
import com.taobao.sophix.SophixManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {

    @Bind(R.id.vp_main)
    NoScrollViewPager vp_main;
    @Bind(R.id.rg_main)
    RadioGroup rg_main;
    @Bind(R.id.ll_main)
    LinearLayout ll_main;
    private int currItem;//当前显示的界面
    private ArrayList<LazyLoadFragment> fragments;//存放fragment页面的工作
    public static final int EXIT_APP =1;
    public static final int EXIT_APP_CONTINUE =2;
    private boolean isExit = false;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EXIT_APP :
                    if(isExit) {
                        finish();
                    }else {
                        isExit = true;
                        Toast.makeText(MainActivity.this, "再按一次点击退出聚优帮！", Toast.LENGTH_SHORT).show();
                        handler.sendEmptyMessageDelayed(EXIT_APP_CONTINUE,3000);
                    }
                    break;
                case EXIT_APP_CONTINUE:
                    isExit = false;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        String md5Value = EncryptUtil.getMd5Value("123456@@11fe468");
        LogUtil.e(md5Value);
    }

    /**
     * 加载数据
     */
    private void initData() {
        loadViewPager();
        rg_main.check(R.id.rb_home);
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
    }

    /**
     * 加载ViewPager页面
     */
    private void loadViewPager() {
        fragments = new ArrayList<>();
        fragments.add(new HomeUpdateFragment());
        //fragments.add(new HomeFragment());
        fragments.add(new TypeFragment());
        fragments.add(new TaobaoFragment());
        fragments.add(new GoodscartFragment());
        fragments.add(new MineFragment());
        FragmentManager fm = getSupportFragmentManager();
        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(fm,fragments);
        vp_main.setAdapter(customPagerAdapter);
    }

    /**
     *
     */
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            //根据选中的RadiaoButton切换到ViewPager不同页面
            //        vp_content.setCurrentItem(0);
            switch (checkedId) {
                case R.id.rb_home://首页
                    currItem = 0;
                    break;
                case R.id.rb_shaidan://分类
                    currItem = 1;
                    break;
                case R.id.rb_type://搜淘宝
                    currItem = 2;
                    break;
                case R.id.rb_shopping://购物车
                    currItem = 3;
                    break;
                case R.id.rb_me://我的
                    currItem = 4;
                    break;
            }
                vp_main.setCurrentItem(currItem, false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getRepeatCount() == 0) {
                //do something...
                handler.sendEmptyMessage(EXIT_APP);
                return true;
            }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        boolean hotfixStute = Tools.getHotfixStute(this);
        if (!isAppOnForeground()) {
            //app 进入后台
            if (hotfixStute) {
                SophixManager.getInstance().killProcessSafely();
                Tools.saveHotfixStute(this, false);
                Toast.makeText(MainActivity.this, "发现聚优帮有更新，请重新启动App", Toast.LENGTH_SHORT).show();
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
