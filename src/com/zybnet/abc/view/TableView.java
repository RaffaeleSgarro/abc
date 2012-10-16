package com.zybnet.abc.view;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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

import com.zybnet.abc.activity.AbbecedarioActivity;
import com.zybnet.abc.model.Model;
import com.zybnet.abc.model.Slot;
import com.zybnet.abc.utils.DatabaseHelper;
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
@SuppressLint("ViewConstructor")
public class TableView extends RelativeLayout implements SharedPreferences.OnSharedPreferenceChangeListener {
	
	public final static int ID = 0x923032f;
	
	public static int[][] BG_COLORS = new int[2][2];
	
	public static final int DARK_GREY;
	
	private ProxyListener slotListener = new ProxyListener();
	
	private boolean decorateDays;
	private boolean decorateOrds;
	
	public static final int DAYS_DECORATION_ID = 29843;
	public static final int ORDS_DECORATION_ID = 92373;
	public static final int TABLE_ID = 25912;
	
	public static final int ROWS = U.SLOTS_PER_DAY_MAX;
	public static final int COLUMNS = 7;
	
	static {
		BG_COLORS[0][0] = Color.parseColor("#e5e5e5"); // grey
		BG_COLORS[0][1] = Color.rgb(255, 255, 255); // white
		BG_COLORS[1][0] = Color.parseColor("#e5cbe5"); // purple
		BG_COLORS[1][1] = Color.parseColor("#ffe5ff"); // pink
		
		DARK_GREY = Color.parseColor("#555555"); // for text
	}
	
	public TableView(AbbecedarioActivity abc) {
		super(abc);
		this.abc = abc;
		setVisibility(View.INVISIBLE);
		setId(ID);
	}
	
	private AbbecedarioActivity abc;
	
	private class AsyncLoad extends AsyncTask<DatabaseHelper, Void, Cursor> {
		
		@Override
		public void onPostExecute(Cursor cursor) {
			setup(cursor);
		}

		@Override
		protected Cursor doInBackground(DatabaseHelper... params) {
			DatabaseHelper helper = params[0];
			Cursor c = helper.getReadableDatabase().query("slot",
					new String[] {"day", "ord", "display_text"},
					null, null, null, null, null);
			return c;
		}
	};
	
	private SharedPreferences prefs() {
		return PreferenceManager.getDefaultSharedPreferences(getContext());
	}
	
	/*
	 * Fill the TimeTable. The cursor must contain day, ord and
	 * subject_name_short columns.
	 * Builds the UI
	 */
	private void setup(Cursor cursor) {
		SharedPreferences prefs = prefs();
		
		decorateDays = prefs.getBoolean(U.P_DECORATE_DAYS, true);
		decorateOrds = prefs.getBoolean(U.P_DECORATE_ORDS, true);
		
		addMainChildren();
		
		setVisibility(View.VISIBLE);
		
		int day_index = cursor.getColumnIndex("day");
		int ord_index = cursor.getColumnIndex("ord");
		int text = cursor.getColumnIndex("display_text");
		
		String[][] slots = new String[ROWS][COLUMNS];
		
		while (cursor.moveToNext()) {
			// Remember 1-based
			int day = cursor.getInt(day_index);
			int ord = cursor.getInt(ord_index);
			
			slots[ord - 1][day - 1] = cursor.getString(text);
		}
		
		addSlots(slots);
		
		for (int i = 0; i < 7; i++) {
			boolean v = prefs().getBoolean(U.P_DAY_PREFIX + (i + 1), true);
			setColumnVisibility(i, v ? View.VISIBLE : View.GONE);
		}
		
		recomputeCellsBackground();
		
		startAnimation();
	}
	
	private Model.Subscriber subscriber = new Model.Subscriber() {
		@Override
		public void onMessage(Model model) {
			final Slot slot = (Slot) model;
			post(new Runnable(){
				public void run() {
					getChildAt(slot.ord - 1, slot.day - 1).setText(slot.display_text);
				}
			});
		}
	};
	
	@Override
	public void onAttachedToWindow() {
		new AsyncLoad().execute(abc.db());
		prefs().registerOnSharedPreferenceChangeListener(this);
		Model.Channel.subscribe(Slot.class, subscriber);
	}
	
	@Override
	public void onDetachedFromWindow() {
		prefs().unregisterOnSharedPreferenceChangeListener(this);
		Model.Channel.unsucribe(Slot.class, subscriber);
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
		return (SlotView)(((ViewGroup) table().getChildAt(row)).getChildAt(column));
	}
	
	public SlotView getChildAt(int[] coords) {
		return getChildAt(coords[0], coords[1]);
	}
	
