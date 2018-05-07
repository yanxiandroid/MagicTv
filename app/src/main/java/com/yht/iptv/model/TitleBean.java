package com.yht.iptv.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2017/10/19.
 */

public class TitleBean implements Parcelable {

    private int id;
    private String name;
    private String type;

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

    public TitleBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TitleBean() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.type);
    }

    protected TitleBean(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<TitleBean> CREATOR = new Parcelable.Creator<TitleBean>() {
        @Override
        public TitleBean createFromParcel(Parcel source) {
            return new TitleBean(source);
        }

        @Override
        public TitleBean[] newArray(int size) {
            return new TitleBean[size];
        }
    };
}
