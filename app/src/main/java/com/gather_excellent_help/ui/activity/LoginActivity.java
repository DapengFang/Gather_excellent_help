package com.gather_excellent_help.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gather_excellent_help.R;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    private String user;
    private String password;

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
        getUserInputInfo();
        tvLogin.setOnClickListener(new MyOnClickListener());
    }

    /**
     * 获取用户输入的信息
     */
    private void getUserInputInfo() {
        user = etLoginUser.getText().toString().trim();
        password = etLoginPsw.getText().toString().trim();
    }

    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_login :

                    break;
            }
        }
    }
}
