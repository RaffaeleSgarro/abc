package com.zybnet.abc.model;

import java.sql.Date;

public class Homework extends Model {
	public Long subject_id;
	
	@NotNull
	public String description;
	
	@NotNull
	public Date due;
}
