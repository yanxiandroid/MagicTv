package com.yht.iptv.view.music;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pili.pldroid.player.PLMediaPlayer;
import com.yht.iptv.model.MusicDetailBean;
import com.yht.iptv.utils.Constants;

import java.util.List;


/**
 * Created by admin on 2017/2/18.
 * 背景音乐播放
 */
public class MusicService extends Service implements PLMediaPlayer.OnCompletionListener, PLMediaPlayer.OnPreparedListener, PLMediaPlayer.OnErrorListener, PLMediaPlayer.OnInfoListener {


    public static PLMediaPlayer mediaPlayer = null;
    private List<MusicDetailBean> musicDetailBeanList;
    private String lastPath;//上一个播放地址
    private int lastPage;//上一个position的fragment
    private int type;//背景音乐和音乐鉴赏
    private int currentPage;//当前的页面
    private String currentPath;//当前的播放地址
    private int position;//列表的集合
    private boolean isSuccessPlay;//是否成功播放

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e("TAG", "onCreate");
        super.onCreate();

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new PLMediaPlayer(getApplicationContext());
        /* 监听播放是否完成 */
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnInfoListener(this);
        isSuccessPlay = false;
        lastPage = -1;
        lastPath = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null){
            return START_STICKY;
        }
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = bundle.getInt("type");
            if (type == Constants.AUDIO_BACKGROUND) {
                //背景音乐
                playMusic();
            } else if (type == Constants.AUDIO_LIST) {
                //音乐列表
                musicDetailBeanList = bundle.getParcelableArrayList("musicList");
                position = bundle.getInt("position");
                currentPage = bundle.getInt("mPage");
                currentPath = musicDetailBeanList.get(position).getFileUpload().getPath();
                //不是同一个页面播放音乐
                if (this.lastPath != null) {
                    if (lastPage != currentPage) {
                        playNetWorkMusic();
                        MusicStatusManger.getInstance().executeListener(currentPath, position, 1);
                    } else {
                        //同一个页面
                        //需要判断是否同一首歌
                        if (lastPath.equals(currentPath)) {
                            if(isSuccessPlay) {
                                //同一首歌
                                //暂停/继续播放
                                if (mediaPlayer.isPlaying()) {
                                    mediaPlayer.pause();
                                    MusicStatusManger.getInstance().executeListener(currentPath, position, 0);
                                } else {
                                    mediaPlayer.start();
                                    MusicStatusManger.getInstance().executeListener(currentPath, position, 1);
                                }
                            }else{
                                //播放失败
                                MusicStatusManger.getInstance().executeListener(currentPath, position, 2);
                            }
                        } else {
                            playNetWorkMusic();
                            MusicStatusManger.getInstance().executeListener(currentPath, position, 1);
                        }
                    }
                } else {
                    playNetWorkMusic();
                    MusicStatusManger.getInstance().executeListener(currentPath, position, 1);
                }

            } else if(type == Constants.AUDIO_STATUS){
                boolean status = bundle.getBoolean("status");
                if(status){
                    //暂停
                    if (this.lastPath != null) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            MusicStatusManger.getInstance().executeListener(currentPath, position, 0);
                        }
                    }
                }else {
                    //播放
                    if (this.lastPath != null) {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                            MusicStatusManger.getInstance().executeListener(currentPath, position, 1);
                        }
                    }
                }
            }

        }
//        Log.e("TAG", "onStartCommand--music");
//        int extras = intent.getIntExtra("extras", -1);
//        musicDetailBeanList = intent.getParcelableArrayListExtra("musicList");
//        if(extras == Constants.playAudio) {
//            playMusic();
//        }
//        if(extras == Constants.pauseAudio){
//            if(mediaPlayer.isPlaying()){
//                mediaPlayer.pause();
//            }else{
//                mediaPlayer.start();
//            }
//        }


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("TAG", "onDestroy");
        stopMusic();
        super.onDestroy();
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


//    @Override
//    public void onPrepared(MediaPlayer mp) {
//        Log.e("TAG", "onPrepared");
//        mp.start();
//    }
//
//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        Log.e("TAG", "onCompletion");
//        if (type == Constants.AUDIO_LIST) {
//            position = (position + 1) % musicDetailBeanList.size();
//            currentPath = musicDetailBeanList.get(position).getFileUpload().getPath();
//            MusicStatusManger.getInstance().executeListener(currentPath, position, false);
//            playNetWorkMusic();
//        } else if (type == Constants.AUDIO_BACKGROUND) {
//            playMusic();
//        }
//
//    }
//
//    @Override
//    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        Log.e("TAG", "onBufferingUpdate----" + percent);
//    }

    public void playMusic() {
        try {
            AssetFileDescriptor afd = getAssets().openFd("musics/background.mp3");
            /* 重置多媒体 */
            mediaPlayer.reset();
            /* 读取mp3文件 */
            mediaPlayer.setDataSource(afd.getFileDescriptor());
            /* 准备播放 */
            // mediaPlayer.prepare();
            /* 开始播放 */
            mediaPlayer.prepareAsync();
            /* 是否单曲循环 */
            mediaPlayer.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playNetWorkMusic() {
        try {
            lastPath = currentPath;
            lastPage = currentPage;
            /* 重置多媒体 */
            mediaPlayer.reset();

            /* 读取mp3文件 */
            mediaPlayer.setDataSource(currentPath);
            /* 准备播放 */
            // mediaPlayer.prepare();
            /* 开始播放 */
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public boolean onError(MediaPlayer mp, int what, int extra) {
//        switch (extra){
//            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
//                break;
//        }
//        return false;
//    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {
        if (type == Constants.AUDIO_LIST) {
            position = (position + 1) % musicDetailBeanList.size();
            currentPath = musicDetailBeanList.get(position).getFileUpload().getPath();
            MusicStatusManger.getInstance().executeListener(currentPath, position, 1);
            playNetWorkMusic();
        } else if (type == Constants.AUDIO_BACKGROUND) {
            playMusic();
        }
    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int extra) {
        isSuccessPlay = false;
        MusicStatusManger.getInstance().executeListener(currentPath, position, 2);
        return true;
    }

    @Override
    public boolean onInfo(PLMediaPlayer mp, int what, int extra) {

        Log.e("Music","what--" + what + "-------" + "extra" + extra);
        if(what == PLMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START){//第一帧音频已经获取到了
            isSuccessPlay = true;
        }

        return false;
    }

    @Override
    public void onPrepared(PLMediaPlayer plMediaPlayer, int i) {
        plMediaPlayer.start();
    }
}
