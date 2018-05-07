package com.yht.iptv.model;

/**
 * Created by admin on 2017/11/10.
 */

public class MinaStatus {

    private boolean status;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
