package com.yht.iptv.callback;

import com.yht.iptv.model.BaseModel;

/**
 * Created by admin on 2018/1/8.
 */

public interface IPresenterDownloadBase<T> {

    void onSuccess(BaseModel<T> dataList);

    void onDownLoadSuccess(String fileName);

    void onError();

}
