package com.yht.iptv.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 *
 */
public class HotelFoodListBean implements Parcelable {

    private int id;
    private String description;
    private FileUpload fileUpload;
    private String name;
    private String price;
    private String type;
    private int restaurantId;
    private Restaurant restaurant;

    public static Creator<HotelFoodListBean> getCreator() {
        return CREATOR;
    }

    // 属性是必须的要有的
    public static final Creator<HotelFoodListBean> CREATOR = new Creator<HotelFoodListBean>() {
        public HotelFoodListBean createFromParcel(Parcel in) {
            HotelFoodListBean data = new HotelFoodListBean();
            data.description = in.readString();
            data.fileUpload = (FileUpload) in.readValue(FileUpload.class.getClassLoader());
            data.name = in.readString();
            data.price = in.readString();
            data.type = in.readString();
            data.id = in.readInt();
            data.restaurantId = in.readInt();
            data.restaurant = (Restaurant) in.readValue(FileUpload.class.getClassLoader());
            return data;
        }

        @Override
        public HotelFoodListBean[] newArray(int size) {
            return new HotelFoodListBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeValue(fileUpload);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(type);
        dest.writeInt(id);
        dest.writeInt(restaurantId);
        dest.writeValue(restaurant);
    }

    public static class Restaurant implements Serializable {
        private String id;
        private String name;

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
    }

    public static class FileUpload implements Serializable {
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FileUpload getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
