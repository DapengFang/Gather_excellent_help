package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/7/28.
 */

public class SearchTaobaoBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"productId":553488979050,"title":"Chigo/志高 XQB42-D1全自动洗衣机小型迷你波轮风干杀菌婴儿童宝","provcity":"浙江 宁波","link_url":"http://item.taobao.com/item.htm?id=553488979050","img_url":"http://img1.tbcdn.cn/tfscom/i3/TB1XQn_SpXXXXc1XVXXXXXXXXXX_!!0-item_pic.jpg","market_price":"898.00","sell_price":"498.00"},{"productId":553571440729,"title":"志高4.2公斤全自动波轮迷你洗衣机小脱水甩干婴儿儿童","provcity":"江苏 无锡","link_url":"http://item.taobao.com/item.htm?id=553571440729","img_url":"http://img3.tbcdn.cn/tfscom/i1/TB1eL4.SXXXXXXRapXXXXXXXXXX_!!0-item_pic.jpg","market_price":"1198.00","sell_price":"499.00"},{"productId":535460616069,"title":"威力XPB35-3501家用小型迷你洗衣机单桶半自动脱水机甩干宝宝儿童","provcity":"浙江 宁波","link_url":"http://item.taobao.com/item.htm?id=535460616069","img_url":"http://img2.tbcdn.cn/tfscom/i1/TB1ddhHQVXXXXbwXFXXXXXXXXXX_!!0-item_pic.jpg","market_price":"718.00","sell_price":"199.00"},{"productId":541389993595,"title":"金帅大容量8公斤半全自动双缸波轮洗衣机双桶家用8kg双筒双杠包邮","provcity":"浙江 宁波","link_url":"http://item.taobao.com/item.htm?id=541389993595","img_url":"http://img4.tbcdn.cn/tfscom/i2/TB1jsyvRVXXXXc8XFXXXXXXXXXX_!!0-item_pic.jpg","market_price":"699.00","sell_price":"438.00"},{"productId":546734187024,"title":"送货入户 欧品8公斤洗衣机全自动家用波轮小型迷你烘干6.5/7.5kg","provcity":"浙江 宁波","link_url":"http://item.taobao.com/item.htm?id=546734187024","img_url":"http://img2.tbcdn.cn/tfscom/i3/2238221879/TB2oZwkt8NkpuFjy0FaXXbRCVXa_!!2238221879.jpg","market_price":"1177.00","sell_price":"499.00"},{"productId":552712013915,"title":"摩鱼 XQB30-S1H 儿童家用小型杀菌全自动婴儿迷你波轮洗衣机","provcity":"上海","link_url":"http://item.taobao.com/item.htm?id=552712013915","img_url":"http://img1.tbcdn.cn/tfscom/i4/TB1Lc7kSXXXXXXdXFXXXXXXXXXX_!!0-item_pic.jpg","market_price":"1699.00","sell_price":"1099.00"},{"productId":532205734996,"title":"WEILI/威力XPB45-298小型迷你洗衣机宿舍半自动单筒波轮带甩干","provcity":"浙江 宁波","link_url":"http://item.taobao.com/item.htm?id=532205734996","img_url":"http://img1.tbcdn.cn/tfscom/i4/TB1nyEdQXXXXXXiXXXXXXXXXXXX_!!0-item_pic.jpg","market_price":"899.00","sell_price":"299.00"},{"productId":542721567992,"title":"韩派XQB70-3070 7公斤全自动波轮洗衣机 控制纳米杀菌 预约快速洗","provcity":"浙江 宁波","link_url":"http://item.taobao.com/item.htm?id=542721567992","img_url":"http://img1.tbcdn.cn/tfscom/i4/TB1eFSvRFXXXXagXpXXXXXXXXXX_!!0-item_pic.jpg","market_price":"1599.00","sell_price":"619.00"},{"productId":549883618870,"title":"小鸭牌XPB35-1709迷你洗衣机小型婴儿童宝宝单筒桶臭氧除菌带洗沥","provcity":"浙江 宁波","link_url":"http://item.taobao.com/item.htm?id=549883618870","img_url":"http://img4.tbcdn.cn/tfscom/i1/TB1xrq7RVXXXXb4XVXXXXXXXXXX_!!0-item_pic.jpg","market_price":"899.00","sell_price":"389.00"},{"productId":531408322228,"title":"WEILI/威力XPB35-3501小型迷你洗衣机单桶家用半自动婴儿童带甩干","provcity":"浙江 宁波","link_url":"http://item.taobao.com/item.htm?id=531408322228","img_url":"http://img3.tbcdn.cn/tfscom/i3/TB1PGkHSXXXXXanaXXXXXXXXXXX_!!0-item_pic.jpg","market_price":"429.00","sell_price":"229.00"},{"productId":553664561377,"title":"海飞XPB42-58S新型迷你双桶洗衣机洗脱一体省水省电宿舍专用","provcity":"浙江 宁波","link_url":"http://item.taobao.com/item.htm?id=553664561377","img_url":"http://img2.tbcdn.cn/tfscom/i4/2868367929/TB24RT7agwjyKJjy1zdXXbgZpXa_!!2868367929.jpg","market_price":"380.00","sell_price":"288.00"},{"productId":550389770792,"title":"小鸭牌xpb25-1603E迷你洗衣机小型婴儿童半自动单桶筒甩干脱水机","provcity":"浙江 宁波","link_url":"http://item.taobao.com/item.htm?id=550389770792","img_url":"http://img4.tbcdn.cn/tfscom/i2/TB1RvptSXXXXXbMaXXXXXXXXXXX_!!0-item_pic.jpg","market_price":"699.00","sell_price":"199.00"},{"productId":534998212442,"title":"Sakura/樱花 XQB45-168 4.5公斤洗衣机全自动 家用波轮小型不锈钢","provcity":"浙江 宁波","link_url":"http://item.taobao.com/item.htm?id=534998212442","img_url":"http://img1.tbcdn.cn/tfscom/i1/TB1QNGqRVXXXXaeXFXXXXXXXXXX_!!0-item_pic.jpg","market_price":"618.00","sell_price":"558.00"},{"productId":45091801831,"title":"TCL XQB60-21CSP 6公斤全自动波轮洗衣机 家用 送货入户","provcity":"广东 惠州","link_url":"http://item.taobao.com/item.htm?id=45091801831","img_url":"http://img4.tbcdn.cn/tfscom/i1/TB19B9cSXXXXXa3XXXXXXXXXXXX_!!0-item_pic.jpg","market_price":"999.00","sell_price":"769.00"},{"productId":523197644392,"title":"美的8公斤/KG洗衣机全自动家用智能洗衣机波轮甩干机MB80-eco11W","provcity":"广东 广州","link_url":"http://item.taobao.com/item.htm?id=523197644392","img_url":"http://img3.tbcdn.cn/tfscom/i1/TB1XIMJSpXXXXbbXXXXXXXXXXXX_!!0-item_pic.jpg","market_price":"1798.00","sell_price":"998.00"},{"productId":44283197286,"title":"WEILI/威力 XQB73-7395-1 波轮洗衣机全自动 大7公斤洗衣机7.3kg","provcity":"广东 佛山","link_url":"http://item.taobao.com/item.htm?id=44283197286","img_url":"http://img4.tbcdn.cn/tfscom/i1/TB16ZLqSpXXXXXUaVXXXXXXXXXX_!!0-item_pic.jpg","market_price":"2680.00","sell_price":"798.00"},{"productId":535002553966,"title":"Hisense/海信 XQB60-H3568 6公斤全自动洗衣机波轮家用脱水带甩干","provcity":"山东 青岛","link_url":"http://item.taobao.com/item.htm?id=535002553966","img_url":"http://img3.tbcdn.cn/tfscom/i3/TB1exXfQXXXXXc6XFXXXXXXXXXX_!!0-item_pic.jpg","market_price":"999.00","sell_price":"699.00"},{"productId":523364075401,"title":"Midea/美的 MB75-eco11W 7.5公斤智能全自动洗衣机波轮手机节能kg","provcity":"浙江 杭州","link_url":"http://item.taobao.com/item.htm?id=523364075401","img_url":"http://img4.tbcdn.cn/tfscom/i2/TB1dRYGSpXXXXaDaFXXXXXXXXXX_!!0-item_pic.jpg","market_price":"1498.00","sell_price":"918.00"},{"productId":520846628898,"title":"TCL XQB55-36SP tcl洗衣机全自动波轮5.5kg公斤家用8档水位10程序","provcity":"广东 广州","link_url":"http://item.taobao.com/item.htm?id=520846628898","img_url":"http://img4.tbcdn.cn/tfscom/i3/TB1GfPFSpXXXXXoapXXXXXXXXXX_!!0-item_pic.jpg","market_price":"799.00","sell_price":"699.00"},{"productId":529022044438,"title":"Ronshen/容声 XQB60-L1028 6公斤全自动波轮洗衣机小型家用","provcity":"广东 佛山","link_url":"http://item.taobao.com/item.htm?id=529022044438","img_url":"http://img2.tbcdn.cn/tfscom/i4/TB1McnASpXXXXayaFXXXXXXXXXX_!!0-item_pic.jpg","market_price":"999.00","sell_price":"699.00"}]
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
         * productId : 553488979050
         * title : Chigo/志高 XQB42-D1全自动洗衣机小型迷你波轮风干杀菌婴儿童宝
         * provcity : 浙江 宁波
         * link_url : http://item.taobao.com/item.htm?id=553488979050
         * img_url : http://img1.tbcdn.cn/tfscom/i3/TB1XQn_SpXXXXc1XVXXXXXXXXXX_!!0-item_pic.jpg
         * market_price : 898.00
         * sell_price : 498.00
         */

        private long productId;
        private String title;
        private String provcity;
        private String link_url;
        private String img_url;
        private String market_price;
        private String sell_price;

        public long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getProvcity() {
            return provcity;
        }

        public void setProvcity(String provcity) {
            this.provcity = provcity;
        }

        public String getLink_url() {
            return link_url;
        }

        public void setLink_url(String link_url) {
            this.link_url = link_url;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getSell_price() {
            return sell_price;
        }

        public void setSell_price(String sell_price) {
            this.sell_price = sell_price;
        }
    }
}
