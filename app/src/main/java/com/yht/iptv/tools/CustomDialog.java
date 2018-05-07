package com.yht.iptv.tools;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.callback.IDialogClick;
import com.yht.iptv.utils.Constants;

/**
 */
public class CustomDialog extends AppCompatDialog implements View.OnClickListener {

    private IDialogClick onClickListener;
    private int content;
    private String confirm = null;
    private String cancel = null;

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomDialog(Context context, IDialogClick onClickListener, int content){
        super(context, R.style.MyDialogDefine);
        this.onClickListener = onClickListener;
        this.content = content;
    }
    public CustomDialog(Context context, IDialogClick onClickListener, int content, String confirm, String cancel){
        super(context, R.style.MyDialogDefine);
        this.onClickListener = onClickListener;
        this.content = content;
        this.confirm = confirm;
        this.cancel = cancel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_play_video_toast);
        TextView tv_content = (TextView) findViewById(R.id.tv_content);
        Button bt_confirm = (Button) findViewById(R.id.bt_confirm);
        Button bt_cancel = (Button) findViewById(R.id.bt_cancel);
        setCancelable(false);
        tv_content.setText(content);
        bt_confirm.setOnClickListener(this);
        bt_confirm.setTag(Constants.CONFIRM);
        if(confirm != null) {
            bt_confirm.setText(confirm);
        }
        bt_cancel.setOnClickListener(this);
        bt_cancel.setTag(Constants.CANCEL);
        if(cancel != null){
            bt_cancel.setText(cancel);
        }
        bt_confirm.requestFocus();
    }

    @Override
    public void onClick(View view) {
        String tag = (String) view.getTag();
        onClickListener.onClick(this,tag);
    }
}
