package com.gather_excellent_help.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gather_excellent_help.R;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.presenter.homepresenter.ActivityListPresenter;
import com.gather_excellent_help.presenter.homepresenter.ActivityPresenter;
import com.gather_excellent_help.presenter.homepresenter.BannerPresenter;
import com.gather_excellent_help.presenter.homepresenter.GroupPresenter;
import com.gather_excellent_help.presenter.homepresenter.QiangPresenter;
import com.gather_excellent_help.presenter.homepresenter.TypePresenter;
import com.gather_excellent_help.presenter.homepresenter.VipPresenter;
import com.gather_excellent_help.ui.activity.ScannerWebActivity;
import com.gather_excellent_help.ui.activity.WareListActivity;
import com.gather_excellent_help.ui.base.BaseFragment;
import com.gather_excellent_help.ui.base.LazyLoadFragment;
import com.gather_excellent_help.ui.widget.CarouselImageView;
import com.gather_excellent_help.ui.widget.MyGridView;
import com.gather_excellent_help.ui.widget.MyNestedScrollView;
import com.gather_excellent_help.ui.widget.RushDownTimer;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.Tools;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import push.jerry.cn.scan.CaptureActivity;

/**
 * Created by Dapeng Fang on 2017/8/21.
 */

public class HomeUpdateFragment extends LazyLoadFragment {

