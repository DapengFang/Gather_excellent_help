package com.gather_excellent_help.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.gather_excellent_help.MainActivity;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class TestActivity extends BaseActivity {

    @Bind(R.id.btn_bind_taobao)
    Button btnBindTaobao;
    @Bind(R.id.activity_test)
    LinearLayout activityTest;
    @Bind(R.id.btn_unbind_taobao)
    Button btnUnbindTaobao;
    @Bind(R.id.btn_toLogin)
    Button btnToLogin;
    @Bind(R.id.btn_person_info)
    Button btnPersonInfo;
    @Bind(R.id.btn_mine)
    Button btnMine;
    @Bind(R.id.btn_home)
    Button btnHome;
    @Bind(R.id.btn_order)
    Button btnOrder;

    private NetUtil netUtils;
    private NetUtil netUtils2;
    private Map<String, String> map;
    private String url = Url.BASE_URL + "bindTaobao.aspx";
    private String unbind_url = Url.BASE_URL + "UnbundTaobao.aspx";
    private String openId;
    private String avatarUrl;
    private String nick;
    private String user_id;
    private String bind_order =Url.BASE_URL + "MatchOrder.aspx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        netUtils2 = new NetUtil();
        map = new HashMap<>();
        String order = "";
        String[] strs = {"111","222","333"};
        for (int i=0;i<strs.length;i++){
            String ord = strs[i];
            order += ord +"a";
        }
        order = order.substring(0,order.length()-1);
        LogUtil.e(order);
        String loginId = CacheUtils.getString(this, CacheUtils.LOGIN_VALUE, "");
        map.put("userId","4");
        map.put("OrderId",order);
        netUtils2.okHttp2Server2(bind_order,map);
        netUtils2.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
               LogUtil.e(call.toString() + "--" + e.getMessage());
            }
        });
        initData();
    }



    private void initData() {
        netUtils = new NetUtil();
        btnBindTaobao.setOnClickListener(new MyOnClickListener());
        btnUnbindTaobao.setOnClickListener(new MyOnClickListener());
        btnToLogin.setOnClickListener(new MyOnClickListener());
        btnPersonInfo.setOnClickListener(new MyOnClickListener());
        btnMine.setOnClickListener(new MyOnClickListener());
        btnHome.setOnClickListener(new MyOnClickListener());
        btnOrder.setOnClickListener(new MyOnClickListener());
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                //parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "," + e.getMessage());
            }
        });
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        isLogin();
        if (isLogin()) {
            user_id = CacheUtils.getString(TestActivity.this, CacheUtils.LOGIN_VALUE, "");
            if (!TextUtils.isEmpty(user_id)) {
                map = new HashMap<>();
                map.put("Id", user_id + "");
                map.put("openId", openId);
                map.put("portrait", avatarUrl);
                map.put("nickname", nick);
            }
        }
    }

    /**
     * 解绑用户信息
     */
    public void unBindUserInfo() {
        isLogin();
        if (isLogin()) {
            user_id = CacheUtils.getString(TestActivity.this, CacheUtils.LOGIN_VALUE, "");
            if (!TextUtils.isEmpty(user_id)) {
                map = new HashMap<>();
                map.put("Id", user_id + "");
            }
        }
    }

    /**
     * @return 是否登录
     */
    private boolean isLogin() {
        return CacheUtils.getBoolean(TestActivity.this, CacheUtils.LOGIN_STATE, false);
    }

    /**
     * @return 是否绑定淘宝
     */
    private boolean isBind() {
        return CacheUtils.getBoolean(TestActivity.this, CacheUtils.BIND_STATE, false);
    }

    /**
     * 登录
     */
    public void bindTaobao() {

        AlibcLogin alibcLogin = AlibcLogin.getInstance();

        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                Toast.makeText(TestActivity.this, "绑定成功！", Toast.LENGTH_LONG).show();
                //获取淘宝用户信息
                LogUtil.e("获取淘宝用户信息: " + AlibcLogin.getInstance().getSession());
                LogUtil.e("代码:" + i);
                CacheUtils.putBoolean(TestActivity.this, CacheUtils.BIND_STATE, true);
                openId = AlibcLogin.getInstance().getSession().openId;
                avatarUrl = AlibcLogin.getInstance().getSession().avatarUrl;
                nick = AlibcLogin.getInstance().getSession().nick;
                getUserInfo();
                netUtils.okHttp2Server2(url, map);
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(TestActivity.this, "绑定失败 ！",
                        Toast.LENGTH_LONG).show();
                Log.i("GGG", "错误码" + code + "原因" + msg);
            }
        });
    }


    /**
     * 退出登录
     */
    public void unBindTaobao() {

        AlibcLogin alibcLogin = AlibcLogin.getInstance();

        alibcLogin.logout(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                Toast.makeText(TestActivity.this, "解绑成功 ",
                        Toast.LENGTH_LONG).show();
                unBindUserInfo();
                netUtils.okHttp2Server2(unbind_url, map);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(TestActivity.this, "解绑失败 ",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()) {
                case R.id.btn_bind_taobao:
                    if (isLogin()) {
                        bindTaobao();
                    } else {
                        Toast.makeText(TestActivity.this, "请先登录聚优帮账号！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_unbind_taobao:
                    if (isBind()) {
                        unBindTaobao();
                    } else {
                        Toast.makeText(TestActivity.this, "请先绑定淘宝，然后再来解绑！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_toLogin:
                    intent = new Intent(TestActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_person_info:
                    intent = new Intent(TestActivity.this, PersonInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_mine:
                    intent = new Intent(TestActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_home:
                    intent = new Intent(TestActivity.this, TestActivity2.class);
                    startActivity(intent);
                    break;
                case R.id.btn_order:
                    intent = new Intent(TestActivity.this, OrderActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

//    public void onEventMainThread(AnyEvent event) {
//        String msg = "onEventMainThread收到了消息：" + event.getMessage();
//        LogUtil.e(msg);
//        user_id = CacheUtils.getString(TestActivity.this, CacheUtils.LOGIN_VALUE, "");
//        isLogin();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // EventBus.getDefault().unregister(this);
    }
}
