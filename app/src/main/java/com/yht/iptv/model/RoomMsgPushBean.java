package com.yht.iptv.model;

/**
 * Created by admin on 2017/11/17.
 * 推送消息bean
 */
public class RoomMsgPushBean {

    private long id;

    private  String roomNum;

    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
