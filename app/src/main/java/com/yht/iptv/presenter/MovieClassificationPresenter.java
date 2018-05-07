package com.yht.iptv.presenter;

import android.content.Context;

import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.MovieClassificationBean;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.LanguageUtils;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by admin on 2016/5/25.
 * 视频分类接口
 */
public class MovieClassificationPresenter {

    private Context mContext;
    private IPresenterBase<List<MovieClassificationBean>> iPresenterBase;

    public MovieClassificationPresenter(Context mContext, IPresenterBase<List<MovieClassificationBean>> iPresenterBase) {
        this.iPresenterBase = iPresenterBase;
        this.mContext = mContext;
    }


    public void request(Object tag){
        HttpParams params = new HttpParams();
        params.put("lang", Constants.currentLanguage);
        long cacheTime = 1000 * 60 * 30;
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.MOVIE_CLASSIFICATION, CacheMode.IF_NONE_CACHE_REQUEST,cacheTime,tag, params, new JsonCallback<BaseModel<List<MovieClassificationBean>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<MovieClassificationBean>>> response) {
                BaseModel<List<MovieClassificationBean>> body = response.body();
                if(body.data != null && body.data.size()!=0){
                    iPresenterBase.onSuccess(body);
                }else{
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onCacheSuccess(Response<BaseModel<List<MovieClassificationBean>>> response) {
                onSuccess(response);
            }

            @Override
            public void onError(Response<BaseModel<List<MovieClassificationBean>>> response) {
                iPresenterBase.onError();
            }
        });

    }

}
