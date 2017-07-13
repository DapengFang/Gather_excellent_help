package com.gather_excellent_help.bean;

import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.MediaType;

/**
 * Created by wuxin on 2017/7/12.
 */

public class HomeTypeBean {
    private int id;
    private String typeName;
    private String typeImg;

    public HomeTypeBean(int id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }


    public HomeTypeBean(String typeName, String typeImg) {
        this.typeName = typeName;
        this.typeImg = typeImg;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeImg() {
        return typeImg;
    }

    public void setTypeImg(String typeImg) {
        this.typeImg = typeImg;
    }
}
