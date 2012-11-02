package com.zybnet.abc.model;

import java.sql.Date;

public class Grade extends Model {
	public Long subject_id;
	
	public String description;
	
	@NotNull
	public String score;
	
	@NotNull
	public Date date;
}
