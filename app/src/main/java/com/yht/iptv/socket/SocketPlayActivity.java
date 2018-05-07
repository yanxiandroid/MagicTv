package com.yht.iptv.socket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.lzy.okgo.OkGo;
import com.open.androidtvwidget.utils.NetWorkUtils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;
import com.yht.iptv.R;
import com.yht.iptv.callback.IDialogClick;
import com.yht.iptv.callback.IDialogListener;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.IPresenterDownloadBase;
import com.yht.iptv.model.AdvertInfo;
import com.yht.iptv.model.AdvertVideoInfo;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.MainPageInfo;
import com.yht.iptv.model.MoviePayStatus;
import com.yht.iptv.model.SocketInfo;
import com.yht.iptv.model.VideoPayBean;
import com.yht.iptv.presenter.AdRecordPresenter;
import com.yht.iptv.presenter.AdvertDownloadPresenter;
import com.yht.iptv.presenter.VideoPayPresenter;
import com.yht.iptv.service.PageRecordService;
import com.yht.iptv.tools.AdvertDialogFragment;
import com.yht.iptv.tools.CustomDialog;
import com.yht.iptv.tools.InstallDialog;
import com.yht.iptv.tools.MediaChargeDialog;
import com.yht.iptv.tools.MyMediaController;
import com.yht.iptv.tools.PayDialog;
import com.yht.iptv.tools.PayMentChargeDialog;
import com.yht.iptv.utils.AppManagerUtils;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.DBUtils;
import com.yht.iptv.utils.DialogUtils;
import com.yht.iptv.utils.FileUtils;
import com.yht.iptv.utils.LanguageUtils;
import com.yht.iptv.utils.QrCodeCreate;
import com.yht.iptv.utils.SPUtils;
import com.yht.iptv.utils.ServiceUtils;
import com.yht.iptv.utils.ToastUtils;
import com.yht.iptv.view.BaseActivity;
import com.yht.iptv.view.hotel.HotelShowActivity;
import com.yht.iptv.view.movie.PlayVideoActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SocketPlayActivity extends BaseActivity implements PLMediaPlayer.OnCompletionListener, PLMediaPlayer.OnInfoListener, PLMediaPlayer.OnBufferingUpdateListener, PLMediaPlayer.OnErrorListener, OnSocketListener {

    private PLVideoView mVideoView;
    private TextView tv_videoName;
    private TextView tv_currentTime;
    private ImageView iv_pause;
    private RelativeLayout rl_video_msg;
    private static final int FORWARD = 1;
    private static final int BACK = 2;
    private static final int CURRENTTIME = 3;
    private static final int HIDE_WINDOW = 4;
    private static final int VIDEO_TIMEOUT = 5;
    private static final int MOVIE_TIME = 6;
    private static final int ADV_TIMES = 7;
    private static final int ADV_PLAY_TIME = 8;
    //    private static final int GET_STATUS = 7;
    private long nowPosition;
    private MyMediaController myMediaController;
    private boolean isRunningSeek;
    private int progressCount;
    private View loadingView;
    private SocketInfo socketInfo;
    private boolean isKeyEnable;
    private final long video_timeout = 60 * 1000;
    private CustomDialog errorDialog;
    private CustomDialog normalDialog;
    private long movieTime;
    private String roomId;
    private MainPageInfo info;
    //    private VideoPayStatusPresenter payStatusPresenter;
    private InstallDialog progressDialog;

    private long recordId;
    private PayMentChargeDialog payMentChargeDialog;
    private MediaChargeDialog mediaChargeDialog;
    private PayDialog payDialog;
    private int movieType = 0;
    private VideoPayPresenter payPresenter;
    private String media_pay_once = null;
    private String media_pay_all = null;
    private TextView tv_advert_msg;
    private int advTime;
    private boolean isAdvPlay;
    private int advertPosition;
    private int currentAdvertId = -1;
    private boolean isAdv15Play;
    private int currentTimes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (PlayVideoActivity.instance != null) {
            AppManagerUtils.getAppManager().finishActivity(PlayVideoActivity.instance);
        }
        if (HotelShowActivity.instance != null) {
            AppManagerUtils.getAppManager().finishActivity(HotelShowActivity.instance);
        }

        roomId = (String) SPUtils.get(this, Constants.ROOM_ID, "");

        info = Constants.mainPageInfo;

        setContentView(R.layout.activity_socket_play);
        //按键不可点击
        isKeyEnable = false;
        movieTime = 0;


//        String receiverMsg = getIntent().getStringExtra("socketMsg");
//
//        socketInfo = dispMovieMsg(receiverMsg);

        socketInfo = (SocketInfo) getIntent().getSerializableExtra("socketMsg");

        mVideoView = (PLVideoView) findViewById(R.id.VideoView);
        iv_pause = (ImageView) findViewById(R.id.iv_pause);
        rl_video_msg = (RelativeLayout) findViewById(R.id.rl_video_msg);
        tv_videoName = (TextView) findViewById(R.id.tv_videoName);
        tv_currentTime = (TextView) findViewById(R.id.tv_currentTime);
        tv_advert_msg = (TextView) findViewById(R.id.tv_advert_msg);
        //设置不显示进度条
        myMediaController = new MyMediaController(this);
        mVideoView.setMediaController(myMediaController);
        loadingView = findViewById(R.id.LoadingView);
        mVideoView.setBufferingIndicator(loadingView);

        //播放设置
        playerOptions();

        //设置监听
        //播放完成
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnBufferingUpdateListener(this);
        //播放失败监听
        mVideoView.setOnErrorListener(this);

        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_16_9);

        if (Constants.mainPageInfo.getAdSetting().getMediaPlayVideo30() == 1) {
            showStartAdv();
        } else {
            initData(0);
        }


        //socket视频播放监听
        SocketMsgManger.getInstance().setOnSocketListener(this);

        //设置超时处理
        mHandler.sendEmptyMessageDelayed(VIDEO_TIMEOUT, video_timeout);
        //开启视频计时
