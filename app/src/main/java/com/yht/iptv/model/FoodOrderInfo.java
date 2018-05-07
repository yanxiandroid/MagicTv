package com.yht.iptv.model;

import java.util.List;

/**
 * Created by admin on 2017/11/14.
 */

public class FoodOrderInfo {


    /**
     * id : 24
     * orderNum : DC_18711923651510303381827482
     * dinesIds : 1,
     * dinesJSON : [{"id":1,"count":1}]
     * roomNum : 1212
     * userId : 顾客
     * price : 0.01
     * status : 0
     * payment : null
     * createTime : 11-10 16:43
     * returnCode : {"wxAPP":{"nonce_str":"uJ8F0AonnWmN19es","secondSign":"E9298E3C4310CD2405246938261B8833","appid":"wx67de71576d9666a3","sign":"76341AB11DAF821803069EAA486C535E","trade_type":"APP","return_msg":"OK","result_code":"SUCCESS","mch_id":"1487107782","return_code":"SUCCESS","prepay_id":"wx201711101643229c6167dbc40091833889","timestamp":"1510303402"},"aliAPP":"alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2017080408033599&biz_content=%7B%22body%22%3A%22%E5%8D%8E%E5%85%83105%E5%A4%A7%E9%85%92%E5%BA%97+-+%E7%82%B9%E9%A4%90%E6%9C%8D%E5%8A%A1%22%2C%22out_trade_no%22%3A%22DC_18711923651510303381827482%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E5%8D%8E%E5%85%83105%E5%A4%A7%E9%85%92%E5%BA%97+-+%E7%82%B9%E9%A4%90%E6%9C%8D%E5%8A%A1%22%2C%22total_amount%22%3A%220.01%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fwww.hyzc.ltd%3A8080%2FcloudServer%2Fdatasynchronizing%2FpaymentAPI%2FaliCallback&sign=W0dlgRCQyfh4G920l5Kcow8wHXlP5grJK1IR6NBlyusvnuyPvRRyPu5k3qEFHxKM1ibT6NNOZstqAbpFgmZPmV9I7OD7VWCl06ErbtBv01F2vz9M7okGk7qJnxmEcwkbnlwuSun%2BV0UqTvp1R2Hb0%2FYKzVvDBj4lGHfur%2F7yZLsWd1%2BLg8wCJ6b4ga7oHme%2FzOT7xEtxEqHF0Ukos3GshXmewHA2lqBpmwaGmawfrKCcYncdvgxMHEBTaIXymUzrm71QNe%2Fl6CVXuWfGw54L4QjPTGjzSuEqKrRKRwTtjWntbNplKqaSTMunFBx6kuoK7VgZ%2FGNvDJNOLha7Vc61dg%3D%3D&sign_type=RSA2&timestamp=2017-11-10+16%3A43%3A22&version=1.0"}
     * dinesList : [{"restaurant":null,"chef":null,"count":1,"description":"烙饼分发面和死面（不经发酵的面团）两类。面可硬可软，饼可薄可厚。死面饼，在面剂上抹油、盐（也有放芝麻酱、红糖的），烙出饼来层多，又称千层饼。","approvalTime":"2017-08-25 16:41:35","remark":"","fileUpload":{"path":"http://10.0.10.104:8080/hotelServer/images/1.468394407513665E12.jpg","fileName":"摄图网-.jpg","id":360,"fileType":"jpg"},"restaurantId":1,"type":"早餐","uploadTime":"2017-08-25 16:41:29","uploadUserId":1,"path":360,"price":"0.01","name":"太极芙蓉红燕","chefId":null,"approvalUserId":1,"id":1,"isPass":1}]
     * restaurantIds : 1
     */

    private int id;
    private String orderNum;
    private String dinesIds;
    private String dinesJSON;
    private String roomNum;
    private String userId;
    private float price;
    private int status;
    private Object payment;
    private String createTime;
    private String returnCode;
    private String restaurantIds;
    private List<DinesListBean> dinesList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getDinesIds() {
        return dinesIds;
    }

