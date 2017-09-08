package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/9/7.
 */

public class LowerMermberBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"users_name":"大石杨三娃","count":0,"deal_total":0,"total":0},{"users_name":"","count":0,"deal_total":0,"total":0},{"users_name":"","count":0,"deal_total":0,"total":0},{"users_name":"","count":0,"deal_total":0,"total":0},{"users_name":"四川自贡市富顺县陈小梅","count":0,"deal_total":0,"total":0},{"users_name":"四川泸县玄滩镇王文树","count":0,"deal_total":0,"total":0},{"users_name":"四川泸州白米场上邓兴玉","count":0,"deal_total":0,"total":0},{"users_name":"四川合江县白鹿镇赵忠良","count":0,"deal_total":0,"total":0},{"users_name":"四川泸州白米胡志兵","count":0,"deal_total":0,"total":0},{"users_name":"四川泸州太平镇罗红波","count":0,"deal_total":0,"total":0}]
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
         * users_name : 大石杨三娃
         * count : 0
         * deal_total : 0.0
         * total : 0.0
         */

        private String users_name;
        private int count;
        private double deal_total;
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

        public double getDeal_total() {
            return deal_total;
        }

        public void setDeal_total(double deal_total) {
            this.deal_total = deal_total;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }
    }
}
