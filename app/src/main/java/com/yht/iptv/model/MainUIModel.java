package com.yht.iptv.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by admin on 2018/3/19.
 * 导航界面的设置
 */

public class MainUIModel {

    /**
     * display : 1
     * icon : {"fileName":"英年早逝.jpg","fileType":"jpg","id":182,"path":"C:/apache-tomcat-8.0.0-RC1/webapps/hotelServer/images/英年早逝.jpg"}
     * id : 1
     * menuName : 酒店服务
     * orderVal : 1
     * secServiceList : [{"api":"0","display":1,"id":4,"menuName":"酒店消息","orderVal":1,"tag":1},{"api":"/iptv.api/introduce/getAllList","display":1,"id":3,"menuName":"酒店介绍","orderVal":2,"tag":0},{"api":"/iptv.api/etiquette/getAllList","display":1,"id":2,"menuName":"礼宾服务","orderVal":3,"tag":0},{"api":"/iptv.api/recreation/getList","display":1,"id":1,"menuName":"康乐服务","orderVal":4,"tag":0},{"api":"/iptv.api/reserve/getAllList","display":1,"id":5,"menuName":"会务服务","orderVal":5,"tag":0},{"api":"0","display":1,"id":6,"menuName":"客房服务","orderVal":6,"tag":2}]
     * tag : 5
     */

    private int display;
    private IconBean icon;
    private int id;
    private String menuName;
    private int orderVal;
    private int tag;
    private List<SecServiceListBean> secServiceList;

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public IconBean getIcon() {
        return icon;
    }

    public void setIcon(IconBean icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getOrderVal() {
        return orderVal;
    }

    public void setOrderVal(int orderVal) {
        this.orderVal = orderVal;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public List<SecServiceListBean> getSecServiceList() {
        return secServiceList;
    }

    public void setSecServiceList(List<SecServiceListBean> secServiceList) {
        this.secServiceList = secServiceList;
    }

    public static class IconBean {
        /**
         * fileName : 英年早逝.jpg
         * fileType : jpg
         * id : 182
         * path : C:/apache-tomcat-8.0.0-RC1/webapps/hotelServer/images/英年早逝.jpg
         */

        private String fileName;
        private String fileType;
        private int id;
        private String path;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class SecServiceListBean implements Parcelable {
        /**
         * api : 0
         * display : 1
         * id : 4
         * menuName : 酒店消息
         * orderVal : 1
         * tag : 1
         */

        private String api;
        private int display;
        private int id;
        private String menuName;
        private int orderVal;
        private int tag;

        public String getApi() {
            return api;
        }

        public void setApi(String api) {
            this.api = api;
        }

        public int getDisplay() {
            return display;
        }

        public void setDisplay(int display) {
            this.display = display;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }

        public int getOrderVal() {
            return orderVal;
        }

        public void setOrderVal(int orderVal) {
            this.orderVal = orderVal;
        }

        public int getTag() {
            return tag;
        }

        public void setTag(int tag) {
            this.tag = tag;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.api);
            dest.writeInt(this.display);
            dest.writeInt(this.id);
            dest.writeString(this.menuName);
            dest.writeInt(this.orderVal);
            dest.writeInt(this.tag);
        }

        public SecServiceListBean() {
        }

        protected SecServiceListBean(Parcel in) {
            this.api = in.readString();
            this.display = in.readInt();
            this.id = in.readInt();
            this.menuName = in.readString();
            this.orderVal = in.readInt();
            this.tag = in.readInt();
        }

        public static final Parcelable.Creator<SecServiceListBean> CREATOR = new Parcelable.Creator<SecServiceListBean>() {
            @Override
            public SecServiceListBean createFromParcel(Parcel source) {
                return new SecServiceListBean(source);
            }

            @Override
            public SecServiceListBean[] newArray(int size) {
                return new SecServiceListBean[size];
            }
        };
    }
}
