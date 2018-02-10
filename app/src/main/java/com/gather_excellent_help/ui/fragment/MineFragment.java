package com.gather_excellent_help.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.MainActivity;
import com.gather_excellent_help.R;
import com.gather_excellent_help.aliapi.DemoTradeCallback;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.HelpRuleBean;
import com.gather_excellent_help.bean.MineBean;
import com.gather_excellent_help.bean.SmsCodeBean;
import com.gather_excellent_help.ui.activity.BindPhoneActivity;
import com.gather_excellent_help.ui.activity.RegisterActivity;
import com.gather_excellent_help.ui.activity.suning.LogisticsInfoActivity;
import com.gather_excellent_help.ui.activity.suning.SuningOrderLogisticsActivity;
import com.gather_excellent_help.ui.activity.suning.saleafter.SaleAfterActivity;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.AlipayManagerActivity;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.activity.OrderActivity;
import com.gather_excellent_help.ui.activity.RuleHelpActivity;
import com.gather_excellent_help.ui.activity.SetActivity;
import com.gather_excellent_help.ui.activity.credits.AccountDetailAvtivity;
import com.gather_excellent_help.ui.activity.credits.BackRebateActivity;
import com.gather_excellent_help.ui.activity.credits.ExtractCreditsActivity;
import com.gather_excellent_help.ui.activity.credits.InviteFriendsActivity;
import com.gather_excellent_help.ui.activity.credits.LowerMemberStaticsActivity;
import com.gather_excellent_help.ui.activity.credits.ShopDetailActivity;
import com.gather_excellent_help.ui.activity.shop.WhichJoinActivity;
import com.gather_excellent_help.ui.activity.suning.SuningOrderActivity;
import com.gather_excellent_help.ui.activity.taosearch.TaoSearchActivity;
import com.gather_excellent_help.ui.activity.test.TestActivity_l02;
import com.gather_excellent_help.ui.activity.test.TestActivity_l04;
import com.gather_excellent_help.ui.base.LazyLoadFragment;
import com.gather_excellent_help.ui.lisetener.MyTextWatcher;
import com.gather_excellent_help.ui.widget.CircularImage;
import com.gather_excellent_help.ui.widget.MyToggleButton;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.Check;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.enctry.Des2;
import com.google.gson.Gson;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.EventBusException;
import okhttp3.Call;

/**
 * Created by wuxin on 2017/7/7.
 */

public class MineFragment extends LazyLoadFragment {

    @Bind(R.id.rl_mine_set)
    RelativeLayout rlMineSet;
    @Bind(R.id.tv_account_money)
    TextView tvAccountMoney;
    @Bind(R.id.tv_account_money2)
    TextView tvAccountMoney2;
    @Bind(R.id.ll_mine_extract_credits)
    LinearLayout llMineExtractCredits;
    @Bind(R.id.ll_mine_find_friends)
    LinearLayout llMineFindFriends;
    @Bind(R.id.ll_mine_account_details)
    LinearLayout llMineAccountDetails;
    @Bind(R.id.ll_mine_huiyuan_statis)
    LinearLayout llMineHuiyuanStatis;
    @Bind(R.id.ll_mine_fanyong_rule)
    LinearLayout llMineFanyongRule;
    @Bind(R.id.ll_mine_shop_details)
    LinearLayout llMineShopDetails;
    @Bind(R.id.ll_mine_taobao_order)
    LinearLayout llMineTaobaoOrder;
    @Bind(R.id.ll_mine_suning_order)
    LinearLayout llMineSuningOrder;
    @Bind(R.id.ll_mine_juyoubang_order)
    LinearLayout llMineJuyoubangOrder;
    @Bind(R.id.ll_mine_zhuan_order)
    LinearLayout llMineZhuanOrder;
    @Bind(R.id.tbn_mine_control)
    MyToggleButton tbnMineControl;
    @Bind(R.id.ll_mine_user_salery)
    LinearLayout llMineUserSalery;
    @Bind(R.id.ll_mine_user_back)
    LinearLayout llMineUserBack;
    @Bind(R.id.tv_account_money_title1)
    TextView tvAccountMoneyTitle1;
    @Bind(R.id.tv_account_money_title2)
    TextView tvAccountMoneyTitle2;
    @Bind(R.id.ll_mine_compont_first)
    LinearLayout llMineCompontFirst;
    @Bind(R.id.ll_mine_compont_second)
    LinearLayout llMineCompontSecond;
    @Bind(R.id.iv_mine_compont_l01)
    ImageView ivMineCompontL01;
    @Bind(R.id.tv_mine_compont_l01)
    TextView tvMineCompontL01;
    @Bind(R.id.iv_mine_compont_l02)
    ImageView ivMineCompontL02;
    @Bind(R.id.tv_mine_compont_l02)
    TextView tvMineCompontL02;
    @Bind(R.id.iv_mine_compont_l03)
    ImageView ivMineCompontL03;
    @Bind(R.id.tv_mine_compont_l03)
    TextView tvMineCompontL03;
    @Bind(R.id.iv_mine_compont_l04)
    ImageView ivMineCompontL04;
    @Bind(R.id.tv_mine_compont_l04)
    TextView tvMineCompontL04;
    @Bind(R.id.iv_mine_compont_l05)
    ImageView ivMineCompontL05;
    @Bind(R.id.tv_mine_compont_l05)
    TextView tvMineCompontL05;
    @Bind(R.id.iv_mine_compont_l06)
    ImageView ivMineCompontL06;
    @Bind(R.id.tv_mine_compont_l06)
    TextView tvMineCompontL06;
    @Bind(R.id.rl_mine_toggle)
    RelativeLayout rlMineToggle;
    @Bind(R.id.ll_lower_member_statics)
    LinearLayout llLowerMemberStatics;
    @Bind(R.id.v_tui_zhuan_line)
    View vTuiZhuanLine;
    @Bind(R.id.v_toggle_line)
    View vToggleLine;

