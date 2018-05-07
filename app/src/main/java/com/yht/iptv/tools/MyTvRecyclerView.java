package com.yht.iptv.tools;

import android.content.Context;
import android.util.AttributeSet;

import com.owen.tvrecyclerview.widget.TvRecyclerView;


/**
 * Created by admin on 2017/11/10.
 */

public class MyTvRecyclerView extends TvRecyclerView {
    public MyTvRecyclerView(Context context) {
        super(context);
    }

    public MyTvRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTvRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        new PagerSnapHelper().attachToRecyclerView(this);
    }


    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        scrollToPosition(getAdapter().getItemCount() * 10000);//开始时的偏移量
    }
}
