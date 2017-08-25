package com.gather_excellent_help.presenter.homepresenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.QiangTaoBean;
import com.gather_excellent_help.presenter.BasePresenter;
import com.gather_excellent_help.ui.activity.QiangTaoActivity;
import com.gather_excellent_help.ui.activity.WebActivity;
import com.gather_excellent_help.ui.activity.WebRecordActivity;
import com.gather_excellent_help.ui.widget.RushDownTimer;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.ScreenUtil;
import com.gather_excellent_help.utils.Tools;
import com.gather_excellent_help.utils.imageutils.ImageLoader;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Dapeng Fang on 2017/8/21.
 */

public class QiangPresenter extends BasePresenter {
    private Context context;
    private LinearLayout llHomeQiangZera;
    private String qiang_url = Url.BASE_URL + "RushBuy.aspx";
    private NetUtil netUtil;
    private double user_rate;
    private int shopType;
    private boolean isToggle;
    private ImageLoader mImageLoader;
    private RelativeLayout rl_item_laod_more;
    private TextView tv_item_home_title;
    private long curr_time;
    private long endtime;
    private List<QiangTaoBean.DataBean> qiangData;
    private TextView tv_qinag_left_count;
    private TextView tv_rush_hour;
    private TextView tv_rush_minute;
    private TextView tv_rush_second;
    private TextView tv_qiang_left_price;
    private ImageView iv_qiang_left_img;
    private LinearLayout ll_qiang_left_zera;
    private LinearLayout ll_qiang_right_zera;
    private RushDownTimer rushDownTimer;
    private int currHour;

    public QiangPresenter(Context context, LinearLayout llHomeQiangZera) {
        this.context = context;
        this.llHomeQiangZera = llHomeQiangZera;
        initView();
        netUtil = new NetUtil();
        shopType = Tools.getShopType(context);
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        String userRate = Tools.getUserRate(context);
        if (!TextUtils.isEmpty(userRate)) {
            double v = Double.parseDouble(userRate);
            user_rate = v / 100;
        }
        isToggle = Tools.isToggleShow(context);
    }

    @Override
    public View initView() {
        rl_item_laod_more = (RelativeLayout) llHomeQiangZera.findViewById(R.id.rl_item_laod_more);
        ll_qiang_left_zera = (LinearLayout) llHomeQiangZera.findViewById(R.id.ll_qiang_left_zera);
        tv_item_home_title = (TextView) llHomeQiangZera.findViewById(R.id.tv_item_home_title);
        tv_qinag_left_count = (TextView) llHomeQiangZera.findViewById(R.id.tv_qinag_left_count);
        tv_rush_hour = (TextView) llHomeQiangZera.findViewById(R.id.tv_rush_hour);
        tv_rush_minute = (TextView) llHomeQiangZera.findViewById(R.id.tv_rush_minute);
        tv_rush_second = (TextView) llHomeQiangZera.findViewById(R.id.tv_rush_second);
        tv_qiang_left_price = (TextView) llHomeQiangZera.findViewById(R.id.tv_qiang_left_price);
        iv_qiang_left_img = (ImageView) llHomeQiangZera.findViewById(R.id.iv_qiang_left_img);
        ll_qiang_right_zera = (LinearLayout) llHomeQiangZera.findViewById(R.id.ll_qiang_right_zera);
        return llHomeQiangZera;
    }