//        countTime();
        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS, Constants.MOVIE_START);
        bundle.putInt("playType", Constants.SOCKET_PLAY);
        bundle.putString("videoId", socketInfo.getVedioId());
        ServiceUtils.startService(PageRecordService.class, bundle);

    }

    /**
     * 播放器设置
     */
    private void playerOptions() {
        AVOptions options = new AVOptions();

        // 准备超时时间，包括创建资源、建立连接、请求码流等，单位是 ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);

        options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);

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
        mHandler.removeCallbacksAndMessages(null);
        OkGo.getInstance().cancelAll();
        DialogUtils.dismissDialog(normalDialog);
        DialogUtils.dismissDialog(errorDialog);
        DialogUtils.dismissDialog(payDialog);
        DialogUtils.dismissDialog(payMentChargeDialog);
        DialogUtils.dismissDialog(mediaChargeDialog);
        DialogUtils.dismissDialog(progressDialog);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mVideoView.stopPlayback();
            }
        }).start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mHandler.removeCallbacks(runnable);
        mHandler.postDelayed(runnable, 60 * 1000);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(runnable);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {

        if (isAdvPlay) {
            mHandler.removeMessages(ADV_PLAY_TIME);
            mHandler.sendEmptyMessageDelayed(ADV_PLAY_TIME, 1000);
            //隐藏广告字
            mHandler.removeMessages(ADV_TIMES);
            tv_advert_msg.setVisibility(View.INVISIBLE);
            String advertPath = FileUtils.getAdvertPath() + Constants.key[advertPosition % Constants.key.length] + ".mp4";
            AdvertDownloadPresenter presenter = new AdvertDownloadPresenter(advertPath, new MyAdvertListener(true));
            presenter.request(this, 30, roomId);

            AdvertInfo advertInfo = DBUtils.find(this, AdvertInfo.class, "id", "=", FileUtils.getAdvertPath() + Constants.key[advertPosition % Constants.key.length]);
            if (advertInfo != null) {
                currentAdvertId = advertInfo.getCurrentAdvId();
            }
            if (currentAdvertId != -1) {
                AdRecordPresenter adRecordPresenter = new AdRecordPresenter();
                adRecordPresenter.request(this, currentAdvertId, 1, roomId, 0, 30);
                LogUtils.e(currentAdvertId + "请求完成");
            }
            //设置加载显示加载框
            mVideoView.setBufferingIndicator(loadingView);
            loadingView.setVisibility(View.VISIBLE);
            if (myMediaController != null) {
                mVideoView.setMediaController(myMediaController);
                myMediaController.hide();
                myMediaController.setNormalState();
            }
            initData(0);
            isAdvPlay = false;
            isKeyEnable = false;
            return;
        }
        if (isAdv15Play) {
            if (myMediaController != null) {
                mVideoView.setMediaController(myMediaController);
                myMediaController.hide();
                myMediaController.setNormalState();
            }
            Log.e("movieAdvPosition", Constants.movieAdvPosition + "");
            initData(Constants.movieAdvPosition);
            //隐藏广告字
            mHandler.removeMessages(ADV_TIMES);
            tv_advert_msg.setVisibility(View.INVISIBLE);

            mHandler.removeMessages(ADV_PLAY_TIME);
            mHandler.sendEmptyMessageDelayed(ADV_PLAY_TIME, 1000);
            String advertPath = FileUtils.getAdvert15Path() + Constants.key[advertPosition % Constants.key.length] + ".mp4";
            AdvertDownloadPresenter presenter = new AdvertDownloadPresenter(advertPath, new MyAdvertListener(false));
            presenter.request(SocketPlayActivity.this, 15, roomId);

            AdvertInfo advertInfo = DBUtils.find(this, AdvertInfo.class, "id", "=", FileUtils.getAdvert15Path() + Constants.key[advertPosition % Constants.key.length]);
            if (advertInfo != null) {
                currentAdvertId = advertInfo.getCurrentAdvId();
            }
            if (currentAdvertId != -1) {
                AdRecordPresenter adRecordPresenter = new AdRecordPresenter();
                adRecordPresenter.request(this, currentAdvertId, 1, roomId, 0, 15);
                LogUtils.e(currentAdvertId + "请求完成");
            }
            mVideoView.setBufferingIndicator(loadingView);
            loadingView.setVisibility(View.VISIBLE);
            isKeyEnable = false;
            isAdv15Play = false;
            return;
        }

//        sendStop(Constants.NORMAL_EXIT);
        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS, Constants.MOVIE_STOP);
        bundle.putInt("status", Constants.NORMAL_EXIT);
        ServiceUtils.startService(PageRecordService.class, bundle);
        finish();
    }

    @Override
    public boolean onInfo(PLMediaPlayer mp, int what, int extra) {

        Log.e("oninfo", "what - " + what);

        //缓冲
        if (what == PLMediaPlayer.MEDIA_INFO_BUFFERING_START) {
            mHandler.removeMessages(VIDEO_TIMEOUT);
            mHandler.sendEmptyMessageDelayed(VIDEO_TIMEOUT, video_timeout);
        }
        //缓冲结束
        if (what == PLMediaPlayer.MEDIA_INFO_BUFFERING_END) {
            mHandler.removeMessages(VIDEO_TIMEOUT);
            DialogUtils.dismissDialog(errorDialog);
            DialogUtils.dismissDialog(normalDialog);
        }
        //第一帧已经开始渲染了
        if (what == PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {

            isKeyEnable = true;
            mHandler.removeMessages(VIDEO_TIMEOUT);

            payVideo();
        }
        return false;
    }

    @Override
    public void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int i) {

    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int errorCode) {
        switch (errorCode) {
            case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                //ToastUtils.showToast(this, "无效的 URL !" + plMediaPlayer.getCurrentPosition());
                break;
            case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                //ToastUtils.showToast(this, "播放资源不存在!" + plMediaPlayer.getCurrentPosition());
                break;
            case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                //ToastUtils.showToast(this, "服务器拒绝连接!" + plMediaPlayer.getCurrentPosition());
                break;
            case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                //ToastUtils.showToast(this, "连接超时!" + plMediaPlayer.getCurrentPosition());
                break;
            case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                //ToastUtils.showToast(this, "空的播放列表!" + plMediaPlayer.getCurrentPosition());
                break;
            case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                //ToastUtils.showToast(this, "与服务器连接断开!" + plMediaPlayer.getCurrentPosition());
                break;
            case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                //ToastUtils.showToast(this, "网络异常!" + plMediaPlayer.getCurrentPosition());
                break;
            case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                //ToastUtils.showToast(this, "未授权，播放一个禁播的流" + plMediaPlayer.getCurrentPosition());
                break;
            case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                //ToastUtils.showToast(this, "播放器准备超时" + plMediaPlayer.getCurrentPosition());
                break;
            case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                //ToastUtils.showToast(this, "读取数据超时" + plMediaPlayer.getCurrentPosition());
                break;
            case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
            default:
                //ToastUtils.showToast(this, "未知错误!" + plMediaPlayer.getCurrentPosition());
                break;
        }

        long currentPosition = plMediaPlayer.getCurrentPosition();

        if (currentPosition <= 0) {
            errorDialog(R.string.play_error, currentPosition, Constants.ERROR_EXIT);
            return true;
        }
        if (!NetWorkUtils.isNetWorkdetect(this)) {
            errorDialog(R.string.play_net_error, currentPosition, Constants.ERROR_EXIT);
            return true;
        }

        //设置加载显示加载框
        loadingView.setVisibility(View.VISIBLE);
        mVideoView.setVideoPath(socketInfo.getMovieUrl());
        mVideoView.seekTo(currentPosition);
        mVideoView.start();
        return true;
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (isAdvPlay || isAdv15Play) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE)) {
                exitDialog(R.string.exit_play_toast);
                return true;
            } else {
                return true;
            }
        }

        if (!isKeyEnable) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE)) {
                exitDialog(R.string.exit_play_toast);
                return true;
            } else {
                return true;
            }
        }

        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            mHandler.removeMessages(HIDE_WINDOW);
            isRunningSeek = true;
            myMediaController.setChangedState();
            doPause();
            //右键按下表示快进
            setMoiveProgress(0);
            return true;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            mHandler.removeMessages(HIDE_WINDOW);
            //左键按下表示快退
            isRunningSeek = true;
            myMediaController.setChangedState();
            doPause();
            setMoiveProgress(1);
            return true;
        }
        if (event.getAction() == KeyEvent.ACTION_UP && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT)) {
            //弹起的时候
            isRunningSeek = false;
            mHandler.removeMessages(FORWARD);
            mHandler.removeMessages(BACK);
            progressCount = 0;
            doResume();
            myMediaController.setNormalState();
            mVideoView.seekTo(nowPosition);
            mHandler.removeMessages(HIDE_WINDOW);
            mHandler.sendEmptyMessageDelayed(HIDE_WINDOW, 3000);
            return true;
        }
        //局中键
        if (event.getAction() == KeyEvent.ACTION_DOWN && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            mHandler.removeMessages(HIDE_WINDOW);
            doPauseResume();
            return true;
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitDialog(Constants.NORMAL_EXIT);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long duration = mVideoView.getDuration();
            long percent = duration / 100;
            switch (msg.what) {
                case FORWARD:
                    myMediaController.show(0);
                    nowPosition = nowPosition + percent;
                    if (nowPosition >= duration) {
                        nowPosition = duration;
                    }
                    long pos = 1000L * nowPosition / duration;
                    myMediaController.setSeekProgress((int) pos);
                    if (isRunningSeek) {
                        mHandler.sendEmptyMessageDelayed(FORWARD, 200);
                    }
                    break;
                case BACK:
                    myMediaController.show(0);
                    nowPosition = nowPosition - percent;
                    if (nowPosition <= 0) {
                        nowPosition = 0;
                    }
                    long pos1 = 1000L * nowPosition / duration;
                    myMediaController.setSeekProgress((int) pos1);
                    if (isRunningSeek) {
                        mHandler.sendEmptyMessageDelayed(BACK, 200);
                    }
                    break;
                case CURRENTTIME:
                    showCurrentTime();
                    if (rl_video_msg.getVisibility() == View.VISIBLE) {
                        mHandler.sendEmptyMessageDelayed(CURRENTTIME, 1000);
                    }
                    break;
                case HIDE_WINDOW:
                    myMediaController.hide();
                    break;
                case VIDEO_TIMEOUT:
                    errorDialog(R.string.play_timeout, mVideoView.getCurrentPosition(), Constants.TIMEOUT_EXIT);
                    break;
                case MOVIE_TIME:
                    movieTime++;
                    mHandler.sendEmptyMessageDelayed(MOVIE_TIME, 1000);
                    break;
//                case GET_STATUS:
//                    payStatusPresenter.request(this, roomId, info.getCustomerId(), Integer.parseInt(socketInfo.getVedioId()));
//                    break;
                case ADV_TIMES://广告剩余标识
                    advTime--;
                    if (advTime <= 0) {
                        advTime = 0;
                    }
                    tv_advert_msg.setText("广告还剩余 " + advTime + " S");
                    mHandler.removeMessages(ADV_TIMES);
                    mHandler.sendEmptyMessageDelayed(ADV_TIMES, 1000);
                case ADV_PLAY_TIME:
                    try {
                        if (Constants.mainPageInfo.getAdSetting().getMediaPlayVideo15() == 1) {
                            currentTimes++;
                            if (currentTimes >= 60 * 30) {
                                currentTimes = 0;
                                advertPosition++;
                                Constants.movieAdvPosition = mVideoView.getCurrentPosition();
                                //播放广告
                                if (FileUtils.fileNumber(FileUtils.getAdvertPath()) == 0) {
                                } else {
                                    //播本地
                                    String advertPath = FileUtils.getAdvert15Path() + Constants.key[advertPosition % Constants.key.length] + ".mp4";
                                    if (new File(advertPath).exists()) {
                                        Log.e("download_success", "1播放地址:" + advertPath);
                                        mVideoView.setMediaController(null);
                                        mVideoView.setVideoPath(advertPath);
                                        mVideoView.start();
                                        isAdv15Play = true;
                                        advTime = 15;
                                        tv_advert_msg.setText("广告还剩余 " + advTime + " S");
                                        tv_advert_msg.setVisibility(View.VISIBLE);
                                        mHandler.removeMessages(ADV_TIMES);
                                        mHandler.sendEmptyMessageDelayed(ADV_TIMES, 2000);
                                    } else {
                                        advertPosition++;
                                        advertPath = FileUtils.getAdvert15Path() + Constants.key[advertPosition % Constants.key.length] + ".mp4";
                                        if (new File(advertPath).exists()) {
                                            Log.e("download_success", "2播放地址:" + advertPath);
                                            myMediaController.hide();
                                            mVideoView.setVideoPath(advertPath);
                                            mVideoView.start();
                                            isAdv15Play = true;
                                            advTime = 15;
                                            tv_advert_msg.setText("广告还剩余 " + advTime + " S");
                                            tv_advert_msg.setVisibility(View.VISIBLE);
                                            mHandler.removeMessages(ADV_TIMES);
                                            mHandler.sendEmptyMessageDelayed(ADV_TIMES, 2000);
                                        } else {
                                            //播放电影
                                        }
                                    }
                                }
                            } else {
                                mHandler.removeMessages(ADV_PLAY_TIME);
                                mHandler.sendEmptyMessageDelayed(ADV_PLAY_TIME, 1000);
                            }
                        }
                    } catch (Exception e) {

                    }
                    break;
            }
        }
    };

    private void doPauseResume() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            myMediaController.show(0);
            iv_pause.setVisibility(View.GONE);
            rl_video_msg.setVisibility(View.VISIBLE);
            mHandler.sendEmptyMessageDelayed(CURRENTTIME, 0);
            mHandler.removeMessages(ADV_PLAY_TIME);
            if (getSupportFragmentManager().findFragmentByTag("advertDialogFragment") == null) {
                AdvertDialogFragment fragment = new AdvertDialogFragment();
                fragment.show(getSupportFragmentManager(), "advertDialogFragment");
            }
        } else {
            mVideoView.start();
            myMediaController.hide();
            iv_pause.setVisibility(View.GONE);
            rl_video_msg.setVisibility(View.GONE);
        }
    }

    private void doResume() {
        if (!mVideoView.isPlaying()) {
            mVideoView.start();
            iv_pause.setVisibility(View.GONE);
            rl_video_msg.setVisibility(View.GONE);
        }
    }

    private void doPause() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            tv_advert_msg.setVisibility(View.INVISIBLE);
            mHandler.removeMessages(ADV_TIMES);
        }
    }

