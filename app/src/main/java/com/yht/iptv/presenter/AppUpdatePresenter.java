package com.yht.iptv.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.yht.iptv.utils.AppUtils;
import com.yht.iptv.utils.FileUtils;
import com.yht.iptv.utils.OkHttpUtils;


import java.io.File;

import okhttp3.Call;

/**
 * Created by admin on 2017/2/15.
 * 升级处理类
 */
public class AppUpdatePresenter {

    private Context context;

    public AppUpdatePresenter(Context context) {
        this.context = context;
    }

    public void request(String downloadPath, String filename, String filePath){

        //首先判断本地是否有文件存在
        File file = new File(FileUtils.getDownLoadPath());
        if(file.isDirectory()){
            if(file.listFiles().length > 0) {
                File files = file.listFiles()[0];
                //判断版本号
                //判断版本号
                if(files != null) {
                    String newVersionName = AppUtils.getVersionNameFromApk(files.getAbsolutePath());
                    int newVersionCode = AppUtils.getVersionCodeFromApk(files.getAbsolutePath());
                    int versionCode = AppUtils.getAppVersionCode();
                    String versionName = AppUtils.getAppVersionName();
                    if(newVersionName != null && newVersionCode != -1) {
                        if (newVersionCode != versionCode || !newVersionName.equals(versionName)) {
                            //需要执行更新操作
                            //发送广播
                            Intent intent = new Intent();
                            intent.setAction("com.update.receiver_action_install");
                            intent.putExtra("apkPath", files.getAbsolutePath());
                            context.sendBroadcast(intent);
                            return;
                        }
                    }
                }
            }
        }

        for (Call call : OkGo.getInstance().getOkHttpClient().dispatcher().runningCalls()) {
            Log.e("TAG","已经有任务在执行了runningCalls------" + call.request().tag());
            if(call.request().tag().equals("DOWNLOAD_APP")){
                return;
            }
        }

        OkHttpUtils.getJson(downloadPath,"DOWNLOAD_APP",new FileCallback(filePath,filename) {

            @Override
            public void onSuccess(Response<File> response) {
                File file = response.body();
                if(file != null) {
                    //发送广播
                    Intent intent = new Intent();
                    intent.setAction("com.update.receiver_action_install");
                    intent.putExtra("apkPath", file.getAbsolutePath());
                    context.sendBroadcast(intent);
                }
            }

            @Override
            public void downloadProgress(Progress progress) {
                Log.e("TAG","totalSize" + progress.totalSize + "--currentSize" + progress.currentSize);
                super.downloadProgress(progress);
            }

        });

    }

}
