package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/7/28.
 */

public class CityBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"proven":"北京市"},{"proven":"天津市"},{"proven":"上海市"},{"proven":"重庆市"},{"proven":"河北省"},{"proven":"河南省"},{"proven":"云南省"},{"proven":"辽宁省"},{"proven":"黑龙江省"},{"proven":"湖南省"},{"proven":"安徽省"},{"proven":"山东省"},{"proven":"新疆维吾尔"},{"proven":"江苏省"},{"proven":"浙江省"},{"proven":"江西省"},{"proven":"湖北省"},{"proven":"广西省"},{"proven":"甘肃省"},{"proven":"山西省"},{"proven":"内蒙古"},{"proven":"陕西省"},{"proven":"福建省"},{"proven":"贵州省"},{"proven":"广东省"},{"proven":"青海省"},{"proven":"西藏"},{"proven":"四川省"},{"proven":"宁夏回族"},{"proven":"海南省"},{"proven":"台湾省"},{"proven":"香港特别行政区"},{"proven":"澳门特别行政区"}]
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
         * proven : 北京市
         */

        private String proven;

        public String getProven() {
            return proven;
        }

        public void setProven(String proven) {
            this.proven = proven;
        }
    }
}
