package com.gather_excellent_help;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import android.support.v4.app.FragmentManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.packet.RedpacketBean;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.activity.RedpacketShowActivity;
import com.gather_excellent_help.ui.adapter.CustomPagerAdapter;
import com.gather_excellent_help.ui.base.LazyLoadFragment;
import com.gather_excellent_help.ui.fragment.GoodscartFragment;
import com.gather_excellent_help.ui.fragment.HomeUpdateFragment;
import com.gather_excellent_help.ui.fragment.MineFragment;
import com.gather_excellent_help.ui.fragment.TaobaoFragment;
import com.gather_excellent_help.ui.fragment.TaobaoUpdateFragment;
import com.gather_excellent_help.ui.fragment.TypeFragment;
import com.gather_excellent_help.ui.widget.NoScrollViewPager;
import com.gather_excellent_help.utils.AndroidWorkaround;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.EncryptUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.ScreenUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.enctry.DES;
import com.gather_excellent_help.utils.enctry.Des2;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.taobao.sophix.SophixManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class MainActivity extends FragmentActivity {

    private static final int SHOW_RED_PACKET = 0x123;
    private static final int LOAD_MAIN = 0x134;
    private NoScrollViewPager vp_main;
    private RadioGroup rg_main;

    private RelativeLayout rl_red_packet;
    private RelativeLayout rl_red_packet_show;
    private ImageView iv_red_packet_exit;
    private ImageView iv_red_packet_get;

    private NetUtil netUtil;
    private Map<String, String> map;


    private String url = Url.BASE_URL + "GetLinkOfAdzone.aspx";

    private int currItem;//当前显示的界面
    private ArrayList<LazyLoadFragment> fragments;//存放fragment页面的工作
    public static final int EXIT_APP = 1;
    public static final int EXIT_APP_CONTINUE = 2;
    private boolean isExit = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EXIT_APP:
                    if (isExit) {
                        finish();
                    } else {
                        isExit = true;
                        Toast.makeText(MainActivity.this, "再按一次点击退出聚优帮！", Toast.LENGTH_SHORT).show();
                        handler.sendEmptyMessageDelayed(EXIT_APP_CONTINUE, 3000);
                    }
                    break;
                case EXIT_APP_CONTINUE:
                    isExit = false;
                    break;
                case SHOW_RED_PACKET:
                    rl_red_packet.setVisibility(View.VISIBLE);
                    Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.red_packet_show);
                    anim.setFillAfter(true);
                    anim.setInterpolator(new OvershootInterpolator());
                    rl_red_packet_show.startAnimation(anim);
                    break;
                case LOAD_MAIN:
                    //初始化
                    netUtil = new NetUtil();
                    rg_main.check(R.id.rb_home);
                    loadViewPager();
                    rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
                    MyOnclickListener myOnclickListener = new MyOnclickListener();
                    rl_red_packet.setOnClickListener(myOnclickListener);
                    iv_red_packet_exit.setOnClickListener(myOnclickListener);
                    iv_red_packet_get.setOnClickListener(myOnclickListener);
                    rl_red_packet_show.setOnClickListener(myOnclickListener);
                    netUtil.setOnServerResponseListener(new OnServerResponseListener());
                    break;
            }
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }
        setContentView(R.layout.activity_main);
        initView();
        initData();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);

    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                Window window = getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= flagTranslucentStatus | flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }

    }

    /**
     * 初始化控件
     */
    private void initView() {
        vp_main = (NoScrollViewPager) findViewById(R.id.vp_main);
        rl_red_packet = (RelativeLayout) findViewById(R.id.rl_red_packet);
        rl_red_packet_show = (RelativeLayout) findViewById(R.id.rl_red_packet_show);
        iv_red_packet_exit = (ImageView) findViewById(R.id.iv_red_packet_exit);
        iv_red_packet_get = (ImageView) findViewById(R.id.iv_red_packet_get);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
    }

    /**
     * 加载数据
     */
    private void initData() {
        int openRedStute = Tools.getOpenRedStute(this);
        if (openRedStute == 1) {
            handler.sendEmptyMessageDelayed(SHOW_RED_PACKET, 3000);
        }
        if (handler != null) {
            handler.sendEmptyMessageDelayed(LOAD_MAIN, 200);
        }
    }

    /**
     * 加载ViewPager页面
     */
    private void loadViewPager() {
        fragments = new ArrayList<>();
        fragments.add(new HomeUpdateFragment());
        fragments.add(new TypeFragment());
        fragments.add(new TaobaoUpdateFragment());
        fragments.add(new GoodscartFragment());
        fragments.add(new MineFragment());
        FragmentManager fm = getSupportFragmentManager();
        LogUtil.e("fragments.size() = " + fragments.size());
        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(fm, fragments);
        if (vp_main != null && customPagerAdapter != null) {
            vp_main.setAdapter(customPagerAdapter);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    /**
     * 底部RadioGroup的点击监听
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
            if (currItem == 0) {
                handler.sendEmptyMessage(EXIT_APP);
            } else {
                currItem = 0;
                rg_main.check(R.id.rb_home);
                vp_main.setCurrentItem(currItem, false);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        boolean hotfixStute = Tools.getHotfixStute(this);
        if (!isAppOnForeground()) {
            //app 进入后台
            if (hotfixStute) {
                Toast.makeText(MainActivity.this, "发现聚优帮有更新，需要重新启动App更新补丁", Toast.LENGTH_SHORT).show();
                Tools.saveHotfixStute(this, false);
                Tools.saveFirstHotfixToast(this, 1);
                SophixManager.getInstance().killProcessSafely();
            }
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
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
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_red_packet:
                    //用来覆盖底下布局的点击事件
                    break;
                case R.id.iv_red_packet_exit:
                    rl_red_packet.setVisibility(View.GONE);
                    break;
                case R.id.iv_red_packet_get:
                    boolean login = Tools.isLogin(MainActivity.this);
                    if (login) {
                        String userLogin = Tools.getUserLogin(MainActivity.this);
                        getRedPacketUrl(userLogin);
                    } else {
                        toLogin();
                    }
                    break;
                case R.id.rl_red_packet_show:
                    boolean clogin = Tools.isLogin(MainActivity.this);
                    if (clogin) {
                        String userLogin = Tools.getUserLogin(MainActivity.this);
                        getRedPacketUrl(userLogin);
                    } else {
                        toLogin();
                    }
                    break;
            }
        }
    }

    private void toLogin() {
        Toast.makeText(MainActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 获取红包链接
     */
    private void getRedPacketUrl(String userLogin) {
        map = new HashMap<>();
        map.put("user_id", userLogin);
        netUtil.okHttp2Server2(MainActivity.this,url, map);
    }

    /**
     * 领取红包
     */
    private void getRedPacket(String redpacket_url) {
        Intent intent = new Intent(this, RedpacketShowActivity.class);
        intent.putExtra("redpacket_url", redpacket_url);
        startActivity(intent);
        rl_red_packet.setVisibility(View.GONE);
    }

    /**
     * 监听页面上的点击事件
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e("领取红包 = " + response);
            RedpacketBean redpacketBean = new Gson().fromJson(response, RedpacketBean.class);
            int statusCode = redpacketBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    List<RedpacketBean.DataBean> data = redpacketBean.getData();
                    if (data != null && data.size() > 0) {
                        String newHblink = data.get(0).getNewHblink();
                        getRedPacket(newHblink);
                    }
                    break;
                case 0:
                    Toast.makeText(MainActivity.this, redpacketBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            EncryptNetUtil.startNeterrorPage(MainActivity.this);
            LogUtil.e(call.toString() + "-" + e.getMessage());
        }
    }

    /**
     * 跳转到首页
     */
    public void refreshMainActivity() {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(LOAD_MAIN, 200);
        }
    }
}
