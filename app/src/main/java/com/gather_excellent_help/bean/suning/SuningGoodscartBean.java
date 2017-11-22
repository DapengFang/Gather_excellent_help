package com.gather_excellent_help.bean.suning;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/15.
 */

public class SuningGoodscartBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * product_mprice : 300.00
         * product_id : 123123
         * product_title : 商品
         * product_num : 2
         * id : 12
         * product_check : 1
         * product_pic : dadadadadad
         * product_spec : 尺寸：23，颜色：蓝色
         * product_sprice : 200.0
         */

        private String product_mprice;
        private String product_id;
        private String product_title;
        private String product_num;
        private String id;
        private String product_check;
        private String product_pic;
        private String product_spec;
        private String product_sprice;
        private String product_spec_id;

        public String getProduct_mprice() {
            return product_mprice;
        }

        public void setProduct_mprice(String product_mprice) {
            this.product_mprice = product_mprice;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_title() {
            return product_title;
        }

        public void setProduct_title(String product_title) {
            this.product_title = product_title;
        }

        public String getProduct_num() {
            return product_num;
        }

        public void setProduct_num(String product_num) {
            this.product_num = product_num;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProduct_check() {
            return product_check;
        }

        public void setProduct_check(String product_check) {
            this.product_check = product_check;
        }

        public String getProduct_pic() {
            return product_pic;
        }

        public void setProduct_pic(String product_pic) {
            this.product_pic = product_pic;
        }

        public String getProduct_spec() {
            return product_spec;
        }

        public void setProduct_spec(String product_spec) {
            this.product_spec = product_spec;
        }

        public String getProduct_sprice() {
            return product_sprice;
        }

        public void setProduct_sprice(String product_sprice) {
            this.product_sprice = product_sprice;
        }

        public String getProduct_spec_id() {
            return product_spec_id;
        }

        public void setProduct_spec_id(String product_spec_id) {
            this.product_spec_id = product_spec_id;
        }
    }
}
