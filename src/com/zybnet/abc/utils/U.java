package com.zybnet.abc.utils;

import java.util.Calendar;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewAnimator;

/*
 * Utility class
 */
public class U {
	
	public static final String NAMESPACE_PREFIX = "http://schemas.android.com/apk/res/";
	
	public static final String P_DAY_PREFIX = "day_";
	public static final String P_DECORATE_DAYS = "decorate_days";
	public static final String P_DECORATE_ORDS = "decorate_ords";
	public static final String P_SLOTS_PER_DAY = "slots_per_day";
	
	public static final int SLOTS_PER_DAY_MIN = 3;
	public static final int SLOTS_PER_DAY_DEFAULT = 5;
	public static final int SLOTS_PER_DAY_MAX = 7;
	
	public static final String SQL_DATE_FORMAT = "yyyy-MM-dd";
	public static final String SQL_TIME_FORMAT = "HH:mm";
	
	private static Calendar calendar;
	
	static {
		calendar = Calendar.getInstance();
	}
	
	public static String uppercaseFirstChar(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
	
	public static Calendar getLocalizedDayOfTheWeek(int day) {
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + day - 1);
		return calendar;
	}
	
	/*
	 * TODO remove
	 * May return null
	 */
	@Deprecated
	public static View swap(ViewAnimator flipper, View view, Animation in, Animation out) {
		flipper.setInAnimation(in);
		flipper.setOutAnimation(out);
		
		View returned = null;
		
		if (flipper.getChildCount() == 2) {
			returned = flipper.getChildAt(0);
			flipper.removeViewAt(0);
		}
		
		flipper.addView(view);
		flipper.showNext();
		
		return returned;
	}
	
	@Deprecated
	public static View swap(ViewAnimator flipper, View view, int in, int out) {
		return swap(flipper, view,
				AnimationUtils.loadAnimation(flipper.getContext(), in),
				AnimationUtils.loadAnimation(flipper.getContext(), out));
		
	}
	
	private static final int DONT_CHANGE_PADDING = 91281239;
	
	private static void setPadding(View view, int left, int top, int right, int bottom) {
		view.setPadding(
				left == DONT_CHANGE_PADDING ? view.getPaddingLeft() : left,
				top == DONT_CHANGE_PADDING ? view.getPaddingTop() : top,
				right == DONT_CHANGE_PADDING ? view.getPaddingRight() : right,
				bottom == DONT_CHANGE_PADDING ? view.getPaddingBottom() : bottom);
	}
	
	public static void setPaddingLeft(View view, int padding) {
		setPadding(view, padding, DONT_CHANGE_PADDING, DONT_CHANGE_PADDING, DONT_CHANGE_PADDING);
	}
	
	public static void setPaddingRight(View view, int padding) {
		setPadding(view, DONT_CHANGE_PADDING, DONT_CHANGE_PADDING, padding, DONT_CHANGE_PADDING);
	}
	
	public static void setPaddingTop(View view, int padding) {
		setPadding(view, DONT_CHANGE_PADDING, padding, DONT_CHANGE_PADDING, DONT_CHANGE_PADDING);
	}
	
	public static void setPaddingBottom(View view, int padding) {
		setPadding(view, DONT_CHANGE_PADDING, DONT_CHANGE_PADDING, DONT_CHANGE_PADDING, padding);
	}
}
