package com.yht.iptv.socket;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yht.iptv.model.SocketInfo;
import com.yht.iptv.utils.AppManagerUtils;

/**
 * Created by admin on 2016/8/5.
 */
public class SocketReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        String socketMsg = intent.getStringExtra("socketMsg");
//        SocketInfo info = dispMovieMsg(context, socketMsg);
//
//        if(info != null) {
//            Activity activity = AppManagerUtils.getAppManager().currentActivity();
//            if(activity instanceof SocketPlayActivity){
//                //设置监听
//                SocketMsgManger.getInstance().setListener(socketMsg);
//            }else {
//                Intent intent1 = new Intent(context, SocketPlayActivity.class);
//                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                intent1.putExtra("socketMsg", socketMsg);
//                context.startActivity(intent1);
//            }
//        }

        SocketInfo socketInfo = (SocketInfo) intent.getSerializableExtra("socketMsg");
        if (socketInfo != null && socketInfo.getCheckCode().equals("HYZNIPTV")) {
            Activity activity = AppManagerUtils.getAppManager().currentActivity();
            if (activity instanceof SocketPlayActivity) {
                //设置监听
                SocketMsgManger.getInstance().setListener(socketInfo);
            } else {
                Intent intent1 = new Intent(context, SocketPlayActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent1.putExtra("socketMsg", socketInfo);
                context.startActivity(intent1);
            }
        }
    }

//    private SocketInfo dispMovieMsg(Context context, String msg) {
//        String[] split = msg.split("&&&&&&&&&");
//        if (split.length != 4) {
//            if (split.length == 1) {
//                final int keycode = Integer.parseInt(split[0]);
//                new Thread() {
//                    public void run() {
//                        try {
//                            Instrumentation inst = new Instrumentation();
//                            inst.sendKeyDownUpSync(keycode);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.start();
////                try {
////                     // 按键操作命令 11.24勘误，之前错误的写成了"input keycode"
////                     String keyCommand = "input keycode " + keycode;
////                     // 调用Runtime模拟按键操作
////                     Runtime.getRuntime().exec(keyCommand);
////                 } catch (Exception e) {
////                     e.printStackTrace();
////                 }
//            } else
//                ToastUtils.showToast(context, "格式不对....");
//            return null;
//        }
//        SocketInfo info = new SocketInfo();
//        info.setMovieName(split[0]);
//        info.setMovieUrl(split[1]);
//        info.setCurrentPosition(split[2]);
//        info.setVedioId(split[3]);
//        return info;
//    }


}
