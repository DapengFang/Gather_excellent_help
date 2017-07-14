package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by ${} on 2017/7/13.
 */

public class HomeRushChangeBean {

    /**
     * cover : {"id":98,"site_id":1,"channel_id":4,"category_id":119,"brand_id":0,"call_index":"","title":"今日新品banner","link_url":"","img_url":"/upload/201707/13/201707131719217910.png","seo_title":"","seo_keywords":"","seo_description":"","tags":"","zhaiyao":"","content":"","sort_id":99,"click":0,"status":0,"is_msg":0,"is_top":0,"is_red":0,"is_hot":0,"is_slide":0,"is_sys":1,"user_name":"admin","like_count":0,"add_time":"2017-07-13T09:19:23Z","update_time":"2017-07-13T09:28:20.2Z","productId":"","couponsUrl":"","couponsGet":0,"couponsSurplus":0,"couponsId":"","couponsPrice":0,"couponsCondition":0,"stock_quantity":0,"market_price":0,"sell_price":0,"tkRate":null,"total_sales":0}
     * item : [{"id":37983,"site_id":1,"channel_id":7,"category_id":53,"brand_id":0,"call_index":"","title":"KEG/韩电 XQB82-C1528AS 8.2公斤全自动家用波轮洗衣机热烘干杀菌","link_url":"http://item.taobao.com/item.htm?id=543748304516","img_url":"http://img4.tbcdn.cn/tfscom/i2/TB1ZdCTRVXXXXbbXpXXXXXXXXXX_!!0-item_pic.jpg","seo_title":"","seo_keywords":"","seo_description":"","tags":"","zhaiyao":"","content":"","sort_id":99,"click":0,"status":0,"is_msg":1,"is_top":0,"is_red":0,"is_hot":0,"is_slide":0,"is_sys":0,"user_name":null,"like_count":0,"add_time":"2017-07-12T09:46:37Z","update_time":"2017-07-12T11:42:18.04Z","total_sales":0,"productId":"543748304516","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":0,"market_price":498,"sell_price":298,"tkRate":null}]
     */

    private CoverBean cover;
    private List<ItemBean> item;

    public CoverBean getCover() {
        return cover;
    }

    public void setCover(CoverBean cover) {
        this.cover = cover;
    }

    public List<ItemBean> getItem() {
        return item;
    }

    public void setItem(List<ItemBean> item) {
        this.item = item;
    }

