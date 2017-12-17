package com.gather_excellent_help.bean.suning;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/12/4.
 */

public class SuningLimitBean {


    /**
     * statusCode : 1
     * statusMessage : 限购详情获取成功！
     * data : [{"limit_num":30,"Purchased_num":20,"cancel_order_num":2}]
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
         * limit_num : 30
         * Purchased_num : 20
         * cancel_order_num : 2
         */

        private int limit_num;
        private int Purchased_num;
        private int cancel_order_num;

        public int getLimit_num() {
            return limit_num;
        }

        public void setLimit_num(int limit_num) {
            this.limit_num = limit_num;
        }

        public int getPurchased_num() {
            return Purchased_num;
        }

        public void setPurchased_num(int Purchased_num) {
            this.Purchased_num = Purchased_num;
        }

        public int getCancel_order_num() {
            return cancel_order_num;
        }

        public void setCancel_order_num(int cancel_order_num) {
            this.cancel_order_num = cancel_order_num;
        }
    }
}
