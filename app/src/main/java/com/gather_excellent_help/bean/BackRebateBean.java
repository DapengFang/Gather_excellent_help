package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/7/28.
 */

public class BackRebateBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"rebate_no":"c170727161708002","audit_time":"0001-01-01T00:00:00","status":3,"value":0.22,"remark":"18514792343购买返佣"}]
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
         * rebate_no : c170727161708002
         * audit_time : 0001-01-01T00:00:00
         * status : 3
         * value : 0.22
         * remark : 18514792343购买返佣
         */

        private String rebate_no;
        private String audit_time;
        private int status;
        private double value;
        private String remark;

        public String getRebate_no() {
            return rebate_no;
        }

        public void setRebate_no(String rebate_no) {
            this.rebate_no = rebate_no;
        }

        public String getAudit_time() {
            return audit_time;
        }

        public void setAudit_time(String audit_time) {
            this.audit_time = audit_time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
