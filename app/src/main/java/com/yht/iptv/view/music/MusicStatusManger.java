package com.yht.iptv.view.music;

/**
 * Created by admin on 2017/2/27.
 */
public class MusicStatusManger {

    private MusicStatusListener listener;
    private static MusicStatusManger instance;

    private MusicStatusManger(){

    }

    public static MusicStatusManger getInstance(){
        if(instance == null){
            instance = new MusicStatusManger();
        }

        return instance;
    }

    public void setMusicStatusListener(MusicStatusListener listener){
        this.listener = listener;
    }

    public void executeListener(String path,int position,int isPause){
        if(listener != null) {
            listener.onStatus(path, position, isPause);
        }
    }

    public void releaseRes(){
        listener = null;
        instance = null;
    }


}
