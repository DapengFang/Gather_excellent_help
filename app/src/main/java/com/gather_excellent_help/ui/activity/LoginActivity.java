package com.gather_excellent_help.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.lisetener.MyTextWatcher;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.Check;
import com.gather_excellent_help.utils.EncryptUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class LoginActivity extends Activity {


    @Bind(R.id.et_login_user)
    EditText etLoginUser;
    @Bind(R.id.et_login_psw)
    EditText etLoginPsw;
    @Bind(R.id.tv_login_lostpsw)
    TextView tvLoginLostpsw;
    @Bind(R.id.tv_login)
    TextView tvLogin;
    @Bind(R.id.tv_register)
    TextView tvRegister;

    private String bind_url = Url.BASE_URL + "bindTaobao.aspx";//绑定淘宝

    private String user;
    private String password;
    private NetUtil netUtils;
    private Map<String,String> map;
    private String url = Url.BASE_URL + "login.aspx";
    private String openId;
    private String avatarUrl;
    private String nick;
    private String which;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        netUtils = new NetUtil();
        etLoginPsw.addTextChangedListener(new MyTextWatcher());
        tvLogin.setOnClickListener(new MyOnClickListener());
        tvRegister.setOnClickListener(new MyOnClickListener());
        tvLoginLostpsw.setOnClickListener(new MyOnClickListener());
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                if(which.equals("login")) {
                    parseData(response);
                }else if(which.equals("bind")) {
                    parseBindData(response);
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
               LogUtil.e(call.toString()+","+e.getMessage());
            }
        });
    }

    /**
     * 解析绑定淘宝的数据
     * @param response
     */
    private void parseBindData(String response) {
        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
        int statusCode = codeStatueBean.getStatusCode();
        switch (statusCode) {
            case 1 :
                CacheUtils.putBoolean(LoginActivity.this, CacheUtils.BIND_STATE, true);
                CacheUtils.putString(LoginActivity.this,CacheUtils.TAOBAO_NICK,nick);
                EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN,"登录成功！"));
                break;
            case 0:

                break;
        }
    }

    /**
     * 解析联网返回数据
     * @param response
     */
    private void parseData(String response) {
        Gson gson = new Gson();
        CodeStatueBean codeStatueBean = gson.fromJson(response, CodeStatueBean.class);
        int statusCode = codeStatueBean.getStatusCode();
        switch (statusCode){
            case 0:
                Toast.makeText(LoginActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(LoginActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                CodeBean codeBean = new Gson().fromJson(response, CodeBean.class);
                List<CodeBean.DataBean> data = codeBean.getData();
                if(data.size()>0) {
                    Integer id = data.get(0).getId();
                    int group_type = data.get(0).getGroup_type();
                    double user_rate = data.get(0).getUser_get_ratio();
                    String advertising = data.get(0).getAdvertising();
                    int group_id = data.get(0).getGroup_id();
                    CacheUtils.putBoolean(LoginActivity.this,CacheUtils.LOGIN_STATE,true);
                    CacheUtils.putString(LoginActivity.this,CacheUtils.LOGIN_VALUE,id+"");
                    CacheUtils.putInteger(LoginActivity.this,CacheUtils.SHOP_TYPE,group_type);
                    CacheUtils.putString(LoginActivity.this,CacheUtils.LOGIN_PHONE,user);
                    CacheUtils.putString(LoginActivity.this,CacheUtils.USER_RATE,user_rate+"");
                    CacheUtils.putInteger(LoginActivity.this, CacheUtils.GROUP_TYPE, group_id);
                    if (advertising != null) {
                        CacheUtils.putString(LoginActivity.this, CacheUtils.ADVER_ID, advertising);
                    }
                    bindTaobao(id+"");
                    finish();
                }
                break;
        }
    }

    /**
     * 获取用户输入的信息
     */
    private void getUserInputInfo() {
        user = etLoginUser.getText().toString().trim();
        password = etLoginPsw.getText().toString().trim();
        if(TextUtils.isEmpty(user)) {
            Toast.makeText(LoginActivity.this, "请输入手机号码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
            return;
        }

            password = password+"@@11fe468";
            password = EncryptUtil.getMd5Value(password);
            map= new HashMap<>();
            map.put("UserName",user);
            map.put("Password",password);
            which = "login";
            netUtils.okHttp2Server2(url,map);

    }

    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent =null;
            switch (view.getId()) {
                case R.id.tv_login :
                    getUserInputInfo();
                    break;
                case R.id.tv_register :
                    intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.tv_login_lostpsw:
                    intent = new Intent(LoginActivity.this, FindPswActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    /**
     * 绑定淘宝
     * @param s
     */
    public void bindTaobao(final String s) {

        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                //获取淘宝用户信息
                LogUtil.e("获取淘宝用户信息: " + AlibcLogin.getInstance().getSession());
                LogUtil.e("代码:" + i);
                openId = AlibcLogin.getInstance().getSession().openId;
                avatarUrl = AlibcLogin.getInstance().getSession().avatarUrl;
                nick = AlibcLogin.getInstance().getSession().nick;
                uploadUserInfo(s);
                which = "bind";
                netUtils.okHttp2Server2(bind_url, map);
            }

            @Override
            public void onFailure(int code, String msg) {
//                Toast.makeText(LoginActivity.this, "绑定失败 ！",
//                        Toast.LENGTH_LONG).show();
//                Log.i("GGG", "错误码" + code + "原因" + msg);
                EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN,"登录成功！"));
            }
        });
    }

    /**
     * 上传用户信息
     * @param s
     */
    public void uploadUserInfo(String s) {

            if (!TextUtils.isEmpty(s)) {
                map = new HashMap<>();
                map.put("Id", s);
                map.put("openId", openId);
                map.put("portrait", avatarUrl);
                map.put("nickname", nick);
            }

    }

}
