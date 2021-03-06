package com.gather_excellent_help.ui.activity.suning;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.sale.ApplyStateBean;
import com.gather_excellent_help.bean.suning.SuningOrderBean;
import com.gather_excellent_help.bean.suning.SuningOrderConfirmBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.suning.saleafter.SaleAfterActivity;
import com.gather_excellent_help.ui.adapter.SuningLogisticsInfoAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;


public class SuningOrderDetailActivity extends BaseActivity {

    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;

    private TextView tv_suning_detail_status;
    private TextView tv_suning_detail_accept;
    private TextView tv_suning_detail_phone;
    private TextView tv_suning_detail_address;
    private LinearLayout ll_suning_detail_container;

    private TextView tv_suning_detail_free;
    private TextView tv_suning_detail_money;
    private TextView tv_suning_detail_orderno;
    private TextView tv_suning_detail_createtime;
    private TextView tv_suning_detail_paytime;
    private TextView tv_suning_detail_sendtime;
    private TextView tv_suning_detail_accepttime;
    private TextView tv_item_detail_right;
    private TextView tv_item_detail_left;
    private TextView tv_item_detail_extra;
    private RelativeLayout rl_suning_order_number;

    private String orderInfo;
    private int status;//当前订单状态
    private AlertDialog alertDialog;

    private NetUtil netUtil;
    private Map<String, String> map;
    private String whick = "";//哪一个
    private String userLogin;

