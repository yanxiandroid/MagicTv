package com.yht.iptv.view.near;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.adapter.BusViewpagerListAdapter;
import com.yht.iptv.adapter.GeneralPageAdapter;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.BusTimetableBean;
import com.yht.iptv.presenter.BusTimetablePresenter;
import com.yht.iptv.service.PageRecordService;
import com.yht.iptv.tools.CustomListViewTv;
import com.yht.iptv.tools.FixedSpeedScroller;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.OkHttpUtils;
import com.yht.iptv.utils.ServiceUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 大巴时刻表
 * Created by Q on 2018/1/16.
 */

public class BusTimetableFragment extends DialogFragment implements IPresenterBase<List<BusTimetableBean>>, DialogInterface.OnKeyListener, ViewPager.OnPageChangeListener {
    private Context mContext;
    private List<View> views;
    private ViewPager view_pager;
    private List<BusTimetableBean> busTimetableBeanList;
    private LayoutInflater inflater;
    private LinearLayout nothing_ll;
    private TextView nothing_text;
    private LinearLayout ll_title;
    private TextView tv_bus_page;
    private BusTimetablePresenter presenter;
    private FixedSpeedScroller mScroller;
    private static final int NORMAL_VALUE = 5;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        views = new ArrayList<>();
        busTimetableBeanList = new ArrayList<>();

        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_START);
        bundle.putString("behaviour", Constants.BUS);
        ServiceUtils.startService(PageRecordService.class, bundle);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_bus_timetable, null);
        this.inflater = LayoutInflater.from(mContext);
        view_pager = (ViewPager) view.findViewById(R.id.view_pager);
        nothing_ll = (LinearLayout) view.findViewById(R.id.nothing_ll);
        ll_title = (LinearLayout) view.findViewById(R.id.ll_title);
        nothing_text = (TextView) view.findViewById(R.id.nothing_text);
        tv_bus_page = (TextView) view.findViewById(R.id.tv_bus_page);

        presenter = new BusTimetablePresenter(this);
        presenter.request(this, "", "");

        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setView(view);
        return dialog.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        getDialog().setOnKeyListener(this);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        params.width = (int) getResources().getDimension(R.dimen.w_1800);
        params.height = (int) getResources().getDimension(R.dimen.w_558);
        params.y = (int) getResources().getDimension(R.dimen.h_166);
        window.setAttributes(params);
        window.setBackgroundDrawableResource(R.color.transparent);
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 60 * 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.cancel(this);
        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_END);
        ServiceUtils.startService(PageRecordService.class, bundle);
    }

    @Override
    public void onSuccess(BaseModel<List<BusTimetableBean>> dataList) {
        nothing_ll.setVisibility(View.GONE);
        List<BusTimetableBean> dataLists = dataList.data;
        busTimetableBeanList.clear();
        busTimetableBeanList.addAll(dataLists);
        initViewPager();
        initListView();
    }

    @Override
    public void onError() {
        if (nothing_ll != null && nothing_text != null && ll_title != null) {
            ll_title.setVisibility(View.GONE);
            nothing_ll.setVisibility(View.VISIBLE);
            nothing_text.setText(getResources().getString(R.string.phone_nothing));
        }
    }


    private void initViewPager() {
        for (int i = 0; i < totalViewPagers(); i++) {
            View view = inflater.inflate(R.layout.viewpager_bus_timetable, null);
            views.add(view);
        }
        GeneralPageAdapter pageAdapter = new GeneralPageAdapter(views);
        view_pager.setAdapter(pageAdapter);
        view_pager.addOnPageChangeListener(this);
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(view_pager.getContext(), new LinearInterpolator());
            mField.set(view_pager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        view_pager.setCurrentItem(0);
        mScroller.setmDuration(0);
        tv_bus_page.setText("< " + (view_pager.getCurrentItem() + 1) + " / " + "" + view_pager.getAdapter().getCount() + " >");
    }

    private void initListView() {
        for (int i = 0; i < views.size(); i++) {
            CustomListViewTv listViewTv = (CustomListViewTv) views.get(i).findViewById(R.id.listViewTv);
            BusViewpagerListAdapter adapter = new BusViewpagerListAdapter(mContext, getViewpagerListSize().get(i));
            listViewTv.setAdapter(adapter);
        }
    }

    /**
     * 返回viewpager lit数据总页数
     */
    private int totalViewPagers() {
        if (busTimetableBeanList.size() != 0) {

            //以4个一行为基准
            int totalSize;

            int shi = busTimetableBeanList.size() / NORMAL_VALUE;
            int ge = busTimetableBeanList.size() % NORMAL_VALUE;
            if (ge == 0) {
                totalSize = shi;
            } else {
                totalSize = shi + 1;
            }

            return totalSize;
        } else {
            return 0;
        }
    }

    private List<List<BusTimetableBean>> getViewpagerListSize() {
        List<List<BusTimetableBean>> list = new ArrayList<>();
        List<BusTimetableBean> list2;
        for (int i = 0; i < totalViewPagers(); i++) {
            list2 = new ArrayList<>();
            if (busTimetableBeanList.size() > NORMAL_VALUE * (i + 1)) {
                for (int j = NORMAL_VALUE * i; j < NORMAL_VALUE * i + NORMAL_VALUE; j++) {
                    list2.add(busTimetableBeanList.get(j));
                }
            } else {
                for (int k = NORMAL_VALUE * i; k < busTimetableBeanList.size(); k++) {
                    list2.add(busTimetableBeanList.get(k));
                }
            }
            list.add(list2);
        }
        return list;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (tv_bus_page != null) {
            tv_bus_page.setText("< " + (position + 1) + " / " + view_pager.getAdapter().getCount() + " >");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Bundle bundle = new Bundle();
            bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_END);
            ServiceUtils.startService(PageRecordService.class, bundle);
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 60 * 1000);
        }
    };


    Handler handler = new Handler();

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        return false;
    }
}
