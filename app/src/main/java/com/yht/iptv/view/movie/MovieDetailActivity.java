package com.yht.iptv.view.movie;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.lzy.okgo.OkGo;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.yht.iptv.R;
import com.yht.iptv.adapter.MediaRecyclerViewAdapter;
import com.yht.iptv.callback.IDialogClick;
import com.yht.iptv.callback.IDialogListener;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.IPresenterDownloadBase;
import com.yht.iptv.model.AdvertInfo;
import com.yht.iptv.model.AdvertVideoInfo;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.MainPageInfo;
import com.yht.iptv.model.MediaDetailBean;
import com.yht.iptv.model.MoviePayStatus;
import com.yht.iptv.model.PictureAdInfo;
import com.yht.iptv.model.VideoPayBean;
import com.yht.iptv.model.VideoPlayParts;
import com.yht.iptv.presenter.AccountPayPresenter;
import com.yht.iptv.presenter.AdRecordPresenter;
import com.yht.iptv.presenter.AdvertDownloadPresenter;
import com.yht.iptv.presenter.MediaDetailPresenter;
import com.yht.iptv.presenter.PictureAdPresenter;
import com.yht.iptv.presenter.VideoPayPresenter;
import com.yht.iptv.presenter.VideoPayStatusPresenter;
import com.yht.iptv.service.PageRecordService;
import com.yht.iptv.tools.CustomDialog;
import com.yht.iptv.tools.InstallDialog;
import com.yht.iptv.tools.PayDialog;
import com.yht.iptv.tools.PayMentChargeDialog;
import com.yht.iptv.utils.AnimationUtils;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.DBUtils;
import com.yht.iptv.utils.DialogUtils;
import com.yht.iptv.utils.FileUtils;
import com.yht.iptv.utils.LanguageUtils;
import com.yht.iptv.utils.OkHttpUtils;
import com.yht.iptv.utils.QrCodeCreate;
import com.yht.iptv.utils.SPUtils;
import com.yht.iptv.utils.ServiceUtils;
import com.yht.iptv.utils.ShowImageUtils;
import com.yht.iptv.utils.ToastUtils;
import com.yht.iptv.view.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.db.sqlite.WhereBuilder;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.yht.iptv.R.id.videoView;


/**
 * Created by Q on 2018/1/5.
 */

public class MovieDetailActivity extends BaseActivity implements TvRecyclerView.OnItemListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private TextView movie_name, media_current_num, media_total_num, time_tv, movie_tv;
    private LinearLayout loadingView;
    private RelativeLayout time_rl;
    private ImageView movie_iv, nothing_iv;
    private TvRecyclerView media_recycler;
    //    private RollPagerView mLoopViewPager;
