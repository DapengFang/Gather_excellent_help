package com.gather_excellent_help.bean.suning;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/7.
 */

public class SuningStockBean {


    /**
     * statusCode : 1
     * statusMessage : 获取库存成功！
     * data : [{"store_text":"现货"}]
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
         * store_text : 现货
         */

        private int store_status;
        private String store_text;

        public int getStore_status() {
            return store_status;
        }

        public void setStore_status(int store_status) {
            this.store_status = store_status;
        }

        public String getStore_text() {
            return store_text;
        }

        public void setStore_text(String store_text) {
            this.store_text = store_text;
        }

    }
}
