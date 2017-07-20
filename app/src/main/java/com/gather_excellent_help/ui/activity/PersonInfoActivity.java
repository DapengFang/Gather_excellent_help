package com.gather_excellent_help.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.UserinfoBean;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.CircularImage;
import com.gather_excellent_help.utils.CacheUtils;
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

public class PersonInfoActivity extends BaseActivity {

    @Bind(R.id.rl_person_exit)
    RelativeLayout rlPersonExit;
    @Bind(R.id.tv_person_infoset_save)
    TextView tvPersonInfosetSave;
    @Bind(R.id.civ_person_head)
    CircularImage civPersonHead;
    @Bind(R.id.tv_person_phone)
    TextView tvPersonPhone;
    @Bind(R.id.tv_person_sex)
    TextView tvPersonSex;
    @Bind(R.id.rl_person_sex)
    RelativeLayout rlPersonSex;
    @Bind(R.id.usertype)
    TextView usertype;
    @Bind(R.id.rl_person_usertype)
    RelativeLayout rlPersonUsertype;
    @Bind(R.id.iv_right2)
    ImageView ivRight2;
    @Bind(R.id.rl_person_address)
    RelativeLayout rlPersonAddress;

    private NetUtil netUtils;
    private Map<String,String> map;
    private String url = Url.BASE_URL + "UserInfo.aspx";
    private String userLogin;//用户登录后的标识
    private ImageLoader mImageLoader;//图片加载类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        netUtils = new NetUtil();
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        getUserInfo();
        netUtils.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {
                LogUtil.e(call.toString()+","+e.getMessage());
            }
        });
        rlPersonExit.setOnClickListener(new MyOnclickListener());
    }

    public class MyOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case  R.id.rl_person_exit:
                    finish();
                    break;
            }
        }
    }

    /**
     * 解析用户信息
     * @param response
     */
    private void parseData(String response) {
        Gson gson = new Gson();
        UserinfoBean userinfoBean = gson.fromJson(response, UserinfoBean.class);
        int statusCode = userinfoBean.getStatusCode();
        List<UserinfoBean.DataBean> data = userinfoBean.getData();
        switch (statusCode) {
            case 1 :
                loadUserInfo(data);
                break;
            case 0:
                Toast.makeText(PersonInfoActivity.this, "修改失败！"+userinfoBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 展示用户信息
     * @param data
     */
    private void loadUserInfo(List<UserinfoBean.DataBean> data) {
        UserinfoBean.DataBean dataBean = data.get(0);
        String avatar = dataBean.getAvatar();
        String group = dataBean.getGroup();
        String mobile = dataBean.getMobile();
        String nick_name = dataBean.getNick_name();
        String sex = dataBean.getSex();
        if(avatar!=null && !avatar.equals("")) {
            mImageLoader.loadImage(avatar,civPersonHead,true);
        }
        if(mobile!=null && !mobile.equals("")) {
            tvPersonPhone.setText(mobile);
        }
        if(sex!=null && !sex.equals("")) {
           tvPersonSex.setText(sex);
        }
        if(group!=null && !group.equals("")) {
            usertype.setText(group);
        }
    }

    /**
     * 获取用户的登录信息
     */
    private void getUserInfo() {
        if(Tools.isLogin(this)) {
            userLogin = Tools.getUserLogin(this);
            map= new HashMap<>();
            map.put("Id",userLogin);
            netUtils.okHttp2Server2(url,map);
        }
    }


}
