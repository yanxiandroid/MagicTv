package com.yht.iptv.callback;


import com.yht.iptv.model.BaseModel;

/**
 * Created by admin on 2016/10/18.
 * yanxi
 */
public interface IPresenterBase<T> {

    void onSuccess(BaseModel<T> dataList);

    void onError();
}
