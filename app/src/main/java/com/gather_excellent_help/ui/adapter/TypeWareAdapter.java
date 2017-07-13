package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;

import com.gather_excellent_help.R;

import java.util.zip.Inflater;

/**
 * Created by wuxin on 2017/7/13.
 */

public class TypeWareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    public TypeWareAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(parent.getContext(), R.layout.item_type_ware,null);
        return new TypeWareViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 12;
    }

    public class TypeWareViewHolder extends RecyclerView.ViewHolder{

        public TypeWareViewHolder(View itemView) {
            super(itemView);
        }
    }
}
