package com.zybnet.abc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.zybnet.abc.R;

public class Database extends SQLiteOpenHelper {

	private Context context;
	
	public Database(Context context, String name, CursorFactory factory, int version) {
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
	
	protected void runScript(SQLiteDatabase db, int resourceId) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				context.getResources().openRawResource(R.raw.schema)));
		
		StringBuilder builder = new StringBuilder();
		String line = null;
		
		while ((line = in.readLine() )!= null) {
			builder.append(line);
			builder.append('\n');
		}
		
		db.execSQL(builder.toString());
	}

}
