package com.zybnet.abc.fragment;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ViewSwitcher;

import com.zybnet.abc.R;
import com.zybnet.abc.utils.SlotDetailHelper;
import com.zybnet.abc.utils.U;
import com.zybnet.abc.view.SlotView;
import com.zybnet.abc.view.TableView;

public class CompactFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
		Activity mActivity = getActivity();
		
		// Setup the timetable view
		TableView table = new TableView(mActivity);
		table.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
		table.setSlotListener(new SlotListener());
		
		return table;
	}

	private class SlotListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			SlotDetailHelper helper = new SlotDetailHelper(abc());
			helper.fillView((SlotView) view, CompactFragment.this);
			
			// TODO
			Rect bounds = new Rect();
			view.getGlobalVisibleRect(bounds);
			
			AnimationSet out = new AnimationSet(true);
			out.addAnimation(new ScaleAnimation(1, 5, 1, 5,
					ScaleAnimation.ABSOLUTE, bounds.centerX(),
					ScaleAnimation.ABSOLUTE, bounds.centerY()));
			out.addAnimation(new TranslateAnimation(0, -bounds.left, 0, -bounds.top));
			out.addAnimation(new AlphaAnimation(1, 0));
			out.setDuration(500);
			
			U.swap((ViewSwitcher) abc().findViewById(R.id.root), helper.getView(),
					loadAnimation(R.anim.compact_first_pane_in), out);
			
			abc().findViewById(R.id.actionbar_back).setOnClickListener(
					new BackNavigationListener(bounds));
		}
		
	}
	
	private class BackNavigationListener implements View.OnClickListener {

		private Rect bounds;
		
		public BackNavigationListener(Rect bounds) {
			this.bounds = bounds;
		}
		
		@Override
		public void onClick(View v) {
			AnimationSet in = new AnimationSet(true);
			in.addAnimation(new AlphaAnimation(0, 1));
			in.addAnimation(new ScaleAnimation(5, 1, 5, 1,
					ScaleAnimation.ABSOLUTE, bounds.centerX(),
					ScaleAnimation.ABSOLUTE, bounds.centerY()));
			in.addAnimation(new TranslateAnimation(-bounds.left, 0, -bounds.top, 0));
			in.setDuration(500);
			
			TableView table = getTableView();
			
			((ViewGroup) table.getParent()).removeView(table);
			
			U.swap((ViewSwitcher) abc().findViewById(R.id.root),
					table, in, loadAnimation(R.anim.compact_first_pane_out));
			
			// TODO remove here and find a general pattern
			getActivity().findViewById(R.id.actionbar_back).setOnClickListener(null);
		}
	}
	
}
