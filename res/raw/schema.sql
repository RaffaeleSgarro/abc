CREATE TABLE "slot" (
	"_id" INTEGER PRIMARY KEY NOT NULL,
	"subject_id" INT DEFAULT -1,
	"start" TEXT,
	"end" TEXT,
	"day" INTEGER NOT NULL,
	"ord" INTEGER NOT NULL,
	"display_text" TEXT,
	"place" TEXT,
	"teacher_id" INT DEFAULT -1,
	
	FOREIGN KEY ("subject_id") REFERENCES "subject" ("_id") ON DELETE SET DEFAULT ON UPDATE CASCADE,
	FOREIGN KEY ("teacher_id") REFERENCES "teacher" ("_id") ON DELETE SET DEFAULT ON UPDATE CASCADE,
	UNIQUE ("day", "ord")
);

CREATE TABLE "subject" (
	"_id" INTEGER PRIMARY KEY NOT NULL,
	"name" TEXT UNIQUE,
	"name_short" TEXT,
	"default_teacher_id" INT DEFAULT -1,
	"default_place" TEXT,
	
	FOREIGN KEY ("default_teacher_id") REFERENCES "teacher" ("_id") ON DELETE SET DEFAULT ON UPDATE CASCADE
);

CREATE TABLE "homework" (
	"_id" INTEGER PRIMARY KEY NOT NULL,
	"subject_id" INTEGER NOT NULL,
	"description" TEXT,
	"due" TEXT,
	
	FOREIGN KEY ("subject_id") REFERENCES "subject"("_id")
);

CREATE TABLE "grade" (
	"_id" INTEGER PRIMARY KEY NOT NULL,
	"subject_id" INTEGER NOT NULL,
	"description" TEXT,
	"score" TEXT,
	"date" TEXT,
	
	FOREIGN KEY ("subject_id") REFERENCES "subject"("_id")
);

CREATE TABLE "teacher" (
	"_id" INTEGER PRIMARY KEY NOT NULL,
	"name" TEXT,
	"email" TEXT,
	"phone" TEXT,
	"notes" TEXT
);