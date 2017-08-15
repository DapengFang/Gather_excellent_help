package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by ${} on 2017/7/17.
 */


public class TyepIndexBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"id":113,"title":"热水器"},{"id":107,"title":"冷柜"},{"id":102,"title":"烟灶消"},{"id":95,"title":"冰箱"},{"id":76,"title":"小家电"},{"id":70,"title":"空调"},{"id":61,"title":"电视机"},{"id":53,"title":"洗衣机"}]
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
         * id : 113
         * title : 热水器
         */

        private int id;
        private String title;
        private String img_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
    }
}
