package com.zybnet.abc.controller;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;

import com.zybnet.abc.R;
import com.zybnet.abc.model.Teacher;
import com.zybnet.abc.utils.DatabaseHelper;
import com.zybnet.abc.utils.IndexController;

public class TeacherController extends IndexController<Teacher> {

	public TeacherController(Context ctx, DatabaseHelper dh) {
		super(ctx, dh);
	}

	@Override
	protected CursorAdapter getAdapter(Cursor cursor) {
		return new SimpleCursorAdapter(ctx, R.layout.teacher_item, cursor,
				new String[] {"name", "email", "phone", "notes"},
				new int[] { R.id.text, R.id.email, R.id.phone, R.id.notes}, 0);
	}

	@Override
	protected Cursor getCursor() {
		return dh.getTeachers();
	}

}
