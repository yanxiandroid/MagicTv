package com.yht.iptv.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatDialog;

import com.apkfuns.logutils.LogUtils;
import com.yht.iptv.callback.IDialogListener;
import com.yht.iptv.tools.InstallDialog;
import com.yht.iptv.tools.UpdateDialog;
import com.yht.iptv.utils.AppUtils;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.DialogUtils;
import com.yht.iptv.utils.ToastUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by admin on 2017/2/17.
 * 升级广播接收器
 */
public class UpdateReceiver extends BroadcastReceiver {

    private InstallDialog Idialog;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        LogUtils.d("升级弹窗");

        //升级广播
        //安装应用程序
        if(!Constants.SYSTEM_ALERT) {
            Constants.SYSTEM_ALERT = true;
            UpdateDialog updateDialog = new UpdateDialog(context, new IDialogListener() {

                @Override
                public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {
                    DialogUtils.dismissDialog(dialog);
                    Idialog = new InstallDialog(context);
                    DialogUtils.showDialog(Idialog);
                    String apkPath = intent.getStringExtra("apkPath");
                    HandlerThread thread = new HandlerThread("install");
                    thread.start();
                    Handler handler = new Handler(thread.getLooper()){
                        @Override
                        public void handleMessage(Message msg) {
                            if(msg.what == 0) {
                                installSlient((String) msg.obj,context);
                            }
                        }
                    };
                    Message message = Message.obtain();
                    message.what = 0;
                    message.obj = apkPath;
                    handler.sendMessage(message);
                }
            });
            DialogUtils.showDialog(updateDialog);
        }
    }


    private void installSlient(String path,Context context) {
        if(AppUtils.isAppRoot()) {
            String cmd = "pm install -r " + path;
            Process process = null;
            DataOutputStream os = null;
            BufferedReader successResult = null;
            BufferedReader errorResult = null;
            StringBuilder successMsg = null;
            StringBuilder errorMsg = null;
            try {
                //静默安装需要root权限
                process = Runtime.getRuntime().exec("su");
                os = new DataOutputStream(process.getOutputStream());
                os.write(cmd.getBytes());
                os.writeBytes("\n");
                os.writeBytes("exit\n");
                os.flush();
                //执行命令
                process.waitFor();
                //获取返回结果
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                    if (process != null) {
                        process.destroy();
                    }
                    if (successResult != null) {
                        successResult.close();
                    }
                    if (errorResult != null) {
                        errorResult.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            DialogUtils.dismissDialog(Idialog);
            //安装应用程序
            Uri uri = Uri.fromFile(new File(path));
            Intent localIntent = new Intent(Intent.ACTION_VIEW);
            localIntent.setDataAndType(uri, "application/vnd.android.package-archive");
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(localIntent);
        }
    }


}