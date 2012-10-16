package com.zybnet.abc.model;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.zybnet.abc.utils.DatabaseHelper;
import com.zybnet.abc.utils.U;


public class Model {
	@Extern
	public static final int NONEXISTENT = -1;
	
	public long _id = NONEXISTENT;
	
	public void save(DatabaseHelper helper) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String table = getClass().getSimpleName().toLowerCase();
		ContentValues values = generateContentValues();
		
		//TODO does it really works?
		if (exists(db)) {
			String selection = "_id = ?";
			String[] args = new String[] {Long.toString(_id)};
			int affected = db.update(table, values, selection, args);
			if (affected != 1)
				throw new RuntimeException("Could not update model " + this);
		} else {
			values.remove("_id");
			long id = db.insert(table, null, values);
			if (id == -1)
				throw new RuntimeException("Could not insert model " + this);
		}
		
		Channel.publish(this);
	}
	
	private boolean exists(SQLiteDatabase db) {
		String table = getClass().getSimpleName().toLowerCase();
		long result = DatabaseUtils.longForQuery(db,
				String.format("SELECT COUNT(_id) FROM %s WHERE _id = ?", table),
				new String[]{Long.toString(_id)});
		return result == 1L;
	}
	
	private ContentValues generateContentValues() {
		ContentValues c = new ContentValues();
		Class<?> clazz = getClass();
		try {
			for (Field field: clazz.getFields()) {
				if (field.getAnnotation(Extern.class) != null)
					continue;
				
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
	
	public interface Subscriber {
		void onMessage(Model model);
	}
	
	public String dump() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		builder.append(" {\n");
		for (Field f: getClass().getFields()) {
			if (f.getAnnotation(Extern.class) != null)
				continue;
			try {
				builder.append(String.format("    %s: %s%n", f.getName(), f.get(this)));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		builder.append("}");
		return builder.toString();
	}
	
	public static class Channel {
		private Map<String, Set<Subscriber>> subscribers;
		
		private Channel() {
			subscribers = new HashMap<String, Set<Subscriber>>();
		}
		
		private static Channel instance = new Channel();
		
		public static void publish(Model model) {
			createTopicMaybe(model.getClass());
			Set<Subscriber> subs = instance.subscribers.get(getKey(model.getClass()));
			
			for (Subscriber sub: subs) {
				sub.onMessage(model);
			}
		}
		
		private static void createTopicMaybe(Class<? extends Model> topic) {
			String key = getKey(topic);
			
			if (instance.subscribers.containsKey(key))
				return;
			
			instance.subscribers.put(key, new HashSet<Subscriber>());
		}
		
		public static void subscribe(Class<? extends Model> topic, Subscriber sub) {
			createTopicMaybe(topic);
			instance.subscribers.get(getKey(topic)).add(sub);
		}
		
		public static void unsucribe(Class<? extends Model> topic, Subscriber sub) {
			instance.subscribers.get(getKey(topic)).remove(sub);
		}
		
		private static String getKey(Class<?> model) {
			return model.getClass().getCanonicalName();
		}
	}
}
