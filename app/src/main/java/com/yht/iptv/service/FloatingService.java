package com.yht.iptv.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.yht.iptv.R;
import com.yht.iptv.tools.HScrollTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/2.
 */

public class FloatingService extends Service implements HScrollTextView.onFinishListener {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmParams;
    private View view;
    private HScrollTextView marqueeText;
//    private MyRun run;
//    private boolean isColose;
    private List<String> strings;
    private int currentPosition;

    @Override
    public void onCreate() {
        super.onCreate();
//        isColose = false;
        currentPosition = 0;
        strings = new ArrayList<>();
        createFloatingView();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null && intent.getExtras() != null){
            Bundle bundle = intent.getExtras();
            String floating_message = bundle.getString("floating_message");
            strings.add(floating_message);
            if(marqueeText.isPaused()) {
                marqueeText.setText(strings.get(currentPosition));
                marqueeText.startScroll();
            }
        }
//        if(marqueeText.isPaused()) {
//            marqueeText.setText(strings.get(currentPostion));
//            marqueeText.startScroll();
//        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        handler.removeCallbacks(run);
//        run = null;
//        handler = null;
        mWindowManager.removeView(view);
    }


    private void createFloatingView(){

        //获取LayoutParams对象
        wmParams = new WindowManager.LayoutParams();

        //获取WindowManger
        mWindowManager = (WindowManager) getApplication().getSystemService(WINDOW_SERVICE);

        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置图片格式，效果为背景透明
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        wmParams.format = PixelFormat.RGBA_8888;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //调整悬浮窗显示的停靠位置置顶
        wmParams.gravity = Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        view = inflater.inflate(R.layout.floating_view, null);
        marqueeText = (HScrollTextView) view.findViewById(R.id.marqueeText);
        marqueeText.setonFinishListener(this);
        mWindowManager.addView(view,wmParams);

//        run = new MyRun();
//
//        handler.postDelayed(run,1000);

    }

//    private Handler handler = new Handler();

    @Override
    public void isFinish() {
        //结束
        currentPosition ++ ;
        if(currentPosition < strings.size()){
            marqueeText.setText(strings.get(currentPosition));
            marqueeText.startScroll();
        }else{
            stopSelf();
        }
    }

//    private class MyRun implements Runnable{
//
//        @Override
//        public void run() {

//            Class<?> cls = marqueeText.getClass();
//            Field field;
//            try {
//                field = cls.getDeclaredField("mMarquee");
//                field.setAccessible(true);
//                Object mMarquee = field.get(marqueeText);
//                if(mMarquee == null){
//                    if(!isColose) {
//                        isColose = true;
//                        handler.postDelayed(run, 5000);
//                    }else{
//                        handler.removeCallbacks(run);
//                        stopSelf();
//                    }
//                    return;
//                }
//                Class<?> aClass = mMarquee.getClass();
//                Field field2 = aClass.getDeclaredField("mStatus");
//                field2.setAccessible(true);
//                byte mStatus = (byte) field2.get(mMarquee);
//                if(mStatus == 0){
//                    handler.removeCallbacks(run);
//                    stopSelf();
//                }else{
//                    handler.postDelayed(run,1000);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

//            Class<?> cls = marqueeText.getClass();
//            Field field;
//            try {
//                field = cls.getDeclaredField("mMarquee");
//                field.setAccessible(true);
//                Object mMarquee = field.get(marqueeText);
//                if(mMarquee == null){
//                    stopSelf();
//                    return;
//                }
//                Class<?> aClass = mMarquee.getClass();
//                Field field2 = aClass.getDeclaredField("mStatus");
//                field2.setAccessible(true);
//                mStatus = (byte) field2.get(mMarquee);
//                if(mStatus == 0){
//                    currentPostion ++;
//                    if(currentPostion == strings.size()){
//                        stopSelf();
//                    }else{
//                        marqueeText.setText(strings.get(currentPostion));
//                    }
//                }else{
//                    handler.postDelayed(run,1000);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if(marqueeText.isEnd()){
//                currentPostion ++ ;
//                if(currentPostion < strings.size()){
//                    marqueeText.setText(strings.get(currentPostion));
//                    marqueeText.startScroll();
//                }else{
//                    stopSelf();
//                }
//            }
//            handler.postDelayed(run,1000);
//        }
//    }

}
