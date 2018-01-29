package com.gather_excellent_help.ui.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.TypeNavigatorBean;

import java.util.List;

/**
 * 外层ExpandListView 适配器的实现
 * Created by MH on 2016/6/16.
 */
public class SimpleExpandableListViewAdapter extends BaseExpandableListAdapter {


    // 大学的集合
    private List<TypeNavigatorBean.DataBean> colleges;

    private Activity activity;

    private OnExpandableClickListener onExpandableClickListener;


    public SimpleExpandableListViewAdapter(List<TypeNavigatorBean.DataBean> colleges, Activity activity) {
        this.colleges = colleges;
        this.activity = activity;
    }

    @Override
    public int getGroupCount() {
        return null == colleges ? 0 : colleges.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // 很关键，，一定要返回  1
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null == colleges ? null : colleges.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null == colleges.get(groupPosition).getSubList() ? null : colleges.get(groupPosition).getSubList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return getGenericView(colleges.get(groupPosition), colleges.get(groupPosition).getTitle(), isExpanded, groupPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        // 返回子ExpandableListView 的对象  此时传入是该父条目，即大学的对象（有歧义。。）


        return getGenericExpandableListView(colleges.get(groupPosition), groupPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private View getGenericView(TypeNavigatorBean.DataBean dataBean, String string, boolean isExpanded, int groupPosition) {
        View inflate = View.inflate(activity, R.layout.item_type_first_arraw, null);
        ImageView arrawNavigator = (ImageView) inflate.findViewById(R.id.iv_first_arraw);
        TextView tvNavigator = (TextView) inflate.findViewById(R.id.tv_first_title);
        arrawSetDirection(isExpanded, arrawNavigator);
        tvNavigator.setText(string);
        return inflate;
    }

    private void arrawSetDirection(boolean isExpanded, ImageView arrawNavigator) {
        if (isExpanded) {
            arrawNavigator.setImageResource(R.drawable.red_down_arraw_s);
        } else {
            arrawNavigator.setImageResource(R.drawable.gray_right_arraw_s);
        }
    }


    /**
     * 返回子ExpandableListView 的对象  此时传入的是该大学下所有班级的集合。
     *
     * @param college
     * @param position
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public ExpandableListView getGenericExpandableListView(TypeNavigatorBean.DataBean college, final int position) {
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        final CustomExpandableListView view = new CustomExpandableListView(activity);
        view.setDividerHeight(0);
        final ClassesExpandableListViewAdapter adapter = new ClassesExpandableListViewAdapter(college.getSubList(), activity);
        adapter.setOnThirdListClickListener(new ClassesExpandableListViewAdapter.OnThirdListClickListener() {
            @Override
            public void onThirdClick(int groupPosition, int childPosition, TextView textView) {
                onExpandableClickListener.onThirdItemClick(position, groupPosition, childPosition, textView);
            }
        });
        view.setGroupIndicator(null);
        view.setAdapter(adapter);
        view.setPadding(20, 0, 0, 0);
        // 加载班级的适配器
        view.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                for (int c = 0; c < adapter.getGroupCount(); c++) {
                    if (c != i) {
                        view.collapseGroup(c);
                    }
                }
                onExpandableClickListener.onSecondItemClick(position, i);
            }
        });
        return view;
    }

    public interface OnExpandableClickListener {
        void onSecondItemClick(int position, int groupPisition);

        void onThirdItemClick(int position, int groupPisition, int childPosition, TextView tv);
    }

    public void setOnExpandableClickListener(OnExpandableClickListener onExpandableClickListener) {
        this.onExpandableClickListener = onExpandableClickListener;
    }
}
