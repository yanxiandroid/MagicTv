package com.yht.iptv.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterMallBase;
import com.yht.iptv.callback.JsonMallCallback;
import com.yht.iptv.model.BaseMallModel;
import com.yht.iptv.model.MallGoodsDetailInfo;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;


/**
 * Created by admin on 2017/6/19.
 */

public class MallGoodsDetailPresenter {
    private IPresenterMallBase<MallGoodsDetailInfo> iPresenterBase;
    private Context mContext;

    public MallGoodsDetailPresenter(Context mContext, IPresenterMallBase<MallGoodsDetailInfo> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }

    public void request(Object tag, String id) {

        HttpParams params = new HttpParams();
        params.put("id", id);

        OkHttpUtils.getJson(HttpConstants.HTTP_MALL_HOST + HttpConstants.MALL_GOODS_DETAIL,tag,params,new JsonMallCallback<BaseMallModel<MallGoodsDetailInfo>>() {
            @Override
            public void onSuccess(Response<BaseMallModel<MallGoodsDetailInfo>> response) {
                iPresenterBase.onSuccess(response.body());
            }

            @Override
            public void onError(Response<BaseMallModel<MallGoodsDetailInfo>> response) {
                super.onError(response);
            }
        });

    }
}
