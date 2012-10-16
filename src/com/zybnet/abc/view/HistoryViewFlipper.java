package com.zybnet.abc.view;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ViewFlipper;

import com.zybnet.abc.R;
import com.zybnet.abc.utils.AnimationListenerStub;
import com.zybnet.abc.utils.U;
import com.zybnet.abc.view.NavigateBackView.Item;

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
	
	public void showView(NavigateBackView.Item item, Animation in, Animation out) {
		setInAnimation(in);
		setOutAnimation(out);
		item.animator = this;
		back.addItem(item);
		addView(item.view);
		invokeShowNext();
	}
	
	private int getDefaultInAnimation() {
		return R.anim.page_in_default;
	}
	
	private int getDefaultOutAnimation() {
		return R.anim.page_out_default;
	}
	
	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getWindowToken(), 0);
	}
	
	public void showView(Item item) {
		showView(item, getDefaultInAnimation(), getDefaultOutAnimation());
	}
	
	public void showView(Item item, int in, int out) {
		showView(item, l(in), l(out));
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
		hideKeyboard();
		super.showNext();
	}
	
	@Override
	public void showPrevious() {
		removeAfterAnimation(new Filter(){
			public boolean accept(View view, HistoryViewFlipper parent) {
				int i = parent.indexOfChild(view);
				return i == (parent.getChildCount() - 1);
			}
		});	
		hideKeyboard();
		super.showPrevious();
	}
	
	public static interface Filter {
		boolean accept(View view, HistoryViewFlipper parent);
	}
	
	private class RemoveListener extends AnimationListenerStub {
		
		List<Filter> filters = new LinkedList<Filter>();
		
		@Override
		public void onAnimationEnd(Animation a) {
			a.setAnimationListener(null);
			post(new Runnable() {
				public void run() {
					backupAnimations();
					for (Filter filter: filters) {
						List<View> children = new LinkedList<View>();
						for (int i = 0; i < getChildCount(); i++) {
							View child = getChildAt(i);
							if (filter.accept(child, HistoryViewFlipper.this))
								children.add(child);
						}
						for (View child: children) {
							removeView(child);
						}
					}
					restoreAnimationBackup();
					filters.clear();
				}
			});
		}
	};
	
	private RemoveListener removeListener = new RemoveListener();
	
	public void removeAfterAnimation(final Filter filter) {
		removeListener.filters.add(filter);
		getOutAnimation().setAnimationListener(removeListener);
	}
	
	public void swapRootChild(final View view, Animation in, Animation out) {
		addView(view);
		back.clearHistoryFor(this);
		setInAnimation(in);
		setOutAnimation(out);
		removeAfterAnimation(new Filter(){
			public boolean accept(View view, HistoryViewFlipper parent) {
				int i = parent.indexOfChild(view);
				return i < parent.getChildCount() - 1;
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
