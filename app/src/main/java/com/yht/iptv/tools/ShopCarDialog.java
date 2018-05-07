package com.yht.iptv.tools;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.callback.IDialogListener;
import com.yht.iptv.utils.Constants;

/**
 * Created by admin on 2016/6/24.
 */
public class ShopCarDialog extends AppCompatDialog implements View.OnClickListener {

    private IDialogListener onClickListener;
    private SpannableStringBuilder builder;

    public ShopCarDialog(Context context) {
        super(context);
    }

    public ShopCarDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public ShopCarDialog(Context context, IDialogListener onClickListener, SpannableStringBuilder builder){
        super(context, R.style.MyDialogDefine);
        this.onClickListener = onClickListener;
        this.builder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shopcar_toast);
        TextView tv_content = (TextView) findViewById(R.id.tv_content);
        Button bt_confirm = (Button) findViewById(R.id.bt_confirm);
        Button bt_cancel = (Button) findViewById(R.id.bt_cancel);
        tv_content.setText(builder);
        bt_confirm.setOnClickListener(this);
        bt_confirm.setTag(Constants.CONFIRM);
        bt_cancel.setOnClickListener(this);
        bt_cancel.setTag(Constants.CANCEL);
        bt_confirm.requestFocus();
    }

    @Override
    public void onClick(View view) {
        String tag = (String) view.getTag();
        onClickListener.onClick(this,tag,null,null);
    }
}
