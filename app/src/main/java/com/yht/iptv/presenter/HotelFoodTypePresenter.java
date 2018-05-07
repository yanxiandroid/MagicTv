package com.yht.iptv.presenter;

import android.content.Context;

import com.lzy.okgo.model.HttpParams;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.HotelFoodTypeBean;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 *菜品列表
 */
public class HotelFoodTypePresenter {

    private IPresenterBase<List<HotelFoodTypeBean>> iPresenterBase;
    private Context mContext;

    public HotelFoodTypePresenter(Context mContext, IPresenterBase<List<HotelFoodTypeBean>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }

    public void request(Object tag,String restaurantId) {
        HttpParams params = new HttpParams();
        params.put("restaurantId", restaurantId);
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.HOTEL_FOOD_TYPE, tag, params, new JsonCallback<BaseModel<List<HotelFoodTypeBean>>>() {

            @Override
            public void onSuccess(com.lzy.okgo.model.Response<BaseModel<List<HotelFoodTypeBean>>> response) {
                BaseModel<List<HotelFoodTypeBean>> body = response.body();
                if (body.code == 1) {
                    iPresenterBase.onSuccess(body);

                }else {
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<BaseModel<List<HotelFoodTypeBean>>> response) {
                iPresenterBase.onError();
            }
        });
    }


}
