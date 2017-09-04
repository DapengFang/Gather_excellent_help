package com.gather_excellent_help.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.NewsDetailBean;
import com.gather_excellent_help.bean.NewsListBean;
import com.gather_excellent_help.bean.NewsTitleBean;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.ui.adapter.NewsFirstAdapter;
import com.gather_excellent_help.ui.adapter.NewsHorizaionalTitleAdapter;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2017/8/10.
 */

public class NewsFirstPresenter {

    private Context context;
    private View rootView;
    private SwipeRefreshLayout swip_news_first;
    private RecyclerView rcv_news_first;
    private String news_url = Url.BASE_URL + "NewsList.aspx";
    private String title_url = Url.BASE_URL + "NewsCategoryList.aspx";
    private String sou_url = Url.BASE_URL + "NewsSearch.aspx";
    private String detail_url = Url.BASE_URL + "NewsInfo.aspx";
    private Map<String,String> map;
    private NetUtil netUtil;
    private LinearLayoutManager layoutManager;
    private int lastVisibleItem;
    private boolean isLoadMore = false; // 是否加载过更多
    private int page =1;//加载第一页
    private String whick;
    private RecyclerView rcvNewsHorizational;
    private String pageSize = "10";
    private String pageIndex = "1";
    private String channel_category = "";
    private String news_id = "";
    private List<NewsListBean.DataBean> newsData;
    private NewsFirstAdapter newsFirstAdapter;
    private List<NewsListBean.DataBean> currData;
    private int curr_click;

    public NewsFirstPresenter(Context context) {
        this.context = context;
        rootView = loadRootView();
        layoutManager = new LinearLayoutManager(context);
        netUtil = new NetUtil();
        netUtil.setOnServerResponseListener(new MyOnServerResponSetListener());
        setupRefresh();

    }

    private View loadRootView() {
        View inflate = View.inflate(context, R.layout.news_first, null);
        swip_news_first = (SwipeRefreshLayout) inflate.findViewById(R.id.swip_news_first);
        rcv_news_first = (RecyclerView) inflate.findViewById(R.id.rcv_news_first);
        return inflate;
    }

    /**
     * 联网获取新闻数据
     */
    public void loadData() {
        whick = "news";
        rcv_news_first.setLayoutManager(layoutManager);
        map = new HashMap<>();
        map.put("pageSize",pageSize);
        map.put("pageIndex",pageIndex);
        map.put("channel_category",channel_category);
        netUtil.okHttp2Server2(news_url,map);
    }
    /**
     * 搜索新闻数据
     */
    public void searchData(String keyWords) {
        whick = "news";
        rcv_news_first.setLayoutManager(layoutManager);
        map = new HashMap<>();
        map.put("pageSize",pageSize);
        map.put("pageIndex",pageIndex);
        map.put("keyWords",keyWords);
        netUtil.okHttp2Server2(sou_url,map);
    }

