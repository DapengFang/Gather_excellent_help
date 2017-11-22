package com.gather_excellent_help.bean.suning;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/6.
 */

public class SuningOrderBean {

    /**
     * statusCode : 1
     * statusMessage : 获取地址成功！
     * data : [{"id":19,"user_id":1,"user_name":"18514792343","accept_name":"超级富豪00000","area":"吉林,长春市,南关区,全区","remark":"购买豪车一辆","address":"f区二单元501","mobile":"18328015840","telphone":"18514792343","email":"","post_code":"147852","is_invoice":1,"invoice_title":"超级富豪","invoice_content":"买豪车","tax_no":"","add_time":"2017-11-03T16:23:26.053","payable_amount":414,"real_amount":414,"order_amount":414,"order_no":"B17110316232686","site_id":2,"goodList":[{"itemId":16,"article_id":2769,"goods_no":"SD0472912040-1","order_id":19,"goods_id":21799,"spec_text":"尺寸：M，形状：正方形，颜色：蓝色","goods_title":"PRE环境博世单门冰箱先采15","img_url":"http://image2.suning.cn/uimg/b2c/newcatentries/0000000000-000000000121347616_1_800x800.jpg","goods_price":145,"real_price":145,"quantity":2,"quantpointity":0}]}]
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
         * id : 19
         * user_id : 1
         * user_name : 18514792343
         * accept_name : 超级富豪00000
         * area : 吉林,长春市,南关区,全区
         * remark : 购买豪车一辆
         * address : f区二单元501
         * mobile : 18328015840
         * telphone : 18514792343
         * email :
         * post_code : 147852
         * is_invoice : 1
         * invoice_title : 超级富豪
         * invoice_content : 买豪车
         * tax_no :
         * add_time : 2017-11-03T16:23:26.053
         * payable_amount : 414
         * real_amount : 414
         * order_amount : 414
         * order_no : B17110316232686
         * site_id : 2
         * goodList : [{"itemId":16,"article_id":2769,"goods_no":"SD0472912040-1","order_id":19,"goods_id":21799,"spec_text":"尺寸：M，形状：正方形，颜色：蓝色","goods_title":"PRE环境博世单门冰箱先采15","img_url":"http://image2.suning.cn/uimg/b2c/newcatentries/0000000000-000000000121347616_1_800x800.jpg","goods_price":145,"real_price":145,"quantity":2,"quantpointity":0}]
         */

        private int id;
        private int user_id;
        private int status;
        private String user_name;
        private String accept_name;
        private String area;
        private String remark;
        private String address;
        private String mobile;
        private String telphone;
        private String email;
        private String post_code;
        private int is_invoice;
        private String invoice_title;
        private String invoice_content;
        private String tax_no;
        private String add_time;//创建时间
        private String confirm_time; //确认时间
        private String payment_time;//支付时间
        private String complete_time;//完成时间
        private String express_time;//发货时间
        private double sn_freight;//物流
        private double payable_amount;
        private double real_amount;
        private double order_amount;
        private String order_no;
        private int site_id;
        private List<GoodListBean> goodList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getAccept_name() {
            return accept_name;
        }

        public void setAccept_name(String accept_name) {
            this.accept_name = accept_name;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getTelphone() {
            return telphone;
        }

        public void setTelphone(String telphone) {
            this.telphone = telphone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPost_code() {
            return post_code;
        }

        public void setPost_code(String post_code) {
            this.post_code = post_code;
        }

        public int getIs_invoice() {
            return is_invoice;
        }

        public void setIs_invoice(int is_invoice) {
            this.is_invoice = is_invoice;
        }

        public String getInvoice_title() {
            return invoice_title;
        }

        public void setInvoice_title(String invoice_title) {
            this.invoice_title = invoice_title;
        }

        public String getInvoice_content() {
            return invoice_content;
        }

        public void setInvoice_content(String invoice_content) {
            this.invoice_content = invoice_content;
        }

        public String getTax_no() {
            return tax_no;
        }

        public void setTax_no(String tax_no) {
            this.tax_no = tax_no;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getConfirm_time() {
            return confirm_time;
        }

        public void setConfirm_time(String confirm_time) {
            this.confirm_time = confirm_time;
        }

        public String getPayment_time() {
            return payment_time;
        }

        public void setPayment_time(String payment_time) {
            this.payment_time = payment_time;
        }

        public String getComplete_time() {
            return complete_time;
        }

        public void setComplete_time(String complete_time) {
            this.complete_time = complete_time;
        }

        public String getExpress_time() {
            return express_time;
        }

        public void setExpress_time(String express_time) {
            this.express_time = express_time;
        }

        public double getSn_freight() {
            return sn_freight;
        }

        public void setSn_freight(double sn_freight) {
            this.sn_freight = sn_freight;
        }

        public double getPayable_amount() {
            return payable_amount;
        }

        public void setPayable_amount(double payable_amount) {
            this.payable_amount = payable_amount;
        }

        public double getReal_amount() {
            return real_amount;
        }

        public void setReal_amount(double real_amount) {
            this.real_amount = real_amount;
        }

        public double getOrder_amount() {
            return order_amount;
        }

        public void setOrder_amount(double order_amount) {
            this.order_amount = order_amount;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public int getSite_id() {
            return site_id;
        }

        public void setSite_id(int site_id) {
            this.site_id = site_id;
        }

        public List<GoodListBean> getGoodList() {
            return goodList;
        }

        public void setGoodList(List<GoodListBean> goodList) {
            this.goodList = goodList;
        }

        public static class GoodListBean {
            /**
             * itemId : 16
             * article_id : 2769
             * goods_no : SD0472912040-1
             * order_id : 19
             * goods_id : 21799
             * spec_text : 尺寸：M，形状：正方形，颜色：蓝色
             * goods_title : PRE环境博世单门冰箱先采15
             * img_url : http://image2.suning.cn/uimg/b2c/newcatentries/0000000000-000000000121347616_1_800x800.jpg
             * goods_price : 145
             * real_price : 145
             * quantity : 2
             * quantpointity : 0
             */

            private int itemId;
            private int article_id;
            private String goods_no;
            private int order_id;
            private int goods_id;
            private String spec_text;
            private String goods_title;
            private String img_url;
            private double goods_price;
            private double real_price;
            private int quantity;
            private int quantpointity;

            public int getItemId() {
                return itemId;
            }

            public void setItemId(int itemId) {
                this.itemId = itemId;
            }

            public int getArticle_id() {
                return article_id;
            }

            public void setArticle_id(int article_id) {
                this.article_id = article_id;
            }

            public String getGoods_no() {
                return goods_no;
            }

            public void setGoods_no(String goods_no) {
                this.goods_no = goods_no;
            }

            public int getOrder_id() {
                return order_id;
            }

            public void setOrder_id(int order_id) {
                this.order_id = order_id;
            }

            public int getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(int goods_id) {
                this.goods_id = goods_id;
            }

            public String getSpec_text() {
                return spec_text;
            }

            public void setSpec_text(String spec_text) {
                this.spec_text = spec_text;
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

            public double getGoods_price() {
                return goods_price;
            }

            public void setGoods_price(double goods_price) {
                this.goods_price = goods_price;
            }

            public double getReal_price() {
                return real_price;
            }

            public void setReal_price(double real_price) {
                this.real_price = real_price;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public int getQuantpointity() {
                return quantpointity;
            }

            public void setQuantpointity(int quantpointity) {
                this.quantpointity = quantpointity;
            }



        }
    }
}
