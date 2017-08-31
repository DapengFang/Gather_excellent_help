package com.gather_excellent_help.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.QiangTaoBean;
import com.gather_excellent_help.bean.QiangTimeBean;
import com.gather_excellent_help.bean.SearchTaobaoBean;
import com.gather_excellent_help.bean.SearchWareBean;
import com.gather_excellent_help.ui.adapter.TaoQiangTimeAdapter;
import com.gather_excellent_help.ui.adapter.TaobaoWareListAdapter;
import com.gather_excellent_help.ui.adapter.WareListAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

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

import static android.R.attr.type;

public class QiangTaoActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.rcv_horizational_time_navigator)
    RecyclerView rcvHorizationalTimeNavigator;
    @Bind(R.id.gv_wart_list)
    GridView gvWartList;
    @Bind(R.id.ll_ware_list_loadmore)
    LinearLayout llWareListLoadmore;

    private String qiang_url = Url.BASE_URL + "RushBuy.aspx";
    private long curr_time;
    private long endtime;
    private NetUtil netUtils;
    private List<QiangTaoBean.DataBean> qiangData;
    //private WareListAdapter wareListAdapter;
    private String end_time;
    private String time_head;
    private String start_time;
    private List<SearchWareBean.DataBean> wareData;
    private int curr_click = 0;//当前点击
    private int isLoadmore = -1;
    private int page = 1;//加载更多
    private Handler handler = new Handler();
    private String page_no ="1";//第几页
    private String page_size = "10";//每页多少
