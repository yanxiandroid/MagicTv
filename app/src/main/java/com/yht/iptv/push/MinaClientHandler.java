package com.yht.iptv.push;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.yht.iptv.model.FoodPayStatus;
import com.yht.iptv.model.MainPageInfo;
import com.yht.iptv.model.MoviePayStatus;
import com.yht.iptv.model.RoomMsgPushBean;
import com.yht.iptv.service.FloatingService;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.GsonUtils;
import com.yht.iptv.utils.SPUtils;
import com.yht.iptv.utils.ServiceUtils;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by admin on 2017/6/29.
 */

public class MinaClientHandler extends IoHandlerAdapter {

    private Context mContext;

    public MinaClientHandler(Context mContext) {
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
        Intent intent = new Intent(mContext,MinaClientService.class);
        mContext.stopService(intent);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        try {
            Log.e("minaHandler", "收到消息" + message.toString());
            if (message instanceof String) {
                String receive = (String) message;
                String substring = receive.substring(0, receive.indexOf(":"));
                if (substring.equals("%server_return%")) {
                    //是接收发送房间号的信息.
                    LogUtils.tag("minaRoom").d(message);
                } else if (substring.equals("%message%")) {
                    String json = receive.substring(receive.indexOf(":") + 1);
                    RoomMsgPushBean roomMsgPushBean = GsonUtils.fromJson(json, RoomMsgPushBean.class);
                    String content = roomMsgPushBean.getContent();
                    Bundle bundle = new Bundle();
                    bundle.putString("floating_message", content);
                    ServiceUtils.startService(FloatingService.class, bundle);
                    session.write(sendJson(roomMsgPushBean));
                } else if (substring.equals("%dinesOrder_status%")) {
                    //餐饮通知
                    String status = receive.substring(receive.indexOf(":") + 1);
                    if (status.equals("1")) {
                        FoodPayStatus foodPayStatus = new FoodPayStatus();
                        foodPayStatus.setStatus(true);
                        //发送到购物车页面
                        EventBus.getDefault().post(foodPayStatus);
                    }
                } else if (substring.equals("%videoOrder_status%")) {
                    String status = receive.substring(receive.indexOf(":") + 1);
                    if (status.equals("1")) {
                        MoviePayStatus moviePayStatus = new MoviePayStatus();
                        moviePayStatus.setStatus(true);
                        //发送到电影页面
                        EventBus.getDefault().post(moviePayStatus);
                    }
                } else if (substring.equals("%paymentSetting%")) {
                    String json = receive.substring(receive.indexOf(":") + 1);
                    MainPageInfo.PaymentSetting paymentSetting = GsonUtils.fromJson(json, MainPageInfo.PaymentSetting.class);
                    //设置
                    Constants.mainPageInfo.setPaymentSetting(paymentSetting);
                }else if (substring.equals("%adSetting%")) {
                    String json = receive.substring(receive.indexOf(":") + 1);
                    MainPageInfo.AdSetting adSetting = GsonUtils.fromJson(json, MainPageInfo.AdSetting.class);
                    //广告设置
                    Constants.mainPageInfo.setAdSetting(adSetting);
                }
            }
        }catch (Exception e){

        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }

    private String sendJson(RoomMsgPushBean roomMsgPushBean){
        //生成新的message发送
        RoomMsgPushBean bean = new RoomMsgPushBean();
        bean.setId(roomMsgPushBean.getId());
        String roomId = (String) SPUtils.get(mContext, Constants.ROOM_ID, "");
        bean.setRoomNum(roomId);
        return "%message%:" + GsonUtils.toJson(bean);
    }
}
