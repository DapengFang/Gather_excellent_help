package com.gather_excellent_help.bean;

import java.io.Serializable;

/**
 * Created by Dapeng Fang on 2017/8/28.
 */

public class ShopInfoNativeBean implements Serializable {
    private int id;
    private String address;
    private String shopDetail;
    private String phone;
    private String shopName;
    private String time;

    public ShopInfoNativeBean() {
    }

    public ShopInfoNativeBean(int id, String address, String shopDetail, String phone, String shopName, String time) {
        this.id = id;
        this.address = address;
        this.shopDetail = shopDetail;
        this.phone = phone;
        this.shopName = shopName;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopDetail() {
        return shopDetail;
    }

    public void setShopDetail(String shopDetail) {
        this.shopDetail = shopDetail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ShopInfoNativeBean{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", shopDetail='" + shopDetail + '\'' +
                ", phone='" + phone + '\'' +
                ", shopName='" + shopName + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
