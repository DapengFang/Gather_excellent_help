package com.gather_excellent_help.ui.activity.suning.saleafter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.taggroup.TagGroup;

public class ExchangeGoodsActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;

    private TagGroup tag_exchange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_goods);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        rl_exit = (RelativeLayout)findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView)findViewById(R.id.tv_top_title_name);

        tag_exchange = (TagGroup)findViewById(R.id.tag_exchange);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        tv_top_title_name.setText("申请换货");
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        String[] tags = {"abc","bcd","asdfgh","你好吗"};
        tag_exchange.setTags(tags);
        tag_exchange.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(TagGroup.TagView v, String tag) {
                Toast.makeText(ExchangeGoodsActivity.this, tag, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class MyonclickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit :
                    finish();
                    break;
            }
        }
    }
}
