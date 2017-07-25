package com.gather_excellent_help.ui.activity.credits;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.activity.wards.SeeWardsActivity;
import com.gather_excellent_help.ui.activity.wards.WardsStatisticsActivity;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.wxapi.Constants;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        setContentView(R.layout.activity_invite_friends);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 数据初始化
     */
    private void initData() {
        tvTopTitleName.setText("邀请好友");
        rlExit.setOnClickListener(new MyOnClickListener());
        llInviteFirend.setOnClickListener(new MyOnClickListener());
        llSeeWard.setOnClickListener(new MyOnClickListener());
        llWardStatistics.setOnClickListener(new MyOnClickListener());
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
        Intent intent = new Intent(InviteFriendsActivity.this, WardsStatisticsActivity.class);
        startActivity(intent);
    }

    /**
     * 打开微信邀请好友
     */
    private void showWeixin() {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = "http://www.baidu.com";
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = "";
        msg.description = "";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.weixin_orange_icon);

        msg.thumbData = Tools.bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
