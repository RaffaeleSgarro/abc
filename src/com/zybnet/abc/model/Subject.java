package com.zybnet.abc.model;

public class Subject extends Model {
	@NotNull
	public String name;
	
	@NotNull
	public String name_short;
	
	public String default_place;
	
	public Long default_teacher_id;
}
