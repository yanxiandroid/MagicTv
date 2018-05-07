package com.yht.iptv.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yht.iptv.R;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.HotelFoodBean;
import com.yht.iptv.model.MainUIBean;
import com.yht.iptv.model.MainUIModel;
import com.yht.iptv.model.MovieClassificationBean;
import com.yht.iptv.model.TitleBean;
import com.yht.iptv.presenter.HotelFoodPresenter;
import com.yht.iptv.presenter.MovieClassificationPresenter;
import com.yht.iptv.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/19.
 * 标题请求的服务类
 */

public class TitleRequestService extends Service {

    private MovieClassificationPresenter movieClassificationPresenter;
    private HotelFoodPresenter hotelFoodPresenter;
    private String module_title;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        movieClassificationPresenter = new MovieClassificationPresenter(this, new ClassificationListener());
        hotelFoodPresenter = new HotelFoodPresenter(this, new HotelFoodListener());

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                module_title = bundle.getString(Constants.MODULE_TITLE);
                List<MainUIModel.SecServiceListBean> serviceList = bundle.getParcelableArrayList("mainUIBeenList");
                if (module_title != null) {
                    switch (module_title) {
                        case Constants.TITLE_MOVIE:
                            //获取分类标题
                            movieClassificationPresenter.request(Constants.TITLE_MOVIE);
                            break;
                        case Constants.TITLE_FOOD:
                            //获取美食标题
                            hotelFoodPresenter.request(Constants.TITLE_FOOD);
                            break;
                        case Constants.TITLE_SERVICE:
                            /*String[] strings = getResources().getStringArray(R.array.hotel_service_list);*/
                            ArrayList<TitleBean> titleList = new ArrayList<>();
                            /*for (int i = 0; i < strings.length; i++) {
                                TitleBean titleBean = new TitleBean();
                                titleBean.setId(i + 0x1000);
                                titleBean.setName(strings[i]);
                                titleBean.setType(module_title);
                                titleList.add(titleBean);
                            }*/
                            if (serviceList != null) {
                                for (int i = 0; i < serviceList.size(); i++) {
                                    if(serviceList.get(i).getDisplay() == 1) {
                                        TitleBean titleBean = new TitleBean();
                                        titleBean.setId(i + 0x1000);
                                        titleBean.setName(serviceList.get(i).getMenuName());
                                        titleBean.setType(module_title);
                                        titleList.add(titleBean);
                                    }
                                }
                            }
                            EventBus.getDefault().post(titleList);
                            stopSelf();
                            break;
                        case Constants.TITLE_NEAR:
                            /*String[] nears = getResources().getStringArray(R.array.near_list);*/
                            ArrayList<TitleBean> titlelist = new ArrayList<>();
                            /*for (int i = 0; i < nears.length; i++) {
                                TitleBean titleBean = new TitleBean();
                                titleBean.setId(i + 0x1000);
                                titleBean.setName(nears[i]);
                                titleBean.setType(module_title);
                                titlelist.add(titleBean);
                            }*/
                            if (serviceList != null) {
                                for (int i = 0; i < serviceList.size(); i++) {
                                    if(serviceList.get(i).getDisplay() == 1) {
                                        TitleBean titleBean = new TitleBean();
                                        titleBean.setId(i + 0x1000);
                                        titleBean.setName(serviceList.get(i).getMenuName());
                                        titleBean.setType(module_title);
                                        titlelist.add(titleBean);
                                    }
                                }
                            }
                            EventBus.getDefault().post(titlelist);
                            stopSelf();
                            break;
                        case Constants.TITLE_OTHER:
                            /*String[] nears = getResources().getStringArray(R.array.near_list);*/
                            ArrayList<TitleBean> titlelist1 = new ArrayList<>();
                            /*for (int i = 0; i < nears.length; i++) {
                                TitleBean titleBean = new TitleBean();
                                titleBean.setId(i + 0x1000);
                                titleBean.setName(nears[i]);
                                titleBean.setType(module_title);
                                titlelist.add(titleBean);
                            }*/
                            if (serviceList != null) {
                                for (int i = 0; i < serviceList.size(); i++) {
                                    if(serviceList.get(i).getDisplay() == 1) {
                                        TitleBean titleBean = new TitleBean();
                                        titleBean.setId(i + 0x1000);
                                        titleBean.setName(serviceList.get(i).getMenuName());
                                        titleBean.setType(module_title);
                                        titlelist1.add(titleBean);
                                    }
                                }
                            }
                            EventBus.getDefault().post(titlelist1);
                            stopSelf();
                            break;
                        default:
                            ArrayList<TitleBean> list = new ArrayList<>();
                            EventBus.getDefault().post(list);
                            stopSelf();
                            break;

                    }
                }
            }
        }

        return START_STICKY;
    }

    //标题类型处理类
    private class ClassificationListener implements IPresenterBase<List<MovieClassificationBean>> {

        //成功获取标题进行一系列初始化
        @Override
        public void onSuccess(BaseModel<List<MovieClassificationBean>> dataList) {
            List<MovieClassificationBean> data = dataList.data;
            MovieClassificationBean bean = new MovieClassificationBean();
            bean.setId(99);
            bean.setName(getString(R.string.hot));
            data.add(0, bean);
            ArrayList<TitleBean> titleList = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                TitleBean titleBean = new TitleBean();
                titleBean.setId(data.get(i).getId());
                titleBean.setName(data.get(i).getName());
                titleBean.setType(module_title);
                titleList.add(titleBean);
            }
            EventBus.getDefault().post(titleList);
            stopSelf();
        }

        @Override
        public void onError() {
            List<TitleBean> dataList = new ArrayList<>();
            EventBus.getDefault().post(dataList);
            stopSelf();
        }
    }

    private class HotelFoodListener implements IPresenterBase<List<HotelFoodBean>> {

        @Override
        public void onSuccess(BaseModel<List<HotelFoodBean>> dataList) {
            List<HotelFoodBean> data = dataList.data;
            ArrayList<TitleBean> titleList = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                TitleBean titleBean = new TitleBean();
                titleBean.setId(data.get(i).getId());
                titleBean.setName(data.get(i).getName());
                titleBean.setType(module_title);
                titleList.add(titleBean);
            }
            EventBus.getDefault().post(titleList);
            stopSelf();
        }

        @Override
        public void onError() {
            List<TitleBean> data = new ArrayList<>();
            EventBus.getDefault().post(data);
            stopSelf();
        }
    }

}
