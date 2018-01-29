package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by wuxin on 2017/7/12.
 */

public class HomeBannerBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"site_id":2,"article_id":4136,"banner_activity_id":0,"activity_id":52,"title":"MegCook嵌入式电磁炉 C22MDA02-C01 多功能家用 不产生油烟不糊锅 精准控温 自动烹饪智能灶双灶","link_url":"https://product.suning.com/0000000000/615300868.html","activity_img":"http://image5.suning.cn/uimg/b2c/newcatentries/0000000000-000000000615300868_1_800x800.jpg","img_url":"http://image1.suning.cn/uimg/b2c/newcatentries/0000000000-000000000615300868_1_400x400.jpg","productId":"615300868","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":0,"market_price":3899,"sell_price":3899,"tkRate":2.56,"commission_rate":0,"suning_rate":0.0256},{"site_id":1,"article_id":1632,"banner_activity_id":0,"activity_id":52,"title":"海尔冰箱137升家用小型静音节能双门BCD-137TMPF","link_url":"https://detail.tmall.com/item.htm?id=534901891026&spm=a219t.7664554.1998457203.288.45356133EV01vX","activity_img":"/upload/201801/05/201801051727550318.jpg","img_url":"http://img.alicdn.com/imgextra/i3/2465480399/TB1BqwDlbYI8KJjy0FaXXbAiVXa_!!0-item_pic.jpg","productId":"534901891026","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":351,"market_price":1049,"sell_price":999,"tkRate":4,"commission_rate":1,"suning_rate":0}]
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
         * site_id : 2
         * article_id : 4136
         * banner_activity_id : 0
         * activity_id : 52
         * title : MegCook嵌入式电磁炉 C22MDA02-C01 多功能家用 不产生油烟不糊锅 精准控温 自动烹饪智能灶双灶
         * link_url : https://product.suning.com/0000000000/615300868.html
         * activity_img : http://image5.suning.cn/uimg/b2c/newcatentries/0000000000-000000000615300868_1_800x800.jpg
         * img_url : http://image1.suning.cn/uimg/b2c/newcatentries/0000000000-000000000615300868_1_400x400.jpg
         * productId : 615300868
         * couponsId :
         * couponsPrice : 0.0
         * couponsCondition : 0.0
         * couponsUrl :
         * couponsSurplus : 0
         * couponsGet : 0
         * stock_quantity : 0
         * market_price : 3899.0
         * sell_price : 3899.0
         * tkRate : 2.56
         * commission_rate : 0.0
         * suning_rate : 0.0256
         */

        private int site_id;
        private int article_id;
        private int banner_activity_id;
        private int activity_id;
        private String title;
        private String link_url;
        private String activity_img;
        private String img_url;
        private String productId;
        private String couponsId;
        private double couponsPrice;
        private double couponsCondition;
        private String couponsUrl;
        private int couponsSurplus;
        private int couponsGet;
        private int stock_quantity;
        private double market_price;
        private double sell_price;
        private double tkRate;
        private double commission_rate;
        private double suning_rate;

        public int getSite_id() {
            return site_id;
        }

        public void setSite_id(int site_id) {
            this.site_id = site_id;
        }

        public int getArticle_id() {
            return article_id;
        }

        public void setArticle_id(int article_id) {
            this.article_id = article_id;
        }

        public int getBanner_activity_id() {
            return banner_activity_id;
        }

        public void setBanner_activity_id(int banner_activity_id) {
            this.banner_activity_id = banner_activity_id;
        }

        public int getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(int activity_id) {
            this.activity_id = activity_id;
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

        public String getActivity_img() {
            return activity_img;
        }

        public void setActivity_img(String activity_img) {
            this.activity_img = activity_img;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
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

        public double getCouponsPrice() {
            return couponsPrice;
        }

        public void setCouponsPrice(double couponsPrice) {
            this.couponsPrice = couponsPrice;
        }

        public double getCouponsCondition() {
            return couponsCondition;
        }

        public void setCouponsCondition(double couponsCondition) {
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

        public double getMarket_price() {
            return market_price;
        }

        public void setMarket_price(double market_price) {
            this.market_price = market_price;
        }

        public double getSell_price() {
            return sell_price;
        }

        public void setSell_price(double sell_price) {
            this.sell_price = sell_price;
        }

        public double getTkRate() {
            return tkRate;
        }

        public void setTkRate(double tkRate) {
            this.tkRate = tkRate;
        }

        public double getCommission_rate() {
            return commission_rate;
        }

        public void setCommission_rate(double commission_rate) {
            this.commission_rate = commission_rate;
        }

        public double getSuning_rate() {
            return suning_rate;
        }

        public void setSuning_rate(double suning_rate) {
            this.suning_rate = suning_rate;
        }
    }
}
