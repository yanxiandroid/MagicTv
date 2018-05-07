package com.yht.iptv.view.movie;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.yht.iptv.R;
import com.yht.iptv.adapter.MediaRecyclerViewAdapter;
import com.yht.iptv.callback.IDialogClick;
import com.yht.iptv.callback.IDialogListener;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.MainPageInfo;
import com.yht.iptv.model.MediaDetailBean;
import com.yht.iptv.model.MoviePayStatus;
import com.yht.iptv.model.VideoPayBean;
import com.yht.iptv.model.VideoPlayParts;
import com.yht.iptv.presenter.AccountPayPresenter;
import com.yht.iptv.presenter.MediaDetailPresenter;
import com.yht.iptv.presenter.VideoPayPresenter;
import com.yht.iptv.presenter.VideoPayStatusPresenter;
import com.yht.iptv.service.PageRecordService;
import com.yht.iptv.tools.CustomDialog;
import com.yht.iptv.tools.InstallDialog;
import com.yht.iptv.tools.PayDialog;
import com.yht.iptv.tools.PayMentChargeDialog;
import com.yht.iptv.tools.VerticalScrollTextView;
import com.yht.iptv.utils.AnimationUtils;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.DBUtils;
import com.yht.iptv.utils.DialogUtils;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Q on 2017/10/13.
 * 电影详情页
 */

public class MediaDetailActivity extends BaseActivity implements TvRecyclerView.OnItemListener {
    private ImageView media_image, media_selected_img;
    private TextView media_name, media_zone, media_type, media_director, media_actor, media_current_num, media_total_num;
    private VerticalScrollTextView media_introduction;
    //    private RelativeLayout media_name_rl;
    private ImageView media_mask_bottom, media_mask_right;
    //        private ScrollTextView media_introduction;
    private TvRecyclerView media_recycler;

    private List<MediaDetailBean> detailList;
    private MediaRecyclerViewAdapter adapter;
    private MediaDetailPresenter detailPresenter;
    private String categoryId;

    private int currentPage = 1;
    private final int pageSize = 100;//一页显示的多少
    private boolean isKeyEnable;
    private int parts;
    private boolean isFirst;
    private MainPageInfo info;
    private final byte TIMEOUT_WHAT = 1;

    private static final int DELAY = 1;
    private InstallDialog progressDialog;
    private String roomId;
    private MyHandler myHandler;
    private int currentPosition;
    private PayDialog payDialog;
    private PayMentChargeDialog payMentChargeDialog;
    private TextView tv_pay_info;
    //    private TvRecyclerViewBridge mRecyclerViewBridge;


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
        roomId = (String) SPUtils.get(this, Constants.ROOM_ID,"");
        myHandler = new MyHandler(this);
        setContentView(R.layout.activity_media_detail);
        initView();
        initData();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Bundle bundle = new Bundle();
            bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_END);
            ServiceUtils.startService(PageRecordService.class, bundle);
            myHandler.removeCallbacks(runnable);
            myHandler.postDelayed(runnable,1000 * 60);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        myHandler.removeCallbacks(runnable);
        myHandler.postDelayed(runnable,1000 * 60);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        myHandler.removeCallbacks(runnable);
        EventBus.getDefault().unregister(this);
    }

    private void initView() {

        media_image = (ImageView) findViewById(R.id.media_image);
        media_selected_img = (ImageView) findViewById(R.id.media_selected_img);
        media_mask_bottom = (ImageView) findViewById(R.id.media_mask_bottom);
        media_name = (TextView) findViewById(R.id.media_name);
        media_zone = (TextView) findViewById(R.id.media_zone);
        media_type = (TextView) findViewById(R.id.media_type);
        media_director = (TextView) findViewById(R.id.media_director);
        media_actor = (TextView) findViewById(R.id.media_actor);
        media_current_num = (TextView) findViewById(R.id.media_current_num);
        media_total_num = (TextView) findViewById(R.id.media_total_num);
//        media_name_rl = (RelativeLayout) findViewById(R.id.media_name_rl);
        media_mask_right = (ImageView) findViewById(R.id.media_mask_right);
//        media_mask_top = (ImageView) findViewById(R.id.media_mask_top);
        media_introduction = (VerticalScrollTextView) findViewById(R.id.media_introduction);
//        media_introduction = (ScrollTextView) findViewById(R.id.media_introduction);
        media_recycler = (TvRecyclerView) findViewById(R.id.media_recycler);
        tv_pay_info = (TextView) findViewById(R.id.tv_pay_info);
        ShowImageUtils.showImageView(MediaDetailActivity.this, R.drawable.media_mask_bottom, media_mask_bottom);
        media_recycler.setHasFixedSize(true);
        media_recycler.setInterceptKeyEvent(true);
        media_recycler.setSelectedItemAtCentered(true);
        media_recycler.setOnItemListener(this);
//        initMainUpView();
    }


    private void initData() {

        detailList = new ArrayList<>();
        detailPresenter = new MediaDetailPresenter(MediaDetailActivity.this, new DetailListener());
        OkHttpUtils.cancel(Constants.DEMAND_Deail_TAG);
        if (categoryId.equals("99")) {
            detailPresenter.request(Constants.DEMAND_Deail_TAG, "", "1", "", currentPage + "", pageSize + "");
        } else {
            detailPresenter.request(Constants.DEMAND_Deail_TAG, categoryId, "", "", currentPage + "", pageSize + "");
        }

        adapter = new MediaRecyclerViewAdapter(MediaDetailActivity.this, detailList);
        media_recycler.setAdapter(adapter);

    }


    @Override
    public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
        float translationY = itemView.getTranslationY();
        AnimationUtils.scaleXYTranslationAnimation(itemView, 300, 1.2f, 1.0f, translationY, 0);
