package com.gather_excellent_help.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gather_excellent_help.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @Bind(R.id.btn_bind_taobao)
    Button btnBindTaobao;
    @Bind(R.id.activity_test)
    LinearLayout activityTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        btnBindTaobao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindTaoBao();
            }
        });
    }

    /**
     * 绑定淘宝
     */
    private void bindTaoBao() {

    }
}
