package com.gather_excellent_help.bean.suning.netcart;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/12/6.
 */

public class NetGoodscartCheckBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private int cart_id;
        private int is_check;

        public int getCart_id() {
            return cart_id;
        }

        public void setCart_id(int cart_id) {
            this.cart_id = cart_id;
        }

        public int getIs_check() {
            return is_check;
        }

        public void setIs_check(int is_check) {
            this.is_check = is_check;
        }
    }
}
