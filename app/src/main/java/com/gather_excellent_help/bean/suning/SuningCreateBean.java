package com.gather_excellent_help.bean.suning;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/8.
 */

public class SuningCreateBean {

    /**
     * statusCode : 1
     * statusMessage : 创建订单成功！
     * data : [{"orderId":16,"add_time":"2017-11-08T16:09:17.2912487+08:00","area_id":"100,025,01,88","address":"f区二单元501","ProductId":["1051100737"],"pay_status":1}]
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
         * orderId : 16
         * add_time : 2017-11-08T16:09:17.2912487+08:00
         * area_id : 100,025,01,88
         * address : f区二单元501
         * ProductId : ["1051100737"]
         * pay_status : 1
         */

        private int orderId;
        private String add_time;
        private String area_id;
        private String address;
        private int pay_status;
        private List<String> ProductId;
        private double pay_price;
        private String order_num;

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getPay_status() {
            return pay_status;
        }

        public void setPay_status(int pay_status) {
            this.pay_status = pay_status;
        }

        public List<String> getProductId() {
            return ProductId;
        }

        public void setProductId(List<String> ProductId) {
            this.ProductId = ProductId;
        }

        public double getPay_price() {
            return pay_price;
        }

        public void setPay_price(double pay_price) {
            this.pay_price = pay_price;
        }

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }
    }
}
