package com.gather_excellent_help.wxapi;

/**
 * 作者：Dapeng Fang on 2016/11/30 15:06
 * <p/>
 * 邮箱：fdp111888@163.com
 */

public class AccessTokenBean {

    /**
     * access_token : qBF5stiGCUgQk7GbXXouyPxU3_vAwsfRzpCHIMB0eA-dNrE6D3KZLSORVzKOtT6np3ReyAJjN1No51J0mBjcudALQKv4mwnmoNrKvTWImuc
     * expires_in : 7200
     * refresh_token : Df4A5liZ5CE2GePdB2txKNuUG6BPmEKm0Re5hXBwn2X9t8mkmnafwZxiL5uZDPcOS4w_WEUdxCl9gk8T6I9dtkTH_PumeYD6d2gLIry_8sc
     * openid : oUyCMxDUgM0z6SivDzo1kTu9LVbY
     * scope : snsapi_userinfo
     * unionid : oZ_4av6yxU5kbiwNQ9k-hEuYULUo
     */

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
