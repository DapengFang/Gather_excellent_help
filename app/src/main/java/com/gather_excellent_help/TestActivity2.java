package com.gather_excellent_help;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.bean.address.Area;
import com.gather_excellent_help.db.DBhelper;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.AddressSelector;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.ScreenUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestActivity2 extends BaseActivity {

    private TextView tv_pcs_choice_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        initView();
        initData();
    }

    private void initView() {
        tv_pcs_choice_cancel = (TextView)findViewById(R.id.tv_pcs_choice_cancel);
    }

    private void initData(){
        tv_pcs_choice_cancel.setText(Html.fromHtml(descString(), getImageGetterInstance(),
                null));
    }

    private String descString() {
        return  "<img src='" + R.mipmap.ic_launcher + "'/>" +" 苏宁商品测试,苏宁商品测试,苏宁商品测试,苏宁商品测试,苏宁商品测试,苏宁商品测试,苏宁商品测试 ";
    }


    public Html.ImageGetter getImageGetterInstance() {
        Html.ImageGetter imgGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int id = Integer.parseInt(source);
                Drawable d = getResources().getDrawable(id);
                int wdp = DensityUtil.dip2px(TestActivity2.this,20);
                int hdp = DensityUtil.dip2px(TestActivity2.this,20);
                d.setBounds(0, 0, wdp, hdp);
                return d;
            }
        };
        return imgGetter;
    }

}
