package com.gather_excellent_help.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wuxin on 2017/7/7.
 */

public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    public Context mContext;

    /** * 控件是否初始化完成 */
    private boolean isViewCreated;
    /** * 数据是否已加载完毕 */
    private boolean isLoadDataCompleted;

    public BaseFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
////        Log.d("TAG", "fragment->setUserVisibleHint");
//        if (getUserVisibleHint()) {
//            isVisible = true;
//            lazyLoad();
//        } else {
//            isVisible = false;
//            onInvisible();
//        }
//    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
//        Log.d("TAG", "fragment->onCreate");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = initView();
        }
//        Log.d("TAG", "fragment->onCreateView");
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Log.d("TAG", "fragment->onActivityCreated");
        initData();
    }

//    protected void lazyLoad() {
//        if (!isPrepared || !isVisible || !isFirst) {
//            return;
//        }
//        Log.d("TAG", getClass().getName() + "->initData()");
//        initData();
//        isFirst = false;
//    }

//    //do something
//    protected void onInvisible() {
//
//
//    }

    public abstract View initView();

    public abstract void initData();
}
