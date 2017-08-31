package com.gather_excellent_help.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.SmsCodeBean;
import com.gather_excellent_help.ui.lisetener.MyTextWatcher;
import com.gather_excellent_help.utils.Check;
import com.gather_excellent_help.utils.EncryptUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class FindPswActivity extends Activity {

    @Bind(R.id.et_findpsw_user)
    EditText etFindpswUser;
    @Bind(R.id.et_findpsw_smscode)
    EditText etFindpswSmscode;
    @Bind(R.id.tv_findpsw_getSms)
    TextView tvFindpswGetSms;
    @Bind(R.id.tv_update)
    TextView tvUpdate;
    @Bind(R.id.et_findpsw_newPsw)
    EditText etFindpswNewPsw;
    @Bind(R.id.rl_back)
    RelativeLayout rlBack;

    private String user;
    private String password;
    private String smscode = "";
    private NetUtil netUtils;
    private Map<String, String> map;
    private String url = Url.BASE_URL + "findBack.aspx";
    private CountDownTimer countDownTimer;
    private String sms_url = Url.BASE_URL + "GetRandom.aspx";
    private String whick = "";
    private String sms_code_s = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_find_psw);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        netUtils = new NetUtil();
        etFindpswNewPsw.addTextChangedListener(new MyTextWatcher());
        tvFindpswGetSms.setOnClickListener(new MyOnClickListener());
        tvUpdate.setOnClickListener(new MyOnClickListener());
        rlBack.setOnClickListener(new MyOnClickListener());
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                if (whick.equals("register")) {
                    parseData(response);
                } else if (whick.equals("sms")) {
                    parseSmsData(response);
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "," + e.getMessage());
            }
        });
    }

    private void parseSmsData(String response) {
        LogUtil.e(response);
        SmsCodeBean smsCodeBean = new Gson().fromJson(response, SmsCodeBean.class);
        int statusCode = smsCodeBean.getStatusCode();
        switch (statusCode) {
            case 0:
                Toast.makeText(FindPswActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                List<SmsCodeBean.DataBean> data = smsCodeBean.getData();
                if (data.size() > 0) {
                    sms_code_s = data.get(0).getSms_code();
                }
                Toast.makeText(FindPswActivity.this, "验证码已发送你的手机，请查收！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析数据
     *
     * @param response
     */
    private void parseData(String response) {
        Gson gson = new Gson();
        CodeStatueBean codeStatueBean = gson.fromJson(response, CodeStatueBean.class);
        int statusCode = codeStatueBean.getStatusCode();
        switch (statusCode) {
            case 0:
                Toast.makeText(FindPswActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(FindPswActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    /**
     * 获取用户手机号
     */
    private void getPhone() {
        user = etFindpswUser.getText().toString().trim();
    }

    ;

    /**
     * 获取用户输入的信息
     */
    private void getUserInputInfo() {
        user = etFindpswUser.getText().toString().trim();
        boolean matches = user.matches(Check.c_phone);
        password = etFindpswNewPsw.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            Toast.makeText(FindPswActivity.this, "请输入手机号码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(FindPswActivity.this, "请输入新密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6 || password.matches(Check.p_sample) || password.matches(Check.p_simple)) {
            Toast.makeText(FindPswActivity.this, "新密码过于简单，为了您的账号安全，请输入6位到12位不同数字或字母和数字的组合！", Toast.LENGTH_SHORT).show();
        } else {
            password = password + "@@11fe468";
            password = EncryptUtil.getMd5Value(password);
            smscode = etFindpswSmscode.getText().toString().trim();
            map = new HashMap<>();
            map.put("userName", user);
            map.put("newPassword", password);
        }
    }

    /**
     * 获取验证码的方法
     */
    private void getSmsCode() {
        getPhone();
        if (user == null || TextUtils.isEmpty(user)) {
            Toast.makeText(FindPswActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        tvFindpswGetSms.setClickable(false);
        tvFindpswGetSms.setTextColor(Color.parseColor("#a9a9a9"));
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tvFindpswGetSms.setText(l / 1000 + "s后重新获取");
            }

            @Override
            public void onFinish() {
                tvFindpswGetSms.setClickable(true);
                tvFindpswGetSms.setText("获取验证码");
                tvFindpswGetSms.setTextColor(Color.parseColor("#a9a9a9"));
            }
        };
        whick = "sms";
        map = new HashMap<>();
        map.put("sms_code", user);
        map.put("type", "2");
        netUtils.okHttp2Server2(sms_url, map);
        countDownTimer.start();

    }


    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.tv_update:
                    getUserInputInfo();
                    if (smscode.equals(sms_code_s)) {
                        whick = "register";
                        netUtils.okHttp2Server2(url, map);
                    } else {
                        Toast.makeText(FindPswActivity.this, "短信验证码不正确，请从新输入！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.tv_findpsw_getSms:
                    getSmsCode();
                    break;
                case R.id.rl_back:
                    finish();
                    break;
            }
        }
    }

}
