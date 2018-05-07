package com.yht.iptv.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by admin on 2018/1/16.
 */
@Table(name = "AdvertInfo")
public class AdvertInfo {

    @Column(name = "id", isId = true)
    private String id;

    @Column(name = "currentAdvId")
    private int currentAdvId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCurrentAdvId() {
        return currentAdvId;
    }

    public void setCurrentAdvId(int currentAdvId) {
        this.currentAdvId = currentAdvId;
    }
}
