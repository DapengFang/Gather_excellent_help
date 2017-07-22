package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by ${} on 2017/7/22.
 */

public class OrderAllBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"trade_id":"33266831568780471","price":"2498.00","create_time":"2017-07-01T12:04:34","tk_status":"","goodsImg":null}]
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
         * trade_id : 33266831568780471
         * price : 2498.00
         * create_time : 2017-07-01T12:04:34
         * tk_status :
         * goodsImg : null
         */

        private String trade_id;
        private String price;
        private String create_time;
        private String tk_status;
        private Object goodsImg;

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

        public Object getGoodsImg() {
            return goodsImg;
        }

        public void setGoodsImg(Object goodsImg) {
            this.goodsImg = goodsImg;
        }
    }
}
