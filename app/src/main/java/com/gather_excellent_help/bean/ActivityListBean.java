package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/8/22.
 */

public class ActivityListBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"title":"【咨询领券减1600】TCL 55P3F 55英寸曲面金属纤薄智能网络LED液晶曲屏电视机60","link_url":"https://detail.tmall.com/item.htm?id=550876925155&spm=a219t.7664554.1998457203.112.2645a9fdlG4sZT&skuId=3525597310923","img_url":"http://img.alicdn.com/imgextra/i4/395601843/TB2fTfbprXlpuFjSszfXXcSGXXa_!!395601843.jpg","productId":"550876925155","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","secondCouponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":1534,"market_price":5599,"sell_price":3999,"tkRate":6.5,"add_time":"2017-08-21T13:58:30","lately_order_time":"0001-01-01T00:00:00"},{"title":"成人浴巾美容院女抹胸专用铺床加厚加大毛巾批發纯棉柔软吸水浴巾","link_url":"https://item.taobao.com/item.htm?id=557167575443","img_url":"http://img.alicdn.com/imgextra/i2/2185469353/TB26PnAXcPEK1JjSZFEXXaA3XXa_!!2185469353.jpg","productId":"557167575443","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","secondCouponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":543845,"market_price":29,"sell_price":9.9,"tkRate":10,"add_time":"2017-08-21T11:25:38","lately_order_time":"0001-01-01T00:00:00"},{"title":"生日礼物女生创意礼品送男女朋友闺蜜七夕友情diy木刻画定制照片","link_url":"https://item.taobao.com/item.htm?spm=a21ag.8106233.C_SItemVisitorRank.3.16e246bfeDqaGL&id=553846349245&qq-pf-to=pcqq.c2c","img_url":"http://img.alicdn.com/imgextra/i4/212819269/TB25g7Dco3iyKJjSspnXXXbIVXa_!!212819269.jpg","productId":"553846349245","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","secondCouponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":2390,"market_price":45,"sell_price":7.9,"tkRate":10,"add_time":"2017-08-21T11:24:16","lately_order_time":"0001-01-01T00:00:00"},{"title":"桂圆干250g*2包农家龙眼肉干福建莆田干货特产500g 买2送零食","link_url":"https://item.taobao.com/item.htm?spm=686.1000925.0.0.iRbyuz&id=45172644135&qq-pf-to=pcqq.c2c","img_url":"http://img.alicdn.com/imgextra/i1/824959904/TB2MOhkt9BjpuFjy1XdXXaooVXa_!!824959904.jpg","productId":"45172644135","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","secondCouponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":2848,"market_price":16.8,"sell_price":9.5,"tkRate":10,"add_time":"2017-08-21T11:23:40","lately_order_time":"0001-01-01T00:00:00"},{"title":"苹果安卓三星小米华为手机USB数据线iPhone6S充电线 type-c数据线","link_url":"https://detail.tmall.com/item.htm?id=556758436825","img_url":"http://img.alicdn.com/imgextra/i1/1907990852/TB1WFH.SFXXXXb6XVXXXXXXXXXX_!!0-item_pic.jpg","productId":"556758436825","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","secondCouponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":43647,"market_price":7.9,"sell_price":3.9,"tkRate":10,"add_time":"2017-08-21T11:22:18","lately_order_time":"0001-01-01T00:00:00"},{"title":"包邮加厚一次性纸杯100只纯白茶水杯家用商务办公广告杯定制","link_url":"https://item.taobao.com/item.htm?spm=a1z09.8149145.0.0.366ca24byFUw21&id=553491309477","img_url":"http://img.alicdn.com/imgextra/i3/2937083852/TB2w_i6tM0kpuFjSspdXXX4YXXa_!!2937083852.jpg","productId":"553491309477","couponsId":"","couponsPrice":0,"couponsCondition":0,"couponsUrl":"","secondCouponsUrl":"","couponsSurplus":0,"couponsGet":0,"stock_quantity":7382,"market_price":12.8,"sell_price":7.8,"tkRate":10,"add_time":"2017-08-21T11:21:54","lately_order_time":"0001-01-01T00:00:00"}]
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
         * title : 【咨询领券减1600】TCL 55P3F 55英寸曲面金属纤薄智能网络LED液晶曲屏电视机60
         * link_url : https://detail.tmall.com/item.htm?id=550876925155&spm=a219t.7664554.1998457203.112.2645a9fdlG4sZT&skuId=3525597310923
         * img_url : http://img.alicdn.com/imgextra/i4/395601843/TB2fTfbprXlpuFjSszfXXcSGXXa_!!395601843.jpg
         * productId : 550876925155
         * couponsId :
         * couponsPrice : 0
         * couponsCondition : 0
         * couponsUrl :
         * secondCouponsUrl :
         * couponsSurplus : 0
         * couponsGet : 0
         * stock_quantity : 1534
         * market_price : 5599
         * sell_price : 3999
         * tkRate : 6.5
         * add_time : 2017-08-21T13:58:30
         * lately_order_time : 0001-01-01T00:00:00
         * commission_rate : 1
         */

        private int site_id;//区分苏宁和淘宝
        private int article_id;//数据库自增id
        private String title;
        private String link_url;
        private String img_url;
        private String productId;//产品id
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
        private String add_time;
        private String lately_order_time;
        private double commission_rate;
        private double suning_rate;

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

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getLately_order_time() {
            return lately_order_time;
        }

        public void setLately_order_time(String lately_order_time) {
            this.lately_order_time = lately_order_time;
        }

        public double getCommission_rate() {
            return commission_rate;
        }

        public void setCommission_rate(double commission_rate) {
            this.commission_rate = commission_rate;
        }

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

        public double getSuning_rate() {
            return suning_rate;
        }

        public void setSuning_rate(double suning_rate) {
            this.suning_rate = suning_rate;
        }
    }
}
