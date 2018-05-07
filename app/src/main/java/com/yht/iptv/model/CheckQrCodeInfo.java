package com.yht.iptv.model;

/**
 *
 * Created by admin on 2016/9/19.
 *
 */
public class CheckQrCodeInfo {

    private String CheckCode;
    private String addressCode;
    private String hostAddressCode;
    private String roomId;
//    private String movieCode;

    public String getCheckCode() {
        return CheckCode;
    }

    public void setCheckCode(String checkCode) {
        CheckCode = checkCode;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getHostAddressCode() {
        return hostAddressCode;
    }

    public void setHostAddressCode(String hostAddressCode) {
        this.hostAddressCode = hostAddressCode;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

//    public String getMovieCode() {
//        return movieCode;
//    }
//
//    public void setMovieCode(String movieCode) {
//        this.movieCode = movieCode;
//    }
}
