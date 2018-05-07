package com.yht.iptv.model;

import java.util.List;

/**
 * Created by admin on 2016/12/20.
 */
public class OrderDetailInfo {

    /**
     * 房间号
     */
    private String roomId;

    /**
     * 客户身份证号
     */
    private String customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 订单总价格
     */
    private String totalPrice;

    private List<OrderList> orderDetail;

    public class OrderList {
        /**
         * 服务ID
         */
        private Long serverId;

        /**
         * 数量
         */
        private Long num;

        /**
         * 价格
         */
        private String price;

        /**
         * 小计价格
         */
        private String subTotalPrice;


        public Long getServerId() {
            return serverId;
        }

        public void setServerId(Long serverId) {
            this.serverId = serverId;
        }

        public Long getNum() {
            return num;
        }

        public void setNum(Long num) {
            this.num = num;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSubTotalPrice() {
            return subTotalPrice;
        }

        public void setSubTotalPrice(String subTotalPrice) {
            this.subTotalPrice = subTotalPrice;
        }
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderList> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderList> orderDetail) {
        this.orderDetail = orderDetail;
    }
}
