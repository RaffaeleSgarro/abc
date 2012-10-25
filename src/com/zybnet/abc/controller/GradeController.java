package com.zybnet.abc.controller;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;

import com.zybnet.abc.model.Grade;
import com.zybnet.abc.utils.DatabaseHelper;
import com.zybnet.abc.utils.IndexController;
import com.zybnet.abc.utils.TitleDescriptionAdapter;

public class GradeController extends IndexController<Grade> {

	Long subject;
	
	public GradeController(Context ctx, DatabaseHelper dh, Long subject) {
		super(ctx, dh);
		this.subject = subject;
	}

	@Override
	protected CursorAdapter getAdapter(Cursor cursor) {
		return new TitleDescriptionAdapter(ctx, cursor, "date", "description");
	}

	@Override
	protected Cursor getCursor() {
		return dh.getGrades(subject);
	}
	
	@Override
	public void fixBelongsTo(Grade grade) {
		grade.subject_id = subject;
	}

}
