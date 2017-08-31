package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/8/29.
 */

public class VersionBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"AppVersion":"1.0.1"}]
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
         * AppVersion : 1.0.1
         */

        private String AppVersion;

        public String getAppVersion() {
            return AppVersion;
        }

        public void setAppVersion(String AppVersion) {
            this.AppVersion = AppVersion;
        }
    }
}
