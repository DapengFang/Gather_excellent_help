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
        private String advertising;
        private int group_id;
        private int apply_status;
        private int pay_status;
        private String wechat_id;
        private int is_phone;
        private String use_phone;
        private String token;

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

        public String getAdvertising() {
            return advertising;
        }

        public void setAdvertising(String advertising) {
            this.advertising = advertising;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public int getApply_status() {
            return apply_status;
        }

        public void setApply_status(int apply_status) {
            this.apply_status = apply_status;
        }

        public int getPay_status() {
            return pay_status;
        }

        public void setPay_status(int pay_status) {
            this.pay_status = pay_status;
        }

        public String getWechat_id() {
            return wechat_id;
        }

        public void setWechat_id(String wechat_id) {
            this.wechat_id = wechat_id;
        }

        public int getIs_phone() {
            return is_phone;
        }

        public void setIs_phone(int is_phone) {
            this.is_phone = is_phone;
        }

        public String getUse_phone() {
            return use_phone;
        }

        public void setUse_phone(String use_phone) {
            this.use_phone = use_phone;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
