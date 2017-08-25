package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/8/24.
 */

public class ApplyMoneyBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"enterAmount":200}]
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
         * enterAmount : 200
         */

        private double enterAmount;

        public double getEnterAmount() {
            return enterAmount;
        }

        public void setEnterAmount(double enterAmount) {
            this.enterAmount = enterAmount;
        }
    }
}
