package com.gather_excellent_help.ui.activity.suning;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.base.BaseActivity;

public class InvoiceChoiceActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;
    private TextView tv_invoice_type_o;
    private TextView tv_invoice_type_t;
    private TextView tv_invoice_edit_o;
    private TextView tv_invoice_edit_t;
    private RelativeLayout rl_invoice_hind_one;
    private RelativeLayout rl_invoice_hind_two;
    private TextView tv_suning_invoice_submit;

    private EditText et_invoice_name;
    private EditText et_invoice_tax_code;

    private int invoice_type;//发票类型
    private int invoice_title_type; //发票抬头类型
    private String title = "";//发票抬头
    private String tax_no = "";//纳税人识别号


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_choice);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
        tv_invoice_type_o = (TextView) findViewById(R.id.tv_invoice_type_o);
        tv_invoice_type_t = (TextView) findViewById(R.id.tv_invoice_type_t);
        tv_invoice_edit_o = (TextView) findViewById(R.id.tv_invoice_edit_o);
        tv_invoice_edit_t = (TextView) findViewById(R.id.tv_invoice_edit_t);
        rl_invoice_hind_one = (RelativeLayout) findViewById(R.id.rl_invoice_hind_one);
        rl_invoice_hind_two = (RelativeLayout) findViewById(R.id.rl_invoice_hind_two);
        tv_suning_invoice_submit = (TextView) findViewById(R.id.tv_suning_invoice_submit);
        et_invoice_name = (EditText) findViewById(R.id.et_invoice_name);
        et_invoice_tax_code = (EditText) findViewById(R.id.et_invoice_tax_code);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tv_top_title_name.setText("发票信息");
        defaultSetShow();
        Intent intent = getIntent();
        String invoice_type = intent.getStringExtra("invoice_type");
        String invoice_title = intent.getStringExtra("invoice_title");
        String invoice_taxno = intent.getStringExtra("invoice_taxno");
        et_invoice_name.setText(invoice_title);
        et_invoice_tax_code.setText(invoice_taxno);
        if(invoice_type.equals("1")) {
            this.invoice_type = 2;
            tv_invoice_type_o.setSelected(true);
            tv_invoice_type_t.setSelected(false);
            rl_invoice_hind_one.setVisibility(View.VISIBLE);
            if(TextUtils.isEmpty(invoice_taxno)) {
                tv_invoice_edit_o.setSelected(true);
                tv_invoice_edit_t.setSelected(false);
                rl_invoice_hind_two.setVisibility(View.GONE);
                this.invoice_title_type = 1;
            }else{
                tv_invoice_edit_o.setSelected(false);
                tv_invoice_edit_t.setSelected(true);
                rl_invoice_hind_two.setVisibility(View.VISIBLE);
                this.invoice_title_type = 2;
            }
        }else if(invoice_type.equals("0")) {
            defaultSetShow();
        }
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        tv_invoice_edit_o.setOnClickListener(myonclickListener);
        tv_invoice_edit_t.setOnClickListener(myonclickListener);
        tv_invoice_type_o.setOnClickListener(myonclickListener);
        tv_invoice_type_t.setOnClickListener(myonclickListener);
        tv_suning_invoice_submit.setOnClickListener(myonclickListener);
    }

    /**
     * 初始化设置
     */
    private void defaultSetShow() {
        tv_invoice_type_o.setSelected(false);
        tv_invoice_type_t.setSelected(true);
        tv_invoice_edit_o.setSelected(true);
        tv_invoice_edit_t.setSelected(false);
        rl_invoice_hind_one.setVisibility(View.GONE);
        rl_invoice_hind_two.setVisibility(View.GONE);
        invoice_type = 2;
        invoice_title_type = 1;
    }

    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_invoice_type_o:
                    tv_invoice_type_o.setSelected(true);
                    tv_invoice_type_t.setSelected(false);
                    tv_invoice_edit_o.setSelected(true);
                    tv_invoice_edit_t.setSelected(false);
                    rl_invoice_hind_one.setVisibility(View.VISIBLE);
                    rl_invoice_hind_two.setVisibility(View.GONE);
                    invoice_type = 1;
                    invoice_title_type = 1;
                    break;
                case R.id.tv_invoice_type_t:
                    tv_invoice_type_o.setSelected(false);
                    tv_invoice_type_t.setSelected(true);
                    tv_invoice_edit_o.setSelected(true);
                    tv_invoice_edit_t.setSelected(false);
                    rl_invoice_hind_one.setVisibility(View.GONE);
                    rl_invoice_hind_two.setVisibility(View.GONE);
                    invoice_type = 2;
                    invoice_title_type = 1;
                    break;
                case R.id.tv_invoice_edit_o:
                    tv_invoice_edit_o.setSelected(true);
                    tv_invoice_edit_t.setSelected(false);
                    rl_invoice_hind_two.setVisibility(View.GONE);
                    invoice_type = 1;
                    invoice_title_type = 1;
                    et_invoice_tax_code.setText("");
                    break;
                case R.id.tv_invoice_edit_t:
                    tv_invoice_edit_o.setSelected(false);
                    tv_invoice_edit_t.setSelected(true);
                    rl_invoice_hind_two.setVisibility(View.VISIBLE);
                    invoice_type = 1;
                    invoice_title_type = 2;
                    et_invoice_tax_code.setText("");
                    break;
                case R.id.tv_suning_invoice_submit:
                    backInvoiceInfo();
                    break;
            }
        }
    }

    private void backInvoiceInfo() {
        title = et_invoice_name.getText().toString().trim();
        tax_no = et_invoice_tax_code.getText().toString().trim();
        int resultCode = 2;
        Intent intent = new Intent();

        if (invoice_type == 1) {
            if (TextUtils.isEmpty(title)) {
                Toast.makeText(InvoiceChoiceActivity.this, "请您填写发票抬头信息！！！", Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra("invoice_title", title);
            intent.putExtra("invoice_tax_no", tax_no);
        }
        intent.putExtra("invoice_type", invoice_type);
        setResult(resultCode, intent);
        finish();
    }
}
