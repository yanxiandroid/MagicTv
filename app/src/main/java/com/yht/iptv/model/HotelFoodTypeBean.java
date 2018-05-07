package com.yht.iptv.model;

import java.io.Serializable;

public class HotelFoodTypeBean implements Serializable {

    private String type;
    private String typeEn;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeEn() {
        return typeEn;
    }

    public void setTypeEn(String typeEn) {
        this.typeEn = typeEn;
    }
}