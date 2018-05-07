package com.yht.iptv.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewConfiguration;
import android.widget.ListView;

/**
 * Created by admin on 2017/10/20.
 */

public class FlingListView extends ListView {

    public FlingListView(Context context) {
        super(context);
    }

    public FlingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlingListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY /10);
    }

    @Override
    public void setFriction(float friction) {
        super.setFriction(ViewConfiguration.getScrollFriction() * 10);
    }
}
