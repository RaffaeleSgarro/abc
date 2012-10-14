package com.zybnet.abc.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.zybnet.abc.R;
import com.zybnet.abc.utils.U;

// This class is not intended to be instantiated in XML
@SuppressLint("ViewConstructor")
public class PreferenceView extends ScrollView {

	public PreferenceView(Context context) {
		super(context);
		setBackgroundColor(Color.BLACK);
		View.inflate(context, R.layout.preferences, this);
		setupCheckboxes();
		setupRadios();
	}
	
	private OnCheckedChangeListener boolListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			BoolPref pref = (BoolPref) buttonView.getTag();
			pref.set(isChecked);
		}
	};
	
	private SharedPreferences prefs() {
		return PreferenceManager.getDefaultSharedPreferences(getContext());
	}
	
	private void setupCheckboxes() {
		setupCheckboxPreference(R.id.decor_days, U.P_DECORATE_DAYS);
		setupCheckboxPreference(R.id.decor_ords, U.P_DECORATE_ORDS);
		
		for (int i = 1;  i <= 7; i++ ) {
			int id = getResources().getIdentifier("day_" + i, "id", getContext().getPackageName());
			setupCheckboxPreference(id, U.P_DAY_PREFIX + i);
			((CheckBox) findViewById(id)).setText(
					U.uppercaseFirstChar(String.format("%tA", U.getLocalizedDayOfTheWeek(i))));
		}
	}
	
	private class BoolPref {
		BoolPref(String name) {
			this.name = name;
		}
		
		String name;
		
		boolean get() {
			return prefs().getBoolean(name, true);
		}
		
		void set(boolean value) {
			prefs().edit().putBoolean(name, value).commit();
		}
	};
	
	private void setupCheckboxPreference(int id, String prefKey) {
		CheckBox v = (CheckBox) findViewById(id);
		BoolPref p = new BoolPref(prefKey);
		v.setChecked(p.get());
		v.setTag(p);
		v.setOnCheckedChangeListener(boolListener);
	}
	
	private void setupRadios() {
		RadioGroup radios = (RadioGroup) findViewById(R.id.slots_per_day);
		int current = prefs().getInt(U.P_SLOTS_PER_DAY, U.SLOTS_PER_DAY_DEFAULT);
		for (int n = U.SLOTS_PER_DAY_MIN; n <= U.SLOTS_PER_DAY_MAX; n++) {
			RadioButton radio = new RadioButton(getContext());
			radio.setText(Integer.toString(n));
			radio.setTag(Integer.valueOf(n));
			U.setPaddingLeft(radio, (int)(50 * getResources().getDisplayMetrics().density));
			radio.setOnClickListener(radioListener);
			radios.addView(radio);
			if (n == current)
				radio.setChecked(true);
		}
	}
	
	private OnClickListener radioListener = new OnClickListener() {
		
		@Override
		public void onClick(View radio) {
			boolean checked = ((RadioButton) radio).isChecked();
			if (checked) {
				prefs().edit().putInt(U.P_SLOTS_PER_DAY, (Integer) radio.getTag()).commit();
			}
		}
	};

}
