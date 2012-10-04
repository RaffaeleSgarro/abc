package com.zybnet.abc.view;

import android.content.Context;
import android.widget.TextView;

public class Cell extends TextView {

	public Cell(Context ctx, CharSequence text, int background) {
		this(ctx);
		setText(text);
		setBackgroundColor(background);
	}
	
	public Cell(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

}
