package com.gather_excellent_help.bean.suning;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/6.
 */

public class SuningWareBean {

    /**
     * statusCode : 1
     * statusMessage : -- 获取商品详情信息，成功 --
     * data : [{"id":2769,"productId":"121347616","urls":[{"original_path":"http://image2.suning.cn/uimg/b2c/newcatentries/0000000000-000000000121347616_1_800x800.jpg"},{"original_path":"http://image1.suning.cn/uimg/b2c/newcatentries/0000000000-000000000121347616_2_800x800.jpg"},{"original_path":"http://image2.suning.cn/uimg/b2c/newcatentries/0000000000-000000000121347616_3_800x800.jpg"},{"original_path":"http://image1.suning.cn/uimg/b2c/newcatentries/0000000000-000000000121347616_4_800x800.jpg"},{"original_path":"http://image2.suning.cn/uimg/b2c/newcatentries/0000000000-000000000121347616_5_800x800.jpg"}],"title":"PRE环境博世单门冰箱先采15","sell_price":145,"market_price":145,"stock_state":"现货","info_url":"/api/goods_info/info.aspx?goods_id=121347616"}]
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
         * id : 2769
         * productId : 121347616
         * urls : [{"original_path":"http://image2.suning.cn/uimg/b2c/newcatentries/0000000000-000000000121347616_1_800x800.jpg"},{"original_path":"http://image1.suning.cn/uimg/b2c/newcatentries/0000000000-000000000121347616_2_800x800.jpg"},{"original_path":"http://image2.suning.cn/uimg/b2c/newcatentries/0000000000-000000000121347616_3_800x800.jpg"},{"original_path":"http://image1.suning.cn/uimg/b2c/newcatentries/0000000000-000000000121347616_4_800x800.jpg"},{"original_path":"http://image2.suning.cn/uimg/b2c/newcatentries/0000000000-000000000121347616_5_800x800.jpg"}]
         * title : PRE环境博世单门冰箱先采15
         * sell_price : 145
         * market_price : 145
         * stock_state : 现货
         * info_url : /api/goods_info/info.aspx?goods_id=121347616
         */

        private int id;
        private String productId;
        private String title;
        private double sell_price;
        private double market_price;
        private String stock_state;
        private String info_url;
        private List<UrlsBean> urls;
        private int limit_num;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getSell_price() {
            return sell_price;
        }

        public void setSell_price(double sell_price) {
            this.sell_price = sell_price;
        }

        public double getMarket_price() {
            return market_price;
        }

        public void setMarket_price(double market_price) {
            this.market_price = market_price;
        }

        public String getStock_state() {
            return stock_state;
        }

        public void setStock_state(String stock_state) {
            this.stock_state = stock_state;
        }

        public String getInfo_url() {
            return info_url;
        }

        public void setInfo_url(String info_url) {
            this.info_url = info_url;
        }

        public List<UrlsBean> getUrls() {
            return urls;
        }

        public void setUrls(List<UrlsBean> urls) {
            this.urls = urls;
        }

        public int getLimit_num() {
            return limit_num;
        }

        public void setLimit_num(int limit_num) {
            this.limit_num = limit_num;
        }

        public static class UrlsBean {
            /**
             * original_path : http://image2.suning.cn/uimg/b2c/newcatentries/0000000000-000000000121347616_1_800x800.jpg
             */

            private String original_path;

            public String getOriginal_path() {
                return original_path;
            }

            public void setOriginal_path(String original_path) {
                this.original_path = original_path;
            }
        }
    }
}
