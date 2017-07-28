package com.gather_excellent_help.bean;

import java.util.List;

/**
 * Created by Dapeng Fang on 2017/7/28.
 */

public class QiangTaoBean {

    /**
     * statusCode : 1
     * statusMessage : 查询成功
     * data : [{"title":"柏油克星龟牌柏油沥青清洁剂","link_url":"https://s.click.taobao.com/t?e=m%3D2%26s%3DcofXbb3xFD8cQipKwQzePOeEDrYVVa64yK8Cckff7TVRAdhuF14FMfs2n%2Fobe8nI5x%2BIUlGKNpUeGl4jyMMV37k1nu3lAqHHhxBBTFOcdpqhkCTRoYshS4tgwMO1YqTJ4q%2BRXokHRqv%2F41rxmOzgOb0OPldk3vZVauqdmAeAMfjTGoVSJwaubTz%2BBThPLxWta7r4UOyt%2Flg%2B5QowgvHJPA%3D%3D","img_url":"http://img4.tbcdn.cn/tfscom///gju1.alicdn.com/tps/i2/1285405001677980277/TB2P69pd.dnpuFjSZPhXXbChpXa_!!0-juitemmedia.jpg","productId":14604106268,"market_price":"19.80","sell_price":"8.8","start_time":"2017-07-28 10:00:00","end_time":"2017-07-29 09:59:59"},{"title":"Kaman便携旅行化妆包洗漱包","link_url":"https://s.click.taobao.com/t?e=m%3D2%26s%3DQ8neL7NZDC4cQipKwQzePOeEDrYVVa64yK8Cckff7TVRAdhuF14FMfs2n%2Fobe8nI5x%2BIUlGKNpUeGl4jyMMV37k1nu3lAqHHhxBBTFOcdpqhkCTRoYshS4tgwMO1YqTJ4q%2BRXokHRqv%2F41rxmOzgOaySAD9Q2bbyPGpJqe8TK5sP4YG%2FFclG2rK6ixxJmwNRkkPC%2BKnZ4dXM4%2FXSm%2FGd4qJn5AyUbPoV","img_url":"http://img4.tbcdn.cn/tfscom///gju1.alicdn.com/tps/i1/164040261600287708/TB2TihptVXXXXX0XpXXXXXXXXXX_!!0-juitemmedia.jpg","productId":529272915678,"market_price":"58.00","sell_price":"9.4","start_time":"2017-07-28 12:00:00","end_time":"2017-07-29 11:59:59"},{"title":"微波炉防烫加厚隔热厨房手套","link_url":"https://s.click.taobao.com/t?e=m%3D2%26s%3D1dHZ7HoPd5wcQipKwQzePOeEDrYVVa64yK8Cckff7TVRAdhuF14FMfs2n%2Fobe8nI5x%2BIUlGKNpUeGl4jyMMV37k1nu3lAqHHhxBBTFOcdpqhkCTRoYshS4tgwMO1YqTJ4q%2BRXokHRqv%2F41rxmOzgOTD56875Abwdzcadu2wjjqBV1v21RJ2puZztW3LWtAOukkPC%2BKnZ4dXM4%2FXSm%2FGd4qJn5AyUbPoV","img_url":"http://img4.tbcdn.cn/tfscom///gju4.alicdn.com/tps/i1/1252906084142003878/TB2lj4CgDAKh1JjSZFDXXbKlFXa_!!0-juitemmedia.jpg","productId":554772394608,"market_price":"88.00","sell_price":"9.8","start_time":"2017-07-28 14:00:00","end_time":"2017-07-29 13:59:59"},{"title":"男女士香水香体止汗走珠露","link_url":"http://item.taobao.com/item.htm?id=554205219696","img_url":"http://img4.tbcdn.cn/tfscom///gju3.alicdn.com/tps/i1/1977206091519601181/TB2ZhVza77myKJjSZFkXXa3vVXa_!!0-juitemmedia.jpg","productId":554205219696,"market_price":"38.00","sell_price":"9.8","start_time":"2017-07-28 14:00:00","end_time":"2017-07-29 13:59:59"},{"title":"鸡翅木无蜡实木筷子家用10双装","link_url":"https://s.click.taobao.com/t?e=m%3D2%26s%3DXI2flv1v4swcQipKwQzePOeEDrYVVa64yK8Cckff7TVRAdhuF14FMfs2n%2Fobe8nI5x%2BIUlGKNpUeGl4jyMMV37k1nu3lAqHHhxBBTFOcdpqhkCTRoYshS4tgwMO1YqTJ4q%2BRXokHRqv%2F41rxmOzgOTD56875Abwdzcadu2wjjqCyw71MOfpQcmjNchwJ34oskkPC%2BKnZ4dXM4%2FXSm%2FGd4qJn5AyUbPoV","img_url":"http://img4.tbcdn.cn/tfscom///gju1.alicdn.com/tps/i3/1252906077338172365/TB2NFGlzHlmpuFjSZFlXXbdQXXa_!!0-juitemmedia.jpg","productId":548981248913,"market_price":"29.00","sell_price":"9.8","start_time":"2017-07-28 14:00:00","end_time":"2017-07-29 13:59:59"}]
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
         * title : 柏油克星龟牌柏油沥青清洁剂
         * link_url : https://s.click.taobao.com/t?e=m%3D2%26s%3DcofXbb3xFD8cQipKwQzePOeEDrYVVa64yK8Cckff7TVRAdhuF14FMfs2n%2Fobe8nI5x%2BIUlGKNpUeGl4jyMMV37k1nu3lAqHHhxBBTFOcdpqhkCTRoYshS4tgwMO1YqTJ4q%2BRXokHRqv%2F41rxmOzgOb0OPldk3vZVauqdmAeAMfjTGoVSJwaubTz%2BBThPLxWta7r4UOyt%2Flg%2B5QowgvHJPA%3D%3D
         * img_url : http://img4.tbcdn.cn/tfscom///gju1.alicdn.com/tps/i2/1285405001677980277/TB2P69pd.dnpuFjSZPhXXbChpXa_!!0-juitemmedia.jpg
         * productId : 14604106268
         * market_price : 19.80
         * sell_price : 8.8
         * start_time : 2017-07-28 10:00:00
         * end_time : 2017-07-29 09:59:59
         */

        private String title;
        private String link_url;
        private String img_url;
        private long productId;
        private String market_price;
        private String sell_price;
        private String start_time;
        private String end_time;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
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

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }
    }
}
