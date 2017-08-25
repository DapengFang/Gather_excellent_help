package com.gather_excellent_help.presenter.homepresenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.HomeWareBean;
import com.gather_excellent_help.presenter.BasePresenter;
import com.gather_excellent_help.ui.widget.DividerItemDecoration;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.update.HomeActivityAdapter;
import com.gather_excellent_help.update.HomeActivityListAdapter;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2017/8/22.
 */

public class ActivityPresenter extends BasePresenter {

    private Context context;
    private RecyclerView rcvHomeActivity;
    private String rush_url = Url.BASE_URL + "IndexGoods.aspx";
    private NetUtil netUtil;
    private List<HomeWareBean.DataBean> rushData;

    public ActivityPresenter(Context context, RecyclerView rcvHomeActivity) {
        this.context = context;
        this.rcvHomeActivity = rcvHomeActivity;
        netUtil = new NetUtil();
    }

    @Override
    public View initView() {
        return rcvHomeActivity;
    }

    @Override
    public void initData() {
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(context);
        rcvHomeActivity.setLayoutManager(layoutManager);
        netUtil.okHttp2Server2(rush_url,null);
        netUtil.setOnServerResponseListener(new MyOnServerResponseListener());
    }

    public class MyOnServerResponseListener implements NetUtil.OnServerResponseListener{

        @Override
        public void getSuccessResponse(String response) {
            if(context!=null) {
                LogUtil.e("activity_presenter === "+response);
                parseData(response);
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
               if(context!=null) {
                   Toast.makeText(context, "请检查你的网络连接是否正常！", Toast.LENGTH_SHORT).show();
               }
        }
    }

    private void parseData(String response) {
        HomeWareBean homeWareBean = new Gson().fromJson(response, HomeWareBean.class);
        int statusCode = homeWareBean.getStatusCode();
        switch (statusCode) {
            case 1 :
                rushData = homeWareBean.getData();
                HomeActivityAdapter homeActivityAdapter = new HomeActivityAdapter(context,rushData);
                rcvHomeActivity.setAdapter(homeActivityAdapter);
                break;
            case 0:
                Toast.makeText(context, homeWareBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
