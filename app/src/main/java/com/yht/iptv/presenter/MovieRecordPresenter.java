package com.yht.iptv.presenter;

import android.content.Context;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.MediaRecordBean;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 * 视频播放记录
 */
public class MovieRecordPresenter {

    private Context mContext;
    private IPresenterBase<List<Long>> iPresenterBase;

    public MovieRecordPresenter(Context mContext, IPresenterBase<List<Long>> iPresenterBase) {
        this.mContext = mContext;
        this.iPresenterBase = iPresenterBase;
    }

    public MovieRecordPresenter() {
    }


    /**
     * 开始视频播放记录
     * @param videoId
     * @param playType
     * @param roomId
     */
    public void sendStart(String videoId,int playType,String roomId){
        HttpParams params = new HttpParams();
        params.put("videoId", videoId);
        params.put("playType", playType);
        params.put("type", "1");//1标题TV,这里默认为1
        params.put("roomNum", roomId);
        OkHttpUtils.postJson(HttpConstants.HTTP_HOST + HttpConstants.MOVIE_START_RECORD,params,new JsonCallback<BaseModel<List<Long>>>(){

            @Override
            public void onSuccess(Response<BaseModel<List<Long>>> response) {
                BaseModel<List<Long>> body = response.body();
                if(iPresenterBase != null) {
                    if (body.code == 1) {
                        if (body.data != null) {
                            iPresenterBase.onSuccess(body);
                        } else {
                            iPresenterBase.onError();
                        }
                    } else {
                        iPresenterBase.onError();
                    }
                }
            }

            @Override
            public void onError(Response<BaseModel<List<Long>>> response) {
                if(iPresenterBase != null) {
                    iPresenterBase.onError();
                }
            }
        });
    }


    /**
     * 结束视频播放记录
     * @param id
     * @param status
     * @param pageRecordId
     */
    public void sendStop(long id, int status, long pageRecordId){
        HttpParams params = new HttpParams();
        params.put("id",id);
        params.put("quitStatus",status);
        params.put("behaviourLogId",pageRecordId);
        OkHttpUtils.postJson(HttpConstants.HTTP_HOST + HttpConstants.MOVIE_STOP_RECORD,params,new JsonCallback<BaseModel<MediaRecordBean>>(){

            @Override
            public void onSuccess(Response<BaseModel<MediaRecordBean>> response) {

            }

        });
    }


    /**
     * 暂停视频播放记录
     * @param id
     */
    public void sendPause(long id){
        HttpParams params = new HttpParams();
        params.put("id",id);
        OkHttpUtils.postJson(HttpConstants.HTTP_HOST + HttpConstants.MOVIE_PAUSE_RECORD,params,new JsonCallback<BaseModel>(){

            @Override
            public void onSuccess(Response<BaseModel> response) {

            }

        });
    }

    /**
     * 继续视频播放记录
     * @param id
     */
    public void sendResume(long id){

        HttpParams params = new HttpParams();
        params.put("id",id);
        OkHttpUtils.postJson(HttpConstants.HTTP_HOST + HttpConstants.MOVIE_RESUME_RECORD,params,new JsonCallback<BaseModel>(){

            @Override
            public void onSuccess(Response<BaseModel> response) {

            }

        });
    }



}
