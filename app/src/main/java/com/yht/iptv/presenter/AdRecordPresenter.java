package com.yht.iptv.presenter;

import android.content.Context;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 * 广告统计
 * Created by Q on 2018/1/11.
 */

public class AdRecordPresenter {
    private Context mContext;
    private IPresenterBase<List<Long>> iPresenterBase;

    public AdRecordPresenter(Context mContext, IPresenterBase<List<Long>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }
    public AdRecordPresenter() {
    }


    /**
     *
     * @param tag
     * @param id 广告ID
     * @param type 广告类型，1：视频广告，2：图片广告
     * @param roomNum 房号
     * @param imageType 尺寸类型，0：不限，1：横版，2：竖版，3：方形   type为2必填
     * @param length 播放时长，秒                                   type为1必填
     */
    public void request(Object tag, long id, int type,String roomNum,int imageType, int length) {
        HttpParams params = new HttpParams();
        params.put("adId", id);
        params.put("type", type);
        params.put("roomNum", roomNum);
        params.put("imageType", imageType);
        params.put("length", length);
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.AD_STATISTICS, tag, params, new JsonCallback<BaseModel<List<Long>>>() {

            @Override
            public void onSuccess(Response<BaseModel<List<Long>>> response) {
                try {
                    if(iPresenterBase != null) {
                        BaseModel<List<Long>> body = response.body();
                        if (body.code == 1) {
                            iPresenterBase.onSuccess(body);
                        } else {
                            iPresenterBase.onError();
                        }
                    }
                }catch (Exception e){
                }
            }
        });

    }

}
