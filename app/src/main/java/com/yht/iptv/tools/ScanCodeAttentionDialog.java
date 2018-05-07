package com.yht.iptv.tools;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;

import com.yht.iptv.R;

/**
 * Created by Q on 2017/7/24.
 */

public class ScanCodeAttentionDialog extends AppCompatDialog {


    public ScanCodeAttentionDialog(Context context) {
        super(context, R.style.MyDialogDefine);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_scan_code_attention_view);
        this.setCancelable(true);
    }
}
