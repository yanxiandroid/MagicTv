package com.yht.iptv.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterMallBase;
import com.yht.iptv.callback.JsonMallCallback;
import com.yht.iptv.model.BaseMallModel;
import com.yht.iptv.model.MallStatusInfo;
import com.yht.iptv.utils.DialogUtils;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;


/**
 * Created by admin on 2017/6/19.
 */

public class MallOrderApplyPresenter {
    private IPresenterMallBase<MallStatusInfo> iPresenterBase;
    private Context mContext;
    private ProgressDialog progressDialog;

    public MallOrderApplyPresenter(ProgressDialog progressDialog, Context mContext, IPresenterMallBase<MallStatusInfo> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
        this.progressDialog = progressDialog;
    }

    public void request(String json,String openid){

        HttpParams params = new HttpParams();
        params.put("i","3");
        params.put("openid",openid);
        params.put("product",json);
        OkHttpUtils.getJson(HttpConstants.HTTP_MALL_HOST + HttpConstants.MALL_GOODS_ORDER_DETAIL,params,new JsonMallCallback<BaseMallModel<MallStatusInfo>>(){
            @Override
            public void onSuccess(Response<BaseMallModel<MallStatusInfo>> response) {
                iPresenterBase.onSuccess(response.body());
            }

            @Override
            public void onError(Response<BaseMallModel<MallStatusInfo>> response) {
                iPresenterBase.onError();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                DialogUtils.dismissDialog(progressDialog);
            }
        });

    }
}
