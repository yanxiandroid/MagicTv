package com.yht.iptv.view.near;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yht.iptv.R;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.MetroMapBean;
import com.yht.iptv.presenter.MetroMapPresenter;
import com.yht.iptv.service.PageRecordService;
import com.yht.iptv.tools.ZoomImageView;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.ServiceUtils;
import com.yht.iptv.view.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 地铁线路图页面
 */
public class ImageDetailsActivity extends BaseActivity implements IPresenterBase<List<MetroMapBean>> {

    /**
     * 自定义的ImageView控制，可对图片进行多点触控缩放和拖动
     */
    private static final int DELAY = 0;
    /**
     * 待展示的图片
     */
    private MyHandler myHandler;
    private FrameLayout ll_zoom;
    private ZoomImageView zoomImageView;
    private boolean isLoadCompled;

    private MetroMapPresenter mapPresenter;
    private List<MetroMapBean> metroMapBeanList;
    private LinearLayout nothing_ll;
    private TextView nothing_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_START);
        bundle.putString("behaviour", Constants.METROMAP);
        ServiceUtils.startService(PageRecordService.class, bundle);
        setContentView(R.layout.activity_image_zoom);
        mapPresenter = new MetroMapPresenter(this);
        mapPresenter.request(this);
        isLoadCompled = false;
        ll_zoom = (FrameLayout) findViewById(R.id.ll_zoom);
        nothing_ll = (LinearLayout) findViewById(R.id.nothing_ll);
        nothing_text = (TextView) findViewById(R.id.nothing_text);
        myHandler = new MyHandler(this);

    }

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_END);
        ServiceUtils.startService(PageRecordService.class, bundle);
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
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (isLoadCompled) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {

                zoomImageView.onKeyDown(event.getKeyCode(), event);
                return true;
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {

                zoomImageView.onKeyDown(event.getKeyCode(), event);

                return true;
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {

                zoomImageView.onKeyDown(event.getKeyCode(), event);

                return true;
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {

                zoomImageView.onKeyDown(event.getKeyCode(), event);

                return true;
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                zoomImageView.onKeyDown(event.getKeyCode(), event);

                return true;
            }
        }
        if (event.getAction() == KeyEvent.KEYCODE_BACK || event.getAction() == KeyEvent.KEYCODE_ESCAPE) {
            finish();
            overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public void onSuccess(BaseModel<List<MetroMapBean>> dataList) {
        if (dataList.data == null || dataList.data.size() <= 0 || dataList.data.get(0) == null) {
            nothing_ll.setVisibility(View.VISIBLE);
            nothing_ll.setBackgroundResource(R.drawable.muti_screen_bg);
            nothing_text.setText(getResources().getString(R.string.no_data));
            return;
        }
        metroMapBeanList = dataList.data;
        if (metroMapBeanList.get(0).getFileUpload() != null) {
            nothing_ll.setVisibility(View.GONE);
            Glide.with(this).load(metroMapBeanList.get(0).getFileUpload().getPath()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    isLoadCompled = true;
                    Message message = Message.obtain();
                    message.what = DELAY;
                    message.obj = resource;
                    myHandler.sendMessageDelayed(message, 10);
                }

            }); //方法中设置asBitmap可以设置回调类型
        } else {
            nothing_ll.setVisibility(View.VISIBLE);
            nothing_ll.setBackgroundResource(R.drawable.muti_screen_bg);
            nothing_text.setText(getResources().getString(R.string.no_data));
        }
    }

    @Override
    public void onError() {
        finish();
        overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
    }

    private static class MyHandler extends Handler {

        WeakReference<ImageDetailsActivity> mWeakReferenceActivity;

        MyHandler(ImageDetailsActivity activity) {
            mWeakReferenceActivity = new WeakReference<>(activity);
        }


        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case DELAY:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if (null != mWeakReferenceActivity) {
                        ImageDetailsActivity activity = mWeakReferenceActivity.get();
                        activity.delayDisplay(bitmap);
                    }
                    break;
            }

        }
    }

    private void delayDisplay(Bitmap bitmap) {
        zoomImageView = new ZoomImageView(this);
        FrameLayout.LayoutParams mLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        ll_zoom.addView(zoomImageView, mLayoutParams);
        zoomImageView.setImageBitmap(bitmap);
    }

}