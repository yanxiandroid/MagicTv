package com.yht.iptv.model;

/**
 * Created by admin on 2017/10/17.
 */

public class EventNetwork {

    private boolean isNetEnable;

    public EventNetwork(boolean isNetEnable) {
        this.isNetEnable = isNetEnable;
    }

    public boolean isNetEnable() {
        return isNetEnable;
    }

    public void setNetEnable(boolean netEnable) {
        isNetEnable = netEnable;
    }
}
