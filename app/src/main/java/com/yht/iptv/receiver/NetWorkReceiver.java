package com.yht.iptv.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.yht.iptv.model.EventNetwork;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by admin on 2017/10/17.
 */

public class NetWorkReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        //如果无网络连接activeInfo为null
        //也可获取网络的类型
        if (activeInfo != null) { //网络连接
            EventBus.getDefault().post(new EventNetwork(true));
        } else { //网络断开
            EventBus.getDefault().post(new EventNetwork(false));
        }
    }

}
