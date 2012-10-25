package com.zybnet.abc.controller;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;

import com.zybnet.abc.model.Subject;
import com.zybnet.abc.utils.DatabaseHelper;
import com.zybnet.abc.utils.IndexController;
import com.zybnet.abc.utils.TitleDescriptionAdapter;

public class SubjectController extends IndexController<Subject> {

	public SubjectController(Context ctx, DatabaseHelper dh) {
		super(ctx, dh);
	}

	@Override
	protected CursorAdapter getAdapter(Cursor cursor) {
		return new TitleDescriptionAdapter(ctx, cursor, "name_short", "name");
	}

	@Override
	protected Cursor getCursor() {
		return dh.getSubjects();
	}
	
}
