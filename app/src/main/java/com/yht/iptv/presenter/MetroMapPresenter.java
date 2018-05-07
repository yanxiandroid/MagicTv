package com.yht.iptv.presenter;

import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.MetroMapBean;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by Q on 2018/1/17.
 */

public class MetroMapPresenter {
    private IPresenterBase<List<MetroMapBean>> iPresenterBase;

    public MetroMapPresenter(IPresenterBase<List<MetroMapBean>> iPresenterBase) {
        this.iPresenterBase = iPresenterBase;

    }


    public void request(Object tag) {
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.NEAR_SUBWAY, tag, new JsonCallback<BaseModel<List<MetroMapBean>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<MetroMapBean>>> response) {
                BaseModel<List<MetroMapBean>> body = response.body();
                if (body.data != null && body.data.size() != 0) {
                    iPresenterBase.onSuccess(body);
                } else {
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onError(Response<BaseModel<List<MetroMapBean>>> response) {
                iPresenterBase.onError();
            }
        });


    }
}
