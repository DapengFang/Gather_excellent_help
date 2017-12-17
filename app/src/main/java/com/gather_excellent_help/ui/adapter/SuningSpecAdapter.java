package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.bean.BackRebateBean;
import com.gather_excellent_help.bean.suning.SuningSpecBean;
import com.gather_excellent_help.ui.activity.suning.saleafter.ExchangeGoodsActivity;
import com.gather_excellent_help.ui.widget.FullyLinearLayoutManager;
import com.gather_excellent_help.ui.widget.taggroup.TagGroup;
import com.gather_excellent_help.utils.LogUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagView;

import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dapeng Fang on 2017/7/27.
 */

public class SuningSpecAdapter extends RecyclerView.Adapter<SuningSpecAdapter.SuningSpecViewHolder> {

    private Context context;
    private List<SuningSpecBean.DataBean> data;
    private LayoutInflater mInflater;


    public SuningSpecAdapter(Context context, List<SuningSpecBean.DataBean> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public SuningSpecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_spec_first, null, false);
        return new SuningSpecViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final SuningSpecViewHolder holder, int position) {
        try {
            SuningSpecBean.DataBean dataBean = data.get(position);
            String title = dataBean.getTitle();
            final List<SuningSpecBean.DataBean.ContentBean> content = dataBean.getContent();
            holder.tv_spec_title.setText(title);
            if (content != null && content.size() > 0) {
//                final String[] tags = new String[content.size()];
//                for (int i = 0; i < content.size(); i++) {
//                    SuningSpecBean.DataBean.ContentBean contentBean = content.get(i);
//                    if (contentBean != null) {
//                        String tag = contentBean.getTitle();
//                        tags[i] = tag;
//                    }
//                }
                final TagAdapter mAdapter = new TagAdapter<SuningSpecBean.DataBean.ContentBean>(content) {


                    @Override
                    public View getView(FlowLayout parent, int position, SuningSpecBean.DataBean.ContentBean contentBean) {

                        TextView tv = (TextView) mInflater.inflate(R.layout.item_tag_spec_title,
                                holder.tag_flowlayout, false);
                        boolean check = contentBean.isCheck();
                        String title = contentBean.getTitle();
                        tv.setText(title);
                        return tv;
                    }


                };
                mAdapter.setSelectedList(0);
                holder.tag_flowlayout.setAdapter(mAdapter);
                holder.tag_flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        if (content != null && content.size() > 0) {
                            for (int i = 0; i < content.size(); i++) {
                                if (position != i) {
                                    SuningSpecBean.DataBean.ContentBean contentBean = content.get(i);
                                    boolean check = contentBean.isCheck();
                                    contentBean.setCheck(false);
                                    com.zhy.view.flowlayout.TagView tv = (TagView) holder.tag_flowlayout.getChildAt(i);
                                    tv.setChecked(false);
                                }
                            }
                            SuningSpecBean.DataBean.ContentBean contentBean = content.get(position);
                            contentBean.setCheck(true);
                            com.zhy.view.flowlayout.TagView tv = (TagView) view;
                            tv.setChecked(true);
                        }
                        return true;
                    }
                });

            }
        } catch (Exception e) {
            LogUtil.e("SuningSpecAdapter error");
            Toast.makeText(context, "系统出现故障，请退出后重新尝试！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    public class SuningSpecViewHolder extends RecyclerView.ViewHolder {

        TextView tv_spec_title;
        TagFlowLayout tag_flowlayout;

        public SuningSpecViewHolder(View itemView) {
            super(itemView);
            tv_spec_title = (TextView) itemView.findViewById(R.id.tv_spec_title);
            tag_flowlayout = (TagFlowLayout) itemView.findViewById(R.id.tag_flowlayout);
        }
    }
}
