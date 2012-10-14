package com.zybnet.abc.utils;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.zybnet.abc.R;

public class TitleDescriptionAdapter extends CursorAdapter {

	protected int titleIndex;
	protected int descriptionIndex;
	
	public TitleDescriptionAdapter(Context context, Cursor c, String titleColumn, String descriptionColumn) {
		super(context, c, false);
		titleIndex = c.getColumnIndex(titleColumn);
		descriptionIndex = c.getColumnIndex(descriptionColumn);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(R.layout.title_description, parent, false);
	}

	@Override
	public final void bindView(View view, Context context, Cursor cursor) {
		((TextView) view.findViewById(R.id.title))
			.setText(getTitle(cursor));
		((TextView) view.findViewById(R.id.description))
			.setText(getDescription(cursor));
	}
	
	protected String getTitle(Cursor cursor) {
		return cursor.getString(titleIndex);
	}
	
	protected String getDescription(Cursor cursor) {
		return cursor.getString(descriptionIndex);
	}

}