//    private void setMoiveProgress(int state) {
//        if (isRunningSeek) {
//            if (state == 0) {
//                boolean isHaveForward = mHandler.hasMessages(FORWARD);
//                mVideoView.pause();
//                if (!isHaveForward) {
//                    mHandler.sendEmptyMessageDelayed(FORWARD, 200);
//                }
//            } else if (state == 1) {
//                boolean isHaveForward = mHandler.hasMessages(BACK);
//                mVideoView.pause();
//                if (!isHaveForward) {
//                    mHandler.sendEmptyMessageDelayed(BACK, 200);
//                }
//            }
//        }
//    }

    private void showCurrentTime() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        String time = sdf.format(new Date(currentTimeMillis));
        tv_currentTime.setText(time);
    }

    private SocketInfo dispMovieMsg(String msg) {
        String[] split = msg.split("&&&&&&&&&");
        if (split.length != 4) {
            ToastUtils.showShort("视频资源不正确");
            finish();
            return null;
        }
        SocketInfo info = new SocketInfo();
        info.setMovieName(split[0]);
        info.setMovieUrl(split[1]);
        info.setCurrentPosition(split[2]);
        info.setVedioId(split[3]);
        return info;
    }

    //获取视频播放消息
    @Override
    public void getMsg(SocketInfo msg) {

        Log.e("sockettest", "getMsg");

        DialogUtils.dismissDialog(errorDialog);
        DialogUtils.dismissDialog(normalDialog);

        OkGo.getInstance().cancelAll();
        DialogUtils.dismissDialog(payDialog);
        DialogUtils.dismissDialog(payMentChargeDialog);
        DialogUtils.dismissDialog(mediaChargeDialog);
        DialogUtils.dismissDialog(progressDialog);
        payDialog = null;
        payMentChargeDialog = null;
        mediaChargeDialog = null;

        movieTime = 0;
        //发送视频记录
//        sendStop(Constants.SOCKET_CHANGE);

        //开始记录视频时长
//        countTime();

        loadingView.setVisibility(View.VISIBLE);
        //按键设置
        isKeyEnable = false;
        //复位
        isRunningSeek = false;
        myMediaController.setNormalState();
        mHandler.removeMessages(HIDE_WINDOW);
        mHandler.removeMessages(FORWARD);
        mHandler.removeMessages(BACK);
        mHandler.removeMessages(VIDEO_TIMEOUT);
        myMediaController.hide();

//        socketInfo = dispMovieMsg(msg);
        socketInfo = msg;

        if (Constants.mainPageInfo.getAdSetting().getMediaPlayVideo30() == 1) {
            showStartAdv();
        } else {
            initData(0);
        }

        iv_pause.setVisibility(View.GONE);
        rl_video_msg.setVisibility(View.GONE);
        //设置超时
        mHandler.sendEmptyMessageDelayed(VIDEO_TIMEOUT, video_timeout);

        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS, Constants.MOVIE_START);
        bundle.putInt("playType", Constants.SOCKET_PLAY);
        bundle.putString("videoId", socketInfo.getVedioId());
        ServiceUtils.startService(PageRecordService.class, bundle);
    }


    private void setMoiveProgress(int state) {
        if (isRunningSeek) {
            if (state == 0) {
                boolean isHaveForward = mHandler.hasMessages(FORWARD);
                if (!isHaveForward) {
                    nowPosition = mVideoView.getCurrentPosition();
                    mHandler.sendEmptyMessageDelayed(FORWARD, 0);
                }
            } else if (state == 1) {
                boolean isHaveForward = mHandler.hasMessages(BACK);
                if (!isHaveForward) {
                    nowPosition = mVideoView.getCurrentPosition();
                    mHandler.sendEmptyMessageDelayed(BACK, 0);
                }
            }
        }
    }


    private void exitDialog(final int status) {
        normalDialog = new CustomDialog(this, new IDialogClick() {
            @Override
            public void onClick(CustomDialog dialog, String tag) {
                if (tag.equals(Constants.CONFIRM)) {
                    DialogUtils.dismissDialog(dialog);
//                    sendStop(status);
                    Bundle bundle = new Bundle();
                    bundle.putByte(Constants.PAGE_STATUS, Constants.MOVIE_STOP);
                    bundle.putInt("status", status);
                    ServiceUtils.startService(PageRecordService.class, bundle);
                    finish();
                } else if (tag.equals(Constants.CANCEL)) {
                    DialogUtils.dismissDialog(dialog);
                }
            }
        }, R.string.exit_play_toast);
        DialogUtils.showDialog(normalDialog);
    }

    private void errorDialog(int content, final long position, final int status) {
        errorDialog = new CustomDialog(this, new IDialogClick() {
            @Override
            public void onClick(CustomDialog dialog, String tag) {
                if (tag.equals(Constants.CONFIRM)) {
                    DialogUtils.dismissDialog(dialog);
                    loadingView.setVisibility(View.VISIBLE);
                    mVideoView.setVideoPath(socketInfo.getMovieUrl());
                    mVideoView.seekTo(position);
                    mVideoView.start();
                } else if (tag.equals(Constants.CANCEL)) {
                    DialogUtils.dismissDialog(dialog);
//                    sendStop(status);
                    Bundle bundle = new Bundle();
                    bundle.putByte(Constants.PAGE_STATUS, Constants.MOVIE_STOP);
                    bundle.putInt("status", status);
                    ServiceUtils.startService(PageRecordService.class, bundle);
                    finish();
                }
            }
        }, content);
        DialogUtils.showDialog(errorDialog);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoviePayTimeOut(String timeOut) {
        if (payDialog != null && payDialog.isShowing() && timeOut.equals("timeOut")) {
            DialogUtils.dismissDialog(payDialog);
            DialogUtils.dismissDialog(payMentChargeDialog);
            this.finish();
        }
    }

    public void onDismissDialogFragment(int keyCode) {
       /* mVideoView.start();
        myMediaController.hide();
        iv_pause.setVisibility(View.GONE);
        rl_video_msg.setVisibility(View.GONE);
        mHandler.removeMessages(ADV_PLAY_TIME);
        mHandler.sendEmptyMessageDelayed(ADV_PLAY_TIME, 1000);*/

        if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            mVideoView.start();
            myMediaController.hide();
            iv_pause.setVisibility(View.GONE);
            rl_video_msg.setVisibility(View.GONE);
            mHandler.removeMessages(ADV_PLAY_TIME);
            mHandler.sendEmptyMessageDelayed(ADV_PLAY_TIME, 1000);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mVideoView.start();
            myMediaController.hide();
            iv_pause.setVisibility(View.GONE);
            rl_video_msg.setVisibility(View.GONE);
            mHandler.removeMessages(ADV_PLAY_TIME);
            mHandler.sendEmptyMessageDelayed(ADV_PLAY_TIME, 1000);
            exitDialog(R.string.exit_play_toast);
        }
    }

