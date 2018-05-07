package com.yht.iptv.tools;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.callback.IDialogListener;

/**
 * Created by Q on 2017/7/24.
 */

public class PaySuccessDialog extends AppCompatDialog implements View.OnClickListener {
    private String money;
    private IDialogListener onClickListener;

    public PaySuccessDialog(Context context) {
        super(context);
    }

    public PaySuccessDialog(Context context, IDialogListener onClickListener, String money) {
        super(context, R.style.MyDialogDefine);
        this.onClickListener = onClickListener;
        this.money = money;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay_success_view);
        TextView pay_money_text = (TextView) findViewById(R.id.pay_money);
        TextView pay_sure_text = (TextView) findViewById(R.id.pay_sure);
        this.setCancelable(false);
        pay_money_text.setText(money + "元");
        pay_sure_text.setOnClickListener(this);
        pay_sure_text.requestFocus();


    }

    @Override
    public void onClick(View v) {
        onClickListener.onClick(this, null, null, null);
    }
}
