package com.yht.iptv.callback;


import com.yht.iptv.model.BaseMallModel;

/**
 * Created by admin on 2016/10/18.
 * yanxi
 */
public interface IPresenterMallBase<T> {

    void onSuccess(BaseMallModel<T> dataList);
    void onError();
}
