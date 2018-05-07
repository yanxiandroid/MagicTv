package com.yht.iptv.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Q on 2017/10/13.
 */

public class MediaDetailBean implements Parcelable {
    private List<Attachments> attachments;
    private List<Category> category;
    private String categoryId;
    private String description;
    private String director;
    private List<District> district;
    private FileUpload fileUpload;
    private BackgroundImage backgroundImage;
    private int id;
    private String name;
    private String playTime;
    private String starred;
    private String typeId;
    private float comboPrice;
    private float price;
    private int videoPayment;
    private long previewBegin;//预览的开始时间，单位毫秒
    private long previewLength;//预览的长度，单位毫秒

    public static Creator<MediaDetailBean> getCreator() {
        return CREATOR;
    }

    // 属性是必须的要有的
    public static final Creator<MediaDetailBean> CREATOR = new Creator<MediaDetailBean>() {
        public MediaDetailBean createFromParcel(Parcel in) {
            MediaDetailBean data = new MediaDetailBean();
            data.attachments = new ArrayList<>();
            in.readList(data.attachments, Attachments.class.getClassLoader());
            data.category = new ArrayList<>();
            in.readList(data.category, Category.class.getClassLoader());
            data.categoryId = in.readString();
            data.description = in.readString();
            data.director = in.readString();
            data.district = new ArrayList<>();
            in.readList(data.district, District.class.getClassLoader());
            data.fileUpload = (FileUpload) in.readValue(FileUpload.class.getClassLoader());
            data.id = in.readInt();
            data.name = in.readString();
            data.playTime = in.readString();
            data.starred = in.readString();
            data.typeId = in.readString();
            data.comboPrice = in.readFloat();
            data.price = in.readFloat();
            data.videoPayment = in.readInt();
            data.previewBegin = in.readLong();
            data.previewLength = in.readLong();
            return data;
        }

        @Override
        public MediaDetailBean[] newArray(int size) {
            return new MediaDetailBean[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(attachments);
        dest.writeList(category);
        dest.writeString(categoryId);
        dest.writeString(description);
        dest.writeString(director);
        dest.writeList(district);
        dest.writeValue(fileUpload);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(playTime);
        dest.writeString(starred);
        dest.writeString(typeId);
        dest.writeFloat(comboPrice);
        dest.writeFloat(price);
        dest.writeInt(videoPayment);
        dest.writeLong(previewBegin);
        dest.writeLong(previewLength);
    }


    public static class Attachments implements Serializable {
        @SerializedName("filenameindisk")
        private String f_pathSvr;
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getF_pathSvr() {
            return f_pathSvr;
        }

        public void setF_pathSvr(String f_pathSvr) {
            this.f_pathSvr = f_pathSvr;
        }
    }

    public static class Category implements Serializable {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class District implements Serializable {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

    public static class BackgroundImage implements Serializable {
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class Type implements Serializable {
        private int categoryId;
        private int id;
        private String name;

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public List<Attachments> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachments> attachments) {
        this.attachments = attachments;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public List<District> getDistrict() {
        return district;
    }

    public void setDistrict(List<District> district) {
        this.district = district;
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

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    public String getStarred() {
        return starred;
    }

    public void setStarred(String starred) {
        this.starred = starred;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public float getComboPrice() {
        return comboPrice;
    }

    public void setComboPrice(float comboPrice) {
        this.comboPrice = comboPrice;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getVideoPayment() {
        return videoPayment;
    }

    public void setVideoPayment(int videoPayment) {
        this.videoPayment = videoPayment;
    }

    public BackgroundImage getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(BackgroundImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public long getPreviewBegin() {
        return previewBegin;
    }

    public void setPreviewBegin(long previewBegin) {
        this.previewBegin = previewBegin;
    }

    public long getPreviewLength() {
        return previewLength;
    }

    public void setPreviewLength(long previewLength) {
        this.previewLength = previewLength;
    }
}
