package com.yht.iptv.presenter;

import android.content.Context;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.MediaDetailBean;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.LanguageUtils;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 *
 * 视频详情
 */
public class MediaDetailPresenter {

    private Context mContext;
    private IPresenterBase<List<MediaDetailBean>> iPresenterBase;

    public MediaDetailPresenter(Context mContext, IPresenterBase<List<MediaDetailBean>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }

    //hotPlay是否热播：1为是，0为否
    public void request(Object tag,String categoryId, String hotPlay, String keyword, String currentPage, String pageSize) {

        HttpParams params = new HttpParams();
        params.put("categoryId", categoryId);
        params.put("hotPlay", hotPlay);
        params.put("keyword", keyword);
        params.put("currentPage", currentPage);
        params.put("pageSize", pageSize);
        params.put("lang", Constants.currentLanguage);
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.MOVIE_DETAIL,tag, params, new JsonCallback<BaseModel<List<MediaDetailBean>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<MediaDetailBean>>> response) {
                BaseModel<List<MediaDetailBean>> body = response.body();
                if (body.code == 1) {
                    iPresenterBase.onSuccess(body);
                } else {
                    iPresenterBase.onError();
                }
            }

            @Override
            public void onError(Response<BaseModel<List<MediaDetailBean>>> response) {
                iPresenterBase.onError();
            }
        });

    }


}
