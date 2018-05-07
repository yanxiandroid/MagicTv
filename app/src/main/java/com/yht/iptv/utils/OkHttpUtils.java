package com.yht.iptv.utils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;

/**
 * Created by admin on 2017/7/27.
 */

public class OkHttpUtils {

    public static <T> void getJson(String url, Object obj, HttpParams params, Callback<T> callback){

        OkGo.<T>get(url)
                .tag(obj)
                .params(params)
                .execute(callback);

    }

    public static <T> void getJson(String url, HttpParams params, Callback<T> callback){

        OkGo.<T>get(url)
                .params(params)
                .execute(callback);

    }

    public static <T> void getJson(String url,Object tag,Callback<T> callback){

        OkGo.<T>get(url)
                .tag(tag)
                .execute(callback);

    }

    public static <T> void getJson(String url, CacheMode cacheMode,long cacheTime,Object tag, HttpParams params,Callback<T> callback){

        OkGo.<T>get(url)
                .tag(tag)
                .cacheKey(url + Constants.currentLanguage)
                .cacheMode(cacheMode)
                .cacheTime(cacheTime)
                .params(params)
                .execute(callback);

    }


    public static <T> void postJson(String url, Object obj, HttpParams params, Callback<T> callback){
        OkGo.<T>post(url)
                .tag(obj)
                .params(params)
                .execute(callback);
    }


    public static <T> void postJson(String url, HttpParams params, Callback<T> callback){
        OkGo.<T>post(url)
                .params(params)
                .execute(callback);
    }


    public static <T> void postJson(String url,Object tag,Callback<T> callback){
        OkGo.<T>post(url)
                .tag(tag)
                .execute(callback);
    }


    public static <T> void postJson(String url,CacheMode cacheMode,long cacheTime,Object tag,HttpParams params,Callback<T> callback){
        OkGo.<T>post(url)
                .tag(tag)
                .cacheMode(cacheMode)
                .cacheTime(cacheTime)
                .cacheKey(url + Constants.currentLanguage)
                .params(params)
                .execute(callback);
    }


    public static void cancel(Object object){
        OkGo.getInstance().cancelTag(object);
    }

    public static void cancel(){
        OkGo.getInstance().cancelAll();
    }

    public static boolean isRunning(Object tag){
        for (Call call : OkGo.getInstance().getOkHttpClient().dispatcher().runningCalls()) {
            if(call.request().tag().equals(tag)){
                return true;
            }
        }
        return false;
    }


}
