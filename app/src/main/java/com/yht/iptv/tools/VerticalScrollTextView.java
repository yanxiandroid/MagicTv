package com.yht.iptv.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.yht.iptv.R;
import com.yht.iptv.utils.AppUtils;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.LanguageUtils;

import java.util.ArrayList;
import java.util.List;

public class VerticalScrollTextView extends AppCompatTextView {

    private float step = 0f;
    private Paint mPaint;
    private String text;
    private List<String> textList;
    private int lineNum;//一行显示的字符
    private float textSize;//字体大小
    private float lineSpacingMultiplier;
    private MyRun myRun;
    private boolean isDesc;//是否从上往下

    public VerticalScrollTextView(Context context) {
        super(context, null);
    }

    public VerticalScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalScrollTextView);
        lineNum = typedArray.getInteger(R.styleable.VerticalScrollTextView_VerticalScrollTextView_lineNum, 10);
        textSize = typedArray.getDimension(R.styleable.VerticalScrollTextView_VerticalScrollTextView_textSize, context.getResources().getDimension(R.dimen.w_1));
        lineSpacingMultiplier = typedArray.getFloat(R.styleable.VerticalScrollTextView_VerticalScrollTextView_lineSpacingMultiplier, 1);
        int color = typedArray.getColor(R.styleable.VerticalScrollTextView_VerticalScrollTextView_textColor, 0xffff00);
        if (LanguageUtils.getInstance().getLanguage(context).equals("en")) {
            if (Constants.verticalscrolltext.equals("movie")) {
                lineNum = lineNum * 2;
            }
        }
        mPaint = new Paint();
        myRun = new MyRun();
        mPaint.setColor(color);
        mPaint.setTextSize(textSize);
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setAntiAlias(true);
        textList = new ArrayList<>();
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        String s = getText().toString();
        text = AppUtils.stringFilter(s);
        textList.clear();

        if (text.contains("\n")) {
            String[] strings = text.split("\n");
            for (int i = 0; i < strings.length; i++) {
                List<String> list = setTextInfo(strings[i]);
                textList.addAll(list);
            }
        } else {
            List<String> list = setTextInfo(text);
            textList.addAll(list);
        }
    }

    public void setStart() {
        removeCallbacks(myRun);
        step = 0f;
        invalidate();
    }

    //下面代码是利用上面计算的显示行数，将文字画在画布上，实时更新。
    @Override
    public void onDraw(Canvas canvas) {
        if (textList.size() == 0) return;

        if (textList.size() * textSize * lineSpacingMultiplier - this.getHeight() > -8) {

            for (int i = 0; i < textList.size(); i++) {
                canvas.drawText(textList.get(i), 0, (i + 1) * textSize * lineSpacingMultiplier - step, mPaint);
            }
            if (step == 0) {
                isDesc = true;
                removeCallbacks(myRun);
                postDelayed(myRun, 5000);
            } else if (step < textList.size() * textSize * lineSpacingMultiplier - VerticalScrollTextView.this.getHeight() + 8) { //没到底部
                if (step > 0) {
                    removeCallbacks(myRun);
                    postDelayed(myRun, 0);
                } else {
                    step = 0;
                    invalidate();
                }
            } else {  //底部
                isDesc = false;
                removeCallbacks(myRun);
                postDelayed(myRun, 5000);
            }

        } else {
            for (int i = 0; i < textList.size(); i++) {
                canvas.drawText(textList.get(i), 0, (i + 1) * textSize * lineSpacingMultiplier, mPaint);
            }
        }
    }


    private class MyRun implements Runnable {

        @Override
        public void run() {
            postInvalidate();
            if (isDesc) {
                step = step + 0.3f;
            } else {
                step = step - 0.3f;
            }
        }
    }


    private List<String> setTextInfo(String text) {
        List<String> list = new ArrayList<>();
        int length = text.length();
        int verticalNum;
        int shi = length / lineNum;
        int ge = length % lineNum;
        if (ge != 0) {
            verticalNum = shi + 1;
        } else {
            verticalNum = shi;
        }
        for (int i = 0; i < verticalNum; i++) {
            if (length > lineNum * (i + 1)) {
                list.add(text.substring(lineNum * i, lineNum * (i + 1)));
            } else {
                list.add(text.substring(lineNum * i, length));
            }
        }
        return list;
    }

}