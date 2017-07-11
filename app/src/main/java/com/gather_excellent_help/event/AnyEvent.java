package com.gather_excellent_help.event;

/**
 * Created by wuxin on 2017/7/11.
 */

public class AnyEvent {
    private int type;
    private String message;

    public AnyEvent(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
