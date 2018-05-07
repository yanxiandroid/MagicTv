package com.yht.iptv.presenter;

import android.content.Context;
import android.util.Log;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.OrderDetailBean;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 *
 */
public class OrderApplyPresenter {

    private IPresenterBase<List<OrderDetailBean>> iPresenterBase;
    private Context mContext;

    public OrderApplyPresenter(Context mContext, IPresenterBase<List<OrderDetailBean>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }

    public void request(Object tag, String json) {
        HttpParams params = new HttpParams();
        params.put("order", json);
        OkHttpUtils.postJson(HttpConstants.HTTP_HOST + HttpConstants.ORDER_APPLY, tag, params, new JsonCallback<BaseModel<List<OrderDetailBean>>>() {


            @Override
            public void onSuccess(Response<BaseModel<List<OrderDetailBean>>> response) {
                BaseModel<List<OrderDetailBean>> body = response.body();
                if (body.code == 1) {
                    iPresenterBase.onSuccess(body);
                    Log.e("OrderApplyPresenter",body.msg);
                } else {
                    iPresenterBase.onError();
                }

            }

            @Override
            public void onError(Response<BaseModel<List<OrderDetailBean>>> response) {
                super.onError(response);
                iPresenterBase.onError();
            }
        });
    }
}
