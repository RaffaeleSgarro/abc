package com.zybnet.abc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

public class TableLayout extends LinearLayout {

	private volatile boolean drawChildren;
	
	public TableLayout(Context context) {
		super(context);
	}
	
	@Override
	public void onWindowFocusChanged(boolean has) {
		if (has) {	
			startAnimation();
		}
	}
	
	@Override
	public void dispatchDraw(Canvas c) {
		if (!drawChildren)
			return;
		
		super.dispatchDraw(c);
	}
	
	
	private void startAnimation() {
		// add listener to set drawChildren to true
		long startTimeMillis = AnimationUtils.currentAnimationTimeMillis();
		drawChildren = true;
		for (int i = 0; i < getChildCount(); i++) {
			ViewGroup row = (ViewGroup) getChildAt(i);
			int rowCount = row.getChildCount();
			for (int j = 0; j < rowCount; j++) {
				View cell = row.getChildAt(j);
				AnimationSet animation = new AnimationSet(true);
				animation.addAnimation(new AlphaAnimation(0, 1f));
				animation.addAnimation(new ScaleAnimation(0.5f, 1, 1, 1));
				animation.setDuration(1000);
				animation.setStartTime(startTimeMillis + (rowCount * i + j) * 40);
				cell.setAnimation(animation);
			}
		}
		
	}
}
