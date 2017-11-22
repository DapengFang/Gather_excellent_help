package com.gather_excellent_help.bean.suning;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/7.
 */

public class SuningFreeBean {

    /**
     * statusCode : 1
     * statusMessage : 运费获取成功！
     * data : [{"freightFare":0}]
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
         * freightFare : 0.0
         */

        private double freightFare;

        public double getFreightFare() {
            return freightFare;
        }

        public void setFreightFare(double freightFare) {
            this.freightFare = freightFare;
        }
    }
}
