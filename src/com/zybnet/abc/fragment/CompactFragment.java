package com.zybnet.abc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.zybnet.abc.R;
import com.zybnet.abc.utils.SlotDetailHelper;
import com.zybnet.abc.view.SlotView;
import com.zybnet.abc.view.TableView;

public class CompactFragment extends BaseFragment {
	
	// TODO read from preferences
	private FrameLayout root;
	private TableView table;
	private SlotDetailHelper detail;
	
	public CompactFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
		Activity mActivity = getActivity();
		root = new FrameLayout(mActivity);
		
		// Setup detail view
		detail = new SlotDetailHelper(getActivity());
		detail.getView().setVisibility(View.INVISIBLE);
		
		root.addView(detail.getView());

		// Setup the timetable view
		table = new TableView(mActivity);
		table.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
		table.setSlotListener(new SlotListener());
		
		root.addView(table);
		
		return root;
	}

	// These variable describe the "selected" cell and are
	// read by detail
	private int _x;
	private int _y;

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
			table.setAnimation(set);
			
			// TODO prepare detail
			SlotView cell = (SlotView) view;
			detail.fillView(cell, CompactFragment.this);

			View dv = detail.getView();
			AnimationSet dSet = new AnimationSet(true);
			dSet.addAnimation(new AlphaAnimation(0, 1));
			dSet.addAnimation(new TranslateAnimation(0, 0, -dv.getHeight(), 0));
			dSet.setStartTime(AnimationUtils.currentAnimationTimeMillis());
			dSet.setStartOffset(250);
			dSet.setDuration(500);
			root.bringChildToFront(dv);
			dv.startAnimation(dSet);
			dv.setVisibility(View.VISIBLE);
			
			getActivity().findViewById(R.id.actionbar_back).setOnClickListener(new BaseButtonsListener());
		}
	}
	
	private class BaseButtonsListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			AnimationSet a1 = new AnimationSet(true);
			a1.addAnimation(new AlphaAnimation(1, 0));
			a1.addAnimation(new TranslateAnimation(0, 0, 0, -detail.getView().getHeight()));
			a1.setDuration(500);
			a1.setFillAfter(true);
			detail.getView().startAnimation(a1);

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
			
			// stop listening
			getActivity().findViewById(R.id.actionbar_back).setOnClickListener(null);
		}
	} 
	
}
