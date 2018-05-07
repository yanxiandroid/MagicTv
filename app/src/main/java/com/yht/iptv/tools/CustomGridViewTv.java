package com.yht.iptv.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.open.androidtvwidget.view.WidgetTvViewBring;

public class CustomGridViewTv extends GridView {


	public CustomGridViewTv(Context context) {
        super(context);
		init(context, null);
	}

	public CustomGridViewTv(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}



	/**
	 * 崩溃了.
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		try {
			super.dispatchDraw(canvas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	WidgetTvViewBring mWidgetTvViewBring;

	private void init(Context context, AttributeSet attrs) {
		this.setChildrenDrawingOrderEnabled(true);
		mWidgetTvViewBring = new WidgetTvViewBring(this);
	}

	@Override
	public void bringChildToFront(View child) {
		mWidgetTvViewBring.bringChildToFront(this, child);
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		// position = getSelectedItemPosition() - getFirstVisiblePosition();
		return mWidgetTvViewBring.getChildDrawingOrder(childCount, i);
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		int selectedItemPosition = getSelectedItemPosition();

		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		if(gainFocus){
			if(selectedItemPosition == -1 || getCount() <= selectedItemPosition){
				setSelection(0);
			}else {
				setSelection(selectedItemPosition);
			}
		}
	}

	@Override
	public boolean isInTouchMode() {
		return !(hasFocus() && !super.isInTouchMode());
	}
}