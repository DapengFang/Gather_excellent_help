package com.gather_excellent_help.bean.suning.netcart;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/12/6.
 */

public class NetGoodsDeleteBean {

    /**
     * statusCode : 1
     * statusMessage : 商品移除成功！
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
