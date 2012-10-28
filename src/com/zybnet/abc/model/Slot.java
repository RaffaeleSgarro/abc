package com.zybnet.abc.model;

import java.sql.Time;

public class Slot extends Model {
	
	public Slot() {
		super();
	}
	
	public Slot(Slot src) {
		super(src);
	}
	
	// These are 1-based
	public int day, ord;
	public Long subject_id, teacher_id;
	public Time start, end;
	public String place, display_text;
}
