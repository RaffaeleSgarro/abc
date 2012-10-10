package com.zybnet.abc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.zybnet.abc.R;
import com.zybnet.abc.utils.AnimationListenerStub;
import com.zybnet.abc.utils.SlotDetailHelper;
import com.zybnet.abc.view.SlotView;
import com.zybnet.abc.view.TableView;

public class RightFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle params) {
		Context mActivity = getActivity();
		FrameLayout root = new FrameLayout(mActivity);
		
		// Setup the timetable view
		TableView table = new TableView(mActivity);
		table.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
		table.setSlotListener(new SlotListener());
				
		root.addView(table);
				
		return root;
	}
	
	private class SlotListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			getTableView().setSlotListener(null);
			
			SlotView slotView = (SlotView) view;
			final ViewGroup leftPane = (ViewGroup) abc().findViewById(R.id.left);
			
			SlotDetailHelper helper = new SlotDetailHelper(abc());
			helper.fillView(slotView.getSlot());
			
			final View oldChild = leftPane.getChildAt(0);
			final View newChild = helper.getView();
			
			leftPane.addView(newChild);
			leftPane.bringChildToFront(oldChild);
			
			AnimationSet a1 = new AnimationSet(true);
			a1.setDuration(500);
			a1.addAnimation(new AlphaAnimation(1, 0));
			a1.addAnimation(new TranslateAnimation(0, 0, 0, -1 * leftPane.getHeight()));
			oldChild.startAnimation(a1);
			a1.setAnimationListener(new AnimationListenerStub(){
				
				@Override
				public void onAnimationEnd(Animation animation) {
					leftPane.removeView(oldChild);
				}
				
			});
			
			AnimationSet a2 = new AnimationSet(true);
			a2.setDuration(400);
			a2.addAnimation(new AlphaAnimation(0, 1));
			a2.addAnimation(new TranslateAnimation(-leftPane.getWidth(), 0, leftPane.getHeight() / 2, 0));
			a2.setStartTime(AnimationUtils.currentAnimationTimeMillis());
			a2.setStartOffset(200);
			a2.setAnimationListener(new AnimationListenerStub(){
				@Override
				public void onAnimationEnd(Animation animation) {
					getTableView().setSlotListener(SlotListener.this);
				}
			});
			newChild.setAnimation(a2);
			
		}
		
	}
}
