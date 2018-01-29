package com.gather_excellent_help.bean.soutao;

/**
 * Created by Dapeng Fang on 2018/1/18.
 */

public class SouTaobaoHelpBean {
    private int hind;
    private String f_title;
    private String s_title;

    public int getHind() {
        return hind;
    }

    public void setHind(int hind) {
        this.hind = hind;
    }

    public String getF_title() {
        return f_title;
    }

    public void setF_title(String f_title) {
        this.f_title = f_title;
    }

    public String getS_title() {
        return s_title;
    }

    public void setS_title(String s_title) {
        this.s_title = s_title;
    }

    @Override
    public String toString() {
        return "SouTaobaoHelpBean{" +
                "hind=" + hind +
                ", f_title='" + f_title + '\'' +
                ", s_title='" + s_title + '\'' +
                '}';
    }
}
