package com.zybnet.abc.model;

import java.sql.Time;

public class Slot extends Model {
	// These are 1-based
	public int day, ord, subject_id;
	public Time start, end;
	public String place, teacher, display_text;
	@Extern
	public String subject_name;
}
