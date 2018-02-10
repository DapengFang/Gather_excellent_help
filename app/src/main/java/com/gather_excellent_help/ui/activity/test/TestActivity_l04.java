package com.gather_excellent_help.ui.activity.test;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;

public class TestActivity_l04 extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_l04);
        try {
            test();
        }catch (Exception e){

        }
    }

    private void test() throws Exception {
        Integer randomNum = EncryptNetUtil.getRandomNumber();
        Toast.makeText(TestActivity_l04.this, "randomNum======" + randomNum, Toast.LENGTH_SHORT).show();
        long timesTamp = EncryptNetUtil.getTimesTamp();
        String token = "8292@@!!asdlol";
        String params = "{\\\"Id\\\":8292,\\\"UserName\\\":\\\"15228132423\\\",\\\"Password\\\":\\\"A0BFD09162B4A3FAC9539EE7BA28F4DD\\\"}";
        String result = EncryptNetUtil.getMD5EncryptHeader(timesTamp,randomNum,"8292",token,params);
        LogUtil.e("result = " +result);
    }
}
