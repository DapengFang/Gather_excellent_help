package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/8/4.
 */

public class HelpRuleBean {

    /**
     * statusCode : 1
     * statusMessage : 请求成功
     * data : [{"app_help_url":"http://admin.210gou.com/app"}]
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
         * app_help_url : http://admin.210gou.com/app
         */

        private String app_help_url;

        public String getApp_help_url() {
            return app_help_url;
        }

        public void setApp_help_url(String app_help_url) {
            this.app_help_url = app_help_url;
        }
    }
}
