package com.gather_excellent_help.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.utils.NetUtils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.alibaba.baichuan.android.trade.AlibcContext.initData;

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
    private NetUtils netUtils;
    private String url;
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
        getUserInputParams();
        hashMap = new HashMap<>();
        tvRegisterGetSms.setOnClickListener(new MyOnclickListener());
        tvRegister.setOnClickListener(new MyOnclickListener());
    }

    /**
     * 获取验证码的方法
     */
    private void getSmsCode() {
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
     * 获取用户输入的信息
     */
    private void getUserInputParams() {
        phone = etRegisterUser.getText().toString().trim();
        password = etRegisterPsw.getText().toString().trim();
        smscode = etRegisterSmscode.getText().toString().trim();
    }

    public class MyOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_register :

                    break;
                case R.id.tv_register_getSms:
                    getSmsCode();
                    break;
            }
        }
    }
}
