package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/8/4.
 */

public class WardStaticsBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"users_name":"18514792343","count":2,"total":1.77}]
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
         * users_name : 18514792343
         * count : 2
         * total : 1.77
         */

        private String users_name;
        private int count;
        private double total;

        public String getUsers_name() {
            return users_name;
        }

        public void setUsers_name(String users_name) {
            this.users_name = users_name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }
    }
}
