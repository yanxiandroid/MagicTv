package com.yht.iptv.model;

import java.io.Serializable;

public class BaseModel<T> implements Serializable {

    private static final long serialVersionUID = 5213230387175987834L;

    public int code;
    public String msg;
    public T data;
    public int totalPage;

    @Override
    public String toString() {
        return "BaseModel{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public BaseModel toResponse() {
        BaseModel lzyResponse = new BaseModel();
        lzyResponse.code = code;
        lzyResponse.msg = msg;
        return lzyResponse;
    }


}