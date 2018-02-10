package com.gather_excellent_help.ui.activity.pswset;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ali.auth.third.core.model.User;
import com.gather_excellent_help.MainActivity;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.SmsCodeBean;
import com.gather_excellent_help.ui.activity.RegisterActivity;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class UserPswsetActivity extends BaseActivity {

    @Bind(R.id.et_pswset_user)
    EditText etPswsetUser;
    @Bind(R.id.et_pswset_smscode)
    EditText etPswsetSmscode;
    @Bind(R.id.tv_pswset_getSms)
    TextView tvPswsetGetSms;
    @Bind(R.id.tv_pswset_confirm)
    TextView tvPswsetConfirm;
    @Bind(R.id.ll_user_pswset_skip)
    LinearLayout llUserPswsetSkip;


    public static final int LOAD_DATA = 1; //加载数据的标识
    
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case  LOAD_DATA:
                    initData();
                    break;
            }
        }
    };
    private CountDownTimer countDownTimer;
    private String phone;
    private Map<String, String> hashMap;
    private NetUtil netUtils;
    private String sms_url = Url.BASE_URL + "GetRandom.aspx";
    private String sms_code_s;
    private String smscode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pswset);
        ButterKnife.bind(this);
        handler.sendEmptyMessageDelayed(LOAD_DATA,500);
    }
    
    
    private void initData(){
        netUtils = new NetUtil();
        netUtils.setOnServerResponseListener(new OnServerResponseListener());
        tvPswsetGetSms.setOnClickListener(new MyOnClickListener());
        tvPswsetConfirm.setOnClickListener(new MyOnClickListener());
        llUserPswsetSkip.setOnClickListener(new MyOnClickListener());
    }

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener{

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
            SmsCodeBean smsCodeBean = new Gson().fromJson(response, SmsCodeBean.class);
            int statusCode = smsCodeBean.getStatusCode();
            switch (statusCode) {
                case 0:
                    Toast.makeText(UserPswsetActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    List<SmsCodeBean.DataBean> data = smsCodeBean.getData();
                    if (data.size() > 0) {
                        sms_code_s = data.get(0).getSms_code();
                    }
                    Toast.makeText(UserPswsetActivity.this, "验证码已发送你的手机，请查收！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "," + e.getMessage());
            EncryptNetUtil.startNeterrorPage(UserPswsetActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_pswset_getSms:
                    getSmsCode();
                    break;
                case R.id.tv_pswset_confirm:
                    toSetPsw();
                    break;
                case R.id.ll_user_pswset_skip:
                    toMainPager();
                    break;
            }
        }
    }

    private void toMainPager() {
        Intent intent = new Intent(UserPswsetActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(UserPswsetActivity.this, "请稍后！", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 跳转到设置密码界面
     */
    private void toSetPsw() {
        smscode = etPswsetSmscode.getText().toString().trim();
        phone = etPswsetUser.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(UserPswsetActivity.this, "请你输入正确的手机号码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(smscode)) {
            Toast.makeText(UserPswsetActivity.this, "请输入验证码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(sms_code_s!=null) {
            if (smscode.equals(sms_code_s)) {
                 Intent intent = new Intent(UserPswsetActivity.this, UserPswCompleteActivity.class);
                 intent.putExtra("phone",phone);
                 startActivity(intent);
            } else {
                Toast.makeText(UserPswsetActivity.this, "短信验证码不正确，请从新输入！", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * 获取验证码的方法
     */
    private void getSmsCode() {
        phone = etPswsetUser.getText().toString().trim();
        if (phone == null || TextUtils.isEmpty(phone)) {
            Toast.makeText(UserPswsetActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        tvPswsetGetSms.setClickable(false);
        tvPswsetGetSms.setTextColor(Color.parseColor("#a9a9a9"));
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tvPswsetGetSms.setText(l / 1000 + "s后重新获取");
            }

            @Override
            public void onFinish() {
                tvPswsetGetSms.setClickable(true);
                tvPswsetGetSms.setText("获取验证码");
                tvPswsetGetSms.setTextColor(Color.parseColor("#a9a9a9"));
            }
        };

        hashMap = new HashMap<>();
        hashMap.put("sms_code", phone);
        hashMap.put("type", "5");
        netUtils.okHttp2Server2(UserPswsetActivity.this,sms_url, hashMap);
        countDownTimer.start();
    }
}
