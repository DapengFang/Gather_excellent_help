package com.gather_excellent_help.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.EncryptUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class SetBindPswActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;
    private EditText et_bind_setpsw_psw;
    private TextView tv_bind_setpsw_submit;

    private String url = Url.BASE_URL + "findBack.aspx";
    private Map<String,String> map;
    private NetUtil netUtil;
    private String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_bind_psw);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout)findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView)findViewById(R.id.tv_top_title_name);
        et_bind_setpsw_psw = (EditText)findViewById(R.id.et_bind_setpsw_psw);
        tv_bind_setpsw_submit = (TextView)findViewById(R.id.tv_bind_setpsw_submit);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        tv_top_title_name.setText("设置密码");
        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        netUtil = new NetUtil();
        netUtil.setOnServerResponseListener(new OnServerResponseListener());
        MyonclickListener myonclickListener = new MyonclickListener();
        tv_bind_setpsw_submit.setOnClickListener(myonclickListener);
    }

    /**
     * 监听全局页面的监听
     */
    public class MyonclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_bind_setpsw_submit :
                    toSetPsw();
                    break;
            }
        }
    }

    /**
     * 设置密码
     */
    private void toSetPsw() {
        String psw = et_bind_setpsw_psw.getText().toString().trim();
        if (TextUtils.isEmpty(psw)) {
            Toast.makeText(SetBindPswActivity.this, "请输入新密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (psw.length() < 6) {
            Toast.makeText(SetBindPswActivity.this, "请输入6位到12位的密码，谢谢", Toast.LENGTH_SHORT).show();
        } else {
            if(user == null) {
                user = "";
            }
            psw = psw + "@@11fe468";
            psw = EncryptUtil.getMd5Value(psw);
            map = new HashMap<>();
            map.put("userName", user);
            map.put("newPassword", psw);
            netUtil.okHttp2Server2(url,map);
        }

    }

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener{

        @Override
        public void getSuccessResponse(String response) {
            Gson gson = new Gson();
            CodeStatueBean codeStatueBean = gson.fromJson(response, CodeStatueBean.class);
            int statusCode = codeStatueBean.getStatusCode();
            switch (statusCode) {
                case 0:
                    Toast.makeText(SetBindPswActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(SetBindPswActivity.this, "密码设置成功！", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(e.getMessage());
        }
    }
}
