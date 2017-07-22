package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by wuxin on 2017/7/12.
 */

public class HomeBannerBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"activity_id":52,"title":"机单甩 家用大容量不锈钢甩干桶 非小洗衣机脱水机甩干","link_url":"http://item.taobao.com/item.htm?id=553647861393","activity_img":"/upload/201707/21/201707211418067089.jpg","img_url":"http://img02.taobaocdn.com/bao/uploaded/i2/TB1pYVERVXXXXa4aXXXXXXXXXXX_!!0-item_pic.jpg","productId":"553647861393","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":72,"market_price":394,"sell_price":394,"tkRate":20}]
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
         * activity_id : 52
         * title : 机单甩 家用大容量不锈钢甩干桶 非小洗衣机脱水机甩干
         * link_url : http://item.taobao.com/item.htm?id=553647861393
         * activity_img : /upload/201707/21/201707211418067089.jpg
         * img_url : http://img02.taobaocdn.com/bao/uploaded/i2/TB1pYVERVXXXXa4aXXXXXXXXXXX_!!0-item_pic.jpg
         * productId : 553647861393
         * couponsId :
         * couponsPrice : 0
         * couponsCondition : 0
         * couponsUrl :
         * couponsSurplus : 0
         * couponsGet : 0
         * stock_quantity : 72
         * market_price : 394
         * sell_price : 394
         * tkRate : 20
         */

        private int activity_id;
        private String title;
        private String link_url;
        private String activity_img;
        private String img_url;
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
        private int tkRate;

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

        public int getTkRate() {
            return tkRate;
        }

        public void setTkRate(int tkRate) {
            this.tkRate = tkRate;
        }
    }
}
