package com.zybnet.abc.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.zybnet.abc.R;

public class FixturesDatabase extends Database {

	public FixturesDatabase(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		if (db.isReadOnly())
			throw new IllegalArgumentException("Database is read-only");
		
		try {
			runScript(db, R.raw.fixtures);
		} catch (Exception e) {
			L.og(e);
		}
	}

}
