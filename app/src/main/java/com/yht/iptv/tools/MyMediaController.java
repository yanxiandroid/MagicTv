package com.yht.iptv.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pili.pldroid.player.IMediaController;
import com.yht.iptv.R;

import java.util.Locale;

/**
 * Created by admin on 2016/8/11.
 */
public class MyMediaController extends FrameLayout implements IMediaController {

    public static int sDefaultTimeout = 3000;
    private Context mContext;
    private LayoutInflater inflater;
    private View mAnchor;
    private View mRoot;
    private boolean mShowing;
    private TextView tv_start_time;
    private TextView tv_end_time;
    private SeekBar seekBar;
    private IMediaController.MediaPlayerControl mPlayer;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private long mDuration;
    private PopupWindow mWindow;
    private boolean isChangeState;
    private AudioManager mAM;

    public MyMediaController(Context context) {
        super(context);
        mContext = context;
        isChangeState = false;
        inflater = LayoutInflater.from(mContext);
        initFloatingWindow();
    }

    public void setSeekProgress(int pos){
        seekBar.setProgress(pos);
    }

    public void setChangedState(){
        isChangeState = true;
    }

    public void setNormalState(){
        isChangeState = false;
        mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS,1000);
    }

    public static void setTimeOut(int timeOut){
        sDefaultTimeout = timeOut;
    }

    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
    }

    @Override
    public void show() {
        show(sDefaultTimeout);
    }

    @Override
    public void show(int timeout) {
        if(timeout != -1) {
            if (!mShowing) {
                mShowing = true;
                int[] location = new int[2];
                if (mAnchor != null) {
                    mAnchor.getLocationOnScreen(location);
                    Rect anchorRect = new Rect(location[0], location[1],
                            location[0] + mAnchor.getWidth(), location[1]
                            + mAnchor.getHeight());

                    mWindow.showAtLocation(mAnchor, Gravity.BOTTOM,
                            anchorRect.left, 0);
                } else {
                    Rect anchorRect = new Rect(location[0], location[1],
                            location[0] + mRoot.getWidth(), location[1]
                            + mRoot.getHeight());

                    mWindow.showAtLocation(mRoot, Gravity.BOTTOM,
                            anchorRect.left, 0);
                }
                mShowing = true;
                mHandler.sendEmptyMessage(SHOW_PROGRESS);
                if (timeout != 0) {
                    mHandler.removeMessages(FADE_OUT);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(FADE_OUT),
                            timeout);
                }
            }
        }
    }

    @Override
    public void hide() {
        if (mShowing) {
            mShowing = false;
            mHandler.removeMessages(SHOW_PROGRESS);
            mWindow.dismiss();
        }
    }

    @Override
    public boolean isShowing() {
        return mShowing;
    }

    @Override
    public void setAnchorView(View view) {
        mRoot = makeControllerView();
        mWindow.setContentView(mRoot);
        mWindow.setWidth(LayoutParams.MATCH_PARENT);
        mWindow.setHeight(LayoutParams.WRAP_CONTENT);
        initControllerView();
    }

    protected View makeControllerView() {
        return inflater.inflate(R.layout.media_controller, null);
    }


    private void initControllerView() {
        tv_start_time = (TextView) mRoot.findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) mRoot.findViewById(R.id.tv_end_time);
        seekBar = (SeekBar) mRoot.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBarChangedDispose());
    }


    private class SeekBarChangedDispose implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
            long duration = mPlayer.getDuration();
            long currentPosition = duration * progress / 1000L;
            String time = generateTime(currentPosition);
            tv_start_time.setText(time);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    @Override
    public void onFinishInflate() {
        if (mRoot != null)
            initControllerView();
        super.onFinishInflate();
    }


    private String generateTime(long position) {
        int totalSeconds = (int) (position / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    if(!isChangeState) {
                        setProgress();
                        if (mShowing) {
                            sendEmptyMessageDelayed(SHOW_PROGRESS,1000);
                        }
                    }
                    break;
            }
        }
    };

    private long setProgress() {
        if (mPlayer == null)
            return 0;

        long position = mPlayer.getCurrentPosition();
        long duration = mPlayer.getDuration();
        if (seekBar != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                seekBar.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            seekBar.setSecondaryProgress(percent * 10);
        }

        mDuration = duration;

        if (tv_end_time != null)
            tv_end_time.setText(generateTime(mDuration));
        if (tv_start_time != null)
            tv_start_time.setText(generateTime(position));

        return position;
    }

    private void initFloatingWindow() {
        mWindow = new PopupWindow(mContext);
        mWindow.setFocusable(false);
        mWindow.setBackgroundDrawable(null);
        mWindow.setOutsideTouchable(true);
    }

}
