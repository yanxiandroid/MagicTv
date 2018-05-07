package com.yht.iptv.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;

import com.yht.iptv.R;
import com.yht.iptv.callback.IDialogListener;
import com.yht.iptv.utils.AnimationUtils;
import com.yht.iptv.utils.Constants;

/**
 * Created by Q on 2017/10/18.
 */

public class MediaChargeDialog extends AppCompatDialog implements View.OnClickListener, View.OnFocusChangeListener {
    private IDialogListener onClickListener;
    private String one = null;
    private String all = null;
    private Context context;
    private Button[] buttons;
    private DialogInterface.OnCancelListener onCancelListener;

    public MediaChargeDialog(Context context) {
        super(context);
    }

    public MediaChargeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    public MediaChargeDialog(Context context, IDialogListener onClickListener, String one, String all,DialogInterface.OnCancelListener onCancelListener) {
        super(context, R.style.MyDialogDefine);
        this.context = context;
        this.onClickListener = onClickListener;
        this.onCancelListener = onCancelListener;
        this.one = one;
        this.all = all;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_movie_charge_toast);
        buttons = new Button[2];
        buttons[0] = (Button) findViewById(R.id.charge_one_tn);
        buttons[1] = (Button) findViewById(R.id.charge_all_tn);
        buttons[0].setTag(Constants.CONFIRM);
        buttons[1].setTag(Constants.CANCEL);
        buttons[0].setText(one);
        buttons[1].setText(all);

        for (int i = 0; i < 2; i++) {
            buttons[i].setOnClickListener(this);
            buttons[i].setOnFocusChangeListener(this);
        }
        buttons[0].requestFocus();
        setOnCancelListener(onCancelListener);

    }

    @Override
    public void onClick(View view) {
        if (onClickListener != null) {
            String tag = (String) view.getTag();
            onClickListener.onClick(this, tag, null, null);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            AnimationUtils.scaleAnimation(v,300,1.0f,1.2f);
        }else {
            AnimationUtils.scaleAnimation(v,300,1.2f,1.0f);
        }
    }
}
