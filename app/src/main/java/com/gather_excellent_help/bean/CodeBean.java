package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by wuxin on 2017/7/10.
 */

public class CodeBean {

    /**
     * statusCode : 1
     * statusMessage : 注册成功
     * data : [{"id":7}]
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
         * id : 7
         */

        private Integer id;
        private int group_type;
        private double user_get_ratio;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public int getGroup_type() {
            return group_type;
        }

        public void setGroup_type(int group_type) {
            this.group_type = group_type;
        }

        public double getUser_get_ratio() {
            return user_get_ratio;
        }

        public void setUser_get_ratio(double user_get_ratio) {
            this.user_get_ratio = user_get_ratio;
        }
    }
}
