package com.gather_excellent_help.ui.activity.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.soutao.SouTaobaoHelpBean;
import com.gather_excellent_help.ui.adapter.soutao.SouTaobaoHelpAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class TestActivity_l03 extends BaseActivity {

    private RecyclerView rcv_sou_taobao_help;
    private List<SouTaobaoHelpBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_l03);
        initView();
        initData();
    }

    private void initView() {
        rcv_sou_taobao_help = (RecyclerView) findViewById(R.id.rcv_sou_taobao_help);
    }

    private void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcv_sou_taobao_help.setLayoutManager(layoutManager);
        data = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            SouTaobaoHelpBean souTaobaoHelpBean = new SouTaobaoHelpBean();
            souTaobaoHelpBean.setF_title("搜淘宝有什么作用");
            souTaobaoHelpBean.setS_title("您平时在淘宝、天猫、天猫超市购物时，均可搜标题拿到福利");
            data.add(souTaobaoHelpBean);
        }
        SouTaobaoHelpAdapter souTaobaoHelpAdapter = new SouTaobaoHelpAdapter(this, data);
        rcv_sou_taobao_help.setAdapter(souTaobaoHelpAdapter);
    }
}
