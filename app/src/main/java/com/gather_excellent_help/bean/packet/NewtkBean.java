package com.gather_excellent_help.bean.packet;

/**
 * Created by Dapeng Fang on 2017/10/20.
 */

public class NewtkBean {

    /**
     * wireless_share_tpwd_create_response : {"model":" ¥JwzH05mV6mM ¥","request_id":"z2bt5wnyli2q"}
     */

    private WirelessShareTpwdCreateResponseBean wireless_share_tpwd_create_response;

    public WirelessShareTpwdCreateResponseBean getWireless_share_tpwd_create_response() {
        return wireless_share_tpwd_create_response;
    }

    public void setWireless_share_tpwd_create_response(WirelessShareTpwdCreateResponseBean wireless_share_tpwd_create_response) {
        this.wireless_share_tpwd_create_response = wireless_share_tpwd_create_response;
    }

    public static class WirelessShareTpwdCreateResponseBean {
        /**
         * model :  ¥JwzH05mV6mM ¥
         * request_id : z2bt5wnyli2q
         */

        private String model;
        private String request_id;

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getRequest_id() {
            return request_id;
        }

        public void setRequest_id(String request_id) {
            this.request_id = request_id;
        }
    }
}
