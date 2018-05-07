package com.yht.iptv.view.music;

/**
 * Created by admin on 2017/2/27.
 */
public class MusicPauseResumeManger {

    private MusicPauseResumeListener listener;
    private static MusicPauseResumeManger instance;

    private MusicPauseResumeManger(){

    }

    public static MusicPauseResumeManger getInstance(){
        if(instance == null){
            instance = new MusicPauseResumeManger();
        }

        return instance;
    }

    public void setMusicStatusListener(MusicPauseResumeListener listener){
        this.listener = listener;
    }

    public void executeListener(boolean isPause){
        if(listener != null) {
            listener.onPlayStatus(isPause);
        }
    }

    public void releaseRes(){
        listener = null;
        instance = null;
    }

}
