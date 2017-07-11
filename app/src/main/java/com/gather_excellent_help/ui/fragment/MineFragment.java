package com.gather_excellent_help.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.activity.SetActivity;
import com.gather_excellent_help.ui.base.BaseFragment;
import com.gather_excellent_help.ui.widget.CircularImage;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.mine_fragment, null);
        return inflate;
    }

    @Override
    public void initData() {
        rlMineSet.setOnClickListener(new MyOnClickListener());
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

    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_mine_set :
                    Intent intent = new Intent(getActivity(), SetActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
