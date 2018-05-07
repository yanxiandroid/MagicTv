package com.yht.iptv.presenter;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.BusTimetableBean;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 * 长途大巴（带参数）
 * Created by Q on 2018/1/16.
 */

public class BusTimetablePresenter {

    private IPresenterBase<List<BusTimetableBean>> iPresenterBase;

    public BusTimetablePresenter(IPresenterBase<List<BusTimetableBean>> iPresenterBase) {
        this.iPresenterBase = iPresenterBase;

    }


    public void request(Object tag, String startStation, String endStation) {
        HttpParams params = new HttpParams();
        params.put("startStation", startStation);
        params.put("endStation", endStation);
        params.put("lang", Constants.currentLanguage);
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.NEAR_BUS, tag, params, new JsonCallback<BaseModel<List<BusTimetableBean>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<BusTimetableBean>>> response) {
                BaseModel<List<BusTimetableBean>> body = response.body();
                if (body.data != null && body.data.size() != 0) {
                    iPresenterBase.onSuccess(body);
                } else {
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onError(Response<BaseModel<List<BusTimetableBean>>> response) {
                iPresenterBase.onError();
            }
        });


    }
}
