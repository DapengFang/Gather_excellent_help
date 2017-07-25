package com.gather_excellent_help.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import com.gather_excellent_help.R;
import com.gather_excellent_help.aliapi.DemoTradeCallback;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.MineBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.activity.OrderActivity;
import com.gather_excellent_help.ui.activity.SetActivity;
import com.gather_excellent_help.ui.activity.credits.AccountDetailAvtivity;
import com.gather_excellent_help.ui.activity.credits.ExtractCreditsActivity;
import com.gather_excellent_help.ui.activity.credits.InviteFriendsActivity;
import com.gather_excellent_help.ui.activity.credits.ShopDetailActivity;
import com.gather_excellent_help.ui.base.BaseFragment;
import com.gather_excellent_help.ui.widget.CircularImage;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.imageutils.ImageLoader;
import com.google.gson.Gson;

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

public class MineFragment extends BaseFragment {

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
    @Bind(R.id.iv_me_daifukuan)
    ImageView ivMeDaifukuan;
    @Bind(R.id.iv_me_daifahuo)
    ImageView ivMeDaifahuo;
    @Bind(R.id.iv_me_daishouhuo)
    ImageView ivMeDaishouhuo;
    @Bind(R.id.iv_me_daipinjia)
    ImageView ivMeDaipinjia;
    @Bind(R.id.tv_mine_mobietype)
    TextView tvMineMobietype;
    @Bind(R.id.ll_mine_see_allorder)
    LinearLayout llMineSeeAllorder;
    @Bind(R.id.ll_mine_see_daifukuan)
    LinearLayout llMineSeeDaifukuan;
    @Bind(R.id.ll_mine_see_yifukuan)
    LinearLayout llMineSeeYifukuan;
    @Bind(R.id.ll_mine_see_yiwancheng)
    LinearLayout llMineSeeYiwancheng;
    @Bind(R.id.ll_mine_see_shouhou)
    LinearLayout llMineSeeShouhou;
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
    private String mseg = "";

    private NetUtil netUtils;
    private Map<String, String> map;
    private String url = Url.BASE_URL + "Mine.aspx";
    private ImageLoader mImageLoader;
    private int group_id = -1;

    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private AlibcTaokeParams alibcTaokeParams = null;//淘客参数，包括pid，unionid，subPid
    private Boolean isTaoke = false;//是否是淘客商品类型
    private String itemId = "522166121586";//默认商品id
    private String shopId = "60552065";//默认店铺id
    private Map<String, String> exParams;//yhhpass参数

    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.mine_fragment, null);
        return inflate;
    }

    @Override
    public void initData() {
        alibcShowParams = new AlibcShowParams(OpenType.H5, false);
        exParams = new HashMap<>();
        exParams.put("isv_code", "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
        setPartTextColor();
        netUtils = new NetUtil();
        defaultSet();
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if (Tools.isLogin(getActivity())) {
            String userLogin = Tools.getUserLogin(getActivity());
            map = new HashMap<>();
            map.put("Id", userLogin);
            netUtils.okHttp2Server2(url, map);
        } else {
            if (TextUtils.isEmpty(mseg)) {
                Toast.makeText(getContext(), "请先登录！", Toast.LENGTH_SHORT).show();
            }
        }
        rlMineSet.setOnClickListener(new MyOnClickListener());
        llMineSeeAllorder.setOnClickListener(new MyOnClickListener());
        llMineSeeDaifukuan.setOnClickListener(new MyOnClickListener());
        llMineSeeYifukuan.setOnClickListener(new MyOnClickListener());
        llMineSeeYiwancheng.setOnClickListener(new MyOnClickListener());
        llMineSeeShouhou.setOnClickListener(new MyOnClickListener());

        llMineExtractCredits.setOnClickListener(new MyOnClickListener());
        llMineFindFriends.setOnClickListener(new MyOnClickListener());
        llMineAccountDetails.setOnClickListener(new MyOnClickListener());
        llMineShopDetails.setOnClickListener(new MyOnClickListener());
        llMineHuiyuanStatis.setOnClickListener(new MyOnClickListener());
        llMineFanyongRule.setOnClickListener(new MyOnClickListener());
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString() + "==" + e.getMessage());
            }
        });
    }

    /**
     * 设置部分字体颜色为红色
     */
    private void setPartTextColor() {
        String str = "0.00/0.00";
        int end = str.indexOf("/");
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(Color.RED), 0, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvAccountMoney.setText(style);
    }

    /**
     * 解析数据
     *
     * @param response
     */
    private void parseData(String response) {
        Gson gson = new Gson();
        MineBean mineBean = gson.fromJson(response, MineBean.class);
        int statusCode = mineBean.getStatusCode();
        List<MineBean.DataBean> data = mineBean.getData();
        switch (statusCode) {
            case 0:
                Toast.makeText(getActivity(), mineBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                loadUserData(data);
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
        group_id = dataBean.getGroup_id();
        if (avatar != null && !TextUtils.isEmpty(avatar)) {
            mImageLoader.loadImage(avatar, civMeHeadIcon, true);
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
                case R.id.ll_mine_see_allorder:
                    if (!Tools.isLogin(getContext())) {
                        toLogin();
                    } else {
                        toOrderPager(0);
                    }
                    break;
                case R.id.ll_mine_see_daifukuan:
                    if (!Tools.isLogin(getContext())) {
                        toLogin();
                    } else {
                        toOrderPager(1);
                    }
                    break;
                case R.id.ll_mine_see_yifukuan:
                    if (!Tools.isLogin(getContext())) {
                        toLogin();
                    } else {
                        toOrderPager(2);
                    }
                    break;
                case R.id.ll_mine_see_yiwancheng:
                    if (!Tools.isLogin(getContext())) {
                        toLogin();
                    } else {
                        toOrderPager(3);
                    }
                    break;
                case R.id.ll_mine_see_shouhou:
                    if (!Tools.isLogin(getContext())) {
                        toLogin();
                    } else {
                        toOrderPager(4);
                    }
                    break;
                case R.id.ll_mine_extract_credits:
                    toExtraCredits();
                    break;
                case R.id.ll_mine_account_details:
                    toAccountDetail();
                    break;
                case R.id.ll_mine_find_friends:
                    toInviateFriend();
                    break;
                case R.id.ll_mine_shop_details:
                    toShopDetail();
                    break;
            }
        }
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
        String msg = "onEventMainThread收到了消息：" + event.getMessage();
        mseg = msg;
        LogUtil.e(msg);
        initData();
    }

}
