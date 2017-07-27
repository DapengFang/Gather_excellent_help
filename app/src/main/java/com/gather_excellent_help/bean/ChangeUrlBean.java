package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by ${} on 2017/7/19.
 */


public class ChangeUrlBean {

    /**
     * statusCode : 1
     * statusMessage : 商品链接转换成功
     * data : [{"click_url":"https://s.click.taobao.com/t?e=m%3D2%26s%3D1WIAOdCZ7CJw4vFB6t2Z2ueEDrYVVa64XoO8tOebS%2BdRAdhuF14FMeA9lRGpVkxz1aH1Hk3GeOgeGl4jyMMV31%2F91mtq8Zg%2BZb0ar9KgqkwcLKv4LEMaK3bOdFtc4uDS06qvb4lce%2B8uFtjdCU9sOd634hvPmnutd8IbCKrt0lvGfTBwuitr46%2FsOD%2BAvuveLHUtVkTCtyFzlA%2FEwG5YYMYl7w3%2FA2kb&unid=application","num_iid":554091961937}]
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
         * click_url : https://s.click.taobao.com/t?e=m%3D2%26s%3D1WIAOdCZ7CJw4vFB6t2Z2ueEDrYVVa64XoO8tOebS%2BdRAdhuF14FMeA9lRGpVkxz1aH1Hk3GeOgeGl4jyMMV31%2F91mtq8Zg%2BZb0ar9KgqkwcLKv4LEMaK3bOdFtc4uDS06qvb4lce%2B8uFtjdCU9sOd634hvPmnutd8IbCKrt0lvGfTBwuitr46%2FsOD%2BAvuveLHUtVkTCtyFzlA%2FEwG5YYMYl7w3%2FA2kb&unid=application
         * num_iid : 554091961937
         */

        private String click_url;
        private long num_iid;

        public String getClick_url() {
            return click_url;
        }

        public void setClick_url(String click_url) {
            this.click_url = click_url;
        }

        public long getNum_iid() {
            return num_iid;
        }

        public void setNum_iid(long num_iid) {
            this.num_iid = num_iid;
        }
    }

    @Override
    public String toString() {
        return "ChangeUrlBean{" +
                "statusCode=" + statusCode +
                ", statusMessage='" + statusMessage + '\'' +
                ", data=" + data +
                '}';
    }
}