    @Bind(R.id.civ_home_ganner)
    CarouselImageView civHomeGanner;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.gv_home_type)
    MyGridView gvHomeType;
    @Bind(R.id.ll_home_vip_zera)
    LinearLayout llHomeVipZera;
    @Bind(R.id.ll_home_qiang_zera)
    LinearLayout llHomeQiangZera;
    @Bind(R.id.ll_home_group_zera)
    LinearLayout llHomeGroupZera;
    @Bind(R.id.rcv_home_activity)
    RecyclerView rcvHomeActivity;
    @Bind(R.id.rcv_home_activity_list)
    RecyclerView rcvHomeActivityList;
    @Bind(R.id.mynested_scrollview)
    MyNestedScrollView mynested_scrollview;
    @Bind(R.id.iv_home_back_top)
    ImageView iv_home_back_top;
    @Bind(R.id.ll_home_sousuo)
    LinearLayout ll_home_sousuo;
    @Bind(R.id.ll_home_loadmore)
    LinearLayout ll_home_loadmore;
    @Bind(R.id.tv_item_home_title)
    TextView tv_item_home_title;
    @Bind(R.id.tv_item_home_more)
    TextView tv_item_home_more;
    @Bind(R.id.ll_home_update_scanner)
    LinearLayout ll_home_update_scanner;

    private boolean mIsRequestDataRefresh = false;
    public static final int TIME_DOWN = 1; //倒计时显示的标识
    public static final int STOP_REFRESH = 2; //加载数据的标识
    public static final int TOTOP = 3; //加载数据的标识
    public static final int CHECK_NULL = 4; //加载数据的标识
    private boolean bannerRefresh = false;
    private boolean typeRefresh = false;
    private long time;

    private RushDownTimer rushDownTimer;
    private Handler handler;

    private QiangPresenter qiangPresenter;
    private VipPresenter vipPresenter;
    private TypePresenter typePresenter;
    private BannerPresenter bannerPresenter;
    private GroupPresenter groupPresenter;
    private ActivityPresenter activityPresenter;
    private ActivityListPresenter activityListPresenter;

    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.home_update_fragment, null);
        return inflate;
    }

    @Override
    public void initData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case TIME_DOWN:
                        time -= 1000;
                        rushDownTimer.calcuteDownTimer(time);
                        qiangPresenter.setRushDownTimer(rushDownTimer);
                        if (time <= 0) {
                            handler.removeMessages(TIME_DOWN);
                            return;
                        }
                        if(handler!=null) {
                            handler.sendEmptyMessageDelayed(TIME_DOWN, 1000);
                        }
                        break;
                    case STOP_REFRESH:
                        if (bannerRefresh == false && typeRefresh == false) {
                            if (mIsRequestDataRefresh == true) {
                                stopDataRefresh();
                                setRefresh(mIsRequestDataRefresh);
                            }
                            if(handler!=null) {
                                handler.removeMessages(STOP_REFRESH);
                            }
                            return;
                        }
                        if(handler!=null) {
                            handler.sendEmptyMessageDelayed(STOP_REFRESH, 200);
                        }
                        break;
                    case TOTOP:
                        if (mynested_scrollview != null) {
                            mynested_scrollview.scrollTo(0, 0);
                        }
                        break;
                    case CHECK_NULL:
                        if (civHomeGanner != null && gvHomeType != null
                                && llHomeVipZera != null && llHomeQiangZera != null
                                && llHomeGroupZera != null && rcvHomeActivity != null
                                && rcvHomeActivityList != null && mynested_scrollview != null && ll_home_loadmore != null) {
                            loadHomeUpdateData();
                            if(handler!=null) {
                                handler.removeMessages(CHECK_NULL);
                            }
                        } else {
                            if(handler!=null) {
                                handler.sendEmptyMessageDelayed(CHECK_NULL, 500);
                            }
                        }

                        break;
                }
            }
        };
        if(handler!=null) {
            handler.removeMessages(TIME_DOWN);
            handler.removeMessages(STOP_REFRESH);
            handler.sendEmptyMessageDelayed(CHECK_NULL,600);
        }
    }

    /**
     * 加载首页数据
     */
    private void loadHomeUpdateData() {
        bannerPresenter = new BannerPresenter(getActivity(), civHomeGanner);
        bannerPresenter.initData();

        typePresenter = new TypePresenter(getActivity(), gvHomeType);
        typePresenter.initData();


        vipPresenter = new VipPresenter(getActivity(), llHomeVipZera);
        vipPresenter.initData();

        qiangPresenter = new QiangPresenter(getActivity(), llHomeQiangZera);
        qiangPresenter.initData();

        qiangPresenter.setOnLoadSuccessListener(new QiangPresenter.OnLoadSuccessListener() {
            @Override
            public void onSuccessResponse(long t) {
                if (llHomeQiangZera != null) {
                    llHomeQiangZera.setVisibility(View.VISIBLE);
                }
                time = t;
                rushDownTimer = new RushDownTimer(getContext());
                if(handler!=null) {
                    handler.removeMessages(TIME_DOWN);
                    handler.sendEmptyMessage(TIME_DOWN);
                }
            }

            @Override
            public void onResponseNoData() {
                if (llHomeQiangZera != null) {
                    llHomeQiangZera.setVisibility(View.GONE);
                }
            }
        });

        groupPresenter = new GroupPresenter(getActivity(), llHomeGroupZera);
        groupPresenter.initData();
        activityPresenter = new ActivityPresenter(getContext(), rcvHomeActivity);
        activityPresenter.initData();
        activityListPresenter = new ActivityListPresenter(getContext(), rcvHomeActivityList, mynested_scrollview, ll_home_loadmore);
        activityListPresenter.initData();
        refreshCallBack(bannerPresenter, typePresenter);
        iv_home_back_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mynested_scrollview.scrollTo(0, 0);
            }
        });
        ll_home_sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() == null) {
                    return;
                }
                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra("content", "isHomeSou");
                intent.putExtra("sousuo", "");
                startActivity(intent);
            }
        });
        ll_home_update_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaptureActivity.open(getContext(), new CaptureActivity.OnScanResultListener() {
                    @Override
                    public void onResult(String result) {
                        LogUtil.e(result);
                        if(result.startsWith("http")) {
                            if(result.contains("rtnurl=")) {
                                String[] sanners = result.split("=");
                                String url = sanners[sanners.length - 1];
                                Intent intent = new Intent(getContext(), ScannerWebActivity.class);
                                intent.putExtra("scaner_url", url);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(getContext(), ScannerWebActivity.class);
                                intent.putExtra("scaner_url", result);
                                startActivity(intent);
                            }
                        }else{
                            Toast.makeText(getContext(), result , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        if(handler!=null) {
            handler.sendEmptyMessageDelayed(TOTOP, 3000);
        }
    }

    /**
     * @param bannerPresenter
     * @param typePresenter
     */
    private void refreshCallBack(BannerPresenter bannerPresenter, TypePresenter typePresenter) {
        bannerPresenter.setOnStopRefreshListener(new BannerPresenter.OnStopRefreshListener() {
            @Override
            public void stopSuccessRefresh() {
                bannerRefresh = false;
                if(handler!=null) {
                    handler.sendEmptyMessage(STOP_REFRESH);
                }
            }

            @Override
            public void stopFailRefresh() {
                if (mIsRequestDataRefresh == true) {
                    stopDataRefresh();
                    setRefresh(mIsRequestDataRefresh);
                }
            }
        });
        typePresenter.setOnStopRefreshListener(new TypePresenter.OnStopRefreshListener() {
            @Override
            public void stopSuccessRefresh() {
                typeRefresh = false;
            }

            @Override
            public void stopFailRefresh() {
                if (mIsRequestDataRefresh == true) {
                    stopDataRefresh();
                    setRefresh(mIsRequestDataRefresh);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        setupSwipeRefresh(swipeRefresh);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        if(handler!=null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    private void setupSwipeRefresh(View view) {
        if (swipeRefresh != null) {
            swipeRefresh.setColorSchemeResources(R.color.colorFirst,
                    R.color.colorSecond, R.color.colorThird);
            swipeRefresh.setProgressViewOffset(true, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestDataRefresh();
                    allRefresh();
                    setRefresh(mIsRequestDataRefresh);
                    initData();
                }
            });
        }
    }

    private void allRefresh() {
        bannerRefresh = true;
        typeRefresh = true;
    }

    public void requestDataRefresh() {
        mIsRequestDataRefresh = true;
    }

    public void stopDataRefresh() {
        mIsRequestDataRefresh = false;
    }


    /**
     * 设置刷新的方法
     *
     * @param requestDataRefresh 是否需要刷新
     */
    public void setRefresh(boolean requestDataRefresh) {
        if (!requestDataRefresh) {
            mIsRequestDataRefresh = false;
            if(swipeRefresh!=null) {
                swipeRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefresh != null) {
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        } else {
            if(swipeRefresh!=null) {
                swipeRefresh.setRefreshing(true);
            }
        }
    }


    public void onEvent(AnyEvent event) {
        if (event.getType() == EventType.EVENT_LOGIN) {
            String msg = "onEventMainThread收到了消息：" + event.getMessage();
            LogUtil.e(msg);
            int groupId = Tools.getGroupId(getContext());
            initData();
        }
    }

    @Override
    protected void stopLoad() {
        if(handler!=null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
