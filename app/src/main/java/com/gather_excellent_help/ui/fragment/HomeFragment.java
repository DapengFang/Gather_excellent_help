package com.gather_excellent_help.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.adapter.HomeRushAllAdapter;
import com.gather_excellent_help.ui.base.BaseFragment;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dapeng Fang on 2017/7/7.
 */

public class HomeFragment extends BaseFragment {

    @Bind(R.id.rcv_home_fragment)
    RecyclerView rcvHomeFragment;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private boolean mIsRequestDataRefresh = false;

    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.home_fragment, null);
        return inflate;
    }

    @Override
    public void initData() {
        HomeRushAllAdapter homeRushAllAdapter = new HomeRushAllAdapter(getContext());
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(getContext());
        rcvHomeFragment.setLayoutManager(layoutManager);
        rcvHomeFragment.setAdapter(homeRushAllAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        setupSwipeRefresh(rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setupSwipeRefresh(View view){
        if(swipeRefresh != null){
            swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark,
                    R.color.white,R.color.red_button_color);
            swipeRefresh.setProgressViewOffset(true, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,getResources().getDisplayMetrics()));
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestDataRefresh();
                    setRefresh(mIsRequestDataRefresh);
                    swipeRefresh.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stopDataRefresh();
                            setRefresh(mIsRequestDataRefresh);
                        }
                    },1000);
                }
            });
        }
    }

    public void requestDataRefresh() {
        mIsRequestDataRefresh = true;
    }
    public void stopDataRefresh(){
        mIsRequestDataRefresh = false;
    }


    /**
     * 设置刷新的方法
     * @param requestDataRefresh 是否需要刷新
     */
    public void setRefresh(boolean requestDataRefresh) {
        if (swipeRefresh == null) {
            return;
        }
        if (!requestDataRefresh) {
            mIsRequestDataRefresh = false;
            swipeRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefresh != null) {
                        swipeRefresh.setRefreshing(false);
                    }
                }
            },1000);
        } else {
            swipeRefresh.setRefreshing(true);
        }
    }

}
