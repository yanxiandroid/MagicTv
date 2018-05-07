package com.yht.iptv.push;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;
import com.yht.iptv.utils.AppUtils;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.SPUtils;
import com.yht.iptv.utils.ServiceUtils;

import java.lang.ref.WeakReference;

/**
 * Created by admin on 2017/6/29.
 */

public class MinaClientService extends Service {

    private MyHandler myHandler;
    private final int SUCCESS = 1;
    private final int FAILED = 2;
    private final int CHECK_ISCONNECT = 3;
    private final int CONNECTING = 4;
    private ConnectManager manager;
    private HandlerThread handlerThread;
    private final int CHECK_TIME = 30 * 1000;
    private Handler childHandler;
    //    private String ip = "10.0.10.105";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        handlerThread = new HandlerThread("MINA");
        handlerThread.start();
        myHandler = new MyHandler(this);
        childHandler = new Handler(handlerThread.getLooper(),new ChildCallback());
        myHandler.removeMessages(CHECK_ISCONNECT);
        myHandler.sendEmptyMessageDelayed(CHECK_ISCONNECT,CHECK_TIME);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        connect(Constants.IP_VALUE);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
        if(manager != null) {
            manager.desConnection();
            manager = null;
        }
        ServiceUtils.startService(MinaClientService.class);
    }

    private static class MyHandler extends Handler {

        private WeakReference<MinaClientService> minaService;
        public MyHandler(MinaClientService minaService) {
            this.minaService = new WeakReference<>(minaService);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final MinaClientService service = this.minaService.get();
            if(msg.what == service.SUCCESS){
                //主线程处理结果
                //成功
                service.successConnect();
                //30s重连
                removeMessages(service.CHECK_ISCONNECT);
                sendEmptyMessageDelayed(service.CHECK_ISCONNECT,service.CHECK_TIME);
            }
            if(msg.what == service.FAILED){
                if(service.manager != null) {
                    service.manager.desConnection();
                    service.manager = null;
                }
                //30s重连
                removeMessages(service.CHECK_ISCONNECT);
                sendEmptyMessageDelayed(service.CHECK_ISCONNECT,service.CHECK_TIME);
            }
            if(msg.what == service.CHECK_ISCONNECT){
                ServiceUtils.startService(MinaClientService.class);
                removeMessages(service.CHECK_ISCONNECT);
                sendEmptyMessageDelayed(service.CHECK_ISCONNECT,service.CHECK_TIME);
            }
        }
    }

    private void connect(String ip){
        manager = ConnectManager.getInstance(new ConnectBean(ip,9124,this));
        childHandler.removeMessages(CONNECTING);
        childHandler.sendEmptyMessage(CONNECTING);
       /* myHandler.removeCallbacks(runnable);
        myHandler.post(runnable);*/
    }


    private void successConnect(){
        if(manager != null) {
            SessionManager.getInstance().setSession(manager.getSession());
            String room_id = (String) SPUtils.get(this, Constants.ROOM_ID, "");
            SessionManager.getInstance().WriteToSession("%mina_sessionCreated%:" + room_id + "_" + AppUtils.getWireMacAddr());
        }
    }

    /**
     * 该callback运行于子线程
     */
    private class ChildCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == CONNECTING) {
                if(manager != null) {
                    if (manager.connect()) {
                        myHandler.removeMessages(CHECK_ISCONNECT);
                        myHandler.sendEmptyMessage(SUCCESS);
                        LogUtils.tag("MINA").e("SUCCESS");
                    } else {
                        myHandler.removeMessages(CHECK_ISCONNECT);
                        myHandler.sendEmptyMessage(FAILED);
                        LogUtils.tag("MINA").e("FAILED");
                    }
                }
            }
            return false;
        }
    }

}