//    private void sendStop(int status) {
//
//        //提交播放次数
//        MovieRecordPresenter presenter = new MovieRecordPresenter();
//        presenter.sendStop(recordId,status);
//    }

    //统计视频时长
//    private void countTime() {
////        mHandler.removeMessages(MOVIE_TIME);
////        mHandler.sendEmptyMessageDelayed(MOVIE_TIME, 1000);
//        String roomId = (String) SPUtils.get(this, Constants.ROOM_ID,"");
//        MovieRecordPresenter presenter = new MovieRecordPresenter(this,new RecordListener());
////        presenter.request(this, socketInfo.getVedioId() + "", String.valueOf(movieTime),roomId);
//        presenter.sendStart(this,socketInfo.getVedioId() + "",4,roomId);
//    }

//    private class MyVideoStatusListener implements IPresenterBase<List<VideoPayBean>> {
//
//        @Override
//        public void onSuccess(BaseModel<List<VideoPayBean>> dataList) {
//            List<VideoPayBean> data = dataList.data;
//            if (data != null && data.get(0) != null) {
//                VideoPayBean videoPayBean = data.get(0);
//                if (videoPayBean.getStatus() == 1) {//已支付
//                    DialogUtils.dismissDialog(payDialog);
//                    DialogUtils.dismissDialog(payMentChargeDialog);
//                    DialogUtils.dismissDialog(mediaChargeDialog);
//                    mHandler.removeMessages(HIDE_WINDOW);
//                    mHandler.sendEmptyMessage(HIDE_WINDOW);
//                    doResume();
//                    ToastUtils.showShort("支付成功!");
//
//                } else {
//                    if (payDialog != null && payDialog.isShowing()) {
//                        OkGo.getInstance().cancelAll();
////                        mHandler.removeMessages(GET_STATUS);
////                        mHandler.sendEmptyMessageDelayed(GET_STATUS, 1000);
//                    }
//                }
//
//            } else {
//                if (payDialog != null && payDialog.isShowing()) {
//                    OkGo.getInstance().cancelAll();
////                    mHandler.removeMessages(GET_STATUS);
////                    mHandler.sendEmptyMessageDelayed(GET_STATUS, 1000);
//                }
//            }
//        }
//
//        @Override
//        public void onError() {
//            if (payDialog != null && payDialog.isShowing()) {
//                OkGo.getInstance().cancelAll();
////                mHandler.removeMessages(GET_STATUS);
////                mHandler.sendEmptyMessageDelayed(GET_STATUS, 1000);
//            }
//        }
//    }

    private class MyVideoPayListener implements IPresenterBase<List<VideoPayBean>> {

        @Override
        public void onSuccess(BaseModel<List<VideoPayBean>> dataList) {
            List<VideoPayBean> data = dataList.data;
            DialogUtils.dismissDialog(progressDialog);
            if (data != null && data.get(0) != null) {
                VideoPayBean videoPayBean = data.get(0);
                if (videoPayBean.getStatus() == 1) {//已支付
                    OkGo.getInstance().cancelAll();
//                    mHandler.removeMessages(GET_STATUS);
                    DialogUtils.dismissDialog(payDialog);
                    DialogUtils.dismissDialog(payMentChargeDialog);
                    DialogUtils.dismissDialog(mediaChargeDialog);
                    DialogUtils.dismissDialog(progressDialog);
                    mHandler.removeMessages(HIDE_WINDOW);
                    mHandler.sendEmptyMessageDelayed(HIDE_WINDOW, 3000);
                    tv_advert_msg.setVisibility(View.VISIBLE);
                    mHandler.removeMessages(ADV_TIMES);
                    mHandler.sendEmptyMessageDelayed(ADV_TIMES, 1000);
                    doResume();
//                    resumePauseCount(false);
                } else {
                    showPayMentChargeDialog(videoPayBean);
                    //未支付
                    /*if (dialog == null) {
                        try {
                            String payTitle;
                            if(LanguageUtils.getInstance().getLanguage(SocketPlayActivity.this).equals("zh")){
                                payTitle = "支付：" + socketInfo.getPrice() + "元";
                            }else{
                                payTitle = " Payment：￥" + socketInfo.getPrice() + " ";
                            }
                            Bitmap alipay = QrCodeCreate.createQRCode(videoPayBean.getAliCode(), getDimension(R.dimen.w_502), getDimension(R.dimen.h_10));
                            Bitmap weixin = QrCodeCreate.createQRCode(videoPayBean.getWxCode(), getDimension(R.dimen.w_502), getDimension(R.dimen.h_10));
                            dialog = new MoviePayDialog(SocketPlayActivity.this, null, alipay, weixin, new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    sendStop(Constants.PAY_CANCEL);
                                    SocketPlayActivity.this.finish();
                                }
                            }, "￥" + String.valueOf(socketInfo.getPrice()), 1);
                            DialogUtils.showDialog(dialog);
                            OkGo.getInstance().cancelAll();
                            mHandler.removeMessages(GET_STATUS);
                            mHandler.sendEmptyMessageDelayed(GET_STATUS, 1000);
                        } catch (Exception e) {
                            ToastUtils.showShort("二维码信息获取失败!");
                            finish();
                        }
                    } else if (dialog.isShowing()) {
                        OkGo.getInstance().cancelAll();
                        mHandler.removeMessages(GET_STATUS);
                        mHandler.sendEmptyMessageDelayed(GET_STATUS, 1000);
                    }*/

                }

            } else {
                ToastUtils.showShort("服务器出现问题");
                finish();
            }
        }

        @Override
        public void onError() {
            DialogUtils.dismissDialog(progressDialog);
            ToastUtils.showShort("服务器出现问题");
            finish();
        }
    }

