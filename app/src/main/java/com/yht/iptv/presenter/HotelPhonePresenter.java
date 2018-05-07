package com.yht.iptv.presenter;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.HotelPhoneInfo;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by admin on 2017/12/13.
 */

public class HotelPhonePresenter {

    private IPresenterBase<List<HotelPhoneInfo>> iPresenterBase;

    public HotelPhonePresenter(IPresenterBase<List<HotelPhoneInfo>> iPresenterBase) {
        this.iPresenterBase = iPresenterBase;
    }

    public void request(Object tag){
        HttpParams params = new HttpParams();
        params.put("currentPage",1+"");
        params.put("pageSize",100+"");
        params.put("lang", Constants.currentLanguage);
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.HOTEL_PHONE, tag ,params, new JsonCallback<BaseModel<List<HotelPhoneInfo>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<HotelPhoneInfo>>> response) {
                List<HotelPhoneInfo> data = response.body().data;
                if(data != null && data.size() > 0) {
                    iPresenterBase.onSuccess(response.body());
                }else{
                    iPresenterBase.onError();
                }
            }
            @Override
            public void onError(Response<BaseModel<List<HotelPhoneInfo>>> response) {
                iPresenterBase.onError();
            }
        });
    }

}
