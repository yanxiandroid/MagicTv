package com.yht.iptv.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.yht.iptv.R;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.PictureAdInfo;
import com.yht.iptv.presenter.AdRecordPresenter;
import com.yht.iptv.presenter.PictureAdPresenter;
import com.yht.iptv.socket.SocketPlayActivity;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.OkHttpUtils;
import com.yht.iptv.utils.SPUtils;
import com.yht.iptv.utils.ShowImageUtils;
import com.yht.iptv.view.movie.PlayVideoActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/1/5.
 */

public class AdvertDialogFragment extends DialogFragment implements DialogInterface.OnKeyListener, IPresenterBase<List<PictureAdInfo>> {


    private Context mContext;
    private List<String> picLists;
    private int advertPosition;
    private ImageView[] imageViews;
    private MyHandler handler;
    private final int ADVERT_DELAY = 1;
    private final int KEY_DISMISS = 2;
    private TextView tv_advert_msg;
    private boolean keyKeep = true;
    private String roomId;
    private List<Integer> advertIds;
    private int keyCode;
    private boolean isFirst = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        picLists = new ArrayList<>();
        advertIds = new ArrayList<>();
        advertPosition = 0;
        imageViews = new ImageView[2];
        handler = new MyHandler(this);
        roomId = (String) SPUtils.get(mContext, Constants.ROOM_ID, "");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_fragment_advert_video, null);

        imageViews[0] = (ImageView) view.findViewById(R.id.iv_advert1);
        imageViews[1] = (ImageView) view.findViewById(R.id.iv_advert2);
        tv_advert_msg = (TextView) view.findViewById(R.id.tv_advert_msg);

        PictureAdPresenter pictureAdPresenter = new PictureAdPresenter(mContext,this);
        pictureAdPresenter.request(this,1,roomId);


        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);

        dialog.setView(view);

        return dialog.create();
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
        try {
            if(Constants.mainPageInfo.getAdSetting().getMediaPlayImage() == 1) {
                PictureAdPresenter pictureAdPresenter = new PictureAdPresenter(mContext, this);
                pictureAdPresenter.request(this, 1,roomId);
            }else{
                ShowImageUtils.showImageView(this,Constants.mainPageInfo.getAdSetting().getMediaDetailImageFile().getPath(),imageViews[0]);
                getDialog().setOnKeyListener(this);
                handler.sendEmptyMessageDelayed(KEY_DISMISS,2000);
            }
        }catch (Exception e){
            ShowImageUtils.showImageView(this,R.drawable.photo_failed,imageViews[0]);
            getDialog().setOnKeyListener(this);
            handler.sendEmptyMessageDelayed(KEY_DISMISS,2000);
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN) {
            LogUtils.d("DOWNDOWN");
            handler.removeMessages(KEY_DISMISS);
            if(!keyKeep) {
                if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_BACK) {
                    this.keyCode = keyCode;
                    this.dismiss();
                }
            }
        }
        if(event.getAction() == KeyEvent.ACTION_UP) {
            keyKeep = false;
        }
        return true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mContext instanceof PlayVideoActivity){
            ((PlayVideoActivity)mContext).onDismissDialogFragment(keyCode);
        }
        if (mContext instanceof SocketPlayActivity){
            ((SocketPlayActivity)mContext).onDismissDialogFragment(keyCode);
        }
    }

    @Override
    public void onSuccess(BaseModel<List<PictureAdInfo>> dataList) {
        try {
            picLists.clear();
            advertIds.clear();
            List<PictureAdInfo> data = dataList.data;
            for (int i = 0 ; i < data.size();i++){
                picLists.add(data.get(i).getFileUpload().getPath());
                advertIds.add(data.get(i).getId());
            }
            handler.removeMessages(ADVERT_DELAY);
            handler.sendEmptyMessageDelayed(ADVERT_DELAY,50);

        }catch (Exception e){

        }finally {
            getDialog().setOnKeyListener(this);
            handler.sendEmptyMessageDelayed(KEY_DISMISS,2000);
        }

    }

    @Override
    public void onError() {
        getDialog().setOnKeyListener(this);
        handler.sendEmptyMessageDelayed(KEY_DISMISS,2000);
    }

    private static class MyHandler extends Handler{

        private WeakReference<AdvertDialogFragment> dialogFragmentWeakReference;

        public MyHandler(AdvertDialogFragment dialogFragment) {
            dialogFragmentWeakReference = new WeakReference<>(dialogFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AdvertDialogFragment fragment = dialogFragmentWeakReference.get();
            if(msg.what == fragment.ADVERT_DELAY){
                fragment.showAdv();
            }
            if(msg.what == fragment.KEY_DISMISS){
                fragment.keyKeep = false;
            }
        }
    }

    private void showAdv(){
        try {
            if(Constants.mainPageInfo.getAdSetting().getMediaPlayImage() == 1) {
                advertPosition++;
                imageViews[advertPosition % imageViews.length].bringToFront();
                if(isFirst){
                    isFirst = false;
                    ShowImageUtils.showImageViewNoMemory(this, picLists.get(advertPosition % picLists.size()), imageViews[advertPosition % imageViews.length], 10);
                }else {
                    ShowImageUtils.showImageViewNoMemory(this, picLists.get(advertPosition % picLists.size()), imageViews[advertPosition % imageViews.length], 2000);
                }

                AdRecordPresenter adRecordPresenter = new AdRecordPresenter();
                adRecordPresenter.request(this, advertIds.get(advertPosition % advertIds.size()), 0,roomId,1, 0);

                handler.removeMessages(ADVERT_DELAY);
                handler.sendEmptyMessageDelayed(ADVERT_DELAY, 10000);
                tv_advert_msg.bringToFront();
                tv_advert_msg.setText(R.string.advert);
                tv_advert_msg.setVisibility(View.VISIBLE);
            }else{
                ShowImageUtils.showImageView(this,Constants.mainPageInfo.getAdSetting().getMediaPlayImageFile().getPath(), imageViews[0]);
            }
        }catch (Exception e){
            ShowImageUtils.showImageView(this,R.drawable.photo_failed, imageViews[0]);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(ADVERT_DELAY);
        handler.removeMessages(KEY_DISMISS);
        OkHttpUtils.cancel(this);
    }
}
