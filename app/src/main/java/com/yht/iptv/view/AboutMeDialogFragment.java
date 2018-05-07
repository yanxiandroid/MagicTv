package com.yht.iptv.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yht.iptv.R;
import com.yht.iptv.utils.LanguageUtils;
import com.yht.iptv.utils.ShowImageUtils;

/**
 * Created by admin on 2017/11/14.
 */

public class AboutMeDialogFragment extends DialogFragment {

    private Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_about, null);

        ImageView iv_about = (ImageView) view.findViewById(R.id.iv_about);

        if (LanguageUtils.getInstance().getLanguage(context).equals("en")) {
            ShowImageUtils.showImageViewNoMemory(this, R.drawable.about_me_en, iv_about, 300);
        } else {
            ShowImageUtils.showImageViewNoMemory(this, R.drawable.about_me, iv_about, 300);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(view);


        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        params.y = (int) getResources().getDimension(R.dimen.h_300);
        window.setAttributes(params);
        window.setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
