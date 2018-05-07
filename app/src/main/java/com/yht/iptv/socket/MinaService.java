package com.yht.iptv.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.yht.iptv.push.MinaClientService;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.NetworkUtils;
import com.yht.iptv.utils.ServiceUtils;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;

/**
 * Created by admin on 2017/6/29.
 * test
 */

public class MinaService extends Service {

    private NioSocketAcceptor acceptor;

    @Override
    public void onCreate() {
        super.onCreate();

        initMina();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initMina() {
        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("myChin", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        acceptor.setHandler(new MinaServiceHandler(this));
        acceptor.getSessionConfig().setReceiveBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 5);
        try {
            acceptor.bind(new InetSocketAddress(8888));
            LogUtils.tag("MINA_SERVIER").e("------mina初始化完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        acceptor.dispose(true);
        acceptor = null;
    }



}
