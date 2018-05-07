package com.yht.iptv.push;

import android.content.Context;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;

/**
 * Created by admin on 2017/6/29.
 */

public class ConnectManager {
    private ConnectBean config;
    private WeakReference<Context> mContext;
    private NioSocketConnector connector;
    private IoSession session;
    private InetSocketAddress mAddress;

    public static ConnectManager connectManager;

    public static ConnectManager getInstance(ConnectBean config){
        if(connectManager == null){
            synchronized (ConnectManager.class){
                if(connectManager == null){
                    connectManager = new ConnectManager(config);
                }
            }
        }
        return connectManager;
    }


    private ConnectManager(ConnectBean config) {
        this.config = config;
        this.mContext = new WeakReference<>(config.getContext());
        init();
    }

    private void init(){
        mAddress = new InetSocketAddress(config.getIp(),config.getPort());
        //初始化Mina
        connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("myChin",new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        connector.setHandler(new MinaClientHandler(mContext.get()));
        connector.setConnectTimeoutMillis(2000);
    }

    public boolean connect(){
        if(session != null && session.isConnected()){
            return true;
        }
        try {
            ConnectFuture future = connector.connect(mAddress);
            future.awaitUninterruptibly();
            session = future.getSession();
        }catch (Exception e){
            return false;
        }
        if(session == null || !session.isConnected()){
            return false;
        }else {
            return true;
        }
    }

    public IoSession getSession(){
        return session;
    }

    public void desConnection(){
        try {
            if(connector != null) {
                connector.dispose();
            }
            connector = null;
            mAddress = null;
            session = null;
            connectManager = null;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
