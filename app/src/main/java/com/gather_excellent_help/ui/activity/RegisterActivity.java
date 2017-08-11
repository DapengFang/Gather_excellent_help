package com.gather_excellent_help.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.SmsCodeBean;
import com.gather_excellent_help.bean.TaoWordBean;
import com.gather_excellent_help.ui.lisetener.MyTextWatcher;
import com.gather_excellent_help.utils.Check;
import com.gather_excellent_help.utils.EncryptUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class RegisterActivity extends Activity {

    @Bind(R.id.et_register_user)
    EditText etRegisterUser;
    @Bind(R.id.et_register_psw)
    EditText etRegisterPsw;
    @Bind(R.id.et_register_smscode)
    EditText etRegisterSmscode;
    @Bind(R.id.tv_register_getSms)
    TextView tvRegisterGetSms;
    @Bind(R.id.tv_register)
    TextView tvRegister;

    private String phone;
    private String password;
    private String smscode = "";
    private NetUtil netUtils;
    private String url = Url.BASE_URL + "register.aspx";
    private HashMap<String, String> hashMap;
    private CountDownTimer countDownTimer;
    private String sms_url = Url.BASE_URL + "GetRandom.aspx";
    private String whick = "";
    private String sms_code_s = "-1" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initDatas();
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        netUtils = new NetUtil();
        etRegisterPsw.addTextChangedListener(new MyTextWatcher());
        tvRegisterGetSms.setOnClickListener(new MyOnclickListener());
        tvRegister.setOnClickListener(new MyOnclickListener());
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                if(whick.equals("register")) {
                    parseData(response);
                }else if(whick.equals("sms")) {
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
                Toast.makeText(RegisterActivity.this, "获取验证码失败！" , Toast.LENGTH_SHORT).show();
                break;
            case 1:
                List<SmsCodeBean.DataBean> data = smsCodeBean.getData();
                if(data.size()>0) {
                    sms_code_s = data.get(0).getSms_code();
                }
                Toast.makeText(RegisterActivity.this, "验证码已发送你的手机，请查收！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析数据
     *
     * @param response
     */
    private void parseData(String response) {
        LogUtil.e("sms = "+response);
        Gson gson = new Gson();
        CodeBean registerBean = gson.fromJson(response, CodeBean.class);
        int statusCode = registerBean.getStatusCode();
        switch (statusCode) {
            case 0:
                Toast.makeText(RegisterActivity.this, "注册失败！" + registerBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    /**
     * 获取验证码的方法
     */
    private void getSmsCode() {
        getPhone();
        if (phone == null && TextUtils.isEmpty(phone)) {
            Toast.makeText(RegisterActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        tvRegisterGetSms.setClickable(false);
        tvRegisterGetSms.setTextColor(Color.parseColor("#ffffff"));
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tvRegisterGetSms.setText(l / 1000 + "s后重新获取");
            }

            @Override
            public void onFinish() {
                tvRegisterGetSms.setClickable(true);
                tvRegisterGetSms.setText("获取验证码");
                tvRegisterGetSms.setTextColor(Color.parseColor("#ffffff"));
            }
        };
        whick = "sms";
        hashMap = new HashMap<>();
        hashMap.put("sms_code",phone);
        hashMap.put("type","1");
        netUtils.okHttp2Server2(sms_url,hashMap);
        countDownTimer.start();

    }

    /**
     * 获取用户手机号
     */
    private void getPhone() {
        phone = etRegisterUser.getText().toString().trim();
    }

    ;

    /**
     * 获取用户输入的信息
     */
    private void getUserInputParams() {
        phone = etRegisterUser.getText().toString().trim();
        password = etRegisterPsw.getText().toString().trim();
        if(TextUtils.isEmpty(phone)) {
            Toast.makeText(RegisterActivity.this, "请你输入正确的手机号码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<6 || password.matches(Check.p_sample) || password.matches(Check.p_simple)) {
            Toast.makeText(RegisterActivity.this, "输入密码过于简单，为了您的账号安全，请输入6位到12位不同数字或字母和数字的组合！", Toast.LENGTH_SHORT).show();
            return;
        }
        password = password + "@@11fe468";
        password = EncryptUtil.getMd5Value(password);
        smscode = etRegisterSmscode.getText().toString().trim();
        if(TextUtils.isEmpty(smscode)) {
            Toast.makeText(RegisterActivity.this, "请输入验证码！", Toast.LENGTH_SHORT).show();
            return;
        }
        LogUtil.e(phone + "==" + password);
        hashMap = new HashMap<>();
        hashMap.put("UserName", phone);
        hashMap.put("Password", password);
    }

    public class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_register:
                    getUserInputParams();
                    if(smscode.equals(sms_code_s)) {
                        whick = "register";
                        netUtils.okHttp2Server2(url, hashMap);
                    }else{
                        Toast.makeText(RegisterActivity.this, "短信验证码不正确，请从新输入！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.tv_register_getSms:
                    getSmsCode();
                    break;
            }
        }
    }

}
