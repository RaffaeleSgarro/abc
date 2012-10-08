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
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zybnet.abc.R;
import com.zybnet.abc.view.Cell;
import com.zybnet.abc.view.TableLayout;

public class TimeTable extends Fragment {

	private int rows, columns;
	private View.OnClickListener listener;
	private FrameLayout root;
	private TableLayout table;
	private RelativeLayout detail;
	
	public TimeTable(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		listener = new ListenerImpl();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
		root = new FrameLayout(this.getActivity());
		
		Activity mActivity = getActivity();
		
		// Setup detail view
		detail = (RelativeLayout) mActivity.getLayoutInflater().inflate(R.layout.cell_detail, root, false);
		detail.setVisibility(View.INVISIBLE);
		detail.findViewById(R.id.discard).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View button) {
				root.bringChildToFront(table);
				AnimationSet a1 = new AnimationSet(true);
				a1.addAnimation(new AlphaAnimation(1, 0));
				a1.addAnimation(new TranslateAnimation(0, 0, 0, -detail.getHeight()));
				a1.setDuration(500);
				a1.setFillAfter(true);
				detail.startAnimation(a1);
				
				AnimationSet a2 = new AnimationSet(true);
				a2.setDuration(500);
				long start = AnimationUtils.currentAnimationTimeMillis();
				a2.setStartTime(start);
				a2.setStartOffset(250);
				a2.addAnimation(new AlphaAnimation(0, 1));
				a2.addAnimation(new ScaleAnimation(rows, 1, columns, 1));
				a2.addAnimation(new TranslateAnimation(_x, 0, _y, 0));
				table.setAnimation(a2);
			}
		});
		root.addView(detail);
		
		// Setup the timetable view
		final int FILL = ViewGroup.LayoutParams.FILL_PARENT;
		table = new TableLayout(mActivity);
		table.setLayoutParams( new ViewGroup.LayoutParams(FILL, FILL));
		table.setOrientation(LinearLayout.VERTICAL);
		
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
			table.addView(row, rowParams);
		}
		root.addView(table);
		return root;
	}
	
	private int _x; 
	private int _y;
	
	private class ListenerImpl implements View.OnClickListener {
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
			_x = -location[0] * rows;
			_y = -location[1] * cols;
			set.addAnimation(new TranslateAnimation(0, _x, 0, _y));
			set.setFillAfter(true);
			
			// TODO prepare detail
			AnimationSet dSet = new AnimationSet(true);
			dSet.addAnimation(new AlphaAnimation(0, 1));
			dSet.addAnimation(new TranslateAnimation(0, 0, - detail.getHeight(), 0));
			dSet.setStartTime(AnimationUtils.currentAnimationTimeMillis());
			dSet.setStartOffset(250);
			dSet.setDuration(500);
			root.bringChildToFront(detail);
			detail.startAnimation(dSet);
			detail.setVisibility(View.VISIBLE);
			table.startAnimation(set);
		}
	}
	
}
