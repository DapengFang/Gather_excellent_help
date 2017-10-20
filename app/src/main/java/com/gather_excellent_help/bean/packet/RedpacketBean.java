package com.gather_excellent_help.bean.packet;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/10/18.
 */

public class RedpacketBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"newHblink":"https://uland.taobao.com/thb?pid=mm_119869053_26726507_130642937","isHb":1}]
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
         * newHblink : https://uland.taobao.com/thb?pid=mm_119869053_26726507_130642937
         * isHb : 1
         */

        private String newHblink;
        private int isHb;

        public String getNewHblink() {
            return newHblink;
        }

        public void setNewHblink(String newHblink) {
            this.newHblink = newHblink;
        }

        public int getIsHb() {
            return isHb;
        }

        public void setIsHb(int isHb) {
            this.isHb = isHb;
        }
    }
}
