package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by ${} on 2017/7/20.
 */

public class CodeStatueBean {

    /**
     * statusCode : 1
     * statusMessage : 解绑支付宝成功
     * data : []
     */

    private int statusCode;
    private String statusMessage;
    private List<?> data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
