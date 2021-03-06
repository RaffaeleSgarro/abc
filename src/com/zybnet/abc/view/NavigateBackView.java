package com.zybnet.abc.view;

import java.util.Iterator;
import java.util.Stack;

import com.zybnet.abc.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ViewAnimator;

public class NavigateBackView extends ImageButton {

	private Stack<Item> history = new Stack<Item>();
	
	public NavigateBackView(Context context) {
		super(context);
	}
	
	public NavigateBackView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public NavigateBackView(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
	}

	public void addItem(Item item) {
		Iterator<Item> i = history.iterator();
		while (i.hasNext()) {
			final Item current = i.next();
			if (current.opener.equals(item.opener)) {
				i.remove();
				current.animator.removeAfterAnimation(new HistoryViewFlipper.Filter() {			
					@Override
					public boolean accept(View view, HistoryViewFlipper parent) {
						return view.equals(current.view);
					}
				});
				break;
			}
		}
		
		if (history.size() > 0) {
			final Item last = history.lastElement();
			if (!last.keep) {
				history.pop();
				last.animator.removeAfterAnimation(new HistoryViewFlipper.Filter() {
					
					@Override
					public boolean accept(View view, HistoryViewFlipper parent) {
						return view.equals(last.view);
					}
				});
			}
		}
		
		history.push(item);
		if (history.size() == 1) {
			startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alpha_in));
		}
	}
	
	private class Back implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			back();
		}
	}
	
	public void back() {
		if (history.size() == 0)
			return;
		
		// TODO remove listeners
		Item item = history.pop();
		item.animator.setInAnimation(item.in);
		item.animator.setOutAnimation(item.out);
		item.animator.showPreviousSafe();
		// TODO setup listeners
		
		if (history.size() == 0) {
			startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alpha_out));
		}
	}
	
	@Override
	public void onAttachedToWindow() {
		setOnClickListener(new Back());
	}
	
	@Override
	public void onDetachedFromWindow() {
		setOnClickListener(null);
	}
	
	public static class Item {
		public Item(Context ctx) {
			in = AnimationUtils.loadAnimation(ctx, R.anim.page_back_in_default);
			out = AnimationUtils.loadAnimation(ctx, R.anim.page_back_out_default);
		}
		
		public View opener, view;
		public HistoryViewFlipper animator;
		public Animation in, out;
		// This item participate to the history
		// If set to false, when a new item is pushed
		// in the same animator, the current item is removed from
		// both the animator and the listener. This is intended
		// for "sticky" views like PreferenceView
		public boolean keep = true;
	}
	
	public void clearHistoryFor(ViewAnimator animator) {
		Iterator<Item> i = history.iterator();
		while (i.hasNext()) {
			Item current = i.next();
			if (current.animator.equals(animator)) {
				i.remove();
			}
		}
	}
}
