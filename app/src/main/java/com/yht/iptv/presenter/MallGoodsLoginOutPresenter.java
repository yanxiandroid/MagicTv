package com.yht.iptv.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.JsonMallCallback;
import com.yht.iptv.model.BaseMallModel;
import com.yht.iptv.model.MallLoginInfo;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

/**
 * Created by admin on 2017/6/19.
 */

public class MallGoodsLoginOutPresenter {
    private Context mContext;

    public MallGoodsLoginOutPresenter(Context mContext) {
        this.mContext = mContext;
    }

    public void request(Object tag, String openid) {

        HttpParams params = new HttpParams();
        params.put("r","member.loginout");
        params.put("i","3");
        params.put("openid",openid);

        OkHttpUtils.getJson(HttpConstants.MALL_GOODS_LOGIN_STATUS, tag, params, new JsonMallCallback<BaseMallModel<MallLoginInfo>>() {
            @Override
            public void onSuccess(Response<BaseMallModel<MallLoginInfo>> response) {

            }
        });
    }
}
