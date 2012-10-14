package com.zybnet.abc.fragment;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.zybnet.abc.R;
import com.zybnet.abc.view.HistoryViewFlipper;
import com.zybnet.abc.view.PreferenceView;
import com.zybnet.abc.view.SlotDetailView;
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
			SlotDetailView details = SlotDetailView.create(abc(), (SlotView) view);
			
			Rect bounds = new Rect();
			view.getGlobalVisibleRect(bounds);
			
			AnimationSet out = new AnimationSet(true);
			out.addAnimation(new ScaleAnimation(1, 5, 1, 5,
					ScaleAnimation.ABSOLUTE, bounds.centerX(),
					ScaleAnimation.ABSOLUTE, bounds.centerY()));
			out.addAnimation(new TranslateAnimation(0, -bounds.left, 0, -bounds.top));
			out.addAnimation(new AlphaAnimation(1, 0));
			out.setDuration(500);
			
			AnimationSet backIn = new AnimationSet(true);
			backIn.addAnimation(new AlphaAnimation(0, 1));
			backIn.addAnimation(new ScaleAnimation(5, 1, 5, 1,
					ScaleAnimation.ABSOLUTE, bounds.centerX(),
					ScaleAnimation.ABSOLUTE, bounds.centerY()));
			backIn.addAnimation(new TranslateAnimation(-bounds.left, 0, -bounds.top, 0));
			backIn.setDuration(500);
			
			switcher().showView(details,
					loadAnimation(R.anim.compact_first_pane_in), out,
					backIn, loadAnimation(R.anim.compact_first_pane_out));
		}
		
	}
	
	private HistoryViewFlipper switcher() {
		return (HistoryViewFlipper) abc().findViewById(R.id.root);
	}

	@Override
	public OnClickListener getSettingsMenuClickedListener() {
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (switcher().getCurrentView() instanceof PreferenceView)
					return;
				
				PreferenceView p = new PreferenceView(abc());
				switcher().showView(p, R.anim.left_pane_in, R.anim.left_pane_out);
			}
		};
	}
	
}
