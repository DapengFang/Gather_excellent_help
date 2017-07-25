package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by wuxin on 2017/7/11.
 */

public class MineBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"avatar":"https://wwc.alicdn.com/avatar/getAvatar.do?userId=2638987498&width=160&height=160&type=sns","nick_name":"呆萌小方","sex":"","group_id":1,"group":"普通会员","mobile":"18514792343"}]
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
         * sex :
         * group_id : 1
         * group : 普通会员
         * mobile : 18514792343
         */

        private String avatar;
        private String nick_name;
        private String sex;
        private int group_id;
        private String group;
        private String mobile;

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
    }
}
