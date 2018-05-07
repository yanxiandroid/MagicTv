package com.yht.iptv.socket;

import com.yht.iptv.model.SocketInfo;

/**
 * Created by admin on 2016/8/5.
 */
public interface OnSocketListener {
    void getMsg(SocketInfo msg);
}
