package com.zybnet.abc.view;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

public class TimeTable extends LinearLayout {
	
	public final static int ID = 0x923032f;
	
	private int rows = -1, columns = -1;
	private static int[][] bgColors = new int[2][2];
	private View.OnClickListener slotListener;
	
	static {
		bgColors[0][0] = Color.parseColor("#e5e5e5"); // grey
		bgColors[0][1] = Color.rgb(255, 255, 255); // white
		bgColors[1][0] = Color.parseColor("#e5cbe5"); // purple
		bgColors[1][1] = Color.parseColor("#ffe5ff"); // pink
	}
	
	public TimeTable(Context context) {
		super(context);
		setVisibility(View.INVISIBLE);
		setId(ID);
	}
	
	public void setCursor(Cursor cursor) {
		setVisibility(View.VISIBLE);
		fillSlots();
		while (cursor.moveToNext()) {
			int row = cursor.getInt(1);
			int col = cursor.getInt(2);
			
			String text = cursor.getString(3);
			getSlot(row, col).setText(text);
		}
		startAnimation();
	}
	
	private void startAnimation() {
		long startTimeMillis = AnimationUtils.currentAnimationTimeMillis();
		for (int i = 0; i < getChildCount(); i++) {
			ViewGroup row = (ViewGroup) getChildAt(i);
			int rowCount = row.getChildCount();
			for (int j = 0; j < rowCount; j++) {
				View cell = row.getChildAt(j);
				AnimationSet animation = new AnimationSet(true);
				animation.addAnimation(new AlphaAnimation(0, 1f));
				animation.addAnimation(new ScaleAnimation(0.5f, 1, 1, 1));
				animation.setDuration(1000);
				animation.setStartTime(startTimeMillis + (rowCount * i + j) * 40);
				cell.setAnimation(animation);
			}
		}
		
	}
	
	public SlotView getSlot(int row, int column) {
		return (SlotView)(((ViewGroup) getChildAt(row)).getChildAt(column));
	}
	
	public void setSlotListener(View.OnClickListener listener) {
		slotListener = listener;
		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < cols(); j++) {
				getSlot(i, j).setOnClickListener(listener);
			}
		}
	}
	
	public int rows() {
		return rows;
	}
	
	public int cols() {
		return columns;
	}
	
	private void fillSlots() {
		this.rows = 7;
		this.columns = 6;
		
		setOrientation(LinearLayout.VERTICAL);

		LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
				FILL_PARENT, FILL_PARENT, 1f / rows());
		LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(
				FILL_PARENT, FILL_PARENT, 1f / cols());
		
		final int grey = Color.parseColor("#555555"); // for text

		for (int i = 0; i < rows(); i++) {
			LinearLayout row = new LinearLayout(getContext());
			row.setOrientation(LinearLayout.HORIZONTAL); // default, but anyway
			for (int j = 0; j < cols(); j++) {
				SlotView cell = new SlotView(getContext(), "", bgColors[i % 2][j % 2]);
				cell.setTextColor(grey);
				cell.setGravity(Gravity.CENTER);
				cell.insertAt(row, cellParams, i, j);
				cell.setOnClickListener(slotListener);
			}
			addView(row, rowParams);
		}
	}
	
	public View.OnClickListener getSlotListener() {
		return slotListener;
	}
}
