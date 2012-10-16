package com.zybnet.abc.model;

import java.sql.Time;

public class Slot extends Model {
	// These are 1-based
	public int day, ord, subject_id;
	public Time start, end;
	public String place, teacher, display_text;
	@Extern
	public String subject_name;
	
	public static Slot getDefault() {
		Slot template = new Slot();
		template = new Slot();
		template.ord = 1;
		template.day = 1;
		template.start = new Time(8, 0, 0);
		template.end = new Time(9, 0, 0);
		template.place = "Nowhere";
		template.teacher = "Nobody";
		template.subject_name = "Nothing";
		template.display_text = "";
		return template;
	}
	
}
