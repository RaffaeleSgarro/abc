package com.zybnet.abc.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

public class TableLayout extends LinearLayout {
	
	public TableLayout(Context context) {
		super(context);
		setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onWindowFocusChanged(boolean has) {
		if (has) {
			getHandler().postDelayed(new Runnable() {
				public void run() {
					startAnimation();
				}
			}, 1000); // the animation is sloppy when the application loads. wait some time
		}
	}
	
	private void startAnimation() {
		// add listener to set drawChildren to true
		setVisibility(View.VISIBLE);
		long startTimeMillis = AnimationUtils.currentAnimationTimeMillis();
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
				Log.v("abc", "start at: " + animation.getStartTime());
				
			}
		}
		
	}
}
