package com.yht.iptv.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/12/28.
 * 订单详情页面模型层
 */
public class OrderDetailBean implements Parcelable {

    //身份证ID
    private String customerId;
    //身份名称
    private String customerName;
    //订单时间
    private String dateTime;
    //订单ID
    private int id;
    //订单号
    private String orderNum;
    //客房id
    private String roomId;
    //订单状态
    private int status;
    //总价
    private String totalPrice;
    //订单信息
    private List<OrderDetail> orderDetail;


    public static Creator<OrderDetailBean> getCreator() {
        return CREATOR;
    }
    // 属性是必须的要有的
    public static final Creator<OrderDetailBean> CREATOR = new Creator<OrderDetailBean>() {
        public OrderDetailBean createFromParcel(Parcel in) {
            OrderDetailBean data = new OrderDetailBean();
            data.customerId = in.readString();
            data.customerName = in.readString();
            data.dateTime = in.readString();
            data.id = in.readInt();
            data.orderNum = in.readString();
            data.roomId = in.readString();
            data.status = in.readInt();
            data.totalPrice = in.readString();
            data.orderDetail = new ArrayList<>();
            in.readList(data.orderDetail, OrderDetail.class.getClassLoader());
            return data;
        }

        @Override
        public OrderDetailBean[] newArray(int size) {
            return new OrderDetailBean[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerId);
        dest.writeString(customerName);
        dest.writeString(dateTime);
        dest.writeInt(id);
        dest.writeString(orderNum);
        dest.writeString(roomId);
        dest.writeInt(status);
        dest.writeString(totalPrice);
        dest.writeList(orderDetail);
    }

    public static class OrderDetail implements Serializable{
        /**
         * 大类ID
         */
        private int bigCategory;
        /**
         * 大类名
         */
        private String bigCategoryName;
        /**
         * 订单某条数据的ID
         */
        private int id;
        /**
         * 订单某条数据的数量
         */
        private int num;
        private int orderId;
        private int isPay;
        private String path;
        /**
         * 订单某条数据是否已消费 0 未消费
         */
        private int isConsume;
        /**
         * 订单某条数据价格
         */
        private String price;
        /**
         * 项目id
         */
        private int serverId;
        /**
         * 项目名称
         */
        private String serverName;
        /**
         * 小类ID
         */
        private int smallCategory;
        /**
         * 小类名
         */
        private String smallCategoryName;
        /**
         * 订单某条数据小计
         */
        private String subTotalPrice;

        public int getBigCategory() {
            return bigCategory;
        }

        public void setBigCategory(int bigCategory) {
            this.bigCategory = bigCategory;
        }

        public String getBigCategoryName() {
            return bigCategoryName;
        }

        public void setBigCategoryName(String bigCategoryName) {
            this.bigCategoryName = bigCategoryName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getIsPay() {
            return isPay;
        }

        public void setIsPay(int isPay) {
            this.isPay = isPay;
        }

        public int getIsConsume() {
            return isConsume;
        }

        public void setIsConsume(int isConsume) {
            this.isConsume = isConsume;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getServerId() {
            return serverId;
        }

        public void setServerId(int serverId) {
            this.serverId = serverId;
        }

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public int getSmallCategory() {
            return smallCategory;
        }

        public void setSmallCategory(int smallCategory) {
            this.smallCategory = smallCategory;
        }

        public String getSmallCategoryName() {
            return smallCategoryName;
        }

        public void setSmallCategoryName(String smallCategoryName) {
            this.smallCategoryName = smallCategoryName;
        }

        public String getSubTotalPrice() {
            return subTotalPrice;
        }

        public void setSubTotalPrice(String subTotalPrice) {
            this.subTotalPrice = subTotalPrice;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }


    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderDetail> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderDetail> orderDetail) {
        this.orderDetail = orderDetail;
    }


}
