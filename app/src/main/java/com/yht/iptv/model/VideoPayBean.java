package com.yht.iptv.model;

/**
 *
 */

public class VideoPayBean {


    /**
     * aliCode : https://qr.alipay.com/bax04153siwz748bd0zn4083
     * status : 0
     * wxCode : weixin://wxpay/bizpayurl?pr=CepgSk2
     */

    private String aliCode;
    private int status;
    private String wxCode;
    private String orderNum;

    public String getAliCode() {
        return aliCode;
    }

    public void setAliCode(String aliCode) {
        this.aliCode = aliCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getWxCode() {
        return wxCode;
    }

    public void setWxCode(String wxCode) {
        this.wxCode = wxCode;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
