package com.gather_excellent_help.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.TypeNavigatorBean;
import com.gather_excellent_help.ui.adapter.TypeWareAdapter;
import com.gather_excellent_help.ui.base.BaseFragment;
import com.gather_excellent_help.ui.widget.Classes;
import com.gather_excellent_help.ui.widget.College;
import com.gather_excellent_help.ui.widget.SimpleExpandableListViewAdapter;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;


/**
 * created by Dapeng Fang
 */
public class TypeFragment extends BaseFragment {
    @Bind(R.id.iv_zhuangtai_exit)
    ImageView ivZhuangtaiExit;
    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.et_sousuo)
    EditText etSousuo;
    @Bind(R.id.cus_list_navigator)
    ExpandableListView cusListNavigator;
    @Bind(R.id.rcv_type_show)
    RecyclerView rcvTypeShow;
    private ArrayList<College> flists;

    private NetUtil netUtil;
    private String url = Url.BASE_URL + "CategoryList.aspx";
    private TypeNavigatorBean.DataBean.SubListBean subListBean;
    private TypeNavigatorBean.DataBean.SubListBean.ThirdListBean thirdListBean;

    /**
     * @return 布局对象
     */
    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.type_fragment, null);
        return inflate;
    }

    /**
     * 数据初始化
     */
    @Override
    public void initData() {
        netUtil = new NetUtil();
        tvTopTitleName.setText("商品分类");
        getNavigatorData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvTypeShow.setLayoutManager(layoutManager);
        TypeWareAdapter typeWareAdapter = new TypeWareAdapter(getContext());
        rcvTypeShow.setAdapter(typeWareAdapter);
    }

    /**
     * 获取左边导航数据
     */
    private void getNavigatorData() {

//        flists = new ArrayList<>();
//        for (int i=0;i<10;i++){
//            College college = new College();
//            college.name = "电视";
//            List<Classes> slists = new ArrayList<>();
//            if(i==3) {
//                for (int z= 1;z<4;z++){
//                    Classes classes = new Classes();
//                    classes.name = "5"+i+"寸";
//                    List<String> tlists = new ArrayList<>();
//                    classes.students =tlists;
//                    slists.add(classes);
//                }
//            }else{
//                for(int j = 1 ;j<3;j++) {
//                    Classes classes = new Classes();
//                    classes.name = "5"+i+"寸";
//                    List<String> tlists = new ArrayList<>();
//                    tlists.add("64核");
//                    tlists.add("HDR");
//                    classes.students = tlists;
//                    slists.add(classes);
//                }
//            }
//            college.classList = slists;
//            flists.add(college);
//        }

        netUtil.okHttp2Server2(url,null);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                parseData(response);
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });

    }

    /**
     * 解析一级索引的数据
     * @param response
     */
    private void parseData(String response) {
        TypeNavigatorBean navigatorData = new Gson().fromJson(response, TypeNavigatorBean.class);
        int statusCode = navigatorData.getStatusCode();
        switch (statusCode) {
            case 1 :
                final List<TypeNavigatorBean.DataBean> data = navigatorData.getData();
                final SimpleExpandableListViewAdapter adapter = new SimpleExpandableListViewAdapter(data,getActivity());
                // 设置适配器
                cusListNavigator.setAdapter(adapter);
                cusListNavigator.setGroupIndicator(null);
                adapter.setOnExpandableClickListener(new SimpleExpandableListViewAdapter.OnExpandableClickListener() {
                    @Override
                    public void onSecondItemClick(int position, int groupPisition) {
                        Toast.makeText(getActivity(), position+"=="+groupPisition, Toast.LENGTH_SHORT).show();
                        subListBean = data.get(position).getSubList().get(groupPisition);
                    }

                    @Override
                    public void onThirdItemClick(int position, int groupPisition, int childPosition) {
                        Toast.makeText(getActivity(), position+"=="+groupPisition+"=="+childPosition, Toast.LENGTH_SHORT).show();
                        thirdListBean = data.get(position).getSubList().get(groupPisition).getThreeList().get(childPosition);
                    }
                });
                break;
            case 0:

                break;
        }

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
}
