package com.gather_excellent_help.bean;
/**
 * Created by ${} on 2017/7/14.
 */

import java.util.List;


public class TypeNavigatorBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"id":113,"title":"热水器","subList":[{"id":118,"title":"60L以上","threeList":[]},{"id":117,"title":"41-60L","threeList":[]},{"id":116,"title":"21-40L","threeList":[]},{"id":115,"title":"10-20L","threeList":[]},{"id":114,"title":"10L以下","threeList":[]}]},{"id":107,"title":"冷柜","subList":[{"id":112,"title":"400L以上","threeList":[]},{"id":111,"title":"300-400L","threeList":[]},{"id":110,"title":"200-299L","threeList":[]},{"id":109,"title":"100-199L","threeList":[]},{"id":108,"title":"100L以下","threeList":[]}]},{"id":102,"title":"烟灶消","subList":[{"id":106,"title":"消毒柜","threeList":[]},{"id":105,"title":"油烟机","threeList":[]},{"id":104,"title":"燃气灶","threeList":[]},{"id":103,"title":"套装","threeList":[]}]},{"id":95,"title":"冰箱","subList":[{"id":101,"title":"510L以上","threeList":[]},{"id":100,"title":"321-510L","threeList":[]},{"id":99,"title":"241-320L","threeList":[]},{"id":98,"title":"211-240L","threeList":[]},{"id":97,"title":"150-210L","threeList":[]},{"id":96,"title":"150L以下","threeList":[]}]},{"id":76,"title":"小家电","subList":[{"id":94,"title":"其它小家电","threeList":[]},{"id":93,"title":"挂烫机","threeList":[]},{"id":92,"title":"取暖器","threeList":[]},{"id":91,"title":"养生壶","threeList":[]},{"id":90,"title":"电烤箱","threeList":[]},{"id":89,"title":"破壁机","threeList":[]},{"id":88,"title":"电炖锅","threeList":[]},{"id":87,"title":"豆浆机","threeList":[]},{"id":86,"title":"电压力锅","threeList":[]},{"id":85,"title":"电饼铛","threeList":[]},{"id":84,"title":"电水壶","threeList":[]},{"id":83,"title":"电磁炉","threeList":[]},{"id":82,"title":"料理机","threeList":[]},{"id":81,"title":"微波炉","threeList":[]},{"id":80,"title":"电饭煲","threeList":[]},{"id":79,"title":"空调扇","threeList":[]},{"id":78,"title":"电风扇","threeList":[]},{"id":77,"title":"净水器","threeList":[]}]},{"id":70,"title":"空调","subList":[{"id":75,"title":"3匹及以上","threeList":[]},{"id":74,"title":"2匹","threeList":[]},{"id":73,"title":"1.5匹","threeList":[]},{"id":72,"title":"1匹","threeList":[]},{"id":71,"title":"小于1匹","threeList":[]}]},{"id":61,"title":"电视机","subList":[{"id":69,"title":"65寸以上","threeList":[{"id":125,"title":"64核"}]},{"id":68,"title":"60-65寸","threeList":[{"id":126,"title":"HDR"}]},{"id":67,"title":"55-58寸","threeList":[]},{"id":66,"title":"47-50寸","threeList":[]},{"id":65,"title":"43-45寸","threeList":[]},{"id":64,"title":"40-42寸","threeList":[]},{"id":63,"title":"30-39寸","threeList":[]},{"id":62,"title":"20-29寸","threeList":[]}]},{"id":53,"title":"洗衣机","subList":[{"id":60,"title":"10公斤及以上","threeList":[]},{"id":59,"title":"9公斤","threeList":[]},{"id":58,"title":"8公斤","threeList":[]},{"id":57,"title":"7公斤","threeList":[]},{"id":56,"title":"6公斤","threeList":[]},{"id":55,"title":"5公斤","threeList":[]},{"id":54,"title":"4公斤及以下","threeList":[]}]}]
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
         * id : 113
         * title : 热水器
         * subList : [{"id":118,"title":"60L以上","threeList":[]},{"id":117,"title":"41-60L","threeList":[]},{"id":116,"title":"21-40L","threeList":[]},{"id":115,"title":"10-20L","threeList":[]},{"id":114,"title":"10L以下","threeList":[]}]
         */

        private int id;
        private String title;
        private boolean check;
        private List<SubListBean> subList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public List<SubListBean> getSubList() {
            return subList;
        }

        public void setSubList(List<SubListBean> subList) {
            this.subList = subList;
        }

        public static class SubListBean {
            /**
             * id : 118
             * title : 60L以上
             * threeList : []
             */

            private int id;
            private String title;
            private boolean check;
            private List<ThirdListBean> threeList;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public boolean isCheck() {
                return check;
            }

            public void setCheck(boolean check) {
                this.check = check;
            }

            public List<ThirdListBean> getThreeList() {
                return threeList;
            }

            public void setThreeList(List<ThirdListBean> threeList) {
                this.threeList = threeList;
            }

            public static class ThirdListBean {
                private int id;
                private String title;
                private boolean check;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public boolean isCheck() {
                    return check;
                }

                public void setCheck(boolean check) {
                    this.check = check;
                }
            }
        }
    }
}
