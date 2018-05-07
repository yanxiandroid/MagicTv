package com.yht.iptv.tools;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.yht.iptv.utils.AppUtils;


/**
 * Created by admin on 2017/11/17.
 */

public class HScrollTextView extends AppCompatTextView {

    // scrolling feature
    private Scroller mSlr;

    // milliseconds for a round of scrolling
    private int mRndDuration = 10000;

    // the X offset when paused
    private int mXPaused = 0;

    // whether it's being paused
    private boolean mPaused = true;

    private int screenWidth = 0;

    private onFinishListener listener;

    public HScrollTextView(Context context) {
        super(context, null);
        setSingleLine();
        setEllipsize(null);
        setVisibility(INVISIBLE);
    }

    public HScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSingleLine();
        setEllipsize(null);
        setVisibility(INVISIBLE);
    }

    public HScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        setSingleLine();
        setEllipsize(null);
        setVisibility(INVISIBLE);
    }


    /**
     * begin to scroll the text from the original position
     */
    public void startScroll() {
        // begin from the very right side
        mXPaused = -1 * AppUtils.getScreenWidth();
        screenWidth = AppUtils.getScreenWidth();
        // assume it's paused
        mPaused = true;
        resumeScroll();
    }

    /**
     * resume the scroll from the pausing point
     */
    public void resumeScroll() {

        if (!mPaused)
            return;

        // Do not know why it would not scroll sometimes
        // if setHorizontallyScrolling is called in constructor.
        setHorizontallyScrolling(true);

        // use LinearInterpolator for steady scrolling
        mSlr = new Scroller(this.getContext(), new LinearInterpolator());
        setScroller(mSlr);

        int scrollingLen = calculateScrollingLen();
        int distance = scrollingLen - (screenWidth + mXPaused);
//        int duration = Double.valueOf(mRndDuration * distance * 1.00000 / scrollingLen).intValue();
        int duration = Double.valueOf(mRndDuration * distance * 1.00000 / screenWidth).intValue();
        setVisibility(VISIBLE);
        mSlr.startScroll(mXPaused, 0, distance, 0, duration);
        invalidate();
        mPaused = false;
    }

    /**
     * calculate the scrolling length of the text in pixel
     *
     * @return the scrolling length in pixels
     */
    private int calculateScrollingLen() {
        TextPaint tp = getPaint();
        Rect rect = new Rect();
        String strTxt = getText().toString();
        tp.getTextBounds(strTxt, 0, strTxt.length(), rect);
        int scrollingLen = rect.width() + screenWidth;
        rect = null;
        return scrollingLen;
    }

    /**
     * pause scrolling the text
     */
    public void pauseScroll() {
        if (null == mSlr)
            return;

        if (mPaused)
            return;

        mPaused = true;

        // abortAnimation sets the current X to be the final X,
        // and sets isFinished to be true
        // so current position shall be saved
        mXPaused = mSlr.getCurrX();

        mSlr.abortAnimation();
    }

    @Override
     /*
     * override the computeScroll to restart scrolling when finished so as that
     * the text is scrolled forever
     */
    public void computeScroll() {
        super.computeScroll();

        if (null == mSlr) return;

//        if (mSlr.isFinished() && (!mPaused)) {
//            this.startScroll();
//        }
        if(mSlr.isFinished() && (!mPaused)){
            if(listener != null){
                listener.isFinish();
            }
        }
    }

    public int getRndDuration() {
        return mRndDuration;
    }

    public void setRndDuration(int duration) {
        this.mRndDuration = duration;
    }

    public boolean isPaused() {
        return mPaused;
    }

    public interface onFinishListener{
        void isFinish();
    }

    public void setonFinishListener(onFinishListener listener){
        this.listener = listener;
    }
}
