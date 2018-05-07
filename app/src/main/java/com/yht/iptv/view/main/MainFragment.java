package com.yht.iptv.view.main;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.yht.iptv.R;
import com.yht.iptv.adapter.MainFragmentRecAdapter;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.EventRefreshMainInfo;
import com.yht.iptv.model.MainPageInfo;
import com.yht.iptv.model.MainUIBean;
import com.yht.iptv.model.MainUIModel;
import com.yht.iptv.model.WeatherInfo;
import com.yht.iptv.presenter.MainNavigationPresenter;
import com.yht.iptv.service.NetWorkService;
import com.yht.iptv.service.PageRecordService;
import com.yht.iptv.service.TitleRequestService;
import com.yht.iptv.service.WeatherService;
import com.yht.iptv.utils.AnimationUtils;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.OkHttpUtils;
import com.yht.iptv.utils.ServiceUtils;
import com.yht.iptv.utils.ShowImageUtils;
import com.yht.iptv.utils.ToastUtils;
import com.yht.iptv.view.BaseFragment;
import com.yht.iptv.view.MainActivity;
import com.yht.iptv.view.MenuDialogFragment;
import com.yht.iptv.view.mall.MallGoodsListActivity;
import com.yht.iptv.view.music.MusicActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 2017/10/11.
 * 向导页面
 */

public class MainFragment extends BaseFragment implements TvRecyclerView.OnItemListener, IPresenterBase<List<MainUIModel>> {

    private TvRecyclerView recyclerView;
    private Context mContext;
    private boolean isHidden;
    private String[] strings;
//    private SparseArray<String> sparseArray;
    private MainPageInfo mainPageInfo;
    private List<MainUIBean> mainUIBeenList;

    private String[] titleList = {Constants.TITLE_MOVIE, Constants.TITLE_MUSIC, Constants.TITLE_TV,Constants.TITLE_WEATHER,Constants.TITLE_SERVICE,Constants.TITLE_SHOPPING,Constants.TITLE_FOOD, Constants.TITLE_NEAR};
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
    private int[] imageResources;
    private int[] imageResourcesSelected;
    private int currentPos;
    private long currentTimeMillis = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

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

        recyclerView = (TvRecyclerView) view.findViewById(R.id.recyclerView);

        RelativeLayout press_info = (RelativeLayout) view.findViewById(R.id.press_info);
        LinearLayout ll_down_info = (LinearLayout) press_info.findViewById(R.id.ll_down_info);
        ll_down_info.setBackgroundColor(Color.parseColor("#7f19191d"));

        strings = mContext.getResources().getStringArray(R.array.main_fragment_tab);
        imageResources = new int[]{
                R.drawable.main_title_movie,
                R.drawable.main_title_music,
                R.drawable.main_title_tv,
                R.drawable.main_title_weather,
                R.drawable.main_title_service,
                R.drawable.main_title_shopping,
                R.drawable.main_title_food,
                R.drawable.main_title_near
                };
        imageResourcesSelected = new int[]{
                R.drawable.main_title_movie_select,
                R.drawable.main_title_music_select,
                R.drawable.main_title_tv_select,
                R.drawable.main_title_weather_select,
                R.drawable.main_title_service_select,
                R.drawable.main_title_shopping_select,
                R.drawable.main_title_food_select,
                R.drawable.main_title_near_select
                };

        EventBus.getDefault().register(this);

//        int index = 0;
//        sparseArray = new SparseArray<>();
//        for (int i = 0; i < strings.length; i++) {
//            if (strings[i].equals("0")) {
//                sparseArray.append(i, "0");
//            } else {
//                sparseArray.append(i, titleList[index++]);
//            }
//        }
//
//        iconArray = new SparseIntArray();
//        iconSelectedArray = new SparseIntArray();
//        for (int i = 0 ; i < imageResources.length; i++){
//            if(imageResources[i] != 0){
//                iconArray.append(i,imageResources[i]);
//                iconSelectedArray.append(i,imageResources[i]);
//            }
//        }

        if (Constants.mainPageInfo == null) {
            //请求网络获取信息
            EventBus.getDefault().post(new EventRefreshMainInfo());
        } else {
            mainPageInfo = Constants.mainPageInfo;
        }

