package com.yht.iptv.view.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.model.WeatherInfo;
import com.yht.iptv.service.PageRecordService;
import com.yht.iptv.utils.AppUtils;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.ServiceUtils;
import com.yht.iptv.utils.ShowImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on 2017/10/24.
 */

public class WeatherDialogFragment extends DialogFragment {

    private Context mContext;
    private TextView tv_weather_tmp;
    private TextView tv_weather_city;
    private ImageView iv_weather_icon;
    private TextView tv_weather_cond;
    private TextView tv_weather_wind;
    private TextView tv_weather_damp;
    private TextView tv_weather_quality;
    private TextView tv_today_temp;
    private ImageView iv_today_icon;
    private TextView tv_tomorrow_temp;
    private ImageView iv_tomorrow_icon;
    private TextView tv_threeDay_temp;
    private ImageView iv_threeDay_icon;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_fragment_weather, null);

        initViews(view);

        builder.setView(view);

        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS,Constants.PAGE_START);
        bundle.putString("behaviour",Constants.WEATHER);
        ServiceUtils.startService(PageRecordService.class,bundle);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawableResource(R.color.transparent);
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,60 * 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS,Constants.PAGE_END);
        ServiceUtils.startService(PageRecordService.class,bundle);
    }

    private void initViews(View view) {
        tv_weather_tmp = (TextView)  view.findViewById(R.id.tv_weather_tmp);
        tv_weather_city = (TextView)  view.findViewById(R.id.tv_weather_city);
        iv_weather_icon = (ImageView) view.findViewById(R.id.iv_weather_icon);
        tv_weather_cond = (TextView)  view.findViewById(R.id.tv_weather_cond);
        tv_weather_wind = (TextView)  view.findViewById(R.id.tv_weather_wind);
        tv_weather_damp = (TextView)  view.findViewById(R.id.tv_weather_damp);
        tv_weather_quality = (TextView) view.findViewById(R.id.tv_weather_quality);


        tv_today_temp = (TextView)  view.findViewById(R.id.tv_today_temp);
        iv_today_icon = (ImageView) view.findViewById(R.id.iv_today_icon);
        tv_tomorrow_temp = (TextView)  view.findViewById(R.id.tv_tomorrow_temp);
        iv_tomorrow_icon = (ImageView) view.findViewById(R.id.iv_tomorrow_icon);
        tv_threeDay_temp = (TextView)  view.findViewById(R.id.tv_threeDay_temp);
        iv_threeDay_icon = (ImageView) view.findViewById(R.id.iv_threeDay_icon);

        TextView tv_today_name = (TextView) view.findViewById(R.id.tv_today_name);
        TextView tv_tomorrow_name = (TextView) view.findViewById(R.id.tv_tomorrow_name);
        TextView tv_threeDay_name = (TextView) view.findViewById(R.id.tv_threeDay_name);
        tv_today_name.setText(showTime(Constants.REAL_TIME));
        tv_tomorrow_name.setText(showTime(Constants.REAL_TIME + 24 * 3600 * 1000));
        tv_threeDay_name.setText(showTime(Constants.REAL_TIME + 24 * 3600 * 1000 * 2));
    }

    private String showTime(long timeMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd", Locale.CHINA);
        return sdf.format(new Date(timeMillis));
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        handler.removeCallbacks(runnable);
    }

    /**
     * 接收WeatherService发送的事件数据
     * @param info
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWeatherInfo(WeatherInfo info){
        if(info.getImagePath() == null){
            //无数据
        }else{
            String path = info.getImagePath();
            initWeatherData(info,path);
        }
    }

    private void initWeatherData(WeatherInfo weatherInfo,String path){
        try {
            if(weatherInfo.getHeWeather5().get(0).getStatus().equals("ok")){
                WeatherInfo.HeWeather5Bean weather5Bean = weatherInfo.getHeWeather5().get(0);
                if(weather5Bean.getNow() != null){
                    ShowImageUtils.showImageView(this,path + weather5Bean.getNow().getCond().getCode() + ".png",iv_weather_icon);
                    tv_weather_cond.setText(getResources().getString(R.string.weather) + "：" + weather5Bean.getNow().getCond().getTxt());
                    tv_weather_wind.setText(getResources().getString(R.string.wind) + "：" + weather5Bean.getNow().getWind().getDir());
                    tv_weather_damp.setText(getResources().getString(R.string.humi) + "：" + weather5Bean.getNow().getHum() + "%");
                    tv_weather_tmp.setText(weather5Bean.getNow().getTmp() + "℃");
                }

                if(weather5Bean.getAqi() != null){
                    tv_weather_quality.setText(getResources().getString(R.string.quality) + "：" + weather5Bean.getAqi().getCity().getQlty());
                }

                if(weather5Bean.getBasic() != null){
                    tv_weather_city.setText(weather5Bean.getBasic().getCity());
                }
                if(weather5Bean.getDaily_forecast() != null){
                    ImageView[] imageViews = new ImageView[]{iv_today_icon,iv_tomorrow_icon,iv_threeDay_icon};
                    TextView[] textViews = new TextView[]{tv_today_temp,tv_tomorrow_temp,tv_threeDay_temp};
                    for(int i = 0 ; i < weather5Bean.getDaily_forecast().size();i++) {
                        textViews[i].setText(weather5Bean.getDaily_forecast().get(i).getTmp().getMin() + "℃ ~ " + weather5Bean.getDaily_forecast().get(i).getTmp().getMax() + "℃");
                        ShowImageUtils.showImageView(this, path + weather5Bean.getDaily_forecast().get(i).getCond().getCode_d() + ".png",imageViews[i]);
                    }
                }
            }else{
            }
        }catch (Exception e){
        }

    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Bundle bundle = new Bundle();
            bundle.putByte(Constants.PAGE_STATUS,Constants.PAGE_END);
            ServiceUtils.startService(PageRecordService.class,bundle);
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable,60 * 1000);
        }
    };


    Handler handler = new Handler();
}
