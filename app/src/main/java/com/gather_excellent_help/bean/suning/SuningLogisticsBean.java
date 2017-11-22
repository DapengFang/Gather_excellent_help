package com.gather_excellent_help.bean.suning;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/14.
 */

public class SuningLogisticsBean {

    /**
     * statusCode : 1
     * statusMessage : 获取物流成功！
     * data : [{"orderId":"100000555321","isPackage":"Y","logisticsDetail":[{"operateState":"您的订单已生成，请尽快完成支付","operateTime":"20171115110157"},{"operateState":"您的订单已支付完成，等待发货","operateTime":"20171116111026"},{"operateState":"您的发货清单【苏宁南京大件配送中心】已打印，待打印发票","operateTime":"20171116145156"}],"orderItemIds":[{"orderItemId":"10000055532101","skuId":"121347616"}]}]
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
         * orderId : 100000555321
         * isPackage : Y
         * logisticsDetail : [{"operateState":"您的订单已生成，请尽快完成支付","operateTime":"20171115110157"},{"operateState":"您的订单已支付完成，等待发货","operateTime":"20171116111026"},{"operateState":"您的发货清单【苏宁南京大件配送中心】已打印，待打印发票","operateTime":"20171116145156"}]
         * orderItemIds : [{"orderItemId":"10000055532101","skuId":"121347616"}]
         */

        private String orderId;
        private String isPackage;
        private List<LogisticsDetailBean> logisticsDetail;
        private List<OrderItemIdsBean> orderItemIds;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getIsPackage() {
            return isPackage;
        }

        public void setIsPackage(String isPackage) {
            this.isPackage = isPackage;
        }

        public List<LogisticsDetailBean> getLogisticsDetail() {
            return logisticsDetail;
        }

        public void setLogisticsDetail(List<LogisticsDetailBean> logisticsDetail) {
            this.logisticsDetail = logisticsDetail;
        }

        public List<OrderItemIdsBean> getOrderItemIds() {
            return orderItemIds;
        }

        public void setOrderItemIds(List<OrderItemIdsBean> orderItemIds) {
            this.orderItemIds = orderItemIds;
        }

        public static class LogisticsDetailBean {
            /**
             * operateState : 您的订单已生成，请尽快完成支付
             * operateTime : 20171115110157
             */

            private String operateState;
            private String operateTime;

            public String getOperateState() {
                return operateState;
            }

            public void setOperateState(String operateState) {
                this.operateState = operateState;
            }

            public String getOperateTime() {
                return operateTime;
            }

            public void setOperateTime(String operateTime) {
                this.operateTime = operateTime;
            }
        }

        public static class OrderItemIdsBean {
            /**
             * orderItemId : 10000055532101
             * skuId : 121347616
             */

            private String orderItemId;
            private String skuId;

            public String getOrderItemId() {
                return orderItemId;
            }

            public void setOrderItemId(String orderItemId) {
                this.orderItemId = orderItemId;
            }

            public String getSkuId() {
                return skuId;
            }

            public void setSkuId(String skuId) {
                this.skuId = skuId;
            }
        }
    }
}