//    private LoopAdapter mLoopAdapter;
    private List<MediaDetailBean> detailList;
    private MediaRecyclerViewAdapter adapter;
    private MediaDetailPresenter detailPresenter;
    private String categoryId;
    private VideoPayStatusPresenter videoPayStatusPresenter;
    private int currentPage = 1;
    private final int pageSize = 100;//一页显示的多少
    private int parts;
    private boolean isFirst;
    private MainPageInfo info;
    private final byte TIMEOUT_WHAT = 1;
    private static final int PLAY = 0;
    private static final int DELAY = 1;
    private static final int SECONDS = 2;
    private static final int ADVERT_DELAY = 3;
    private static final int DIALOG_DELAY = 4;
    private InstallDialog progressDialog;
    private String roomId;
    private MyHandler myHandler;
    private int currentPosition = 0;
    private PayDialog payDialog;
    private VideoPayPresenter videoPayPresenter;
    private PayMentChargeDialog payMentChargeDialog;

    private List<String> pictureAdInfos;
    private List<Integer> pictureAdIds;
    //    private PictureAdPresenter pictureAdPresenter;
    private VideoView movie_video;
    private String video_tag = "picture";//区别播放的是广告还是预览片
    private byte seconds = 15;
    private boolean permissions = false;//视频播放权限
    private int previewBegin = 10 * 60 * 1000;
    private int previewLength = 5 * 60 * 1000;
    private int advertPosition = 0;
    private String onlineAdvertPath;
    private boolean isCompleted = false;//用来判断预览视频是否播放完成
    private int currentAdvertId = -1;
    private boolean isAdvPlayFirst = false;//判断是否
    private ImageView[] imageViews;
    private TextView tv_advert_msg;
    private int advertPhotoPosition = 0;
    private boolean isDownload = true;//是否下载完成
    private boolean iskeyEnable = false;//
    private boolean isenterEnable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.verticalscrolltext = "movie";
        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_START);
        bundle.putString("behaviour", Constants.MOVIE);
        ServiceUtils.startService(PageRecordService.class, bundle);
        isFirst = true;
        categoryId = getIntent().getStringExtra("categoryId");
        info = Constants.mainPageInfo;
        roomId = (String) SPUtils.get(this, Constants.ROOM_ID, "");
        myHandler = new MyHandler(this);
        setContentView(R.layout.activity_movie_detail);
        imageViews = new ImageView[2];
        initView();
        initData();
    }

    private void initView() {
        movie_name = (TextView) findViewById(R.id.movie_name);
        time_tv = (TextView) findViewById(R.id.time_tv);
        movie_iv = (ImageView) findViewById(R.id.movie_iv);
        nothing_iv = (ImageView) findViewById(R.id.nothing_iv);
        ShowImageUtils.showImageView(this, R.drawable.main_bg, movie_iv);
        media_recycler = (TvRecyclerView) findViewById(R.id.media_recycler);
        media_current_num = (TextView) findViewById(R.id.media_current_num);
        media_total_num = (TextView) findViewById(R.id.media_total_num);
        movie_tv = (TextView) findViewById(R.id.movie_tv);
        loadingView = (LinearLayout) findViewById(R.id.LoadingView);
        movie_tv.setVisibility(View.GONE);
        time_rl = (RelativeLayout) findViewById(R.id.time_rl);
        tv_advert_msg = (TextView) findViewById(R.id.tv_advert_msg);
        tv_advert_msg.setVisibility(View.GONE);
        media_recycler.setHasFixedSize(true);
        media_recycler.setInterceptKeyEvent(true);
        media_recycler.setSelectedItemAtCentered(true);
        media_recycler.setOnItemListener(this);

        imageViews[0] = (ImageView) findViewById(R.id.iv_advert1);
        imageViews[1] = (ImageView) findViewById(R.id.iv_advert2);

        movie_video = (VideoView) findViewById(R.id.movie_video);

        movie_video.setOnInfoListener(this);
        movie_video.setOnErrorListener(this);
        movie_video.setOnCompletionListener(this);
        movie_video.setOnPreparedListener(this);
    }


    private void initData() {
        pictureAdInfos = new ArrayList<>();
        pictureAdIds = new ArrayList<>();
        detailList = new ArrayList<>();
        videoPayStatusPresenter = new VideoPayStatusPresenter(this, new VideoPayStatusListener());
        detailPresenter = new MediaDetailPresenter(MovieDetailActivity.this, new DetailListener());
        if (categoryId.equals("99")) {
            detailPresenter.request(Constants.DEMAND_Deail_TAG, "", "1", "", currentPage + "", pageSize + "");
        } else {
            detailPresenter.request(Constants.DEMAND_Deail_TAG, categoryId, "", "", currentPage + "", pageSize + "");
        }
        if (Constants.mainPageInfo.getAdSetting().getMediaDetailVideo() == 1) {
            //下载30秒视频
            String videoPath1 = FileUtils.getAdvert15Path() + Constants.key[advertPosition % Constants.key.length] + ".mp4";
            String videoPath2 = FileUtils.getAdvert15Path() + Constants.key[(advertPosition + 1) % Constants.key.length] + ".mp4";
            if (new File(videoPath1).exists()) {
                video_tag = "picture";
                isAdvPlayFirst = true;
                movie_video.setVideoPath(videoPath1);
                movie_video.start();
                Log.e("movie_video", "movie_video");
                AdvertDownloadPresenter presenter = new AdvertDownloadPresenter(videoPath1, new MyAdvertListener());
                presenter.request(this, 15, roomId);
            } else if (new File(videoPath2).exists()) {
                video_tag = "picture";
                isAdvPlayFirst = true;
                movie_video.setVideoPath(videoPath2);
                movie_video.start();
                Log.e("movie_video", "movie_video");
                AdvertDownloadPresenter presenter = new AdvertDownloadPresenter(videoPath2, new MyAdvertListener());
                presenter.request(this, 15, roomId);
            } else {
                AdvertDownloadPresenter presenter = new AdvertDownloadPresenter(new MyAdvertListener());
                presenter.request(this, 15, roomId);
            }
        } else {
//            AdvertDownloadPresenter presenter = new AdvertDownloadPresenter(new MyAdvertListener());
//            presenter.request(this, 15, roomId);
        }
        if (Constants.mainPageInfo.getAdSetting().getMediaDetailImage() == 1) {
            PictureAdPresenter pictureAdPresenter = new PictureAdPresenter(MovieDetailActivity.this, new PictureAdListener());
            pictureAdPresenter.request(this, 2, roomId);
        } else {
            nothing_iv.setVisibility(View.VISIBLE);
            time_rl.setVisibility(View.GONE);
            try {
                ShowImageUtils.showImageView(this, Constants.mainPageInfo.getAdSetting().getMediaDetailImageFile().getPath(), nothing_iv);
            } catch (Exception e) {
                ShowImageUtils.showImageView(this, R.drawable.main_bg, nothing_iv);
            }
        }
        adapter = new MediaRecyclerViewAdapter(MovieDetailActivity.this, detailList);
        media_recycler.setAdapter(adapter);
    }

    /**
     * 获取视频支付情况
     */
    private void videoPayStatus() {
        if (detailList.get(currentPosition).getVideoPayment() == 1) {
            if (detailList.get(currentPosition).getPrice() > 0f) {
                if (isenterEnable) {
                    initDialog(MovieDetailActivity.this, getStrings(R.string.loading));
                }
                videoPayStatusPresenter.request(this, roomId, info.getCustomerId(), detailList.get(currentPosition).getId());
                myHandler.sendEmptyMessageDelayed(TIMEOUT_WHAT, 5000);
            } else {
                permissions = true;
                if (isenterEnable) {
                    DialogUtils.dismissDialog(progressDialog);
                    ClickData();
                }
            }
        } else {
            permissions = true;
            if (isenterEnable) {
                DialogUtils.dismissDialog(progressDialog);
                ClickData();
            }
        }
    }

    /**
     * 初始化电影名
     */
    private void initViewData() {
        if (detailList.size() > 0 && detailList.get(0) != null) {
            movie_name.setText(detailList.get(currentPosition).getName());
            media_current_num.setText(currentPosition + 1 + "");
        }
    }

    /**
     * 播放15秒广告视频
     */
    private void initVideoView() {
        String advertPath = FileUtils.getAdvert15Path() + Constants.key[advertPosition % Constants.key.length] + ".mp4";
        if (new File(advertPath).exists()) {
            video_tag = "picture";
            movie_video.setVideoPath(advertPath);
            loadingView.setVisibility(View.GONE);
            movie_tv.setVisibility(View.GONE);
            movie_video.start();
            if (isDownload) {
                isDownload = false;
                //请求下一条广告
                AdvertDownloadPresenter presenter = new AdvertDownloadPresenter(advertPath, new MyAdvertListener());
                presenter.request(this, 15, roomId);
            }

        } else {
            advertPath = FileUtils.getAdvert15Path() + Constants.key[(advertPosition + 1) % Constants.key.length] + ".mp4";
            if (new File(advertPath).exists()) {
                video_tag = "picture";
                movie_video.setVideoPath(advertPath);
                loadingView.setVisibility(View.GONE);
                movie_tv.setVisibility(View.GONE);
                movie_video.start();
                if (isDownload) {
                    isDownload = false;
                    //请求下一条广告
                    AdvertDownloadPresenter presenter = new AdvertDownloadPresenter(advertPath, new MyAdvertListener());
                    presenter.request(this, 15, roomId);
                }
            } else {
                time_rl.setVisibility(View.GONE);
                playVideo();
                if (isDownload) {
                    isDownload = false;
                    //请求下一条广告
                    AdvertDownloadPresenter presenter = new AdvertDownloadPresenter(advertPath, new MyAdvertListener());
                    presenter.request(this, 15, roomId);
                }
            }
        }
    }

    /**
     * 播放预览视频
     */
    private void playVideo() {
        try {
            if (detailList != null && detailList.size() != 0) {
                previewBegin = (int) detailList.get(currentPosition % detailList.size()).getPreviewBegin();
                previewLength = (int) detailList.get(currentPosition % detailList.size()).getPreviewLength();
                video_tag = "video";
                String url = detailList.get(currentPosition).getAttachments().get(0).getF_pathSvr();
                //设置加载显示加载框
                loadingView.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(getDimension(R.dimen.w_1046),getDimension(R.dimen.h_588));
                LayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                movie_video.setLayoutParams(LayoutParams);
                movie_video.setVideoPath(url);
                movie_video.seekTo(previewBegin);
                movie_video.start();
                time_rl.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }
    }

    public int getDimension(int id) {
        return (int) getResources().getDimension(id);
    }

    /**
     * 视频统计
     */
    private void adStatistics() {
        try {
            AdvertInfo advertInfo = DBUtils.find(MovieDetailActivity.this, AdvertInfo.class, "id", "=", FileUtils.getAdvert15Path() + Constants.key[advertPosition % Constants.key.length]);
            if (advertInfo != null) {
                currentAdvertId = advertInfo.getCurrentAdvId();
            }
            if (currentAdvertId != -1) {
                AdRecordPresenter adRecordPresenter = new AdRecordPresenter();
                adRecordPresenter.request(this, currentAdvertId, 1, roomId, 0, Math.abs(15 - seconds));
            }
        } catch (Exception e) {
            Log.e("adStatistics", "视频广告统计");

        }
    }


    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY://预览视频播放
                    int playTime = movie_video.getCurrentPosition();
                    if (playTime >= (previewBegin + previewLength)) {
                        handler2.removeMessages(PLAY);
//                        if (permissions) {
//                            playVideo(true, detailList.get(currentPosition));
//                        } else {
//                            movie_video.pause();
//                            isCompleted = true;
//                            if (videoPayPresenter == null) {
//                                videoPayPresenter = new VideoPayPresenter(MovieDetailActivity.this, new VideoPayListener());
//                                videoPayPresenter.request(roomId, info.getCustomerId(), 0, detailList.get(currentPosition).getId());
//                            }
//                        }
                        playVideo();
                    } else {
                        handler2.removeMessages(PLAY);
                        handler2.sendEmptyMessageDelayed(PLAY, 300);
                    }
                    isShowingPayMentChargeDialog();
                    break;
                case DELAY://广告视频切换
                    videoPayStatus();
                    if (Constants.mainPageInfo.getAdSetting().getMediaDetailVideo() == 1) {
                        if (video_tag.equals("picture")) {
                            handler2.removeMessages(SECONDS);
                            adStatistics();
                            advertPosition++;
                        }
                        initVideoView();
                    } else {
                        playVideo();
                    }
                    break;
                case SECONDS://广告倒计时
                    String str;
                    str = getResources().getString(R.string.ad_left) + " " + seconds + " S";
                    time_rl.setVisibility(View.VISIBLE);
                    time_tv.setText(str);
                    seconds--;
                    if (seconds <= 0) {
                        time_rl.setVisibility(View.GONE);
                        handler2.removeMessages(SECONDS);
                    } else {
                        handler2.removeMessages(SECONDS);
                        handler2.sendEmptyMessageDelayed(SECONDS, 1000);
                    }
                    isShowingPayMentChargeDialog();
                    break;
                case ADVERT_DELAY:
                    showAdv();
                    break;
                case DIALOG_DELAY:
                    playVideo(true, detailList.get(currentPosition));
                    break;
            }
        }
    };

    @Override
    public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
        float translationY = itemView.getTranslationY();
        AnimationUtils.scaleXYTranslationAnimation(itemView, 300, 1.2f, 1.0f, translationY, 0);
    }

    @Override
    public void onItemSelected(TvRecyclerView parent, final View itemView, final int position) {
        currentPosition = position % detailList.size();
        if (isFirst) {
            isFirst = false;
            initViewData();
            videoPayStatus();
            if (Constants.mainPageInfo.getAdSetting().getMediaDetailVideo() == 0) {
                playVideo();
            }
            float translationY = itemView.getTranslationY();
            float height = itemView.getHeight() * 0.1f;
            AnimationUtils.scaleXYTranslationAnimation(itemView, 300, 1.0f, 1.2f, translationY, -height);

            return;
        }
        initViewData();
        float translationY = itemView.getTranslationY();
        float height = itemView.getHeight() * 0.1f;
        AnimationUtils.scaleXYTranslationAnimation(itemView, 300, 1.0f, 1.2f, translationY, -height);

        //广告操作
        handler2.removeMessages(PLAY);
        loadingView.setVisibility(View.GONE);
        movie_tv.setVisibility(View.GONE);
//        if (Constants.mainPageInfo.getAdSetting().getMediaDetailVideo() == 1) {
//            video_tag = "picture";
//            handler2.removeMessages(DELAY);
//            handler2.sendEmptyMessageDelayed(DELAY, 2000);
//        } else {
//            playVideo();
//        }
        isenterEnable = false;
        handler2.removeMessages(DELAY);
        handler2.sendEmptyMessageDelayed(DELAY, 2000);
    }

    @Override
    public void onReviseFocusFollow(TvRecyclerView parent, View itemView, int position) {
    }

    @Override
    public void onItemClick(TvRecyclerView parent, View itemView, int position) {
        if (info == null) {
            ToastUtils.showShort("主页信息获取失败,请返回主页重新获取!");
            return;
        }
        currentPosition = position % detailList.size();
        isenterEnable = true;
        videoPayStatus();
    }


    private void ClickData() {
        isenterEnable = false;
        if (permissions) {
            DialogUtils.dismissDialog(progressDialog);
            playVideo(true, detailList.get(currentPosition));
        } else {
            movie_video.pause();
            handler2.removeMessages(PLAY);
            if (video_tag.equals("picture")) {
                handler2.removeMessages(SECONDS);
            }
            VideoPayPresenter videoPayPresenter = new VideoPayPresenter(MovieDetailActivity.this, new VideoPayListener());
            videoPayPresenter.request(roomId, info.getCustomerId(), 0, detailList.get(currentPosition).getId());
        }
    }

    /************************************************************/

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            if (video_tag.equals("video")) {
                loadingView.setVisibility(View.GONE);
                movie_iv.setVisibility(View.GONE);
                String str;
                if (permissions) {
                    str = getResources().getString(R.string.proview_toast);
                } else {
                    if (LanguageUtils.getInstance().getLanguage(this).equals("en")) {
                        str = getResources().getString(R.string.proview_toast_after) + detailList.get(currentPosition).getPrice();
                    } else {
                        str = getResources().getString(R.string.proview_toast_front) + detailList.get(currentPosition).getPrice() + getResources().getString(R.string.proview_toast_after);
                    }

                }
                movie_tv.setVisibility(View.VISIBLE);
                movie_tv.setText(str);
                handler2.removeMessages(PLAY);
                handler2.sendEmptyMessage(PLAY);
            } else {
                movie_iv.setVisibility(View.GONE);
                seconds = 15;
                handler2.removeMessages(SECONDS);
                handler2.sendEmptyMessage(SECONDS);
            }
            isShowingPayMentChargeDialog();

        }
        return false;
    }

    private void isShowingPayMentChargeDialog() {
        if (payMentChargeDialog != null && payMentChargeDialog.isShowing()) {
            movie_video.pause();
            handler2.removeMessages(PLAY);
            if (video_tag.equals("picture")) {
                handler2.removeMessages(SECONDS);
            }
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e("VIDEO_VIEW", "what" + what);
        loadingView.setVisibility(View.GONE);
        movie_iv.setVisibility(View.VISIBLE);
        try {
            ShowImageUtils.showImageViewNoMemory(this, detailList.get(currentPosition).getBackgroundImage().getPath(), movie_iv, 200);
        } catch (Exception e) {
            ShowImageUtils.showImageView(this, R.drawable.main_bg, movie_iv);
        }
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (video_tag.equals("picture")) {
            adStatistics();
            advertPosition++;
            playVideo();
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                mp.start();
                Log.e("VIDEO_VIEW", "SEEKok");
            }
        });
    }

    /************************************************************/

    private class DetailListener implements IPresenterBase<List<MediaDetailBean>> {

        @Override
        public void onSuccess(BaseModel<List<MediaDetailBean>> dataList) {
            if (dataList.data.size() <= 0) {
                ToastUtils.showShort("暂无影片!");
                detailList.clear();
                adapter.notifyDataSetChanged();
                return;
            }
            if (dataList.data.size() == 1 && dataList.data.get(0) == null) {
                ToastUtils.showShort("详情格式错误,请检查服务端信息!");
                finish();
                overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
                return;
            }
            if (dataList.data.size() > 0 && dataList.data.get(0) != null) {
                detailList.clear();
                detailList.addAll(dataList.data);
                adapter.notifyDataSetChanged();

            }
            media_total_num.setText("/" + (detailList.size()));
            media_recycler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    media_recycler.setSelection(3);
                }
            }, 10);
        }

        @Override
        public void onError() {
            finish();
            overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
        }
    }

    /************************************************************/
    private void playVideo(boolean isLastParts, MediaDetailBean mediaDetailBean) {
        if (isLastParts) {
            //获取数据库中的最近的集数
            parts = getVideoParts(String.valueOf(mediaDetailBean.getId()));
        } else {
            parts = 0;
        }
        video_tag = "video";
        handler2.removeMessages(SECONDS);
        adStatistics();
        Intent intent = new Intent(this, PlayVideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie_detail", mediaDetailBean);
        bundle.putInt("position", parts);
        bundle.putInt("payStatus", 1);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //获取进度
    private int getVideoParts(String videoId) {
        WhereBuilder builder = WhereBuilder.b("videoId", "=", videoId);
        VideoPlayParts videoPlayParts = DBUtils.find(this, VideoPlayParts.class, builder);
        if (videoPlayParts == null) {
            return 0;
        } else {
            return videoPlayParts.getCurrentParts();
        }
    }

    /************************************************************/

    private class PictureAdListener implements IPresenterBase<List<PictureAdInfo>> {

        @Override
        public void onSuccess(BaseModel<List<PictureAdInfo>> dataList) {
//            if (dataList.data.size() <= 0) {
//                ToastUtils.showShort("暂无广告!");
//                pictureAdInfos.clear();
//                return;
//            }
//            if (dataList.data.size() == 1 && dataList.data.get(0) == null) {
//                ToastUtils.showShort("广告格式错误,请检查服务端信息!");
//                finish();
//                overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
//                return;
//            }
//            if (dataList.data.size() > 0 && dataList.data.get(0) != null) {
//                pictureAdInfos.clear();
//                pictureAdInfos.addAll(dataList.data);
//                nothing_iv.setVisibility(View.GONE);
//                ad_rl.setVisibility(View.VISIBLE);
//            }
            try {
                List<PictureAdInfo> data = dataList.data;
                Log.e("Picture", "data有数据");
                for (int i = 0; i < data.size(); i++) {
                    pictureAdInfos.add(data.get(i).getFileUpload().getPath());
                    pictureAdIds.add(data.get(i).getId());
                }
                nothing_iv.setVisibility(View.GONE);
                tv_advert_msg.setVisibility(View.VISIBLE);
                handler2.removeMessages(ADVERT_DELAY);
                handler2.sendEmptyMessageDelayed(ADVERT_DELAY, 50);
            } catch (Exception e) {
                video_tag = "video";
            }
        }

        @Override
        public void onError() {
            nothing_iv.setVisibility(View.VISIBLE);
            time_rl.setVisibility(View.GONE);
            video_tag = "video";
            tv_advert_msg.setVisibility(View.GONE);
            try {
                ShowImageUtils.showImageView(MovieDetailActivity.this, Constants.mainPageInfo.getAdSetting().getMediaDetailImageFile().getPath(), nothing_iv);
            } catch (Exception e) {
                ShowImageUtils.showImageView(MovieDetailActivity.this, R.drawable.main_bg, nothing_iv);
            }
            overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
        }
    }

    /************************************************************/

    private class MyAdvertListener implements IPresenterDownloadBase<List<AdvertVideoInfo>> {
        private int advertId = -1;//广告ID

        @Override
        public void onSuccess(BaseModel<List<AdvertVideoInfo>> dataList) {
            onlineAdvertPath = dataList.data.get(0).getFileUpload().getPath();
            advertId = dataList.data.get(0).getId();
            Log.e("adStatistics", "advertId:" + advertId);
        }

        @Override
        public void onDownLoadSuccess(String fileName) {
            isDownload = true;
            String id = FileUtils.getAdvert15Path() + fileName;
            AdvertInfo advertInfo = DBUtils.find(MovieDetailActivity.this, AdvertInfo.class, "id", "=", id);
            if (advertInfo == null) {
                advertInfo = new AdvertInfo();
                advertInfo.setId(id);
                advertInfo.setCurrentAdvId(advertId);
                DBUtils.save(MovieDetailActivity.this, advertInfo);
            } else {
                advertInfo.setCurrentAdvId(advertId);
                DBUtils.update(MovieDetailActivity.this, advertInfo, "currentAdvId");
            }
            if (!isAdvPlayFirst) {
                isAdvPlayFirst = true;
                initVideoView();
            }

        }

        @Override
        public void onError() {
            String advertPath = FileUtils.getAdvert15Path() + Constants.key[advertPosition % Constants.key.length] + ".mp4";
            if (new File(advertPath).exists()) {
                video_tag = "picture";
                movie_video.setVideoPath(advertPath);
                movie_video.start();
                if (isDownload) {
                    isDownload = false;
                    //请求下一条广告
                    AdvertDownloadPresenter presenter = new AdvertDownloadPresenter(advertPath, new MyAdvertListener());
                    presenter.request(this, 15, roomId);
                }
            }
        }
    }


    /************************************************************/
    private void initDialog(Activity activity, String msg) {
        progressDialog = new InstallDialog(activity, msg);
        DialogUtils.showDialog(progressDialog);
    }

    private static class MyHandler extends Handler {
        private WeakReference<Activity> weakReference;

        MyHandler(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MovieDetailActivity activity = (MovieDetailActivity) weakReference.get();
            if (msg.what == activity.TIMEOUT_WHAT) {
                activity.dismissDialog();
            }

        }
    }

    /************************************************************/
    private class VideoPayStatusListener implements IPresenterBase<List<VideoPayBean>> {

        @Override
        public void onSuccess(BaseModel<List<VideoPayBean>> dataList) {
            List<VideoPayBean> data = dataList.data;
            if (data != null && data.get(0) != null) {
                if (data.get(0).getStatus() == 1) {//已支付
                    permissions = true;
                } else {
                    permissions = false;
                }
                myHandler.removeMessages(TIMEOUT_WHAT);
                if (isenterEnable) {
                    ClickData();
                }
            } else {
                ToastUtils.showShort("视频信息获取失败,请检查网络连接");
                permissions = false;
                if (isenterEnable) {
                    DialogUtils.dismissDialog(progressDialog);
                    isenterEnable = false;
                }
                myHandler.removeMessages(TIMEOUT_WHAT);
            }
        }

        @Override
        public void onError() {
            if (isenterEnable) {
                DialogUtils.dismissDialog(progressDialog);
                isenterEnable = false;
            }
            ToastUtils.showShort("视频信息获取失败,请检查网络连接");
            myHandler.removeMessages(TIMEOUT_WHAT);
        }
    }

    private class VideoPayListener implements IPresenterBase<List<VideoPayBean>> {

        @Override
        public void onSuccess(BaseModel<List<VideoPayBean>> dataList) {
            myHandler.removeMessages(TIMEOUT_WHAT);
            DialogUtils.dismissDialog(progressDialog);
            List<VideoPayBean> data = dataList.data;
            if (data != null && data.get(0) != null) {
                VideoPayBean videoPayBean = data.get(0);
                if (videoPayBean.getStatus() == 1) {//已支付
                    OkHttpUtils.cancel();
                    DialogUtils.dismissDialog(payDialog);
                } else {
                    //未支付
                    showPayMentChargeDialog(videoPayBean);
                }

            } else {
                ToastUtils.showShort("服务器出现问题");
                finish();
                overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
            }
        }

        @Override
        public void onError() {
            DialogUtils.dismissDialog(progressDialog);
            ToastUtils.showShort("服务器出现问题");
            finish();
            overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
        }

    }

    /**
     * 选择支付方式弹窗
     */
    private void showPayMentChargeDialog(final VideoPayBean videoPayBean) {
        if (payMentChargeDialog == null || !payMentChargeDialog.isShowing()) {
            payMentChargeDialog = new PayMentChargeDialog(MovieDetailActivity.this, new IDialogListener() {
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
                    } else if (tag.equals(Constants.THIRD)) {
                        accountDialog(videoPayBean);
                    }
                }
            }, "￥" + String.valueOf(detailList.get(currentPosition).getPrice()), 1, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancelData();
                    dialog.dismiss();
                }
            });
            DialogUtils.dismissDialog(progressDialog);
            DialogUtils.showDialog(payMentChargeDialog);
        }

    }

    private void accountDialog(final VideoPayBean videoPayBean) {
        CustomDialog dialog = new CustomDialog(this, new IDialogClick() {
            @Override
            public void onClick(CustomDialog dialog, String tag) {
                if (tag.equals(Constants.CONFIRM)) {
                    if (videoPayBean != null) {
                        DialogUtils.dismissDialog(dialog);
                        initDialog(MovieDetailActivity.this, getStrings(R.string.loading));
                        //请求房账支付
                        AccountPayPresenter payPresenter = new AccountPayPresenter(new IPresenterBase() {
                            @Override
                            public void onSuccess(BaseModel dataList) {
                                DialogUtils.dismissDialog(payDialog);
                                DialogUtils.dismissDialog(progressDialog);
                                DialogUtils.dismissDialog(payMentChargeDialog);
                                ToastUtils.showShort("支付成功!");
                                playVideo(true, detailList.get(currentPosition));
                            }

                            @Override
                            public void onError() {
                                DialogUtils.dismissDialog(progressDialog);
                                ToastUtils.showShort("网络异常!");
                            }
                        });
                        payPresenter.request(videoPayBean.getOrderNum());
                    }
                } else if (tag.equals(Constants.CANCEL)) {
                    DialogUtils.dismissDialog(dialog);
                }
            }
        }, R.string.account_toast);
        DialogUtils.showDialog(dialog);
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
                payDialog = new PayDialog(MovieDetailActivity.this, payment, bitmap
                        , new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        videoPayPresenter = null;
                        payMentChargeDialog = null;
                        payDialog = null;
                        dialog.dismiss();
                    }
                }, "￥" + detailList.get(currentPosition).getPrice(), 0);
                DialogUtils.showDialog(payDialog);
                OkGo.getInstance().cancelAll();
            } catch (Exception e) {
                ToastUtils.showShort("二维码信息获取失败!");
                finish();
                overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
            }
        } else if (payDialog.isShowing()) {
            OkGo.getInstance().cancelAll();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoviePayStatus(MoviePayStatus status) {
        if (payDialog != null && payDialog.isShowing() && status.isStatus()) {
            DialogUtils.dismissDialog(payDialog);
            DialogUtils.dismissDialog(payMentChargeDialog);
            ToastUtils.showShort("支付成功!");
            handler2.sendEmptyMessageDelayed(DIALOG_DELAY,100);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoviePayTimeOut(String timeOut) {
        if (payDialog != null && payDialog.isShowing() && timeOut.equals("timeOut")) {
            DialogUtils.dismissDialog(payDialog);
            DialogUtils.dismissDialog(payMentChargeDialog);
            cancelData();
        }
    }


    private void dismissDialog() {
        DialogUtils.dismissDialog(progressDialog);
    }

    /**
     * 选择支付弹窗和二维码弹窗消失事件处理
     */
    private void cancelData() {
        time_rl.setVisibility(View.GONE);
        videoPayPresenter = null;
        payMentChargeDialog = null;
        payDialog = null;
        if (video_tag.equals("picture")) {
            movie_video.start();
            handler2.sendEmptyMessage(SECONDS);
        } else {
            if (isCompleted) {
                playVideo();
            } else {
                movie_video.start();
                handler2.removeMessages(PLAY);
                handler2.sendEmptyMessage(PLAY);
            }
        }
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Bundle bundle = new Bundle();
            bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_END);
            ServiceUtils.startService(PageRecordService.class, bundle);
            myHandler.removeCallbacks(runnable);
            myHandler.postDelayed(runnable, 1000 * 60);
        }
    };

    /************************************************************/

    @Override
    protected void onResume() {
        super.onResume();
        handler2.removeMessages(ADVERT_DELAY);
        handler2.sendEmptyMessageDelayed(ADVERT_DELAY, 1000);
        if (video_tag.equals("video")) {
            time_rl.setVisibility(View.GONE);
            videoPayStatus();
            playVideo();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        myHandler.removeCallbacks(runnable);
        myHandler.postDelayed(runnable, 1000 * 60);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        myHandler.removeCallbacks(runnable);
        handler2.removeMessages(PLAY);
        handler2.removeMessages(SECONDS);
        handler2.removeMessages(DELAY);
        handler2.removeMessages(ADVERT_DELAY);
        EventBus.getDefault().unregister(this);
        OkHttpUtils.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_END);
        ServiceUtils.startService(PageRecordService.class, bundle);
        myHandler.removeCallbacks(runnable);
        videoPayPresenter = null;
        payMentChargeDialog = null;
        payDialog = null;
        myHandler.removeCallbacksAndMessages(null);
        handler2.removeCallbacksAndMessages(null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        movie_video.stopPlayback();
    }

    /**
     * 视频详情DialogFragment消失事件处理
     */
    public void onDissmissMovieDetailDialogFragment() {
        if (video_tag.equals("picture")) {
            if (!movie_video.isPlaying()) {
                movie_video.start();
            }
            handler2.removeMessages(SECONDS);
            handler2.sendEmptyMessage(SECONDS);
        } else {
            if (isCompleted) {
                playVideo();
            } else {
                if (!movie_video.isPlaying()) {
                    movie_video.start();
                }
            }
        }
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {


        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!iskeyEnable) {
                iskeyEnable = true;
            } else {
                return true;
            }
        }

        if (event.getAction() == KeyEvent.ACTION_UP) {
            iskeyEnable = false;
        }


        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            if (detailList.size() == 0) {
                return true;
            }
            MediaDetailBean bean = detailList.get(currentPosition);
            MovieDetailDialogFragment movieDetailDialogFragment = new MovieDetailDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("MediaDetail", bean);
            movieDetailDialogFragment.setArguments(bundle);
            movieDetailDialogFragment.show(getSupportFragmentManager(), "movieDetailDialogFragment");
            movie_video.pause();
            if (Constants.mainPageInfo.getAdSetting().getMediaDetailVideo() == 1 && video_tag.equals("picture")) {
                handler2.removeMessages(SECONDS);
            }
            return true;
        }

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE) {
                if (detailList.size() == 0) {

                } else {
                    if (Constants.mainPageInfo.getAdSetting().getMediaDetailVideo() == 1 && video_tag.equals("picture")) {
                        adStatistics();
                    }
                    finish();
                    overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
                }
            }
        }

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                if (detailList.size() == 0) {
                    return true;
                }
            }
        }

        return super.dispatchKeyEvent(event);

    }

    private void showAdv() {
        try {
            if (Constants.mainPageInfo.getAdSetting().getMediaDetailImage() == 1) {
                advertPhotoPosition++;
                imageViews[advertPhotoPosition % imageViews.length].bringToFront();
                ShowImageUtils.showImageViewNoMemory(this, pictureAdInfos.get(advertPhotoPosition % pictureAdInfos.size()), imageViews[advertPhotoPosition % imageViews.length], 2000);

                AdRecordPresenter adRecordPresenter = new AdRecordPresenter();
                adRecordPresenter.request(this, pictureAdIds.get(advertPhotoPosition % pictureAdIds.size()), 0, roomId, 2, 0);

                handler2.removeMessages(ADVERT_DELAY);
                handler2.sendEmptyMessageDelayed(ADVERT_DELAY, 10000);
                tv_advert_msg.bringToFront();
                tv_advert_msg.setText(R.string.advert);
                tv_advert_msg.setVisibility(View.VISIBLE);
            } else {
                tv_advert_msg.setVisibility(View.GONE);
                ShowImageUtils.showImageView(this, Constants.mainPageInfo.getAdSetting().getMediaPlayImageFile().getPath(), imageViews[0]);
            }
        } catch (Exception e) {
            tv_advert_msg.setVisibility(View.GONE);
            ShowImageUtils.showImageView(this, Constants.mainPageInfo.getAdSetting().getMediaPlayImageFile().getPath(), imageViews[0]);
        }

    }
}
