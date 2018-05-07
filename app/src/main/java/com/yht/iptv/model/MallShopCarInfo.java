package com.yht.iptv.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by admin on 2017/8/17.
 */

@Table(name = "MallShopCarInfo")
public class MallShopCarInfo {

    @Column(name = "goodsId_typeId" ,isId = true)
    private String goodsId_typeId;

    @Column(name = "goodsId")
    private String goodsid;

    @Column(name = "thumb")
    private String thumb;

    @Column(name = "goodsTitle")
    private String goodsTitle;

    @Column(name = "goodsPrice")
    private String goodsPrice;

    @Column(name = "goodsNum")
    private String total;

    @Column(name = "typeId1")
    private String typeId1;

    @Column(name = "typeName1")
    private String typeName1;

    @Column(name = "typeId2")
    private String typeId2;

    @Column(name = "typeName2")
    private String typeName2;

    @Column(name = "type_id")
    private String optionid;


    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }


    public String getTypeId1() {
        return typeId1;
    }

    public void setTypeId1(String typeId1) {
        this.typeId1 = typeId1;
    }

    public String getTypeName1() {
        return typeName1;
    }

    public void setTypeName1(String typeName1) {
        this.typeName1 = typeName1;
    }

    public String getTypeId2() {
        return typeId2;
    }

    public void setTypeId2(String typeId2) {
        this.typeId2 = typeId2;
    }

    public String getTypeName2() {
        return typeName2;
    }

    public void setTypeName2(String typeName2) {
        this.typeName2 = typeName2;
    }


    public String getGoodsId_typeId() {
        return goodsId_typeId;
    }

    public void setGoodsId_typeId(String goodsId_typeId) {
        this.goodsId_typeId = goodsId_typeId;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOptionid() {
        return optionid;
    }

    public void setOptionid(String optionid) {
        this.optionid = optionid;
    }
}
