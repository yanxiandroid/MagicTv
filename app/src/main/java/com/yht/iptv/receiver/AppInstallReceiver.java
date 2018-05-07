package com.yht.iptv.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by admin on 2017/5/22.
 */

public class AppInstallReceiver extends BroadcastReceiver {

    private static final String ACTION_ADDED = "android.intent.action.PACKAGE_ADDED";
    private static final String ACTION_REMOVED = "android.intent.action.PACKAGE_REMOVED";
    private static final String ACTION_REPLACED = "android.intent.action.PACKAGE_REPLACED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getData().getSchemeSpecificPart();
        Intent myIntent;
        PackageManager pm;
        if(packageName.equals(context.getPackageName())) {
            switch (intent.getAction()) {
                case ACTION_REPLACED:
                    Log.e("install","REPLACED");
                    myIntent = new Intent();
                    pm = context.getPackageManager();
                    try {
                        myIntent = pm.getLaunchIntentForPackage(packageName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(myIntent);
                    break;
                case ACTION_ADDED:
                    Log.e("install","ADDED");
//                    myIntent = new Intent();
//                    pm = context.getPackageManager();
//                    try {
//                        myIntent = pm.getLaunchIntentForPackage(packageName);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(myIntent);
                    break;
            }
        }
    }

}
