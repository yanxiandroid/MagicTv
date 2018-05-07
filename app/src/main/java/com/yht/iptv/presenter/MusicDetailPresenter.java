package com.yht.iptv.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.MusicDetailBean;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.LanguageUtils;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;


/**
 * Created by admin on 2017/2/23.
 */
public class MusicDetailPresenter {

    private IPresenterBase<List<MusicDetailBean>> iPresenterBase;
    private Context mContext;

    public MusicDetailPresenter(Context mContext, IPresenterBase<List<MusicDetailBean>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }

    public void request(String musicTypeId){

        HttpParams params = new HttpParams();
        params.put("musicTypeId",musicTypeId);
        params.put("lang", Constants.currentLanguage);
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.MUSIC_DETAIL, params, new JsonCallback<BaseModel<List<MusicDetailBean>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<MusicDetailBean>>> response) {
                iPresenterBase.onSuccess(response.body());
            }

            @Override
            public void onError(Response<BaseModel<List<MusicDetailBean>>> response) {
                iPresenterBase.onError();
            }
        });
    }

}
