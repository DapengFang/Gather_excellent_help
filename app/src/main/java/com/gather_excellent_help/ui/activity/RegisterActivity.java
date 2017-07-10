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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }
}
