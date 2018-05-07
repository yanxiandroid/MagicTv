package com.yht.iptv.socket;

import com.yht.iptv.model.SocketInfo;

/**
 * Created by admin on 2016/8/5.
 */
public class SocketMsgManger {

    private static SocketMsgManger msgManger;
    private OnSocketListener listener;

    public static SocketMsgManger getInstance() {

        if(msgManger == null){
            msgManger = new SocketMsgManger();
        }

        return msgManger;
    }

    private SocketMsgManger() {
    }

    public void setOnSocketListener(OnSocketListener listener){
        this.listener = listener;
    }

    public void setListener(SocketInfo msg){
        if(listener!= null){
            listener.getMsg(msg);
        }
    }

}
