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
//                if(mIsRequestDataRefresh ==true) {
//                    stopDataRefresh();
//                    setRefresh(mIsRequestDataRefresh);
//                }
//                loadRecyclerData(rushData);
                netUtils2.okHttp2Server2(group_url,null);
                break;
            case 0:
                Toast.makeText(getContext(), homeWareBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

//    @NonNull
//    private List<HomeRushChangeBean> oldRushWareData(HomeRushBean homeRushBean) {
//        List<HomeRushBean.DataBean> data = homeRushBean.getData();
//        List<HomeRushChangeBean> homeChangeDatas = new ArrayList<>();
//        for (int i=0;i<data.size();i++){
//            HomeRushBean.DataBean dataBean = data.get(i);
//            HomeRushBean.DataBean.TodayNewBean today_new = dataBean.getToday_New();
//            HomeRushBean.DataBean.SiloHandlingBean silo_handling = dataBean.getSilo_Handling();
//            HomeRushBean.DataBean.GoodsWarmupBean goods_warmup = dataBean.getGoods_Warmup();
//            HomeRushBean.DataBean.HotSaleBean hot_sale = dataBean.getHot_Sale();
//
//            HomeRushChangeBean homeRushChangeBean1 = new HomeRushChangeBean();
//            HomeRushChangeBean.CoverBean coverBean1 = new HomeRushChangeBean.CoverBean();
//
//            HomeRushBean.DataBean.TodayNewBean.CoverBean cover1 = today_new.getCover();
//            List<HomeRushBean.DataBean.TodayNewBean.ItemBean> oitem1 = today_new.getItem();
//            LogUtil.e("oitem1.size() = " + oitem1.size());
//            List<HomeRushChangeBean.ItemBean> items1 = new ArrayList<>();
//            for (int j=0;j<oitem1.size();j++){
//                HomeRushChangeBean.ItemBean itemBean1 = new HomeRushChangeBean.ItemBean();
//                HomeRushBean.DataBean.TodayNewBean.ItemBean oitemBean1 = oitem1.get(j);
//                itemBean1.setId(oitemBean1.getId());
//                itemBean1.setCategory_id(oitemBean1.getCategory_id());
//                itemBean1.setSite_id(oitemBean1.getSite_id());
//                itemBean1.setChannel_id(oitemBean1.getChannel_id());
//                itemBean1.setBrand_id(oitemBean1.getBrand_id());
//                itemBean1.setCall_index(oitemBean1.getCall_index());
//                itemBean1.setTitle(oitemBean1.getTitle());
//                itemBean1.setLink_url(oitemBean1.getLink_url());
//                itemBean1.setImg_url(oitemBean1.getImg_url());
//                itemBean1.setStock_quantity(oitemBean1.getStock_quantity());
//                itemBean1.setMarket_price(oitemBean1.getMarket_price());
//                itemBean1.setSell_price(oitemBean1.getSell_price());
//                items1.add(itemBean1);
//            }
//            coverBean1.setAdd_time(cover1.getAdd_time());
//            coverBean1.setBrand_id(cover1.getBrand_id());
//            coverBean1.setCall_index(cover1.getCall_index());
//            coverBean1.setCategory_id(cover1.getCategory_id());
//            coverBean1.setTitle(cover1.getTitle());
//            coverBean1.setLink_url(cover1.getLink_url());
//            coverBean1.setImg_url(cover1.getImg_url());
//            LogUtil.e("items1.size() == " +items1.size());
//            homeRushChangeBean1.setCover(coverBean1);
//            homeRushChangeBean1.setItem(items1);
//
//            HomeRushChangeBean homeRushChangeBean2 = new HomeRushChangeBean();
//            HomeRushChangeBean.CoverBean coverBean2 = new HomeRushChangeBean.CoverBean();
//            HomeRushBean.DataBean.SiloHandlingBean.CoverBeanX cover2 = silo_handling.getCover();
//            List<HomeRushBean.DataBean.SiloHandlingBean.ItemBeanX> oitem2 = silo_handling.getItem();
//            LogUtil.e("oitem2.size() = " + oitem2.size());
//            List<HomeRushChangeBean.ItemBean> items2 = new ArrayList<>();
//            for (int j=0;j<oitem2.size();j++){
//                HomeRushChangeBean.ItemBean itemBean2 = new HomeRushChangeBean.ItemBean();
//                HomeRushBean.DataBean.SiloHandlingBean.ItemBeanX oitemBean2 = oitem2.get(j);
//                itemBean2.setId(oitemBean2.getId());
//                itemBean2.setCategory_id(oitemBean2.getCategory_id());
//                itemBean2.setSite_id(oitemBean2.getSite_id());
//                itemBean2.setChannel_id(oitemBean2.getChannel_id());
//                itemBean2.setBrand_id(oitemBean2.getBrand_id());
//                itemBean2.setCall_index(oitemBean2.getCall_index());
//                itemBean2.setTitle(oitemBean2.getTitle());
//                itemBean2.setLink_url(oitemBean2.getLink_url());
//                itemBean2.setImg_url(oitemBean2.getImg_url());
//                itemBean2.setStock_quantity(oitemBean2.getStock_quantity());
//                itemBean2.setMarket_price(oitemBean2.getMarket_price());
//                itemBean2.setSell_price(oitemBean2.getSell_price());
//                items2.add(itemBean2);
//            }
//            coverBean2.setAdd_time(cover2.getAdd_time());
//            coverBean2.setBrand_id(cover2.getBrand_id());
//            coverBean2.setCall_index(cover2.getCall_index());
//            coverBean2.setCategory_id(cover2.getCategory_id());
//            coverBean2.setTitle(cover2.getTitle());
//            coverBean2.setLink_url(cover2.getLink_url());
//            coverBean2.setImg_url(cover2.getImg_url());
//            LogUtil.e("items2.size() == " +items2.size());
//            homeRushChangeBean2.setCover(coverBean2);
//            homeRushChangeBean2.setItem(items2);
//
//            HomeRushChangeBean homeRushChangeBean3 = new HomeRushChangeBean();
//            HomeRushChangeBean.CoverBean coverBean3 = new HomeRushChangeBean.CoverBean();
//            HomeRushBean.DataBean.GoodsWarmupBean.CoverBeanXX cover3 = goods_warmup.getCover();
//            List<HomeRushBean.DataBean.GoodsWarmupBean.ItemBeanXX> oitem3 = goods_warmup.getItem();
//            LogUtil.e("oitem3.size() = " + oitem3.size());
//            List<HomeRushChangeBean.ItemBean> items3 = new ArrayList<>();
//            for (int j=0;j<oitem3.size();j++){
//                HomeRushChangeBean.ItemBean itemBean3 = new HomeRushChangeBean.ItemBean();
//                HomeRushBean.DataBean.GoodsWarmupBean.ItemBeanXX oitemBean3 = oitem3.get(j);
//                itemBean3.setId(oitemBean3.getId());
//                itemBean3.setCategory_id(oitemBean3.getCategory_id());
//                itemBean3.setSite_id(oitemBean3.getSite_id());
//                itemBean3.setChannel_id(oitemBean3.getChannel_id());
//                itemBean3.setBrand_id(oitemBean3.getBrand_id());
//                itemBean3.setCall_index(oitemBean3.getCall_index());
//                itemBean3.setTitle(oitemBean3.getTitle());
//                itemBean3.setLink_url(oitemBean3.getLink_url());
//                itemBean3.setImg_url(oitemBean3.getImg_url());
//                itemBean3.setStock_quantity(oitemBean3.getStock_quantity());
//                itemBean3.setMarket_price(oitemBean3.getMarket_price());
//                itemBean3.setSell_price(oitemBean3.getSell_price());
//                items3.add(itemBean3);
//
//            }
//            coverBean3.setAdd_time(cover3.getAdd_time());
//            coverBean3.setBrand_id(cover3.getBrand_id());
//            coverBean3.setCall_index(cover3.getCall_index());
//            coverBean3.setCategory_id(cover3.getCategory_id());
//            coverBean3.setTitle(cover3.getTitle());
//            coverBean3.setLink_url(cover3.getLink_url());
//            coverBean3.setImg_url(cover3.getImg_url());
//            homeRushChangeBean3.setCover(coverBean3);
//            homeRushChangeBean3.setItem(items3);
//            LogUtil.e("items3.size() == " +items3.size());
//
//            HomeRushChangeBean homeRushChangeBean4 = new HomeRushChangeBean();
//            HomeRushChangeBean.CoverBean coverBean4 = new HomeRushChangeBean.CoverBean();
//            HomeRushBean.DataBean.HotSaleBean.CoverBeanXXX cover4 = hot_sale.getCover();
//            List<HomeRushBean.DataBean.HotSaleBean.ItemBeanXXX> oitem4 = hot_sale.getItem();
//            LogUtil.e("oitem4.size() = " + oitem4.size());
//            List<HomeRushChangeBean.ItemBean> items4 = new ArrayList<>();
//            for (int j=0;j<oitem4.size();j++){
//                HomeRushChangeBean.ItemBean itemBean4 = new HomeRushChangeBean.ItemBean();
//                HomeRushBean.DataBean.HotSaleBean.ItemBeanXXX oitemBean4 = oitem4.get(j);
//                itemBean4.setId(oitemBean4.getId());
//                itemBean4.setCategory_id(oitemBean4.getCategory_id());
//                itemBean4.setSite_id(oitemBean4.getSite_id());
//                itemBean4.setChannel_id(oitemBean4.getChannel_id());
//                itemBean4.setBrand_id(oitemBean4.getBrand_id());
//                itemBean4.setCall_index(oitemBean4.getCall_index());
//                itemBean4.setTitle(oitemBean4.getTitle());
//                itemBean4.setLink_url(oitemBean4.getLink_url());
//                itemBean4.setImg_url(oitemBean4.getImg_url());
//                itemBean4.setStock_quantity(oitemBean4.getStock_quantity());
//                itemBean4.setMarket_price(oitemBean4.getMarket_price());
//                itemBean4.setSell_price(oitemBean4.getSell_price());
//                items4.add(itemBean4);
//            }
//            coverBean4.setAdd_time(cover4.getAdd_time());
//            coverBean4.setBrand_id(cover4.getBrand_id());
//            coverBean4.setCall_index(cover4.getCall_index());
//            coverBean4.setCategory_id(cover4.getCategory_id());
//            coverBean4.setTitle(cover4.getTitle());
//            coverBean4.setLink_url(cover4.getLink_url());
//            coverBean4.setImg_url(cover4.getImg_url());
//            homeRushChangeBean4.setCover(coverBean4);
//            homeRushChangeBean4.setItem(items4);
//            LogUtil.e("items4.size() == " +items4.size());
//            homeChangeDatas.add(homeRushChangeBean1);
//            homeChangeDatas.add(homeRushChangeBean2);
//            homeChangeDatas.add(homeRushChangeBean3);
//            homeChangeDatas.add(homeRushChangeBean4);
//        }
//        LogUtil.e("homeChangeDatas.size() == "+homeChangeDatas.size());
//        for (int i=0;i<homeChangeDatas.size();i++){
//            HomeRushChangeBean homeRushChangeBean = homeChangeDatas.get(i);
//            List<HomeRushChangeBean.ItemBean> item = homeRushChangeBean.getItem();
//            LogUtil.e("item.size() == " + item.size());
//        }
//        return homeChangeDatas;
//    }

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
            swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark,
                    R.color.white,R.color.red_button_color);
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