	// Adds layouts for decorations and main view
	private void addMainChildren() {
		RelativeLayout.LayoutParams p;
		LinearLayout.LayoutParams p2;
		
		// h is horizontal decoration, for days
		LinearLayout h = ll(DAYS_DECORATION_ID, LinearLayout.HORIZONTAL);
		h.setVisibility(decorateDays ? View.VISIBLE : View.GONE);
		for (int i = 0; i < COLUMNS; i++) {
			p2 = new LinearLayout.LayoutParams(FILL_PARENT, WRAP_CONTENT, 1f/COLUMNS);
			h.addView(createHeader(String.format("%ta",
					U.getLocalizedDayOfTheWeek(i + 1)).toUpperCase()), p2);
		}
		p = new RelativeLayout.LayoutParams(FILL_PARENT, WRAP_CONTENT);
		p.addRule(ALIGN_PARENT_TOP);
		p.addRule(ALIGN_PARENT_LEFT);
		p.addRule(ALIGN_PARENT_RIGHT);
		addView(h, p);
		
		// v is the ords decoration, the vertical one
		LinearLayout v = ll(ORDS_DECORATION_ID, LinearLayout.VERTICAL);
		for (int i = 0; i < ROWS; i++) {
			p2 = new LinearLayout.LayoutParams(WRAP_CONTENT, FILL_PARENT, 1f/ROWS);
			View hv = createHeader(Integer.toString(i + 1));
			hv.setPadding(3, 0, 3, 0);
			boolean visible = i < prefs().getInt(U.P_SLOTS_PER_DAY, U.SLOTS_PER_DAY_DEFAULT);
			hv.setVisibility(visible ? View.VISIBLE : View.GONE);
			v.addView(hv, p2);
		}
		
		p = new RelativeLayout.LayoutParams(WRAP_CONTENT, FILL_PARENT);
		p.addRule(ALIGN_BOTTOM);
		p.addRule(ALIGN_LEFT);
		p.addRule(BELOW, DAYS_DECORATION_ID);
		v.setVisibility(decorateOrds ? View.VISIBLE : View.GONE);
		addView(v, p);
		
		v.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		U.setPaddingLeft(h, decorateOrds ? v.getMeasuredWidth() : 0);
		
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
				FILL_PARENT, FILL_PARENT, 1f / ROWS);
		LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(
				FILL_PARENT, FILL_PARENT, 1f / COLUMNS);
		
		int slotsPerDay = prefs().getInt(U.P_SLOTS_PER_DAY, U.SLOTS_PER_DAY_DEFAULT);
		
		for (int i = 0; i < ROWS; i++) {
			LinearLayout row = new LinearLayout(getContext());
			String[] slots_row = slots[i];
			row.setOrientation(LinearLayout.HORIZONTAL); // default, but anyway
			row.setVisibility(i < slotsPerDay ? View.VISIBLE:View.GONE);
			for (int j = 0; j < COLUMNS; j++) {
				SlotView cell = new SlotView(getContext(), slots_row[j]);
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
	
	/*
	 * return the header and the cells
	 */
	private View[] getAllChildrenForColumn(int column) {
		View[] views = new View[ROWS + 1];
		views[0] = days().getChildAt(column);
		for (int i = 0; i < ROWS; i++) {
			views[i + 1] = ((ViewGroup) table().getChildAt(i)).getChildAt(column);
		}
		return views;
	}
	
	private void setColumnVisibility(int column, int visibility) {
		for (View view : getAllChildrenForColumn(column)) {
			view.setVisibility(visibility);
		}
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
		if (key.equals(U.P_DECORATE_DAYS)) {
			boolean value = prefs.getBoolean(key, true);
			days().setVisibility(value ? View.VISIBLE : View.GONE);
		} else if (key.equals(U.P_DECORATE_ORDS)) {
			boolean value = prefs.getBoolean(key, true);
			ords().setVisibility(value ? View.VISIBLE : View.GONE);
			U.setPaddingLeft(days(), value ? ords().getWidth() : 0);
		} else if (key.startsWith(U.P_DAY_PREFIX)) {
			int day = Integer.parseInt(key.substring(key.length() - 1));
			boolean visible = prefs().getBoolean(U.P_DAY_PREFIX + day, true);
			setColumnVisibility(day - 1, visible ? View.VISIBLE : View.GONE);
			recomputeCellsBackground();
		} else if (key.equals(U.P_SLOTS_PER_DAY)) {
			int slots = prefs.getInt(U.P_SLOTS_PER_DAY, U.SLOTS_PER_DAY_DEFAULT);
			for (int i = 0; i < ROWS; i++) {
				int visibility = i < slots ? View.VISIBLE : View.GONE;
				ords().getChildAt(i).setVisibility(visibility);
				table().getChildAt(i).setVisibility(visibility);
			}
		}
		
	}
	
	private void recomputeCellsBackground() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0, count = 0; j < COLUMNS; j++) {
				View view = getChildAt(i, j);
				if (view.getVisibility() != View.VISIBLE)
					continue;
				view.setBackgroundColor(BG_COLORS[i % 2][count % 2]);
				count++;
			}
		}
	}
	
}
