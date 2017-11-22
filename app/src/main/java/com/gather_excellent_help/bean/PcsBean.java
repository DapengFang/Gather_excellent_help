package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/1.
 */

public class PcsBean {

    /**
     * statusCode : 1
     * statusMessage : 获取省份成功！！！！
     * data : [{"area_id":"240","name":"云南省","level":1},{"area_id":"40","name":"内蒙古","level":1},{"area_id":"10","name":"北京","level":1},{"area_id":"80","name":"吉林","level":1},{"area_id":"230","name":"四川省","level":1},{"area_id":"30","name":"天津","level":1},{"area_id":"270","name":"宁夏","level":1},{"area_id":"110","name":"安徽省","level":1},{"area_id":"120","name":"山东省","level":1},{"area_id":"50","name":"山西","level":1},{"area_id":"190","name":"广东省","level":1},{"area_id":"210","name":"广西省","level":1},{"area_id":"290","name":"新疆","level":1},{"area_id":"100","name":"江苏省","level":1},{"area_id":"140","name":"江西省","level":1},{"area_id":"60","name":"河北","level":1},{"area_id":"180","name":"河南省","level":1},{"area_id":"130","name":"浙江省","level":1},{"area_id":"200","name":"海南省","level":1},{"area_id":"170","name":"湖北省","level":1},{"area_id":"160","name":"湖南省","level":1},{"area_id":"260","name":"甘肃省","level":1},{"area_id":"150","name":"福建省","level":1},{"area_id":"300","name":"西藏自治区","level":1},{"area_id":"220","name":"贵州省","level":1},{"area_id":"70","name":"辽宁","level":1},{"area_id":"320","name":"重庆市","level":1},{"area_id":"250","name":"陕西省","level":1},{"area_id":"280","name":"青海省","level":1},{"area_id":"90","name":"黑龙江","level":1},{"area_id":"20","name":"上海","level":1},{"area_id":"010","name":"北京市","level":1},{"area_id":"020","name":"上海","level":1},{"area_id":"030","name":"天津市","level":1},{"area_id":"040","name":"内蒙古","level":1},{"area_id":"050","name":"山西省","level":1},{"area_id":"060","name":"河北省","level":1},{"area_id":"070","name":"辽宁省","level":1},{"area_id":"080","name":"吉林省","level":1},{"area_id":"090","name":"黑龙江","level":1},{"area_id":"HOK","name":"香港","level":1},{"area_id":"MAC","name":"澳门","level":1}]
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
         * area_id : 240
         * name : 云南省
         * level : 1
         */

        private String area_id;
        private String name;
        private int level;

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}
