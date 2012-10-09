CREATE TABLE subject (
	_id INT PRIMARY KEY,
	name TEXT UNIQUE,
	name_short TEXT,
	default_teacher TEXT,
	default_where TEXT
);

CREATE TABLE slot (
	_id INT PRIMARY KEY,
	subject_id INT DEFAULT -1,
	start DATETIME,
	end DATETIME,
	row INT NOT NULL,
	column INT NOT NULL,
	where TEXT DEFAULTS 'Nowhere',
	teacher TEXT DEFAULTS 'Nobody',
	
	FOREIGN KEY (subject_id) REFERENCES subject(_id) ON DELETE SET DEFAULT ON UPDATE CASCADE
);

CREATE TABLE homework (
	_id INT PRIMARY KEY,
	subject_id INT NOT NULL,
	description TEXT,
	due DATE,
	
	FOREIGN KEY (subject_id) REFERENCES subject(_id) ON DELETE CASCADE
);

CREATE TABLE test (
	_id INT PRIMARY KEY,
	subject_id INT NOT NULL,
	description TEXT,
	score TEXT,
	date DATE
);
