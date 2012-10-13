package com.zybnet.abc.view;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zybnet.abc.utils.L;
import com.zybnet.abc.utils.U;

/*
 * This class is a RelativeLayout with three children
 *  
 *   1. days decoration
 *   2. ords decoration
 *   3. table
 *   
 * The table is a LinearLayout in vertical orientation
 * 
 */
public class TableView extends RelativeLayout implements SharedPreferences.OnSharedPreferenceChangeListener {
	
	public final static int ID = 0x923032f;
	
	public static int[][] BG_COLORS = new int[2][2];
	
	public static final int DARK_GREY;
	
	private ProxyListener slotListener = new ProxyListener();
	
	private SparseIntArray columnsToDays;
	private int displayedSlotsPerDay;
	
	private boolean decorateDays;
	private boolean decorateOrds;
	
	public static final int DAYS_DECORATION_ID = 29843;
	public static final int ORDS_DECORATION_ID = 92373;
	public static final int TABLE_ID = 25912;
	
	static {
		BG_COLORS[0][0] = Color.parseColor("#e5e5e5"); // grey
		BG_COLORS[0][1] = Color.rgb(255, 255, 255); // white
		BG_COLORS[1][0] = Color.parseColor("#e5cbe5"); // purple
		BG_COLORS[1][1] = Color.parseColor("#ffe5ff"); // pink
		
		DARK_GREY = Color.parseColor("#555555"); // for text
	}
	
	public TableView(Context context) {
		super(context);
		setVisibility(View.INVISIBLE);
		setId(ID);
	}
	
	private SharedPreferences prefs() {
		return PreferenceManager.getDefaultSharedPreferences(getContext());
	}
	
	// Builds the UI
	private void setup(Cursor cursor) {
		SharedPreferences prefs = prefs();
		columnsToDays = new SparseIntArray();
		for (int i = 1, count = 0; i <= 7; i++) {
			if (prefs.getBoolean(U.P_DAY_PREFIX + i, true)) {
				columnsToDays.put(count, i);
				count++;
			}
		}
		displayedSlotsPerDay = prefs.getInt(U.P_SLOTS_PER_DAY, 6);
		decorateDays = prefs.getBoolean(U.P_DECORATE_DAYS, true);
		decorateOrds = prefs.getBoolean(U.P_DECORATE_ORDS, true);
		
		addMainChildren();
		
		setVisibility(View.VISIBLE);
		
		int day_index = cursor.getColumnIndex("day");
		int ord_index = cursor.getColumnIndex("ord");
		int subject = cursor.getColumnIndex("subject_name_short");
		
		String[][] slots = new String[rows()][cols()];
		
		while (cursor.moveToNext()) {
			// Remember 1-based
			int day = cursor.getInt(day_index);
			int ord = cursor.getInt(ord_index);
			
			if (isDisplayable(day, ord)) {
				slots[ord - 1][dayToColumn(day)] = cursor.getString(subject);
			}
		}
		
		addSlots(slots);
		
		startAnimation();
	}
	
	private Cursor pendingCursor;
	
