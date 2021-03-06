package com.gather_excellent_help.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
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
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.EncryptUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class LoginActivity extends Activity {


    EditText etLoginUser;
    EditText etLoginPsw;
    TextView tvLoginLostpsw;
    TextView tvLogin;
    TextView tvRegister;
    private TextView tv_login_wx;

    private String bind_url = Url.BASE_URL + "bindTaobao.aspx";//绑定淘宝

    private String user;
    private String password;
    private NetUtil netUtils;
    private Map<String, String> map;
    private String url = Url.BASE_URL + "login.aspx";
    private String openId;
    private String avatarUrl;
    private String nick;
    private String which;
    private AlibcLogin alibcLogin;
    private IWXAPI api;
    private String weixin_app_id = "wxc883e0b88fddcc71";
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        initView();
        regToWx();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tvLogin = (TextView) findViewById(R.id.tv_login);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        tvLoginLostpsw = (TextView) findViewById(R.id.tv_login_lostpsw);
        etLoginUser = (EditText) findViewById(R.id.et_login_user);
        etLoginPsw = (EditText) findViewById(R.id.et_login_psw);
        tv_login_wx = (TextView) findViewById(R.id.tv_login_wx);
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
        tv_login_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginToWchat();
            }
        });
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                try {
                    LogUtil.e(response);
                    if (which.equals("login")) {
                        parseData(response);
                    } else if (which.equals("bind")) {
                        parseBindData(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "," + e.getMessage());
                EncryptNetUtil.startNeterrorPage(LoginActivity.this);
            }
        });
    }

    /** -------------------------微信第三方登录---------------------- */
    /**
     * 微信平台应用授权登录接入代码示例
     */
    private void regToWx() {
        // 通过WXAPIFactory工厂,获得IWXAPI的实例
        api = WXAPIFactory.createWXAPI(LoginActivity.this, weixin_app_id, true);
        // 将应用的appid注册到微信
        api.registerApp(weixin_app_id);
    }

    private void loginToWchat() {
        LogUtil.e("登录到微信！");
        getCode();
    }

    //获取微信访问getCode
    private void getCode() {
        if (!api.isWXAppInstalled()) {
            Toast.makeText(this, "请先安装微信应用！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!api.isWXAppSupportAPI()) {
            Toast.makeText(this, "请先更新微信应用！", Toast.LENGTH_SHORT).show();
            return;
        }
        // send oauth request
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);
        finish();
    }

    /**
     * 解析绑定淘宝的数据
     *
     * @param response
     */
    private void parseBindData(String response) throws Exception {
        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
        int statusCode = codeStatueBean.getStatusCode();
        switch (statusCode) {
            case 1:
                CacheUtils.putBoolean(LoginActivity.this, CacheUtils.BIND_STATE, true);
                CacheUtils.putString(LoginActivity.this, CacheUtils.TAOBAO_NICK, nick);
                EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN, "登录成功！"));
                break;
            case 0:

                break;
        }
    }

    /**
     * 解析联网返回数据
     *
     * @param response
     */
    private void parseData(String response) {
        Gson gson = new Gson();
        CodeStatueBean codeStatueBean = gson.fromJson(response, CodeStatueBean.class);
        int statusCode = codeStatueBean.getStatusCode();
        switch (statusCode) {
            case 0:
                Toast.makeText(LoginActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(LoginActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                CodeBean codeBean = new Gson().fromJson(response, CodeBean.class);
                List<CodeBean.DataBean> data = codeBean.getData();
                if (data.size() > 0) {
                    Integer id = data.get(0).getId();
                    int group_type = data.get(0).getGroup_type();
                    double user_rate = data.get(0).getUser_get_ratio();
                    String advertising = data.get(0).getAdvertising();
                    int group_id = data.get(0).getGroup_id();
                    String token = data.get(0).getToken();
                    LogUtil.e("user = " + user);
                    CacheUtils.putBoolean(LoginActivity.this, CacheUtils.LOGIN_STATE, true);
                    CacheUtils.putString(LoginActivity.this, CacheUtils.LOGIN_VALUE, id + "");
                    CacheUtils.putInteger(LoginActivity.this, CacheUtils.SHOP_TYPE, group_type);
                    CacheUtils.putString(LoginActivity.this, CacheUtils.LOGIN_PHONE, user);
                    CacheUtils.putString(LoginActivity.this, CacheUtils.USER_RATE, user_rate + "");
                    CacheUtils.putInteger(LoginActivity.this, CacheUtils.GROUP_TYPE, group_id);
                    CacheUtils.putString(LoginActivity.this, CacheUtils.LOGIN_TOKEN, token);
                    if (advertising != null) {
                        CacheUtils.putString(LoginActivity.this, CacheUtils.ADVER_ID, advertising);
                    }
                    EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN, "登录成功！"));
                    showBindTaobaoDialog(id);
                }
                break;
        }
    }

    /**
     * 展示绑定淘宝的dialog
     */
    private void showBindTaobaoDialog(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("绑定淘宝账号")
                .setMessage("为了方便您之后的操作，请您先绑定淘宝账号。若取消绑定将会在您查看商品详情时提示您继续绑定操作")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bindTaobao(id + "");
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        alertDialog = builder.create();
        if (LoginActivity.this != null && !LoginActivity.this.isFinishing()) {
            alertDialog.show();
        }
    }

    /**
     * 获取用户输入的信息
     */
    private void getUserInputInfo() {
        user = etLoginUser.getText().toString().trim();
        password = etLoginPsw.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            Toast.makeText(LoginActivity.this, "请输入手机号码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        which = "login";
        password = password + "@@11fe468";
        password = EncryptUtil.getMd5Value(password);
        map = new HashMap<>();
        map.put("UserName", user);
        map.put("Password", password);
        netUtils.okHttp2Server2(LoginActivity.this, url, map);
    }

    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()) {
                case R.id.tv_login:
                    getUserInputInfo();
                    break;
                case R.id.tv_register:
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
     *
     * @param s
     */
    public void bindTaobao(final String s) {

        alibcLogin = AlibcLogin.getInstance();
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
                netUtils.okHttp2Server2(LoginActivity.this, bind_url, map);
            }

            @Override
            public void onFailure(int code, String msg) {
//                Toast.makeText(LoginActivity.this, "绑定失败 ！",
//                        Toast.LENGTH_LONG).show();
//                Log.i("GGG", "错误码" + code + "原因" + msg);
                EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN, "登录成功！"));
            }
        });
    }

    /**
     * 上传用户信息
     *
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlibcTradeSDK.destory();
        if (api != null) {
            api.unregisterApp();
            api = null;
        }
    }
}
