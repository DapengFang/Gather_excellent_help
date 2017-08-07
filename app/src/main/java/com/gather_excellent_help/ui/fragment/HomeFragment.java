package com.gather_excellent_help.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.HomeGroupBean;
import com.gather_excellent_help.bean.HomeRushBean;
import com.gather_excellent_help.bean.HomeRushChangeBean;
import com.gather_excellent_help.bean.HomeWareBean;
import com.gather_excellent_help.bean.QiangTaoBean;
import com.gather_excellent_help.bean.TyepIndexBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.ui.adapter.HomeRushAllAdapter;
import com.gather_excellent_help.ui.base.BaseFragment;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.RushDownTimer;
import com.gather_excellent_help.utils.DataCleanManager;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    private NetUtil netUtils4;//抢购区的商品数据联网
    private String rush_url = Url.BASE_URL + "IndexGoods.aspx";

    private String group_url = Url.BASE_URL + "GroupBuy.aspx";
    private String type_url = Url.BASE_URL + "IndexCategory.aspx";
    private String qiang_url = Url.BASE_URL + "RushBuy.aspx";
    private HomeRushAllAdapter homeRushAllAdapter;
    private List<HomeWareBean.DataBean> rushData;
    private List<HomeGroupBean.DataBean> groupData;
    private List<TyepIndexBean.DataBean> typeData;
    private List<QiangTaoBean.DataBean> qiangData;
    private RushDownTimer rushDownTimer; //倒计时处理类
    public static final int TIME_DOWN = 1; //倒计时显示的标识
    public static final int LOAD_DATA = 2; //加载数据的标识
    private long time;//倒计时总时长
    private boolean isFirst;//是否有抢购

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIME_DOWN:
                    time-=1000;
                    rushDownTimer.calcuteDownTimer(time);
                    down_timer = "down_timer";
                    homeRushAllAdapter.setRushDownTimer(rushDownTimer);
                    if(time<=0) {
                        handler.removeMessages(TIME_DOWN);
                        return;
                    }
                    handler.sendEmptyMessageDelayed(TIME_DOWN,1000);
                    break;
                case LOAD_DATA:
                    if(rushData.size()>0 && groupData.size()>0 && typeData.size()>0 && qiangData.size() > 0 && isFirst) {
                        if(mIsRequestDataRefresh ==true) {
                            stopDataRefresh();
                            setRefresh(mIsRequestDataRefresh);
                        }
                        if(isFirst) {
                           handler.removeMessages(TIME_DOWN);
                           time = endtime - curr_time;
                           rushDownTimer = new RushDownTimer(getContext());
                           handler.sendEmptyMessage(TIME_DOWN);
                           loadRecyclerData(rushData,groupData,typeData,rushDownTimer,qiangData);
                           }
                        handler.removeMessages(LOAD_DATA);
                        return;
                    }else if(rushData.size()>0&&groupData.size()>0&&!isFirst) {
                        if(mIsRequestDataRefresh ==true) {
                            stopDataRefresh();
                            setRefresh(mIsRequestDataRefresh);
                        }
                        loadRecyclerData(rushData,groupData,typeData,rushDownTimer,qiangData);
                        handler.removeMessages(LOAD_DATA);
                        return;
                    }
                    handler.sendEmptyMessageDelayed(LOAD_DATA,200);
                    break;
            }
        }
    };
    private WeakReference<Handler> wef =new WeakReference<Handler>(handler);
    private String down_timer = "";
    private long endtime;
    private long curr_time;

    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.home_fragment, null);
        return inflate;
    }

    @Override
    public void initData() {
        cleanCache();
        initNetUtils();
        requestDataRefresh();
        setRefresh(mIsRequestDataRefresh);
        initMutilData();
        net2Server();
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e("热销"+response);
                if(getContext()==null) {
                    return;
                }
                parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                stopDataRefresh();
                setRefresh(mIsRequestDataRefresh);
                if(getContext()==null) {
                    return;
                }
                Toast.makeText(getContext(), "请检查你的网络连接是否正常！", Toast.LENGTH_SHORT).show();
            }
        });
        netUtils2.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e("团购" + response);
                if(getContext()==null) {
                    return;
                }
                parseData2(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });
        netUtils3.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e("分类"+response);
                if(getContext()==null) {
                    return;
                }
                parseData3(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });
        netUtils4.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e("抢购" + response);
                if(getContext()==null) {
                    return;
                }
                parseData4(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e("nerUtils4=="+call.toString()+e.getMessage());
                isFirst =false;
            }
        });

    }

    /**
     * 初始化首页数据
     */
    private void initMutilData() {
        rushData = new ArrayList<>();
        groupData = new ArrayList<>();
        typeData = new ArrayList<>();
        qiangData = new ArrayList<>();
    }

    /**
     * 联网请求
     */
    private void net2Server() {
        curr_time = System.currentTimeMillis();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        Date date = new Date(curr_time);
        String currtimes = sdf.format(date);
        String[] timesArray = currtimes.split(":");
        String year = timesArray[0];
        String month = timesArray[1];
        String day = timesArray[2];
        String hour = timesArray[3];
        String minute = timesArray[4];
        String second = timesArray[5];
        int currHour = Integer.parseInt(hour);
        int currDay = Integer.parseInt(day);
        String start_time = year+"-"+month+"-"+day+" "+hour+":00:00";
        Date d= null;
        try {
            d = sdf2.parse(start_time);
            long time = d.getTime();
            endtime = time + 3600 * 1000;
            String end_time = sdf2.format(new Date(endtime));
            Map<String, String> map = new HashMap<>();
            map.put("pageSize","3");
            map.put("pageIndex","1");
            map.put("start_time",start_time);
            map.put("end_time",end_time);
            netUtils4.okHttp2Server2(qiang_url,map);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        netUtils.okHttp2Server2(rush_url, null);
        netUtils2.okHttp2Server2(group_url,null);
        netUtils3.okHttp2Server2(type_url,null);
    }

    /**
     * 出事化联网加载类
     */
    private void initNetUtils() {
        netUtils = new NetUtil();
        netUtils2 = new NetUtil();
        netUtils3 = new NetUtil();
        netUtils4 = new NetUtil();
    }

    /**
     * 解析首页抢购数据
     * @param response
     */
    private void parseData4(String response) {
        QiangTaoBean qiangTaoBean = new Gson().fromJson(response, QiangTaoBean.class);
        int statusCode = qiangTaoBean.getStatusCode();
        switch (statusCode) {
            case 1 :
                isFirst =true;
                qiangData = qiangTaoBean.getData();
                break;
            case 0:
                if(getContext()==null) {
                    return;
                }
                Toast.makeText(getContext(), qiangTaoBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
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
                handler.sendEmptyMessage(LOAD_DATA);
                break;
            case 0:
                Toast.makeText(getContext(), homeWareBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void loadRecyclerData(List<HomeWareBean.DataBean> homeChangeDatas, List<HomeGroupBean.DataBean> groupData, List<TyepIndexBean.DataBean> typeData, RushDownTimer rushDownTimer, List<QiangTaoBean.DataBean> qiangData) {
        if(rcvHomeFragment!=null) {
            FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(getContext());
            rcvHomeFragment.setLayoutManager(layoutManager);
            homeRushAllAdapter = new HomeRushAllAdapter(getContext(),homeChangeDatas,getActivity(),groupData,typeData,rushDownTimer,qiangData,isFirst);
            rcvHomeFragment.setAdapter(homeRushAllAdapter);
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
        handler.removeMessages(TIME_DOWN);
        handler.removeMessages(LOAD_DATA);
        wef.clear();
        cleanCache();
    }

    /**
     * 清除缓存
     */
    private void cleanCache() {
       checkContextNull();
        DataCleanManager.cleanInternalCache(getContext());
        DataCleanManager.cleanExternalCache(getContext());
    }

    private void  checkContextNull() {
        if(getContext()==null) {
            return ;
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
                    cleanCache();
                    requestDataRefresh();
                    setRefresh(mIsRequestDataRefresh);
                    netUtils.okHttp2Server2(rush_url, null);
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
    public void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                netUtils.okHttp2Server2(rush_url, null);
            }
        },1000);
    }
    public void onEvent(AnyEvent event) {
        String msg = "onEventMainThread收到了消息：" + event.getMessage();
        LogUtil.e("homefragment");
        LogUtil.e(msg);
        initData();
    }
}
