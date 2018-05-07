package com.yht.iptv.view.music;

/**
 * Created by admin on 2017/2/27.
 */
public class MusicAnimManger {

    private MusicAnimListener listener;
    private static MusicAnimManger instance;

    private MusicAnimManger(){

    }

    public static MusicAnimManger getInstance(){
        if(instance == null){
            instance = new MusicAnimManger();
        }

        return instance;
    }

    public void setMusicStatusListener(MusicAnimListener listener){
        this.listener = listener;
    }

    public void executeListener(boolean isPause){
        if(listener != null) {
            listener.onStatus(isPause);
        }
    }

    public void releaseRes(){
        listener = null;
        instance = null;
    }

}
