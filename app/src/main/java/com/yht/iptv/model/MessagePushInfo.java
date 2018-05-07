package com.yht.iptv.model;

/**
 * Created by admin on 2017/11/10.
 */

public class MessagePushInfo {

    /**
     * id : 6
     * message : {"status":1,"content":"Test 5! Repeat! Test 5!"}
     * tag : all
     * pushStatus : 1
     * pushTime : 2017-06-15 14:52:25.0
     */

    private int id;
    private String message;
    private String tag;
    private int pushStatus;
    private String pushTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(int pushStatus) {
        this.pushStatus = pushStatus;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }
}
