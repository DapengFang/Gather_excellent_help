package com.gather_excellent_help.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.gather_excellent_help.bean.ListBean;
import com.gather_excellent_help.bean.SearchWareBean;
import com.gather_excellent_help.ui.adapter.WareListAdapter;
import com.gather_excellent_help.ui.adapter.WareSelectorAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.TypeSelectorPopupwindow;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;



public class WareListActivity extends BaseActivity {

    @Bind(R.id.gv_wart_list)
    GridView gvWartList;
    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.et_ware_list_content)
    EditText etWareListContent;
    @Bind(R.id.tv_ware_list_old)
    TextView tvWareListOld;
    @Bind(R.id.rl_sousuo)
    RelativeLayout rlSousuo;
    @Bind(R.id.ll_ware_list_pingpai)
    LinearLayout llWareListPingpai;
    @Bind(R.id.ll_ware_list_price)
    LinearLayout llWareListPrice;
    @Bind(R.id.ll_ware_list_leibie)
    LinearLayout llWareListLeibie;
    @Bind(R.id.ll_ware_list_rongliang)
    LinearLayout llWareListRongliang;
    @Bind(R.id.iv_up_price)
    ImageView ivUpPrice;
    @Bind(R.id.iv_down_price)
    ImageView ivDownPrice;
    @Bind(R.id.ll_ware_list_loadmore)
    LinearLayout ll_ware_list_loadmore;

    private NetUtil netUtil;//搜索条件列表的网络接口
    private NetUtil netUtil2;//搜索商品列表的网络接口
    private String pingpai_url = Url.BASE_URL + "goodsBrand.aspx";
    private TypeSelectorPopupwindow typeSelectorPopupwindow;
    private int crr_click = -1; //记录几点那个条目（类别，品牌，容量）
    private int sort_price = SortPrice.ASC.ordinal();
    private String sousuoStr;
    private String sousuo_url = Url.BASE_URL + "SearchGoods.aspx";
    private Map<String,String> map;
    private String keyword = "";//关键字
    private String PriceOder = "2";//1----降序，2-----升序
    private String Type = "";//商品类型
    private String brandId = "";//品牌
    private String capacity = "";//容量
    private String pageIndex = "1";//页数
    private String pageSize = "10";//每一页的数量
    private WareListAdapter wareListAdapter;
    private List<ListBean.DataBean> conditionData; //查询条件数据
    private List<SearchWareBean.DataBean> wareData;
    private int page = 1;//显示第几页
    private static Handler handler = new Handler();

    private String type_id;
    private String ware_url = Url.BASE_URL + "CategoryGoodList.aspx";
    private int isLoadmore = -1;
    private List<SearchWareBean.DataBean> newData;
    private String content_et = "";
    private String activity_id = "";
    private String activity_url = Url.BASE_URL + "ActivityMoreList.aspx";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_list);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        netUtil = new NetUtil();
        netUtil2 = new NetUtil();
        map = new HashMap<>();
        Intent intent = getIntent();
        sousuoStr = intent.getStringExtra("content");
        type_id = intent.getStringExtra("type_id");
        activity_id = intent.getStringExtra("activity_id");
        if(type_id!=null && !TextUtils.isEmpty(type_id)) {
            tvWareListOld.setVisibility(View.GONE);
            etWareListContent.setHint("请输入商品名称");
            Map<String,String> map2 = new HashMap<>();
            map.put("id",type_id);
            netUtil2.okHttp2Server2(ware_url,map2);
        }else{
            tvWareListOld.setVisibility(View.GONE);
            etWareListContent.setHint("请输入商品名称");
            if(sousuoStr.equals("isQiang")) {
                map = new HashMap<>();
                map.put("pageSize","10");
                map.put("pageIndex","1");
                String qiang_url = Url.BASE_URL + "GroupBuyList.aspx";
                netUtil2.okHttp2Server2(qiang_url,map);
            }else if(sousuoStr.equals("isVip")) {
                map = new HashMap<>();
                map.put("pageSize","10");
                map.put("pageIndex","1");
                String vip_url = Url.BASE_URL + "ChannelPriceList.aspx";
                netUtil2.okHttp2Server2(vip_url,map);
            }else if(sousuoStr.equals("activity")) {
                map = new HashMap<>();
                map.put("pageSize","10");
                map.put("pageIndex","1");
                map.put("activity_id",activity_id);
                netUtil2.okHttp2Server2(activity_url,map);
            }else if(sousuoStr!=null && !TextUtils.isEmpty(sousuoStr)) {
                tvWareListOld.setVisibility(View.VISIBLE);
                tvWareListOld.setText(sousuoStr);
                etWareListContent.setHint("");
                keyword = sousuoStr;
                searchWareList(keyword);
            }else{
                etWareListContent.setHint("请输入商品名称");
                tvWareListOld.setVisibility(View.GONE);
            }
        }

        rlExit.setOnClickListener(new MyOnClickListener());
        llWareListPrice.setOnClickListener(new MyOnClickListener());
        llWareListLeibie.setOnClickListener(new MyOnClickListener());
        llWareListPingpai.setOnClickListener(new MyOnClickListener());
        llWareListRongliang.setOnClickListener(new MyOnClickListener());
        tvWareListOld.setOnClickListener(new MyOnClickListener());
        rlSousuo.setOnClickListener(new MyOnClickListener());
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
              LogUtil.e(call.toString()+"--"+e.getMessage());
            }
        });
        netUtil2.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                parseData2(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString()+"--"+e.getMessage());
            }
        });
    }

    /**
     * @param response
     * 解析搜索后的商品数据
     */
    private void parseData2(String response) {
        SearchWareBean searchWareBean = new Gson().fromJson(response, SearchWareBean.class);
        int statusCode = searchWareBean.getStatusCode();
        switch (statusCode) {
            case 1 :
                if(gvWartList==null) {
                    return;
                }
                if(isLoadmore!=-1) {
                    newData = searchWareBean.getData();
                    wareData.addAll(newData);
                    wareListAdapter.notifyDataSetChanged();
                }else{
                    wareData = searchWareBean.getData();
                    newData = wareData;
                    wareListAdapter = new WareListAdapter(WareListActivity.this, wareData);
                    gvWartList.setAdapter(wareListAdapter);
                }
                gvWartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String couponsUrl = wareData.get(i).getCouponsUrl();
                        if(couponsUrl!=null && !TextUtils.isEmpty(couponsUrl)) {
                            Intent intent = new Intent(WareListActivity.this, WebActivity.class);
                            intent.putExtra("web_url", couponsUrl);
                            startActivity(intent);
                        }else{
                            String link_url = wareData.get(i).getLink_url();
                            String goods_id = wareData.get(i).getProductId();
                            String goods_img = wareData.get(i).getImg_url();
                            String goods_title = wareData.get(i).getTitle();
                            Intent intent = new Intent(WareListActivity.this, WebRecordActivity.class);
                            intent.putExtra("url",link_url);
                            intent.putExtra("goods_id",goods_id);
                            intent.putExtra("goods_img",goods_img);
                            intent.putExtra("goods_title",goods_title);
                            startActivity(intent);
                        }
                    }
                });
                gvWartList.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                          if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                              if (absListView.getLastVisiblePosition() == (absListView.getCount() - 1)) {
                                  isLoadmore = 0;
                                  if(type_id!=null && !TextUtils.isEmpty(type_id)) {
                                     showLoadNoMore();
                                  }else{
                                      if(sousuoStr!=null && !TextUtils.isEmpty(sousuoStr)) {
                                              page++;
                                              LogUtil.e("page == "+page);
                                              tvWareListOld.setVisibility(View.VISIBLE);
                                              tvWareListOld.setText(sousuoStr);
                                              etWareListContent.setHint("");
                                              keyword = sousuoStr;
                                              pageIndex = String.valueOf(page);
                                              LogUtil.e("newData.size() ==" + newData.size());
                                              if(newData.size() <Integer.valueOf(pageSize)) {
                                                  showLoadNoMore();
                                              }else{
                                                  showLoadMore();
                                                  handler.postDelayed(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                          searchWareList(keyword);
                                                      }
                                                  },500);
                                              }
                                      }else if(!TextUtils.isEmpty(content_et)) {
                                          page++;
                                          LogUtil.e("page == "+page);
                                          tvWareListOld.setVisibility(View.VISIBLE);
                                          tvWareListOld.setText(sousuoStr);
                                          etWareListContent.setHint("");
                                          keyword = sousuoStr;
                                          pageIndex = String.valueOf(page);
                                          LogUtil.e("newData.size() ==" + newData.size());
                                          if(newData.size() <Integer.valueOf(pageSize)) {
                                              showLoadNoMore();
                                          }else{
                                              showLoadMore();
                                              handler.postDelayed(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      searchWareList(keyword);
                                                  }
                                              },500);
                                          }
                                      }{
                                          etWareListContent.setHint("请输入商品名称");
                                          tvWareListOld.setVisibility(View.GONE);
                                      }
                                  }
                              }else{
                                  ll_ware_list_loadmore.setVisibility(View.GONE);
                              }
                          }
                    }

                    @Override
                    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                    }
                });
                break;
            case 0:
                Toast.makeText(WareListActivity.this, searchWareBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showLoadMore() {
        ll_ware_list_loadmore.setVisibility(View.VISIBLE);
        TextView tvTitle = (TextView) ll_ware_list_loadmore.getChildAt(0);
        tvTitle.setText("加载更多...");
    }

    private void showLoadNoMore() {
        TextView tvTitle = (TextView) ll_ware_list_loadmore.getChildAt(0);
        tvTitle.setText("没有更多的数据了...");
        ll_ware_list_loadmore.setVisibility(View.VISIBLE);
    }

    /**
     * @param response 解析数据
     */
    private void parseData(String response) {
        ListBean listBean = new Gson().fromJson(response, ListBean.class);
        int statusCode = listBean.getStatusCode();
        switch (statusCode) {
            case 1:
                LogUtil.e(response);
                conditionData = listBean.getData();
                if(conditionData==null) {
                    return;
                }
                setPopMenuNull();
                showPopMenu(conditionData);

                break;
            case 0:
                Toast.makeText(WareListActivity.this, listBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showPopMenu(List<ListBean.DataBean> data) {
        if (typeSelectorPopupwindow == null) {
            typeSelectorPopupwindow = new TypeSelectorPopupwindow(WareListActivity.this, data);
            showPopupwindow();
        } else if (typeSelectorPopupwindow != null
                && typeSelectorPopupwindow.isShowing()) {
            typeSelectorPopupwindow.dismiss();
        } else {
            typeSelectorPopupwindow.showAsDropDown(llWareListPingpai, 5, 5);
        }

    }

    /**
     * 显示popupwindow
     */
    private void showPopupwindow() {
        switch (crr_click) {
            case 0:
                typeSelectorPopupwindow.showAsDropDown(llWareListLeibie, 5, 5);
                break;
            case 1:
                typeSelectorPopupwindow.showAsDropDown(llWareListPingpai, 5, 5);
                break;
            case 2:
                typeSelectorPopupwindow.showAsDropDown(llWareListRongliang, 5, 5);
                break;
        }
        typeSelectorPopupwindow.setOnItemClickListener(new WareSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                isLoadmore = -1;
                ListBean.DataBean dataBean = conditionData.get(position);
                int id = dataBean.getId();
                brandId = String.valueOf(id);
                if(keyword!=null && !TextUtils.isEmpty(keyword)) {
                    showLoadNoMore();
                    searchWareList(keyword);
                }else{
                    String et_str = etWareListContent.getText().toString().trim();
                    if(et_str!=null && !TextUtils.isEmpty(et_str)) {
                        keyword = et_str;
                        ll_ware_list_loadmore.setVisibility(View.GONE);
                        searchWareList(keyword);
                    }else{
                        Toast.makeText(WareListActivity.this, "请输入商品名称！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 将popupwindow置空，方便后面调用
     */
    private void setPopMenuNull() {
        if (typeSelectorPopupwindow != null) {
            typeSelectorPopupwindow = null;
        }
    }

    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_ware_list_price:
                    if (sort_price == SortPrice.ASC.ordinal()) {
                        sort_price = SortPrice.DESC.ordinal();
                    } else {
                        sort_price = SortPrice.ASC.ordinal();
                    }
                    setSortPriceIcon(sort_price);
                    setSortPriceWareList(sort_price);
                    break;
                case R.id.ll_ware_list_leibie:
                    crr_click = 0;
                    netUtil.okHttp2Server2(pingpai_url, null);
                    break;
                case R.id.ll_ware_list_pingpai:
                    crr_click = 1;
                    netUtil.okHttp2Server2(pingpai_url, null);
                    break;
                case R.id.ll_ware_list_rongliang:
                    crr_click = 2;
                    netUtil.okHttp2Server2(pingpai_url, null);
                    break;
                case R.id.tv_ware_list_old:
                    tvWareListOld.setText("");
                    sousuoStr = "";
                    tvWareListOld.setVisibility(View.GONE);
                    etWareListContent.setHint("请输入商品名称");
                    break;
                case R.id.rl_sousuo:
                    isLoadmore = -1;
                    ll_ware_list_loadmore.setVisibility(View.GONE);
                    content_et = etWareListContent.getText().toString().trim();
                    if(sousuoStr!=null && !TextUtils.isEmpty(sousuoStr)) {
                        Toast.makeText(WareListActivity.this, sousuoStr, Toast.LENGTH_SHORT).show();
                        keyword = sousuoStr;
                        pageIndex = "1";
                        searchWareList(keyword);
                    }else if(content_et!=null && !TextUtils.isEmpty(content_et)) {
                        keyword =content_et;
                        pageIndex = "1";
                        searchWareList(keyword);
                    }else{
                        Toast.makeText(WareListActivity.this, "请输入搜索内容！", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.rl_exit:
                    finish();
                    break;
            }
        }
    }

    /**
     * @param sort_price
     * 设置查询升降序的列表
     */
    private void setSortPriceWareList(int sort_price) {
        if(sort_price == SortPrice.ASC.ordinal()) {
            PriceOder = String.valueOf(2);
        }else{
            PriceOder = String.valueOf(1);
        }
        if(keyword!=null && !TextUtils.isEmpty(keyword)) {
            searchWareList(keyword);
        }else{
            Toast.makeText(WareListActivity.this, "请输入商品名称！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param keyword
     * 点击搜索按钮搜索商品
     *     private String keyword = "";//关键字
    private String PriceOder = "2";//1----降序，2-----升序
    private String Type = "";//商品类型
    private String brandId = "";//品牌
    private String capacity = "";//容量
    private String pageIndex = "0";//页数
    private String pageSize = "6";//每一页的数量
     */
    private void searchWareList(String keyword) {
        map.put("keyword",keyword);
        map.put("PriceOder",PriceOder);
        map.put("Type",Type);
        map.put("brandId",brandId);
        map.put("capacity",capacity);
        map.put("pageIndex",pageIndex);
        map.put("pageSize",pageSize);
        netUtil2.okHttp2Server2(sousuo_url,map);
    }

    /**
     * @param sort_price 设置升降序的图标
     */
    private void setSortPriceIcon(int sort_price) {
        if(sort_price == SortPrice.ASC.ordinal()) {
            ivUpPrice.setImageResource(R.drawable.up_black_arraw);
            ivDownPrice.setImageResource(R.drawable.down_gray_arraw);
        }else{
            ivUpPrice.setImageResource(R.drawable.up_gray_arraw);
            ivDownPrice.setImageResource(R.drawable.down_black_arraw);
        }

    }

    public enum SortPrice {
        ASC,
        DESC;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
