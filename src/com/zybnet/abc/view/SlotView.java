package com.zybnet.abc.view;

import java.util.Calendar;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zybnet.abc.model.Slot;

public class SlotView extends TextView {

	private int row = -1, column = -1;
	
	public SlotView(Context ctx, CharSequence text, int background) {
		this(ctx);
		setText(text);
		setBackgroundColor(background);
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
	
	public int getRow() {return row;}
	public int getColumn() {return column;}

	public String toString() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, getColumn() + 1);
		return String.format("%tA, hour %d", c, getRow() + 1);
	}
	
	public Slot getSlot() {
		// TODO
		return new Slot();
	}
}
