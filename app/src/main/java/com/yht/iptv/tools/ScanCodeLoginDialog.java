package com.yht.iptv.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.widget.ImageView;

import com.yht.iptv.R;

/**
 * Created by Q on 2017/7/24.
 */

public class ScanCodeLoginDialog extends AppCompatDialog {
    private Bitmap bitmap;

    private OnCancelListener onCancelListener;

    public ScanCodeLoginDialog(Context context) {
        super(context);
    }

    public ScanCodeLoginDialog(Context context, Bitmap bitmap, OnCancelListener onCancelListener) {
        super(context, R.style.MyDialogDefine);
        this.bitmap = bitmap;
        this.onCancelListener = onCancelListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_scan_code_login_view);
        ImageView scan_code_login_img = (ImageView) findViewById(R.id.scan_code_login_img);
        scan_code_login_img.setImageBitmap(bitmap);
        setOnCancelListener(onCancelListener);
    }
}
