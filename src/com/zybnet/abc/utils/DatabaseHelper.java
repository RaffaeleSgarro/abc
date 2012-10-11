package com.zybnet.abc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zybnet.abc.R;
import com.zybnet.abc.model.Slot;

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
	
	/*
	 * Returns the Slot from the database. If no slot exists
	 * returns Slot.template, but still this is not saved
	 * in the database
	 */
	public Slot getSlot(int day, int ord) {
		String sql = "" +
				"SELECT slot.* , " +
				"  subject.name AS subject_name, subject._id AS subject_id " +
				"FROM slot LEFT JOIN subject ON slot.subject_id = subject._id " +
				"WHERE slot.day = ? AND slot.ord = ? ";
		String[] args = {Integer.toString(day), Integer.toString(ord)};
		Cursor c = getReadableDatabase().rawQuery(sql, args);
		
		if (c.getCount() != 1) {
			Slot slot = Slot.getDefault();
			slot.day = day;
			slot.ord = ord;
			return slot;
		}
		
		c.moveToFirst();
		Slot slot = new Slot();
		slot.day = _i(c, "day");
		slot.ord = _i(c, "ord");
		slot.teacher = _s(c, "teacher");
		slot.where = _s(c, "where");
		slot.start = _time(c, "start");
		slot.end = _time(c, "end");
		slot.subject_name = _s(c, "subject_name");
		slot.subject_id = _i(c, "subject_id");
		c.close();
		return slot;
	}
	
	public String _s(Cursor c, String column) {
		return c.getString(c.getColumnIndex(column));
	}
	
	public int _i(Cursor c, String column) {
		return c.getInt(c.getColumnIndex(column));
	}

	public Date _date(Cursor c, String column) {
		// TODO
		L.og("Date as string: " + c.getString(c.getColumnIndex(column)));
		L.og("Date as int: " + c.getInt(c.getColumnIndex(column)));
		return new Date(c.getInt(c.getColumnIndex(column)));
	}
	
	public Date _time(Cursor c, String column) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			return sdf.parse(c.getString(c.getColumnIndex(column)));
		} catch (ParseException e) {
			Log.e(L.TAG, "Cannot parse date", e);
			return new Date();
		}
	}
}
