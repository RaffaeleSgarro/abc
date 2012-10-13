package com.zybnet.abc.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.zybnet.abc.R;
import com.zybnet.abc.utils.AnimationListenerStub;
import com.zybnet.abc.utils.U;

/*
 * The key point of this animator is integration with
 * a NavigationBackView. The two workhorse methods are
 * 
 *  - showView(view, in, out, backIn, backOut)
 * which interacts with the global history
 * 
 * and
 * 
 *  - swapRootChild(view, in, out)
 * which does not register a new entry in the navigation
 * history
 * 
 */
public class HistoryViewFlipper extends ViewFlipper {

	private int backId;
	private NavigateBackView back;
	
	public HistoryViewFlipper(Context context) {
		super(context);
	}
	
	public HistoryViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
		String ns = U.NAMESPACE_PREFIX + context.getPackageName();
		backId = attrs.getAttributeResourceValue(ns, "back", -1);
	}
	
	@Override
	public void onAttachedToWindow() {
		back = (NavigateBackView) ((Activity) getContext()).findViewById(backId);
		if (back == null) {
			throw new NullPointerException("Have you set the back button with?" +
					" abc:back=\"@+id/nav_back\"");
		}
	}
	
	@Override
	public void onDetachedFromWindow() {
		back.clearHistoryFor(this);
		back = null;
	}
	
	private boolean _flag = false;
	
	public void showView(View view, Animation in, Animation out, Animation backIn, Animation backOut) {
		back.addItem(this, backIn, backOut);
		addView(view);
		setInAnimation(in);
		setOutAnimation(out);
		invokeShowNext();
	}
	
	public void showView(View view, Animation in, Animation out) {
		showView(view, in, out, in, out);
	}
	
	private int getDefaultInAnimation() {
		return R.anim.left_pane_in;
	}
	
	private int getDefaultOutAnimation() {
		return R.anim.left_pane_out;
	}
	
	public void showView(View view) {
		// TODO create suitable animations
		showView(view, getDefaultInAnimation(), getDefaultOutAnimation());
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
		getOutAnimation().setAnimationListener(new AnimationListenerStub(){
			@Override
			public void onAnimationEnd(Animation animation) {
				animation.setAnimationListener(null);
				post(new Runnable(){
					public void run() {
						backupAnimations();
						removeViewAt(getChildCount() - 1);
						restoreAnimationBackup();
					}
				});
			}
		});		
		super.showPrevious();
	}
	
	private Animation getEndsLast(Animation a1, Animation a2) {
		return (getAnimationEnd(a1) > getAnimationEnd(a2)) ? a1 : a2;
	}
	
	private long getAnimationEnd(Animation a) {
		return a.getStartTime() + a.getStartOffset() + a.getDuration();
	}
	
	public void swapRootChild(final View view, Animation in, Animation out) {
		addView(view);
		back.clearHistoryFor(this);
		setInAnimation(in);
		setOutAnimation(out);
		getEndsLast(in, out).setAnimationListener(new AnimationListenerStub() {
			@Override
			public void onAnimationEnd(Animation a){
				// remove this listener
				a.setAnimationListener(null);
				post(new Runnable() {
					public void run() {
						backupAnimations();
						
						while (getChildCount() > 1)
							removeViewAt(0);
						
						restoreAnimationBackup();
					}
				});
			}
		});
				
		invokeShowNext();
	}
	
	private Animation backupIn, backupOut;
	
	private void backupAnimations() {
		backupIn = getInAnimation();
		setInAnimation(null);
		backupOut = getOutAnimation();
		setOutAnimation(null);
	}
	
	private void restoreAnimationBackup(){
		setInAnimation(backupIn);
		setOutAnimation(backupOut);
	}
	
	private void invokeShowNext() {
		_flag = true;
		showNext();
		_flag = false;
	}
	
	public void swapRootChild(View view, int in, int out) {
		swapRootChild(view, l(in), l(out));
	}

	public void swapRootChild(View view) {
		swapRootChild(view, getDefaultInAnimation(), getDefaultOutAnimation());
	}
	
}
