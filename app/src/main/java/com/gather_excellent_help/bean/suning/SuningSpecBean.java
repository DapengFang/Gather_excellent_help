package com.gather_excellent_help.bean.suning;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/11/5.
 */

public class SuningSpecBean {


    /**
     * statusCode : 1
     * statusMessage : --你获得了该商品的规格--
     * data : [{"spec_id":35,"parent_id":0,"title":"尺寸","content":[{"spec_id":37,"parent_id":35,"title":"M"}]},{"spec_id":31,"parent_id":0,"title":"形状","content":[{"spec_id":33,"parent_id":31,"title":"正方形"}]},{"spec_id":27,"parent_id":0,"title":"颜色","content":[{"spec_id":30,"parent_id":27,"title":"蓝色"}]}]
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
         * spec_id : 35
         * parent_id : 0
         * title : 尺寸
         * content : [{"spec_id":37,"parent_id":35,"title":"M"}]
         */

        private int spec_id;
        private int parent_id;
        private String title;
        private List<ContentBean> content;

        public int getSpec_id() {
            return spec_id;
        }

        public void setSpec_id(int spec_id) {
            this.spec_id = spec_id;
        }

        public int getParent_id() {
            return parent_id;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        public static class ContentBean {
            /**
             * spec_id : 37
             * parent_id : 35
             * title : M
             */

            private int spec_id;
            private int parent_id;
            private String title;
            private boolean isCheck;

            public int getSpec_id() {
                return spec_id;
            }

            public void setSpec_id(int spec_id) {
                this.spec_id = spec_id;
            }

            public int getParent_id() {
                return parent_id;
            }

            public void setParent_id(int parent_id) {
                this.parent_id = parent_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }
        }
    }
}