        //启动服务更新天气
        ServiceUtils.startService(WeatherService.class);

        initData();


//        MainFragmentRecAdapter adapter = new MainFragmentRecAdapter(mContext, strings, imageResources);
//        recyclerView.setAdapter(adapter);


        handler.post(runnable);

        if(savedInstanceState == null) {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setSelection(2);
                }
            }, 50);
        }else{
            final int selectedPos = savedInstanceState.getInt("selectedPos");
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setSelection(selectedPos);
                }
            }, 50);
        }

        mainUIBeenList = new ArrayList<>();
        //请求网络
        MainNavigationPresenter presenter = new MainNavigationPresenter(mContext,this);
        presenter.request(this);

        LogUtils.d("main_createview");

        return view;
    }


    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示
            isHidden = true;
        } else {// 重新显示到最前端中
            isHidden = false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("main_create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("main_destroy");
    }

    @Override
    public void onStart() {
        super.onStart();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,0);
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,0);
        EventBus.getDefault().unregister(this);
        OkHttpUtils.cancel(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d("main_destroyview");
        handler.removeCallbacks(runnable);
        EventBus.getDefault().unregister(this);
        //执行activity播放酒店视频
        ((MainActivity)mContext).onMainDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!isHidden) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (recyclerView.getSelectedPosition() == 2) {
                    return true;
                }
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                try {
                    int childCount = recyclerView.getAdapter().getItemCount();
                    if (recyclerView.getSelectedPosition() == childCount - 3) {
                        return true;
                    }
                }catch (Exception e){
                    return true;
                }
            }

            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                if (getFragmentManager().findFragmentByTag("menu") == null) {
                    MenuDialogFragment fragment = new MenuDialogFragment();
                    fragment.show(getFragmentManager(), "menu");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
        ImageView iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
        ImageView iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        TextView tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        ViewGroup.LayoutParams iv_icon_params = iv_icon.getLayoutParams();
        iv_icon_params.width = (int) getResources().getDimension(R.dimen.w_78);
        iv_icon_params.height = (int) getResources().getDimension(R.dimen.h_78);
        iv_icon.setLayoutParams(iv_icon_params);
        iv_icon.setImageResource(mainUIBeenList.get(position).getIcon());
        tv_title.setTextColor(Color.parseColor("#7FFFFFFF"));
        float translationY = iv_bg.getTranslationY();
        AnimationUtils.scaleYColorTranslationAnimation(iv_bg, iv_icon, 400, Color.parseColor("#e6e1c990"), Color.parseColor("#e692876c"), 1.2f, 1.0f, translationY, 0);
    }

    @Override
    public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
        ImageView iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
        ImageView iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        TextView tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        ViewGroup.LayoutParams iv_icon_params = iv_icon.getLayoutParams();
        iv_icon_params.width = (int) mContext.getResources().getDimension(R.dimen.w_92);
        iv_icon_params.height = (int) mContext.getResources().getDimension(R.dimen.h_92);
        iv_icon.setLayoutParams(iv_icon_params);
        iv_icon.setImageResource(mainUIBeenList.get(position).getIconSelected());
        tv_title.setTextColor(Color.parseColor("#FFFFFF"));
        float translationY = iv_bg.getTranslationY();
        float height = iv_bg.getHeight() * 0.1f;
        AnimationUtils.scaleYColorTranslationAnimation(iv_bg, iv_icon, 400, Color.parseColor("#e692876c"), Color.parseColor("#e6e1c990"), 1.0f, 1.2f, translationY, -height);
    }

    @Override
    public void onReviseFocusFollow(TvRecyclerView parent, View itemView, int position) {

    }

    @Override
    public void onItemClick(TvRecyclerView parent, View itemView, final int position) {
        currentPos = position;
        if(mainUIBeenList.get(currentPos).getType().equals(Constants.TITLE_TV)){
            if(isInstallByread(Constants.mstar_tv_player)) {
                Bundle bundle = new Bundle();
                bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_START);
                bundle.putString("behaviour", Constants.TV);
                ServiceUtils.startService(PageRecordService.class, bundle);
                //进入TV播放界面
                ComponentName componentName = new ComponentName(Constants.mstar_tv_player,
                        "com.mstar.tv.tvplayer.ui.RootActivity");
                Intent intent01 = new Intent(Intent.ACTION_MAIN);
                intent01.addCategory(Intent.CATEGORY_LAUNCHER);
                intent01.setComponent(componentName);
                startActivity(intent01);
            }else {
                ToastUtils.showShort("该设备不支持电视直播!");
            }
            return;
        }
        Intent intent = new Intent(mContext,NetWorkService.class);
        intent.putExtra("IPAddress",Constants.IP_ADDRESS);
        mContext.startService(intent);
    }


    private void initData() {
        if (mainPageInfo != null) {
            hotel_name_en.setText(mainPageInfo.getEnName() + "  ");
            hotel_name.setText(mainPageInfo.getZhName());
            ShowImageUtils.showImageView(mContext, mainPageInfo.getLogo().getPath(), iv_logo);
        } else {
            ShowImageUtils.showImageView(mContext, R.drawable.hotel_phone_icon, iv_logo);
        }
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
            handler.postDelayed(runnable, 1000);
        }
    };

    public String getWeekOfDate(Date date) {
        String[] weekDays = getResources().getStringArray(R.array.guide_week);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDays[w];
    }

    /**
     * 接收mainActivity请求更新界面
     *
     * @param mainPageInfo
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainPageInfo(MainPageInfo mainPageInfo) {
        if (mainPageInfo.getId() != null) {
            this.mainPageInfo = mainPageInfo;
        }
        initData();
    }

    /**
     * 接收WeatherService发送的事件数据
     *
     * @param info
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWeatherInfo(WeatherInfo info) {
        if (info.getImagePath() == null) {
            //无数据
        } else {
            String path = info.getImagePath();
            initWeatherData(info, path);
        }
    }


    private void initWeatherData(WeatherInfo weatherInfo, String path) {
        try {
            if (weatherInfo.getHeWeather5().get(0).getStatus().equals("ok")) {
                WeatherInfo.HeWeather5Bean weather5Bean = weatherInfo.getHeWeather5().get(0);
                if (weather5Bean.getNow() != null) {
                    ShowImageUtils.showImageView(this, path + weather5Bean.getNow().getCond().getCode() + ".png", iv_weather_icon);
                    tv_weather_cond.setText(weather5Bean.getNow().getCond().getTxt());
                    tv_weather_tmp.setText(weather5Bean.getNow().getTmp() + "℃");
                    tv_weather_wind.setText(weather5Bean.getNow().getWind().getDir());
                }
                if (weather5Bean.getAqi() != null) {
                    tv_weather_quality.setText(weather5Bean.getAqi().getCity().getQlty());
                }

                ll_weather.setVisibility(View.VISIBLE);
            } else {
                ll_weather.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            ll_weather.setVisibility(View.GONE);
        }

    }

    private Handler handler = new Handler();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedPos",recyclerView.getSelectedPosition());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetInfo(String status){
        if(status.equals(Constants.NETWORK_OK)){
            switch (mainUIBeenList.get(currentPos).getType()) {
                case Constants.TITLE_MUSIC:
                    //进入音乐播放界面
                    Intent intent = new Intent(mContext, MusicActivity.class);
                    mContext.startActivity(intent);
                    ((Activity)mContext).overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
                    return;
                case Constants.TITLE_SHOPPING:
                    if (Constants.mainPageInfo == null) {
                        ToastUtils.showShort("主页信息为空,请检查网络连接");
                        return;
                    }
                    //进入购物界面
                    Intent intent02 = new Intent(mContext, MallGoodsListActivity.class);
                    startActivity(intent02);
                    ((Activity)mContext).overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
                    return;
                case Constants.TITLE_WEATHER:
                    if (getFragmentManager().findFragmentByTag("WeatherDialogFragment") == null) {
                        //进入天气界面
                        WeatherDialogFragment fragment = new WeatherDialogFragment();
                        fragment.show(getFragmentManager(), "WeatherDialogFragment");
                    }
                    ServiceUtils.startService(WeatherService.class);
                    return;
//                case Constants.TITLE_MESSAGE:
//                    if (getFragmentManager().findFragmentByTag("WeatherDialogFragment") == null) {
//                        //进入消息页面
//                        MessagePushDialogFragment messagePushDialogFragment = new MessagePushDialogFragment();
//                        messagePushDialogFragment.show(getFragmentManager(), "MessagePushDialogFragment");
//                    }
//                    return;
            }
            if (getFragmentManager().findFragmentByTag("MainMenuDialogFragment") == null) {
                MainMenuDialogFragment fragment = new MainMenuDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("mainUIBeenList", (ArrayList<? extends Parcelable>) mainUIBeenList.get(currentPos).getSecServiceList());
                fragment.setArguments(bundle);
                fragment.show(getFragmentManager(), "MainMenuDialogFragment");
            }
            Bundle bundle = new Bundle();
            bundle.putString(Constants.MODULE_TITLE, mainUIBeenList.get(currentPos).getType());
            if(mainUIBeenList.get(currentPos).getSecServiceList() != null) {
                bundle.putParcelableArrayList("mainUIBeenList", (ArrayList<? extends Parcelable>) mainUIBeenList.get(currentPos).getSecServiceList());
            }
            ServiceUtils.startService(TitleRequestService.class, bundle);
        }else{
            ToastUtils.showShort("网络连接失败");
        }
    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    @Override
    public void onSuccess(BaseModel<List<MainUIModel>> dataList) {
        LogUtils.d(dataList);
        try {
            mainUIBeenList.clear();
            List<MainUIModel> data = dataList.data;
            for(int i = 0 ; i < data.size();i++){
                if(data.get(i).getDisplay() == 1) {
                    MainUIBean mainUIBean = new MainUIBean();
                    int tag = data.get(i).getTag();
                    mainUIBean.setIcon(imageResources[(tag - 1) % imageResources.length]);
                    mainUIBean.setIconSelected(imageResourcesSelected[(tag - 1) % imageResources.length]);
                    mainUIBean.setName(data.get(i).getMenuName());
                    if(tag-1 < titleList.length) {
                        mainUIBean.setType(titleList[(tag - 1)]);
                    }else {
                        mainUIBean.setType(Constants.TITLE_OTHER);
                    }
                    mainUIBean.setTag(tag);
                    mainUIBean.setSecServiceList(data.get(i).getSecServiceList());
                    mainUIBeenList.add(mainUIBean);
                }
            }
            mainUIBeenList.add(0,new MainUIBean());
            mainUIBeenList.add(0,new MainUIBean());
            mainUIBeenList.add(new MainUIBean());
            mainUIBeenList.add(new MainUIBean());

            recyclerView.setHasFixedSize(true);
            recyclerView.setInterceptKeyEvent(true);
            recyclerView.setSelectedItemAtCentered(true);
            recyclerView.setOnItemListener(this);
            MainFragmentRecAdapter adapter = new MainFragmentRecAdapter(mContext, mainUIBeenList);
            recyclerView.setAdapter(adapter);

            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setSelection(2);
                }
            }, 50);

        }catch (Exception e){

        }
    }

    @Override
    public void onError() {
        try {
            for(int i = 0 ; i < 4;i++){
                MainUIBean mainUIBean = new MainUIBean();
                mainUIBean.setIcon(imageResources[i%imageResources.length]);
                mainUIBean.setIconSelected(imageResourcesSelected[i%imageResources.length]);
                mainUIBean.setName(strings[(i + 2)%strings.length]);
                if(i < titleList.length) {
                    mainUIBean.setType(titleList[i]);
                }else {
                    mainUIBean.setType(Constants.TITLE_OTHER);
                }
                mainUIBean.setTag(i);
                mainUIBeenList.add(mainUIBean);
            }

            mainUIBeenList.add(0,new MainUIBean());
            mainUIBeenList.add(0,new MainUIBean());
            mainUIBeenList.add(new MainUIBean());
            mainUIBeenList.add(new MainUIBean());

            recyclerView.setHasFixedSize(true);
            recyclerView.setInterceptKeyEvent(true);
            recyclerView.setSelectedItemAtCentered(true);
            recyclerView.setOnItemListener(this);
            MainFragmentRecAdapter adapter = new MainFragmentRecAdapter(mContext, mainUIBeenList);
            recyclerView.setAdapter(adapter);

            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setSelection(2);
                }
            }, 50);

        }catch (Exception e){

        }
    }
}
