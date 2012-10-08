package com.zybnet.abc.view;

import com.zybnet.abc.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ExamsListView extends ListView {
	
	private Paint mPaint = new Paint();
	private Paint mBackgroundPaint = new Paint();
	
	private static final String[] EXAMS = {
		"Lorem", "Ipsum", "Dolor", "Sit Amen", "Bubba", "Trallallero",
		"Once upon a Time", "Ruby on Rails", "Jack lo Squartatore", "Zerocalcare",
		"Oh Fiorentina!", "Ma le gambe, ma le gambe, a noi piacciono di più",
		"Raffaella Fico", "Non so più cosa scrivere"};
	
	public ExamsListView(Context ctx) {
		super(ctx);
		
		setId(android.R.id.list);
		setDivider(null);
		
		setAdapter(new ArrayAdapter<String>(getContext(), R.layout.exam, R.id.name, EXAMS));
		setCacheColorHint(0);
		
		mPaint.setColor(Color.RED);
		mBackgroundPaint.setColor(Color.WHITE);
	}
	
	public ExamsListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

}
