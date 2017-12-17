package com.gather_excellent_help.bean.suning.netcart;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/12/6.
 */

public class NetGoodsUpdateBean {

    /**
     * statusCode : 1
     * statusMessage : 修改成功！
     * data : [{"cart_id":8,"user_id":9368,"channel_id":7,"article_id":2936,"goods_id":21819,"quantity":2}]
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
         * cart_id : 8
         * user_id : 9368
         * channel_id : 7
         * article_id : 2936
         * goods_id : 21819
         * quantity : 2
         */

        private int cart_id;
        private int user_id;
        private int channel_id;
        private int article_id;
        private int goods_id;
        private int quantity;

        public int getCart_id() {
            return cart_id;
        }

        public void setCart_id(int cart_id) {
            this.cart_id = cart_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(int channel_id) {
            this.channel_id = channel_id;
        }

        public int getArticle_id() {
            return article_id;
        }

        public void setArticle_id(int article_id) {
            this.article_id = article_id;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
