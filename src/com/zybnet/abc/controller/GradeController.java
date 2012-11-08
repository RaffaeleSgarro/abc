package com.zybnet.abc.controller;

import java.text.DateFormat;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zybnet.abc.R;
import com.zybnet.abc.model.Grade;
import com.zybnet.abc.utils.DatabaseHelper;
import com.zybnet.abc.utils.IndexController;

public class GradeController extends IndexController<Grade> {

	Long subject;
	
	public GradeController(Context ctx, DatabaseHelper dh, Long subject) {
		super(ctx, dh);
		this.subject = subject;
	}

	@Override
	protected CursorAdapter getAdapter(Cursor cursor) {
		return new GradeAdapter(ctx, cursor);
	}

	@Override
	protected Cursor getCursor() {
		return dh.getGrades(subject);
	}
	
	@Override
	public void fixBelongsTo(Grade grade) {
		grade.subject_id = subject;
	}
	
	private static class GradeAdapter extends CursorAdapter {

		public GradeAdapter(Context ctx, Cursor c) {
			super(ctx, c, false);
		}

		@Override
		public void bindView(View convert, Context ctx, Cursor cursor) {
			setText(convert, R.id.date, DateFormat.getDateInstance().format(DatabaseHelper._date(cursor, "date")));
			
			String description = cursor.getString(cursor.getColumnIndex("description"));
			if (description == null)
				description = ctx.getString(R.string.edit);
			setText(convert, R.id.description, description);
			
			setText(convert, R.id.score, cursor.getString(cursor.getColumnIndex("score")));
			
		}

		private void setText(View parent, int id, String text) {
			((TextView) parent.findViewById(id)).setText(text);
		}
		
		@Override
		public View newView(Context ctx, Cursor cursor, ViewGroup root) {
			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.grade_item, root, false);
			return view;
		}
		
	}

}
