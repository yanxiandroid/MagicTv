package com.yht.iptv.view.hotel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;
import com.yht.iptv.R;
import com.yht.iptv.callback.IDialogClick;
import com.yht.iptv.tools.CustomDialog;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.DialogUtils;
import com.yht.iptv.utils.ToastUtils;
import com.yht.iptv.view.BaseActivity;

public class HotelShowActivity extends BaseActivity implements PLMediaPlayer.OnErrorListener, PLMediaPlayer.OnCompletionListener, PLMediaPlayer.OnInfoListener {

    private PLVideoView mVideoView;
    public static HotelShowActivity instance;
    private boolean isKeyEnable;
    private String videoPath;
    private View loadingView;
    private static final int VIDEO_TIMEOUT = 5;
    private final long video_timeout = 20 * 1000;
    private CustomDialog normalDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hotel_show);

        isKeyEnable = false;

        instance = this;

        Bundle bundle = getIntent().getExtras();
        videoPath = bundle.getString("hotel_introduce");

        mVideoView = (PLVideoView) findViewById(R.id.VideoView);
        loadingView = findViewById(R.id.LoadingView);
        mVideoView.setBufferingIndicator(loadingView);

        //播放设置
        playerOptions();

        //播放失败监听
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnInfoListener(this);

        mVideoView.setVideoPath(videoPath);

        mHandler.sendEmptyMessageDelayed(VIDEO_TIMEOUT,video_timeout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
        mHandler.removeMessages(VIDEO_TIMEOUT);
        mHandler.removeCallbacksAndMessages(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mVideoView.stopPlayback();
            }
        }).start();
    }

    /**
     * 播放器设置
     */
    private void playerOptions() {
        AVOptions options = new AVOptions();

        // 准备超时时间，包括创建资源、建立连接、请求码流等，单位是 ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);

        // 默认的缓存大小，单位是 ms
        // 默认值是：2000
        options.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 2000);

        // 最大的缓存大小，单位是 ms
        // 默认值是：4000
        options.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 4000);

        //解码方式为硬件解码
        // 解码方式，codec＝1，硬解; codec=0, 软解
        options.setInteger(AVOptions.KEY_MEDIACODEC, 1);

        //设置地址自动播放
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 1);

        // 请在开始播放之前配置
        mVideoView.setAVOptions(options);
    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int errorCode) {
        if(plMediaPlayer.getCurrentPosition() == 0){
            ToastUtils.showShort("网络连接超时....");
            finish();
            overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
        }
        long currentPosition = plMediaPlayer.getCurrentPosition();
        //设置加载显示加载框
        mVideoView.setVideoPath(videoPath);
        loadingView.setVisibility(View.VISIBLE);
        mVideoView.seekTo(currentPosition);
        mVideoView.start();
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        if(isKeyEnable) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                exitDialog();
                return true;
            }
            return super.dispatchKeyEvent(event);
//        }
//        return true;
    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {
        finish();
        overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
    }

    @Override
    public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
        //缓冲
        if(what == PLMediaPlayer.MEDIA_INFO_BUFFERING_START){
            mHandler.removeMessages(VIDEO_TIMEOUT);
            mHandler.sendEmptyMessageDelayed(VIDEO_TIMEOUT,video_timeout);
        }
        //缓冲结束
        if(what == PLMediaPlayer.MEDIA_INFO_BUFFERING_END){
            mHandler.removeMessages(VIDEO_TIMEOUT);
        }
        //第一帧已经开始渲染了
        if (what == PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {

            isKeyEnable = true;
            mHandler.removeMessages(VIDEO_TIMEOUT);
        }
        return false;
    }


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case VIDEO_TIMEOUT:
                    ToastUtils.showShort("获取视频信息超时");
                    HotelShowActivity.this.finish();
                    overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
                    break;
            }
        }
    };

    private void exitDialog() {
        normalDialog = new CustomDialog(this, new IDialogClick() {
            @Override
            public void onClick(CustomDialog dialog, String tag) {
                if (tag.equals(Constants.CONFIRM)) {
                    DialogUtils.dismissDialog(dialog);
                    finish();
                    overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
                } else if (tag.equals(Constants.CANCEL)) {
                    DialogUtils.dismissDialog(dialog);
                }
            }
        }, R.string.exit_play_toast);
        DialogUtils.showDialog(normalDialog);
    }
}
