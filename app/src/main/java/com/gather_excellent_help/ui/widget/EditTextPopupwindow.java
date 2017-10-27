package com.gather_excellent_help.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.utils.ScreenUtil;


/**
 * 作者：Dapeng Fang on 2016/12/29 10:22
 * <p/>
 * 邮箱：fdp111888@163.com
 */

public class EditTextPopupwindow extends PopupWindow {

    private Context context;
    private LayoutInflater inflater;
    private TextView tv_train_dynamic_content;
    private String content;

    public EditTextPopupwindow(Context context,TextView tv_train_dynamic_content){
        super(context);
        setFocusable(true);
        this.context = context;
        this.tv_train_dynamic_content = tv_train_dynamic_content;
        inflater =LayoutInflater.from(context);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        View view= inflater.inflate(R.layout.item_edittext_popupwindow,null);
        final EditText et_edittext_pop = (EditText) view.findViewById(R.id.et_edittext_pop);
        TextView tv_popup_submit = (TextView) view.findViewById(R.id.tv_popup_submit);
        TextView tv_popup_cancel = (TextView) view.findViewById(R.id.tv_popup_cancel);
        String callmsg = tv_train_dynamic_content.getText().toString().trim();
        if(!callmsg.contains("说点什么吧")) {
            et_edittext_pop.setText(callmsg);
        }
        et_edittext_pop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               if(editable.length()==60) {
                   Toast.makeText(context, "内容字数不超过15个字", Toast.LENGTH_SHORT).show();
                   content = editable.toString();
               }
            }
        });
        tv_popup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cont = et_edittext_pop.getText().toString().trim();
                if(!TextUtils.isEmpty(cont)) {
                    tv_train_dynamic_content.setText(cont);
                }else{
                    tv_train_dynamic_content.setText("说点什么吧！");
                }
                if(isShowing()) {
                    dismiss();
                }
            }
        });
        tv_popup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShowing()) {
                    dismiss();
                }
            }
        });
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ScreenUtil.getScreenHeight(context)/2);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(Color.WHITE);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }
}
