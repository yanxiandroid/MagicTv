package com.yht.iptv.presenter;

import android.content.Context;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.PictureAdInfo;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 * 图片广告网络请求
 * Created by Q on 2018/1/6.
 */

public class PictureAdPresenter {
    private Context mContext;
    private IPresenterBase<List<PictureAdInfo>> iPresenterBase;

    public PictureAdPresenter(Context mContext, IPresenterBase<List<PictureAdInfo>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;

    }

    /**
     *
     * @param tag
     * @param type 尺寸类型，0：不限，1：横版，2：竖版，3：方形
     * @param roomNum 房间号
     */
    public void request(Object tag, int type,String roomNum) {
        HttpParams params = new HttpParams();
        params.put("type", type);
        params.put("roomNum",roomNum);
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.IMAGE_DISPLAY, tag, params, new JsonCallback<BaseModel<List<PictureAdInfo>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<PictureAdInfo>>> response) {
                BaseModel<List<PictureAdInfo>> body = response.body();
                try {
                    if (body.code == 1) {
                        if (body.data.size() != 0 && body.data.get(0) != null) {
                            iPresenterBase.onSuccess(body);
                        } else {
                            iPresenterBase.onError();
                        }
                    } else {
                        iPresenterBase.onError();
                    }
                }catch (Exception e){
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onError(Response<BaseModel<List<PictureAdInfo>>> response) {
                super.onError(response);
                iPresenterBase.onError();
            }
        });


    }
}
