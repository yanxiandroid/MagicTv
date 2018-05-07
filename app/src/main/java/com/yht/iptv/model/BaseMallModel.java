package com.yht.iptv.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/8/12.
 */

public class BaseMallModel<T> implements Serializable {


    /**
     * msg : 获取成功
     * result :
     * status : 1
     */

    public String msg;
    public T result;
    public String status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BaseMallModel toResponse() {
        BaseMallModel lzyResponse = new BaseMallModel();
        lzyResponse.status = status;
        lzyResponse.msg = msg;
        return lzyResponse;
    }
}
