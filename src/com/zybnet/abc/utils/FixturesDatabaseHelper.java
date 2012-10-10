package com.zybnet.abc.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.zybnet.abc.R;

public class FixturesDatabaseHelper extends DatabaseHelper {

	/*
	 * Returns a helper for an in-memory database
	 */
	public FixturesDatabaseHelper(Context ctx) {
		this(ctx, null, null, 1);
	}
	
	public FixturesDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		try {
			runScript(db, R.raw.fixtures);
		} catch (Exception e) {
			L.og(e);
		}
	}
}
