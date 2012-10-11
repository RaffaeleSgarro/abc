package com.zybnet.abc.view;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

public class TableView extends LinearLayout {
	
	public final static int ID = 0x923032f;
	
	private static int[][] bgColors = new int[2][2];
	private ProxyListener slotListener = new ProxyListener();
	
	private SparseIntArray columnsToDays = new SparseIntArray();
	private int displayedSlotsPerDay;
	
	static {
		bgColors[0][0] = Color.parseColor("#e5e5e5"); // grey
		bgColors[0][1] = Color.rgb(255, 255, 255); // white
		bgColors[1][0] = Color.parseColor("#e5cbe5"); // purple
		bgColors[1][1] = Color.parseColor("#ffe5ff"); // pink
	}
	
	public TableView(Context context) {
		super(context);
		setVisibility(View.INVISIBLE);
		setId(ID);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		for (int i = 1, count = 0; i <= 7; i++) {
			if (prefs.getBoolean("day_" + i, true)) {
				columnsToDays.put(count, i);
				count++;
			}
		}
		displayedSlotsPerDay = prefs.getInt("slots_per_day", 6);
	}
	
	/*
	 * Fill the TimeTable. The cursor must contain day, ord and
	 * subject_name_short columns
	 * 
	 */
	public void setCursor(Cursor cursor) {
		setVisibility(View.VISIBLE);
		
		int day_index = cursor.getColumnIndex("day");
		int ord_index = cursor.getColumnIndex("ord");
		int subject = cursor.getColumnIndex("subject_name_short");
		
		String[][] slots = new String[displayedSlotsPerDay][columnsToDays.size()];
		
		while (cursor.moveToNext()) {
			// Remember 1-based
			int day = cursor.getInt(day_index);
			int ord = cursor.getInt(ord_index);
			
			if (isDisplayable(day, ord)) {
				slots[ord - 1][dayToColumn(day)] = cursor.getString(subject);
			}
		}
		
		fillSlots(slots);
		
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
	
	/*
	 * Returns the SlotView at position (row, column)
	 * Note that each SlotView has an associated Slot which declares two
	 * related but different fields: day and ord. The actual position
	 * of the SlotView inside the table depends on day, ord and user
	 * preferences.
	 * 
	 * @param row 0-based index
	 * @param column 0-based index
	 */
	public SlotView getChildAt(int row, int column) {
		return (SlotView)(((ViewGroup) getChildAt(row)).getChildAt(column));
	}
	
	public SlotView getChildAt(int[] coords) {
		return getChildAt(coords[0], coords[1]);
	}
	
	/*
	 * Returns the SlotView, IF ANY, for displaying the ord-th slot in day.
	 * NOTE: This method returns NULL if in the current user preferences
	 * there is no room allocated for this slot, ie if the day column is
	 * shifted out or ord is greater than the maximum per day.
	 * 
	 * @param day 1-based, locale specific
	 * @param ord 1-based
	 */
	public SlotView getChildForSlot(int day, int ord) {
		if (!isDisplayable(day, ord))
			return null;
		
		return getChildAt(dayToColumn(day), ord - 1);
	}
	
	/*
	 * @param day 1-based locale specific day
	 * @param ord 1-based order of slot in the day
	 */
	private boolean isDisplayable(int day, int ord) {
		return dayToColumn(day) >= 0 && ord > 0 && ord <= rows();
	}
	
	/*
	 * Returns the 0-based index of the columns for
	 * the 1-based day.
	 * 
	 * The return is a negative number if the day
	 * is not enabled in the preferences
	 * 
	 */
	public int dayToColumn(int day) {
		return columnsToDays.indexOfValue(day);
	}
	
	/*
	 * Take a 0-based column index and returns the
	 * 1-based day if it's show, 0 otherwise
	 */
	public int columnToDay(int column) {
		return columnsToDays.get(column);
	}
	
	public int rows() {
		return displayedSlotsPerDay;
	}
	
	public int cols() {
		return columnsToDays.size();
	}
	
	private void fillSlots(String[][] slots) {
		setOrientation(LinearLayout.VERTICAL);

		LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
				FILL_PARENT, FILL_PARENT, 1f / rows());
		LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(
				FILL_PARENT, FILL_PARENT, 1f / cols());
		
		final int grey = Color.parseColor("#555555"); // for text

		for (int i = 0; i < slots.length; i++) {
			LinearLayout row = new LinearLayout(getContext());
			String[] slots_row = slots[i];
			row.setOrientation(LinearLayout.HORIZONTAL); // default, but anyway
			for (int j = 0; j < slots_row.length; j++) {
				SlotView cell = new SlotView(getContext(), slots_row[j], bgColors[i % 2][j % 2]);
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
	
	public void setSlotListener(View.OnClickListener listener) {
		slotListener.listener = listener;
	}
	
	private static class ProxyListener implements View.OnClickListener {
		View.OnClickListener listener;
		
		@Override
		public void onClick(View view) {
			if (listener != null)
				listener.onClick(view);
		}
	}
	
}
