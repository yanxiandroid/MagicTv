package com.yht.iptv.presenter;

import android.content.Context;

import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.MainPageInfo;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.LanguageUtils;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by admin on 2016/5/25.
 * 视频类型
 */
public class MainPagePresenter {

    private IPresenterBase<List<MainPageInfo>> iPresenterBase;
    private Context mContext;

    public MainPagePresenter(Context mContext, IPresenterBase<List<MainPageInfo>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }

    public void request(String room_id,Object tag){

        HttpParams params = new HttpParams();
        params.put("roomId",room_id);
        params.put("lang", Constants.currentLanguage);
        long cacheTime = 1000 * 10;
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.MAIN_PAGE_INFO, CacheMode.IF_NONE_CACHE_REQUEST,cacheTime,tag, params, new JsonCallback<BaseModel<List<MainPageInfo>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<MainPageInfo>>> response) {
                BaseModel<List<MainPageInfo>> body = response.body();
                List<MainPageInfo> data = body.data;
                if(data != null && data.size()!= 0){
                    iPresenterBase.onSuccess(body);
                }else{
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onCacheSuccess(Response<BaseModel<List<MainPageInfo>>> response) {
                onSuccess(response);
            }

            @Override
            public void onError(Response<BaseModel<List<MainPageInfo>>> response) {
                iPresenterBase.onError();
            }
        });
    }
}
