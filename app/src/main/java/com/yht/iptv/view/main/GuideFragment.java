package com.yht.iptv.view.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.yht.iptv.R;
import com.yht.iptv.callback.IFragmentOnclickListenr;
import com.yht.iptv.model.EventRefreshMainInfo;
import com.yht.iptv.model.MainPageInfo;
import com.yht.iptv.model.WeatherInfo;
import com.yht.iptv.utils.AppUtils;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.LanguageUtils;
import com.yht.iptv.utils.ShowImageUtils;
import com.yht.iptv.view.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on 2017/10/11.
 * 向导页面
 */

public class GuideFragment extends BaseFragment implements View.OnFocusChangeListener, View.OnClickListener {

    private final int TV_LANUAGE_ZH = 1;
    private final int TV_LANUAGE_EN = 2;
    private ImageView iv_under_line_zh;
    private ImageView iv_under_line_en;

    private Context mContext;
    private TextView tv_language_zh;
    private TextView tv_language_en;

    private IFragmentOnclickListenr fragmentOnclickListenr;
    private ImageView iv_logo;
    private TextView hotel_name_en;
    private TextView hotel_name;
    private TextView tv_time;
    private TextView tv_date;
    private TextView tv_week;
    private LinearLayout ll_weather;
    private ImageView iv_weather_icon;
    private TextView tv_weather_tmp;
    private TextView tv_weather_cond;
    private TextView tv_weather_wind;
    private TextView tv_weather_quality;
    private TextView tv_welcome;
    private TextView tv_passenger;
    private TextView tv_welcome_detail;
    private TextView bt_more_press;
    private TextView bt_more;
    private TextView bt_apply_press;
    private TextView tv_apply;
    private int currentLanguage;
    private boolean isHidden;

