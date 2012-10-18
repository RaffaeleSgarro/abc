package com.zybnet.abc.fragment;

import android.graphics.Rect;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.zybnet.abc.R;
import com.zybnet.abc.view.HistoryViewFlipper;
import com.zybnet.abc.view.NavigateBackView;
import com.zybnet.abc.view.PreferenceView;
import com.zybnet.abc.view.SlotDetailView;
import com.zybnet.abc.view.SlotView;
import com.zybnet.abc.view.TableView;

public class CompactFragment extends BaseFragment {

	private class SlotListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			SlotDetailView details = new SlotDetailView(abc(), switcher(), (SlotView) view);
			
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
			
			NavigateBackView.Item item = new NavigateBackView.Item(getActivity());
			item.opener = abc().findViewById(TableView.ID);
			item.view = details;
			item.in = backIn;
			item.out = loadAnimation(R.anim.compact_first_pane_out);
			
			switcher().showView(item, loadAnimation(R.anim.compact_first_pane_in), out);
		}
		
	}
	
	private HistoryViewFlipper switcher() {
		return (HistoryViewFlipper) abc().findViewById(R.id.root);
	}

	@Override
	public OnClickListener getSettingsMenuClickedListener() {
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View btn) {
				if (switcher().getCurrentView() instanceof PreferenceView)
					return;
				
				NavigateBackView.Item item = new NavigateBackView.Item(abc());
				item.view = new PreferenceView(abc());
				item.opener = btn;
				switcher().showView(item, R.anim.page_in_default, R.anim.page_out_default);
			}
		};
	}

	@Override
	public void addTable(TableView table) {
		table.setSlotListener(new SlotListener());
		switcher().addView(table);
	}
	
}
