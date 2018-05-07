package com.yht.iptv.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterMallBase;
import com.yht.iptv.callback.JsonMallCallback;
import com.yht.iptv.model.BaseMallModel;
import com.yht.iptv.model.MallGoodsListInfo;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

/**
 * Created by admin on 2017/6/19.
 */

public class MallListPresenter {
    private IPresenterMallBase<MallGoodsListInfo> iPresenterBase;
    private Context mContext;

    public MallListPresenter(Context mContext, IPresenterMallBase<MallGoodsListInfo> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }

    public void request(Object tag,String cate,String hotelId,String currentPage,String pagesize,String keywords){

        HttpParams params = new HttpParams();
        params.put("cate",cate);
        params.put("hotelID",hotelId);
        params.put("page",currentPage);
        params.put("pagesize",pagesize);
        params.put("keywords",keywords);

        OkHttpUtils.getJson(HttpConstants.HTTP_MALL_HOST + HttpConstants.MALL_GOODS_LIST,tag,params,new JsonMallCallback<BaseMallModel<MallGoodsListInfo>>(){
            @Override
            public void onSuccess(Response<BaseMallModel<MallGoodsListInfo>> response) {

                BaseMallModel<MallGoodsListInfo> body = response.body();

                if(body.result!=null && body.result.getGoods_list().size() != 0 && body.result.getGoods_list().get(0) != null) {
                    iPresenterBase.onSuccess(body);
                }else{
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onError(Response<BaseMallModel<MallGoodsListInfo>> response) {
                iPresenterBase.onError();
            }
        });

    }
}
