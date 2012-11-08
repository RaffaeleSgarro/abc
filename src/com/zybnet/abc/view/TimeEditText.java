package com.zybnet.abc.view;

import java.sql.Time;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import android.widget.TimePicker;

import com.zybnet.abc.R;

public class TimeEditText extends TextView {

	public TimeEditText(Context context) {
		super(context);
	}
	
	public TimeEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public TimeEditText(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
	}
	
	private OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TimeEditView view = new TimeEditView(getContext());
			view.setup();
			
			NavigateBackView.Item item = new NavigateBackView.Item(getContext());
			
			item.opener = TimeEditText.this;
			item.view = view;
			item.keep = false;
			
			flipper.showView(item);
		}
	};
	
	public void setup(HistoryViewFlipper flipper) {
		this.flipper = flipper;
		setFocusable(true);
		setFocusableInTouchMode(true);
		setOnClickListener(click);
	}
	
	public Time getTime() {
		return time;
	}
	
	public void setTime(Time time) {
		this.time = time;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		setText(sdf.format(time));
	}
	
	private HistoryViewFlipper flipper;
	private Time time;
	
	private class TimeEditView extends EditView {

		public void setup() {
			TimePicker picker = (TimePicker) findViewById(R.id.time);
			picker.setCurrentHour(time.getHours());
			picker.setCurrentMinute(time.getMinutes());
		}
		
		public TimeEditView(Context ctx) {
			super(ctx, R.layout.time_picker, delegate);
		}
		
	};
	
	private EditView.Delegate delegate = new EditView.Delegate() {

		private TimePicker picker;
		
		@Override
		public void save(EditView view) {
			time.setHours(picker.getCurrentHour());
			time.setMinutes(picker.getCurrentMinute());
			setTime(time);
			flipper.back();
		}
		
		@Override
		public void afterInflate(EditView view) {
			picker = (TimePicker) view.findViewById(R.id.time);
			picker.setIs24HourView(true);
			view.findViewById(R.id.delete).setVisibility(View.GONE);
			
			View save = view.findViewById(R.id.save);
			MarginLayoutParams params = (MarginLayoutParams) save.getLayoutParams();
			params.rightMargin = 0;
			save.setLayoutParams(params);
		}
	};

}
