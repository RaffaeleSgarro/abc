package com.zybnet.abc.utils;

import java.util.Calendar;

import android.view.View;
import android.widget.ViewSwitcher;

/*
 * Utility class
 */
public class U {
	
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
	 * May return null
	 */
	public static View swap(ViewSwitcher flipper, View view, int in, int out) {
		flipper.setInAnimation(flipper.getContext(), in);
		flipper.setOutAnimation(flipper.getContext(), out);
		
		View returned = null;
		
		if (flipper.getChildCount() == 2) {
			returned = flipper.getChildAt(0);
			flipper.removeViewAt(0);
		}
		
		flipper.addView(view);
		flipper.showNext();
		
		return returned;
	}
}
