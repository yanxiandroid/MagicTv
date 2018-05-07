package com.yht.iptv.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.yht.iptv.R;
import com.yht.iptv.model.AppMangerInfo;
import com.yht.iptv.model.CheckQrCodeInfo;
import com.yht.iptv.utils.AnimationUtils;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.NetworkUtils;
import com.yht.iptv.utils.OkHttpUtils;
import com.yht.iptv.utils.QrCodeCreate;
import com.yht.iptv.utils.SPUtils;
import com.yht.iptv.utils.ShowImageUtils;
import com.yht.iptv.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by admin on 2017/11/7.
 */

public class MutiScreenDialogFragment extends DialogFragment {

    private  Context context;
    private ImageView iv_app_address;
    private ImageView iv_ip_address;
    private LinearLayout ll_qrcode;
    private RelativeLayout general_title;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_muti_screen, null);

        ImageView iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
        iv_app_address = (ImageView) view.findViewById(R.id.iv_app_address);
        iv_ip_address = (ImageView) view.findViewById(R.id.iv_ip_address);
        ll_qrcode = (LinearLayout) view.findViewById(R.id.ll_qrcode);

        ll_qrcode.setAlpha(0f);

        general_title = (RelativeLayout) view.findViewById(R.id.general_title);
        View view1 = general_title.findViewById(R.id.view);
        view1.setVisibility(View.GONE);
        general_title.setAlpha(0f);

        ShowImageUtils.showImageView(this,R.drawable.muti_screen_bg,iv_bg);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(view);

        EventBus.getDefault().register(this);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawableResource(R.color.transparent);
    }

    /**
     *  生成二维码信息
     * @param path 下载地址
     * @return 二维码信息
     */
    private Bitmap[] getMessageId(String path) {
        Bitmap[] qrCode = new Bitmap[2];
        //获取下载二维码地址和ip地址
        String ipAddress = NetworkUtils.getIPAddress(true);
        if(ipAddress.equals("0")){
            ToastUtils.showShort("请检查网络连接!");
            dismiss();
            return null;
        }
        String roomId = (String) SPUtils.get(context, Constants.ROOM_ID,"");
        //生成json格式
        Gson gson = new Gson();
        CheckQrCodeInfo checkInfo = new CheckQrCodeInfo();
        checkInfo.setAddressCode(ipAddress);
        checkInfo.setCheckCode(Constants.CHECKIP);
        checkInfo.setHostAddressCode(HttpConstants.HTTP_HOST);
        checkInfo.setRoomId(roomId);
//        checkInfo.setMovieCode(Constants.USER_ID);
        String json = gson.toJson(checkInfo);
        //获取下载地址
        String appUrl;
        if(path == null || path.equals("")) {
            ToastUtils.showShort("app下载地址为空!");
            appUrl = "下载地址为空";
        }else {
            appUrl = path;
        }

        try {
            qrCode[0] = QrCodeCreate.createQRCode(json, (int)context.getResources().getDimension(R.dimen.w_422), (int)context.getResources().getDimension(R.dimen.h_10));
            qrCode[1] = QrCodeCreate.createQRCode(appUrl, (int)context.getResources().getDimension(R.dimen.w_422), (int)context.getResources().getDimension(R.dimen.h_10));
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return qrCode;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQrCodeCreate(AppMangerInfo info){
        String path = null;
        if(info.getName() != null){
            path = info.getFileUpload().getPath();
        }
        Bitmap[] qrCode = getMessageId(path);
        if(qrCode != null) {
            iv_app_address.setImageBitmap(qrCode[1]);
            iv_ip_address.setImageBitmap(qrCode[0]);
            AnimationUtils.alphaAnimation(ll_qrcode,1000,0f,1f);
            AnimationUtils.alphaAnimation(general_title,1000,0f,1f);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        OkHttpUtils.cancel();
    }
}
