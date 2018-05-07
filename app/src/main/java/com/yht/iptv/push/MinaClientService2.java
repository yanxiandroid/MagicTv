//package com.yht.iptv.push;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.IBinder;
//import android.os.Looper;
//import android.os.Message;
//import android.support.annotation.Nullable;
//
//import com.apkfuns.logutils.LogUtils;
//import com.yht.iptv.utils.AppUtils;
//import com.yht.iptv.utils.Constants;
//import com.yht.iptv.utils.NetworkUtils;
//import com.yht.iptv.utils.SPUtils;
//import com.yht.iptv.utils.ServiceUtils;
//import com.yht.iptv.utils.ToastUtils;
//
//import java.lang.ref.WeakReference;
//
///**
// * Created by admin on 2017/6/29.
// */
//
//public class MinaClientService2 extends Service {
//
//    private MyHandler myHandler;
//    private final int SUCCESS = 1;
//    private final int FAILED = 2;
//    private final int CHECK_ISCONNECT = 3;
//    private ConnectManager manager;
//    private HandlerThread handlerThread;
//    private final int CHECK_TIME = 30 * 1000;
////    private String ip = "10.0.10.105";
//
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        handlerThread = new HandlerThread("MINA");
//        handlerThread.start();
//        myHandler = new MyHandler(this, handlerThread.getLooper());
//        myHandler.removeMessages(CHECK_ISCONNECT);
//        myHandler.sendEmptyMessageDelayed(CHECK_ISCONNECT,CHECK_TIME);
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        connect(Constants.IP_VALUE);
////        if(intent != null) {
////            String ip = intent.getStringExtra("ip");
////            if(ip == null){
////                if(this.ip == null){
////                    Intent intent1 = new Intent();
////                    intent1.setAction("com.mina.client.notification");
////                    intent1.putExtra("notification","ip为空");
////                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
////                }else{
////                    connect(this.ip);
////                }
////            }else {
////                this.ip = ip;
////                connect(ip);
////            }
////        }
//        return START_STICKY;
//    }
//
//
//
//    private void init(String ip) {
//        manager = ConnectManager.getInstance(new ConnectBean(ip,9124,this));
//    }
//
//
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            if(manager.connect()) {
//                myHandler.sendEmptyMessage(SUCCESS);
//                LogUtils.tag("MINA").e("SUCCESS");
//            }else{
//                myHandler.sendEmptyMessage(FAILED);
//                LogUtils.tag("MINA").e("FAILED");
//            }
//        }
//    };
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        handlerThread.quit();
//        if(manager != null) {
//            manager.desConnection();
//            manager = null;
//        }
//        ServiceUtils.startService(MinaClientService2.class);
//    }
//
//    private static class MyHandler extends Handler {
//
//        private WeakReference<MinaClientService2> minaService;
//        public MyHandler(MinaClientService2 minaService, Looper looper) {
//            super(looper);
//            this.minaService = new WeakReference<>(minaService);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            final MinaClientService2 service = this.minaService.get();
//            if(msg.what == service.SUCCESS){
//                //主线程处理结果
//                //成功
//                service.successConnect();
//            }
//            if(msg.what == service.FAILED){
//                service.manager.desConnection();
//                service.manager = null;
//                //30s重连
//                removeMessages(service.CHECK_ISCONNECT);
//                sendEmptyMessageDelayed(service.CHECK_ISCONNECT,service.CHECK_TIME);
//            }
//            if(msg.what == service.CHECK_ISCONNECT){
//                ServiceUtils.startService(MinaClientService2.class);
//                removeMessages(service.CHECK_ISCONNECT);
//                sendEmptyMessageDelayed(service.CHECK_ISCONNECT,service.CHECK_TIME);
//            }
//        }
//    }
//
//    private void connect(String ip){
//        init(ip);
//        myHandler.removeCallbacks(runnable);
//        myHandler.post(runnable);
//    }
//
//
//    private void successConnect(){
//        SessionManager.getInstance().setSession(manager.getSession());
//        String room_id = (String) SPUtils.get(this, Constants.ROOM_ID, "");
//        SessionManager.getInstance().WriteToSession("%mina_sessionCreated%:" + room_id + "_" + AppUtils.getWireMacAddr());
//    }
//
//}
