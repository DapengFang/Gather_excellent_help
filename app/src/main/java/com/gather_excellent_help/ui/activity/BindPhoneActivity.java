package com.gather_excellent_help.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.TestActivity;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.BindPhoneBean;
import com.gather_excellent_help.bean.CodeBean;
import com.gather_excellent_help.bean.SmsCodeBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.credits.ExtractCreditsActivity;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
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
    private String bind_url = Url.BASE_URL + "WeChatLoginPhone.aspx";
    private String whick = "";
    private Map<String, String> hashMap;
    private NetUtil netUtils;
    private String sms_code_s = "";
    private String wechat_json = "";
    private String phone = "";


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
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
        et_bind_phone = (EditText) findViewById(R.id.et_bind_phone);
        et_bind_sms = (EditText) findViewById(R.id.et_bind_sms);
        tv_bind_phone_getsms = (TextView) findViewById(R.id.tv_bind_phone_getsms);
        tv_bind_phone_submit = (TextView) findViewById(R.id.tv_bind_phone_submit);
    }

    private void initData() {
        Intent intent = getIntent();
        wechat_json = intent.getStringExtra("wechat_json");
        tv_top_title_name.setText("绑定手机号");
        rl_exit.setVisibility(View.GONE);
        netUtils = new NetUtil();
        netUtils.setOnServerResponseListener(new OnServerResponseListener());
        MyonclickListener myonclickListener = new MyonclickListener();
        et_bind_phone.setOnClickListener(myonclickListener);
        rl_exit.setOnClickListener(myonclickListener);
        tv_bind_phone_getsms.setOnClickListener(myonclickListener);
        tv_bind_phone_submit.setOnClickListener(myonclickListener);
    }

    /**
     * 监听页面上全局的点击事件
     */
    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.et_bind_phone:
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
        phone = et_bind_phone.getText().toString().trim();
        String sms = et_bind_sms.getText().toString().trim();
        if (phone == null || TextUtils.isEmpty(phone)) {
            Toast.makeText(BindPhoneActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sms == null || TextUtils.isEmpty(sms)) {
            Toast.makeText(BindPhoneActivity.this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!sms.equals(sms_code_s)) {
            Toast.makeText(BindPhoneActivity.this, "输入验证码不正确，请重新输入！", Toast.LENGTH_SHORT).show();
            et_bind_sms.setText("");
            return;
        }
        tv_bind_phone_submit.setClickable(false);
        //联网请求绑定手机号
        whick = "bind";
        hashMap = new HashMap<>();
        hashMap.put("phone_number", phone);
        hashMap.put("wechat_json", wechat_json);
        netUtils.okHttp2Server2(bind_url, hashMap);
    }

    /**
     * 获取验证码的方法
     */
    private void getSmsCode() {
        phone =  et_bind_phone.getText().toString().trim();
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
        hashMap.put("type", "5");
        netUtils.okHttp2Server2(sms_url, hashMap);
        if (countDownTimer != null) {
            countDownTimer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    /**
     * 联网请求后的回调监听
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
            if (whick.equals("bind")) {
                tv_bind_phone_submit.setClickable(true);
                parseBindData(response);
            } else if (whick.equals("sms")) {
                parseSmsData(response);
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN,"登录成功！"));
            tv_bind_phone_submit.setClickable(true);
            LogUtil.e("网络连接出现问题~");
        }
    }

    /**
     * 解析绑定手机的数据
     *
     * @param response 绑定手机号码的返回数据
     */
    private void parseBindData(String response) {
        LogUtil.e(response);
        CodeBean codeBean = new Gson().fromJson(response, CodeBean.class);
        int statusCode = codeBean.getStatusCode();
        switch (statusCode) {
            case 1:
                List<CodeBean.DataBean> data = codeBean.getData();
                if(data.size()>0) {
                    Integer id = data.get(0).getId();
                    int group_type = data.get(0).getGroup_type();
                    double user_rate = data.get(0).getUser_get_ratio();
                    String advertising = data.get(0).getAdvertising();
                    int group_id = data.get(0).getGroup_id();
                    int is_phone = data.get(0).getIs_phone();
                    String use_phone = data.get(0).getUse_phone();
                    CacheUtils.putBoolean(BindPhoneActivity.this, CacheUtils.LOGIN_STATE, true);
                    CacheUtils.putString(BindPhoneActivity.this, CacheUtils.LOGIN_VALUE, id + "");
                    CacheUtils.putInteger(BindPhoneActivity.this, CacheUtils.SHOP_TYPE, group_type);
                    CacheUtils.putString(BindPhoneActivity.this, CacheUtils.LOGIN_PHONE, use_phone);
                    CacheUtils.putString(BindPhoneActivity.this, CacheUtils.USER_RATE, user_rate + "");
                    CacheUtils.putInteger(BindPhoneActivity.this, CacheUtils.GROUP_TYPE, group_id);
                    if (advertising != null) {
                        CacheUtils.putString(BindPhoneActivity.this, CacheUtils.ADVER_ID, advertising);
                    }
                    if (is_phone == 1) {
                        Toast.makeText(BindPhoneActivity.this, "检测到您绑定的手机号码之前注册过，您可以直接通过之前的密码进行登录操作。", Toast.LENGTH_SHORT).show();
                    } else if (is_phone == 0) {
                        //设置密码
                        Intent intent = new Intent(BindPhoneActivity.this, SetBindPswActivity.class);
                        intent.putExtra("user", phone);
                        startActivity(intent);
                    }
                }
                EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN,"登录成功！"));
                finish();
                break;
            case 0:
                Toast.makeText(BindPhoneActivity.this, codeBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析短信的请求后的数据
     *
     * @param response
     */
    private void parseSmsData(String response) {
        LogUtil.e("sms = " + response);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}