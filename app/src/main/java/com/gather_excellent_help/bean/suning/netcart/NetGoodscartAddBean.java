package com.gather_excellent_help.bean.suning.netcart;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/12/6.
 */

public class NetGoodscartAddBean {

    /**
     * statusCode : 1
     * statusMessage : 添加成功，在购物车等亲！
     * data : [{"cart_id":8}]
     */

    private int statusCode;
    private String statusMessage;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * cart_id : 8
         */

        private int cart_id;

        public int getCart_id() {
            return cart_id;
        }

        public void setCart_id(int cart_id) {
            this.cart_id = cart_id;
        }
    }
}
