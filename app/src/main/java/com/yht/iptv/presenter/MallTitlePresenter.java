package com.yht.iptv.presenter;

import android.content.Context;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterMallBase;
import com.yht.iptv.callback.JsonMallCallback;
import com.yht.iptv.model.BaseMallModel;
import com.yht.iptv.model.MallTitleInfo;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;


/**
 * Created by admin on 2017/6/19.
 */

public class MallTitlePresenter {
    private IPresenterMallBase<List<MallTitleInfo>> iPresenterBase;
    private Context mContext;

    public MallTitlePresenter(Context mContext, IPresenterMallBase<List<MallTitleInfo>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }

    public void request(){

        OkHttpUtils.getJson(HttpConstants.HTTP_MALL_HOST + HttpConstants.MALL_GOODS_TITLE,new HttpParams(),new JsonMallCallback<BaseMallModel<List<MallTitleInfo>>>(){
            @Override
            public void onSuccess(Response<BaseMallModel<List<MallTitleInfo>>> response) {
                BaseMallModel<List<MallTitleInfo>> body = response.body();
                if(body.result != null && body.result.size() != 0 && body.result.get(0) != null) {
                    iPresenterBase.onSuccess(body);
                }else{
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onError(Response<BaseMallModel<List<MallTitleInfo>>> response) {
                iPresenterBase.onError();
            }
        });


    }
}
