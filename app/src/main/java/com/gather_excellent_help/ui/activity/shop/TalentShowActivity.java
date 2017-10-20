package com.gather_excellent_help.ui.activity.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.umeng.socialize.media.Base;

public class TalentShowActivity extends BaseActivity {

    private ImageView iv_zhuangtai_exit;
    private TextView tv_top_title_name;
    private TextView tv_talent_submit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent_show);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_zhuangtai_exit = (ImageView)findViewById(R.id.iv_zhuangtai_exit);
        tv_top_title_name = (TextView)findViewById(R.id.tv_top_title_name);
        tv_talent_submit = (TextView)findViewById(R.id.tv_talent_submit);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        tv_top_title_name.setText("微达人");
        iv_zhuangtai_exit.setVisibility(View.GONE);
        tv_talent_submit.setOnClickListener(new MyOnclickListener());
    }

    public class MyOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_talent_submit:
                    finish();
                    break;
            }
        }
    }


}
