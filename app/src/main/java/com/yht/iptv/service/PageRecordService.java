package com.yht.iptv.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.presenter.MovieRecordPresenter;
import com.yht.iptv.presenter.PageRecordPresenter;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.OkHttpUtils;
import com.yht.iptv.utils.SPUtils;

import java.util.List;

/**
 * Created by admin on 2017/11/13.
 */

public class PageRecordService extends Service implements IPresenterBase<List<Long>> {

    private MovieRecordPresenter movieRecordPresenter;
    private PageRecordPresenter pageRecordPresenter;
    private long recordId = -1;
    private String roomId;
    private long pageRecordId = -1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        roomId = (String) SPUtils.get(this, Constants.ROOM_ID, "");
        movieRecordPresenter = new MovieRecordPresenter(this,this);
        pageRecordPresenter = new PageRecordPresenter(new PageRecordListener());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null && intent.getExtras() != null){

            Bundle bundle = intent.getExtras();
            byte pageStatus = bundle.getByte(Constants.PAGE_STATUS);
            switch (pageStatus){
                case Constants.MOVIE_START:
                    recordId = -1;
                    String videoId = bundle.getString("videoId");
                    int playType = bundle.getInt("playType");
                    movieRecordPresenter.sendStart(videoId,playType,roomId);
                    break;
                case Constants.MOVIE_PAULE:
                    if(recordId != -1) {
                        movieRecordPresenter.sendPause(recordId);
                    }
                    break;
                case Constants.MOVIE_RESUME:
                    if(recordId != -1) {
                        movieRecordPresenter.sendResume(recordId);
                    }
                    break;
                case Constants.MOVIE_STOP:
                    if(recordId != -1) {
                        int status = bundle.getInt("status");
                        movieRecordPresenter.sendStop(recordId, status,pageRecordId);
                    }
                    break;
                case Constants.PAGE_START:
                    pageRecordId = -1;
                    String behaviour = bundle.getString("behaviour");
                    pageRecordPresenter.pageStart(roomId,behaviour);
                    break;
                case Constants.PAGE_END:
                    if(pageRecordId != -1) {
                        pageRecordPresenter.pageEnd(pageRecordId);
                    }
                    break;
            }
        }


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieRecordPresenter = null;
        OkHttpUtils.cancel();
    }

    @Override
    public void onSuccess(BaseModel<List<Long>> dataList) {
        recordId = dataList.data.get(0);
    }

    @Override
    public void onError() {

    }

    private class PageRecordListener implements IPresenterBase<List<Long>> {

        @Override
        public void onSuccess(BaseModel<List<Long>> dataList) {
            pageRecordId = dataList.data.get(0);
        }

        @Override
        public void onError() {

        }
    }

}
