package com.yht.iptv.model;

import java.io.Serializable;

/**
 * Created by admin on 2016/8/8.
 */
public class SocketInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String checkCode;
    private String movieUrl;
    private String movieName;
    private String currentPosition;
    private String vedioId;
    private int videoPayment;
    private int status;//0 :未支付 1 :已支付
    private double comboPrice; //视频包日价格
    private double price;//视频单片价格


    public String getMovieUrl() {
        return movieUrl;
    }

    public void setMovieUrl(String movieUrl) {
        this.movieUrl = movieUrl;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getVedioId() {
        return vedioId;
    }

    public void setVedioId(String vedioId) {
        this.vedioId = vedioId;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }


    public int getVideoPayment() {
        return videoPayment;
    }

    public void setVideoPayment(int videoPayment) {
        this.videoPayment = videoPayment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getComboPrice() {
        return comboPrice;
    }

    public void setComboPrice(double comboPrice) {
        this.comboPrice = comboPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
