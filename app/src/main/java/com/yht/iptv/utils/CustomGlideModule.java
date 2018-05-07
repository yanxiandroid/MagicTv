//package com.yht.iptv.utils;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.GlideBuilder;
//import com.bumptech.glide.Registry;
//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.load.DecodeFormat;
//import com.bumptech.glide.load.engine.cache.LruResourceCache;
//import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
//import com.bumptech.glide.load.model.ModelLoader;
//import com.bumptech.glide.module.AppGlideModule;
//import com.bumptech.glide.request.RequestOptions;
//
//import java.io.InputStream;
//
//@GlideModule
//public class CustomGlideModule extends AppGlideModule {
//
//    @Override
//    public boolean isManifestParsingEnabled() {
//        return false;
//    }
//
//
//    @Override
//    public void applyOptions(Context context, GlideBuilder builder) {
//        /*MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
//                .setMemoryCacheScreens(2)
//                .build();
//        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
//        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
//        builder.setLogLevel(Log.DEBUG);*/
//    }
//}