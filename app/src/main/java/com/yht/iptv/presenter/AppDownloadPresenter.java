package com.yht.iptv.presenter;

import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.AppMangerInfo;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.utils.AppUtils;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

public class AppDownloadPresenter {

    private IPresenterBase<List<AppMangerInfo>> iPresenterBase;

    public AppDownloadPresenter(IPresenterBase<List<AppMangerInfo>> iPresenterBase) {
        this.iPresenterBase = iPresenterBase;
    }

    public void request() {
        HttpParams params = new HttpParams();
        params.put("version","V" + AppUtils.getAppVersionName() + "_" + AppUtils.getAppVersionCode());
        params.put("type","1");
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.APP_DOWNLOAD, params, new JsonCallback<BaseModel<List<AppMangerInfo>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<AppMangerInfo>>> response) {
                BaseModel<List<AppMangerInfo>> body = response.body();
                iPresenterBase.onSuccess(body);
            }

            @Override
            public void onError(Response<BaseModel<List<AppMangerInfo>>> response) {
                super.onError(response);
                iPresenterBase.onError();
            }
        });
    }

    public void requestPhone(Object tag) {
        HttpParams params = new HttpParams();
        params.put("version","");
        params.put("type","2");
        long cacheTime = 12 * 3600 * 1000;
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.APP_DOWNLOAD, CacheMode.IF_NONE_CACHE_REQUEST,cacheTime,tag,params, new JsonCallback<BaseModel<List<AppMangerInfo>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<AppMangerInfo>>> response) {
                BaseModel<List<AppMangerInfo>> body = response.body();
                iPresenterBase.onSuccess(body);
            }

            @Override
            public void onError(Response<BaseModel<List<AppMangerInfo>>> response) {
                super.onError(response);
                iPresenterBase.onError();
            }
        });
    }

    public void destory(){
        iPresenterBase = null;
    }
}