package com.zybnet.abc.utils;

import java.util.Calendar;

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
}
