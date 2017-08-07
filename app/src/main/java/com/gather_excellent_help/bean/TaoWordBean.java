package com.gather_excellent_help.bean;

/**
 * Created by Dapeng Fang on 2017/8/1.
 */

public class TaoWordBean {

    /**
     * statusCode : 1
     * statusMessage : 生成成功！！！！
     * data : ￥r6bM01f6svw￥
     */

    private int statusCode;
    private String statusMessage;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
