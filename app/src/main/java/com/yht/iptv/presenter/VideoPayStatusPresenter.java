package com.yht.iptv.presenter;

import android.content.Context;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.VideoPayBean;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 * 查询订单状态
 * Created by admin on 2017/6/19.
 */

public class VideoPayStatusPresenter {
    private IPresenterBase<List<VideoPayBean>> iPresenterBase;
    private Context mContext;

    public VideoPayStatusPresenter(Context mContext, IPresenterBase<List<VideoPayBean>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }

    public void request(Object tag, String roomNum, String userId, int videoId) {
        HttpParams params = new HttpParams();
        params.put("videoId", videoId);
        params.put("roomNum", roomNum);
        params.put("userId", userId);
        OkHttpUtils.postJson(HttpConstants.HTTP_HOST + HttpConstants.GET_PAY_STATUS, tag, params, new JsonCallback<BaseModel<List<VideoPayBean>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<VideoPayBean>>> response) {
                BaseModel<List<VideoPayBean>> body = response.body();
                if (body.code == 1) {
                    if (body.data.size() != 0 && body.data.get(0) != null) {
                        iPresenterBase.onSuccess(body);
                    } else {
                        iPresenterBase.onError();
                    }
                } else {
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<BaseModel<List<VideoPayBean>>> response) {
                iPresenterBase.onError();
            }
        });
    }
}
