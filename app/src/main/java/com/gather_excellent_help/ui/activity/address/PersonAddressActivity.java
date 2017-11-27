package com.gather_excellent_help.ui.activity.address;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.AddressDetailBean;
import com.gather_excellent_help.bean.CodeBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.adapter.PersonAddressAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.WanRecycleView;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class PersonAddressActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;
    private WanRecycleView wan_me_address;
    private RecyclerView rcv_me_address;
    private TextView tv_address_top_num;
    private TextView tv_me_add_newaddress;
    private LinearLayout ll_address_clear_show;

    private String get_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=GetUserAddress";
    private NetUtil netUtil;
    private Map<String, String> map;
    private List<AddressDetailBean.DataBean> data;
    private PersonAddressAdapter personAddressAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_address);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
        wan_me_address = (WanRecycleView) findViewById(R.id.wan_me_address);
        tv_address_top_num = (TextView) findViewById(R.id.tv_address_top_num);
        tv_me_add_newaddress = (TextView) findViewById(R.id.tv_me_add_newaddress);
        ll_address_clear_show = (LinearLayout) findViewById(R.id.ll_address_clear_show);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tv_top_title_name.setText("收货地址管理");
        getAddressList();
        if (wan_me_address!=null && wan_me_address.isRefreshing()) {
            wan_me_address.onRefreshComplete();
        }
        OnServerResponseListener onServerResponseListener = new OnServerResponseListener();
        netUtil.setOnServerResponseListener(onServerResponseListener);
        rcv_me_address = wan_me_address.getRefreshableView();
        RecyclerView.LayoutManager layoutManager = new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcv_me_address.setLayoutManager(layoutManager);

        //设置刷新相关
        wan_me_address.setScrollingWhileRefreshingEnabled(true);
        wan_me_address.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        wan_me_address.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }

        });
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        tv_me_add_newaddress.setOnClickListener(myonclickListener);

    }

    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_me_add_newaddress:
                    if (personAddressAdapter != null) {
                        int itemCount = personAddressAdapter.getItemCount();
                        LogUtil.e("itemCount = " + itemCount);
                        if (itemCount < 5) {
                            Intent intent = new Intent(PersonAddressActivity.this, AddNewAddressActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(PersonAddressActivity.this, "收货地址最多不能超过5个。", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent intent = new Intent(PersonAddressActivity.this, AddNewAddressActivity.class);
                        startActivity(intent);
                    }
                    break;
            }
        }
    }

    /**
     * 获取地址列表
     */
    private void getAddressList() {
        netUtil = new NetUtil();
        String userLogin = Tools.getUserLogin(this);
        map = new HashMap<>();
        map.put("user_id", userLogin);
        netUtil.okHttp2Server2(get_url, map);
    }

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
            AddressDetailBean addressDetailBean = new Gson().fromJson(response, AddressDetailBean.class);
            int statusCode = addressDetailBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    data = addressDetailBean.getData();
                    if (data != null && data.size() > 0) {
                        ll_address_clear_show.setVisibility(View.GONE);
                        personAddressAdapter = new PersonAddressAdapter(PersonAddressActivity.this, data, tv_address_top_num);
                        rcv_me_address.setAdapter(personAddressAdapter);
                        tv_address_top_num.setText(data.size() + "");
                    } else {
                        ll_address_clear_show.setVisibility(View.VISIBLE);
                    }
                    break;
                case 0:
                    Toast.makeText(PersonAddressActivity.this, addressDetailBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e("网络连接出现问题~");
        }
    }

    /**
     * 刷新数据
     *
     * @param event
     */
    public void onEvent(AnyEvent event) {
        if (event.getType() == EventType.UPDATA_ADDRESS) {
            String msg = "onEventMainThread收到了消息：" + event.getMessage();
            LogUtil.e(msg);
            initData();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
