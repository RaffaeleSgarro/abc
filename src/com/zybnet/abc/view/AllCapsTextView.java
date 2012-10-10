package com.zybnet.abc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class AllCapsTextView extends TextView {

	public AllCapsTextView(Context context) {
		super(context);
	}
	
	public AllCapsTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public AllCapsTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void setText(CharSequence str, TextView.BufferType buffer) {
		super.setText(str.toString().toUpperCase(), buffer);
	}

}
