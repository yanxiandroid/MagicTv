package com.yht.iptv.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.yht.iptv.R;
import com.yht.iptv.utils.ShowImageUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Q on 2017/11/3.
 */

public class PayDialog extends AppCompatDialog {
    private Context context;
    private Bitmap bitmap;
    private DialogInterface.OnCancelListener onCancelListener;
    private String title_price;
    private int movieType;//影片类型  0:看本片  1:任看  2:点餐
    private int payment;//支付方式：0 ：支付宝 1：微信

    private FrameLayout pay_bg;
    private TextView tv_content;
    private ImageView pay_icon;
    private ImageView scan_code_img;
    private ImageView payment_icon;
    private TextView tv_time_out;
    private byte seconds = 60;

    public PayDialog(Context context) {
        super(context);
    }

    public PayDialog(Context context, int theme) {
        super(context, theme);
    }

    public PayDialog(Context context, int payment, Bitmap bitmap, DialogInterface.OnCancelListener onCancelListener, String title_price, int movieType) {
        super(context, R.style.MyDialogDefine);
        this.context = context;
        this.bitmap = bitmap;
        this.payment = payment;
        this.onCancelListener = onCancelListener;
        this.title_price = title_price;
        this.movieType = movieType;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setContentView(R.layout.dialog_pay_toast);
        pay_bg = (FrameLayout) window.findViewById(R.id.pay_bg);
        tv_content = (TextView) window.findViewById(R.id.tv_content);
        pay_icon = (ImageView) window.findViewById(R.id.pay_icon);
        payment_icon = (ImageView) window.findViewById(R.id.payment_icon);
        scan_code_img = (ImageView) window.findViewById(R.id.scan_code_img);
        tv_time_out = (TextView) window.findViewById(R.id.tv_time_out);

        String title;
        if (movieType == 0) {
            title = context.getResources().getString(R.string.mall_goods_pay_part1) + title_price + context.getResources().getString(R.string.mall_goods_pay_one);
        } else if (movieType == 1) {
            title = context.getResources().getString(R.string.mall_goods_pay_part1) + title_price + context.getResources().getString(R.string.mall_goods_pay_all);
        } else {
            title = context.getResources().getString(R.string.mall_goods_pay_part2) + title_price;
        }
        tv_content.setText(setTextChanged(title,title_price));


        if (payment == 0) {
            pay_bg.setBackgroundColor(Color.parseColor("#12b0ff"));
            ShowImageUtils.showImageView(context, R.drawable.pay_alipay_icon, pay_icon);
            ShowImageUtils.showImageView(context, R.drawable.alipay_icon, payment_icon);
        } else {
            pay_bg.setBackgroundColor(Color.parseColor("#69ec69"));
            ShowImageUtils.showImageView(context, R.drawable.pay_weixin_icon, pay_icon);
            ShowImageUtils.showImageView(context, R.drawable.weixin_icon, payment_icon);
        }

        this.setCancelable(true);
        scan_code_img.setImageBitmap(bitmap);
        setOnCancelListener(onCancelListener);
        handler.post(runnable);
    }


    private SpannableStringBuilder setTextChanged(String font,String textColor) {
        int start = font.indexOf(textColor);
        Spannable wordToSpan = new SpannableString(font);
        wordToSpan.setSpan(new AbsoluteSizeSpan(getDimension(R.dimen.w_60)), start, textColor.length() + start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder builder = new SpannableStringBuilder(wordToSpan);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#f69b4d"));
        builder.setSpan(span, start, textColor.length() + start, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        return builder;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        LogUtils.d("dismiss");
        handler.removeCallbacks(runnable);
    }

    public int getDimension(int id) {
        return (int) context.getResources().getDimension(id);
    }

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seconds--;
            String msg;
            if(movieType == 2) {
                msg = context.getResources().getString(R.string.surplus_time) +" "+ seconds + context.getResources().getString(R.string.surplus_food);
            }else{
                msg = context.getResources().getString(R.string.surplus_time)+" " + seconds + context.getResources().getString(R.string.surplus_movie);
            }
            tv_time_out.setText(setTextChanged(msg,seconds + "s"));
            if(seconds <= 0){
                EventBus.getDefault().post("timeOut");
                handler.removeCallbacks(runnable);
            }else {
                handler.postDelayed(runnable, 1000);
            }
        }
    };

}
