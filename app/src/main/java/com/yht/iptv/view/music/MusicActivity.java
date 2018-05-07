package com.yht.iptv.view.music;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.MusicTypeBean;
import com.yht.iptv.model.PictureAdInfo;
import com.yht.iptv.presenter.AdRecordPresenter;
import com.yht.iptv.presenter.MusicTypePresenter;
import com.yht.iptv.presenter.PictureAdPresenter;
import com.yht.iptv.service.PageRecordService;
import com.yht.iptv.tools.FixedSpeedScroller;
import com.yht.iptv.utils.AnimationUtils;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.OkHttpUtils;
import com.yht.iptv.utils.SPUtils;
import com.yht.iptv.utils.ServiceUtils;
import com.yht.iptv.utils.ShowImageUtils;
import com.yht.iptv.view.BaseActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/2/23.
 * 音乐鉴赏
 */
public class MusicActivity extends BaseActivity implements IPresenterBase<List<MusicTypeBean>>, TabLayout.OnTabSelectedListener, View.OnFocusChangeListener, MusicAnimListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView iv_music_play_status;
    private ImageView iv_music_play;
    private ObjectAnimator objectAnimator;
    private long currentPlayTime;
    private boolean isPause;
    private FixedSpeedScroller mScroller;
    private final int ADVERT_DELAY = 1;
    private List<String> advertList;
    private int advertPosition;
    private ImageView[] imageViews;
    private TextView tv_advert_msg;
    private List<Integer> advertIds;
    private String roomId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_START);
        bundle.putString("behaviour", Constants.MUSIC);
        ServiceUtils.startService(PageRecordService.class, bundle);

        setContentView(R.layout.activity_music);
        advertPosition = 0;

        ImageView iv_bg = (ImageView) findViewById(R.id.iv_bg);
        imageViews = new ImageView[2];
        imageViews[0] = (ImageView) findViewById(R.id.iv_advert);
        imageViews[1] = (ImageView) findViewById(R.id.iv_advert2);
        tv_advert_msg = (TextView) findViewById(R.id.tv_advert_msg);
        advertList = new ArrayList<>();
        advertIds = new ArrayList<>();
        //设置背景
        ShowImageUtils.showImageView(this, R.drawable.music_bg, iv_bg);

        //注册监听服务通知的接口
        MusicAnimManger.getInstance().setMusicStatusListener(this);

        roomId = (String) SPUtils.get(this, Constants.ROOM_ID, "");

        try {
            if (Constants.mainPageInfo.getAdSetting().getMusicPlayImage() == 1) {
                //请求广告信息
                PictureAdPresenter pictureAdPresenter = new PictureAdPresenter(this, new AdvertPhotoListener());
                pictureAdPresenter.request(this, 3, roomId);
            } else {
                ShowImageUtils.showImageViewTrans(MusicActivity.this, Constants.mainPageInfo.getAdSetting().getMusicPlayImageFile().getPath(), imageViews[0]);
            }
        } catch (Exception e) {
            ShowImageUtils.showImageViewTrans(MusicActivity.this, R.drawable.photo_failed, imageViews[0]);
        }


        //网络请求获取音乐种类
        MusicTypePresenter musicTypePresenter = new MusicTypePresenter(this);
        musicTypePresenter.request();


        LinearLayout hotel_title_view = (LinearLayout) findViewById(R.id.hotel_title_view);
        ImageView iv_icon = (ImageView) hotel_title_view.findViewById(R.id.iv_icon);
        TextView tv_title_name = (TextView) hotel_title_view.findViewById(R.id.tv_title_name);
        iv_icon.setImageResource(R.drawable.music_main_icon);
        tv_title_name.setText(R.string.main_music_play);

        RelativeLayout ll_toast = (RelativeLayout) findViewById(R.id.ll_toast);
        if (ll_toast != null) {
            LinearLayout ll_more = (LinearLayout) ll_toast.findViewById(R.id.ll_more);
            TextView tv_apply = (TextView) ll_toast.findViewById(R.id.tv_apply);
            ll_more.setVisibility(View.GONE);
            tv_apply.setText(R.string.pause_play);
        }

        iv_music_play_status = (ImageView) findViewById(R.id.iv_music_play_status);
        iv_music_play = (ImageView) findViewById(R.id.iv_music_play);
        ShowImageUtils.showImageView(this, R.drawable.music_play_icon, iv_music_play);
        ShowImageUtils.showImageView(this, R.drawable.music_play_status, iv_music_play_status);
        //动画的默认值暂停
        objectAnimator = AnimationUtils.rotateAnimation(iv_music_play, ValueAnimator.INFINITE, 10000, 0f, 359f);
        AnimationUtils.animatorPause(objectAnimator);
        AnimationUtils.rotateAnimation(iv_music_play_status, 0, 0, getDimension(R.dimen.w_234), getDimension(R.dimen.h_50), 0f, -15f);
    }

    private void initTab(List<MusicTypeBean> musicTypeBeanList) {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < musicTypeBeanList.size(); i++) {
            Bundle args = new Bundle();
            args.putInt("currentPage", i);
            args.putParcelableArrayList("musicTypeBeanList", (ArrayList<? extends Parcelable>) musicTypeBeanList);
            MusicTabFragment fragment = new MusicTabFragment();
            fragment.setArguments(args);
            fragments.add(fragment);
        }
        MusicFragmentPagerAdapter adapter = new MusicFragmentPagerAdapter(getSupportFragmentManager(), this, musicTypeBeanList, fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(musicTypeBeanList.size());
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(viewPager.getContext(), new DecelerateInterpolator());
            mField.set(viewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewPager.setCurrentItem(0);
        mScroller.setmDuration(300);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(adapter.getTabView(i));
            }
            //((View)tabLayout.getTabAt(0).getCustomView().getParent()).setFocusable(false);

        }
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null && tab.getCustomView() != null) {
                ((View) tab.getCustomView().getParent()).setTag(tab.getPosition());
                ((View) tab.getCustomView().getParent()).setOnFocusChangeListener(this);
                ((View) tab.getCustomView().getParent()).setId(musicTypeBeanList.get(i).getId());
                //设置第0个tablayout的背景
                if (i == 0) {
                    TextView textView = (TextView) tab.getCustomView().findViewById(R.id.textView);
                    textView.setBackgroundResource(R.drawable.music_type_selected);
                }

            }
        }

        //viewpager获取焦点
        viewPager.requestFocus();

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


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == ADVERT_DELAY) {
                advertPosition++;
                imageViews[advertPosition % imageViews.length].bringToFront();
                ShowImageUtils.showImageViewTrans(MusicActivity.this, advertList.get(advertPosition % advertList.size()), imageViews[advertPosition % imageViews.length]);
                tv_advert_msg.bringToFront();
                tv_advert_msg.setText(R.string.advert);
                tv_advert_msg.setVisibility(View.VISIBLE);

                try {
                    if (Constants.mainPageInfo.getAdSetting().getMusicPlayImage() == 1) {
                        AdRecordPresenter adRecordPresenter = new AdRecordPresenter();
                        adRecordPresenter.request(this, advertIds.get(advertPosition % advertIds.size()), 0, roomId, 3, 0);
                        handler.removeMessages(ADVERT_DELAY);
                        handler.sendEmptyMessageDelayed(ADVERT_DELAY, 10000);
                    } else {
                        ShowImageUtils.showImageViewTrans(MusicActivity.this, Constants.mainPageInfo.getAdSetting().getMusicPlayImageFile().getPath(), imageViews[0]);
                    }
                } catch (Exception e) {
                    ShowImageUtils.showImageViewTrans(MusicActivity.this, R.drawable.photo_failed, imageViews[0]);
                }

            }
        }
    };


    @Override
    public void onSuccess(BaseModel<List<MusicTypeBean>> dataList) {
        //初始化tab
        if (dataList.data.size() >= 0) {
            initTab(dataList.data);
        } else {
//            ToastUtils.showToast(this, R.string.net_conn_error);
            finish();
            overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
        }
    }

    @Override
    public void onError() {
//        ToastUtils.showToast(this, R.string.net_conn_error);
        finish();
        overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //改变字体的颜色
        if (tab.getCustomView() != null) {

            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TextView textView = (TextView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.textView);
                if (tab.getPosition() == i) {
                    textView.setBackgroundResource(R.drawable.music_type_selected);
                } else {
                    textView.setBackgroundResource(R.color.transparent);
                }
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            int position = (int) v.getTag();
            if (viewPager != null) {
                viewPager.setCurrentItem(position);
            }
        }
    }

    @Override
    protected void onResume() {
        isPause = false;
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, MusicService.class);
        bundle.putInt("type", Constants.AUDIO_STATUS);
        bundle.putBoolean("status", false);
        intent.putExtras(bundle);
        startService(intent);
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e("dispathch", "onPause");
        isPause = true;
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, MusicService.class);
        bundle.putInt("type", Constants.AUDIO_STATUS);
        bundle.putBoolean("status", true);
        intent.putExtras(bundle);
        startService(intent);
        super.onPause();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        Log.e("dispathch", "key");

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            //enterAppManger(event);
        }

        if (isPause) {
            return true;
        }

        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (viewPager != null) {
                if (viewPager.getCurrentItem() == 0) {
                    //拦截第0个条目
                    return true;
                }
            }
        }

        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            if (getCurrentFocus() instanceof ListView) {
                if (((ListView) getCurrentFocus()).getSelectedItemPosition() == 0) {
                    return true;
                }
            }
        }


        return super.dispatchKeyEvent(event);
    }

    @Override
    public void finish() {
        super.finish();
        //关闭服务
        Intent intent = new Intent(this, MusicService.class);
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
//        recordEnd();
        super.onDestroy();
        if (MusicAnimManger.getInstance() != null) {
            MusicAnimManger.getInstance().releaseRes();
        }
        OkHttpUtils.cancel();
        viewPager = null;
        tabLayout = null;

        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_END);
        ServiceUtils.startService(PageRecordService.class, bundle);
        handler.removeCallbacks(runnable);
        handler.removeMessages(ADVERT_DELAY);
        handler.removeCallbacksAndMessages(null);

    }

    //监听音乐动画状态
    @Override
    public void onStatus(boolean isPause) {
        if (isPause) {
            //暂停
            if (objectAnimator.isRunning()) {
                currentPlayTime = AnimationUtils.animatorPause(objectAnimator);
                AnimationUtils.rotateAnimation(iv_music_play_status, 0, 300, getDimension(R.dimen.w_234), getDimension(R.dimen.h_50), 0f, -15f);
            }
        } else {
            if (!objectAnimator.isRunning()) {
                AnimationUtils.animatorResume(objectAnimator, currentPlayTime);
                AnimationUtils.rotateAnimation(iv_music_play_status, 0, 300, getDimension(R.dimen.w_234), getDimension(R.dimen.h_50), -15f, 0f);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    private class AdvertPhotoListener implements IPresenterBase<List<PictureAdInfo>> {

        @Override
        public void onSuccess(BaseModel<List<PictureAdInfo>> dataList) {

            try {
                List<PictureAdInfo> pictureAdInfos = dataList.data;
                for (int i = 0; i < pictureAdInfos.size(); i++) {
                    advertList.add(pictureAdInfos.get(i).getFileUpload().getPath());
                    advertIds.add(pictureAdInfos.get(i).getId());
                }
                handler.removeMessages(ADVERT_DELAY);
                handler.sendEmptyMessageDelayed(ADVERT_DELAY, 50);


            } catch (Exception e) {

            }

        }

        @Override
        public void onError() {

        }
    }
}
