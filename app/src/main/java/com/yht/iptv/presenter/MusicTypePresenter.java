package com.yht.iptv.presenter;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.MusicTypeBean;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;


/**
 * Created by admin on 2017/2/23.
 */
public class MusicTypePresenter {

    private IPresenterBase<List<MusicTypeBean>> iPresenterBase;

    public MusicTypePresenter(IPresenterBase<List<MusicTypeBean>> iPresenterBase) {
        this.iPresenterBase = iPresenterBase;
    }

    public void request(){

        HttpParams params = new HttpParams();
        params.put("lang", Constants.currentLanguage);
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.MUSIC_LIST, params, new JsonCallback<BaseModel<List<MusicTypeBean>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<MusicTypeBean>>> response) {
                iPresenterBase.onSuccess(response.body());
            }

            @Override
            public void onError(Response<BaseModel<List<MusicTypeBean>>> response) {
                iPresenterBase.onError();
            }
        });

    }

}
