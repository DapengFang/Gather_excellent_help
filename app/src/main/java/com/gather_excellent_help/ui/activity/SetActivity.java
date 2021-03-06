package com.gather_excellent_help.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.SmsCodeBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.lisetener.MyTextWatcher;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.Check;
import com.gather_excellent_help.utils.DataCleanManager;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Cache;
import okhttp3.Call;

public class SetActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.rl_share)
    RelativeLayout rlShare;
    @Bind(R.id.rl_set_payaccount)
    RelativeLayout rlSetPayaccount;
    @Bind(R.id.rl_set_bindtaobao)
    RelativeLayout rlSetBindtaobao;
    @Bind(R.id.rl_set_updatepsw)
    RelativeLayout rlSetUpdatepsw;
    @Bind(R.id.rl_set_about)
    RelativeLayout rlSetAbout;
    @Bind(R.id.rl_set_cache)
    RelativeLayout rlSetCache;
    @Bind(R.id.rl_set_userinfo)
    RelativeLayout rlSetUserinfo;
    @Bind(R.id.tv_set_logout)
    TextView tvSetLogout;
    TextView tvSetClearCache;
    @Bind(R.id.tv_set_bind_alipay)
    TextView tvSetBindAlipay;
    @Bind(R.id.tv_set_bind_taobao)
    TextView tvSetBindTaobao;

    private TextView tv_set_version;

    private NetUtil netUtils;
    private Map<String, String> map;
    private String bind_url = Url.BASE_URL + "bindTaobao.aspx";
    private String unbind_url = Url.BASE_URL + "UnbundTaobao.aspx";
    private String pay_url = Url.BASE_URL + "BindAlipay.aspx";
    private String unpay_url = Url.BASE_URL + "UnbundAlipay.aspx";
    private String openId;
    private String avatarUrl;
    private String nick;
    private String user_id;
    private String which = ""; //哪一个联网请求
    private String account;

    private String sms_url = Url.BASE_URL + "GetRandom.aspx";
    private String sms_code_s = "-1";
    private CountDownTimer countDownTimer;
    private AlertDialog alertDialog;
    private File apkFile;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        EventBus.getDefault().register(this);
        initView();
        ButterKnife.bind(this);
        initData();
    }

    private void initView() {
        tvSetClearCache = (TextView) findViewById(R.id.tv_set_clear_cache);
        tv_set_version = (TextView) findViewById(R.id.tv_set_version);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (isLogin()) {
            user_id = Tools.getUserLogin(this);
        }
        tvTopTitleName.setText("设置");
        String version = Tools.getVersion(this);
        String show_version = "当前版本号：" + version;
        if (show_version.length() > 6) {
            Tools.setPartTextColor(tv_set_version, show_version, "：");
        } else {
            tv_set_version.setText("");
        }
        boolean bindTao = Tools.isBindTao(this);
        if (bindTao) {
            nick = CacheUtils.getString(SetActivity.this, CacheUtils.TAOBAO_NICK, "");
            tvSetBindTaobao.setText("绑定/解绑淘宝账号(" + nick + ")");
        } else {
            tvSetBindTaobao.setText("绑定/解绑淘宝账号");
        }

        if (isPay()) {
            account = CacheUtils.getString(SetActivity.this, CacheUtils.ALIPAY_ACCOUNT, "");
            tvSetBindAlipay.setText("绑定/解绑支付宝账号(" + account + ")");
        } else {
            tvSetBindAlipay.setText("绑定/解绑支付宝账号");
        }
        if (isLogin()) {
            tvSetLogout.setVisibility(View.VISIBLE);
        } else {
            tvSetLogout.setVisibility(View.GONE);
        }
        try {
            String totalCacheSize = DataCleanManager.getTotalCacheSize(this);
            tvSetClearCache.setText("清理缓存 (" + totalCacheSize + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
        netUtils = new NetUtil();
        rlExit.setOnClickListener(new MyOnClickListener());
        rlSetAbout.setOnClickListener(new MyOnClickListener());
        rlSetBindtaobao.setOnClickListener(new MyOnClickListener());
        rlSetCache.setOnClickListener(new MyOnClickListener());
        rlSetPayaccount.setOnClickListener(new MyOnClickListener());
        rlSetUpdatepsw.setOnClickListener(new MyOnClickListener());
        rlSetUserinfo.setOnClickListener(new MyOnClickListener());
        tvSetLogout.setOnClickListener(new MyOnClickListener());
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                try {
                    parseSetData(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "," + e.getMessage());
                EncryptNetUtil.startNeterrorPage(SetActivity.this);
            }
        });
    }

    private void parseSetData(String response) throws Exception {
        LogUtil.e(response);
        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
        int statusCode = codeStatueBean.getStatusCode();
        switch (statusCode) {
            case 1:
                if (which.equals("bind")) {
                    Toast.makeText(SetActivity.this, "绑定成功！", Toast.LENGTH_LONG).show();
                    tvSetBindTaobao.setText("绑定/解绑淘宝账号(" + nick + ")");
                    CacheUtils.putBoolean(SetActivity.this, CacheUtils.BIND_STATE, true);
                    CacheUtils.putString(SetActivity.this, CacheUtils.TAOBAO_NICK, nick);
                    EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN, "绑定成功！"));
                } else if (which.equals("unbind")) {
                    Toast.makeText(SetActivity.this, "解绑成功！", Toast.LENGTH_LONG).show();
                    tvSetBindTaobao.setText("绑定/解绑淘宝账号");
                    CacheUtils.putString(SetActivity.this, CacheUtils.TAOBAO_NICK, "");
                    CacheUtils.putBoolean(SetActivity.this, CacheUtils.BIND_STATE, false);
                    EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN, "解绑成功！"));
                } else if (which.equals("pay")) {
                    tvSetBindAlipay.setText("绑定/解绑支付宝账号(" + account + ")");
                    CacheUtils.putBoolean(SetActivity.this, CacheUtils.PAY_STATE, true);
                    CacheUtils.putString(SetActivity.this, CacheUtils.ALIPAY_ACCOUNT, account);
                    CacheUtils.putString(SetActivity.this, CacheUtils.ALIPAY_USERNAME, name);
                    EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN, "绑定成功！"));
                } else if (which.equals("unpay")) {
                    tvSetBindAlipay.setText("绑定/解绑支付宝账号");
                    CacheUtils.putBoolean(SetActivity.this, CacheUtils.PAY_STATE, false);
                    CacheUtils.putString(SetActivity.this, CacheUtils.ALIPAY_ACCOUNT, "");
                    EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN, "解绑成功！"));
                } else if (which.equals("sms")) {
                    parseSmsData(response);
                }
                break;
            case 0:
                Toast.makeText(SetActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析短信验证数据
     *
     * @param response
     */
    private void parseSmsData(String response) {
        LogUtil.e(response);
        SmsCodeBean smsCodeBean = new Gson().fromJson(response, SmsCodeBean.class);
        int statusCode = smsCodeBean.getStatusCode();
        switch (statusCode) {
            case 0:
                Toast.makeText(SetActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                List<SmsCodeBean.DataBean> data = smsCodeBean.getData();
                if (data.size() > 0) {
                    sms_code_s = data.get(0).getSms_code();
                }
                Toast.makeText(SetActivity.this, "验证码已发送你的手机，请查收！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 用来处理监听事件的类
     */
    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.rl_set_payaccount:
                    if (isPay()) {
                        toAlipayBindInfoPage();
                    } else {
                        showAlipayAccount();
                    }
                    break;
                case R.id.rl_set_bindtaobao:
                    if (isBind()) {
                        showUnbindTaobaoDialog();
                    } else {
                        showBindTaobaoDialog();
                    }
                    break;
                case R.id.rl_set_updatepsw:
                    intent = new Intent(SetActivity.this, FindPswActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rl_set_about:
                    //关于聚优帮相关的内容展示
                    //Toast.makeText(SetActivity.this, "关于聚优帮", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rl_set_cache:
                    showCacheClearDialog();
                    break;
                case R.id.tv_set_logout:
                    showAppExitDialog();
                    break;
                case R.id.rl_set_userinfo:
                    intent = new Intent(SetActivity.this, PersonInfoActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    /**
     * 跳转到绑定支付宝信息页面
     */
    private void toAlipayBindInfoPage() {
        Intent intent = new Intent(SetActivity.this, AlipayInfoActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 展示绑定淘宝的dialog
     */
    private void showBindTaobaoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("绑定淘宝账号")
                .setMessage("您需要绑定淘宝账号，若取消绑定将会在您查看商品详情时提示您继续绑定操作")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bindTaobao();
                    }
                })
                .setNegativeButton("取消", null);
        alertDialog = builder.create();
        if (SetActivity.this != null && !SetActivity.this.isFinishing()) {
            alertDialog.show();
        }
    }

    /**
     * 解除淘宝绑定的dialog
     */
    private void showUnbindTaobaoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示")
                .setMessage("您确定要解除淘宝绑定吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        unBindTaobao();
                    }
                })
                .setNegativeButton("取消", null);
        alertDialog = builder.create();
        if (SetActivity.this != null && !SetActivity.this.isFinishing()) {
            alertDialog.show();
        }
    }

    /**
     * 解除支付宝绑定的dialog
     */
    private void showUnBindAlipayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示")
                .setMessage("您确定要解除支付宝绑定吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        unBindPay();
                    }
                })
                .setNegativeButton("取消", null);
        alertDialog = builder.create();
        if (SetActivity.this != null && !SetActivity.this.isFinishing()) {
            alertDialog.show();
        }
    }

    /**
     * 清理缓存的dialog
     */
    private void showCacheClearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示")
                .setMessage("您确定要执行此操作吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            DataCleanManager.cleanApplicationCache(SetActivity.this);
                            String totalCacheSize = DataCleanManager.getTotalCacheSize(SetActivity.this);
                            tvSetClearCache.setText("清理缓存 (" + totalCacheSize + ")");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("取消", null);
        alertDialog = builder.create();
        if (SetActivity.this != null && !SetActivity.this.isFinishing()) {
            alertDialog.show();
        }

    }

    /**
     * 是否退出的dialog
     */
    private void showAppExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示")
                .setMessage("您确定要退出吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
//                            if (isBind()) {
//                                unBindTaobao();
//                            } else {
//                                CacheUtils.putBoolean(SetActivity.this, CacheUtils.BIND_STATE, false);
//                                CacheUtils.putString(SetActivity.this, CacheUtils.TAOBAO_NICK, "");
//                            }
                            CacheUtils.putBoolean(SetActivity.this, CacheUtils.LOGIN_STATE, false);
                            CacheUtils.putString(SetActivity.this, CacheUtils.LOGIN_VALUE, "");
                            CacheUtils.putInteger(SetActivity.this, CacheUtils.GROUP_TYPE, -1);
                            CacheUtils.putInteger(SetActivity.this, CacheUtils.SHOP_TYPE, -1);
                            CacheUtils.putString(SetActivity.this, CacheUtils.ALIPAY_ACCOUNT, "");
                            CacheUtils.putBoolean(SetActivity.this, CacheUtils.PAY_STATE, false);
                            CacheUtils.putString(SetActivity.this, CacheUtils.USER_RATE, "");
                            CacheUtils.putString(SetActivity.this, CacheUtils.LOGIN_PHONE, "");
                            EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN, "退出登录！"));
                            finish();
                            loginUser();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("取消", null);
        alertDialog = builder.create();
        if (SetActivity.this != null && !SetActivity.this.isFinishing()) {
            alertDialog.show();
        }
    }

    /**
     * 绑定淘宝
     */
    public void bindTaobao() {

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
                uploadUserInfo();
                which = "bind";
                netUtils.okHttp2Server2(SetActivity.this, bind_url, map);
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(SetActivity.this, "绑定失败 ！",
                        Toast.LENGTH_LONG).show();
                Log.i("GGG", "错误码" + code + "原因" + msg);
            }
        });
    }


    /**
     * 解绑淘宝
     */
    public void unBindTaobao() {

        AlibcLogin alibcLogin = AlibcLogin.getInstance();

        alibcLogin.logout(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                unBindUserInfo();
                which = "unbind";
                netUtils.okHttp2Server2(SetActivity.this, unbind_url, map);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(SetActivity.this, "解绑失败 ",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 上传用户信息
     */
    public void uploadUserInfo() {
        if (isLogin()) {
            getUserLoginId();
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
     * 获取用户的登录标识
     */
    private void getUserLoginId() {
        user_id = Tools.getUserLogin(this);
    }

    /**
     * 解绑用户信息
     */
    public void unBindUserInfo() {
        if (user_id != null && !TextUtils.isEmpty(user_id)) {
            map = new HashMap<>();
            map.put("Id", user_id);
        }
    }

    /**
     * @return 是否登录
     */
    private boolean isLogin() {
        return CacheUtils.getBoolean(SetActivity.this, CacheUtils.LOGIN_STATE, false);
    }

    /**
     * @return 是否绑定淘宝
     */
    private boolean isBind() {
        return CacheUtils.getBoolean(SetActivity.this, CacheUtils.BIND_STATE, false);
    }

    /**
     * @return 是否绑定支付宝
     */
    private boolean isPay() {
        return CacheUtils.getBoolean(SetActivity.this, CacheUtils.PAY_STATE, false);
    }


    /**
     * 设置用户支付宝账号新
     */
    private void showAlipayAccount() {
        View inflate = View.inflate(this, R.layout.bind_alipay_dailog, null);
        final EditText etAccount = (EditText) inflate.findViewById(R.id.et_pay_account);
        final EditText etName = (EditText) inflate.findViewById(R.id.et_pay_name);
        final EditText etSmsCode = (EditText) inflate.findViewById(R.id.et_alipay_smscode);
        final TextView tvAlipayGetSms = (TextView) inflate.findViewById(R.id.tv_alipay_getSms);
        TextView tvBindAlipayCancel = (TextView) inflate.findViewById(R.id.tv_bind_alipay_cancel);
        TextView tvBindAlipayConfirm = (TextView) inflate.findViewById(R.id.tv_bind_alipay_confirm);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.setView(inflate).create();
        etAccount.addTextChangedListener(new MyTextWatcher());
        if (SetActivity.this != null && !SetActivity.this.isFinishing()) {
            alertDialog.show();
        }
        tvAlipayGetSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_account = etAccount.getText().toString().trim();
                String userPhone = Tools.getUserPhone(SetActivity.this);
                LogUtil.e(userPhone);
                getSmsCode(userPhone, user_account, tvAlipayGetSms);
            }
        });

        tvBindAlipayCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alertDialog != null && alertDialog.isShowing()) {
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer = null;
                    }
                    alertDialog.dismiss();
                }
            }
        });
        tvBindAlipayConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account = etAccount.getText().toString().trim();
                name = etName.getText().toString().trim();
                String smscode = etSmsCode.getText().toString().trim();
                if (!Check.isTelPhoneNumber(account) && !Check.isEmail(account)) {
                    Toast.makeText(SetActivity.this, "输入的支付宝账号不能为空而且只能为正确的手机号码或者邮箱，请您重新输入！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Check.isLegalName(name)) {
                    Toast.makeText(SetActivity.this, "输入用户名不能为空而且只能为中文真实姓名，请您重新输入！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(smscode)) {
                    Toast.makeText(SetActivity.this, "请输入短信验证码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (smscode.equals(sms_code_s)) {
                    bindPay(account, name);
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer = null;
                    }
                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }

                } else {
                    Toast.makeText(SetActivity.this, "短信验证码不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });

    }

    /**
     * 绑定支付宝
     */
    private void bindPay(String account, String name) {
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(name)) {
            Toast.makeText(SetActivity.this, "支付宝账号和密码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            if (isLogin()) {
                getUserLoginId();
                Map<String, String> map = new HashMap<>();
                map.put("Id", user_id);
                map.put("alipay", account);
                map.put("alipayName", name);
                which = "pay";
                netUtils.okHttp2Server2(SetActivity.this, pay_url, map);
            } else {
                loginUser();
            }
        }
    }

    /**
     * 登录账号
     */
    private void loginUser() {
        Intent intent = new Intent(SetActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 解绑支付宝
     */
    private void unBindPay() {
        if (isLogin()) {
            getUserLoginId();
            Map map = new HashMap<String, String>();
            map.put("Id", user_id);
            which = "unpay";
            netUtils.okHttp2Server2(SetActivity.this, unpay_url, map);
        } else {
            loginUser();
        }
    }

    /**
     * 获取验证码的方法
     */
    private void getSmsCode(String userPhone, String user, final TextView tv) {

        tv.setClickable(false);
        tv.setTextColor(Color.parseColor("#ffffff"));
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tv.setText(l / 1000 + "s后重新获取");
            }

            @Override
            public void onFinish() {
                tv.setClickable(true);
                tv.setText("获取验证码");
                tv.setTextColor(Color.parseColor("#ffffff"));
            }
        };
        which = "sms";
        map = new HashMap<>();
        map.put("Id", user_id);
        map.put("sms_code", userPhone);
        map.put("type", "3");
        netUtils.okHttp2Server2(SetActivity.this, sms_url, map);
        if (countDownTimer != null) {
            countDownTimer.start();
        }
    }

    public void onEvent(AnyEvent event) {
        if (event.getType() == EventType.UNBIND_ALIPAY) {
            String msg = "onEventMainThread收到了消息：" + event.getMessage();
            LogUtil.e(msg);
            initData();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        EventBus.getDefault().unregister(this);
    }
}
