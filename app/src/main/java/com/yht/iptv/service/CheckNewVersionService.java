package com.yht.iptv.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.AppMangerInfo;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.DownloadInfo;
import com.yht.iptv.presenter.AppDownloadPresenter;
import com.yht.iptv.presenter.AppUpdatePresenter;
import com.yht.iptv.utils.AppUtils;
import com.yht.iptv.utils.FileUtils;
import com.yht.iptv.utils.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by admin on 2017/10/16.
 */

public class CheckNewVersionService extends Service implements IPresenterBase<List<AppMangerInfo>> {

    private AppDownloadPresenter presenter;
    private boolean isPhone;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isPhone = false;
        presenter = new AppDownloadPresenter(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null || intent.getExtras() == null) {
            isPhone = false;
            //检测新版本
            presenter.request();
        }else{
            Bundle bundle = intent.getExtras();
            boolean phone_urls = bundle.getBoolean("phone_url");
            if(phone_urls){
                isPhone = true;
                presenter.requestPhone(this);
            }else{
                isPhone = false;
                presenter.request();
            }
        }

        return START_STICKY;
    }

    @Override
    public void onSuccess(BaseModel<List<AppMangerInfo>> dataList) {
        if(isPhone){
            if (dataList.data != null && dataList.data.size() > 0 && dataList.data.get(0) != null) {
                AppMangerInfo appMangerInfo = dataList.data.get(0);
                EventBus.getDefault().post(appMangerInfo);
            }else{
                AppMangerInfo info = new AppMangerInfo();
                EventBus.getDefault().post(info);
            }
            stopSelf();
        }else {
            if (dataList.data != null && dataList.data.size() > 0) {
                if (dataList.data.get(0) != null) {
                    if (dataList.data.get(0).getFileUpload() != null && dataList.data.get(0).getFileUpload().getPath() != null) {
                        //获取地址
                        DownloadInfo info = new DownloadInfo();
                        String path = dataList.data.get(0).getFileUpload().getPath();
                        String fileName = dataList.data.get(0).getFileUpload().getFileName();
                        info.setDownloadPath(path);
                        info.setFileName(fileName);
                        info.setFilePath(FileUtils.getDownLoadPath());
                        AppUpdatePresenter updatePresenter = new AppUpdatePresenter(this);
                        updatePresenter.request(info.getDownloadPath(), info.getFileName(), info.getFilePath());
                        stopSelf();
                    }
                }
            }
        }
    }

    @Override
    public void onError() {
        if(isPhone){
            AppMangerInfo info = new AppMangerInfo();
            EventBus.getDefault().post(info);
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isPhone){
            OkHttpUtils.cancel(this);
        }
    }
}
