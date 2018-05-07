package com.yht.iptv.view.movie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.lzy.okgo.OkGo;
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
import com.yht.iptv.model.MediaDetailBean;
import com.yht.iptv.model.VideoPayBean;
import com.yht.iptv.model.VideoPlayInfo;
import com.yht.iptv.model.VideoPlayParts;
import com.yht.iptv.presenter.AdRecordPresenter;
import com.yht.iptv.presenter.AdvertDownloadPresenter;
import com.yht.iptv.presenter.VideoPayPresenter;
import com.yht.iptv.presenter.VideoPayStatusPresenter;
import com.yht.iptv.service.PageRecordService;
import com.yht.iptv.tools.AdvertDialogFragment;
import com.yht.iptv.tools.CustomDialog;
import com.yht.iptv.tools.MediaChargeDialog;
import com.yht.iptv.tools.MyMediaController;
import com.yht.iptv.tools.PayDialog;
import com.yht.iptv.tools.PayMentChargeDialog;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.DBUtils;
import com.yht.iptv.utils.DialogUtils;
import com.yht.iptv.utils.FileUtils;
import com.yht.iptv.utils.LanguageUtils;
import com.yht.iptv.utils.NetworkUtils;
import com.yht.iptv.utils.OkHttpUtils;
import com.yht.iptv.utils.QrCodeCreate;
import com.yht.iptv.utils.SPUtils;
import com.yht.iptv.utils.ServiceUtils;
import com.yht.iptv.utils.TextUtils;
import com.yht.iptv.utils.ToastUtils;
import com.yht.iptv.view.BaseActivity;

import org.xutils.db.sqlite.WhereBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Q on 2017/10/17.
 */

public class PlayVideoActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, PLMediaPlayer.OnCompletionListener, PLMediaPlayer.OnInfoListener, PLMediaPlayer.OnVideoSizeChangedListener, PLMediaPlayer.OnBufferingUpdateListener, PLMediaPlayer.OnPreparedListener, PLMediaPlayer.OnSeekCompleteListener, PLMediaPlayer.OnErrorListener {
    private PLVideoView mVideoView;
    private TextView tv_videoName, tv_currentTime, tv_pay_toast;
    private ImageView iv_pause;
    private RelativeLayout rl_video_msg;
    private LinearLayout loadingView;
    private RelativeLayout rl_menu;
    private Button[] button;
    private Button[] button1;
    public static PlayVideoActivity instance;
    private MyMediaController myMediaController;
    private static final int FORWARD = 1;
    private static final int BACK = 2;
    private static final int CURRENTTIME = 3;
    private static final int HIDE_WINDOW = 4;
    private static final int VIDEO_TIMEOUT = 5;
    //    private static final int MOVIE_TIME = 6;
    //    private static final int GET_STATUS = 7;
    private static final int TIMEOUT_WHAT = 8;
    private static final int DIALOG_SHOW_TIME = 9;//对话框超时标识
    private static final int ADV_PLAY_TIME = 10;//间隔播广告标识
    private static final int ADV_TIMES = 11;//广告剩余标识
    private String videoPath;
    private MediaDetailBean movieDetail;
    private boolean isRunningSeek;
    private int position;
    private int payStatus;//1免费视频,0收费视频
    private long nowPosition;
    private String roomId;
    private MainPageInfo info;

    private final long video_timeout = 30 * 1000;
    private CustomDialog errorDialog;
    private CustomDialog normalDialog;

    private long recordId = 0;

    private boolean isLoadPay;//是否正在加载收费信息
    private boolean isKeyEnable;

