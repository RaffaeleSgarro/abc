package com.zybnet.abc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.zybnet.abc.R;

public class DatabaseHelper extends SQLiteOpenHelper {

	private Context context;
	
	/*
	 * Returns the default database. For now, it's an in-memory db
	 * so data is not persistent.
	 * 
	 */
	public DatabaseHelper(Context ctx) {
		this(ctx, null, null, 1);
	}
	
	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {	
		try {
			runScript(db, R.raw.schema);
		} catch (IOException e) {
			L.og(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		throw new UnsupportedOperationException();
	}
	
	// IMPORTANT! Statements must be separated by and empty line
	// The last statement must be terminated by a newline, too
	// so likely the fill will end with a newline
	protected void runScript(SQLiteDatabase db, int resourceId) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				context.getResources().openRawResource(resourceId)));
		
		StringBuilder builder = new StringBuilder();
		String line = null;
		
		while ((line = in.readLine() )!= null) {
			if (line.trim().length() == 0 && builder.length() > 2 && builder.charAt(builder.length() - 2) == ';') {
				db.execSQL(builder.toString());
				builder = new StringBuilder();
				continue;
			}
			builder.append(line);
			builder.append('\n');
		}
		
	}

}
