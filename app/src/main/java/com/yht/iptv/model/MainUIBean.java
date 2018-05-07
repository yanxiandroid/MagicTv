package com.yht.iptv.model;

/**
 * Created by admin on 2018/3/19.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 导航类别
 */
public class MainUIBean implements Parcelable {

    private String name;//名字
    private int icon;//图标
    private int iconSelected;//选中图标
    private String type;//标识
    private int tag;
    private List<MainUIModel.SecServiceListBean> secServiceList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIconSelected() {
        return iconSelected;
    }

    public void setIconSelected(int iconSelected) {
        this.iconSelected = iconSelected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public MainUIBean() {
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public List<MainUIModel.SecServiceListBean> getSecServiceList() {
        return secServiceList;
    }

    public void setSecServiceList(List<MainUIModel.SecServiceListBean> secServiceList) {
        this.secServiceList = secServiceList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.icon);
        dest.writeInt(this.iconSelected);
        dest.writeString(this.type);
        dest.writeInt(this.tag);
        dest.writeTypedList(this.secServiceList);
    }

    protected MainUIBean(Parcel in) {
        this.name = in.readString();
        this.icon = in.readInt();
        this.iconSelected = in.readInt();
        this.type = in.readString();
        this.tag = in.readInt();
        this.secServiceList = in.createTypedArrayList(MainUIModel.SecServiceListBean.CREATOR);
    }

    public static final Creator<MainUIBean> CREATOR = new Creator<MainUIBean>() {
        @Override
        public MainUIBean createFromParcel(Parcel source) {
            return new MainUIBean(source);
        }

        @Override
        public MainUIBean[] newArray(int size) {
            return new MainUIBean[size];
        }
    };
}
