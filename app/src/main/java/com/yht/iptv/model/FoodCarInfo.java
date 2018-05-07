package com.yht.iptv.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 餐厅数据库信息
 * Created by Q on 2017/10/27.
 */
@Table(name = "FoodCarInfo")
public class FoodCarInfo {

    @Column(name = "id", isId = true)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private String price;
    @Column(name = "num")
    private String num;
    @Column(name = "image")
    private String image;
    @Column(name = "foodId")
    private String foodId;
    @Column(name = "restaurantId")
    private String restaurantId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

}
