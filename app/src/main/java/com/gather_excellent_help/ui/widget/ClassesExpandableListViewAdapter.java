package com.gather_excellent_help.ui.widget;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.TypeNavigatorBean;
import com.gather_excellent_help.utils.DensityUtil;

import java.util.List;

/**
 *
 * 班级的适配器
 * Created by MH on 2016/6/16.
 */
public class ClassesExpandableListViewAdapter extends BaseExpandableListAdapter {


    // 班级的集合
    private List<TypeNavigatorBean.DataBean.SubListBean> classes;

    // 创建布局使用
    private Activity activity;

    private OnSecondListClickListener onSecondListClickListener;


    public ClassesExpandableListViewAdapter(List<TypeNavigatorBean.DataBean.SubListBean> classes, Activity activity) {
        this.classes = classes;
        this.activity = activity;
    }

    @Override
    public int getGroupCount() {
        // 获取一级条目的数量  就是班级的大小
        return null == classes? 0 : classes.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // 获取对应一级条目下二级条目的数量，就是各个班学生的数量
        return null == classes.get(groupPosition).getThreeList() ? 0:classes.get(groupPosition).getThreeList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // 获取一级条目的对应数据  ，感觉没什么用
        return null == classes? null:classes.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // 获取对应一级条目下二级条目的对应数据  感觉没什么用
        return null == classes.get(groupPosition).getThreeList() ? null : classes.get(groupPosition).getThreeList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // 直接返回，没什么用
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // 直接返回，没什么用
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // 谁知道这个是干什么。。。。
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        // 获取对应一级条目的View  和ListView 的getView相似

        return getGenericView(classes.get(groupPosition).getTitle(),isExpanded,groupPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        // 获取对应二级条目的View  和ListView 的getView相似
        return getGenericChildView(classes.get(groupPosition).getThreeList().get(childPosition).getTitle(),groupPosition,childPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // 根据方法名，此处应该表示二级条目是否可以被点击   先返回true 再讲
        return true;
    }


    /**
     * 根据字符串生成布局，，因为我没有写layout.xml 所以用java 代码生成
     *
     *      实际中可以通过Inflate加载布局文件并返回
     * @param string
     * @param isExpanded
     * @param groupPosition
     * @return
     */
    private View getGenericView(String string, boolean isExpanded, final int groupPosition) {

//        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        TextView textView = new TextView(activity);
//        textView.setLayoutParams(layoutParams);
//
//        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
//
//        textView.setPadding(40, 30, 0, 30);
//        textView.setText(string);
//            textView.setTextColor(Color.BLACK);
        View inflate = View.inflate(activity, R.layout.item_type_second_arraw, null);
        ImageView arrawNavigator = (ImageView) inflate.findViewById(R.id.iv_first_arraw);
        TextView tvNavigator = (TextView) inflate.findViewById(R.id.tv_first_title);
        int childrenCount = getChildrenCount(groupPosition);
        if(childrenCount == 0) {
            arrawNavigator.setVisibility(View.GONE);
//            inflate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //二级的点击事件
//                    onSecondListClickListener.onSecondClick(groupPosition);
//                }
//            });
        }else{
            arrawSetDirection(isExpanded, arrawNavigator);
        }
        tvNavigator.setText(string);
        return inflate;
    }

    private void arrawSetDirection(boolean isExpanded, ImageView arrawNavigator) {
        arrawNavigator.setVisibility(View.VISIBLE);
        if(isExpanded) {
            arrawNavigator.setImageResource(R.drawable.down_red_arraw);
        }else{
            arrawNavigator.setImageResource(R.drawable.left_red_type_arraw);
        }
    }

    private TextView getGenericChildView(String string, final int groupPosition, final int childPosition) {

        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textView = new TextView(activity);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setPadding(100, 30, 0, 30);
        textView.setText(string);
        textView.setTextSize(DensityUtil.dip2px(activity,4));
        textView.setTextColor(Color.parseColor("#99000000"));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //3级的点击事件
                onThirdListClickListener.onThirdClick(groupPosition,childPosition);
            }
        });
        return textView;
    }

    public interface OnSecondListClickListener{
        void onSecondClick(int groupPosition);

    }

    private OnThirdListClickListener onThirdListClickListener;

    public interface OnThirdListClickListener{
        void onThirdClick(int groupPosition,int childPosition);
    }

    public void setOnSecondListClickListener(OnSecondListClickListener onSecondListClickListener) {
        this.onSecondListClickListener = onSecondListClickListener;
    }

    public void setOnThirdListClickListener(OnThirdListClickListener onThirdListClickListener) {
        this.onThirdListClickListener = onThirdListClickListener;
    }
}
