package com.yht.iptv.presenter;

import android.content.Context;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.WeatherBean;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.LanguageUtils;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

public class WeatherPresenter {
    private IPresenterBase<List<WeatherBean>> iPresenterBase;
    private Context mContext;

    public WeatherPresenter(Context mContext, IPresenterBase<List<WeatherBean>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }

    public void request(Object tag) {
        long cacheTime = 1000 * 60 * 30;
        HttpParams params = new HttpParams();
        String url = HttpConstants.HTTP_HOST + HttpConstants.WEATHER_INFO;
        params.put("lang", Constants.currentLanguage);
        OkHttpUtils.getJson(url,CacheMode.IF_NONE_CACHE_REQUEST,cacheTime,tag, params, new JsonCallback<BaseModel<List<WeatherBean>>>() {

            @Override
            public void onSuccess(Response<BaseModel<List<WeatherBean>>> response) {
                BaseModel<List<WeatherBean>> body = response.body();
                if (body.data.size() != 0 && body.data.get(0) != null) {
                    iPresenterBase.onSuccess(body);
                } else {
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onCacheSuccess(Response<BaseModel<List<WeatherBean>>> response) {
                onSuccess(response);
            }

            @Override
            public void onError(Response<BaseModel<List<WeatherBean>>> response) {
                iPresenterBase.onError();
            }
        });
    }
}