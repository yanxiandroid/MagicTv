package com.yht.iptv.tools;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.callback.IDialogListener;
import com.yht.iptv.utils.AnimationUtils;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.SPUtils;

/**
 * Created by Q on 2017/10/18.
 */

public class PayMentChargeDialog extends AppCompatDialog implements View.OnClickListener, View.OnFocusChangeListener {
    private IDialogListener onClickListener;
    private String money = null;
    private Context context;
    private Button[] buttons;
    private OnCancelListener onCancelListener;
    private int movieType;//影片类型  0:看本片  1:任看  2:点餐
    private TextView charge_buy_price,charge_buy_payment;

    public PayMentChargeDialog(Context context) {
        super(context);
    }

    public PayMentChargeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    public PayMentChargeDialog(Context context, IDialogListener onClickListener, String money, int movieType,OnCancelListener onCancelListener) {
        super(context, R.style.MyDialogDefine);
        this.context = context;
        this.onClickListener = onClickListener;
        this.onCancelListener = onCancelListener;
        this.money = money;
        this.movieType = movieType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setContentView(R.layout.dialog_pay_ment_toast);
        String pay_choose = (String) SPUtils.get(context, Constants.PAY_CHOOSE, "");
        buttons = new Button[3];
        buttons[0] = (Button) window.findViewById(R.id.charge_account_tn);
        buttons[1] = (Button) window.findViewById(R.id.charge_alipay_tn);
        buttons[2] = (Button) window.findViewById(R.id.charge_weixin_tn);
        buttons[0].setTag(Constants.THIRD);
        buttons[1].setTag(Constants.CONFIRM);
        buttons[2].setTag(Constants.CANCEL);

        for (int i = 0; i < 3; i++) {
            buttons[i].setOnClickListener(this);
            buttons[i].setOnFocusChangeListener(this);
        }
        buttons[0].requestFocus();
        setOnCancelListener(onCancelListener);
        if(Constants.mainPageInfo.getPaymentSetting().getPayWithRoomfee() == 1 && Constants.mainPageInfo.getPaymentSetting().getPayOnline() == 0) {
                buttons[1].setVisibility(View.GONE);
                buttons[2].setVisibility(View.GONE);
        }else if(Constants.mainPageInfo.getPaymentSetting().getPayWithRoomfee() == 0 && Constants.mainPageInfo.getPaymentSetting().getPayOnline() == 1){
            buttons[0].setVisibility(View.GONE);
        }
        charge_buy_price = (TextView) window.findViewById(R.id.charge_buy_price);
        charge_buy_payment = (TextView) window.findViewById(R.id.charge_buy_payment);
        if (movieType == 1){
            String title = context.getResources().getString(R.string.mall_goods_pay_part2) + money + context.getResources().getString(R.string.charge_buy_payment);
            charge_buy_price.setText(setTextChanged(title,money));
            charge_buy_payment.setText(context.getResources().getString(R.string.charge_buy_payment_food));
        }else {
            charge_buy_price.setText(context.getResources().getString(R.string.charge_buy_payment_food));
            charge_buy_payment.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        if (hasFocus) {
            AnimationUtils.scaleAnimation(v, 300, 1.0f, 1.2f);
        } else {
            AnimationUtils.scaleAnimation(v, 300, 1.2f, 1.0f);
        }
    }

    private SpannableStringBuilder setTextChanged(String font, String textColor) {
        int start = font.indexOf(textColor);
        Spannable wordToSpan = new SpannableString(font);
        wordToSpan.setSpan(new AbsoluteSizeSpan(getDimension(R.dimen.w_60)), start, textColor.length() + start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder builder = new SpannableStringBuilder(wordToSpan);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#f69b4d"));
        builder.setSpan(span, start, textColor.length() + start, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        return builder;
    }

    public int getDimension(int id) {
        return (int) context.getResources().getDimension(id);
    }
}
