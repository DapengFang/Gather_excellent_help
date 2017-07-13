package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by wuxin on 2017/7/12.
 */

public class HomeBannerBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"id":57,"site_id":1,"channel_id":4,"category_id":52,"brand_id":0,"call_index":"","title":"banner3","link_url":"https://detail.tmall.com/item.htm?id=535414640225&spm=a219t.7664554.1998457203.95.hjb0P4","img_url":"/upload/201707/10/201707101523119873.jpg","seo_title":"","seo_keywords":"","seo_description":"","tags":"","zhaiyao":"","content":"","sort_id":99,"click":0,"status":0,"is_msg":0,"is_top":0,"is_red":0,"is_hot":0,"is_slide":0,"is_sys":1,"user_name":"admin","like_count":0,"add_time":"2017-07-10T07:22:52Z","update_time":"2017-07-11T07:27:33.39Z"},{"id":56,"site_id":1,"channel_id":4,"category_id":52,"brand_id":0,"call_index":"","title":"banner2","link_url":"https://detail.tmall.com/item.htm?id=535414640225&spm=a219t.7664554.1998457203.95.hjb0P4","img_url":"/upload/201707/10/201707101523047148.jpg","seo_title":"","seo_keywords":"","seo_description":"","tags":"","zhaiyao":"","content":"","sort_id":99,"click":0,"status":0,"is_msg":0,"is_top":0,"is_red":0,"is_hot":0,"is_slide":0,"is_sys":1,"user_name":"admin","like_count":0,"add_time":"2017-07-10T07:22:43Z","update_time":"2017-07-11T07:27:37.73Z"},{"id":55,"site_id":1,"channel_id":4,"category_id":52,"brand_id":0,"call_index":"","title":"banner1","link_url":"https://detail.tmall.com/item.htm?id=535414640225&spm=a219t.7664554.1998457203.95.hjb0P4","img_url":"/upload/201707/10/201707101522130449.jpg","seo_title":"","seo_keywords":"","seo_description":"","tags":"","zhaiyao":"","content":"","sort_id":99,"click":0,"status":0,"is_msg":0,"is_top":0,"is_red":0,"is_hot":0,"is_slide":0,"is_sys":1,"user_name":"admin","like_count":0,"add_time":"2017-07-10T07:22:14Z","update_time":"2017-07-11T07:27:43.747Z"}]
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
         * id : 57
         * site_id : 1
         * channel_id : 4
         * category_id : 52
         * brand_id : 0
         * call_index :
         * title : banner3
         * link_url : https://detail.tmall.com/item.htm?id=535414640225&spm=a219t.7664554.1998457203.95.hjb0P4
         * img_url : /upload/201707/10/201707101523119873.jpg
         * seo_title :
         * seo_keywords :
         * seo_description :
         * tags :
         * zhaiyao :
         * content :
         * sort_id : 99
         * click : 0
         * status : 0
         * is_msg : 0
         * is_top : 0
         * is_red : 0
         * is_hot : 0
         * is_slide : 0
         * is_sys : 1
         * user_name : admin
         * like_count : 0
         * add_time : 2017-07-10T07:22:52Z
         * update_time : 2017-07-11T07:27:33.39Z
         */

        private int id;
        private int site_id;
        private int channel_id;
        private int category_id;
        private int brand_id;
        private String call_index;
        private String title;
        private String link_url;
        private String img_url;
        private String seo_title;
        private String seo_keywords;
        private String seo_description;
        private String tags;
        private String zhaiyao;
        private String content;
        private int sort_id;
        private int click;
        private int status;
        private int is_msg;
        private int is_top;
        private int is_red;
        private int is_hot;
        private int is_slide;
        private int is_sys;
        private String user_name;
        private int like_count;
        private String add_time;
        private String update_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSite_id() {
            return site_id;
        }

        public void setSite_id(int site_id) {
            this.site_id = site_id;
        }

        public int getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(int channel_id) {
            this.channel_id = channel_id;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public int getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(int brand_id) {
            this.brand_id = brand_id;
        }

        public String getCall_index() {
            return call_index;
        }

        public void setCall_index(String call_index) {
            this.call_index = call_index;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink_url() {
            return link_url;
        }

        public void setLink_url(String link_url) {
            this.link_url = link_url;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getSeo_title() {
            return seo_title;
        }

        public void setSeo_title(String seo_title) {
            this.seo_title = seo_title;
        }

        public String getSeo_keywords() {
            return seo_keywords;
        }

        public void setSeo_keywords(String seo_keywords) {
            this.seo_keywords = seo_keywords;
        }

        public String getSeo_description() {
            return seo_description;
        }

        public void setSeo_description(String seo_description) {
            this.seo_description = seo_description;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getZhaiyao() {
            return zhaiyao;
        }

        public void setZhaiyao(String zhaiyao) {
            this.zhaiyao = zhaiyao;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getSort_id() {
            return sort_id;
        }

        public void setSort_id(int sort_id) {
            this.sort_id = sort_id;
        }

        public int getClick() {
            return click;
        }

        public void setClick(int click) {
            this.click = click;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIs_msg() {
            return is_msg;
        }

        public void setIs_msg(int is_msg) {
            this.is_msg = is_msg;
        }

        public int getIs_top() {
            return is_top;
        }

        public void setIs_top(int is_top) {
            this.is_top = is_top;
        }

        public int getIs_red() {
            return is_red;
        }

        public void setIs_red(int is_red) {
            this.is_red = is_red;
        }

        public int getIs_hot() {
            return is_hot;
        }

        public void setIs_hot(int is_hot) {
            this.is_hot = is_hot;
        }

        public int getIs_slide() {
            return is_slide;
        }

        public void setIs_slide(int is_slide) {
            this.is_slide = is_slide;
        }

        public int getIs_sys() {
            return is_sys;
        }

        public void setIs_sys(int is_sys) {
            this.is_sys = is_sys;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public int getLike_count() {
            return like_count;
        }

        public void setLike_count(int like_count) {
            this.like_count = like_count;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }
    }
}
