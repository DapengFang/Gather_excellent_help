package com.gather_excellent_help.ui.activity;

import android.app.Activity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }
}
