package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by ${} on 2017/7/17.
 */


public class HomeGroupBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"activity_id":124,"title":"半全自动宿舍微型小洗衣机单筒迷你洗衣机带甩干脱水洗脱小型儿童","link_url":"http://item.taobao.com/item.htm?id=553584340223","activity_img":"","img_url":"http://img03.taobaocdn.com/bao/uploaded/i3/TB1.RJORVXXXXcmXFXXXXXXXXXX_!!0-item_pic.jpg","productId":"553584340223","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","secondCouponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":2154,"market_price":214,"sell_price":214,"tkRate":20},{"activity_id":124,"title":"中欧高端16/15kg大型双桶双缸半自动洗衣机 宾馆旅馆商用家用杀菌","link_url":"http://item.taobao.com/item.htm?id=552462681459","activity_img":"","img_url":"http://img4.tbcdn.cn/tfscom/i4/2597757991/TB24QoxhStkpuFjy0FhXXXQzFXa_!!2597757991.jpg","productId":"552462681459","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","secondCouponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":0,"market_price":498,"sell_price":298,"tkRate":0},{"activity_id":124,"title":"单筒单桶大容量半全自动小型迷你洗衣机 带甩干脱水家用波轮紫光","link_url":"http://item.taobao.com/item.htm?id=553928466888","activity_img":"","img_url":"http://img03.taobaocdn.com/bao/uploaded/i3/TB1gG9hPFXXXXclapXXXXXXXXXX_!!0-item_pic.jpg","productId":"553928466888","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","secondCouponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":583,"market_price":405,"sell_price":405,"tkRate":20},{"activity_id":124,"title":"9.2KG公斤双筒双缸半自动大容量家用洗衣机带甩干新品上市","link_url":"http://item.taobao.com/item.htm?id=552537639139","activity_img":"","img_url":"http://img01.taobaocdn.com/bao/uploaded/i1/TB1_M6TLpXXXXXvXpXXXXXXXXXX_!!1-item_pic.gif","productId":"552537639139","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","secondCouponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":2901,"market_price":1627,"sell_price":1138.9,"tkRate":20},{"activity_id":124,"title":"威力XPB30-3038S家用小型迷你洗衣机双桶半自动甩干婴儿儿童宝宝","link_url":"http://item.taobao.com/item.htm?id=531718442513","activity_img":"","img_url":"http://img2.tbcdn.cn/tfscom/i1/TB1gTukRXXXXXcDXVXXXXXXXXXX_!!0-item_pic.jpg","productId":"531718442513","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","secondCouponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":0,"market_price":498,"sell_price":298,"tkRate":0}]
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
         * activity_id : 124
         * title : 半全自动宿舍微型小洗衣机单筒迷你洗衣机带甩干脱水洗脱小型儿童
         * link_url : http://item.taobao.com/item.htm?id=553584340223
         * activity_img :
         * img_url : http://img03.taobaocdn.com/bao/uploaded/i3/TB1.RJORVXXXXcmXFXXXXXXXXXX_!!0-item_pic.jpg
         * productId : 553584340223
         * couponsId :
         * couponsPrice : 0
         * couponsCondition : 0
         * couponsUrl :
         * secondCouponsUrl :
         * couponsSurplus : 0
         * couponsGet : 0
         * stock_quantity : 2154
         * market_price : 214
         * sell_price : 214
         * tkRate : 20
         * commission_rate:1
         */

        private int site_id;
        private int article_id;
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
        private String secondCouponsUrl;
        private int couponsSurplus;
        private int couponsGet;
        private int stock_quantity;
        private double market_price;
        private double sell_price;
        private double tkRate;
        private double commission_rate;

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

        public String getSecondCouponsUrl() {
            return secondCouponsUrl;
        }

        public void setSecondCouponsUrl(String secondCouponsUrl) {
            this.secondCouponsUrl = secondCouponsUrl;
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
    }
}
