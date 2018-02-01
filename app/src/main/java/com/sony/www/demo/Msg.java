package com.sony.www.demo;

/**
 * Created by nsh on 2017/10/24.
 */

public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SEND = 1;

    private String cotent;
    private int type;

    public Msg(String cotent, int type) {
        this.cotent = cotent;
        this.type = type;
    }

    public String getCotent() {
        return cotent;
    }

    public int getType() {
        return type;
    }
}
