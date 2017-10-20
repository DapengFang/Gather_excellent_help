package com.gather_excellent_help.ui.activity.shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.AlipayManagerActivity;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class StoreshowActivity extends BaseActivity {

    private ImageView iv_zhuangtai_exit;
    private TextView tv_top_title_name;
    private TextView tv_online_pay;
    private TextView tv_outline_pay;
    private TextView tv_store_submit;
    private int which_check = 1;

    private NetUtil netUtil;
    private Map<String,String> map;

    private String pay_url = Url.BASE_URL + "ChoosePayWay.aspx" ;//加盟付款方式

    private String pay_type = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storeshow);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_zhuangtai_exit = (ImageView) findViewById(R.id.iv_zhuangtai_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
        tv_online_pay = (TextView)findViewById(R.id.tv_online_pay);
        tv_outline_pay = (TextView)findViewById(R.id.tv_outline_pay);
        tv_store_submit = (TextView)findViewById(R.id.tv_store_submit);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //初始化的相关操作
        netUtil = new NetUtil();
        map = new HashMap<>();
        tv_top_title_name.setText("实体店");
        tv_online_pay.setSelected(true);
        tv_outline_pay.setSelected(false);
        //处理页面上的点击事件
        MyOnClickListener myOnclickListener = new MyOnClickListener();
        tv_outline_pay.setOnClickListener(myOnclickListener);
        tv_online_pay.setOnClickListener(myOnclickListener);
        tv_store_submit.setOnClickListener(myOnclickListener);
        iv_zhuangtai_exit.setOnClickListener(myOnclickListener);
        //处理联网请求的相关操作
        netUtil.setOnServerResponseListener(new OnServerResponseListener());
    }

    /**
     * 页面点击事件的监听
     */
    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_online_pay:
                    which_check = 1;
                    changeCheck(which_check);
                    break;
                case R.id.tv_outline_pay :
                    which_check = 2;
                    changeCheck(which_check);
                    break;
                case R.id.tv_store_submit:
                    tv_store_submit.setClickable(false);
                    savePayWays();
                    break;
                case R.id.iv_zhuangtai_exit:
                    finish();
                    break;
            }
        }
    }

    /**
     * 保存付款方式到服务器中
     */
    private void savePayWays(){
        if(which_check == 1) {
            pay_type = "1";
        }else if(which_check == 2) {
            pay_type = "2";
        }
        String userLogin = Tools.getUserLogin(this);
        map = new HashMap<>();
        map.put("user_id",userLogin);
        map.put("pay_type",pay_type);
        netUtil.okHttp2Server2(pay_url,map);
    }

    /**
     * 跳转到不同的界面
     */
    private void justDiffPage() {
        if(which_check == 1) {
            Intent intent = new Intent(this, AlipayManagerActivity.class);
            startActivity(intent);
        }else if(which_check == 2) {
            Intent intent = new Intent(this, StoreSuccActivity.class);
            startActivity(intent);
            finish();
        }
        EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN,"更新"));
    }

    /**
     * 处理选择不同的付款方式
     * @param whick
     */
    private void changeCheck(int whick){
        if(whick == 1) {
            tv_online_pay.setSelected(true);
            tv_outline_pay.setSelected(false);
        }else if(whick == 2) {
            tv_online_pay.setSelected(false);
            tv_outline_pay.setSelected(true);
        }
    }

    /**
     * 联网请求的监听
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener{

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
            tv_store_submit.setClickable(true);
            CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
            int statusCode = codeStatueBean.getStatusCode();
            switch (statusCode) {
                case 1 :
                    justDiffPage();
                    break;
                case 0:
                    Toast.makeText(StoreshowActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
            tv_store_submit.setClickable(true);
        }
    }


    public void onEvent(AnyEvent event){
        int type = event.getType();
        if(type == EventType.STORE_EXIT) {
            LogUtil.e(event.getMessage());
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
