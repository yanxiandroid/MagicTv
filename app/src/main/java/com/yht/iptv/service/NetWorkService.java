package com.yht.iptv.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.NetworkUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by admin on 2017/11/23.
 */

public class NetWorkService extends IntentService {

    public NetWorkService() {
        super("NetWorkService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LogUtils.e("onHandleIntent");
        if(NetworkUtils.isAvailableByPing(Constants.IP_VALUE)){
            EventBus.getDefault().post(Constants.NETWORK_OK);
        }else{
            EventBus.getDefault().post(Constants.NETWORK_ERROR);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        new Thread(runnable).start();
//        return START_STICKY;
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e("onDestroy");
    }



//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            if(NetworkUtils.isAvailableByPing(Constants.IP_VALUE)){
//                EventBus.getDefault().post(Constants.NETWORK_OK);
//            }else{
//                EventBus.getDefault().post(Constants.NETWORK_ERROR);
//            }
//            stopSelf();
//        }
//    };
}
