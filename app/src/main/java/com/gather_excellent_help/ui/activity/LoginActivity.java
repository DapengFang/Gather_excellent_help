package com.gather_excellent_help.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.lisetener.MyTextWatcher;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.EncryptUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

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
    private NetUtil netUtils;
    private Map<String,String> map;
    private String url = Url.BASE_URL + "login.aspx";

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
        netUtils = new NetUtil();
        etLoginPsw.addTextChangedListener(new MyTextWatcher());
        tvLogin.setOnClickListener(new MyOnClickListener());
        tvRegister.setOnClickListener(new MyOnClickListener());
        tvLoginLostpsw.setOnClickListener(new MyOnClickListener());
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
     * 解析联网返回数据
     * @param response
     */
    private void parseData(String response) {
        Gson gson = new Gson();
        CodeBean codeBean = gson.fromJson(response, CodeBean.class);
        int statusCode = codeBean.getStatusCode();
        int id = codeBean.getData().get(0).getId();
        switch (statusCode){
            case 0:
                Toast.makeText(LoginActivity.this, "登录失败" + codeBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(LoginActivity.this, codeBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                CacheUtils.putBoolean(LoginActivity.this,CacheUtils.LOGIN_STATE,true);
                CacheUtils.putInteger(LoginActivity.this,CacheUtils.LOGIN_VALUE,id);
                EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN,"登录成功！"));
                break;
        }
    }

    /**
     * 获取用户输入的信息
     */
    private void getUserInputInfo() {
        user = etLoginUser.getText().toString().trim();
        password = etLoginPsw.getText().toString().trim();
        password = password+"@@11fe468";
        password = EncryptUtil.getMd5Value(password);
        map= new HashMap<>();
        map.put("UserName",user);
        map.put("Password",password);
    }

    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent =null;
            switch (view.getId()) {
                case R.id.tv_login :
                    getUserInputInfo();
                    netUtils.okHttp2Server2(url,map);
                    break;
                case R.id.tv_register :
                    intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.tv_login_lostpsw:
                    intent = new Intent(LoginActivity.this, FindPswActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
