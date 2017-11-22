package com.gather_excellent_help.bean.suning;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/9.
 */

public class SuningOrderConfirmBean {

    /**
     * statusCode : 1
     * statusMessage : 获取库存成功！
     * data : [{"store_text":"现货"}]
     */

    private int statusCode;
    private String statusMessage;
    private List<SuningOrderConfirmBean.DataBean> data;

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

    public List<SuningOrderConfirmBean.DataBean> getData() {
        return data;
    }

    public void setData(List<SuningOrderConfirmBean.DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * orderStatus : 3
         */

        private int orderStatus;

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }
    }
}
