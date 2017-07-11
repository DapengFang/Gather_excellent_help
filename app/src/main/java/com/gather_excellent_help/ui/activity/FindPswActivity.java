package com.gather_excellent_help.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeBean;
import com.gather_excellent_help.ui.lisetener.MyTextWatcher;
import com.gather_excellent_help.utils.EncryptUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class FindPswActivity extends Activity {

    @Bind(R.id.et_findpsw_user)
    EditText etFindpswUser;
    @Bind(R.id.et_findpsw_smscode)
    EditText etFindpswSmscode;
    @Bind(R.id.tv_findpsw_getSms)
    TextView tvFindpswGetSms;
    @Bind(R.id.tv_update)
    TextView tvUpdate;
    @Bind(R.id.et_findpsw_newPsw)
    EditText etFindpswNewPsw;

    private String user;
    private String password;
    private NetUtil netUtils;
    private Map<String,String> map;
    private String url = Url.BASE_URL + "findBack.aspx";
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_find_psw);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        netUtils = new NetUtil();
        etFindpswNewPsw.addTextChangedListener(new MyTextWatcher());
        tvFindpswGetSms.setOnClickListener(new MyOnClickListener());
        tvUpdate.setOnClickListener(new MyOnClickListener());
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
     * 解析数据
     * @param response
     */
    private void parseData(String response) {
        Gson gson = new Gson();
        CodeBean registerBean = gson.fromJson(response, CodeBean.class);
        int statusCode = registerBean.getStatusCode();
        switch (statusCode) {
            case 0 :
                Toast.makeText(FindPswActivity.this, "修改失败！"+registerBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(FindPswActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 获取用户手机号
     */
    private void getPhone(){
        user = etFindpswUser.getText().toString().trim();
    };

    /**
     * 获取用户输入的信息
     */
    private void getUserInputInfo() {
        user = etFindpswUser.getText().toString().trim();
        password = etFindpswNewPsw.getText().toString().trim();
        password = password+"@@11fe468";
        password = EncryptUtil.getMd5Value(password);
        map= new HashMap<>();
        map.put("userName",user);
        map.put("newPassword",password);
    }

    /**
     * 获取验证码的方法
     */
    private void getSmsCode() {
        getPhone();
        if(user==null && TextUtils.isEmpty(user)) {
            Toast.makeText(FindPswActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        tvFindpswGetSms.setClickable(false);
        tvFindpswGetSms.setTextColor(Color.parseColor("#ffffff"));
        countDownTimer =new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long l) {
                tvFindpswGetSms.setText(l/1000+"s后重新获取");
            }

            @Override
            public void onFinish() {
                tvFindpswGetSms.setClickable(true);
                tvFindpswGetSms.setText("获取验证码");
                tvFindpswGetSms.setTextColor(Color.parseColor("#ffffff"));
            }
        };
        countDownTimer.start();

    }


    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
              case R.id.tv_update:
                  getUserInputInfo();
                  netUtils.okHttp2Server2(url,map);
                  break;
              case R.id.tv_findpsw_getSms:
                  getSmsCode();
                  break;
            }
        }
    }
}
