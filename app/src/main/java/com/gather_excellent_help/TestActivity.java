package com.gather_excellent_help;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.DividerItemDecoration;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.update.HomeActivityAdapter;
import com.gather_excellent_help.update.HomeActivityListAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestActivity extends BaseActivity {

    @Bind(R.id.rcv_home_activity)
    RecyclerView rcvHomeActivity;
    @Bind(R.id.rcv_home_activity_list)
    RecyclerView rcvHomeActivityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        rcvHomeActivityList.setLayoutManager(fullyLinearLayoutManager);
        rcvHomeActivityList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
//        HomeActivityListAdapter homeActivityListAdapter = new HomeActivityListAdapter(this);
//        rcvHomeActivityList.setAdapter(homeActivityListAdapter);
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        rcvHomeActivity.setLayoutManager(layoutManager);
//        HomeActivityAdapter homeActivityAdapter = new HomeActivityAdapter(this, rushData);
//        rcvHomeActivity.setAdapter(homeActivityAdapter);
    }
}
