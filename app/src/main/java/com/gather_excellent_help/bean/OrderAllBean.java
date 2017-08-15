package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by ${} on 2017/7/22.
 */

public class OrderAllBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"trade_id":"42456943338780471","price":"30.00","create_time":"2017-08-04T14:51:20","tk_status":"12","goodsImg":"http://img04.taobaocdn.com/bao/uploaded/i4/TB1sQjCSpXXXXXmaXXXXXXXXXXX_!!0-item_pic.jpg"},{"trade_id":"42378485419438485","price":"2799.00","create_time":"2017-08-04T13:55:38","tk_status":"13","goodsImg":""},{"trade_id":"42399359918736643","price":"3099.00","create_time":"2017-08-04T13:54:56","tk_status":"12","goodsImg":""},{"trade_id":"42339472588287806","price":"120.00","create_time":"2017-08-04T13:52:17","tk_status":"12","goodsImg":""},{"trade_id":"42363254899227297","price":"2899.00","create_time":"2017-08-04T13:05:49","tk_status":"13","goodsImg":""},{"trade_id":"42349582848614629","price":"399.00","create_time":"2017-08-04T12:56:28","tk_status":"12","goodsImg":""},{"trade_id":"42329036336245586","price":"1098.00","create_time":"2017-08-04T12:52:38","tk_status":"12","goodsImg":""},{"trade_id":"42309156398982582","price":"2799.00","create_time":"2017-08-04T12:07:08","tk_status":"12","goodsImg":""},{"trade_id":"42336066557264180","price":"3599.00","create_time":"2017-08-04T12:06:09","tk_status":"13","goodsImg":""},{"trade_id":"42327945843538080","price":"518.00","create_time":"2017-08-04T12:04:34","tk_status":"12","goodsImg":""},{"trade_id":"42307508288982582","price":"2999.00","create_time":"2017-08-04T12:02:34","tk_status":"12","goodsImg":""},{"trade_id":"42300048207911314","price":"1599.00","create_time":"2017-08-04T12:01:22","tk_status":"12","goodsImg":""},{"trade_id":"42304732698864969","price":"518.00","create_time":"2017-08-04T11:59:01","tk_status":"12","goodsImg":""},{"trade_id":"42377591848538377","price":"2499.00","create_time":"2017-08-04T11:57:22","tk_status":"12","goodsImg":""},{"trade_id":"42329058244267722","price":"18.00","create_time":"2017-08-04T11:54:30","tk_status":"12","goodsImg":""},{"trade_id":"42377563087538377","price":"2999.00","create_time":"2017-08-04T11:52:33","tk_status":"12","goodsImg":""},{"trade_id":"42286242938122531","price":"1498.00","create_time":"2017-08-04T11:07:39","tk_status":"13","goodsImg":""},{"trade_id":"42301149603193886","price":"2999.00","create_time":"2017-08-04T11:05:22","tk_status":"13","goodsImg":""},{"trade_id":"42235588542990059","price":"1199.00","create_time":"2017-08-04T10:06:50","tk_status":"12","goodsImg":""},{"trade_id":"42285210482558927","price":"245.00","create_time":"2017-08-04T10:06:48","tk_status":"12","goodsImg":""}]
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
         * trade_id : 42456943338780471
         * price : 30.00
         * create_time : 2017-08-04T14:51:20
         * tk_status : 12
         * goodsImg : http://img04.taobaocdn.com/bao/uploaded/i4/TB1sQjCSpXXXXXmaXXXXXXXXXXX_!!0-item_pic.jpg
         */

        private String trade_id;
        private String price;
        private String create_time;
        private String tk_status;
        private String goodsImg;
        private double amount;
        public String getTrade_id() {
            return trade_id;
        }

        public void setTrade_id(String trade_id) {
            this.trade_id = trade_id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getTk_status() {
            return tk_status;
        }

        public void setTk_status(String tk_status) {
            this.tk_status = tk_status;
        }

        public String getGoodsImg() {
            return goodsImg;
        }

        public void setGoodsImg(String goodsImg) {
            this.goodsImg = goodsImg;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
}
