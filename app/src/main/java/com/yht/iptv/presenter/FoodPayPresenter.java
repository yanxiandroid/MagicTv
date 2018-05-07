package com.yht.iptv.presenter;

import android.content.Context;

import com.lzy.okgo.model.HttpParams;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.VideoPayBean;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 * 生成点餐订单并返回二维码或签名订单
 */

public class FoodPayPresenter {
    private IPresenterBase<List<VideoPayBean>> iPresenterBase;

    public FoodPayPresenter(Context mContext, IPresenterBase<List<VideoPayBean>> iPresenterBase) {
        this.iPresenterBase = iPresenterBase;
    }

    public void request(String json, String price, String roomNum, String userId) {
        HttpParams params = new HttpParams();
        params.put("dines", json);
        params.put("price", price);
        params.put("roomNum", roomNum);
        params.put("userId", userId);
        OkHttpUtils.postJson(HttpConstants.HTTP_HOST + HttpConstants.GET_FOOD_PAY_QRCODE, "GET_FOOD_PAY_INFO", params, new JsonCallback<BaseModel<List<VideoPayBean>>>() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<BaseModel<List<VideoPayBean>>> response) {
                BaseModel<List<VideoPayBean>> body = response.body();
                if (body.code == 1) {

                    if (body.data.size() != 0 && body.data.get(0) != null) {
                        iPresenterBase.onSuccess(body);
                    } else {
                        iPresenterBase.onError();
                    }
                } else {
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<BaseModel<List<VideoPayBean>>> response) {
                super.onError(response);
                iPresenterBase.onError();
            }
        });
    }
}