    public static class CoverBean {
        /**
         * id : 98
         * site_id : 1
         * channel_id : 4
         * category_id : 119
         * brand_id : 0
         * call_index :
         * title : 今日新品banner
         * link_url :
         * img_url : /upload/201707/13/201707131719217910.png
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
         * add_time : 2017-07-13T09:19:23Z
         * update_time : 2017-07-13T09:28:20.2Z
         * productId :
         * couponsUrl :
         * couponsGet : 0
         * couponsSurplus : 0
         * couponsId :
         * couponsPrice : 0
         * couponsCondition : 0
         * stock_quantity : 0
         * market_price : 0
         * sell_price : 0
         * tkRate : null
         * total_sales : 0
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
        private String productId;
        private String couponsUrl;
        private int couponsGet;
        private int couponsSurplus;
        private String couponsId;
        private int couponsPrice;
        private int couponsCondition;
        private int stock_quantity;
        private int market_price;
        private int sell_price;
        private Object tkRate;
        private int total_sales;

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

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getCouponsUrl() {
            return couponsUrl;
        }

        public void setCouponsUrl(String couponsUrl) {
            this.couponsUrl = couponsUrl;
        }

        public int getCouponsGet() {
            return couponsGet;
        }

        public void setCouponsGet(int couponsGet) {
            this.couponsGet = couponsGet;
        }

        public int getCouponsSurplus() {
            return couponsSurplus;
        }

        public void setCouponsSurplus(int couponsSurplus) {
            this.couponsSurplus = couponsSurplus;
        }

        public String getCouponsId() {
            return couponsId;
        }

        public void setCouponsId(String couponsId) {
            this.couponsId = couponsId;
        }

        public int getCouponsPrice() {
            return couponsPrice;
        }

        public void setCouponsPrice(int couponsPrice) {
            this.couponsPrice = couponsPrice;
        }

        public int getCouponsCondition() {
            return couponsCondition;
        }

        public void setCouponsCondition(int couponsCondition) {
            this.couponsCondition = couponsCondition;
        }

        public int getStock_quantity() {
            return stock_quantity;
        }

        public void setStock_quantity(int stock_quantity) {
            this.stock_quantity = stock_quantity;
        }

        public int getMarket_price() {
            return market_price;
        }

        public void setMarket_price(int market_price) {
            this.market_price = market_price;
        }

        public int getSell_price() {
            return sell_price;
        }

        public void setSell_price(int sell_price) {
            this.sell_price = sell_price;
        }

        public Object getTkRate() {
            return tkRate;
        }

        public void setTkRate(Object tkRate) {
            this.tkRate = tkRate;
        }

        public int getTotal_sales() {
            return total_sales;
        }

        public void setTotal_sales(int total_sales) {
            this.total_sales = total_sales;
        }
    }

    public static class ItemBean {
        /**
         * id : 37983
         * site_id : 1
         * channel_id : 7
         * category_id : 53
         * brand_id : 0
         * call_index :
         * title : KEG/韩电 XQB82-C1528AS 8.2公斤全自动家用波轮洗衣机热烘干杀菌
         * link_url : http://item.taobao.com/item.htm?id=543748304516
         * img_url : http://img4.tbcdn.cn/tfscom/i2/TB1ZdCTRVXXXXbbXpXXXXXXXXXX_!!0-item_pic.jpg
         * seo_title :
         * seo_keywords :
         * seo_description :
         * tags :
         * zhaiyao :
         * content :
         * sort_id : 99
         * click : 0
         * status : 0
         * is_msg : 1
         * is_top : 0
         * is_red : 0
         * is_hot : 0
         * is_slide : 0
         * is_sys : 0
         * user_name : null
         * like_count : 0
         * add_time : 2017-07-12T09:46:37Z
         * update_time : 2017-07-12T11:42:18.04Z
         * total_sales : 0
         * productId : 543748304516
         * couponsId :
         * couponsPrice : 0
         * couponsCondition : 0
         * couponsUrl :
         * couponsSurplus : 0
         * couponsGet : 0
         * stock_quantity : 0
         * market_price : 498
         * sell_price : 298
         * tkRate : null
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
        private Object user_name;
        private int like_count;
        private String add_time;
        private String update_time;
        private int total_sales;
        private String productId;
        private String couponsId;
        private int couponsPrice;
        private int couponsCondition;
        private String couponsUrl;
        private int couponsSurplus;
        private int couponsGet;
        private int stock_quantity;
        private int market_price;
        private int sell_price;
        private Object tkRate;

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

        public Object getUser_name() {
            return user_name;
        }

        public void setUser_name(Object user_name) {
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

        public int getTotal_sales() {
            return total_sales;
        }

        public void setTotal_sales(int total_sales) {
            this.total_sales = total_sales;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getCouponsId() {
            return couponsId;
        }

        public void setCouponsId(String couponsId) {
            this.couponsId = couponsId;
        }

        public int getCouponsPrice() {
            return couponsPrice;
        }

        public void setCouponsPrice(int couponsPrice) {
            this.couponsPrice = couponsPrice;
        }

        public int getCouponsCondition() {
            return couponsCondition;
        }

        public void setCouponsCondition(int couponsCondition) {
            this.couponsCondition = couponsCondition;
        }

        public String getCouponsUrl() {
            return couponsUrl;
        }

        public void setCouponsUrl(String couponsUrl) {
            this.couponsUrl = couponsUrl;
        }

        public int getCouponsSurplus() {
            return couponsSurplus;
        }

        public void setCouponsSurplus(int couponsSurplus) {
            this.couponsSurplus = couponsSurplus;
        }

        public int getCouponsGet() {
            return couponsGet;
        }

        public void setCouponsGet(int couponsGet) {
            this.couponsGet = couponsGet;
        }

        public int getStock_quantity() {
            return stock_quantity;
        }

        public void setStock_quantity(int stock_quantity) {
            this.stock_quantity = stock_quantity;
        }

        public int getMarket_price() {
            return market_price;
        }

        public void setMarket_price(int market_price) {
            this.market_price = market_price;
        }

        public int getSell_price() {
            return sell_price;
        }

        public void setSell_price(int sell_price) {
            this.sell_price = sell_price;
        }

        public Object getTkRate() {
            return tkRate;
        }

        public void setTkRate(Object tkRate) {
            this.tkRate = tkRate;
        }
    }
}
