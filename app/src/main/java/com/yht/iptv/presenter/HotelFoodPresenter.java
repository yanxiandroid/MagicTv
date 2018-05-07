package com.yht.iptv.presenter;

import android.content.Context;

import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.HotelFoodBean;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.LanguageUtils;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;


/**
 * 餐厅列表
 */
public class HotelFoodPresenter {

    private IPresenterBase<List<HotelFoodBean>> iPresenterBase;
    private Context mContext;

    public HotelFoodPresenter(Context mContext, IPresenterBase<List<HotelFoodBean>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }


    public void request(Object tag){

        HttpParams params = new HttpParams();
        params.put("lang", Constants.currentLanguage);
        long cacheTime = 1000 * 3600 * 2;
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.HOTEL_FOOD, CacheMode.IF_NONE_CACHE_REQUEST,cacheTime,tag, params, new JsonCallback<BaseModel<List<HotelFoodBean>>>() {

            @Override
            public void onSuccess(Response<BaseModel<List<HotelFoodBean>>> response) {
                BaseModel<List<HotelFoodBean>> body = response.body();
                if(body.data != null && body.data.size() !=0){
                    iPresenterBase.onSuccess(body);
                }else{
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onCacheSuccess(Response<BaseModel<List<HotelFoodBean>>> response) {
                onSuccess(response);
            }

            @Override
            public void onError(Response<BaseModel<List<HotelFoodBean>>> response) {
                iPresenterBase.onError();
            }
        });

    }
}