    private MainPageInfo mainPageInfo;
    private TextView tv_version;
    private long currentTimeMillis = 0;
    private boolean isGoto = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_guide,container,false);

        RelativeLayout guide_info = (RelativeLayout) view.findViewById(R.id.guide_info);
        iv_logo = (ImageView) guide_info.findViewById(R.id.iv_logo);
        hotel_name_en = (TextView) guide_info.findViewById(R.id.hotel_name_en);
        hotel_name = (TextView) guide_info.findViewById(R.id.hotel_name);
        tv_time = (TextView) guide_info.findViewById(R.id.tv_time);

        LinearLayout ll_current_time = (LinearLayout) guide_info.findViewById(R.id.ll_current_time);
        tv_date = (TextView) ll_current_time.findViewById(R.id.tv_date);
        tv_week = (TextView) ll_current_time.findViewById(R.id.tv_week);

        ll_weather = (LinearLayout) guide_info.findViewById(R.id.ll_weather);
        iv_weather_icon = (ImageView) guide_info.findViewById(R.id.iv_weather_icon);
        tv_weather_tmp = (TextView) guide_info.findViewById(R.id.tv_weather_tmp);
        tv_weather_cond = (TextView) guide_info.findViewById(R.id.tv_weather_cond);
        tv_weather_wind = (TextView) guide_info.findViewById(R.id.tv_weather_wind);
        tv_weather_quality = (TextView) guide_info.findViewById(R.id.tv_weather_quality);

        tv_welcome = (TextView) view.findViewById(R.id.tv_welcome);
        tv_passenger = (TextView) view.findViewById(R.id.tv_Passenger);
        tv_welcome_detail = (TextView) view.findViewById(R.id.tv_welcome_detail);

        tv_version = (TextView) view.findViewById(R.id.tv_version);

        tv_language_zh = (TextView) view.findViewById(R.id.tv_language_zh);
        tv_language_en = (TextView) view.findViewById(R.id.tv_language_en);
        iv_under_line_zh = (ImageView) view.findViewById(R.id.iv_under_line_zh);
        iv_under_line_en = (ImageView) view.findViewById(R.id.iv_under_line_en);

        RelativeLayout press_info = (RelativeLayout) view.findViewById(R.id.press_info);
        LinearLayout ll_cancel = (LinearLayout) press_info.findViewById(R.id.ll_cancel);
        LinearLayout ll_choose = (LinearLayout) press_info.findViewById(R.id.ll_choose);
        bt_more_press = (TextView) press_info.findViewById(R.id.bt_more_press);
        bt_more = (TextView) press_info.findViewById(R.id.bt_more);
        bt_apply_press = (TextView) press_info.findViewById(R.id.bt_apply_press);
        tv_apply = (TextView) press_info.findViewById(R.id.tv_apply);
        press_info.setVisibility(View.GONE);
        ll_cancel.setVisibility(View.GONE);
        ll_choose.setVisibility(View.GONE);

        tv_language_zh.setOnFocusChangeListener(this);
        tv_language_en.setOnFocusChangeListener(this);
        tv_language_zh.setTag(TV_LANUAGE_ZH);
        tv_language_en.setTag(TV_LANUAGE_EN);
        tv_language_zh.setOnClickListener(this);
        tv_language_en.setOnClickListener(this);

        handler.postDelayed(runnable,0);

        if(savedInstanceState == null) {
            tv_language_zh.requestFocus();
            tv_language_zh.setTextColor(Color.parseColor("#fedfa9"));
            iv_under_line_zh.setVisibility(View.VISIBLE);
        }else{
            if(Constants.currentLanguage.equals("zh")){
                tv_language_zh.requestFocus();
                tv_language_zh.setTextColor(Color.parseColor("#fedfa9"));
                iv_under_line_zh.setVisibility(View.VISIBLE);
            }else{
                tv_language_en.requestFocus();
                tv_language_en.setTextColor(Color.parseColor("#ffffff"));
                iv_under_line_en.setVisibility(View.VISIBLE);
            }
        }

        tv_version.setText("V" + AppUtils.getAppVersionName() + "_" + AppUtils.getAppVersionCode());

        EventBus.getDefault().register(this);


        return view;
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示
            tv_language_zh.setOnFocusChangeListener(null);
            tv_language_en.setOnFocusChangeListener(null);
            EventBus.getDefault().unregister(this);
            handler.removeCallbacks(runnable);
            isHidden = true;
            LogUtils.d("hide");
        } else {// 重新显示到最前端中
            isGoto = false;
            tv_language_zh.setOnFocusChangeListener(this);
            tv_language_en.setOnFocusChangeListener(this);
            if(!EventBus.getDefault().isRegistered(this)){
                EventBus.getDefault().register(this);
            }
            isHidden = false;
            if(currentLanguage == 1) {
                tv_language_zh.requestFocus();
            }else{
                tv_language_en.requestFocus();
            }
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable,0);
            LogUtils.d("show");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("guide_create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("guide_destroy");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d("guide_destroyview");
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        int tag = (int) view.getTag();
        if (hasFocus) {
            if (tag == TV_LANUAGE_ZH) {
                ((TextView) view).setTextColor(Color.parseColor("#fedfa9"));
                iv_under_line_zh.setVisibility(View.VISIBLE);
                LanguageUtils.getInstance().changeLanguage("zh",mContext);
                currentLanguage = 1;
                Constants.currentLanguage = "zh";
                //发送一个给mainActivity请求
                EventBus.getDefault().post(new EventRefreshMainInfo());
            } else if (tag == TV_LANUAGE_EN) {
                ((TextView) view).setTextColor(Color.parseColor("#fedfa9"));
                iv_under_line_en.setVisibility(View.VISIBLE);
                LanguageUtils.getInstance().changeLanguage("en",mContext);
                currentLanguage = 2;
                Constants.currentLanguage = "en";
                //发送一个给mainActivity请求
                EventBus.getDefault().post(new EventRefreshMainInfo());
            }

        } else {
            if(!isGoto) {
                if (tag == TV_LANUAGE_ZH) {
                    ((TextView) view).setTextColor(Color.parseColor("#ffffff"));
                    iv_under_line_zh.setVisibility(View.INVISIBLE);
                } else if (tag == TV_LANUAGE_EN) {
                    ((TextView) view).setTextColor(Color.parseColor("#ffffff"));
                    iv_under_line_en.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_language_zh:
                isGoto = true;
                if(mContext instanceof IFragmentOnclickListenr){
                    ((IFragmentOnclickListenr)mContext).onGuideClick();
                }
                break;
            case R.id.tv_language_en:
                isGoto = true;
                if(mContext instanceof IFragmentOnclickListenr){
                    ((IFragmentOnclickListenr)mContext).onGuideClick();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(!isHidden) {
            LogUtils.d(event.getKeyCode() + "");
        }
        return false;
    }

    private void showCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA);
        String times = sdf.format(new Date(Constants.REAL_TIME));
        String[] timesArray = times.split(" ");
        tv_week.setText(getWeekOfDate(new Date(Constants.REAL_TIME)));
        tv_date.setText(timesArray[0]);
        tv_time.setText(timesArray[1]);
        if(mainPageInfo != null) {
            mainPageInfo.setSystemTime(Constants.REAL_TIME);
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            showCurrentTime();
            handler.postDelayed(runnable,1000);
        }
    };

    public String getWeekOfDate(Date date) {
        String[] weekDays = mContext.getResources().getStringArray(R.array.guide_week);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDays[w];
    }

    private void initData(){
        if(mainPageInfo != null){
            tv_welcome_detail.setText("\u3000\u3000" + mainPageInfo.getSpeech());
            hotel_name_en.setText(mainPageInfo.getEnName() + "  ");
            hotel_name.setText(mainPageInfo.getZhName());
            ShowImageUtils.showImageView(mContext,mainPageInfo.getLogo().getPath(),iv_logo);
        }else{
            tv_welcome_detail.setText(R.string.guide_detail);
            ShowImageUtils.showImageView(mContext,R.drawable.hotel_phone_icon,iv_logo);
        }
        if(Constants.currentLanguage.equals("zh")) {
            tv_passenger.setText(getString(R.string.guide_dear) + mainPageInfo.getCustomerName() + "：");
        }else{
            tv_passenger.setText(getString(R.string.guide_dear) + " " + mainPageInfo.getCustomerName() + ":");
        }
        tv_welcome.setText(R.string.guide_welcome);
        bt_more_press.setText(R.string.general_press);
        bt_more.setText(R.string.general_more);
        bt_apply_press.setText(R.string.general_press);
        tv_apply.setText(R.string.general_enter);
    }

    private Handler handler = new Handler();


    /**
     * 接收mainActivity请求更新界面
     * @param mainPageInfo
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainPageInfo(MainPageInfo mainPageInfo){
        if(mainPageInfo.getId() != null){
            this.mainPageInfo = mainPageInfo;
        }
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d("onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d("onStop");
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

    private void initWeatherData(WeatherInfo weatherInfo, String path){
        try {
            if(weatherInfo.getHeWeather5().get(0).getStatus().equals("ok")){
                WeatherInfo.HeWeather5Bean weather5Bean = weatherInfo.getHeWeather5().get(0);
                if(weather5Bean.getNow() != null){
                    ShowImageUtils.showImageView(this,path + weather5Bean.getNow().getCond().getCode() + ".png",iv_weather_icon);
                    tv_weather_cond.setText(weather5Bean.getNow().getCond().getTxt());
                    tv_weather_tmp.setText(weather5Bean.getNow().getTmp() + "℃");
                    tv_weather_wind.setText(weather5Bean.getNow().getWind().getDir());
                }
                if(weather5Bean.getAqi() != null) {
                    tv_weather_quality.setText(weather5Bean.getAqi().getCity().getQlty());
                }

                ll_weather.setVisibility(View.VISIBLE);
            }else{
                ll_weather.setVisibility(View.GONE);
            }
        }catch (Exception e){
            ll_weather.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
