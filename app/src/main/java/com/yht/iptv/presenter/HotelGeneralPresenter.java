package com.yht.iptv.presenter;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.HotelGeneralInfo;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by admin on 2017/12/14.
 */

public class HotelGeneralPresenter {

    private IPresenterBase<List<HotelGeneralInfo>> infoIPresenterBase;

    public HotelGeneralPresenter(IPresenterBase<List<HotelGeneralInfo>> infoIPresenterBase){
        this.infoIPresenterBase = infoIPresenterBase;
    }

    public void request(Object tag,String url){
        HttpParams params = new HttpParams();
        params.put("lang", Constants.currentLanguage);
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + url, tag, params, new JsonCallback<BaseModel<List<HotelGeneralInfo>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<HotelGeneralInfo>>> response) {
                List<HotelGeneralInfo> data = response.body().data;
                if(data != null && data.size() > 0){
                    infoIPresenterBase.onSuccess(response.body());
                }else{
                    infoIPresenterBase.onError();
                }
            }

            @Override
            public void onError(Response<BaseModel<List<HotelGeneralInfo>>> response) {
                super.onError(response);
                infoIPresenterBase.onError();
            }
        });
    }

    public void request(Object tag,String url,int id){
        HttpParams params = new HttpParams();
        params.put("lang", Constants.currentLanguage);
        params.put("id", id);
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + url, tag, params, new JsonCallback<BaseModel<List<HotelGeneralInfo>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<HotelGeneralInfo>>> response) {
                List<HotelGeneralInfo> data = response.body().data;
                if(data != null && data.size() > 0){
                    infoIPresenterBase.onSuccess(response.body());
                }else{
                    infoIPresenterBase.onError();
                }
            }

            @Override
            public void onError(Response<BaseModel<List<HotelGeneralInfo>>> response) {
                super.onError(response);
                infoIPresenterBase.onError();
            }
        });
    }
}
