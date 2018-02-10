package com.gather_excellent_help.ui.activity.shop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.BrandBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.credits.MerchantEnterActivity;
import com.gather_excellent_help.ui.adapter.BrandListAdapter;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.MyGridView;
import com.gather_excellent_help.utils.EncryptNetUtil;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class ShopBrandUpdateActivity extends BaseActivity {

    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.tv_top_title_name)
    TextView tvTopTitleName;
    @Bind(R.id.rl_share)
    RelativeLayout rlShare;
    @Bind(R.id.iv_merchant_brand_arraw)
    ImageView ivMerchantBrandArraw;
    @Bind(R.id.ll_merchant_brand_expand)
    LinearLayout llMerchantBrandExpand;
    @Bind(R.id.gv_merchant_brand)
    MyGridView gvMerchantBrand;
    @Bind(R.id.tv_shop_info_next)
    TextView tvShopInfoNext;
    private String brand_url = Url.BASE_URL + "goodsBrand.aspx";
    private NetUtil netUtil;
    private BrandListAdapter brandListAdapter;
    private List<BrandBean.DataBean> brandData;//加载的数据
    private List<BrandBean.DataBean> newDatas;//获取的数据
    private boolean isExpand;
    private ArrayList<BrandBean.DataBean> selectlists = new ArrayList<>();
    private String name;
    private String telephone;
    private String address;
    private String info;
    private String business_time;
    private String brand = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_brand_update);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initData();
    }

    /**
     * 加载数据
     */
    private void initData(){
        getLastPageInfo();
        ivMerchantBrandArraw.setImageResource(R.drawable.left_black_arraw);
        tvTopTitleName.setText("主营品牌");
        netUtil = new NetUtil();
        netUtil.okHttp2Server2(ShopBrandUpdateActivity.this,brand_url, null);
        netUtil.setOnServerResponseListener(new MyOnServerResponseListener());
        gvMerchantBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view.findViewById(R.id.tv_brand_list_title);
                CardView cv = (CardView) view.findViewById(R.id.cv_brand_list);
                BrandBean.DataBean dataBean = brandData.get(i);
                dataBean.setSelect(!dataBean.isSelect());
                boolean select = dataBean.isSelect();
                if (select) {
                    tv.setTextColor(Color.WHITE);
                    cv.setCardBackgroundColor(Color.RED);
                    selectlists.add(dataBean);
                } else {
                    tv.setTextColor(Color.parseColor("#88000000"));
                    cv.setCardBackgroundColor(Color.parseColor("#f2f2f2"));
                    if (selectlists.contains(dataBean)) {
                        selectlists.remove(dataBean);
                    }
                }
            }
        });
        rlExit.setOnClickListener(new MyonClickListener());
        llMerchantBrandExpand.setOnClickListener(new MyonClickListener());
        tvShopInfoNext.setOnClickListener(new MyonClickListener());
    }

    /**
     * 获取上个界面的信息
     */
    private void getLastPageInfo() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        telephone = intent.getStringExtra("telephone");
        address = intent.getStringExtra("address");
        info = intent.getStringExtra("info");
        business_time = intent.getStringExtra("business_time");
        LogUtil.e(name + "--" + telephone + "--" + address + "--" + info + "--" + business_time);
    }

    public class MyonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_exit :
                    finish();
                    break;
                case R.id.ll_merchant_brand_expand:
                    isExpand = !isExpand;
                    if (!isExpand) {
                        brandData = new ArrayList<>();
                        for (int i = 0; i < 8; i++) {
                            if(newDatas!=null && newDatas.size()>0) {
                                brandData.add(newDatas.get(i));
                            }
                        }
                        ivMerchantBrandArraw.setImageResource(R.drawable.left_black_arraw);
                    } else {
                        brandData = newDatas;
                        ivMerchantBrandArraw.setImageResource(R.drawable.down_black_arraw);
                    }
                    if (gvMerchantBrand != null) {
                        brandListAdapter = new BrandListAdapter(ShopBrandUpdateActivity.this, brandData);
                        gvMerchantBrand.setAdapter(brandListAdapter);
                    }
                    break;
                case R.id.tv_shop_info_next:
                    toJustNextPage();
                    break;
            }
        }
    }

    /**
     * 跳转到下一个界面
     */
    private void toJustNextPage() {
        Intent intent = new Intent(ShopBrandUpdateActivity.this, ShopPhotoUpdateActivity.class);
        if(name!=null) {
            intent.putExtra("name",name);
        }
        if(telephone!=null) {
            intent.putExtra("telephone",telephone);
        }
        if(address!=null) {
            intent.putExtra("address",address);
        }
        if(info!=null) {
            intent.putExtra("info",info);
        }
        if(business_time!=null) {
            intent.putExtra("business_time",business_time);
        }
        if(selectlists.size()>0) {
            for (int i = 0; i < selectlists.size(); i++) {
                brand += selectlists.get(i).getTitle() + "、";
            }
            brand = brand.substring(0, brand.length() - 1);
            intent.putExtra("brand",brand);
            startActivity(intent);
        }else{
            Toast.makeText(ShopBrandUpdateActivity.this, "请选择品牌", Toast.LENGTH_SHORT).show();
        }

    }

    public class MyOnServerResponseListener implements NetUtil.OnServerResponseListener{

        @Override
        public void getSuccessResponse(String response) {
            parseData(response);
        }

        @Override
        public void getFailResponse(Call call, Exception e) {
            LogUtil.e(call.toString() + "--" + e.getMessage());
            EncryptNetUtil.startNeterrorPage(ShopBrandUpdateActivity.this);
        }
    }

    /**
     * 解析品牌数据
     *
     * @param response
     */
    private void parseData(String response) {
        BrandBean brandBean = new Gson().fromJson(response, BrandBean.class);
        newDatas = brandBean.getData();
        if (newDatas != null && newDatas.size() > 4) {
            brandData = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                brandData.add(newDatas.get(i));
            }
            brandListAdapter = new BrandListAdapter(ShopBrandUpdateActivity.this, brandData);
            gvMerchantBrand.setAdapter(brandListAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(AnyEvent event) {
        if(event.getType() == EventType.EVENT_EXIT) {
            LogUtil.e("exit-------------------");
            finish();
        }
    }

}
