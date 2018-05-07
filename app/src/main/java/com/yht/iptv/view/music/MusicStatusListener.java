package com.yht.iptv.view.music;

/**
 * Created by admin on 2017/2/27.
 */
public interface MusicStatusListener {
    /**
     *
     * @param path
     * @param position
     * @param playStatus 0 pause 1 play 2 stop
     */
    void onStatus(String path, int position, int playStatus);
}
