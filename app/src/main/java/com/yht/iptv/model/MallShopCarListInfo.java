package com.yht.iptv.model;

import java.util.List;

/**
 * Created by admin on 2017/8/17.
 */

public class MallShopCarListInfo {

    /*****酒店ID*****/
    private String hotelId;
    /*****房间号*****/
    private String RoomId;
    /*****商品总数*****/
    private int goodsAllNum;
    private String userId;

    private List<MallShopCarInfo> mallShopCarInfos;

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }

    public List<MallShopCarInfo> getMallShopCarInfos() {
        return mallShopCarInfos;
    }

    public void setMallShopCarInfos(List<MallShopCarInfo> mallShopCarInfos) {
        this.mallShopCarInfos = mallShopCarInfos;
    }

    public int getGoodsAllNum() {
        return goodsAllNum;
    }

    public void setGoodsAllNum(int goodsAllNum) {
        this.goodsAllNum = goodsAllNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
