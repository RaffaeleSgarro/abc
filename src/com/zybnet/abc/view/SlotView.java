package com.zybnet.abc.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

public class SlotView extends TextView {

	private int row = -1, column = -1;
	
	public SlotView(Context ctx, CharSequence text) {
		this(ctx);
		setText(text);
		setClickable(true);
	}
	
	public SlotView(Context context) {
		super(context);
	}
	
	public void insertAt(ViewGroup parent, ViewGroup.LayoutParams params, int row, int column) {
		parent.addView(this, column, params);
		this.row = row;
		this.column = column;
	}
	
	/*
	 * returns the position in the table. This IS NOT the
	 * slot's day/ord value
	 */
	public int getRow() {return row;}
	public int getColumn() {return column;}
	
}
