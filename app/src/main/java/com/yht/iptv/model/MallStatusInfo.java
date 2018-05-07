package com.yht.iptv.model;

/**
 * Created by admin on 2017/8/22.
 */

public class MallStatusInfo {


    /**
     * isnew : false
     * cartcount : 2
     */

    private boolean isnew;
    private String cartcount;

    public boolean isIsnew() {
        return isnew;
    }

    public void setIsnew(boolean isnew) {
        this.isnew = isnew;
    }

    public String getCartcount() {
        return cartcount;
    }

    public void setCartcount(String cartcount) {
        this.cartcount = cartcount;
    }

}
