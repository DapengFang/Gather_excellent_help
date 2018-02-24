package com.gather_excellent_help.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.soutao.SouTaobaoHelpBean;
import com.gather_excellent_help.ui.activity.taosearch.TaoSearchActivity;
import com.gather_excellent_help.ui.adapter.soutao.SouTaobaoHelpAdapter;
import com.gather_excellent_help.ui.base.LazyLoadFragment;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2018/1/24.
 */

public class TaobaoUpdateFragment extends LazyLoadFragment {

    private RecyclerView rcv_sou_taobao_help;
    private List<SouTaobaoHelpBean> data;
    private EditText et_test_search_content;
    private TextView tv_test_tao_search;
    private NetUtil netUtil;
    private Map<String, String> map;
    private String taobao_url = Url.BASE_URL + "suning/submit_help_tool.ashx?action=GetSearchTBJC";
    private String channel_id = "11";
    private String category_id = "216";
    private String userLogin;


    @Override
    protected View initView() {
        View inflate = View.inflate(getContext(), R.layout.activity_test_l03, null);
        rcv_sou_taobao_help = (RecyclerView) inflate.findViewById(R.id.rcv_sou_taobao_help);
        et_test_search_content = (EditText) inflate.findViewById(R.id.et_test_search_content);
        tv_test_tao_search = (TextView) inflate.findViewById(R.id.tv_test_tao_search);
        return inflate;
    }

    @Override
    protected void initData() {
        netUtil = new NetUtil();
        userLogin = Tools.getUserLogin(getContext());
        OnServerResponseListener onServerResponseListener = new OnServerResponseListener();
        netUtil.setOnServerResponseListener(onServerResponseListener);
        loadTaoData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcv_sou_taobao_help.setLayoutManager(layoutManager);
        data = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            SouTaobaoHelpBean souTaobaoHelpBean = new SouTaobaoHelpBean();
            souTaobaoHelpBean.setF_title("搜淘宝有什么作用");
            souTaobaoHelpBean.setS_title("您平时在淘宝、天猫、天猫超市购物时，均可搜标题拿到福利");
            data.add(souTaobaoHelpBean);
        }
        SouTaobaoHelpAdapter souTaobaoHelpAdapter = new SouTaobaoHelpAdapter(getContext(), data);
        rcv_sou_taobao_help.setAdapter(souTaobaoHelpAdapter);
        MyonclickListener myonclickListener = new MyonclickListener();
        tv_test_tao_search.setOnClickListener(myonclickListener);
    }

    /**
     * 获取淘宝图文数据
     */
    private void loadTaoData() {
        map = new HashMap();
        map.put("channel_id", channel_id);
        map.put("category_id", category_id);
        netUtil.okHttp2Server2(getContext(), taobao_url, map);
    }

    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_test_tao_search:
                    toSearchTaoWare();
                    break;
            }
        }
    }

    /**
     * 搜索淘宝商品
     */
    private void toSearchTaoWare() {
        String search_words = et_test_search_content.getText().toString().trim();
        Intent intent = new Intent(getContext(), TaoSearchActivity.class);
        intent.putExtra("search_words", search_words);
        startActivity(intent);
    }

    /**
     * 联网监听的回调
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            parseTaobaoData(response);
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
            EncryptNetUtil.startNeterrorPage(getContext());
        }
    }

    /**
     * 解析淘宝的数据
     */
    private void parseTaobaoData(String response) {
        LogUtil.e("淘宝图文数据 = " + response);
    }
}
