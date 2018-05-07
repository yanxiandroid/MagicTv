package com.yht.iptv.presenter;

import android.content.Context;

import com.lzy.okgo.model.HttpParams;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.HotelFoodListBean;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 *菜品列表
 */
public class HotelFoodListPresenter {

    private IPresenterBase<List<HotelFoodListBean>> iPresenterBase;
    private Context mContext;

    public HotelFoodListPresenter(Context mContext, IPresenterBase<List<HotelFoodListBean>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }

    public void request(Object tag,String restaurantId,String typeEn) {
        HttpParams params = new HttpParams();
        params.put("restaurantId", restaurantId);
        params.put("typeEn", typeEn);
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.HOTEL_FOOD_LIST, tag, params, new JsonCallback<BaseModel<List<HotelFoodListBean>>>() {

            @Override
            public void onSuccess(com.lzy.okgo.model.Response<BaseModel<List<HotelFoodListBean>>> response) {
                BaseModel<List<HotelFoodListBean>> body = response.body();
                if (body.code == 1) {
                    iPresenterBase.onSuccess(body);

                }else {
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<BaseModel<List<HotelFoodListBean>>> response) {
                iPresenterBase.onError();
            }
        });
    }


}
