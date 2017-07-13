package com.gather_excellent_help.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.adapter.TypeWareAdapter;
import com.gather_excellent_help.ui.base.BaseFragment;
import com.gather_excellent_help.ui.widget.Classes;
import com.gather_excellent_help.ui.widget.College;
import com.gather_excellent_help.ui.widget.CustomExpandableListView;
import com.gather_excellent_help.ui.widget.SimpleExpandableListViewAdapter;
import com.gather_excellent_help.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wuxin on 2017/7/7.
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
        tvTopTitleName.setText("商品分类");
        getNavigatorData();
        SimpleExpandableListViewAdapter adapter = new SimpleExpandableListViewAdapter(flists,getActivity());
        // 设置适配器
        cusListNavigator.setAdapter(adapter);
        cusListNavigator.setGroupIndicator(null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvTypeShow.setLayoutManager(layoutManager);
        TypeWareAdapter typeWareAdapter = new TypeWareAdapter(getContext());
        rcvTypeShow.setAdapter(typeWareAdapter);
    }

    /**
     * 获取左边导航数据
     */
    private void getNavigatorData() {

        flists = new ArrayList<>();
        for (int i=0;i<10;i++){
            College college = new College();
            college.name = "电视";
            List<Classes> slists = new ArrayList<>();
            for(int j = 1 ;j<3;j++) {
                Classes classes = new Classes();
                classes.name = "5"+i+"寸";
                List<String> tlists = new ArrayList<>();
                tlists.add("64核");
                tlists.add("HDR");
                classes.students = tlists;
                slists.add(classes);
            }
            college.classList = slists;
            flists.add(college);
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