    private LinearLayout ll_mine_shiti_show;
    //private LinearLayout ll_mine_salery_show;

    private LinearLayout ll_mine_u_like;
    private LinearLayout ll_mine_suning_order;
    private String mseg = "";
    private SwipeRefreshLayout swip_fresh;

    private CircularImage civMeHeadIcon;

    private NetUtil netUtils;
    private NetUtil netUtils2;
    private Map<String, String> map;
    private String url = Url.BASE_URL + "Mine.aspx";
    private int group_id = -1;

    private ImageView iv_mine_compont_l01;
    private ImageView iv_mine_compont_l02;
    private ImageView iv_mine_compont_l03;
    private ImageView iv_mine_compont_l04;
    private ImageView iv_mine_compont_l05;
    private ImageView iv_mine_compont_l06;

    private LinearLayout ll_userinfo_head;
    private TextView tv_mine_tabao_account;
    private LinearLayout ll_userinfo_alipay;
    private TextView tv_userinfo_alipay;
    private TextView tv_userinfo_level_icon;
    private TextView tv_userinfo_level_name;

    private RelativeLayout rl_mine_head_login;
    private RelativeLayout rl_mine_head_userinfo;

    private Button btn_mine_head_login;
    private Button btn_mine_head_register;

    private TextView tv_grand_total;
    private ImageView iv_apply_ad_show;

    private LinearLayout ll_mine_content_show;

    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private AlibcTaokeParams alibcTaokeParams = null;//淘客参数，包括pid，unionid，subPid
    private Boolean isTaoke = false;//是否是淘客商品类型
    private String itemId = "522166121586";//默认商品id
    private String shopId = "60552065";//默认店铺id
    private Map<String, String> exParams;//yhhpass参数
    private double amount;
    private double frostAmount;
    private String advertising;
    private boolean login;
    private int groupId;
    private int shopType;
    private int applyState;
    private int payState;

    private String help_url = Url.BASE_URL + "UserHelp.aspx";//帮助
    private String rule_url = Url.BASE_URL + "RebateRules.aspx";//返佣
    private String bind_url = Url.BASE_URL + "bindTaobao.aspx";//绑定淘宝
    private String pay_url = Url.BASE_URL + "BindAlipay.aspx";//绑定支付宝
    private String sms_url = Url.BASE_URL + "GetRandom.aspx";//获取短信验证码
    private double user_earn;
    private String which;

    public static final int CHECK_NULL = 4; //加载数据的标识

    private static final int REQUEST_CODE_SCAN_GALLERY = 0x123;//从相册获取二维码的标识

