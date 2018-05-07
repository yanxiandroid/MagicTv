package com.yht.iptv.presenter;

import android.content.Context;

import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.HotelFoodBean;
import com.yht.iptv.model.MessagePushInfo;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;


/**
 * 餐厅列表
 */
public class PushMessagePresenter {

    private IPresenterBase<List<MessagePushInfo>> iPresenterBase;
    private Context mContext;

    public PushMessagePresenter(Context mContext, IPresenterBase<List<MessagePushInfo>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }


    public void request(Object tag,String roomId){

        HttpParams params = new HttpParams();
        params.put("roomNum",roomId);
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.PUSH_MESSAGE,tag, params, new JsonCallback<BaseModel<List<MessagePushInfo>>>() {

            @Override
            public void onSuccess(Response<BaseModel<List<MessagePushInfo>>> response) {
                BaseModel<List<MessagePushInfo>> body = response.body();
                if(body.data != null && body.data.size() !=0){
                    iPresenterBase.onSuccess(body);
                }else{
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onError(Response<BaseModel<List<MessagePushInfo>>> response) {
                iPresenterBase.onError();
            }
        });

    }
}
