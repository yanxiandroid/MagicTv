package com.yht.iptv.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.callback.IDialogListener;
import com.yht.iptv.utils.AppUtils;
import com.yht.iptv.utils.Constants;

/**
 * Created by Q on 2017/10/18.
 */

public class MediaPayDialog extends AppCompatDialog implements View.OnClickListener, View.OnFocusChangeListener {
    private IDialogListener onClickListener;
    private Bitmap alipay;
    private Bitmap weixin;
    private TextView[] textviews;
    private RelativeLayout rl_qrcode;
    private RelativeLayout rl_room_info;
    private DialogInterface.OnCancelListener onCancelListener;
    private Context context;
    private TextView tv_content;
    private String title_price;
    private int movieType;//影片类型  0:看本片  1:任看  2:点餐
    private String pay_choose;
    private TextView tv_room_info;

    public MediaPayDialog(Context context) {
        super(context);
    }

    public MediaPayDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public MediaPayDialog(Context context, IDialogListener onClickListener, Bitmap alipay, Bitmap weixin, DialogInterface.OnCancelListener onCancelListener, String title_price, int movieType) {
        super(context, R.style.MyDialogDefine);
        this.context = context;
        this.onClickListener = onClickListener;
        this.alipay = alipay;
        this.weixin = weixin;
        this.onCancelListener = onCancelListener;
        this.title_price = title_price;
        this.movieType = movieType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setContentView(R.layout.dialog_movie_pay_toast);
        showPosition(getWindow());
//        pay_choose = (String) SPUtils.get(context, Constants.PAY_CHOOSE, Constants.ALL_PAY);
        pay_choose = Constants.ONLINE_PAY;
        textviews = new TextView[2];
        rl_qrcode = (RelativeLayout) window.findViewById(R.id.rl_qrcode);
        tv_content = (TextView) window.findViewById(R.id.tv_content);
        TextView tv_price = (TextView) window.findViewById(R.id.tv_price);
        ImageView iv_qrcode1 = (ImageView) rl_qrcode.findViewById(R.id.scan_code_alipay_img);
        ImageView iv_qrcode2 = (ImageView) rl_qrcode.findViewById(R.id.scan_code_weixin_img);
        rl_room_info = (RelativeLayout) window.findViewById(R.id.rl_room_info);
        rl_qrcode.setVisibility(View.GONE);
        rl_room_info.setVisibility(View.GONE);
        textviews[0] = (TextView) window.findViewById(R.id.tv_pay_net);
        textviews[1] = (TextView) window.findViewById(R.id.tv_pay_loc);
        tv_room_info = (TextView) window.findViewById(R.id.tv_room_info);

        if (pay_choose.equals(Constants.ALL_PAY)) {
            textviews[0].setVisibility(View.VISIBLE);
            textviews[1].setVisibility(View.VISIBLE);
        } else if (pay_choose.equals(Constants.ONLINE_PAY)) {
            textviews[0].setVisibility(View.VISIBLE);
            textviews[1].setVisibility(View.GONE);
        } else {
            textviews[0].setVisibility(View.GONE);
            textviews[1].setVisibility(View.VISIBLE);
        }

        this.setCancelable(true);
        iv_qrcode1.setImageBitmap(alipay);
        iv_qrcode2.setImageBitmap(weixin);
        textviews[0].setOnFocusChangeListener(this);
        textviews[1].setOnFocusChangeListener(this);
        textviews[0].setTag(Constants.CONFIRM);
        textviews[1].setTag(Constants.CANCEL);
        textviews[0].setOnClickListener(this);
        textviews[1].setOnClickListener(this);
        tv_price.setText(title_price);
        String title;
        if (movieType == 0) {
            title = context.getResources().getString(R.string.mall_goods_pay_part1) + title_price + context.getResources().getString(R.string.mall_goods_pay_one);
        } else if (movieType == 1) {
            title = context.getResources().getString(R.string.mall_goods_pay_part2) + title_price + context.getResources().getString(R.string.mall_goods_pay_all);
        } else {
            title = context.getResources().getString(R.string.mall_goods_pay_part2) + title_price + "点餐费用";
        }
        tv_content.setText(setTextChanged(title));
        tv_room_info.setText("支付的费用：" + title_price + "，将会计入房账清单中，您退房时将一并支付，按OK键确认支付");
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
        if (hasFocus) {
            if (v.getId() == R.id.tv_pay_net) {
                rl_qrcode.setVisibility(View.VISIBLE);
                rl_room_info.setVisibility(View.GONE);
                Drawable bottomDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.mall_goods_pay_shape, null);
                bottomDrawable.setBounds(0, 0, bottomDrawable.getMinimumWidth(), bottomDrawable.getMinimumHeight());//必须设置图片大小，否则不显示
                textviews[0].setCompoundDrawables(null, null, null, bottomDrawable);
                textviews[1].setCompoundDrawables(null, null, null, null);
            }
            if (v.getId() == R.id.tv_pay_loc) {
                rl_qrcode.setVisibility(View.GONE);
                rl_room_info.setVisibility(View.VISIBLE);
                Drawable bottomDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.mall_goods_pay_shape, null);
                bottomDrawable.setBounds(0, 0, bottomDrawable.getMinimumWidth(), bottomDrawable.getMinimumHeight());//必须设置图片大小，否则不显示
                textviews[1].setCompoundDrawables(null, null, null, bottomDrawable);
                textviews[0].setCompoundDrawables(null, null, null, null);
            }
        } else {
            rl_qrcode.setVisibility(View.GONE);
            rl_room_info.setVisibility(View.GONE);
        }
    }

    /**
     * 设置显示位置
     */
    private void showPosition(Window window) {
        WindowManager.LayoutParams wl = window.getAttributes();
        //设置X轴最右边Y轴最上面
        wl.x = AppUtils.getScreenWidth();
        wl.y = AppUtils.getScreenHeight();
        // 以下这两句是为了保证按钮可以垂直满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
        // 设置显示位置
        this.onWindowAttributesChanged(wl);
    }

    private SpannableStringBuilder setTextChanged(String font) {
        int start = font.indexOf(title_price);
        Spannable wordToSpan = new SpannableString(font);
        wordToSpan.setSpan(new AbsoluteSizeSpan(getDimension(R.dimen.w_60)), start, title_price.length() + start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder builder = new SpannableStringBuilder(wordToSpan);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#f69b4d"));
        builder.setSpan(span, start, title_price.length() + start, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        return builder;
    }

    public int getDimension(int id) {
        return (int) context.getResources().getDimension(id);
    }
}
