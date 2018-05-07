package com.yht.iptv.tools;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by admin on 2017/10/20.
 */

public class MyView extends RelativeLayout {

    private String color;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.setBackgroundColor(Color.parseColor(color));
    }
}
