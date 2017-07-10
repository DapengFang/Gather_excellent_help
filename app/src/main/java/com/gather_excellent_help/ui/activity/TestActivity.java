package com.gather_excellent_help.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.gather_excellent_help.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @Bind(R.id.btn_bind_taobao)
    Button btnBindTaobao;
    @Bind(R.id.activity_test)
    LinearLayout activityTest;
    @Bind(R.id.btn_unbind_taobao)
    Button btnUnbindTaobao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        btnBindTaobao.setOnClickListener(new MyOnClickListener());
        btnUnbindTaobao.setOnClickListener(new MyOnClickListener());
    }

    /**
     * 登录
     */
    public void bindTaobao() {

        AlibcLogin alibcLogin = AlibcLogin.getInstance();

        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                Toast.makeText(TestActivity.this, "登录成功 ",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(TestActivity.this, "登录失败 ",
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * 退出登录
     */
    public void unBindTaobao() {

        AlibcLogin alibcLogin = AlibcLogin.getInstance();

        alibcLogin.logout(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                Toast.makeText(TestActivity.this, "登出成功 ",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(TestActivity.this, "登录失败 ",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_bind_taobao :
                      bindTaobao();
                    break;
                case R.id.btn_unbind_taobao:
                      unBindTaobao();
                    break;
            }
        }
    }
}
