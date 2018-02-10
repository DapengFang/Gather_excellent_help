package com.gather_excellent_help.ui.activity.credits;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.ExractDetailBean;
import com.gather_excellent_help.ui.adapter.ExtractDetailAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class ExtractDetailActivity extends BaseActivity {

    @Bind(R.id.iv_zhuangtai_exit)
    ImageView ivZhuangtaiExit;
    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.rl_share)
    RelativeLayout rlShare;
    @Bind(R.id.rcv_account_detail)
    RecyclerView rcvAccountDetail;
    private RelativeLayout rl_order_no_zhanwei;
    private NetUtil netUtil;
    private Map<String, String> map;
    private String id;
    private String pageSize = "10";
    private String pageIndex = "1";
    private String url = Url.BASE_URL + "WithdrawLog.aspx";
    private boolean isLoadmore = false;
    private int page = 1;
    private FullyLinearLayoutManager layoutManager;
    private int lastVisibleItem;
    private List<ExractDetailBean.DataBean> extractData;
    private List<ExractDetailBean.DataBean> currData;
    private ExtractDetailAdapter extractDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract_detail);
        rl_order_no_zhanwei = (RelativeLayout) findViewById(R.id.rl_order_no_zhanwei);
        ButterKnife.bind(this);
        initData();

    }

    private void initData() {
        tvTopTitleName.setText("提取明细");
        rlShare.setVisibility(View.GONE);
        rlExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        layoutManager = new FullyLinearLayoutManager(this);
        rcvAccountDetail.setLayoutManager(layoutManager);
        netUtil = new NetUtil();
        id = Tools.getUserLogin(this);
        map = new HashMap<>();
        map.put("id", id);
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
        netUtil.okHttp2Server2(ExtractDetailActivity.this,url, map);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "-" + e.getMessage());
                EncryptNetUtil.startNeterrorPage(ExtractDetailActivity.this);
            }
        });
    }

    private void parseData(String response) {
        ExractDetailBean exractDetailBean = new Gson().fromJson(response, ExractDetailBean.class);
        int statusCode = exractDetailBean.getStatusCode();
        switch (statusCode) {
            case 1:
                if (isLoadmore) {
                    page++;
                    currData = exractDetailBean.getData();
                    extractData.addAll(currData);
                    extractDetailAdapter.notifyDataSetChanged();
                } else {
                    page = 2;
                    currData = exractDetailBean.getData();
                    if (currData != null) {
                        if (currData.size() > 0) {
                            rl_order_no_zhanwei.setVisibility(View.GONE);
                        } else {
                            rl_order_no_zhanwei.setVisibility(View.VISIBLE);
                        }
                    }
                    extractData = currData;
                    extractDetailAdapter = new ExtractDetailAdapter(ExtractDetailActivity.this, extractData);
                    rcvAccountDetail.setAdapter(extractDetailAdapter);
                }
                rcvAccountDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            lastVisibleItem = layoutManager
                                    .findLastVisibleItemPosition();

                            if (lastVisibleItem + 1 == layoutManager
                                    .getItemCount()) {
                                isLoadmore = true;
                                if (currData.size() < 10) {
                                    Toast.makeText(ExtractDetailActivity.this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pageIndex = String.valueOf(page);
                                        //联网请求数据
                                        map = new HashMap<>();
                                        map.put("id", id);
                                        map.put("pageSize", pageSize);
                                        map.put("pageIndex", pageIndex);
                                        netUtil.okHttp2Server2(ExtractDetailActivity.this,url, map);
                                    }
                                }, 1000);
                            }
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    }
                });
                break;
            case 0:
                Toast.makeText(ExtractDetailActivity.this, exractDetailBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
