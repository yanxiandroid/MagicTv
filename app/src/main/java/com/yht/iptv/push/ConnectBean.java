package com.yht.iptv.push;

import android.content.Context;

/**
 * Created by admin on 2017/6/29.
 */

public class ConnectBean {


    private String ip;
    private int port;
    private Context context;

    public ConnectBean(String ip, int port,Context context) {
        this.ip = ip;
        this.port = port;
        this.context = context;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public Context getContext() {
        return context;
    }
}
