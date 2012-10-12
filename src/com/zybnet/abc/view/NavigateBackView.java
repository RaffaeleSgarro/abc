package com.zybnet.abc.view;

import java.util.Stack;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
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

	public void addItem(ViewAnimator animator, Animation in, Animation out) {
		history.push(new Item(animator, in, out));
	}
	
	private class Back implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			if (history.size() == 0)
				return;
			
			// TODO remove listeners
			Item item = history.pop();
			item.animator.setInAnimation(item.in);
			item.animator.setOutAnimation(item.out);
			item.animator.showPrevious();
			// TODO setup listeners
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
	
	private static class Item {
		public Item(ViewAnimator animator, Animation in, Animation out) {
			this.animator = animator;
			this.in = in;
			this.out = out;
		}
		public ViewAnimator animator;
		public Animation in, out;
	}
}
