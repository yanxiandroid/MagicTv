package com.yht.iptv.tools;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.callback.IDialogListener;

/**
 * Created by admin on 2016/6/24.
 * 自动升级对话框
 */
public class NoticeDialog extends AppCompatDialog implements View.OnClickListener {

    private IDialogListener onClickListener;
    private String content = null;
    private String confirm = null;

    public NoticeDialog(Context context) {
        super(context);
    }

    public NoticeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public NoticeDialog(Context context, IDialogListener onClickListener){
        super(context, R.style.MyDialogDefine);
        this.onClickListener = onClickListener;
    }
/*    public NoticeDialog(Context context, IDialogListener onClickListener, String content, String confirm){
        super(context, R.style.MyDialogDefine);
        this.onClickListener = onClickListener;
        this.content = content;
        this.confirm = confirm;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notice_toast);
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        Button bt_confirm = (Button) findViewById(R.id.bt_confirm);
        TextView tv_content = (TextView) findViewById(R.id.tv_content);
        setCancelable(false);
        bt_confirm.setOnClickListener(this);
        bt_confirm.requestFocus();
        if(content != null && confirm != null){
            bt_confirm.setText(confirm);
            tv_content.setText(content);
        }
    }

    @Override
    public void onClick(View view) {
        onClickListener.onClick(this,null,null,null);
    }
}
