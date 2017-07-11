package com.gather_excellent_help.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class TestActivity extends AppCompatActivity {

    @Bind(R.id.btn_bind_taobao)
    Button btnBindTaobao;
    @Bind(R.id.activity_test)
    LinearLayout activityTest;
    @Bind(R.id.btn_unbind_taobao)
    Button btnUnbindTaobao;
    @Bind(R.id.btn_toLogin)
    Button btnToLogin;

    private NetUtil netUtils;
    private Map<String, String> map;
    private String url = Url.BASE_URL + "bindTaobao.aspx";
    private String openId;
    private String avatarUrl;
    private String nick;
    private Long user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {
        netUtils = new NetUtil();
        btnBindTaobao.setOnClickListener(new MyOnClickListener());
        btnUnbindTaobao.setOnClickListener(new MyOnClickListener());
        btnToLogin.setOnClickListener(new MyOnClickListener());
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                //parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString()+","+e.getMessage());
            }
        });
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo(){
        isLogin();
        if(isLogin()) {
            user_id = CacheUtils.getInteger(TestActivity.this, CacheUtils.LOGIN_VALUE, -1);
            if(user_id !=-1) {
                map= new HashMap<>();
                map.put("Id", user_id +"");
                map.put("openId",openId);
                map.put("portrait",avatarUrl);
                map.put("nickname",nick);
            }
        }
    }

    private boolean isLogin() {
        return CacheUtils.getBoolean(TestActivity.this, CacheUtils.LOGIN_STATE, false);
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
                openId = AlibcLogin.getInstance().getSession().openId;
                avatarUrl = AlibcLogin.getInstance().getSession().avatarUrl;
                nick = AlibcLogin.getInstance().getSession().nick;
                getUserInfo();
                netUtils.okHttp2Server2(url,map);
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
                Toast.makeText(TestActivity.this, "登出成功 ",
                        Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(TestActivity.this, "登录失败 ",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_bind_taobao:
                    if(isLogin()) {
                        bindTaobao();
                    }else{
                        Toast.makeText(TestActivity.this, "请先登录聚优帮账号！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_unbind_taobao:
                    unBindTaobao();
                    break;
                case R.id.btn_toLogin:
                    Intent intent = new Intent(TestActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    public void onEventMainThread(AnyEvent event) {
        String msg = "onEventMainThread收到了消息：" + event.getMessage();
        LogUtil.e(msg);
        user_id = CacheUtils.getInteger(TestActivity.this, CacheUtils.LOGIN_VALUE, -1);
        isLogin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
