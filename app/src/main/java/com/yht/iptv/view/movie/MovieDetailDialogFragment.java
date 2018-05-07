package com.yht.iptv.view.movie;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.model.MediaDetailBean;
import com.yht.iptv.tools.VerticalScrollTextView;
import com.yht.iptv.utils.ShowImageUtils;

import java.util.List;

/**
 * Created by Q on 2018/1/5.
 */

public class MovieDetailDialogFragment extends DialogFragment implements DialogInterface.OnKeyListener {
    private Context context;
    private ImageView media_bg;
    private TextView media_name, media_zone, media_type, media_director, media_actor;
    private VerticalScrollTextView media_introduction;
    private MediaDetailBean mediaDetailBean;


    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mediaDetailBean = bundle.getParcelable("MediaDetail");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_media_detail, null);
        media_bg = (ImageView) view.findViewById(R.id.media_bg);
        media_name = (TextView) view.findViewById(R.id.media_name);
        media_zone = (TextView) view.findViewById(R.id.media_zone);
        media_type = (TextView) view.findViewById(R.id.media_type);
        media_director = (TextView) view.findViewById(R.id.media_director);
        media_actor = (TextView) view.findViewById(R.id.media_actor);
        media_introduction = (VerticalScrollTextView) view.findViewById(R.id.media_introduction);
        initData();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(view);


        return builder.create();
    }


    private void initData() {
        if (mediaDetailBean != null) {
            if (mediaDetailBean != null) {
                media_name.setText(mediaDetailBean.getName());//影名
                List<MediaDetailBean.District> district = mediaDetailBean.getDistrict();//地区
                media_zone.setText(getString(R.string.region) + ":" + getMovieZone(district));//地区
                media_director.setText(getString(R.string.director) + ":" + mediaDetailBean.getDirector());//导演
                List<MediaDetailBean.Category> type = mediaDetailBean.getCategory();//类型
                media_type.setText(getString(R.string.type) + ":" + getMovieType(type));//类型
                media_actor.setText(getString(R.string.stars) + ":" + mediaDetailBean.getStarred());//演员
                media_actor.setSelected(true);
                media_introduction.setText(getString(R.string.describe) + ":" + mediaDetailBean.getDescription());//简介
                media_introduction.setStart();
                if (mediaDetailBean.getBackgroundImage() != null && mediaDetailBean.getBackgroundImage().getPath() != null) {
                    ShowImageUtils.showImageViewNoMemory(this, mediaDetailBean.getBackgroundImage().getPath(), media_bg, 200);
                } else {
                    ShowImageUtils.showImageView(this, mediaDetailBean.getFileUpload().getPath(), media_bg);
                }
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        getDialog().setOnKeyListener(this);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.movieDetailDialog);
    }


    /**
     * 获取影片地区
     *
     * @return
     */
    private String getMovieZone(List<MediaDetailBean.District> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                sb.append(list.get(i).getName());
                if (i < list.size() - 1) {
                    sb.append("/");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取影片类型
     *
     * @return
     */
    private String getMovieType(List<MediaDetailBean.Category> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                sb.append(list.get(i).getName());
                if (i < list.size() - 1) {
                    sb.append("/");
                }
            }
        }
        return sb.toString();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (context instanceof MovieDetailActivity) {
                ((MovieDetailActivity) context).onDissmissMovieDetailDialogFragment();
            }
            dismiss();
            return true;
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE) {
            if (context instanceof MovieDetailActivity) {
                ((MovieDetailActivity) context).onDissmissMovieDetailDialogFragment();
            }
            dismiss();
        }
        return false;
    }
}
