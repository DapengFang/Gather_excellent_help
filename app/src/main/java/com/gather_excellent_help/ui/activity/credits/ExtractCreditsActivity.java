package com.gather_excellent_help.ui.activity.credits;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.MineBean;
import com.gather_excellent_help.bean.SmsCodeBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.activity.SetActivity;
import com.gather_excellent_help.ui.activity.error.SuccessActivity;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class ExtractCreditsActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.tv_exteact_account)
    TextView tvExteactAccount;
    @Bind(R.id.et_extract_cash)
    EditText etExtractCash;
    @Bind(R.id.ll_extract_rule)
    LinearLayout llExtractRule;
    @Bind(R.id.tv_extract_credits_commit)
    TextView tvExtractCreditsCommit;

    private String sms_url = Url.BASE_URL + "GetRandom.aspx";
    private String pay_url = Url.BASE_URL + "BindAlipay.aspx";
    private String extract_url = Url.BASE_URL + "ExtractPoints.aspx";
    private Map<String, String> map;
    private NetUtil netUtil;
    private NetUtil netUtil2;
    private NetUtil netUtil3;

    private String mine_url = Url.BASE_URL + "Mine.aspx";
    private double amount;
    private CountDownTimer countDownTimer;
    private String account;
    private String userLogin;
    private String which = "";
    private String sms_code_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract_credits);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tvTopTitleName.setText("提取现金");
        netUtil = new NetUtil();
        netUtil2 = new NetUtil();
        netUtil3 = new NetUtil();
        getExtractCredits();
        netUtil3.setOnServerResponseListener(new OnNetutilResponseListener());
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1:
                        EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN, "提取成功！"));
                        getExtractCredits();
                        Intent intent = new Intent(ExtractCreditsActivity.this, SuccessActivity.class);
                        intent.putExtra("extract_type", "1");
                        intent.putExtra("extract_message", "提现成功");
                        startActivity(intent);
                        finish();
                        break;
                    case 0:
                        Intent intent2 = new Intent(ExtractCreditsActivity.this, SuccessActivity.class);
                        intent2.putExtra("extract_type", "0");
                        intent2.putExtra("extract_message", codeStatueBean.getStatusMessage());
                        startActivity(intent2);
                        break;
                }

            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                tvExtractCreditsCommit.setClickable(true);
                LogUtil.e(call.toString() + "--" + e.getMessage());
                EncryptNetUtil.startNeterrorPage(ExtractCreditsActivity.this);
            }
        });
        rlExit.setOnClickListener(new MyOnClickListener());
        tvExtractCreditsCommit.setOnClickListener(new MyOnClickListener());
        llExtractRule.setOnClickListener(new MyOnClickListener());
        /**
         * 第一种方法
         */
        etExtractCash.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        etExtractCash.setText(s);
                        etExtractCash.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    etExtractCash.setText(s);
                    etExtractCash.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etExtractCash.setText(s.subSequence(0, 1));
                        etExtractCash.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });
    }

    private void getExtractCredits() {
        userLogin = Tools.getUserLogin(this);
        if (TextUtils.isEmpty(userLogin)) {
            Toast.makeText(ExtractCreditsActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
            toLogin();
            return;
        }
        map = new HashMap<>();
        map.put("Id", userLogin);
        netUtil2.okHttp2Server2(ExtractCreditsActivity.this, mine_url, map);
        netUtil2.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e("---" + response + "----");
                tvExtractCreditsCommit.setClickable(true);
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1:
                        MineBean mineBean = new Gson().fromJson(response, MineBean.class);
                        List<MineBean.DataBean> data = mineBean.getData();
                        if (data.size() > 0) {
                            amount = mineBean.getData().get(0).getAmount();
                            DecimalFormat df = new DecimalFormat("#0.00");
                            df.setRoundingMode(RoundingMode.DOWN);
                            if (amount == 0) {
                                tvExteactAccount.setText("0.00");
                            } else {
                                tvExteactAccount.setText("" + df.format(amount));
                            }
                        }
                        break;
                    case 0:
                        Toast.makeText(ExtractCreditsActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "-" + e.getMessage());
                EncryptNetUtil.startNeterrorPage(ExtractCreditsActivity.this);
            }
        });
    }

    /**
     * 登录
     */
    private void toLogin() {
        Intent intent = new Intent(ExtractCreditsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 处理点击事件
     */
    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_extract_credits_commit:
                    commitExtractCredits();
                    break;
                case R.id.ll_extract_rule:
                    showExtractRule();
                    break;
            }
        }
    }

    /**
     * 显示提取规则
     */
    private void showExtractRule() {
        Intent intent = new Intent(this, ExtractDetailActivity.class);
        startActivity(intent);
    }

    /**
     * 提取积分
     */
    private void commitExtractCredits() {
        tvExtractCreditsCommit.setClickable(false);
        String etCredits = etExtractCash.getText().toString().trim();
        if (TextUtils.isEmpty(etCredits)) {
            Toast.makeText(ExtractCreditsActivity.this, "请输入提取数量！", Toast.LENGTH_SHORT).show();
            tvExtractCreditsCommit.setClickable(true);
        } else {
            double cret = Double.valueOf(etCredits);
            if (cret >= 2 && cret <= amount) {
                String userLogin = Tools.getUserLogin(this);
                boolean bindAlipay = Tools.isBindAlipay(this);
                map = new HashMap<>();
                map.put("Id", userLogin);
                map.put("id", userLogin);
                map.put("extract_count", etCredits);
                if (bindAlipay) {
                    netUtil.okHttp2Server2(ExtractCreditsActivity.this, extract_url, map);
                } else {
                    tvExtractCreditsCommit.setClickable(true);
                    toBindAlipay();
                }
            } else {
                Toast.makeText(ExtractCreditsActivity.this, "提现金额需要不低于最小额度并且不能高于可提取现金数量！！！", Toast.LENGTH_SHORT).show();
                tvExtractCreditsCommit.setClickable(true);
            }
        }
    }

    /**
     *
     */
    private void toBindAlipay() {
        Toast.makeText(ExtractCreditsActivity.this, "请先绑定支付宝账号！", Toast.LENGTH_SHORT).show();
        View inflate = View.inflate(this, R.layout.bind_alipay_dailog, null);
        final EditText etAccount = (EditText) inflate.findViewById(R.id.et_pay_account);
        final EditText etName = (EditText) inflate.findViewById(R.id.et_pay_name);
        final EditText etSmsCode = (EditText) inflate.findViewById(R.id.et_alipay_smscode);
        final TextView tvAlipayGetSms = (TextView) inflate.findViewById(R.id.tv_alipay_getSms);
        TextView tvBindAlipayCancel = (TextView) inflate.findViewById(R.id.tv_bind_alipay_cancel);
        TextView tvBindAlipayConfirm = (TextView) inflate.findViewById(R.id.tv_bind_alipay_confirm);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.setView(inflate).create();
        alertDialog.show();
        tvAlipayGetSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_account = etAccount.getText().toString().trim();
                String userPhone = Tools.getUserPhone(ExtractCreditsActivity.this);
                if (TextUtils.isEmpty(userPhone)) {
                    Toast.makeText(ExtractCreditsActivity.this, "检测到登录出现故障，请退出当前账号后重新登录！！！", Toast.LENGTH_SHORT).show();
                }
                LogUtil.e("userPhone = " + userPhone);
                getSmsCode(userPhone, user_account, tvAlipayGetSms);
            }
        });

        tvBindAlipayCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        });
        tvBindAlipayConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account = etAccount.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String smscode = etSmsCode.getText().toString().trim();

                if (TextUtils.isEmpty(account)) {
                    Toast.makeText(ExtractCreditsActivity.this, "请输入支付宝账号!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(ExtractCreditsActivity.this, "请输入用户名!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(smscode)) {
                    Toast.makeText(ExtractCreditsActivity.this, "请输入短信验证码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (smscode.equals(sms_code_s)) {
                    bindPay(account, name);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(ExtractCreditsActivity.this, "短信验证码不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });
    }

    /**
     * 解析短信验证数据
     *
     * @param response
     */
    private void parseSmsData(String response) {
        LogUtil.e(response);
        SmsCodeBean smsCodeBean = new Gson().fromJson(response, SmsCodeBean.class);
        int statusCode = smsCodeBean.getStatusCode();
        switch (statusCode) {
            case 0:
                Toast.makeText(ExtractCreditsActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                List<SmsCodeBean.DataBean> data = smsCodeBean.getData();
                if (data.size() > 0) {
                    sms_code_s = data.get(0).getSms_code();
                }
                Toast.makeText(ExtractCreditsActivity.this, "验证码已发送你的手机，请查收！", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /**
     * 绑定支付宝
     */
    private void bindPay(String account, String name) {
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(name)) {
            Toast.makeText(ExtractCreditsActivity.this, "支付宝账号和密码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            Map map = new HashMap<String, String>();
            map.put("Id", userLogin);
            map.put("alipay", account);
            map.put("alipayName", name);
            which = "pay";
            netUtil3.okHttp2Server2(ExtractCreditsActivity.this, pay_url, map);
        }
    }

    /**
     * 获取验证码的方法
     */
    private void getSmsCode(String userPhone, String user, final TextView tv) {

        tv.setClickable(false);
        tv.setTextColor(Color.parseColor("#ffffff"));
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tv.setText(l / 1000 + "s后重新获取");
            }

            @Override
            public void onFinish() {
                tv.setClickable(true);
                tv.setText("获取验证码");
                tv.setTextColor(Color.parseColor("#ffffff"));
            }
        };
        which = "sms";
        map = new HashMap<>();
        map.put("Id",userLogin);
        map.put("sms_code", userPhone);
        map.put("type", "3");
        netUtil3.okHttp2Server2(ExtractCreditsActivity.this, sms_url, map);
        countDownTimer.start();
    }

    public class OnNetutilResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
            int statusCode = codeStatueBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    if (which.equals("pay")) {
                        Toast.makeText(ExtractCreditsActivity.this, "绑定支付宝成功！", Toast.LENGTH_SHORT).show();
                        CacheUtils.putBoolean(ExtractCreditsActivity.this, CacheUtils.PAY_STATE, true);
                        CacheUtils.putString(ExtractCreditsActivity.this, CacheUtils.ALIPAY_ACCOUNT, account);
                    } else if (which.equals("sms")) {
                        parseSmsData(response);
                    }
                    break;
                case 0:

                    break;
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
            EncryptNetUtil.startNeterrorPage(ExtractCreditsActivity.this);
        }
    }

}
