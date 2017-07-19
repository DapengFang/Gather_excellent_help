package com.gather_excellent_help.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.MineBean;
import com.gather_excellent_help.ui.activity.OrderActivity;
import com.gather_excellent_help.ui.activity.SetActivity;
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

    private NetUtil netUtils;
    private Map<String, String> map;
    private String url = Url.BASE_URL + "Mine.aspx";
    private ImageLoader mImageLoader;

    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.mine_fragment, null);
        return inflate;
    }

    @Override
    public void initData() {
        setPartTextColor();
        netUtils = new NetUtil();
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if (Tools.isLogin(getActivity())) {
            String userLogin = Tools.getUserLogin(getActivity());
            map = new HashMap<>();
            map.put("Id", userLogin);
            netUtils.okHttp2Server2(url, map);
        }
        rlMineSet.setOnClickListener(new MyOnClickListener());
        llMineSeeAllorder.setOnClickListener(new MyOnClickListener());
        llMineSeeDaifukuan.setOnClickListener(new MyOnClickListener());
        llMineSeeYifukuan.setOnClickListener(new MyOnClickListener());
        llMineSeeYiwancheng.setOnClickListener(new MyOnClickListener());
        llMineSeeShouhou.setOnClickListener(new MyOnClickListener());
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
        mImageLoader.loadImage(avatar, civMeHeadIcon, true);
        tvMeNickname.setText(nick_name);
        tvMineMobietype.setText(group + "(" + mobile + ")");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.rl_mine_set:
                    intent = new Intent(getActivity(), SetActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_mine_see_allorder:
                    intent = new Intent(getActivity(), OrderActivity.class);
                    intent.putExtra("tab_p",0);
                    startActivity(intent);
                    break;
                case R.id.ll_mine_see_daifukuan:
                    intent = new Intent(getActivity(), OrderActivity.class);
                    intent.putExtra("tab_p",1);
                    startActivity(intent);
                    break;
                case R.id.ll_mine_see_yifukuan:
                    intent = new Intent(getActivity(), OrderActivity.class);
                    intent.putExtra("tab_p",2);
                    startActivity(intent);
                    break;
                case R.id.ll_mine_see_yiwancheng:
                    intent = new Intent(getActivity(), OrderActivity.class);
                    intent.putExtra("tab_p",3);
                    startActivity(intent);
                    break;
                case R.id.ll_mine_see_shouhou:
                    intent = new Intent(getActivity(), OrderActivity.class);
                    intent.putExtra("tab_p",4);
                    startActivity(intent);
                    break;
            }
        }
    }
}
