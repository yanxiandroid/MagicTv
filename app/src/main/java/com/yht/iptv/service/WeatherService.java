package com.yht.iptv.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.WeatherBean;
import com.yht.iptv.model.WeatherInfo;
import com.yht.iptv.presenter.WeatherPresenter;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.Convert;
import com.yht.iptv.utils.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by admin on 2017/10/17.
 */

public class WeatherService extends Service implements IPresenterBase<List<WeatherBean>> {


    private WeatherPresenter presenter;
    private WeatherInfo weatherInfo;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        presenter = new WeatherPresenter(this,this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        presenter.request(this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
        weatherInfo = null;
        OkHttpUtils.cancel(this);
    }

    @Override
    public void onSuccess(BaseModel<List<WeatherBean>> dataList) {
        String result = dataList.data.get(0).getResult();
        if(result == null ||result.equals("")){
            if(weatherInfo == null){
                weatherInfo = new WeatherInfo();
            }
            EventBus.getDefault().post(weatherInfo);
            stopSelf();
        }else {
            weatherInfo = Convert.fromJson(result, WeatherInfo.class);
            String path = dataList.data.get(0).getPath() + Constants.WEATHER_URL;
            weatherInfo.setImagePath(path);
            EventBus.getDefault().post(weatherInfo);
            stopSelf();
        }
    }

    @Override
    public void onError() {
        if(weatherInfo == null){
            weatherInfo = new WeatherInfo();
        }
        EventBus.getDefault().post(weatherInfo);
        stopSelf();
    }
}
