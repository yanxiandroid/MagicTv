package com.yht.iptv.view;

import android.content.Intent;
import android.os.Bundle;
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
import com.yht.iptv.model.HotelGeneralInfo;
import com.yht.iptv.model.MainUIModel;
import com.yht.iptv.presenter.HotelGeneralPresenter;
import com.yht.iptv.tools.FixedSpeedScroller;
import com.yht.iptv.utils.ShowImageUtils;
import com.yht.iptv.utils.ToastUtils;
import com.yht.iptv.view.hotel.HotelShowActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/3/28.
 * 后台通用的页面处理
 */

public class GeneralActivity extends BaseActivity implements ViewPager.OnPageChangeListener, IPresenterBase<List<HotelGeneralInfo>> {

    private ViewPager viewPager;
    private TextView tv_title;
    private TextView tv_detail;
    private TextView tv_page;
    private TextView tv_play;
    private ImageView iv_left_arrow;
    private ImageView iv_right_arrow;
    private List<View> views;
    private List<HotelGeneralInfo> generalInfos;
    private FixedSpeedScroller mScroller;
    private MainUIModel.SecServiceListBean mainUIBeenList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hotel_general);

        views = new ArrayList<>();
        generalInfos = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        tv_page = (TextView) findViewById(R.id.tv_page);
        tv_play = (TextView) findViewById(R.id.tv_play);
        iv_left_arrow = (ImageView) findViewById(R.id.iv_left_arrow);
        iv_right_arrow = (ImageView) findViewById(R.id.iv_right_arrow);

        mainUIBeenList = getIntent().getParcelableExtra("mainUIBeenList");

        if(mainUIBeenList == null){
            ToastUtils.showShort("服务器配置出错!");
            finish();
            return;
        }
        HotelGeneralPresenter presenter = new HotelGeneralPresenter(this);
        presenter.request(this, mainUIBeenList.getApi(),mainUIBeenList.getId());

    }


    private void initViewPager() {
        for (int i = 0; i < generalInfos.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            try {
                ShowImageUtils.showImageView(this, generalInfos.get(i).getFileUpload().getPath(), imageView);
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

    private void initData(int position) {
        try {
            tv_page.setText((position + 1) + " / " + viewPager.getAdapter().getCount());
            tv_page.setVisibility(View.VISIBLE);
            tv_title.setText(generalInfos.get(position).getName());
            tv_title.setSelected(true);
            tv_detail.setText("\u3000\u3000" + generalInfos.get(position).getDetails());
            if (generalInfos.get(position).getAttachments() == null || generalInfos.get(position).getAttachments().getFilenameindisk() == null || generalInfos.get(position).getAttachments().getFilenameindisk().equals("")) {
                tv_play.setVisibility(View.INVISIBLE);
            } else {
                tv_play.setVisibility(View.VISIBLE);
            }
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
        try {
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                if (tv_play != null && tv_play.getVisibility() == View.VISIBLE) {
                    //播放视频
                    Intent intent = new Intent(this, HotelShowActivity.class);
                    intent.putExtra("hotel_introduce", generalInfos.get(viewPager.getCurrentItem()).getAttachments().getFilenameindisk());
                    startActivity(intent);
                    overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
                    return true;
                }
            }
        } catch (Exception e) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
            finish();
            overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
        }
        return super.onKeyDown(keyCode, event);
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

    @Override
    public void onSuccess(BaseModel<List<HotelGeneralInfo>> dataList) {
        List<HotelGeneralInfo> data = dataList.data;
        generalInfos.clear();
        generalInfos.addAll(data);
        initViewPager();
    }

    @Override
    public void onError() {
        ToastUtils.showShort("数据为空!");
        finish();
    }
}