    private void setupRefresh() {
        if(swip_news_first!=null) {
            swip_news_first.setColorSchemeResources(R.color.colorFirst,
                    R.color.colorSecond,R.color.colorThird);
            swip_news_first.setProgressViewOffset(true, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,context.getResources().getDisplayMetrics()));
        }
    }


    public View getRootView() {
        return rootView;
    }



    /**
     * @param rcvNewsHorizational
     * 新闻顶部标题导航栏
     */
    public void loadTitleData(RecyclerView rcvNewsHorizational) {
        this.rcvNewsHorizational = rcvNewsHorizational;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rcvNewsHorizational.setLayoutManager(layoutManager);
        whick = "title";
        netUtil.okHttp2Server2(title_url,null);
    }

    public class MyOnServerResponSetListener implements NetUtil.OnServerResponseListener{

        @Override
        public void getSuccessResponse(String response) {
            CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
            int statusCode = codeStatueBean.getStatusCode();
            switch (statusCode) {
                case 1 :
                     if(whick.equals("title")) {
                         LogUtil.e("新闻标题="+response);
                         NewsTitleBean newsTitleBean = new Gson().fromJson(response, NewsTitleBean.class);
                         final List<NewsTitleBean.DataBean> titleData = newsTitleBean.getData();
                         if(titleData.size()>0) {
                             titleData.get(0).setCheck(true);
                         }
                         NewsHorizaionalTitleAdapter newsHorizaionalTitleAdapter = new NewsHorizaionalTitleAdapter(context, titleData);
                         if(rcvNewsHorizational!=null) {
                             rcvNewsHorizational.setAdapter(newsHorizaionalTitleAdapter);
                         }
                         if(titleData.size()>0) {
                             int id = titleData.get(0).getId();
                             channel_category = String.valueOf(id);
                             loadData();
                         }
                         newsHorizaionalTitleAdapter.setOnTitleClickListener(new NewsHorizaionalTitleAdapter.OnTitleClickListener() {
                             @Override
                             public void onTitleClick(View v, int position) {
                                 NewsTitleBean.DataBean dataBean = titleData.get(position);
                                 int id = dataBean.getId();
                                 channel_category = String.valueOf(id);
                                 pageIndex = "1";
                                 isLoadMore =false;
                                 loadData();
                             }
                         });
                     }else if(whick.equals("news")) {
                         LogUtil.e("新闻 == "+response);
                         NewsListBean newsListBean = new Gson().fromJson(response, NewsListBean.class);
                         currData = newsListBean.getData();
                         int size = currData.size();
                         if(isLoadMore) {
                             page++;
                             if (size<10) {
                                 newsFirstAdapter.updateLoadStatus(newsFirstAdapter.LOAD_NONE);
                                 return;
                             }
                             else {
                                newsData.addAll(currData);
                             }
                             newsFirstAdapter.notifyDataSetChanged();
                         }else{
                             newsData = currData;
                             newsFirstAdapter = new NewsFirstAdapter(context, newsData);
                             rcv_news_first.setAdapter(newsFirstAdapter);
                             newsFirstAdapter.notifyDataSetChanged();
                             page = 2;
                         }
                         if(size == 0) {
                             Toast.makeText(context, "没有搜索到查询内容！", Toast.LENGTH_SHORT).show();
                             return;
                         }
                         newsFirstAdapter.setOnNewsItemClickListner(new NewsFirstAdapter.OnNewsItemClickListner() {
                             @Override
                             public void onItemClick(View v, int position) {
                                 curr_click = position;
                                 NewsListBean.DataBean dataBean = newsData.get(position);
                                 int id = dataBean.getId();
                                 news_id = String.valueOf(id);
                                 loadNewsDetail();
                             }
                         });
                         scrollRecycleView();
                     }else if(whick.equals("detail")) {
                         LogUtil.e("新闻详情"+response);
                         NewsDetailBean newsDetailBean = new Gson().fromJson(response, NewsDetailBean.class);
                         String link_url = "";
                         List<NewsDetailBean.DataBean> data = newsDetailBean.getData();
                         if(data!=null) {
                             if(data.size()>0) {
                                 link_url = data.get(0).getLink_url();
                             }
                         }
                         String imgurl = "";
                         String newsTitle = "";
                         if(newsData!=null) {
                             String img_url = newsData.get(curr_click).getImg_url();
                             if(img_url!=null) {
                                 imgurl = Url.IMG_URL + img_url;
                             }
                             String title = newsData.get(curr_click).getTitle();
                             if(title!=null) {
                                 newsTitle = title;
                             }
                         }
                         if(link_url!=null && !TextUtils.isEmpty(link_url)) {
                             Intent intent = new Intent(context, WebActivity.class);
                             intent.putExtra("web_url",Url.IMG_URL+link_url);
                             intent.putExtra("type","detail");
                             intent.putExtra("news_img",imgurl);
                             intent.putExtra("news_title",newsTitle);
                             LogUtil.e("--------"+Url.IMG_URL+link_url);
                             context.startActivity(intent);
                         }
                     }
                    swip_news_first.setRefreshing(false);
                    break;
                case 0:
                    swip_news_first.setRefreshing(false);
                    if(context!=null) {
                        Toast.makeText(context,codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString()+"---"+e.getMessage());
            swip_news_first.setRefreshing(false);
        }
    }

    /**
     * 获取新闻详情页数据
     */
    private void loadNewsDetail() {
        whick = "detail";
        rcv_news_first.setLayoutManager(layoutManager);
        map = new HashMap<>();
        map.put("news_id",news_id);
        netUtil.okHttp2Server2(detail_url,map);
    }

    public void scrollRecycleView() {
        if(rcv_news_first!=null && newsFirstAdapter!=null) {
            rcv_news_first.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        lastVisibleItem = layoutManager
                                .findLastVisibleItemPosition();
                        if (layoutManager.getItemCount() == 1) {
                            newsFirstAdapter.updateLoadStatus(newsFirstAdapter.LOAD_NONE);
                            return;
                        }
                        if (lastVisibleItem + 1 == layoutManager
                                .getItemCount()) {
                            newsFirstAdapter.updateLoadStatus(newsFirstAdapter.LOAD_PULL_TO);
                            isLoadMore = true;
                            newsFirstAdapter.updateLoadStatus(newsFirstAdapter.LOAD_MORE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                 pageIndex = String.valueOf(page);
                                    loadData();
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
        }
    }

    public void refreshSwip(){
        swip_news_first.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoadMore = false;
                page = 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex = String.valueOf(page);
                        loadData();
                    }
                },1000);
            }
        });
    }
}