//    private void resumePauseCount(boolean isPause){
//        MovieRecordPresenter presenter = new MovieRecordPresenter();
//        if(isPause){
//            presenter.sendPause(recordId);
//        }else{
//            presenter.sendResume(recordId);
//        }
//
//    }


    private void payVideo() {
        if (socketInfo.getVideoPayment() == 1) {
            if (socketInfo.getStatus() == 0) {
                if (socketInfo.getPrice() > 0f) {
                    if (LanguageUtils.getInstance().getLanguage(this).equals("zh")) {
                        media_pay_once = socketInfo.getPrice() + "元" + getStrings(R.string.once_watch);
                        media_pay_all = socketInfo.getComboPrice() + "元" + getStrings(R.string.all_watch);
                    } else {
                        media_pay_once = "￥" + socketInfo.getPrice() + getStrings(R.string.once_watch);
                        media_pay_all = "￥" + socketInfo.getComboPrice() + getStrings(R.string.all_watch);
                    }
                    doPause();
//                    resumePauseCount(true);
                    //收费
//                    payStatusPresenter = new VideoPayStatusPresenter(this, new MyVideoStatusListener());
                    payPresenter = new VideoPayPresenter(this, new MyVideoPayListener());
                    payPresenter.request(roomId, info.getCustomerId(), 0, Integer.parseInt(socketInfo.getVedioId()));
//                    showMediaChargeDialog();
                }
            }
        }
    }

    private void initDialog(Activity activity, String msg) {
//        progressDialog = new ProgressDialog(activity);
//        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        progressDialog.setCancelable(false);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setMessage(msg);
//        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                return true;
//            }
//        });
        progressDialog = new InstallDialog(activity, msg);
        DialogUtils.showDialog(progressDialog);
    }


