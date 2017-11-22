package com.gather_excellent_help.bean.suning;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/8.
 */

public class SuningPaystateBean {

    /**
     * statusCode : 1
     * statusMessage : 支付成功！
     * data : [{"pay_text":"已支付"}]
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
         * pay_text : 已支付
         */

        private String pay_text;

        public String getPay_text() {
            return pay_text;
        }

        public void setPay_text(String pay_text) {
            this.pay_text = pay_text;
        }
    }
}
