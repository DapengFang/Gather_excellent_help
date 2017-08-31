package com.gather_excellent_help.ui.activity.credits;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.InviteFriendBean;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.activity.WebRecordActivity;
import com.gather_excellent_help.ui.activity.wards.SeeWardsActivity;
import com.gather_excellent_help.ui.activity.wards.WardsStatisticsActivity;
import com.gather_excellent_help.ui.activity.wards.WardsStatisticsUpdateActivity;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class InviteFriendsActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.ll_invite_firend)
    LinearLayout llInviteFirend;
    @Bind(R.id.ll_see_ward)
    LinearLayout llSeeWard;
    @Bind(R.id.ll_ward_statistics)
    LinearLayout llWardStatistics;

    private String invite_url = Url.BASE_URL + "ShareApp.aspx";
    private NetUtil netUtil;
    private Map<String,String> map;
    private String share_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 数据初始化
     */
    private void initData() {
        netUtil = new NetUtil();
        tvTopTitleName.setText("邀请好友");
        map = new HashMap<>();
        String userLogin = Tools.getUserLogin(this);
        if(TextUtils.isEmpty(userLogin)) {
            toLogin();
            return;
        }
        map.put("id",userLogin);
        netUtil.okHttp2Server2(invite_url,map);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });
        rlExit.setOnClickListener(new MyOnClickListener());
        llInviteFirend.setOnClickListener(new MyOnClickListener());
        llSeeWard.setOnClickListener(new MyOnClickListener());
        llWardStatistics.setOnClickListener(new MyOnClickListener());
    }

    /**
     * 登录
     */
    private void toLogin() {
        Intent intent = new Intent(InviteFriendsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 解析数据
     * @param response
     */
    private void parseData(String response) {
        InviteFriendBean inviteFriendBean = new Gson().fromJson(response, InviteFriendBean.class);
        int statusCode = inviteFriendBean.getStatusCode();
        switch (statusCode) {
            case 1 :
                List<InviteFriendBean.DataBean> data = inviteFriendBean.getData();
                if(data.size()>0) {
                    share_url = data.get(0).getShare_url();
                }
                break;
            case 0:
                Toast.makeText(InviteFriendsActivity.this, inviteFriendBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.ll_invite_firend:
                    showWeixin();
                    break;
                case R.id.ll_see_ward:
                    toSeeWards();
                    break;
                case R.id.ll_ward_statistics:
                    toStatisticsWards();
                    break;
            }
        }
    }

    /**
     * 跳转到查看奖励界面
     */
    private void toSeeWards() {
        Intent intent = new Intent(InviteFriendsActivity.this, SeeWardsActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到奖励统计
     */
    private void toStatisticsWards() {
        Intent intent = new Intent(InviteFriendsActivity.this, WardsStatisticsUpdateActivity.class);
        startActivity(intent);
    }

    /**
     * 打开微信邀请好友
     */
    private void showWeixin() {
        if(share_url!=null) {
            String share_content = "我邀请你一起来聚优帮耍，快点打开"+share_url+"和我一起玩转聚优帮吧。";
            new ShareAction(InviteFriendsActivity.this)
                    .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                    .withText(share_content)//分享内容
                    .setCallback(shareListener)//回调监听器
                    .share();
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(InviteFriendsActivity.this,"成功了",Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(InviteFriendsActivity.this,"失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(InviteFriendsActivity.this,"取消了",Toast.LENGTH_LONG).show();

        }
    };

}
