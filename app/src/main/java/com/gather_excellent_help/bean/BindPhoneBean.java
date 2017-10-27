package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/10/27.
 */

public class BindPhoneBean {

    /**
     * statusCode : 1
     * statusMessage : 与已有账号绑定成功
     * data : [{"is_exist":1}]
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
         * is_exist : 1
         */

        private int is_exist;

        public int getIs_exist() {
            return is_exist;
        }

        public void setIs_exist(int is_exist) {
            this.is_exist = is_exist;
        }
    }
}
