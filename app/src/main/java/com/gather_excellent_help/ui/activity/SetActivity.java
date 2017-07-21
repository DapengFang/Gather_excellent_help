package com.gather_excellent_help.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.DataCleanManager;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class SetActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.iv_zhuangtai_share)
    ImageView ivZhuangtaiShare;
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
    @Bind(R.id.tv_set_clear_cache)
    TextView tvSetClearCache;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (isLogin()) {
            tvSetLogout.setVisibility(View.VISIBLE);
        } else {
            tvSetLogout.setVisibility(View.GONE);
        }
        try {
            String totalCacheSize = DataCleanManager.getTotalCacheSize(this);
            tvSetClearCache.setText("清理缓存 ("+totalCacheSize +")");
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
                LogUtil.e(response);
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1:
                        if (which.equals("bind")) {
                            Toast.makeText(SetActivity.this, "绑定成功！", Toast.LENGTH_LONG).show();
                            EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN, "绑定成功！"));
                        } else if (which.equals("unbind")) {
                            Toast.makeText(SetActivity.this, "解绑成功！", Toast.LENGTH_LONG).show();
                            EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN, "解绑成功！"));
                        } else if (which.equals("pay")) {
                            CacheUtils.putBoolean(SetActivity.this, CacheUtils.PAY_STATE, true);
                        } else if (which.equals("unpay")) {
                            CacheUtils.putBoolean(SetActivity.this, CacheUtils.PAY_STATE, false);
                        }
                        break;
                    case 0:
                        Toast.makeText(SetActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "," + e.getMessage());
            }
        });
    }

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
                        unBindPay();
                    } else {
                        showPayAcount();
                    }
                    break;
                case R.id.rl_set_bindtaobao:
                    if (isBind()) {
                        unBindTaobao();
                    } else {
                        bindTaobao();
                    }
                    break;
                case R.id.rl_set_updatepsw:
                    intent = new Intent(SetActivity.this, FindPswActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rl_set_about:

                    break;
                case R.id.rl_set_cache:
                    showCacheClearDialog();
                    break;
                case R.id.tv_set_logout:
                    CacheUtils.putBoolean(SetActivity.this, CacheUtils.LOGIN_STATE, false);
                    EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN, "退出登录！"));
                    finish();
                    loginUser();
                    break;
                case R.id.rl_set_userinfo:
                    intent = new Intent(SetActivity.this, PersonInfoActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    /**
     * 清理缓存的dialog
     */
    private void showCacheClearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("温馨提示")
                .setMessage("此操作将会删除你的登录保存信息以及个人信息,请慎重操作。")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            DataCleanManager.cleanApplicationCache(SetActivity.this);
                            String totalCacheSize = DataCleanManager.getTotalCacheSize(SetActivity.this);
                            tvSetClearCache.setText("清理缓存 ("+totalCacheSize+")");
                            CacheUtils.putBoolean(SetActivity.this,CacheUtils.LOGIN_STATE,false);
                            EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN, "清理缓存!"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton("取消", null)
                .show();
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
                CacheUtils.putBoolean(SetActivity.this, CacheUtils.BIND_STATE, true);
                openId = AlibcLogin.getInstance().getSession().openId;
                avatarUrl = AlibcLogin.getInstance().getSession().avatarUrl;
                nick = AlibcLogin.getInstance().getSession().nick;
                uploadUserInfo();
                which = "bind";
                netUtils.okHttp2Server2(bind_url, map);
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
                CacheUtils.putBoolean(SetActivity.this, CacheUtils.BIND_STATE, false);
                unBindUserInfo();
                which = "unbind";
                netUtils.okHttp2Server2(unbind_url, map);
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

    private void getUserLoginId() {
        user_id = CacheUtils.getString(SetActivity.this, CacheUtils.LOGIN_VALUE, "");
    }

    /**
     * 解绑用户信息
     */
    public void unBindUserInfo() {
        isLogin();
        if (isLogin()) {
            user_id = CacheUtils.getString(SetActivity.this, CacheUtils.LOGIN_VALUE, "");
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
     * 设置用户支付宝账号
     */
    private void showPayAcount() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.item_pay_account, null);
        final EditText etAccount = (EditText) view.findViewById(R.id.et_pay_account);
        final EditText etName = (EditText) view.findViewById(R.id.et_pay_name);
        builder.setTitle("绑定支付宝")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String account = etAccount.getText().toString().trim();
                        String name = etName.getText().toString().trim();
                        bindPay(account, name);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
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
                Map map = new HashMap<String, String>();
                map.put("Id", user_id);
                map.put("alipay", account);
                map.put("alipayName", name);
                which = "pay";
                netUtils.okHttp2Server2(pay_url, map);
            } else {
                loginUser();
            }
        }
    }

    /**
     * 登录账号
     */
    private void loginUser() {
        Toast.makeText(SetActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
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
            netUtils.okHttp2Server2(unpay_url, map);
        } else {
            loginUser();
        }
    }
}
