package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/8/24.
 */

public class ExractDetailBean {


    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"withdrawal_no":"170801101438007","audit_time":"2017-08-04T10:01:16.79","status":1,"value":0.12,"remark":"提现成功！支付宝订单号：20170804110070001502030031651102到账时间：2017-08-04 10:01:10"},{"withdrawal_no":"170801101403001","audit_time":"2017-08-01T10:15:17.33","status":2,"value":100,"remark":"提现失败！支付宝账号和姓名不匹配,请确认姓名是否正确"},{"withdrawal_no":"170727143042003","audit_time":"2017-08-01T10:12:36.27","status":2,"value":100,"remark":"提现失败！支付宝账号和姓名不匹配,请确认姓名是否正确"}]
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
         * withdrawal_no : 170801101438007
         * audit_time : 2017-08-04T10:01:16.79
         * status : 1
         * value : 0.12
         * remark : 提现成功！支付宝订单号：20170804110070001502030031651102到账时间：2017-08-04 10:01:10
         */

        private String withdrawal_no;
        private String audit_time;
        private int status;
        private double value;
        private String remark;

        public String getWithdrawal_no() {
            return withdrawal_no;
        }

        public void setWithdrawal_no(String withdrawal_no) {
            this.withdrawal_no = withdrawal_no;
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
