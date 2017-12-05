package com.gather_excellent_help.bean.sale;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/12/5.
 */

public class ApplyStateBean {

    /**
     * statusCode : 1
     * statusMessage : 获取售后申请状态成功！
     * data : [{"apply_status":0}]
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
         * apply_status : 0
         */

        private int apply_status;

        public int getApply_status() {
            return apply_status;
        }

        public void setApply_status(int apply_status) {
            this.apply_status = apply_status;
        }
    }
}
