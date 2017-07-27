package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/7/26.
 */

public class ShopDetailBean {


    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"name":"某某旗舰店","store_url":"/upload/1/.png","telephone":"028-2222222","groupId":5,"business":"电视机,洗衣机","StarBusinessTime":"9:00","EndBusinessTime":"21:00","info":"暂无介绍","address":"四川省成都市新津县邓双镇邓双派出所邓公警务室1号","user_id":"18514792343"}]
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
         * name : 某某旗舰店
         * store_url : /upload/1/.png
         * telephone : 028-2222222
         * groupId : 5
         * business : 电视机,洗衣机
         * StarBusinessTime : 9:00
         * EndBusinessTime : 21:00
         * info : 暂无介绍
         * address : 四川省成都市新津县邓双镇邓双派出所邓公警务室1号
         * user_id : 18514792343
         */

        private String name;
        private String store_url;
        private String telephone;
        private int groupId;
        private String business;
        private String StarBusinessTime;
        private String EndBusinessTime;
        private String info;
        private String address;
        private String user_id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStore_url() {
            return store_url;
        }

        public void setStore_url(String store_url) {
            this.store_url = store_url;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public String getStarBusinessTime() {
            return StarBusinessTime;
        }

        public void setStarBusinessTime(String StarBusinessTime) {
            this.StarBusinessTime = StarBusinessTime;
        }

        public String getEndBusinessTime() {
            return EndBusinessTime;
        }

        public void setEndBusinessTime(String EndBusinessTime) {
            this.EndBusinessTime = EndBusinessTime;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
