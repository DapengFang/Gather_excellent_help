package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/2.
 */

public class AddressDetailBean {

    /**
     * statusCode : 1
     * statusMessage : 获取地址成功！
     * data : [{"id":1,"user_name":"test","accept_name":"测试人","area":"广东省,深圳市,宝安区","address":"西乡街道西乡街道33号","mobile":"13800138000","telphone":"","email":"test@qq.com","post_code":"","area_id":null,"is_default":0,"add_time":"2017-05-04T01:56:08.71"},{"id":2,"user_name":"100001","accept_name":"丑八怪","area":"四川,成都,高新","address":"黄金时代","mobile":"","telphone":"18328015749","email":"","post_code":"636049","area_id":"12,13,14","is_default":1,"add_time":"2017-11-02T10:28:42.25"},{"id":3,"user_name":"18514792343","accept_name":"小明","area":"云南省,西双版纳州,景洪市,主城区","address":"f区1栋二单元501","mobile":"18870882835","telphone":"18870882835","email":"","post_code":"123654","area_id":"240,691,01","is_default":1,"add_time":"2017-11-02T10:29:35.723"},{"id":4,"user_name":"18514792343","accept_name":"小敏","area":"内蒙古,呼伦贝尔市,海拉尔区,正阳街道","address":"10栋二单元","mobile":"15679462354","telphone":"15679462354","email":"","post_code":"123654","area_id":"40,470,01","is_default":1,"add_time":"2017-11-02T10:37:13.253"}]
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
         * id : 1l
         * user_name : test
         * accept_name : 测试人
         * area : 广东省,深圳市,宝安区
         * address : 西乡街道西乡街道33号
         * mobile : 13800138000
         * telphone :
         * email : test@qq.com
         * post_code :
         * area_id : null
         * is_default : 0
         * add_time : 2017-05-04T01:56:08.71
         */

        private int id;
        private String user_name;
        private String accept_name;
        private String area;
        private String address;
        private String mobile;
        private String telphone;
        private String email;
        private String post_code;
        private String area_id;
        private int is_default;
        private String add_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getAccept_name() {
            return accept_name;
        }

        public void setAccept_name(String accept_name) {
            this.accept_name = accept_name;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getTelphone() {
            return telphone;
        }

        public void setTelphone(String telphone) {
            this.telphone = telphone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPost_code() {
            return post_code;
        }

        public void setPost_code(String post_code) {
            this.post_code = post_code;
        }

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public int getIs_default() {
            return is_default;
        }

        public void setIs_default(int is_default) {
            this.is_default = is_default;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}
