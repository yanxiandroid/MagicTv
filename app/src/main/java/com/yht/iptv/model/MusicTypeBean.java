package com.yht.iptv.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2017/2/23.
 */
public class MusicTypeBean implements Parcelable {

    /**
     * id : 2
     * name : 流行音乐
     */

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }


    protected MusicTypeBean(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<MusicTypeBean> CREATOR = new Creator<MusicTypeBean>() {
        @Override
        public MusicTypeBean createFromParcel(Parcel source) {
            return new MusicTypeBean(source);
        }

        @Override
        public MusicTypeBean[] newArray(int size) {
            return new MusicTypeBean[size];
        }
    };
}
