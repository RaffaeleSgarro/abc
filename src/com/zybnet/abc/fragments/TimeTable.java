package com.zybnet.abc.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.zybnet.abc.view.Cell;
import com.zybnet.abc.view.TableLayout;

public class TimeTable extends Fragment {

	private int rows, columns;
	private View.OnClickListener listener;
	private LinearLayout root;
	
	public TimeTable(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		listener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final int rows = TimeTable.this.rows;
				final int cols = TimeTable.this.columns;
				AnimationSet set = new AnimationSet(true);
				set.setDuration(1000);
				set.addAnimation(new ScaleAnimation(1, rows, 1, cols));
				set.addAnimation(new AlphaAnimation(1, 0));
				int[] location = new int[2];
				view.getLocationOnScreen(location);
				
				set.addAnimation(new TranslateAnimation(0, -location[0] * rows, 0, -location[1] * cols));
				root.startAnimation(set);
			}
		};
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
		Activity mActivity = getActivity();
		final int FILL = ViewGroup.LayoutParams.FILL_PARENT;
		LinearLayout root = new TableLayout(mActivity);
		root.setLayoutParams( new ViewGroup.LayoutParams(FILL, FILL));
		root.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(FILL, FILL, 1f/rows);
		LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(FILL, FILL, 1f/columns);
		
		int[][] bgColors = new int[2][2];
		bgColors[0][0] = Color.parseColor("#e5e5e5"); // grey
		bgColors[0][1] = Color.rgb(255, 255, 255); // white
		bgColors[1][0] = Color.parseColor("#e5cbe5"); // purple
		bgColors[1][1] = Color.parseColor("#ffe5ff"); //pink
		
		final int grey = Color.parseColor("#555555"); // for text
		
		for (int i = 0; i < rows; i ++) {
			LinearLayout row = new LinearLayout(mActivity);
			row.setOrientation(LinearLayout.HORIZONTAL); // default, but anyway
			for (int j = 0; j < columns; j++) {
				Cell cell = new Cell(mActivity, "FOO", bgColors[i % 2][j % 2]);
				cell.setTextColor(grey);
				cell.setGravity(Gravity.CENTER);
				cell.setOnClickListener(listener);
				row.addView(cell, cellParams);
			}
			root.addView(row, rowParams);
		}
		this.root = root;
		return root;
	}
	
}
