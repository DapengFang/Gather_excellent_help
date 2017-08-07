package com.gather_excellent_help.bean;

/**
 * Created by Dapeng Fang on 2017/8/4.
 */

public class QiangTimeBean {
    private int time;
    private boolean check;

    public QiangTimeBean() {
    }

    public QiangTimeBean(int time, boolean check) {
        this.time = time;
        this.check = check;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