//        mRecyclerViewBridge.setUnFocusView(itemView);
    }

    @Override
    public void onItemSelected(TvRecyclerView parent, final View itemView, final int position) {
        if (isFirst) {
            isFirst = false;
//            mRecyclerViewBridge.setFocusView(itemView,1.0f);
            initViewData(position);
            float translationY = itemView.getTranslationY();
            float height = itemView.getHeight() * 0.1f;
            AnimationUtils.scaleXYTranslationAnimation(itemView, 300, 1.0f, 1.2f, translationY, -height);

            return;
        }
//        mRecyclerViewBridge.setFocusView(itemView,1.0f);
        //执行渐变动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(media_image, "alpha", 1f, 0.02f);
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                initViewData(position);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        float translationY = itemView.getTranslationY();
        float height = itemView.getHeight() * 0.1f;
        AnimationUtils.scaleXYTranslationAnimation(itemView, 300, 1.0f, 1.2f, translationY, -height);
    }

    @Override
    public void onReviseFocusFollow(TvRecyclerView parent, View itemView, int position) {

    }

    @Override
    public void onItemClick(TvRecyclerView parent, View itemView, int position) {
        if(info == null){
            ToastUtils.showShort("主页信息获取失败,请返回主页重新获取!");
            return;
        }
        currentPosition = position % detailList.size();
        if(detailList.get(currentPosition).getVideoPayment() == 1) {
            if(detailList.get(currentPosition).getComboPrice() > 0f) {
                VideoPayStatusPresenter videoPayStatusPresenter = new VideoPayStatusPresenter(this, new VideoPayStatusListener());
                videoPayStatusPresenter.request(this, roomId, info.getCustomerId(), detailList.get(currentPosition).getId());
//            getVideoPayStatus = "first";
                initDialog(this, getStrings(R.string.loading));
                myHandler.sendEmptyMessageDelayed(TIMEOUT_WHAT, 5000);
            }else{
                playVideo(true,detailList.get(currentPosition));
            }
        }else{
            playVideo(true,detailList.get(currentPosition));
        }
//        playVideo(true, detailList.get(position % detailList.size()));
    }


    /*****************************************************************/

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
        progressDialog = new InstallDialog(activity,msg);
        DialogUtils.showDialog(progressDialog);
    }

    private static class MyHandler extends Handler {
        private WeakReference<Activity> weakReference;

        MyHandler(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MediaDetailActivity activity = (MediaDetailActivity) weakReference.get();
            if (msg.what == activity.TIMEOUT_WHAT) {
                activity.dismissDialog();
            }

        }
    }

    private void dismissDialog(){
        DialogUtils.dismissDialog(progressDialog);
    }

    private class VideoPayStatusListener implements IPresenterBase<List<VideoPayBean>> {

        @Override
        public void onSuccess(BaseModel<List<VideoPayBean>> dataList) {
            List<VideoPayBean> data = dataList.data;
            if (data != null && data.get(0) != null) {
                if (data.get(0).getStatus() == 1) {//已支付
                    myHandler.removeMessages(TIMEOUT_WHAT);
                    DialogUtils.dismissDialog(progressDialog);
                    //跳转
                    playVideo(true,detailList.get(currentPosition));
                } else {
                    VideoPayPresenter videoPayPresenter = new VideoPayPresenter(MediaDetailActivity.this, new VideoPayListener());
                    videoPayPresenter.request(roomId, info.getCustomerId(), 0, detailList.get(currentPosition).getId());
                }
            } else {
                ToastUtils.showShort("视频信息获取失败,请检查网络连接");
                DialogUtils.dismissDialog(progressDialog);
                myHandler.removeMessages(TIMEOUT_WHAT);
            }
        }

        @Override
        public void onError() {
            DialogUtils.dismissDialog(progressDialog);
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
//                    mHandler.removeMessages(GET_STATUS);
                    DialogUtils.dismissDialog(payDialog);
                } else {
                    //未支付
                    showPayMentChargeDialog(videoPayBean);
                }

            } else {
                ToastUtils.showShort("服务器出现问题");
                finish();
                overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
            }
        }

        @Override
        public void onError() {
            DialogUtils.dismissDialog(progressDialog);
            ToastUtils.showShort("服务器出现问题");
            finish();
            overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
        }

    }

    /**
     * 选择支付方式弹窗
     */
    private void showPayMentChargeDialog(final VideoPayBean videoPayBean) {
        payMentChargeDialog = new PayMentChargeDialog(MediaDetailActivity.this, new IDialogListener() {
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
                } else if(tag.equals(Constants.THIRD)){
                    accountDialog(videoPayBean);
                }
            }
        }, String.valueOf(detailList.get(currentPosition).getComboPrice()),1, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        DialogUtils.showDialog(payMentChargeDialog);
    }

    private void accountDialog(final VideoPayBean videoPayBean){
        CustomDialog dialog = new CustomDialog(this, new IDialogClick() {
            @Override
            public void onClick(CustomDialog dialog, String tag) {
                if (tag.equals(Constants.CONFIRM)) {
                    if(videoPayBean != null) {
                        DialogUtils.dismissDialog(dialog);
                        initDialog(MediaDetailActivity.this,getStrings(R.string.loading));
                        //请求房账支付
                        AccountPayPresenter payPresenter = new AccountPayPresenter(new IPresenterBase() {
                            @Override
                            public void onSuccess(BaseModel dataList) {
                                DialogUtils.dismissDialog(payDialog);
                                DialogUtils.dismissDialog(progressDialog);
                                DialogUtils.dismissDialog(payMentChargeDialog);
                                ToastUtils.showShort("支付成功!");
                                playVideo(true,detailList.get(currentPosition));
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
                payDialog = new PayDialog(MediaDetailActivity.this, payment, bitmap
                        , new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }, "￥" + detailList.get(currentPosition).getPrice(), 0);
                DialogUtils.showDialog(payDialog);
                OkGo.getInstance().cancelAll();
//                mHandler.removeMessages(GET_STATUS);
//                mHandler.sendEmptyMessageDelayed(GET_STATUS, 1000);
            } catch (Exception e) {
                ToastUtils.showShort("二维码信息获取失败!");
                finish();
                overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
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
            ToastUtils.showShort("支付成功!");
            playVideo(true,detailList.get(currentPosition));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoviePayTimeOut(String timeOut) {
        if (payDialog != null && payDialog.isShowing() && timeOut.equals("timeOut")) {
            DialogUtils.dismissDialog(payDialog);
            DialogUtils.dismissDialog(payMentChargeDialog);
        }
    }


/************************************************************/

    private class DetailListener implements IPresenterBase<List<MediaDetailBean>> {

        @Override
        public void onSuccess(BaseModel<List<MediaDetailBean>> dataList) {
            if (dataList.data.size() <= 0) {
                ToastUtils.showShort("暂无影片!");
                detailList.clear();
                adapter.notifyDataSetChanged();
//                media_name_rl.setVisibility(View.GONE);
                media_mask_bottom.setVisibility(View.GONE);
                media_mask_right.setVisibility(View.GONE);
//                media_mask_top.setVisibility(View.GONE);
                return;
            }
            if (dataList.data.size() == 1 && dataList.data.get(0) == null) {
                ToastUtils.showShort("详情格式错误,请检查服务端信息!");
                finish();
                overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
                return;
            }
            if (dataList.data.size() > 0 && dataList.data.get(0) != null) {
//                dataList.data.add(0,new MediaDetailBean());
//                dataList.data.add(0,new MediaDetailBean());
//                dataList.data.add(0,new MediaDetailBean());
                detailList.clear();
                detailList.addAll(dataList.data);
                adapter.notifyDataSetChanged();

            }
            media_total_num.setText("/" + (detailList.size()));
            if(detailList.get(0).getVideoPayment() == 1 &&  detailList.get(0).getComboPrice() > 0f) {
                if(Constants.currentLanguage.equals("zh")) {
                    tv_pay_info.setText(detailList.get(0).getPrice() + "元看大片");
                }else{
                    tv_pay_info.setText("￥ " + detailList.get(0).getPrice());
                }
            }
            media_recycler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    media_recycler.setSelection(3);
                }
            }, 10);

        }

        @Override
        public void onError() {
//            media_name_rl.setVisibility(View.GONE);
            media_mask_bottom.setVisibility(View.GONE);
            media_mask_right.setVisibility(View.GONE);
//            media_mask_top.setVisibility(View.GONE);
            finish();
            overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
        }
    }


    private void playVideo(boolean isLastParts, MediaDetailBean mediaDetailBean) {
        if (isLastParts) {
            //获取数据库中的最近的集数
            parts = getVideoParts(String.valueOf(mediaDetailBean.getId()));
        } else {
            parts = 0;
        }
        Intent intent = new Intent(this, PlayVideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie_detail", mediaDetailBean);
        bundle.putInt("position", parts);
        bundle.putInt("payStatus",1);
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


    private void initViewData(int position) {
        media_image.setAlpha(1.0f);
        if (detailList.size() > 0 && detailList.get(0) != null) {
//            media_selected_img.setVisibility(View.VISIBLE);
            media_current_num.setText(position % detailList.size() + 1 + "");
            media_name.setText(detailList.get(position % detailList.size()).getName());//影名
            List<MediaDetailBean.District> district = detailList.get(position % detailList.size()).getDistrict();//地区
            media_zone.setText(getString(R.string.region) + ":" + getMovieZone(district));//地区
            media_director.setText(getString(R.string.director) + ":" + detailList.get(position % detailList.size()).getDirector());//导演
            List<MediaDetailBean.Category> type = detailList.get(position % detailList.size()).getCategory();//类型
            media_type.setText(getString(R.string.type) + ":" + getMovieType(type));//类型
            media_actor.setText(getString(R.string.stars) + ":" + detailList.get(position % detailList.size()).getStarred());//演员
            media_actor.setSelected(true);
            media_introduction.setText(getString(R.string.describe) + ":" + detailList.get(position % detailList.size()).getDescription());//简介
            media_introduction.setStart();
            if (detailList.get(position % detailList.size()).getBackgroundImage() != null && detailList.get(position % detailList.size()).getBackgroundImage().getPath() != null) {
//                ShowImageUtils.showImageView(MediaDetailActivity.this,detailList.get(position % detailList.size()).getBackgroundImage().getPath(),media_image);
                ShowImageUtils.showImageViewNoMemory(this, detailList.get(position % detailList.size()).getBackgroundImage().getPath(), media_image, 200);
            } else {
//                Glide.with(MediaDetailActivity.this).load(detailList.get(position % detailList.size()).getFileUpload().getPath()).into(media_image);
                ShowImageUtils.showImageView(this, detailList.get(position % detailList.size()).getFileUpload().getPath(), media_image);
            }
        }
    }


    /**
     * 获取影片地区
     *
     * @return
     */
    private String getMovieZone(List<MediaDetailBean.District> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                sb.append(list.get(i).getName());
                if (i < list.size() - 1) {
                    sb.append("/");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取影片类型
     *
     * @return
     */
    private String getMovieType(List<MediaDetailBean.Category> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                sb.append(list.get(i).getName());
                if (i < list.size() - 1) {
                    sb.append("/");
                }
            }
        }
        return sb.toString();
    }
//    private void initMainUpView(){
//        // 移动框
//        MainUpView mainUpView = (MainUpView) findViewById(R.id.mainUpView);
//        mainUpView.setEffectBridge(new TvRecyclerViewBridge());
//        mRecyclerViewBridge = (TvRecyclerViewBridge) mainUpView.getEffectBridge();
//        mRecyclerViewBridge.setUpRectResource(R.drawable.media_selected);
//        mainUpView.setDrawUpRectPadding(new Rect(0,-getDimension(R.dimen.h_175),0,getDimension(R.dimen.h_175)));
//        mRecyclerViewBridge.setTranDurAnimTime(200);
//    }


//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//
//        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
//            if (!isKeyEnable) {
//                isKeyEnable = true;
//            } else {
//                return true;
//            }
//        }
//
//        if (event.getAction() == KeyEvent.ACTION_UP) {
//            isKeyEnable = false;
//        }
//
//        return super.dispatchKeyEvent(event);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_END);
        ServiceUtils.startService(PageRecordService.class, bundle);
        myHandler.removeCallbacks(runnable);
        myHandler.removeCallbacksAndMessages(null);
        OkHttpUtils.cancel();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
