package com.zybnet.abc.utils;

import android.util.Log;

public class L {
	
	public static final String TAG = "abc";
	
	public static void og(String what) {
		Log.v(TAG, what);
	}
	
	public static void og(Throwable e) {
		Log.e(TAG, e.getMessage(), e);
	}
}
