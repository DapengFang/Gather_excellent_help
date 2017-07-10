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
import com.gather_excellent_help.bean.CodeBean;
import com.gather_excellent_help.ui.lisetener.MyTextWatcher;
import com.gather_excellent_help.utils.EncryptUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.HashMap;

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
    private String smscode;
    private NetUtil netUtils;
    private String url = "http://192.168.200.100:8080/api/juyoubang/register.aspx";
    private HashMap<String, String> hashMap;
    private CountDownTimer countDownTimer;

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
                parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
               LogUtil.e(call.toString()+","+e.getMessage());
            }
        });
    }

    /**
     * 解析数据
     * @param response
     */
    private void parseData(String response) {
        Gson gson = new Gson();
        CodeBean registerBean = gson.fromJson(response, CodeBean.class);
        int statusCode = registerBean.getStatusCode();
        switch (statusCode) {
            case 0 :
                Toast.makeText(RegisterActivity.this, "注册失败！"+registerBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 获取验证码的方法
     */
    private void getSmsCode() {
        getPhone();
        if(phone==null && TextUtils.isEmpty(phone)) {
            Toast.makeText(RegisterActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
                tvRegisterGetSms.setClickable(false);
                tvRegisterGetSms.setTextColor(Color.parseColor("#ffffff"));
                countDownTimer =new CountDownTimer(60*1000,1000) {
                    @Override
                    public void onTick(long l) {
                        tvRegisterGetSms.setText(l/1000+"s后重新获取");
                    }

                    @Override
                    public void onFinish() {
                        tvRegisterGetSms.setClickable(true);
                        tvRegisterGetSms.setText("获取验证码");
                        tvRegisterGetSms.setTextColor(Color.parseColor("#ffffff"));
                    }
                };
                countDownTimer.start();

    }

    /**
     * 获取用户手机号
     */
    private void getPhone(){
        phone = etRegisterUser.getText().toString().trim();
    };

    /**
     * 获取用户输入的信息
     */
    private void getUserInputParams() {
        phone = etRegisterUser.getText().toString().trim();
        password = etRegisterPsw.getText().toString().trim();
        password = password+"@@11fe468";
        password = EncryptUtil.getMd5Value(password);
        smscode = etRegisterSmscode.getText().toString().trim();
        LogUtil.e(phone+"=="+password);
        hashMap = new HashMap<>();
        hashMap.put("UserName",phone);
        hashMap.put("Password",password);
    }

    public class MyOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_register :
                     getUserInputParams();
                     netUtils.okHttp2Server2(url,hashMap);

//                    OkHttpUtils.get().url(url)
//                            .addParams("UserName","55554646")
//                            .addParams("Password","dsa215121212")
//                            .build().execute(new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//
//                        }
//
//                        @Override
//                        public void onResponse(String response, int id) {
//                            LogUtil.e(response);
//                        }
//                    });

                    break;
                case R.id.tv_register_getSms:
                    getSmsCode();
                    break;
            }
        }
    }
}
