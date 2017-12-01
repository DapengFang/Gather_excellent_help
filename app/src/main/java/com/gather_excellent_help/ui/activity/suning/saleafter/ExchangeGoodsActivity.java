package com.gather_excellent_help.ui.activity.suning.saleafter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.taggroup.TagGroup;
import com.gather_excellent_help.utils.LogUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Set;

public class ExchangeGoodsActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;

    private TagFlowLayout tag_flowlayout;
    private LayoutInflater mInflater;

    private String[] tags;
    private TagAdapter mAdapter;



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

        tag_flowlayout = (TagFlowLayout)findViewById(R.id.tag_flowlayout);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        mInflater = LayoutInflater.from(this);
        tv_top_title_name.setText("申请换货");
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        tags = new String[]{"abc","bcd","asdfgh","你好吗"};
        mAdapter =new TagAdapter<String>(tags)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_tag_spec_title,
                        tag_flowlayout, false);
                tv.setText(s);
                return tv;
            }

        };
        mAdapter.setSelectedList(0);
        tag_flowlayout.setAdapter(mAdapter);
        tag_flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                Toast.makeText(ExchangeGoodsActivity.this, tags[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        Set<Integer> selectedList = tag_flowlayout.getSelectedList();
       for (Integer index:selectedList){
           LogUtil.e(index+"------");
       }
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
