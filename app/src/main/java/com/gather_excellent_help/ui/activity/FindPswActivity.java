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

public class FindPswActivity extends Activity {

    @Bind(R.id.et_findpsw_user)
    EditText etFindpswUser;
    @Bind(R.id.et_findpsw_smscode)
    EditText etFindpswSmscode;
    @Bind(R.id.tv_findpsw_getSms)
    TextView tvFindpswGetSms;
    @Bind(R.id.tv_login)
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_find_psw);
        ButterKnife.bind(this);
    }
}
