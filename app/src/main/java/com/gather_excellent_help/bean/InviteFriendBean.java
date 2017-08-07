package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/8/2.
 */

public class InviteFriendBean {

    /**
     * statusCode : 1
     * statusMessage : 分享成功
     * data : [{"share_url":"http://192.168.200.198:8080/api/share/appreg.aspx?referees_id=4&from=singlePage"}]
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
         * share_url : http://192.168.200.198:8080/api/share/appreg.aspx?referees_id=4&from=singlePage
         */

        private String share_url;

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }
    }
}