    private String cancel_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=DeletSingerOrder";//取消订单
    private String confrim_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=recive_myProduct";//确认订单
    private String confrim_child_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=recive_mySNProduct";//确认子订单
    private String apply_state_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=GetApplyStatus";//检查申请状态
    private int order_id;//订单id
    private String order_no;
    private double real_amount;
    private SuningOrderBean.DataBean.GoodListBean goodListBean;
    private String goods_title;
    private String spec_text;
    private double real_price;
    private double goods_price;
    private int quantity;
    private int w_order_id;
    private int w_itemId;
    private int w_article_id;
    private int article_id;
    private DecimalFormat df;
    private int goods_id;
    private String productId = "";
    private int count;
    private boolean isContinue;
    private String content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suning_order_detail);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView) findViewById(R.id.tv_top_title_name);

        ll_suning_detail_container = (LinearLayout) findViewById(R.id.ll_suning_detail_container);
        tv_suning_detail_status = (TextView) findViewById(R.id.tv_suning_detail_status);
        tv_suning_detail_accept = (TextView) findViewById(R.id.tv_suning_detail_accept);
        tv_suning_detail_phone = (TextView) findViewById(R.id.tv_suning_detail_phone);
        tv_suning_detail_address = (TextView) findViewById(R.id.tv_suning_detail_address);

        tv_suning_detail_free = (TextView) findViewById(R.id.tv_suning_detail_free);
        tv_suning_detail_money = (TextView) findViewById(R.id.tv_suning_detail_money);
        tv_suning_detail_orderno = (TextView) findViewById(R.id.tv_suning_detail_orderno);
        tv_suning_detail_createtime = (TextView) findViewById(R.id.tv_suning_detail_createtime);
        tv_suning_detail_paytime = (TextView) findViewById(R.id.tv_suning_detail_paytime);
        tv_suning_detail_sendtime = (TextView) findViewById(R.id.tv_suning_detail_sendtime);
        tv_suning_detail_accepttime = (TextView) findViewById(R.id.tv_suning_detail_accepttime);
        tv_item_detail_right = (TextView) findViewById(R.id.tv_item_detail_right);
        tv_item_detail_left = (TextView) findViewById(R.id.tv_item_detail_left);
        tv_item_detail_extra = (TextView) findViewById(R.id.tv_item_detail_extra);
        rl_suning_order_number = (RelativeLayout) findViewById(R.id.rl_suning_order_number);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tv_top_title_name.setText("订单详情");
        userLogin = Tools.getUserLogin(this);
        netUtil = new NetUtil();
        OnServerResponseListener onServerResponseListener = new OnServerResponseListener();
        netUtil.setOnServerResponseListener(onServerResponseListener);
        Intent intent = getIntent();
        orderInfo = intent.getStringExtra("orderInfo");
        getOrderInfo(orderInfo);
        MyonclickListener myonclickListener = new MyonclickListener();
        rl_exit.setOnClickListener(myonclickListener);
        tv_item_detail_left.setOnClickListener(myonclickListener);
        tv_item_detail_right.setOnClickListener(myonclickListener);
        rl_suning_order_number.setOnClickListener(myonclickListener);
    }

    /**
     * 获取订单信息
     *
     * @param orderInfo
     */
    private void getOrderInfo(String orderInfo) {
        if (orderInfo != null) {
            SuningOrderBean.DataBean dataBean = new Gson().fromJson(orderInfo, SuningOrderBean.DataBean.class);
            if (dataBean != null) {
                status = dataBean.getOrder_status();
                order_no = dataBean.getOrder_no();
                order_id = dataBean.getId();
                real_amount = dataBean.getReal_amount();
                df = new DecimalFormat("#0.00");
                showOrderStatus(status);
                showOrderAddress(dataBean);
                showWareInfo(dataBean, df);
                showFreeAndMoney(dataBean, df);
                showOrderTime(dataBean, status);
            }
        }
    }

    /**
     * 显示订单时间
     *
     * @param dataBean
     * @param status
     */
    private void showOrderTime(SuningOrderBean.DataBean dataBean, int status) {

        String add_time = dataBean.getAdd_time();
        String payment_time = dataBean.getPayment_time();
        String express_time = dataBean.getExpress_time();
        String complete_time = dataBean.getComplete_time();
        content = "订单号：" + order_no;
        setTextPartColor(tv_suning_detail_orderno, 4, content);

        if (status == 1) {
            tv_suning_detail_createtime.setVisibility(View.VISIBLE);
            tv_suning_detail_paytime.setVisibility(View.GONE);
            tv_suning_detail_sendtime.setVisibility(View.GONE);
            tv_suning_detail_accepttime.setVisibility(View.GONE);
            if (add_time != null) {
                content = "创建时间：" + add_time;
                setTextPartColor(tv_suning_detail_createtime, 5, content);
            }
            tv_item_detail_extra.setVisibility(View.GONE);
            tv_item_detail_left.setVisibility(View.VISIBLE);
            tv_item_detail_right.setVisibility(View.VISIBLE);
            tv_item_detail_left.setText("取消订单");
            tv_item_detail_right.setText("立即付款");
        } else if (status == 2) {
            tv_suning_detail_createtime.setVisibility(View.VISIBLE);
            tv_suning_detail_paytime.setVisibility(View.VISIBLE);
            tv_suning_detail_sendtime.setVisibility(View.GONE);
            tv_suning_detail_accepttime.setVisibility(View.GONE);
            if (add_time != null) {
                content = "创建时间：" + add_time;
                setTextPartColor(tv_suning_detail_createtime, 5, content);
            }
            if (payment_time != null) {
                content = "付款时间：" + payment_time;
                setTextPartColor(tv_suning_detail_paytime, 5, content);
            }
            tv_item_detail_extra.setVisibility(View.GONE);
            tv_item_detail_left.setVisibility(View.GONE);
            tv_item_detail_right.setVisibility(View.VISIBLE);
            tv_item_detail_right.setText("提醒发货");
        } else if (status == 3) {
            tv_suning_detail_createtime.setVisibility(View.VISIBLE);
            tv_suning_detail_paytime.setVisibility(View.VISIBLE);
            tv_suning_detail_sendtime.setVisibility(View.GONE);
            tv_suning_detail_accepttime.setVisibility(View.GONE);
            if (add_time != null) {
                content = "创建时间：" + add_time;
                setTextPartColor(tv_suning_detail_createtime, 5, content);
            }
            if (payment_time != null) {
                content = "付款时间：" + payment_time;
                setTextPartColor(tv_suning_detail_paytime, 5, content);
            }
            if (express_time != null) {
                content = "发货时间：" + express_time;
                setTextPartColor(tv_suning_detail_sendtime, 5, content);
            }
            tv_item_detail_extra.setVisibility(View.GONE);
            tv_item_detail_left.setVisibility(View.GONE);
            tv_item_detail_right.setVisibility(View.VISIBLE);
            tv_item_detail_right.setText("确认收货");
        } else if (status == 4) {
            tv_suning_detail_createtime.setVisibility(View.VISIBLE);
            tv_suning_detail_paytime.setVisibility(View.VISIBLE);
            tv_suning_detail_sendtime.setVisibility(View.GONE);
            tv_suning_detail_accepttime.setVisibility(View.VISIBLE);
            if (add_time != null) {
                content = "创建时间：" + add_time;
                setTextPartColor(tv_suning_detail_createtime, 5, content);
            }
            if (payment_time != null) {
                content = "付款时间：" + payment_time;
                setTextPartColor(tv_suning_detail_paytime, 5, content);
            }
            if (express_time != null) {
                content = "发货时间：" + express_time;
                setTextPartColor(tv_suning_detail_sendtime, 5, content);
            }
            if (complete_time != null) {
                content = "完成时间：" + complete_time;
                setTextPartColor(tv_suning_detail_accepttime, 5, content);
            }
            tv_item_detail_extra.setVisibility(View.GONE);
            tv_item_detail_left.setVisibility(View.GONE);
            tv_item_detail_right.setVisibility(View.VISIBLE);
            tv_item_detail_right.setText("晒单");
        }
    }

    /**
     * 设置控件上面部分字体颜色
     *
     * @param tv
     * @param end
     * @param content
     */
    private void setTextPartColor(TextView tv, int end, String content) {
        SpannableString spannableString = new SpannableString(content);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#808080"));
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(1.2f);
        spannableString.setSpan(colorSpan, 0, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan01, end, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);
    }

    /**
     * 显示邮费和付款金额
     *
     * @param dataBean
     * @param df
     */
    private void showFreeAndMoney(SuningOrderBean.DataBean dataBean, DecimalFormat df) {
        double real_amount = dataBean.getReal_amount();
        double sn_freight = dataBean.getSn_freight();
        if (sn_freight == 0) {
            tv_suning_detail_free.setText("免运费");
        } else {
            tv_suning_detail_free.setText(" ¥" + df.format(sn_freight));
        }
        tv_suning_detail_money.setText(" ¥" + df.format(real_amount));
    }

    /**
     * 展示商品信息
     *
     * @param dataBean
     * @param df
     */
    private void showWareInfo(SuningOrderBean.DataBean dataBean, final DecimalFormat df) {
        try {
            final List<SuningOrderBean.DataBean.GoodListBean> goodList = dataBean.getGoodList();
            order_id = dataBean.getId();
            if (goodList != null && goodList.size() > 0) {
                ll_suning_detail_container.removeAllViews();
                for (int i = 0; i < goodList.size(); i++) {
                    View inflate = View.inflate(SuningOrderDetailActivity.this, R.layout.suning_order_detail_ware, null);
                    LinearLayout ll_order_detail_ware = (LinearLayout) inflate.findViewById(R.id.ll_order_detail_ware);
                    ImageView iv_suning_order_ware = (ImageView) inflate.findViewById(R.id.iv_suning_order_ware);
                    TextView tv_suning_order_title = (TextView) inflate.findViewById(R.id.tv_suning_order_title);
                    TextView tv_suning_order_type = (TextView) inflate.findViewById(R.id.tv_suning_order_type);
                    TextView tv_suning_order_realprice = (TextView) inflate.findViewById(R.id.tv_suning_order_realprice);
                    TextView tv_suning_order_oldprice = (TextView) inflate.findViewById(R.id.tv_suning_order_oldprice);
                    TextView tv_suning_order_number = (TextView) inflate.findViewById(R.id.tv_suning_order_number);
                    RelativeLayout rl_suning_detail_back = (RelativeLayout) inflate.findViewById(R.id.rl_suning_detail_back);
                    TextView tv_item_order_back = (TextView) inflate.findViewById(R.id.tv_item_order_back);
                    TextView tv_item_order_seelogistic = (TextView) inflate.findViewById(R.id.tv_item_order_seelogistic);
                    TextView tv_item_order_confirmorder = (TextView) inflate.findViewById(R.id.tv_item_order_confirmorder);
                    goodListBean = goodList.get(i);
                    if (goodListBean != null) {
                        article_id = goodListBean.getArticle_id();
                        goods_id = goodListBean.getGoods_id();
                        productId = goodListBean.getProductId();
                        goods_title = goodListBean.getGoods_title();
                        spec_text = goodListBean.getSpec_text();
                        real_price = goodListBean.getReal_price();
                        goods_price = goodListBean.getGoods_price();
                        quantity = goodListBean.getQuantity();
                        w_order_id = goodListBean.getOrder_id();
                        w_itemId = goodListBean.getItemId();
                        if (goodListBean.getImg_url() != null) {
                            String img_url = goodListBean.getImg_url().replace("800x800", "400x400");
                            Glide.with(SuningOrderDetailActivity.this).load(img_url)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                                    .placeholder(R.mipmap.zhanwei_icon)//加载过程中的图片
                                    .error(R.mipmap.zhanwei_icon)//加载失败的时候显示的图片
                                    .into(iv_suning_order_ware);//请求成功后把图片设置到的控件
                        }
                        if (spec_text != null) {
                            if (spec_text.length() > 16) {
                                String new_spec = spec_text.substring(0, 16) + "...";
                                tv_suning_order_type.setText(new_spec);
                            } else {
                                tv_suning_order_type.setText(spec_text);
                            }
                        }

                        if (goods_title != null) {
                            tv_suning_order_title.setText(goods_title);
                        }
                        tv_suning_order_realprice.setText(" ¥" + String.valueOf(df.format(real_price)));
                        tv_suning_order_oldprice.getPaint().setAntiAlias(true);
                        tv_suning_order_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                        tv_suning_order_oldprice.setText(" ¥" + String.valueOf(df.format(goods_price)));
                        tv_suning_order_number.setText("x" + quantity);
                        if (status == 1) {
                            rl_suning_detail_back.setVisibility(View.GONE);
                            tv_item_order_confirmorder.setVisibility(View.GONE);
                        } else if (status == 2) {
                            rl_suning_detail_back.setVisibility(View.VISIBLE);
                            tv_item_order_back.setText("退款");
                            tv_item_order_confirmorder.setVisibility(View.GONE);
                        } else if (status == 3) {
                            rl_suning_detail_back.setVisibility(View.VISIBLE);
                            tv_item_order_back.setText("退款");
                            tv_item_order_confirmorder.setVisibility(View.VISIBLE);
                        } else if (status == 4) {
                            rl_suning_detail_back.setVisibility(View.VISIBLE);
                            tv_item_order_back.setText("申请售后");
                            tv_item_order_confirmorder.setVisibility(View.GONE);
                        } else {
                            rl_suning_detail_back.setVisibility(View.GONE);
                            tv_item_order_confirmorder.setVisibility(View.GONE);
                        }
                        final int finalI = i;
                        tv_item_order_confirmorder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goodListBean = goodList.get(finalI);
                                if (goodListBean != null) {
                                    w_order_id = goodListBean.getOrder_id();
                                    w_article_id = goodListBean.getArticle_id();
                                }
                                confirmChildOrder(String.valueOf(w_order_id), String.valueOf(w_article_id));
                            }
                        });
                        tv_item_order_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                goodListBean = goodList.get(finalI);
                                if (goodListBean != null) {
                                    w_order_id = goodListBean.getOrder_id();
                                    w_itemId = goodListBean.getItemId();
                                }
                                checkApplyState(String.valueOf(w_order_id), String.valueOf(w_itemId));
                                //toBackApply(goodListBean, goods_title, spec_text, df, real_price, goods_price, quantity, article_id, order_id);
                            }
                        });
                        tv_item_order_seelogistic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goodListBean = goodList.get(finalI);
                                if (goodListBean != null) {
                                    article_id = goodListBean.getArticle_id();
                                }
                                Intent intent = new Intent(SuningOrderDetailActivity.this, SuningLogisticsDetailInfoActivity.class);
                                intent.putExtra("order_id", order_id);
                                intent.putExtra("article_id", article_id);
                                startActivity(intent);
                            }
                        });
                        ll_order_detail_ware.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                goodListBean = goodList.get(finalI);
                                if (goodListBean != null) {
                                    article_id = goodListBean.getArticle_id();
                                    goods_id = goodListBean.getGoods_id();
                                    productId = goodListBean.getProductId();
                                    goods_title = goodListBean.getGoods_title();
                                    spec_text = goodListBean.getSpec_text();
                                    real_price = goodListBean.getReal_price();
                                    goods_price = goodListBean.getGoods_price();
                                    quantity = goodListBean.getQuantity();
                                    w_order_id = goodListBean.getOrder_id();
                                    w_itemId = goodListBean.getItemId();
                                }
                                Intent intent = new Intent(SuningOrderDetailActivity.this, SuningDetailActivity.class);
                                intent.putExtra("article_id", article_id);
                                intent.putExtra("goods_id", productId);
                                intent.putExtra("goods_img", goodListBean.getImg_url());
                                intent.putExtra("goods_title", goods_title);
                                intent.putExtra("goods_price", df.format(real_price));
                                intent.putExtra("c_price", df.format(goods_price));
                                startActivity(intent);
                                LogUtil.e("suning detail = " + article_id + "-" + goods_id + "-" + goodListBean.getImg_url() + "-" + real_price + "-" + goods_price);
                            }
                        });
                        ll_suning_detail_container.addView(inflate);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e("SuningOrderDetailActivity error");
            Toast.makeText(SuningOrderDetailActivity.this, "系统出现故障，请退出后重新打开！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 跳转到申请退款服务界面
     *
     * @param goodListBean
     * @param goods_title
     * @param spec_text
     * @param df
     * @param real_price
     * @param goods_price
     * @param quantity
     * @param article_id
     * @param order_id
     */
    private void toBackApply(SuningOrderBean.DataBean.GoodListBean goodListBean, String goods_title, String spec_text, DecimalFormat df, double real_price, double goods_price, int quantity, int article_id, int order_id) {
        if (goodListBean != null) {
            Intent intent = new Intent(SuningOrderDetailActivity.this, SaleAfterActivity.class);
            Bundle bundle = new Bundle();
            if (goodListBean.getImg_url() != null) {
                bundle.putString("ware_img", goodListBean.getImg_url());
            }
            if (goods_title != null) {
                bundle.putString("ware_title", goods_title);
            }
            if (spec_text != null) {
                bundle.putString("spec_text", spec_text);
            }
            bundle.putString("real_price", df.format(real_price) + "");
            bundle.putString("goods_price", df.format(goods_price) + "");
            bundle.putString("quantity", String.valueOf(quantity));
            bundle.putString("article_id", String.valueOf(article_id));
            bundle.putString("order_id", String.valueOf(order_id));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * 展示收货地址
     *
     * @param dataBean
     */
    private void showOrderAddress(SuningOrderBean.DataBean dataBean) {
        String accept_name = dataBean.getAccept_name();
        String mobile = dataBean.getMobile();
        String area = dataBean.getArea();
        String address = dataBean.getAddress();
        tv_suning_detail_accept.setText(accept_name);
        tv_suning_detail_phone.setText(mobile);
        String address_detail = "收货地址: " + area + " " + address;
        SpannableString spannableString = new SpannableString(address_detail);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#808080"));
        spannableString.setSpan(colorSpan, 0, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_suning_detail_address.setText(spannableString);
    }

    /**
     * 展示订单状态
     *
     * @param status
     */
    private void showOrderStatus(int status) {
        if (status == 1) {
            tv_suning_detail_status.setText("买家待付款，支付后将及时为您安排发货。");
        } else if (status == 2) {
            tv_suning_detail_status.setText("买家已付款，等待卖家发货。");
        } else if (status == 3) {
            tv_suning_detail_status.setText("卖家已发货，请您查看物流信息。");
        } else if (status == 4) {
            tv_suning_detail_status.setText("买家已签收，交易完成。");
        }
    }

    /**
     * 页面上事件监听
     */
    public class MyonclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_exit:
                    finish();
                    break;
                case R.id.tv_item_detail_left:
                    leftButtonHandler();
                    break;
                case R.id.tv_item_detail_right:
                    rightButtonHandler();
                    break;
                case R.id.rl_suning_order_number:
                    copeOrdernoToPanel();
                    break;
            }
        }
    }

    /**
     * 复制订单号到剪切板
     */
    private void copeOrdernoToPanel() {
        Toast.makeText(this, "此订单号已经复制到剪切板~", Toast.LENGTH_SHORT).show();
        Tools.copyToClipboard(this, order_no);
    }

    /**
     * 处理右边button的点击事件
     */
    private void rightButtonHandler() {
        if (status == 1) {
            toPayOrder(real_amount, order_no, order_id);
        } else if (status == 2) {
            remindSend();
        } else if (status == 3) {
            confirmOrder(String.valueOf(order_id));
        } else if (status == 4) {
            //评价
            Toast.makeText(SuningOrderDetailActivity.this, "该功能正在开发中。", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理左边button的点击事件
     */
    private void leftButtonHandler() {
        if (status == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示")
                    .setMessage("你确定要取消的订单吗?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cancelOrder(String.valueOf(order_id));
                        }
                    })
                    .setNegativeButton("取消", null);
            alertDialog = builder.create();
            if (SuningOrderDetailActivity.this != null && !SuningOrderDetailActivity.this.isFinishing()) {
                alertDialog.show();
            }
        } else if (status == 3) {
            //seeLogisticsInfo();
        }
    }

    /**
     * 退款或售后
     */
    private void toRebackOrder() {
        Toast.makeText(SuningOrderDetailActivity.this, "退款售后！", Toast.LENGTH_SHORT).show();
    }

    /**
     * 订单支付
     */
    private void toPayOrder(double pay_price, String order_num, int orderId) {
        Intent intent = new Intent(this, CheckStandActivity.class);
        intent.putExtra("pay_price", pay_price);
        intent.putExtra("order_num", order_num);
        intent.putExtra("orderId", orderId);
        startActivity(intent);
        finish();
    }

    /**
     * 取消订单
     */
    private void cancelOrder(String order_id) {
        whick = "cancel_order";
        map = new HashMap<>();
        map.put("user_id", userLogin);
        map.put("order_id", order_id);
        netUtil.okHttp2Server2(SuningOrderDetailActivity.this, cancel_url, map);
    }


    /**
     * 提醒发货
     */
    private void remindSend() {
        if (count > 2) {
            if (isContinue) {
                Toast.makeText(SuningOrderDetailActivity.this, "超过提醒次数。", Toast.LENGTH_SHORT).show();
                isContinue = true;
            }
            return;
        }
        Toast.makeText(this, "提醒成功。", Toast.LENGTH_SHORT).show();
        count++;
    }

    /**
     * 查看物流信息
     */
    private void seeLogisticsInfo() {
        Intent intent = new Intent(SuningOrderDetailActivity.this, SuningLogisticsDetailInfoActivity.class);
        intent.putExtra("order_id", order_id);
        startActivity(intent);
    }

    /**
     * 确认订单
     */
    private void confirmOrder(String order_id) {
        whick = "confirm_order";
        map = new HashMap<>();
        map.put("user_id", userLogin);
        map.put("order_id", order_id);
        map.put("order_status", "3");
        netUtil.okHttp2Server2(SuningOrderDetailActivity.this, confrim_url, map);
    }

    /**
     * 确认订单
     */
    private void confirmChildOrder(String order_id, String article_id) {
        whick = "confirm_child_order";
        map = new HashMap<>();
        map.put("user_id", userLogin);
        map.put("order_id", order_id);
        map.put("article_id", article_id);
        map.put("order_status", "3");
        netUtil.okHttp2Server2(SuningOrderDetailActivity.this, confrim_child_url, map);
    }


    /**
     * 监听联网请求的回调
     */
    public class OnServerResponseListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            try {
                if (whick.equals("cancel_order")) {
                    parseCancelOrderData(response);
                } else if (whick.equals("confirm_order")) {
                    parderConfirmOrderData(response);
                } else if (whick.equals("confirm_child_order")) {
                    parseConfirmChildOrderData(response);
                } else if (whick.equals("apply_state")) {
                    parseApplyState(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "-" + e.getMessage());
            EncryptNetUtil.startNeterrorPage(SuningOrderDetailActivity.this);
        }
    }

    /**
     * 解析申请状态
     *
     * @param response
     */
    private void parseApplyState(String response) throws Exception {
        LogUtil.e("申请状态 = " + response);
        ApplyStateBean applyStateBean = new Gson().fromJson(response, ApplyStateBean.class);
        int statusCode = applyStateBean.getStatusCode();
        switch (statusCode) {
            case 1:
                List<ApplyStateBean.DataBean> data = applyStateBean.getData();
                if (data != null && data.size() > 0) {
                    ApplyStateBean.DataBean dataBean = data.get(0);
                    if (dataBean != null) {
                        int apply_status = dataBean.getApply_status();
                        if (apply_status == 0) {
                            toBackApply(goodListBean, goods_title, spec_text, df, real_price, goods_price, quantity, article_id, order_id);
                        } else if (apply_status == 1) {
                            Toast.makeText(SuningOrderDetailActivity.this, "您的退货申请正在审核中。", Toast.LENGTH_SHORT).show();
                        } else if (apply_status == 2) {
                            Toast.makeText(SuningOrderDetailActivity.this, "您的退货申请已被驳回。", Toast.LENGTH_SHORT).show();
                        } else if (apply_status == 3) {
                            Toast.makeText(SuningOrderDetailActivity.this, "正在退货。", Toast.LENGTH_SHORT).show();
                        } else if (apply_status == 4) {
                            Toast.makeText(SuningOrderDetailActivity.this, "退货成功。", Toast.LENGTH_SHORT).show();
                        } else if (apply_status == 5) {
                            Toast.makeText(SuningOrderDetailActivity.this, "已退款。", Toast.LENGTH_SHORT).show();
                        } else if (apply_status == 6) {
                            Toast.makeText(SuningOrderDetailActivity.this, "已退货。", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case 0:
                Toast.makeText(SuningOrderDetailActivity.this, applyStateBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析取消订单数据
     *
     * @param response
     */
    private void parseCancelOrderData(String response) throws Exception {
        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
        int statusCode = codeStatueBean.getStatusCode();
        switch (statusCode) {
            case 1:
                Toast.makeText(SuningOrderDetailActivity.this, "订单取消成功。", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new AnyEvent(EventType.UPDATA_ORDER_LIST, "刷新订单列表"));
                finish();
                break;
            case 0:
                Toast.makeText(SuningOrderDetailActivity.this, "订单取消失败！！！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析确认订单数据
     *
     * @param response
     */
    private void parderConfirmOrderData(String response) throws Exception {
        SuningOrderConfirmBean suningOrderConfirmBean = new Gson().fromJson(response, SuningOrderConfirmBean.class);
        int statusCode = suningOrderConfirmBean.getStatusCode();
        switch (statusCode) {
            case 1:
                Toast.makeText(SuningOrderDetailActivity.this, "确认订单成功。", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new AnyEvent(EventType.UPDATA_ORDER_LIST, "刷新订单列表"));
                finish();
                break;
            case 0:
                Toast.makeText(SuningOrderDetailActivity.this, "确认订单失败！！！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析确认子订单数据
     *
     * @param response
     */
    private void parseConfirmChildOrderData(String response) throws Exception {
        SuningOrderConfirmBean suningOrderConfirmBean = new Gson().fromJson(response, SuningOrderConfirmBean.class);
        int statusCode = suningOrderConfirmBean.getStatusCode();
        switch (statusCode) {
            case 1:
                Toast.makeText(SuningOrderDetailActivity.this, "确认订单成功。", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case 0:
                Toast.makeText(SuningOrderDetailActivity.this, "确认订单失败！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 检查申请状态
     */
    private void checkApplyState(String order_id, String orderItemId) {
        if (netUtil == null) {
            netUtil = new NetUtil();
        }
        whick = "apply_state";
        map = new HashMap<>();
        map.put("order_id", order_id);
        map.put("orderItemId", orderItemId);
        netUtil.okHttp2Server2(SuningOrderDetailActivity.this, apply_state_url, map);
    }
}
