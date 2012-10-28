package com.zybnet.abc.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.zybnet.abc.utils.DatabaseHelper;
import com.zybnet.abc.utils.L;
import com.zybnet.abc.utils.U;


public class Model {
	
	public Model() {}
	
	public Model(Model src) {
		Model dst = this;
		for (Field field : getPublicFields(true)) {
			try {
				field.set(dst, field.get(src));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public List<Field> getPublicFields(boolean includeExterns) {
		List<Field> fields = new ArrayList<Field>(Arrays.asList(getClass().getFields()));
		Iterator<Field> i = fields.iterator();
		while (i.hasNext()) {
			Field field = i.next();
			
			if (Modifier.isStatic(field.getModifiers()))
				i.remove();
			
		}
		return fields;
	}
	
	public List<Field> getPublicFields() {
		return getPublicFields(false);
	}
	
	public Long _id = null;
	
	public void save(DatabaseHelper helper) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String table = getClass().getSimpleName().toLowerCase();
		ContentValues values = generateContentValues();
		
		MessageBus.Action action;
		
		L.og(dump());
		
		//TODO does it really works?
		if (exists(db)) {
			action = MessageBus.Action.UPDATE;
			String selection = "_id = ?";
			String[] args = new String[] {Long.toString(_id)};
			int affected = db.update(table, values, selection, args);
			if (affected != 1)
				throw new RuntimeException("Could not update model " + this.dump());
		} else {
			action = MessageBus.Action.CREATE;
			values.remove("_id");
			long id = db.insert(table, null, values);
			
			if (id == -1)
				throw new RuntimeException("Could not insert model " + this.dump());
			
			this._id = Long.valueOf(id);
		}
		
		MessageBus.publish(this, action);
	}
	
	public boolean exists(SQLiteDatabase db) {
		String table = getClass().getSimpleName().toLowerCase();
		long result = DatabaseUtils.longForQuery(db,
				String.format("SELECT COUNT(_id) FROM %s WHERE _id = ?", table),
				new String[]{ (_id == null) ? "NULL" : _id.toString() });
		return result == 1L;
	}
	
	private ContentValues generateContentValues() {
		ContentValues c = new ContentValues();
		try {
			for (Field field: getPublicFields()) {
				
				Object value = field.get(this);
				
				if (value == null)
					continue;
				
				Class<?> type = field.getType();
				
				if (type.equals(java.sql.Date.class)) {
					java.sql.Date date = (java.sql.Date) field.get(this);
					DateFormat df = new SimpleDateFormat(U.SQL_DATE_FORMAT);
					c.put(field.getName(), df.format(date));
				} else if (type.equals(java.sql.Time.class)) {
					java.sql.Time time = (java.sql.Time) field.get(this);
					DateFormat df = new SimpleDateFormat(U.SQL_TIME_FORMAT);
					c.put(field.getName(), df.format(time));
				} else {
					c.put(field.getName(), value.toString());
				}
			}
			return c;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public String dump() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		builder.append(" {\n");
		for (Field f: getPublicFields(true)) {
			try {
				builder.append(String.format("    %s: %s%n", f.getName(), f.get(this)));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		builder.append("}");
		return builder.toString();
	}
	
	public void delete(DatabaseHelper dh) {
		SQLiteDatabase db = dh.getWritableDatabase();
		
		if (!exists(db))
			return;
		
		String table = getClass().getSimpleName().toLowerCase();
		db.delete(table, "_id = ?", new String[]{Long.toString(_id)});
		
		MessageBus.publish(this, MessageBus.Action.DELETE);
	}
}
