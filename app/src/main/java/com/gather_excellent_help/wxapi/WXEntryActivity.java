package com.gather_excellent_help.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.BindPhoneActivity;
import com.gather_excellent_help.ui.activity.LoginActivity;
import com.gather_excellent_help.ui.activity.SetActivity;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * 作者：Dapeng Fang on 2016/10/8 13:52
 * <p/>
 * 邮箱：fdp111888@163.com
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {


    private Context context;
    private IWXAPI api;
    private String WX_APP_ID = "wxc883e0b88fddcc71";
    private String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 获取用户个人信息
    private String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
    private String WX_APP_SECRET = "73657caeee905f216e6228fe3f3ed5e0";

    private NetUtil netUtil;
    private Map<String,String> map;

    private String wex_url = Url.BASE_URL + "WeChatLogin.aspx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        netUtil = new NetUtil();
        netUtil.setOnServerResponseListener(new OnServerResponseListener());
        api = WXAPIFactory.createWXAPI(this, WX_APP_ID, false);
        api.handleIntent(getIntent(), this);
    }


    @Override
    public void onReq(BaseReq baseReq) {
        LogUtil.e("WXEntryActivity = " + baseReq.toString());
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case 0:
                LogUtil.e("用户同意");
                String code = ((SendAuth.Resp) baseResp).code;
                String get_access_token = getCodeRequest(code);
                getAccessToken(get_access_token);
                finish();
                break;
            case -4:
                LogUtil.e("用户拒绝授权");
                finish();
                break;
            case -2:
                LogUtil.e("用户取消");
                finish();
                break;
        }
    }

    /**
     * @param get_access_token 获取accessToken的url
     */
    private void getAccessToken(String get_access_token) {
        OkHttpUtils
                .post()
                .url(get_access_token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.e(call.toString() + "--" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.e(response);
                        AccessTokenBean accessTokenBean = new Gson().fromJson(response, AccessTokenBean.class);
                        String userInfo_url = getUserInfo(accessTokenBean.getAccess_token(), accessTokenBean.getOpenid());
                        getUserInfoDetail(userInfo_url);
                    }
                });
    }


    /**
     * 获取用户个人信息的URL（微信）
     *
     * @param access_token 获取access_token时给的
     * @param openid       获取access_token时给的
     * @return URL
     */
    private String getUserInfo(String access_token, String openid) {
        String result = null;
        GetUserInfo = GetUserInfo.replace("ACCESS_TOKEN",
                urlEnodeUTF8(access_token));
        GetUserInfo = GetUserInfo.replace("OPENID", urlEnodeUTF8(openid));
        result = GetUserInfo;
        return result;
    }

    /**
     * 获取access_token的URL（微信）
     *
     * @param code 授权时，微信回调给的
     * @return URL
     */
    private String getCodeRequest(String code) {
        String result = null;
        GetCodeRequest = GetCodeRequest.replace("APPID",
                urlEnodeUTF8(WX_APP_ID));
        GetCodeRequest = GetCodeRequest.replace("SECRET",
                urlEnodeUTF8(WX_APP_SECRET));
        GetCodeRequest = GetCodeRequest.replace("CODE", urlEnodeUTF8(code));
        result = GetCodeRequest;
        return result;

    }


    private String urlEnodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @param userInfo_url 获取微信用户详细信息
     */
    public void getUserInfoDetail(String userInfo_url) {
        OkHttpUtils
                .get()
                .url(userInfo_url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.e(call.toString() + "--" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.e(response);
                        //Toast.makeText(WXEntryActivity.this, response, Toast.LENGTH_SHORT).show();
                        map = new HashMap<>();
                        map.put("wechat_json",response);
                        netUtil.okHttp2Server2(wex_url,map);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class OnServerResponseListener implements NetUtil.OnServerResponseListener{

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
            CodeBean codeBean = new Gson().fromJson(response, CodeBean.class);
            int statusCode = codeBean.getStatusCode();
            switch (statusCode) {
                case 1 :
                    List<CodeBean.DataBean> data = codeBean.getData();
                    if(data.size()>0) {
                        Integer id = data.get(0).getId();
                        int group_type = data.get(0).getGroup_type();
                        double user_rate = data.get(0).getUser_get_ratio();
                        int group_id = data.get(0).getGroup_id();
                        String wechat_id = data.get(0).getWechat_id();
                        CacheUtils.putBoolean(WXEntryActivity.this, CacheUtils.LOGIN_STATE, true);
                        CacheUtils.putString(WXEntryActivity.this, CacheUtils.LOGIN_VALUE, id + "");
                        CacheUtils.putInteger(WXEntryActivity.this, CacheUtils.SHOP_TYPE, group_type);
                        CacheUtils.putString(WXEntryActivity.this, CacheUtils.USER_RATE, user_rate + "");
                        CacheUtils.putInteger(WXEntryActivity.this, CacheUtils.GROUP_TYPE, group_id);
                        Intent intent = new Intent(WXEntryActivity.this, BindPhoneActivity.class);
                        intent.putExtra("wechat_id",wechat_id);
                        startActivity(intent);
                    }
                    break;
                case 0:
                    Toast.makeText(WXEntryActivity.this, codeBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
            finish();
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(e.getMessage());
        }
    }
}
