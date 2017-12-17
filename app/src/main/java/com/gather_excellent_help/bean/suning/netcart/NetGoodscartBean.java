package com.gather_excellent_help.bean.suning.netcart;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/12/6.
 */

public class NetGoodscartBean {

    /**
     * statusCode : 1
     * statusMessage : 获取订单列表成功！
     * data : [{"user_id":9368,"channel_id":7,"article_id":2937,"goods_id":21802,"quantity":1,"goods_list":[{"goods_title":"荣耀8全网通（FRD-AL10）（4+64G）魅海蓝","img_url":"http://image1.suning.cn/uimg/b2c/newcatentries/0000000000-000000000162904965_1_400x400.jpg","market_price":2297,"sell_price":2297,"limit_num":3}],"gg_list":[{"goods_no":"SD0542689823-15","spec_text":"版式：苏宁独家包销 70g A4，版本：全网通4+64G，颜色：蓝色，尺寸：S"}],"purchase_num":1}]
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
         * user_id : 9368
         * channel_id : 7
         * article_id : 2937
         * goods_id : 21802
         * quantity : 1
         * goods_list : [{"goods_title":"荣耀8全网通（FRD-AL10）（4+64G）魅海蓝","img_url":"http://image1.suning.cn/uimg/b2c/newcatentries/0000000000-000000000162904965_1_400x400.jpg","market_price":2297,"sell_price":2297,"limit_num":3}]
         * gg_list : [{"goods_no":"SD0542689823-15","spec_text":"版式：苏宁独家包销 70g A4，版本：全网通4+64G，颜色：蓝色，尺寸：S"}]
         * purchase_num : 1
         */


        private int user_id;
        private int cart_id;
        private int channel_id;
        private int article_id;
        private int goods_id;
        private int quantity;
        private int purchase_num;
        private int is_check;
        private List<GoodsListBean> goods_list;
        private List<GgListBean> gg_list;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getCart_id() {
            return cart_id;
        }

        public void setCart_id(int cart_id) {
            this.cart_id = cart_id;
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

        public int getPurchase_num() {
            return purchase_num;
        }

        public void setPurchase_num(int purchase_num) {
            this.purchase_num = purchase_num;
        }


        public int getIs_check() {
            return is_check;
        }

        public void setIs_check(int is_check) {
            this.is_check = is_check;
        }

        public List<GoodsListBean> getGoods_list() {
            return goods_list;
        }

        public void setGoods_list(List<GoodsListBean> goods_list) {
            this.goods_list = goods_list;
        }

        public List<GgListBean> getGg_list() {
            return gg_list;
        }

        public void setGg_list(List<GgListBean> gg_list) {
            this.gg_list = gg_list;
        }

        public static class GoodsListBean {
            /**
             * goods_title : 荣耀8全网通（FRD-AL10）（4+64G）魅海蓝
             * img_url : http://image1.suning.cn/uimg/b2c/newcatentries/0000000000-000000000162904965_1_400x400.jpg
             * market_price : 2297.0
             * sell_price : 2297.0
             * limit_num : 3
             */

            private String ProductId;
            private String goods_title;
            private String img_url;
            private double market_price;
            private double sell_price;
            private int limit_num;

            public String getProductId() {
                return ProductId;
            }

            public void setProductId(String productId) {
                ProductId = productId;
            }

            public String getGoods_title() {
                return goods_title;
            }

            public void setGoods_title(String goods_title) {
                this.goods_title = goods_title;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
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

            public int getLimit_num() {
                return limit_num;
            }

            public void setLimit_num(int limit_num) {
                this.limit_num = limit_num;
            }
        }

        public static class GgListBean {
            /**
             * goods_no : SD0542689823-15
             * spec_text : 版式：苏宁独家包销 70g A4，版本：全网通4+64G，颜色：蓝色，尺寸：S
             */

            private String goods_no;
            private String spec_text;

            public String getGoods_no() {
                return goods_no;
            }

            public void setGoods_no(String goods_no) {
                this.goods_no = goods_no;
            }

            public String getSpec_text() {
                return spec_text;
            }

            public void setSpec_text(String spec_text) {
                this.spec_text = spec_text;
            }
        }
    }
}
