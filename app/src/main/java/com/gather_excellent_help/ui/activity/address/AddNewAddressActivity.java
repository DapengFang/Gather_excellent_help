package com.gather_excellent_help.ui.activity.address;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.PcsChoicePopupwindow;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class AddNewAddressActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;
    private EditText et_newaddress_name;
    private EditText et_newaddress_phone;
    private TextView tv_address_pcs;
    private EditText et_newaddress_address;
    private EditText et_newaddress_postall;
    private TextView tv_add_newaddress_submit;
    private LinearLayout llRoot;

    private View vShadow;

    private PcsChoicePopupwindow pcsChoicePopupwindow;
    private String pcs = "";
    private String area = "";
    private String area_id = "";

    private String add_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=AddUserAddress";
    private String update_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=EditUserAddress";
    private NetUtil netUtil;
    private Map<String,String> map;
    private Bundle extras;
    private int isdefault;
    private int addrId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout)findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView)findViewById(R.id.tv_top_title_name);
        et_newaddress_name = (EditText)findViewById(R.id.et_newaddress_name);
        et_newaddress_phone = (EditText)findViewById(R.id.et_newaddress_phone);
        tv_address_pcs = (TextView)findViewById(R.id.tv_address_pcs);
        et_newaddress_address = (EditText)findViewById(R.id.et_newaddress_address);
        et_newaddress_postall = (EditText)findViewById(R.id.et_newaddress_postall);
        tv_add_newaddress_submit = (TextView)findViewById(R.id.tv_add_newaddress_submit);
        vShadow = findViewById(R.id.v_shadow);
        llRoot = (LinearLayout) findViewById(R.id.ll_root);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        Intent intent = getIntent();
        extras = intent.getExtras();
        if(extras!=null) {
            addrId = extras.getInt("addrId");
            String accept_name = extras.getString("accept_name");
            String mobile = extras.getString("mobile");
            String c_area = extras.getString("area");
            String c_area_id = extras.getString("area_id");
            String address = extras.getString("address");
            String postmall = extras.getString("postmall");
            isdefault = extras.getInt("isdefault");
            pcs = c_area;
            area = c_area;
            area_id = c_area_id;
            et_newaddress_name.setText(accept_name);
            et_newaddress_phone.setText(mobile);
            tv_address_pcs.setText(c_area.replace(",",""));
            et_newaddress_address.setText(address);
            et_newaddress_postall.setText(postmall);
        }
        netUtil = new NetUtil();
        OnServerResponseListener onServerResponseListener = new OnServerResponseListener();
        netUtil.setOnServerResponseListener(onServerResponseListener);
        tv_top_title_name.setText("收货地址信息");
        et_newaddress_address.setSingleLine(false);
        et_newaddress_address.setHorizontallyScrolling(false);
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        tv_address_pcs.setOnClickListener(myonclickListener);
        tv_add_newaddress_submit.setOnClickListener(myonclickListener);

    }


    /**
     * 监听页面上的所有点击事件
     */
    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_address_pcs:
                    showPopMenu();
                    break;
                case R.id.tv_add_newaddress_submit:
                    if(extras!=null) {
                        LogUtil.e("编辑");
                        updateUserAddress();
                    }else{
                        LogUtil.e("保存");
                        saveUserAddress();
                    }
                    break;
            }
        }
    }

    /**
     * 修改用户的收货地址
     */
    private void updateUserAddress() {
        String name =  et_newaddress_name.getText().toString().trim();
        String phone = et_newaddress_phone.getText().toString().trim();
        String address = et_newaddress_address.getText().toString().trim();
        String postall = et_newaddress_postall.getText().toString().trim();
        if(TextUtils.isEmpty(name)) {
            Toast.makeText(AddNewAddressActivity.this, "收货人姓名不能为空！！！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phone)) {
            Toast.makeText(AddNewAddressActivity.this, "手机号不能为空！！！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pcs)) {
            Toast.makeText(AddNewAddressActivity.this, "请选择省市区！！！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(address)) {
            Toast.makeText(AddNewAddressActivity.this, "详细地址不能为空！！！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(postall.length()!=6 && !TextUtils.isEmpty(postall)) {
            Toast.makeText(AddNewAddressActivity.this, "邮政编码为6位！！！", Toast.LENGTH_SHORT).show();
            return;
        }
        String check_default = String.valueOf(isdefault);
        tv_add_newaddress_submit.setClickable(false);
        String userLogin = Tools.getUserLogin(this);
        map = new HashMap<>();
        map.put("user_id",userLogin);
        map.put("addrId",String.valueOf(addrId));
        map.put("accept_name",name);
        map.put("is_default",check_default);
        map.put("address",address);
        map.put("zip",postall);
        map.put("area",area);
        map.put("area_id",area_id);
        map.put("mobile",phone);
        LogUtil.e(update_url);
        netUtil.okHttp2Server2(update_url,map);
    }

    /**
     * 保存用户的收货地址
     */
    private void saveUserAddress() {
        String name =  et_newaddress_name.getText().toString().trim();
        String phone = et_newaddress_phone.getText().toString().trim();
        String address = et_newaddress_address.getText().toString().trim();
        String postall = et_newaddress_postall.getText().toString().trim();
        if(TextUtils.isEmpty(name)) {
            Toast.makeText(AddNewAddressActivity.this, "收货人姓名不能为空！！！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phone)) {
            Toast.makeText(AddNewAddressActivity.this, "手机号不能为空！！！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pcs)) {
            Toast.makeText(AddNewAddressActivity.this, "请选择省市区！！!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(address)) {
            Toast.makeText(AddNewAddressActivity.this, "详细地址不能为空！！！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(postall)) {
            Toast.makeText(AddNewAddressActivity.this, "邮政编码不能为空！！！", Toast.LENGTH_SHORT).show();
            return;
        }
        tv_add_newaddress_submit.setClickable(false);
        String userLogin = Tools.getUserLogin(this);
        map = new HashMap<>();
        map.put("user_id",userLogin);
        map.put("accept_name",name);
        map.put("is_default","1");
        map.put("address",address);
        map.put("zip",postall);
        map.put("area",area);
        map.put("area_id",area_id);
        map.put("mobile",phone);
        LogUtil.e(add_url);
        netUtil.okHttp2Server2(add_url,map);

    }

    /**
     * 显示省市区的popupwindow
     */
    private void showPopMenu() {
        vShadow.setVisibility(View.VISIBLE);
        if (pcsChoicePopupwindow == null) {
            pcsChoicePopupwindow = new PcsChoicePopupwindow(this,vShadow);
            pcsChoicePopupwindow.showAtLocation(llRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else if (pcsChoicePopupwindow != null
                && pcsChoicePopupwindow.isShowing()) {
            pcsChoicePopupwindow.dismiss();
        } else {
            pcsChoicePopupwindow.showAtLocation(llRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        pcsChoicePopupwindow.setOnItemClickListenr(new PcsChoicePopupwindow.OnItemClickListenr() {
            @Override
            public void getFinalAddress(String address,String a,String a_id) {
                LogUtil.e(address);
                pcs = address;
                area = a;
                area_id = a_id;
                LogUtil.e(area + "---" + area_id);
                tv_address_pcs.setText(address);
                tv_address_pcs.setTextColor(Color.parseColor("#99000000"));
                if (pcsChoicePopupwindow.isShowing()) {
                    pcsChoicePopupwindow.dismiss();
                }
                vShadow.setVisibility(View.GONE);
            }
        });

    }

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener{

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
            tv_add_newaddress_submit.setClickable(true);
            CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
            int statusCode = codeStatueBean.getStatusCode();
            Toast.makeText(AddNewAddressActivity.this, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
            switch (statusCode) {
                case 1 :
                    EventBus.getDefault().post(new AnyEvent(EventType.UPDATA_ADDRESS,"刷新地址界面"));
                    EventBus.getDefault().post(new AnyEvent(EventType.UPDATA_ADDRESS_ORDER,"更新订单页面的地址信息"));
                    finish();
                    break;
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e("网络连接出现问题~");
            tv_add_newaddress_submit.setClickable(true);
        }
    }
}
