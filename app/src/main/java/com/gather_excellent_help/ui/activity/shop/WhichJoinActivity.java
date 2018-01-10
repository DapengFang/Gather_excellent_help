package com.gather_excellent_help.ui.activity.shop;

import android.content.Intent;
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
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class WhichJoinActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;


    private RelativeLayout rl_which_join_weidaren;
    private RelativeLayout rl_which_join_shiti;

    private int curr_check = 1;

    private String type = "6";

    private NetUtil netUtil;
    private Map<String, String> map;

    private String join_url = Url.BASE_URL + "ChooseType.aspx";//加盟保存的接口
    private String which;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_which_join);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
        rl_which_join_weidaren = (RelativeLayout) findViewById(R.id.rl_which_join_weidaren);
        rl_which_join_shiti = (RelativeLayout) findViewById(R.id.rl_which_join_shiti);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //初始化的相关操作
        netUtil = new NetUtil();
        tv_top_title_name.setText("申请赚钱");


        //处理页面上的点击事件
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        rl_exit.setOnClickListener(myOnClickListener);
        rl_which_join_weidaren.setOnClickListener(myOnClickListener);
        rl_which_join_shiti.setOnClickListener(myOnClickListener);
        //联网请求的回调
        netUtil.setOnServerResponseListener(new OnServerResponseListener());
    }

    /**
     * 监听页面点击事件
     */
    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.rl_which_join_weidaren:
                    curr_check = 1;
                    rl_which_join_weidaren.setClickable(false);
                    rl_which_join_shiti.setClickable(false);
                    saveDiffWays();
                    break;
                case R.id.rl_which_join_shiti:
                    curr_check = 2;
                    rl_which_join_weidaren.setClickable(false);
                    rl_which_join_shiti.setClickable(false);
                    saveDiffWays();
                    break;
            }
        }
    }

    /**
     * 保存加盟方式到服务器
     */
    private void saveDiffWays() {
        String userLogin = Tools.getUserLogin(this);
        if (curr_check == 1) {
            type = "6";
        } else if (curr_check == 2) {
            type = "5";
        }
        which = "join";
        map = new HashMap<>();
        map.put("user_id", userLogin);
        map.put("type", type);
        netUtil.okHttp2Server2(join_url, map);
    }


    /**
     * 跳转到不同的界面
     */
    private void toDiffPage() {
        if (curr_check == 1) {
            Intent intent = new Intent(this, TalentShowActivity.class);
            startActivity(intent);
        } else if (curr_check == 2) {
            Intent intent = new Intent(this, StoreshowActivity.class);
            startActivity(intent);
        }
        EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN, "更新"));
        finish();
    }

    /**
     * 联网返回数据回调的监听
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
            if (which.equals("join")) {
                parseJoinData(response);
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
            rl_which_join_weidaren.setClickable(true);
            rl_which_join_shiti.setClickable(true);
        }
    }

    /**
     * 解析商家入驻的数据
     *
     * @param response
     */
    private void parseJoinData(String response) {
        rl_which_join_weidaren.setClickable(true);
        rl_which_join_shiti.setClickable(true);
        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
        int statusCode = codeStatueBean.getStatusCode();
        switch (statusCode) {
            case 1:
                toDiffPage();
                break;
            case 0:
                Toast.makeText(WhichJoinActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
