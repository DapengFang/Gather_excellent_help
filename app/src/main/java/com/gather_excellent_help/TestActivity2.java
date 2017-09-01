package com.gather_excellent_help;

import android.os.Bundle;
import android.widget.Toast;

import com.gather_excellent_help.bean.address.Area;
import com.gather_excellent_help.db.DBhelper;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.ui.widget.AddressSelector;
import com.gather_excellent_help.utils.LogUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestActivity2 extends BaseActivity {

    @Bind(R.id.add_test_selector)
    AddressSelector addTestSelector;

    private DBhelper db;
    private ArrayList<Area> city;
    private ArrayList<Area> district;

    private String p = "";
    private String c = "";
    private String s = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        ButterKnife.bind(this);
        initData();
    }

    private void initData(){
        db = new DBhelper(this);
        final ArrayList<Area> province = db.getProvince();
        addTestSelector.setTabAmount(3);
        addTestSelector.setCities(province,"p");
        addTestSelector.setOnItemClickListener(new AddressSelector.OnItemClickListener() {
            @Override
            public void itemClick(AddressSelector addressSelector, Area area, int tabPosition, int position) {
                switch (tabPosition){
                    case 0:
                        if(province!=null) {
                            Area curr = province.get(position);
                            String pcode = curr.getCode();
                            city = db.getCity(pcode);
                            addressSelector.setCities(city,"c");
                            p = curr.getName();
                        }
                        break;
                    case 1:
                        if(city!=null) {
                            Area curr1 = city.get(position);
                            String pcode1 = curr1.getCode();
                            district = db.getDistrict(pcode1);
                            addressSelector.setCities(district,"s");
                            c = curr1.getName();
                        }
                        break;
                    case 2:
                        if(district!=null) {
                            Area curr2 = district.get(position);
                            String s = curr2.getName();
                            String address = "";
                            if(p.equals(c)) {
                                address = p + s;
                            }else{
                                address = p + c + s;
                            }
                            Toast.makeText(TestActivity2.this,"tabPosition ："+tabPosition+" "+ address,Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });

        addTestSelector.setOnTabSelectedListener(new AddressSelector.OnTabSelectedListener() {
            @Override
            public void onTabSelected(AddressSelector addressSelector, AddressSelector.Tab tab) {
                switch (tab.getIndex()){
                    case 0:
                        addressSelector.setCities(province,"p");
                        break;
                    case 1:
                        if(city!=null) {
                            addressSelector.setCities(city,"c");
                        }
                        break;
                    case 2:
                       if(district!=null) {
                           addressSelector.setCities(district,"s");
                       }
                        break;
                }
            }

            @Override
            public void onTabReselected(AddressSelector addressSelector, AddressSelector.Tab tab) {

            }
        });

    }
}
