package com.yht.iptv.tools;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 *
 */

public class ScrollTextView extends AppCompatTextView {

    private Handler mHandler;
    private Runnable mRunnable;

    public ScrollTextView(Context context) {
        this(context, null);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollTextView(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        startScroll();
    }

    private void startScroll() {
        initSrcoll();
        scrollTo(0, 0);
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, 5000);    //首行滚动延时
    }

    private void initSrcoll() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        if (mRunnable == null) {
            mRunnable = new Runnable() {
                public void run() {
                    int textAreaheight = getHeight() - getPaddingBottom() - getPaddingTop();
                    int scrollY = getScrollY();
                    int lineHeight = getLineHeight();
                    int lineCount = getLineCount();
                    double viewCount = Math.floor(textAreaheight / lineHeight);    //可见区域最大显示的行数
                    //能显示得下就无需滚动
                    if (lineCount <= viewCount) {
                        return;
                    }
                    //最大需要滚动的长度
                    int maxScrollY = lineCount * lineHeight;    //等于文字总长
                    if (scrollY >= maxScrollY) {  //滚动头时则复位
                        scrollTo(0, 0);
                        mHandler.postDelayed(this, 1000);    //回滚后延时再滚动
                    } else {
                        scrollBy(0, 1);
                        mHandler.postDelayed(this, 300);
                    }
                }
            };
        }
    }
}
