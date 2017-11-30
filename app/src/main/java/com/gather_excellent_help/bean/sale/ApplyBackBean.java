package com.gather_excellent_help.bean.sale;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/30.
 */

public class ApplyBackBean {

    /**
     * statusCode : 1
     * statusMessage : 提交成功，请稍事等待，我们将尽快为您处理！
     * data : [{"orderId":"100000545842","orderItemId":25,"is_apply":2}]
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
         * orderId : 100000545842
         * orderItemId : 25
         * is_apply : 2
         */

        private String orderId;
        private int orderItemId;
        private int is_apply;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getOrderItemId() {
            return orderItemId;
        }

        public void setOrderItemId(int orderItemId) {
            this.orderItemId = orderItemId;
        }

        public int getIs_apply() {
            return is_apply;
        }

        public void setIs_apply(int is_apply) {
            this.is_apply = is_apply;
        }
    }
}
