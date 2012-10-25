package com.zybnet.abc.controller;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;

import com.zybnet.abc.model.Homework;
import com.zybnet.abc.utils.DatabaseHelper;
import com.zybnet.abc.utils.IndexController;
import com.zybnet.abc.utils.TitleDescriptionAdapter;

public class HomeworkController extends IndexController<Homework> {

	private Long subject;
	
	public HomeworkController(Context ctx, DatabaseHelper dh, Long subject) {
		super(ctx, dh);
		this.subject = subject;
	}

	@Override
	protected CursorAdapter getAdapter(Cursor cursor) {
		return new TitleDescriptionAdapter(ctx, cursor, "due", "description");
	}

	@Override
	protected Cursor getCursor() {
		return dh.getHomework(subject);
	}
	
	@Override
	public void fixBelongsTo(Homework homework) {
		homework.subject_id = subject;
	}

}
