package com.gather_excellent_help.wxapi;

import java.util.List;

/**
 * 作者：Dapeng Fang on 2016/11/30 15:22
 * <p/>
 * 邮箱：fdp111888@163.com
 */

public class WchatUserInfoBean {

    /**
     * openid : oUyCMxDUgM0z6SivDzo1kTu9LVbY
     * nickname : 回忆づ回不到的地方
     * sex : 1
     * language : zh_CN
     * city : Ganzhou
     * province : Jiangxi
     * country : CN
     * headimgurl : http://wx.qlogo.cn/mmopen/LDx58sCofibC9du5nXqQkrbev3nichfY2w3uDtwoIXImDwxiav2RCWiaufUXUqKGnAboCchzibmfD4s6dAvQMydbe18NscPn25iauf/0
     * privilege : []
     * unionid : oZ_4av6yxU5kbiwNQ9k-hEuYULUo
     */

    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<?> privilege;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<?> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<?> privilege) {
        this.privilege = privilege;
    }
}
