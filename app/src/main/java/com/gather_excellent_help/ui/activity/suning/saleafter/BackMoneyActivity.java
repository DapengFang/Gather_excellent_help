package com.gather_excellent_help.ui.activity.suning.saleafter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.sale.TuihuoBean;
import com.gather_excellent_help.ui.activity.SetActivity;
import com.gather_excellent_help.ui.adapter.sale.TuihuanAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.TuihuoMenuPopWindow;
import com.gather_excellent_help.utils.Check;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.ScreenUtil;
import com.gather_excellent_help.utils.Tools;
import com.umeng.socialize.media.Base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class BackMoneyActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;
    private RelativeLayout rl_tuihuo_reason;
    private RelativeLayout rl_tuihuo_explain;
    private TextView tv_tuihuo_choice_reason;
    private TextView tv_tuihuo_more_content;

    private TextView tv_back_money_commit;

    private LayoutInflater layoutInflater;
    //private String[] reasons = {"收到商品破损", "商品错发、漏发", "商品需要维修", "发票问题", "收到商品与描述不符", "商品质量问题", "未按约定时间发货", "其他"};
    private String[] reasons = {"缺货", "排错了/订单信息有误", "不想要了", "其他"};
    private String explain = "";//退款说明
    private String reason = "";//退款原因
    private AlertDialog alertDialog;

    private NetUtil netUtil;
    private Map<String, String> map;
    private String apply_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=ApplyRejectedOfSN";
    private String user_id = "";
    private String order_id = "";//订单id
    private String article_id = "";   //产品表自增id
    private String apply_reason = "";   //退货理由

    private String ware_img;
    private String ware_title;
    private String spec_text;
    private String real_price;
    private String goods_price;
    private String quantity;
    private ImageView iv_suning_order_ware;
    private TextView tv_suning_order_title;
    private TextView tv_suning_order_type;
    private TextView tv_suning_order_realprice;
    private TextView tv_suning_order_oldprice;
    private TextView tv_suning_order_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_money);
        layoutInflater = LayoutInflater.from(this);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);
        rl_tuihuo_reason = (RelativeLayout) findViewById(R.id.rl_tuihuo_reason);
        rl_tuihuo_explain = (RelativeLayout) findViewById(R.id.rl_tuihuo_explain);
        tv_tuihuo_choice_reason = (TextView) findViewById(R.id.tv_tuihuo_choice_reason);
        tv_tuihuo_more_content = (TextView) findViewById(R.id.tv_tuihuo_more_content);
        tv_back_money_commit = (TextView) findViewById(R.id.tv_back_money_commit);

        iv_suning_order_ware = (ImageView) findViewById(R.id.iv_suning_order_ware);
        tv_suning_order_title = (TextView) findViewById(R.id.tv_suning_order_title);
        tv_suning_order_type = (TextView) findViewById(R.id.tv_suning_order_type);
        tv_suning_order_realprice = (TextView) findViewById(R.id.tv_suning_order_realprice);
        tv_suning_order_oldprice = (TextView) findViewById(R.id.tv_suning_order_oldprice);
        tv_suning_order_number = (TextView) findViewById(R.id.tv_suning_order_number);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        user_id = Tools.getUserLogin(this);
        netUtil = new NetUtil();
        tv_top_title_name.setText("申请退款");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ware_img = bundle.getString("ware_img");
        ware_title = bundle.getString("ware_title");
        spec_text = bundle.getString("spec_text");
        real_price = bundle.getString("real_price");
        goods_price = bundle.getString("goods_price");
        quantity = bundle.getString("quantity");
        article_id = bundle.getString("article_id");
        order_id = bundle.getString("order_id");
        LogUtil.e(article_id + "-" +order_id);
        wareDataShow();

        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        rl_tuihuo_reason.setOnClickListener(myonclickListener);
        rl_tuihuo_explain.setOnClickListener(myonclickListener);
        tv_back_money_commit.setOnClickListener(myonclickListener);
        OnServerResponseListener onServerResponseListener = new OnServerResponseListener();
        netUtil.setOnServerResponseListener(onServerResponseListener);
    }

    /**
     * 显示商品信息
     */
    private void wareDataShow() {
        String img_url = ware_img.replace("800x800", "400x400");
        Glide.with(BackMoneyActivity.this).load(img_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                .into(iv_suning_order_ware);//请求成功后把图片设置到的控件

        if (spec_text != null) {
            tv_suning_order_type.setText(spec_text);
        }

        if (ware_title != null) {
            tv_suning_order_title.setText(ware_title);
        }
        tv_suning_order_realprice.setText("￥" + real_price);
        tv_suning_order_oldprice.getPaint().setAntiAlias(true);
        tv_suning_order_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tv_suning_order_oldprice.setText("￥" + goods_price);
        tv_suning_order_number.setText("x" + quantity);
    }

    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.rl_tuihuo_reason:
                    showTuihuoReason();
                    break;
                case R.id.rl_tuihuo_explain:
                    showExplainDialog();
                    break;
                case R.id.tv_back_money_commit:
                    applyBackMoney();
                    break;
            }
        }
    }

    /**
     * 申请退款n
     */
    private void applyBackMoney() {
        if (TextUtils.isEmpty(reason) || reason.equals("请选择退货原因")) {
            Toast.makeText(BackMoneyActivity.this, "请选择退货原因", Toast.LENGTH_SHORT).show();
            return;
        }
        if (explain.equals("选填")) {
            explain = "";
        }
        apply_reason = reason + "a" + explain;
        map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("order_id", order_id);
        map.put("article_id", article_id);
        map.put("apply_reason", apply_reason);
        netUtil.okHttp2Server2(apply_url, map);
    }

    /**
     * 退货原因
     */
    private void showTuihuoReason() {
        final ArrayList<TuihuoBean> datas = new ArrayList<>();
        for (int i = 0; i < reasons.length; i++) {
            TuihuoBean tuihuoBean = new TuihuoBean();
            tuihuoBean.setTextItem(reasons[i]);
            datas.add(tuihuoBean);
        }
        View inflate = layoutInflater.inflate(R.layout.pop_tuihuan_show, null, false);
        ListView lv_tuihuanhuo = (ListView) inflate.findViewById(R.id.lv_tuihuanhuo);
        TuihuanAdapter tuihuanAdapter = new TuihuanAdapter(this, datas);
        lv_tuihuanhuo.setAdapter(tuihuanAdapter);
        final TuihuoMenuPopWindow tuihuoMenuPopWindow = new TuihuoMenuPopWindow(this, inflate);
        tuihuoMenuPopWindow.showAsDropDown(rl_tuihuo_reason, ScreenUtil.getScreenWidth(this) / 6, 5);
        lv_tuihuanhuo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(BackMoneyActivity.this, "" + datas.get(i).getTextItem(), Toast.LENGTH_SHORT).show();
                tuihuoMenuPopWindow.dismiss();
                tv_tuihuo_choice_reason.setText(datas.get(i).getTextItem());
                reason = datas.get(i).getTextItem();
            }
        });
    }

    /**
     * 退款说明的dialog
     */
    private void showExplainDialog() {
        View inflate = layoutInflater.inflate(R.layout.item_back_sale_detail, null, false);
        final EditText et_sale_detail = (EditText) inflate.findViewById(R.id.et_sale_detail);
        TextView tv_back_sale_cancel = (TextView) inflate.findViewById(R.id.tv_back_sale_cancel);
        TextView tv_back_sale_confirm = (TextView) inflate.findViewById(R.id.tv_back_sale_confirm);
        et_sale_detail.setSingleLine(false);
        et_sale_detail.setHorizontallyScrolling(false);
        et_sale_detail.setText(explain);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.setView(inflate).create();
        if (BackMoneyActivity.this != null && !BackMoneyActivity.this.isFinishing()) {
            alertDialog.show();
        }
        tv_back_sale_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        });
        tv_back_sale_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = et_sale_detail.getText().toString().trim();
                explain = content;
                if (TextUtils.isEmpty(explain)) {
                    tv_tuihuo_more_content.setText("选填");
                } else {
                    tv_tuihuo_more_content.setText(content);
                }
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }

        });
    }

    /**
     * 联网请求的回调
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            LogUtil.e(response);
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
        }
    }
}