    private Handler handler;
    private int apply_type = -1;
    private int pay_type;
    private boolean mIsRequestDataRefresh;
    private boolean bindTao;
    private boolean bindAlipay;
    private AlertDialog alertDialog;
    private AlibcLogin alibcLogin;
    private String openId;
    private String avatarUrl;
    private String nick;
    private String userLogin;
    private String account;
    private String name;
    private CountDownTimer countDownTimer;
    private String sms_code_s;
    private String alipay_account;
    private String alipay_name;
    private String taobao_nick;
    private double grand_total;

    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.mine_fragment, null);
        ll_mine_u_like = (LinearLayout) inflate.findViewById(R.id.ll_mine_u_like);
        ll_mine_suning_order = (LinearLayout) inflate.findViewById(R.id.ll_mine_suning_order);
        ll_mine_shiti_show = (LinearLayout) inflate.findViewById(R.id.ll_mine_shiti_show);
        //ll_mine_salery_show = (LinearLayout) inflate.findViewById(R.id.ll_mine_salery_show);
        swip_fresh = (SwipeRefreshLayout) inflate.findViewById(R.id.swip_fresh);
        civMeHeadIcon = (CircularImage) inflate.findViewById(R.id.civ_me_head_icon);

        iv_mine_compont_l01 = (ImageView) inflate.findViewById(R.id.iv_mine_compont_l01);
        iv_mine_compont_l02 = (ImageView) inflate.findViewById(R.id.iv_mine_compont_l02);
        iv_mine_compont_l03 = (ImageView) inflate.findViewById(R.id.iv_mine_compont_l03);
        iv_mine_compont_l04 = (ImageView) inflate.findViewById(R.id.iv_mine_compont_l04);
        iv_mine_compont_l05 = (ImageView) inflate.findViewById(R.id.iv_mine_compont_l05);
        iv_mine_compont_l06 = (ImageView) inflate.findViewById(R.id.iv_mine_compont_l06);

        ll_userinfo_head = (LinearLayout) inflate.findViewById(R.id.ll_userinfo_head);
        tv_mine_tabao_account = (TextView) inflate.findViewById(R.id.tv_mine_tabao_account);
        ll_userinfo_alipay = (LinearLayout) inflate.findViewById(R.id.ll_userinfo_alipay);
        tv_userinfo_alipay = (TextView) inflate.findViewById(R.id.tv_userinfo_alipay);
        tv_userinfo_level_icon = (TextView) inflate.findViewById(R.id.tv_userinfo_level_icon);
        tv_userinfo_level_name = (TextView) inflate.findViewById(R.id.tv_userinfo_level_name);

        rl_mine_head_login = (RelativeLayout) inflate.findViewById(R.id.rl_mine_head_login);
        rl_mine_head_userinfo = (RelativeLayout) inflate.findViewById(R.id.rl_mine_head_userinfo);

        btn_mine_head_login = (Button) inflate.findViewById(R.id.btn_mine_head_login);
        btn_mine_head_register = (Button) inflate.findViewById(R.id.btn_mine_head_register);

        tv_grand_total = (TextView) inflate.findViewById(R.id.tv_grand_total);
        iv_apply_ad_show = (ImageView) inflate.findViewById(R.id.iv_apply_ad_show);

        ll_mine_content_show = (LinearLayout) inflate.findViewById(R.id.ll_mine_content_show);
        return inflate;
    }

    @Override
    public void initData() {
        login = Tools.isLogin(getContext());
        bindTao = Tools.isBindTao(getContext());
        bindAlipay = Tools.isBindAlipay(getContext());
        groupId = Tools.getGroupId(getContext());
        shopType = Tools.getShopType(getContext());
        LogUtil.e("Mine shopType = " + shopType);
        LogUtil.e("groupId = " + groupId);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case CHECK_NULL:
                        llMineCompontFirst.setVisibility(View.VISIBLE);
                        llMineCompontSecond.setVisibility(View.VISIBLE);
                        tvMineCompontL01.setText("提取现金");
                        tvMineCompontL02.setText("邀请好友");
                        tvMineCompontL03.setText("账户明细");
                        tvMineCompontL04.setText("商家信息");
                        tvMineCompontL05.setText("用户帮助");
                        tvMineCompontL06.setText("返佣规则");
                        iv_mine_compont_l01.setImageResource(R.drawable.tixian_icon);
                        iv_mine_compont_l02.setImageResource(R.drawable.friend_icon);
                        iv_mine_compont_l03.setImageResource(R.drawable.zhanghu_icon);
                        iv_mine_compont_l04.setImageResource(R.drawable.shopinfo_icon);
                        iv_mine_compont_l05.setImageResource(R.drawable.help_icon);
                        iv_mine_compont_l06.setImageResource(R.drawable.fanyong_icon);

                        llMineUserBack.setVisibility(View.VISIBLE);
                        rlMineToggle.setVisibility(View.VISIBLE);
                        llMineZhuanOrder.setVisibility(View.VISIBLE);
                        vTuiZhuanLine.setVisibility(View.VISIBLE);
                        vToggleLine.setVisibility(View.VISIBLE);
                        if (llMineCompontFirst != null && llMineCompontSecond != null
                                && tvMineCompontL01 != null && tvMineCompontL02 != null
                                && tvMineCompontL03 != null && tvMineCompontL04 != null
                                && tvMineCompontL05 != null && tvMineCompontL06 != null
                                && llMineUserBack != null && rlMineToggle != null &&
                                llMineZhuanOrder != null &&
                                civMeHeadIcon != null) {
                            loadMineData();
                            if (handler != null) {
                                handler.removeMessages(CHECK_NULL);
                            }
                        } else {
                            if (handler != null) {
                                handler.sendEmptyMessageDelayed(CHECK_NULL, 600);
                            }
                        }
                        break;
                }
            }
        };
        if (handler != null) {
            handler.sendEmptyMessageDelayed(CHECK_NULL, 300);
        }
    }

    private void setupSwipeRefresh() {
        if (swip_fresh != null) {
            swip_fresh.setColorSchemeResources(R.color.colorFirst,
                    R.color.colorSecond, R.color.colorThird);
            swip_fresh.setProgressViewOffset(true, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
            swip_fresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestDataRefresh();
                    setRefresh(mIsRequestDataRefresh);
                    initData();
                }
            });
        }
    }

    /**
     * 设置刷新的方法
     *
     * @param requestDataRefresh 是否需要刷新
     */
    public void setRefresh(boolean requestDataRefresh) {
        if (!requestDataRefresh) {
            mIsRequestDataRefresh = false;
            if (swip_fresh != null) {
                swip_fresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swip_fresh != null) {
                            swip_fresh.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        } else {
            if (swip_fresh != null) {
                swip_fresh.setRefreshing(true);
            }
        }
    }

    public void requestDataRefresh() {
        mIsRequestDataRefresh = true;
    }

    public void stopDataRefresh() {
        mIsRequestDataRefresh = false;
    }

    /**
     * 导入我的界面信息
     */
    private void loadMineData() {
        if (shopType == 1) {
            loadGroupuserDefault();
            tvAccountMoneyTitle1.setText("余额/提现中");
            tvAccountMoneyTitle2.setText("赚（冻结期）");
            tvAccountMoney2.setVisibility(View.VISIBLE);
            tvAccountMoney.setVisibility(View.VISIBLE);
            llMineZhuanOrder.setVisibility(View.VISIBLE);
            vTuiZhuanLine.setVisibility(View.VISIBLE);
            ll_mine_shiti_show.setVisibility(View.VISIBLE);
            //ll_mine_salery_show.setVisibility(View.GONE);
        } else {
            loadCuserDefault();
            tvAccountMoneyTitle1.setText("余额");
            tvAccountMoneyTitle2.setText("充值");
            tvAccountMoney.setVisibility(View.GONE);
            tvAccountMoney2.setVisibility(View.GONE);
            llMineZhuanOrder.setVisibility(View.GONE);
            vTuiZhuanLine.setVisibility(View.GONE);
            ll_mine_shiti_show.setVisibility(View.GONE);
            //ll_mine_salery_show.setVisibility(View.VISIBLE);
        }
        if (groupId == 5) {
            loadLowerMermberShow();
        } else {
            loadLowerMermberhind();
        }
        boolean toggle_show = CacheUtils.getBoolean(getContext(), CacheUtils.TOGGLE_SHOW, false);
        tbnMineControl.setCurrentState(toggle_show);
        alibcShowParams = new AlibcShowParams(OpenType.H5, false);
        exParams = new HashMap<>();
        exParams.put("isv_code", "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
        netUtils = new NetUtil();
        netUtils2 = new NetUtil();
        defaultSet();
        ll_mine_content_show.setVisibility(View.VISIBLE);
        if (login) {
            rl_mine_head_login.setVisibility(View.GONE);
            rl_mine_head_userinfo.setVisibility(View.VISIBLE);
            userLogin = Tools.getUserLogin(getActivity());
            which = "userinfo";
            map = new HashMap<>();
            map.put("Id", userLogin);
            netUtils.okHttp2Server2(getContext(), url, map);
        } else {
            rl_mine_head_login.setVisibility(View.VISIBLE);
            rl_mine_head_userinfo.setVisibility(View.GONE);
        }
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        rlMineSet.setOnClickListener(myOnClickListener);
        llMineExtractCredits.setOnClickListener(myOnClickListener);
        llMineFindFriends.setOnClickListener(myOnClickListener);
        llMineAccountDetails.setOnClickListener(myOnClickListener);
        llMineShopDetails.setOnClickListener(myOnClickListener);
        llMineHuiyuanStatis.setOnClickListener(myOnClickListener);
        llMineFanyongRule.setOnClickListener(myOnClickListener);

        llMineTaobaoOrder.setOnClickListener(myOnClickListener);
        llMineJuyoubangOrder.setOnClickListener(myOnClickListener);
        llMineZhuanOrder.setOnClickListener(myOnClickListener);
        llMineUserSalery.setOnClickListener(myOnClickListener);
        llMineUserBack.setOnClickListener(myOnClickListener);
        //civMeHeadIcon.setOnClickListener(myOnClickListener);
        llLowerMemberStatics.setOnClickListener(myOnClickListener);
        //ivMePersonLingdang.setOnClickListener(myOnClickListener);

        ll_mine_u_like.setOnClickListener(myOnClickListener);
        ll_mine_suning_order.setOnClickListener(myOnClickListener);

        ll_userinfo_head.setOnClickListener(myOnClickListener);
        ll_userinfo_alipay.setOnClickListener(myOnClickListener);

        btn_mine_head_login.setOnClickListener(myOnClickListener);
        btn_mine_head_register.setOnClickListener(myOnClickListener);
        setupSwipeRefresh();
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                try {
                    LogUtil.e("userinfo = " + response);
                    if (which.equals("userinfo")) {
                        parseData(response);
                    } else if (which.equals("bind_taobao")) {
                        parseBindTaobaoData(response);
                    } else if (which.equals("sms")) {
                        parseSmsData(response);
                    } else if (which.equals("bind_pay")) {
                        parseBindPayData(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "==" + e.getMessage());
                EncryptNetUtil.startNeterrorPage(getContext());
            }
        });
        if (tbnMineControl != null) {
            tbnMineControl.setOnStateChangeListener(new MyToggleButton.OnStateChangeListener() {
                @Override
                public void isCurrentState(boolean currentState) {
                    CacheUtils.putBoolean(getContext(), CacheUtils.TOGGLE_SHOW, currentState);
                    if (currentState) {
                        Toast.makeText(getContext(), "隐藏！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "显示！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        netUtils2.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1:
                        try {
                            parseHelpData(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 0:
                        Toast.makeText(getContext(), codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "-" + e.getMessage());
                EncryptNetUtil.startNeterrorPage(getContext());
            }
        });
    }

    /**
     * @param response 解析绑定支付宝数据
     */
    private void parseBindPayData(String response) throws Exception {
        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
        int statusCode = codeStatueBean.getStatusCode();
        switch (statusCode) {
            case 1:
                CacheUtils.putBoolean(getContext(), CacheUtils.PAY_STATE, true);
                CacheUtils.putString(getContext(), CacheUtils.ALIPAY_ACCOUNT, account);
                CacheUtils.putString(getContext(), CacheUtils.ALIPAY_USERNAME, name);
                initData();
                break;
            case 0:
                Toast.makeText(getContext(), codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 隐藏下级会员统计
     */
    private void loadLowerMermberhind() {
        llLowerMemberStatics.setVisibility(View.GONE);
    }

    /**
     * 显示下级会员统计
     */
    private void loadLowerMermberShow() {
        llLowerMemberStatics.setVisibility(View.VISIBLE);
    }


    /**
     * 解析帮助和返佣规则的数据
     *
     * @param response
     */
    private void parseHelpData(String response) throws Exception {
        HelpRuleBean helpRuleBean = new Gson().fromJson(response, HelpRuleBean.class);
        List<HelpRuleBean.DataBean> data = helpRuleBean.getData();
        String app_help_url = data.get(0).getApp_help_url();
        toHelp(app_help_url);
    }

    private void loadCuserDefault() {
        llMineCompontFirst.setVisibility(View.VISIBLE);
        llMineCompontSecond.setVisibility(View.GONE);
        tvMineCompontL01.setText("申请推广赚钱");
        tvMineCompontL03.setText("用户帮助");
        iv_mine_compont_l01.setImageResource(R.drawable.ruzhu_icon);
        iv_mine_compont_l03.setImageResource(R.drawable.help_icon);
        llMineUserBack.setVisibility(View.INVISIBLE);
        llMineZhuanOrder.setVisibility(View.GONE);
        rlMineToggle.setVisibility(View.GONE);
        vTuiZhuanLine.setVisibility(View.GONE);
        vToggleLine.setVisibility(View.GONE);
        iv_apply_ad_show.setVisibility(View.VISIBLE);
    }

    private void loadGroupuserDefault() {
        llMineCompontFirst.setVisibility(View.VISIBLE);
        llMineCompontSecond.setVisibility(View.VISIBLE);
        tvMineCompontL01.setText("提取现金");
        tvMineCompontL02.setText("邀请好友");
        tvMineCompontL03.setText("账户明细");
        tvMineCompontL04.setText("商家信息");
        tvMineCompontL05.setText("用户帮助");
        tvMineCompontL06.setText("返佣规则");
        iv_mine_compont_l01.setImageResource(R.drawable.tixian_icon);
        iv_mine_compont_l02.setImageResource(R.drawable.friend_icon);
        iv_mine_compont_l03.setImageResource(R.drawable.zhanghu_icon);
        iv_mine_compont_l04.setImageResource(R.drawable.shopinfo_icon);
        iv_mine_compont_l05.setImageResource(R.drawable.help_icon);
        iv_mine_compont_l06.setImageResource(R.drawable.fanyong_icon);
        llMineUserBack.setVisibility(View.VISIBLE);
        rlMineToggle.setVisibility(View.VISIBLE);
        llMineZhuanOrder.setVisibility(View.VISIBLE);
        vTuiZhuanLine.setVisibility(View.VISIBLE);
        vToggleLine.setVisibility(View.VISIBLE);
        iv_apply_ad_show.setVisibility(View.GONE);
    }

    /**
     * 解析数据
     *
     * @param response
     */
    private void parseData(String response) throws Exception {
        LogUtil.e(response);
        Gson gson = new Gson();
        MineBean mineBean = gson.fromJson(response, MineBean.class);
        int statusCode = mineBean.getStatusCode();
        List<MineBean.DataBean> data = mineBean.getData();
        switch (statusCode) {
            case 0:
                if (getActivity() == null) {
                    return;
                }
                Toast.makeText(getActivity(), mineBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                if (data != null) {
                    loadUserData(data);
                }
                break;
        }
    }

    /**
     * 导入用户信息
     *
     * @param data
     */
    private void loadUserData(List<MineBean.DataBean> data) {
        MineBean.DataBean dataBean = data.get(0);
        String avatar = dataBean.getAvatar();
        String group = dataBean.getGroup();
        String mobile = dataBean.getMobile();
        String nick_name = dataBean.getNick_name();
        amount = dataBean.getAmount();
        user_earn = dataBean.getUser_earn();
        frostAmount = dataBean.getFrostAmount();
        group_id = dataBean.getGroup_id();
        advertising = dataBean.getAdvertising();
        applyState = dataBean.getApply_status();
        payState = dataBean.getPay_status();
        apply_type = dataBean.getApply_type();
        pay_type = dataBean.getPay_type();
        shopType = dataBean.getGroup_type();
        grand_total = dataBean.getGrand_total();
        groupId = group_id;
        String user_get_ratio = dataBean.getUser_get_ratio();
        if (user_get_ratio != null) {
            CacheUtils.putString(getContext(), CacheUtils.USER_RATE, user_get_ratio);
        }
        if (advertising != null) {
            CacheUtils.putString(getContext(), CacheUtils.ADVER_ID, advertising);
        }
        CacheUtils.putInteger(getContext(), CacheUtils.GROUP_TYPE, group_id);
        CacheUtils.putInteger(getContext(), CacheUtils.SHOP_TYPE, shopType);
        if (civMeHeadIcon == null) {
            return;
        }
        if (avatar != null && !TextUtils.isEmpty(avatar)) {
            if (!avatar.contains("http")) {
                avatar = Url.IMG_URL + avatar;
            }
            Glide.with(getContext()).load(avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                    .into(civMeHeadIcon);//请求成功后把图片设置到的控件
        } else {
            civMeHeadIcon.setImageResource(R.drawable.default_person_icon);
        }
        if (shopType == 1) {
            tvAccountMoneyTitle1.setText("余额/提现中");
            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.DOWN);
            tv_grand_total.setText(df.format(grand_total));
            tvAccountMoney.setText(df.format(amount) + "/" + df.format(frostAmount));
            tvAccountMoney.setVisibility(View.VISIBLE);
            tvAccountMoney2.setText(df.format(user_earn));
            Tools.setPartTextColor2(tvAccountMoney, df.format(amount) + "/" + df.format(frostAmount), "/");
            tvAccountMoneyTitle2.setText("赚(冻结期)");
            tvAccountMoney2.setVisibility(View.VISIBLE);
            ll_mine_shiti_show.setVisibility(View.VISIBLE);
            //ll_mine_salery_show.setVisibility(View.GONE);
        } else {
            tvAccountMoneyTitle1.setText("余额");
            tvAccountMoneyTitle2.setText("充值");
            tvAccountMoney2.setVisibility(View.GONE);
            ll_mine_shiti_show.setVisibility(View.GONE);
            //ll_mine_salery_show.setVisibility(View.VISIBLE);
        }
        if (shopType == 1) {
            loadGroupuserDefault();
        } else {
            loadCuserDefault();
        }
        if (groupId == 5) {
            loadLowerMermberShow();
        } else {
            loadLowerMermberhind();
        }

        if (groupId == 5) {
            tv_userinfo_level_icon.setBackgroundResource(R.drawable.head_daili_level_icon);
            tv_userinfo_level_icon.setText("VIP4");
            tv_userinfo_level_name.setText("代理商");
        } else if (groupId == 4) {
            tv_userinfo_level_icon.setBackgroundResource(R.drawable.head_shiti_level_icon);
            tv_userinfo_level_icon.setText("VIP3");
            tv_userinfo_level_name.setText("实体商家");
        } else if (groupId == 6) {
            tv_userinfo_level_icon.setBackgroundResource(R.drawable.head_daren_level_icon);
            tv_userinfo_level_icon.setText("VIP2");
            tv_userinfo_level_name.setText("微达人");
        } else if (groupId == 1) {
            tv_userinfo_level_icon.setBackgroundResource(R.drawable.head_putong_level_icon);
            tv_userinfo_level_icon.setText("LV1");
            tv_userinfo_level_name.setText("普通会员");
        } else {
            tv_userinfo_level_icon.setVisibility(View.GONE);
            tv_userinfo_level_name.setText("其他");
        }

        LogUtil.e("group_id ==================" + groupId);
        if (bindTao) {
            taobao_nick = CacheUtils.getString(getContext(), CacheUtils.TAOBAO_NICK, "");
            tv_mine_tabao_account.setText(taobao_nick);
        } else {
            tv_mine_tabao_account.setText("绑定淘宝>>");
        }
        if (bindAlipay) {
            alipay_account = CacheUtils.getString(getContext(), CacheUtils.ALIPAY_ACCOUNT, "");
            alipay_name = CacheUtils.getString(getContext(), CacheUtils.ALIPAY_USERNAME, "");
            tv_userinfo_alipay.setText(alipay_account + "(" + alipay_name + ")");
        } else {
            tv_userinfo_alipay.setText("未绑定，马上绑定>>");
        }

    }

    /**
     * 默认设置用户的信息
     */
    private void defaultSet() {
        civMeHeadIcon.setImageResource(R.drawable.default_person_icon);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Override
    protected void stopLoad() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    /**
     * 用于监听控件操作的处理类
     */
    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_mine_set:
                    if (Tools.isLogin(getActivity())) {
                        toSetActivity();
                    } else {
                        toLogin();
                    }
                    break;
                case R.id.ll_mine_extract_credits:
                    if (!login) {
                        toLogin();
                        return;
                    }
                    LogUtil.e("shopType = " + shopType + ",applyState = " + applyState + ",apply_type = "
                            + apply_type + ",pay_type = " + pay_type + ",payState = " + payState);
                    if (shopType == 1) {
                        toExtraCredits();
                    } else {
                        if (applyState == 1) {
                            Toast.makeText(getContext(), "您已经申请成功", Toast.LENGTH_SHORT).show();
                        } else if (applyState == 2) {
                            Toast.makeText(getContext(), "您的申请被驳回，请核对后重新申请！", Toast.LENGTH_SHORT).show();
                        } else if (applyState == 3) {
                            if (apply_type == 6) {
                                Toast.makeText(getContext(), "您的申请已提交，请等待工作人员处理！", Toast.LENGTH_SHORT).show();
                            } else if (apply_type == 5) {
                                if (pay_type == 1) {
                                    if (payState == 1) {
                                        Toast.makeText(getContext(), "您的申请已提交，请等待工作人员处理！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "请您支付加盟费用！", Toast.LENGTH_SHORT).show();
                                        toAlipay();
                                    }
                                } else if (pay_type == 2) {
                                    Toast.makeText(getContext(), "您的申请已提交，请等待工作人员处理！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else if (applyState == 0) {
                            toMerchantEnter();
                        }
                    }
                    break;
                case R.id.ll_mine_account_details:
                    if (!login) {
                        toLogin();
                        return;
                    }
                    if (shopType == 1) {
                        toAccountDetail();
                    } else {
                        //跳帮助
                        which = "help";
                        netUtils2.okHttp2Server2(getContext(), help_url, null);
                    }
                    break;
                case R.id.ll_mine_find_friends:
                    if (!login) {
                        toLogin();
                        return;
                    }
                    toInviateFriend();
                    break;
                case R.id.ll_mine_shop_details:
                    if (!login) {
                        toLogin();
                        return;
                    }
                    toShopDetail();
                    break;
                case R.id.ll_mine_taobao_order:
                    AlibcTrade.show(getActivity(), new AlibcMyOrdersPage(0, true), alibcShowParams, alibcTaokeParams, null, new DemoTradeCallback(getActivity()));
                    break;
                case R.id.ll_mine_juyoubang_order:
                    toJuyouOrder();
                    break;
                case R.id.ll_mine_zhuan_order:
                    toZhuanOrder();
                    break;
                case R.id.ll_mine_user_salery:
                    toAccountDetail();
                    break;
                case R.id.ll_mine_user_back:
                    toBackRebate();
                    break;
                case R.id.ll_mine_huiyuan_statis:
                    //条帮助
                    which = "help";
                    netUtils2.okHttp2Server2(getContext(), help_url, null);
                    break;
                case R.id.ll_mine_fanyong_rule:
                    //跳返佣规则
                    which = "rule";
                    netUtils2.okHttp2Server2(getContext(), rule_url, null);
                    break;
                case R.id.civ_me_head_icon:
                    if (login) {
                        toSetActivity();
                    } else {
                        toLogin();
                    }
                    break;
                case R.id.ll_lower_member_statics:
                    toLowerMemberStatics();
                    break;
                case R.id.ll_mine_u_like:
                    //toSuningGoodscart();
                    break;
                case R.id.ll_mine_suning_order:
                    if (!login) {
                        toLogin();
                        return;
                    }
                    toSuningOrder();
                    break;
                case R.id.ll_userinfo_head:
                    if (!bindTao) {
                        bindTao();
                    }
                    break;
                case R.id.ll_userinfo_alipay:
                    if (!bindAlipay) {
                        bindAlipay();
                    }
                    break;
                case R.id.btn_mine_head_login:
                    toLogin();
                    break;
                case R.id.btn_mine_head_register:
                    toRegister();
                    break;
            }
        }
    }


    /**
     * 绑定支付宝
     */
    private void bindAlipay() {
        showAlipayAccount();
    }

    /**
     * 绑定淘宝
     */
    private void bindTao() {
        showBindTaobaoDialog(userLogin);
    }

    /**
     * 跳转到苏宁购物车
     */
    private void toSuningGoodscart() {
        Intent intent = new Intent(getContext(), TestActivity_l04.class);
        startActivity(intent);
    }

    /**
     * 跳转到苏宁页面
     */
    private void toSuningOrder() {
        Intent intent = new Intent(getContext(), SuningOrderActivity.class);
        startActivity(intent);
    }


    /**
     * 跳转到下级会员统计界面
     */
    private void toLowerMemberStatics() {
        Intent intent = new Intent(getContext(), LowerMemberStaticsActivity.class);
        startActivity(intent);
    }

    private void toHelp(String url) {
        LogUtil.e("返佣规则 = " + url);
        Intent intent = new Intent(getContext(), RuleHelpActivity.class);
        intent.putExtra("web_url", url);
        intent.putExtra("which", which);
        startActivity(intent);

    }

    /**
     * 跳转到商家入驻界面
     */
    private void toMerchantEnter() {
        Intent intent = new Intent(getContext(), WhichJoinActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到返佣界面
     */
    private void toBackRebate() {
        Intent intent = new Intent(getContext(), BackRebateActivity.class);
        startActivity(intent);
    }


    /**
     * 跳转到聚优帮订单
     */
    private void toJuyouOrder() {
        Intent intent = new Intent(getActivity(), OrderActivity.class);
        intent.putExtra("order_type", -1);
        intent.putExtra("tab_p", 0);
        startActivity(intent);
    }

    /**
     * 跳转到推广赚订单
     */
    private void toZhuanOrder() {
        Intent intent = new Intent(getActivity(), OrderActivity.class);
        intent.putExtra("order_type", -2);
        intent.putExtra("tab_p", 0);
        startActivity(intent);
    }

    /**
     * 跳转商铺信息
     */
    private void toShopDetail() {
        Intent intent = new Intent(getActivity(), ShopDetailActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转邀请好友
     */
    private void toInviateFriend() {
        Intent intent = new Intent(getActivity(), InviteFriendsActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转账户明细
     */
    private void toAccountDetail() {
        Intent intent = new Intent(getActivity(), AccountDetailAvtivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到提取积分宝
     */
    private void toExtraCredits() {
        Intent intent = new Intent(getActivity(), ExtractCreditsActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到设置界面
     */
    private void toSetActivity() {
        Intent intent = new Intent(getActivity(), SetActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到订单管理页面
     *
     * @param tab_p//订单状态 0：全部订单 1：代付款
     */
    private void toOrderPager(int tab_p) {
        Intent intent;
        if (group_id != 1) {
            intent = new Intent(getActivity(), OrderActivity.class);
            intent.putExtra("tab_p", tab_p);
            startActivity(intent);
        } else {
            AlibcTrade.show(getActivity(), new AlibcMyOrdersPage(tab_p, true), alibcShowParams, alibcTaokeParams, null, new DemoTradeCallback(getActivity()));
        }
    }

    /**
     * 跳转到登录页面
     */
    private void toLogin() {
        Toast.makeText(getContext(), "请先登录账号！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到注册页面
     */
    private void toRegister() {
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }

    public void onEvent(AnyEvent event) {
        if (event.getType() == EventType.EVENT_LOGIN) {
            String msg = "onEventMainThread收到了消息：" + event.getMessage();
            mseg = msg;
            LogUtil.e(msg);
            initData();
        }
    }

    /**
     * 去支付宝支付页面
     */
    private void toAlipay() {
        Intent intent = new Intent(getContext(), AlipayManagerActivity.class);
        startActivity(intent);
    }

    /**
     * 展示绑定淘宝的dialog
     */
    private void showBindTaobaoDialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("绑定淘宝账号")
                .setMessage("为了方便您之后的操作，请您先绑定淘宝账号。若取消绑定将会在您查看商品详情时提示您继续绑定操作")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bindTaobao(id);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        alertDialog = builder.create();
        MainActivity mainActivity = (MainActivity) getActivity();
        if (getContext() != null && !mainActivity.isFinishing()) {
            alertDialog.show();
        }
    }

    /* 绑定淘宝
     * @param s
     */
    public void bindTaobao(final String s) {

        alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                //获取淘宝用户信息
                LogUtil.e("获取淘宝用户信息: " + AlibcLogin.getInstance().getSession());
                LogUtil.e("代码:" + i);
                openId = AlibcLogin.getInstance().getSession().openId;
                avatarUrl = AlibcLogin.getInstance().getSession().avatarUrl;
                nick = AlibcLogin.getInstance().getSession().nick;
                uploadUserInfo(s);
                which = "bind_taobao";
                netUtils.okHttp2Server2(getContext(), bind_url, map);
            }

            @Override
            public void onFailure(int code, String msg) {
//                Toast.makeText(LoginActivity.this, "绑定失败 ！",
//                        Toast.LENGTH_LONG).show();
//                Log.i("GGG", "错误码" + code + "原因" + msg);
                EventBus.getDefault().post(new AnyEvent(EventType.EVENT_LOGIN, "登录成功！"));
            }
        });
    }

    /**
     * 上传用户信息
     *
     * @param s
     */
    public void uploadUserInfo(String s) {

        if (!TextUtils.isEmpty(s)) {
            map = new HashMap<>();
            map.put("Id", s);
            map.put("openId", openId);
            map.put("portrait", avatarUrl);
            map.put("nickname", nick);
        }
    }

    /**
     * 解析绑定淘宝的数据
     *
     * @param response
     */
    private void parseBindTaobaoData(String response) throws Exception {
        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
        int statusCode = codeStatueBean.getStatusCode();
        switch (statusCode) {
            case 1:
                CacheUtils.putBoolean(getContext(), CacheUtils.BIND_STATE, true);
                CacheUtils.putString(getContext(), CacheUtils.TAOBAO_NICK, nick);
                initData();
                break;
            case 0:
                Toast.makeText(getContext(), codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 设置用户支付宝账号新
     */
    private void showAlipayAccount() {
        View inflate = View.inflate(getContext(), R.layout.bind_alipay_dailog, null);
        final EditText etAccount = (EditText) inflate.findViewById(R.id.et_pay_account);
        final EditText etName = (EditText) inflate.findViewById(R.id.et_pay_name);
        final EditText etSmsCode = (EditText) inflate.findViewById(R.id.et_alipay_smscode);
        final TextView tvAlipayGetSms = (TextView) inflate.findViewById(R.id.tv_alipay_getSms);
        TextView tvBindAlipayCancel = (TextView) inflate.findViewById(R.id.tv_bind_alipay_cancel);
        TextView tvBindAlipayConfirm = (TextView) inflate.findViewById(R.id.tv_bind_alipay_confirm);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        alertDialog = builder.setView(inflate).create();
        etAccount.addTextChangedListener(new MyTextWatcher());
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null && !activity.isFinishing()) {
            alertDialog.show();
        }
        tvAlipayGetSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_account = etAccount.getText().toString().trim();
                String userPhone = Tools.getUserPhone(getContext());
                LogUtil.e(userPhone);
                getSmsCode(userPhone, user_account, tvAlipayGetSms);
            }
        });

        tvBindAlipayCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alertDialog != null && alertDialog.isShowing()) {
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer = null;
                    }
                    alertDialog.dismiss();
                }
            }
        });
        tvBindAlipayConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account = etAccount.getText().toString().trim();
                name = etName.getText().toString().trim();
                String smscode = etSmsCode.getText().toString().trim();
                if (!Check.isTelPhoneNumber(account) && !Check.isEmail(account)) {
                    Toast.makeText(getContext(), "输入的支付宝账号不能为空而且只能为正确的手机号码或者邮箱，请您重新输入！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Check.isLegalName(name)) {
                    Toast.makeText(getContext(), "输入用户名不能为空而且只能为中文真实姓名，请您重新输入！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(smscode)) {
                    Toast.makeText(getContext(), "请输入短信验证码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (smscode.equals(sms_code_s)) {
                    bindPay(account, name);
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer = null;
                    }
                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }

                } else {
                    Toast.makeText(getContext(), "短信验证码不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });

    }

    /**
     * 获取验证码的方法
     */
    private void getSmsCode(String userPhone, String user, final TextView tv) {

        tv.setClickable(false);
        tv.setTextColor(Color.parseColor("#ffffff"));
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tv.setText(l / 1000 + "s后重新获取");
            }

            @Override
            public void onFinish() {
                tv.setClickable(true);
                tv.setText("获取验证码");
                tv.setTextColor(Color.parseColor("#ffffff"));
            }
        };
        which = "sms";
        map = new HashMap<>();
        map.put("Id", userLogin);
        map.put("sms_code", userPhone);
        map.put("type", "3");
        netUtils.okHttp2Server2(getContext(), sms_url, map);
        if (countDownTimer != null) {
            countDownTimer.start();
        }
    }

    /**
     * 解析短信验证数据
     *
     * @param response
     */
    private void parseSmsData(String response) throws Exception {
        LogUtil.e(response);
        SmsCodeBean smsCodeBean = new Gson().fromJson(response, SmsCodeBean.class);
        int statusCode = smsCodeBean.getStatusCode();
        switch (statusCode) {
            case 0:
                Toast.makeText(getContext(), "获取验证码失败！", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                List<SmsCodeBean.DataBean> data = smsCodeBean.getData();
                if (data.size() > 0) {
                    sms_code_s = data.get(0).getSms_code();
                }
                Toast.makeText(getContext(), "验证码已发送你的手机，请查收！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 绑定支付宝
     */
    private void bindPay(String account, String name) {
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "支付宝账号和密码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            if (login) {
                Map map = new HashMap<String, String>();
                map.put("Id", userLogin);
                map.put("alipay", account);
                map.put("alipayName", name);
                which = "bind_pay";
                netUtils.okHttp2Server2(getContext(), pay_url, map);
            } else {
                toLogin();
            }
        }
    }
}
