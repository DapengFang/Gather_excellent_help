package com.gather_excellent_help.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.QiangTaoBean;
import com.gather_excellent_help.bean.QiangTimeBean;
import com.gather_excellent_help.bean.SearchWareBean;
import com.gather_excellent_help.ui.adapter.TaoQiangTimeAdapter;
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

public class QiangTaoActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.rcv_horizational_time_navigator)
    RecyclerView rcvHorizationalTimeNavigator;
    @Bind(R.id.gv_wart_list)
    GridView gvWartList;

    private String qiang_url = Url.BASE_URL + "RushBuy.aspx";
    private long curr_time;
    private long endtime;
    private NetUtil netUtils;
    private List<QiangTaoBean.DataBean> qiangData;
    private WareListAdapter wareListAdapter;
    private String end_time;
    private String time_head;
    private String start_time;
    private List<SearchWareBean.DataBean> wareData;
    private int curr_click = 0;//当前点击

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
        tvTopTitleName.setText("淘抢购");
        rlExit.setOnClickListener(new MyOnclickListener());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvHorizationalTimeNavigator.setLayoutManager(mLayoutManager);
        netUtils = new NetUtil();
        net2Server();
        final ArrayList<QiangTimeBean> timeData = loadTimeNavData();
        final TaoQiangTimeAdapter taoQiangTimeAdapter = new TaoQiangTimeAdapter(this, timeData);
        rcvHorizationalTimeNavigator.setAdapter(taoQiangTimeAdapter);
        taoQiangTimeAdapter.setOnItemclickListener(new TaoQiangTimeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                curr_click =position;
                for (int i=0;i<timeData.size();i++){
                    QiangTimeBean qiangTimeBean = timeData.get(i);
                    if(i==position) {
                        qiangTimeBean.setCheck(true);
                    }else{
                        qiangTimeBean.setCheck(false);
                    }
                }
                taoQiangTimeAdapter.notifyDataSetChanged();
                QiangTimeBean qiangTimeBean = timeData.get(position);
                int time = qiangTimeBean.getTime();
                if(time==23) {
                    start_time = time_head +"23:00";
                    end_time = time_head +"00:00";
                }else{
                    start_time = time_head +time+":00";
                    end_time = time_head +(time+1)+":00";
                }
                net2ServerCheck();
            }
        });
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1:
                        if(gvWartList==null) {
                            return;
                        }
                        SearchWareBean searchWareBean = new Gson().fromJson(response, SearchWareBean.class);
                        wareData = searchWareBean.getData();
                        wareListAdapter = new WareListAdapter(QiangTaoActivity.this, wareData);
                        gvWartList.setAdapter(wareListAdapter);
                        gvWartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                if(wareData ==null) {
                                    return;
                                }
                                if(curr_click == 0) {
                                    String link_url = wareData.get(i).getLink_url();
                                    String goods_id = wareData.get(i).getProductId();
                                    String goods_img = wareData.get(i).getImg_url();
                                    String goods_title = wareData.get(i).getTitle();
                                    Intent intent = new Intent(QiangTaoActivity.this, WebRecordActivity.class);
                                    intent.putExtra("url",link_url);
                                    intent.putExtra("goods_id",goods_id);
                                    intent.putExtra("goods_img",goods_img);
                                    intent.putExtra("goods_title",goods_title);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(QiangTaoActivity.this, "抢购还未开启，请够耐心的等待！", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
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
    private void net2Server() {
        LogUtil.e(start_time+"--------"+end_time);
        start_time = getCurrentTime();
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", "6");
        map.put("pageIndex", "1");
        map.put("start_time", start_time);
        map.put("end_time", end_time);
        netUtils.okHttp2Server2(qiang_url, map);
    }
    /**
     * 联网请求
     */
    private void net2ServerCheck() {
        LogUtil.e(start_time+"--------"+end_time);
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", "6");
        map.put("pageIndex", "1");
        map.put("start_time", start_time);
        map.put("end_time", end_time);
        netUtils.okHttp2Server2(qiang_url, map);
    }

    private ArrayList<QiangTimeBean> loadTimeNavData(){
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
       for (int i=currHour;i<24;i++){
           QiangTimeBean qiangTimeBean = new QiangTimeBean();
           qiangTimeBean.setTime(i);
           times.add(qiangTimeBean);
           if(i==currHour) {
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
}
