package com.yht.iptv.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by admin on 2017/6/28.
 */

public class LanguageUtils {


    private static LanguageUtils instance;

    private LanguageUtils(){
    }

    public static LanguageUtils getInstance(){
        if(instance == null){
            synchronized (LanguageUtils.class){
                instance = new LanguageUtils();
            }
        }
        return instance;
    }

    public void changeLanguage(String languageToLoad,Context mContext){
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = mContext.getResources().getConfiguration();
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        config.setLocale(locale);
        mContext.getResources().updateConfiguration(config, metrics);
    }
    public String getLanguage(Context mContext) {
        Locale locale = mContext.getResources().getConfiguration().locale;
        return locale.getLanguage();
    }

}