//    private List<SearchWareBean.DataBean> newData;
//    private List<SearchWareBean.DataBean> taobaodata;

    private List<SearchTaobaoBean.DataBean> taobaodata;//要加载的数据
    private List<SearchTaobaoBean.DataBean> newData;//每次获取的数据
    private TaobaoWareListAdapter taobaoWareListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qiang_tao);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        showLoading();
        rlExit.setOnClickListener(new MyOnclickListener());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvHorizationalTimeNavigator.setLayoutManager(mLayoutManager);
        netUtils = new NetUtil();
        start_time = getCurrentTime();
        net2Server(page_size,page_no,start_time,end_time);
        final ArrayList<QiangTimeBean> timeData = loadTimeNavData();
        final TaoQiangTimeAdapter taoQiangTimeAdapter = new TaoQiangTimeAdapter(this, timeData);
        rcvHorizationalTimeNavigator.setAdapter(taoQiangTimeAdapter);
        taoQiangTimeAdapter.setOnItemclickListener(new TaoQiangTimeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                curr_click = position;
                for (int i = 0; i < timeData.size(); i++) {
                    QiangTimeBean qiangTimeBean = timeData.get(i);
                    if (i == position) {
                        qiangTimeBean.setCheck(true);
                    } else {
                        qiangTimeBean.setCheck(false);
                    }
                }
                taoQiangTimeAdapter.notifyDataSetChanged();
                QiangTimeBean qiangTimeBean = timeData.get(position);
                int time = qiangTimeBean.getTime();
                if (time == 23) {
                    start_time = time_head + "23:00";
                    end_time = time_head + "00:00";
                } else {
                    start_time = time_head + time + ":00";
                    end_time = time_head + (time + 1) + ":00";
                }
                isLoadmore = -1;
                page_no = "1";
                net2Server(page_size,page_no,start_time,end_time);
            }
        });
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e("qiangtao = "+ response);
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1:
                        if (gvWartList == null) {
                            return;
                        }
                        SearchTaobaoBean searchTaobaoBean = new Gson().fromJson(response, SearchTaobaoBean.class);
                        if(isLoadmore!=-1) {
                            page++;
                            LogUtil.e("page == "+page);
                            newData = searchTaobaoBean.getData();
                            taobaodata.addAll(newData);
                            taobaoWareListAdapter.notifyDataSetChanged();
                        }else{
                            taobaodata = searchTaobaoBean.getData();
                            newData = taobaodata;
                            taobaoWareListAdapter = new TaobaoWareListAdapter(QiangTaoActivity.this, taobaodata);
                            gvWartList.setAdapter(taobaoWareListAdapter);
                            page = 2;
                        }

                        gvWartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                if (taobaodata == null) {
                                    return;
                                }
                                if (curr_click == 0) {
                                    LogUtil.e("淘抢购 ===" +taobaodata.size());
                                    SearchTaobaoBean.DataBean.CouponInfoBean coupon_info = taobaodata.get(i).getCoupon_info();
                                    if(coupon_info!=null) {
                                        String coupon_info_cest = coupon_info.getCoupon_info();
                                        String coupon_click_url = coupon_info.getCoupon_click_url();
                                        if(coupon_info_cest!=null && !TextUtils.isEmpty(coupon_info_cest)) {
                                            String link_url = taobaodata.get(i).getLink_url();
                                            String goods_id = String.valueOf(taobaodata.get(i).getProductId());
                                            String goods_img = taobaodata.get(i).getImg_url();
                                            String goods_title = taobaodata.get(i).getTitle();
                                            String sell_price = taobaodata.get(i).getSell_price();
                                            int index = coupon_info_cest.indexOf("减")+1;
                                            String coupon = coupon_info_cest.substring(index, coupon_info_cest.length() - 1);
                                            if(coupon_click_url!=null && !TextUtils.isEmpty(coupon_click_url)) {
                                                Intent intent = new Intent(QiangTaoActivity.this, WebActivity.class);
                                                intent.putExtra("web_url", coupon_click_url);
                                                intent.putExtra("url", link_url);
                                                intent.putExtra("goods_id", goods_id);
                                                intent.putExtra("goods_img", goods_img);
                                                intent.putExtra("goods_title", goods_title);
                                                intent.putExtra("goods_price",sell_price);
                                                intent.putExtra("goods_coupon",coupon);
                                                intent.putExtra("goods_coupon_url",coupon_click_url);
                                                startActivity(intent);
                                            }else{
                                                Intent intent = new Intent(QiangTaoActivity.this, WebRecordActivity.class);
                                                intent.putExtra("url", link_url);
                                                intent.putExtra("goods_id", goods_id);
                                                intent.putExtra("goods_img", goods_img);
                                                intent.putExtra("goods_title", goods_title);
                                                intent.putExtra("goods_price",sell_price);
                                                startActivity(intent);
                                            }
                                        }else{
                                            String link_url = taobaodata.get(i).getLink_url();
                                            String goods_id = String.valueOf(taobaodata.get(i).getProductId());
                                            String goods_img = taobaodata.get(i).getImg_url();
                                            String goods_title = taobaodata.get(i).getTitle();
                                            String sell_price = taobaodata.get(i).getSell_price();
                                            Intent intent = new Intent(QiangTaoActivity.this, WebRecordActivity.class);
                                            intent.putExtra("url", link_url);
                                            intent.putExtra("goods_id", goods_id);
                                            intent.putExtra("goods_img", goods_img);
                                            intent.putExtra("goods_title", goods_title);
                                            intent.putExtra("goods_price",sell_price);
                                            startActivity(intent);
                                        }
                                    }else{

                                    }
                                } else {
                                    Toast.makeText(QiangTaoActivity.this, "抢购还未开启，请够耐心的等待！", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        gvWartList.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                                    if (absListView.getLastVisiblePosition() == (absListView.getCount() - 1)) {
                                        isLoadmore = 0;
                                        page_no = String.valueOf(page);
                                        if(newData.size() <Integer.valueOf(page_size)) {
                                            showLoadNoMore();
                                        }else{
                                            showLoadMore();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    net2Server(page_size,page_no,start_time,end_time);
                                                }
                                            },500);
                                        }
                                    }else{
                                        llWareListLoadmore.setVisibility(View.GONE);
                                    }
                                }

                            }

                            @Override
                            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                            }
                        });
                        llWareListLoadmore.setVisibility(View.GONE);
                        break;
                    case 0:

                        break;
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });
    }

    private void showLoadMore() {
        llWareListLoadmore.setVisibility(View.VISIBLE);
        TextView tvTitle = (TextView) llWareListLoadmore.getChildAt(1);
        llWareListLoadmore.getChildAt(0).setVisibility(View.VISIBLE);
        tvTitle.setText("加载更多...");
    }

    private void showLoadNoMore() {
        TextView tvTitle = (TextView) llWareListLoadmore.getChildAt(1);
        tvTitle.setText("没有更多的数据了...");
        llWareListLoadmore.getChildAt(0).setVisibility(View.GONE);
        llWareListLoadmore.setVisibility(View.VISIBLE);
    }
    private void showLoading() {
        TextView tvTitle = (TextView) llWareListLoadmore.getChildAt(1);
        tvTitle.setText("正在加载中...");
        llWareListLoadmore.getChildAt(0).setVisibility(View.VISIBLE);
        llWareListLoadmore.setVisibility(View.VISIBLE);
    }

    /**
     * 监听点击事件的类
     */
    public class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
            }
        }

    }


    /**
     * 联网请求
     */
    private void net2Server(String page_size,String page_no,String start_time,String end_time) {
        LogUtil.e(start_time + "--------" + end_time);
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", page_size);
        map.put("pageIndex", page_no);
        map.put("start_time", start_time);
        map.put("end_time", end_time);
        netUtils.okHttp2Server2(qiang_url, map);
    }

//    /**
//     * 联网请求
//     */
//    private void net2ServerCheck() {
//        LogUtil.e(start_time + "--------" + end_time);
//        Map<String, String> map = new HashMap<>();
//        map.put("pageSize", "6");
//        map.put("pageIndex", "1");
//        map.put("start_time", start_time);
//        map.put("end_time", end_time);
//        netUtils.okHttp2Server2(qiang_url, map);
//    }

    private ArrayList<QiangTimeBean> loadTimeNavData() {
        curr_time = System.currentTimeMillis();
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
        ArrayList<QiangTimeBean> times = new ArrayList<>();
        for (int i = currHour; i < 24; i++) {
            QiangTimeBean qiangTimeBean = new QiangTimeBean();
            qiangTimeBean.setTime(i);
            times.add(qiangTimeBean);
            if (i == currHour) {
                qiangTimeBean.setCheck(true);
            }
        }
        return times;
    }

    @NonNull
    private String getCurrentTime() {
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
        time_head = year + "-" + month + "-" + day + " ";
        String start_time = year + "-" + month + "-" + day + " " + hour + ":00:00";
        Date d = null;
        try {
            d = sdf2.parse(start_time);
            long time = d.getTime();
            endtime = time + 3600 * 1000;
            end_time = sdf2.format(new Date(endtime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return start_time;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
