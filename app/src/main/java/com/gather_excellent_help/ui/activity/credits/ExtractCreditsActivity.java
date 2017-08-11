package com.gather_excellent_help.ui.activity.credits;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

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

    private String extract_url = Url.BASE_URL + "ExtractPoints.aspx";
    private Map<String,String> map;
    private NetUtil netUtil;
    private NetUtil netUtil2;

    private String mine_url = Url.BASE_URL + "Mine.aspx";
    private double amount;

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
        getExtractCredits();
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1 :
                        EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN,"提取成功！"));
                        getExtractCredits();
                        Toast.makeText(ExtractCreditsActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 0:
                        Toast.makeText(ExtractCreditsActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }

            }

            @Override
            public void getFailResponse(Call call, Exception e) {
             LogUtil.e(call.toString() + "--" +e.getMessage());
            }
        });
        rlExit.setOnClickListener(new MyOnClickListener());
        tvExtractCreditsCommit.setOnClickListener(new MyOnClickListener());
        llExtractRule.setOnClickListener(new MyOnClickListener());
    }

    private void getExtractCredits() {
        String userLogin = Tools.getUserLogin(this);
        if(TextUtils.isEmpty(userLogin)) {
            Toast.makeText(ExtractCreditsActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
            toLogin();
            return;
        }
        map = new HashMap<>();
        map.put("Id", userLogin);
        netUtil2.okHttp2Server2(mine_url, map);
        netUtil2.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1 :
                        MineBean mineBean = new Gson().fromJson(response, MineBean.class);
                        List<MineBean.DataBean> data = mineBean.getData();
                        if(data.size()>0) {
                            amount = mineBean.getData().get(0).getAmount();
                            DecimalFormat df = new DecimalFormat("30.00");
                            tvExteactAccount.setText("可提取现金: " + df.format(amount));
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

    }

    /**
     * 提取积分
     */
    private void commitExtractCredits() {
        String etCredits = etExtractCash.getText().toString().trim();
        Integer cret = Integer.valueOf(etCredits);
        if(TextUtils.isEmpty(etCredits)) {
            Toast.makeText(ExtractCreditsActivity.this, "请输入提取数量！", Toast.LENGTH_SHORT).show();
        }else if(cret>0 && cret <= amount) {
            String userLogin = Tools.getUserLogin(this);
            map = new HashMap<>();
            map.put("id",userLogin);
            map.put("extract_count",etCredits);
            netUtil.okHttp2Server2(extract_url,map);
        }else{
            Toast.makeText(ExtractCreditsActivity.this, "输入数量不正确！", Toast.LENGTH_SHORT).show();
        }
    }


}
