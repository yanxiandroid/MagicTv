package com.yht.iptv.presenter;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.NearGeneralBean;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by Q on 2018/1/17.
 */

public class NearGeneralPresenter {

    private IPresenterBase<List<NearGeneralBean>> infoIPresenterBase;

    public NearGeneralPresenter(IPresenterBase<List<NearGeneralBean>> infoIPresenterBase) {
        this.infoIPresenterBase = infoIPresenterBase;
    }

    public void request(Object tag, String url) {
        HttpParams params = new HttpParams();
        params.put("lang", Constants.currentLanguage);
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + url, tag, params, new JsonCallback<BaseModel<List<NearGeneralBean>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<NearGeneralBean>>> response) {
                List<NearGeneralBean> data = response.body().data;
                if (data != null && data.size() > 0) {
                    infoIPresenterBase.onSuccess(response.body());
                } else {
                    infoIPresenterBase.onError();
                }
            }

            @Override
            public void onError(Response<BaseModel<List<NearGeneralBean>>> response) {
                super.onError(response);
                infoIPresenterBase.onError();
            }
        });
    }
}
