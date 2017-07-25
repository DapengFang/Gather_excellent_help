package com.gather_excellent_help.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.gather_excellent_help.utils.LogUtil;

import java.lang.ref.WeakReference;

/**
 * Created by Dapeng Fang on 2017/7/24.
 */

public class RushDownTimer {

    private String hour;//时
    private String  minute;//分
    private String second;//秒

    private Context context;

    public RushDownTimer(Context context) {
        this.context = context;
    }

    public void calcuteDownTimer(long time){
        int h = (int) (time/3600000);
        time = time%3600000;
        if(h<10) {
            hour = "0" + h;
        }else{
            hour = String.valueOf(h);
        }
        int m = (int) (time/60000);
        if(m<10) {
            minute = "0" + m;
        }else{
            minute = String.valueOf(m);
        }
        time = time%60000;
        int s = (int) (time/1000);
        if(s<10) {
            second = "0" + s;
        }else{
            second = String.valueOf(s);
        }
        LogUtil.e("hour =" +getHour() +",minute =" +getMinute() + ",second = " +getSecond());
    }


    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getSecond() {
        return second;
    }

}
