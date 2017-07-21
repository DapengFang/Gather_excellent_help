package com.gather_excellent_help.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.HomeGroupBean;
import com.gather_excellent_help.bean.HomeRushBean;
import com.gather_excellent_help.bean.HomeRushChangeBean;
import com.gather_excellent_help.bean.HomeWareBean;
import com.gather_excellent_help.bean.TyepIndexBean;
import com.gather_excellent_help.ui.adapter.HomeRushAllAdapter;
import com.gather_excellent_help.ui.base.BaseFragment;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2017/7/7.
 */

public class HomeFragment extends BaseFragment {

    @Bind(R.id.rcv_home_fragment)
    RecyclerView rcvHomeFragment;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private boolean mIsRequestDataRefresh = false;
    private NetUtil netUtils;//热销区域的联网接口
    private NetUtil netUtils2;//团购区的商品联网接口
    private NetUtil netUtils3;//分类区的商品联网接口
    private String url = Url.BASE_URL + "IndexGoods.aspx";
    private String group_url = Url.BASE_URL + "GroupBuy.aspx";
    private String type_url = Url.BASE_URL + "IndexCategory.aspx";
    private HomeRushAllAdapter homeRushAllAdapter;
    private List<HomeWareBean.DataBean> rushData;
    private List<HomeGroupBean.DataBean> groupData;
    private List<TyepIndexBean.DataBean> typeData;

    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.home_fragment, null);
        return inflate;
    }

    @Override
    public void initData() {
        netUtils = new NetUtil();
        netUtils2 = new NetUtil();
        netUtils3 = new NetUtil();
        requestDataRefresh();
        setRefresh(mIsRequestDataRefresh);
        netUtils.okHttp2Server2(url, null);
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                stopDataRefresh();
                setRefresh(mIsRequestDataRefresh);
            }
        });
        netUtils2.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                parseData2(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });
        netUtils3.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                parseData3(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });
    }

    /**
     * @param response
     * 解析首页分类索引
     */
    private void parseData3(String response) {
        TyepIndexBean tyepIndexBean = new Gson().fromJson(response, TyepIndexBean.class);
        int statusCode = tyepIndexBean.getStatusCode();
        switch (statusCode) {
            case 1 :
                typeData = tyepIndexBean.getData();
                if(mIsRequestDataRefresh ==true) {
                    stopDataRefresh();
                    setRefresh(mIsRequestDataRefresh);
                }
                loadRecyclerData(rushData,groupData,typeData);
                break;
            case 0:
                Toast.makeText(getContext(), tyepIndexBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析团购区数据
     * @param response
     */
    private void parseData2(String response) {
        HomeGroupBean homeGroupBean = new Gson().fromJson(response, HomeGroupBean.class);
        int statusCode = homeGroupBean.getStatusCode();
        switch (statusCode) {
            case 1 :
                groupData = homeGroupBean.getData();
                netUtils3.okHttp2Server2(type_url,null);
                break;
            case 0:
                Toast.makeText(getContext(), homeGroupBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析数据
     * @param response
     */
    private void parseData(String response) {
        HomeWareBean homeWareBean = new Gson().fromJson(response, HomeWareBean.class);
        int statusCode = homeWareBean.getStatusCode();
        switch (statusCode) {
            case 1 :
                rushData = homeWareBean.getData();
                netUtils2.okHttp2Server2(group_url,null);
                break;
            case 0:
                Toast.makeText(getContext(), homeWareBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void loadRecyclerData(List<HomeWareBean.DataBean> homeChangeDatas, List<HomeGroupBean.DataBean> groupData,List<TyepIndexBean.DataBean> typeData) {
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(getContext());
        rcvHomeFragment.setLayoutManager(layoutManager);
        homeRushAllAdapter = new HomeRushAllAdapter(getContext(),homeChangeDatas,getActivity(),groupData,typeData);
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
            swipeRefresh.setColorSchemeResources(R.color.colorFirst,
                    R.color.colorSecond,R.color.colorThird);
            swipeRefresh.setProgressViewOffset(true, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,getResources().getDisplayMetrics()));
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestDataRefresh();
                    setRefresh(mIsRequestDataRefresh);
                    netUtils.okHttp2Server2(url, null);
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
