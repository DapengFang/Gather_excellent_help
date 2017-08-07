package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/8/1.
 */

public class BrandBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"id":10,"channel_id":7,"title":"小天鹅"},{"id":9,"channel_id":7,"title":"美的(Midea)"},{"id":8,"channel_id":7,"title":"创维(Skyworth)"},{"id":7,"channel_id":7,"title":"TCL"},{"id":6,"channel_id":7,"title":"海尔(Haier)"},{"id":4,"channel_id":7,"title":"小米"}]
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
         * id : 10
         * channel_id : 7
         * title : 小天鹅
         */

        private int id;
        private int channel_id;
        private String title;
        private boolean select;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(int channel_id) {
            this.channel_id = channel_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }
    }
}
