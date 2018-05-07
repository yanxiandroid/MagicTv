package com.yht.iptv.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

/**
 * Created by admin on 2017/1/9.
 */
public class TextUtils {

    public static CharSequence getChangeTextColor(CharSequence s,int start,int end,String color){
        SpannableStringBuilder builder = new SpannableStringBuilder(s);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color));
        builder.setSpan(span,start,end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return  builder;
    }
}
