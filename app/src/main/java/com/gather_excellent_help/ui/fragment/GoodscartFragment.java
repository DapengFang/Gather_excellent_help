package com.gather_excellent_help.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.presenter.NewsFirstPresenter;
import com.gather_excellent_help.ui.adapter.MyNewsAdapter;
import com.gather_excellent_help.ui.base.BaseFragment;
import com.gather_excellent_help.ui.widget.ViewpagerIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wuxin on 2017/7/7.
 */

public class GoodscartFragment extends BaseFragment {
    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.et_taobao_search_content)
    EditText etTaobaoSearchContent;
    @Bind(R.id.rl_taobao_sousuo)
    RelativeLayout rlTaobaoSousuo;
    @Bind(R.id.rcv_news_horizational)
    RecyclerView rcvNewsHorizational;
    @Bind(R.id.fl_news_frag)
    FrameLayout flNewsFrag;
    private Context context;
    private NewsFirstPresenter newsFirstPresenter;


    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.news_fragment, null);
        return inflate;
    }

    @Override
    public void initData() {
        rlExit.setVisibility(View.INVISIBLE);
        newsFirstPresenter = new NewsFirstPresenter(context);
        View rootView = newsFirstPresenter.getRootView();
        flNewsFrag.removeAllViews();
        flNewsFrag.addView(rootView);
        newsFirstPresenter.loadTitleData(rcvNewsHorizational);
        newsFirstPresenter.refreshSwip();
        //newsFirstPresenter.scrollRecycleView();
        rlTaobaoSousuo.setOnClickListener(new MyOnclickListener());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        context =getContext();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public class MyOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
             switch (view.getId()) {
                 case R.id.rl_taobao_sousuo :
                     Toast.makeText(getContext(), "正在搜索中，请稍后！", Toast.LENGTH_SHORT).show();
                     String keyword = etTaobaoSearchContent.getText().toString().trim();
                     if(TextUtils.isEmpty(keyword)) {
                         Toast.makeText(context, "关键字不能为空！", Toast.LENGTH_SHORT).show();
                         return;
                     }
                     InputMethodManager imm = (InputMethodManager)getContext().getSystemService(
                             Context.INPUT_METHOD_SERVICE);
                     imm.hideSoftInputFromWindow(etTaobaoSearchContent.getWindowToken(), 0);
                     newsFirstPresenter.searchData(keyword);
                     break;
             }
        }
    }

}
