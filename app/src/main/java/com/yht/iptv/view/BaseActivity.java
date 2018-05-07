package com.yht.iptv.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.yht.iptv.push.MinaClientService;
import com.yht.iptv.utils.AppManagerUtils;
import com.yht.iptv.utils.ServiceUtils;

/**
 * Created by admin on 2017/10/11.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置不灭屛
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        //添加activity
        AppManagerUtils.getAppManager().addActivity(this);

        //判断client服务是否启动
        boolean serviceRunning = ServiceUtils.isServiceRunning("com.yht.iptv.minaClientService");
        if(!serviceRunning){
            ServiceUtils.startService(MinaClientService.class);
        }

    }

    public int getDimension(int id) {
        return (int) getResources().getDimension(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除activity
        AppManagerUtils.getAppManager().finishActivity(this);
    }


    public String getStrings(int id) {
        return  getResources().getString(id);
    }
}
