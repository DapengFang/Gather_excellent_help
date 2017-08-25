package com.gather_excellent_help.presenter.homepresenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.gather_excellent_help.api.HomeData;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.HomeTypeBean;
import com.gather_excellent_help.bean.TyepIndexBean;
import com.gather_excellent_help.presenter.BasePresenter;
import com.gather_excellent_help.ui.activity.WareListActivity;
import com.gather_excellent_help.ui.adapter.HomeTypeAdapter;
import com.gather_excellent_help.ui.widget.MyGridView;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2017/8/21.
 */

public class TypePresenter extends BasePresenter {
    private Context context;
    private MyGridView gvHomeType;
    private String type_url = Url.BASE_URL + "IndexCategory.aspx";
    private NetUtil netUtil;
    private List<TyepIndexBean.DataBean> typeData;

    public TypePresenter(Context context, MyGridView gvHomeType) {
        this.context = context;
        this.gvHomeType = gvHomeType;
        netUtil = new NetUtil();
    }

    @Override
    public View initView() {
        return gvHomeType;
    }

    @Override
    public void initData() {
        netUtil.okHttp2Server2(type_url,null);
        netUtil.setOnServerResponseListener(new MyOnServerResponseListener());
    }

    public class MyOnServerResponseListener implements NetUtil.OnServerResponseListener{

        @Override
        public void getSuccessResponse(String response) {
           if(context!=null) {
               parseData(response);
           }
            onStopRefreshListener.stopSuccessRefresh();
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString()+"----"+e.getMessage());
            onStopRefreshListener.stopFailRefresh();
        }
    }

    private void parseData(String response) {
        TyepIndexBean tyepIndexBean = new Gson().fromJson(response, TyepIndexBean.class);
        int statusCode = tyepIndexBean.getStatusCode();
        switch (statusCode) {
            case 1 :
                typeData = tyepIndexBean.getData();
                ArrayList<HomeTypeBean> lists = new ArrayList<>();
                for (int i = 0; i < HomeData.titles.length; i++) {
                    HomeTypeBean homeTypeBean = new HomeTypeBean(HomeData.imgs[i], HomeData.titles[i]);
                    lists.add(homeTypeBean);
                }
                HomeTypeAdapter homeTypeAdapter = new HomeTypeAdapter(context, lists,typeData);
                gvHomeType.setAdapter(homeTypeAdapter);
                gvHomeType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        int id = typeData.get(i).getId();
                        Intent intent = new Intent(context, WareListActivity.class);
                        intent.putExtra("type_id", String.valueOf(id));
                        context.startActivity(intent);
                    }
                });
                break;
            case 0:
                Toast.makeText(context, tyepIndexBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private OnStopRefreshListener onStopRefreshListener;

    public interface  OnStopRefreshListener{
        void stopSuccessRefresh();
        void stopFailRefresh();
    }

    public void setOnStopRefreshListener(OnStopRefreshListener onStopRefreshListener) {
        this.onStopRefreshListener = onStopRefreshListener;
    }
}