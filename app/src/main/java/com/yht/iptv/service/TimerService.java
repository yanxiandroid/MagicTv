package com.yht.iptv.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.yht.iptv.utils.Constants;

/**
 * Created by admin on 2018/2/5.
 */

public class TimerService extends Service {


    private static final int TIME_DELAY = 1;

    private MyHandler handler;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        handler = new MyHandler();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        handler.removeMessages(TIME_DELAY);
        handler.sendEmptyMessageDelayed(TIME_DELAY,1000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(TIME_DELAY);
        handler.removeCallbacksAndMessages(null);
    }

    private static class MyHandler extends Handler{


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TIME_DELAY:
                    Constants.REAL_TIME += 1000;
                    removeMessages(TIME_DELAY);
                    sendEmptyMessageDelayed(TIME_DELAY,1000);
                    break;
            }
        }
    }


}