	@Override
	public void onAttachedToWindow() {
		if (pendingCursor != null) {
			setup(pendingCursor);
			pendingCursor = null;
		}
		prefs().registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onDetachedFromWindow() {
		prefs().unregisterOnSharedPreferenceChangeListener(this);
	}
	
	/*
	 * Fill the TimeTable. The cursor must contain day, ord and
	 * subject_name_short columns.
	 * 
	 * When this method is invoked, the table may not have been
	 * attached to any window, so the save the cursor and
	 * add children asynchronously
	 * 
	 */
	public void setCursor(Cursor cursor) {
		if (getWindowToken() == null) {
			pendingCursor = cursor;
		} else {
			setup(cursor);
		}
	}
	
	private void startAnimation() {
		long startTimeMillis = AnimationUtils.currentAnimationTimeMillis();
		ViewGroup table = (ViewGroup) findViewById(TABLE_ID);
		for (int i = 0; i < table.getChildCount(); i++) {
			ViewGroup row = (ViewGroup) table.getChildAt(i);
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
	
	// Adds layouts for decorations and main view
	private void addMainChildren() {
		RelativeLayout.LayoutParams p;
		LinearLayout.LayoutParams p2;
		
		// h is horizontal decoration, for days
		LinearLayout h = ll(DAYS_DECORATION_ID, LinearLayout.HORIZONTAL);
		h.setVisibility(decorateDays ? View.VISIBLE : View.GONE);
		for (int i = 0; i < cols(); i++) {
			p2 = new LinearLayout.LayoutParams(FILL_PARENT, WRAP_CONTENT, 1f/cols());
			h.addView(createHeader(String.format("%ta",
					U.getLocalizedDayOfTheWeek(columnToDay(i))).toUpperCase()), p2);
		}
		p = new RelativeLayout.LayoutParams(FILL_PARENT, WRAP_CONTENT);
		p.addRule(ALIGN_PARENT_TOP);
		p.addRule(ALIGN_PARENT_LEFT);
		p.addRule(ALIGN_PARENT_RIGHT);
		addView(h, p);
		
		// v is the ords decoration, the vertical one
		LinearLayout v = ll(ORDS_DECORATION_ID, LinearLayout.VERTICAL);
		for (int i = 0; i < rows(); i++) {
			p2 = new LinearLayout.LayoutParams(WRAP_CONTENT, FILL_PARENT, 1f/rows());
			View hv = createHeader(Integer.toString(i + 1));
			hv.setPadding(3, 0, 3, 0);
			v.addView(hv, p2);
		}
		
		p = new RelativeLayout.LayoutParams(WRAP_CONTENT, FILL_PARENT);
		p.addRule(ALIGN_BOTTOM);
		p.addRule(ALIGN_LEFT);
		p.addRule(BELOW, DAYS_DECORATION_ID);
		v.setVisibility(decorateOrds ? View.VISIBLE : View.GONE);
		addView(v, p);
		
		U.setPaddingLeft(h, v.getWidth());
		
		LinearLayout t = ll(TABLE_ID, LinearLayout.VERTICAL);
		p = new RelativeLayout.LayoutParams(FILL_PARENT, FILL_PARENT);
		p.addRule(BELOW, DAYS_DECORATION_ID);
		p.addRule(RIGHT_OF, ORDS_DECORATION_ID);
		addView(t, p);
	}
	
	private TextView createHeader(String text) {
		TextView tv = new TextView(getContext());
		tv.setText(text);
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(Color.WHITE);
		return tv;
	}
	
	private LinearLayout ll(int id, int orientation) {
		LinearLayout ll = new LinearLayout(getContext());
		ll.setId(id);
		ll.setOrientation(orientation);
		return ll;
	}
	
	private void addSlots(String[][] slots) {
		LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
				FILL_PARENT, FILL_PARENT, 1f / rows());
		LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(
				FILL_PARENT, FILL_PARENT, 1f / cols());
		
		for (int i = 0; i < slots.length; i++) {
			LinearLayout row = new LinearLayout(getContext());
			String[] slots_row = slots[i];
			row.setOrientation(LinearLayout.HORIZONTAL); // default, but anyway
			for (int j = 0; j < slots_row.length; j++) {
				SlotView cell = new SlotView(getContext(), slots_row[j], BG_COLORS[i % 2][j % 2]);
				cell.setTextColor(DARK_GREY);
				cell.setGravity(Gravity.CENTER);
				cell.insertAt(row, cellParams, i, j);
				cell.setOnClickListener(slotListener);
			}
			((ViewGroup) findViewById(TABLE_ID)).addView(row, rowParams);
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

	private ViewGroup group(int id) {
		return (ViewGroup) findViewById(id);
	}
	
	private ViewGroup table() {
		return group(TABLE_ID);
	}
	
	private ViewGroup days() {
		return group(DAYS_DECORATION_ID);
	}
	
	private ViewGroup ords() {
		return group(ORDS_DECORATION_ID);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		L.og("preferences changed");
		if (key.equals(U.P_DECORATE_DAYS)) {
			boolean value = prefs.getBoolean(key, true);
			days().setVisibility(value ? View.VISIBLE : View.GONE);
		} else if (key.equals(U.P_DECORATE_ORDS)) {
			boolean value = prefs.getBoolean(key, true);
			ords().setVisibility(value ? View.VISIBLE : View.GONE);
			U.setPaddingLeft(days(), value ? ords().getWidth() : 0);
		}
		
	}
	
}
