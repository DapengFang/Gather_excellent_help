package com.gather_excellent_help.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.SearchTaobaoBean;
import com.gather_excellent_help.bean.SearchWareBean;
import com.gather_excellent_help.ui.adapter.TaobaoWareListAdapter;
import com.gather_excellent_help.ui.base.BaseFragment;
import com.gather_excellent_help.ui.widget.TaobaoShaixuanPopupwindow;
import com.gather_excellent_help.ui.widget.TaobaoZonghePopupwindow;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by wuxin on 2017/7/7.
 */

public class TaobaoFragment extends BaseFragment {
    @Bind(R.id.rl_exit)
    RelativeLayout rlExit;
    @Bind(R.id.et_taobao_search_content)
    EditText etTaobaoSearchContent;
    @Bind(R.id.rl_taobao_sousuo)
    RelativeLayout rlTaobaoSousuo;
    @Bind(R.id.tv_taobao_zonghe_sort)
    TextView tvTaobaoZongheSort;
    @Bind(R.id.iv_taobao_zonghe_sort)
    ImageView ivTaobaoZongheSort;
    @Bind(R.id.ll_taobao_zonghe)
    LinearLayout llTaobaoZonghe;
    @Bind(R.id.tv_taobao_xiaoliang_sort)
    TextView tvTaobaoXiaoliangSort;
    @Bind(R.id.ll_taobao_xiaoliang)
    LinearLayout llTaobaoXiaoliang;
    @Bind(R.id.tv_taobao_price_sort)
    TextView tvTaobaoPriceSort;
    @Bind(R.id.iv_up_price)
    ImageView ivUpPrice;
    @Bind(R.id.iv_down_price)
    ImageView ivDownPrice;
    @Bind(R.id.ll_taobao_price)
    LinearLayout llTaobaoPrice;
    @Bind(R.id.tv_taobao_shaixuan_sort)
    TextView tvTaobaoShaixuanSort;
    @Bind(R.id.iv_taobao_shaixuan_sort)
    ImageView ivTaobaoShaixuanSort;
    @Bind(R.id.ll_taobao_shaixuan)
    LinearLayout llTaobaoShaixuan;
    @Bind(R.id.gv_taobao_list)
    GridView gvTaobaoList;
    @Bind(R.id.ll_taobao_loadmore)
    LinearLayout llTaobaoLoadmore;
    @Bind(R.id.activity_ware_list)
    LinearLayout activityWareList;

    private boolean price_sort ;

