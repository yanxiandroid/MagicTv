package com.yht.iptv.socket;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.yht.iptv.model.SocketInfo;
import com.yht.iptv.utils.GsonUtils;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;

/**
 * Created by admin on 2017/6/29.
 */

public class MinaServiceHandler extends IoHandlerAdapter {

    private Context mContext;

    public MinaServiceHandler(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();
        cfg.setUseReadOperation(true);
        cfg.setWriteTimeout(30);
        cfg.setKeepAlive(true);
        cfg.setSoLinger(0);
        cfg.setTcpNoDelay(true);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
//        String received = message.toString();
//        session.write(received);
        LogUtils.tag("MINA_SERVICE").e(message.toString());
        //收到消息处理
        if (message instanceof String) {
            String received = (String) message;
            SocketInfo socketInfo;
            try {
                socketInfo = GsonUtils.fromJson(received, SocketInfo.class);
            }catch (Exception e){
                session.write("json parse error!");
                return;
            }

            if (socketInfo.getCheckCode().equals("HYZNIPTV")) {
                session.write("success");
                //发送广播
                Intent intent = new Intent();
                intent.setAction("com.yht.iptv.socketReceiver");
                intent.putExtra("socketMsg", socketInfo);
                mContext.sendBroadcast(intent);
            } else {
                session.write("checked failed");
            }
        } else {
            session.write("send failed");
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }
}
