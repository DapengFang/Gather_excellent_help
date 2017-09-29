package com.gather_excellent_help.bean;
/**
 * Created by ${} on 2017/7/17.
 */

import java.util.List;


public class SearchWareBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"title":"机单甩 家用大容量不锈钢甩干桶 非小洗衣机脱水机甩干","link_url":"http://item.taobao.com/item.htm?id=553647861393","img_url":"http://img02.taobaocdn.com/bao/uploaded/i2/TB1pYVERVXXXXa4aXXXXXXXXXXX_!!0-item_pic.jpg","productId":"553647861393","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":72,"market_price":394,"sell_price":394,"tkRate":20},{"title":"单筒单桶大容量半全自动小型迷你洗衣机 带甩干脱水家用波轮紫光","link_url":"http://item.taobao.com/item.htm?id=553928466888","img_url":"http://img03.taobaocdn.com/bao/uploaded/i3/TB1gG9hPFXXXXclapXXXXXXXXXX_!!0-item_pic.jpg","productId":"553928466888","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":583,"market_price":405,"sell_price":405,"tkRate":20},{"title":"中欧高端16/15kg大型双桶双缸半自动洗衣机 宾馆旅馆商用家用杀菌","link_url":"http://item.taobao.com/item.htm?id=552462681459","img_url":"http://img4.tbcdn.cn/tfscom/i4/2597757991/TB24QoxhStkpuFjy0FhXXXQzFXa_!!2597757991.jpg","productId":"552462681459","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":0,"market_price":498,"sell_price":298,"tkRate":0},{"title":"WEILI/威力XPB45-298 4.0KG 小型迷你洗衣机半自动单筒波轮带甩干","link_url":"http://item.taobao.com/item.htm?id=531689187419","img_url":"http://img4.tbcdn.cn/tfscom/i3/TB181P6PVXXXXa6XFXXXXXXXXXX_!!0-item_pic.jpg","productId":"531689187419","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":0,"market_price":498,"sell_price":298,"tkRate":0},{"title":"微型家用迷你半自动小孩儿童洗衣机冼双筒桶同内衣内裤宿舍","link_url":"http://item.taobao.com/item.htm?id=554423830480","img_url":"http://img2.tbcdn.cn/tfscom/i1/TB1CbQwRVXXXXX_XFXXXXXXXXXX_!!0-item_pic.jpg","productId":"554423830480","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":0,"market_price":498,"sell_price":298,"tkRate":0},{"title":"迷你洗衣机单筒小型迷你单通半自动洗衣机洗脱一体机带甩干","link_url":"http://item.taobao.com/item.htm?id=554082573662","img_url":"http://img1.tbcdn.cn/tfscom/i4/TB1J1rnRVXXXXaVXVXXXXXXXXXX_!!0-item_pic.jpg","productId":"554082573662","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":0,"market_price":498,"sell_price":298,"tkRate":0}]
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
         * title : 机单甩 家用大容量不锈钢甩干桶 非小洗衣机脱水机甩干
         * link_url : http://item.taobao.com/item.htm?id=553647861393
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
         * commission_rate：1
         */

        private String title;
        private String link_url;
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

        public String getSecondCouponsUrl() {
            return secondCouponsUrl;
        }

        public void setSecondCouponsUrl(String secondCouponsUrl) {
            this.secondCouponsUrl = secondCouponsUrl;
        }

        public double getCommission_rate() {
            return commission_rate;
        }

        public void setCommission_rate(double commission_rate) {
            this.commission_rate = commission_rate;
        }
    }
}
