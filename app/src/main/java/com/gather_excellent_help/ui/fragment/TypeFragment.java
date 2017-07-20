package com.gather_excellent_help.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.gather_excellent_help.R;
import com.gather_excellent_help.aliapi.DemoTradeCallback;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.TypeNavigatorBean;
import com.gather_excellent_help.bean.TypeWareBean;
import com.gather_excellent_help.ui.activity.WareListActivity;
import com.gather_excellent_help.ui.activity.WebRecordActivity;
import com.gather_excellent_help.ui.adapter.TypeWareAdapter;
import com.gather_excellent_help.ui.base.BaseFragment;
import com.gather_excellent_help.ui.widget.Classes;
import com.gather_excellent_help.ui.widget.College;
import com.gather_excellent_help.ui.widget.SimpleExpandableListViewAdapter;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.imageutils.ImageLoader;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;


/**
 * created by Dapeng Fang
 */
public class TypeFragment extends BaseFragment {
    @Bind(R.id.iv_zhuangtai_exit)
    ImageView ivZhuangtaiExit;
    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.et_sousuo)
    EditText etSousuo;
    @Bind(R.id.cus_list_navigator)
    ExpandableListView cusListNavigator;
    @Bind(R.id.rcv_type_show)
    RecyclerView rcvTypeShow;
    @Bind(R.id.tv_type_search)
    TextView tvTypeSearch;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;


    private boolean mIsRequestDataRefresh = false;
    private NetUtil netUtil;
    private NetUtil netUtil2;
    private String url = Url.BASE_URL + "CategoryList.aspx";
    private String ware_url = Url.BASE_URL + "CategoryGoodList.aspx";
    private TypeNavigatorBean.DataBean.SubListBean subListBean;
    private TypeNavigatorBean.DataBean.SubListBean.ThirdListBean thirdListBean;

    /**
     * @return 布局对象
     */
    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.type_fragment, null);
        return inflate;
    }

    /**
     * 数据初始化
     */
    @Override
    public void initData() {
        requestDataRefresh();
        swipeRefresh.setRefreshing(mIsRequestDataRefresh);
        netUtil = new NetUtil();
        netUtil2 = new NetUtil();
        tvTopTitleName.setText("商品分类");
        ivZhuangtaiExit.setVisibility(View.GONE);
        getNavigatorData();
        netUtil2.okHttp2Server2(ware_url,null);
        netUtil2.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                 parseData2(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });
        tvTypeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = etSousuo.getText().toString().trim();
                if(TextUtils.isEmpty(content)) {
                    Toast.makeText(getActivity(), "请输入你要搜索商品的名称！", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(getActivity(), WareListActivity.class);
                    intent.putExtra("content",content);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 解析商品数据
     * @param response
     */
    private void parseData2(String response) {
        TypeWareBean typeWareBean = new Gson().fromJson(response, TypeWareBean.class);
        int statusCode = typeWareBean.getStatusCode();
        switch (statusCode) {
            case 1 :
                if(mIsRequestDataRefresh ==true) {
                    stopDataRefresh();
                    setRefresh(mIsRequestDataRefresh);
                }
                final List<TypeWareBean.DataBean> data = typeWareBean.getData();
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                rcvTypeShow.setLayoutManager(layoutManager);
                TypeWareAdapter typeWareAdapter = new TypeWareAdapter(getContext(),data);
                rcvTypeShow.setAdapter(typeWareAdapter);
                typeWareAdapter.setOnItemClickListener(new TypeWareAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        String link_url = data.get(position).getLink_url();
                        String goods_id = data.get(position).getProductId();
                        String goods_img = Url.IMG_URL + data.get(position).getImg_url();
                        String goods_title = data.get(position).getTitle();
                        Intent intent = new Intent(getContext(), WebRecordActivity.class);
                        intent.putExtra("url",link_url);
                        intent.putExtra("goods_id",goods_id);
                        intent.putExtra("goods_img",goods_img);
                        intent.putExtra("goods_title",goods_title);
                        getContext().startActivity(intent);
                    }
                });
                break;
            case 0:
                if(mIsRequestDataRefresh ==true) {
                    stopDataRefresh();
                    setRefresh(mIsRequestDataRefresh);
                }
                break;
        }
    }

    /**
     * 获取左边导航数据
     */
    private void getNavigatorData() {

        netUtil.okHttp2Server2(url,null);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });

    }

    /**
     * 解析一级索引的数据
     * @param response
     */
    private void parseData(String response) {
        TypeNavigatorBean navigatorData = new Gson().fromJson(response, TypeNavigatorBean.class);
        int statusCode = navigatorData.getStatusCode();
        switch (statusCode) {
            case 1 :
                final List<TypeNavigatorBean.DataBean> data = navigatorData.getData();
                final SimpleExpandableListViewAdapter adapter = new SimpleExpandableListViewAdapter(data,getActivity());
                // 设置适配器
                cusListNavigator.setAdapter(adapter);
                cusListNavigator.setGroupIndicator(null);
                adapter.setOnExpandableClickListener(new SimpleExpandableListViewAdapter.OnExpandableClickListener() {
                    @Override
                    public void onSecondItemClick(int position, int groupPisition) {
                        subListBean = data.get(position).getSubList().get(groupPisition);
                        int id = subListBean.getId();
                        Map<String,String> map = new HashMap<>();
                        map.put("id",String.valueOf(id));
                        netUtil2.okHttp2Server2(ware_url,map);
                    }

                    @Override
                    public void onThirdItemClick(int position, int groupPisition, int childPosition) {
                        thirdListBean = data.get(position).getSubList().get(groupPisition).getThreeList().get(childPosition);
                        int id = thirdListBean.getId();
                        Map<String,String> map = new HashMap<>();
                        map.put("id",String.valueOf(id));
                        netUtil2.okHttp2Server2(ware_url,map);
                    }
                });

                if(mIsRequestDataRefresh ==true) {
                    netUtil2.okHttp2Server2(ware_url,null);
                }
                break;
            case 0:
                if(mIsRequestDataRefresh ==true) {
                    stopDataRefresh();
                    setRefresh(mIsRequestDataRefresh);
                }
                break;
        }

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
                    netUtil.okHttp2Server2(url, null);
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
}
