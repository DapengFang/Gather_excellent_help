package com.gather_excellent_help.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.presenter.NewsFirstPresenter;
import com.gather_excellent_help.ui.base.BaseFragment;
import com.gather_excellent_help.ui.base.LazyLoadFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wuxin on 2017/7/7.
 */

public class GoodscartFragment extends LazyLoadFragment {
    @Bind(R.id.et_taobao_search_content)
    EditText etTaobaoSearchContent;
    @Bind(R.id.rl_taobao_sousuo)
    RelativeLayout rlTaobaoSousuo;
    @Bind(R.id.rcv_news_horizational)
    RecyclerView rcvNewsHorizational;
    @Bind(R.id.fl_news_frag)
    FrameLayout flNewsFrag;
    @Bind(R.id.rl_news_search_before)
    RelativeLayout rlNewsSearchBefore;
    @Bind(R.id.tv_news_edit_search)
    TextView tvNewsEditSearch;
    @Bind(R.id.ll_news_search_after)
    LinearLayout llNewsSearchAfter;
    @Bind(R.id.rl_edit_text_exit)
    RelativeLayout rlEditTextExit;
    private Context context;
    private NewsFirstPresenter newsFirstPresenter;

    public static final int CHECK_NULL = 4; //加载数据的标识

    private Handler handler;


    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.news_fragment, null);
        return inflate;
    }

    @Override
    public void initData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case CHECK_NULL:
                        if (rlNewsSearchBefore != null && llNewsSearchAfter != null
                                && rlEditTextExit != null && flNewsFrag != null
                                && rlTaobaoSousuo != null && etTaobaoSearchContent != null
                                && rcvNewsHorizational != null) {
                            loadNewsData();
                            if (handler != null) {
                                handler.removeMessages(CHECK_NULL);
                            }
                        } else {
                            if (handler != null) {
                                handler.sendEmptyMessageDelayed(CHECK_NULL, 500);
                            }
                        }
                        break;
                }
            }
        };
        if (handler != null) {
            handler.sendEmptyMessageDelayed(CHECK_NULL, 600);
        }

    }

    /**
     * 导入新闻数据
     */
    private void loadNewsData() {
        initSearch();
        newsFirstPresenter = new NewsFirstPresenter(context);
        View rootView = newsFirstPresenter.getRootView();
        flNewsFrag.removeAllViews();
        flNewsFrag.addView(rootView);
        newsFirstPresenter.loadTitleData(rcvNewsHorizational);
        newsFirstPresenter.refreshSwip();
        rlTaobaoSousuo.setOnClickListener(new MyOnclickListener());
        rlNewsSearchBefore.setOnClickListener(new MyOnclickListener());
        etTaobaoSearchContent.addTextChangedListener(watcher);
        rlEditTextExit.setOnClickListener(new MyOnclickListener());
        etTaobaoSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = etTaobaoSearchContent.getText().toString().trim();
                    if (TextUtils.isEmpty(keyword)) {
                        rlNewsSearchBefore.setVisibility(View.VISIBLE);
                        llNewsSearchAfter.setVisibility(View.GONE);
                        return true;
                    }
                    Toast.makeText(getContext(), "正在搜索中，请稍后！", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etTaobaoSearchContent.getWindowToken(), 0);
                    newsFirstPresenter.searchData(keyword);
                    return true;
                }
                return false;
            }
        });
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            int length = s.length();
            if (length == 0) {
                tvNewsEditSearch.setText("取消");
                rlEditTextExit.setVisibility(View.GONE);
            } else {
                tvNewsEditSearch.setText("搜索");
                rlEditTextExit.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };

    private void initSearch() {
        rlNewsSearchBefore.setVisibility(View.VISIBLE);
        llNewsSearchAfter.setVisibility(View.GONE);
        rlEditTextExit.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        context = getContext();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Override
    protected void stopLoad() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    public class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_taobao_sousuo:
                    String keyword = etTaobaoSearchContent.getText().toString().trim();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etTaobaoSearchContent.getWindowToken(), 0);
                    if (TextUtils.isEmpty(keyword)) {
                        rlNewsSearchBefore.setVisibility(View.VISIBLE);
                        llNewsSearchAfter.setVisibility(View.GONE);
                        return;
                    }
                    Toast.makeText(getContext(), "正在搜索中，请稍后！", Toast.LENGTH_SHORT).show();
                    newsFirstPresenter.searchData(keyword);
                    break;
                case R.id.rl_news_search_before:
                    rlNewsSearchBefore.setVisibility(View.GONE);
                    llNewsSearchAfter.setVisibility(View.VISIBLE);
                    // 获取编辑框焦点
                    etTaobaoSearchContent.setFocusable(true);
                    etTaobaoSearchContent.requestFocus();
                    //打开软键盘
                    InputMethodManager im = (InputMethodManager) getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    break;
                case R.id.rl_edit_text_exit:
                    etTaobaoSearchContent.setText("");
                    break;
            }
        }
    }

}
