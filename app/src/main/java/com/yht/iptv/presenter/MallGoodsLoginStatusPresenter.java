package com.yht.iptv.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterMallBase;
import com.yht.iptv.callback.JsonMallCallback;
import com.yht.iptv.model.BaseMallModel;
import com.yht.iptv.model.MallLoginInfo;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;


/**
 * Created by admin on 2017/6/19.
 */

public class MallGoodsLoginStatusPresenter {
    private IPresenterMallBase<MallLoginInfo> iPresenterBase;
    private Context mContext;

    public MallGoodsLoginStatusPresenter(Context mContext, IPresenterMallBase<MallLoginInfo> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }

    public void request(Object tag, String hotelID,String roomNum,String userID) {

        HttpParams params = new HttpParams();
        params.put("r","member.login");
        params.put("i","3");
        params.put("hotelID",hotelID);
        params.put("roomNum",roomNum);
        params.put("userID",userID);
        OkHttpUtils.getJson(HttpConstants.MALL_GOODS_LOGIN_STATUS,tag,params,new JsonMallCallback<BaseMallModel<MallLoginInfo>>() {

            @Override
            public void onSuccess(Response<BaseMallModel<MallLoginInfo>> response) {
                iPresenterBase.onSuccess(response.body());
            }

            @Override
            public void onError(Response<BaseMallModel<MallLoginInfo>> response) {
                iPresenterBase.onError();
            }
        });

    }
}