    private NetUtil netUtil;
    private Map<String,String> map;
    private String search_url = Url.BASE_URL + "SearchMore.aspx";
    private String keyword = "";//关键字
    private String city = "";//城市
    private String type = "";//排序
    private String is_tmall = "";//是否是天猫
    private String start_price = "";//范围下限
    private String end_price = "";//范围上限
    private String page_no ="1";//第几页
    private String page_size = "6";//每页多少
    private TaobaoShaixuanPopupwindow taobaoShaixuanPopupwindow;
    private TaobaoZonghePopupwindow taobaoZonghePopupwindow;


    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.taobao_fragment, null);
        return inflate;
    }

    @Override
    public void initData() {
        rlExit.setVisibility(View.INVISIBLE);
        netUtil = new NetUtil();
        searchTaobaoWare(keyword,city,type,is_tmall,start_price,end_price,page_no);
        netUtil.setOnServerResponseListener(new NetUtil.OnServerResponseListener() {
            @Override
            public void getSuccessResponse(String response) {
                LogUtil.e(response);
                CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                int statusCode = codeStatueBean.getStatusCode();
                switch (statusCode) {
                    case 1 :
                        SearchTaobaoBean searchTaobaoBean = new Gson().fromJson(response, SearchTaobaoBean.class);
                        List<SearchTaobaoBean.DataBean> data = searchTaobaoBean.getData();
                        TaobaoWareListAdapter taobaoWareListAdapter = new TaobaoWareListAdapter(getContext(), data);
                        gvTaobaoList.setAdapter(taobaoWareListAdapter);
                        break;
                    case 0:
                        Toast.makeText(getContext(), codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void getFailResponse(Call call, Exception e) {

            }
        });
        rlTaobaoSousuo.setOnClickListener(new MyOnclickListener());
        llTaobaoZonghe.setOnClickListener(new MyOnclickListener());
        llTaobaoXiaoliang.setOnClickListener(new MyOnclickListener());
        llTaobaoPrice.setOnClickListener(new MyOnclickListener());
        llTaobaoShaixuan.setOnClickListener(new MyOnclickListener());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 监听点击事件的类
     */
    public class MyOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
           switch (view.getId()) {
               case R.id.ll_taobao_zonghe :
                   tvTaobaoZongheSort.setSelected(true);
                   ivTaobaoZongheSort.setSelected(true);
                   textSelectChange(0);
                   ivUpPrice.setImageResource(R.drawable.up_gray_arraw);
                   ivDownPrice.setImageResource(R.drawable.down_gray_arraw);
                   ivTaobaoShaixuanSort.setSelected(false);
                   showPopMenu2();
                   break;
               case R.id.ll_taobao_xiaoliang:
                   tvTaobaoXiaoliangSort.setSelected(true);
                   textSelectChange(1);
                   ivUpPrice.setImageResource(R.drawable.up_gray_arraw);
                   ivDownPrice.setImageResource(R.drawable.down_gray_arraw);
                   imgSelectChanger();
                   type ="4";
                   searchTaobaoWare(keyword,city,type,is_tmall,start_price,end_price,page_no);
                   break;
               case R.id.ll_taobao_price:
                   tvTaobaoPriceSort.setSelected(true);
                   textSelectChange(2);
                   price_sort =!price_sort;
                   setPriceSortImg(price_sort);
                   imgSelectChanger();
                   if(price_sort) {
                       type = "5";
                   }else{
                       type = "6";
                   }
                   searchTaobaoWare(keyword,city,type,is_tmall,start_price,end_price,page_no);
                   break;
               case R.id.ll_taobao_shaixuan:
                   tvTaobaoShaixuanSort.setSelected(true);
                   ivTaobaoShaixuanSort.setSelected(true);
                   textSelectChange(3);
                   ivUpPrice.setImageResource(R.drawable.up_gray_arraw);
                   ivDownPrice.setImageResource(R.drawable.down_gray_arraw);
                   ivTaobaoZongheSort.setSelected(false);
                   showPopMenu();
                   break;
               case R.id.rl_taobao_sousuo:
                   searchTaobaoWare(keyword,city,type,is_tmall,start_price,end_price,page_no);
                   break;
           }
        }
    }

    /**
     * 搜索淘宝商品
     */
    private void searchTaobaoWare(String keyword,String city,String type,String is_tmall
    ,String start_price,String end_price,String page_no) {
        map = new HashMap<>();
        map.put("keyword",keyword);
        map.put("city",city);
        map.put("type",type);
        map.put("is_tmall",is_tmall);
        map.put("start_price",start_price);
        map.put("end_price",end_price);
        map.put("page_no",page_no);
        map.put("page_size",page_size);
        netUtil.okHttp2Server2(search_url,map);
    }

    /**
     * 设置升降序价格显示图标
     * @param price_sort
     */
    private void setPriceSortImg(boolean price_sort) {
        if(price_sort) {
            ivUpPrice.setImageResource(R.drawable.up_red_arraw);
            ivDownPrice.setImageResource(R.drawable.down_gray_arraw);
        }else{
            ivUpPrice.setImageResource(R.drawable.up_gray_arraw);
            ivDownPrice.setImageResource(R.drawable.down_red_arraw);
        }
    }

    public void textSelectChange(int pos){
        TextView[] compont_texts = {tvTaobaoZongheSort,tvTaobaoXiaoliangSort,tvTaobaoPriceSort,tvTaobaoShaixuanSort};
        for (int i=0;i<compont_texts.length;i++){
           if(i!=pos) {
               compont_texts[i].setSelected(false);
           }
       }
    }

    public void imgSelectChanger(){
       ivTaobaoShaixuanSort.setSelected(false);
        ivTaobaoZongheSort.setSelected(false);
    }

    private void showPopMenu() {
        if (taobaoShaixuanPopupwindow == null) {
            taobaoShaixuanPopupwindow = new TaobaoShaixuanPopupwindow(getContext());
            taobaoShaixuanPopupwindow.showAsDropDown(llTaobaoShaixuan, 5, 5);
        } else if (taobaoShaixuanPopupwindow != null
                && taobaoShaixuanPopupwindow.isShowing()) {
            taobaoShaixuanPopupwindow.dismiss();
        } else {
            taobaoShaixuanPopupwindow.showAsDropDown(llTaobaoShaixuan, 5, 5);
        }

        taobaoShaixuanPopupwindow.setOnPopupClickListener(new TaobaoShaixuanPopupwindow.OnPopupClickListener() {
            @Override
            public void onPopupClick(String start, String end, boolean check, String c) {
                start_price = start;
                end_price = end;
                if(check) {
                    is_tmall = "1";
                }else{
                    is_tmall = "0";
                }
                city =c;
                searchTaobaoWare(keyword,city,type,is_tmall,start_price,end_price,page_no);
            }
        });
        searchTaobaoWare(keyword,city,type,is_tmall,start_price,end_price,page_no);

    }
    private void showPopMenu2() {
        if (taobaoZonghePopupwindow == null) {
            taobaoZonghePopupwindow = new TaobaoZonghePopupwindow(getContext());
            taobaoZonghePopupwindow.showAsDropDown(llTaobaoZonghe, 5, 5);
        } else if (taobaoZonghePopupwindow != null
                && taobaoZonghePopupwindow.isShowing()) {
            taobaoZonghePopupwindow.dismiss();
        } else {
            taobaoZonghePopupwindow.showAsDropDown(llTaobaoZonghe, 5, 5);
        }

        taobaoZonghePopupwindow.setOnTypeSelectedListener(new TaobaoZonghePopupwindow.OnTypeSelectedListener() {
            @Override
            public void onSelectedPos(int pos) {
                int types = pos+1;
                type = String.valueOf(types);
                searchTaobaoWare(keyword,city,type,is_tmall,start_price,end_price,page_no);
            }
        });

        searchTaobaoWare(keyword,city,type,is_tmall,start_price,end_price,page_no);

    }
}
