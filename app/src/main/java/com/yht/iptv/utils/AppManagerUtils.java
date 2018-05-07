package com.yht.iptv.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.Stack;

public class AppManagerUtils {

    private static Stack<Activity> activityStack;
    private static AppManagerUtils instance;

    private AppManagerUtils(){}
    /** 
     * 单一实例 
     */  
    public static AppManagerUtils getAppManager(){
        if(instance==null){
            synchronized (AppManagerUtils.class) {
                if(instance == null) {
                    instance = new AppManagerUtils();
                }
            }
        }  
        return instance;  
    }  
    /** 
     * 添加Activity到堆栈 
     */  
    public void addActivity(Activity activity){
        if(activityStack==null){  
            activityStack=new Stack<>();
        }  
        activityStack.add(activity);  
    }  
    /** 
     * 获取当前Activity（堆栈中最后一个压入的） 
     */  
    public Activity currentActivity(){
        return activityStack.lastElement();
    }  
    /** 
     * 结束当前Activity（堆栈中最后一个压入的） 
     */  
    public void finishActivity(){
        Activity activity=activityStack.lastElement();
        finishActivity(activity);  
    }  
    /** 
     * 结束指定的Activity 
     */  
    public void finishActivity(Activity activity){
        if(activity!=null){
            activityStack.remove(activity);
            activity.finish();
        }  
    }  
    /** 
     * 结束指定类名的Activity 
     */  
    public void finishActivity(Class<?> cls){  
        for (Activity activity : activityStack) {
            if(activity.getClass().equals(cls) ){  
                finishActivity(activity);  
            }  
        }  
    }  
    /** 
     * 结束所有Activity 
     */  
    public void finishAllActivity(){  
        for (int i = 0, size = activityStack.size(); i < size; i++){  
            if (null != activityStack.get(i)){  
                activityStack.get(i).finish();
            }  
        }  
        activityStack.clear();  
    }

    /**
     * 结束所有Activity
     */
    public int getAllActivitySize(){
        return activityStack.size();
    }
    /**
     * 应用程序退出
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            System.exit(0);
        }
    }

    //重启APP
    public void reBootAppSelf(Context context){
        Intent i = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

} 