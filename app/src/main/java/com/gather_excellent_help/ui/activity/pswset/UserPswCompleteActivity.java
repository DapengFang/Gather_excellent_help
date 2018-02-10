package com.gather_excellent_help.ui.activity.pswset;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.MainActivity;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.Check;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.EncryptUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class UserPswCompleteActivity extends BaseActivity {

    @Bind(R.id.rl_back)
    RelativeLayout rlBack;
    @Bind(R.id.et_set_psw)
    EditText etSetPsw;
    @Bind(R.id.et_set_psw_again)
    EditText etSetPswAgain;
    @Bind(R.id.tv_pswset_complete)
    TextView tvPswsetComplete;

    private String phone;
    public static final int LOAD_DATA = 1; //加载数据的标识
    private String url = Url.BASE_URL + "UsersDevicebound.aspx";
    private NetUtil netUtil;
    private Map<String, String> map;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_DATA:
                    initData();
                    break;
            }
        }
    };
    private String psw;
    private String pswAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_psw_complete);
        ButterKnife.bind(this);
        handler.sendEmptyMessageDelayed(LOAD_DATA, 500);
    }

    private void initData() {
        netUtil = new NetUtil();
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        if (phone == null) {
            phone = "";
        }
        netUtil.setOnServerResponseListener(new OnServerResponseListener());
        rlBack.setOnClickListener(new MyOnclickListener());
        tvPswsetComplete.setOnClickListener(new MyOnclickListener());
    }

    public class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_back:
                    finish();
                    break;
                case R.id.tv_pswset_complete:
                    completePswSet();
                    break;
            }
        }
    }

    /**
     * 完成密码修改
     */
    private void completePswSet() {
        psw = etSetPsw.getText().toString().trim();
        pswAgain = etSetPswAgain.getText().toString().trim();
        if (TextUtils.isEmpty(psw)) {
            Toast.makeText(UserPswCompleteActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (psw.length() < 6 || psw.matches(Check.p_sample) || psw.matches(Check.p_simple)) {
            Toast.makeText(UserPswCompleteActivity.this, "输入密码过于简单，为了您的账号安全，请输入6位到12位不同数字或字母和数字的组合！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pswAgain)) {
            Toast.makeText(UserPswCompleteActivity.this, "请再次输入密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!psw.equals(pswAgain)) {
            Toast.makeText(UserPswCompleteActivity.this, "两次输入密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
            return;
        }

        String deviceId = Tools.getDeviceId(this);
        map = new HashMap<>();
        map.put("UserName", phone);
        map.put("Password", psw);
        map.put("device_number", deviceId);
        netUtil.okHttp2Server2(UserPswCompleteActivity.this,url, map);

    }

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
            CodeBean codeBean = new Gson().fromJson(response, CodeBean.class);
            int statusCode = codeBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    List<CodeBean.DataBean> data = codeBean.getData();
                    if (data.size() > 0) {
                        Integer id = data.get(0).getId();
                        int group_type = data.get(0).getGroup_type();
                        double user_rate = data.get(0).getUser_get_ratio();
                        String advertising = data.get(0).getAdvertising();
                        int group_id = data.get(0).getGroup_id();
                        CacheUtils.putBoolean(UserPswCompleteActivity.this, CacheUtils.LOGIN_STATE, true);
                        CacheUtils.putString(UserPswCompleteActivity.this, CacheUtils.LOGIN_VALUE, id + "");
                        CacheUtils.putInteger(UserPswCompleteActivity.this, CacheUtils.SHOP_TYPE, group_type);
                        CacheUtils.putString(UserPswCompleteActivity.this, CacheUtils.LOGIN_PHONE, phone);
                        CacheUtils.putString(UserPswCompleteActivity.this, CacheUtils.USER_RATE, user_rate + "");
                        CacheUtils.putInteger(UserPswCompleteActivity.this, CacheUtils.GROUP_TYPE, group_id);

                        if (advertising != null) {
                            CacheUtils.putString(UserPswCompleteActivity.this, CacheUtils.ADVER_ID, advertising);
                        }

                    }
                    Intent intent = new Intent(UserPswCompleteActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 0:
                    Toast.makeText(UserPswCompleteActivity.this, codeBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
            EncryptNetUtil.startNeterrorPage(UserPswCompleteActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
