package com.gather_excellent_help.bean.suning;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/8.
 */

public class SuningSpecidBackBean {

    /**
     * statusCode : 1
     * statusMessage : 获取规格详请产品ID成功！
     * data : [{"goods_specid":"21726"}]
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
         * goods_specid : 21726
         */

        private String goods_specid;

        public String getGoods_specid() {
            return goods_specid;
        }

        public void setGoods_specid(String goods_specid) {
            this.goods_specid = goods_specid;
        }
    }
}
