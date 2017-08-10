package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by wuxin on 2017/7/11.
 */

public class MineBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"avatar":"https://wwc.alicdn.com/avatar/getAvatar.do?userId=2638987498&width=160&height=160&type=sns","nick_name":"呆萌小方","sex":"保密","group_id":4,"advertising":"121866255","group":"实体商家","mobile":"18514792343","amount":202.04,"frostAmount":98.68}]
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
         * avatar : https://wwc.alicdn.com/avatar/getAvatar.do?userId=2638987498&width=160&height=160&type=sns
         * nick_name : 呆萌小方
         * sex : 保密
         * group_id : 4
         * advertising : 121866255
         * group : 实体商家
         * mobile : 18514792343
         * amount : 202.04
         * frostAmount : 98.68
         */

        private String avatar;
        private String nick_name;
        private String sex;
        private int group_id;
        private String advertising;
        private String group;
        private String mobile;
        private double amount;
        private double frostAmount;
        private double user_earn;
        private String user_get_ratio;


        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public String getAdvertising() {
            return advertising;
        }

        public void setAdvertising(String advertising) {
            this.advertising = advertising;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getFrostAmount() {
            return frostAmount;
        }

        public void setFrostAmount(double frostAmount) {
            this.frostAmount = frostAmount;
        }

        public double getUser_earn() {
            return user_earn;
        }

        public void setUser_earn(double user_earn) {
            this.user_earn = user_earn;
        }

        public String getUser_get_ratio() {
            return user_get_ratio;
        }

        public void setUser_get_ratio(String user_get_ratio) {
            this.user_get_ratio = user_get_ratio;
        }
    }
}
