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
}
