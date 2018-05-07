package com.yht.iptv.presenter;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

/**
 * Created by admin on 2017/12/4.
 */

public class AccountPayPresenter {

    private IPresenterBase iPresenterBase;

    public AccountPayPresenter(IPresenterBase iPresenterBase ) {

        this.iPresenterBase = iPresenterBase;
    }

    public void request(String orderNum){
        HttpParams params = new HttpParams();
        params.put("orderNum",orderNum);
        OkHttpUtils.postJson(HttpConstants.HTTP_HOST + HttpConstants.ACCOUNT_PAY, params, new JsonCallback<BaseModel>() {
            @Override
            public void onSuccess(Response<BaseModel> response) {
                BaseModel body = response.body();
                if(body.code == 1){
                    iPresenterBase.onSuccess(body);
                }else{
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onError(Response<BaseModel> response) {
                iPresenterBase.onError();
            }
        });

    }

    public void requestDines(String orderNum){
        HttpParams params = new HttpParams();
        params.put("orderNum",orderNum);
        OkHttpUtils.postJson(HttpConstants.HTTP_HOST + HttpConstants.ACCOUNT_PAY_DINES, params, new JsonCallback<BaseModel>() {
            @Override
            public void onSuccess(Response<BaseModel> response) {
                BaseModel body = response.body();
                if(body.code == 1){
                    iPresenterBase.onSuccess(body);
                }else{
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onError(Response<BaseModel> response) {
                iPresenterBase.onError();
            }
        });

    }

}
