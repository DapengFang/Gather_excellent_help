package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/8/10.
 */

public class NewsListBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"id":14,"category_id":129,"category_name":"公益","title":"1座村小，60名学生，他们希望山村也能响起上课铃","img_url":"","click":0,"add_time":"2017-08-10T14:26:57.693"},{"id":5,"category_id":129,"category_name":"公益","title":"夫妻九寨沟遇难:母亲被巨石砸中瞬间护住女儿","img_url":"","click":0,"add_time":"2017-08-10T13:12:30"},{"id":3,"category_id":129,"category_name":"公益","title":"截至8月10日九寨沟县地震已致20人遇难 431人受伤","img_url":"","click":0,"add_time":"2017-08-10T12:58:28"},{"id":2,"category_id":129,"category_name":"公益","title":"九寨沟震后24小时6万人大转移 平武成安全岛(图)","img_url":"","click":0,"add_time":"2017-08-10T12:58:07"},{"id":1,"category_id":129,"category_name":"公益","title":"四川发布地灾风险3级预警 29个地区需警惕","img_url":"","click":0,"add_time":"2017-08-10T11:28:11"}]
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
         * id : 14
         * category_id : 129
         * category_name : 公益
         * title : 1座村小，60名学生，他们希望山村也能响起上课铃
         * img_url :
         * click : 0
         * add_time : 2017-08-10T14:26:57.693
         */

        private int id;
        private int category_id;
        private String category_name;
        private String title;
        private String img_url;
        private int click;
        private String add_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public int getClick() {
            return click;
        }

        public void setClick(int click) {
            this.click = click;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}
