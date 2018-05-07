package com.yht.iptv.presenter;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.FoodOrderInfo;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by admin on 2017/11/14.
 */

public class FoodOrderPresenter {

    private IPresenterBase<List<FoodOrderInfo>> iPresenterBase;

    public FoodOrderPresenter(IPresenterBase<List<FoodOrderInfo>> iPresenterBase) {
        this.iPresenterBase = iPresenterBase;
    }

    public void request(String roomId, Object tag){
        HttpParams params = new HttpParams();
        params.put("roomNum",roomId);
        params.put("pageSize",100);

        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.FOOD_ORDER, tag ,params, new JsonCallback<BaseModel<List<FoodOrderInfo>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<FoodOrderInfo>>> response) {
                List<FoodOrderInfo> data = response.body().data;
                if(data != null && data.size() > 0) {
                    iPresenterBase.onSuccess(response.body());
                }else{
                    iPresenterBase.onError();
                }
            }
            @Override
            public void onError(Response<BaseModel<List<FoodOrderInfo>>> response) {
                iPresenterBase.onError();
            }
        });
    }

}
