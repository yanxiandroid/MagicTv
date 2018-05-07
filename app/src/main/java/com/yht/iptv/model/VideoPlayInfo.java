package com.yht.iptv.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by admin on 2016/6/20.
 */
@Table(name = "VideoPlayInfo")
public class VideoPlayInfo {
    @Column(name = "videoId",isId = true)
    private String videoId;
    @Column(name = "video_progress")
    private long vedio_progress;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public long getVedio_progress() {
        return vedio_progress;
    }

    public void setVedio_progress(long vedio_progress) {
        this.vedio_progress = vedio_progress;
    }
}
