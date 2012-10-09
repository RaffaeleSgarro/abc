package com.zybnet.abc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zybnet.abc.R;
import com.zybnet.abc.view.SlotView;
import com.zybnet.abc.view.TimeTable;

public class Compact extends Fragment {
	
	// TODO read from preferences
	private FrameLayout root;
	private TimeTable table;
	private RelativeLayout detail;
	private TextView detail_title;
	
	public Compact() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
		Activity mActivity = getActivity();
		root = new FrameLayout(mActivity);
		
		// Setup detail view
		detail = (RelativeLayout) mActivity.getLayoutInflater().inflate(
				R.layout.cell_detail, root, false);
		detail.setVisibility(View.INVISIBLE);
		detail_title = (TextView) detail.findViewById(R.id.title);
		detail.findViewById(R.id.discard).setOnClickListener(new BaseButtonsListener());
		
		root.addView(detail);

		// Setup the timetable view
		table = new TimeTable(mActivity);
		table.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
		table.setSlotListener(new SlotListener());
		
		root.addView(table);
		
		return root;
	}

	// These variable describe the "selected" cell and are
	// read by detail
	private int _x;
	private int _y;
	private int _selected_row;
	private int _selected_col;

	/*
	 * Set as the slot listener for the time table. Performs the zooming
	 */
	private class SlotListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			AnimationSet set = new AnimationSet(true);
			set.setDuration(1000);
			set.addAnimation(new ScaleAnimation(1, table.rows(), 1, table.cols()));
			set.addAnimation(new AlphaAnimation(1, 0));
			int[] location = new int[2];
			view.getLocationOnScreen(location);
			_x = -location[0] * table.rows();
			_y = -location[1] * table.cols();
			set.addAnimation(new TranslateAnimation(0, _x, 0, _y));
			set.setFillAfter(true);
			table.startAnimation(set);
			
			// TODO prepare detail
			SlotView cell = (SlotView) view;
			_selected_row = cell.getRow();
			_selected_col = cell.getColumn();
			detail_title.setText(cell.toString());

			AnimationSet dSet = new AnimationSet(true);
			dSet.addAnimation(new AlphaAnimation(0, 1));
			dSet.addAnimation(new TranslateAnimation(0, 0, -detail.getHeight(), 0));
			dSet.setStartTime(AnimationUtils.currentAnimationTimeMillis());
			dSet.setStartOffset(250);
			dSet.setDuration(500);
			root.bringChildToFront(detail);
			detail.startAnimation(dSet);
			detail.setVisibility(View.VISIBLE);
		}
	}
	
	private class BaseButtonsListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			AnimationSet a1 = new AnimationSet(true);
			a1.addAnimation(new AlphaAnimation(1, 0));
			a1.addAnimation(new TranslateAnimation(0, 0, 0, -detail.getHeight()));
			a1.setDuration(500);
			a1.setFillAfter(true);
			detail.startAnimation(a1);

			root.bringChildToFront(table);
			AnimationSet a2 = new AnimationSet(true);
			a2.setDuration(500);
			a2.setStartTime(AnimationUtils.currentAnimationTimeMillis());
			a2.setFillAfter(true);
			a2.setStartOffset(250);
			a2.addAnimation(new AlphaAnimation(0, 1));
			a2.addAnimation(new ScaleAnimation(table.rows(), 1, table.cols(), 1));
			a2.addAnimation(new TranslateAnimation(_x, 0, _y, 0));
			table.startAnimation(a2);
		}
	} 
	
}