//    private void showMediaChargeDialog() {
////
////        mediaChargeDialog = new MediaChargeDialog(this, new IDialogListener() {
////            @Override
////            public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {
////
////                if (tag.equals(Constants.CONFIRM)) {
////                    DialogUtils.dismissDialog(mediaPayDialog);
////                    mediaPayDialog = null;
////                    movieType = 0;
////                    moviePrice = String.valueOf(movieDetail.getPrice());
////                    videoPayPresenter.request(roomId, customerId, movieType, movieDetail.getId());
////                    initDialog(PlayVideoActivity.this, "支付信息获取中...");
////                    DialogUtils.showDialog(progressDialog);
////                } else if (tag.equals(Constants.CANCEL)) {
////                    DialogUtils.dismissDialog(mediaPayDialog);
////                    mediaPayDialog = null;
////                    movieType = 1;
////                    moviePrice = String.valueOf(movieDetail.getComboPrice());
////                    videoPayPresenter.request(roomId, customerId, movieType, movieDetail.getId());
////                    initDialog(PlayVideoActivity.this, "支付信息获取中...");
////                    DialogUtils.showDialog(progressDialog);
////                }
////            }
////        }, media_pay_once, media_pay_all, new DialogInterface.OnCancelListener() {
////            @Override
////            public void onCancel(DialogInterface dialog) {
//////                uploadPlayCounts(Constants.PAY_CANCEL);
////                PlayVideoActivity.this.finish();
////            }
////        });
////        DialogUtils.showDialog(mediaChargeDialog);
//
//        //                uploadPlayCounts(Constants.PAY_CANCEL);
//        mediaChargeDialog = new MediaChargeDialog(this, new IDialogListener() {
//            @Override
//            public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {
//
//                if (tag.equals(Constants.CONFIRM)) {
//                    DialogUtils.dismissDialog(payMentChargeDialog);
//                    payMentChargeDialog = null;
//                    movieType = 0;
//                    payPresenter.request(roomId, info.getCustomerId(), movieType, Integer.parseInt(socketInfo.getVedioId()));
//                    initDialog(SocketPlayActivity.this, "支付信息获取中...");
//                    DialogUtils.showDialog(progressDialog);
//                } else if (tag.equals(Constants.CANCEL)) {
//                    DialogUtils.dismissDialog(payMentChargeDialog);
//                    payMentChargeDialog = null;
//                    movieType = 1;
//                    payPresenter.request(roomId, info.getCustomerId(), movieType, Integer.parseInt(socketInfo.getVedioId()));
//                    initDialog(SocketPlayActivity.this, "支付信息获取中...");
//                    DialogUtils.showDialog(progressDialog);
//
//                }
//            }
//        }, media_pay_once, media_pay_all, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
////                uploadPlayCounts(Constants.PAY_CANCEL);
//                SocketPlayActivity.this.finish();
//            }
//        });
//        DialogUtils.showDialog(mediaChargeDialog);
//    }

    /**
     * 选择支付方式弹窗
     */
    private void showPayMentChargeDialog(final VideoPayBean videoPayBean) {
        DialogUtils.dismissDialog(mediaChargeDialog);
        mediaChargeDialog = null;
        payMentChargeDialog = new PayMentChargeDialog(SocketPlayActivity.this, new IDialogListener() {
            @Override
            public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {
                if (tag.equals(Constants.CONFIRM)) {
                    DialogUtils.dismissDialog(payDialog);
                    payDialog = null;
                    setMediaPayDialog(0, videoPayBean);
                } else if (tag.equals(Constants.CANCEL)) {
                    DialogUtils.dismissDialog(payDialog);
                    payDialog = null;
                    setMediaPayDialog(1, videoPayBean);
                }
            }
        }, String.valueOf(socketInfo.getPrice()),1, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
//                showMediaChargeDialog();
                SocketPlayActivity.this.finish();
            }
        });
        DialogUtils.showDialog(payMentChargeDialog);
    }

    /**
     * 支付二维码弹窗
     */
    private void setMediaPayDialog(int payment, VideoPayBean videoPayBean) {
        if (payDialog == null) {
            try {
                Bitmap bitmap;
                if (payment == 0) {
                    bitmap = QrCodeCreate.createQRCode(videoPayBean.getAliCode(), getDimension(R.dimen.w_502), getDimension(R.dimen.h_10));
                } else {
                    bitmap = QrCodeCreate.createQRCode(videoPayBean.getWxCode(), getDimension(R.dimen.w_502), getDimension(R.dimen.h_10));
                }
                payDialog = new PayDialog(SocketPlayActivity.this, payment, bitmap
                        , new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }, "￥" + socketInfo.getPrice(), movieType);
                DialogUtils.showDialog(payDialog);
                OkGo.getInstance().cancelAll();
//                mHandler.removeMessages(GET_STATUS);
//                mHandler.sendEmptyMessageDelayed(GET_STATUS, 1000);
            } catch (Exception e) {
                ToastUtils.showShort("二维码信息获取失败!");
                finish();
            }
        } else if (payDialog.isShowing()) {
            OkGo.getInstance().cancelAll();
//            mHandler.removeMessages(GET_STATUS);
//            mHandler.sendEmptyMessageDelayed(GET_STATUS, 1000);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoviePayStatus(MoviePayStatus status) {
        if (payDialog != null && payDialog.isShowing() && status.isStatus()) {
            DialogUtils.dismissDialog(payDialog);
            DialogUtils.dismissDialog(payMentChargeDialog);
            DialogUtils.dismissDialog(mediaChargeDialog);
            mHandler.removeMessages(HIDE_WINDOW);
            mHandler.sendEmptyMessage(HIDE_WINDOW);
            doResume();
            //继续
            ToastUtils.showShort("支付成功!");
            tv_advert_msg.setVisibility(View.VISIBLE);
            mHandler.removeMessages(ADV_TIMES);
            mHandler.sendEmptyMessageDelayed(ADV_TIMES, 1000);
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Bundle bundle = new Bundle();
            bundle.putByte(Constants.PAGE_STATUS, Constants.MOVIE_STOP);
            bundle.putInt("status", Constants.NORMAL_EXIT);
            ServiceUtils.startService(PageRecordService.class, bundle);
            mHandler.removeCallbacks(runnable);
            mHandler.postDelayed(runnable, 60 * 1000);
        }
    };

    /**
     * 播放开始广告
     **/
    private void showStartAdv() {
        if (FileUtils.fileNumber(FileUtils.getAdvertPath()) == 0) {
            //播放电影
            initData(0);
        } else {
            //播本地
            //生成一个0-10
            advertPosition = (int) (Math.random() * 10);
            String advertPath = FileUtils.getAdvertPath() + Constants.key[advertPosition % Constants.key.length] + ".mp4";
            if (new File(advertPath).exists()) {
                mVideoView.setMediaController(null);
                mVideoView.setVideoPath(advertPath);
                mVideoView.start();
                isAdvPlay = true;
                advTime = 30;
                tv_advert_msg.setText("广告还剩余 " + advTime + " S");
                tv_advert_msg.setVisibility(View.VISIBLE);
                mHandler.removeMessages(ADV_TIMES);
                mHandler.sendEmptyMessageDelayed(ADV_TIMES, 2000);

                if(myMediaController != null){
                    myMediaController.hide();
                }

            } else {
                advertPosition++;
                advertPath = FileUtils.getAdvertPath() + Constants.key[advertPosition % Constants.key.length] + ".mp4";
                if (new File(advertPath).exists()) {
                    mVideoView.setMediaController(null);
                    mVideoView.setVideoPath(advertPath);
                    mVideoView.start();
                    isAdvPlay = true;
                    advTime = 30;
                    tv_advert_msg.setText("广告还剩余 " + advTime + " S");
                    tv_advert_msg.setVisibility(View.VISIBLE);
                    mHandler.removeMessages(ADV_TIMES);
                    mHandler.sendEmptyMessageDelayed(ADV_TIMES, 2000);

                    if(myMediaController != null){
                        myMediaController.hide();
                    }

                } else {
                    //播放电影
                    initData(0);
                }
            }
        }

    }

    private void initData(long position) {

        tv_videoName.setText(socketInfo.getMovieName());

        mVideoView.setVideoPath(socketInfo.getMovieUrl());

        mVideoView.start();
        if (position == 0) {
            mVideoView.seekTo(Integer.parseInt(socketInfo.getCurrentPosition()));
        } else {
            mVideoView.seekTo(position);
        }

    }

    private class MyAdvertListener implements IPresenterDownloadBase<List<AdvertVideoInfo>> {

        private boolean is30;

        public MyAdvertListener(boolean is30) {
            this.is30 = is30;
        }

        private int advertId = -1;//广告ID

        @Override
        public void onSuccess(BaseModel<List<AdvertVideoInfo>> dataList) {
            LogUtils.e("success");
            advertId = dataList.data.get(0).getId();
        }

        @Override
        public void onDownLoadSuccess(String fileName) {
            String id;
            if (is30) {
                //保存ID到数据库
                id = FileUtils.getAdvertPath() + fileName;
            } else {
                id = FileUtils.getAdvert15Path() + fileName;
            }
            AdvertInfo advertInfo = DBUtils.find(SocketPlayActivity.this, AdvertInfo.class, "id", "=", id);
            if (advertInfo == null) {
                advertInfo = new AdvertInfo();
                advertInfo.setId(id);
                advertInfo.setCurrentAdvId(advertId);
                DBUtils.save(SocketPlayActivity.this, advertInfo);
            } else {
                advertInfo.setCurrentAdvId(advertId);
                DBUtils.update(SocketPlayActivity.this, advertInfo, "currentAdvId");
            }
        }

        @Override
        public void onError() {
            LogUtils.e("error");
        }
    }

}
