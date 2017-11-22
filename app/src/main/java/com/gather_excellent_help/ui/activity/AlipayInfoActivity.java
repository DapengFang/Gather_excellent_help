package com.gather_excellent_help.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class AlipayInfoActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;
    private TextView tv_alipay_account;
    private TextView tv_alipay_user;
    private TextView tv_alipay_unbind;

    private NetUtil netUtil;
    private Map<String, String> map;
    private String account;
    private String username;
    private AlertDialog alertDialog;
    private String userLogin;

    private String unpay_url = Url.BASE_URL + "UnbundAlipay.aspx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay_info);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
        tv_alipay_account = (TextView) findViewById(R.id.tv_alipay_account);
        tv_alipay_user = (TextView) findViewById(R.id.tv_alipay_user);
        tv_alipay_unbind = (TextView) findViewById(R.id.tv_alipay_unbind);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        netUtil = new NetUtil();
        userLogin = Tools.getUserLogin(this);
        account = CacheUtils.getString(AlipayInfoActivity.this, CacheUtils.ALIPAY_ACCOUNT, "");
        username = CacheUtils.getString(AlipayInfoActivity.this, CacheUtils.ALIPAY_USERNAME, "");
        tv_top_title_name.setText("支付宝绑定信息");
        if (account != null) {
            tv_alipay_account.setText("绑定支付宝账号\t\t\t\t\t\t" + account);
        }
        if (username != null) {
            tv_alipay_user.setText("用户名\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + username);
        }
        OnServerResponseListener onServerResponseListener = new OnServerResponseListener();
        netUtil.setOnServerResponseListener(onServerResponseListener);
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        tv_alipay_unbind.setOnClickListener(myonclickListener);
    }

    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_alipay_unbind:
                    showUnBindAlipayDialog();
                    break;
            }
        }
    }

    /**
     * 解除支付宝绑定的dialog
     */
    private void showUnBindAlipayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示")
                .setMessage("您确定要解除支付宝绑定吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        unBindPay();
                    }
                })
                .setNegativeButton("取消", null);
        alertDialog = builder.create();
        if (AlipayInfoActivity.this != null && !AlipayInfoActivity.this.isFinishing()) {
            alertDialog.show();
        }
    }

    /**
     * 解绑支付宝
     */
    private void unBindPay() {
        Map map = new HashMap<String, String>();
        map.put("Id", userLogin);
        netUtil.okHttp2Server2(unpay_url, map);
    }

    /**
     * 联网请求的回调
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
            CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
            int statusCode = codeStatueBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    CacheUtils.putBoolean(AlipayInfoActivity.this, CacheUtils.PAY_STATE, false);
                    CacheUtils.putString(AlipayInfoActivity.this, CacheUtils.ALIPAY_ACCOUNT, "");
                    EventBus.getDefault().post(new AnyEvent(EventType.UNBIND_ALIPAY, "解绑支付宝"));
                    finish();
                    break;
                case 0:
                    Toast.makeText(AlipayInfoActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
        }
    }
}
