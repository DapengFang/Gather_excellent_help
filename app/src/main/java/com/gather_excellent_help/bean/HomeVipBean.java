package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/8/9.
 */

public class HomeVipBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"activity_id":0,"title":"Midea/美的 MP80-DS805 8公斤半自动双缸波轮洗衣机迷你家用","link_url":"https://item.taobao.com/item.htm?id=554808058398&item_num_id=554808058398","activity_img":"","img_url":"http://img.alicdn.com/imgextra/i3/TB1.Vl7RFXXXXapXXXXXXXXXXXX_!!0-item_pic.jpg","productId":"554808058398","couponsId":"50022703","couponsPrice":10,"couponsCondition":498,"couponsUrl":"http://uland.taobao.com/coupon/edetail?e=vX%2FFExml7vkGQASttHIRqVAQu1FtoG%2F2nMr7fgX2gPwEwusJFf0tLxHdZUEZXVOQld8gFXib3x%2FJRvkRvh%2BuLcGSOsUw4E0f3FCIHeXMfiWeaD2hx1IMB2uFqp8TFaHMonv6QcvcARY%3D&traceId=0ab84e8f15021824734711939e","secondCouponsUrl":"","couponsSurplus":99679,"couponsGet":0,"stock_quantity":394,"market_price":618,"sell_price":505,"tkRate":3},{"activity_id":52,"title":"美的冰箱门测试用","link_url":"https://item.taobao.com/item.htm?spm=0.7095261.0.0.4634959w6WSR2&id=556075509708","activity_img":"/upload/201707/21/201707211418067089.jpg","img_url":"http://img01.taobaocdn.com/bao/uploaded/i1/2640098420/TB2xH4Dct3nyKJjSZFjXXcdBXXa_!!2640098420.jpg","productId":"556075509708","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","secondCouponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":110,"market_price":16,"sell_price":16,"tkRate":0.5},{"activity_id":52,"title":"测试用品 内部测试清洁用品","link_url":"https://item.taobao.com/item.htm?spm=686.1000925.0.0.3f11c9edVHPjpO&id=554275576533","activity_img":"/upload/201707/21/201707211418067089.jpg","img_url":"http://img01.taobaocdn.com/bao/uploaded/i1/2640098420/TB2XelwaB4lpuFjy1zjXXcAKpXa_!!2640098420.jpg","productId":"554275576533","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","secondCouponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":62,"market_price":20,"sell_price":20,"tkRate":2},{"activity_id":52,"title":"机单甩 家用大容量不锈钢甩干桶 非小洗衣机脱水机甩干","link_url":"http://item.taobao.com/item.htm?id=553647861393","activity_img":"/upload/201707/21/201707211418067089.jpg","img_url":"http://img.alicdn.com/imgextra/i3/2605869446/TB2jb7wt88lpuFjSspaXXXJKpXa_!!2605869446.jpg","productId":"553647861393","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","secondCouponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":72,"market_price":394,"sell_price":394,"tkRate":20}]
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
         * activity_id : 0
         * title : Midea/美的 MP80-DS805 8公斤半自动双缸波轮洗衣机迷你家用
         * link_url : https://item.taobao.com/item.htm?id=554808058398&item_num_id=554808058398
         * activity_img :
         * img_url : http://img.alicdn.com/imgextra/i3/TB1.Vl7RFXXXXapXXXXXXXXXXXX_!!0-item_pic.jpg
         * productId : 554808058398
         * couponsId : 50022703
         * couponsPrice : 10
         * couponsCondition : 498
         * couponsUrl : http://uland.taobao.com/coupon/edetail?e=vX%2FFExml7vkGQASttHIRqVAQu1FtoG%2F2nMr7fgX2gPwEwusJFf0tLxHdZUEZXVOQld8gFXib3x%2FJRvkRvh%2BuLcGSOsUw4E0f3FCIHeXMfiWeaD2hx1IMB2uFqp8TFaHMonv6QcvcARY%3D&traceId=0ab84e8f15021824734711939e
         * secondCouponsUrl :
         * couponsSurplus : 99679
         * couponsGet : 0
         * stock_quantity : 394
         * market_price : 618
         * sell_price : 505
         * tkRate : 3
         * commission_rate:1
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
        private String secondCouponsUrl;
        private int couponsSurplus;
        private int couponsGet;
        private int stock_quantity;
        private double market_price;
        private double sell_price;
        private double tkRate;
        private double commission_rate;

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