    public void setDinesIds(String dinesIds) {
        this.dinesIds = dinesIds;
    }

    public String getDinesJSON() {
        return dinesJSON;
    }

    public void setDinesJSON(String dinesJSON) {
        this.dinesJSON = dinesJSON;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getPayment() {
        return payment;
    }

    public void setPayment(Object payment) {
        this.payment = payment;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getRestaurantIds() {
        return restaurantIds;
    }

    public void setRestaurantIds(String restaurantIds) {
        this.restaurantIds = restaurantIds;
    }

    public List<DinesListBean> getDinesList() {
        return dinesList;
    }

    public void setDinesList(List<DinesListBean> dinesList) {
        this.dinesList = dinesList;
    }

    public static class DinesListBean {
        /**
         * restaurant : null
         * chef : null
         * count : 1
         * description : 烙饼分发面和死面（不经发酵的面团）两类。面可硬可软，饼可薄可厚。死面饼，在面剂上抹油、盐（也有放芝麻酱、红糖的），烙出饼来层多，又称千层饼。
         * approvalTime : 2017-08-25 16:41:35
         * remark :
         * fileUpload : {"path":"http://10.0.10.104:8080/hotelServer/images/1.468394407513665E12.jpg","fileName":"摄图网-.jpg","id":360,"fileType":"jpg"}
         * restaurantId : 1
         * type : 早餐
         * uploadTime : 2017-08-25 16:41:29
         * uploadUserId : 1
         * path : 360
         * price : 0.01
         * name : 太极芙蓉红燕
         * chefId : null
         * approvalUserId : 1
         * id : 1
         * isPass : 1
         */

        private Object restaurant;
        private Object chef;
        private int count;
        private String description;
        private String approvalTime;
        private String remark;
        private FileUploadBean fileUpload;
        private int restaurantId;
        private String type;
        private String uploadTime;
        private int uploadUserId;
        private int path;
        private String price;
        private String name;
        private Object chefId;
        private int approvalUserId;
        private int id;
        private int isPass;

        public Object getRestaurant() {
            return restaurant;
        }

        public void setRestaurant(Object restaurant) {
            this.restaurant = restaurant;
        }

        public Object getChef() {
            return chef;
        }

        public void setChef(Object chef) {
            this.chef = chef;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getApprovalTime() {
            return approvalTime;
        }

        public void setApprovalTime(String approvalTime) {
            this.approvalTime = approvalTime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public FileUploadBean getFileUpload() {
            return fileUpload;
        }

        public void setFileUpload(FileUploadBean fileUpload) {
            this.fileUpload = fileUpload;
        }

        public int getRestaurantId() {
            return restaurantId;
        }

        public void setRestaurantId(int restaurantId) {
            this.restaurantId = restaurantId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUploadTime() {
            return uploadTime;
        }

        public void setUploadTime(String uploadTime) {
            this.uploadTime = uploadTime;
        }

        public int getUploadUserId() {
            return uploadUserId;
        }

        public void setUploadUserId(int uploadUserId) {
            this.uploadUserId = uploadUserId;
        }

        public int getPath() {
            return path;
        }

        public void setPath(int path) {
            this.path = path;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getChefId() {
            return chefId;
        }

        public void setChefId(Object chefId) {
            this.chefId = chefId;
        }

        public int getApprovalUserId() {
            return approvalUserId;
        }

        public void setApprovalUserId(int approvalUserId) {
            this.approvalUserId = approvalUserId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsPass() {
            return isPass;
        }

        public void setIsPass(int isPass) {
            this.isPass = isPass;
        }

        public static class FileUploadBean {
            /**
             * path : http://10.0.10.104:8080/hotelServer/images/1.468394407513665E12.jpg
             * fileName : 摄图网-.jpg
             * id : 360
             * fileType : jpg
             */

            private String path;
            private String fileName;
            private int id;
            private String fileType;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getFileType() {
                return fileType;
            }

            public void setFileType(String fileType) {
                this.fileType = fileType;
            }
        }
    }
}
