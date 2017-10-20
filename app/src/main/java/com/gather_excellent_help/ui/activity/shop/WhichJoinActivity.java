package com.gather_excellent_help.ui.activity.shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
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

    private ImageView iv_zhuangtai_exit;
    private TextView tv_top_title_name;
    private CheckBox cb_daren_select;
    private CheckBox cb_shiti_select;
    private TextView tv_join_submit;
    private RelativeLayout rl_daren_select;
    private RelativeLayout rl_shiti_select;

    private int curr_check = 1;

    private String type = "6";

    private NetUtil netUtil;
    private Map<String, String> map;

    private String join_url = Url.BASE_URL + "ChooseType.aspx";//加盟保存的接口

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
        iv_zhuangtai_exit = (ImageView) findViewById(R.id.iv_zhuangtai_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
        cb_daren_select = (CheckBox) findViewById(R.id.cb_daren_select);
        cb_shiti_select = (CheckBox) findViewById(R.id.cb_shiti_select);
        tv_join_submit = (TextView) findViewById(R.id.tv_join_submit);
        rl_daren_select = (RelativeLayout) findViewById(R.id.rl_daren_select);
        rl_shiti_select = (RelativeLayout) findViewById(R.id.rl_shiti_select);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //初始化的相关操作
        netUtil = new NetUtil();
        tv_top_title_name.setText("申请加盟");
        //处理页面上的点击事件
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        iv_zhuangtai_exit.setOnClickListener(myOnClickListener);
        rl_daren_select.setOnClickListener(myOnClickListener);
        rl_shiti_select.setOnClickListener(myOnClickListener);
        tv_join_submit.setOnClickListener(myOnClickListener);
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
                case R.id.iv_zhuangtai_exit:
                    finish();
                    break;
                case R.id.rl_daren_select:
                    curr_check = 1;
                    checkBox(curr_check);
                    break;
                case R.id.rl_shiti_select:
                    curr_check = 2;
                    checkBox(curr_check);
                    break;
                case R.id.tv_join_submit:
                    tv_join_submit.setClickable(false);
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
        EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN,"更新"));
        finish();
    }

    /**
     * 根据当前选中去处理相关
     *
     * @param curr_check
     */
    private void checkBox(int curr_check) {
        if (curr_check == 1) {
            cb_daren_select.setChecked(true);
            cb_shiti_select.setChecked(false);
        } else if (curr_check == 2) {
            cb_daren_select.setChecked(false);
            cb_shiti_select.setChecked(true);
        }
    }

    /**
     * 处理联网的监听
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
            tv_join_submit.setClickable(true);
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

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
            tv_join_submit.setClickable(true);
        }
    }
}
