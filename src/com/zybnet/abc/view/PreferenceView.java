package com.zybnet.abc.view;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zybnet.abc.R;
import com.zybnet.abc.utils.U;

// This class is not intended to be instantiated in XML
@SuppressLint("ViewConstructor")
public class PreferenceView extends ScrollView {

	public PreferenceView(Context context) {
		super(context);
		setBackgroundColor(Color.BLACK);
		View.inflate(context, R.layout.preferences, this);
		setupListeners();
	}
	
	private void setupDays() {
		Calendar c = Calendar.getInstance();
		TextView[] views = new TextView[7];
		views[0] = (TextView) findViewById(R.id.day_1);
		views[1] = (TextView) findViewById(R.id.day_2);
		views[2] = (TextView) findViewById(R.id.day_3);
		views[3] = (TextView) findViewById(R.id.day_4);
		views[4] = (TextView) findViewById(R.id.day_5);
		views[5] = (TextView) findViewById(R.id.day_6);
		views[6] = (TextView) findViewById(R.id.day_7);
		
		for (int i = 0;  i < views.length; i++ ) {
			c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + i);
			TextView tv = views[i];
			tv.setText(U.uppercaseFirstChar(String.format("%tA", c)));
			//tv.setPadding(tv.getPaddingLeft() + 10, 10, 10, 10);
		}
	}
	
	private void setupListeners() {
		setupDays();
	}

}