    @Override
    public void initData() {
        tv_item_home_title.setText("抢购区");
        net2Server();
        netUtil.setOnServerResponseListener(new MyOnServerResponserListener());
        rl_item_laod_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currHour>=0 && currHour<8) {
                    Toast.makeText(context, "活动在早上8点准时开启，请耐心等待！", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(context, QiangTaoActivity.class);
                    context.startActivity(intent);
                }
            }
        });
        ll_qiang_left_zera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currHour>=0 && currHour<8) {
                    Toast.makeText(context, "活动在早上8点准时开启，请耐心等待！", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(context, QiangTaoActivity.class);
                    context.startActivity(intent);
                }
            }
        });

    }

    private void net2Server() {
        curr_time = System.currentTimeMillis();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        Date date = new Date(curr_time);
        String currtimes = sdf.format(date);
        String[] timesArray = currtimes.split(":");
        String year = timesArray[0];
        String month = timesArray[1];
        String day = timesArray[2];
        String hour = timesArray[3];
        String minute = timesArray[4];
        String second = timesArray[5];
        currHour = Integer.parseInt(hour);
        int currDay = Integer.parseInt(day);
        String start_time = year + "-" + month + "-" + day + " " + hour + ":00:00";
        Date d = null;
        try {
            d = sdf2.parse(start_time);
            long time = d.getTime();
            endtime = time + 3600 * 1000;
            String end_time = sdf2.format(new Date(endtime));
            Map<String, String> map = new HashMap<>();
            map.put("pageSize", "3");
            map.put("pageIndex", "1");
            map.put("start_time", start_time);
            map.put("end_time", end_time);
            netUtil.okHttp2Server2(qiang_url, map);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_qinag_left_count.setText("距离第"+(currHour +1)+"场倒计时");
    }

    /**
     * 设置倒计时
     * @param rushDownTimer
     */
    public void setRushDownTimer(RushDownTimer rushDownTimer) {
        this.rushDownTimer = rushDownTimer;
        if (tv_rush_hour != null) {
            tv_rush_hour.setText(rushDownTimer.getHour());
        }
        if (tv_rush_minute != null) {
            tv_rush_minute.setText(rushDownTimer.getMinute());
        }
        if (tv_rush_second != null) {
            tv_rush_second.setText(rushDownTimer.getSecond());
        }
    }

    public class MyOnServerResponserListener implements NetUtil.OnServerResponseListener {

        @Override
        public void getSuccessResponse(String response) {
            if (context != null) {
                parseData(response);
            }
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
          if(context!=null) {
              Toast.makeText(context, "请检查你的网络连接是否正常！", Toast.LENGTH_SHORT).show();
          }
        }
    }

    private void parseData(String response) {
        QiangTaoBean qiangTaoBean = new Gson().fromJson(response, QiangTaoBean.class);
        int statusCode = qiangTaoBean.getStatusCode();
        switch (statusCode) {
            case 1 :
                qiangData = qiangTaoBean.getData();
                if(qiangData!=null) {
                    loadData(qiangData);
                }
                onLoadSuccessListener.onSuccessResponse(endtime-curr_time);
                break;
            case 0:
                Toast.makeText(context, qiangTaoBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 加载抢购区数据
     * @param qiangData
     */
    private void loadData(List<QiangTaoBean.DataBean> qiangData) {
        /*
        * 加载左边区域
        * */
        if(qiangData.size()<3) {
            return;
        }
        QiangTaoBean.DataBean leftData = qiangData.get(0);
        String sell_prices = leftData.getSell_price();
        String img_urls = leftData.getImg_url();

        if(tv_qiang_left_price!=null && sell_prices!=null) {
            int price = (int) Double.parseDouble(sell_prices);
            if(price<10) {
                tv_qiang_left_price.setText("?");
            }else{
                String s = String.valueOf(price);
                String str = s.substring(0, 1);
                for (int i=0;i<s.length()-1;i++){
                 str+="?";
                }
                tv_qiang_left_price.setText(str);
            }
        }
        if(iv_qiang_left_img!=null && img_urls!=null) {
            mImageLoader.loadImage(img_urls,iv_qiang_left_img,true);
        }

        /*
        * 加载右边商品区域
        * */
        DecimalFormat df = new DecimalFormat("#0.00");
        int childCount = ll_qiang_right_zera.getChildCount();
        for (int i=0;i<childCount-1;i++){
            View childAt = ll_qiang_right_zera.getChildAt(2 * i);
            LinearLayout ll_qiang_right_ware = (LinearLayout) childAt.findViewById(R.id.ll_qiang_right_ware);
            ImageView iv_qiang_ware_img = (ImageView) childAt.findViewById(R.id.iv_qiang_ware_img);
            TextView tv_qiang_ware_coupon = (TextView) childAt.findViewById(R.id.tv_qiang_ware_coupon);
            TextView tv_qiang_ware_title = (TextView) childAt.findViewById(R.id.tv_qiang_ware_title);
            RelativeLayout rl_qiang_ware_count = (RelativeLayout) childAt.findViewById(R.id.rl_qiang_ware_count);
            View v_qiang_ware_progress = childAt.findViewById(R.id.v_qiang_ware_progress);
            TextView tv_qiang_ware_percent = (TextView) childAt.findViewById(R.id.tv_qiang_ware_percent);
            TextView tv_qiang_ware_number = (TextView) childAt.findViewById(R.id.tv_qiang_ware_number);
            TextView tv_qiang_ware_price = (TextView) childAt.findViewById(R.id.tv_qiang_ware_price);
            TextView tv_qiang_ware_zhuan = (TextView) childAt.findViewById(R.id.tv_qiang_ware_zhuan);
            TextView tv_qiang_ware_coast = (TextView) childAt.findViewById(R.id.tv_qiang_ware_coast);
            LinearLayout ll_qiang_ware_zhuan = (LinearLayout) childAt.findViewById(R.id.ll_qiang_ware_zhuan);
            QiangTaoBean.DataBean dataBean = qiangData.get(i + 1);
            final String img_url = dataBean.getImg_url();
            QiangTaoBean.DataBean.CouponInfoBean coupon_info = dataBean.getCoupon_info();
            final String title = dataBean.getTitle();
            double sold_num = dataBean.getSold_num();
            double total_amount = dataBean.getTotal_amount();
            final String sell_price = dataBean.getSell_price();
            final String goods_id = String.valueOf(dataBean.getProductId());
            double sell_price_s = Double.parseDouble(sell_price);
            String coupon_info_s = null;
            String coupon_click_url = null;
            String max_commission_rate = null;
            double rate = 0;
            if(coupon_info!=null) {
                coupon_info_s =  coupon_info.getCoupon_info();
                coupon_click_url = coupon_info.getCoupon_click_url();
                max_commission_rate = coupon_info.getMax_commission_rate();
                rate = Double.parseDouble(max_commission_rate)/100;
            }
            int coupon_p = 0;

            if (coupon_info_s != null && !TextUtils.isEmpty(coupon_info_s)) {
                int index_s = coupon_info_s.indexOf("减") + 1;
                String substring_s1 = coupon_info_s.substring(index_s, coupon_info_s.length() - 1);
                coupon_p = Integer.parseInt(substring_s1);
            }
            double zhuan = (sell_price_s - coupon_p) * rate * 0.9f * user_rate;
            double coast = sell_price_s - zhuan - coupon_p;
            double percent = 0;
            if (total_amount == 0) {
                percent = 0;
            } else {
                double st = sold_num / total_amount;
                percent = (int) (st * 100);
            }
            final String link_url = dataBean.getLink_url();
            if(iv_qiang_ware_img!=null && img_url!=null) {
                mImageLoader.loadImage(img_url+"_320x320q90.jpg",iv_qiang_ware_img,true);
            }
            if(tv_qiang_ware_coupon!=null) {
                tv_qiang_ware_coupon.setText("领券减"+coupon_p);
            }
            if(tv_qiang_ware_title!=null && title!=null) {
                tv_qiang_ware_title.setText(title);
            }
            LogUtil.e("sold_num"+sold_num+",---total_number"+total_amount+",===="+percent);
            if(tv_qiang_ware_percent!=null) {
                tv_qiang_ware_percent.setText((int)percent+"%");
            }
            if(tv_qiang_ware_number!=null) {
                tv_qiang_ware_number.setText("已抢"+(int)sold_num+"件");
            }
            int width = ScreenUtil.getScreenWidth(context) * 16/ 60;
            ViewGroup.LayoutParams lp2;
            lp2 = rl_qiang_ware_count.getLayoutParams();
            lp2.width = width;
            rl_qiang_ware_count.setLayoutParams(lp2);
            ViewGroup.LayoutParams lp;
            lp = v_qiang_ware_progress.getLayoutParams();
            lp.width = (int) (width * (percent / 100));
            v_qiang_ware_progress.setLayoutParams(lp);
            if(sell_price!=null && tv_qiang_ware_price!=null) {
                tv_qiang_ware_price.setText(sell_price);
            }
            if(tv_qiang_ware_zhuan!=null) {
                tv_qiang_ware_zhuan.setText("￥"+df.format(zhuan));
            }
            if(tv_qiang_ware_coast!=null) {
                tv_qiang_ware_coast.setText("￥"+df.format(coast));
            }
            if(coupon_p>0) {
                tv_qiang_ware_coupon.setVisibility(View.VISIBLE);
            }else{
                tv_qiang_ware_coupon.setVisibility(View.GONE);
            }
            if (shopType == 1) {
                if (isToggle) {
                    ll_qiang_ware_zhuan.setVisibility(View.GONE);
                } else {
                    ll_qiang_ware_zhuan.setVisibility(View.VISIBLE);
                }
            } else {
                ll_qiang_ware_zhuan.setVisibility(View.GONE);
            }

            if(zhuan == 0) {
                ll_qiang_ware_zhuan.setVisibility(View.GONE);
            }
            final String finalCoupon_info_s = coupon_info_s;
            final String finalCoupon_click_url = coupon_click_url;
            final int finalCoupon_p = coupon_p;
            ll_qiang_right_ware.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finalCoupon_info_s !=null && !TextUtils.isEmpty(finalCoupon_info_s)) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("web_url", finalCoupon_click_url);
                        intent.putExtra("url", link_url);
                        intent.putExtra("goods_id", goods_id);
                        intent.putExtra("goods_img", img_url);
                        intent.putExtra("goods_title", title);
                        intent.putExtra("goods_price", sell_price);
                        intent.putExtra("goods_coupon",String.valueOf(finalCoupon_p));
                        intent.putExtra("goods_coupon_url",finalCoupon_click_url);
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent(context, WebRecordActivity.class);
                        intent.putExtra("url", link_url);
                        intent.putExtra("goods_id", goods_id);
                        intent.putExtra("goods_img", img_url);
                        intent.putExtra("goods_title", title);
                        intent.putExtra("goods_price", sell_price);
                        context.startActivity(intent);
                    }
                }
            });

        }
    }

    private OnLoadSuccessListener onLoadSuccessListener;

    public void setOnLoadSuccessListener(OnLoadSuccessListener onLoadSuccessListener) {
        this.onLoadSuccessListener = onLoadSuccessListener;
    }

    public interface OnLoadSuccessListener{
        void onSuccessResponse(long time);
    }
}
