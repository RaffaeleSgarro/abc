package com.zybnet.abc.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.zybnet.abc.R;
import com.zybnet.abc.utils.AnimationListenerStub;
import com.zybnet.abc.utils.U;

public class HistoryFilpperView extends ViewFlipper {

	private int backId;
	private NavigateBackView back;
	
	public HistoryFilpperView(Context context) {
		super(context);
	}
	
	public HistoryFilpperView(Context context, AttributeSet attrs) {
		super(context, attrs);
		String ns = U.NAMESPACE_PREFIX + context.getPackageName();
		backId = attrs.getAttributeResourceValue(ns, "back", -1);
	}
	
	@Override
	public void onAttachedToWindow() {
		back = (NavigateBackView) ((Activity) getContext()).findViewById(backId);
	}
	
	@Override
	public void onDetachedFromWindow() {
		// TODO cleanup
	}
	
	private boolean _flag = false;
	
	public void showView(View view, Animation in, Animation out, Animation backIn, Animation backOut) {
		back.addItem(this, backIn, backOut);
		addView(view);
		setInAnimation(in);
		setOutAnimation(out);
		_flag = true;
		showNext();
		_flag = false;
	}
	
	public void showView(View view, Animation in, Animation out) {
		showView(view, in, out, in, out);
	}
	
	public void showView(View view) {
		// TODO create suitable animations
		showView(view, R.anim.left_pane_in, R.anim.left_pane_out);
	}
	
	public void showView(View view, int in, int out) {
		showView(view, in, out, in, out);
	}
	
	public void showView(View view, int in, int out, int backIn, int backOut) {
		showView(view, l(in), l(out), l(backIn), l(backOut));
	}
	
	private Animation l(int id) {
		return AnimationUtils.loadAnimation(getContext(), id);
	}
	
	@Override
	public void showNext() {
		if (!_flag)
			throw new IllegalStateException("You shouldn't call showNext()." +
					" Use showView() instead for better control " +
					" over animations and integration with the back history");
		super.showNext();
	}
	
	@Override
	public void showPrevious() {
		Animation in = getInAnimation();
		Animation out = getOutAnimation();
		((in.getDuration() > out.getDuration()) ? in : out)
			.setAnimationListener(new ChildRemover(this));		
		super.showPrevious();
	}
	
	// static, avoid leaks
	private static class ChildRemover extends AnimationListenerStub {
		ChildRemover(ViewGroup parent) {
			this.parent = parent;
		}
		
		private ViewGroup parent;
		
		@Override
		public void onAnimationEnd(Animation animation) {
			parent.removeViewAt(parent.getChildCount() - 1);
			animation.setAnimationListener(null);
		}
	}

}
