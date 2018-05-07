package com.yht.iptv.view.near;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.adapter.GeneralPageAdapter;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.NearGeneralBean;
import com.yht.iptv.presenter.NearGeneralPresenter;
import com.yht.iptv.service.PageRecordService;
import com.yht.iptv.tools.FixedSpeedScroller;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.ServiceUtils;
import com.yht.iptv.utils.ShowImageUtils;
import com.yht.iptv.utils.ToastUtils;
import com.yht.iptv.view.BaseActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 酒店周边
 * Created by Q on 2018/1/17.
 */

public class NearGeneralActivty extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private TextView tv_title;
    private TextView tv_detail;
    private TextView tv_page;
    private TextView tv_play;
    private ImageView iv_left_arrow;
    private ImageView iv_right_arrow;
    private List<View> views;
    private List<NearGeneralBean> nearGeneralInfos;
    private FixedSpeedScroller mScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_general);

        views = new ArrayList<>();
        nearGeneralInfos = new ArrayList<>();

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        tv_page = (TextView) findViewById(R.id.tv_page);
        tv_play = (TextView) findViewById(R.id.tv_play);
        iv_left_arrow = (ImageView) findViewById(R.id.iv_left_arrow);
        iv_right_arrow = (ImageView) findViewById(R.id.iv_right_arrow);

        int position = getIntent().getIntExtra("position", 0) - 10;

        String[] arrays = {HttpConstants.NEAR_PLACES, HttpConstants.NEAR_FOOD, HttpConstants.NEAR_SHOP, HttpConstants.NEAR_SPECIALTY};
        String[] pages = {Constants.PLACES, Constants.FOOD, Constants.SHOP, Constants.SPECIALTY};
        if (position > arrays.length) {
            ToastUtils.showShort("操作不符");
            finish();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_START);
        bundle.putString("behaviour", pages[position]);
        ServiceUtils.startService(PageRecordService.class, bundle);

        NearGeneralPresenter presenter = new NearGeneralPresenter(new NearGeneralListener());
        presenter.request(this, arrays[position]);
    }


    private class NearGeneralListener implements IPresenterBase<List<NearGeneralBean>> {

        @Override
        public void onSuccess(BaseModel<List<NearGeneralBean>> dataList) {
            List<NearGeneralBean> data = dataList.data;
            if (data != null && data.size() != 0) {
                nearGeneralInfos.clear();
                nearGeneralInfos.addAll(data);
                initViewPager();
            } else {
                finish();
                overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
            }

        }

        @Override
        public void onError() {
            finish();
            overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 60 * 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    Handler handler = new Handler();


    private void initViewPager() {
        for (int i = 0; i < nearGeneralInfos.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            try {
                ShowImageUtils.showImageView(this, nearGeneralInfos.get(i).getFileUpload().getPath(), imageView);
                views.add(imageView);
            } catch (Exception e) {
                ShowImageUtils.showImageView(this, R.drawable.photo_loading_failed, imageView);
                views.add(imageView);
            }
        }
        GeneralPageAdapter pageAdapter = new GeneralPageAdapter(views);
        viewPager.setAdapter(pageAdapter);
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(viewPager.getContext(), new DecelerateInterpolator());
            mField.set(viewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewPager.setCurrentItem(0);
        mScroller.setmDuration(500);
        viewPager.addOnPageChangeListener(this);
        initData(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        initData(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void initData(int position) {
        try {
            tv_page.setText((position + 1) + " / " + viewPager.getAdapter().getCount());
            tv_page.setVisibility(View.VISIBLE);
            tv_title.setText(nearGeneralInfos.get(position).getName());
            tv_title.setSelected(true);
            tv_detail.setText("\u3000\u3000" + nearGeneralInfos.get(position).getDetails());
            tv_play.setVisibility(View.INVISIBLE);
            if (viewPager.getAdapter().getCount() <= 1) {
                iv_left_arrow.setVisibility(View.INVISIBLE);
                iv_right_arrow.setVisibility(View.INVISIBLE);
            } else {
                if (position == 0) {
                    iv_left_arrow.setVisibility(View.INVISIBLE);
                    iv_right_arrow.setVisibility(View.VISIBLE);
                } else if (position == viewPager.getAdapter().getCount() - 1) {
                    iv_left_arrow.setVisibility(View.VISIBLE);
                    iv_right_arrow.setVisibility(View.INVISIBLE);
                } else {
                    iv_left_arrow.setVisibility(View.VISIBLE);
                    iv_right_arrow.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
            finish();
            overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}
