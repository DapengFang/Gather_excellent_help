package com.gather_excellent_help.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.TestActivity;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.SmsCodeBean;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class BindPhoneActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;

    private EditText et_bind_phone;
    private EditText et_bind_sms;
    private TextView tv_bind_phone_getsms;
    private TextView tv_bind_phone_submit;
    private CountDownTimer countDownTimer;

    private String sms_url = Url.BASE_URL + "GetRandom.aspx";
    private String bind_url = Url.BASE_URL + "BindTel.aspx ";
    private String whick = "";
    private Map<String, String> hashMap;
    private NetUtil netUtils;
    private String sms_code_s = "";
    private String wechat_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout)findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView)findViewById(R.id.tv_top_title_name);
        et_bind_phone = (EditText)findViewById(R.id.et_bind_phone);
        et_bind_sms = (EditText)findViewById(R.id.et_bind_sms);
        tv_bind_phone_getsms = (TextView)findViewById(R.id.tv_bind_phone_getsms);
        tv_bind_phone_submit = (TextView)findViewById(R.id.tv_bind_phone_submit);
    }

    private void initData(){
        Intent intent = getIntent();
        wechat_id = intent.getStringExtra("wechat_id");
        tv_top_title_name.setText("绑定手机号");
        netUtils = new NetUtil();
        MyonclickListener myonclickListener = new MyonclickListener();
        et_bind_phone.setOnClickListener(myonclickListener);
        rl_exit.setOnClickListener(myonclickListener);
        tv_bind_phone_getsms.setOnClickListener(myonclickListener);
        tv_bind_phone_submit.setOnClickListener(myonclickListener);
    }

    public class MyonclickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.et_bind_phone :
                    et_bind_phone.setCursorVisible(true);
                    break;
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_bind_phone_getsms:
                    getSmsCode();
                    break;
                case R.id.tv_bind_phone_submit:
                    bindPhone();
                    break;
            }
        }
    }

    /**
     * 绑定手机号
     */
    private void bindPhone() {
        tv_bind_phone_submit.setClickable(false);
        String phone = et_bind_phone.getText().toString().trim();
        String sms = et_bind_sms.getText().toString().trim();
        if (phone == null || TextUtils.isEmpty(phone)) {
            Toast.makeText(BindPhoneActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sms == null || TextUtils.isEmpty(sms) ) {
            Toast.makeText(BindPhoneActivity.this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        whick = "bind";
        hashMap = new HashMap<>();
        hashMap.put("phone_number",phone);
        hashMap.put("wechat_id",wechat_id);
        netUtils.okHttp2Server(bind_url,hashMap);
    }

    /**
     * 获取验证码的方法
     */
    private void getSmsCode() {
        String phone = et_bind_phone.getText().toString().trim();
        if (phone == null || TextUtils.isEmpty(phone)) {
            Toast.makeText(BindPhoneActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        tv_bind_phone_getsms.setClickable(false);
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tv_bind_phone_getsms.setText(l / 1000 + "s后重新获取");
            }

            @Override
            public void onFinish() {
                tv_bind_phone_getsms.setClickable(true);
                tv_bind_phone_getsms.setText("获取验证码");
            }
        };

        whick = "sms";
        hashMap = new HashMap<>();
        hashMap.put("sms_code", phone);
        hashMap.put("type", "1");
        netUtils.okHttp2Server2(sms_url, hashMap);

        if(countDownTimer !=null) {
            countDownTimer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer!=null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    /**
     * 联网请求后的回调监听
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener{

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
            if(whick.equals("bind")) {
                tv_bind_phone_submit.setClickable(true);
                parseBindData(response);
            }else if(whick.equals("sms")) {
                parseSmsData(response);
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
           tv_bind_phone_submit.setClickable(true);
           LogUtil.e("网络连接出现问题~");
        }
    }

    /**
     * 解析绑定手机的数据
     * @param response 绑定手机号码的返回数据
     */
    private void parseBindData(String response) {
        LogUtil.e(response);
    }

    /**
     * 解析短信的请求后的数据
     * @param response
     */
    private void parseSmsData(String response) {
        LogUtil.e(response);
        SmsCodeBean smsCodeBean = new Gson().fromJson(response, SmsCodeBean.class);
        int statusCode = smsCodeBean.getStatusCode();
        switch (statusCode) {
            case 0:
                Toast.makeText(BindPhoneActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                List<SmsCodeBean.DataBean> data = smsCodeBean.getData();
                if (data.size() > 0) {
                    sms_code_s = data.get(0).getSms_code();
                }
                Toast.makeText(BindPhoneActivity.this, "验证码已发送你的手机，请查收！", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
