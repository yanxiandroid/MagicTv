package com.yht.iptv.view;

import android.app.Application;
import android.content.Context;

import com.apkfuns.logutils.LogUtils;
import com.danikula.videocache.HttpProxyCacheServer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.squareup.leakcanary.LeakCanary;
import com.yht.iptv.utils.CrashHandler;
import com.yht.iptv.utils.FileUtils;
import com.yht.iptv.utils.TypefaceUtil;
import com.yht.iptv.utils.Utils;

import org.xutils.x;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 *
 */

public class MyApplication extends Application {


    private static int TIME_OUT = 10 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(getApplicationContext());
        //工具类
        Utils.init(this);
        LeakCanary.install(this);
        x.Ext.init(this);
        initLogUtils();
        initOkhttp();
        TypefaceUtil.replaceSystemDefaultFont(this, "font/font.ttf");
    }

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        MyApplication app = (MyApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)
                .fileNameGenerator(new MyFileNameGenerator())
                .cacheDirectory(new File(FileUtils.getInfoPath("VideoCache")))
                .maxCacheFilesCount(1)
                .build();
    }

    private void initLogUtils(){
        LogUtils.getLogConfig()
                .configAllowLog(true)
                .configTagPrefix("LogUtils")
                .configShowBorders(true)
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}");

        // 支持写入日志到文件
        LogUtils.getLog2FileConfig().configLog2FileEnable(false);
    }

    private void initOkhttp(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //配置日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkHttp");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        //配置超时时间
        //全局的读取超时时间
        builder.readTimeout(TIME_OUT,TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        //全局配置
        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(0);
    }




}
