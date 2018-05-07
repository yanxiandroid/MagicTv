package com.yht.iptv.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by admin on 2016/6/20.
 */
@Table(name = "VideoPlayParts")
public class VideoPlayParts {
    @Column(name = "videoId",isId = true)
    private String videoId;

    @Column(name = "currentParts")
    private int currentParts;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getCurrentParts() {
        return currentParts;
    }

    public void setCurrentParts(int currentParts) {
        this.currentParts = currentParts;
    }
}
