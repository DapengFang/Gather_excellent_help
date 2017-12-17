package com.gather_excellent_help.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.aliapi.DemoTradeCallback;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.HelpRuleBean;
import com.gather_excellent_help.bean.MineBean;
import com.gather_excellent_help.ui.activity.BindPhoneActivity;
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
import com.gather_excellent_help.ui.base.LazyLoadFragment;
import com.gather_excellent_help.ui.widget.CircularImage;
import com.gather_excellent_help.ui.widget.MyToggleButton;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by wuxin on 2017/7/7.
 */

public class MineFragment extends LazyLoadFragment {

    @Bind(R.id.civ_me_head_icon)
    CircularImage civMeHeadIcon;
    @Bind(R.id.iv_me_person_set)
    ImageView ivMePersonSet;
    @Bind(R.id.rl_mine_set)
    RelativeLayout rlMineSet;
    @Bind(R.id.iv_me_person_lingdang)
    ImageView ivMePersonLingdang;
    @Bind(R.id.tv_me_nickname)
    TextView tvMeNickname;
    @Bind(R.id.rl_me_nickname)
    RelativeLayout rlMeNickname;
    @Bind(R.id.tv_account_money)
    TextView tvAccountMoney;
    @Bind(R.id.tv_account_money2)
    TextView tvAccountMoney2;
    @Bind(R.id.tv_mine_mobietype)
    TextView tvMineMobietype;
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
    @Bind(R.id.v_mine)
    View vMine;
    @Bind(R.id.ll_mine_back_show)
    LinearLayout llMineBackShow;
    @Bind(R.id.v_lower_member_up)
    View vLowerMemberUp;
    @Bind(R.id.ll_lower_member_statics)
    LinearLayout llLowerMemberStatics;
    @Bind(R.id.v_lower_member_down)
    View vLowerMemberDown;
    @Bind(R.id.v_tui_zhuan_line)
    View vTuiZhuanLine;
    @Bind(R.id.v_toggle_line)
    View vToggleLine;


    private LinearLayout ll_mine_u_like;
    private LinearLayout ll_mine_suning_order;
    private String mseg = "";

    private NetUtil netUtils;
    private NetUtil netUtils2;
    private Map<String, String> map;
    private String url = Url.BASE_URL + "Mine.aspx";
    private int group_id = -1;

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
    private double user_earn;
    private String which;

    public static final int CHECK_NULL = 4; //加载数据的标识

    private static final int REQUEST_CODE_SCAN_GALLERY = 0x123;//从相册获取二维码的标识

