package com.yht.iptv.tools;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Circle;
import com.yht.iptv.R;

/**
 * Created by admin on 2017/11/2.
 */

public class InstallDialog extends AppCompatDialog {

    private Context context;
    private String title;


    public InstallDialog(Context context) {
        super(context, R.style.MyDialogDefine);
        this.context = context;
    }

    public InstallDialog(Context context, int theme) {
        super(context, theme);
    }

    public InstallDialog(Context context, String title) {
        super(context, R.style.MyDialogDefine);
        this.context = context;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        setContentView(R.layout.dialog_install);
        TextView tv_show = (TextView) findViewById(R.id.tv_show);
        if(title != null && !title.equals("")) {
            tv_show.setText(title);
        }
        setCancelable(true);
    }
}
