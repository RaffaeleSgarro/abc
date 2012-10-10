CREATE TABLE "slot" (
	"_id" INT PRIMARY KEY,
	"subject_id" INT DEFAULT -1,
	"start" DATETIME NOT NULL DEFAULT '08:00',
	"end" DATETIME NOT NULL DEFAULT '09:00',
	"day" INT NOT NULL,
	"ord" INT NOT NULL,
	"where" TEXT DEFAULTS 'Nowhere',
	"teacher" TEXT DEFAULTS 'Nobody',
	
	FOREIGN KEY ("subject_id") REFERENCES "subject" ("_id") ON DELETE SET DEFAULT ON UPDATE CASCADE
);

CREATE TABLE "subject" (
	"_id" INT PRIMARY KEY,
	"name" TEXT UNIQUE,
	"name_short" TEXT,
	"default_teacher" TEXT,
	"default_where" TEXT
);

CREATE TABLE "homework" (
	"_id" INT PRIMARY KEY,
	"subject_id" INT NOT NULL,
	"description" TEXT,
	"due" DATE,
	
	FOREIGN KEY ("subject_id") REFERENCES "subject"("_id") ON DELETE CASCADE
);

CREATE TABLE "test" (
	"_id" INT PRIMARY KEY,
	"subject_id" INT NOT NULL,
	"description" TEXT,
	"score" TEXT,
	"date" DATE
);

