package com.yht.iptv.presenter;

import android.content.Context;

import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.MainUIModel;
import com.yht.iptv.model.WeatherBean;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

public class MainNavigationPresenter {
    private IPresenterBase<List<MainUIModel>> iPresenterBase;
    private Context mContext;

    public MainNavigationPresenter(Context mContext, IPresenterBase<List<MainUIModel>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }

    public void request(Object tag) {
        HttpParams params = new HttpParams();
        params.put("lang",Constants.currentLanguage);
        String url = HttpConstants.HTTP_HOST + HttpConstants.MAIN_NAVIGATION;
        OkHttpUtils.getJson(url,tag, params, new JsonCallback<BaseModel<List<MainUIModel>>>() {

            @Override
            public void onSuccess(Response<BaseModel<List<MainUIModel>>> response) {
                BaseModel<List<MainUIModel>> body = response.body();
                if (body.data.size() != 0 && body.data.get(0) != null) {
                    iPresenterBase.onSuccess(body);
                } else {
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onError(Response<BaseModel<List<MainUIModel>>> response) {
                iPresenterBase.onError();
            }
        });
    }
}