    private Handler handler;
    private int apply_type = -1;
    private int pay_type;

    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.mine_fragment, null);
        ll_mine_u_like = (LinearLayout) inflate.findViewById(R.id.ll_mine_u_like);
        ll_mine_suning_order = (LinearLayout) inflate.findViewById(R.id.ll_mine_suning_order);
        return inflate;
    }

    @Override
    public void initData() {
        login = Tools.isLogin(getContext());
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
                        tvMineCompontL02.setText("邀请好友赚钱啦");
                        tvMineCompontL03.setText("账户明细");
                        tvMineCompontL04.setText("商家信息");
                        tvMineCompontL05.setText("帮助");
                        tvMineCompontL06.setText("返佣规则");
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
                                llMineZhuanOrder != null && tvMeNickname != null &&
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
            handler.sendEmptyMessageDelayed(CHECK_NULL, 500);
        }
    }

    /**
     * 导入我的界面信息
     */
    private void loadMineData() {
        if (shopType == 1) {
            loadGroupuserDefault();
            tvAccountMoneyTitle1.setText("余额/提现中");
            tvAccountMoneyTitle2.setText("赚(冻结期)");
            tvAccountMoney2.setVisibility(View.VISIBLE);
            llMineZhuanOrder.setVisibility(View.VISIBLE);
            tvAccountMoney.setVisibility(View.VISIBLE);
            vTuiZhuanLine.setVisibility(View.VISIBLE);
        } else {
            loadCuserDefault();
            tvAccountMoneyTitle1.setText("余额");
            tvAccountMoneyTitle2.setText("充值");
            tvAccountMoney.setVisibility(View.GONE);
            tvAccountMoney2.setVisibility(View.GONE);
            llMineZhuanOrder.setVisibility(View.GONE);
            vTuiZhuanLine.setVisibility(View.GONE);
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
        if (login) {
            String userLogin = Tools.getUserLogin(getActivity());
            map = new HashMap<>();
            map.put("Id", userLogin);
            netUtils.okHttp2Server2(url, map);
        }
        rlMineSet.setOnClickListener(new MyOnClickListener());
        llMineExtractCredits.setOnClickListener(new MyOnClickListener());
        llMineFindFriends.setOnClickListener(new MyOnClickListener());
        llMineAccountDetails.setOnClickListener(new MyOnClickListener());
        llMineShopDetails.setOnClickListener(new MyOnClickListener());
        llMineHuiyuanStatis.setOnClickListener(new MyOnClickListener());
        llMineFanyongRule.setOnClickListener(new MyOnClickListener());

        llMineTaobaoOrder.setOnClickListener(new MyOnClickListener());
        llMineJuyoubangOrder.setOnClickListener(new MyOnClickListener());
        llMineZhuanOrder.setOnClickListener(new MyOnClickListener());
        llMineUserSalery.setOnClickListener(new MyOnClickListener());
        llMineUserBack.setOnClickListener(new MyOnClickListener());
        civMeHeadIcon.setOnClickListener(new MyOnClickListener());
        llLowerMemberStatics.setOnClickListener(new MyOnClickListener());
        ivMePersonLingdang.setOnClickListener(new MyOnClickListener());

        ll_mine_u_like.setOnClickListener(new MyOnClickListener());
        ll_mine_suning_order.setOnClickListener(new MyOnClickListener());
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e("userinfo = " + response);
                parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "==" + e.getMessage());
            }
        });
        if (tbnMineControl != null) {
            tbnMineControl.setOnStateChangeListener(new MyToggleButton.OnStateChangeListener() {
                @Override
                public void isCurrentState(boolean currentState) {
                    CacheUtils.putBoolean(getContext(), CacheUtils.TOGGLE_SHOW, currentState);
                    if (currentState) {
                        Toast.makeText(getContext(), "推广赚已隐藏！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "推广赚已显示！", Toast.LENGTH_SHORT).show();
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
                        parseHelpData(response);
                        break;
                    case 0:
                        Toast.makeText(getContext(), codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "-" + e.getMessage());
            }
        });
    }

    /**
     * 隐藏下级会员统计
     */
    private void loadLowerMermberhind() {
        llLowerMemberStatics.setVisibility(View.GONE);
        vLowerMemberUp.setVisibility(View.GONE);
        vLowerMemberDown.setVisibility(View.GONE);
    }

    /**
     * 显示下级会员统计
     */
    private void loadLowerMermberShow() {
        llLowerMemberStatics.setVisibility(View.VISIBLE);
        vLowerMemberUp.setVisibility(View.VISIBLE);
        vLowerMemberDown.setVisibility(View.VISIBLE);
    }


    /**
     * 解析帮助和返佣规则的数据
     *
     * @param response
     */
    private void parseHelpData(String response) {
        HelpRuleBean helpRuleBean = new Gson().fromJson(response, HelpRuleBean.class);
        List<HelpRuleBean.DataBean> data = helpRuleBean.getData();
        String app_help_url = data.get(0).getApp_help_url();
        toHelp(app_help_url);
    }

    private void loadCuserDefault() {
        llMineCompontFirst.setVisibility(View.VISIBLE);
        llMineCompontSecond.setVisibility(View.GONE);
        tvMineCompontL01.setText("申请加盟一起赚钱");
        tvMineCompontL03.setText("帮助");
        llMineUserBack.setVisibility(View.INVISIBLE);
        llMineZhuanOrder.setVisibility(View.GONE);
        rlMineToggle.setVisibility(View.GONE);
        vTuiZhuanLine.setVisibility(View.GONE);
        vToggleLine.setVisibility(View.GONE);
    }

    private void loadGroupuserDefault() {
        llMineCompontFirst.setVisibility(View.VISIBLE);
        llMineCompontSecond.setVisibility(View.VISIBLE);
        tvMineCompontL01.setText("提取现金");
        tvMineCompontL02.setText("邀请好友赚钱啦");
        tvMineCompontL03.setText("账户明细");
        tvMineCompontL04.setText("商家信息");
        tvMineCompontL05.setText("帮助");
        tvMineCompontL06.setText("返佣规则");
        llMineUserBack.setVisibility(View.VISIBLE);
        rlMineToggle.setVisibility(View.VISIBLE);
        llMineZhuanOrder.setVisibility(View.VISIBLE);
        vTuiZhuanLine.setVisibility(View.VISIBLE);
        vToggleLine.setVisibility(View.VISIBLE);
    }

    /**
     * 解析数据
     *
     * @param response
     */
    private void parseData(String response) {
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
        if (nick_name != null && !TextUtils.isEmpty(nick_name)) {
            tvMeNickname.setText(nick_name);
        } else {
            tvMeNickname.setText("聚优帮用户");
        }
        if (group != null && !TextUtils.isEmpty(group)) {
            tvMineMobietype.setText(group + "(" + mobile + ")");
        } else {
            tvMineMobietype.setText("用户类型 + (" + mobile + ")");
        }
        if (shopType == 1) {
            tvAccountMoneyTitle1.setText("余额/提现中");
            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.DOWN);
            tvAccountMoney.setText(df.format(amount) + "/" + df.format(frostAmount));
            tvAccountMoney.setVisibility(View.VISIBLE);
            tvAccountMoney2.setText(df.format(user_earn));
            Tools.setPartTextColor2(tvAccountMoney, df.format(amount) + "/" + df.format(frostAmount), "/");
            tvAccountMoneyTitle2.setText("赚(冻结期)");
            tvAccountMoney2.setVisibility(View.VISIBLE);
        } else {
            tvAccountMoneyTitle1.setText("余额");
            tvAccountMoneyTitle2.setText("充值");
            tvAccountMoney2.setVisibility(View.GONE);
        }
        if (shopType == 1) {
            loadGroupuserDefault();
        } else {
            loadCuserDefault();
        }
    }

    /**
     * 默认设置用户的信息
     */
    private void defaultSet() {
        civMeHeadIcon.setImageResource(R.drawable.default_person_icon);
        tvMeNickname.setText("聚优帮用户");
        tvMineMobietype.setText("用户类型 (手机号)");
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
                    if (shopType == 1) {
                        toExtraCredits();
                    } else {
                        if (applyState == 1) {
                            Toast.makeText(getContext(), "你已经申请成功", Toast.LENGTH_SHORT).show();
                        } else if (applyState == 2) {
                            Toast.makeText(getContext(), "你的申请被驳回，请核对后重新申请！", Toast.LENGTH_SHORT).show();
                        } else if (applyState == 3) {
                            if (apply_type == 6) {
                                Toast.makeText(getContext(), "你的申请已提交，请等待工作人员处理！", Toast.LENGTH_SHORT).show();
                            } else if (apply_type == 5) {
                                if (pay_type == 1) {
                                    if (payState == 1) {
                                        Toast.makeText(getContext(), "你的申请已提交，请等待工作人员处理！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "请你支付加盟费用！", Toast.LENGTH_SHORT).show();
                                        toAlipay();
                                    }
                                } else if (pay_type == 2) {
                                    Toast.makeText(getContext(), "你的申请已提交，请等待工作人员处理！", Toast.LENGTH_SHORT).show();
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
                        netUtils2.okHttp2Server2(help_url, null);
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
                    netUtils2.okHttp2Server2(help_url, null);
                    break;
                case R.id.ll_mine_fanyong_rule:
                    //跳返佣规则
                    which = "rule";
                    netUtils2.okHttp2Server2(rule_url, null);
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

                case R.id.iv_me_person_lingdang:
                    //测试热修复的小铃铛
                    Toast.makeText(getContext(), "热修复测试成功！！！", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.ll_mine_u_like:
                    //toSuningGoodscart();
                    break;
                case R.id.ll_mine_suning_order:
                    toSuningOrder();
                    break;
            }
        }
    }

    /**
     * 跳转到苏宁购物车
     */
    private void toSuningGoodscart() {
        Intent intent = new Intent(getContext(), BindPhoneActivity.class);
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

}