    private PayDialog payDialog;
    private MediaChargeDialog mediaChargeDialog;
    private PayMentChargeDialog payMentChargeDialog;
    private String media_pay_once = null;
    private String media_pay_all = null;
    //    private MediaPayDialog mediaPayDialog;
    private VideoPayPresenter videoPayPresenter;
    private VideoPayStatusPresenter videoPayStatusPresenter;
    private VideoPayBean videoPayBean;//请求bean
    private ProgressDialog progressDialog;
    private int movieType = 0;
    private String moviePrice;
    private boolean isAdvPlay = false;//30s开始的广告
    private boolean isAdv15Play = false;//15s插播的广告
    private int currentTimes;//当前的播放电影的时间20分钟一次广告
    private int advertPosition = 0;
    private TextView tv_advert_msg;
    private int advTime;//广告剩余时间
    private int currentAdvertId = -1;//广告ID
    private boolean isNextPlay = false;//是否点击下一个视频
    private View currentClickView = null;//当前点击的VIEW
    //    private String customerId = "qipa";
//    private String getVideoPayStatus;//查询支付订单标识 first或者second


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isKeyEnable = false;
        instance = this;
        Bundle bundle = getIntent().getExtras();
        movieDetail = bundle.getParcelable("movie_detail");
        position = bundle.getInt("position");
        payStatus = bundle.getInt("payStatus");
        if (movieDetail.getAttachments().get(position) == null || movieDetail.getAttachments().get(position).getF_pathSvr() == null) {
            ToastUtils.showShort("视频信息错误!");
            finish();
            return;
        }
        ////开始记录
        Bundle bundle1 = new Bundle();
        bundle1.putByte(Constants.PAGE_STATUS, Constants.MOVIE_START);
        bundle1.putString("videoId", String.valueOf(movieDetail.getId()));
        bundle1.putInt("playType", Constants.PREVIEW);
        ServiceUtils.startService(PageRecordService.class, bundle1);
        //获取主页信息和房间号
        roomId = (String) SPUtils.get(this, Constants.ROOM_ID, "");
        info = Constants.mainPageInfo;
        videoPayStatusPresenter = new VideoPayStatusPresenter(this, new VideoPayStatusListener());
        setContentView(R.layout.activity_play_video);
        initView();
        if (Constants.mainPageInfo.getAdSetting().getMediaPlayVideo30() == 1) {
            showStartAdv();
        } else {
            initData();
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


    private void initView() {
        iv_pause = (ImageView) findViewById(R.id.iv_pause);
        rl_video_msg = (RelativeLayout) findViewById(R.id.rl_video_msg);
        tv_videoName = (TextView) findViewById(R.id.tv_videoName);
        tv_currentTime = (TextView) findViewById(R.id.tv_currentTime);
        mVideoView = (PLVideoView) findViewById(R.id.VideoView);
        rl_menu = (RelativeLayout) findViewById(R.id.rl_menu);
        loadingView = (LinearLayout) findViewById(R.id.LoadingView);
        tv_pay_toast = (TextView) findViewById(R.id.tv_pay_toast);
        tv_advert_msg = (TextView) findViewById(R.id.tv_advert_msg);
        //设置加载显示加载框
        mVideoView.setBufferingIndicator(loadingView);
        loadingView.setVisibility(View.VISIBLE);

        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_16_9);

        //播放设置
        playerOptions();
        //设置监听
        //播放完成
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnVideoSizeChangedListener(this);
        mVideoView.setOnBufferingUpdateListener(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnSeekCompleteListener(this);
        mVideoView.setOnErrorListener(this);
        tv_pay_toast.setVisibility(View.INVISIBLE);
    }


    private void initData() {
        if (payStatus == 1) {
            tv_pay_toast.setVisibility(View.GONE);
            videoPath = movieDetail.getAttachments().get(position).getF_pathSvr();
            //获取进度
            nowPosition = getVideoProgress(String.valueOf(movieDetail.getAttachments().get(position).getId()));
            rl_menu.addView(getViews(movieDetail.getAttachments().size(), position + 1)[0]);
            rl_menu.addView(getViews(movieDetail.getAttachments().size(), position + 1)[1]);
            if (movieDetail.getAttachments().size() > 1) {
                //记录片
                tv_videoName.setText(movieDetail.getName() + " 第" + (position + 1) + "集");
            } else {
                //电影
                tv_videoName.setText(movieDetail.getName());
            }
            playPosition();
            return;
        }
        if (movieDetail.getVideoPayment() == 1) {
            if (movieDetail.getPrice() > 0f) {
                //收费视频
//                tv_pay_toast.setVisibility(View.VISIBLE);
                if (LanguageUtils.getInstance().getLanguage(this).equals("zh")) {
                    media_pay_once = movieDetail.getPrice() + "元" + getStrings(R.string.once_watch);
                    media_pay_all = movieDetail.getComboPrice() + "元" + getStrings(R.string.all_watch);
                } else {
                    media_pay_once = "￥" + movieDetail.getPrice() + getStrings(R.string.once_watch);
                    media_pay_all = "￥" + movieDetail.getComboPrice() + getStrings(R.string.all_watch);
                }
                videoPayPresenter = new VideoPayPresenter(this, new VideoPayListener());
            } else {
                //免费视频
                payStatus = 1;
                tv_pay_toast.setVisibility(View.GONE);
            }
        } else {
            //免费视频
            payStatus = 1;
            tv_pay_toast.setVisibility(View.GONE);
        }

        videoPath = movieDetail.getAttachments().get(position).getF_pathSvr();
        //获取进度
        nowPosition = getVideoProgress(String.valueOf(movieDetail.getAttachments().get(position).getId()));
        rl_menu.addView(getViews(movieDetail.getAttachments().size(), position + 1)[0]);
        rl_menu.addView(getViews(movieDetail.getAttachments().size(), position + 1)[1]);
        if (movieDetail.getAttachments().size() > 1) {
            //记录片
            tv_videoName.setText(movieDetail.getName() + " 第" + (position + 1) + "集");
        } else {
            //电影
            tv_videoName.setText(movieDetail.getName());
        }

        if (payStatus == 1) {
            playPosition();
        } else {
            videoPayStatusPresenter.request(this, roomId, info.getCustomerId(), movieDetail.getId());
//            getVideoPayStatus = "first";
            initDialog(this, getStrings(R.string.loading));
            mHandler.sendEmptyMessageDelayed(TIMEOUT_WHAT, 5000);
        }
    }

    private void playPosition() {
        if (payStatus == 1) {
            if (nowPosition != 0) {
                //提示是否继续播放之前的记录
                showDialog();
            } else {
                //设置播放地址
                mVideoView.setVideoPath(videoPath);
                //设置超时处理
                mHandler.sendEmptyMessageDelayed(VIDEO_TIMEOUT, video_timeout);
            }
        } else {
            isLoadPay = false;
            //设置播放地址
            mVideoView.setVideoPath(videoPath);
            //设置超时处理
            mHandler.sendEmptyMessageDelayed(VIDEO_TIMEOUT, video_timeout);
        }

        //关联播放器
        myMediaController = new MyMediaController(this);
        mVideoView.setMediaController(myMediaController);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHandler.removeCallbacks(runnable);
        mHandler.postDelayed(runnable, 60 * 1000);
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacks(runnable);
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


    private LinearLayout[] getViews(int number, int currentNum) {
        LinearLayout[] views = new LinearLayout[2];
        //创建集数布局按钮
        views[0] = new LinearLayout(this);
        views[0].setId(R.id.ll_button_select);
        RelativeLayout.LayoutParams params = (new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        params.setMargins(getDimension(R.dimen.w_92), getDimension(R.dimen.h_10), 0, 0);
        views[0].setLayoutParams(params);
        params.addRule(RelativeLayout.BELOW, R.id.tv_select);

        views[1] = new LinearLayout(this);
        RelativeLayout.LayoutParams params1 = (new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        params1.setMargins(getDimension(R.dimen.w_92), 0, 0, 0);
        views[1].setLayoutParams(params1);
        params1.addRule(RelativeLayout.BELOW, R.id.ll_button_select);

        //设置集数范围需要button数
        int size = getCurrentItem(number);

        //设置具体集数按钮初始化
        button1 = new Button[10];
        for (int j = 0; j < 10; j++) {
            button1[j] = new Button(this);
            button1[j].setVisibility(View.GONE);
            button1[j].setOnClickListener(this);
            button1[0].setId(R.id.media_detail_bt_numbers);
            button1[j].setTag(0x300);
            button1[j].setTextColor(Color.parseColor("#FFFFFF"));
            button1[j].setBackgroundResource(R.drawable.bt_numbers_single_selector);
            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                    getDimension(R.dimen.w_98), getDimension(R.dimen.h_76));
            mLayoutParams.setMargins(getDimension(R.dimen.w_12), 0, 0, 0);
            views[1].addView(button1[j], mLayoutParams);
        }
        //设置集数范围的值
        button = new Button[size];
        for (int i = 0; i < size; i++) {
            button[i] = new Button(this);

            button[i].setOnFocusChangeListener(this);
            button[i].setTag(i);
            button[i].setNextFocusDownId(R.id.media_detail_bt_numbers);
            button[i].setBackgroundResource(R.drawable.bt_number_selector);
            button[i].setTextColor(Color.parseColor("#FFFFFF"));
            if (i == 0) {
                button[i].setId(R.id.media_detail_bt_number_collection);
            } else {
                button[i].setId(0x400 + i);
            }
            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                    getDimension(R.dimen.w_208), getDimension(R.dimen.h_76));
            mLayoutParams.setMargins(getDimension(R.dimen.w_12), 0, 0, 0);
            if (number > 10 * (i + 1)) {
                button[i].setText((i * 10 + 1) + "~" + 10 * (i + 1));
            } else {
                button[i].setText((i * 10 + 1) + "~" + number);
            }

            views[0].addView(button[i], mLayoutParams);

        }

        //获取当前的集数在几个按钮上
        int currentSize = getCurrentItem(currentNum);
        //设置默认集数按钮
        String text = button[currentSize - 1].getText().toString();
        String[] split = text.split("~");
        int start = Integer.parseInt(split[0]);
        int end = Integer.parseInt(split[1]);

        if ((end - start + 1) < 10) {
            for (int i = end - start + 1; i < 10; i++) {
                button1[i].setVisibility(View.GONE);
            }
        }
        for (int i = 0; i < end - start + 1; i++) {
            button1[i].setVisibility(View.VISIBLE);
            button1[i].setText(String.valueOf(start + i));
            if (start + i == currentNum) {
                button1[i].requestFocus();
            }
        }

        return views;
    }

    //获取集数需要的条目数
    private int getCurrentItem(int currentNum) {
        int shi = currentNum / 10;
        int ge = currentNum % 10;
        int size = 0;

        if (ge > 0) {
            size = shi + 1;
        } else if (ge == 0) {
            size = shi;
        }
        return size;
    }


    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            if (((int) v.getTag()) == 0x300) {
                if (movieDetail.getAttachments().get(position) != null && movieDetail.getAttachments().get(position).getF_pathSvr() != null) {
                    /*iv_pause.setVisibility(View.GONE);
                    rl_video_msg.setVisibility(View.GONE);
                    loadingView.setVisibility(View.VISIBLE);
                    position = Integer.parseInt(((Button) v).getText().toString()) - 1;
                    videoPath = movieDetail.getAttachments().get(position).getF_pathSvr();
                    isRunningSeek = false;
                    rl_menu.setVisibility(View.GONE);
                    mHandler.removeMessages(HIDE_WINDOW);
                    mHandler.removeMessages(FORWARD);
                    mHandler.removeMessages(BACK);
                    myMediaController.hide();
                    myMediaController.setNormalState();
                    mVideoView.setVideoPath(videoPath);
                    mVideoView.start();
                    tv_videoName.setText(movieDetail.getName() + " 第" + (position + 1) + "集");*/
                    currentClickView = v;
                    if (Constants.mainPageInfo.getAdSetting().getMediaPlayVideo30() == 1) {
                        showStartAdv(v);
                    } else {
                        playNextVideo(v);
                    }
                } else {
                    ToastUtils.showShort("视频信息有误,请检查后台信息");
                }
            }
        }
    }


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
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
                        if (myMediaController != null) {
                            myMediaController.hide();
                        }
                        break;
                    case VIDEO_TIMEOUT:
                        errorDialog(R.string.play_timeout, Constants.moviePosition, Constants.TIMEOUT_EXIT);
                        break;
                    case TIMEOUT_WHAT:
                        DialogUtils.dismissDialog(progressDialog);
                        break;
                    case DIALOG_SHOW_TIME://对话框消失标识
                        DialogUtils.dismissDialog(normalDialog);
                        mVideoView.setVideoPath(videoPath);
                        mVideoView.seekTo(0);
                        break;
                    case ADV_TIMES://广告剩余标识
                        advTime--;
                        if (advTime <= 0) {
                            advTime = 0;
                        }
                        tv_advert_msg.setText(getResources().getString(R.string.ad_left) + " " + advTime + " S");
                        mHandler.removeMessages(ADV_TIMES);
                        mHandler.sendEmptyMessageDelayed(ADV_TIMES, 1000);
                        break;
                    case ADV_PLAY_TIME:
                        try {
                            if (Constants.mainPageInfo.getAdSetting().getMediaPlayVideo15() == 1) {
                                currentTimes++;
                                if (currentTimes >= 60 * 30) {
                                    rl_menu.setVisibility(View.GONE);
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
                                            tv_advert_msg.setText(getResources().getString(R.string.ad_left) + " " + advTime + " S");
                                            tv_advert_msg.setVisibility(View.VISIBLE);
                                            mHandler.removeMessages(ADV_TIMES);
                                            mHandler.sendEmptyMessageDelayed(ADV_TIMES, 2000);
                                        } else {
                                            advertPosition++;
                                            advertPath = FileUtils.getAdvert15Path() + Constants.key[advertPosition % Constants.key.length] + ".mp4";
                                            if (new File(advertPath).exists()) {
                                                Log.e("download_success", "2播放地址:" + advertPath);
                                                mVideoView.setMediaController(null);
                                                mVideoView.setVideoPath(advertPath);
                                                mVideoView.start();
                                                isAdv15Play = true;
                                                advTime = 15;
                                                tv_advert_msg.setText(getResources().getString(R.string.ad_left) + " " + advTime + " S");
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
            } catch (Exception e) {

            }
        }
    };

    /**
     * 格式化视频时间
     */
    private void showCurrentTime() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        String time = sdf.format(new Date(currentTimeMillis));
        tv_currentTime.setText(time);
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
        DialogUtils.dismissDialog(progressDialog);
        DialogUtils.dismissDialog(mediaChargeDialog);
        DialogUtils.dismissDialog(payDialog);
        DialogUtils.dismissDialog(payMentChargeDialog);
//        mHandler.removeMessages(MOVIE_TIME);
        mHandler.removeMessages(VIDEO_TIMEOUT);
        mHandler.removeMessages(TIMEOUT_WHAT);
        mHandler.removeMessages(DIALOG_SHOW_TIME);
        mHandler.removeMessages(ADV_PLAY_TIME);
        mHandler.removeMessages(ADV_TIMES);
        mHandler.removeCallbacksAndMessages(null);
        OkHttpUtils.cancel();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mVideoView.stopPlayback();
            }
        }).start();
    }


    /**
     * 视频加载超时等错误弹窗
     *
     * @param content
     * @param position
     * @param status
     */
    private void errorDialog(int content, final long position, final int status) {
        errorDialog = new CustomDialog(this, new IDialogClick() {
            @Override
            public void onClick(CustomDialog dialog, String tag) {
                if (tag.equals(Constants.CONFIRM)) {
                    DialogUtils.dismissDialog(dialog);
                    loadingView.setVisibility(View.VISIBLE);
                    mVideoView.setVideoPath(videoPath);
                    mVideoView.seekTo(position);
                    mVideoView.start();
                    mHandler.removeMessages(VIDEO_TIMEOUT);
                    mHandler.sendEmptyMessageDelayed(VIDEO_TIMEOUT, video_timeout);
                } else if (tag.equals(Constants.CANCEL)) {
                    DialogUtils.dismissDialog(dialog);
                    savePosition(position);
                    saveParts();
                    Bundle bundle = new Bundle();
                    bundle.putByte(Constants.PAGE_STATUS, Constants.MOVIE_STOP);
                    bundle.putInt("status", status);
                    ServiceUtils.startService(PageRecordService.class, bundle);
                    //请求网络
//                    mHandler.removeMessages(MOVIE_TIME);
                    finish();
                }
            }
        }, content);
        DialogUtils.showDialog(errorDialog);
    }


    /**
     * 退出弹窗
     *
     * @param content
     */
    private void exitDialog(int content) {
        CustomDialog dialog = new CustomDialog(this, new IDialogClick() {
            @Override
            public void onClick(CustomDialog dialog, String tag) {
                if (tag.equals(Constants.CONFIRM)) {
                    DialogUtils.dismissDialog(dialog);
                    savePosition(mVideoView.getCurrentPosition());
                    saveParts();
                    //请求网络
//                    mHandler.removeMessages(MOVIE_TIME);
//                    uploadPlayCounts(Constants.NORMAL_EXIT);
                    Bundle bundle = new Bundle();
                    bundle.putByte(Constants.PAGE_STATUS, Constants.MOVIE_STOP);
                    bundle.putInt("status", Constants.NORMAL_EXIT);
                    ServiceUtils.startService(PageRecordService.class, bundle);

                    if (isAdv15Play) {
                        AdvertInfo advertInfo = DBUtils.find(PlayVideoActivity.this, AdvertInfo.class, "id", "=", FileUtils.getAdvert15Path() + Constants.key[advertPosition % Constants.key.length]);
                        if (advertInfo != null) {
                            currentAdvertId = advertInfo.getCurrentAdvId();
                        }
                        if (currentAdvertId != -1) {
                            AdRecordPresenter adRecordPresenter = new AdRecordPresenter();
                            adRecordPresenter.request(this, currentAdvertId, 1, roomId, 0, Math.abs(15 - advTime));
                            LogUtils.e(currentAdvertId + "请求完成");
                        }
                    }

                    if (isAdvPlay) {
                        AdvertInfo advertInfo = DBUtils.find(PlayVideoActivity.this, AdvertInfo.class, "id", "=", FileUtils.getAdvertPath() + Constants.key[advertPosition % Constants.key.length]);
                        if (advertInfo != null) {
                            currentAdvertId = advertInfo.getCurrentAdvId();
                        }
                        if (currentAdvertId != -1) {
                            AdRecordPresenter adRecordPresenter = new AdRecordPresenter();
                            adRecordPresenter.request(this, currentAdvertId, 1, roomId, 0, Math.abs(30 - advTime));
                            LogUtils.e(currentAdvertId + "请求完成");
                        }
                    }

                    finish();
                } else if (tag.equals(Constants.CANCEL)) {
                    DialogUtils.dismissDialog(dialog);
                }
            }
        }, content);
        DialogUtils.showDialog(dialog);
    }


    /**
     * 继续播放记录弹窗
     */
    private void showDialog() {
        mHandler.removeMessages(DIALOG_SHOW_TIME);
        mHandler.sendEmptyMessageDelayed(DIALOG_SHOW_TIME, 5000);
        normalDialog = new CustomDialog(this, new IDialogClick() {
            @Override
            public void onClick(CustomDialog dialog, String tag) {
                mHandler.removeMessages(DIALOG_SHOW_TIME);
                if (tag.equals(Constants.CONFIRM)) {
                    DialogUtils.dismissDialog(dialog);
                    mVideoView.setVideoPath(videoPath);
                    mVideoView.seekTo(nowPosition);
                    //设置超时处理
                    mHandler.removeMessages(VIDEO_TIMEOUT);
                    mHandler.sendEmptyMessageDelayed(VIDEO_TIMEOUT, video_timeout);
//                    countTime();
                } else if (tag.equals(Constants.CANCEL)) {
                    DialogUtils.dismissDialog(dialog);
                    mVideoView.setVideoPath(videoPath);
                    mVideoView.seekTo(0);
                    //设置超时处理
                    mHandler.removeMessages(VIDEO_TIMEOUT);
                    mHandler.sendEmptyMessageDelayed(VIDEO_TIMEOUT, video_timeout);
//                    countTime();
                }
            }
        }, R.string.play_progress_toast, getStrings(R.string.continues), getStrings(R.string.replay));
        DialogUtils.showDialog(normalDialog);
    }


    /**
     * 选择支付方式弹窗
     */
    private void showPayMentChargeDialog(final VideoPayBean videoPayBean) {
        DialogUtils.dismissDialog(mediaChargeDialog);
        mediaChargeDialog = null;
        payMentChargeDialog = new PayMentChargeDialog(PlayVideoActivity.this, new IDialogListener() {
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
        }, moviePrice, 1,new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                showMediaChargeDialog();
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
                payDialog = new PayDialog(PlayVideoActivity.this, payment, bitmap
                        , new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }, "￥" + moviePrice, movieType);
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


    /**
     * 选择影片类型弹窗
     */
    private void showMediaChargeDialog() {
//
//        mediaChargeDialog = new MediaChargeDialog(this, new IDialogListener() {
//            @Override
//            public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {
//
//                if (tag.equals(Constants.CONFIRM)) {
//                    DialogUtils.dismissDialog(mediaPayDialog);
//                    mediaPayDialog = null;
//                    movieType = 0;
//                    moviePrice = String.valueOf(movieDetail.getPrice());
//                    videoPayPresenter.request(roomId, customerId, movieType, movieDetail.getId());
//                    initDialog(PlayVideoActivity.this, "支付信息获取中...");
//                    DialogUtils.showDialog(progressDialog);
//                } else if (tag.equals(Constants.CANCEL)) {
//                    DialogUtils.dismissDialog(mediaPayDialog);
//                    mediaPayDialog = null;
//                    movieType = 1;
//                    moviePrice = String.valueOf(movieDetail.getComboPrice());
//                    videoPayPresenter.request(roomId, customerId, movieType, movieDetail.getId());
//                    initDialog(PlayVideoActivity.this, "支付信息获取中...");
//                    DialogUtils.showDialog(progressDialog);
//                }
//            }
//        }, media_pay_once, media_pay_all, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
////                uploadPlayCounts(Constants.PAY_CANCEL);
//                PlayVideoActivity.this.finish();
//            }
//        });
//        DialogUtils.showDialog(mediaChargeDialog);

        mediaChargeDialog = new MediaChargeDialog(this, new IDialogListener() {
            @Override
            public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {

                if (tag.equals(Constants.CONFIRM)) {
                    DialogUtils.dismissDialog(payMentChargeDialog);
                    payMentChargeDialog = null;
                    movieType = 0;
                    moviePrice = String.valueOf(movieDetail.getPrice());
                    videoPayPresenter.request(roomId, info.getCustomerId(), movieType, movieDetail.getId());
                    initDialog(PlayVideoActivity.this, "支付信息获取中...");
                    DialogUtils.showDialog(progressDialog);
                } else if (tag.equals(Constants.CANCEL)) {
                    DialogUtils.dismissDialog(payMentChargeDialog);
                    payMentChargeDialog = null;
                    movieType = 1;
                    moviePrice = String.valueOf(movieDetail.getComboPrice());
                    videoPayPresenter.request(roomId, info.getCustomerId(), movieType, movieDetail.getId());
                    initDialog(PlayVideoActivity.this, "支付信息获取中...");
                    DialogUtils.showDialog(progressDialog);

                }
            }
        }, media_pay_once, media_pay_all, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
//                uploadPlayCounts(Constants.PAY_CANCEL);
                Bundle bundle = new Bundle();
                bundle.putByte(Constants.PAGE_STATUS, Constants.MOVIE_STOP);
                bundle.putInt("status", Constants.PAY_CANCEL);
                ServiceUtils.startService(PageRecordService.class, bundle);
                PlayVideoActivity.this.finish();
            }
        });
        DialogUtils.showDialog(mediaChargeDialog);
    }

    public void onDismissDialogFragment(int keyCode) {
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
            myMediaController.show(3000);
        }
    }

    private void doPause() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            myMediaController.show(0);
        }
    }

    //保存进度
    private void savePosition(long currentPosition) {
        String videoId = String.valueOf(movieDetail.getAttachments().get(position).getId());
        VideoPlayInfo info = new VideoPlayInfo();
        info.setVideoId(videoId);
        info.setVedio_progress(currentPosition);
        setVideoProgress(info);
    }

    //保存集数
    private void saveParts() {
        String videoId = String.valueOf(movieDetail.getId());
        VideoPlayParts parts = new VideoPlayParts();
        parts.setVideoId(videoId);
        parts.setCurrentParts(position);
        setVideoParts(parts);
    }


    //获取进度
    private long getVideoProgress(String videoId) {
        WhereBuilder builder = WhereBuilder.b("videoId", "=", videoId);
        VideoPlayInfo videoPlayInfos = DBUtils.find(this, VideoPlayInfo.class, builder);
        if (videoPlayInfos == null) {
            return 0;
        } else {
            return videoPlayInfos.getVedio_progress();
        }
    }

    //保存进度
    private void setVideoProgress(VideoPlayInfo info) {
        List<VideoPlayInfo> videoPlayInfos = DBUtils.findAll(this, VideoPlayInfo.class);
        if (videoPlayInfos == null || videoPlayInfos.size() == 0) {
            DBUtils.save(this, info);
        } else {
            DBUtils.saveOrUpdate(this, info);
        }
    }

    //保存集数
    private void setVideoParts(VideoPlayParts parts) {
        List<VideoPlayParts> videoPlayParts = DBUtils.findAll(this, VideoPlayParts.class);
        if (videoPlayParts == null || videoPlayParts.size() == 0) {
            DBUtils.save(this, parts);
        } else {
            DBUtils.saveOrUpdate(this, parts);
        }
    }


    //设置快进快退
    private void setMoiveProgress(int state) {
        if (isRunningSeek) {
            if (state == 0) {
                boolean isHaveForward = mHandler.hasMessages(FORWARD);
                mVideoView.pause();
                if (!isHaveForward) {
                    nowPosition = mVideoView.getCurrentPosition();
                    mHandler.sendEmptyMessageDelayed(FORWARD, 0);
                }
            } else if (state == 1) {
                boolean isHaveForward = mHandler.hasMessages(BACK);
                mVideoView.pause();
                if (!isHaveForward) {
                    nowPosition = mVideoView.getCurrentPosition();
                    mHandler.sendEmptyMessageDelayed(BACK, 0);
                }
            }
        }
    }


    //显示菜单
    private void showMenu() {
        int currentNum = position + 1;
        rl_menu.setVisibility(View.VISIBLE);
        rl_menu.bringToFront();
        //获取当前的集数在几个按钮上
        int currentSize = getCurrentItem(currentNum);
        //设置默认集数按钮
        String text = button[currentSize - 1].getText().toString();
        String[] split = text.split("~");
        int start = Integer.parseInt(split[0]);
        int end = Integer.parseInt(split[1]);

        if ((end - start + 1) < 10) {
            for (int i = end - start + 1; i < 10; i++) {
                button1[i].setVisibility(View.GONE);
            }
        }
        for (int i = 0; i < end - start + 1; i++) {
            button1[i].setVisibility(View.VISIBLE);
            button1[i].setText(String.valueOf(start + i));
            if (start + i == currentNum) {
                button1[i].requestFocus();
            }
        }
    }


    /**
     * 选集按钮的焦点
     *
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int tag = (int) v.getTag();
        if (hasFocus) {
            String text = button[tag].getText().toString();
            String[] split = text.split("~");
            int start = Integer.parseInt(split[0]);
            int end = Integer.parseInt(split[1]);

            if ((end - start + 1) < 10) {
                for (int i = end - start + 1; i < 10; i++) {
                    button1[i].setVisibility(View.GONE);
                }
            }
            for (int i = 0; i < end - start + 1; i++) {
                button1[i].setVisibility(View.VISIBLE);
                button1[i].setText(start + i + "");
                button1[i].setNextFocusUpId(v.getId());
            }

        }
    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {
        LogUtils.e("tv_advert_msg");
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
            if (!isNextPlay) {
                initData();
            } else {
                if (currentClickView != null) {
                    playNextVideo(currentClickView);
                }
            }
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
            mVideoView.setVideoPath(videoPath);
            mVideoView.seekTo(Constants.movieAdvPosition);
            mVideoView.start();
            //隐藏广告字
            mHandler.removeMessages(ADV_TIMES);
            tv_advert_msg.setVisibility(View.INVISIBLE);

            mHandler.removeMessages(ADV_PLAY_TIME);
            mHandler.sendEmptyMessageDelayed(ADV_PLAY_TIME, 1000);
            String advertPath = FileUtils.getAdvert15Path() + Constants.key[advertPosition % Constants.key.length] + ".mp4";
            AdvertDownloadPresenter presenter = new AdvertDownloadPresenter(advertPath, new MyAdvertListener(false));
            presenter.request(PlayVideoActivity.this, 15, roomId);

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
        //播放完一集删除一条数据记录
        savePosition(0);
        if (position < movieDetail.getAttachments().size() - 1) {
            if (movieDetail.getAttachments().get(position + 1) != null && movieDetail.getAttachments().get(position + 1).getF_pathSvr() != null) {
                //表示还有需要播放的视频
                videoPath = movieDetail.getAttachments().get(position + 1).getF_pathSvr();
                loadingView.setVisibility(View.VISIBLE);
                position = position + 1;
                //记录片
                tv_videoName.setText(movieDetail.getName() + " 第" + (position + 1) + "集");
                if (Constants.mainPageInfo.getAdSetting().getMediaPlayVideo30() == 1) {
                    showStartAdv(null);
                } else {
                    mVideoView.setVideoPath(videoPath);
                    mVideoView.start();
                }


            } else {
                ToastUtils.showShort("视频信息错误,请检查后台数据!");
                finish();
            }

        } else {

            //请求网络
//            mHandler.removeMessages(MOVIE_TIME);
//            uploadPlayCounts(Constants.NORMAL_EXIT);
            Bundle bundle = new Bundle();
            bundle.putByte(Constants.PAGE_STATUS, Constants.MOVIE_STOP);
            bundle.putInt("status", Constants.NORMAL_EXIT);
            ServiceUtils.startService(PageRecordService.class, bundle);
            //表示最后一集退出
            finish();
        }

        Log.e("playvedio", "position--MOVIE_TYPE--" + position);
    }

    @Override
    public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
        Log.e("playvideo", "onInfo: " + what + ", " + extra);
        //缓冲
        if (what == PLMediaPlayer.MEDIA_INFO_BUFFERING_START) {
            mHandler.removeMessages(VIDEO_TIMEOUT);
            mHandler.sendEmptyMessageDelayed(VIDEO_TIMEOUT, video_timeout);
            if (mp.getCurrentPosition() > 0) {
                Constants.moviePosition = mp.getCurrentPosition();
            }
        }
        //缓冲结束
        if (what == PLMediaPlayer.MEDIA_INFO_BUFFERING_END) {
            mHandler.removeMessages(VIDEO_TIMEOUT);
            DialogUtils.dismissDialog(errorDialog);
            DialogUtils.dismissDialog(normalDialog);

            loadingView.setVisibility(View.GONE);

            if (payStatus != 1) {
                if ((payDialog != null && payDialog.isShowing()) || (mediaChargeDialog != null && mediaChargeDialog.isShowing()) || (payMentChargeDialog != null && payMentChargeDialog.isShowing())) {
                    doPause();
                }
            }
        }
        //第一帧已经开始渲染了
        if (what == PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            if (payStatus != 1) {
                tv_pay_toast.setVisibility(View.VISIBLE);
                String s = tv_pay_toast.getText().toString();
                if (LanguageUtils.getInstance().getLanguage(this).equals("zh")) {
                    CharSequence textColor = TextUtils.getChangeTextColor(s, s.indexOf("，") + 1, s.length(), "#ffb340");
                    tv_pay_toast.setText(textColor);
                } else {
                    CharSequence textColor = TextUtils.getChangeTextColor(s, s.indexOf("and") + 3, s.length(), "#ffb340");
                    tv_pay_toast.setText(textColor);
                }
            }
            isKeyEnable = true;
            mHandler.removeMessages(VIDEO_TIMEOUT);
        }

        return false;
    }

    @Override
    public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int i) {
        if (plMediaPlayer.getCurrentPosition() >= 5 * 60 * 1000) {
//            Log.e("time","5分钟时间");
            if (payStatus != 1 && !isLoadPay) {
                isLoadPay = true;
                mHandler.removeMessages(HIDE_WINDOW);
                doPause();
                showMediaChargeDialog();
                mHandler.removeCallbacks(runnable);
                //暂停
                Bundle bundle = new Bundle();
                bundle.putByte(Constants.PAGE_STATUS, Constants.MOVIE_PAULE);
                ServiceUtils.startService(PageRecordService.class, bundle);
            }

        }
    }

    @Override
    public void onPrepared(PLMediaPlayer plMediaPlayer, int i) {

    }

    @Override
    public void onSeekComplete(PLMediaPlayer plMediaPlayer) {
        Log.e("seek", "seekok");
    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
        //隐藏广告字
        mHandler.removeMessages(ADV_TIMES);
        tv_advert_msg.setVisibility(View.INVISIBLE);
        Log.e("onerror", "onerror");
        if (isAdvPlay) {
            isAdvPlay = false;
            initData();
            return true;
        }
        switch (i) {
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
        if (currentPosition > 0) {
            Constants.moviePosition = currentPosition;
        }
        if (Constants.moviePosition <= 0) {
            mHandler.removeMessages(VIDEO_TIMEOUT);

            errorDialog(R.string.play_error, Constants.moviePosition, Constants.ERROR_EXIT);
            return true;
        }
        if (!NetworkUtils.isConnected()) {
            mHandler.removeMessages(VIDEO_TIMEOUT);
            errorDialog(R.string.play_net_error, Constants.moviePosition, Constants.ERROR_EXIT);

            return true;
        }
        //设置加载显示加载框
        loadingView.setVisibility(View.VISIBLE);
        mVideoView.setVideoPath(videoPath);
        mVideoView.seekTo(Constants.moviePosition);
        mVideoView.start();
        return true;
    }

    private class VideoPayStatusListener implements IPresenterBase<List<VideoPayBean>> {

        @Override
        public void onSuccess(BaseModel<List<VideoPayBean>> dataList) {
            List<VideoPayBean> data = dataList.data;
            DialogUtils.dismissDialog(progressDialog);
            if (data != null && data.get(0) != null) {
                videoPayBean = data.get(0);
                if (data.get(0).getStatus() == 1) {//已支付
                    payStatus = 1;
                    playPosition();
                } else {
                    payStatus = 0;
                    playPosition();
                }
            } else {
                PlayVideoActivity.this.finish();
                ToastUtils.showShort("视频信息获取失败,请检查网络连接");
            }
        }

        @Override
        public void onError() {
            PlayVideoActivity.this.finish();
            ToastUtils.showShort("视频信息获取失败,请检查网络连接");
        }
    }

    private class VideoPayListener implements IPresenterBase<List<VideoPayBean>> {

        @Override
        public void onSuccess(BaseModel<List<VideoPayBean>> dataList) {
            List<VideoPayBean> data = dataList.data;
            if (data != null && data.get(0) != null) {
                videoPayBean = data.get(0);
                if (videoPayBean.getStatus() == 1) {//已支付
                    OkGo.getInstance().cancelAll();
//                    mHandler.removeMessages(GET_STATUS);
                    DialogUtils.dismissDialog(payDialog);
                    mHandler.removeMessages(HIDE_WINDOW);
                    mHandler.sendEmptyMessageDelayed(HIDE_WINDOW, 3000);
                    doResume();
                } else {
                    //未支付
                    showPayMentChargeDialog(videoPayBean);
                }

            } else {
                isLoadPay = false;
                ToastUtils.showShort("服务器出现问题");
                finish();
            }
            DialogUtils.dismissDialog(progressDialog);
        }

        @Override
        public void onError() {
            isLoadPay = false;
            DialogUtils.dismissDialog(progressDialog);
            ToastUtils.showShort("服务器出现问题");
            finish();
        }

    }


//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//
//            switch (msg.what) {
//                case TIMEOUT_WHAT:
//                    DialogUtils.dismissDialog(progressDialog);
//                    break;
//            }
//
//        }
//    };

    private void initDialog(Activity activity, String msg) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(msg);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN && (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            return super.dispatchKeyEvent(event);
        }


        if (isAdvPlay || isAdv15Play) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE)) {
                if (rl_menu.getVisibility() == View.VISIBLE) {
                    rl_menu.setVisibility(View.GONE);
                } else {
                    exitDialog(R.string.exit_play_toast);
                }
                return true;
            } else {
                return true;
            }
        }

        if (!isKeyEnable) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE)) {
                if (rl_menu.getVisibility() == View.VISIBLE) {
                    rl_menu.setVisibility(View.GONE);
                } else {
                    exitDialog(R.string.exit_play_toast);
                }
                return true;
            } else {
                return true;
            }
        }
        if (rl_menu.getVisibility() == View.GONE) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                mHandler.removeMessages(HIDE_WINDOW);
                isRunningSeek = true;
                myMediaController.setChangedState();
                doPause();
                //右键按下表示快进
                setMoiveProgress(0);
                //取消广告计时
                mHandler.removeMessages(ADV_PLAY_TIME);
                return true;
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                mHandler.removeMessages(HIDE_WINDOW);
                //左键按下表示快退
                isRunningSeek = true;
                myMediaController.setChangedState();
                doPause();
                setMoiveProgress(1);
                //取消广告计时
                mHandler.removeMessages(ADV_PLAY_TIME);
                return true;
            }
            if (event.getAction() == KeyEvent.ACTION_UP && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT)) {
                //弹起的时候
                isRunningSeek = false;
                mHandler.removeMessages(FORWARD);
                mHandler.removeMessages(BACK);
                doResume();
                myMediaController.setNormalState();
                mVideoView.seekTo(nowPosition);
                mHandler.removeMessages(HIDE_WINDOW);
                mHandler.sendEmptyMessageDelayed(HIDE_WINDOW, 3000);
                //计时广告时间
                mHandler.removeMessages(ADV_PLAY_TIME);
                mHandler.sendEmptyMessageDelayed(ADV_PLAY_TIME, 1000);
                return true;
            }
            //局中键
            if (event.getAction() == KeyEvent.ACTION_DOWN && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                mHandler.removeMessages(HIDE_WINDOW);
                doPauseResume();
                return true;
            }

        }

        //退出键
        if (event.getAction() == KeyEvent.ACTION_DOWN && (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE)) {
            if (rl_menu.getVisibility() == View.VISIBLE) {
                rl_menu.setVisibility(View.GONE);
            } else {
                exitDialog(R.string.exit_play_toast);
            }
            return true;
        }

        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            if (movieDetail.getAttachments().size() > 1) {
                if (rl_menu.getVisibility() == View.VISIBLE) {
                    rl_menu.setVisibility(View.GONE);
                } else {
                    showMenu();
                }
            }
            return true;
        }


        return super.dispatchKeyEvent(event);
        //}
        //return true;
    }

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoviePayStatus(MoviePayStatus status) {
        if (payDialog != null && payDialog.isShowing() && status.isStatus()) {
            payStatus = 1;
            tv_pay_toast.setVisibility(View.GONE);
            DialogUtils.dismissDialog(payDialog);
            DialogUtils.dismissDialog(payMentChargeDialog);
            DialogUtils.dismissDialog(mediaChargeDialog);
            mHandler.removeMessages(HIDE_WINDOW);
            mHandler.sendEmptyMessage(HIDE_WINDOW);
            doResume();
            //继续
            Bundle bundle = new Bundle();
            bundle.putByte(Constants.PAGE_STATUS, Constants.MOVIE_RESUME);
            ServiceUtils.startService(PageRecordService.class, bundle);
            ToastUtils.showShort("支付成功!");
            mHandler.removeCallbacks(runnable);
            mHandler.postDelayed(runnable,60 * 1000);
        }
    }*/

    /**
     * 播放开始广告
     **/
    private void showStartAdv() {
        if (FileUtils.fileNumber(FileUtils.getAdvertPath()) == 0) {
            //播放电影
            initData();
        } else {
            //播本地
            //生成一个0-10
            advertPosition = (int) (Math.random() * 10);
            String advertPath = FileUtils.getAdvertPath() + Constants.key[advertPosition % Constants.key.length] + ".mp4";
            if (new File(advertPath).exists()) {
                mVideoView.setVideoPath(advertPath);
                mVideoView.start();
                isAdvPlay = true;
                advTime = 30;
                tv_advert_msg.setText(getResources().getString(R.string.ad_left) + " " + advTime + " S");
                tv_advert_msg.setVisibility(View.VISIBLE);
                mHandler.removeMessages(ADV_TIMES);
                mHandler.sendEmptyMessageDelayed(ADV_TIMES, 2000);

            } else {
                advertPosition++;
                advertPath = FileUtils.getAdvertPath() + Constants.key[advertPosition % Constants.key.length] + ".mp4";
                if (new File(advertPath).exists()) {
                    mVideoView.setVideoPath(advertPath);
                    mVideoView.start();
                    isAdvPlay = true;
                    advTime = 30;
                    tv_advert_msg.setText(getResources().getString(R.string.ad_left) + " " + advTime + " S");
                    tv_advert_msg.setVisibility(View.VISIBLE);
                    mHandler.removeMessages(ADV_TIMES);
                    mHandler.sendEmptyMessageDelayed(ADV_TIMES, 2000);

                } else {
                    //播放电影
                    initData();
                }
            }
        }

    }

    /**
     * 播放开始广告
     **/
    private void showStartAdv(View v) {
        rl_menu.setVisibility(View.GONE);
        if (FileUtils.fileNumber(FileUtils.getAdvertPath()) == 0) {
            //播放电影
            playNextVideo(v);
            currentTimes = 0;
            mHandler.removeMessages(ADV_PLAY_TIME);
            mHandler.sendEmptyMessageDelayed(ADV_PLAY_TIME, 1000);
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
                tv_advert_msg.setText(getResources().getString(R.string.ad_left) + " " + advTime + " S");
                tv_advert_msg.setVisibility(View.VISIBLE);
                mHandler.removeMessages(ADV_TIMES);
                mHandler.sendEmptyMessageDelayed(ADV_TIMES, 2000);
                currentTimes = 0;
                mHandler.removeMessages(ADV_PLAY_TIME);
                isNextPlay = true;
            } else {
                advertPosition++;
                advertPath = FileUtils.getAdvertPath() + Constants.key[advertPosition % Constants.key.length] + ".mp4";
                if (new File(advertPath).exists()) {
                    mVideoView.setMediaController(null);
                    mVideoView.setVideoPath(advertPath);
                    mVideoView.start();
                    isAdvPlay = true;
                    advTime = 30;
                    tv_advert_msg.setText(getResources().getString(R.string.ad_left) + " " + advTime + " S");
                    tv_advert_msg.setVisibility(View.VISIBLE);
                    mHandler.removeMessages(ADV_TIMES);
                    mHandler.sendEmptyMessageDelayed(ADV_TIMES, 2000);
                    currentTimes = 0;
                    mHandler.removeMessages(ADV_PLAY_TIME);
                    isNextPlay = true;
                } else {
                    playNextVideo(v);
                    mHandler.removeMessages(ADV_PLAY_TIME);
                    mHandler.sendEmptyMessageDelayed(ADV_PLAY_TIME, 1000);
                }
            }
        }
    }

    private void playNextVideo(View v) {
        //播放电影
        iv_pause.setVisibility(View.GONE);
        rl_video_msg.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        if (v != null) {
            position = Integer.parseInt(((Button) v).getText().toString()) - 1;
            videoPath = movieDetail.getAttachments().get(position).getF_pathSvr();
        }
        isRunningSeek = false;
        mHandler.removeMessages(HIDE_WINDOW);
        mHandler.removeMessages(FORWARD);
        mHandler.removeMessages(BACK);
        myMediaController.hide();
        myMediaController.setNormalState();
        mVideoView.setVideoPath(videoPath);
        mVideoView.start();
        tv_videoName.setText(movieDetail.getName() + " 第" + (position + 1) + "集");
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
            AdvertInfo advertInfo = DBUtils.find(PlayVideoActivity.this, AdvertInfo.class, "id", "=", id);
            if (advertInfo == null) {
                advertInfo = new AdvertInfo();
                advertInfo.setId(id);
                advertInfo.setCurrentAdvId(advertId);
                DBUtils.save(PlayVideoActivity.this, advertInfo);
            } else {
                advertInfo.setCurrentAdvId(advertId);
                DBUtils.update(PlayVideoActivity.this, advertInfo, "currentAdvId");
            }
        }

        @Override
        public void onError() {
            LogUtils.e("error");
        }
    }
}
