package com.zybnet.abc.model;

import java.util.Date;

public class Slot {
	// These are 1-based
	public int day, ord, _id, subject_id;
	public Date start, end;
	public String where, teacher, subject_name, display_text;
	
	public static Slot getDefault() {
		Slot template = new Slot();
		template = new Slot();
		template.ord = 1;
		template.day = 1;
		template.start = new Date();
		template.start.setHours(8);
		template.start.setMinutes(0);
		template.end = new Date();
		template.end.setHours(9);
		template.end.setMinutes(0);
		template.where = "Nowhere";
		template.teacher = "Nobody";
		template.subject_name = "Nothing";
		template.display_text = "";
		return template;
	}
	
}
