package com.gather_excellent_help.ui.activity.shop;

import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.PcsChoicePopupwindow;
import com.gather_excellent_help.utils.LogUtil;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class ShopInfoUpadateActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.rl_share)
    RelativeLayout rlShare;
    @Bind(R.id.et_merchant_address)
    EditText etMerchantAddress;
    @Bind(R.id.et_merchant_shopinfo)
    EditText etMerchantShopinfo;
    @Bind(R.id.et_merchant_phone)
    EditText etMerchantPhone;
    @Bind(R.id.et_merchant_shopname)
    EditText etMerchantShopname;
    @Bind(R.id.tv_shop_time_am)
    TextView tvShopTimeAm;
    @Bind(R.id.tv_shop_time_pm)
    TextView tvShopTimePm;
    @Bind(R.id.tv_shop_info_next)
    TextView tvShopInfoNext;
    @Bind(R.id.tv_merchant_pcs_choice)
    TextView tvMerchantPcsChoice;
    @Bind(R.id.activity_shop_info_upadate)
    LinearLayout llRoot;
    @Bind(R.id.v_shadow)
    View vShadow;
    private String startTime = "";
    private String endTime = "";
    private String name = "";
    private String telephone = "";
    private String address = "";
    private String info = "";
    private String business_time = "9:00a18:00";
    private PcsChoicePopupwindow pcsChoicePopupwindow;
    private String pcs = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info_upadate);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initData();
    }

    /**
     * 初始化数据加载
     */
    private void initData() {
        tvTopTitleName.setText("基本信息");
        rlShare.setVisibility(View.GONE);
        rlExit.setOnClickListener(new MyOnClickListener());
        tvShopTimeAm.setOnClickListener(new MyOnClickListener());
        tvShopTimePm.setOnClickListener(new MyOnClickListener());
        tvShopInfoNext.setOnClickListener(new MyOnClickListener());
        tvMerchantPcsChoice.setOnClickListener(new MyOnClickListener());
    }

    /**
     * 监听全局点击事件的类
     */
    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_shop_time_am:
                    showTimePicker(R.id.tv_shop_time_am);
                    break;
                case R.id.tv_shop_time_pm:
                    showTimePicker(R.id.tv_shop_time_pm);
                    break;
                case R.id.tv_shop_info_next:
                    toNextPage();
                    break;
                case R.id.tv_merchant_pcs_choice:
                    showPopMenu();
                    break;
            }
        }
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
            public void getFinalAddress(String address) {
                pcs = address;
                LogUtil.e(address);
                tvMerchantPcsChoice.setText(address);
                tvMerchantPcsChoice.setTextColor(Color.parseColor("#88000000"));
                if (pcsChoicePopupwindow.isShowing()) {
                    pcsChoicePopupwindow.dismiss();
                }
                vShadow.setVisibility(View.GONE);
            }
        });

    }


    /**
     * 跳转到下一级页面
     */
    private void toNextPage() {
        name = etMerchantShopname.getText().toString().trim();
        telephone = etMerchantPhone.getText().toString().trim();
        address = etMerchantAddress.getText().toString().trim();
        info = etMerchantShopinfo.getText().toString().trim();
        if (TextUtils.isEmpty(pcs)) {
            Toast.makeText(ShopInfoUpadateActivity.this, "请选择省市区！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(ShopInfoUpadateActivity.this, "请输入店名！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(telephone)) {
            Toast.makeText(ShopInfoUpadateActivity.this, "请输入电话！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(ShopInfoUpadateActivity.this, "请输入地址！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(info)) {
            Toast.makeText(ShopInfoUpadateActivity.this, "请输入店铺简介！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
            business_time = startTime + "a" + endTime;
        }
        Intent intent = new Intent(ShopInfoUpadateActivity.this, ShopBrandUpdateActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("telephone", telephone);
        intent.putExtra("address", pcs + address);
        intent.putExtra("info", info);
        intent.putExtra("business_time", business_time);
        startActivity(intent);
    }

    /**
     * 选择时间
     *
     * @param id
     */
    private void showTimePicker(final int id) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                if (id == R.id.tv_shop_time_am) {
                    if (minute < 10) {
                        tvShopTimeAm.setText("早上" + hour + ":0" + minute);
                        startTime = hour + ":0" + minute;
                    } else {
                        tvShopTimeAm.setText("早上" + hour + ":" + minute);
                        startTime = hour + ":" + minute;
                    }
                } else {
                    if (minute < 10) {
                        tvShopTimePm.setText("晚上" + hour + ":0" + minute);
                        endTime = hour + ":0" + minute;
                    } else {
                        tvShopTimePm.setText("晚上" + hour + ":" + minute);
                        endTime = hour + ":" + minute;
                    }
                }
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(AnyEvent event) {
        if (event.getType() == EventType.EVENT_EXIT) {
            LogUtil.e("exit-------------------");
            finish();
        }
    }
}
