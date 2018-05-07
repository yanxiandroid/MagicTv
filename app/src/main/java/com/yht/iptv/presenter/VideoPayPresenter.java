package com.yht.iptv.presenter;

import android.content.Context;

import com.lzy.okgo.model.HttpParams;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.VideoPayBean;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 * 生成视频订单并返回二维码或签名订单
 */

public class VideoPayPresenter {
    private IPresenterBase<List<VideoPayBean>> iPresenterBase;
    private Context mContext;

    public VideoPayPresenter(Context mContext, IPresenterBase<List<VideoPayBean>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }

    public void request(String roomNum, String userId, int isCombo, int videoId) {
        HttpParams params = new HttpParams();
        params.put("videoId", videoId);
        params.put("roomNum", roomNum);
        params.put("userId", userId);
        params.put("isCombo", isCombo);
        OkHttpUtils.postJson(HttpConstants.HTTP_HOST + HttpConstants.GET_PAY_QRCODE, "GET_PAY_INFO", params, new JsonCallback<BaseModel<List<VideoPayBean>>>() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<BaseModel<List<VideoPayBean>>> response) {
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
                super.onError(response);
                iPresenterBase.onError();
            }
        });
    }
}
